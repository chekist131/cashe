package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;

public class ArrayBufferObject<T> extends ContinuousBufferObject<T> {
    private byte[] data;

    public ArrayBufferObject(int bufferSize) {
        data = new byte[bufferSize];
    }

    @Override
    byte[] fetch(){
        return data;
    }

    @Override
    void stash(byte[] data){
        this.data = data;
    }

    @Override
    public int getSize() {
        return data.length;
    }

    @Override
    public void close() {

    }
}
