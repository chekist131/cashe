package com.anton.buffer;

import com.anton.buffer.object.Buffer;

import java.util.Map;
import java.util.TreeMap;

public abstract class IContinuousBuffer<T, Key extends Comparable<? super Key>> implements Buffer<T, Key> {
    protected Map<Key, Place> keysToStartAndLength;
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
    public boolean isContainsKey(Key key) {
        return keysToStartAndLength.containsKey(key);
    }
}
