package com.anton.test;

import com.anton.*;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

import java.util.Comparator;

public class DoubleBufferContinuousTest extends DoubleBufferTest {

    @Test
    public void save() throws BufferOverflowException, BufferKeyAlreadyExistsException {
        doubleBuffer = new DoubleBuffer(
                ContinuousBuffer::new, externalBufferSize,
                ContinuousBuffer::new, internalBufferSize,
                comparator);
        save(doubleBuffer);
    }

    @Test
    public void restore() throws BufferKeyNotFoundException {
        restore(doubleBuffer);
    }

}
