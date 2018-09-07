package com.huang.auto.coder.factory;

import com.huang.auto.coder.factory.pojo.Method;
import com.huang.auto.coder.swing.mybatis.MethodEnum;
import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.utils.StringTransverter;
import com.huang.auto.coder.factory.pojo.Table;

import java.io.File;
import java.util.*;

/**
 * Created by Joss on 2016/10/26.
 * 目前查询默认返回结果为List
 * <p>
 * 实现思路：
 * 1：添加方法信息
 * 2：生成Mapper接口信息
 * 2.1：生成SELECT方法（目前只返回集合）
 * 2.2：生成INSERT方法（目前只支持参数对象）
 * 2.3：生成UPDATE方法（目前只支持参数对象）
 * 2.4：生成DELETE方法（目前只支持参数对象）
 * 3：生成Mapper XML信息
 * 3.0：生成paramMap和resultMap
 * 3.1：生成SELECT方法（目前只返回集合）
 * 3.2：生成INSERT方法（目前只支持参数对象）
 * 3.3：生成UPDATE方法（目前只支持参数对象）
 * 3.4：生成DELETE方法（目前只支持参数对象）
 */
public class MapperGenerateFactory extends ContextGenerateFactory {

    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \n" +
            "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";
    private static Map<String, String> jdbcTypeMap = new HashMap<String, String>();

    static {
        jdbcTypeMap.put("int", "INTEGER");
        jdbcTypeMap.put("double", "DOUBLE");
        jdbcTypeMap.put("float", "FLOAT");
        jdbcTypeMap.put("varchar", "VARCHAR");
        jdbcTypeMap.put("bigint", "BIGINT");
        jdbcTypeMap.put("text", "VARCHAR");
        jdbcTypeMap.put("char", "CHAR");
        jdbcTypeMap.put("date", "DATE");
        jdbcTypeMap.put("time", "DATE");
        jdbcTypeMap.put("year", "INTEGER");
        jdbcTypeMap.put("datetime", "DATE");
        jdbcTypeMap.put("timestamp", "DATE");
        jdbcTypeMap.put("longtext", "VARCHAR");
    }

    /**
     * Mapper相关内容生成工厂
     *
     * @param saveDirectory 保存路径
     * @param interfaceName 接口名称
     * @param beanFile      JavaBean文件
     * @param beanTable     bean对应的Table信息
     */
    public MapperGenerateFactory(File saveDirectory, String interfaceName, File beanFile, Table beanTable) {
        super(saveDirectory, interfaceName, beanFile, beanTable);
    }


    /**
     * 获取待生成的接口内容
     *
     * @return 接口内容字符串
     */
    public String getInterfaceContext() {
        StringBuffer interfaceContextBuffer = new StringBuffer();

        String import_bean = JavaClassContextGenerator.generateImportByFile(beanFile);
        String packageMessage = JavaClassContextGenerator.generatePackageByFile(saveDirectory);
        String head = packageMessage + "\n\n" + IMPORT_LIST + "\n" + import_bean + "\n\n";
        interfaceContextBuffer.append(head);
        interfaceContextBuffer.append("public interface " + interfaceName + " {\n\n");
        interfaceContextBuffer.append(getInterfaceMethodContext());
        interfaceContextBuffer.append("}");

        return interfaceContextBuffer.toString();
    }

    /**
     * 获取实现的内容
     *
     * @return 内容字符串
     */
    @Override
    public String getImplementsContext() {
        return getXMLContext();
    }

    /**
     * 获取MyBatis的的XML内容
     *
     * @return 内容字符串
     */
    public String getXMLContext() {
        StringBuffer xmlContext = new StringBuffer();

        xmlContext.append(XML_HEAD);
        String packageContext = JavaClassContextGenerator.generatePackageUrlByFile(saveDirectory) + "."
                + interfaceName;
        xmlContext.append("<mapper namespace=\"" + packageContext + "\">\n\n");

//          1:resultMap
        xmlContext.append(getResultMap());
        xmlContext.append("\n");
//          2:parameterMap
        xmlContext.append(getParameterMapContext());
        xmlContext.append("\n");
//          3:SELECT
        xmlContext.append(getSelectMethodContext());
        xmlContext.append("\n");
//          4:INSERT
        xmlContext.append(getInsertMethodContext());
        xmlContext.append("\n");
//          5:UPDATE
        xmlContext.append(getUpdateMethodContext());
        xmlContext.append("\n");
//          6:DELETE
        xmlContext.append(getDeleteMethodContext());
        xmlContext.append("\n");

        xmlContext.append("</mapper>");
        return xmlContext.toString();
    }

