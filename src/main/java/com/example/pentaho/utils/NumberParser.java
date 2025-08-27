package com.example.pentaho.utils;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class NumberParser {

    public static String replaceWithHalfWidthNumber(String input) {
        if (input != null && !input.isEmpty()) {
            input = input.replaceAll("[-－]", "之");
            if (containsLittleChineseNumbers(input)) {
                String newNum = LittleDigitConvert.convertToDigit(input);
                newNum = assembleString(input,newNum);
                return newNum;
            } else if (containsBigChineseNumbers(input)) {
                String newNum = BigDigitConvert.convertToDigit(input);
                newNum = assembleString(input,newNum);
                return newNum;
            } else if (containsNumbers(input)) {
                Map<String, String> map = new HashMap<>();
                map.put("０", "0");
                map.put("１", "1");
                map.put("２", "2");
                map.put("３", "3");
                map.put("４", "4");
                map.put("５", "5");
                map.put("６", "6");
                map.put("７", "7");
                map.put("８", "8");
                map.put("９", "9");
                map.put("一", "1");
                map.put("二", "2");
                map.put("三", "3");
                map.put("四", "4");
                map.put("五", "5");
                map.put("六", "6");
                map.put("七", "7");
                map.put("八", "8");
                map.put("九", "9");
                map.put("零", "0");
                map.put("壹", "1");
                map.put("貳", "2");
                map.put("參", "3");
                map.put("肆", "4");
                map.put("伍", "5");
                map.put("陸", "6");
                map.put("柒", "7");
                map.put("捌", "8");
                map.put("玖", "9");
                map.put("卅", "3");
                map.put("廿", "2");
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < input.length(); i++) {
                    String currentChar = String.valueOf(input.charAt(i));
                    if (map.containsKey(currentChar)) {
                        result.append(map.get(currentChar));
                    } else {
                        result.append(currentChar);
                    }
                }
                return result.toString();
            }
        } else if (input == null) {
            return "";
        }
        return input;
    }


    public static boolean containsLittleChineseNumbers(String input) {
        String regex = ".*(十|零).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    public static boolean containsBigChineseNumbers(String input) {
        String regex = ".*(拾|佰).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean containsNumbers(String input) {
        String regex = ".*(０|１|２|３|４|５|６|７|８|９|一|二|三|四|五|六|七|八|九|零|壹|貳|參|肆|伍|陸|柒|捌|玖|卅|廿).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private static final HashMap<Character, Integer> numberMap = new HashMap<>();

    static {
        numberMap.put('零', 0);
        numberMap.put('一', 1);
        numberMap.put('二', 2);
        numberMap.put('三', 3);
        numberMap.put('四', 4);
        numberMap.put('五', 5);
        numberMap.put('六', 6);
        numberMap.put('七', 7);
        numberMap.put('八', 8);
        numberMap.put('九', 9);
    }

    public static String chineseNumberToArabic(String chineseNumber) {
        Pattern p;
        Matcher m;
        for (String regex : regexMap.keySet()) {
            p = Pattern.compile(regex);
            m = p.matcher(chineseNumber);
            while (m.find()) {
                String exper = regexMap.get(regex);
                List<String> list = new ArrayList<>();
                for (int i = 1; i <= m.groupCount(); i++) {
                    list.add(NumRegex.numMap.get(m.group(i)));
                }
                exper = MessageFormat.format(exper, list.toArray());
                String text = m.group();
                String value = experToValue(exper);
                chineseNumber = chineseNumber.replace(text, value);
            }
        }
        return chineseNumber;
    }


    /***
     * 轉盛小寫中文數字
     * @param input
     * @return
     */
    public static String replaceWithChineseNumber(String input) {
        if (input != null && !input.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("壹", "一");
            map.put("貳", "二");
            map.put("叁", "三");
            map.put("肆", "四");
            map.put("伍", "五");
            map.put("陸", "六");
            map.put("柒", "七");
            map.put("捌", "八");
            map.put("玖", "九");
            map.put("零", "零");
            map.put("一", "一");
            map.put("二", "二");
            map.put("三", "三");
            map.put("四", "四");
            map.put("五", "五");
            map.put("六", "六");
            map.put("七", "七");
            map.put("八", "八");
            map.put("九", "九");
            map.put("０", "零");
            map.put("１", "一");
            map.put("２", "二");
            map.put("３", "三");
            map.put("４", "四");
            map.put("５", "五");
            map.put("６", "六");
            map.put("７", "七");
            map.put("８", "八");
            map.put("９", "九");
            map.put("０", "零");
            map.put("1", "一");
            map.put("2", "二");
            map.put("3", "三");
            map.put("4", "四");
            map.put("5", "五");
            map.put("6", "六");
            map.put("7", "七");
            map.put("8", "八");
            map.put("9", "九");
            map.put("0", "零");
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                String currentChar = String.valueOf(input.charAt(i));
                if (map.containsKey(currentChar)) {
                    result.append(map.get(currentChar));
                } else {
                    result.append(currentChar);
                }
            }
            return result.toString();
        }
        return "";
    }

    /**
     * Ff 轉換成樓
     * -¯－－ ─ ?─ 轉成之
     */
    public static String convertFToFloorAndHyphenToZhi(String input) {
        if (input.endsWith("F") || input.endsWith("ｆ") || input.endsWith("Ｆ") || input.endsWith("f")) {
            input = input.substring(0, input.length() - 1) + "樓";
        }
        Pattern pattern = Pattern.compile("[\\-¯－－ ─ ?─―]");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            input = input.replace(matcher.group(),"之");
        }
        return input;
    }

    public static String extractNumericPart(String input) {
        if (StringUtils.isNotNullOrEmpty(input)) {
            Pattern numericPattern = Pattern.compile("[一二三四五六七八九十百千0-9]+");
            Matcher numericMatcher = numericPattern.matcher(input);
            if (numericMatcher.find()) {
                return numericMatcher.group();
            }
        }
        return "";
    }

    public static String padNumber(String comparisonValue, String numPart) {
        int zeroPaddingCount = Math.max(0, comparisonValue.length() - numPart.length());
        StringBuilder paddedNumBuilder = new StringBuilder();
        for (int i = 0; i < zeroPaddingCount; i++) {
            paddedNumBuilder.append("0");
        }
        paddedNumBuilder.append(numPart);
        return paddedNumBuilder.toString();
    }


    public static String experToValue(String exper) {
        String[] experArr = null;
        experArr = exper.split(encodeUnicode("+"));

        int value = 0;
        for (String sExper : experArr) {
            String[] sExperArr = sExper.split(encodeUnicode("*"));
            value += Integer.valueOf(sExperArr[0]) * Integer.valueOf(sExperArr[1]);
        }
        return String.valueOf(value);
    }

    private static String encodeUnicode(String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i : utfBytes) {
            String hexB = Integer.toHexString(i);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }


    private static final Map<String, String> regexMap = new LinkedHashMap<String, String>();
    static {
        String regex = NumRegex.getNumRegex() + encodeUnicode("百") + NumRegex.getNumRegex() + encodeUnicode("十") + NumRegex.getNumRegex();
        String exper = "{0}*100+{1}*10+{2}*1";
        regexMap.put(regex, exper);
        regex = NumRegex.getNumRegex() + encodeUnicode("百") + encodeUnicode("零") + NumRegex.getNumRegex();
        exper = "{0}*100+{1}*1";
        regexMap.put(regex, exper);
        regex = NumRegex.getNumRegex() + encodeUnicode("百") + NumRegex.getNumRegex();
        exper = "{0}*100+{1}*10";
        regexMap.put(regex, exper);
        regex = NumRegex.getNumRegex() + encodeUnicode("百");
        exper = "{0}*100";
        regexMap.put(regex, exper);
        regex = NumRegex.getNumRegex() + encodeUnicode("十") + NumRegex.getNumRegex();
        exper = "{0}*10+{1}*1";
        regexMap.put(regex, exper);
        regex = NumRegex.getNumRegex() + encodeUnicode("十");
        exper = "{0}*10";
        regexMap.put(regex, exper);
        regex = encodeUnicode("十") + NumRegex.getNumRegex();
        exper = "1*10+{0}*1";
        regexMap.put(regex, exper);
        regex = encodeUnicode("十");
        exper = "1*10";
        regexMap.put(regex, exper);
        regex = NumRegex.getNumRegex();
        exper = "{0}*1";
        regexMap.put(regex, exper);
    }

    static class NumRegex {
        public static final Map<String, String> numMap = new HashMap<String, String>();

        static {
            numMap.put("一", "1");
            numMap.put("二", "2");
            numMap.put("三", "3");
            numMap.put("四", "4");
            numMap.put("五", "5");
            numMap.put("六", "6");
            numMap.put("七", "7");
            numMap.put("八", "8");
            numMap.put("九", "9");
        }

        private static String numRegex;

        public static String getNumRegex() {
            if (numRegex == null || numRegex.length() == 0) {
                numRegex = "([";
                for (String s : numMap.keySet()) {
                    numRegex += encodeUnicode(s);
                }
                numRegex += "])";
            }
            return numRegex;
        }

    }

    private static String assembleString(String input, String newNum){
        Matcher numMatcher = Pattern.compile("[零壹貳叁肆伍陸柒捌玖拾佰一二三四五六七八九十百]+").matcher(input);
        if (numMatcher.find()) {
            int numStart = numMatcher.start();
            String prefix = input.substring(0, numStart);
            String suffix = input.substring(numStart + numMatcher.group().length());
            StringBuilder result = new StringBuilder();
            result.append(prefix);
            result.append(newNum);
            result.append(suffix);
            return result.toString();
        } else {
            return newNum;
        }
    }



    static class FullWidthNumRegex {
        public static final Map<String, String> fullWidthNumMap = new HashMap<String, String>();

        static {
            fullWidthNumMap.put("1", "１");
            fullWidthNumMap.put("2", "２");
            fullWidthNumMap.put("3", "３");
            fullWidthNumMap.put("4", "４");
            fullWidthNumMap.put("5", "５");
            fullWidthNumMap.put("6", "６");
            fullWidthNumMap.put("7", "７");
            fullWidthNumMap.put("8", "８");
            fullWidthNumMap.put("9", "９");
        }

        public static final Map<String, String> chineseNumMap = new HashMap<String, String>();

        /**五樓*/
        static {
            chineseNumMap.put("1", "一");
            chineseNumMap.put("2", "二");
            chineseNumMap.put("3", "三");
            chineseNumMap.put("4", "四");
            chineseNumMap.put("5", "五");
            chineseNumMap.put("6", "六");
            chineseNumMap.put("7", "七");
            chineseNumMap.put("8", "八");
            chineseNumMap.put("9", "九");
            chineseNumMap.put("10", "十");
        }

        public static String getFullWidthNumMap(String input) {
            Map<String, String> useMap = new HashMap<String, String>();
            if(input.contains("樓")){
                useMap = chineseNumMap;
            }else{
                useMap = fullWidthNumMap;
            }
            char[] chars = input.toCharArray();
            StringBuilder result =new StringBuilder("");
                for (char charr : chars) {
               if(useMap.containsKey(String.valueOf(charr))){
                   result.append(useMap.get(String.valueOf(charr)));
               }else{
                   result.append(charr);
               }
            }
            return result.toString();
        }
    }

    public static String replaceWithFullWidthNumber(String input){
        if(!StringUtils.isNullOrEmpty(input)){
            return  FullWidthNumRegex.getFullWidthNumMap(input);
        }
        return input;
    }
}
