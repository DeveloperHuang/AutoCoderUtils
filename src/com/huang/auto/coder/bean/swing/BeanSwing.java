package com.huang.auto.coder.bean.swing;

import com.huang.auto.coder.bean.service.JavaBeanTransverter;
import com.huang.auto.coder.utils.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

/**
 * Created by huang on 2016/10/4.
 */
public class BeanSwing extends JFrame{
    private JComboBox<String> chooseTableComboBox;
    private JLabel chooseTableLable;
    private JLabel chooseLocalLabel;
    private JButton SaveButton;
    private JLabel showLabel;
    private JLabel classNameLable;
    private JTextField packageTextField;
    private JTextField classNameTextField;
    private JLabel packageLabel;
    private JPanel DataBasePanel;
    private JPanel chooseLocalPanel;
    private JPanel savePanel;
    private JPanel LocalPanel;
    private JPanel labelPanel;
    private JPanel classNamePanel;
    private JPanel packagePanel;
    private JPanel mainPanel;
    private JTextArea BeanTextArea;
    private JScrollPane BeanTextPanel;
    private JButton chooseLocalButton;
    private JTextField localUrlTextField;
    private JPanel configPanel;
    private JTextField addressField;
    private JTextField passwordField;
    private JLabel addressLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JButton connectButtion;
    private JComboBox<String> chooseDataBaseComboBox;
    private JLabel chooseDataBaseLabel;
    private JButton refreshBeanText;

    private JFileChooser fileChooser ;
    private File lastDirectory;
    private DataBaseTableUtils dataBaseTableUtils;

    public BeanSwing(){
        chooseLocalButton.addActionListener(new ChooseLocalButtonListener());
        chooseDataBaseComboBox.addItemListener(new DataBaseComboBoxItemStateListener());
        chooseTableComboBox.addItemListener(new TableComboBoxItemStateListener());
        connectButtion.addActionListener(new ConnectButtionListener());
        refreshBeanText.addActionListener(new RefreshTextButtonListener());
    }


    private String[] tableNames = new String[]{"device","user","id"};

    public void tryConnect(String address,String username,String password){
        dataBaseTableUtils = new DataBaseTableUtils(address,username,password);
        if(dataBaseTableUtils.isConnectSuccessFlag()){
            showDialogMessage("连接成功：");
            fillDataBaseComBoBoxFromDataBase();
        }else{
            //提示连接失败
            showDialogMessage("连接失败：");
        }
    }

    /**
     * 加载所有的数据库名称，并填充到数据库选项中
     */
    public void fillDataBaseComBoBoxFromDataBase(){
        List<String> dataBases = dataBaseTableUtils.loadAllDataBase();
        resetDataBaseComboBox(dataBases);
    }

    /**
     * 加载所有的数据表，并填充到数据表的选项中
     */
    public void fillTableComboBoxFromDataBase(String dataBaseName){
        List<String> tableNames = dataBaseTableUtils.loadAllTables(dataBaseName);
        resetTableComboBox(tableNames);
    }

    public void fillBeanTextAreaFromDataBase(String packageMessage,String className,String dataBaseName,String tableName){
        Table table = dataBaseTableUtils.loadTableInfomation(dataBaseName,tableName);
        String message = JavaBeanTransverter.transverterBeanClassString(packageMessage,className,table);
        rewriteBeanText(message);
    }

    /**
     * 重新设置DataBase下拉框中的内容
     * @param dataBaseNames
     */
    public void resetDataBaseComboBox(List<String> dataBaseNames){
        chooseDataBaseComboBox.removeAllItems();
        for(String dataBaseName : dataBaseNames){
            chooseDataBaseComboBox.addItem(dataBaseName);
        }
    }


    /**
     * 重新设置Table下拉框中的内容
     * @param tableNames
     */
    public void resetTableComboBox(List<String> tableNames){
        chooseTableComboBox.removeAllItems();
        for(String tableName : tableNames){
            chooseTableComboBox.addItem(tableName);
        }
    }

    /**
     * 重新设置BeanTextArea的内容
     * @param beanText
     */
    public void rewriteBeanText(String beanText){
        BeanTextArea.setText(beanText);
    }

    public void showDialogMessage(String message){
        DialogMessageUtils.sendMessage(this.mainPanel,message);
    }


    class ConnectButtionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String address = addressField.getText();
            String userName = usernameField.getText();
            String password = passwordField.getText();
            tryConnect(address,userName,password);
        }
    }

    FileChooseUtils fileChooseUtils ;

    class ChooseLocalButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(fileChooser == null ){
                fileChooser = new JFileChooser();

            }
            if(fileChooseUtils == null ){
                fileChooseUtils = new FileChooseUtils();
            }

//            if(lastDirectory != null){
//                fileChooser.setCurrentDirectory(lastDirectory);
//            }
//            String className = classNameTextField.getText();
//            fileChooser.setSelectedFile(new File(className+".java"));
//            int option = fileChooser.showSaveDialog(BeanSwing.this);
//            if(option == JFileChooser.APPROVE_OPTION){
//                File file = fileChooser.getCurrentDirectory();
//                localUrlTextField.setText(file.getAbsolutePath());
//            }else if(option == JFileChooser.CANCEL_OPTION){
//            }



            String className = classNameTextField.getText();
            File file = fileChooseUtils.saveFile(BeanSwing.this,chooseLocalButton,className+".java");
            if(file != null){
                localUrlTextField.setText(file.getAbsolutePath());
            }
            //存储上次打开的地址
//            lastDirectory = fileChooser.getCurrentDirectory();
        }
    }

    class TableComboBoxItemStateListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            //如果选中表，则重新生成Bean Class 字符串
            if(e.getStateChange() == ItemEvent.SELECTED){
                String tableName = e.getItem().toString();
                String className;
                if(tableName.length() > 1){
                    className = tableName.substring(0,1).toUpperCase()+tableName.substring(1);
                }else{
                    className = tableName.toUpperCase();
                }
                classNameTextField.setText(className);
                String packageMessage = packageTextField.getText();
                String dataBaseName = chooseDataBaseComboBox.getSelectedItem().toString();
                fillBeanTextAreaFromDataBase(packageMessage,className,dataBaseName,tableName);
            }
        }
    }

    class DataBaseComboBoxItemStateListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            //如果选中数据库，则重新加载表
            if(e.getStateChange() == ItemEvent.SELECTED){
                String dataBaseName = chooseDataBaseComboBox.getSelectedItem().toString();
                fillTableComboBoxFromDataBase(dataBaseName);
            }

        }
    }

    class RefreshTextButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String packageMessage = packageTextField.getText();
            String dataBaseName = chooseDataBaseComboBox.getSelectedItem().toString();
            String className = classNameTextField.getText();
            String tableName = chooseTableComboBox.getSelectedItem().toString();
            fillBeanTextAreaFromDataBase(packageMessage,className,dataBaseName,tableName);
        }
    }


        public static void main(String[] args) {
            JFrame frame = new JFrame("BeanSwing");
            frame.setContentPane(new BeanSwing().mainPanel);
            SwingConsole.run(frame,700,800);
        }
}
