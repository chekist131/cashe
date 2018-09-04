package com.anton.buffer.string;

import com.anton.buffer.object.BufferEmulatorObject;

public class BufferEmulator extends BufferEmulatorObject<String> implements AbstractBuffer {

    public BufferEmulator(int bufferSize) {
        super(bufferSize);
    }
}
