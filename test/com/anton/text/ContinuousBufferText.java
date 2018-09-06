package com.anton.text;

import com.anton.buffer.IContinuousBuffer;
import com.anton.buffer.Place;
import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;
import com.anton.buffer.BufferComparator;

import java.util.*;

public abstract class ContinuousBufferText<Key extends Comparable<? super Key>>
        extends IContinuousBuffer<String, Key> implements BufferText<Key> {
    @Override
    public Set<Map.Entry<Key, String>> getExtraValues(Key key, String value, BufferComparator<String, Key> comparator)
            throws BufferIOException {
        char[] data = fetch();
        SortedSet<Map.Entry<Key, String>> allData = new TreeSet<>(comparator.reversed());
        for(Map.Entry<Key, Place> keyToStartAndLength: keysToStartAndLength.entrySet()){
            allData.add(
                    new AbstractMap.SimpleImmutableEntry<>(keyToStartAndLength.getKey(),
                    new String(
                            data,
                            keyToStartAndLength.getValue().getStart(),
                            keyToStartAndLength.getValue().getLength())));
        }
        allData.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
        Set<Map.Entry<Key, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        final int necessaryBytes = value.length() - getFree();
        int byteCounter = 0;
        for(Map.Entry<Key, String> e: allData){
            extra.add(e);
            byteCounter += e.getValue().length();
            if (byteCounter >= necessaryBytes)
                break;
        }
        return extra;
    }

    @Override
    public Set<Map.Entry<Key, String>> getValuableValues(int freeBytes, BufferComparator<String, Key> comparator)
            throws BufferIOException {
        char[] data = fetch();
        SortedSet<Map.Entry<Key, String>> allData = new TreeSet<>(comparator);
        for(Map.Entry<Key, Place> keyToStartAndLength: keysToStartAndLength.entrySet()){
            allData.add(
                    new AbstractMap.SimpleImmutableEntry<>(keyToStartAndLength.getKey(),
                    new String(
                            data,
                            keyToStartAndLength.getValue().getStart(),
                            keyToStartAndLength.getValue().getLength())));
        }
        Set<Map.Entry<Key, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        int byteCounter = 0;
        for(Map.Entry<Key, String> e: allData){
            byteCounter += e.getValue().length();
            if (byteCounter > freeBytes)
                break;
            extra.add(e);
        }
        return extra;
    }

    @Override
    public void save(Key key, String o)
            throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        if (keysToStartAndLength.containsKey(key))
            throw new BufferKeyAlreadyExistsException();
        if (getFree() < o.length())
            throw new BufferOverflowException(getFree(), o.length());
        char[] data = fetch();
        for (int i = lastIndex; i < lastIndex + o.length(); i++) {
            data[i] = o.charAt(i - lastIndex);
        }
        stash(data);
        keysToStartAndLength.put(key, new Place(lastIndex, o.length()));
        lastIndex += o.length();
    }

    @Override
    public String restore(Key key) throws BufferKeyNotFoundException, BufferIOException {
        if (!keysToStartAndLength.containsKey(key))
            throw new BufferKeyNotFoundException(key);
        Place startAndLengthValue = keysToStartAndLength.get(key);
        String result;
        char[] data = fetch();
        result = new String(data, startAndLengthValue.getStart(), startAndLengthValue.getLength());
        for (int i = startAndLengthValue.getStart(); i < startAndLengthValue.getStart() + startAndLengthValue.getLength(); i++)
            data[i] = (char) -1;
        for (int i = startAndLengthValue.getStart() + startAndLengthValue.getLength(); i < lastIndex; i++)
            data[i - startAndLengthValue.getLength()] = data[i];
        for (int i = lastIndex - startAndLengthValue.getLength(); i < lastIndex; i++)
            data[i] = (char) -1;
        stash(data);
        for(Map.Entry<Key, Place> keyToStartAndLengthTemp: keysToStartAndLength.entrySet()){
            if (keyToStartAndLengthTemp.getValue().getStart() > startAndLengthValue.getStart())
                keyToStartAndLengthTemp.setValue(new Place(
                        keyToStartAndLengthTemp.getValue().getStart() - startAndLengthValue.getLength(),
                        keyToStartAndLengthTemp.getValue().getLength()));
        }
        lastIndex -= startAndLengthValue.getLength();
        keysToStartAndLength.remove(key);
        return result;
    }

    protected abstract char[] fetch() throws BufferIOException;

    protected abstract void stash(char[] data) throws BufferIOException;
}
