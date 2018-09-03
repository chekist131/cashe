package com.anton.test;

import com.anton.Cache;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.LeastRecentlyUsed;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void test() throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferKeyNotFoundException {
        Cache cache = new Cache(5, 10, LeastRecentlyUsed::new);
        cache.save(5, "Anton");
        assertEquals(5, cache.getExternalBufferUsed());
        assertEquals(0, cache.getInternalBufferUsed());

        cache.save(12, "Ivan");
        assertEquals(4, cache.getExternalBufferUsed());
        assertEquals(5, cache.getInternalBufferUsed());

        cache.restore(12);
        assertEquals(5, cache.getExternalBufferUsed());
        assertEquals(0, cache.getInternalBufferUsed());
    }

}