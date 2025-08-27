package com.example.pentaho.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LittleDigitConvert {

    private static final char[] UPPER = {'零','一','二','三','四','五','六','七','八','九'};
    private static final char[] NUIT = {'十','百'};
    private static final char[] DIGIT = {'0','1','2','3','4','5','6','7','8','9'};
    private static final Map<Character, Character> mapU = new HashMap<Character, Character>();
    private static final Map<Character, Character> mapD = new HashMap<Character, Character>();
    private static final List<Character> listN = new ArrayList<Character>();
    private StringBuilder num;

    static {
        for(int i=0; i<DIGIT.length; i++){
            mapU.put(DIGIT[i], UPPER[i]);
            mapD.put(UPPER[i], DIGIT[i]);
        }
        for(int i=0; i<NUIT.length; i++){
            listN.add(NUIT[i]);
        }
    }

    /**
     * 阿拉伯数字转大写汉字
     * @param digit 待转换数字
     * @return
     */
    public String convertToUpper(String digit){
        num = new StringBuilder();
        String[] temp = digit.split("^0{0,}"); //形如0010转为10
        String removeZero = temp.length == 2 ? temp[1] : temp[0];
        //逆序循环，最终字符串需转换
        for(int i=removeZero.length()-1 , j=0; i>=0; i--, j++){
            switch (j) {
                case 0: num.append(mapU.get(removeZero.charAt(i)));break;
                case 4: num.append(NUIT[3]).append(mapU.get(removeZero.charAt(i)));break;//万
                case 8: num.append(NUIT[4]).append(mapU.get(removeZero.charAt(i)));break;//亿
                default:round(i, j%4, removeZero);//每4位一循环
            }
        }
        //转换后的字符串首位会出现多余的零，去除首位零
        if(num.charAt(0) == UPPER[0]){
            num.deleteCharAt(0);
        }
        //转换后的字符串中间会出现形如“壹佰零万”的情况，去除字符串中多余的零
        for(int i=0; i<num.length(); i++){
            if(num.charAt(i) == UPPER[0] &&
                    listN.contains(num.charAt(i-1)) &&
                    listN.contains(num.charAt(i+1))){
                num.deleteCharAt(i);
            }
        }
        //逆序转换
        return num.reverse().toString();
    }

    private void round(int i, int j, String removeZero){
        //出现连续两个0时，不作处理，直接返回
        if(num.charAt(num.length()-1) == UPPER[0] && removeZero.charAt(i) == DIGIT[0]){
            return;
        }

        if(removeZero.charAt(i)!=DIGIT[0]){
            switch (j) {
                case 0: num.append(NUIT[3]).append(mapU.get(removeZero.charAt(i)));break;//i=12 （万亿级）
                case 1: num.append(NUIT[0]).append(mapU.get(removeZero.charAt(i)));break;//拾
                case 2: num.append(NUIT[1]).append(mapU.get(removeZero.charAt(i)));break;//佰
                case 3: num.append(NUIT[2]).append(mapU.get(removeZero.charAt(i)));break;//仟
                default:num.append(mapU.get(removeZero.charAt(i)));break;
            }
        } else{
            num.append(mapU.get(removeZero.charAt(i)));
        }
    }

    /**
     * 小寫中文轉阿拉伯數字
     * @param upper 待轉換的小寫中文
     * @return
     */
    public static String convertToDigit(String upper){
        Pattern regexPattern = Pattern.compile(".*[^一二三四五六七八九]十[一二三四五六七八九]?.*|^十[一二三四五六七八九].*?|^十[^一二三四五六七八九].*?|^[^一二三四五六七八九]十.*?|十"); //特別處理11~19
        Matcher matcher = regexPattern.matcher(upper);
        if (matcher.matches() && upper.indexOf("十") == upper.lastIndexOf("十")) {
            int number = 10;
            String[] digits = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
            String[] digitValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
            for (int i = 0; i < digits.length; i++) {
                if (upper.contains(digits[i])) {
                    number += Integer.parseInt(digitValues[i]);
                }
            }
            return String.valueOf(number);
        }
        long digit = 1L;  //倍数
        long result = 0L; //最终结果
        long pre = 0L;    //前一次转换后的数
        long w_radix = 1L;//基数（万）
        long y_radix = 1L;//基数（亿)
        int w = 0; //记录万出现的次数
        int y = 0; //记录亿出现的次数
        int z = 0; //记录连续出现单位的次数（数组NUIT中的单位）
        for(int i=upper.length()-1; i>=0; i--){
            if(mapD.containsKey(upper.charAt(i)) && upper.charAt(i) != UPPER[0]){
                result = digit * Long.parseLong(String.valueOf(mapD.get(upper.charAt(i)))) + pre;
                pre = result;
                digit = 1L; //重置倍数
                z = 0;  //重置z
            } else if(listN.contains(upper.charAt(i))){
                if((++z) == 2) { //形如“壹佰亿”，连续出现单位字符时，需重置倍数
                    digit = 1L;
                }

                if(upper.charAt(i) == NUIT[0]){
                    /* 当数字大于亿小于万亿时，需特殊处理  */
                    digit = (w ==1 && y == 1) ? digit*10L*y_radix : digit*10L*w_radix*y_radix;
                }
                else if(upper.charAt(i) == NUIT[1]){
                    digit = (w ==1 && y == 1) ? digit*100L*y_radix : digit*100L*w_radix*y_radix;
                }
                else if(upper.charAt(i) == NUIT[2]){
                    digit = (w ==1 && y == 1) ? digit*1000L*y_radix : digit*1000L*w_radix*y_radix;
                }
                else if(upper.charAt(i) == NUIT[3]){
                    digit = (w ==1 && y == 1) ? digit*10000L*y_radix : digit*10000L;
                    w_radix = 10000L;
                    ++w;
                }
                else if(upper.charAt(i) == NUIT[4]){
                    digit *= 100000000L;
                    y_radix = 100000000L;
                    ++y;
                }
            }
        }
        System.out.println("String.valueOf(result): "+String.valueOf(result));
        return String.valueOf(result);
    }
}
