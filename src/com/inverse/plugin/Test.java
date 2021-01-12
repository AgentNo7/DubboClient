package com.inverse.plugin;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author keeper
 * @since 2021/1/12
 */
public class Test {
    public static void main(String[] args) throws IOException {
        DubboSocket dubboSocket = new DubboSocket();

        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            System.out.print(">>>");
            String s = scanner.nextLine();
            String out = dubboSocket.sendCommand(s);
            System.out.println(out);
        }
    }
}
