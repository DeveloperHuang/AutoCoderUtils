package com.huang.auto.mapper;

import com.huang.auto.pojo.Demo;
import com.huang.auto.utils.SpringUtils;
import com.huang.auto.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by JianQiu on 2016/11/1.
 */
public class TestDemoMapper {

    private DemoMapper mapper ;

    @Before
    public void init(){
        mapper = SpringUtils.getBean(DemoMapper.class);
    }


    @Test
    public void testDeleteDemoById(){
        Demo demo = new Demo();
        demo.setId(2);
        mapper.deleteDemoById(demo);
        TestSelectAllDemo();
    }

    @Test
    public void testInsertDemo() {

        Date now = new Date();

        Demo demo = new Demo();
        demo.setName("张三");
        mapper.insertDemo(demo);

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
        mapper.insertDemo(demo);
        TestSelectAllDemo();

    }

    @Test
    public void testUpdateDemoById(){
        Demo demo = new Demo();
        demo.setId(2);
        demo.setTChar('z');
        demo.setTVarchar("updateVarchar");
        mapper.updateDemoById(demo);
        TestSelectAllDemo();
    }

    @Test
    public void testSelectDemoById(){
        Demo demo = new Demo();
        demo.setId(1);
        List<Demo> demoList = mapper.selectDemoById(demo);
        TestUtils.showList(demoList);
    }

    @Test
    public void TestSelectAllDemo(){
        List<Demo> demoList = mapper.selectAllDemos();
        TestUtils.showList(demoList);
    }



}
