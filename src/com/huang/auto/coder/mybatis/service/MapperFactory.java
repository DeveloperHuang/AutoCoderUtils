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
            "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";
    private static Map<String,String> jdbcTypeMap = new HashMap<String,String>();
    static {
        jdbcTypeMap.put("int","INTEGER");
        jdbcTypeMap.put("double","DOUBLE");
        jdbcTypeMap.put("float","Float");
        jdbcTypeMap.put("varchar","VARCHAR");
        jdbcTypeMap.put("bigint","BIGINT");
        jdbcTypeMap.put("text","VARCHAR");
        jdbcTypeMap.put("char","CHAR");
        jdbcTypeMap.put("date","DATE");
        jdbcTypeMap.put("time","DATE");
        jdbcTypeMap.put("year","INTEGER");
        jdbcTypeMap.put("datetime","DATE");
        jdbcTypeMap.put("timestamp","DATE");
        jdbcTypeMap.put("longtext","VARCHAR");
    }

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
        addMethodInfo(methodEnum,methodInfo);
    }

    /**
     * 获取待生成的接口内容
     * @return 接口内容字符串
     */
    public String getInterfaceContext(){
        StringBuffer interfaceContextBuffer = new StringBuffer();

        String import_bean = JavaClassTransverter.builderImportByFile(beanFile);
        String packageMessage = JavaClassTransverter.builderPackageByFile(saveDirectory);
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
                methodContext.append("\tpublic "+resultContext+" "+methodInfo.methodName
                        +"("+beanClassName+" "+ StringTransverter.initialLowerCaseTransvert(beanClassName)+");\n");
            }
        }
        return methodContext.toString();
    }

    public String getXMLContext() {
        StringBuffer xmlContext = new StringBuffer();

        xmlContext.append(XML_HEAD);
        String packageContext = JavaClassTransverter.builderPackageContextByFile(saveDirectory);
        xmlContext.append("<mapper namespace=\""+saveDirectory+"\">\n");
        //TODO 待生成的内容如下

//          1:resultMap
        xmlContext.append(getResultMap());
//          2:parameterMap
        xmlContext.append(getParameterMapContext());
//          3:SELECT
        xmlContext.append(getSelectMethodContext());
//          4:INSERT
        xmlContext.append(getInsertMethodContext());
//          5:UPDATE
        xmlContext.append(getUpdateMethodContext());
//          6:DELETE
        xmlContext.append(getDeleteMethodContext());

        xmlContext.append("</mapper>");
        return xmlContext.toString();
    }

    private String getResultMap(){
        StringBuffer resultMapContext = new StringBuffer();
        String packageContext = JavaClassTransverter.builderPackageContextByFile(beanFile)+"."+JavaClassTransverter.getClassName(beanFile);
        resultMapContext.append("\t<resultMap type=\""+packageContext+"\"\n" +
                "\t\tid=\""+getResultMapId()+"\">\n");
        List<Column> columnList = beanTable.getColumns();
        for(Column column : columnList){
            resultMapContext.append("\t\t<result property=\""+column.getFieldName()+"\" column=\""+column.getFieldName()+"\"/>\n");
        }
        resultMapContext.append("\t</resultMap>\n");
        return resultMapContext.toString();
    }

    private String getParameterMapContext(){
        StringBuffer parameterMapContext = new StringBuffer();

        String packageContext = JavaClassTransverter.builderPackageContextByFile(beanFile)+"."+JavaClassTransverter.getClassName(beanFile);
        parameterMapContext.append("\t<parameterMap id=\""+getParameterMapId()+"\"\n" +
                "\t\ttype=\""+packageContext+"\">\n");
        List<Column> columnList = beanTable.getColumns();
        for(Column column : columnList){
            //TODO 编写jdbcType和数据库type的对应关系
            String jdbcType = jdbcTypeMap.get(column.getFieldType());
            parameterMapContext.append("\t\t<parameter property=\""+column.getFieldName()
                    +"\" jdbcType=\""+jdbcType+"\" />\n");
        }
        parameterMapContext.append("\t</parameterMap>\n");
        return parameterMapContext.toString();
    }

    private String getParameterMapId(){
        String paramMapId = JavaClassTransverter.getLowerClassName(beanFile)+"Param";
        return paramMapId;
    }

    private String getResultMapId(){
        String resultMapId = JavaClassTransverter.getLowerClassName(beanFile)+"Result";
        return resultMapId;
    }

    private String getSelectMethodContext(){
        StringBuffer selectMethodContext = new StringBuffer();
        List<MethodInfo> methodInfoList = methodInfoListMap.get(MethodEnum.SELECT);
        if(methodInfoList != null && methodInfoList.size() > 0){
            for(MethodInfo methodInfo : methodInfoList){

                List<Column> paramColumnList = methodInfo.paramColumnList;
                if(paramColumnList == null || paramColumnList.size() == 0){
                    continue;
                }
                //***************构造SELECT内容******************
                StringBuffer paramBuffer = new StringBuffer();
                for(int i = 0 ; i < paramColumnList.size() ; i++){
                    paramBuffer.append(paramColumnList.get(i).getFieldName());
                    if(i != paramColumnList.size()-1){
                        paramBuffer.append(",");
                    }
                }
                //***************构造SELECT内容完毕******************
                //***************构造WHERE内容******************
                StringBuffer whereBuffere = new StringBuffer();
                List<Column> whereColumnList = methodInfo.whereColumnList;
                if(whereColumnList != null && whereColumnList.size() > 0){
                    whereBuffere.append("\t\tWHERE ");
                    for(int i = 0 ; i < whereColumnList.size() ; i++){
                        //id = #{id}
                        whereBuffere.append(whereColumnList.get(i).getFieldName()
                                +" = #{"+whereColumnList.get(i).getFieldName()+"}");
                        if(i != whereColumnList.size()-1){
                            whereBuffere.append(" AND ");
                        }
                    }
                    whereBuffere.append("\n");
                }
                //***************构造WHERE内容完毕******************
                selectMethodContext.append("\t<select id=\""+methodInfo.methodName+"\" resultMap=\""+getResultMapId()+"\">\n");
                selectMethodContext.append("\t\tSELECT "+paramBuffer.toString()+"\n");
                selectMethodContext.append("\t\tFROM "+beanTable.getTableName()+"\n");
                selectMethodContext.append(whereBuffere.toString());
                selectMethodContext.append("\t</select>\n");
            }
        }
        return selectMethodContext.toString();
    }

    private String getInsertMethodContext(){
        StringBuffer insertMethodContext = new StringBuffer();
        List<MethodInfo> methodInfoList = methodInfoListMap.get(MethodEnum.INSERT);
        if(methodInfoList != null && methodInfoList.size() > 0){
            for(MethodInfo methodInfo : methodInfoList){
                List<Column> paramColumnList = methodInfo.paramColumnList;
                if(paramColumnList == null || paramColumnList.size() == 0){
                    continue;
                }
                StringBuffer columnBuffer = new StringBuffer();
                StringBuffer propertyBuffer = new StringBuffer();
                for (int i = 0; i < paramColumnList.size(); i++) {

                    String columnName = paramColumnList.get(i).getFieldName();
                    columnBuffer.append(columnName);
                    propertyBuffer.append("#{"+columnName+"}");
                    if (i != paramColumnList.size() - 1) {
                        columnBuffer.append(",");
                        propertyBuffer.append(",");
                    }
                }
                insertMethodContext.append("\t<insert id=\""+methodInfo.methodName+"\" parameterMap=\""+getParameterMapId()+"\">\n");
                insertMethodContext.append("\t\tINSERT INTO "+beanTable.getTableName()+"("+columnBuffer.toString()+")\n");
                insertMethodContext.append("\t\tVALUES("+propertyBuffer.toString()+")\n");
                insertMethodContext.append("\t</insert>\n");
            }
        }
        return insertMethodContext.toString();
    }

    private String getUpdateMethodContext(){
        StringBuffer updateMethodContext = new StringBuffer();
        List<MethodInfo> methodInfoList = methodInfoListMap.get(MethodEnum.UPDATE);
        if(methodInfoList != null && methodInfoList.size() > 0){
            for(MethodInfo methodInfo : methodInfoList){
                List<Column> paramColumnList = methodInfo.paramColumnList;
                if(paramColumnList == null || paramColumnList.size() == 0){
                    continue;
                }
                //******************构建设置参数********************
                StringBuffer paramBuffer = new StringBuffer();
                for(int i = 0 ; i < paramColumnList.size() ; i++){
                    String fieldName = paramColumnList.get(i).getFieldName();
                    paramBuffer.append(""+fieldName+" = #{"+fieldName+"}");
                    if(i != paramColumnList.size()-1){
                        paramBuffer.append(",");
                    }
                }
                //******************构建条件参数********************
                List<Column> whereColumnList = methodInfo.whereColumnList;
                StringBuffer whereBuffer = new StringBuffer();
                if(whereColumnList != null || whereColumnList.size() > 0){
                    whereBuffer.append("\t\tWHERE ");
                    for(int i = 0 ; i < whereColumnList.size() ; i++){
                        String fieldName = whereColumnList.get(i).getFieldName();
                        whereBuffer.append(fieldName+" = #{"+fieldName+"}");
                        if(i != whereColumnList.size()-1){
                            whereBuffer.append(" AND ");
                        }
                    }
                    whereBuffer.append("\n");
                }

                //******************组装********************
                updateMethodContext.append("\t<update id=\""+methodInfo.methodName+"\" parameterMap=\""+getParameterMapId()+"\">\n");
                updateMethodContext.append("\t\tUPDATE "+beanTable.getTableName()+"\n");
                updateMethodContext.append("\t\tSET "+paramBuffer.toString()+"\n");
                updateMethodContext.append(whereBuffer.toString());
                updateMethodContext.append("\t\t</update>\n");
            }
        }
        return updateMethodContext.toString();
    }

    private String getDeleteMethodContext(){
        StringBuffer deleteMethodContext = new StringBuffer();
        List<MethodInfo> methodInfoList = methodInfoListMap.get(MethodEnum.DELETE);
        if(methodInfoList != null && methodInfoList.size() > 0){
            for(MethodInfo methodInfo : methodInfoList){
                List<Column> whereColumnList = methodInfo.whereColumnList;
                StringBuffer whereBuffer = new StringBuffer();
                if(whereColumnList != null || whereColumnList.size() > 0){
                    whereBuffer.append("\t\tWHERE ");
                    for(int i = 0 ; i < whereColumnList.size() ; i++){
                        String fieldName = whereColumnList.get(i).getFieldName();
                        whereBuffer.append(fieldName+" = #{"+fieldName+"}");
                        if(i != whereColumnList.size()-1){
                            whereBuffer.append(" AND ");
                        }
                    }
                    whereBuffer.append("\n");
                }
                deleteMethodContext.append("\t<delete id=\""+methodInfo.methodName+"\" parameterMap=\""+getParameterMapId()+"\">\n");
                deleteMethodContext.append("\t\tDELETE FROM "+beanTable.getTableName()+"\n");
                deleteMethodContext.append(whereBuffer.toString());
                deleteMethodContext.append("\t</delete>\n");
            }
        }
        return deleteMethodContext.toString();
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
