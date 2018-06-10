package com.arjvik.arjmart.dialogflow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Intent {
	private String name;
	private String displayName;
}
