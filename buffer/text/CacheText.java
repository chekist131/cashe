package com.anton.buffer.text;

import com.anton.CacheStrategy;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class CacheText implements BufferableText {
    private DoubleBufferText doubleBufferText;
    private BufferText externalBufferForDoubleBuffer;
    private BufferText internalBufferForDoubleBuffer;
    private Map<Integer, Date> savingTime;

    public CacheText(
            int externalSize,
            int internalSize,
            Function<Map<Integer, Date>, CacheStrategy<String, Integer>> cacheStrategyConstructor) throws BufferIOException {
        this.savingTime = new TreeMap<>();
        this.externalBufferForDoubleBuffer = new ArrayBufferText(externalSize);
        this.internalBufferForDoubleBuffer = new FileBufferText(internalSize, "l2buffer");
        this.doubleBufferText = new DoubleBufferText(
                this.externalBufferForDoubleBuffer,
                this.internalBufferForDoubleBuffer,
                cacheStrategyConstructor.apply(this.savingTime));
    }

    public int getExternalBufferUsed(){
        return doubleBufferText.getExternalBufferUsed();
    }

    public int getInternalBufferUsed(){
        return doubleBufferText.getInternalBufferUsed();
    }

    @Override
    public void close(){
        externalBufferForDoubleBuffer.close();
        internalBufferForDoubleBuffer.close();
    }

    @Override
    public boolean isContainsKey(Integer key) {
        return doubleBufferText.isContainsKey(key);
    }

    public void save(Integer key, String value) throws BufferKeyAlreadyExistsException, BufferOverflowException, BufferIOException {
        savingTime.put(key, new Date());
        try{
            doubleBufferText.save(key, value);
        } catch (BufferKeyAlreadyExistsException | BufferOverflowException e){
            savingTime.remove(key);
            throw e;
        }
    }

    public String restore(Integer key) throws BufferKeyNotFoundException, BufferIOException {
        Date time = savingTime.get(key);
        savingTime.remove(key);
        try{
            return doubleBufferText.restore(key);
        } catch (BufferKeyNotFoundException e){
            savingTime.put(key, time);
            throw e;
        }
    }
}
