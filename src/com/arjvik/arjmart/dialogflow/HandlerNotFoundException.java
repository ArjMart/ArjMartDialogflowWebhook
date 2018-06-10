package com.arjvik.arjmart.dialogflow;

import javax.ws.rs.InternalServerErrorException;

public class HandlerNotFoundException extends InternalServerErrorException {

	private static final long serialVersionUID = 1L;

	public HandlerNotFoundException(String intent) {
		super("Handler not found for intent "+intent);
	}
	public HandlerNotFoundException(String intent, Exception e) {
		super("Handler not found for intent "+intent, e);
	}

}
