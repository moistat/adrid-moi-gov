package com.example.pentaho.component;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleQueryDTO {

    private String redisKey;

    private String redisValue;

    private List<String> redisValueList;

    private String originalAddress;

    private String county;

    private String town;


    public SingleQueryDTO() {
    }

    public SingleQueryDTO(String originalAddress, String county, String town) {
        this.originalAddress = originalAddress;
        this.county = county;
        this.town = town;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }


    public String getRedisValue() {
        return redisValue;
    }

    public void setRedisValue(String redisValue) {
        this.redisValue = redisValue;
    }

    public List<String> getRedisValueList() {
        return redisValueList;
    }

    public void setRedisValueList(List<String> redisValueList) {
        this.redisValueList = redisValueList;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }


    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "SingleQueryDTO{" +
                "redisKey='" + redisKey + '\'' +
                ", redisValue='" + redisValue + '\'' +
                ", redisValueList=" + redisValueList +
                ", originalAddress='" + originalAddress + '\'' +
                ", county='" + county + '\'' +
                ", town='" + town + '\'' +
                '}';
    }
}
