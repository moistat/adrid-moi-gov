package com.example.pentaho.component;


import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRedisDB extends AbstractInMemoryRedis{

    public InMemoryRedisDB(ConcurrentHashMap<String, Object> DB) {
        super(DB);
    }

}
