package com.arjvik.arjmart.dialogflow;

import net.andreinc.aleph.AlephFormatter.Style;

public enum AlephStyles implements Style {
	ArjMartAlephStyle('$','(',')','`');
    
    private final char START_CHARACTER;
    private final char OPEN_BRACKET;
    private final char CLOSE_BRACKET;
    private final char ESCAPE_CHARACTER;
    
    private AlephStyles(char START_CHARACTER, char OPEN_BRACKET, char CLOSE_BRACKET, char ESCAPE_CHARACTER) {
        this.START_CHARACTER = START_CHARACTER;
        this.OPEN_BRACKET = OPEN_BRACKET;
        this.CLOSE_BRACKET = CLOSE_BRACKET;
        this.ESCAPE_CHARACTER = ESCAPE_CHARACTER;
    }

    public char getStartCharacter() {
        return START_CHARACTER;
    }

    public char getOpenBracket() {
        return OPEN_BRACKET;
    }

    public char getCloseBracket() {
        return CLOSE_BRACKET;
    }

    public char getEscapeCharacter() {
        return ESCAPE_CHARACTER;
    }
}
