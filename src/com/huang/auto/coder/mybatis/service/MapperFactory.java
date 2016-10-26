package com.huang.auto.coder.mybatis.service;

import com.huang.auto.coder.mybatis.swing.MethodEnum;
import com.huang.auto.coder.utils.Column;
import com.huang.auto.coder.utils.JavaClassTransverter;
import com.huang.auto.coder.utils.StringTransverter;
import com.huang.auto.coder.utils.Table;

import java.io.File;
import java.util.*;

/**
 * Created by JianQiu on 2016/10/26.
 * 目前查询默认返回结果为List
 *
 * 实现思路：
 * 1：添加方法信息
 * 2：生成Mapper接口信息
 *  2.1：生成SELECT方法（目前只返回集合）
 *  2.2：生成INSERT方法（目前只支持参数对象）
 *  2.3：生成UPDATE方法（目前只支持参数对象）
 *  2.4：生成DELETE方法（目前只支持参数对象）
 * 3：生成Mapper XML信息
 *  3.0：生成paramMap和resultMap
 *  3.1：生成SELECT方法（目前只返回集合）
 *  3.2：生成INSERT方法（目前只支持参数对象）
 *  3.3：生成UPDATE方法（目前只支持参数对象）
 *  3.4：生成DELETE方法（目前只支持参数对象）
 */
public class MapperFactory {

    public static final String IMPORT_LIST = "import java.util.List;";
    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \n" +
            "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";

    private File saveDirectory;
    private String interfaceName;
    private File beanFile;
    private Table beanTable;
    private Map<String,Column> tableColumnMap;
    private Map<MethodEnum,List<MethodInfo>> methodInfoListMap;


    public MapperFactory(File saveDirectory, String interfaceName, File beanFile, Table beanTable) {
        this.saveDirectory = saveDirectory;
        this.interfaceName = interfaceName;
        this.beanFile = beanFile;
        this.beanTable = beanTable;

        methodInfoListMap = new HashMap<MethodEnum,List<MethodInfo>>();
        tableColumnMap = new HashMap<String, Column>();
        for(Column column : beanTable.getColumns()){
            tableColumnMap.put(column.getFieldName(),column);
        }
    }

    /**
     * 添加待生成的方法
     * @param methodEnum 方法类型
     * @param methodName 方法名称
     * @param paramList 参数集合
     * @param whereList 条件集合
     */
    public void addMethod(MethodEnum methodEnum, String methodName, List<String> paramList,List<String> whereList){

        List<Column> paramColumnList = null;
        if(paramList != null){
            paramColumnList = getColumnListByNameList(paramList);
        }
        List<Column> whereColumnList = null;
        if(whereList != null){
            whereColumnList = getColumnListByNameList(whereList);
        }
        MethodInfo methodInfo = new MethodInfo(methodName,paramColumnList,whereColumnList);
    }

    /**
     * 获取待生成的接口内容
     * @return 接口内容字符串
     */
    public String getInterfaceContext(){
        StringBuffer interfaceContextBuffer = new StringBuffer();

        String import_bean = JavaClassTransverter.builderJavaPackageByFile(beanFile).replace("package","import");
        String packageMessage = JavaClassTransverter.builderJavaPackageByFile(saveDirectory);
        String head = packageMessage+"\n"+IMPORT_LIST+"\n"+ import_bean+"\n";
        interfaceContextBuffer.append(head);
        interfaceContextBuffer.append("public interface "+interfaceName+" {\n");
        interfaceContextBuffer.append(getInterfaceMethodContext());
        interfaceContextBuffer.append("}");

        return interfaceContextBuffer.toString();
    }

    /**
     * 获取接口方法内容
     * @return 方法内容字符串
     */
    private String getInterfaceMethodContext(){
        StringBuffer methodContext = new StringBuffer();
        Set<MethodEnum> methodEnumSet = methodInfoListMap.keySet();
        for(MethodEnum methodEnum : methodEnumSet){
            List<MethodInfo> methodInfoList = methodInfoListMap.get(methodEnum);
            String resultContext = null;
            String beanClassName = JavaClassTransverter.getClassName(beanFile);
            switch (methodEnum){
                case SELECT:
                    resultContext = "List<"+beanClassName+">";
                    break;
                default:
                    resultContext = "void";
                    break;
            }
            for(MethodInfo methodInfo : methodInfoList){
                methodContext.append("public "+resultContext+" "+methodInfo.methodName
                        +"("+beanClassName+" "+ StringTransverter.initialLowerCaseTransvert(beanClassName)+");\n");
            }
        }
        return methodContext.toString();
    }

    public String getXMLContext() {
        StringBuffer xmlContext = new StringBuffer();

        xmlContext.append(XML_HEAD);
        String packageMessage = JavaClassTransverter.builderJavaPackageByFile(saveDirectory);
        String simplePackage = packageMessage.replace("package","").replace(";","").trim();
        xmlContext.append("<mapper namespace=\""+simplePackage+"\">");
        //TODO 待生成的内容如下
        /*
         * 1:resultMap
         * 2:parameterMap
         * 3:SELECT
         * 4:INSERT
         * 5:UPDATE
         * 6:DELETE
         */
        return xmlContext.toString();
    }


    /**
     * 将方法信息存入内存中
     * @param methodEnum 方法类型
     * @param methodInfo 方法信息
     */
    private void addMethodInfo(MethodEnum methodEnum ,MethodInfo methodInfo){
        List<MethodInfo> methodInfoList ;
        if(methodInfoListMap.containsKey(methodEnum)){
            methodInfoList = methodInfoListMap.get(methodEnum);
        }else{
            methodInfoList = new ArrayList<>();
            methodInfoListMap.put(methodEnum,methodInfoList);
        }
        methodInfoList.add(methodInfo);
    }

    /**
     * 将参数名称和table的列一一对应
     * @param nameList 参数名称列表
     * @return 对应的Column列
     */
    private List<Column> getColumnListByNameList(List<String> nameList){
        List<Column> columnList = new ArrayList<Column>();
        for(String name : nameList){
            columnList.add(tableColumnMap.get(name));
        }
        return columnList;
    }

    class MethodInfo{
        private String methodName;
        private List<Column> paramColumnList;
        private List<Column> whereColumnList;

        public MethodInfo(String methodName, List<Column> paramColumnList, List<Column> whereColumnList) {
            this.methodName = methodName;
            this.paramColumnList = paramColumnList;
            this.whereColumnList = whereColumnList;
        }
    }

}
