package com.anton.test;

import com.anton.*;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;
import com.anton.strateges.DefaultBufferComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

abstract class DoubleBufferTest {

    static int externalBufferSize = 5;
    static int internalBufferSize = 10;
    static BufferComparator comparator = new DefaultBufferComparator();

    public abstract void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException;

    void saveAndRestore(
            AbstractBuffer externalBuffer,
            AbstractBuffer internalBuffer,
            BufferComparator comparator)
            throws
            BufferOverflowException,
            BufferKeyAlreadyExistsException,
            BufferIOException,
            BufferKeyNotFoundException {
        DoubleBuffer doubleBuffer = new DoubleBuffer(externalBuffer, internalBuffer, comparator);

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