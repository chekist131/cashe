package com.anton.buffer;

import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;

import java.io.*;
import java.util.Map;
import java.util.Set;

public interface Buffer<T extends Serializable, Key extends Comparable<? super Key>> extends Bufferable<T, Key>{
    int getSize();

    int getUsed();

    default int getFree(){
        return getSize() - getUsed();
    }

    default void saveSeveral(Set<Map.Entry<Key, T>> values)
            throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException {
        int bytes = 0;
        for(Map.Entry<Key, T> entry: values) {
            bytes += getCountOfElements(entry.getValue());
        }
        if (getFree() < bytes)
            throw new BufferOverflowException(getFree(), bytes);
        if (values.stream().map(Map.Entry::getKey).anyMatch(this::isContainsKey))
            throw new BufferKeyAlreadyExistsException();
        for(Map.Entry<Key, T> entry: values)
            save(entry.getKey(), entry.getValue());
    }

    default void ejectSeveral(Set<Key> keys) throws BufferKeyNotFoundException, BufferIOException {
        if (!keys.stream().allMatch(this::isContainsKey))
            throw  new BufferKeyNotFoundException();
        for(Key key: keys)
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
    Set<Map.Entry<Key, T>> getExtraValues(Key key, T value, BufferComparator<T, Key> comparator) throws BufferIOException;

    /**
     * Find most valuable elements to free freeBytes or less count of bytes
     * @param freeBytes maximum count of bytes of valuable values
     * @param comparator compare strategy
     * @return valuable values
     * @throws BufferIOException
     */
    Set<Map.Entry<Key, T>> getValuableValues(int freeBytes, BufferComparator<T, Key> comparator) throws BufferIOException;

    @Override
    void close() throws BufferIOException;

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
