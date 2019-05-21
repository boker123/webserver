package cn.tedu.core;

import cn.tedu.http.HttpRequest;
import cn.tedu.http.HttpResponse;

import java.io.*;
import java.net.Socket;

/**
 *这个类用来完成WebServer类的优化
 *
 * @author Boker
 */
public class ClientHandler implements Runnable {
    // 创建一个代表客户端的Socket对象
    private Socket socket;

    // 创建构造函数，传入socket对象并保存
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    //重写run方法
    @Override
    public void run() {
        try {
            HttpRequest request = new HttpRequest(socket.getInputStream());
            HttpResponse response = new HttpResponse(socket.getOutputStream());

            response.setProtocol("Http/1.1");
            response.setStatus(200);
            response.setContentType("text/html");
            File file = new File("web"+request.getUri());
            response.setContentLength((int)file.length());
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            byte[] bs = new byte[(int) file.length()];
            bis.read(bs);
            // 响应
            response.getOut().write(bs);
            response.getOut().flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
