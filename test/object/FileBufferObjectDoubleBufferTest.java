package com.anton.test.object;

import com.anton.buffer.object.AbstractBufferObject;
import com.anton.buffer.object.FileBufferObject;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class FileBufferObjectDoubleBufferTest extends DoubleBufferObjectTest {
    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                AbstractBufferObject<String> extBuf = new FileBufferObject<>(externalBufferSize, "bufferObj1");
                AbstractBufferObject<String> intBuf = new FileBufferObject<>(internalBufferSize, "bufferObj2")
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
