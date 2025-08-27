package com.example.pentaho.service;

import com.example.pentaho.component.Address;
import com.example.pentaho.component.IbdTbAddrCodeOfDataStandardDTO;
import com.example.pentaho.utils.NullFormatter;
import com.example.pentaho.utils.NumberParser;
import com.google.common.base.Strings;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompareService {

    private static final Set<String> EXCLUDED_JOIN_STEPS = Set.of("JE621", "JD721", "JE431", "JE421", "JE511");

    public List<IbdTbAddrCodeOfDataStandardDTO> filterByVillageCd(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        List<String> villageCds = Arrays.asList(address.getVillageCd().split(","));
        List<IbdTbAddrCodeOfDataStandardDTO> mapList = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> unMapList = new ArrayList<>();
        resultList.forEach(m -> {
            try {
                NullFormatter.formatNullValue(m, IbdTbAddrCodeOfDataStandardDTO::getVillageCd, IbdTbAddrCodeOfDataStandardDTO::setVillageCd, () -> "000");
                if (villageCds.contains(m.getVillageCd())) {
                    mapList.add(m);
                } else {
                    unMapList.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String replacmentForVillage = address.getSegmentExistNumber();
        if (!mapList.isEmpty()) {
            resultList = new ArrayList<>(mapList);
        } else {
            replacmentForVillage = address.getSegmentExistNumber().substring(0, 2) + "0" + address.getSegmentExistNumber().substring(3, address.getSegmentExistNumber().length());
            address.setSegmentExistNumber(replacmentForVillage);
            resultList = new ArrayList<>(unMapList);
        }
        return resultList;
    }


    public List<IbdTbAddrCodeOfDataStandardDTO> filterByVillage(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        List<IbdTbAddrCodeOfDataStandardDTO> mapList = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> unMapList = new ArrayList<>();
        List<String> dtoVillages = resultList.stream().filter(x -> !Strings.isNullOrEmpty(x.getVillage())).map(x -> x.getVillage()).collect(Collectors.toList());
        String targetVillage = findTheBestOne(address.getVillage(), dtoVillages);
        resultList.forEach(m -> {
            if (targetVillage.equals(m.getVillage())) {
                mapList.add(m);
            } else {
                unMapList.add(m);
            }
        });

        String replacmentForVillage = address.getSegmentExistNumber();
        if (!mapList.isEmpty()) {
            resultList = new ArrayList<>(mapList);
        } else {
            replacmentForVillage = address.getSegmentExistNumber().substring(0, 2) + "0" + address.getSegmentExistNumber().substring(3, address.getSegmentExistNumber().length());
            address.setSegmentExistNumber(replacmentForVillage);
            resultList = new ArrayList<>(unMapList);
        }
        return resultList;
    }

    public List<IbdTbAddrCodeOfDataStandardDTO> filterByNeighbor(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        List<IbdTbAddrCodeOfDataStandardDTO> mapList = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> unMapList = new ArrayList<>();
        resultList.forEach(e -> {
            NullFormatter.formatNullValue(e, IbdTbAddrCodeOfDataStandardDTO::getNeighbor, IbdTbAddrCodeOfDataStandardDTO::setNeighbor, () -> "");
            NullFormatter.formatNullValue(e, IbdTbAddrCodeOfDataStandardDTO::getNeighborCd, IbdTbAddrCodeOfDataStandardDTO::setNeighborCd, () -> "000");
            if (e.getNeighborCd().equals(address.getNeighborCd())) {
                mapList.add(e);
            } else {
                unMapList.add(e);
            }
        });

        String replacmentForNeighbor = address.getSegmentExistNumber();
        if (!mapList.isEmpty()) {
            address.setSegmentExistNumber(replacmentForNeighbor);
            resultList = new ArrayList<>(mapList);
        } else {
            replacmentForNeighbor = address.getSegmentExistNumber().substring(0, 8) + "0" + address.getSegmentExistNumber().substring(9, 10);
            address.setSegmentExistNumber(replacmentForNeighbor);
            resultList = new ArrayList<>(unMapList);
        }
        return resultList;
    }


    public Map<String, String> findTargetNumFlrPosAndNumFlrId(Address address, String redisMappingJoinStep, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        Set<String> possibleJoinStep = new HashSet<String>();
        possibleJoinStep.add(redisMappingJoinStep);

        Map<String, String[]> floorMapping = Map.of(
                "JC4", new String[]{address.getJC4NumFlrPos(), address.getJC4NumFlrId()},
                "JB5", new String[]{address.getJB5NumFlrPos(), address.getJB5NumFlrId()},
                "JB4", new String[]{address.getJB4NumFlrPos(), address.getJB4NumFlrId()},
                "JB3", new String[]{address.getJB3NumFlrPos(), address.getJB3NumFlrId()},
                "JB2", new String[]{address.getJB2NumFlrPos(), address.getJB2NumFlrId()}
        );

        for (IbdTbAddrCodeOfDataStandardDTO dto : resultList) {
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getNumFlrPos, IbdTbAddrCodeOfDataStandardDTO::setNumFlrPos, () -> "00000");
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getNumFlrId, IbdTbAddrCodeOfDataStandardDTO::setNumFlrId, () -> "0000000000000000000");


            if ((dto.getNumFlrPos() + dto.getNumFlrId()).equals(address.getNumFlrPos() + address.getNumFlrId())) {
                possibleJoinStep.clear();
                possibleJoinStep.add(redisMappingJoinStep);
                break;
            }

            floorMapping.forEach((key, value) -> {
                if (dto.getNumFlrPos().equals(value[0])) {
                    possibleJoinStep.add(renewJoinStep(key, redisMappingJoinStep));
                }
            });
        }

        List<String> sortedJoinStepList = possibleJoinStep.stream().sorted().toList();
        String targetJoinStep = sortedJoinStepList.get(sortedJoinStepList.size() - 1);
        address.setJoinStep(targetJoinStep);

        Map<String, String> targetMap = new HashMap() {{
            put("tagetNumFlrPos", address.getNumFlrPos());
            put("tagetNumFlrId", address.getNumFlrId());
        }};

        if (redisMappingJoinStep.equals(targetJoinStep)) {
            return targetMap;
        }

        floorMapping.forEach((key, value) -> {
            if (targetJoinStep.contains(key)) {
                targetMap.put("tagetNumFlrPos", value[0]);
                targetMap.put("tagetNumFlrId", value[1]);
            }
        });

        return targetMap;
    }

    public List<IbdTbAddrCodeOfDataStandardDTO> filterByNumFlrPosAndNumFlrId(Address address, String redisMappingJoinStep, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        List<IbdTbAddrCodeOfDataStandardDTO> mapList = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> unMapList = new ArrayList<>();
        Map<String, String> targetMap = findTargetNumFlrPosAndNumFlrId(address, redisMappingJoinStep, resultList);
        String targetNumFlrPos = targetMap.get("tagetNumFlrPos");
        String tagetNumFlrId = targetMap.get("tagetNumFlrId");
        String target = targetNumFlrPos + tagetNumFlrId;
        if (address.getJoinStep().contains("JB4")) {
            String firstSixNum = tagetNumFlrId.substring(0, 6);
            HashMap<String, String> JB4SpecialFilter = new HashMap<>() {{
                put("10000", "12000" + firstSixNum + "0000100000000");
                put("12000", "10000" + firstSixNum + "0000000000000");
            }};
            target = JB4SpecialFilter.containsKey(address.getNumFlrPos()) ? JB4SpecialFilter.get(address.getNumFlrPos()) : tagetNumFlrId + targetNumFlrPos;
        }
        for (IbdTbAddrCodeOfDataStandardDTO dto : resultList) {
            if ((dto.getNumFlrPos() + dto.getNumFlrId()).equals(target)) {
                mapList.add(dto);
            } else {
                unMapList.add(dto);
            }
        }

        if (!mapList.isEmpty()) {
            resultList = new ArrayList<>(mapList);
        } else {
            resultList = new ArrayList<>(unMapList);
        }
        return resultList;
    }


    String renewJoinStep(String startCode, String oldJoinStep) {
        String endCode = oldJoinStep.substring(3, 5);
        String result = startCode + endCode;
        return result;
    }

    public List<IbdTbAddrCodeOfDataStandardDTO> filterByRoomIdSn(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        List<IbdTbAddrCodeOfDataStandardDTO> mapList = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> unMapList = new ArrayList<>();

        resultList.forEach(dto -> {
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getRoomIdSn, IbdTbAddrCodeOfDataStandardDTO::setRoomIdSn, () -> "00000");
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getRoom, IbdTbAddrCodeOfDataStandardDTO::setRoom, () -> "");
            if (dto.getRoomIdSn().equals(address.getRoomIdSn())) {
                mapList.add(dto);
            } else {
                unMapList.add(dto);
            }
        });

        if (!mapList.isEmpty()) {
            resultList = mapList;
        } else {
            resultList = unMapList;
        }
        return resultList;
    }


    public List<IbdTbAddrCodeOfDataStandardDTO> filterByRoadAreaSn(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        List<IbdTbAddrCodeOfDataStandardDTO> mapList = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> unMapList = new ArrayList<>();
        resultList.forEach(e -> {
            NullFormatter.formatNullValue(e, IbdTbAddrCodeOfDataStandardDTO::getRoadAreaSn, IbdTbAddrCodeOfDataStandardDTO::setRoadAreaSn, () -> "0000000");
            if (e.getRoadAreaSn().equals(address.getRoadAreaSn())) {
                mapList.add(e);
            } else {
                unMapList.add(e);
            }
        });

        if (!mapList.isEmpty()) {
            resultList = mapList;
        } else {
            resultList = unMapList;
        }
        return resultList;
    }

    public void renewSegNumberForRoanArea(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        for (IbdTbAddrCodeOfDataStandardDTO dto : resultList) {
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getRoad, IbdTbAddrCodeOfDataStandardDTO::setRoad, () -> "");
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getArea, IbdTbAddrCodeOfDataStandardDTO::setArea, () -> "");

            address.setRoad(NumberParser.replaceWithChineseNumber(address.getRoad()));
            List<String> roadAreaSns = Arrays.asList(address.getRoadAreaSn().split(","));

            if (roadAreaSns.contains(dto.getRoadAreaSn())) {
                if (dto.getArea().equals(address.getArea())) {
                    address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 4) + "1" + address.getSegmentExistNumber().substring(5, 10));
                } else {
                    address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 4) + "2" + address.getSegmentExistNumber().substring(5, 10));
                }

                if (dto.getRoad().equals(address.getRoad())) {
                    address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 3) + "1" + address.getSegmentExistNumber().substring(4, 10));
                } else {
                    address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 3) + "2" + address.getSegmentExistNumber().substring(4, 10));
                }
            } else {
                address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 4) + "0" + address.getSegmentExistNumber().substring(5, 10));
                address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 3) + "0" + address.getSegmentExistNumber().substring(4, 10));
            }
        }
    }


    public void renewSegNumberForRoom(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        for (IbdTbAddrCodeOfDataStandardDTO dto : resultList) {
            NullFormatter.formatNullValue(dto, IbdTbAddrCodeOfDataStandardDTO::getRoom, IbdTbAddrCodeOfDataStandardDTO::setRoom, () -> "");

            address.setRoom(NumberParser.replaceWithFullWidthNumber(address.getRoom()));
            List<String> roomIdSns = Arrays.asList(address.getRoomIdSn().split(","));

            if (roomIdSns.contains(dto.getRoomIdSn())) {
                if (dto.getRoom().equals(address.getRoom())) {
                    address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 9) + "1");
                } else {
                    address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 9) + "0");
                }
            } else {
                address.setSegmentExistNumber(address.getSegmentExistNumber().substring(0, 9) + "0");
            }
        }
    }

    private String checkJoinStep(Address address) {
        String redisJoinStep = address.getJoinStep();
        char[] segArray = address.getSegmentExistNumber().toCharArray();

        String[] joinSteps = new String[]{"JA1", "JA2", "JA3", "JB1", "JB2", "JB3", "JB4", "JB5", "JC1", "JC2", "JC3", "JC4"};
        Map<String, Integer> joins = new HashMap<>() {{
            put("JA2", 8);
            put("JA3", 2);
            put("JB1", 9);
            put("JC2", 3);
            put("JC3", 4);
        }};

        String schema = redisJoinStep.substring(0, 3);
        int startAt = 0;
        for (int i = 0; i < joinSteps.length; i++) {
            if (joinSteps[i].equals(schema)) {
                startAt = i;
                break;
            }
        }

        String revisedJoinStep = redisJoinStep;
        for (int i = startAt; i < joinSteps.length; i++) {
            if (joins.get(joinSteps[i]) == null) {
                continue;
            }
            if (String.valueOf(segArray[joins.get(joinSteps[i])]).equals("0")) {
                revisedJoinStep = joinSteps[i];
                if (revisedJoinStep.indexOf("JC3") >= 0) {
                    if (String.valueOf(segArray[4]).equals("1")) {
                        revisedJoinStep = renewJoinStep("JC2", revisedJoinStep);
                    }
                }

                if (revisedJoinStep.indexOf("JC2") >= 0) {
                    if (String.valueOf(segArray[3]).equals("1")) {
                        revisedJoinStep = renewJoinStep("JB1", revisedJoinStep);
                    }
                }


                if (revisedJoinStep.indexOf("JB1") >= 0) {
                    if (String.valueOf(segArray[9]).equals("1")) {
                        revisedJoinStep = renewJoinStep("JA3", revisedJoinStep);
                    }
                }

                if (revisedJoinStep.indexOf("JA3") >= 0) {
                    if (String.valueOf(segArray[2]).equals("1")) {
                        revisedJoinStep = renewJoinStep("JA2", revisedJoinStep);
                    }
                }


                if (revisedJoinStep.indexOf("JA2") >= 0) {
                    if (String.valueOf(segArray[2]).equals("0")) {
                        revisedJoinStep = renewJoinStep("JA3", revisedJoinStep);
                    }

                    if (String.valueOf(segArray[8]).equals("1")) {
                        revisedJoinStep = renewJoinStep("JA1", revisedJoinStep);
                    }
                }

                if (revisedJoinStep.indexOf("JA1") >= 0) {
                    revisedJoinStep = renewJoinStep("JA1", revisedJoinStep);
                }
            }
        }

        if (revisedJoinStep.indexOf("JA3") >= 0) {
            if (String.valueOf(segArray[2]).equals("1")) {
                revisedJoinStep = renewJoinStep("JA2", revisedJoinStep);
            }
        }


        if (revisedJoinStep.indexOf("JA2") >= 0) {
            if (String.valueOf(segArray[2]).equals("0")) {
                revisedJoinStep = renewJoinStep("JA3", revisedJoinStep);
            }

            if (String.valueOf(segArray[8]).equals("1")) {
                revisedJoinStep = renewJoinStep("JA1", revisedJoinStep);
            }
        }
        return revisedJoinStep;
    }


    private String findTheBestOne(String model, List<String> targets) {
        List<String> bestMatches = new ArrayList<>();
        JaroWinklerDistance distance = new JaroWinklerDistance();
        double highestScore = 0.0;
        for (String target : targets) {
            double score = distance.apply(model, target);
            if (score > highestScore) {
                highestScore = score;
                bestMatches.clear();
                bestMatches.add(target);
            } else if (score == highestScore) {
                bestMatches.add(target);
            }
        }
        return bestMatches.isEmpty() ? "" : bestMatches.get(0);
    }
}
