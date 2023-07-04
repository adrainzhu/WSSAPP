package com.example.dingtaihw.DataBase;

import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.Model.LL.RequestParts;
import com.example.dingtaihw.Model.LL.SendParts;
import com.example.dingtaihw.Model.LL.SuggestParts;
import com.example.dingtaihw.Model.ll_item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB_EUtils {
    private static Connection connection2 = null;
    private static String url = "jdbc:oracle:thin:@10.18.103.41:1521:ecology";
    private static String username = "ecology";
    private static String password = "Weaver_2022";

    private static HashMap<String, String> typemap = new HashMap<String, String>() {
    };

    public static Connection getConn() {
        typemap.put("0", "Wafer");
        typemap.put("1", "耗材");
        typemap.put("2", "Target");
        typemap.put("3", "石英");
        typemap.put("4", "Spare Parts");
        typemap.put("5", "光阻");
        typemap.put("6", "化学品");
        typemap.put("7", "气体");
        typemap.put("8", "Mask");
        typemap.put("9", "PPE");
        typemap.put("10", "办公文具");
        typemap.put("11", "保洁用品");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection2 = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection2;
    }

    public static List<Goods> checkgoods(List<Goods> goods) {
        List<Goods> result = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            for (int i = 0; i < goods.size(); i++) {
                Goods good = goods.get(i);
                String sql = "SELECT 1 FROM uf_whrk WHERE drrq=TO_CHAR(SYSDATE,'yyyy-mm-dd')" +
                        " AND zt=0 " +
                        "AND ddh='" + good.getSoNumber() + "' " +
                        "AND lh='" + good.getLh() + "' " +
                        "AND sl='" + good.getNumber() + "'";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    if (updategood(good.getSoNumber(), good.getLh(), good.getNumber()))
                        good.setStatus("OK");
                } else {
                    good.setStatus("数据不匹配");
                }
                result.add(good);
            }

            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public static String checkgood(String sono, String lh, String number) {
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "SELECT 1 FROM uf_whrk WHERE zt=0 " +
                    "AND lh='" + sono + "' " +
                    "AND ph='" + lh + "' " +
                    "AND sl='" + number + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                if (updategood(sono, lh, number)) ;
                return "OK";
            }
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return "Fail";
    }

    public static boolean updategood(String sono, String lh, String number) {
        boolean st = false;
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "UPDATE uf_whrk SET zt=1 WHERE " +
                    "lh='" + sono + "' AND ph='" + lh + "' AND sl='" + number + "'";
            System.out.println(sql);
            st = statement.execute(sql);
            statement.close();
            conn.close();
            return st;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public static List<ll_item> getllitem() {
        List<ll_item> result = new ArrayList<>();
        Map<String,String> settings=getSettings();
        try {
            Connection connection = getConn();
            Statement statement = connection.createStatement();
            String sql = "SELECT fm.REQUESTID ,fm.LCBH ,fm.LLLB ,h.LASTNAME  FROM FORMTABLE_MAIN_432 fm " +
                    "LEFT JOIN WORKFLOW_requestBASE wb ON fm.REQUESTID =wb.REQUESTID " +
                    "LEFT JOIN HRMRESOURCE h ON fm.SQR =h.ID " +
                    "WHERE wb.CURRENTNODEID ='"+settings.get("Nodeid")+"'";
            System.out.println("领料单sql:" + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ll_item ll_item = new ll_item();
                ll_item.setSerialno(resultSet.getString(2));
                ll_item.setUser(resultSet.getString(4));
                ll_item.setType(typemap.get(resultSet.getString(3)));
                ll_item.setRequestid(resultSet.getString(1));
                result.add(ll_item);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;

    }

    public static List<RequestParts> getRquestParts(String dh) {
        List<RequestParts> returnparts = new ArrayList<>();
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "select b.lh,b.wlms,b.XQSL1,b.ZDCB,b.ZDPC,b.BZ,a.LLLB  from formtable_main_432 a " +
                    "left join FORMTABLE_MAIN_432_DT1 b on a.id=b.MAINID " +
                    "where a.LCBH='" + dh + "'";
            System.out.println("需求信息sql:" + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                RequestParts re = new RequestParts();
                re.setLh(resultSet.getString((1)));
                re.setInfo(resultSet.getString(2));
                re.setRequestnum(resultSet.getString(3));
                re.setOrderwh(resultSet.getString(4));
                re.setPno(resultSet.getString(5));
                re.setBz(resultSet.getString(6));
                re.setRequesttype(typemap.get(resultSet.getString(7)));
                returnparts.add(re);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }

        return returnparts;
    }

    public static List<SuggestParts> getSuggestParts(String dh) {
        List<SuggestParts> returnparts = new ArrayList<>();
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "select b.lh,b.PM,b.CB,b.PC,b.JW,b.SFSL " +
                    "from formtable_main_432 a left join FORMTABLE_MAIN_432_DT3 b on a.id=b.MAINID  " +
                    " where a.LCBH='" + dh + "'";
            System.out.println("建议信息sql:" + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SuggestParts sg = new SuggestParts();
                sg.setLh(resultSet.getString((1)));
                sg.setInfo(resultSet.getString(2));
                sg.setCb(resultSet.getString(3));
                sg.setPno(resultSet.getString(4));
                sg.setLocation(resultSet.getString(5));
                sg.setSuggestnum(resultSet.getString(6));
                returnparts.add(sg);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }

        return returnparts;
    }

    public static SendParts getPartsinfo(SendParts sendParts) {
        SendParts returnparts = sendParts;
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "select wlms,plant,pc from uf_ckllsj where lh='" + returnparts.getLh() + "'" +
                    " and pc='" + returnparts.getPno() + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("获取parts信息：" + sql);
            while (resultSet.next()) {
                returnparts.setInfo(resultSet.getString(1));
                returnparts.setLocation(resultSet.getString(2));
            }
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sendParts;
    }

    public static Map<String, String> getSettings() {
        Map<String, String> result = new HashMap<>();
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "SELECT distinct LLDNODEID from uf_App_Settings";
            System.out.println(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.put("Nodeid", resultSet.getString(1));
            }
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static boolean UpdateSettings(String nodeid) {
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "update uf_App_Settings set LLDNODEID='" + nodeid + "'";
            System.out.println(statement.executeUpdate(sql));
            statement.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
}
