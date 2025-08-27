package com.example.pentaho.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlExcetor {

    private final Logger log = LoggerFactory.getLogger(SqlExcetor.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();

    public SqlExcetor(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public <T> List<T> queryForList(String sql, Map<String,Object> params, Class<T> elementType){
        try {
            List<Map<String, Object>> dtos = namedParameterJdbcTemplate.queryForList(sql, params);
            List<T> result = dtos.stream()
                    .map(dto -> {
                        try {
                            return objectMapperUtils.convertValue(dto, elementType);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error mapping DTO", e);
                        }
                    })
                    .collect(Collectors.toList());

            logger(sql,params,result);
            return result;
        }catch (Exception e){
            log.info("e:{}",e);
            throw new RuntimeException("Error occured",e);
        }
    }

    private void logger(String sql, Map<String,Object> params,Object obj){
        log.info(" === sql:{} === ",sql);
        log.info(" === params:{} === ",params);
        log.info(" === result:{} === ",obj);
    }

}
