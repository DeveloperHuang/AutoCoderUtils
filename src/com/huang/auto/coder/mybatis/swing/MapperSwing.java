package com.huang.auto.coder.mybatis.swing;

import com.huang.auto.coder.utils.DataBaseTableUtils;
import com.huang.auto.coder.utils.DialogMessageUtils;
import com.huang.auto.coder.utils.SwingConsole;
import com.huang.auto.coder.utils.Table;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.util.List;

/**
 * Created by huang on 2016/10/6.
 */
public class MapperSwing {
    private JTextField addressTextField;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JComboBox chooseDataBaseComboBox;
    private JComboBox chooseTableComboBox;
    private JTextField beanFilePathTextField;
    private JButton chooseBeanButton;
    private JTextField mapperSaveLocalTextField;
    private JButton chooseMapperSaveLocalButton;
    private JTextField mapperClassNameTextField;
    private JTextField testMapperSaveLocalTextField;
    private JButton chooseTestMapperLocalButton;
    private JPanel mainPanel;
    private JPanel connectConfigPanel;
    private JLabel addressLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel chooseDataBaseLabel;
    private JPanel choosePanel;
    private JLabel chooseTableLabel;
    private JLabel chooseBeanLabel;
    private JLabel mapperSaveLocalLabel;
    private JLabel mapperClassNameLabel;
    private JLabel testMapperSaveLocalLabel;
    private JTextField mapperPackageTextField;
    private JLabel mapperPackageLabel;
    private JButton mapperSaveButton;
    private JTextField testMapperClassNameFieldText;
    private JTextField testMapperPackageTextField;
    private JButton saveTestMapperButton;
    private JLabel testMapperClassNameLabel;
    private JLabel testMapperPackageLabel;
    private JPanel mapperPanel;
    private JTextField serviceSaveLocalTextField;
    private JButton chooseServiceSaveLocalButton;
    private JTextField serviceClassNameTextField;
    private JTextField servicePackageTextField;
    private JButton saveServiceButton;
    private JTextField testServiceSaveLocalTextField;
    private JButton chooseTestServiceLocalButton;
    private JTextField testServiceClassNameTextField;
    private JTextField testServicePackageTextField;
    private JButton saveTestServiceButton;
    private JLabel serviceSaveLocalLabel;
    private JLabel serviceClassNameLabel;
    private JLabel servicePackageLabel;
    private JLabel testServiceSaveLocalLabel;
    private JLabel testServiceClassNameLabel;
    private JLabel testServicePackageLabel;
    private JPanel servicePanel;
    private JButton connectButton;
    private JTabbedPane SQLTabbedPane;
    private JPanel insertPanel;
    private JPanel selectPanel;
    private JPanel updatePanel;
    private JPanel deletePanel;
    private SQLPanel sqlPanel;

    private String[] params = new String[]{"id","name","message","value","time","type","ip","energy","phone","flag"};
    private DataBaseTableUtils dataBaseTableUtils;
    private Table table;

    /*
     * 1：建立连接
     * 2：选择数据库和表
     * 3：选择Bean文件
     * 4：选择Mapper路径
     *  4.1 自动生成className（tableName+Mapper），package（搜索路径根地址（src））
     *  4.2 选择生成Test路径，自动生成className（tableName+Mapper+Test），package（搜索路径根地址（test、src））
     *  4.3 保留上次的Mapper路径，下次选择时直接打开该路径
     * 5：选择Service路径
     *  5.1 自动生成className（tableName+Service），package（搜索路径根地址（src））
     *  5.2 选择生成Test路径，自动生成className（tableName+ServiceImpl+Test），package（搜索路径根地址（test、src））
     *  5.3 保留上次的Mapper路径，下次选择时直接打开该路径
     * 6：设置添加方法列表
     *  6.1：生成默认的添加列表（CRUD）
     *  6.2：编辑列表
     * 7：保存Mapper和Test
     * 8：保存Service和Test
     *
     */

