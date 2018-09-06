package com.anton.text.test;

import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import com.anton.text.BufferText;
import com.anton.text.BufferEmulatorText;
import org.junit.Test;

public class EmulatorTextTest extends TextTest {
    @Test
    public void saveAndRestore() throws BufferKeyAlreadyExistsException, BufferIOException, BufferKeyNotFoundException, BufferOverflowException {
        try(
                BufferText<Integer> extBuf = new BufferEmulatorText<>(externalBufferSize);
                BufferText<Integer> intBuf = new BufferEmulatorText<>(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
