package com.testyingtou.yingtouproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import utils.Sms;
import utils.SmsUser;
import utils.UserDataManager;

public class ResetTransactPwdSecondActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvPhone;
    private EditText mEtCode;
    private UserDataManager manager;
    private String randomNum = (int) ((Math.random() * 9 + 1) * 100000) + "";//获取0~9的随机6位数
    Sms sms = new Sms();
    //获取登录的手机号
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_transact_pwd_second);
        mTvPhone = (TextView) findViewById(R.id.tv_reset_transact_second_phone);
        mEtCode = (EditText) findViewById(R.id.et_reset_transact_second_code);
        findViewById(R.id.iv_reset_transact_second_back).setOnClickListener(this);
        findViewById(R.id.btn_reset_transact_second_code).setOnClickListener(this);
        findViewById(R.id.btn_reset_transact_second_next).setOnClickListener(this);

        getPhone();
        //建立本地数据库
        if (manager == null) {
            manager = new UserDataManager(this);
            manager.openDataBase();
        }
    }

    /**
     * 获取登录界面传过来的手机号，并对其进行为*显示方式，例如 134****5678
     */
    private void getPhone() {
        Intent intent = getIntent();
        //获取登录的手机号
        phone = intent.getStringExtra("PHONE");
        //将手机号显示为中间四位是*
        String newPhone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        mTvPhone.setText(newPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_reset_transact_second_back:
                finish();
                break;
            case R.id.btn_reset_transact_second_code:
                SmsUser smsUser = new SmsUser("Tom", randomNum);
                Gson gson = new Gson();//解析Gson
                final String code = gson.toJson(smsUser);
                //启动新线程 用来发送短信
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sms.getSms(phone, code);
                        } catch (Exception e) {
                        }
                    }
                }).start();
                break;
            case R.id.btn_reset_transact_second_next:
                if (mEtCode.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "验证码为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!mEtCode.getText().toString().trim().equals(randomNum)) {
                    Toast.makeText(this, "验证码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(this, "suc", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }
}
