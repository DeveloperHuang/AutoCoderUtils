package com.huang.auto.coder.bean.service;

import com.huang.auto.coder.utils.Column;
import com.huang.auto.coder.utils.Table;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2016/10/6.
 */
public class JavaBeanTransverter {

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
        reflectTypeMap.put("date","Date");
        reflectTypeMap.put("time","Date");
        reflectTypeMap.put("year","Integer");
        reflectTypeMap.put("datetime","Date");
        reflectTypeMap.put("timestamp","Date");
        reflectTypeMap.put("longtext","String");
    }


    public static String transverterBeanClassString(String packageMessage, String className, Table table){

        StringBuffer head = new StringBuffer();

        StringBuffer methodMessage = new StringBuffer();
        StringBuffer attributeMessage = new StringBuffer();

        List<Column> columnList = table.getColumns();
        if(columnList != null){
            boolean alreadyAddDateImport = false;
            for(Column column : columnList){
                String javaType = getJavaTypeByDataBaseType(column.getFieldType());
                if(!alreadyAddDateImport && "Date".equals(javaType)){
                    head.append("import java.util.Date;\n");
                    alreadyAddDateImport = true;
                }
                methodMessage.append(transverterBeanMethodString(column.getFieldName(),javaType));
                attributeMessage.append(transverterBeanAttributeString(column.getFieldName(),javaType));
            }

        }

        String classMessage = packageMessage+"\n\n"+head+"\n\npublic class "+className+"{\n"+attributeMessage+"\n"+methodMessage+"\n}";
        return classMessage;
    }

    private static String transverterBeanMethodString(String fieldName,String javaType){
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
     * 获取Bean属性字符串
     * @param fieldName
     * @param javaType
     * @return
     */
    private static String transverterBeanAttributeString(String fieldName,String javaType){
        return "\tprivate "+javaType+" "+fieldName+";\n";
    }

    /**
     * 数据库类型和Java类型的映射。如果不匹配，返回String
     * @param dataBaseType
     * @return
     */
    private static String getJavaTypeByDataBaseType(String dataBaseType){
        if(reflectTypeMap.containsKey(dataBaseType)){
            return reflectTypeMap.get(dataBaseType);
        }else{
            return "String";
        }
    }
}
