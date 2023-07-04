package com.example.dingtaihw.DataBase;

import android.os.Bundle;
import android.os.Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBThread implements Runnable{
    public Connection conn;
    private String Result;
    private String input;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    private String option;


    @Override
    public void run() {
         conn=DBUtils.getConn();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            ResultSet resultSet = null;
            resultSet = statement.executeQuery("select "+input+" from uf_smUser where yhm=?" + option);
            Result=resultSet.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Message ms=new Message();
        Bundle data=new Bundle();
        data.putString("Result",Result);

    }
}
