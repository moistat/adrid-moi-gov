package com.example.pentaho.cofig;


import com.example.pentaho.component.InMemoryRedisDB;
import com.example.pentaho.utils.Resources;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RedisConfig {

    private final static Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    private Resources resources;


    @Bean
    @Qualifier("InMemoryRedisDB0")
    public InMemoryRedisDB inMemoryRedisDB0() throws JsonProcessingException {
        Map<String,Object> DB0Map = (HashMap) resources.readAsObject("classpath:redisData/redis-data.json", ConcurrentHashMap.class).get("DB0");
        log.info(" ===  DB0Map :{} === ",DB0Map);
        ConcurrentHashMap<String,Object> DB0 = new ConcurrentHashMap<>();
        DB0.putAll(DB0Map);
        log.info(" ===  DB0 :{} === ",DB0);
        InMemoryRedisDB inMemoryRedisDB = new InMemoryRedisDB(DB0);
        inMemoryRedisDB.initSetOperation();
        return inMemoryRedisDB;
    }

    @Bean
    @Qualifier("InMemoryRedisDB1")
    public InMemoryRedisDB inMemoryRedisDB1() throws JsonProcessingException {
        Map<String,Object> DB0Map = (HashMap) resources.readAsObject("classpath:redisData/redis-data.json", ConcurrentHashMap.class).get("DB1");
        log.info(" ===  DB1Map :{} === ",DB0Map);
        ConcurrentHashMap<String,Object> DB0 = new ConcurrentHashMap<>();
        DB0.putAll(DB0Map);
        log.info(" ===  DB1 :{} === ",DB0);
        InMemoryRedisDB inMemoryRedisDB = new InMemoryRedisDB(DB0);
        inMemoryRedisDB.initSetOperation();
        return inMemoryRedisDB;
    }
}
