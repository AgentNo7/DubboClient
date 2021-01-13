package com.inverse.plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapperDialog;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.inverse.plugin.Tools.toList;
import static com.inverse.plugin.Tools.wrapErrorDialog;

/**
 * @author keeper
 */
public class DubboClient {

    private JPanel panel;
    private JComboBox<String> classBox;
    private JComboBox<String> methodBox;
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


    private DubboSocket dubboSocket;

    public DubboClient(Project project, ToolWindow toolWindow) {
        Tools.setProject(project);
        connectButton.addActionListener(e -> {
            String host = hostTextField.getText();
            String portText = portTextField.getText();
            Integer port = wrapErrorDialog(() -> Integer.parseInt(portText), "输入正确端口号");
            if (port == null) {
                return;
            }

            Object o = wrapErrorDialog(() -> {
                try {
                    connect(host, port);
                    return this;
                } catch (IOException ioException) {
                    throw new RuntimeException("连接失败");
                }
            }, "连接失败");
            if (o == null) {
                return;
            }

            try {
                String ls = dubboSocket.sendCommand("ls");
                List<String> interfaces = toList(ls);
                if (interfaces.size() == 0) {
                    return;
                }
                for (String anInterface : interfaces) {
                    classBox.addItem(anInterface);
                }
                String firstClassMethod = dubboSocket.sendCommand("ls " + interfaces.get(0));
                List<String> methodList = toList(firstClassMethod);
                for (String method : methodList) {
                    methodBox.addItem(method);
                }
            } catch (IOException ioException) {
                Tools.showErrorDialog("连接异常");
                throw new RuntimeException("连接异常");
            }

            classBox.addItemListener(e1 -> {
                String clazz = (String) e1.getItem();
                String classMethod;
                try {
                    classMethod = dubboSocket.sendCommand("ls " + clazz);
                } catch (IOException ioException) {
                    Tools.showErrorDialog("连接异常");
                    throw new RuntimeException("连接异常");
                }
                methodBox.removeAllItems();
                List<String> methodList = toList(classMethod);
                for (String method : methodList) {
                    methodBox.addItem(method);
                }
            });

            invokeButton.addActionListener(e2 -> {
                String clazzName = ((String) classBox.getSelectedItem()).trim();
                String methodName = ((String) methodBox.getSelectedItem()).trim();
                String classAndMethod = clazzName + "." + methodName;
                String args = inputTextArea.getText();
                String result;
                try {
                    String command = "invoke " + classAndMethod + "(" + args + ")";
                    result = dubboSocket.sendCommand(command);
                } catch (IOException ioException) {
                    Tools.showErrorDialog("连接异常");
                    throw new RuntimeException("连接异常");
                }
                outputTextArea.setText(result);
            });
        });
    }

    public void connect(String host, int port) throws IOException {
        dubboSocket = new DubboSocket(host, port);
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
