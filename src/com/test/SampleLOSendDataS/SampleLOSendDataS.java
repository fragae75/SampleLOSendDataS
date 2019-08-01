package com.test.SampleLOSendDataS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.test.SampleLOSendDataS.DeviceData;
import com.test.SampleLOSendDataS.MyKey;

public class SampleLOSendDataS {

	/**
	 * 
	 * @param caCertStream
	 * @return
	 * @throws MqttSecurityException
	 * @throws KeyManagementException
	 * 
	 * Get a ThrustManager array, create a TLS SSLContext with this ThrustManager and return a SSL Socket factory
	 */
    private static SSLSocketFactory getSSLSocketFactory (InputStream caCertStream) throws MqttSecurityException, KeyManagementException {
        try{
            SSLContext ctx;
            SSLSocketFactory sslSockFactory;

            TrustManager[] tm = SSLUtils.getTrustManagers(caCertStream);


            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            sslSockFactory = ctx.getSocketFactory();

            return sslSockFactory;

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new MqttSecurityException(e);
        }
    }

	
	public static void main(String[] args) throws FileNotFoundException, KeyManagementException {
        Random rand = new Random();
        String API_KEY = MyKey.key; // <-- REPLACE by your API key !
        
        String SERVER = "ssl://liveobjects.orange-business.com:8883";
        String DEVICE_URN = "urn:lo:nsid:sensor:SampleLOSendDataS";

        // *** data to push ***
        DeviceData data = new DeviceData();
        // streamId
        data.s = "SampleLOSendDataS";
        // value: JSON object...
        data.v = new HashMap<String, Object>();
        // Hygrometrie : 0 - 100
    	data.v.put("hygrometry", rand.nextInt(100));
    	// T° from -20 to 120
    	data.v.put("temperature", rand.nextInt(140) - 20);
		// Rev/min : 0 - 9999
    	data.v.put("revmin", rand.nextInt(9999));
        // location (lat/lon)
        data.loc = new Double[] { 45.759723, 4.84223 };
        // model : define the data model (hygrometry/temperature/revmin) that will be indexed into Live Objects (this model is the same as the Android app sample)
        data.m = "demo";
        // tags
        data.t = Arrays.asList("SampleLO");
        // encoding to JSON
        String CONTENT = new Gson().toJson(data);

        try {
            MqttClient sampleClient = new MqttClient(SERVER, DEVICE_URN, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("json+device"); // selecting mode "Device"
            connOpts.setPassword(API_KEY.toCharArray()); // passing API key value as password
            connOpts.setCleanSession(true);
            
            // Get the certificate from the program resource and build a SSLSocketFactory
            InputStream is = SampleLOSendDataS.class.getResourceAsStream("/ca_cert_LO.txt");
            
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory(is);
            connOpts.setSocketFactory(sslSocketFactory);


            // Connection
            System.out.println("Publish - Connecting to broker: " + SERVER);
            sampleClient.connect(connOpts);
            System.out.println("Publish - Connected");

            // Publish data
            System.out.println("Publishing message: " + CONTENT);
            MqttMessage message = new MqttMessage(CONTENT.getBytes());
            message.setQos(0);
            sampleClient.publish("dev/data", message);
            System.out.println("Message published");

            // Disconnection
            sampleClient.disconnect();
            System.out.println("Disconnected");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        

        System.out.println("End of the program ");
        
	}

}
