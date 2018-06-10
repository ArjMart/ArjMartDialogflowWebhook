package com.arjvik.arjmart.dialogflow.handlers;

import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

public class BrowseCatalogNextHandler extends AbstractBrowseCatalogHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		int offset = convertToInt(request.getQueryResult().getParameters().get("offset"));
		return browseCatalogWithOffset(request, offset, "BrowseCatalogNext");
	}

}
