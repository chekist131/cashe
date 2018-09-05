package com.anton;

import com.anton.buffer.object.*;
import com.anton.buffer.object.strategies.CacheStrategy;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Cache<T> implements Bufferable<T> {
    private DoubleBuffer<T> doubleBuffer;
    private Buffer<T> externalBufferForDoubleBuffer;
    private Buffer<T> internalBufferForDoubleBuffer;
    private Map<Integer, Date> savingTime;

    public Cache(
            int externalSize,
            int internalSize,
            String filename,
            Function<Map<Integer, Date>, CacheStrategy<T>> cacheStrategyConstructor) throws BufferIOException {
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
    public boolean isContainsKey(int key) {
        return doubleBuffer.isContainsKey(key);
    }

    public void save(int key, T value) throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException {
        savingTime.put(key, new Date());
        try{
            doubleBuffer.save(key, value);
        } catch (BufferKeyAlreadyExistsException | BufferOverflowException e){
            savingTime.remove(key);
            throw e;
        }
    }

    public T restore(int key) throws BufferKeyNotFoundException, BufferIOException {
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
