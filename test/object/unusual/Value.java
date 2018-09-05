package com.anton.test.object.unusual;

import java.io.Serializable;

public class Value implements Serializable {

    private String s;

    Value(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }
}
