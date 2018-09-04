package com.anton;

import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        int[] a = {1,2,3,4,5};
        int[] b = Arrays.copyOfRange(a, 2,4);
        for(int x: b){
            System.out.println(x);
        }
    }
}
