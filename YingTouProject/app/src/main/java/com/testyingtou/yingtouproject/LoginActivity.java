package com.testyingtou.yingtouproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import Interface.RequestServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.Constant;
import utils.DataBaseField;
import utils.PublicSetting;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtPhone, mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        mEtPhone = (EditText) findViewById(R.id.et_login_phone);
        mEtPwd = (EditText) findViewById(R.id.et_login_pwd);
        findViewById(R.id.tv_login_forget).setOnClickListener(this);
        findViewById(R.id.tv_login_register).setOnClickListener(this);
        findViewById(R.id.btn_login_login).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_forget://忘记密码
                startActivity(new Intent(this, ResetActivity.class));
                break;

            case R.id.tv_login_register: //注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.btn_login_login://确定
                login();
                break;
        }
    }

    /**
     * 确定
     */
    private void login() {
        if (isEmpty()) {
            final String phone = mEtPhone.getText().toString().trim();
            String pwd = mEtPwd.getText().toString().trim();

            if (!PublicSetting.isMobile(phone)) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Constant.URL_LOGIN)
                    .build();
            RequestServices loginServices = retrofit.create(RequestServices.class);
            Call<ResponseBody> call = loginServices.getLessUrl("logins?phone=" + phone + "&password=" + pwd);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json = response.body().string();
                        Gson gson = new Gson();
                        DataBaseField bean = gson.fromJson(json, DataBaseField.class);
                        //bean.getLogin():成功返回success,失败返回error
                        if ("success".equals(bean.getLogin())) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            String userId = bean.getOrign();
                            intent.putExtra(Constant.USER_ID, userId);
                            intent.putExtra(Constant.PHONE, phone);
                            startActivity(intent);
                        } else if ("error".equals(bean.getLogin())) {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }


    /**
     * 判断帐号或者密码是否为空
     */
    public boolean isEmpty() {
        if (mEtPhone.getText().toString().trim().equals("")) {
            Toast.makeText(this, "帐号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * onResume返回页面时调用一次
     * 当从个人中心返回到页面时，清空密码框的内容
     */
    public void onResume() {
        mEtPwd.setText("");
        super.onResume();
    }

}
