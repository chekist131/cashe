package com.anton;

import com.anton.exceptions.BufferIOException;
import com.anton.strateges.BufferComparator;

public class BufferFactory {

    public static BufferEmulator getBufferEmulator(int bufferSize, BufferComparator comparator){
        return new BufferEmulator(bufferSize, comparator);
    }

    public static ArrayBuffer getContinuousBuffer(int bufferSize, BufferComparator comparator){
        return new ArrayBuffer(bufferSize, comparator);
    }

    public static FileBuffer getFileBuffer(int bufferSize, BufferComparator comparator, String fileName) throws BufferIOException {
        return new FileBuffer(bufferSize, comparator, fileName);
    }
}
