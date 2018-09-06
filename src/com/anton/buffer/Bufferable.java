package com.anton.buffer;

import com.anton.BufferIOException;
import com.anton.BufferKeyAlreadyExistsException;
import com.anton.BufferKeyNotFoundException;
import com.anton.BufferOverflowException;

import java.io.Serializable;

public interface Bufferable<T extends Serializable, Key extends Comparable<? super Key>> extends AutoCloseable{
    void save(Key key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException;

    T restore(Key key) throws BufferKeyNotFoundException, BufferIOException;

    boolean isContainsKey(Key key);
}
