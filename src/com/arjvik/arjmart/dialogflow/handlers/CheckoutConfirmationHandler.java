package com.arjvik.arjmart.dialogflow.handlers;

import javax.ws.rs.core.Response;

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

public class CheckoutConfirmationHandler extends AbstractHandler implements IntentFulfillmentHandler {

	@Override
	public WebhookResponse handle(WebhookRequest request) {
		OrderResourceClient client = ResourceClientProvider.get(OrderResourceClient.class);
		Order cart = client.getOrAddCart().readEntity(Order.class);
		OrderTotal orderTotal = client.getOrderTotal(cart.getOrderID()).readEntity(OrderTotal.class);
		Response checkoutResponse = client.checkout(cart.getOrderID());
		switch(checkoutResponse.getStatus()) {
		case 200:
		case 204:
			UserResourceClient userClient = ResourceClientProvider.get(UserResourceClient.class);
			User user = userClient.getUser(cart.getUserID()).readEntity(User.class);
			String creditCardNumber = user.getCreditCardNumber();
			String truncatedCreditCard = creditCardNumber.substring(Math.max(0, creditCardNumber.length() - 4));
			String response = AlephFormatter.str(getResponseTemplate("CheckoutConfirmation"))
											.style(AlephStyles.ArjMartAlephStyle)
											.args("total", orderTotal.getTotal(), "creditCard", truncatedCreditCard)
											.fmt();
			return new WebhookResponse(response);
		case 402:
			UserResourceClient errorUserClient = ResourceClientProvider.get(UserResourceClient.class);
			User errorUser = errorUserClient.getUser(cart.getUserID()).readEntity(User.class);
			String errorCreditCardNumber = errorUser.getCreditCardNumber();
			String errorTruncatedCreditCard = errorCreditCardNumber.substring(Math.max(0, errorCreditCardNumber.length() - 4));
			String errorResponse = AlephFormatter.str(getResponseTemplate("CheckoutConfirmationPaymentError"))
											.style(AlephStyles.ArjMartAlephStyle)
											.args("total", orderTotal.getTotal(), "creditCard", errorTruncatedCreditCard)
											.fmt();
			return new WebhookResponse(errorResponse);
		case 409:
			String invalidStateResponse = getResponseTemplate("CheckoutConfirmationInvalidOrderStatus");
			return new WebhookResponse(invalidStateResponse);
		default:
			throw new RuntimeException("Bad response code: "+checkoutResponse.getStatus());
		}
	}
	
}