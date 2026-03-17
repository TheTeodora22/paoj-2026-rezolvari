package com.pao.laboratory03.exceptions;

public class DuplicateEntryException extends RuntimeException{
    private String message;
    public DuplicateEntryException(String message)
    {
        super(message);
        this.message = message;
    }
}
