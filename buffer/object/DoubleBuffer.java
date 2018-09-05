package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.buffer.object.strategies.BufferComparator;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DoubleBuffer<T, Key extends Comparable<? super Key>> implements Bufferable<T, Key> {

    private Buffer<T, Key> externalBuffer;
    private Buffer<T, Key> internalBuffer;
    private BufferComparator<T, Key> comparator;

    public DoubleBuffer(
            Buffer<T, Key> externalBuffer,
            Buffer<T, Key> internalBuffer,
            BufferComparator<T, Key> comparator) {
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
    public boolean isContainsKey(Key key) {
        return externalBuffer.isContainsKey(key) || internalBuffer.isContainsKey(key);
    }

    @Override
    public void save(Key key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        try{
            externalBuffer.save(key, o);
        } catch (BufferOverflowException e){
            Set<Map.Entry<Key, T>> extraValues = externalBuffer.getExtraValues(key, o, comparator);
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
    public T restore(Key key) throws BufferKeyNotFoundException, BufferIOException {
        try{
            T value = externalBuffer.restore(key);
            Set<Map.Entry<Key, T>> valuableValues = internalBuffer.getValuableValues(externalBuffer.getFree(), comparator);
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
