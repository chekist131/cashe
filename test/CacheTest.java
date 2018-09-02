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
        cache.save(12, "Ivan");
        cache.restore(12);
    }

}