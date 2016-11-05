package com.huang.auto.mapper;

import java.util.List;
import com.huang.auto.pojo.Demo;





public interface DemoMapper {

    public void deleteDemoById(Demo demo);

    public void insertDemo(Demo demo);

    public void updateDemoById(Demo demo);

    public List<Demo> selectDemoById(Demo demo);
    public List<Demo> selectAllDemos();

}