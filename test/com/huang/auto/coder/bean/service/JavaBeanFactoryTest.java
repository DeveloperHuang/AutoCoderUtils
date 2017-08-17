package com.huang.auto.coder.bean.service;

import com.huang.auto.coder.factory.JavaBeanFactory;
import com.huang.auto.coder.utils.DataBaseTableUtils;
import com.huang.auto.coder.factory.pojo.Table;
import org.junit.Test;

/**
 * Created by huang on 2016/10/6.
 */
public class JavaBeanFactoryTest {
    private DataBaseTableUtils utils = new DataBaseTableUtils("localhost","root","root");

    @Test
    public void testTransverterBeanClassString(){
        Table table = utils.loadTableInfomation("test","TestTableOne");
        String message = JavaBeanFactory.generateBeanClassString("package com.huang.auto.coder.bean.service;","TestTableOne",table);
        System.out.println(message);
    }

}
