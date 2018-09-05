package com.anton.buffer.object.strategies;

import java.util.Date;
import java.util.Map;

public class LeastRecentlyUsed<T> extends CacheStrategy<T> {

    public LeastRecentlyUsed(Map<Integer, Date> savingTime) {
        super(savingTime);
    }

    @Override
    public int compare(Map.Entry<Integer, T> o1, Map.Entry<Integer, T> o2) {
        return savingTime.get(o2.getKey()).compareTo(savingTime.get(o1.getKey()));
    }
}
