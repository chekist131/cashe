package com.anton.test.object.unusual;

public class KeyGen1 implements Comparable<KeyGen1> {

    private int key;

    KeyGen1(int key) {
        this.key = key;
    }

    @Override
    public int compareTo(KeyGen1 o) {
        return key - o.key;
    }
}
