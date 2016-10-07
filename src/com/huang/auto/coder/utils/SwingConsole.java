package com.huang.auto.coder.utils;

import javax.swing.*;

/**
 * Created by huang on 2016/8/6.
 */
public class SwingConsole {
    public static void run(final JFrame frame,int width,int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setTitle(frame.getClass().getSimpleName());
                frame.setSize(width,height);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
