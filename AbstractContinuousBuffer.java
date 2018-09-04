package com.anton;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;
import com.anton.string.AbstractBuffer;

import java.util.*;

public abstract class AbstractContinuousBuffer extends AbstractBuffer {

    private Map<Integer, Place> keysToStartAndLength;
    private int lastIndex;

    AbstractContinuousBuffer() {
        this.lastIndex = 0;
        this.keysToStartAndLength = new TreeMap<>();
    }

    @Override
    public int getUsed(){
        return lastIndex;
    }

    @Override
    public boolean isContainsKey(int key) {
        return keysToStartAndLength.containsKey(key);
    }

    @Override
    public Set<Map.Entry<Integer, String>> getExtraValues(int key, String value, BufferComparator<String> comparator) throws BufferIOException {
        char[] data = fetch();
        SortedSet<Map.Entry<Integer, String>> allData = new TreeSet<>(comparator.reversed());
        for(Map.Entry<Integer, Place> keyToStartAndLength: keysToStartAndLength.entrySet()){
            allData.add(new AbstractMap.SimpleImmutableEntry<>(keyToStartAndLength.getKey(),
                    new String(data, keyToStartAndLength.getValue().getStart(), keyToStartAndLength.getValue().getLength())));
        }
        allData.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
        Set<Map.Entry<Integer, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        final int necessaryBytes = value.length() - getFree();
        int byteCounter = 0;
        for(Map.Entry<Integer, String> e: allData){
            extra.add(e);
            byteCounter += e.getValue().length();
            if (byteCounter >= necessaryBytes)
                break;
        }
        return extra;
    }

    @Override
    public Set<Map.Entry<Integer, String>> getValuableValues(int freeBytes, BufferComparator<String> comparator) throws BufferIOException {
        char[] data = fetch();
        SortedSet<Map.Entry<Integer, String>> allData = new TreeSet<>(comparator);
        for(Map.Entry<Integer, Place> keyToStartAndLength: keysToStartAndLength.entrySet()){
            allData.add(new AbstractMap.SimpleImmutableEntry<>(keyToStartAndLength.getKey(),
                    new String(data, keyToStartAndLength.getValue().getStart(), keyToStartAndLength.getValue().getLength())));
        }
        Set<Map.Entry<Integer, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        int byteCounter = 0;
        for(Map.Entry<Integer, String> e: allData){
            byteCounter += e.getValue().length();
            if (byteCounter > freeBytes)
                break;
            extra.add(e);
        }
        return extra;
    }

    @Override
    public void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
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
    public String restore(int key) throws BufferKeyNotFoundException, BufferIOException {
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
        for(Map.Entry<Integer, Place> keyToStartAndLengthTemp: keysToStartAndLength.entrySet()){
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
