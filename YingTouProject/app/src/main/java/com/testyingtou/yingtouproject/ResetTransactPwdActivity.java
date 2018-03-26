package com.testyingtou.yingtouproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import utils.PublicSetting;
import utils.UserDataManager;

public class ResetTransactPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtName;
    private UserDataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_transact_pwd);
        findViewById(R.id.iv_reset_transact_back).setOnClickListener(this);
        findViewById(R.id.btn_reset_transact_next).setOnClickListener(this);
        mEtName = (EditText) findViewById(R.id.et_reset_transact_name);
        if (manager == null) {
            manager = new UserDataManager(this);
            manager.openDataBase();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_reset_transact_back://返回
                finish();
                break;
            case R.id.btn_reset_transact_next://下一步

                String name = mEtName.getText().toString().trim();
                //检查用户是否存在
                int existName = manager.queryName(name);
                if (name.equals("")) {
                    Toast.makeText(this, "手机号/用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查输入的手机号是否合法
                if (!PublicSetting.isMobile(name)) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用户不存在时返回
                if (existName <= 0) {
                    Toast.makeText(this, "该手机号/用户名尚未注册", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, ResetTransactPwdSecondActivity.class);
                intent.putExtra("PHONE",name);
                startActivity(intent);
                break;
        }
    }

}
