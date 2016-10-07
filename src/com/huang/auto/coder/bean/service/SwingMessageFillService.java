package com.huang.auto.coder.bean.service;

import com.huang.auto.coder.bean.swing.BeanSwing;
import com.huang.auto.coder.utils.DataBaseTableUtils;
import com.huang.auto.coder.utils.Table;

import java.util.List;

/**
 * Created by huang on 2016/10/6.
 */
public class SwingMessageFillService {

    private BeanSwing beanSwing;

    private DataBaseTableUtils dataBaseTableUtils;

    public SwingMessageFillService(BeanSwing beanSwing){
        this.beanSwing = beanSwing;
    }


    public void tryConnect(String address,String username,String password){
        dataBaseTableUtils = new DataBaseTableUtils(address,username,password);
        if(dataBaseTableUtils.isConnectSuccessFlag()){
            beanSwing.resetDialogMessage("连接成功：");
            fillDataBaseComBoBoxFromDataBase();
        }else{
            //提示连接失败
            beanSwing.resetDialogMessage("连接失败：");
        }
    }

    /**
     * 加载所有的数据库名称，并填充到数据库选项中
     */
    public void fillDataBaseComBoBoxFromDataBase(){
        List<String> dataBases = dataBaseTableUtils.loadAllDataBase();
        beanSwing.resetDataBaseComboBox(dataBases);
    }

    /**
     * 加载所有的数据表，并填充到数据表的选项中
     */
    public void fillTableComboBoxFromDataBase(String dataBaseName){
        List<String> tableNames = dataBaseTableUtils.loadAllTables(dataBaseName);
        beanSwing.resetTableComboBox(tableNames);
    }

    public void fillBeanTextAreaFromDataBase(String packageMessage,String className,String dataBaseName,String tableName){
        Table table = dataBaseTableUtils.loadTableInfomation(dataBaseName,tableName);
        String message = JavaBeanTransverter.transverterBeanClassString(packageMessage,className,table);
        beanSwing.rewriteBeanText(message);
    }



}
