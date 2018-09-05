package com.anton.test.object;

import com.anton.buffer.object.Buffer;
import com.anton.buffer.object.BufferEmulator;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class EmulatorTest extends ObjectTest {

    @Override
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                Buffer<String> extBuf = new BufferEmulator<>(externalBufferSize);
                Buffer<String> intBuf = new BufferEmulator<>(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
