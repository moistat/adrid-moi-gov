package com.example.pentaho.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ObjectMapperUtils {

    private static final Logger log = LoggerFactory.getLogger(ObjectMapperUtils.class);
    private final ObjectMapper objectMapper;

    public ObjectMapperUtils() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T mapping(Object obj, Class<T> clazz) throws JsonProcessingException {
        log.info(" model :{} , class :{}", obj, clazz.getName());
        return objectMapper.readValue(objectMapper.writeValueAsString(obj), clazz);
    }

    public <T> T mapping(String objStr, Class<T> clazz) throws JsonProcessingException {
        log.info(" model :{} , class :{}", objStr, clazz.getName());
        return objectMapper.readValue(objStr, clazz);
    }

    public <T> T convertValue(Map<String, Object> map, Class<T> targetType) throws JsonProcessingException {
        return objectMapper.convertValue(map, targetType);
    }
}
