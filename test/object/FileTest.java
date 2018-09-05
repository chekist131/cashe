package com.anton.test.object;

import com.anton.buffer.object.Buffer;
import com.anton.buffer.object.FileBuffer;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class FileTest extends ObjectTest {
    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                Buffer<String> extBuf = new FileBuffer<>(externalBufferSize, "bufferObj1");
                Buffer<String> intBuf = new FileBuffer<>(internalBufferSize, "bufferObj2")
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
