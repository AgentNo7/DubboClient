package com.inverse.plugin;

import org.bouncycastle.util.Arrays;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author keeper
 * @since 2021/1/12
 */
public class DubboSocket extends Socket {

    public static final String SUFFIX = "\ndubbo>";

    private final byte[] buf = new byte[8192];

    private static DubboSocket INSTANCE = null;

    private BufferedWriter writer;
    private InputStream inputStream;

    public DubboSocket(String host, int port) throws IOException {
        super(host, port);
        writer = new BufferedWriter(new OutputStreamWriter(getOutputStream(), "GBK"));
        inputStream = getInputStream();
        setKeepAlive(true);
    }

    public DubboSocket() throws IOException {
        this("127.0.0.1", 20882);
    }

    public String sendCommand(String command) throws IOException {
        writer.write(command);
        writer.newLine();
        writer.flush();
        return readAll();
    }

    private String readAll() throws IOException {
        Arrays.clear(buf);
        inputStream.read(buf);
        String s = new String(buf, "UTF-8");
        s = s.trim();
        if (s.endsWith(SUFFIX)) {
            return s.substring(0, s.length() - SUFFIX.length());
        }
        return s;
    }
}
