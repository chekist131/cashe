package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.Map;
import java.util.Set;

public abstract class AbstractBuffer implements IBuffer {

    private BufferComparator comparator;

    AbstractBuffer(BufferComparator comparator) {
        this.comparator = comparator;
    }

    BufferComparator getComparator() {
        return comparator;
    }

    public abstract int getSize();

    public abstract int getUsed();

    int getFree(){
        return getSize() - getUsed();
    }

    void saveSeveral(Set<Map.Entry<Integer, String>> values)
            throws BufferOverflowException, BufferKeyAlreadyExistsException {
        int bytes = values.stream().map(entry -> entry.getValue().length()).reduce(0, (a, b) -> a + b);
        if (getFree() < bytes)
            throw new BufferOverflowException(getFree(), bytes);
        if (values.stream().map(Map.Entry::getKey).anyMatch(this::isContainsKey))
            throw new BufferKeyAlreadyExistsException();
        for(Map.Entry<Integer, String> entry: values)
            save(entry.getKey(), entry.getValue());
    }

    void ejectSeveral(Set<Integer> keys) throws BufferKeyNotFoundException {
        for(int key: keys)
            restore(key);
    }

    /**
     * Return less valuable elements from (buffer + new element)
     * @return extra values
     */
    protected abstract Set<Map.Entry<Integer, String>> getExtraValues(int key, String value);

    /**
     * Find most valuable elements to free freeBytes or less count of bytes
     * @param freeBytes maximum count of bytes of valuable values
     * @return valuable values
     */
    protected abstract Set<Map.Entry<Integer, String>> getValuableValues(final int freeBytes);
}
