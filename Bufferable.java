package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

public interface Bufferable {

    void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException;

    String restore(int key) throws BufferKeyNotFoundException;

    boolean isContainsKey(int key);
}
