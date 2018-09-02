package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DoubleBuffer implements Bufferable {

    private AbstractBuffer externalBuffer;
    private AbstractBuffer internalBuffer;

    public DoubleBuffer(
            AbstractBuffer externalBuffer,
            AbstractBuffer internalBuffer,
            BufferComparator comparator) {
        this.externalBuffer = externalBuffer;
        this.externalBuffer.setComparator(comparator);
        this.internalBuffer = internalBuffer;
        this.externalBuffer.setComparator(comparator);
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
    public void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException {
        try{
            externalBuffer.save(key, o);
        } catch (BufferOverflowException e){
            Set<Map.Entry<Integer, String>> extraValues = externalBuffer.getExtraValues(key, o);
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
    public String restore(int key) throws BufferKeyNotFoundException {
        try{
            String value = externalBuffer.restore(key);
            Set<Map.Entry<Integer, String>> valuableValues = internalBuffer.getValuableValues(externalBuffer.getFree());
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
}
