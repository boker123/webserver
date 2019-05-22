package cn.tedu.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
* 这个类用来封装请求信息
*
* @author Boker
*/
public class HttpRequest {
    // 1,声明三个请求参数

    private String method;
    private String uri;
    private String protocol;

    // 声明一个map存放用户名和密码
    private Map<String, String> map = new HashMap<>();

    public HttpRequest(InputStream in) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in));
        try {
            String line = reader.readLine();
            if(line != null && line.length() > 0) {
                String[] datas = line.split(" ");
                this.method = datas[0];
                this.uri = datas[1];
                if(uri.equals("/")) {
                    this.uri = "/index.html";
                }

                // 获取用户浏览器上的值
                if(uri!=null && uri.contains("?")) {
                    String xiaochuan = uri.split("\\?")[1];
                    String[] strs = xiaochuan.split("&");
                    for(String string : strs) {
                        String key = string.split("=")[0];
                        String val = string.split("=")[1];

                        map.put(key,val);
                    }
                }

                this.protocol = datas[2];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 提供公共的方法来根据key查询value
    public String getParameter(String key) {
        return map.get(key);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
