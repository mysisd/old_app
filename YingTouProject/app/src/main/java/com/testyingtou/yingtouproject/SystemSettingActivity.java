package com.testyingtou.yingtouproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SystemSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private int timeIndex;
    private int networkIndex;
    private int wifiIndex;
    private String[] timeItems = new String[]{"10分钟", "30分钟", "1小时", "2小时", "4小时", "6小时", "8小时", "不提示"};
    private String[] networkItems = new String[]{"10秒", "5秒", "500毫秒", "不刷新"};
    private String[] wifiItems = new String[]{"1秒", "500毫秒", "不刷新"};
    private TimeOnClick timeOnClick = new TimeOnClick();
    private NetworkOnClick networkOnClick = new NetworkOnClick();
    private WifiOnClick wifiOnClick = new WifiOnClick();
    private TextView mTvTime;
    private TextView mTvNetwork;
    private TextView mTvWifi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.rl_system_time).setOnClickListener(this);
        findViewById(R.id.rl_system_network).setOnClickListener(this);
        findViewById(R.id.rl_system_wifi).setOnClickListener(this);
        findViewById(R.id.iv_system_back).setOnClickListener(this);
        findViewById(R.id.rl_system_modify_login_password).setOnClickListener(this);
        findViewById(R.id.rl_system_modify_transact_password).setOnClickListener(this);
        findViewById(R.id.rl_system_reset_transact_password).setOnClickListener(this);
        mTvTime = (TextView) findViewById(R.id.tv_system_time);
        mTvNetwork = (TextView) findViewById(R.id.tv_system_network);
        mTvWifi = (TextView) findViewById(R.id.tv_system_wifi);
    }

    /**
     * 显示对话框
     *
     * @param item            ：数组
     * @param index           ：数组索引
     * @param onClickListener ：监听事件
     */
    private void showDialog(String[] item, int index, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setSingleChoiceItems(item, index, onClickListener)
                .setPositiveButton("确定", onClickListener)
                .setNegativeButton("取消", onClickListener)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_system_time:
                showDialog(timeItems, timeIndex, timeOnClick);
                break;
            case R.id.rl_system_network:
                showDialog(networkItems, networkIndex, networkOnClick);
                break;
            case R.id.rl_system_wifi:
                showDialog(wifiItems, wifiIndex, wifiOnClick);
                break;
            case R.id.iv_system_back:
                finish();
                break;
            case R.id.rl_system_modify_login_password://修改登录密码
                startActivity(new Intent(this,ModifyLoginPwdActivity.class));
                break;
            case R.id.rl_system_modify_transact_password:
                startActivity(new Intent(this,ModifyTransactPwdActivity.class));
                break;
            case R.id.rl_system_reset_transact_password:
                startActivity(new Intent(this,ResetTransactPwdActivity.class));
                break;
        }
    }

    class TimeOnClick implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            //which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
            if (which >= 0) {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                timeIndex = which;
            } else {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mTvTime.setText(timeItems[timeIndex]);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                }
            }
        }
    }

    class NetworkOnClick implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            //which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
            if (which >= 0) {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                networkIndex = which;
            } else {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mTvNetwork.setText(networkItems[networkIndex]);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                }
            }
        }
    }

    class WifiOnClick implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            //which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
            if (which >= 0) {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                wifiIndex = which;
            } else {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mTvWifi.setText(wifiItems[wifiIndex]);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                }
            }
        }
    }
}
