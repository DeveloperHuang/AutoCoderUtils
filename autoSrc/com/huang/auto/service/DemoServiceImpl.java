package com.huang.auto.service;

import java.util.List;
import com.huang.auto.mapper.DemoMapper;
import com.huang.auto.pojo.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService{

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public List<Demo> selectDemoById(Demo demo){
        return demoMapper.selectDemoById(demo);
    }
    @Override
    public List<Demo> selectAllDemos(){
        return demoMapper.selectAllDemos();
    }

    @Override
    public void deleteDemoById(Demo demo){
        demoMapper.deleteDemoById(demo);
    }

    @Override
    public void updateDemoById(Demo demo){
        demoMapper.updateDemoById(demo);
    }

    @Override
    public void insertDemo(Demo demo){
        demoMapper.insertDemo(demo);
    }

}