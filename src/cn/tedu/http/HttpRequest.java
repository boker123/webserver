package cn.tedu.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
                this.protocol = datas[2];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
