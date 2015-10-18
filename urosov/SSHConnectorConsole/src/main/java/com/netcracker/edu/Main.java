package com.netcracker.edu;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
    private static final int DEFAULT_PORT = 22;

    public static void main(String[] args) {
        Session session = null;

        try {
            session = connectJSch(args[0]);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String command;
            while (!(command = reader.readLine()).equals("exit")) {
                if (!command.isEmpty()) {
                    printExecResult(getExecChannel(session), /*command*/ command, System.out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
    private static Session connectJSch(String accessString) throws JSchException {
        if (!accessString.matches("^[^/@:]+/[^/@:]+@[^/@:]+(:\\d+)?$")) { // TODO rewrite
            throw new IllegalArgumentException("Entered access string must be formatted as following: " +
                    "username[/password]@host[:port]");
        }

        int usernameEndIndex = accessString.contains("/") ? accessString.indexOf('/') : accessString.indexOf('@');
        String username = accessString.substring(0, usernameEndIndex);
        String password = accessString.contains("/") ? accessString.substring(accessString.indexOf('/') + 1, accessString.indexOf('@')) : "";
        if (!accessString.contains(":")) {
            accessString = accessString + ":" + DEFAULT_PORT;
        }
        String host = accessString.substring(accessString.indexOf('@') + 1, accessString.indexOf(':'));
        int port = Integer.parseInt(accessString.substring(accessString.indexOf(':') + 1));

        JSch jSch = new JSch();
        Session session = jSch.getSession(username, host, port);

        if (!password.isEmpty()) {
            session.setPassword(password);
        }
        UserInfo userInfo = new LocalUserInfo();
        session.setUserInfo(userInfo);
        session.connect();

        return session;
    }

    private static ChannelExec getExecChannel(Session session) throws JSchException {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");

        channel.setInputStream(null);
        channel.setErrStream(System.err);

        return channel;
    }

    private static void printExecResult(ChannelExec channel, String command, PrintStream destination) throws Exception {
        channel.setCommand(command);
        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                destination.print(new String(tmp, 0, i));
            }

            if (channel.isClosed()) {
                break;
            }
            Thread.sleep(100);
        }

        channel.disconnect();
    }
}
