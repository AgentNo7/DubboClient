package com.inverse.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yhf
 * @since 2021/1/16
 */
@State(
        name = "dubbo-client.idea.plugin",
        storages = {@Storage("setting.xml")}
)
public class SettingsState implements PersistentStateComponent<SettingsState> {

    public String inputEncoding = "GBK";

    public String outputEncoding = "GBK";

    public Integer bufSize = 8192;

    public static SettingsState getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, SettingsState.class);
    }

    @Nullable
    @Override
    public SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
