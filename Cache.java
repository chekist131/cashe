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

public class Cache implements Bufferable {

    private DoubleBuffer buffer;
    private Map<Integer, Date> savingTime;

    public Cache(int externalBufferSize, int internalBufferSize,
                 Function<Map<Integer, Date>, CacheStrategy> cacheStrategyConstructor) {
        savingTime = new TreeMap<>();
        this.buffer = new DoubleBuffer(
                BufferFactory.getBufferEmulator(externalBufferSize, cacheStrategyConstructor.apply(savingTime)),
                BufferFactory.getBufferEmulator(internalBufferSize, cacheStrategyConstructor.apply(savingTime)),
                cacheStrategyConstructor.apply(savingTime));
    }

    public int getExternalBufferUsed(){
        return buffer.getExternalBufferUsed();
    }

    public int getInternalBufferUsed(){
        return buffer.getInternalBufferUsed();
    }

    public void save(int key, String value) throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException {
        savingTime.put(key, new Date());
        try{
            buffer.save(key, value);
        } catch (BufferKeyAlreadyExistsException | BufferOverflowException e){
            savingTime.remove(key);
            throw e;
        }
    }

    public String restore(int key) throws BufferKeyNotFoundException, BufferIOException {
        Date time = savingTime.get(key);
        savingTime.remove(key);
        try{
            return buffer.restore(key);
        } catch (BufferKeyNotFoundException e){
            savingTime.put(key, time);
            throw e;
        }
    }

    @Override
    public boolean isContainsKey(int key) {
        return buffer.isContainsKey(key);
    }


}
