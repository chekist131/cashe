package com.anton.text.test;

import com.anton.text.BufferText;
import com.anton.text.FileBufferText;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import org.junit.Test;

public class FileTextTest extends TextTest {
    @Test
    public void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException {
        try(
                BufferText<Integer> extBuf = new FileBufferText<>(externalBufferSize, "test1");
                BufferText<Integer> intBuf = new FileBufferText<>(internalBufferSize, "test2")
        ){
            super.saveAndRestore(extBuf, intBuf);
        }
    }
}
