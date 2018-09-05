package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.buffer.object.strategies.BufferComparator;

import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class BufferEmulator<T extends Serializable, Key extends Comparable<? super Key>> implements Buffer<T, Key> {
    private Map<Key, T> data;

    private int size;
    private int used;

    public BufferEmulator(int bufferSize){
        data = new HashMap<>();
        this.size = bufferSize;
        this.used = 0;
    }

    public int getSize(){
        return size;
    }

    public int getUsed() {
        return used;
    }

    @Override
    public boolean isContainsKey(Key key) {
        return data.containsKey(key);
    }

    @Override
    public Set<Map.Entry<Key, T>> getExtraValues(Key key, T value, BufferComparator<T, Key> comparator) throws BufferIOException {
        data.put(key, value);
        Set<Map.Entry<Key, T>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        final int necessaryBytes = getCountOfElements(value) - getFree();
        int byteCounter = 0;
        List<Map.Entry<Key, T>> sortedExtra = data.entrySet().stream()
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
        for(Map.Entry<Key, T> e: sortedExtra){
            extra.add(e);
            byteCounter += getCountOfElements(e.getValue());
            if (byteCounter >= necessaryBytes)
                break;
        }
        data.remove(key);
        return extra;
    }

    @Override
    public Set<Map.Entry<Key, T>> getValuableValues(final int freeBytes, BufferComparator<T, Key> comparator) throws BufferIOException {
        Set<Map.Entry<Key, T>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        int byteCounter = 0;
        List<Map.Entry<Key, T>> sortedExtra = data.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        for(Map.Entry<Key, T> e: sortedExtra){
            byteCounter += getCountOfElements(e.getValue());
            if (byteCounter > freeBytes)
                break;
            extra.add(e);
        }
        return extra;
    }

    @Override
    public void save(Key key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        if (data.containsKey(key))
            throw new BufferKeyAlreadyExistsException();
        int length = getCountOfElements(o);
        if (getFree() < length)
            throw new BufferOverflowException(getFree(), length);
        used += length;
        data.put(key, o);
    }

    @Override
    public T restore(Key key) throws BufferKeyNotFoundException, BufferIOException {
        T result = data.get(key);
        if (result == null)
            throw new BufferKeyNotFoundException(key);
        data.remove(key);
        used -= getCountOfElements(result);
        return result;
    }

    @Override
    public void close(){

    }
}
