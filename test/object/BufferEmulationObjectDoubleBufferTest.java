package com.anton.test.object;

import com.anton.buffer.object.AbstractBufferObject;
import com.anton.buffer.object.BufferEmulatorObject;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class BufferEmulationObjectDoubleBufferTest extends DoubleBufferObjectTest {

    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                AbstractBufferObject<String> extBuf = new BufferEmulatorObject<>(externalBufferSize);
                AbstractBufferObject<String> intBuf = new BufferEmulatorObject<>(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
