package com.anton;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.CacheStrategy;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Cache implements Bufferable, AutoCloseable {

    private DoubleBuffer doubleBuffer;
    private Map<Integer, Date> savingTime;

    private AbstractBuffer externalBufferForDoubleBuffer;
    private AbstractBuffer internalBufferForDoubleBuffer;

    public Cache(int externalBufferSize, int internalBufferSize,
                 Function<Map<Integer, Date>, CacheStrategy> cacheStrategyConstructor) {
        savingTime = new TreeMap<>();
        this.externalBufferForDoubleBuffer =
                BufferFactory.getBufferEmulator(externalBufferSize, cacheStrategyConstructor.apply(savingTime));
        this.internalBufferForDoubleBuffer =
                BufferFactory.getBufferEmulator(internalBufferSize, cacheStrategyConstructor.apply(savingTime));
        this.doubleBuffer = new DoubleBuffer(
                externalBufferForDoubleBuffer,
                internalBufferForDoubleBuffer,
                cacheStrategyConstructor.apply(savingTime));
    }

    public int getExternalBufferUsed(){
        return doubleBuffer.getExternalBufferUsed();
    }

    public int getInternalBufferUsed(){
        return doubleBuffer.getInternalBufferUsed();
    }

    public void save(int key, String value) throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException {
        savingTime.put(key, new Date());
        try{
            doubleBuffer.save(key, value);
        } catch (BufferKeyAlreadyExistsException | BufferOverflowException e){
            savingTime.remove(key);
            throw e;
        }
    }

    public String restore(int key) throws BufferKeyNotFoundException, BufferIOException {
        Date time = savingTime.get(key);
        savingTime.remove(key);
        try{
            return doubleBuffer.restore(key);
        } catch (BufferKeyNotFoundException e){
            savingTime.put(key, time);
            throw e;
        }
    }

    @Override
    public boolean isContainsKey(int key) {
        return doubleBuffer.isContainsKey(key);
    }


    @Override
    public void close(){
        externalBufferForDoubleBuffer.close();
        internalBufferForDoubleBuffer.close();
    }
}
