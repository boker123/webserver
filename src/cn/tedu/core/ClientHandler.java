package cn.tedu.core;

import cn.tedu.Utils.JDBCUtils;
import cn.tedu.context.HttpContext;
import cn.tedu.context.ServerContext;
import cn.tedu.http.HttpRequest;
import cn.tedu.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

                // 判断用户是否要登录或注册
                if(request.getUri().startsWith("/LoginUser") || request.getUri().startsWith("/RegistUser")) {
                    service(request,response);
                    return;
                }

                // 配置响应头
                response.setProtocol("Http/1.1");
                response.setStatus(HttpContext.CODE_OK);

                // 获取文件路径
                File file = new File(ServerContext.webRoot +request.getUri());
                // 判断访问的文件是否存在
                // 配置响应文件
                response.setContentType(getContentTypeByFile(file));
                if(!file.exists()) {
                    file = new File(ServerContext.webRoot+ServerContext.notFoundPage);
                    response.setStatus(HttpContext.CODE_NOTFOUND);
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

    // 完成登录或注册
    private void service(HttpRequest request, HttpResponse response) {
        if(request.getUri().startsWith("/RegistUser")) {

            Connection conn = null;
            PreparedStatement ps = null;
            try {
                // 1，注册驱动 2，连接数据库
                conn = JDBCUtils.getConnection();

                // 3，获取传输器
                String sql = "insert into user values(null,?,?)";
                ps = conn.prepareStatement(sql);

                // 设置参数
                ps.setString(1, request.getParameter("username"));
                ps.setString(2, request.getParameter("password"));

                // 4,执行sql
                int rows = ps.executeUpdate();

                // 响应注册成功页面
                response.setProtocol(ServerContext.protocol);
                response.setStatus(HttpContext.CODE_OK);
                File file = new File(ServerContext.webRoot+"/regist_success.html");
                response.setContentType(getContentTypeByFile(file));
                response.setContentLength((int)file.length());

                // 读文件写文件
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                byte[] bs = new byte[(int) file.length()];
                bis.read(bs);
                response.getOut().write(bs);
                socket.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 6,释放资源
                JDBCUtils.close(null, ps, conn);
            }
        }
    }

    // 获得响应文件格式
    private String  getContentTypeByFile(File file) {
        // 获取文件名
        String filename = file.getName();
        // 获取文件名后缀
        String ext = filename.substring(filename.lastIndexOf(".")+1);//不+1的话登录会报错
//        System.out.println(ext);
        return ServerContext.typeMap.get(ext);
    }
}
