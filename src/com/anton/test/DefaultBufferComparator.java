package com.anton.test;

import com.anton.buffer.BufferComparator;

import java.util.Map;

public class DefaultBufferComparator implements BufferComparator<String, Integer> {
    @Override
    public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
