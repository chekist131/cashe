package com.anton.unusual;

import com.anton.buffer.Buffer;
import com.anton.buffer.BufferEmulator;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
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
