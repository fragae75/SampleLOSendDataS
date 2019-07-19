# SampleLOSendDataS

Sample application for Datavenue Live Objects <a>https://liveobjects.orange-business.com</a>

It is a simple sample that sends a payload to Live Objects as a MQTT device ('json+device' user name) over a SSL connection (=> MQTTS).
It generates a standard Live Objects payload as follow :<br>
 
	{
		"streamId":"SampleLOSendDataS",
		"timestamp":"2018-04-07T15:52:31.150Z",
		"location":{"lat":45.759723,"lon":4.84223},
		"model":"demo",
		"value":
		{
			"hygrometry":xx,
			"revmin":xxxx,
			"temperature":xx
		},
		"tags":["SampleLO"],
		"metadata":{"source":"urn:lo:nsid:sensor:SampleLOSendDataS","connector":"mqtt"}
	}

This sample generates the same kind of payload as the Android app available at : 
<a>https://play.google.com/store/apps/details?id=com.orange.lo.assetdemo</a>
<br>


<h1> Installation notes </h1>

1) Create an account on Live Objects. You can get a free account (10 MQTT devices for 1 year) at : <a>https://liveobjects.orange-business.com/#/request_account</a> <br>
Don't check "Lora" otherwise the account will not be instantly created.

2) Generate your Device API key : menu Configuration/API Keys (<a>https://liveobjects.orange-business.com/#/config/apikeys</a>) click on "Add", select 'MQTT device' profile rights

3) Create a MyKey class with the generated API key: 


	package com.test.SampleLOSendData; 
	
	public final class MyKey { 
		static String key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; 
	}


4) You will find into the repository 4 jar files into the /lib. Add them as "external JARs" into you IDE (eg Eclipse).


