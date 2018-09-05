package com.anton.buffer.text;

import com.anton.buffer.BufferEmulator;

public class BufferEmulatorText extends BufferEmulator<String, Integer> implements BufferText {

    public BufferEmulatorText(int bufferSize) {
        super(bufferSize);
    }
}
