package com.anton.strateges;

import java.util.Date;
import java.util.Map;

public abstract class CacheStrategy implements BufferComparator {

    Map<Integer, Date> savingTime;

    CacheStrategy(Map<Integer, Date> savingTime) {
        this.savingTime = savingTime;
    }
}
