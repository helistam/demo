package com.example.demo.Counter;

public class  CounterService {
    private static int counter = 0;

    public static synchronized void incrementCounter() {
        counter++;
    }

    public static  int getCounter() {
        return counter;
    }
    }
