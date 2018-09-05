package com.anton.buffer.text;

import com.anton.buffer.object.DoubleBuffer;
import com.anton.buffer.object.strategies.BufferComparator;

public class DoubleBufferText extends DoubleBuffer<String> implements BufferableText {
    public DoubleBufferText(
            BufferText externalBuffer,
            BufferText internalBuffer,
            BufferComparator<String> comparator) {
        super(externalBuffer, internalBuffer, comparator);
    }
}
