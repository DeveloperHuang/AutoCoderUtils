package com.huang.auto.coder.mybatis.service;

import com.huang.auto.coder.mybatis.swing.MethodEnum;
import com.huang.auto.coder.utils.Column;
import com.huang.auto.coder.utils.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JianQiu on 2016/11/2.
 */
public abstract class BaseFactory {
    public static final String IMPORT_LIST = "import java.util.List;";
    protected File saveDirectory;
    protected String interfaceName;
    protected File beanFile;
    protected Table beanTable;
    protected Map<String,Column> tableColumnMap;
    protected Map<MethodEnum,List<MethodInfo>> methodInfoListMap;

    /**
     * 内容生成工厂
     * @param saveDirectory 保存路径
     * @param interfaceName 接口名称
     * @param beanFile JavaBean文件
     * @param beanTable bean对应的Table信息
     */
    public BaseFactory(File saveDirectory, String interfaceName, File beanFile, Table beanTable) {
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
        MethodInfo methodInfo = new MethodInfo(methodName,paramColumnList,whereColumnList);
        addMethodInfo(methodEnum,methodInfo);
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
