package com.anton;

import com.anton.buffer.Buffer;
import com.anton.buffer.ArrayBuffer;
import org.junit.Test;

public class ArrayTest extends ObjectTest {
    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                Buffer<String, Integer> extBuf = new ArrayBuffer<>(externalBufferSize);
                Buffer<String, Integer> intBuf = new ArrayBuffer<>(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
