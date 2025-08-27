package com.example.pentaho.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class JoinStepDesUtil {

    @Autowired
    private ResourceLoader resourceLoader;

    private final static  ObjectMapper objectMapper = new ObjectMapper();

    public  String readAsString(String filePath) {
        try {
            InputStream inputStream = resourceLoader.getResource(filePath).getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String readLine;
            StringBuilder fileContent = new StringBuilder();
            while ((readLine = bf.readLine()) != null) {
                fileContent.append(readLine);
            }
            bf.close();
            return fileContent.toString();
        } catch (Exception e) {
            return "";
        }
    }


    public String getJoinStepDes(String joinStep) throws IOException {
        if(StringUtils.isNullOrEmpty(joinStep)){
            return joinStep;
        }
        String content = readAsString("classpath:joinstep-mapping.json");
        JsonNode jsonNode = objectMapper.readTree(content);
        return StringUtils.isNullOrEmpty(jsonNode.get(joinStep.substring(0,3)).asText()) ? joinStep : joinStep+":"+jsonNode.get(joinStep.substring(0,3)).asText();
    }
}

