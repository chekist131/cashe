package com.anton;

import com.anton.strateges.BufferComparator;

public class ArrayBuffer extends AbstractContinuousBuffer {

    private char[] data;

    ArrayBuffer(int bufferSize, BufferComparator comparator) {
        super(comparator);
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
}
