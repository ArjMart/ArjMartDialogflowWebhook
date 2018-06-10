package com.arjvik.arjmart.dialogflow.entities;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {
	private String responseId;
	private String session;
	private QueryResult queryResult;
	private JsonNode originalDetectIntentRequest;
}
