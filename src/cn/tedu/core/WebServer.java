package cn.tedu.core;

import cn.tedu.context.ServerContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 *
 */
public class WebServer {
    private ServerSocket server;
    private ExecutorService pool;

    public WebServer() {
        try {
            server = new ServerSocket(ServerContext.port);
            pool = Executors.newFixedThreadPool(ServerContext.maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 创建start方法
    // 用来接收请求，处理业务，响应
    public void start() {
        try {
            while(true) {
                Socket socket = server.accept();
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 4,创建mian方法启动服务器
    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
