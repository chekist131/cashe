package com.anton.test.text;

import com.anton.buffer.string.BufferText;
import com.anton.buffer.string.FileBufferText;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

public class FileTextTest extends TextTest {
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                BufferText extBuf = new FileBufferText(externalBufferSize, "test1");
                BufferText intBuf = new FileBufferText(internalBufferSize, "test2")
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
