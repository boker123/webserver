package cn.tedu.context;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类用来提取位置文件数据
 *
 * @author Boker
 */

public class ServerContext {
    // 1,声明4个变量
    public static int port;
    public static int maxSize;
    public static String protocol;
    public static String webRoot;
    public static String notFoundPage;
    public static Map<String, String> typeMap = new HashMap<>();

    // 2,在静态代码块中完成初始化变量
    static {
        init();
    }

    // 读取配置文件
    private static void init() {

        try {
            // 从dom树中获取值
            SAXReader reader = new SAXReader();
            Document doc = reader.read("config/server.xml");
            Element server = doc.getRootElement();
            Element service = server.element("service");
            Element connect = service.element("connect");
            webRoot = service.elementText("webRoot");
            // 为变量赋值
            port = Integer.valueOf(connect.attributeValue("port"));
            maxSize = Integer.valueOf(connect.attributeValue("maxSize"));
            protocol = connect.attributeValue("protocol");
            notFoundPage = service.elementText("not-found-page");
            List<Element> list = service.element("typemappings").elements();
            for(Element element : list) {
                String Key = element.attributeValue("ext");
                String value = element.attributeValue("type");
                typeMap.put(Key, value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
