package com.anton.buffer.object.strategies;

import java.util.Date;
import java.util.Map;

public abstract class CacheStrategy<T> implements BufferComparator<T>  {

    Map<Integer, Date> savingTime;

    CacheStrategy(Map<Integer, Date> savingTime) {
        this.savingTime = savingTime;
    }

}
