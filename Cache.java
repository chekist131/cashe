package com.anton;

import com.anton.buffer.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Cache<T extends Serializable, Key extends Comparable<? super Key>> implements Bufferable<T, Key> {
    private DoubleBuffer<T, Key> doubleBuffer;
    private Buffer<T, Key> externalBufferForDoubleBuffer;
    private Buffer<T, Key> internalBufferForDoubleBuffer;
    private Map<Key, Date> savingTime;

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

    public int getExternalBufferUsed(){
        return doubleBuffer.getExternalBufferUsed();
    }

    public int getInternalBufferUsed(){
        return doubleBuffer.getInternalBufferUsed();
    }

    @Override
    public void close(){
        externalBufferForDoubleBuffer.close();
        internalBufferForDoubleBuffer.close();
    }

    @Override
    public boolean isContainsKey(Key key) {
        return doubleBuffer.isContainsKey(key);
    }

    public void save(Key key, T value) throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException {
        savingTime.put(key, new Date());
        try{
            doubleBuffer.save(key, value);
        } catch (BufferKeyAlreadyExistsException | BufferOverflowException e){
            savingTime.remove(key);
            throw e;
        }
    }

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
