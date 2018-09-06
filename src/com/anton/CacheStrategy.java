package com.anton;

import com.anton.buffer.BufferComparator;

import java.util.Date;
import java.util.Map;

/**
 * Abstract class is the base of certain cache strategies based on time of their last usage
 * @param <T> cached value
 * @param <Key> keys of the values
 */
public abstract class CacheStrategy<T, Key> implements BufferComparator<T, Key> {

    Map<Key, Date> savingTime;

    /**
     * Create the cache strategy will be used for comparing different cache values (based on the time
     * of their last usage)
     * @param savingTime the map collection used for storing of matching the keys of cached values
     *                   and the times of their last usages
     */
    CacheStrategy(Map<Key, Date> savingTime) {
        this.savingTime = savingTime;
    }

}
