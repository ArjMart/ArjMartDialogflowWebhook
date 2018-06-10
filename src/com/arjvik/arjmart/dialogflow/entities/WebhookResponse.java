package com.arjvik.arjmart.dialogflow.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class WebhookResponse {
	@NonNull private String fulfillmentText;
	private List<JsonNode> fulfillmentMessages;
	private String source;
	private JsonNode payload;
	@NonNull private List<Context> outputContexts = new ArrayList<>();
	private JsonNode followupEventInput;

	public WebhookResponse(@NonNull final String fulfillmentText, List<Context> outputContexts) {
		this(fulfillmentText);
		this.outputContexts = outputContexts;
	}

}
