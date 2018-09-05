package com.anton;

import java.util.Date;
import java.util.Map;

public class MostRecentlyUsed<T, Key> extends CacheStrategy<T, Key> {

    public MostRecentlyUsed(Map<Key, Date> savingTime) {
        super(savingTime);
    }

    @Override
    public int compare(Map.Entry<Key, T> o1, Map.Entry<Key, T> o2) {
        return savingTime.get(o1.getKey()).compareTo(savingTime.get(o2.getKey()));
    }
}
