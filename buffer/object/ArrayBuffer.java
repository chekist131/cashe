package com.anton.buffer.object;

public class ArrayBuffer<T, Key extends Comparable<? super Key>> extends ContinuousBuffer<T, Key> {
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
