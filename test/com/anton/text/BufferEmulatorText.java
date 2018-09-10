package com.anton.text;

import com.anton.BufferEmulator;

public class BufferEmulatorText<Key extends Comparable<? super Key>>
        extends BufferEmulator<String, Key> implements BufferText<Key> {

    public BufferEmulatorText(int bufferSize) {
        super(bufferSize);
    }
}
