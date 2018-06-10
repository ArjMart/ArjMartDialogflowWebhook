package com.arjvik.arjmart.dialogflow.handlers;

import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

public class SearchCatalogPreviousHandler extends AbstractSearchCatalogHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		int offset = convertToInt(request.getQueryResult().getParameters().get("offset"));
		String query = request.getQueryResult().getParameters().get("query").toString();
		return searchCatalogWithOffset(request, query, offset, "SearchCatalogPrevious");
	}

}
