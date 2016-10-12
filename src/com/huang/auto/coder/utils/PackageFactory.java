package com.huang.auto.coder.utils;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by JianQiu on 2016/10/12.
 */
public class PackageFactory {

    public static final String SRC_PATH = File.separator+"src"+File.separator;
    public static final String TEST_PATH = File.separator+"test"+File.separator;

    public static String builderJavaPackageByFile(File file){
        String path;
        if (file.isDirectory()) {
            path = file.getPath();
        }else{
            path = file.getParentFile().getPath();
        }

        String sourceDir = "";
        if(path.contains(SRC_PATH)){
            sourceDir = SRC_PATH;
        }else if(path.contains(TEST_PATH)){
            sourceDir = TEST_PATH;
        }else{
            return null;
        }
        int lastIndex = path.lastIndexOf(sourceDir);
        String packagePath = path.substring(lastIndex+sourceDir.length());
        String packageMessage = "package "+packagePath.replaceAll(Matcher.quoteReplacement(File.separator),".")+";";
        return packageMessage;
    }

}
