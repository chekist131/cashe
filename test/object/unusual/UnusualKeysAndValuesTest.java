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
            Value value1 = new Value("Anton");
            KeyGen2 key1 = new KeyGen2(5);
            buffer.save(new KeyGen2(5), value1);

            assertEquals(value1, buffer.restore(key1));
        }
    }

}
