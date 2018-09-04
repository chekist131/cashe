package com.anton.string;

import com.anton.object.AbstractBufferObject;
import com.anton.object.BufferableObject;

public abstract class AbstractBuffer extends AbstractBufferObject<String> implements Bufferable, AutoCloseable {

    protected int getCountOfElements(Object o) {
        return ((String)o).getBytes().length;
    }
}
