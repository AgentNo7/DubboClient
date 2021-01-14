package com.inverse.plugin;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * @author keeper
 * @since 2021/1/13
 */
public class Tools {

    private static Project project;

    public static String toGbk(String str) {
        try {
            return new String(str.getBytes(StandardCharsets.UTF_8), "GBK");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static List<String> toList(String str) {
        if (str == null || str.length() == 0) {
            return Collections.emptyList();
        }
        String[] split = str.split("\n");
        return Arrays.asList(split);
    }

    public static void setProject(Project proj) {
        project = proj;
    }

    public static <T> T wrapErrorDialog(Supplier<T> supplier, String errorMsg) {
        try {
            return supplier.get();
        } catch (Exception e) {
            final DialogBuilder builder = new DialogBuilder(project);
            builder.addCloseButton().setText("close");
            builder.setErrorText(errorMsg);
            builder.setTitle("Tip");
            builder.show();
            return null;
        }
    }

    public static void showErrorDialog(String errorMsg) {
        final DialogBuilder builder = new DialogBuilder(project);
        builder.addCloseButton().setText("close");
        builder.setErrorText(errorMsg);
        builder.setTitle("Tip");
        builder.show();
    }

    public static <T> T blockGetFutureResult(Future<ExecuteResult<T>> future) {
        ExecuteResult<T> result;
        try {
            result = future.get();
            return result.getData();
        } catch (InterruptedException | ExecutionException e) {
            Tools.showErrorDialog("连接异常");
            throw new RuntimeException("连接异常", e);
        }
    }
}
