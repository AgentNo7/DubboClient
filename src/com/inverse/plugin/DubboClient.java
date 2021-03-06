package com.inverse.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

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

    private static DubboClient instance;

    public static DubboClient getInstance() {
        return instance;
    }


    public DubboSocket dubboSocket;

    public DubboClient(Project project, ToolWindow toolWindow) {
        Tools.setProject(project);
        connectButton.addActionListener(e -> {
            String host = hostTextField.getText();
            String portText = portTextField.getText();
            Integer port = wrapErrorDialog(() -> Integer.parseInt(portText), "输入正确端口号");
            if (port == null) {
                return;
            }

            synchronized (DubboClient.class) {
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
            }

            Future<ExecuteResult<String>> ls1 = dubboSocket.sendCommand("ls");
            String ls = Tools.blockGetFutureResult(ls1);
            List<String> interfaces = toList(ls);
            if (interfaces.size() == 0) {
                return;
            }
            classBox.removeAllItems();
            for (String anInterface : interfaces) {
                classBox.addItem(anInterface);
            }
            Future<ExecuteResult<String>> ls2 = dubboSocket.sendCommand("ls " + interfaces.get(0));
            String firstClassMethod = Tools.blockGetFutureResult(ls2);
            List<String> methodList = toList(firstClassMethod);
            methodBox.removeAllItems();
            for (String method : methodList) {
                methodBox.addItem(method);
            }
        });

        classBox.addItemListener(e1 -> {
            String clazz = (String) e1.getItem();
            String classMethod;
            Future<ExecuteResult<String>> ls3 = dubboSocket.sendCommand("ls " + clazz);
            classMethod = Tools.blockGetFutureResult(ls3);
            methodBox.removeAllItems();
            List<String> methodNameList = toList(classMethod);
            for (String method : methodNameList) {
                methodBox.addItem(method);
            }
        });

        invokeButton.addActionListener(e2 -> {
            String clazzName = ((String) classBox.getSelectedItem()).trim();
            String methodName = ((String) methodBox.getSelectedItem()).trim();
            String classAndMethod = clazzName + "." + methodName;
            String args = inputTextArea.getText();
            String command = "invoke " + classAndMethod + "(" + args + ")";
            Future<ExecuteResult<String>> invokeResult = dubboSocket.sendCommand(command, outputTextArea);
            if (invokeResult == DubboSocket.IN_EXECUTION_FUTURE) {
                Tools.showErrorDialog("请求还在运行中");
            }
        });

        instance = this;
    }

    public void connect(String host, int port) throws IOException {
        if (dubboSocket != null) {
            try {
                dubboSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
