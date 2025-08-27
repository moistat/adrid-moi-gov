package com.example.pentaho.utils;

import com.example.pentaho.component.Address;
import com.example.pentaho.component.InMemoryRedisDB;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.pentaho.utils.NumberParser.*;

@Component
public class AddressParser {

    @Autowired
    @Qualifier("InMemoryRedisDB0")
    private InMemoryRedisDB InMemoryRedisDB0;

    private final String BASEMENT_PATTERN = "basement:[一二三四五六七八九十百千]+樓"; //經過一次PARSE之後，如果有地下或屋頂，都會被改為basement:開頭
    private final String ALL_CHAR_FOR_ALLEY = "[0-9０-９A-ZＡ-Ｚa-zａ-ｚ\\uFF10-\\uFF19零一二三四五六七八九十百千甲乙丙丁戊己庚壹貳參肆伍陸柒捌玖拾佰卅廿整棟之-]";
    private final String ALL_CHAR = "[0-9０-９A-ZＡ-Ｚa-zａ-ｚ\\uFF10-\\uFF19零一二三四五六七八九十百千甲乙丙丁戊己庚壹貳參肆伍陸柒捌玖拾佰卅廿整棟]";
    private final String DYNAMIC_ALLEY_PART = "|卓厝|安農新邨|吉祥園|蕭厝|泰安新村|美喬|１弄圳東|堤外|中興二村|溝邊|長埤|清水|南苑|二橫路|朝安|黃泥塘|建行新村|牛頭|永和山莊";

    private final String COUNTY = "(?<zipcode>(^\\d{5}|^\\d{3}|^\\d)?)(?<county>(?:.*?[縣市](?!場)|%s))?"; //(?!場)==>為了避免"內埔市場"這種area被切到這裡

    private final String TOWN = "(?<town>\\D+?(市區|鎮區|鎮市|[鄉鎮市區]|%s)(?![村里鄰路巷段街道弄]))?";

    private final String VILLAGE = "(?<village>(?<!路)%s(新里里|村里|[^路]*?里|[^路]*?村|%s)(?![村里鄰路巷段街道弄]))?";


    private final String NEIGHBOR = "(?<neighbor>" + ALL_CHAR + "+鄰)?";

    private final String ROAD = "(?<road>(.*?段|.*?街|.*?大道|.*?路(?!巷)|%s)?)";

    private final String SPECIALLANE = "(?<speciallane>鐵路.*巷|丹路.*巷)?";

    private final String LANE = "(?<lane>.*?巷)?";

    private final String ALLEY = "(?<alley>" + ALL_CHAR_FOR_ALLEY + "+弄" + DYNAMIC_ALLEY_PART + ")?";
    private final String SUBALLEY = "(?<subAlley>" + ALL_CHAR + "+[衖衕橫]{1})?";

    private final String NUMFLR1 =
            "(?<numFlr1>" + ALL_CHAR + "+[\\-¯－－ ─ ?─―之號樓FｆＦf之區棟]{1}|" + //2¯ 2?50號 =>號之之號
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+(?!.*[樓FｆＦf])|" + //之2~樓
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+[號]|" +//之2~號
                    ALL_CHAR + "+[FｆＦf]{1} |" + //2F
                    BASEMENT_PATTERN
                    + "|" + ALL_CHAR + "+[室]"
                    + "|" + ALL_CHAR + "+(?!室))?";// only 數字


    private final String NUMFLR2 =
            "(?<numFlr2>" + ALL_CHAR + "+[\\-¯－－ ─ ?─―之號樓FｆＦf之區棟]{1}|" + //2¯
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+(?!.*[樓FｆＦf])|" + //之2~樓
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+[號]|" +//之2~號
                    ALL_CHAR + "[\\-¯－－ ─ ?─―]" + ALL_CHAR + "[號]|" +
                    ALL_CHAR + "+[FｆＦf]{1} |" + //2F
                    BASEMENT_PATTERN
                    + "|" + ALL_CHAR + "+[室]"
                    + "|" + ALL_CHAR + "+(?!室))?";// only 數字

