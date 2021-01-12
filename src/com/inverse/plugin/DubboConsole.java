package com.inverse.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

/**
 * @author keeper
 */
public class DubboConsole {

    private JPanel panel;
    private JComboBox classBox;
    private JComboBox methodBox;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JLabel host;
    private JLabel port;
    private JLabel classLabel;
    private JLabel methodLabel;
    private JTextArea inputTextArea;
    private JLabel inputLabel;
    private JTextArea outputTextArea;
    private JLabel outputLabel;
    private JButton invokeButton;
    private JButton connectButton;

    public DubboConsole(Project project, ToolWindow toolWindow) {
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JPanel getPanel() {
        return panel;
    }

    public JComboBox getComboBox1() {
        return classBox;
    }

    public JComboBox getComboBox2() {
        return methodBox;
    }

    public JTextField getHostTextField() {
        return hostTextField;
    }

    public JTextField getPortTextField() {
        return portTextField;
    }

    public JLabel getHost() {
        return host;
    }

    public JLabel getPort() {
        return port;
    }

    public JLabel getClassLabel() {
        return classLabel;
    }

    public JLabel getMethodLabel() {
        return methodLabel;
    }

    public JTextArea getTextArea1() {
        return inputTextArea;
    }

    public JLabel getInputLabel() {
        return inputLabel;
    }

    public JTextArea getTextArea2() {
        return outputTextArea;
    }

    public JLabel getOutputLabel() {
        return outputLabel;
    }

    public JButton getInvokeButton() {
        return invokeButton;
    }

    public JButton getConnectButton() {
        return connectButton;
    }
}
