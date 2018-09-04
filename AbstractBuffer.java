package com.anton;

import com.anton.object.AbstractBufferObject;
import com.anton.object.BufferableObject;

public abstract class AbstractBuffer extends AbstractBufferObject<String> implements BufferableObject<String>, AutoCloseable {

    protected int getCountOfElements(Object o) {
        return ((String)o).getBytes().length;
    }
}
