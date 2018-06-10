package com.arjvik.arjmart.dialogflow.handlers;

import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

public class BrowseCatalogHandler extends AbstractBrowseCatalogHandler implements IntentFulfillmentHandler {
	
	@Override
	public WebhookResponse handle(WebhookRequest request) {
		return browseCatalogWithOffset(request, 0, "BrowseCatalog");
	}

}