package com.huang.auto.coder.swing.mybatis;

import com.huang.auto.coder.factory.ContextGenerateFactory;
import com.huang.auto.coder.factory.JavaClassContextGenerator;
import com.huang.auto.coder.factory.MapperGenerateFactory;
import com.huang.auto.coder.factory.ServiceGenerateFactory;
import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.factory.pojo.Table;
import com.huang.auto.coder.swing.CheckBoxPanelGroupManager;
import com.huang.auto.coder.swing.DialogMessageUtils;
import com.huang.auto.coder.swing.RadioPanelGroupManager;
import com.huang.auto.coder.swing.SwingConsole;
import com.huang.auto.coder.utils.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
    private JButton saveMapperButton;
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
    private JButton SELECTButton;
    private JButton DELETEButton;
    private JButton INSERTButton;
    private JButton UPDATEButton;
    private JPanel SQLMethodPanel;


    private JDialog dialog ;

    private SQLPanel sqlPanel;

    private DataBaseTableUtils dataBaseTableUtils;
    private FileChooseUtils fileChooseUtils;

    private File chooseBeanFile;
    private File mapperSaveDirector;
    private File serviceSaveDirector;

    //每个方法类型对应方法组
    private Map<MethodEnum,RadioPanelGroupManager> methodListGroupManagerMap;
    //每个方法对应的参数组
    private Map<RadioPanelGroupManager.RadioPanelContainer,CheckBoxPanelGroupManager> parameterListGroupManagerMap;
    //每个方法对应的条件组
    private Map<RadioPanelGroupManager.RadioPanelContainer,CheckBoxPanelGroupManager> whereListGroupManagerMap;

    private MethodRadioChangeListener radioChangeListener = new MethodRadioChangeListener();
    private SQLMethodButtonListener sqlMethodButtonListener = new SQLMethodButtonListener();

    //当前表信息
    private Table currBeanTable;
    private Map<String,String> columnMap;
    private MethodEnum currMethodEnum = MethodEnum.SELECT;//

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
//        refreshSQLPanelDialog();
//        MenuPanel.men
        initConfig();
        initListener();
        initSQLDialog();
    }

    private void initListener() {

        passwordTextField.addKeyListener(new PasswordKeyAdapter());
        connectButton.addActionListener(new ConnectButtionListener());
        chooseDataBaseComboBox.addItemListener(new DataBaseComboBoxItemStateListener());
        chooseTableComboBox.addItemListener(new TableComboBoxItemStateListener());

        chooseBeanButton.addActionListener(new ChooseBeanButtonListener());
        chooseMapperSaveLocalButton.addActionListener(new ChooseMapperButtonListener());
        saveMapperButton.addActionListener(new SaveMapperButtonListener());
        chooseTestMapperLocalButton.addActionListener(new ChooseTestMapperButtonListener());
        chooseServiceSaveLocalButton.addActionListener(new ChooseServiceButtonListener());
        saveServiceButton.addActionListener(new SaveServiceButtonListener());
        chooseTestServiceLocalButton.addActionListener(new ChooseTestServiceButtonListener());

        SELECTButton.addActionListener(sqlMethodButtonListener);
        INSERTButton.addActionListener(sqlMethodButtonListener);
        UPDATEButton.addActionListener(sqlMethodButtonListener);
        DELETEButton.addActionListener(sqlMethodButtonListener);

        JButton addButton = sqlPanel.getAddButton();
        JButton deleteButton = sqlPanel.getDeleteButton();
        JButton triggerAllParamButton = sqlPanel.getTriggerAllParam();
        JButton triggerAllWhereButton = sqlPanel.getTriggerAllWhere();

        addButton.addActionListener(new AddButtonActionListener());
        deleteButton.addActionListener(new DeleteButtonActionListener());
        triggerAllParamButton.addActionListener(new TriggerAllParamButtonActionListener());
        triggerAllWhereButton.addActionListener(new TriggerAllWhereButtonActionListener());

    }

    public void initConfig(){
        sqlPanel = new SQLPanel();
        fileChooseUtils = new FileChooseUtils();
        methodListGroupManagerMap = new HashMap<MethodEnum, RadioPanelGroupManager>();
        parameterListGroupManagerMap = new HashMap<RadioPanelGroupManager.RadioPanelContainer, CheckBoxPanelGroupManager>();
        whereListGroupManagerMap = new HashMap<RadioPanelGroupManager.RadioPanelContainer, CheckBoxPanelGroupManager>();

        String address = PropertiesUtils.getPropertiesValue("address","db");
        String username = PropertiesUtils.getPropertiesValue("username","db");
        String password = PropertiesUtils.getPropertiesValue("password","db");
        addressTextField.setText(address);
        usernameTextField.setText(username);
        passwordTextField.setText(password);

        columnMap = new HashMap<String, String>();
    }


    /**
     * 清除上个table对应的所有数据
     */
    public void clearMethodManager(){
        methodListGroupManagerMap.clear();
        parameterListGroupManagerMap.clear();
        whereListGroupManagerMap.clear();
    }

    public void initSQLDialog(){
        dialog = new JDialog();
        dialog.setLayout(new FlowLayout());
        dialog.add(sqlPanel.getMethodManagerPanel());
        dialog.setLocationRelativeTo(this.mainPanel);
        dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);


        Dimension dimension = sqlPanel.getMethodManagerPanel().getPreferredSize();
        dialog.setSize(dimension);
        dialog.setLocation((int)(dialog.getX()-dimension.getWidth()/2),(int)(dialog.getY()-dimension.getHeight()/2));
    }

    //######################### 页面操作 #############################################

    /**
     * 刷新SQLPanel弹窗
     * 1:获取该方法组的所有方法Panel
     * 2：将所有的Panel添加到对应的Panel
     * 3：获取该方法组选中的方法容器
     * 4：通过该容器获取所有的参数组和条件组
     * 5：将参数组和条件组添加到对应的Panel
     */
    public void refreshSQLPanelDialog(){
        JPanel managerPanel = sqlPanel.getMethodManagerPanel();
        JPanel paramPanel = sqlPanel.getParameterPanel();
        JPanel wherePanel = sqlPanel.getWherePanel();
        JPanel methodListPanel = sqlPanel.getMethodListPanel();



        paramPanel.removeAll();
        wherePanel.removeAll();
        methodListPanel.removeAll();

//        1:获取该方法组的所有方法Panel

        //当前方法组管理者
        MethodEnum methodEnum = currMethodEnum;
        RadioPanelGroupManager currMethodManager = getMethodRadioGroupManager(methodEnum);
        if(currMethodManager == null){
            currMethodManager = new RadioPanelGroupManager();
            methodListGroupManagerMap.put(methodEnum,currMethodManager);
        }

        //所属当前方法的Panel容器集合
        List<RadioPanelGroupManager.RadioPanelContainer> currMethodContainerList = currMethodManager.getAllPanelContainer();
//        2：将所有的方法集添加到方法列表Panel中
        for(RadioPanelGroupManager.RadioPanelContainer panelContainer : currMethodContainerList){
            methodListPanel.add(panelContainer.getPanel());
        }
//        3：获取该方法组选中的方法容器
        RadioPanelGroupManager.RadioPanelContainer selectedMethodContainer = currMethodManager.getSelectedPanelContainer();

        if(selectedMethodContainer != null) {
//            4：通过该容器获取所有的参数组和条件组
//        5：将参数组和条件组添加到对应的Panel
            CheckBoxPanelGroupManager parameterGroupManager = parameterListGroupManagerMap.get(selectedMethodContainer);
            if (parameterGroupManager != null) {
                List<CheckBoxPanelGroupManager.CheckBoxPanelContainer> parameterContainerList = parameterGroupManager.getAllPanelContainer();
                for (CheckBoxPanelGroupManager.CheckBoxPanelContainer paramContainer : parameterContainerList) {
                    paramPanel.add(paramContainer.getPanel());
                }
            }

            CheckBoxPanelGroupManager whereGroupManager = whereListGroupManagerMap.get(selectedMethodContainer);
            if (whereGroupManager != null) {
                List<CheckBoxPanelGroupManager.CheckBoxPanelContainer> whereContainerList = whereGroupManager.getAllPanelContainer();
                for (CheckBoxPanelGroupManager.CheckBoxPanelContainer whereContainer : whereContainerList) {
                    wherePanel.add(whereContainer.getPanel());
                }
            }
        }

        managerPanel.updateUI();
        dialog.remove(managerPanel);
        dialog.add(managerPanel);
        dialog.validate();
        dialog.repaint();
        dialog.setVisible(true);
//        selectedComponent.updateUI();
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

    public CheckBoxPanelGroupManager createColumnPanelGroupManager(){
        CheckBoxPanelGroupManager checkBoxPanelGroupManager = new CheckBoxPanelGroupManager();
        if(currBeanTable != null){
            for(Column column : currBeanTable.getColumns()){
                checkBoxPanelGroupManager.creatCheckBoxPanel(new JLabel(column.getFieldName()));
            }
        }
        return checkBoxPanelGroupManager;
    }

    /**
     * 给MyBatis代码构建工厂添加方法信息
     * @param buildFactory 实现了Mybatis构建工厂的实例
     */
    public void addAllMethodsToFactory(ContextGenerateFactory buildFactory){
        Set<MethodEnum> methodEnumSet = methodListGroupManagerMap.keySet();
        for(MethodEnum methodEnum : methodEnumSet){
            RadioPanelGroupManager methodGroupManager = methodListGroupManagerMap.get(methodEnum);
            List<RadioPanelGroupManager.RadioPanelContainer> methodContainerList = methodGroupManager.getAllPanelContainer();
            for(RadioPanelGroupManager.RadioPanelContainer methodContainer : methodContainerList){
                List<String> paramColumns = getSelectedColumnList(parameterListGroupManagerMap.get(methodContainer));
                List<String> whereColumns = getSelectedColumnList(whereListGroupManagerMap.get(methodContainer));
                JTextField methodNameField = (JTextField) methodContainer.getComponent();
                buildFactory.addMethod(methodEnum,methodNameField.getText(),paramColumns,whereColumns);
            }
        }
    }

    public List<String> getSelectedColumnList(CheckBoxPanelGroupManager manager){
        List<Component> columnListLable =manager.getSelectedComponent();
        List<String> columnList = new ArrayList<String>();
        for(Component component : columnListLable){
            JLabel label = (JLabel) component;
            columnList.add(label.getText());
        }
        return columnList;
    }


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
                currBeanTable = dataBaseTableUtils.loadTableInfomation(dataBaseName,tableName);
                //column快速匹配
                columnMap.clear();
                for(Column column : currBeanTable.getColumns()){
                    columnMap.put(column.getFieldName(),column.getFieldType());
                }
                clearMethodManager();

                String className = StringTransverter.initialUpperCaseTransvert(tableName)+"Mapper";
                mapperClassNameTextField.setText(className);
                //TODO 如果存在上次的Bean目录，则搜索该目录的Bean文件
//                setBeanFilePathFieldText(tableName+".java");
            }
        }
    }


    class ChooseBeanButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.selectedFile(MapperSwing.this.mainPanel,chooseBeanButton, chooseBeanFile);
            if(file != null){
                chooseBeanFile = file;
                beanFilePathTextField.setText(file.getName());
            }
        }
    }

    class ChooseMapperButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel,chooseMapperSaveLocalButton);
            if(file != null){
                mapperSaveDirector = file;
                mapperSaveLocalTextField.setText(mapperSaveDirector.getPath());
                if(chooseTableComboBox.getSelectedIndex() >= 0){
                    String packageMessage = JavaClassContextGenerator.generatePackageByFile(mapperSaveDirector);
                    mapperPackageTextField.setText(packageMessage);
                }
            }
        }
    }

    class SaveMapperButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(mapperSaveDirector != null && chooseBeanFile != null && currBeanTable != null){
                String interfaceName = mapperClassNameTextField.getText();
                MapperGenerateFactory mapperBuildFactory = new MapperGenerateFactory(mapperSaveDirector,interfaceName,chooseBeanFile,currBeanTable);
                addAllMethodsToFactory(mapperBuildFactory);
                String mapperInterfacePath = mapperSaveDirector.getPath()+File.separator+interfaceName+".java";
                String mapperXMLPath = mapperSaveDirector.getPath()+File.separator+interfaceName+".xml";
                File mapperInterfaceFile = new File(mapperInterfacePath);
                File mapperXMLFile = new File(mapperXMLPath);
                try {
                    FileIOUtils.writeJavaFile(mapperInterfaceFile,mapperBuildFactory.getInterfaceContext());
                    FileIOUtils.writeJavaFile(mapperXMLFile,mapperBuildFactory.getXMLContext());
                    sendDialogMessage("保存Mapper成功：");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    sendDialogMessage("保存Mapper失败：");
                }
            }
        }
    }

    class ChooseTestMapperButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel, chooseTestMapperLocalButton);
            if(file != null){
                testMapperSaveLocalTextField.setText(file.getPath());
                if(chooseTableComboBox.getSelectedIndex() >= 0){
                    String tableName = (String) chooseTableComboBox.getSelectedItem();
                    String className = StringTransverter.initialUpperCaseTransvert(tableName)+"MapperTest";
                    String packageMessage = JavaClassContextGenerator.generatePackageByFile(file);
                    testMapperClassNameTextField.setText(className);
                    testMapperPackageTextField.setText(packageMessage);
                }
            }
        }
    }

    class ChooseServiceButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel, chooseServiceSaveLocalButton);
            if(file != null){
                serviceSaveDirector = file;
                serviceSaveLocalTextField.setText(serviceSaveDirector.getPath());
                if(chooseTableComboBox.getSelectedIndex() >= 0){
                    String tableName = (String) chooseTableComboBox.getSelectedItem();
                    String className = StringTransverter.initialUpperCaseTransvert(tableName)+"Service";
                    String packageMessage = JavaClassContextGenerator.generatePackageByFile(serviceSaveDirector);
                    serviceClassNameTextField.setText(className);
                    servicePackageTextField.setText(packageMessage);
                }
            }
        }
    }

    class SaveServiceButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(serviceSaveDirector != null && mapperSaveDirector != null && chooseBeanFile != null && currBeanTable != null){
                String serviceInterfaceName = serviceClassNameTextField.getText();
                String mapperInterfaceName = mapperClassNameTextField.getText();
                ServiceGenerateFactory serviceBuildFactory = new ServiceGenerateFactory(serviceSaveDirector,serviceInterfaceName
                        ,mapperSaveDirector,mapperInterfaceName,chooseBeanFile,currBeanTable);
                addAllMethodsToFactory(serviceBuildFactory);
                String serviceInterfacePath = serviceSaveDirector.getPath()+File.separator+serviceInterfaceName+".java";
                String serviceImplementPath = serviceSaveDirector.getPath()+File.separator+serviceInterfaceName+"Impl.java";
                File serviceInterfaceFile = new File(serviceInterfacePath);
                File serviceImplementFile = new File(serviceImplementPath);
                try {
                    FileIOUtils.writeJavaFile(serviceInterfaceFile,serviceBuildFactory.getInterfaceContext());
                    FileIOUtils.writeJavaFile(serviceImplementFile,serviceBuildFactory.getImplementsContext());
                    sendDialogMessage("保存Service成功：");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    sendDialogMessage("保存Service失败：");
                }
            }
        }
    }

    class ChooseTestServiceButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileChooseUtils.saveDirectory(MapperSwing.this.mainPanel, chooseTestServiceLocalButton);
            if(file != null){
                testServiceSaveLocalTextField.setText(file.getPath());
                if(chooseTableComboBox.getSelectedIndex() >= 0){
                    String tableName = (String) chooseTableComboBox.getSelectedItem();
                    String className = StringTransverter.initialUpperCaseTransvert(tableName)+"ServiceTest";
                    String packageMessage = JavaClassContextGenerator.generatePackageByFile(file);
                    testServiceClassNameTextField.setText(className);
                    testServicePackageTextField.setText(packageMessage);
                }
            }
        }
    }

    class SQLMethodButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton methodButton = (JButton) e.getSource();
            if(methodButton == SELECTButton){
                currMethodEnum = MethodEnum.SELECT;
            }else if(methodButton == INSERTButton){
                currMethodEnum = MethodEnum.INSERT;
            }else if(methodButton == UPDATEButton){
                currMethodEnum = MethodEnum.UPDATE;
            }else if(methodButton == DELETEButton){
                currMethodEnum = MethodEnum.DELETE;
            }
            refreshSQLPanelDialog();
        }
    }

    class AddButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            MethodEnum methodEnum = currMethodEnum;
            //当前方法管理器
            RadioPanelGroupManager currMethodManager = methodListGroupManagerMap.get(methodEnum);
            //待添加的方法TextField
            JTextField methodTextField = new JTextField("defaultMethodName");
            methodTextField.setPreferredSize(new Dimension(100,25));
            //待添加的方法组件
            RadioPanelGroupManager.RadioPanelContainer addMethodPanelContainer =
                    currMethodManager.creatRadionPanel(methodTextField);

            //添加监听事件
            addMethodPanelContainer.getRadioButton().addChangeListener(radioChangeListener);

