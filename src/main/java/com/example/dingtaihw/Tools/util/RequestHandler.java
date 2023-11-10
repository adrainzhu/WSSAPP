package com.example.dingtaihw.Tools.util;

import com.alibaba.fastjson.JSONObject;
import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.Model.Entity.WorkflowDetailTableInfoEntity;
import com.example.dingtaihw.Model.Entity.WorkflowRequestTableField;
import com.example.dingtaihw.Model.Entity.WorkflowRequestTableRecord;
import com.example.dingtaihw.Model.LL.SendParts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import oracle.sql.DATE;

public class RequestHandler {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Map<String,String> SYSTEM_CACHE = new HashMap <>();
    public static final String HOST = "http://10.18.101.39:8081";
    /**
     * ecology系统发放的授权许可证(appid)
     */
    private static final String APPID = "QMSAPPID";


    /**
     * 第一步：
     *
     * 调用ecology注册接口,根据appid进行注册,将返回服务端公钥和Secret信息
     */
    public static Map<String,Object> testRegist(String address){

        //获取当前系统RSA加密的公钥
        RSA rsa = new RSA();
        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();

        // 客户端RSA私钥
        SYSTEM_CACHE.put("LOCAL_PRIVATE_KEY",privateKey);
        // 客户端RSA公钥
        SYSTEM_CACHE.put("LOCAL_PUBLIC_KEY",publicKey);

        //调用ECOLOGY系统接口进行注册
        String data = HttpRequest.post(address + "/api/ec/dev/auth/regist")
                .header("appid",APPID)
                .header("cpk",publicKey)
                .timeout(2000)
                .execute().body();

        // 打印ECOLOGY响应信息
        Map<String,Object> datas = JSONUtil.parseObj(data);
        //ECOLOGY返回的系统公钥
        SYSTEM_CACHE.put("SERVER_PUBLIC_KEY",StrUtil.nullToEmpty((String)datas.get("spk")));
        //ECOLOGY返回的系统密钥
        SYSTEM_CACHE.put("SERVER_SECRET",StrUtil.nullToEmpty((String)datas.get("secrit")));
        return datas;
    }
    public static Map<String,Object> testGetoken(String address){
        // 从系统缓存或者数据库中获取ECOLOGY系统公钥和Secret信息
        String secret = SYSTEM_CACHE.get("SERVER_SECRET");
        String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");

        // 如果为空,说明还未进行注册,调用注册接口进行注册认证与数据更新
        if (Objects.isNull(secret)||Objects.isNull(spk)){
            testRegist(address);
            // 重新获取最新ECOLOGY系统公钥和Secret信息
            secret = SYSTEM_CACHE.get("SERVER_SECRET");
            spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
        }

        // 公钥加密,所以RSA对象私钥为null
        RSA rsa = new RSA(null,spk);
        //对秘钥进行加密传输，防止篡改数据
        String encryptSecret = rsa.encryptBase64(secret,CharsetUtil.CHARSET_UTF_8,KeyType.PublicKey);
        //调用ECOLOGY系统接口进行注册
        String data = HttpRequest.post(address+ "/api/ec/dev/auth/applytoken")
                .header("appid",APPID)
                .header("secret",encryptSecret)
                .header("time","3600")
                .execute().body();

        System.out.println("testGetoken()："+data);
        Map<String,Object> datas = JSONUtil.parseObj(data);

        //ECOLOGY返回的token
        // TODO 为Token缓存设置过期时间
        SYSTEM_CACHE.put("SERVER_TOKEN",StrUtil.nullToEmpty((String)datas.get("token")));

        return datas;
    }

