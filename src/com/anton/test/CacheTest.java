package com.anton.test;

import com.anton.Cache;
import com.anton.LeastRecentlyUsed;
import com.anton.MostRecentlyUsed;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest {
    @Test
    public void saveAndRestore() throws Exception {
        try(Cache<String, Integer> cache =
                    new Cache<String, Integer>(15, 20, "l2buffer", LeastRecentlyUsed::new)){
            cache.save(5, "Anton");
            assertEquals(12, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());
            Thread.sleep(10);

            cache.save(12, "Ivan");
            assertEquals(11, cache.getExternalBufferUsed());
            assertEquals(12, cache.getInternalBufferUsed());

            Thread.sleep(10);
            assertEquals("Anton", cache.restore(5));
            assertEquals(11, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());
        }
        try(Cache<String,Integer> cache =
                    new Cache<String, Integer>(15, 20, "l2buffer", MostRecentlyUsed::new)){
            cache.save(5, "Anton");
            assertEquals(12, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());

            Thread.sleep(10);
            cache.save(12, "Ivan");
            assertEquals(12, cache.getExternalBufferUsed());
            assertEquals(11, cache.getInternalBufferUsed());

            Thread.sleep(10);
            assertEquals("Anton", cache.restore(5));
            assertEquals(11, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());
        }
    }
}
