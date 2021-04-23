package com.stm.top10word.exception;

public class ReaderException extends RuntimeException {

    public ReaderException(String message, Throwable ex) {
        super(message, ex);
    }

    public ReaderException(Throwable ex) {
        super(ex);
    }

    public ReaderException(String message) {
        super(message);
    }
}
