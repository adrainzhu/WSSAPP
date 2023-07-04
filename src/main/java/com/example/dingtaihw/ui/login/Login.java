package com.example.dingtaihw.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.example.dingtaihw.DataBase.DBUtils;
import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.MainActivity;
import com.example.dingtaihw.databinding.ActivityLogin2Binding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.dingtaihw.R;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLogin2Binding binding;

    private int trycounts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        binding.loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username;
                String password;


                username = binding.username.getText().toString();
                password = binding.psdword.getText().toString();
                try {


                    //密码正确 登录成功
                    Runnable oracleTask = new Runnable() {
                        @Override
                        public void run() {
                            String result;
                            String id;
                            String userid;
                            result = DBUtils.getValue("select status from UF_SMUSER where yhm='" + username + "'");
                            if (result.equals("1")) {
                                Looper.prepare();
                                Toast.makeText(Login.this, "账号已锁定，请联系管理员解锁", Toast.LENGTH_LONG).show();
                                Looper.loop();
                                return;
                            } else {

                                result = DBUtils.getValue("select mm from UF_SMUSER where yhm='" + username + "'");
                                if (result.equals(password)) {
                                    Looper.prepare();
                                    id=DBUtils.getValue("select gh from UF_SMUSER where yhm='" + username + "'");
                                    userid=DBUtils.getUserid(id);
                                    System.out.println("id="+id);
                                    bundle.putString("userid",id);
                                    bundle.putString("username",username);
                                    bundle.putString("pushid",userid);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    Looper.loop();
                                } else if (result.equals("No_User")) {
                                    Looper.prepare();
                                    Toast.makeText(Login.this, "用户名不存在，请重新输入", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                } else if (trycounts < 3) {
                                    Looper.prepare();
                                    Toast.makeText(Login.this, "密码错误，请重新输入，失败" + (3 - trycounts) + "次后账号将锁定", Toast.LENGTH_LONG).show();
                                    trycounts++;
                                    Looper.loop();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(Login.this, "账号已锁定，请联系管理员解锁", Toast.LENGTH_LONG).show();
                                    String bool = DBUtils.updatestatus("update UF_SMUSER set status='1' where yhm='" + username + "'");
                                    if (!bool.equals("true")) {
                                        Toast.makeText(Login.this, bool, Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }
                                    Looper.loop();
                                }
                            }

                        }
                    };
                    Thread thread = new Thread(oracleTask);
                    thread.start();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}