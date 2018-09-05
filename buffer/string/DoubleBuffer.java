package com.anton.buffer.string;

import com.anton.strateges.BufferComparator;

public class DoubleBuffer extends com.anton.buffer.object.DoubleBuffer<String> implements BufferableText {
    public DoubleBuffer(
            BufferText externalBuffer,
            BufferText internalBuffer,
            BufferComparator<String> comparator) {
        super(externalBuffer, internalBuffer, comparator);
    }
}
