package com.example.demo.counter;
public class  CounterService {
    private static int counter = 0;

    public static  void incrementCounter() {
        counter++;
    }

    public static int getCounter() {
        return counter;
    }
}

