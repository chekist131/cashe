package com.anton.buffer.object.strategies;

import java.util.Comparator;
import java.util.Map;

public interface BufferComparator<T, Key> extends Comparator<Map.Entry<Key, T>> {
}
