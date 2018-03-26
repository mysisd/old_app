package com.testyingtou.yingtouproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import Interface.RequestServices;
import fragment.OrdersSellLimitFragment;
import fragment.OrdersSellMarketFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SellActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvMarketOrder, mTvLimitOrder, mTvPrice, mTvTotalMoney, mTvMaxNum, mTvTitle, mTvBuyPrice, mTvSellPrice;
    private OrdersSellMarketFragment mFragOrdersMarket;
    private OrdersSellLimitFragment mFragOrdersLimit;
    private String mBuyPrice, mSellPrice, mCommodityNo, mNumber, format, mUserId;
    private EditText mEtContent;
    private RequestServices mServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        initUI();
        setData();
        initRetrofit();
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
        mTvTitle = (TextView) findViewById(R.id.tv_sell_title);
        mTvPrice = (TextView) findViewById(R.id.tv_sell_price);
        mTvBuyPrice = (TextView) findViewById(R.id.tv_sell_buy_price);
        mTvSellPrice = (TextView) findViewById(R.id.tv_sell_sell_price);
        mTvMaxNum = (TextView) findViewById(R.id.tv_sell_max_num);
        mTvMarketOrder = (TextView) findViewById(R.id.tv_sell_market_order);
        mTvLimitOrder = (TextView) findViewById(R.id.tv_sell_limit_order);
        mTvTotalMoney = (TextView) findViewById(R.id.tv_sell_total_money);
        mTvMarketOrder.setOnClickListener(this);
        mTvLimitOrder.setOnClickListener(this);
        findViewById(R.id.iv_sell_back).setOnClickListener(this);
        findViewById(R.id.btn_sell).setOnClickListener(this);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://xmyttz.com/rx/rx/")
                .build();
        mServices = retrofit.create(RequestServices.class);
        Call<ResponseBody> callMoney = mServices.getLessUrl("moneys?username=" + mUserId);

        callMoney.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.e("moneys", json);
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

        Call<ResponseBody> callHand = mServices.getLessUrl("hand_nums?num=" + mNumber + "&type=" + mCommodityNo + "&username=" + mUserId);
        callHand.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.e("hand_num", json);
                    mTvMaxNum.setText(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sell_market_order:
                setChoiceItem(0);
                break;
            case R.id.tv_sell_limit_order:
                setChoiceItem(1);
                break;
            case R.id.iv_sell_back:
                finish();
                break;
            case R.id.btn_sell:
//                //获取frag的控件
                mEtContent = (EditText) mFragOrdersMarket.getView().findViewById(R.id.et_order_sell_market);
                Log.e("mEtContent", mEtContent.getText().toString() + "- -");
//

                Retrofit retrofit = new Retrofit
                        .Builder()
                        .baseUrl("http://xmyttz.com/rx/rx/")
                        .build();
                RequestServices services = retrofit.create(RequestServices.class);
                Call<ResponseBody> call = services.getLessUrl(
                        "sell_options?hand=" + mEtContent.getText().toString()
                                + "&pay=" + mBuyPrice
                                + "&sell=" + mSellPrice
                                + "&type=" + mCommodityNo
                                + "&num=" + mNumber
                                + "&username=" + mUserId);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            double result =
                                    Double.parseDouble(mTvMaxNum.getText().toString()) -
                                            Double.parseDouble(mEtContent.getText().toString());
                            if (result >= 0) {
//                                String json = response.body().string();
//                                Gson gson = new Gson();
//                                InteractionBean bean = gson.fromJson(json, InteractionBean.class);
//                                String res = bean.getRes();
//                                Log.e("json", json+ "1");
//                                Log.e("res", res);
//                                if ("success".equals(res)) {
//                                    new AlertDialog.Builder(SellActivity.this)
//                                            .setMessage("卖出成功")
//                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    try {
//                                                        mEtContent.setText("1");
//                                                        Log.e("btn", mTvTotalMoney.getText().toString() + "s");
//
//                                                        //TODO 购买一次之后,最多可购买的数量没有重置
//
//                                                    } catch (NumberFormatException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            })
//                                            .show();
//                                } else {
                                    new AlertDialog.Builder(SellActivity.this)
                                            .setMessage("卖出成功")
                                            .setPositiveButton("确定", null)
                                            .show();
//                                }
                            } else {
                                new AlertDialog.Builder(SellActivity.this)
                                        .setMessage("手数不足,不能卖出更多")
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
                            Toast.makeText(SellActivity.this, "手数不能为空", Toast.LENGTH_SHORT).show();
                        }

//                        catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
//
//

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
                    mFragOrdersMarket = new OrdersSellMarketFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("MAX_NUM", "3567");
//                    bundle.putString("MAX_NUM", mTvMaxNum.getText().toString());
//                    Log.e("MAX_NUM", ":" + mTvMaxNum.getText().toString());
                    mFragOrdersMarket.setArguments(bundle);
                    ft.add(R.id.fl_sell, mFragOrdersMarket);

                } else {
                    ft.show(mFragOrdersMarket);
                }
                break;
            case 1:
                mTvLimitOrder.setTextColor(ContextCompat.getColor(this, R.color.colorLightYellow));
                if (mFragOrdersLimit == null) {
                    mFragOrdersLimit = new OrdersSellLimitFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("PRICE", mTvPrice.getText().toString());
                    bundle.putString("MONEY", mTvTotalMoney.getText().toString());
                    mFragOrdersLimit.setArguments(bundle);
                    ft.add(R.id.fl_sell, mFragOrdersLimit);

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
