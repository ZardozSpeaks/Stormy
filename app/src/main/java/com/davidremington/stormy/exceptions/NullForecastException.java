package com.davidremington.stormy.exceptions;


public class NullForecastException extends Exception {

    private static final String EXCEPTION_MESSAGE = "The forecast response was malformed or could not be found!";

    public NullForecastException() {
        super(EXCEPTION_MESSAGE);
    }


}
