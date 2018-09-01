package com.anton.test;

import com.anton.*;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class DoubleBufferBufferTest extends DoubleBufferTest {

    @Test
    public void save() throws BufferOverflowException, BufferKeyAlreadyExistsException {
        doubleBuffer = new DoubleBuffer(
                Buffer::new, externalBufferSize,
                Buffer::new, internalBufferSize,
                comparator);
        save(doubleBuffer);
    }

    @Test
    public void restore() throws BufferKeyNotFoundException {
        restore(doubleBuffer);
    }

}
