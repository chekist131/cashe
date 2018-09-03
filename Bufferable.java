package com.anton;

import com.anton.exceptions.BufferIOException;
import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

public interface Bufferable {

    void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException, BufferIOException;

    String restore(int key) throws BufferKeyNotFoundException, BufferIOException;

    boolean isContainsKey(int key);
}
