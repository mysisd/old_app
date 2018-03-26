package com.testyingtou.yingtouproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import utils.PublicSetting;
import utils.UserData;
import utils.UserDataManager;

public class ModifyLoginPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtOldPwd, mEtNewPwd, mEtAgainPwd;
    private UserDataManager mUserDataManager;
    private ImageView mIvClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pwd);
        initUI();
        //建立本地数据库
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }

    }

    /**
     * 监听事件
     */
    private void setListener() {
        findViewById(R.id.iv_modify_login_back).setOnClickListener(this);
        findViewById(R.id.btn_modify_login_ok).setOnClickListener(this);
        mIvClear.setOnClickListener(this);
    }

    private void initUI() {
        mEtOldPwd = (EditText) findViewById(R.id.et_modify_login_old_pwd);
        mEtNewPwd = (EditText) findViewById(R.id.et_modify_login_new_pwd);
        mEtAgainPwd = (EditText) findViewById(R.id.et_modify_login_new_pwd_again);
        mIvClear = (ImageView) findViewById(R.id.iv_modify_login_clear);
        mEtNewPwd.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtNewPwd.getText().toString().trim().length() > 0) {
                    mIvClear.setVisibility(View.VISIBLE);
                } else {
                    mIvClear.setVisibility(View.GONE);
                }
            }

            public void afterTextChanged(Editable s) {

            }
        });
        setListener();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_modify_login_back://返回按钮
                finish();
                break;
            case R.id.iv_modify_login_clear:
                mEtNewPwd.setText("");
                break;
            case R.id.btn_modify_login_ok:
                final String oldPwd = mEtOldPwd.getText().toString().trim();
                final String newPwd = mEtNewPwd.getText().toString().trim();
                String againPwd = mEtAgainPwd.getText().toString().trim();
                if (isNameOrPwdEmpty()) {

                    int queryPwd = mUserDataManager.queryPwd(oldPwd);
                    if (queryPwd <= 0) {
                        Toast.makeText(this, "输入的密码与用户密码不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (oldPwd.equals(newPwd)) {
                        Toast.makeText(this, "新密码不能与旧密码相同", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPwd.equals(againPwd)) {
                        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPwd.length() < 6) {
                        Toast.makeText(this, "密码是由数字+字母组合,长度在6~16位之间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //TODO

                    UserData userData = new UserData(newPwd);
                    mUserDataManager.openDataBase();
                    boolean flag = mUserDataManager.updatePwd(userData);
                    if (flag) {
                        //两秒后自动跳转，并弹出Toast
                        PublicSetting.autoDelay("修改密码成功", this);
                    }


                }

                break;
        }
    }


    public boolean isNameOrPwdEmpty() {
        if (mEtOldPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "原密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtNewPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtAgainPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
