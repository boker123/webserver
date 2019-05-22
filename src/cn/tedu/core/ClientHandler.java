package cn.tedu.core;

import cn.tedu.context.ServerContext;
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
            // 声明请求获取对象
            HttpRequest request = new HttpRequest(socket.getInputStream());
            if(request.getUri()!=null && request.getUri().length()>0){
                HttpResponse response = new HttpResponse(socket.getOutputStream());

                // 配置响应头
                response.setProtocol("Http/1.1");
                response.setStatus(200);
                response.setStatusStr("OK");

                // 获取文件路径
                File file = new File(ServerContext.webRoot +request.getUri());
                // 判断访问的文件是否存在
                // 配置响应文件
                response.setContentType(getContentTypeByFile(file));
                if(!file.exists()) {
                    file = new File(ServerContext.webRoot+ServerContext.notFoundPage);
                    response.setStatus(404);
                    response.setStatusStr("Not Found");
                }

                response.setContentLength((int)file.length());

                BufferedInputStream bis = new BufferedInputStream(
                        new FileInputStream(file));
                byte[] bs = new byte[(int) file.length()];
                bis.read(bs);
                // 响应
                response.getOut().write(bs);
                response.getOut().flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String  getContentTypeByFile(File file) {
        // 获取文件名
        String filename = file.getName();
        // 获取文件名后缀
        String ext = filename.substring(filename.lastIndexOf("."));
        return ServerContext.typeMap.get(ext);
    }
}
