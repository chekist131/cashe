package com.anton;

import com.anton.exceptions.BufferKeyAlreadyExistsException;
import com.anton.exceptions.BufferKeyNotFoundException;
import com.anton.exceptions.BufferOverflowException;

public interface IBuffer {

    void save(int key, String o) throws BufferOverflowException, BufferKeyAlreadyExistsException;

    String restore(int key) throws BufferKeyNotFoundException;
}
