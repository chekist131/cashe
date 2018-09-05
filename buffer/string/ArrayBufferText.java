package com.anton.buffer.string;

public class ArrayBufferText extends ContinuousBufferText {

    private char[] data;

    public ArrayBufferText(int bufferSize) {
        data = new char[bufferSize];
    }

    @Override
    public int getSize(){
        return data.length;
    }

    @Override
    protected char[] fetch() {
        return data;
    }

    @Override
    protected void stash(char[] data) {
        this.data = data;
    }

    @Override
    public void close(){

    }
}
