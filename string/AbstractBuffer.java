package com.anton.string;

import com.anton.object.AbstractBufferObject;
import com.anton.object.BufferableObject;

public interface AbstractBuffer extends AbstractBufferObject<String>, Bufferable, AutoCloseable {

    default int getCountOfElements(Object o) {
        return ((String)o).getBytes().length;
    }
}
