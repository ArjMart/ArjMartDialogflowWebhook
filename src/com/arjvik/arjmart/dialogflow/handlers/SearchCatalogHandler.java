package com.arjvik.arjmart.dialogflow.handlers;

import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

public class SearchCatalogHandler extends AbstractSearchCatalogHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		String query = request.getQueryResult().getParameters().get("query").toString();
		return searchCatalogWithOffset(request, query, 0, "SearchCatalog");
	}

}
