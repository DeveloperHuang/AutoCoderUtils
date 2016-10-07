package com.huang.auto.coder.bean.service;

import com.huang.auto.coder.utils.Column;
import com.huang.auto.coder.utils.DataBaseTableUtils;
import com.huang.auto.coder.utils.Table;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2016/10/6.
 */
public class JavaBeanTransverterTest {
    private DataBaseTableUtils utils = new DataBaseTableUtils("localhost","root","root");

    @Test
    public void testTransverterBeanClassString(){
        Table table = utils.loadTableInfomation("test","TestTableOne");
        String message = JavaBeanTransverter.transverterBeanClassString("package com.huang.auto.coder.bean.service;","TestTableOne",table);
        System.out.println(message);
    }

}
