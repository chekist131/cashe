package com.anton;

import com.anton.buffer.BufferComparator;

import java.util.Date;
import java.util.Map;

public abstract class CacheStrategy<T, Key> implements BufferComparator<T, Key> {

    Map<Key, Date> savingTime;

    CacheStrategy(Map<Key, Date> savingTime) {
        this.savingTime = savingTime;
    }

}
