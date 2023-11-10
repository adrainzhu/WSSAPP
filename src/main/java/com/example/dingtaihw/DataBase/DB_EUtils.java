package com.example.dingtaihw.DataBase;

import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.Model.LL.RequestParts;
import com.example.dingtaihw.Model.LL.SendParts;
import com.example.dingtaihw.Model.LL.SuggestParts;
import com.example.dingtaihw.Model.Out.Lot_Out;
import com.example.dingtaihw.Model.Out.Out_LotDetail;
import com.example.dingtaihw.Model.Out.Out_Wuliu;
import com.example.dingtaihw.Model.ll_item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        Map<String, String> settings = getSettings();
        try {
            Connection connection = getConn();
            Statement statement = connection.createStatement();
            String sql = "SELECT fm.REQUESTID ,fm.LCBH ,fm.LLLB ,h.LASTNAME  FROM FORMTABLE_MAIN_432 fm " +
                    "LEFT JOIN WORKFLOW_requestBASE wb ON fm.REQUESTID =wb.REQUESTID " +
                    "LEFT JOIN HRMRESOURCE h ON fm.SQR =h.ID " +
                    "WHERE wb.CURRENTNODEID ='" + settings.get("Nodeid") + "'";
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

    public static List<Lot_Out> getOutList() {
        List<Lot_Out> lotlist = new ArrayList<>();
        Map<String, String> settings = getSettings();
        String sql = "select c.lastname,a.dn,a.lcbh,a.requestid,b.CURRENTNODEID from formtable_main_656 a \n" +
                "LEFT JOIN WORKFLOW_requestBASE b ON a.REQUESTID =b.REQUESTID\n" +
                "LEFT JOIN hrmresource c on a.sqr=c.id\n" +
                "where b.CURRENTNODEID='" + settings.get("Outid") + "'";
        try {
            Connection connection = getConn();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Lot_Out lot_out = new Lot_Out(resultSet.getString(3), resultSet.getString(1), resultSet.getString(2), resultSet.getString(4));
                lotlist.add(lot_out);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return lotlist;
    }

    public static List<Out_Wuliu> getWLList() {
        List<Out_Wuliu> lotlist = new ArrayList<>();
        Map<String, String> settings = getSettings();
        String sql = "select c.lastname,a.wldh,a.lcbh,a.requestid,b.CURRENTNODEID from formtable_main_656 a \n" +
                "LEFT JOIN WORKFLOW_requestBASE b ON a.REQUESTID =b.REQUESTID\n" +
                "LEFT JOIN hrmresource c on a.sqr=c.id\n" +
                "where b.CURRENTNODEID='" + settings.get("Wuliu") + "'";
        try {
            Connection connection = getConn();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Out_Wuliu out_wuliu = new Out_Wuliu(resultSet.getString(2), resultSet.getString(1), resultSet.getString(3), resultSet.getString(4));
                lotlist.add(out_wuliu);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return lotlist;
    }

    public static List<RequestParts> getRquestParts(String dh) {
        List<RequestParts> returnparts = new ArrayList<>();
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "select b.lh,b.wlms,b.XQSL1,b.ZDCB,b.ZDPC,b.BZ,a.LLLB  from formtable_main_432 a " +
                    "left join FORMTABLE_MAIN_432_DT1 b on a.id=b.MAINID " +
                    "where a.LCBH='" + dh + "'";
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
            String sql = "SELECT distinct LLDNODEID,CHDNODEID,wuliu from uf_App_Settings";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.put("Nodeid", resultSet.getString(1));
                result.put("Outid", resultSet.getString(2));
                result.put("Wuliu", resultSet.getString(3));
            }
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static boolean UpdateSettings(Map<String, String> map) {
        try {
            String nodeid = map.get("Nodeid");
            String outid = map.get("Outid");
            String wuliuid = map.get("Wuliu");
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String sql = "update uf_App_Settings set LLDNODEID='" + nodeid + "' ,CHDNODEID='" + outid + "'" +
                    ",wuliu='" + wuliuid + "'";
            System.out.println(sql);
            System.out.println(statement.executeUpdate(sql));
            statement.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);


        }
    }

    public static List<Out_LotDetail> getOutDetailList(String reid) {
        List<Out_LotDetail> out_lotDetailList = new ArrayList<>();
        String s = "select a.lotid3,a.productid,a.name,a.lotqty,b.wldh from formtable_main_656_dt1 a left join formtable_main_656 b on a.mainid=b.id" +
                " where b.requestid='" + reid + "'";
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(s);
            while (resultSet.next()) {
                Out_LotDetail out_lotDetail = new Out_LotDetail(resultSet.getString(3), resultSet.getString(1), resultSet.getString(4),
                        resultSet.getString(2), resultSet.getString(5));
                out_lotDetailList.add(out_lotDetail);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
        return out_lotDetailList;
    }

    public static List<Out_LotDetail> getWLOutDetailList(String reid) {
        List<Out_LotDetail> out_lotDetailList = new ArrayList<>();
        String s = "select a.lotid3,a.productid,a.name,a.lotqty,b.wldh from formtable_main_656_dt1 a left join formtable_main_656 b on a.mainid=b.id" +
                " where b.requestid='" + reid + "'";
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(s);
            while (resultSet.next()) {
                Out_LotDetail out_lotDetail = new Out_LotDetail(resultSet.getString(3), resultSet.getString(1),
                        resultSet.getString(4), resultSet.getString(2), resultSet.getString(5));
                out_lotDetailList.add(out_lotDetail);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
        return out_lotDetailList;
    }

    public static void submitscaninfo(String time, String function, String usr, String result) {
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String s = "insert into uf_scanrecord(smsj,smgn,czr,jgfh)values('" + time + "','" + function + "','" + usr + "','" + result + "')";
            System.out.println("提交输出" + s);
            statement.execute(s);

        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

    }

    public static String submitlocation(String lotid, String locid) {
        String result = "上架成功";
        try {
            Connection conn = getConn();
            Statement statement = conn.createStatement();
            String s = "insert into uf_WareHouseLoct(lh,location,FORMMODEID)values('" + lotid + "','" + locid + "','70501'" + ")";
            statement.execute(s);

        } catch (SQLException e) {
            result = e.toString();
            throw new RuntimeException(e);
        }
        return result;
    }

}