    private StringBuffer getResultMap() {
        StringBuffer resultMapContext = new StringBuffer();
        String packageContext = JavaClassContextGenerator.generatePackageUrlByFile(beanFile) + "." + JavaClassContextGenerator.getClassName(beanFile);
        resultMapContext.append("\t<resultMap type=\"" + packageContext + "\"\n" +
                "\t\tid=\"" + getResultMapId() + "\">\n");
        List<Column> columnList = beanTable.getColumns();
        for (Column column : columnList) {
            String lowerCamelFieldName = StringTransverter.lowerCamelCase(column.getFieldName());
            resultMapContext.append("\t\t<result property=\"" + lowerCamelFieldName + "\" column=\"" + column.getFieldName() + "\"/>\n");
        }
        resultMapContext.append("\t</resultMap>\n");
        return resultMapContext;
    }

    private StringBuffer getParameterMapContext() {
        StringBuffer parameterMapContext = new StringBuffer();

        String packageContext = JavaClassContextGenerator.generatePackageUrlByFile(beanFile) + "." + JavaClassContextGenerator.getClassName(beanFile);
        parameterMapContext.append("\t<parameterMap id=\"" + getParameterMapId() + "\"\n" +
                "\t\ttype=\"" + packageContext + "\">\n");
        List<Column> columnList = beanTable.getColumns();
        for (Column column : columnList) {
            String jdbcType = jdbcTypeMap.get(column.getFieldType());
            String lowerCamelFieldName = StringTransverter.lowerCamelCase(column.getFieldName());
            parameterMapContext.append("\t\t<parameter property=\"" + lowerCamelFieldName
                    + "\" jdbcType=\"" + jdbcType + "\" />\n");
        }
        parameterMapContext.append("\t</parameterMap>\n");
        return parameterMapContext;
    }

    private String getParameterMapId() {
        String paramMapId = JavaClassContextGenerator.getLowerClassName(beanFile) + "Param";
        return paramMapId;
    }

    private String getResultMapId() {
        String resultMapId = JavaClassContextGenerator.getLowerClassName(beanFile) + "Result";
        return resultMapId;
    }

    private StringBuffer getSelectMethodContext() {
        StringBuffer selectMethodContext = new StringBuffer();
        List<Method> methodList = methodInfoListMap.get(MethodEnum.SELECT);
        if (methodList != null && methodList.size() > 0) {
            for (Method method : methodList) {

                List<Column> paramColumnList = method.getParamColumnList();
                if (paramColumnList == null || paramColumnList.size() == 0) {
                    continue;
                }
                //***************构造SELECT内容******************
                StringBuffer paramBuffer = new StringBuffer();
                for (int i = 0; i < paramColumnList.size(); i++) {
                    paramBuffer.append(paramColumnList.get(i).getFieldName());
                    if (i != paramColumnList.size() - 1) {
                        paramBuffer.append(",");
                    }
                }
                //***************构造SELECT内容完毕******************
                //***************构造WHERE内容******************
                List<Column> whereColumnList = method.getWhereColumnList();
                StringBuffer whereSQLContext = getWhereIFSQLContext(whereColumnList);
                //***************构造WHERE内容完毕******************
                String paramMap = "";
                if (method.getWhereColumnList() != null && method.getWhereColumnList().size() > 0) {
                    paramMap = " parameterMap=\"" + getParameterMapId() + "\"";
                }
                selectMethodContext.append("\t<select id=\"" + method.getMethodName() + "\" resultMap=\"" + getResultMapId() + "\""
                        + paramMap + ">\n");
                selectMethodContext.append("\t\tSELECT " + paramBuffer.toString() + "\n");
                selectMethodContext.append("\t\tFROM " + beanTable.getTableName() + "\n");
                selectMethodContext.append(whereSQLContext);
                selectMethodContext.append("\t</select>\n");
            }
        }
        return selectMethodContext;
    }

    private StringBuffer getInsertMethodContext() {
        StringBuffer insertMethodContext = new StringBuffer();
        List<Method> methodList = methodInfoListMap.get(MethodEnum.INSERT);
        if (methodList != null && methodList.size() > 0) {
            for (Method method : methodList) {
                List<Column> paramColumnList = method.getParamColumnList();
                if (paramColumnList == null || paramColumnList.size() == 0) {
                    continue;
                }
                StringBuffer columnBuffer = new StringBuffer();
                StringBuffer propertyBuffer = new StringBuffer();
                for (int i = 0; i < paramColumnList.size(); i++) {

                    String columnName = paramColumnList.get(i).getFieldName();
                    String lowerCamelFieldName = StringTransverter.lowerCamelCase(columnName);
                    columnBuffer.append(columnName);
                    propertyBuffer.append("#{" + lowerCamelFieldName + "}");
                    if (i != paramColumnList.size() - 1) {
                        columnBuffer.append(",");
                        propertyBuffer.append(",");
                    }
                }
                insertMethodContext.append("\t<insert id=\"" + method.getMethodName() + "\" parameterMap=\"" + getParameterMapId() + "\">\n");
                insertMethodContext.append("\t\tINSERT INTO " + beanTable.getTableName() + "(" + columnBuffer.toString() + ")\n");
                insertMethodContext.append("\t\tVALUES(" + propertyBuffer.toString() + ")\n");
                insertMethodContext.append("\t</insert>\n");
            }
        }
        return insertMethodContext;
    }

