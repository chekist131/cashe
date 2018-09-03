package com.anton.test;

import com.anton.AbstractBuffer;
import com.anton.BufferFactory;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class FileBufferDoubleBufferTest extends DoubleBufferTest {
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                AbstractBuffer extBuf = BufferFactory.getFileBuffer(externalBufferSize, comparator, "test1");
                AbstractBuffer intBuf = BufferFactory.getFileBuffer(internalBufferSize, comparator, "test2")
        ){
            super.saveAndRestore(extBuf, intBuf, comparator);
        }
    }
}
