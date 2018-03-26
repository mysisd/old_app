package com.testyingtou.yingtouproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;

import Interface.RequestServices;
import fragment.OrdersBuyLimitFragment;
import fragment.OrdersBuyMarketFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.DataBaseField;

public class BuyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvMarketOrder, mTvLimitOrder, mTvPrice, mTvTotalMoney, mTvBuyPrice, mTvSellPrice, mTvMaxNum, mTvTitle;
    private OrdersBuyMarketFragment mFragOrdersMarket;
    private OrdersBuyLimitFragment mFragOrdersLimit;
    private String mBuyPrice, mSellPrice, mCommodityNo, mNumber,  mUserId;
    private EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        initUI();
        setData();
        initRetrofit();


    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://xmyttz.com/rx/rx/")
                .build();
        RequestServices services = retrofit.create(RequestServices.class);
        Call<ResponseBody> call = services.getLessUrl("moneys?username=" + mUserId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    mTvTotalMoney.setText(json);
                    int mMaxNum = (int) (Double.parseDouble(json) / Double.parseDouble(mBuyPrice));
                    mTvMaxNum.setText(String.valueOf(mMaxNum));
                    setChoiceItem(0);//设置默认显示frag
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void setData() {
        Intent intent = getIntent();
        mCommodityNo = intent.getStringExtra("COMMODITY_NO");
        mNumber = intent.getStringExtra("NUMBER");
        mBuyPrice = intent.getStringExtra("BUY_PRICE");
        mSellPrice = intent.getStringExtra("SELL_PRICE");
        mUserId = intent.getStringExtra("USER_ID");

        String title = mCommodityNo + mNumber;
        mTvTitle.setText(title);
        mTvPrice.setText(mBuyPrice);
        mTvBuyPrice.setText(mBuyPrice);
        mTvSellPrice.setText(mSellPrice);
    }

    private void initUI() {
        mTvMarketOrder = (TextView) findViewById(R.id.tv_buy_market_order);
        mTvLimitOrder = (TextView) findViewById(R.id.tv_buy_limit_order);
        mTvPrice = (TextView) findViewById(R.id.tv_buy_price);
        mTvBuyPrice = (TextView) findViewById(R.id.tv_sell_buy_price);
        mTvSellPrice = (TextView) findViewById(R.id.tv_sell_sell_price);
        mTvTotalMoney = (TextView) findViewById(R.id.tv_buy_total_money);
        mTvMaxNum = (TextView) findViewById(R.id.tv_buy_max_num);
        mTvTitle = (TextView) findViewById(R.id.tv_buy_title);

        mTvMarketOrder.setOnClickListener(this);
        mTvLimitOrder.setOnClickListener(this);
        findViewById(R.id.iv_buy_back).setOnClickListener(this);
        findViewById(R.id.btn_buy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_buy_market_order:
                setChoiceItem(0);
                break;
            case R.id.tv_buy_limit_order:
                setChoiceItem(1);
                break;
            case R.id.iv_buy_back:
                finish();
                break;
            case R.id.btn_buy:
                //获取frag的控件
                mEtContent = (EditText) mFragOrdersMarket.getView().findViewById(R.id.et_order_buy_market);
                Log.e("mEtContent", mEtContent.getText().toString() + "- -");

                Retrofit retrofit = new Retrofit
                        .Builder()
                        .baseUrl("http://xmyttz.com/rx/rx/")
                        .build();
                RequestServices services = retrofit.create(RequestServices.class);
                Call<ResponseBody> call = services.getLessUrl(
                        "add_position?hand=" + mEtContent.getText().toString()
                                + "&pay=" + mBuyPrice
                                + "&sell=" + mSellPrice
                                + "&type=" + mCommodityNo
                                + "&num=" + mNumber
                                + "&username=" + mUserId);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            final double result = Double.parseDouble(mTvTotalMoney.getText().toString()) -
                                    (Double.parseDouble(mEtContent.getText().toString()) * Double.parseDouble(mBuyPrice));
                            if (result >= 0) {
                                final String json = response.body().string();
                                Gson gson = new Gson();
                                DataBaseField bean = gson.fromJson(json, DataBaseField.class);
                                String res = bean.getRes();
                                Log.e("res", res);
                                if ("success".equals(res)) {
                                    new AlertDialog.Builder(BuyActivity.this)
                                            .setMessage("购买成功")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        //保留小数点后两位(不足两位自动补齐)
                                                        DecimalFormat df = new DecimalFormat("######0.00");
                                                        String format = df.format(result);
                                                        mTvTotalMoney.setText(format);
                                                        int finalResult = (int) (Double.parseDouble(format) / Double.parseDouble(mBuyPrice));
                                                        mTvMaxNum.setText(String.valueOf(finalResult));
                                                        mEtContent.setText("1");
                                                        Log.e("btn", result+"s");
                                                        //TODO 购买一次之后,最多可购买的数量没有重置

                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            })
                                            .show();
                                } else {
                                    new AlertDialog.Builder(BuyActivity.this)
                                            .setMessage("购买失败")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            } else {
                                new AlertDialog.Builder(BuyActivity.this)
                                        .setMessage("当前资金不足,购买失败")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mEtContent.setText("1");
                                            }
                                        })
                                        .show();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(BuyActivity.this,"手数不能为空",Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                break;
        }
    }

    /**
     * 判断当前显示是哪个模块
     */
    public void setChoiceItem(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        setAttribute(ft);

        switch (index) {
            case 0:
                mTvMarketOrder.setTextColor(ContextCompat.getColor(this, R.color.colorLightYellow));
                if (mFragOrdersMarket == null) {
                    mFragOrdersMarket = new OrdersBuyMarketFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("MAX_NUM", mTvMaxNum.getText().toString());
                    Log.e("MAX_NUM", ":" + mTvMaxNum.getText().toString());
                    mFragOrdersMarket.setArguments(bundle);
                    ft.add(R.id.fl_buy, mFragOrdersMarket);

                } else {
                    ft.show(mFragOrdersMarket);
                }
                break;
            case 1:
                mTvLimitOrder.setTextColor(ContextCompat.getColor(this, R.color.colorLightYellow));
                if (mFragOrdersLimit == null) {
                    mFragOrdersLimit = new OrdersBuyLimitFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("PRICE", mTvPrice.getText().toString());
                    bundle.putString("MONEY", mTvTotalMoney.getText().toString());
                    mFragOrdersLimit.setArguments(bundle);
                    ft.add(R.id.fl_buy, mFragOrdersLimit);

                } else {
                    ft.show(mFragOrdersLimit);
                }
                break;
        }
        ft.commit();
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认 及frag的隐藏
     */
    private void setAttribute(FragmentTransaction ft) {
        mTvMarketOrder.setTextColor(ContextCompat.getColor(this, R.color.colorDark));
        mTvLimitOrder.setTextColor(ContextCompat.getColor(this, R.color.colorDark));
        if (mFragOrdersMarket != null) {
            ft.hide(mFragOrdersMarket);
        }
        if (mFragOrdersLimit != null) {
            ft.hide(mFragOrdersLimit);
        }
    }
}
