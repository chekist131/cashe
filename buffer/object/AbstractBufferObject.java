package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.strateges.BufferComparator;

import java.io.*;
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

    default int getCountOfElements(T o){
        int length = -100;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(o);
            objectOutput.flush();
            length = byteArrayOutputStream.toByteArray().length;
            objectOutput.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return length;
    }

    default Object getElements(T o){
        byte[] array = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(o);
            objectOutput.flush();
            array = byteArrayOutputStream.toByteArray();
            objectOutput.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    default T getObjectFromBytes(Object bytes){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((byte[])bytes);
        T object = null;
        try{
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
            object = (T)objectInput.readObject();
            objectInput.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }
}
