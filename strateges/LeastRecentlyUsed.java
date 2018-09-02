package com.anton.strateges;

import java.util.Date;
import java.util.Map;

public class LeastRecentlyUsed extends CacheStrategy {

    public LeastRecentlyUsed(Map<Integer, Date> savingTime) {
        super(savingTime);
    }

    @Override
    public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
        return savingTime.get(o2.getKey()).compareTo(savingTime.get(o1.getKey()));
    }
}
