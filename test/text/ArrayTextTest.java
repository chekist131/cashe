package com.anton.test.text;

import com.anton.buffer.text.ArrayBufferText;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.buffer.text.BufferText;
import org.junit.Test;

public class ArrayTextTest extends TextTest {
    @Test
    public void saveAndRestore()
            throws BufferKeyAlreadyExistsException, BufferIOException,
            BufferKeyNotFoundException, BufferOverflowException {
        try(
                BufferText extBuf = new ArrayBufferText(externalBufferSize);
                BufferText intBuf = new ArrayBufferText(internalBufferSize)
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