    private StringBuffer getUpdateMethodContext() {
        StringBuffer updateMethodContext = new StringBuffer();
        List<Method> methodList = methodInfoListMap.get(MethodEnum.UPDATE);
        if (methodList != null && methodList.size() > 0) {
            for (Method method : methodList) {
                List<Column> paramColumnList = method.getParamColumnList();
                if (paramColumnList == null || paramColumnList.size() == 0) {
                    continue;
                }
                //******************构建设置参数********************
                StringBuffer paramBuffer = new StringBuffer();
                for (int i = 0; i < paramColumnList.size(); i++) {
                    String fieldName = paramColumnList.get(i).getFieldName();
                    String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                    paramBuffer.append("" + fieldName + " = #{" + lowerCamelFieldName + "}");
                    if (i != paramColumnList.size() - 1) {
                        paramBuffer.append(",");
                    }
                }
                //******************构建条件参数********************
                List<Column> whereColumnList = method.getWhereColumnList();
                StringBuffer whereSQLContext = getWhereSQLContext(whereColumnList);
                //******************组装********************
                updateMethodContext.append("\t<update id=\"" + method.getMethodName() + "\" parameterMap=\"" + getParameterMapId() + "\">\n");
                updateMethodContext.append("\t\tUPDATE " + beanTable.getTableName() + "\n");
                updateMethodContext.append("\t\tSET " + paramBuffer.toString() + "\n");
                updateMethodContext.append(whereSQLContext);
                updateMethodContext.append("\t</update>\n");
            }
        }
        return updateMethodContext;
    }

    private StringBuffer getDeleteMethodContext() {
        StringBuffer deleteMethodContext = new StringBuffer();
        List<Method> methodList = methodInfoListMap.get(MethodEnum.DELETE);
        if (methodList != null && methodList.size() > 0) {
            for (Method method : methodList) {
                List<Column> whereColumnList = method.getWhereColumnList();
                StringBuffer whereSQLContext = getWhereSQLContext(whereColumnList);
                deleteMethodContext.append("\t<delete id=\"" + method.getMethodName() + "\" parameterMap=\"" + getParameterMapId() + "\">\n");
                deleteMethodContext.append("\t\tDELETE FROM " + beanTable.getTableName() + "\n");
                deleteMethodContext.append(whereSQLContext);
                deleteMethodContext.append("\t</delete>\n");
            }
        }
        return deleteMethodContext;
    }

    private StringBuffer getWhereIFSQLContext(List<Column> whereColumnList) {
        StringBuffer whereBuffer = new StringBuffer();
        if (whereColumnList != null ) {
            if(whereColumnList.size() == 1 && whereColumnList.get(0).isPrimaryKey()){
                whereBuffer.append("\t\tWHERE ");
                String fieldName = whereColumnList.get(0).getFieldName();
                String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                whereBuffer.append(fieldName + " = #{" + lowerCamelFieldName + "}");
                whereBuffer.append("\n");
            }else if(whereColumnList.size() > 0){
                whereBuffer.append("\t\t<where>\n ");
                for (int i = 0; i < whereColumnList.size(); i++) {
                    String fieldName = whereColumnList.get(i).getFieldName();
                    String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                    //<if test="id != null and id != ''">
                    whereBuffer.append("\t\t\t<if test=\"")
                            .append(lowerCamelFieldName)
                            .append(" != null and ")
                            .append(lowerCamelFieldName)
                            .append(" != ''\">\n");
                    //AND id = #{id}
                    whereBuffer.append("\t\t\t\tAND ")
                            .append(fieldName).
                            append(" = #{").
                            append(lowerCamelFieldName).
                            append("}\n");
                    whereBuffer.append("\t\t\t</if>\n");
                }
                whereBuffer.append("\t\t</where>\n");
            }
        }
        return whereBuffer;
    }

    private StringBuffer getWhereSQLContext(List<Column> whereColumnList) {
        StringBuffer whereBuffer = new StringBuffer();
        if (whereColumnList != null && whereColumnList.size() > 0) {
            whereBuffer.append("\t\tWHERE ");
            for (int i = 0; i < whereColumnList.size(); i++) {
                String fieldName = whereColumnList.get(i).getFieldName();
                String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                whereBuffer.append(fieldName + " = #{" + lowerCamelFieldName + "}");
                if (i != whereColumnList.size() - 1) {
                    whereBuffer.append(" AND ");
                }
            }
            whereBuffer.append("\n");
        }
        return whereBuffer;
    }
}
