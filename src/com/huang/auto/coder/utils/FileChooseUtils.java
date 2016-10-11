package com.huang.auto.coder.utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huang on 2016/10/11.
 */
public class FileChooseUtils {

    public Map<Object,File> lastOpenDirectoryMap = new HashMap<Object,File>();
    public Map<Object,File> lastSaveDirectoryMap = new HashMap<Object,File>();

    public File selectedFile(Component parentPanel,Object belongObject){
        File currentDirectory = null;
        if(lastOpenDirectoryMap.containsKey(belongObject)){
            currentDirectory = lastOpenDirectoryMap.get(belongObject);
        }
        return selectedFile(parentPanel,belongObject,currentDirectory);
    }

    public File selectedFile(Component parentPanel,Object belongObject,File currentDirectory){

        JFileChooser fileChooser = new JFileChooser();
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }
        int option = fileChooser.showOpenDialog(parentPanel);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getCurrentDirectory();
            lastOpenDirectoryMap.put(belongObject,file);
            return file;
        }
        return null;
    }

    public File saveFile(Component parentPanel,Object belongObject){
        return saveFile(parentPanel,belongObject,"");
    }

    public File saveFile(Component parentPanel,Object belongObject,String savaFileName){
        File currentDirectory = null;
        if(lastSaveDirectoryMap.containsKey(belongObject)){
            currentDirectory = lastSaveDirectoryMap.get(belongObject);
        }
        return saveFile(parentPanel,belongObject,currentDirectory,savaFileName);
    }

    public File saveFile(Component parentPanel, Object belongObject, File currentDirectory, String saveFileName){

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(saveFileName));
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }
        int option = fileChooser.showSaveDialog(parentPanel);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getCurrentDirectory();
            lastSaveDirectoryMap.put(belongObject,file);
            return file;
        }
        return null;
    }
}
