package com.example.pentaho.service;

import com.example.pentaho.component.*;
import com.example.pentaho.repository.IbdTbAddrCodeOfDataStandardRepository;
import com.example.pentaho.repository.IbdTbIhChangeDoorplateHisRepository;
import com.example.pentaho.utils.*;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.pentaho.utils.NumberParser.*;

@Service
public class SingleQueryService {


    private static final Logger log = LoggerFactory.getLogger(SingleQueryService.class);
    @Autowired
    private AddressParser addressParser;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CompareService compareService;

    @Autowired
    private IbdTbIhChangeDoorplateHisRepository ibdTbIhChangeDoorplateHisRepository;

    @Autowired
    private IbdTbAddrCodeOfDataStandardRepository bdTbAddrCodeOfDataStandardRepository;

    String segmentExistNumber = "";

    private final Resources resources;

    private HashMap jointStepsMappingMap;

    private HashMap JC4Map;
    private HashMap JB5Map;
    private HashMap JB4Map;
    private HashMap JB3Map;
    private HashMap JB2Map;

    private static final Set<String> EXCLUDED_JOIN_STEPS = Set.of("JE621", "JD721", "JE431", "JE421", "JE511");


    public SingleQueryService(Resources resources) {
        this.resources = resources;
        jointStepsMappingMap = resources.readAsObject("classpath:joinstep/joinstep.json", HashMap.class);
        JC4Map = (HashMap<String, String>) jointStepsMappingMap.get("JC4");
        JB5Map = (HashMap<String, String>) jointStepsMappingMap.get("JB5");
        JB4Map = (HashMap<String, String>) jointStepsMappingMap.get("JB4");
        JB3Map = (HashMap<String, String>) jointStepsMappingMap.get("JB3");
        JB2Map = (HashMap<String, String>) jointStepsMappingMap.get("JB2");
    }


    public void removeWhitespace(SingleQueryDTO singleQueryDTO) {
        String result = singleQueryDTO.getOriginalAddress().replaceAll("\\s+", "");
        singleQueryDTO.setOriginalAddress(result);
    }


