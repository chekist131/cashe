package com.anton;

/**
 * The value with the certain key already exists in the buffer
 */
public class BufferKeyAlreadyExistsException extends BufferException {
    public BufferKeyAlreadyExistsException() {
        super("key of the value already exists in buffer");
    }
}
