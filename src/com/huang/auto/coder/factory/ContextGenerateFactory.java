package com.huang.auto.coder.factory;

import com.huang.auto.coder.factory.pojo.Method;
import com.huang.auto.coder.swing.mybatis.MethodEnum;
import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.factory.pojo.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
