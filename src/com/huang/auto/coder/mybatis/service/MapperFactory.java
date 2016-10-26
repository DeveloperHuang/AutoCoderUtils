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

    private String packageMessage;
    private String interfaceName;
    private File beanFile;
    private Table beanTable;
    private Map<String,Column> tableColumnMap;
    private Map<MethodEnum,List<MethodInfo>> methodInfoListMap;


    public MapperFactory(String packageMessage, String interfaceName, File beanFile, Table beanTable) {
        this.packageMessage = packageMessage;
        this.interfaceName = interfaceName;
        this.beanFile = beanFile;
        this.beanTable = beanTable;

        methodInfoListMap = new HashMap<MethodEnum,List<MethodInfo>>();
        tableColumnMap = new HashMap<String, Column>();
        for(Column column : beanTable.getColumns()){
            tableColumnMap.put(column.getFieldName(),column);
        }
    }

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

    private List<Column> getColumnListByNameList(List<String> nameList){
        List<Column> columnList = new ArrayList<Column>();
        for(String name : nameList){
            columnList.add(tableColumnMap.get(name));
        }
        return columnList;
    }

}
