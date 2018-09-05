package com.anton.buffer.object.strategies;

import java.util.Comparator;
import java.util.Map;

public interface BufferComparator<T> extends Comparator<Map.Entry<Integer, T>> {
}
