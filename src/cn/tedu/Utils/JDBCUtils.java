package cn.tedu.Utils;

import java.sql.*;
import java.util.ResourceBundle;

public class JDBCUtils {
    private JDBCUtils(){};
    public static Connection getConnection() {
        try {
            // 读取属性文件
            ResourceBundle rb = ResourceBundle.getBundle("jdbc");

            Class.forName(rb.getString("driverClass"));
            String url = rb.getString("jdbcUrl");
            String user=rb.getString("user");
            String password=rb.getString("password");
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void close(ResultSet rs, Statement st, Connection conn) {
        if(rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                rs=null;
            }
        }
        if(st!=null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                st=null;
            }
        }
        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn=null;
            }
        }
    }
}
