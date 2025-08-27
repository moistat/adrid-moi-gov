package com.example.pentaho.service;

import com.example.pentaho.component.*;
import com.example.pentaho.utils.StringUtils;
import com.google.common.base.Strings;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisService {


    @Autowired
    @Qualifier("InMemoryRedisDB0")
    private InMemoryRedisDB InMemoryRedisDB0;

    @Autowired
    @Qualifier("InMemoryRedisDB1")
    private InMemoryRedisDB InMemoryRedisDB1;

    public List<String> findListByKey(String key) {
        AbstractInMemoryRedis.ListOperation listOps = InMemoryRedisDB0.opsForList();
        List<String> elements = listOps.range(key, 0, -1);
        return elements;
    }

    public List<String> findSetsByKeys(List<String> keys) {
        List<String> resultList = new ArrayList<>();
        List<Set<String>> results = InMemoryRedisDB0.executePipelined(keys);
        for (Object result : results) {
            if (result instanceof Set) {
                Set<String> elements = (Set<String>) result;
                resultList.addAll(elements);
            }
        }
        return resultList;
    }


    public Map<String, Set<String>> findMapsByKeys(Address address) {
        List<String> keys = address.getMappingId();

        Map<String, Set<String>> resultList = new HashMap<>();

        List<Set<String>> results = InMemoryRedisDB1.executePipelined(keys);
        System.out.println("results "+results);

        int index = 0;
        for (Object result : results) {
            if (result instanceof Set) {
                Set<String> elements = (Set<String>) result;
                if ( elements!=null && !elements.isEmpty()) {
                    resultList.put(keys.get(index), elements);
                }
            }
            index++;
        }
        System.out.println("resultList "+resultList);
        return resultList;
    }


    public Map<String, Set<String>> fuzzyWithoutVillageAndNeighborAndRoom(Address address) {
        List<String> keys = address.getMappingId();

        Map<String, Set<String>> noVillageAndNeighborList = new HashMap<>();

        /**返回*/
        Map<String, Set<String>> resultList = new HashMap<>();

        List<Set<String>> results = InMemoryRedisDB0.executePipelined(keys);


        int index = 0;
        for (Object result : results) {
            if (result instanceof Set) {
                Set<String> elements = (Set<String>) result;
                if (!elements.isEmpty()) {
                    noVillageAndNeighborList.put(keys.get(index), elements);
                }
            }
            index++;
        }

        resultList = noVillageAndNeighborList;

        return resultList;
    }

    /**
     * set單一個值 (redis: set)
     */
    public void setData(SingleQueryDTO singleQueryDTO) {
        InMemoryRedisDB0.opsForValue().set(singleQueryDTO.getRedisKey(), singleQueryDTO.getRedisValue());
    }


    private static final List<String> KEYWORDS = Arrays.asList(
            "COUNTY", "TOWN", "VILLAGE", "ROAD", "AREA", "LANE", "ALLEY",
            "NUM_FLR_1", "NUM_FLR_2", "NUM_FLR_3", "NUM_FLR_4", "NUM_FLR_5"
    );


    /***
     *  e:org.springframework.data.redis.connection.RedisPipelineException: Pipeline contained one or more invalid commands
     * @param keyMap
     * @param segmentExistNumber
     * @return
     */
    public Map<String, String> findSetByKeys(Map<String, String> keyMap, String segmentExistNumber) {
        Map<String, String> resultMap = new HashMap<>();
        List<String> redisKeys = new ArrayList<>(keyMap.keySet());

        StringBuilder segmentExistNumberBuilder = new StringBuilder(segmentExistNumber);

        try {
            List<Set<String>> results = new ArrayList<>();
            for (String key : redisKeys) {
                System.out.println("key:"+key);
                Set<String> members = InMemoryRedisDB0.sMembers(key);
                System.out.println("members:"+members);
                results.add(members);
            }

            System.out.println("results:"+results);
            for (int i = 0; i < results.size(); i++) {
                Set<String> resultSet = results.get(i);
                String key = redisKeys.get(i);
                if (resultSet != null && !resultSet.isEmpty()) {
                    String redisValue = String.join(",", resultSet);
                    System.out.println("key:"+key);
                    System.out.println("redisValue:"+redisValue);
                    resultMap.put(key, redisValue);
                    if (containsKeyword(key)) {
                        segmentExistNumberBuilder.append("1");
                    }
                } else {
                    if (containsKeyword(key)) {
                        segmentExistNumberBuilder.append("0");
                    }

                    List<String> fuzzyKeys = Arrays.asList("COUNTY", "TOWN", "VILLAGE", "ROADAREA");
                    if (fuzzyKeys.contains(key.split(":")[0])) {
                        String[] parts = key.split(":");
                        Set<String> scanSet = new HashSet<>();
                        if (parts.length == 2 && !"null".equals(parts[1])) {
                            scanSet = scanFuzzyKeysAndReturnSet(key);
                        }
                        if (!scanSet.isEmpty()) {
                            String value = String.join(",", scanSet);
                            resultMap.put(key, value);
                        } else {
                            resultMap.put(key, keyMap.get(key));
                        }
                    } else {
                        resultMap.put(key, keyMap.get(key));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("segmentExistNumber", segmentExistNumberBuilder.toString());
        return resultMap;
    }


    private Boolean containsKeyword(String key) {
        for (String keyword : KEYWORDS) {
            if (key.split(":")[0].equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    public Set<String> scanFuzzyKeysAndReturnSet(String key) {
        Set<String> resultSet = new HashSet<>();
        if (key.split(":")[1] != null) {

            List<String> scanKeys;
            if (key.contains("?")) {
                scanKeys = new ArrayList<>();
                scanKeys.add(key.split(":")[0] + ":*" + key.split(":")[1].replace("?", "*") + "*");
            } else {
                String title = key.split(":")[0] + ":";
                key = key.trim();
                if (!StringUtils.isNullOrEmpty(key) && key.split(":").length >= 2) {
                    scanKeys = fuzzyKeys(title, key.split(":")[1]);
                } else {
                    return resultSet;
                }
            }
            String finalKey = key;
            JaroWinklerDistance distance = new JaroWinklerDistance();
            List<String> bestMatches = new ArrayList<>();
            scanKeys.forEach(k -> {
                double highestScore = 0.0;
                Set<String> members = InMemoryRedisDB0.sMembers(k);
                for (String member : members) {
                    double score = distance.apply(finalKey, member);
                    if (score > highestScore) {
                        highestScore = score;
                        bestMatches.clear();
                        bestMatches.add(member);
                    } else if (score == highestScore) {
                        bestMatches.add(member);
                    }
                }
            });
            resultSet = bestMatches.stream().filter(x -> Strings.isNullOrEmpty(x)).collect(Collectors.toSet());
        }
        return resultSet;
    }

    private List<String> fuzzyKeys(String title, String key) {

        ArrayList<String> keys = new ArrayList<>();
        char[] chars = key.toCharArray();
        if (chars.length == 1) {
            String newKeys = title + "*" + key + "*";
            keys.add(newKeys);
            return keys;
        }
        for (int i = 0; i < chars.length; i++) {
            String newKeys = title;
            if (i == 0) {
                newKeys += "*" + chars[i] + chars[i + 1] + "*";
            } else if (i == (chars.length - 1)) {
                newKeys += "*" + chars[i - 1] + chars[i] + "*";
            } else {
                newKeys += "*" + chars[i] + chars[i + 1] + "*";
            }
            keys.add(newKeys);
        }
        return keys;
    }

}
