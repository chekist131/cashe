package com.anton.test.unusual;

import java.util.Objects;

public class KeyGen1 implements Comparable<KeyGen1> {

    private String key;

    KeyGen1(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(KeyGen1 o) {
        return key.compareTo(o.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyGen1)) return false;
        KeyGen1 keyGen1 = (KeyGen1) o;
        return Objects.equals(key, keyGen1.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
