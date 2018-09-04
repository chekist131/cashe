package com.anton.test.object;

import com.anton.buffer.object.AbstractBufferObject;
import com.anton.buffer.object.DoubleBufferObject;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;
import com.anton.strateges.DefaultBufferComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class DoubleBufferObjectTest {
    static int externalBufferSize = 45;
    static int internalBufferSize = 10;
    private static BufferComparator<String> comparator = new DefaultBufferComparator();

    public abstract void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException;

    void saveAndRestore(
            AbstractBufferObject<String> externalBuffer,
            AbstractBufferObject<String> internalBuffer)
            throws
            BufferOverflowException,
            BufferKeyAlreadyExistsException,
            BufferIOException,
            BufferKeyNotFoundException {
        DoubleBufferObject<String> doubleBuffer = new DoubleBufferObject<>(externalBuffer, internalBuffer, comparator);

        doubleBuffer.save(1, "Igor");
        assertEquals(11, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());

        try{
            doubleBuffer.save(1, "Gena");
            assertNotEquals(1,1);
        } catch (BufferKeyAlreadyExistsException ignored){ }

        doubleBuffer.save(2, "Anton");
        assertEquals(23, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());

        doubleBuffer.save(3, "Anuta");
        assertEquals(35, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());

        try{
            doubleBuffer.save(5, "Nick");
            assertNotEquals(1,1);
        } catch (BufferOverflowException e){
            assertEquals(10, e.getBytesRemain());
            assertEquals(11, e.getBytesRequire());
        }

        doubleBuffer.save(4, "J");
        assertEquals(43, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());

        try{
            doubleBuffer.restore(0);
            assertNotEquals(1,1);
        } catch (BufferKeyNotFoundException ignored){

        }

        assertEquals("Anuta", doubleBuffer.restore(3));
        assertEquals(doubleBuffer.getExternalBufferUsed(), 31);
        assertEquals(doubleBuffer.getInternalBufferUsed(), 0);

        try{
            doubleBuffer.save(3, "Druba");
        } catch (BufferOverflowException | BufferKeyAlreadyExistsException e){
            assertNotEquals(1, 1);
        }

        assertEquals("Igor", doubleBuffer.restore(1));
        assertEquals(32, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());

        assertEquals("Anton", doubleBuffer.restore(2));
        assertEquals(20, doubleBuffer.getExternalBufferUsed());
        assertEquals(0, doubleBuffer.getInternalBufferUsed());
    }
}
