package com.huang.auto.coder.mybatis.swing;

import javax.swing.*;

/**
 * Created by JianQiu on 2016/10/10.
 */
public class SQLPanel {
    private JButton triggerAllWhere;
    private JButton addButton;
    private JButton deleteButton;
    private JButton triggerAllParam;
    private JPanel methodPanel;
    private JPanel methodListPanel;
    private JPanel methodManagerPanel;
    private JPanel parameterPanel;
    private JPanel wherePanel;
    private JList methodList;

    public SQLPanel() {
    }


    public JList getMethodList() {
        return methodList;
    }

    public void setMethodList(JList methodList) {
        this.methodList = methodList;
    }

    public JButton getTriggerAllWhere() {
        return triggerAllWhere;
    }

    public void setTriggerAllWhere(JButton triggerAllWhere) {
        this.triggerAllWhere = triggerAllWhere;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public void setAddButton(JButton addButton) {
        this.addButton = addButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public JButton getTriggerAllParam() {
        return triggerAllParam;
    }

    public void setTriggerAllParam(JButton triggerAllParam) {
        this.triggerAllParam = triggerAllParam;
    }

    public JPanel getMethodPanel() {
        return methodPanel;
    }

    public void setMethodPanel(JPanel methodPanel) {
        this.methodPanel = methodPanel;
    }

    public JPanel getMethodListPanel() {
        return methodListPanel;
    }

    public void setMethodListPanel(JPanel methodListPanel) {
        this.methodListPanel = methodListPanel;
    }

    public JPanel getMethodManagerPanel() {
        return methodManagerPanel;
    }

    public void setMethodManagerPanel(JPanel methodManagerPanel) {
        this.methodManagerPanel = methodManagerPanel;
    }

    public JPanel getParameterPanel() {
        return parameterPanel;
    }

    public void setParameterPanel(JPanel parameterPanel) {
        this.parameterPanel = parameterPanel;
    }

    public JPanel getWherePanel() {
        return wherePanel;
    }

    public void setWherePanel(JPanel wherePanel) {
        this.wherePanel = wherePanel;
    }
}
