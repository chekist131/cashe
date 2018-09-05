package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DoubleBuffer<T> implements Bufferable<T> {

    private Buffer<T> externalBuffer;
    private Buffer<T> internalBuffer;
    private BufferComparator<T> comparator;

    public DoubleBuffer(
            Buffer<T> externalBuffer,
            Buffer<T> internalBuffer,
            BufferComparator<T> comparator) {
        this.externalBuffer = externalBuffer;
        this.internalBuffer = internalBuffer;
        this.comparator = comparator;
    }

    public int getExternalBufferUsed(){
        return externalBuffer.getUsed();
    }

    public int getInternalBufferUsed(){
        return internalBuffer.getUsed();
    }

    @Override
    public boolean isContainsKey(int key) {
        return externalBuffer.isContainsKey(key) || internalBuffer.isContainsKey(key);
    }

    @Override
    public void save(int key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        try{
            externalBuffer.save(key, o);
        } catch (BufferOverflowException e){
            Set<Map.Entry<Integer, T>> extraValues = externalBuffer.getExtraValues(key, o, comparator);
            try{
                internalBuffer.saveSeveral(extraValues);
            } catch (BufferKeyAlreadyExistsException ignored){

            }
            if (extraValues.stream().allMatch(entry -> entry.getKey() != key)){
                try{
                    externalBuffer.ejectSeveral(extraValues.stream().map(Map.Entry::getKey).collect(Collectors.toSet()));
                } catch (BufferKeyNotFoundException ignored) {

                }
                try{
                    externalBuffer.save(key, o);
                } catch (BufferKeyAlreadyExistsException ignored){

                }
            }
        }

    }

    @Override
    public T restore(int key) throws BufferKeyNotFoundException, BufferIOException {
        try{
            T value = externalBuffer.restore(key);
            Set<Map.Entry<Integer, T>> valuableValues = internalBuffer.getValuableValues(externalBuffer.getFree(), comparator);
            try{
                externalBuffer.saveSeveral(valuableValues);
            } catch (BufferKeyAlreadyExistsException | BufferOverflowException ignored){

            }
            try{
                internalBuffer.ejectSeveral(valuableValues.stream().map(Map.Entry::getKey).collect(Collectors.toSet()));
            } catch (BufferKeyNotFoundException ignored){

            }
            return value;
        } catch (BufferKeyNotFoundException e){
            return internalBuffer.restore(key);
        }

    }

    @Override
    public void close(){

    }
}
