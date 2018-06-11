package com.arjvik.arjmart.dialogflow.handlers;

import com.arjvik.arjmart.api.order.Order;
import com.arjvik.arjmart.api.order.OrderResourceClient;
import com.arjvik.arjmart.api.order.OrderTotal;
import com.arjvik.arjmart.api.user.User;
import com.arjvik.arjmart.api.user.UserResourceClient;
import com.arjvik.arjmart.dialogflow.AbstractHandler;
import com.arjvik.arjmart.dialogflow.AlephStyles;
import com.arjvik.arjmart.dialogflow.IntentFulfillmentHandler;
import com.arjvik.arjmart.dialogflow.apiclient.ResourceClientProvider;
import com.arjvik.arjmart.dialogflow.entities.WebhookRequest;
import com.arjvik.arjmart.dialogflow.entities.WebhookResponse;

import net.andreinc.aleph.AlephFormatter;

public class CheckoutHandler extends AbstractHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		OrderResourceClient client = ResourceClientProvider.get(OrderResourceClient.class);
		Order cart = client.getOrAddCart().readEntity(Order.class);
		OrderTotal orderTotal = client.getOrderTotal(cart.getOrderID()).readEntity(OrderTotal.class);
		UserResourceClient userClient = ResourceClientProvider.get(UserResourceClient.class);
		User user = userClient.getUser(cart.getUserID()).readEntity(User.class);
		String creditCardNumber = user.getCreditCardNumber();
		String truncatedCreditCard = creditCardNumber.substring(Math.max(0, creditCardNumber.length() - 4));
		String response = AlephFormatter.str(getResponseTemplate("Checkout"))
										.style(AlephStyles.ArjMartAlephStyle)
										.args("total", orderTotal.getTotal(), "creditCard", truncatedCreditCard)
										.fmt();
		return new WebhookResponse(response);
	}
	
}
