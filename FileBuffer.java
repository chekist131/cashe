package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.io.*;
import java.util.*;

public class FileBuffer extends AbstractBuffer {

    private String fileName;
    private Map<Integer, Place> keysToStartAndLength;
    private int size;
    private int lastIndex;

    public FileBuffer(int bufferSize, BufferComparator comparator, String fileName) {
        super(comparator);
        this.size = bufferSize;
        this.keysToStartAndLength = new TreeMap<>();
        this.lastIndex = 0;
        this.fileName = fileName;
        File f = new File(this.fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSize(){
        return size;
    }

    public int getUsed(){
        return lastIndex;
    }

    @Override
    public boolean isContainsKey(int key) {
        return keysToStartAndLength.containsKey(key);
    }

    @Override
    protected Set<Map.Entry<Integer, String>> getExtraValues(int key, String value) {
        char[] data = new char[size];
        try(Reader reader = new FileReader(fileName)){
            reader.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SortedSet<Map.Entry<Integer, String>> allData = new TreeSet<>(getComparator().reversed());
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
    protected Set<Map.Entry<Integer, String>> getValuableValues(int freeBytes) {
        char[] data = new char[size];
        try(Reader reader = new FileReader(fileName)){
            reader.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SortedSet<Map.Entry<Integer, String>> allData = new TreeSet<>(getComparator());
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
    public void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException {
        if (keysToStartAndLength.containsKey(key))
            throw new BufferKeyAlreadyExistsException();
        if (getFree() < o.length())
            throw new BufferOverflowException(getFree(), o.length());
        char[] data = new char[size];
        try(Reader reader = new FileReader(fileName)){
            reader.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = lastIndex; i < lastIndex + o.length(); i++) {
            data[i] = o.charAt(i - lastIndex);
        }
        try(Writer writer = new FileWriter(fileName)){
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        keysToStartAndLength.put(key, new Place(lastIndex, o.length()));
        lastIndex += o.length();
    }

    @Override
    public String restore(int key) throws BufferKeyNotFoundException {
        if (!keysToStartAndLength.containsKey(key))
            throw new BufferKeyNotFoundException(key);
        Place startAndLengthValue = keysToStartAndLength.get(key);
        String result;
        char[] data = new char[size];
        try(Reader reader = new FileReader(fileName)){
            reader.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = new String(data, startAndLengthValue.getStart(), startAndLengthValue.getLength());
        for (int i = startAndLengthValue.getStart(); i < startAndLengthValue.getStart() + startAndLengthValue.getLength(); i++)
            data[i] = (char) -1;
        for (int i = startAndLengthValue.getStart() + startAndLengthValue.getLength(); i < lastIndex; i++)
            data[i - startAndLengthValue.getLength()] = data[i];
        for (int i = lastIndex - startAndLengthValue.getLength(); i < lastIndex; i++)
            data[i] = (char) -1;
        try(Writer writer = new FileWriter(fileName)){
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
