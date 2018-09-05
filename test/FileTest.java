package com.anton.test;

import com.anton.buffer.Buffer;
import com.anton.buffer.FileBuffer;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import org.junit.Test;

public class FileTest extends ObjectTest {
    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                Buffer<String, Integer> extBuf = new FileBuffer<>(externalBufferSize, "bufferObj1");
                Buffer<String, Integer> intBuf = new FileBuffer<>(internalBufferSize, "bufferObj2")
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
