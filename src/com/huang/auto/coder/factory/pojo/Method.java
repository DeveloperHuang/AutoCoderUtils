package com.huang.auto.coder.factory.pojo;

import java.util.List;

/**
 * Created by JianQiu on 2016/11/2.
 */
public class Method {
    private String methodName;
    private List<Column> paramColumnList;
    private List<Column> whereColumnList;

    public Method(String methodName, List<Column> paramColumnList, List<Column> whereColumnList) {
        this.methodName = methodName;
        this.paramColumnList = paramColumnList;
        this.whereColumnList = whereColumnList;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Column> getParamColumnList() {
        return paramColumnList;
    }

    public void setParamColumnList(List<Column> paramColumnList) {
        this.paramColumnList = paramColumnList;
    }

    public List<Column> getWhereColumnList() {
        return whereColumnList;
    }

    public void setWhereColumnList(List<Column> whereColumnList) {
        this.whereColumnList = whereColumnList;
    }
}