        public void submitRequest(String requestid, String userid,List<SendParts> sendParts) {
            String token=(String) testGetoken(IdentityVerifyUtil.HOST ).get("token");
            String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
            Map<String, String> heads = new HashMap<>();
            heads.put("token", token);
            heads.put("appid", APPID);
            RSA rsa = new RSA(null,spk);
            //对用户信息进行加密传输,暂仅支持传输OA用户ID
            String encryptUserid = rsa.encryptBase64(userid, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
            heads.put("userid", encryptUserid);
            String url = HOST + "/api/workflow/paService/submitRequest";
            HttpManager http = new HttpManager();
            Map<String, String> params = new HashMap<>();
            params.put("requestId", requestid);
            params.put("detailData", getFormDetailData(sendParts));
            System.out.println(getFormDetailData(sendParts));
            //params.put("remark", "restful接口提交流程测试");
            try {
                String result = http.postDataSSL(url, params, heads);
                DB_EUtils.submitscaninfo(sdf.format(new Date()),"材料出货",userid,result);
                System.out.println(result);
            } catch (Exception e) {
                DB_EUtils.submitscaninfo(sdf.format(new Date()),"材料出货",userid,e.toString());
            }
        }

        public void outsubmitRequest(String requestid,String userid)
        {  String token= SYSTEM_CACHE.get("SERVER_TOKEN");
            if (StrUtil.isEmpty(token)){
                token = (String) testGetoken(IdentityVerifyUtil.HOST ).get("token");
            }
            String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
            Map<String, String> heads = new HashMap<>();
            heads.put("token", token);
            heads.put("appid", APPID);
            RSA rsa = new RSA(null,spk);
            //对用户信息进行加密传输,暂仅支持传输OA用户ID
            String encryptUserid = rsa.encryptBase64(userid, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
            heads.put("userid", encryptUserid);
            String url = HOST + "/api/workflow/paService/submitRequest";
            HttpManager http = new HttpManager();
            Map<String, String> params = new HashMap<>();
            params.put("requestId", requestid);
            params.put("remark", "PDA扫码检查完成");
            try {
                String result = http.postDataSSL(url, params, heads);
                DB_EUtils.submitscaninfo(sdf.format(new Date()),"成品出货",userid,result);
                System.out.println(result);
            } catch (Exception e) {
                DB_EUtils.submitscaninfo(sdf.format(new Date()),"成品出货",userid,e.toString());
            }
        }

    private String getFormDetailData(List<SendParts> sendParts) {
        List<WorkflowDetailTableInfoEntity> details = new ArrayList<>();
        //明细信息
        WorkflowDetailTableInfoEntity detail1 = new WorkflowDetailTableInfoEntity();
        detail1.setTableDBName("formtable_main_432_dt2");
        //明细数据
        List<WorkflowRequestTableRecord> detailRows = new ArrayList<>();
        for (int i = 0; i < sendParts.size(); i++) {
            WorkflowRequestTableRecord row = new WorkflowRequestTableRecord();
            List<WorkflowRequestTableField> rowDatas = new ArrayList<>();
            //行字段数据
            WorkflowRequestTableField row1field1 = new WorkflowRequestTableField();
            row1field1.setFieldName("lh");
            row1field1.setFieldValue(sendParts.get(i).getLh());
            row1field1.setEdit(true);
            rowDatas.add(row1field1);
            WorkflowRequestTableField row1field2 = new WorkflowRequestTableField();
            row1field2.setFieldName("pm");
            row1field2.setFieldValue(sendParts.get(i).getInfo());
            row1field2.setEdit(true);
            rowDatas.add(row1field2);
            WorkflowRequestTableField row1field3 = new WorkflowRequestTableField();
            row1field3.setFieldName("dw");
            row1field3.setFieldValue(sendParts.get(i).getUnit());
            row1field3.setEdit(true);
            rowDatas.add(row1field3);
            WorkflowRequestTableField row1field4 = new WorkflowRequestTableField();
            row1field4.setFieldName("pc");
            row1field4.setFieldValue(sendParts.get(i).getPno());
            row1field4.setEdit(true);
            rowDatas.add(row1field4);
            WorkflowRequestTableField row1field5 = new WorkflowRequestTableField();
            row1field5.setFieldName("plant");
            row1field5.setFieldValue(sendParts.get(i).getLocation());
            row1field5.setEdit(true);
            rowDatas.add(row1field5);
            WorkflowRequestTableField row1field6 = new WorkflowRequestTableField();
            row1field6.setFieldName("sfsl");
            row1field6.setFieldValue(sendParts.get(i).getSendnum());
            row1field6.setEdit(true);
            rowDatas.add(row1field6);
            row.setRecordOrder(0);
            row.setWorkflowRequestTableFields(rowDatas);
            detailRows.add(row);
        }
        detail1.setWorkflowRequestTableRecords(detailRows);
        details.add(detail1);
        return JSONObject.toJSONString(details);
    }
}
