package com.oeitho.exception;

public class PlayerNotFoundException extends Exception {
    
    private static final long serialVersionUID = 1685372123345922658L;

    public PlayerNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
    
}