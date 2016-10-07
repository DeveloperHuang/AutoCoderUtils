package com.huang.auto.coder.utils;

import java.util.List;

/**
 * Created by huang on 2016/10/6.
 */
public class Table {
    private String tableName;
    private List<Column> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
