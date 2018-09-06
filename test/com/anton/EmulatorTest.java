package com.anton;

import com.anton.buffer.Buffer;
import com.anton.buffer.BufferEmulator;
import org.junit.Test;

public class EmulatorTest extends ObjectTest {

    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                Buffer<String, Integer> extBuf = new BufferEmulator<>(externalBufferSize);
                Buffer<String, Integer> intBuf = new BufferEmulator<>(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
