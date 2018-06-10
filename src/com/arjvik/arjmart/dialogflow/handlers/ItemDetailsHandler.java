package com.arjvik.arjmart.dialogflow.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;

import com.arjvik.arjmart.api.item.Item;
import com.arjvik.arjmart.api.item.ItemAttribute;
import com.arjvik.arjmart.api.item.ItemPrice;
import com.arjvik.arjmart.api.item.ItemResourceClient;
import com.arjvik.arjmart.dialogflow.AbstractHandler;
import com.arjvik.arjmart.dialogflow.AlephStyles;
import com.arjvik.arjmart.dialogflow.Contexts;
import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.apiclient.ResourceClientProvider;
import com.arjvik.arjmart.dialogflow.entities.Context;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

import net.andreinc.aleph.AlephFormatter;

public class ItemDetailsHandler extends AbstractHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		Map<String, Object> parameters = request.getQueryResult().getParameters();
		int itemID = convertToInt(parameters.get("itemNumber"));
		String key = "item"+itemID+"sku";
		if(parameters.containsKey(key) && parameters.get(key) != null && !parameters.get(key).equals("")) {
			ItemResourceClient client = ResourceClientProvider.get(ItemResourceClient.class);
			Item item = client.getItem(convertToInt(parameters.get(key))).readEntity(Item.class);
			List<ItemAttribute> attributes = client.getAllAttribute(item.getSKU()).readEntity(new GenericType<List<ItemAttribute>>(){});
			StringBuilder attributeResponses = new StringBuilder();
			//for (ItemAttribute attribute : attributes) {
			for (int i = 0; i < attributes.size(); i++) {
				ItemAttribute attribute = attributes.get(i);
				ItemPrice price = client.getPrice(item.getSKU(), attribute.getID()).readEntity(ItemPrice.class);
				String attributeResponse = AlephFormatter.str(getResponseTemplate("ItemDetailsAttribute"))
														 .style(AlephStyles.ArjMartAlephStyle)
														 .args("index", i+1, "attribute", attribute, "price", price.getPrice())
														 .fmt();
				attributeResponses.append(attributeResponse);
			}
			String response = AlephFormatter.str(getResponseTemplate("ItemDetails"))
											.style(AlephStyles.ArjMartAlephStyle)
											.args("item", item, "attributeCount", attributes.size(), "attributes", attributeResponses.toString())
											.fmt();
			List<Context> outputContexts = new ArrayList<>();
			Context c = new Context(contextName(Contexts.ITEM_DETAILS_SKU, request), Contexts.ITEM_DETAILS_SKU_LIFESPAN, mapOf("sku", item.getSKU()));
			outputContexts.add(c);
			return new WebhookResponse(response, outputContexts);
		} else {
			String response = getResponseTemplate("ItemDetailsNotEnoughItems");
			return new WebhookResponse(response);
		}
	}

}
