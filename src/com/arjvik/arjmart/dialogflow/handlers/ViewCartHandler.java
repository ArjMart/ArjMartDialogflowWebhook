package com.arjvik.arjmart.dialogflow.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.GenericType;

import com.arjvik.arjmart.api.item.Item;
import com.arjvik.arjmart.api.item.ItemAttribute;
import com.arjvik.arjmart.api.item.ItemPrice;
import com.arjvik.arjmart.api.item.ItemResourceClient;
import com.arjvik.arjmart.api.order.Order;
import com.arjvik.arjmart.api.order.OrderLine;
import com.arjvik.arjmart.api.order.OrderResourceClient;
import com.arjvik.arjmart.api.order.OrderTotal;
import com.arjvik.arjmart.dialogflow.AbstractHandler;
import com.arjvik.arjmart.dialogflow.AlephStyles;
import com.arjvik.arjmart.dialogflow.Contexts;
import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.apiclient.ResourceClientProvider;
import com.arjvik.arjmart.dialogflow.entities.Context;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

import net.andreinc.aleph.AlephFormatter;

public class ViewCartHandler extends AbstractHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		OrderResourceClient client = ResourceClientProvider.get(OrderResourceClient.class);
		Order cart = client.getOrAddCart().readEntity(Order.class);
		List<OrderLine> orderLines = client.getOrderLines(cart.getOrderID()).readEntity(new GenericType<List<OrderLine>>(){});
		if(orderLines.isEmpty()) {
			String response = getResponseTemplate(("ViewCartEmpty"));
			return new WebhookResponse(response);
		}
		StringBuilder orderLinesResponse = new StringBuilder();
		ItemResourceClient itemClient = ResourceClientProvider.get(ItemResourceClient.class);
		for (OrderLine orderLine : orderLines) {
			Item item = itemClient.getItem(orderLine.getSKU()).readEntity(Item.class);
			ItemAttribute itemAttribute = itemClient.getAttribute(orderLine.getSKU(), orderLine.getItemAttributeID()).readEntity(ItemAttribute.class);
			ItemPrice price = itemClient.getPrice(orderLine.getSKU(), orderLine.getItemAttributeID()).readEntity(ItemPrice.class);
			String orderLineResponse = AlephFormatter.str(getResponseTemplate("ViewCartLines"))
													 .style(AlephStyles.ArjMartAlephStyle)
													 .args("item", item, "attribute", itemAttribute, "quantity", orderLine.getQuantity(), "unitPrice", price.getPrice())
													 .fmt();
			orderLinesResponse.append(orderLineResponse);
		}
		OrderTotal total = client.getOrderTotal(cart.getOrderID()).readEntity(OrderTotal.class);
		String response = AlephFormatter.str(getResponseTemplate("ViewCart"))
										.style(AlephStyles.ArjMartAlephStyle)
										.args("uniqueCount", orderLines.size(), "lines", orderLinesResponse.toString(), "total", total.getTotal())
										.fmt();
		List<Context> outputContexts = new ArrayList<>();
		Context c = new Context(contextName(Contexts.AWAITING_CHECKOUT, request), Contexts.AWAITING_CHECKOUT_LIFESPAN, Collections.emptyMap());
		outputContexts.add(c);
		return new WebhookResponse(response, outputContexts);
	}

}
