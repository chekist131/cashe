package com.anton.buffer.object;

public class ArrayBuffer<T> extends ContinuousBuffer<T> {
    private byte[] data;

    public ArrayBuffer(int bufferSize) {
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
