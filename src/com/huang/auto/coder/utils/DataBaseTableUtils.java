package com.huang.auto.coder.utils;

import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2016/10/3.
 */
public class DataBaseTableUtils {

    private Connection connection;
    private final String DATABASE_NAME = "information_schema";
    private boolean connectSuccessFlag = false;

    public DataBaseTableUtils() {
        String address = PropertiesUtils.getPropertiesValue("localhost","db");
        String username = PropertiesUtils.getPropertiesValue("username","db");
        String password = PropertiesUtils.getPropertiesValue("password","db");
        resetConnect(address,username,password);
    }

    public DataBaseTableUtils(String address,String username,String password){
        resetConnect(address,username,password);
    }

    public boolean resetConnect(String address,String username,String password){
        try {
            connection = JDBCUtils.getConnection(address,username,password,DATABASE_NAME);
            connectSuccessFlag = true;
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("error parame: address="+address+" username="+username+" password="+password);
        connectSuccessFlag = false;
        return false;
    }

    public boolean isConnectSuccessFlag() {
        return connectSuccessFlag;
    }

    public List<String> loadAllDataBase(){
        try {
            String sql = "SELECT SCHEMA_NAME FROM SCHEMATA";
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            List<String> dataBases = new ArrayList<String>();
            while(rs.next()){
                String dataBaseName = rs.getString("SCHEMA_NAME");
                if(!dataBaseName.contains("_schema")){
                    dataBases.add(dataBaseName);
                }
            }
            return dataBases;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> loadAllTables(){
        String sql = "SELECT TABLE_NAME FROM TABLES";
        return loadAllTablesBySQL(sql);
    }


    public  List<String> loadAllTables(String dataBaseName){
        String sql = "SELECT TABLE_NAME FROM TABLES WHERE TABLE_SCHEMA = '"+dataBaseName+"'";
        return loadAllTablesBySQL(sql);
    }

    private List<String> loadAllTablesBySQL(String sql){
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            List<String> tables = new ArrayList<String>();
            while(rs.next()){
                tables.add(rs.getString("TABLE_NAME"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Table loadTableInfomation(String dataBaseName,String tableName){
        Table table = new Table();
        table.setTableName(tableName);
        String sql = "SELECT COLUMN_NAME,DATA_TYPE FROM COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql);
            preparedStatement.setString(1,dataBaseName);
            preparedStatement.setString(2,tableName);
            ResultSet rs = preparedStatement.executeQuery();
            List<Column> columnList = new ArrayList<Column>();
            while(rs.next()){
                Column column = new Column();
                column.setFieldName(rs.getString("COLUMN_NAME"));
                column.setFieldType(rs.getString("DATA_TYPE"));
                columnList.add(column);
            }
            table.setColumns(columnList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }
}