    private final String NUMFLR3 =
            "(?<numFlr3>" + ALL_CHAR + "+[\\-¯－－ ─ ?─―之號樓FｆＦf之區棟]{1}|" + //2¯
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+(?!.*[樓FｆＦf])|" + //之2~樓
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+[號]|" +//之2~號
                    ALL_CHAR + "[\\-¯－－ ─ ?─―]" + ALL_CHAR + "[號]|" +
                    ALL_CHAR + "+[FｆＦf]{1} |" + //2F
                    BASEMENT_PATTERN
                    + "|" + ALL_CHAR + "+[室]"
                    + "|" + ALL_CHAR + "+(?!室))?";// only 數字


    private final String NUMFLR4 =
            "(?<numFlr4>" + ALL_CHAR + "+[\\-¯－－ ─ ?─―之號樓FｆＦf之區棟]{1}|" + //2¯
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+(?!.*[樓FｆＦf])|" + //之2~樓
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+[號]|" +//之2~號
                    ALL_CHAR + "[\\-¯－－ ─ ?─―]" + ALL_CHAR + "[號]|" +
                    ALL_CHAR + "+[FｆＦf]{1} |" + //2F
                    BASEMENT_PATTERN
                    + "|" + ALL_CHAR + "+[室]"
                    + "|" + ALL_CHAR + "+(?!室))?";// only 數字


    private final String NUMFLR5 =
            "(?<numFlr5>" + ALL_CHAR + "+[\\-¯－－ ─ ?─―之號樓FｆＦf之區棟]{1}|" + //2¯
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+(?!.*[樓FｆＦf])|" + //之2~樓
                    "[之\\-¯－－ ─ ?─―]{1}" + ALL_CHAR + "+[號]|" +//之2~號
                    ALL_CHAR + "[\\-¯－－ ─ ?─―]" + ALL_CHAR + "[號]|" +
                    ALL_CHAR + "+[FｆＦf]{1} |" + //2F
                    BASEMENT_PATTERN
                    + "|" + ALL_CHAR + "+[室]"
                    + "|" + ALL_CHAR + "+(?!室))?";// only 數字

    private final String CONTINUOUS_NUM = "(?<continuousNum>[之\\-¯－]{1}" + ALL_CHAR + "+[樓FｆＦf]{1})?";

    /**
     * 室基本上在num_Frl 階段就會被切走
     **/
    private final String ROOM = "(?<room>" + ALL_CHAR + "+[室])?";

    private final String BASEMENTSTR = "(?<basementStr>屋頂突出.*層|地下.*層|地下.*樓|屋頂突出物|地下室|屋頂樓|地下|底層|屋頂|頂樓|頂層|頂加|頂)?";
    private final String ADDRREMAINS = "(?<addrRemains>.+)?";
    private final String REMARK = "(?<remark>[\\(\\{\\〈\\【\\[\\〔\\『\\「\\「\\《\\（](.*?)[\\)\\〉\\】\\]\\〕\\』\\」\\}\\」\\》\\）])?";
    //〈〉【】[]〔〕()『』「」{}「」《》（）


    public Address parseAddress(String origninalAddress, Address address) {
        if (address == null) {
            address = new Address();
        }

        address.setCleanAddress(cleanAddress(origninalAddress));

        Map<String, Set<String>> allKeys = new LinkedHashMap<>();

        allKeys = findAllKeys();

        origninalAddress = findNeighbor(address, origninalAddress);

        origninalAddress = findZipcode(address, origninalAddress);

        origninalAddress = findCounty(allKeys, address, origninalAddress);

        origninalAddress = findTown(allKeys, address, origninalAddress);

        origninalAddress = findVillage(allKeys, address, origninalAddress);

        String pattern = getPattern(address, allKeys);

        Pattern regexPattern = Pattern.compile(pattern);

        Matcher matcher = regexPattern.matcher(origninalAddress);

        if (matcher.matches()) {
            return setAddress(matcher, address);
        }

        return address;
    }

