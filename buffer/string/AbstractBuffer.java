package com.anton.buffer.string;

import com.anton.buffer.object.AbstractBufferObject;

public interface AbstractBuffer extends AbstractBufferObject<String>, Bufferable, AutoCloseable {

    default int getCountOfElements(Object o) {
        return ((String)o).getBytes().length;
    }
}
