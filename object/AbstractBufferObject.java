package com.anton.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.Map;
import java.util.Set;

public interface AbstractBufferObject<T> extends BufferableObject<T>, AutoCloseable {
    int getSize();

    int getUsed();

    default int getFree(){
        return getSize() - getUsed();
    }

    default void saveSeveral(Set<Map.Entry<Integer, T>> values)
            throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        int bytes = values.stream().map(entry -> getCountOfElements(entry.getValue())).reduce(0, (a, b) -> a + b);
        if (getFree() < bytes)
            throw new BufferOverflowException(getFree(), bytes);
        if (values.stream().map(Map.Entry::getKey).anyMatch(this::isContainsKey))
            throw new BufferKeyAlreadyExistsException();
        for(Map.Entry<Integer, T> entry: values)
            save(entry.getKey(), entry.getValue());
    }

    default void ejectSeveral(Set<Integer> keys) throws BufferKeyNotFoundException, BufferIOException {
        for(int key: keys)
            restore(key);
    }

    /**
     * Return less valuable elements from (buffer + new element)
     * @return extra values
     */
    Set<Map.Entry<Integer, T>> getExtraValues(int key, T value, BufferComparator<T> comparator) throws BufferIOException;

    /**
     * Find most valuable elements to free freeBytes or less count of bytes
     * @param freeBytes maximum count of bytes of valuable values
     * @return valuable values
     */
    Set<Map.Entry<Integer, T>> getValuableValues(int freeBytes, BufferComparator<T> comparator) throws BufferIOException;

    @Override
    void close();

    default int getCountOfElements(Object o){
        // TODO: 9/4/2018  
        return -1;
    }
}
