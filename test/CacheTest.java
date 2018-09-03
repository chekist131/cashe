package com.anton.test;

import com.anton.Cache;
import com.anton.strateges.LeastRecentlyUsed;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void saveAndRestore() throws Exception {
        try(Cache cache = new Cache(5, 10, LeastRecentlyUsed::new)){
            cache.save(5, "Anton");
            assertEquals(5, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());

            cache.save(12, "Ivan");
            assertEquals(4, cache.getExternalBufferUsed());
            assertEquals(5, cache.getInternalBufferUsed());

            assertEquals("Ivan", cache.restore(12));
            assertEquals(5, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());
        }

    }

}