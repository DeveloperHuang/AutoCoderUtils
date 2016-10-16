package com.huang.auto.coder.utils;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JianQiu on 2016/10/15.
 * 每个对象管理一组Panel
 */
public class RadioPanelGroupManager {

    private Map<JRadioButton,RadioPanelContainer> panelContainerMap = new ConcurrentHashMap<JRadioButton, RadioPanelContainer>();
    private ButtonGroup buttonGroup = new ButtonGroup();

    public RadioPanelGroupManager(){

    }

    public JPanel creatRadionPanel(Component component){
        JPanel panel = new JPanel(new FlowLayout());
        JRadioButton radioButton = new JRadioButton();
        RadioPanelContainer panelContainer = new RadioPanelContainer(panel,radioButton,component);

        buttonGroup.add(radioButton);
        panelContainerMap.put(radioButton,panelContainer);
        panel.add(radioButton);
        panel.add(component);
        return panel;
    }

    /**
     * @return 返回选中的JPanel， null 没有选中的对象，
     */
    public JPanel getSelectedPanel(){
        RadioPanelContainer panelContainer = getSelectedPanelContainer();
        if(panelContainer != null){
            return panelContainer.getPanel();
        }else{
            return null;
        }
    }
    /**
     * @return 返回选中的组件（创建Panel时传入的组件）， null 没有选中的对象，
     */
    public Component getSelectedComponent(){
        RadioPanelContainer panelContainer = getSelectedPanelContainer();
        if(panelContainer != null){
            return panelContainer.getComponent();
        }else{
            return null;
        }
    }
    /**
     * @return 返回选中的RadioButton， null 没有选中的对象，
     */
    public JRadioButton getSelectedRadioButton(){
        RadioPanelContainer panelContainer = getSelectedPanelContainer();
        if(panelContainer != null){
            return panelContainer.getRadioButton();
        }else{
            return null;
        }
    }

    /**
     * @return 返回删除的JPanel， null 没有选中的对象，删除无效
     */
    public JPanel removeSelectedPanelFromGroup(){
        RadioPanelContainer panelContainer = getSelectedPanelContainer();
        if(panelContainer != null){
            panelContainerMap.remove(panelContainer.getRadioButton());
            buttonGroup.remove(panelContainer.getRadioButton());
            return panelContainer.getPanel();
        }else{
            return null;
        }
    }

    public RadioPanelContainer getSelectedPanelContainer(){

        JRadioButton radioButton = null ;

        Enumeration<AbstractButton> radioBtns=buttonGroup.getElements();
        while (radioBtns.hasMoreElements()) {
            AbstractButton btn = radioBtns.nextElement();
            if(btn.isSelected()){
                radioButton = (JRadioButton) btn;
                break;
            }
        }
        if(radioButton != null) {
            RadioPanelContainer panelContainer = panelContainerMap.get(radioButton);
            return panelContainer;
        }else{
            return null;
        }
    }

    /**
     *
     * @return 获取所有的容器
     */
    public List<RadioPanelContainer> getAllPanelContainer(){

        Collection<RadioPanelContainer> allContainer = panelContainerMap.values();
        List<RadioPanelContainer> allPanelContainer = new ArrayList<RadioPanelContainer>();
        for(RadioPanelContainer container : allContainer){
            allPanelContainer.add(container);
        }
        return allPanelContainer;
    }


    public class RadioPanelContainer {
        private JPanel panel;
        private Component component;
        private JRadioButton radioButton;

        public RadioPanelContainer() {
        }

        public RadioPanelContainer(JPanel panel, JRadioButton radioButton, Component component) {
            this.panel = panel;
            this.component = component;
            this.radioButton = radioButton;
        }

        public JPanel getPanel() {
            return panel;
        }

        private void setPanel(JPanel panel) {
            this.panel = panel;
        }

        public JRadioButton getRadioButton() {
            return radioButton;
        }

        private void setRadioButton(JRadioButton radioButton) {
            this.radioButton = radioButton;
        }

        public Component getComponent() {
            return component;
        }

        private void setComponent(Component component) {
            this.component = component;
        }
    }
}
