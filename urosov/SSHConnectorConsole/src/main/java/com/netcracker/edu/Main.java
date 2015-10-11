package com.netcracker.edu;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.io.PrintStream;

public class Main {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 22;

    public static void main(String[] args) {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = connectJSch(args);
            // TODO Wrap into a cycle: read from console until it is an "exit"
            printExecResult(getExecChannel(session), /*command*/ "uname", System.out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private static Session connectJSch(String... accessString) throws JSchException {
        // TODO parse accessString: username/password@host:port <- with probable exclusions
        String username = "vasiliy"; // These are my local credentials
        String password = "netcracker";
        String host = "192.168.0.107";
        int port = 22;

        JSch jSch = new JSch();
        Session session = jSch.getSession(username, host, port);

        if (password != null && !password.isEmpty()) {
            session.setPassword(password);
        }
        UserInfo userInfo = new LocalUserInfo(); // TODO check impact of this string
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

            // TODO Figure out what these do
            if (channel.isClosed()) {
                destination.println("exit-status: " + channel.getExitStatus());
                break;
            }
            Thread.sleep(100);
        }

        channel.disconnect();
    }
}
