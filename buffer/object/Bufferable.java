package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

import java.io.Serializable;

public interface Bufferable<T extends Serializable, Key extends Comparable<? super Key>> extends AutoCloseable{
    void save(Key key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException;

    T restore(Key key) throws BufferKeyNotFoundException, BufferIOException;

    boolean isContainsKey(Key key);
}
