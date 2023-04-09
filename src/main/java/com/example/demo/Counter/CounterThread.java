package com.example.demo.Counter;

import org.springframework.stereotype.Component;

@Component
public class CounterThread extends Thread{

    @Override
    public void start() {
        CounterService.incrementCounter();
    }
}