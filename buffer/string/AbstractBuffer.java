package com.anton.buffer.string;

import com.anton.buffer.object.AbstractBufferObject;

public interface AbstractBuffer extends AbstractBufferObject<String>, Bufferable, AutoCloseable {

    default int getCountOfElements(String o) {
        return o.getBytes().length;
    }

    default Object getElements(String o){
        return o.getBytes();
    }

//    default String getObjectFromBytes(Object chars){
//        return null;// TODO: 9/4/2018
//    }
}
