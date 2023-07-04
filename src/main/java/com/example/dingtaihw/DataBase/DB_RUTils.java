package com.example.dingtaihw.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DB_RUTils {
    private static Connection connection2 = null;
    private static String url = "jdbc:oracle:thin:@10.18.103.12:1521:ARPTPDB1";
    private static String username = "OA";
    private static String password = "oa123";


    public static Connection getConn() {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection2 = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection2;
    }

    public static int checklot(String lotid) {
        String sql = "select results,sap_material from v_wms_shipinfo where lotid='" + lotid + "'";
        Connection connection = getConn();
        System.out.println(sql);
        String result = " ";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getString(1);
                System.out.println("result="+result);
                if (result==null) {
                    System.out.println(resultSet.getString(2));
                    updatelot(resultSet.getString(2));
                    resultSet.close();
                    statement.close();
                    connection.close();
                        return 0;//入库成功
                } else if(result.equals("SUCCESS")){
                    resultSet.close();
                    statement.close();
                    connection.close();
                    connection.close();
                  return 1;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;//没有信息 不可以入库
    }

    public static void updatelot(String sapno) {
        String sql = "update rptdb.ref_wms_workorder  set results='SUCCESS' where sap_material='" + sapno + "'";
        Connection connection = getConn();
        String result = "";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.toString());

        }
    }
}
