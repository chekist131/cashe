package com.anton.test;

import com.anton.*;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class BufferEmulatorDoubleBufferTest extends DoubleBufferTest {
    @Test
    public void saveAndRestore() throws BufferKeyAlreadyExistsException, BufferIOException, BufferKeyNotFoundException, BufferOverflowException {
        try(
                AbstractBuffer extBuf = BufferFactory.getBufferEmulator(externalBufferSize, null);
                AbstractBuffer intBuf = BufferFactory.getBufferEmulator(internalBufferSize, null)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
