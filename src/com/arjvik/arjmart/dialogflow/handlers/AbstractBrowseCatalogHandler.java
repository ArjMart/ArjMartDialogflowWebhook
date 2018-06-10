package com.arjvik.arjmart.dialogflow.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;

import com.arjvik.arjmart.api.item.Item;
import com.arjvik.arjmart.api.item.ItemCount;
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

public abstract class AbstractBrowseCatalogHandler extends AbstractHandler implements IntentFulfillmentHandler {

	@Override
	public abstract WebhookResponse handle(WebhookRequest request);
	
	protected WebhookResponse browseCatalogWithOffset(WebhookRequest request, int offset, String templateName) {
		ItemResourceClient client = ResourceClientProvider.get(ItemResourceClient.class);
		List<Item> items = client.getAll(offset, 3, null)
								 .readEntity(new GenericType<List<Item>>(){});
		String response;
		switch(items.size()) {
		case 3:	
			response = AlephFormatter.str(getResponseTemplate(templateName + "3"))
									 .style(AlephStyles.ArjMartAlephStyle)
									 .args("item1",items.get(0),"item2",items.get(1),"item3",items.get(2))
									 .fmt();
			break;
		case 2:
			response = AlephFormatter.str(getResponseTemplate(templateName + "2"))
			 						 .style(AlephStyles.ArjMartAlephStyle)
			 						 .args("item1",items.get(0),"item2",items.get(1))
			 						 .fmt();
			break;
		case 1:
			response = AlephFormatter.str(getResponseTemplate(templateName + "1"))
				 					 .style(AlephStyles.ArjMartAlephStyle)
				 					 .args("item1",items.get(0))
				 					 .fmt();
			break;
		case 0:
			response = AlephFormatter.str(getResponseTemplate(templateName + "0"))
			 						 .style(AlephStyles.ArjMartAlephStyle)
			 						 .fmt();
			break;
		default:
			throw new RuntimeException();
		}
		List<Context> outputContexts = new ArrayList<>();
		//Browse Catalog Previous offset context
		if(offset >= 3) {
			Context browseCatalogPrevious = new Context(contextName(Contexts.BROWSE_CATALOG_PREVIOUS, request), Contexts.BROWSE_CATALOG_PREVIOUS_LIFESPAN, mapOf("offset", offset - 3));
			outputContexts.add(browseCatalogPrevious);
		}
		//Browse Catalog Next offset context
		ItemCount count = client.getItemCount(null).readEntity(ItemCount.class);
		if(offset + 3 < count.getCount()) {
			Context browseCatalogNext = new Context(contextName(Contexts.BROWSE_CATALOG_NEXT, request), Contexts.BROWSE_CATALOG_NEXT_LIFESPAN, mapOf("offset", offset + 3));
			outputContexts.add(browseCatalogNext);
		}
		//List item indexes offset context
		{
			Map<String, Object> contextParams = new HashMap<>();
			for (int i = 0; i < items.size(); i++) {
				contextParams.put("item"+(i+1)+"sku", items.get(i).getSKU());
			}
			for (int i = items.size(); i < 3; i++) {
				contextParams.put("item"+(i+1)+"sku", null);
			}
			Context itemListIndexes = new Context(contextName(Contexts.ITEM_LIST_INDEXES, request), Contexts.ITEM_LIST_INDEXES_LIFESPAN, contextParams);
			outputContexts.add(itemListIndexes);
		}
		return new WebhookResponse(response, outputContexts);
	}

}
