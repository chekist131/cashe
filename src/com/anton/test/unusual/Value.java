package com.anton.test.unusual;

import java.io.Serializable;
import java.util.Objects;

public class Value implements Serializable {

    private String s;

    Value(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;
        Value value = (Value) o;
        return Objects.equals(s, value.s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s);
    }
}
