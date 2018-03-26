package com.testyingtou.yingtouproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import fragment.DynamicsFragment;
import fragment.MarketFragment;
import fragment.MineFragment;
import fragment.TransactFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MarketFragment marketFragment;
    private TransactFragment transactFragment;
    private DynamicsFragment dynamicsFragment;
    private MineFragment mineFragment;

    private LinearLayout mLlMarket, mLlTransact, mLlDynamics, mLlMine;
    private TextView mTvMarket, mTvTransact, mTvDynamics, mTvMine;
    //定义FragmentManager对象管理器
    private FragmentManager fm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        initUI();//初始化界面控件
        setChoiceItem(0);//初始化页面加载时显示第一个选项卡
    }

    private void initUI() {

        mLlMarket = (LinearLayout) findViewById(R.id.ll_main_markets);//行情
        mTvMarket = (TextView) findViewById(R.id.tv_main_markets);
        mLlTransact = (LinearLayout) findViewById(R.id.ll_main_transact);//交易
        mTvTransact = (TextView) findViewById(R.id.tv_main_transact);
        mLlDynamics = (LinearLayout) findViewById(R.id.ll_main_dynamics);//动态
        mTvDynamics = (TextView) findViewById(R.id.tv_main_dynamics);
        mLlMine = (LinearLayout) findViewById(R.id.ll_main_mine);//我的
        mTvMine = (TextView) findViewById(R.id.tv_main_mine);

        mLlMarket.setOnClickListener(this);
        mLlTransact.setOnClickListener(this);
        mLlDynamics.setOnClickListener(this);
        mLlMine.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_main_markets:
                setChoiceItem(0);
                break;
            case R.id.ll_main_transact:
                setChoiceItem(1);
                break;
            case R.id.ll_main_dynamics:
                setChoiceItem(2);
                break;
            case R.id.ll_main_mine:
                setChoiceItem(3);
                break;
        }
    }


    public void setChoiceItem(int index) {
        FragmentTransaction ft = fm.beginTransaction();
        // 清空, 重置选项, 隐藏Fragment
        setAttribute(ft);
        switch (index) {
            case 0:
                openMarketFragment(ft);

                break;
            case 1:
                openTransactFragment(ft);

                break;
            case 2:
                openDynamicsFragment(ft);
                break;
            case 3:
                openMineFragment(ft);
                break;
        }
        ft.commit();
    }

    private void openMineFragment(FragmentTransaction ft) {
        // 旧版getResources().getColor过时
        // mTvMine.setTextColor(getResources().getColor(R.color.colorDark));
        mTvMine.setTextColor(ContextCompat.getColor(this, R.color.colorDark));//新版
        mLlMine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue));

        //如果marketFragment为空，则创建一个并添加到界面上
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            ft.add(R.id.frag_main, mineFragment);
        } else {
            //否则就显示出来
            ft.show(mineFragment);
        }
    }

    private void openDynamicsFragment(FragmentTransaction ft) {
        mTvDynamics.setTextColor(ContextCompat.getColor(this, R.color.colorDark));
        mLlDynamics.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue));
        //如果marketFragment为空，则创建一个并添加到界面上
        if (dynamicsFragment == null) {
            dynamicsFragment = new DynamicsFragment();
            ft.add(R.id.frag_main, dynamicsFragment);
        } else {
            //否则就显示出来
            ft.show(dynamicsFragment);
        }
    }

    private void openTransactFragment(FragmentTransaction ft) {
        mTvTransact.setTextColor(ContextCompat.getColor(this, R.color.colorDark));
        mLlTransact.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue));
        //如果marketFragment为空，则创建一个并添加到界面上
        if (transactFragment == null) {
            transactFragment = new TransactFragment();
            ft.add(R.id.frag_main, transactFragment);
        } else {
            //否则就显示出来
            ft.show(transactFragment);
        }
    }

    private void openMarketFragment(FragmentTransaction ft) {
        // firstImage.setImageResource(R.drawable.XXXX)
        mTvMarket.setTextColor(ContextCompat.getColor(this, R.color.colorDark));
        mLlMarket.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue));
        //如果marketFragment为空，则创建一个并添加到界面上
        if (marketFragment == null) {
            marketFragment = new MarketFragment();
            ft.add(R.id.frag_main, marketFragment);
        } else {
            //否则就显示出来
            ft.show(marketFragment);
        }
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认 及frag的隐藏
     */
    private void setAttribute(FragmentTransaction ft) {
        mTvMarket.setTextColor(ContextCompat.getColor(this, R.color.colorCommon));
        mLlMarket.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        mTvTransact.setTextColor(ContextCompat.getColor(this, R.color.colorCommon));
        mLlTransact.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        mTvDynamics.setTextColor(ContextCompat.getColor(this, R.color.colorCommon));
        mLlDynamics.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        mTvMine.setTextColor(ContextCompat.getColor(this, R.color.colorCommon));
        mLlMine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));

        if (marketFragment != null) {
            ft.hide(marketFragment);
        }
        if (transactFragment != null) {
            ft.hide(transactFragment);
        }
        if (dynamicsFragment != null) {
            ft.hide(dynamicsFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }
}
