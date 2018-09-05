package com.anton.buffer.string;

import com.anton.buffer.object.Buffer;

public interface BufferText extends Buffer<String>, BufferableText{

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
