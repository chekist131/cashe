package com.anton.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.Map;
import java.util.Set;

public abstract class AbstractBufferObject<T> implements BufferableObject<T>, AutoCloseable {
    public abstract int getSize();

    public abstract int getUsed();

    public int getFree(){
        return getSize() - getUsed();
    }

    public void saveSeveral(Set<Map.Entry<Integer, T>> values)
            throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        int bytes = values.stream().map(entry -> getCountOfElements(entry.getValue())).reduce(0, (a, b) -> a + b);
        if (getFree() < bytes)
            throw new BufferOverflowException(getFree(), bytes);
        if (values.stream().map(Map.Entry::getKey).anyMatch(this::isContainsKey))
            throw new BufferKeyAlreadyExistsException();
        for(Map.Entry<Integer, T> entry: values)
            save(entry.getKey(), entry.getValue());
    }

    public void ejectSeveral(Set<Integer> keys) throws BufferKeyNotFoundException, BufferIOException {
        for(int key: keys)
            restore(key);
    }

    /**
     * Return less valuable elements from (buffer + new element)
     * @return extra values
     */
    public abstract Set<Map.Entry<Integer, T>> getExtraValues(int key, String value, BufferComparator comparator) throws BufferIOException;

    /**
     * Find most valuable elements to free freeBytes or less count of bytes
     * @param freeBytes maximum count of bytes of valuable values
     * @return valuable values
     */
    public abstract Set<Map.Entry<Integer, T>> getValuableValues(int freeBytes, BufferComparator comparator) throws BufferIOException;

    @Override
    public abstract void close();

    protected int getCountOfElements(Object o){
        return -1;
    }
}
