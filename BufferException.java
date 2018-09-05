package com.anton;

abstract class BufferException extends Exception {

    BufferException(String message) {
        super(message);
    }

    BufferException(String message, Throwable cause) {
        super(message, cause);
    }
}
