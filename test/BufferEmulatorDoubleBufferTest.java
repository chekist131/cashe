package com.anton.test;

import com.anton.*;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.string.AbstractBuffer;
import org.junit.Test;

public class BufferEmulatorDoubleBufferTest extends DoubleBufferTest {
    @Test
    public void saveAndRestore() throws BufferKeyAlreadyExistsException, BufferIOException, BufferKeyNotFoundException, BufferOverflowException {
        try(
                AbstractBuffer extBuf = new BufferEmulator(externalBufferSize);
                AbstractBuffer intBuf = new BufferEmulator(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
