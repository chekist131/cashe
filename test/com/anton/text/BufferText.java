package com.anton.text;

import com.anton.buffer.Buffer;

public interface BufferText<Key extends Comparable<? super Key>> extends Buffer<String, Key>, BufferableText<Key>{

    default int getCountOfElements(String o) {
        return o.getBytes().length;
    }

    default Object getElements(String o){
        return o.getBytes();
    }

    default String getObjectFromBytes(Object chars){
        return new String((char[]) chars);
    }
}
