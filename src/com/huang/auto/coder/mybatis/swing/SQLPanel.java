package com.huang.auto.coder.mybatis.swing;

import javax.swing.*;

/**
 * Created by JianQiu on 2016/10/10.
 */
public class SQLPanel {
    private JButton saveAllButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton saveCurrButton;
    private JPanel methodPanel;
    private JPanel methodListPanel;
    private JPanel methodManagerPanel;
    private JPanel parameterPanel;
    private JPanel wherePanel;
    private JList methodList;
    private JScrollPane methodScrollPane;
    private DefaultComboBoxModel listItems;

    public SQLPanel() {
        listItems = new DefaultComboBoxModel();
        methodList.setModel(listItems);
    }

    public DefaultComboBoxModel getListItems() {
        return listItems;
    }

    public void setListItems(DefaultComboBoxModel listItems) {
        this.listItems = listItems;
    }

    public JList getMethodList() {
        return methodList;
    }

    public void setMethodList(JList methodList) {
        this.methodList = methodList;
    }

    public JScrollPane getMethodScrollPane() {
        return methodScrollPane;
    }

    public void setMethodScrollPane(JScrollPane methodScrollPane) {
        this.methodScrollPane = methodScrollPane;
    }

    public JButton getSaveAllButton() {
        return saveAllButton;
    }

    public void setSaveAllButton(JButton saveAllButton) {
        this.saveAllButton = saveAllButton;
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

    public JButton getSaveCurrButton() {
        return saveCurrButton;
    }

    public void setSaveCurrButton(JButton saveCurrButton) {
        this.saveCurrButton = saveCurrButton;
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
