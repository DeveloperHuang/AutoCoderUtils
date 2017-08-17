package com.huang.auto.coder.utils;

import org.junit.Test;

/**
 * Created by sadwx on 17/6/16.
 */
public class StringTransverterTest {

    @Test
    public void lowerCamelCaseTest(){
        String message = "a_sf_sfa";
        System.out.println(StringTransverter.lowerCamelCase(message));
    }
}
