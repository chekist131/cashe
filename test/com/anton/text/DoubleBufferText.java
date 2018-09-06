package com.anton.text;

import com.anton.buffer.DoubleBuffer;
import com.anton.buffer.BufferComparator;

public class DoubleBufferText<Key extends Comparable<? super Key>>
        extends DoubleBuffer<String, Key> implements BufferableText<Key> {
    public DoubleBufferText(
            BufferText<Key> externalBuffer,
            BufferText<Key> internalBuffer,
            BufferComparator<String, Key> comparator) {
        super(externalBuffer, internalBuffer, comparator);
    }
}
