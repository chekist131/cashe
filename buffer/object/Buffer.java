package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;
import com.anton.buffer.object.strategies.BufferComparator;

import java.io.*;
import java.util.Map;
import java.util.Set;

public interface Buffer<T> extends Bufferable<T>{
    int getSize();

    int getUsed();

    default int getFree(){
        return getSize() - getUsed();
    }

    default void saveSeveral(Set<Map.Entry<Integer, T>> values)
            throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        int bytes = 0;
        for(Map.Entry<Integer, T> entry: values) {
            bytes += getCountOfElements(entry.getValue());
        }
        if (getFree() < bytes)
            throw new BufferOverflowException(getFree(), bytes);
        if (values.stream().map(Map.Entry::getKey).anyMatch(this::isContainsKey))
            throw new BufferKeyAlreadyExistsException();
        for(Map.Entry<Integer, T> entry: values)
            save(entry.getKey(), entry.getValue());
    }

    default void ejectSeveral(Set<Integer> keys) throws BufferKeyNotFoundException, BufferIOException {
        if (!keys.stream().allMatch(this::isContainsKey))
            throw  new BufferKeyNotFoundException();
        for(int key: keys)
            restore(key);
    }

    /**
     * Return less valuable elements from (buffer + new element)
     * @param key adding key
     * @param value adding value
     * @param comparator compare strategy
     * @return extra values
     * @throws BufferIOException
     */
    Set<Map.Entry<Integer, T>> getExtraValues(int key, T value, BufferComparator<T> comparator) throws BufferIOException;

    /**
     * Find most valuable elements to free freeBytes or less count of bytes
     * @param freeBytes maximum count of bytes of valuable values
     * @param comparator compare strategy
     * @return valuable values
     * @throws BufferIOException
     */
    Set<Map.Entry<Integer, T>> getValuableValues(int freeBytes, BufferComparator<T> comparator) throws BufferIOException;

    @Override
    void close();

    default int getCountOfElements(T o) throws BufferIOException {
        int length;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(o);
            objectOutput.flush();
            length = byteArrayOutputStream.toByteArray().length;
            objectOutput.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
        return length;
    }

    default Object getElements(T o) throws BufferIOException {
        byte[] array;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(o);
            objectOutput.flush();
            array = byteArrayOutputStream.toByteArray();
            objectOutput.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
        return array;
    }

    default T getObjectFromBytes(Object bytes) throws BufferIOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((byte[])bytes);
        T object;
        try{
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
            Object o = objectInput.readObject();
            object = (T)o;
            objectInput.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new BufferIOException(e);
        }
        return object;
    }
}
