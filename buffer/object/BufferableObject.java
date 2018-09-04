package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

public interface BufferableObject<T> {
    void save(int key, T o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException;

    T restore(int key) throws BufferKeyNotFoundException, BufferIOException;

    boolean isContainsKey(int key);
}
