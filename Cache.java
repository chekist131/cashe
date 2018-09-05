package com.anton;

import com.anton.buffer.object.DoubleBuffer;
import com.anton.buffer.text.*;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.CacheStrategy;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Cache implements BufferableText{

    private DoubleBuffer<String> doubleBuffer;
    private Map<Integer, Date> savingTime;

    private BufferText externalBufferForDoubleBuffer;
    private BufferText internalBufferForDoubleBuffer;

    public Cache(int externalBufferSize, int internalBufferSize,
                 Function<Map<Integer, Date>, CacheStrategy> cacheStrategyConstructor) throws BufferIOException {
        savingTime = new TreeMap<>();
        this.externalBufferForDoubleBuffer = new ArrayBufferText(externalBufferSize);
        this.internalBufferForDoubleBuffer = new FileBufferText(internalBufferSize, "cacheL2Buffer");
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
