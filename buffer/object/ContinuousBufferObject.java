package com.anton.buffer.object;

import com.anton.buffer.IContinuousBuffer;
import com.anton.buffer.Place;
import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.*;

public abstract class ContinuousBufferObject<T> extends IContinuousBuffer<T> {
    @Override
    public Set<Map.Entry<Integer, T>> getExtraValues(int key, T value, BufferComparator<T> comparator) throws BufferIOException {
        byte[] data = fetch();
        SortedSet<Map.Entry<Integer, T>> allData = new TreeSet<>(comparator.reversed());
        for(Map.Entry<Integer, Place> keyToStartAndLength: keysToStartAndLength.entrySet()){
            int to = keyToStartAndLength.getValue().getLength() + keyToStartAndLength.getValue().getStart();
            int start = keyToStartAndLength.getValue().getStart();
            allData.add(
                    new AbstractMap.SimpleImmutableEntry<>(
                            keyToStartAndLength.getKey(),
                            getObjectFromBytes(Arrays.copyOfRange(data, start, to))));
        }
        allData.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
        Set<Map.Entry<Integer, T>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        final int necessaryBytes = getCountOfElements(value) - getFree();
        int byteCounter = 0;
        for(Map.Entry<Integer, T> e: allData){
            extra.add(e);
            byteCounter += e.getKey() == key ? getCountOfElements(value) : keysToStartAndLength.get(e.getKey()).getLength();
            if (byteCounter >= necessaryBytes)
                break;
        }
        return extra;
    }

    @Override
    public Set<Map.Entry<Integer, T>> getValuableValues(int freeBytes, BufferComparator<T> comparator) throws BufferIOException {
        byte[] data = fetch();
        SortedSet<Map.Entry<Integer, T>> allData = new TreeSet<>(comparator);
        for(Map.Entry<Integer, Place> keyToStartAndLength: keysToStartAndLength.entrySet()){
            int to = keyToStartAndLength.getValue().getLength() + keyToStartAndLength.getValue().getStart();
            int start = keyToStartAndLength.getValue().getStart();
            allData.add(new AbstractMap.SimpleImmutableEntry<>(
                    keyToStartAndLength.getKey(),
                    getObjectFromBytes(Arrays.copyOfRange(data, start, to))));
        }
        Set<Map.Entry<Integer, T>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        int byteCounter = 0;
        for(Map.Entry<Integer, T> e: allData){
            byteCounter += keysToStartAndLength.get(e.getKey()).getLength();
            if (byteCounter > freeBytes)
                break;
            extra.add(e);
        }
        return extra;
    }

    @Override
    public void save(int key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        if (keysToStartAndLength.containsKey(key))
            throw new BufferKeyAlreadyExistsException();
        byte[] valueBytes = (byte[])getElements(o);
        if (getFree() < valueBytes.length)
            throw new BufferOverflowException(getFree(), valueBytes.length);
        byte[] data = fetch();
        for (int i = lastIndex; i < lastIndex + valueBytes.length; i++) {
            data[i] = valueBytes[i - lastIndex];
        }
        stash(data);
        keysToStartAndLength.put(key, new Place(lastIndex, valueBytes.length));
        lastIndex += valueBytes.length;
    }

    @Override
    public T restore(int key) throws BufferKeyNotFoundException, BufferIOException {
        if (!keysToStartAndLength.containsKey(key))
            throw new BufferKeyNotFoundException(key);
        Place startAndLengthValue = keysToStartAndLength.get(key);
        byte[] data = fetch();
        T result = getObjectFromBytes(
                Arrays.copyOfRange(data,
                        startAndLengthValue.getStart(), startAndLengthValue.getLength() + startAndLengthValue.getStart()));
        for (int i = startAndLengthValue.getStart(); i < startAndLengthValue.getStart() + startAndLengthValue.getLength(); i++)
            data[i] = (byte) -1;
        for (int i = startAndLengthValue.getStart() + startAndLengthValue.getLength(); i < lastIndex; i++)
            data[i - startAndLengthValue.getLength()] = data[i];
        for (int i = lastIndex - startAndLengthValue.getLength(); i < lastIndex; i++)
            data[i] = (byte) -1;
        stash(data);
        for (Map.Entry<Integer, Place> keyToStartAndLengthTemp : keysToStartAndLength.entrySet()) {
            if (keyToStartAndLengthTemp.getValue().getStart() > startAndLengthValue.getStart())
                keyToStartAndLengthTemp.setValue(new Place(
                        keyToStartAndLengthTemp.getValue().getStart() - startAndLengthValue.getLength(),
                        keyToStartAndLengthTemp.getValue().getLength()));
        }
        lastIndex -= startAndLengthValue.getLength();
        keysToStartAndLength.remove(key);
        return result;
    }

    abstract byte[] fetch() throws BufferIOException;

    abstract void stash(byte[] data) throws BufferIOException;
}
