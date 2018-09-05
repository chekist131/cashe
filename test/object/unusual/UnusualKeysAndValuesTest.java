package com.anton.test.object.unusual;

import com.anton.buffer.object.Buffer;
import com.anton.buffer.object.BufferEmulator;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnusualKeysAndValuesTest {

    @Test
    public void init()
            throws BufferIOException, BufferKeyAlreadyExistsException,
            BufferOverflowException, BufferKeyNotFoundException {
        try(Buffer<Value, KeyGen2> buffer = new BufferEmulator<>(100)){
            buffer.save(new KeyGen2("5"), new Value("Anton"));
            assertEquals(new Value("Anton"), buffer.restore(new KeyGen2("5")));
        }
    }

}
