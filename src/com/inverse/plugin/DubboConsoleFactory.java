package com.inverse.plugin;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author keeper
 * @since 2021/1/7
 */
public class DubboConsoleFactory implements ToolWindowFactory {

    public static final String BANNER = "                         /$$                                                     /$$                           /$$           /$$       /$$                                                                         /$$          \n" +
            "                        | $$                                                    | $$                          | $$          | $$      | $$                                                                        | $$          \n" +
            " /$$  /$$  /$$  /$$$$$$ | $$  /$$$$$$$  /$$$$$$  /$$$$$$/$$$$   /$$$$$$        /$$$$$$    /$$$$$$         /$$$$$$$ /$$   /$$| $$$$$$$ | $$$$$$$   /$$$$$$         /$$$$$$$  /$$$$$$  /$$$$$$$   /$$$$$$$  /$$$$$$ | $$  /$$$$$$ \n" +
            "| $$ | $$ | $$ /$$__  $$| $$ /$$_____/ /$$__  $$| $$_  $$_  $$ /$$__  $$      |_  $$_/   /$$__  $$       /$$__  $$| $$  | $$| $$__  $$| $$__  $$ /$$__  $$       /$$_____/ /$$__  $$| $$__  $$ /$$_____/ /$$__  $$| $$ /$$__  $$\n" +
            "| $$ | $$ | $$| $$$$$$$$| $$| $$      | $$  \\ $$| $$ \\ $$ \\ $$| $$$$$$$$        | $$    | $$  \\ $$      | $$  | $$| $$  | $$| $$  \\ $$| $$  \\ $$| $$  \\ $$      | $$      | $$  \\ $$| $$  \\ $$|  $$$$$$ | $$  \\ $$| $$| $$$$$$$$\n" +
            "| $$ | $$ | $$| $$_____/| $$| $$      | $$  | $$| $$ | $$ | $$| $$_____/        | $$ /$$| $$  | $$      | $$  | $$| $$  | $$| $$  | $$| $$  | $$| $$  | $$      | $$      | $$  | $$| $$  | $$ \\____  $$| $$  | $$| $$| $$_____/\n" +
            "|  $$$$$/$$$$/|  $$$$$$$| $$|  $$$$$$$|  $$$$$$/| $$ | $$ | $$|  $$$$$$$        |  $$$$/|  $$$$$$/      |  $$$$$$$|  $$$$$$/| $$$$$$$/| $$$$$$$/|  $$$$$$/      |  $$$$$$$|  $$$$$$/| $$  | $$ /$$$$$$$/|  $$$$$$/| $$|  $$$$$$$\n" +
            " \\_____/\\___/  \\_______/|__/ \\_______/ \\______/ |__/ |__/ |__/ \\_______/         \\___/   \\______/        \\_______/ \\______/ |_______/ |_______/  \\______/        \\_______/ \\______/ |__/  |__/|_______/  \\______/ |__/ \\_______/\n" +
            "                                                                                                                                                                                                                                \n" +
            "                                                                                                                                                                                                                                \n" +
            "                                                                                                                                                                                                                                \n";


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //创建出DubboConsole
        DubboClient dubboClient = new DubboClient(project, toolWindow);
        Content dubboConsole = contentFactory.createContent(dubboClient.getPanel(), "Dubbo Client", false);
        toolWindow.getContentManager().addContent(dubboConsole);
        //创建DubboClient
        ConsoleView consoleView = MyConsoleView.getInstance(project).getConsoleView();
        consoleView.print(BANNER, ConsoleViewContentType.LOG_INFO_OUTPUT);
        Content content = contentFactory.createContent(createConsolePanel(consoleView, null), "Dubbo Console", false);
        //给toolWindow设置内容
        toolWindow.getContentManager().addContent(content);
    }

    private JComponent createConsolePanel(final ConsoleView view, final ActionGroup actions) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), "Center");
        if (actions != null) {
            final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("unknown", actions, false);
            final JComponent actionToolbarComponent = actionToolbar.getComponent();
            panel.add(actionToolbarComponent, "West");
        }
        return panel;
    }
}
