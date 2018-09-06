package com.anton.text.test;

import com.anton.text.ArrayBufferText;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import com.anton.text.BufferText;
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
