package com.arjvik.arjmart.dialogflow.entities;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResult {
	private String queryText;
	private Map<String, Object> parameters;
	private boolean allRequiredParamsPresent;
	private String fulfillmentText;
	private List<JsonNode> fulfillmentMessages;
	private List<Context> outputContexts;
	private Intent intent;
	private double intentDetectionConfidence;
	private double speechDetectionConfidence;
	private JsonNode diagnosticInfo;
	private String languageCode;
	private String action;
}
