package com.example.demo.cache;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache {
    private Map<String, String> cacheMap;

    public Cache() {
        cacheMap = new ConcurrentHashMap<>();
    }

    public void put(String key, String value) {
        cacheMap.put(key, value);
    }

    public String get(String key) {
        return cacheMap.get(key);
    }

    public void clear() {
        cacheMap.clear();
    }
}