package com.anton.text.test;

import com.anton.text.BufferText;
import com.anton.text.DoubleBufferText;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import com.anton.buffer.BufferComparator;
import com.anton.DefaultBufferComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

abstract class TextTest {

    static int externalBufferSize = 5;
    static int internalBufferSize = 10;
    private static BufferComparator<String, Integer> comparator = new DefaultBufferComparator();

    public abstract void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException;

    void saveAndRestore(
            BufferText<Integer> externalBuffer,
            BufferText<Integer> internalBuffer)
            throws
            BufferOverflowException,
            BufferKeyAlreadyExistsException,
            BufferIOException,
            BufferKeyNotFoundException {
        DoubleBufferText<Integer> doubleBuffer = new DoubleBufferText<>(externalBuffer, internalBuffer, comparator);

        doubleBuffer.save(1, "Igor");
        assertEquals(4, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());

        try{
            doubleBuffer.save(1, "Gena");
            assertNotEquals(1,1);
        } catch (BufferKeyAlreadyExistsException ignored){ }

        doubleBuffer.save(2, "Anton");
        assertEquals(5, doubleBuffer.getExternalBufferUsed());
        assertEquals(4, doubleBuffer.getInternalBufferUsed());

        doubleBuffer.save(3, "Anuta");
        assertEquals(5, doubleBuffer.getExternalBufferUsed());
        assertEquals(9, doubleBuffer.getInternalBufferUsed());

        try{
            doubleBuffer.save(5, "Nick");
            assertNotEquals(1,1);
        } catch (BufferOverflowException e){
            assertEquals(1, e.getBytesRemain());
            assertEquals(4, e.getBytesRequire());
        }

        doubleBuffer.save(4, "J");
        assertEquals(5, doubleBuffer.getExternalBufferUsed());
        assertEquals(10, doubleBuffer.getInternalBufferUsed());

        try{
            doubleBuffer.restore(0);
            assertNotEquals(1,1);
        } catch (BufferKeyNotFoundException ignored){

        }

        assertEquals("Anuta", doubleBuffer.restore(3));
        assertEquals(doubleBuffer.getExternalBufferUsed(), 5);
        assertEquals(doubleBuffer.getInternalBufferUsed(), 5);

        try{
            doubleBuffer.save(3, "Druba");
        } catch (BufferOverflowException | BufferKeyAlreadyExistsException e){
            assertNotEquals(1, 1);
        }

        assertEquals("Igor", doubleBuffer.restore(1));
        assertEquals(5, doubleBuffer.getExternalBufferUsed());
        assertEquals(6, doubleBuffer.getInternalBufferUsed());

        assertEquals("Anton", doubleBuffer.restore(2));
        assertEquals(5, doubleBuffer.getExternalBufferUsed());
        assertEquals(1, doubleBuffer.getInternalBufferUsed());
    }
}