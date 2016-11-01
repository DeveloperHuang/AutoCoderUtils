package com.huang.auto.coder.mybatis.service;

import com.huang.auto.coder.mybatis.swing.MethodEnum;
import com.huang.auto.coder.utils.Column;
import com.huang.auto.coder.utils.DataBaseTableUtils;
import com.huang.auto.coder.utils.SwingConsole;
import com.huang.auto.coder.utils.Table;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by JianQiu on 2016/10/29.
 */
public class TestMapperFactory {
    DataBaseTableUtils dataBaseTableUtils ;
    private File beanfile;
    private File saveDirectory;
    private Table table ;
    private String interfaceName;
    private MapperFactory mapperFactory;

    @Before
    public void init(){

        beanfile = new File("E:\\IntelliJWorkspace\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\pojo\\Demo.java");
        saveDirectory = new File("E:\\IntelliJWorkspace\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\mapper");
        dataBaseTableUtils = new DataBaseTableUtils("localhost","root","root");
        table = dataBaseTableUtils.loadTableInfomation("autocode","demo");
        interfaceName = "DemoMapper";
        mapperFactory = new MapperFactory(saveDirectory,interfaceName,beanfile,table);
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

    //TODO 测试方法是否可运行，增加测试数据库，测试source文件，

    private void showMessage(String message){
        int width = 1400;
        int height = 1000;
        JFrame jFrame = new JFrame();
        JPanel jPanel = new JPanel();

        JTextArea textArea = new JTextArea();
        textArea.setSize(width,height);
        textArea.setText(message);
        jFrame.setContentPane(textArea);
        SwingConsole.run(jFrame,width,height);
    }


}
