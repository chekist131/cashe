package com.anton.test.object;

import com.anton.buffer.object.Cache;
import com.anton.buffer.object.strategies.LeastRecentlyUsed;
import com.anton.buffer.object.strategies.MostRecentlyUsed;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest {
    @Test
    public void saveAndRestore() throws Exception {
        try(Cache<String> cache = new Cache<>(15, 20, "l2buffer", LeastRecentlyUsed::new)){
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
        try(Cache<String> cache = new Cache<>(15, 20, "l2buffer", MostRecentlyUsed::new)){
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
