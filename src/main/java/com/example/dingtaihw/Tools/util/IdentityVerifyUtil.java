package com.example.dingtaihw.Tools.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import weaver.rsa.security.RSA;

import java.util.HashMap;
import java.util.Map;

/**
 * description :
 * author ：JHY
 * date : 2020/6/3
 * version : 1.0
 */
public class IdentityVerifyUtil {
    public static final String APPID = "QMSAPPID";
    public static final String HOST = "http://10.18.101.39:8081";
    //系统公钥信息
    private String SPK = null;
    //秘钥信息
    private String SECRET = null;

    private static IdentityVerifyUtil instance;

    public static synchronized IdentityVerifyUtil getInstance() {
        if (instance == null) {
            instance = new IdentityVerifyUtil();
        }
        return instance;
    }

    private IdentityVerifyUtil() {
    }




    /**
     * 获取请求头信息
     * @param token
     * @param userid
     * @param spk
     * @return
     */
    public static Map<String, String> getHttpHeads(String token,String userid,String spk){
        Map<String, String> heads = new HashMap<>();
        heads.put("token", token);
        heads.put("appid", IdentityVerifyUtil.APPID);
        cn.hutool.crypto.asymmetric.RSA rsa = new cn.hutool.crypto.asymmetric.RSA(null,spk);
        //对用户信息进行加密传输,暂仅支持传输OA用户ID
        String encryptUserid = rsa.encryptBase64(userid, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
        heads.put("userid", encryptUserid);
        return heads;
    }




}