    public SingleQueryResultDTO findJson(SingleQueryDTO singleQueryDTO) {
        SingleQueryResultDTO result = new SingleQueryResultDTO();

        List<IbdTbAddrCodeOfDataStandardDTO> list = new ArrayList<>();

        List<IbdTbAddrCodeOfDataStandardDTO> resultList = new ArrayList<>();

        removeWhitespace(singleQueryDTO);

        if (checkIfMultiAddress(singleQueryDTO)) {
            result.setText("該地址屬於多重地址");
            return result;
        }

        String cleanAddress = removeRepeatCountyAndTown(singleQueryDTO);

        Address address = parseAddressAndFindMappingId(cleanAddress);

        address = findSeq(address);

        Set<String> seqSet = address.getSeqSet();

        String redisMappingJoinStep = address.getJoinStep();


        if (!seqSet.isEmpty()) {
            list = stardardDataAndDataRepository(address);
            log.debug("seq 撈出資料 :{}", list);
            NullFormatter.formatNullValue(address, Address::getVillageCd, Address::setVillageCd, () -> "000");
            list = compareService.filterByVillageCd(address, list);
            log.debug(" filterByVillageCd :{}", list);

            NullFormatter.formatNullValue(address, Address::getVillage, Address::setVillage, () -> "");
            list = compareService.filterByVillage(address, list);
            log.debug(" filterByVillage :{}", list);

            NullFormatter.formatNullValue(address, Address::getNeighbor, Address::setNeighbor, () -> "");
            NullFormatter.formatNullValue(address, Address::getNeighborCd, Address::setNeighborCd, () -> "000");
            list = compareService.filterByNeighbor(address, list);
            log.debug(" filterByNeighbor :{}", list);

            NullFormatter.formatNullValue(address, Address::getRoadAreaSn, Address::setRoadAreaSn, () -> "0000000");
            list = compareService.filterByRoadAreaSn(address, list);
            log.debug(" filterByRoadAreaSn :{}", list);

            list = compareService.filterByNumFlrPosAndNumFlrId(address, redisMappingJoinStep, list);
            log.debug(" filterByNumFlrPosAndNumFlrId :{}", list);


            NullFormatter.formatNullValue(address, Address::getRoomIdSn, Address::setRoomIdSn, () -> "00000");
            NullFormatter.formatNullValue(address, Address::getRoom, Address::setRoom, () -> "");
            list = compareService.filterByRoomIdSn(address, list);
            log.debug(" filterByRoomIdSn :{}", list);


            NullFormatter.formatNullValue(address, Address::getRoad, Address::setRoad, () -> "");
            NullFormatter.formatNullValue(address, Address::getArea, Address::setArea, () -> "");
            compareService.renewSegNumberForRoanArea(address, list);

            NullFormatter.formatNullValue(address, Address::getRoom, Address::setRoom, () -> "");
            compareService.renewSegNumberForRoom(address, list);

            String revisedJoinStep = checkJoinStepFromFirst(address);
            address.setJoinStep(revisedJoinStep);

            resultList = list;

            resultList = resultList.stream()
                    .peek(dto -> dto.setJoinStep(revisedJoinStep))
                    .collect(Collectors.toMap(
                            IbdTbAddrCodeOfDataStandardDTO::getSeq,
                            dto -> dto,
                            (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            result.setText("查詢結果");
        }
        replaceJoinStepWhenMultiAdress(address, resultList);
        setJoinStepWhenResultIsEmpty(resultList, result, address);
        result.setData(resultList);
        return result;
    }


    private int findStartIndex(String redisJoinStep, String[] joinSteps) {
        String schema = redisJoinStep.substring(0, 3);
        for (int i = 0; i < joinSteps.length; i++) {
            if (joinSteps[i].equals(schema)) {
                return i;
            }
        }
        return 0;
    }


    private String handleJoinStepConditions(String revisedJoinStep, String joinStep, char[] segArray) {
        switch (joinStep) {
            case "JA1":
                if (segArray[8] == '0') {
                    revisedJoinStep = renewJoinStep("JA2", revisedJoinStep);
                }
                break;
            case "JA2":
            case "JA3":
                revisedJoinStep = handleJA2JA3Conditions(revisedJoinStep, segArray);
                break;
            case "JB1":
                if (segArray[3] != '1' || segArray[4] != '1') {
                    revisedJoinStep = renewJoinStep("JC2", revisedJoinStep);
                } else {
                    revisedJoinStep = renewJoinStep("JB1", revisedJoinStep);
                }
                break;
            case "JC2":
                if (segArray[3] == '0' || segArray[4] == '0') {
                    revisedJoinStep = renewJoinStep("JC3", revisedJoinStep);
                }
                break;
            case "JC3":
                if (isFuzzyRoadAreaSn(segArray)) {
                    revisedJoinStep = renewJoinStep("JC2", revisedJoinStep);
                }
                break;
        }
        return revisedJoinStep;
    }

    private String handleJA2JA3Conditions(String revisedJoinStep, char[] segArray) {
        if (segArray[2] == '1') {
            revisedJoinStep = renewJoinStep("JA2", revisedJoinStep);
        } else {
            revisedJoinStep = renewJoinStep("JA3", revisedJoinStep);
        }

        if (revisedJoinStep.contains("JA2") && segArray[8] == '0') {
            revisedJoinStep = renewJoinStep("JA2", revisedJoinStep);
        } else {
            revisedJoinStep = revisedJoinStep;
        }
        return revisedJoinStep;
    }


    private String checkJoinStepFromFirst(Address address) {
        String redisJoinStep = address.getJoinStep();
        char[] segArray = address.getSegmentExistNumber().toCharArray();

        if (EXCLUDED_JOIN_STEPS.contains(redisJoinStep)) {
            return redisJoinStep;
        }

        String[] joinSteps = new String[]{"JA1", "JA2", "JA3", "JB1", "JB2", "JB3", "JB4", "JB5", "JC1", "JC2", "JC3", "JC4"};

        Map<String, Integer> joins = new HashMap<>() {{
            put("JA2", 8);
            put("JA3", 2);
            put("JB1", 9);
            put("JC2", 3);
        }};

        int startAt = findStartIndex(redisJoinStep, joinSteps);

        String revisedJoinStep = redisJoinStep;
        for (int i = startAt; i < joinSteps.length; i++) {
            String joinStep = joinSteps[i];
            Integer position = joins.get(joinStep);

            if (position != null && !String.valueOf(segArray[position]).equals("1")) {
                revisedJoinStep = renewJoinStep(joinStep, revisedJoinStep);
                revisedJoinStep = handleJoinStepConditions(revisedJoinStep, joinStep, segArray);
            }
        }

        return revisedJoinStep;
    }


    public boolean isFuzzyRoadAreaSn(char[] segArray) {
        boolean result = false;
        boolean isFuzzyRoad = segArray[3] == '2' ? true : false;
        boolean isFuzzyArea = segArray[4] == '2' ? true : false;
        if (isFuzzyRoad || isFuzzyArea) {
            result = true;
        }
        return result;

    }

    public List<Map<String, String>> reviseAddress(SingleQueryDTO singleQueryDTO) {
        singleQueryDTO.setOriginalAddress(singleQueryDTO.getOriginalAddress().trim());
        List<Map<String, String>> result = new ArrayList<>();
        List<IbdTbAddrCodeOfDataStandardDTO> resultList = new ArrayList<>();
        HashMap<String, String> msg = new HashMap<>();

        if (checkIfMultiAddress(singleQueryDTO)) {
            msg.put("msg", "該地址屬於多重地址");
            result.add(msg);
            return result;
        }

        String cleanAddress = removeRepeatCountyAndTown(singleQueryDTO);


        cleanAddress = cleanAddress.trim();


        Address address = parseAddressAndFindMappingId(cleanAddress);

        address = findSeq(address);

        Set<String> seqSet = address.getSeqSet();

        String redisJoinStep = address.getJoinStep();
        if (!seqSet.isEmpty()) {
            resultList = stardardDataAndDataRepository(address);

            NullFormatter.formatNullValue(address, Address::getVillageCd, Address::setVillageCd, () -> "000");
            resultList = compareService.filterByVillageCd(address, resultList);

            NullFormatter.formatNullValue(address, Address::getVillage, Address::setVillage, () -> "");
            resultList = compareService.filterByVillage(address, resultList);

            NullFormatter.formatNullValue(address, Address::getNeighbor, Address::setNeighbor, () -> "");
            NullFormatter.formatNullValue(address, Address::getNeighborCd, Address::setNeighborCd, () -> "000");
            resultList = compareService.filterByNeighbor(address, resultList);

            NullFormatter.formatNullValue(address, Address::getRoadAreaSn, Address::setRoadAreaSn, () -> "0000000");
            resultList = compareService.filterByRoadAreaSn(address, resultList);

            resultList = compareService.filterByNumFlrPosAndNumFlrId(address, redisJoinStep, resultList);

            NullFormatter.formatNullValue(address, Address::getRoomIdSn, Address::setRoomIdSn, () -> "00000");
            NullFormatter.formatNullValue(address, Address::getRoom, Address::setRoom, () -> "");
            resultList = compareService.filterByRoomIdSn(address, resultList);


            resultList = resultList.stream()
                    .collect(Collectors.toMap(
                            IbdTbAddrCodeOfDataStandardDTO::getSeq,
                            dto -> dto,
                            (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            Address finalAddress = address;
            resultList.forEach(dto -> {
                Map<String, String> data = extractMistakePart(dto, finalAddress);
                result.add(data);
            });
            return result;
        } else {
            msg.put("msg", "查無地址");
            result.add(msg);
            return result;
        }

    }

    private Map<String, String> extractMistakePart(IbdTbAddrCodeOfDataStandardDTO ibdTbAddrCodeOfDataStandardDTO, Address address) {
        HashMap<String, String> result = new HashMap<>();
        NullFormatter.formatNullValue(address, Address::getRoad, Address::setRoad, () -> "");
        NullFormatter.formatNullValue(address, Address::getArea, Address::setArea, () -> "");
        NullFormatter.formatNullValue(ibdTbAddrCodeOfDataStandardDTO, IbdTbAddrCodeOfDataStandardDTO::getRoad, IbdTbAddrCodeOfDataStandardDTO::setRoad, () -> "");
        NullFormatter.formatNullValue(ibdTbAddrCodeOfDataStandardDTO, IbdTbAddrCodeOfDataStandardDTO::getArea, IbdTbAddrCodeOfDataStandardDTO::setArea, () -> "");

        StringBuilder msg = new StringBuilder();
        if (!address.getRoad().equals(ibdTbAddrCodeOfDataStandardDTO.getRoad())) {
            msg.append("路名錯誤").append(";");
        }
        if (!address.getArea().equals(ibdTbAddrCodeOfDataStandardDTO.getArea())) {
            msg.append("地名錯誤").append(";");
        }
        if (StringUtils.isNullOrEmpty(msg.toString())) {
            msg.append("地址填寫正確");
        }

        result.put("msg", msg.toString());
        result.put("origrinalAddress", address.getOriginalAddress());
        result.put("fullAddress", ibdTbAddrCodeOfDataStandardDTO.getFullAddress());
        return result;
    }

    public Address parseAddressAndFindMappingId(String cleanAddress) {
        Address address = addressParser.parseAddress(cleanAddress, null);
        address.setOriginalAddress(cleanAddress);
        handleNumTypeCd(address);
        handleAddressRemains(address);
        return findCdAndMappingId(address);
    }

    private void handleNumTypeCd(Address address) {
        String defaultNumTypeCd = "95";
        address.setNumTypeCd(defaultNumTypeCd);

    }

    private void handleAddressRemains(Address address) {
        if (StringUtils.isNotNullOrEmpty(address.getAddrRemains()) && StringUtils.isNullOrEmpty(address.getContinuousNum())) {
            String numTypeCd = getNumTypeCd(address);
            if (!"95".equals(numTypeCd)) {
                addressParser.parseAddress(address.getCleanAddress(), address);
            }

            if (Strings.isNullOrEmpty(address.getArea())) {
                addressParser.parseArea(address);
            }
        }
    }


    private static String getNumTypeCd(Address address) {
        String oldAddrRemains = address.getAddrRemains();
        String newAddrRemains = "";
        String numTypeCd;
        if (oldAddrRemains.startsWith("臨") || oldAddrRemains.endsWith("臨")) {
            numTypeCd = "96";
            newAddrRemains = oldAddrRemains.replace("臨", "");
        } else if (oldAddrRemains.startsWith("建") || oldAddrRemains.endsWith("建")) {
            numTypeCd = "97";
            newAddrRemains = oldAddrRemains.replace("建", "");
        } else if (oldAddrRemains.startsWith("特") || oldAddrRemains.endsWith("特")) {
            numTypeCd = "98";
            newAddrRemains = oldAddrRemains.replace("特", "");
        } else if (oldAddrRemains.startsWith("附") || oldAddrRemains.endsWith("附")) {
            numTypeCd = "99";
            newAddrRemains = oldAddrRemains.replace("附", "");
        } else {
            numTypeCd = "95";
            newAddrRemains = oldAddrRemains;
        }
        address.setCleanAddress(address.getCleanAddress().replace(oldAddrRemains, newAddrRemains));
        return numTypeCd;
    }


    Address findSeq(Address address) {
        Set<String> seqSet = new HashSet<>();
        Map<String, Set<String>> resultsBy56 = findMapsByKeys(address);
        if (resultsBy56 != null && !resultsBy56.isEmpty()) {
            seqSet = mappingCountyAndTown(address, resultsBy56);
        } else {
            build56MappingIdsWithoutVillageNeighborRoom(address);
            Map<String, Set<String>> resultsBy50 = fuzzyWithoutVillageAndNeighborAndRoom(address);
            if (resultsBy50.isEmpty() || resultsBy50 == null) {
                address.setSeqSet(seqSet);
                return address;
            }

            seqSet = filterCountyAndTown(address, resultsBy50);
        }
        address.setSeqSet(seqSet);
        return address;
    }

    public Set<String> filterCountyAndTown(Address address, Map<String, Set<String>> resultMap) {
        Set<String> seqSet = new HashSet<>();
        Set<String> joinStepSet = new HashSet<>();
        String[] countys = address.getCountyCd().split(",");
        String[] towns = address.getTownCd().split(",");
        Set<String> countyTowns = new HashSet<String>();
        for (String county : countys) {
            for (String town : towns) {
                countyTowns.add(county + town);
            }
        }


        resultMap.keySet().forEach(key -> {
            if (!resultMap.get(key).isEmpty() && resultMap.get(key) != null) {
                resultMap.get(key).forEach(str -> {
                    String[] split = str.split(":");
                    String addressCd = split[0];
                    String seq = split[2];
                    String joinStep = split[1];
                    countyTowns.forEach(countyAndTown -> {
                        if (countyAndTown.equals(addressCd)) {
                            seqSet.add(seq);
                            joinStepSet.add(joinStep);
                        }
                    });
                });
            }
        });

        List<String> sortedJoinStepList = joinStepSet.stream().sorted().toList();
        if (!joinStepSet.isEmpty()) {
            address.setJoinStep(sortedJoinStepList.get(0));
        }

        return seqSet;
    }

    private void replaceJoinStepWhenMultiAdress(Address address, List<IbdTbAddrCodeOfDataStandardDTO> resultList) {
        if (address.getJoinStep() != null && resultList.size() > 1) {

            switch (address.getJoinStep()) {
                case "JA211", "JA311", "JA212", "JA312" -> address.setJoinStep("JD111");
                case "JB111", "JB112" -> address.setJoinStep("JD311");
                case "JB311" -> address.setJoinStep("JD411");
                case "JB312" -> address.setJoinStep("JD412");
                case "JB411" -> address.setJoinStep("JD511");
                case "JB412" -> address.setJoinStep("JD512");

            }
            resultList.forEach(ele -> {
                ele.setJoinStep(address.getJoinStep());
            });
        }
    }

    Map<String, Set<String>> findMapsByKeys(Address address) {
        return redisService.findMapsByKeys(address);
    }


    Map<String, Set<String>> fuzzyWithoutVillageAndNeighborAndRoom(Address address) {
        return redisService.fuzzyWithoutVillageAndNeighborAndRoom(address);
    }

    private List splitCdStr(String cdStr) {
        if (cdStr.indexOf(",") >= 0) {
            return Arrays.stream(cdStr.split(",")).toList();
        }
        return Arrays.asList(cdStr);
    }

    Set<String> mappingCountyAndTown(Address address, Map<String, Set<String>> resultsBeforeSplit) {
        Set<String> joinStepSet = new HashSet<>();
        Set<String> seqSet = new HashSet<>();
        if (resultsBeforeSplit != null && !resultsBeforeSplit.isEmpty()) {
            List countyCds = splitCdStr(address.getCountyCd());
            List townCds = splitCdStr(address.getTownCd());
            ArrayList<String> countyTownCds = new ArrayList<>();
            countyCds.forEach(countyCd -> {
                townCds.forEach(townCd -> {
                    countyTownCds.add(String.valueOf(countyCd) + townCd);
                });
            });

            if (!resultsBeforeSplit.keySet().isEmpty()) {
                for (String mappingId : resultsBeforeSplit.keySet()) {
                    if (resultsBeforeSplit.get(mappingId) != null && resultsBeforeSplit.get(mappingId).size() > 0) {
                        for (String seqsStr : resultsBeforeSplit.get(String.valueOf(mappingId))) {
                            String[] seqArray = seqsStr.split(":");
                            String addressCd = seqArray[0];

                            if (countyTownCds.contains(addressCd)) {
                                String joinStep = seqArray[1];
                                joinStepSet.add(joinStep);
                                String seq = seqArray[2];
                                seqSet.add(seq);
                            }
                        }
                    }
                }
            }
            if (!seqSet.isEmpty()) {
                List<String> sortedJoinStepList = joinStepSet.stream().sorted().toList();
                address.setJoinStep(sortedJoinStepList.get(0));
                if ("JC211".equals(address.getJoinStep()) && StringUtils.isNullOrEmpty(address.getArea())) {
                    address.setJoinStep("JC311");
                }
            }
        }
        return seqSet;
    }


    public Address findCdAndMappingId(Address address) {
        segmentExistNumber = "";
        String county = address.getCounty();
        String town = address.getTown();
        String village = address.getVillage();
        String road = address.getRoad();
        String area = address.getArea();
        String roadAreaKey = replaceWithHalfWidthNumber(road) + (area == null ? "" : area);
        String lane = address.getLane();
        String alley = address.getAlley();
        String subAlley = address.getSubAlley();
        String alleyIdSnKey = replaceWithHalfWidthNumber(alley) + replaceWithHalfWidthNumber(subAlley);
        String numTypeCd = address.getNumTypeCd();
        if (StringUtils.isNotNullOrEmpty(address.getContinuousNum())) {
            formatCoutinuousFlrNum(address.getContinuousNum(), address);
        }

        addressParser.extractRoom(address);

        String numFlr1 = address.getNumFlr1();
        String numFlr2 = address.getNumFlr2();
        String numFlr3 = address.getNumFlr3();
        String numFlr4 = address.getNumFlr4();
        String numFlr5 = address.getNumFlr5();

        String room = address.getRoom();

        Map<String, String> keyMap = new LinkedHashMap<>();
        keyMap.put("COUNTY:" + county, "00000");
        keyMap.put("TOWN:" + town, "000");
        keyMap.put("VILLAGE:" + village, "000");
        keyMap.put("ROADAREA:" + roadAreaKey, "0000000");
        keyMap.put("ROAD:" + road, "");
        keyMap.put("AREA:" + area, "");
        keyMap.put("ALLEY:" + alleyIdSnKey, "0000000");
        keyMap.put("LANE:" + replaceWithHalfWidthNumber(lane), "0000");
        keyMap.put("ROOM:" + replaceWithHalfWidthNumber(address.getRoom()), "00000"); //5
        keyMap.put("NUM_FLR_1:" + normalizeFloor(numFlr1, address, "NUM_FLR_1").getNumFlr1(), "000000"); //6
        keyMap.put("NUM_FLR_2:" + normalizeFloor(numFlr2, address, "NUM_FLR_2").getNumFlr2(), "00000"); //5
        keyMap.put("NUM_FLR_3:" + normalizeFloor(numFlr3, address, "NUM_FLR_3").getNumFlr3(), "0000"); //4
        keyMap.put("NUM_FLR_4:" + normalizeFloor(numFlr4, address, "NUM_FLR_4").getNumFlr4(), "000"); //3
        keyMap.put("NUM_FLR_5:" + normalizeFloor(numFlr5, address, "NUM_FLR_5").getNumFlr5(), "0"); //1
        Map<String, String> resultMap = redisService.findSetByKeys(keyMap, segmentExistNumber);
        if (Strings.isNullOrEmpty(address.getCountyCd())) {
            address.setCountyCd(resultMap.get("COUNTY:" + county));
        }
        if (Strings.isNullOrEmpty(address.getTownCd())) {
            address.setTownCd(resultMap.get("TOWN:" + town));
        }

        if (Strings.isNullOrEmpty(address.getVillageCd())) {
            address.setVillageCd(resultMap.get("VILLAGE:" + village));
        }

        address.setNeighborCd(findNeighborCd(address.getNeighbor()));
        address.setRoadAreaSn(StringUtils.isNullOrEmpty(roadAreaKey) ? "0000000" : resultMap.get("ROADAREA:" + roadAreaKey));
        address.setLaneCd(StringUtils.isNullOrEmpty(lane) ? "0000" : resultMap.get("LANE:" + replaceWithHalfWidthNumber(lane)));
        address.setAlleyIdSn(StringUtils.isNullOrEmpty(alleyIdSnKey) ? "0000000" : resultMap.get("ALLEY:" + alleyIdSnKey));
        address.setNumFlr1Id(setNumFlrId(resultMap, address, "NUM_FLR_1"));
        address.setNumFlr2Id(setNumFlrId(resultMap, address, "NUM_FLR_2"));
        address.setNumFlr3Id(setNumFlrId(resultMap, address, "NUM_FLR_3"));
        address.setNumFlr4Id(setNumFlrId(resultMap, address, "NUM_FLR_4"));
        address.setNumFlr5Id(setNumFlrId(resultMap, address, "NUM_FLR_5"));
        String basementStr = address.getBasementStr() == null ? "0" : address.getBasementStr();
        String numFlrPos = getNumFlrPos(address);
        address.setNumFlrPos(numFlrPos);
        address.setRoomIdSn(resultMap.get("ROOM:" + replaceWithHalfWidthNumber(room)));
        assembleMultiMappingIdWithoutCountyAndTownWithNumFlrId(address);
        address.setSegmentExistNumber(combineSegment(resultMap.getOrDefault("segmentExistNumber", ""), address));
        return address;
    }


    public void formatCoutinuousFlrNum(String input, Address address) {
        if (StringUtils.isNotNullOrEmpty(input)) {
            String firstPattern = "(?<coutinuousNum1>[之-]+[\\d\\uFF10-\\uFF19]+)(?<coutinuousNum2>\\D+[之樓FｆＦf])?"; //之45一樓
            String secondPattern = "(?<coutinuousNum1>[之-]\\D+)(?<coutinuousNum2>[\\d\\uFF10-\\uFF19]+[之樓FｆＦf])?"; //之四五1樓
            Matcher matcherFirst = Pattern.compile(firstPattern).matcher(input);
            Matcher matcherSecond = Pattern.compile(secondPattern).matcher(input);

            String[] flrArray = {address.getNumFlr1(), address.getNumFlr2(), address.getNumFlr3(), address.getNumFlr4(), address.getNumFlr5()};
            int count = 0;
            for (int i = 0; i < flrArray.length; i++) {
                if (StringUtils.isNullOrEmpty(flrArray[i])) {
                    count = i + 1;
                    break;
                }
            }

            if (matcherFirst.matches()) {
                setFlrNum(count, matcherFirst.group("coutinuousNum1"), matcherFirst.group("coutinuousNum2"), address);
            } else if (matcherSecond.matches()) {
                setFlrNum(count, matcherSecond.group("coutinuousNum1"), matcherSecond.group("coutinuousNum2"), address);
            }
        }
    }


    private void setFlrNum(int count, String first, String second, Address address) {
        first = replaceWithHalfWidthNumber(first);
        second = replaceWithHalfWidthNumber(second);
        switch (count) {
            case 1:
                address.setNumFlr1(first);
                address.setNumFlr2(second);
                address.setNumFlr3(address.getAddrRemains()); //剩下跑到remain的就塞到最後
                break;
            case 2:
                address.setNumFlr2(first);
                address.setNumFlr3(second);
                address.setNumFlr4(address.getAddrRemains());//剩下跑到remain的就塞到最後
                break;
            case 3:
                address.setNumFlr3(first);
                address.setNumFlr4(second);
                address.setNumFlr5(address.getAddrRemains());//剩下跑到remain的就塞到最後
                break;
            case 4:
                address.setNumFlr4(first);
                address.setNumFlr5(second);
                break;
        }
    }

    public static String combineSegment(String segmentExistNumber, Address address) {
        if (segmentExistNumber.length() != 12) {
            throw new IllegalArgumentException("segmentExistNumber initial value 應為 14 碼");
        }
        String flrSegNum = "0";
        for (int i = 7; i <= 11; i++) {
            if ("1".equals(segmentExistNumber.charAt(i))) {
                flrSegNum = "1";
                break;
            }
        }

        segmentExistNumber = segmentExistNumber.substring(0, 7) + flrSegNum;
        if (StringUtils.isNullOrEmpty(address.getNeighbor()) || "000".equals(address.getNeighborCd())) {
            segmentExistNumber += "0";
        } else {
            segmentExistNumber += "1";
        }

        if (StringUtils.isNotNullOrEmpty(address.getRoom()) || "00000".equals(address.getNeighborCd())) {
            segmentExistNumber += "0";
        } else {
            segmentExistNumber += "1";
        }

        return segmentExistNumber;
    }

    public Address normalizeFloor(String rawString, Address address, String flrType) {
        if (rawString != null) {
            String result = convertFToFloorAndHyphenToZhi(replaceWithHalfWidthNumber(rawString).replace("basement:", ""));
            switch (flrType) {
                case "NUM_FLR_1":
                    address.setNumFlr1(result);
                    break;
                case "NUM_FLR_2":
                    address.setNumFlr2(result);
                    break;
                case "NUM_FLR_3":
                    address.setNumFlr3(result);
                    break;
                case "NUM_FLR_4":
                    address.setNumFlr4(result);
                    break;
                case "NUM_FLR_5":
                    address.setNumFlr5(result);
                    break;
            }
            return address;
        }
        return address;
    }

    public String setNumFlrId(Map<String, String> resultMap, Address address, String flrType) {
        String result = "";
        address = normalizeFloor(getNumFlrByType(address, flrType), address, flrType);
        String numericPart = replaceWithHalfWidthNumber(extractNumericPart(getNumFlrByType(address, flrType)));
        switch (flrType) {
            case "NUM_FLR_1":
                result = resultMap.get(flrType + ":" + address.getNumFlr1());
                return getResult(result, "000000", numericPart);
            case "NUM_FLR_2":
                result = resultMap.get(flrType + ":" + address.getNumFlr2());
                return getResult(result, "00000", numericPart);
            case "NUM_FLR_3":
                result = resultMap.get(flrType + ":" + address.getNumFlr3());
                return getResult(result, "0000", numericPart);
            case "NUM_FLR_4":
                result = resultMap.get(flrType + ":" + address.getNumFlr4());
                return getResult(result, "000", numericPart);
            case "NUM_FLR_5":
                result = resultMap.get(flrType + ":" + address.getNumFlr5());
                return getResult(result, "0", numericPart);
            default:
                return result;
        }
    }

    private String getResult(String result, String comparisonValue, String numericPart) {
        if (comparisonValue.equals(result)) {
            return padNumber(comparisonValue, numericPart);
        } else {
            return result;
        }
    }

    public String findNeighborCd(String rawNeighbor) {
        if (StringUtils.isNotNullOrEmpty(rawNeighbor)) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(replaceWithHalfWidthNumber(rawNeighbor));
            if (matcher.find()) {
                String neighborResult = matcher.group();
                String paddedNumber = String.format("%03d", Integer.parseInt(neighborResult));
                return paddedNumber;
            }
        } else {
            return "000";
        }
        return "000";
    }

    public String getNumFlrPos(Address address) {
        String[] patternFlr1 = {".+號$", ".+樓$", ".+之$", "^之.+", ".+棟$", ".+區$", "^[0-9０-９a-zA-Zａ-ｚＡ-Ｚ一二三四五六七八九東南西北甲乙丙]+$"};
        String[] patternFlr2 = {".+號$", ".+樓$", ".+之$", "^之.+", ".+棟$", ".+區$", "^[0-9０-９a-zA-Zａ-ｚＡ-Ｚ一二三四五六七八九東南西北甲乙丙]+$"};
        String[] patternFlr3 = {".+號$", ".+樓$", ".+之$", "^之.+", ".+棟$", ".+區$", "^[0-9０-９a-zA-Zａ-ｚＡ-Ｚ一二三四五六七八九東南西北甲乙丙]+$"};
        String[] patternFlr4 = {".+號$", ".+樓$", ".+之$", "^之.+", ".+棟$", ".+區$", "^[0-9０-９a-zA-Zａ-ｚＡ-Ｚ一二三四五六七八九東南西北甲乙丙]+$"};
        String[] patternFlr5 = {".+號$", ".+樓$", ".+之$", "^之.+", ".+棟$", ".+區$", "^[0-9０-９a-zA-Zａ-ｚＡ-Ｚ一二三四五六七八九東南西北甲乙丙]+$"};


        return getNum(address.getNumFlr1(), patternFlr1) + getNum(address.getNumFlr2(), patternFlr2) +
                getNum(address.getNumFlr3(), patternFlr3) + getNum(address.getNumFlr4(), patternFlr4) +
                getNum(address.getNumFlr5(), patternFlr5);
    }

    private String getNum(String inputString, String[] patternArray) {
        if (inputString != null && !inputString.isEmpty()) {
            for (int i = 0; i < patternArray.length; i++) {
                Pattern pattern = Pattern.compile(patternArray[i]);
                Matcher matcher = pattern.matcher(inputString);
                if (matcher.matches()) {
                    return String.valueOf(i + 1);
                }
            }
        } else {
            return "0";
        }
        return "0";
    }

    private String getNumFlrByType(Address address, String flrType) {
        switch (flrType) {
            case "NUM_FLR_1":
                return address.getNumFlr1();
            case "NUM_FLR_2":
                return address.getNumFlr2();
            case "NUM_FLR_3":
                return address.getNumFlr3();
            case "NUM_FLR_4":
                return address.getNumFlr4();
            case "NUM_FLR_5":
                return address.getNumFlr5();
            default:
                return "";
        }
    }


    private void assembleMultiMappingIdWithoutCountyAndTownWithNumFlrId(Address address) {
        String numTypeCd = address.getNumTypeCd();

        String basementStr = address.getBasementStr() == null ? "0" : address.getBasementStr();

        List<String> villageCds = new ArrayList<>(splitAndAddToList(address.getVillageCd()));

        List<String> neighborCds = Arrays.asList(address.getNeighborCd());

        List<String> roadAreaCds = new ArrayList<>(splitAndAddToList(address.getRoadAreaSn()));

        List<String> lanes = new ArrayList<>(splitAndAddToList(address.getLaneCd()));
        List<String> alleyIdSns = new ArrayList<>(Arrays.asList(address.getAlleyIdSn()));

        List<String> roomIdSns = new ArrayList<>(splitAndAddToList(address.getRoomIdSn()));

        List<LinkedHashMap<String, String>> mappingIdMapList = new ArrayList<>();
        List<String> mappingIdStringList = new ArrayList<>();

        String originalNumFlrId = address.getNumFlr1Id() + address.getNumFlr2Id() + address.getNumFlr3Id() + address.getNumFlr4Id() + address.getNumFlr5Id();
        address.setNumFlrId(originalNumFlrId);

        assembleNumFlrIdByNumFlrPosByNewJob(address);
        List<String> filteredNumFlrIds = Arrays.asList(address.getNumFlrId(), address.getJB2NumFlrId(), address.getJB3NumFlrId(), address.getJB4NumFlrId(), address.getJB5NumFlrId(), address.getJC4NumFlrId()).stream()
                .filter(StringUtils::isNotNullOrEmpty)
                .collect(Collectors.toList());


        List<String> filteredNumFlrPos = Arrays.asList(address.getNumFlrPos(), address.getJB2NumFlrPos(), address.getJB3NumFlrPos(), address.getJB4NumFlrPos(), address.getJB5NumFlrPos(), address.getJC4NumFlrPos()).stream()
                .filter(StringUtils::isNotNullOrEmpty)
                .collect(Collectors.toList());

        for (String villageCd : villageCds) {
            for (String neighbor : neighborCds) {
                for (String roadAreaCd : roadAreaCds) {
                    for (String laneCd : lanes) {
                        for (String alleyIdSn : alleyIdSns) {
                            for (String numFlrId : filteredNumFlrIds) {
                                for (String numFlrPos : filteredNumFlrPos) {
                                    for (String roomIdsn : roomIdSns) {
                                        //一個 mappingIdMap 所有value組成一個 String 是一組mappingId
                                        LinkedHashMap<String, String> mappingIdMap = new LinkedHashMap<>();
                                        mappingIdMap.put("VILLAGE", villageCd);//里
                                        mappingIdMap.put("NEIGHBOR", neighbor);
                                        mappingIdMap.put("ROADAREA", roadAreaCd);
                                        mappingIdMap.put("LANE", laneCd);
                                        mappingIdMap.put("ALLEY", alleyIdSn);//弄
                                        mappingIdMap.put("NUMTYPE", numTypeCd);
                                        mappingIdMap.put("NUM_FLR_ID", numFlrId);
                                        mappingIdMap.put("BASEMENT", basementStr);
                                        mappingIdMap.put("NUMFLRPOS", numFlrPos);
                                        mappingIdMap.put("ROOM", roomIdsn);
                                        List<String> mappingIdList = Stream.of(
                                                        villageCd, neighbor,
                                                        roadAreaCd, laneCd, alleyIdSn, numTypeCd,
                                                        numFlrId, basementStr, numFlrPos, roomIdsn)
                                                .map(Object::toString)
                                                .collect(Collectors.toList());
                                        //一個 mappingIdMap 所有value組成一個 String 是一組mappingId
                                        mappingIdMapList.add(mappingIdMap);
                                        mappingIdStringList.add(String.join("", mappingIdList));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        address.setMappingIdMap(mappingIdMapList);
        address.setMappingId(mappingIdStringList);
    }


    private void assembleNumFlrIdByNumFlrPosByNewJob(Address address) {

        if (JC4Map.containsKey(address.getNumFlrPos())) {
            String JC4NumFlrPos = (String) JC4Map.get(address.getNumFlrPos());
            String JC4NumFlrId = "";
            String JC4NumFlr1Id = address.getNumFlr1Id();
            String JC4NumFlr2Id = address.getNumFlr2Id();
            String JC4NumFlr3Id = address.getNumFlr3Id();
            String JC4NumFlr4Id = address.getNumFlr4Id();
            String JC4NumFlr5Id = address.getNumFlr5Id();
            if ("12700".equals(address.getNumFlrPos())) {
                int index = address.getNumFlrPos().indexOf("7");
                switch (index) {
                    case 0:
                        JC4NumFlr1Id = address.getNumFlr1Id().replaceFirst("7", "0");
                        break;
                    case 1:
                        JC4NumFlr2Id = address.getNumFlr2Id().replaceFirst("7", "0");
                        break;
                    case 2:
                        JC4NumFlr3Id = address.getNumFlr3Id().replaceFirst("7", "0");
                        break;
                    case 3:
                        JC4NumFlr4Id = address.getNumFlr4Id().replaceFirst("7", "0");
                        break;
                    case 4:
                        JC4NumFlr5Id = address.getNumFlr5Id();
                    default:
                        break;
                }
                JC4NumFlrId = JC4NumFlr1Id + JC4NumFlr2Id + JC4NumFlr3Id + JC4NumFlr4Id + JC4NumFlr5Id;
            } else if ("12400".equals(address.getNumFlrPos())) {
                int index = address.getNumFlrPos().indexOf("4");
                switch (index) {
                    case 0:
                        JC4NumFlr1Id = address.getNumFlr1Id().replaceFirst("0", "7");
                        break;
                    case 1:
                        JC4NumFlr2Id = address.getNumFlr2Id().replaceFirst("0", "7");
                        break;
                    case 2:
                        JC4NumFlr3Id = address.getNumFlr3Id().replaceFirst("0", "7");
                        break;
                    case 3:
                        JC4NumFlr4Id = address.getNumFlr4Id().replaceFirst("0", "7");
                        break;
                    case 4:
                        JC4NumFlr5Id = address.getNumFlr5Id();
                    default:
                        break;
                }
                JC4NumFlrId = JC4NumFlr1Id + JC4NumFlr2Id + JC4NumFlr3Id + JC4NumFlr4Id + JC4NumFlr5Id;
            } else {
                JC4NumFlrId = assembleNumFlrId(address, JC4NumFlrPos);
            }
            address.setJC4NumFlrId(JC4NumFlrId);
            address.setJC4NumFlrPos(JC4NumFlrPos);
        }

        if (JB5Map.containsKey(address.getNumFlrPos())) {
            String JB5NumFlrPos = (String) JB5Map.get(address.getNumFlrPos());
            String JB5NumFlrId = assembleNumFlrId(address, JB5NumFlrPos);
            address.setJB5NumFlrId(JB5NumFlrId);
            address.setJB5NumFlrPos(JB5NumFlrPos);
        }

        if (JB4Map.containsKey(address.getNumFlrPos())) {
            String JB4NumFlrPos = (String) JB4Map.get(address.getNumFlrPos());
            String JB4NumFlrId = "";
            if ("10000".equals(address.getNumFlrPos())) {
                JB4NumFlrId = address.getNumFlr1Id() + "00001" + "0000" + "000" + "0";
            } else {
                JB4NumFlrId = assembleNumFlrId(address, JB4NumFlrPos);
            }
            address.setJB4NumFlrId(JB4NumFlrId);
            address.setJB4NumFlrPos(JB4NumFlrPos);
        }

        if (JB3Map.containsKey(address.getNumFlrPos())) {
            String JB3NumFlrPos = (String) JB3Map.get(address.getNumFlrPos());
            String JB3NumFlrId = assembleNumFlrId(address, JB3NumFlrPos);
            address.setJB3NumFlrId(JB3NumFlrId);
            address.setJB3NumFlrPos(JB3NumFlrPos);

        }


        Set<String> keys = JB2Map.keySet();
        for (String key : keys) {
            if (address.getNumFlrPos().indexOf(key) >= 0) {
                String JB2NumFlrPos = address.getNumFlrPos().replace(key, (String) JB2Map.get(key));
                String JB2NumFlrId = assembleNumFlrId(address, JB2NumFlrPos);
                address.setJB2NumFlrId(JB2NumFlrId);
                address.setJB2NumFlrPos(JB2NumFlrPos);
            }
        }
    }


    private String assembleNumFlrId(Address address, String assembleNumFlrPos) {
        char[] chars = assembleNumFlrPos.toCharArray();
        String numFlr1Id = address.getNumFlr1Id();
        String numFlr2Id = address.getNumFlr2Id();
        String numFlr3Id = address.getNumFlr3Id();
        String numFlr4Id = address.getNumFlr4Id();
        String numFlr5Id = address.getNumFlr5Id();
        for (int i = 0; i < chars.length; i++) {
            switch (i) {
                case 0:
                    if (chars[i] == '0') {
                        numFlr1Id = "000000";
                        numFlr2Id = "00000";
                        numFlr3Id = "0000";
                        numFlr4Id = "000";
                        numFlr5Id = "0";
                    }
                    break;
                case 1:
                    if (chars[i] == '0') {
                        numFlr2Id = "00000";
                        numFlr3Id = "0000";
                        numFlr4Id = "000";
                        numFlr5Id = "0";
                    }
                    break;
                case 2:
                    if (chars[i] == '0') {
                        numFlr3Id = "0000";
                        numFlr4Id = "000";
                        numFlr5Id = "0";
                    }
                    break;
                case 3:
                    if (chars[i] == '0') {
                        numFlr4Id = "000";
                        numFlr5Id = "0";
                    }
                    break;
                case 4:
                    if (chars[i] == '0') {
                        numFlr5Id = "0";
                    }
                    break;
                default:
                    break;
            }
        }
        String assembleNumFlrId = numFlr1Id + numFlr2Id + numFlr3Id + numFlr4Id + numFlr5Id;
        return assembleNumFlrId;
    }


    private static List<String> splitAndAddToList(String input) {
        List<String> result = new ArrayList<>();

        if (input.contains(",")) {
            result.addAll(Arrays.asList(input.split(",")));
        } else {
            result.add(input);
        }
        return result;
    }

    private String removeRepeatCountyAndTown(SingleQueryDTO singleQueryDTO) {
        String county = Strings.isNullOrEmpty(singleQueryDTO.getCounty()) ? "" : singleQueryDTO.getCounty();
        String town = Strings.isNullOrEmpty(singleQueryDTO.getTown()) ? "" : singleQueryDTO.getTown();
        String patternStr = Pattern.quote(county + town);
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(singleQueryDTO.getOriginalAddress());
        int count = 0;
        String result = singleQueryDTO.getOriginalAddress();
        while (matcher.find()) {
            count++;
            if (count >= 2) {
                result = singleQueryDTO.getOriginalAddress().replaceFirst(matcher.group(), "");
                return result;
            }
        }
        return result;
    }

    Boolean checkIfMultiAddress(SingleQueryDTO singleQueryDTO) {
        return singleQueryDTO.getOriginalAddress().matches(".*[、~，].*");
    }

    void setJoinStepWhenResultIsEmpty(List<IbdTbAddrCodeOfDataStandardDTO> list, SingleQueryResultDTO result, Address address) {
        if (list.isEmpty()) {
            IbdTbAddrCodeOfDataStandardDTO dto = new IbdTbAddrCodeOfDataStandardDTO();
            String segNum = address.getSegmentExistNumber();

            if (address.getOriginalAddress().contains("地號") || address.getOriginalAddress().contains("段號")) {
                setResult(dto, result, "JE311", "地段號");
                list.add(dto);
                return;
            }


            if (Strings.isNullOrEmpty(address.getNumFlrPos()) || !address.getNumFlrPos().contains("1")) {
                setResult(dto, result, "JE411", "缺少門牌號");
                list.add(dto);
                return;
            }

            if (segNum.startsWith("11") && segNum.charAt(3) == '0' && segNum.charAt(4) == '0' && segNum.charAt(5) == '0') {
                setResult(dto, result, "JE421", "缺少路地名");
                list.add(dto);
                return;
            }

            if (!segNum.startsWith("11") && (StringUtils.isNullOrEmpty(address.getCounty()) || StringUtils.isNullOrEmpty(address.getTown()))) {
                setResult(dto, result, "JE431", "缺少行政區");
                list.add(dto);
                return;
            }


            if (checkSegNum(segNum)) {
                setResult(dto, result, "JE511", "查無地址");
                list.add(dto);
                return;
            }

            if (!Strings.isNullOrEmpty(address.getCounty()) && !Strings.isNullOrEmpty(address.getTown()) && segNum.startsWith("00")) {
                setResult(dto, result, "JE521", "查無地址");
                list.add(dto);
                return;
            }

            if ((!Strings.isNullOrEmpty(address.getVillage()) && segNum.charAt(2) == '0') || (!Strings.isNullOrEmpty(address.getRoad()) && !Strings.isNullOrEmpty(address.getArea()) && segNum.charAt(3) == '0' && segNum.charAt(4) == '0')) {
                setResult(dto, result, "JE531", "查無地址");
                list.add(dto);
                return;
            }
            setResult(dto, result, "JE911", "上述比對階段均無法處理");
            list.add(dto);
        }
    }


    private void setResult(IbdTbAddrCodeOfDataStandardDTO dto, SingleQueryResultDTO result, String joinStep, String text) {
        dto.setJoinStep(joinStep);
        result.setText(text);
    }

    private Boolean checkSegNum(String segNum) {
        String hasRoad = "";
        String noRoad = "";

        String[] viilages = new String[]{"0", "1"};
        String[] areas = new String[]{"0", "1"};
        String[] alleys = new String[]{"0", "1"};
        String[] lanes = new String[]{"0", "1"};
        Set<String> patterns = new HashSet<>();

        for (String village : viilages) {
            for (String area : areas) {
                for (String lane : lanes) {
                    for (String alley : alleys) {
                        hasRoad = "11" + village + "1" + area + lane + alley + "1";
                        noRoad = "11" + village + "0" + area + lane + alley + "1";
                        patterns.add(hasRoad);
                        patterns.add(noRoad);
                    }
                }
            }
        }
        for (String pattern : patterns) {
            if (segNum.substring(0, segNum.length() - 2).equals(pattern)) {
                return true;
            }
        }
        return false;
    }

    private List<IbdTbAddrCodeOfDataStandardDTO> stardardDataAndDataRepository(Address address) {
        if (address.getJoinStep().charAt(3) == '2') {
            List<IbdTbIhChangeDoorplateHis> hisList = ibdTbIhChangeDoorplateHisRepository.findByHistorySeq(address.getSeqSet().stream().toList());
            return bdTbAddrCodeOfDataStandardRepository.findByAddressIdGetNumFlrPOS(hisList, address);
        } else {
            return bdTbAddrCodeOfDataStandardRepository.findBySeqsFromStardardDataAndDataRepository(address.getSeqSet().stream().map(Integer::parseInt).collect(Collectors.toList()));
        }


    }

    void build56MappingIdsWithoutVillageNeighborRoom(Address address) {
        String withoutVillage = "000";
        String withoutNeighbor = "000";
        String withoutRoom = "00000";
        List<String> newMappingIds = new ArrayList<>();
        address.getMappingId().forEach(mappingId -> {
            String newId = withoutVillage + withoutNeighbor + mappingId.substring(6, mappingId.length() - 5) + withoutRoom;
            newMappingIds.add(newId);
        });
        address.setMappingId(newMappingIds);
    }


    String renewJoinStep(String startCode, String oldJoinStep) {
        String endCode = oldJoinStep.substring(3, 5);
        String result = startCode + endCode;
        return result;
    }
}
