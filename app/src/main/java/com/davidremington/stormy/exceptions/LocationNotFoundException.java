package com.davidremington.stormy.exceptions;


public class LocationNotFoundException extends Exception {

    private static final String EXCEPTION_MESSAGE = "The requested location is ambiguous or cannot be found!";

    public LocationNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
}
