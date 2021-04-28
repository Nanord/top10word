package com.stm.top10word.exception;

public class ProcessStarterException extends RuntimeException {

    public ProcessStarterException(String message, Throwable ex) {
        super(message, ex);
    }

    public ProcessStarterException(Throwable ex) {
        super(ex);
    }

    public ProcessStarterException(String message) {
        super(message);
    }
}
