package com.arjvik.arjmart.dialogflow;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WebhookResource {

	@POST
	public WebhookResponse fulfill(WebhookRequest request) {
		return instantiate(getFulfillmentHandler(request.getQueryResult().getAction())).handle(request);
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends IntentFulfillmentHandler> getFulfillmentHandler(String action) {
		try {
			return (Class<? extends IntentFulfillmentHandler>)
						Class.forName("com.arjvik.arjmart.dialogflow.handlers."+action+"Handler");
		} catch (ClassNotFoundException e) {
			throw new HandlerNotFoundException(action, e);
		}
	}
	
	private <T> T instantiate(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
