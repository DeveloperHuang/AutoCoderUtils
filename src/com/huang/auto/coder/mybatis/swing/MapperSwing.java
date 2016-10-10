package com.huang.auto.coder.mybatis.swing;

import com.huang.auto.coder.utils.SwingConsole;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

/**
 * Created by huang on 2016/10/6.
 */
public class MapperSwing {
    private JTextField addressTextField;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JComboBox chhooseDataBaseComboBox;
    private JComboBox chooseTableComboBox;
    private JTextField beanFileNameTextField;
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
    private JPanel seletePanel;
    private JPanel updatePanel;
    private JPanel deletePanel;
    private SQLPanel sqlPanel;

    private String[] params = new String[]{"id","name","message"};

    public MapperSwing() {
        sqlPanel = new SQLPanel();
        JPanel jpanel = sqlPanel.getMethodManagerPanel();
        System.out.println(jpanel);

//        MenuPanel.men
        SQLTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JPanel parameterPanel = sqlPanel.getParameterPanel();
                parameterPanel.removeAll();

                JPanel seletePanel = (JPanel) SQLTabbedPane.getSelectedComponent();


            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("MapperSwing");
        frame.setContentPane(new MapperSwing().mainPanel);
        SwingConsole.run(frame,800,800);
    }
}
