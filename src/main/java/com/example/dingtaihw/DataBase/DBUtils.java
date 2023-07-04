package com.example.dingtaihw.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtils {
    private static Connection connection = null;
//    private static String url = "jdbc:oracle:thin:@10.18.10.153:1521:ecology";
//    private static String username = "ecology";
//    private static String password = "Weaver_2021";
private static String url = "jdbc:oracle:thin:@10.18.103.41:1521:ecology";
    private static String username = "ecology";
    private static String password = "Weaver_2022";


    public static Connection getConn() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, username,password);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
public static String updatestatus(String sql){
    try {
        Connection conn = getConn();
        Statement statement = conn.createStatement();
        boolean execute = statement.execute(sql);
        statement.close();
        conn.close();
        System.out.println(execute);
        return String.valueOf(execute);
}catch (SQLException e) {
        System.out.println(e.getMessage());
        return e.getMessage();

    }}
    public static String getValue(String sql) {
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                String result = resultSet.getString(1);
                resultSet.close();
                statement.close();
                conn.close();
                return result;
            } else {
                return "No_User";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String getUserid(String username) {
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql="select id  from hrmresource where loginid='"+username+"'";
            System.out.println(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                String result = resultSet.getString(1);
                resultSet.close();
                statement.close();
                conn.close();
                return result;
            } else {
                return "No_ID";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}

