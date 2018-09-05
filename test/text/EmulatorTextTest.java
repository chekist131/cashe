package com.anton.test.text;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.buffer.string.BufferText;
import com.anton.buffer.string.BufferEmulatorText;
import org.junit.Test;

public class EmulatorTextTest extends TextTest {
    @Test
    public void saveAndRestore() throws BufferKeyAlreadyExistsException, BufferIOException, BufferKeyNotFoundException, BufferOverflowException {
        try(
                BufferText extBuf = new BufferEmulatorText(externalBufferSize);
                BufferText intBuf = new BufferEmulatorText(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
