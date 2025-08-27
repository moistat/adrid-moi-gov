package com.example.pentaho.component;




import com.google.common.base.Joiner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractInMemoryRedis {

    private ConcurrentHashMap<String, Object> DB;

    private ListOperation listOperation = new ListOperation();

    private SetOperations setOperation = new SetOperations();


    public AbstractInMemoryRedis(ConcurrentHashMap<String,Object> DB) {
        this.DB = DB;
    }

    public void initListOperation(){
        Map<String, List<String>> collect = DB.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof Collection<?>)
                .filter(entry -> ((Collection<?>) entry.getValue()).stream().allMatch(item -> item instanceof String))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ((Collection<?>) entry.getValue()).stream().map(String.class::cast).collect(Collectors.toList())
                ));
        listOperation.setOpsForList(collect);
    }

    public void initSetOperation(){
        Map<String, Set<String>> collect = DB.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof List<?>)
                .filter(entry -> ((Collection<?>) entry.getValue()).stream().allMatch(item -> item instanceof String))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ((Collection<?>) entry.getValue()).stream().map(String.class::cast).collect(Collectors.toSet())
                ));
        setOperation.setOpsForSet(collect);
    }

    public void delete(String key){
        DB.remove(key);
    }

    public ListOperation opsForList(){
        return listOperation;
    }

    public List<Set<String>> executePipelined(List<String> keys){
        List<Set<String>> resultList = new ArrayList<>();
            for (String key : keys) {
                // lRange for List ,smember for Set
                resultList.add(sMembers(key));
            }
            return resultList;
    }

    public Set<String> sMembers(String key){
        Set<String> strings = setOperation.getOpsForSet().get(key);
        System.out.println("smember key : "+key+" , reslut :"+strings);
        return strings;
    }

    public SetOperations opsForSet() {
        return setOperation;
    }

    public StringOperation opsForValue(){
        Map<String, String> collect = DB.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof String)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (String) entry.getValue())
                );

        StringOperation stringOperation = new StringOperation(collect);
        return stringOperation;
    }


    public class StringOperation {

        private Map<String, String> opsForValue;

        public StringOperation() {
        }

        public StringOperation(Map<String, String> opsForValue) {
            this.opsForValue = opsForValue;
        }

        public String get(String key){
            return opsForValue.get(key);
        }

        public void set(String key, String value){
            opsForValue.put(key,value);
        }
    }

    public class ListOperation {

        private Map<String, List<String>> opsForList;

        public List<String> range(String key,int start,int end){
            List<String> result = new ArrayList<>();
            Set<String> mappingKeys = opsForList.keySet().stream().filter(k -> key.equals(k)).collect(Collectors.toSet());
            if(mappingKeys.isEmpty()){
                return result;
            }

            int index = start;
            for (String mapKey : mappingKeys){
                if(index > end){
                    break;
                }
                String resultStr = Joiner.on(",").join(opsForList.get(mapKey));
//                String resultStr = Strings.join(",", opsForList.get(mapKey));
                result.add(resultStr);
                index ++;
            }
            return result;
        }

        public Map<String, List<String>> getOpsForList() {
            return opsForList;
        }

        public void setOpsForList(Map<String, List<String>> opsForList) {
            this.opsForList = opsForList;
        }
    }



    public class SetOperations {

        private  Map<String, Set<String>> opsForSet;


        public SetOperations() {

        }

        public SetOperations(Map<String, Set<String>> opsForSet) {
            this.opsForSet = opsForSet;
        }

        public Set<String> members(String key){
            return opsForSet.get(key);
        }

        public Map<String, Set<String>> getOpsForSet() {
            return opsForSet;
        }

        public void setOpsForSet(Map<String, Set<String>> opsForSet) {
            this.opsForSet = opsForSet;
        }
    }



}
