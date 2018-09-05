package com.anton;

/**
 * I/O, cast or serialization exception happened in the buffer
 */
public class BufferIOException extends BufferException {
    public BufferIOException(Exception cause) {
        super("buffer I/O exception", cause);
    }
}
