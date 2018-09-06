package com.anton;

/**
 * Value with the requested key not exists in the buffer
 */
public class BufferKeyNotFoundException extends BufferException {

    public BufferKeyNotFoundException(Object key) {
        super("Object with the key " + key + " not found");
    }

    public BufferKeyNotFoundException() {
        super("Object key not found");
    }
}
