package com.anton.buffer;

import com.anton.buffer.object.Buffer;

import java.util.Map;
import java.util.TreeMap;

public abstract class IContinuousBuffer<T> implements Buffer<T> {
    protected Map<Integer, Place> keysToStartAndLength;
    protected int lastIndex;

    public IContinuousBuffer() {
        this.lastIndex = 0;
        this.keysToStartAndLength = new TreeMap<>();
    }

    @Override
    public int getUsed(){
        return lastIndex;
    }

    @Override
    public boolean isContainsKey(int key) {
        return keysToStartAndLength.containsKey(key);
    }
}
