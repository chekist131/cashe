package com.anton.test.text;

import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import com.anton.buffer.text.BufferText;
import com.anton.buffer.text.BufferEmulatorText;
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
