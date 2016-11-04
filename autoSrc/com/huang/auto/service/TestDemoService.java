package com.huang.auto.service;

import com.huang.auto.pojo.Demo;
import com.huang.auto.utils.SpringUtils;
import com.huang.auto.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by JianQiu on 2016/11/2.
 */
public class TestDemoService {

    private DemoService service;

    @Before
    public void init(){
        service = SpringUtils.getBean(DemoService.class);
    }

    @Test
    public void testDeleteDemoById(){
        Demo demo = new Demo();
        demo.setId(6);
        service.deleteDemoById(demo);
        testSelectAllDemo();
    }

    @Test
    public void testInsertDemo() {

        Date now = new Date();

        Demo demo = new Demo();
        demo.setName("张三");
        service.insertDemo(demo);

        demo.setName("李四");
        demo.setTInt(123);
        demo.setTDouble(321.1231);
        demo.setTFloat(1231.2131f);
        demo.setTVarchar("varcharvarchar");
        demo.setTBigint(1020123213213213213l);
        demo.setTText("Text");
        demo.setTChar('d');

        demo.setTDate(now);
        demo.setTTime(now);
        demo.setTYear(2016);
        demo.setTDatetime(now);
        demo.setTTimestamp(now);
        demo.setTLongText("LongText");
        service.insertDemo(demo);
        testSelectAllDemo();

    }

    @Test
    public void testUpdateDemoById(){
        Demo demo = new Demo();
        demo.setId(6);
        demo.setTChar('z');
        demo.setTVarchar("updateVarchar");
        service.updateDemoById(demo);
        testSelectAllDemo();
    }

    @Test
    public void testSelectDemoById(){
        Demo demo = new Demo();
        demo.setId(1);
        List<Demo> demoList = service.selectDemoById(demo);
        TestUtils.showList(demoList);
    }

    @Test
    public void testSelectAllDemo(){
        List<Demo> demoList = service.selectAllDemos();
        TestUtils.showList(demoList);
    }


}
