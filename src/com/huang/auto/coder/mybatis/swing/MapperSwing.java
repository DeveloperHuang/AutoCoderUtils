package com.huang.auto.coder.mybatis.swing;

import com.huang.auto.coder.utils.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JTextField testMapperClassNameTextField;
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
    private FileChooseUtils fileChooseUtils;
    private File lastChooseBeanFile ;
    //每个方法类型对应方法组
    private Map<MethodEnum,RadioPanelGroupManager> methodListGroupManagerMap;
    //每个方法对应的参数组
    private Map<RadioPanelGroupManager.RadioPanelContainer,CheckBoxPanelGroupManager> parameterListGroupManagerMap;
    //每个方法对应的条件组
    private Map<RadioPanelGroupManager.RadioPanelContainer,CheckBoxPanelGroupManager> whereListGroupManagerMap;


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
        fileChooseUtils = new FileChooseUtils();
        methodListGroupManagerMap = new HashMap<MethodEnum, RadioPanelGroupManager>();
        parameterListGroupManagerMap = new HashMap<RadioPanelGroupManager.RadioPanelContainer, CheckBoxPanelGroupManager>();
        whereListGroupManagerMap = new HashMap<RadioPanelGroupManager.RadioPanelContainer, CheckBoxPanelGroupManager>();

        resetMethodManagerPanel();
