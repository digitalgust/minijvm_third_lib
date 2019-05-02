/* Copyright (c) 2008-2015, Avian Contributors

   Permission to use, copy, modify, and/or distribute this software
   for any purpose with or without fee is hereby granted, provided
   that the above copyright notice and this permission notice appear
   in all copies.

   There is NO WARRANTY for this software.  See license.txt for
   details. */
package java.net;

import org.mini.net.SocketNative;

import java.io.IOException;

public class ServerSocket extends SocketImpl {

    boolean isListen = false;

    public ServerSocket() throws IOException {
        super();
    }

    public ServerSocket(int port)
            throws IOException {
        this(port, 0);
    }

    public ServerSocket(int port,
            int backlog)
            throws IOException {
        this(port, backlog, null);
    }

    public ServerSocket(int port,
            int backlog,
            InetAddress bindAddr)
            throws IOException {
        this();
        bind(fd, "", port);

    }

    void listen() throws IOException {
        if (isListen) {
            return;
        }
        int ret = SocketNative.listen0(fd);
        if (ret < 0) {
            throw new IOException("ServerSocket listen error");
        }
        isListen = true;
    }

    public Socket accept() throws IOException {
        if (fd < 0) {
            throw new IOException("ServerSocket not open");
        }
        listen();
        int sockfd = SocketNative.accept0(fd);
        if (sockfd < 0) {
            throw new IOException("ServerSocket accept error");
        }
        Socket socket = new Socket(sockfd);
        return socket;
    }
}
