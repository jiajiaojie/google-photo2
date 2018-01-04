package com.example.jiaojiejia.googlephoto.utils;

import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类型转换类
 * Created by guofengjia on 16/3/29.
 */
public class Format {

    /**
     * 判断是否是json字符串
     * @param str
     * @return
     */
    public static boolean checkJsonStr(String str){
        boolean flag=false;
        try {
            JSONArray array = new JSONArray(str);
            flag=true;
        } catch (JSONException e) {// 抛错 说明JSON字符不是数组或根本就不是JSON
            try {
                JSONObject object = new JSONObject(str);
                flag=true;

            } catch (JSONException e2) {// 抛错 说明JSON字符根本就不是JSON
                flag=false;
            }
        }catch(NullPointerException e3){
            flag=false;
        }
        return flag;
    }

    /**
     * 判断是否为http路径
     * @param s
     * @return
     */
    public static boolean isHttp(String s) {
        return !isEmpty(s) && s.matches("^((https|http)?://.*)$");
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断是否为空
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(SparseArray array) {
        return array == null || array.size() <= 0;
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();

    }

    /**
     * 判断是否为空
     *
     * @param objs
     * @return
     */
    public static boolean isEmpty(Object[] objs) {
        return objs == null || objs.length <= 0;
    }

    /**
     * 判断是否为空
     *
     * @param bytes
     * @return
     */
    public static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length <= 0;
    }

    public static boolean isEmpty(int[] ints) {
        return ints == null || ints.length <= 0;
    }

}
