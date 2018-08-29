package com.huang.auto.coder.mybatis.service;

import com.huang.auto.coder.factory.MapperGenerateFactory;
import com.huang.auto.coder.swing.mybatis.MethodEnum;
import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.utils.DataBaseTableUtils;
import com.huang.auto.coder.swing.SwingConsole;
import com.huang.auto.coder.factory.pojo.Table;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joss on 2016/10/29.
 */
public class TestMapperFactory {
    DataBaseTableUtils dataBaseTableUtils ;
    private File beanfile;
    private File saveDirectory;
    private Table table ;
    private String interfaceName;
    private MapperGenerateFactory mapperFactory;

    @Before
    public void init(){

        beanfile = new File("E:\\workspace\\IDEAWorkspace\\MySelfPojo\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\pojo\\Product.java");
        saveDirectory = new File("E:\\workspace\\IDEAWorkspace\\MySelfPojo\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\mapper");
        dataBaseTableUtils = new DataBaseTableUtils("localhost","root","root");
        table = dataBaseTableUtils.loadTableInfomation("antarctic_crm","tbl_product");
        interfaceName = "ProductMapper";
        mapperFactory = new MapperGenerateFactory(saveDirectory,interfaceName,beanfile,table);
        initMethod();
    }

    public void initMethod(){
        List<String> paramList = new ArrayList<String>();
        List<String> paramListNoId = new ArrayList<String>();
        List<String> whereList = new ArrayList<String>();
        whereList.add("id");
        for(Column column : table.getColumns()){
            paramList.add(column.getFieldName());
            if(!"id".equals(column.getFieldName())){
                paramListNoId.add(column.getFieldName());
            }
        }


        mapperFactory.addMethod(MethodEnum.SELECT,"selectDemoById",paramList,whereList);
        mapperFactory.addMethod(MethodEnum.SELECT,"selectAllDemos",paramList,null);
        mapperFactory.addMethod(MethodEnum.INSERT,"insertDemo",paramListNoId,null);
        mapperFactory.addMethod(MethodEnum.UPDATE,"updateDemoById",paramListNoId,whereList);
        mapperFactory.addMethod(MethodEnum.DELETE,"deleteDemoById",null,whereList);
    }


    @Test
    public void testGetInterfaceContext(){
        String interfaceContext = mapperFactory.getInterfaceContext();
        showMessage(interfaceContext);
        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetXMLContext(){
        String xmlContext = mapperFactory.getXMLContext();
        showMessage(xmlContext);
        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void showMessage(String message){
        int width = 1400;
        int height = 1000;
        JFrame jFrame = new JFrame();

        JTextArea textArea = new JTextArea();
        textArea.setSize(width,height);
        textArea.setText(message);
        jFrame.setContentPane(textArea);
        SwingConsole.run(jFrame,width,height);
    }


}
