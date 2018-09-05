package com.anton;

public class BufferKeyAlreadyExistsException extends BufferException {
    public BufferKeyAlreadyExistsException() {
        super("key of the value already exists in buffer");
    }
}
