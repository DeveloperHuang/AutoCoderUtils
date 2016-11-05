package com.huang.auto.service;

import java.util.List;
import com.huang.auto.pojo.Demo;

public interface DemoService {

	public List<Demo> selectDemoById(Demo demo);
	public List<Demo> selectAllDemos();

	public void deleteDemoById(Demo demo);

	public void updateDemoById(Demo demo);

	public void insertDemo(Demo demo);

}