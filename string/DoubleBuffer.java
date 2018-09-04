package com.anton.string;

import com.anton.object.DoubleBufferObject;
import com.anton.strateges.BufferComparator;

public class DoubleBuffer extends DoubleBufferObject<String> implements Bufferable {
    public DoubleBuffer(
            AbstractBuffer externalBuffer,
            AbstractBuffer internalBuffer,
            BufferComparator<String> comparator) {
        super(externalBuffer, internalBuffer, comparator);
    }
}
