package com.anton;

import com.anton.buffer.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Double leveled cache. First level is array, second level is file
 * @param <T> type of cached elements (must implement Serializable interface)
 * @param <Key> type of key of cached elements
 */
public class Cache<T extends Serializable, Key extends Comparable<? super Key>> implements Bufferable<T, Key> {
    private DoubleBuffer<T, Key> doubleBuffer;
    private Buffer<T, Key> externalBufferForDoubleBuffer;
    private Buffer<T, Key> internalBufferForDoubleBuffer;
    private Map<Key, Date> savingTime;

    /**
     * Create cache with certain L1 and L2 sizes of buffers,
     * the name of file of L2 buffer, and caching strategy based on time
     * @param externalSize size of L1 buffer (bytes)
     * @param internalSize size of L2 buffer (bytes)
     * @param filename name of file used by L2 buffer
     * @param cacheStrategyConstructor cashing strategy based on time (constructor reference
     *                                 with (Map<Key, java.util.Date>) signature)
     */
    public Cache(
            int externalSize,
            int internalSize,
            String filename,
            Function<Map<Key, Date>, CacheStrategy<T, Key>> cacheStrategyConstructor) throws BufferIOException {
        this.savingTime = new TreeMap<>();
        this.externalBufferForDoubleBuffer = new ArrayBuffer<>(externalSize);
        this.internalBufferForDoubleBuffer = new FileBuffer<>(internalSize, filename);
        this.doubleBuffer = new DoubleBuffer<>(
                this.externalBufferForDoubleBuffer,
                this.internalBufferForDoubleBuffer,
                cacheStrategyConstructor.apply(this.savingTime));
    }

    /**
     * Get count of used bytes from L1 buffer
     */
    public int getExternalBufferUsed(){
        return doubleBuffer.getExternalBufferUsed();
    }

    /**
     * Get count of used bytes from L2 buffer
     */
    public int getInternalBufferUsed(){
        return doubleBuffer.getInternalBufferUsed();
    }

    @Override
    public void close() throws BufferIOException{
        externalBufferForDoubleBuffer.close();
        internalBufferForDoubleBuffer.close();
    }

    /**
     * The value with the key is contains in cache or not
     * @param key key
     */
    @Override
    public boolean isContainsKey(Key key) {
        return doubleBuffer.isContainsKey(key);
    }

    /**
     * Save used value with the key in cache with the time of the last usage
     * @param key key
     * @param value value
     * @param date time of the last usage
     */
    public void save(Key key, T value, Date date)
        throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException {
        savingTime.put(key, date);
        try{
            doubleBuffer.save(key, value);
        } catch (BufferKeyAlreadyExistsException | BufferOverflowException e){
            savingTime.remove(key);
            throw e;
        }
    }

    /**
     * Save just used value with the key in cache
     * @param key key
     * @param value value
     */
    public void save(Key key, T value) throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException{
        this.save(key, value, new Date());
    }

    /**
     * Get the value by the key and remove this value from the cache
     * @param key key
     * @return value
     */
    public T restore(Key key) throws BufferKeyNotFoundException, BufferIOException {
        Date time = savingTime.get(key);
        savingTime.remove(key);
        try{
            return doubleBuffer.restore(key);
        } catch (BufferKeyNotFoundException e){
            savingTime.put(key, time);
            throw e;
        }
    }
}
