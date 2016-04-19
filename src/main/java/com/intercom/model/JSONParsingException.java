package com.intercom.model;

public class JSONParsingException extends RuntimeException {

    public JSONParsingException(String message) {
        super(message);
    }

    public JSONParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
