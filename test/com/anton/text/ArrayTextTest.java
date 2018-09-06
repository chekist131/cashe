package com.anton.text;

import com.anton.buffer.text.ArrayBufferText;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import com.anton.buffer.text.BufferText;
import org.junit.Test;

public class ArrayTextTest extends TextTest {
    @Test
    public void saveAndRestore()
            throws BufferKeyAlreadyExistsException, BufferIOException,
            BufferKeyNotFoundException, BufferOverflowException {
        try(
                BufferText<Integer> extBuf = new ArrayBufferText<>(externalBufferSize);
                BufferText<Integer> intBuf = new ArrayBufferText<>(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
