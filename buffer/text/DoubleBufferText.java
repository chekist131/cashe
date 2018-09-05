package com.anton.buffer.text;

import com.anton.buffer.DoubleBuffer;
import com.anton.buffer.BufferComparator;

public class DoubleBufferText extends DoubleBuffer<String, Integer> implements BufferableText {
    public DoubleBufferText(
            BufferText externalBuffer,
            BufferText internalBuffer,
            BufferComparator<String, Integer> comparator) {
        super(externalBuffer, internalBuffer, comparator);
    }
}
