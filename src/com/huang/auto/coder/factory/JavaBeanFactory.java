package com.huang.auto.coder.factory;

import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.utils.StringTransverter;
import com.huang.auto.coder.factory.pojo.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2016/10/6.
 */
public class JavaBeanFactory {

    /**
     * key 为DataBaseType
     * Value 为JavaType
     */
    private static Map<String,String> reflectTypeMap = new HashMap<String,String>();
    static {
        reflectTypeMap.put("int","Integer");
        reflectTypeMap.put("double","Double");
        reflectTypeMap.put("float","Float");
        reflectTypeMap.put("varchar","String");
        reflectTypeMap.put("bigint","Long");
        reflectTypeMap.put("text","String");
        reflectTypeMap.put("char","String");
        reflectTypeMap.put("date","String");
        reflectTypeMap.put("time","Date");
        reflectTypeMap.put("year","Integer");
        reflectTypeMap.put("datetime","String");
        reflectTypeMap.put("timestamp","Date");
        reflectTypeMap.put("longtext","String");
        reflectTypeMap.put("decimal","BigDecimal");
    }


    /**
     * 生成Java Bean的内容
     * @param packageMessage package内容
     * @param className class名称
     * @param table 数据库表信息
     * @return
     */
    public static String generateBeanClassString(String packageMessage, String className, Table table){

        StringBuffer head = new StringBuffer();
        head.append("import java.io.Serializable;\n");
        StringBuffer methodMessage = new StringBuffer();
        StringBuffer attributeMessage = new StringBuffer();
        attributeMessage.append("\tprivate static final long serialVersionUID = 1L;\n");

        List<Column> columnList = table.getColumns();
        if(columnList != null){
            boolean alreadyAddDateImport = false;
            boolean alreadyAddBigDecimalImport = false;
            for(Column column : columnList){
                String javaType = getJavaTypeByDataBaseType(column.getFieldType());
                if(!alreadyAddDateImport && "Date".equals(javaType)){
                    head.append("import java.util.Date;\n");
                    alreadyAddDateImport = true;
                }
                if(!alreadyAddBigDecimalImport && "BigDecimal".equals(javaType)){
                    head.append("import java.math.BigDecimal;\n");
                    alreadyAddBigDecimalImport = true;
                }
                String fieldName = StringTransverter.lowerCamelCase(column.getFieldName());
                methodMessage.append(generateBeanMethodString(fieldName,javaType));
                attributeMessage.append(generateBeanAttributeString(fieldName,javaType));
            }

        }

        String classMessage = packageMessage+"\n\n"+head+"\n\npublic class "+className+
                " implements Serializable{\n"+attributeMessage+"\n"+methodMessage+"\n}";
        return classMessage;
    }

    /**
     * 生成JavaBean的get set方法内容
     * @param fieldName
     * @param javaType
     * @return
     */
    private static String generateBeanMethodString(String fieldName, String javaType){
        String fieldMethodName;
        if(fieldName.length() > 1){
            fieldMethodName = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
        }else{
            fieldMethodName = fieldName.toUpperCase();
        }
        StringBuffer methodMessage = new StringBuffer();
        methodMessage.append("\tpublic void set"+fieldMethodName+" ("+javaType+" "+fieldName+"){\n");
        methodMessage.append("\t\tthis."+fieldName+" = "+fieldName+";\n");
        methodMessage.append("\t}\n");
        methodMessage.append("\tpublic "+javaType+" get"+fieldMethodName+" (){\n");
        methodMessage.append("\t\treturn this."+fieldName+";\n");
        methodMessage.append("\t}\n");
        return methodMessage.toString();
    }

    /**
     * 生成Bean属性字符串
     * @param fieldName bean名称
     * @param javaType get set 方法
     * @return
     */
    private static String generateBeanAttributeString(String fieldName, String javaType){
        return "\tprivate "+javaType+" "+fieldName+";\n";
    }

    /**
     * 数据库类型和Java类型的映射。如果不匹配，返回String
     * @param dataBaseType
     * @return
     */
    public static String getJavaTypeByDataBaseType(String dataBaseType){
        if(reflectTypeMap.containsKey(dataBaseType)){
            return reflectTypeMap.get(dataBaseType);
        }else{
            return "String";
        }
    }
}
