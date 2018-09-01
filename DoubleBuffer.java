package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.stratages.BufferComparator;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class DoubleBuffer implements IBuffer{

    private AbstractBuffer externalBuffer;
    private AbstractBuffer internalBuffer;

    public DoubleBuffer(AbstractBuffer externalBuffer, AbstractBuffer internalBuffer) {
        this.externalBuffer = externalBuffer;
        this.internalBuffer = internalBuffer;
    }

    public DoubleBuffer(
            BiFunction<Integer, BufferComparator, AbstractBuffer> externalBufferConstructor,
            int externalBufferSize,
            BiFunction<Integer, BufferComparator, AbstractBuffer> internalBufferConstructor,
            int internalBufferSize,
            BufferComparator comparator) {
        externalBuffer = externalBufferConstructor.apply(externalBufferSize, comparator);
        internalBuffer = internalBufferConstructor.apply(internalBufferSize, comparator);
    }

    public int getExternalBufferUsed(){
        return externalBuffer.getUsed();
    }

    public int getInternalBufferUsed(){
        return internalBuffer.getUsed();
    }

    @Override
    public void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException {
        try{
            externalBuffer.save(key, o);
        } catch (BufferOverflowException e){
            Set<Map.Entry<Integer, String>> extraValues = externalBuffer.getExtraValues(o.length());
            try{
                try{
                    internalBuffer.saveSeveral(extraValues);
                } catch (BufferKeyAlreadyExistsException ignored){

                }
                try{
                    externalBuffer.ejectSeveral(extraValues.stream().map(Map.Entry::getKey).collect(Collectors.toSet()));
                } catch (BufferKeyNotFoundException ignored) {

                }
                try{
                    externalBuffer.save(key, o);
                } catch (BufferKeyAlreadyExistsException ignored){

                }
            } catch (BufferOverflowException e1){
                internalBuffer.save(key, o);
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
