package com.anton.text.test;

import com.anton.text.CacheText;
import com.anton.LeastRecentlyUsed;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheTextTest {

    @Test
    public void saveAndRestore() throws Exception {
        try(CacheText<Integer> cache = new CacheText<>(5, 10, LeastRecentlyUsed::new)){
            cache.save(5, "Anton");
            assertEquals(5, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());

            Thread.sleep(10);
            cache.save(12, "Ivan");
            assertEquals(4, cache.getExternalBufferUsed());
            assertEquals(5, cache.getInternalBufferUsed());

            Thread.sleep(10);
            assertEquals("Ivan", cache.restore(12));
            assertEquals(5, cache.getExternalBufferUsed());
            assertEquals(0, cache.getInternalBufferUsed());
        }
    }

}