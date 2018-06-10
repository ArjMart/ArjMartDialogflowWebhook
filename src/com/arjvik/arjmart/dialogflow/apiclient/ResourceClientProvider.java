package com.arjvik.arjmart.dialogflow.apiclient;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

public class ResourceClientProvider {
	public static <T> T get(Class<T> clazz) {
		return WebResourceFactory.newResource(clazz, WebTargetProvider.getWebTarget());
	}
}
