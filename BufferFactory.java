package com.anton;

import com.anton.strateges.BufferComparator;

public class BufferFactory {

    public static BufferEmulator getBufferEmulator(int bufferSize, BufferComparator comparator){
        return new BufferEmulator(bufferSize, comparator);
    }

    public static ContinuousBuffer getContinuousBuffer(int bufferSize, BufferComparator comparator){
        return new ContinuousBuffer(bufferSize, comparator);
    }

    public static FileBuffer getFileBuffer(int bufferSize, BufferComparator comparator, String fileName){
        return new FileBuffer(bufferSize, comparator, fileName);
    }
}
