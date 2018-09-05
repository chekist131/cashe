package com.anton.buffer.string;

import com.anton.buffer.object.BufferEmulator;

public class BufferEmulatorText extends BufferEmulator<String> implements BufferText {

    public BufferEmulatorText(int bufferSize) {
        super(bufferSize);
    }
}
