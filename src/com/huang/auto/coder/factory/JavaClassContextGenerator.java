package com.huang.auto.coder.factory;

import com.huang.auto.coder.utils.StringTransverter;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by JianQiu on 2016/10/12.
 */
public class JavaClassContextGenerator {

    public static final String SRC_PATH = File.separator+"src"+File.separator;
    public static final String TEST_PATH = File.separator+"test"+File.separator;
    public static final String AUTO_TEST_PATH = File.separator+"autoSrc"+File.separator;

    public static final String[] SRC_PATHS = new String[] {SRC_PATH,TEST_PATH,AUTO_TEST_PATH};

    /**
     * 获取完整的package语句
     * @param file java文件或路径
     * @return package语句
     */
    public static String generatePackageByFile(File file){
        String packageContext = generatePackageContextByFile(file);
        if(packageContext != null && packageContext.length() > 0){
            return "package "+packageContext+";";
        }else{
            return null;
        }
    }

    public static String generateImportByFile(File file){
        String packageContext = generatePackageContextByFile(file);
        if(packageContext != null && packageContext.length() > 0){
            return "import "+packageContext+"."+getClassName(file)+";";
        }else{
            return null;
        }
    }

    /**
     * 获取package的内容（不包含package和;）
     * @param file java文件或路径
     * @return package中的路径内容
     */
    public static String generatePackageContextByFile(File file){
        String path;
        if (file.isDirectory()) {
            path = file.getPath();
        }else{
            path = file.getParentFile().getPath();
        }

        String sourceDir = "";
        boolean findSource = false;
        for(String srcPath : SRC_PATHS){
            if(path.contains(srcPath)){
                sourceDir = srcPath;
                findSource = true;
                break;
            }
        }
        if(!findSource){
            return null;
        }
        int lastIndex = path.lastIndexOf(sourceDir);
        String packagePath = path.substring(lastIndex+sourceDir.length());
        String packageMessage = packagePath.replaceAll(Matcher.quoteReplacement(File.separator),".");
        return packageMessage;
    }

    /**
     * 获取Java文件的ClassName
     * @return ClassName
     */
    public static String getClassName(File file){
        String fileName = file.getName();
        return fileName.substring(0,fileName.indexOf("."));
    }

    /**
     * 获取首字母小写的className
     * @param file java文件
     * @return 首字母小写的className
     */
    public static String getLowerClassName(File file){
        return StringTransverter.initialLowerCaseTransvert(getClassName(file));
    }

}
