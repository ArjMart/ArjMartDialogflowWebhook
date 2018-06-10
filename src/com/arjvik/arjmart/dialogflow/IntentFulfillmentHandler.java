package com.arjvik.arjmart.dialogflow;

import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

public interface IntentFulfillmentHandler {

	public WebhookResponse handle(WebhookRequest request);
	
}
