package com.huang.auto.coder.utils;

/**
 * Created by JianQiu on 2016/10/12.
 */
public class StringTransverter {

    public static String initialUpperCaseTransvert(String baseName){
        String result;
        if (baseName.length() > 1) {
            result = baseName.substring(0,1).toUpperCase()+baseName.substring(1);
        }else if(baseName.length() == 1){
            result = baseName.toUpperCase();
        }else{
            result = "";
        }

        return result;
    }

    public static String initialLowerCaseTransvert(String baseName){
        String result;
        if (baseName.length() > 1) {
            result = baseName.substring(0,1).toLowerCase()+baseName.substring(1);
        }else if(baseName.length() == 1){
            result = baseName.toLowerCase();
        }else{
            result = "";
        }

        return result;
    }
}
