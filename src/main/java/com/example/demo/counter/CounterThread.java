package com.example.demo.counter;

import org.springframework.stereotype.Component;

@Component
public class CounterThread extends Thread{

    @Override
    public void start() {
        CounterService.incrementCounter();
    }

}