    public Map<String, Object> sortRedisValue(Set<String> set) {
        HashMap<String, Object> map = new HashMap<>();
        List<String> pattern = new ArrayList<>();
        Map<String, List<String>> mapper = new HashMap<>();
        for (String val : set) {
            String word = val.split(":")[0];
            pattern.add(word);
            List<String> cds = new ArrayList<>();
            if (val.split(":").length > 1) {
                String cd = val.split(":")[1];
                if (mapper.get(word) != null) {
                    cds = mapper.get(word);
                }
                cds.add(cd);
            }
            mapper.put(word, cds);
        }
        pattern = pattern.stream()
                .sorted((s1, s2) -> Integer.compare(s2.length(), s1.length()))
                .collect(Collectors.toCollection(ArrayList::new));
        map.put("pattern", pattern);
        map.put("mapper", mapper);
        return map;
    }

    public Address extractNeighbor(Address address) {
        Pattern pattern = Pattern.compile("(?<neighbor>[0-9０-９A-Za-zａ-ｚ\\uFF10-\\uFF19零一二三四五六七八九十百千甲乙丙丁戊己庚壹貳參肆伍陸柒捌玖拾佰卅廿整棟]+鄰)");
        Matcher matcher = pattern.matcher(address.getRoad());
        if (StringUtils.isNotNullOrEmpty(matcher.group("neighbor"))) {
            address.setNeighbor(matcher.group("neighbor"));
            address.setRoad(address.getRoad().replace(matcher.group("neighbor"), ""));
        }
        return address;
    }

    public Address extractRoom(Address address) {
        String[] numFlrs = new String[]{
                address.getNumFlr1(), address.getNumFlr2(), address.getNumFlr3(),
                address.getNumFlr4(), address.getNumFlr5()};

        int whichFlr = findRoomFloor(numFlrs);

        if (whichFlr >= 0) {
            String roomFloor = numFlrs[whichFlr];
            address.setRoom(roomFloor);
            shiftFloors(address, whichFlr);
        }

        return address;
    }

    private int findRoomFloor(String[] numFlrs) {
        for (int i = 0; i < numFlrs.length; i++) {
            if (StringUtils.isNotNullOrEmpty(numFlrs[i]) && numFlrs[i].contains("室")) {
                return i;
            }
        }
        return -1;
    }

    private void shiftFloors(Address address, int startIdx) {
        for (int i = startIdx; i < 4; i++) {
            String nextFlr = getFlrByIndex(i + 1, address);
            setFlrByIndex(i, nextFlr, address);
        }
        setFlrByIndex(4, null, address);
    }

    private String getFlrByIndex(int index, Address address) {
        switch (index) {
            case 1: return address.getNumFlr1();
            case 2: return address.getNumFlr2();
            case 3: return address.getNumFlr3();
            case 4: return address.getNumFlr4();
            case 5: return address.getNumFlr5();
            default: return null;
        }
    }

    // 根據索引設置樓層的屬性值
    private void setFlrByIndex(int index, String value, Address address) {
        switch (index) {
            case 1: address.setNumFlr1(value); break;
            case 2: address.setNumFlr2(value); break;
            case 3: address.setNumFlr3(value); break;
            case 4: address.setNumFlr4(value); break;
            case 5: address.setNumFlr5(value); break;
        }
    }


    public String findNeighbor(Address address, String origninalAddress) {
        String negihborPattern = ALL_CHAR + "+鄰";
        Pattern patternForNeighbor = Pattern.compile(negihborPattern);
        Matcher matcherForNeighobr = patternForNeighbor.matcher(origninalAddress);
        if (matcherForNeighobr.find()) {
            address.setNeighbor(matcherForNeighobr.group());
            origninalAddress = origninalAddress.replace(matcherForNeighobr.group(), "");
        }
        return origninalAddress;
    }


