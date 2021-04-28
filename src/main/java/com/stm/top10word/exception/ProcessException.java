package com.stm.top10word.exception;

public class ProcessException extends Exception {

    public ProcessException(Throwable ex) {
        super(ex);
    }

    public ProcessException(String message) {
        super(message);
    }

}
