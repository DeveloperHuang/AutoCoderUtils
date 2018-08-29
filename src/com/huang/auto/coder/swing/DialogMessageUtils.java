package com.huang.auto.coder.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joss on 2016/10/11.
 */
public class DialogMessageUtils {

    public static final long DEFAULT_CLOSED_TIME = 2000;

    public static void sendMessage(JComponent component ,String message){
        sendMessage(component,message,DEFAULT_CLOSED_TIME);
    }

    public static void sendMessage(JComponent component ,String message,long closeTime){
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(component);
        dialog.add(new JLabel(message));
        dialog.setSize(100,60);
        dialog.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(closeTime);
                    dialog.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