    public String findVillage(Map<String, Set<String>> allKeys, Address address, String origninalAddress) {
        ArrayList<String> villageAlias = (ArrayList<String>) sortRedisValue(allKeys.get("VILLAGE_ALIAS:")).get("pattern");
        Map<String, Object> mapper = (Map<String, Object>) sortRedisValue(allKeys.get("VILLAGE_ALIAS:")).get("mapper");
        String countyCd = Strings.isNullOrEmpty(address.getCountyCd()) ? "00000" : address.getCountyCd();
        String townCd = Strings.isNullOrEmpty(address.getTownCd()) ? "000" : address.getTownCd();
        String targetCountyTownCd = countyCd + townCd;
        for (String village : villageAlias) {
            if (origninalAddress.contains(village)) {
                List<String> allCds = (ArrayList) mapper.get(village);
                if ("00000000".equals(targetCountyTownCd)) {
                    address.setVillage(village);
                    origninalAddress = origninalAddress.replace(village, "");
                    return origninalAddress;
                }

                Optional<String> option = allCds.stream().filter(cd -> cd.startsWith(targetCountyTownCd))
                        .findFirst();
                if (option.isPresent()) {
                    String villageCd = option.get().substring(8, option.get().length());
                    address.setVillage(village);
                    address.setVillageCd(villageCd);
                    origninalAddress = origninalAddress.replace(village, "");
                    return origninalAddress;
                }
            }
        }
        return origninalAddress;
    }

    public String findZipcode(Address address, String origninalAddress) {
        String zipcodeStr = "(?<zipcode>(^\\d{8}|^\\d{5}|^\\d{3}|^\\d)?)";
        Pattern zipcodePatten = Pattern.compile(zipcodeStr);
        Matcher zipcodematcher = zipcodePatten.matcher(origninalAddress);
        if (zipcodematcher.find()) {
            String zipcode = zipcodematcher.group();
            address.setZipcode(zipcode);
            origninalAddress = origninalAddress.replace(zipcode, "");
        }
        return origninalAddress;
    }

    public String findCounty(Map<String, Set<String>> allKeys, Address address, String origninalAddress) {
        Map<String, Object> sort = sortRedisValue(allKeys.get("COUNTY_ALIAS:"));
        ArrayList<String> countyAlias = (ArrayList<String>) sort.get("pattern");
        Map<String, Object> mapper = (Map) sort.get("mapper");
        for (String county : countyAlias) {
            if (origninalAddress.contains(county)) {
                List<String> allCds = (ArrayList) mapper.get(county);
                address.setCounty(county);
                address.setCountyCd(allCds.get(0));
                origninalAddress = origninalAddress.replace(county, "");
                return origninalAddress;
            }
        }
        return origninalAddress;
    }


    public String findTown(Map<String, Set<String>> allKeys, Address address, String origninalAddress) {
        ArrayList<String> townAlias = (ArrayList<String>) sortRedisValue(allKeys.get("TOWN_ALIAS:")).get("pattern");
        Map<String, Object> mapper = (Map) sortRedisValue(allKeys.get("TOWN_ALIAS:")).get("mapper");
        for (String town : townAlias) {
            if (origninalAddress.contains(town)) {
                if (Strings.isNullOrEmpty(address.getCountyCd())) {
                    address.setTown(town);
                    origninalAddress = origninalAddress.replace(town, "");
                    return origninalAddress;
                }

                List<String> allCds = (ArrayList) mapper.get(town);
                Optional<String> option = allCds.stream().filter(cd -> cd.startsWith(address.getCountyCd())).findFirst();
                if (option.isPresent()) {
                    String townCd = option.get().substring(5, option.get().length());
                    address.setTown(town);
                    address.setTownCd(townCd);
                    origninalAddress = origninalAddress.replace(town, "");
                    return origninalAddress;
                }
            }
        }
        return origninalAddress;
    }


    public static String cleanAddress(String originalAddress) {
        if (Strings.isNullOrEmpty(originalAddress)) {
            return originalAddress;
        }
        return originalAddress.replace("台灣省", "").replace("福建省", "")
                .replaceAll("[`!@#$%^&*+=|';',./！@#￥%……&*+|‘”“’。，\\\\\\s]+", "");
    }


