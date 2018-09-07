package com.huang.auto.coder.factory;

import com.huang.auto.coder.factory.pojo.Method;
import com.huang.auto.coder.swing.mybatis.MethodEnum;
import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.factory.pojo.Table;
import com.huang.auto.coder.utils.StringTransverter;

import java.io.File;
import java.util.*;

/**
 * Created by Joss on 2016/11/2.
 */
public abstract class ContextGenerateFactory {
    public static final String IMPORT_LIST = "import java.util.List;";
    public static final String IMPORT_SERVICE = "import org.springframework.stereotype.Service;";
    public static final String IMPORT_AUTOWIRED = "import org.springframework.beans.factory.annotation.Autowired;";

    protected File saveDirectory;
    protected String interfaceName;
    protected File beanFile;
    protected Table beanTable;
    protected Map<String,Column> tableColumnMap;
    protected Map<MethodEnum,List<Method>> methodInfoListMap;

    /**
     * 内容生成工厂
     * @param saveDirectory 保存路径
     * @param interfaceName 接口名称
     * @param beanFile JavaBean文件
     * @param beanTable bean对应的Table信息
     */
    public ContextGenerateFactory(File saveDirectory, String interfaceName, File beanFile, Table beanTable) {
        this.saveDirectory = saveDirectory;
        this.interfaceName = interfaceName;
        this.beanFile = beanFile;
        this.beanTable = beanTable;

        methodInfoListMap = new HashMap<MethodEnum,List<Method>>();
        tableColumnMap = new HashMap<String, Column>();
        for(Column column : beanTable.getColumns()){
            tableColumnMap.put(column.getFieldName(),column);
        }
    }

    /**
     * 添加方法
     * @param methodEnum 方法类型
     * @param methodName 方法名称
     * @param paramList 参数列表
     * @param whereList 条件列表
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
        Method method = new Method(methodName,paramColumnList,whereColumnList);
        addMethodInfo(methodEnum, method);
    }

    /**
     * 将方法信息存入内存中
     * @param methodEnum 方法类型
     * @param method 方法信息
     */
    private void addMethodInfo(MethodEnum methodEnum ,Method method){
        List<Method> methodList;
        if(methodInfoListMap.containsKey(methodEnum)){
            methodList = methodInfoListMap.get(methodEnum);
        }else{
            methodList = new ArrayList<>();
            methodInfoListMap.put(methodEnum, methodList);
        }
        methodList.add(method);
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

    /**
     * 获取接口内容
     * @return 内容字符串
     */
    public abstract String getInterfaceContext();

    /**
     * 获取实现的内容
     * @return 内容字符串
     */
    public abstract String getImplementsContext();

    /**
     * 获取接口方法内容
     *
     * @return 方法内容字符串
     */
    protected StringBuffer getInterfaceMethodContext() {
        StringBuffer methodContext = new StringBuffer();
        Set<MethodEnum> methodEnumSet = methodInfoListMap.keySet();
        String beanClassName = JavaClassContextGenerator.getClassName(beanFile);
        for (MethodEnum methodEnum : methodEnumSet) {
            List<Method> methodList = methodInfoListMap.get(methodEnum);
            for (Method method : methodList) {
                String currMethodContext = getInterfaceContextByMethod(method,methodEnum,beanClassName);
                methodContext.append(currMethodContext);
            }
            methodContext.append("\n");
        }
        return methodContext;
    }

    protected String getInterfaceContextByMethod(Method method,MethodEnum methodEnum,String beanClassName){
        String methodContext = null;

        String beanClassLowerName = StringTransverter.initialLowerCaseTransvert(beanClassName);
        String returnBeanClass = null;
        switch (methodEnum) {
            case SELECT:
                returnBeanClass = "List<" + beanClassName + ">";
                break;
            default:
                returnBeanClass = "void";
                break;
        }

        //如果没有查询条件，直接返回空参接口
        if (methodEnum != MethodEnum.INSERT && (method.getWhereColumnList() == null || method.getWhereColumnList().size() == 0)) {
            methodContext = ("\tpublic " + returnBeanClass + " " + method.getMethodName() + "();\n");
            return methodContext;
        }

        if (methodEnum == MethodEnum.SELECT) {
            //根据ID查询数据
            if (method.getWhereColumnList().size() == 1
                    && method.getWhereColumnList().get(0).isPrimaryKey()) {
                Column column = method.getWhereColumnList().get(0);
                String fieldType = column.getFieldType();
                String fieldName = column.getFieldName();
                //WHERE 条件使用小驼峰命名法获取参数
                String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                String javaType = JavaBeanFactory.getJavaTypeByDataBaseType(fieldType);

                methodContext = ("\tpublic " + beanClassName + " " + method.getMethodName()
                        + "(" + javaType + " " + lowerCamelFieldName + ");\n");
            } else {
                methodContext = ("\tpublic " + returnBeanClass + " " + method.getMethodName()
                        + "(" + beanClassName + " " + beanClassLowerName + ");\n");
            }
        } else if(methodEnum == MethodEnum.DELETE){
            //根据ID删除数据的接口
            if (method.getWhereColumnList().size() == 1
                    && method.getWhereColumnList().get(0).isPrimaryKey()) {
                Column column = method.getWhereColumnList().get(0);
                String fieldType = column.getFieldType();
                String fieldName = column.getFieldName();
                //WHERE 条件使用小驼峰命名法获取参数
                String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                String javaType = JavaBeanFactory.getJavaTypeByDataBaseType(fieldType);

                methodContext = ("\tpublic " + returnBeanClass + " " + method.getMethodName()
                        + "(" + javaType + " " + lowerCamelFieldName + ");\n");
            }
        }

        //通用接口
        if(methodContext == null){
            methodContext = ("\tpublic " + returnBeanClass + " " + method.getMethodName()
                    + "(" + beanClassName + " " + beanClassLowerName + ");\n");
        }

        return methodContext;

    }
}