//            JPanel methodListPanel = sqlPanel.getMethodListPanel();
//            methodListPanel.add(radioPanelContainer.getPanel());

            // 初始化参数和条件
            CheckBoxPanelGroupManager paramPanelManager = createColumnPanelGroupManager();
            CheckBoxPanelGroupManager wherePanelManager = createColumnPanelGroupManager();
            parameterListGroupManagerMap.put(addMethodPanelContainer,paramPanelManager);
            whereListGroupManagerMap.put(addMethodPanelContainer,wherePanelManager);
            refreshSQLPanelDialog();
        }
    }

    class DeleteButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            MethodEnum methodEnum = currMethodEnum;
            RadioPanelGroupManager currMethodManager = methodListGroupManagerMap.get(methodEnum);
            if(currMethodManager == null){
                return ;
            }
            RadioPanelGroupManager.RadioPanelContainer currRadioContainer
                    = currMethodManager.removeSelectedPanelFromGroup();
            if(currRadioContainer != null){
                parameterListGroupManagerMap.remove(currRadioContainer);
                whereListGroupManagerMap.remove(currRadioContainer);
            }
            refreshSQLPanelDialog();
        }
    }



    class MethodRadioChangeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            JRadioButton jRadioButton = (JRadioButton) e.getSource();
            if(jRadioButton.isSelected()){
                refreshSQLPanelDialog();
            }
        }
    }

    class TriggerAllParamButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            RadioPanelGroupManager radioPanelGroupManager = methodListGroupManagerMap.get(currMethodEnum);
            RadioPanelGroupManager.RadioPanelContainer currMethodContainer = radioPanelGroupManager.getSelectedPanelContainer();
            if(currMethodContainer != null){
                CheckBoxPanelGroupManager paramGroupManager = parameterListGroupManagerMap.get(currMethodContainer);
                List<JCheckBox> allCheckBox = paramGroupManager.getAllCheckBox();
                for(JCheckBox checkBox : allCheckBox){
                    checkBox.setSelected(!checkBox.isSelected());
                }
                refreshSQLPanelDialog();
            }
        }
    }

    class TriggerAllWhereButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            RadioPanelGroupManager radioPanelGroupManager = methodListGroupManagerMap.get(currMethodEnum);
            RadioPanelGroupManager.RadioPanelContainer currMethodContainer = radioPanelGroupManager.getSelectedPanelContainer();
            if(currMethodContainer != null){
                CheckBoxPanelGroupManager whereGroupManager = whereListGroupManagerMap.get(currMethodContainer);
                List<JCheckBox> allCheckBox = whereGroupManager.getAllCheckBox();
                for(JCheckBox checkBox : allCheckBox){
                    checkBox.setSelected(!checkBox.isSelected());
                }
                refreshSQLPanelDialog();
            }
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("MapperSwing");
        frame.setContentPane(new MapperSwing().mainPanel);
        SwingConsole.run(frame,1400,800);
    }
}
