package com.huang.auto.coder.utils;

import com.huang.auto.coder.factory.JavaClassContextGenerator;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.io.File;

/** 
* JavaClassContextGenerator Tester.
* 
* @author <Authors name> 
* @version 1.0 
*/ 
public class PackageFactoryTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

    /** 
    * 
    * Method: generatePackageByFile(File file)
    * 
    */ 
    @Test
    public void testBuilderJavaPackageByFile() {
        File file = new File("abc"+File.separator+"ds"+File.separator+"src"+File.separator+"ds"+File.separator+"src"
                +File.separator+"com"+File.separator+"name"+File.separator+"da.java");
        String packageMessage = JavaClassContextGenerator.generatePackageByFile(file);
        System.out.println(packageMessage);
    }


} 
