package com.anton.buffer.text;

import com.anton.buffer.Buffer;

public interface BufferText extends Buffer<String, Integer>, BufferableText{

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