    public Address parseArea(Address address) {
        String[] regexArray = {
                "^[^0-9０-９]+;",
                "^[^0-9０-９一二三四五六七八九十]+[一二三四五六七八九十]+[^0-9０-９一二三四五六七八九十]*;",
                "^[^0-9０-９一二三四五六七八九十]{1,7}"
        };
        String match = "";
        for (String regex : regexArray) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(address.getAddrRemains());
            if (matcher.find()) {
                match = matcher.group();
                break;
            }
        }
        if (StringUtils.isNotNullOrEmpty(match)) {
            address.setArea(match);
            String cleanAddress = address.getCleanAddress();
            int lastIndex = cleanAddress.lastIndexOf(match);
            String newAddressString = cleanAddress.substring(0, lastIndex) + cleanAddress.substring(lastIndex + match.length());
            address = parseAddress(newAddressString, address);
        }
        return address;
    }


    private String getPattern(Address address, Map<String, Set<String>> allKeys) {
        ArrayList<String> countyAlias = (ArrayList<String>) sortRedisValue(allKeys.get("COUNTY_ALIAS:")).get("pattern");
        ArrayList<String> townAlias = (ArrayList<String>) sortRedisValue(allKeys.get("TOWN_ALIAS:")).get("pattern");
        ArrayList<String> villageAlias = (ArrayList<String>) sortRedisValue(allKeys.get("VILLAGE_ALIAS:")).get("pattern");
        ArrayList<String> roadAlias = (ArrayList<String>) sortRedisValue(allKeys.get("ROAD_ALIAS:")).get("pattern");
        ArrayList<String> specialArea = (ArrayList<String>) sortRedisValue(allKeys.get("SPECIAL_AREA:")).get("pattern");

        String newCounty = String.format(COUNTY, String.join("|", countyAlias));
        String newTown = String.format(TOWN, String.join("|", townAlias));
        String newVillage = String.format(VILLAGE, "(?!" + String.join("|", specialArea) + ")", String.join("|", villageAlias));
        String newRoad = String.format(ROAD, String.join("|", roadAlias));
        String newArea = "(?<area>(" + String.join("|", specialArea) + ")?)";
        String matchRules = "";


        if (Strings.isNullOrEmpty(address.getCounty())) {
            matchRules += newCounty;
        }

        if (Strings.isNullOrEmpty(address.getTown())) {
            matchRules += newTown;
        }

        if (Strings.isNullOrEmpty(address.getVillage())) {
            matchRules += newVillage;
        }


        if (Strings.isNullOrEmpty(address.getNeighbor())) {
            matchRules += NEIGHBOR;
        }

        return matchRules + SPECIALLANE + newRoad + newArea + LANE + ALLEY + SUBALLEY + NUMFLR1 + NUMFLR2 + NUMFLR3 + NUMFLR4 + NUMFLR5 + CONTINUOUS_NUM + ROOM + BASEMENTSTR + REMARK + ADDRREMAINS;
    }


    private Map<String, Set<String>> findAllKeys() {
        String[] keys = {"COUNTY_ALIAS:", "TOWN_ALIAS:", "VILLAGE_ALIAS:", "ROAD_ALIAS:", "SPECIAL_AREA:"};
        Map<String, Set<String>> resultMap = new HashMap<>();
        try {
            for (String key : keys) {
                Set<String> deserializedSet = InMemoryRedisDB0.sMembers(key);
                resultMap.put(key, deserializedSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    public Address setAddress(Matcher matcher, Address address) {
        String basementString = matcher.group("basementStr");
        if (StringUtils.isNotNullOrEmpty(basementString)) {
            String parseBasement = parseBasement(basementString, address.getCleanAddress(), address);
            return parseAddress(parseBasement, address);
        }


        if (Strings.isNullOrEmpty(address.getCounty())) {
            if (Strings.isNullOrEmpty(address.getZipcode())) {
                address.setZipcode(matcher.group("zipcode"));
            }
            address.setCounty(matcher.group("county"));
        }

        if (Strings.isNullOrEmpty(address.getTown())) {
            address.setTown(matcher.group("town"));
        }

        if (Strings.isNullOrEmpty(address.getVillage())) {
            address.setVillage(matcher.group("village"));
        }

        if (Strings.isNullOrEmpty(address.getNeighbor())) {
            address.setNeighbor(matcher.group("neighbor"));
        }

        address.setRoad(matcher.group("road"));
        address.setArea(matcher.group("area"));

        if (address.getRoad().contains("鄰")) {
            extractNeighbor(address);
        }

        address.setLane(StringUtils.isNotNullOrEmpty(matcher.group("speciallane")) ? matcher.group("speciallane") : matcher.group("lane"));

        address.setAlley(matcher.group("alley"));

        address.setSubAlley(matcher.group("subAlley"));

        address.setNumFlr1(parseBasementForBF(matcher.group("numFlr1"), address));

        address.setNumFlr2(parseBasementForBF(matcher.group("numFlr2"), address));

        address.setNumFlr3(parseBasementForBF(matcher.group("numFlr3"), address));

        address.setNumFlr4(parseBasementForBF(matcher.group("numFlr4"), address));

        address.setNumFlr5(parseBasementForBF(matcher.group("numFlr5"), address));

        address.setContinuousNum(matcher.group("continuousNum"));

        address.setRoom(matcher.group("room"));

        address.setAddrRemains(matcher.group("addrRemains"));

        address.setRemark(matcher.group("remark"));

        return address;
    }

    private String parseBasement(String basementString, String origninalAddress, Address address) {
        String[] basemantPattern1 = {"地下層", "地下室", "地下", "底層"};
        String[] basemantPattern2 = {".*地下.*層.*", ".*地下室.*層.*", ".*地下.*樓.*", "屋頂突出.*層"};
        String[] roof1 = {"屋頂突出物", "屋頂樓", "屋頂", "頂樓", "頂層", "頂加", "頂"};
        if (Arrays.asList(basemantPattern1).contains(basementString)) {
            origninalAddress = origninalAddress.replace(basementString, "一樓");
            address.setBasementStr("1");
        } else if (Arrays.asList(roof1).contains(basementString)) {
            origninalAddress = origninalAddress.replace(basementString, "");
            address.setBasementStr("2");
        } else {
            for (String basemantPattern : basemantPattern2) {
                Pattern regex = Pattern.compile(basemantPattern);
                Matcher basemantMatcher = regex.matcher(basementString);
                if (basemantMatcher.matches()) {
                    String numericPart = extractNumericPart(basementString);
                    origninalAddress = origninalAddress.replaceAll(basementString, "basement:" + replaceWithChineseNumber(numericPart) + "樓");
                    if (basementString.contains("頂")) {
                        address.setBasementStr("2");
                    } else {
                        address.setBasementStr("1");
                    }
                    break;
                }
            }
        }
        return origninalAddress;
    }



    private String parseBasementForBF(String input, Address address) {
        if (StringUtils.isNotNullOrEmpty(input)) {
            String[] basemantPattern1 = {"BF", "bf", "B1", "b1", "Ｂ１", "ｂ１", "ＢＦ", "ｂｆ"};
            String[] basemantPattern2 = {".*B.*樓", ".*b.*樓", ".*Ｂ.*樓", ".*ｂ.*樓", ".*B.*F", ".*b.*f", ".*Ｂ.*Ｆ", ".*ｂ.*ｆ"};
            String[] basemantPattern3 = {"整棟樓"};
            if (Arrays.asList(basemantPattern1).contains(input)) {
                address.setBasementStr("1");
                return "一樓";
            }

            if (Arrays.asList(basemantPattern3).contains(input)) {
                address.setBasementStr("0");
                return "";
            }

            for (String basemantPattern : basemantPattern2) {
                Pattern regex = Pattern.compile(basemantPattern);
                Matcher basemantMatcher = regex.matcher(input);
                if (basemantMatcher.matches()) {
                    String numericPart = extractNumericPart(input);
                    address.setBasementStr("1");
                    return replaceWithChineseNumber(numericPart) + "樓";

                }
            }
        }
        return input;
    }
}


