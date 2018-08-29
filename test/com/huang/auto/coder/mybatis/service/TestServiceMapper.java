package com.huang.auto.coder.mybatis.service;

import com.huang.auto.coder.factory.ServiceGenerateFactory;
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
 * Created by Joss on 2016/11/2.
 */
public class TestServiceMapper {

    DataBaseTableUtils dataBaseTableUtils ;
    private File beanfile;
    private File mapperSaveDirectory;
    private File serviceSaveDirectory;
    private Table table ;
    private String mapperInterfaceName;
    private String serviceInterfaceName;
    private ServiceGenerateFactory serviceFactory;

    @Before
    public void init(){

        beanfile = new File("E:\\IntelliJWorkspace\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\pojo\\Demo.java");
        mapperSaveDirectory = new File("E:\\IntelliJWorkspace\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\mapper");
        serviceSaveDirectory = new File("E:\\IntelliJWorkspace\\AutoCoderUtils\\autoSrc\\com\\huang\\auto\\service");
        dataBaseTableUtils = new DataBaseTableUtils("localhost","root","root");
        table = dataBaseTableUtils.loadTableInfomation("autocode","demo");
        mapperInterfaceName = "DemoMapper";
        serviceInterfaceName = "DemoService";
        serviceFactory = new ServiceGenerateFactory(serviceSaveDirectory,serviceInterfaceName,mapperSaveDirectory, mapperInterfaceName,beanfile,table);
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

        serviceFactory.addMethod(MethodEnum.SELECT,"selectDemoById",paramList,whereList);
        serviceFactory.addMethod(MethodEnum.SELECT,"selectAllDemos",paramList,null);
        serviceFactory.addMethod(MethodEnum.INSERT,"insertDemo",paramListNoId,null);
        serviceFactory.addMethod(MethodEnum.UPDATE,"updateDemoById",paramListNoId,whereList);
        serviceFactory.addMethod(MethodEnum.DELETE,"deleteDemoById",null,whereList);
    }

    @Test
    public void testGetInterfaceContext(){
        String interfaceContext = serviceFactory.getInterfaceContext();
        showMessage(interfaceContext);
        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetImplementsContext(){
        String implementsContext = serviceFactory.getImplementsContext();
        showMessage(implementsContext);
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
        JPanel jPanel = new JPanel();

        JTextArea textArea = new JTextArea();
        textArea.setSize(width,height);
        textArea.setText(message);
        jFrame.setContentPane(textArea);
        SwingConsole.run(jFrame,width,height);
    }

}
