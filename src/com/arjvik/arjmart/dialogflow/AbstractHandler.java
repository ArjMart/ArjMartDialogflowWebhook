package com.arjvik.arjmart.dialogflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

public abstract class AbstractHandler implements IntentFulfillmentHandler {

	private static Properties responseTemplates = getResponseTemplates();
	
	@Override
	public abstract WebhookResponse handle(WebhookRequest request);

	protected String contextName(String name, WebhookRequest request) {
		return request.getSession() + "/contexts/" + name;
	}

	private static Properties getResponseTemplates() {
		try {
			InputStream in = AbstractHandler.class.getClassLoader().getResourceAsStream("response-templates.properties");
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		} catch (IOException e) {
			throw new RuntimeException("Could not read response templates, check if response-templates.properties exists in classpath");
		}
	}
	
	protected String getResponseTemplate(String name) {
		return responseTemplates.getProperty(name);
	}

	protected int convertToInt(Object o) {
		if(o instanceof Double)
			return (int) ((double) o);
		else if(o instanceof Integer)
			return (int) o;
		else if(o instanceof String)
			return Integer.parseInt((String) o);
		else
			throw new NumberFormatException("Object \""+o+"\" could not be converted to an integer");
	}
	
	@SuppressWarnings("unchecked")
	protected <K, V> Map<K, V> mapOf(Object... args) {
		Map<K, V> map = new LinkedHashMap<>();
		for (int i = 0; i < args.length; i+=2) {
			map.put((K) args[i], (V) args[i+1]);
		}
		return map;
	}
	
}
