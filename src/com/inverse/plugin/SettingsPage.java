package com.inverse.plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author yhf
 * @since 2021/1/16
 */
public class SettingsPage implements Configurable {
    private JPanel settingPanel;

    private Project project;


    /**
     * 设置信息
     */
    private SettingsState settings;

    private JTextField inputTextField;
    private JTextField outputTextField;
    private JTextField bufSizeField;

    /**
     * 自动构造  idea 会携带当前的project 参数信息
     *
     * @param project
     */
    public SettingsPage(Project project) {
        this.project = project;
        settings = SettingsState.getInstance(this.project);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Dubbo Client Settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return settingPanel;
    }

    @Override
    public boolean isModified() {
        return !settings.inputEncoding.equals(inputTextField.getText())
                || !settings.outputEncoding.equals(outputTextField.getText());
    }

    @Override
    public void apply() throws ConfigurationException {
        int size = 0;
        try {
            size = Integer.parseInt(bufSizeField.getText());
        } catch (Exception e) {
            Tools.showErrorDialog("bufSize 不正确");
        }
        settings.inputEncoding = inputTextField.getText();
        settings.outputEncoding = outputTextField.getText();
        settings.bufSize = size;

        applySettings();
    }

    @Override
    public void reset() {
        inputTextField.setText(settings.inputEncoding);
        outputTextField.setText(settings.outputEncoding);
        bufSizeField.setText(settings.bufSize.toString());
    }

    public void applySettings() {
        DubboClient instance = DubboClient.getInstance();
        if (instance == null) {
            return;
        }
        instance.dubboSocket.setBufSize(settings.bufSize);
        try {
            instance.dubboSocket.setWriterEncoding(settings.inputEncoding);
        } catch (IOException e) {
            Tools.showErrorDialog("inputEncoding 不正确");
            throw new RuntimeException(e);
        }
        try {
            Charset.forName(settings.outputEncoding);
        } catch (Exception e) {
            Tools.showErrorDialog("outputEncoding 不正确");
            throw new RuntimeException(e);
        }
    }
}
