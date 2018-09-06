package com.anton.buffer;

import java.util.Comparator;
import java.util.Map;

public interface BufferComparator<T, Key> extends Comparator<Map.Entry<Key, T>> {
}
