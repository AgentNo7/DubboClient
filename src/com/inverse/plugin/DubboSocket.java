package com.inverse.plugin;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.bouncycastle.util.Arrays;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author keeper
 * @since 2021/1/12
 */
public class DubboSocket extends Socket {

    public static final ExecuteResult<String> IN_EXECUTION = ExecuteResult.fail("COMMAND IN EXECUTION!");
    public static final Future<ExecuteResult<String>> IN_EXECUTION_FUTURE = new Future<ExecuteResult<String>>() {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public ExecuteResult<String> get() {
            return IN_EXECUTION;
        }

        @Override
        public ExecuteResult<String> get(long timeout, @NotNull TimeUnit unit) {
            return IN_EXECUTION;
        }
    };

    private static ExecutorService executor = new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            new DefaultThreadFactory("DefaultThread-"));
    public static final String SUFFIX = "\ndubbo>";

    private volatile Future<ExecuteResult<String>> runningTask = null;

    private byte[] buf;

    private BufferedWriter writer;
    private InputStream inputStream;

    public DubboSocket(String host, int port) throws IOException {
        super(host, port);
        setSoTimeout(5000);
        //charset
        SettingsState instance = SettingsState.getInstance(Tools.getProject());
        writer = new BufferedWriter(new OutputStreamWriter(getOutputStream(), instance.inputEncoding));
        //byte
        buf = new byte[instance.bufSize];
        inputStream = getInputStream();
        setKeepAlive(true);
    }

    public DubboSocket() throws IOException {
        this("127.0.0.1", 20882);
    }

    public void setWriterEncoding(String encoding) throws IOException {
        writer = new BufferedWriter(new OutputStreamWriter(getOutputStream(), encoding));
    }

    public void setBufSize(int size) {
        buf = new byte[size];
    }

    public synchronized Future<ExecuteResult<String>> sendCommand(String command) {
        return sendCommand(command, null);
    }

    public synchronized Future<ExecuteResult<String>> sendCommand(String command, JTextArea outputTextArea) {
        if (runningTask != null) {
            return IN_EXECUTION_FUTURE;
        }
        Future<ExecuteResult<String>> future = executor.submit(() -> {
            try {
                write(command);
                ExecuteResult<String> success = ExecuteResult.success(readAll());
                runningTask = null;
                if (outputTextArea != null) {
                    outputTextArea.setText(success.getData());
                }
                return success;
            } catch (Exception e) {
                return ExecuteResult.fail(e.getMessage());
            }
        });
        runningTask = future;
        return future;
    }

    private void write(String command) throws IOException {
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    private String readAll() throws IOException {
        Arrays.clear(buf);
        inputStream.read(buf);
        //charset
        SettingsState instance = SettingsState.getInstance(Tools.getProject());
        String s = new String(buf, instance.outputEncoding);
        s = s.trim();
        if (s.endsWith(SUFFIX)) {
            return s.substring(0, s.length() - SUFFIX.length());
        }
        return s;
    }
}
