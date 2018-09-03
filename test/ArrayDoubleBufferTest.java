package com.anton.test;

import com.anton.*;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class ArrayDoubleBufferTest extends DoubleBufferTest {

    @Test
    public void save() throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        doubleBuffer = new DoubleBuffer(
                BufferFactory.getContinuousBuffer(externalBufferSize, comparator),
                BufferFactory.getContinuousBuffer(internalBufferSize, comparator),
                comparator);
        save(doubleBuffer);
    }

    @Test
    public void restore() throws BufferKeyNotFoundException, BufferIOException {
        restore(doubleBuffer);
    }

}