    public MapperSwing() {
        sqlPanel = new SQLPanel();
        resetMethodManagerPanel();
//        MenuPanel.men
        SQLTabbedPane.addChangeListener(new SQLTabbedPaneChangeListener());
        connectButton.addActionListener(new ConnectButtionListener());
        chooseDataBaseComboBox.addItemListener(new DataBaseComboBoxItemStateListener());
        chooseTableComboBox.addItemListener(new TableComboBoxItemStateListener());

    }

    //######################### 页面操作 #############################################

    /**
     * 重新设置方法管理页面
     */
    public void resetMethodManagerPanel(){
        JPanel managerPanel = sqlPanel.getMethodManagerPanel();
        JPanel paramPanel = sqlPanel.getParameterPanel();
        JPanel methodListPanel = sqlPanel.getMethodListPanel();
        JPanel wherePanel = sqlPanel.getWherePanel();

        JPanel selectedComponent = (JPanel) SQLTabbedPane.getSelectedComponent();
        if(selectedComponent == null){
            return;
        }
        //添加的同时会自动删除所属父级
        selectedComponent.add(managerPanel);
        paramPanel.removeAll();
        for(String name : params){
            paramPanel.add(new JCheckBox(name));
            wherePanel.add(new JCheckBox(name));
        }

//        methodListPanel.removeAll();
        if(insertPanel == selectedComponent){
            methodListPanel.add(new JLabel("INSERT"));
        }else if(updatePanel == selectedComponent){
            methodListPanel.add(new JLabel("UPDATE"));
        }else if(deletePanel == selectedComponent){
            methodListPanel.add(new JLabel("DELETE"));
        }else if(selectPanel == selectedComponent){
            methodListPanel.add(new JLabel("SELECT"));
        }
    }

    public void setDataBaseComboBox(List<String> dataBases){
        chooseDataBaseComboBox.removeAllItems();
        for(String dataBase : dataBases){
            chooseDataBaseComboBox.addItem(dataBase);
        }
    }

    public void setTableComboBox(List<String> tables){
        chooseTableComboBox.removeAllItems();
        for(String table : tables){
            chooseTableComboBox.addItem(table);
        }
    }

    public void setBeanFilePathFieldText(String filePath){
        beanFilePathTextField.setText(filePath);
    }

    public void sendDialogMessage(String message){
        DialogMessageUtils.sendMessage(this.mainPanel,message);
    }

    //######################### 逻辑 #############################################


    //######################### 监听器 #############################################

    /**
     * 1：建立连接
     * 2：加载数据库
     */
    class ConnectButtionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String address = addressTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            dataBaseTableUtils = new DataBaseTableUtils(address,username,password);
            if(dataBaseTableUtils.isConnectSuccessFlag()){
                sendDialogMessage("连接成功：");
                List<String> dataBases = dataBaseTableUtils.loadAllDataBase();
                setDataBaseComboBox(dataBases);
            }else{
                sendDialogMessage("连接失败：");
            }
        }
    }

    class TableComboBoxItemStateListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            //如果选中表，则重新生成Bean Class 字符串
            if(e.getStateChange() == ItemEvent.SELECTED){
                String tableName = e.getItem().toString();
                String dataBaseName = chooseDataBaseComboBox.getSelectedItem().toString();
                table = dataBaseTableUtils.loadTableInfomation(dataBaseName,tableName);
                //TODO 如果存在上次的Bean目录，则搜索该目录的Bean文件
                setBeanFilePathFieldText(tableName+".java");
            }
        }
    }

    class DataBaseComboBoxItemStateListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            //如果选中数据库，则重新加载表
            if(e.getStateChange() == ItemEvent.SELECTED){
                String dataBaseName = e.getItem().toString();
                List<String> tables = dataBaseTableUtils.loadAllTables(dataBaseName);
                setTableComboBox(tables);
            }

        }
    }

    class SQLTabbedPaneChangeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            resetMethodManagerPanel();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MapperSwing");
        frame.setContentPane(new MapperSwing().mainPanel);
        SwingConsole.run(frame,800,800);
    }
}