//        MenuPanel.men
        initListener();

        initConfig();

    }

    private void initListener() {

        passwordTextField.addKeyListener(new PasswordKeyAdapter());
        connectButton.addActionListener(new ConnectButtionListener());
        chooseDataBaseComboBox.addItemListener(new DataBaseComboBoxItemStateListener());
        chooseTableComboBox.addItemListener(new TableComboBoxItemStateListener());

        chooseBeanButton.addActionListener(new ChooseBeanButtonListener());
        chooseMapperSaveLocalButton.addActionListener(new ChooseMapperButtonListener());
        chooseTestMapperLocalButton.addActionListener(new ChooseTestMapperButtonListener());
        chooseServiceSaveLocalButton.addActionListener(new ChooseServiceButtonListener());
        chooseTestServiceLocalButton.addActionListener(new ChooseTestServiceButtonListener());

        SQLTabbedPane.addChangeListener(new SQLTabbedPaneChangeListener());
    }

    public void initConfig(){
        String address = PropertiesUtils.getPropertiesValue("address","db");
        String username = PropertiesUtils.getPropertiesValue("username","db");
        String password = PropertiesUtils.getPropertiesValue("password","db");
        addressTextField.setText(address);
        usernameTextField.setText(username);
        passwordTextField.setText(password);
        passwordTextField.addKeyListener(new PasswordKeyAdapter());
    }

    //######################### 页面操作 #############################################

    /**
     * 重新设置方法管理页面
     * 1:获取该方法组的所有方法Panel
     * 2：将所有的Panel添加到对应的Panel
     * 3：获取该方法组选中的方法容器
     * 4：通过该容器获取所有的参数组和条件组
     * 5：将参数组和条件组添加到对应的Panel
     */
    public void resetMethodManagerPanel(){
        JPanel managerPanel = sqlPanel.getMethodManagerPanel();
        JPanel paramPanel = sqlPanel.getParameterPanel();
        JPanel wherePanel = sqlPanel.getWherePanel();
        JPanel methodListPanel = sqlPanel.getMethodListPanel();

        //选中的方法组对象
        JPanel selectedComponent = (JPanel) SQLTabbedPane.getSelectedComponent();
        if(selectedComponent == null){
            return;
        }
        //添加的同时会自动删除所属父级
        selectedComponent.add(managerPanel);
        //删除所有动态添加的组件
        paramPanel.removeAll();
        wherePanel.removeAll();
        methodListPanel.removeAll();

//        1:获取该方法组的所有方法Panel
        RadioPanelGroupManager currMethodManager;
        if(insertPanel == selectedComponent){
            currMethodManager = getMethodRadioGroupManager(MethodEnum.INSERT);
        }else if(updatePanel == selectedComponent){
            currMethodManager = getMethodRadioGroupManager(MethodEnum.UPDATE);
        }else if(deletePanel == selectedComponent){
            currMethodManager = getMethodRadioGroupManager(MethodEnum.DELETE);
        }else{
            //SELECT Panel
            currMethodManager = getMethodRadioGroupManager(MethodEnum.SELECT);
        }
//        2：将所有的Panel添加到对应的Panel
        List<RadioPanelGroupManager.RadioPanelContainer> currMethodContainerList = currMethodManager.getAllPanelContainer();
        for(RadioPanelGroupManager.RadioPanelContainer panelContainer : currMethodContainerList){
            methodListPanel.add(panelContainer.getPanel());
        }
//        3：获取该方法组选中的方法容器
//        4：通过该容器获取所有的参数组和条件组
//        5：将参数组和条件组添加到对应的Panel
        RadioPanelGroupManager.RadioPanelContainer selectedMethodContainer = currMethodManager.getSelectedPanelContainer();

        CheckBoxPanelGroupManager parameterGroupManager = parameterListGroupManagerMap.get(selectedMethodContainer);
        List<CheckBoxPanelGroupManager.CheckBoxPanelContainer> parameterContainerList = parameterGroupManager.getAllPanelContainer();
        for(CheckBoxPanelGroupManager.CheckBoxPanelContainer paramContainer : parameterContainerList){
            paramPanel.add(paramContainer.getPanel());
        }

        CheckBoxPanelGroupManager whereGroupManager = whereListGroupManagerMap.get(selectedMethodContainer);
        List<CheckBoxPanelGroupManager.CheckBoxPanelContainer> whereContainerList = whereGroupManager.getAllPanelContainer();
        for(CheckBoxPanelGroupManager.CheckBoxPanelContainer whereContainer : whereContainerList){
            wherePanel.add(whereContainer.getPanel());
        }

    }

    /**
     * 获取或创建MethodGroupManager
     * @param methodEnum
     * @return
     */
    private RadioPanelGroupManager getMethodRadioGroupManager(MethodEnum methodEnum){
        RadioPanelGroupManager methodManager;
        if(methodListGroupManagerMap.containsKey(methodEnum)){
            methodManager = methodListGroupManagerMap.get(methodEnum);
        }else{
            methodManager = new RadioPanelGroupManager();
            methodListGroupManagerMap.put(methodEnum,methodManager);
        }
        return methodManager;
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

    //TODO 添加方法：设置SQL页面参数（根据表属性，add按钮，生成参数集和条件集）


    //######################### 监听器 #############################################

    class PasswordKeyAdapter extends KeyAdapter{
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if(e.getKeyChar() == KeyEvent.VK_ENTER){
                connectButton.doClick();
            }
        }
    }

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

    class TableComboBoxItemStateListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            //如果选中表，则重新生成Bean Class 字符串
            if(e.getStateChange() == ItemEvent.SELECTED){
                String tableName = e.getItem().toString();
                String dataBaseName = chooseDataBaseComboBox.getSelectedItem().toString();
                table = dataBaseTableUtils.loadTableInfomation(dataBaseName,tableName);
                //TODO 如果存在上次的Bean目录，则搜索该目录的Bean文件
//                setBeanFilePathFieldText(tableName+".java");
            }
        }
    }


    class ChooseBeanButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.selectedFile(MapperSwing.this.mainPanel,chooseBeanButton);
            lastChooseBeanFile = file;
            beanFilePathTextField.setText(file.getPath());
        }
    }

    class ChooseMapperButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel,chooseMapperSaveLocalButton);
            mapperSaveLocalTextField.setText(file.getPath());
            if(chooseTableComboBox.getSelectedIndex() >= 0){
                String tableName = (String) chooseTableComboBox.getSelectedItem();
                String className = StringTransverter.initialUpperCaseTransvert(tableName)+"Mapper";
                String packageMessage = PackageFactory.builderJavaPackageByFile(file);
                mapperClassNameTextField.setText(className);
                mapperPackageTextField.setText(packageMessage);
            }
        }
    }

    class ChooseTestMapperButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel,chooseTestMapperLocalButton);
            testMapperSaveLocalTextField.setText(file.getPath());
            if(chooseTableComboBox.getSelectedIndex() >= 0){
                String tableName = (String) chooseTableComboBox.getSelectedItem();
                String className = StringTransverter.initialUpperCaseTransvert(tableName)+"MapperTest";
                String packageMessage = PackageFactory.builderJavaPackageByFile(file);
                testMapperClassNameTextField.setText(className);
                testMapperPackageTextField.setText(packageMessage);
            }
        }
    }

    class ChooseServiceButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel,chooseServiceSaveLocalButton);
            serviceSaveLocalTextField.setText(file.getPath());
            if(chooseTableComboBox.getSelectedIndex() >= 0){
                String tableName = (String) chooseTableComboBox.getSelectedItem();
                String className = StringTransverter.initialUpperCaseTransvert(tableName)+"Service";
                String packageMessage = PackageFactory.builderJavaPackageByFile(file);
                serviceClassNameTextField.setText(className);
                servicePackageTextField.setText(packageMessage);
            }
        }
    }

    class ChooseTestServiceButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel,chooseTestServiceLocalButton);
            testServiceSaveLocalTextField.setText(file.getPath());
            if(chooseTableComboBox.getSelectedIndex() >= 0){
                String tableName = (String) chooseTableComboBox.getSelectedItem();
                String className = StringTransverter.initialUpperCaseTransvert(tableName)+"ServiceTest";
                String packageMessage = PackageFactory.builderJavaPackageByFile(file);
                testServiceClassNameTextField.setText(className);
                testServicePackageTextField.setText(packageMessage);
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
