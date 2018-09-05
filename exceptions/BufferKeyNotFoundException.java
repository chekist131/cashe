package com.anton.exceptions;

public class BufferKeyNotFoundException extends BufferException {

    public BufferKeyNotFoundException(int key) {
        super("Object with the key " + key + " not found");
    }

    public BufferKeyNotFoundException() {
        super("Object key not found");
    }
}
