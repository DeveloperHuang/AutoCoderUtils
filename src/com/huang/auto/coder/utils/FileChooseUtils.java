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
    public File defaultDirectory;
    /**
     * 选择文件
     * @param parentPanel 所属Panel
     * @param belongObject 所属对象，用于标识记录上次打开的路径
     * @return 选中的文件
     */
    public File selectedFile(Component parentPanel,Object belongObject){
        File currentDirectory = null;
        if(lastOpenDirectoryMap.containsKey(belongObject)){
            currentDirectory = lastOpenDirectoryMap.get(belongObject);
        }
        return selectedFile(parentPanel,belongObject,currentDirectory);
    }

    /**
     * 选择文件
     * @param parentPanel 所属Panel
     * @param belongObject 所属对象，用于标识记录上次打开的路径
     * @param currentDirectory 默认打开的目录
     * @return 选中的文件
     */
    public File selectedFile(Component parentPanel,Object belongObject,File currentDirectory){

        JFileChooser fileChooser = new JFileChooser();
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }else if(defaultDirectory != null){
            fileChooser.setCurrentDirectory(defaultDirectory);
        }
        int option = fileChooser.showOpenDialog(parentPanel);
        if(option == JFileChooser.APPROVE_OPTION){
            File fileDirectory = fileChooser.getCurrentDirectory();
            lastOpenDirectoryMap.put(belongObject,fileDirectory);
            defaultDirectory = fileDirectory;
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * 选择目录
     * @param parentPanel 所属Panel
     * @param belongObject 所属对象，用于标识记录上次打开的路径
     * @return 选中的目录
     */
    public File selectedDirectory(Component parentPanel,Object belongObject){
        File currentDirectory = null;
        if(lastOpenDirectoryMap.containsKey(belongObject)){
            currentDirectory = lastOpenDirectoryMap.get(belongObject);
        }
        return selectedDirectory(parentPanel,belongObject,currentDirectory);
    }

    /**
     * 选择目录
     * @param parentPanel 所属Panel
     * @param belongObject 所属对象，用于标识记录上次打开的路径
     * @param currentDirectory 默认打开的目录
     * @return 选中的目录
     */
    public File selectedDirectory(Component parentPanel,Object belongObject,File currentDirectory){

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }else if(defaultDirectory != null){
            fileChooser.setCurrentDirectory(defaultDirectory);
        }
        int option = fileChooser.showOpenDialog(parentPanel);
        if(option == JFileChooser.APPROVE_OPTION){
            File directory = fileChooser.getCurrentDirectory();
            lastOpenDirectoryMap.put(belongObject,directory);
            defaultDirectory = directory;
            return fileChooser.getSelectedFile();
        }
        return null;
    }


    /**
     * 打开目录选择器，获取保存的目录。获取待保存的目录
     * @param parentPanel 所属Panel
     * @param belongObject 所属对象，用于标识记录上次打开的路径
     * @return 选择的目录
     */
    public File saveDirectory(Component parentPanel, Object belongObject){
        File currentDirectory = null;
        if(lastSaveDirectoryMap.containsKey(belongObject)){
            currentDirectory = lastSaveDirectoryMap.get(belongObject);
        }
        return saveDirectory(parentPanel,belongObject,currentDirectory);
    }
    /**
     * 打开目录选择器，获取保存的目录。获取待保存的目录
     * @param parentPanel 所属Panel
     * @param belongObject 所属对象，用于标识记录上次打开的路径
     * @param currentDirectory 默认打开路径
     * @return 选择的目录
     */
    public File saveDirectory(Component parentPanel, Object belongObject, File currentDirectory){

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }else if(defaultDirectory != null){
            fileChooser.setCurrentDirectory(defaultDirectory);
        }
        int option = fileChooser.showSaveDialog(parentPanel);
        if(option == JFileChooser.APPROVE_OPTION){
            File directory = fileChooser.getCurrentDirectory();
            lastSaveDirectoryMap.put(belongObject,directory);
            defaultDirectory = directory;
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
