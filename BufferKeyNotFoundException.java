package com.anton;

public class BufferKeyNotFoundException extends BufferException {

    public BufferKeyNotFoundException(Object key) {
        super("Object with the key " + key + " not found");
    }

    public BufferKeyNotFoundException() {
        super("Object key not found");
    }
}
