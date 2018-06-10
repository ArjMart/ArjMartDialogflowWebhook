package com.arjvik.arjmart.dialogflow.handlers;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.arjvik.arjmart.api.item.Item;
import com.arjvik.arjmart.api.item.ItemAttribute;
import com.arjvik.arjmart.api.item.ItemResourceClient;
import com.arjvik.arjmart.api.order.Order;
import com.arjvik.arjmart.api.order.OrderLine;
import com.arjvik.arjmart.api.order.OrderResourceClient;
import com.arjvik.arjmart.api.order.Quantity;
import com.arjvik.arjmart.dialogflow.AbstractHandler;
import com.arjvik.arjmart.dialogflow.AlephStyles;
import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.apiclient.ResourceClientProvider;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

import net.andreinc.aleph.AlephFormatter;

public class AddToCartHandler extends AbstractHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		Map<String, Object> parameters = request.getQueryResult().getParameters();
		int sku = convertToInt(parameters.get("sku"));
		int itemAttributeID = convertToInt(parameters.get("attribute"));
		int quantity = convertToInt(parameters.get("quantity"));
		ItemResourceClient itemClient = ResourceClientProvider.get(ItemResourceClient.class);
		Item item = itemClient.getItem(sku).readEntity(Item.class);
		Response itemAttributeResponse = itemClient.getAttribute(sku, itemAttributeID);
		if(itemAttributeResponse.getStatus() != 200) {
			String response = getResponseTemplate("AddToCartNotEnoughOptions");
			return new WebhookResponse(response);
		}
		ItemAttribute itemAttribute = itemAttributeResponse.readEntity(ItemAttribute.class);
		OrderResourceClient client = ResourceClientProvider.get(OrderResourceClient.class);
		Order cart = client.getOrAddCart().readEntity(Order.class);
		OrderLine orderLine = new OrderLine();
		orderLine.setSKU(sku);
		orderLine.setItemAttributeID(itemAttributeID);
		orderLine.setQuantity(quantity);
		Response orderLineResponse = client.addOrderLine(orderLine, cart.getOrderID());
		switch(orderLineResponse.getStatus()) {
		case 200:
		case 201:
			orderLine = orderLineResponse.readEntity(OrderLine.class);
			String response = AlephFormatter.str(getResponseTemplate("AddToCart"))
											.style(AlephStyles.ArjMartAlephStyle)
											.args("quantity", quantity, "item", item, "attribute", itemAttribute)
											.fmt();
			return new WebhookResponse(response);
		case 409:
			List<OrderLine> orderLines = client.getOrderLines(cart.getOrderID()).readEntity(new GenericType<List<OrderLine>>(){});
			OrderLine conflictingOrderLine = null;
			conflictSearch: for (OrderLine existingOrderLine : orderLines) {
				if(existingOrderLine.getSKU()==sku && existingOrderLine.getItemAttributeID()==itemAttributeID){
					conflictingOrderLine = existingOrderLine;
					break conflictSearch;
				}
			}
			Quantity quantityTotal = new Quantity(orderLine.getQuantity() + conflictingOrderLine.getQuantity());
			orderLine = client.editOrderLine(quantityTotal, cart.getOrderID(), conflictingOrderLine.getOrderLineID()).readEntity(OrderLine.class);
			String alreadyExistsResponse = AlephFormatter.str(getResponseTemplate("AddToCartAlreadyExists"))
														 .style(AlephStyles.ArjMartAlephStyle)
														 .args("quantityNew", quantity, "quantityTotal", quantityTotal.getQuantity(), "item", item, "attribute", itemAttribute)
														 .fmt();
			return new WebhookResponse(alreadyExistsResponse);
		default:
			throw new RuntimeException();
		}
	}

}
