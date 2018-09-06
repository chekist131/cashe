package com.anton;

import com.anton.buffer.Buffer;
import com.anton.buffer.DoubleBuffer;
import com.anton.buffer.BufferComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class ObjectTest {
    static int externalBufferSize = 45;
    static int internalBufferSize = 10;
    private static BufferComparator<String, Integer> comparator = new DefaultBufferComparator();

    public abstract void saveAndRestore()
            throws BufferOverflowException, BufferKeyAlreadyExistsException,
            BufferIOException, BufferKeyNotFoundException;

    void saveAndRestore(
            Buffer<String, Integer> externalBuffer,
            Buffer<String, Integer> internalBuffer)
            throws
            BufferOverflowException,
            BufferKeyAlreadyExistsException,
            BufferIOException,
            BufferKeyNotFoundException {
        DoubleBuffer<String, Integer> doubleBuffer = new DoubleBuffer<>(externalBuffer, internalBuffer, comparator);

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
