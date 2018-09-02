package com.anton.test;

import com.anton.BufferEmulator;
import com.anton.BufferFactory;
import com.anton.DoubleBuffer;
import com.anton.FileBuffer;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class FileBufferDoubleBufferTest extends DoubleBufferTest {

    @Test
    public void save() throws BufferOverflowException, BufferKeyAlreadyExistsException {
        doubleBuffer = new DoubleBuffer(
                /*BufferEmulator::new, externalBufferSize,
                FileBuffer::new, internalBufferSize,*/
                BufferFactory.getFileBuffer(externalBufferSize, comparator, "test1"),
                BufferFactory.getFileBuffer(internalBufferSize, comparator, "test2"),
                comparator);
        save(doubleBuffer);
    }

    @Test
    public void restore() throws BufferKeyNotFoundException {
        restore(doubleBuffer);
    }

}
