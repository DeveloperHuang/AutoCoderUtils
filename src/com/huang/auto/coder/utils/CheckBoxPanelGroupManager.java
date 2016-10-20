package com.huang.auto.coder.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JianQiu on 2016/10/15.
 * 每个对象管理一组Panel
 */
public class CheckBoxPanelGroupManager {

    private Map<JCheckBox,CheckBoxPanelContainer> panelContainerMap = new ConcurrentHashMap<JCheckBox, CheckBoxPanelContainer>();
    private Map<JCheckBox,Boolean> selectedCheckBoxMap = new ConcurrentHashMap<JCheckBox, Boolean>();
    private CheckBoxActionListener selectedListener = new CheckBoxActionListener();

    public CheckBoxPanelGroupManager(){

    }

    /**
     * 创建组装好的容器
     * @param component 组件
     * @return 组装了CheckBox的容器
     */
    public CheckBoxPanelContainer creatCheckBoxPanel(Component component){
        JPanel panel = new JPanel(new FlowLayout());
        JCheckBox checkBox = new JCheckBox();
        checkBox.addActionListener(selectedListener);
        CheckBoxPanelContainer panelContainer = new CheckBoxPanelContainer(panel,checkBox,component);

        panelContainerMap.put(checkBox,panelContainer);
        panel.add(checkBox);
        panel.add(component);
        return panelContainer;
    }

    /**
     *
     * @return 返回选中的Panel容器,如果没有则返回一个空的集合
     */
    public List<JPanel> getSelectedPanel(){
        List<JPanel> selectedPanel = new ArrayList<JPanel>();
        List<CheckBoxPanelContainer> selectPanelContainerList = getSelectedPanelContainer();
        for(CheckBoxPanelContainer selectPanelContainer : selectPanelContainerList){
            selectedPanel.add(selectPanelContainer.getPanel());
        }
        return selectedPanel;
    }

    /**
     *
     * @return 返回选中的组件,如果没有则返回一个空的集合
     */
    public List<Component> getSelectedComponent(){
        List<Component> selectedComponent = new ArrayList<Component>();
        List<CheckBoxPanelContainer> selectPanelContainerList = getSelectedPanelContainer();
        for(CheckBoxPanelContainer selectPanelContainer : selectPanelContainerList){
            selectedComponent.add(selectPanelContainer.getComponent());
        }
        return selectedComponent;
    }

    /**
     * @return 返回选中的checkBox,如果没有则返回一个空的集合
     */
    public List<JCheckBox> getSelectedCheckBox(){
        List<JCheckBox> selectedCheckBox = new ArrayList<JCheckBox>();
        List<CheckBoxPanelContainer> selectPanelContainerList = getSelectedPanelContainer();
        for(CheckBoxPanelContainer selectPanelContainer : selectPanelContainerList){
            selectedCheckBox.add(selectPanelContainer.getCheckBox());
        }
        return selectedCheckBox;
    }

    /**
     * 从管理器中删除选中的Panel容器
     * @return 删除的Panel容器
     */
    public List<JPanel> removeSelectedPanelFromGroup(){
        List<CheckBoxPanelContainer> selectPanelContainerList = getSelectedPanelContainer();
        List<JPanel> removedPanelList = new ArrayList<JPanel>();
        for(CheckBoxPanelContainer selectPanelContainer : selectPanelContainerList){
            panelContainerMap.remove(selectPanelContainer.getCheckBox());
            selectedCheckBoxMap.remove(selectPanelContainer.getCheckBox());
            removedPanelList.add(selectPanelContainer.getPanel());
        }

        return removedPanelList;
    }

    /**
     * @return 获取选中的容器
     */
    public List<CheckBoxPanelContainer> getSelectedPanelContainer(){
        Set<JCheckBox> checkBoxes = selectedCheckBoxMap.keySet();
        List<CheckBoxPanelContainer> selectPanelContainerList = new ArrayList<CheckBoxPanelContainer>();
        for(JCheckBox checkBox : checkBoxes){
            selectPanelContainerList.add(panelContainerMap.get(checkBox));
        }
        return selectPanelContainerList;
    }

    /**
     *
     * @return 获取所有的组装后的Panel,如果没有则返回一个空的集合
     */
    public List<JPanel> getAllPanel(){
        List<JPanel> allPanel = new ArrayList<JPanel>();
        List<CheckBoxPanelContainer> allPanelContainer = getAllPanelContainer();
        for(CheckBoxPanelContainer container : allPanelContainer){
            allPanel.add(container.getPanel());
        }
        return allPanel;
    }

    /**
     *
     * @return 获取所有的组件,如果没有则返回一个空的集合
     */
    public List<Component> getAllComponent(){
        List<Component> allComponents = new ArrayList<Component>();
        List<CheckBoxPanelContainer> allPanelContainer = getAllPanelContainer();
        for(CheckBoxPanelContainer container : allPanelContainer){
            allComponents.add(container.getComponent());
        }
        return allComponents;
    }

    /**
     *
     * @return 获取所有的CheckBox,如果没有则返回一个空的集合
     */
    public List<JCheckBox> getAllCheckBox(){
        List<JCheckBox> allCheckBox = new ArrayList<JCheckBox>();
        List<CheckBoxPanelContainer> allPanelContainer = getAllPanelContainer();
        for(CheckBoxPanelContainer container : allPanelContainer){
            allCheckBox.add(container.getCheckBox());
        }
        return allCheckBox;
    }


    /**
     *
     * @return 获取所有的容器,如果没有则返回一个空的集合
     */
    public List<CheckBoxPanelContainer> getAllPanelContainer(){
        Collection<CheckBoxPanelContainer> checkBoxPanelContainers = panelContainerMap.values();
        List<CheckBoxPanelContainer> allPanelContainerList = new ArrayList<CheckBoxPanelContainer>();
        for(CheckBoxPanelContainer container : checkBoxPanelContainers){
            allPanelContainerList.add(container);
        }
        return allPanelContainerList;
    }


    class CheckBoxActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            if(checkBox.isSelected()){
                selectedCheckBoxMap.put(checkBox,true);
            }else{
                selectedCheckBoxMap.remove(checkBox);
            }
        }
    }

    public void clear(){
        panelContainerMap.clear();
        selectedCheckBoxMap.clear();
        selectedListener = new CheckBoxActionListener();
    }

    public class CheckBoxPanelContainer {
        private JPanel panel;
        private Component component;
        private JCheckBox checkBox;

        public CheckBoxPanelContainer() {
        }

        public CheckBoxPanelContainer(JPanel panel, JCheckBox checkBox, Component component) {
            this.panel = panel;
            this.component = component;
            this.checkBox = checkBox;
        }

        public JPanel getPanel() {
            return panel;
        }

        private void setPanel(JPanel panel) {
            this.panel = panel;
        }

        public JCheckBox getCheckBox() {
            return checkBox;
        }

        private void setCheckBox(JCheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public Component getComponent() {
            return component;
        }

        private void setComponent(Component component) {
            this.component = component;
        }
    }
}
