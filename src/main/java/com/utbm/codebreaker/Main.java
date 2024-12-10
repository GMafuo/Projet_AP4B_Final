package com.utbm.codebreaker;

import com.utbm.codebreaker.gui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}