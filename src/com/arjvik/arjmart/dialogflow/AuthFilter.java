package com.arjvik.arjmart.dialogflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
	
	private final String SECRET;
	
	@Inject
	public AuthFilter() {
		try {
			Properties properties = new Properties();
			InputStream in = AuthFilter.class.getClassLoader().getResourceAsStream("authorization-secrets.properties");
			properties.load(in);
			SECRET = properties.getProperty("APIAuthorizationSecret");
		} catch (IOException | NullPointerException e) {
			throw new RuntimeException("Could not read authorization secrets, check if authorization-secrets.properties exists in classpath", e);
		}
	}

	@Override
	public void filter(ContainerRequestContext req) throws IOException {
		if(!SECRET.equals(req.getHeaderString("X-API-Authorization-Secret")))
			req.abortWith(Response.status(Status.UNAUTHORIZED)
								  .header("WWW-Authenticate","Header X-API-Authorization-Secret")
								  .build());
	}

}
