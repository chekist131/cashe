package com.anton;

public class BufferIOException extends BufferException {
    public BufferIOException(Exception cause) {
        super("buffer I/O exception", cause);
    }
}
