package com.arjvik.arjmart.dialogflow.entities;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Context {
	private String name;
	private int lifespanCount;
	private Map<String, Object> parameters;
}
