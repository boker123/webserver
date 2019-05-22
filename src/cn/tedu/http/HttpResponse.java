package cn.tedu.http;

import java.io.OutputStream;
import java.io.PrintStream;

/**
* 这个类用来封装响应信息
 *
 * @author Boker
*/
public class HttpResponse {
    // 1，声明4个响应信息
    private String protocol;
    private int status;
    private String contentType;
    private int contentLength;
    private String statusStr;

    // 2,在构造函数中传入OutputStream对象
    private OutputStream out;
    public HttpResponse(OutputStream out) {
        this.out=out;
    }

    boolean isSend;
    public OutputStream getOut() {
        if(!isSend) {
            PrintStream ps = new PrintStream(out);
            ps.println(protocol+" "+status+" "+statusStr);
            ps.println("Content-Type:"+contentType);
            ps.println("Content-Length:"+contentLength);
            ps.println();
            isSend=true;
        }
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
