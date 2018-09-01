package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

import java.util.*;
import java.util.stream.Collectors;

public class Buffer extends AbstractBuffer {

    private Map<Integer, String> data = new HashMap<>();

    private int size;
    private int used;

    public Buffer(int bufferSize){
        this.size = bufferSize;
    }

    public int getSize(){
        return size;
    }

    public int getUsed() {
        return used;
    }

    @Override
    protected boolean isContainsKey(int key) {
        return data.containsKey(key);
    }

    @Override
    protected Set<Map.Entry<Integer, String>> getExtraValues(int minBytes){
        Set<Map.Entry<Integer, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        final int necessaryBytes = minBytes - getFree();
        int byteCounter = 0;
        List<Map.Entry<Integer, String>> sortedExtra = data.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toList());
        for(Map.Entry<Integer, String> e: sortedExtra){
            extra.add(e);
            byteCounter += e.getValue().length();
            if (byteCounter >= necessaryBytes)
                break;
        }
        return extra;
    }

    @Override
    protected Set<Map.Entry<Integer, String>> getValuableValues(final int freeBytes){
        Set<Map.Entry<Integer, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        int byteCounter = 0;
        List<Map.Entry<Integer, String>> sortedExtra = data.entrySet().stream()
                .sorted(Comparator.comparing(entry -> ((Map.Entry<Integer, String>)entry).getValue()).reversed())
                .collect(Collectors.toList());
        for(Map.Entry<Integer, String> e: sortedExtra){
            byteCounter += e.getValue().length();
            if (byteCounter > freeBytes)
                break;
            extra.add(e);
        }
        return extra;
    }

    @Override
    public void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException {
        if (data.containsKey(key))
            throw new BufferKeyAlreadyExistsException();
        if (getFree() < o.length())
            throw new BufferOverflowException(getFree(), o.length());
        used += o.length();
        data.put(key, o);
    }

    @Override
    public String restore(int key) throws BufferKeyNotFoundException {
        String result = data.get(key);
        if (result == null)
            throw new BufferKeyNotFoundException(key);
        data.remove(key);
        used -= result.length();
        return result;
    }

}
