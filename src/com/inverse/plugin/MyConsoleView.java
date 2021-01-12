package com.inverse.plugin;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyConsoleView
{
    private static MyConsoleView instance;
    private final ConsoleView consoleView;
    
    public static MyConsoleView getInstance(final Project project) {
        if (MyConsoleView.instance == null) {
            MyConsoleView.instance = new MyConsoleView(project);
        }
        return MyConsoleView.instance;
    }
    
    public ConsoleView getConsoleView() {
        return this.consoleView;
    }

    private MyConsoleView(final Project project) {
        this.consoleView = this.createConsole(project);
    }
    
    public void print(final String line, final ConsoleViewContentType consoleViewContentType) {
        this.consoleView.print(line + "\n", consoleViewContentType);
    }
    
    public ConsoleView createConsole(@NotNull final Project project) {
        final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        return consoleBuilder.getConsole();
    }
    
}
