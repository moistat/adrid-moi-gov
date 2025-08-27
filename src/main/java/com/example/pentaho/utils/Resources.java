package com.example.pentaho.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class Resources {

    private final  ResourceLoader resourceLoader;

    private final ObjectMapper objectMapper;


    public Resources(ResourceLoader resourceLoader,ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }


    public  String readAsString(String location){
        Resource resource = resourceLoader.getResource(location);
        try(
                InputStream inputStream = resource.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ){
            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        }catch (IOException e) {
            throw new ResourceReadingException(e);
        }
    }


//    public Query.Builder readAsQueryBuilder(String location, Object... parameters) {
//        String string = readAsString(location);
//        return Query.builder(string, parameters);
//    }

//    public Query readAsQuery(String location, Object... parameters) {
//        return readAsQueryBuilder(location, parameters).build();
//    }

    public <T> T readAsObject(String location, Class<T> valueType){
        String string = readAsString(location);
        try {
           return (T) objectMapper.readValue(string, valueType);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode readAsJsonNode(String location){
        String string = readAsString(location);
        try {
           return objectMapper.readTree(string);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    public static class ResourceReadingException extends RuntimeException  {

        public ResourceReadingException(Throwable cause) {
            super(cause);
        }
        public ResourceReadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
