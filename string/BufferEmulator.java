package com.anton.string;

import com.anton.object.BufferEmulatorObject;
import com.anton.string.AbstractBuffer;

public class BufferEmulator extends BufferEmulatorObject<String> implements AbstractBuffer {

    public BufferEmulator(int bufferSize) {
        super(bufferSize);
    }
}
