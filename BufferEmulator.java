package com.anton;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.util.*;
import java.util.stream.Collectors;

public class BufferEmulator extends AbstractBuffer {

    private Map<Integer, String> data;

    private int size;
    private int used;

    BufferEmulator(int bufferSize, BufferComparator comparator){
        super(comparator);
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
    public boolean isContainsKey(int key) {
        return data.containsKey(key);
    }

    @Override
    protected Set<Map.Entry<Integer, String>> getExtraValues(int key, String value){
        data.put(key, value);
        Set<Map.Entry<Integer, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        final int necessaryBytes = value.length() - getFree();
        int byteCounter = 0;
        List<Map.Entry<Integer, String>> sortedExtra = data.entrySet().stream()
                .sorted(getComparator().reversed())
                .collect(Collectors.toList());
        for(Map.Entry<Integer, String> e: sortedExtra){
            extra.add(e);
            byteCounter += e.getValue().length();
            if (byteCounter >= necessaryBytes)
                break;
        }
        data.remove(key);
        return extra;
    }

    @Override
    protected Set<Map.Entry<Integer, String>> getValuableValues(final int freeBytes){
        Set<Map.Entry<Integer, String>> extra = new TreeSet<>(Comparator.comparing(Map.Entry::getKey));
        int byteCounter = 0;
        List<Map.Entry<Integer, String>> sortedExtra = data.entrySet().stream()
                .sorted(getComparator())
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

    @Override
    public void close(){

    }
}
