package com.anton.buffer.object.strategies;

import java.util.Date;
import java.util.Map;

public class MostRecentlyUsed<T> extends CacheStrategy<T> {

    public MostRecentlyUsed(Map<Integer, Date> savingTime) {
        super(savingTime);
    }

    @Override
    public int compare(Map.Entry<Integer, T> o1, Map.Entry<Integer, T> o2) {
        return savingTime.get(o1.getKey()).compareTo(savingTime.get(o2.getKey()));
    }
}
