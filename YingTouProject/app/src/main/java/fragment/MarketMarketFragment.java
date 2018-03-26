package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.testyingtou.yingtouproject.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Interface.RequestServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.Constant;
import utils.DishEvent;
import utils.DividerItemDecoration;
import utils.FiveEvent;
import utils.FuturesBean;
import utils.MarketBean;
import utils.PublicSetting;

public class MarketMarketFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fm;
    private KLineFragment mFragKline;
    private TimeShareFragment mFragShare;
    private DishFragment mFragDish;
    private FiveFragment mFragFive;
    private static List<String> mRvData;
    private TextView mTvOilTitle, mTvOilPrice, mTvOilApplies, mTvOilSwing, mTvGoldTitle, mTvGoldPrice, mTvGoldApplies, mTvGoldSwing, mTvHsiTitle, mTvHsiPrice,
            mTvHsiApplies, mTvHsiSwing, mTvKline, mTvShare, mTvDish, mTvFive, mTvName, mTvSubName, mTvPrice, mTvApplies;
    //下划线
    private View mUnderlineKline, mUnderlineShare, mUnderlineDish, mUnderlineFive, mDivider;
    private boolean fragState;
    private FrameLayout mFlState;
    private LinearLayout mLlState;
    private RelativeLayout mRlOil, mRlGold, mRlHsi;
    //标题,价格,涨跌幅,涨跌幅(百分百),涨跌额,开盘,最高,成交量,持仓量,昨结,最低,振幅,卖价1.2.3.4.5,卖量1.2.3.4.5,买价1.2.3.4.5,买量1.2.3.4.5
    private String mOilCommodityNo, mOilNumber, mOilTitle, mOilSubTitle, mOilPrice, mOilApplies, mOilAppliesPercent, mOilRiseOrFall, mOilOpenPrice, mOilHighestPrice, mOilVolume, mOilHoldings, mOilYesterday,
            mOilLowest, mOilAmplitudePercent, mOilSellPrice1, mOilSellPrice2, mOilSellPrice3, mOilSellPrice4, mOilSellPrice5, mOilSellQuantity1, mOilSellQuantity2,
            mOilSellQuantity3, mOilSellQuantity4, mOilSellQuantity5, mOilBuyPrice1, mOilBuyPrice2, mOilBuyPrice3, mOilBuyPrice4, mOilBuyPrice5,
            mOilBuyQuantity1, mOilBuyQuantity2, mOilBuyQuantity3, mOilBuyQuantity4, mOilBuyQuantity5,
            mGoldCommodityNo, mGoldNumber, mGoldTitle, mGoldSubTitle, mGoldPrice, mGoldApplies, mGoldAppliesPercent, mGoldRiseOrFall, mGoldOpenPrice, mGoldHighestPrice, mGoldVolume, mGoldHoldings,
            mGoldYesterday, mGoldLowest, mGoldAmplitudePercent, mGoldSellPrice1, mGoldSellPrice2, mGoldSellPrice3, mGoldSellPrice4, mGoldSellPrice5, mGoldSellQuantity1,
            mGoldSellQuantity2, mGoldSellQuantity3, mGoldSellQuantity4, mGoldSellQuantity5, mGoldBuyPrice1, mGoldBuyPrice2, mGoldBuyPrice3, mGoldBuyPrice4,
            mGoldBuyPrice5, mGoldBuyQuantity1, mGoldBuyQuantity2, mGoldBuyQuantity3, mGoldBuyQuantity4, mGoldBuyQuantity5,
            mHsiCommodityNo, mHsiNumber, mHsiTitle, mHsiSubTitle, mHsiPrice, mHsiApplies, mHsiAppliesPercent, mHsiRiseOrFall, mHsiOpenPrice, mHsiHighestPrice, mHsiVolume, mHsiHoldings, mHsiYesterday,
            mHsiLowest, mHsiAmplitudePercent, mHsiSellPrice1, mHsiSellPrice2, mHsiSellPrice3, mHsiSellPrice4, mHsiSellPrice5, mHsiSellQuantity1, mHsiSellQuantity2,
            mHsiSellQuantity3, mHsiSellQuantity4, mHsiSellQuantity5, mHsiBuyPrice1, mHsiBuyPrice2, mHsiBuyPrice3, mHsiBuyPrice4, mHsiBuyPrice5,
            mHsiBuyQuantity1, mHsiBuyQuantity2, mHsiBuyQuantity3, mHsiBuyQuantity4, mHsiBuyQuantity5,
            commodityNo, number, buyPrice, riseOrFall, openPrice, highest, volume, holdings, sellPrice, appliesPercent, yesterday, lowest, amplitudePercent,
            sellPrice1, sellPrice2, sellPrice3, sellPrice4, sellPrice5, sellQuantity1, sellQuantity2, sellQuantity3, sellQuantity4, sellQuantity5,
            buyPrice1, buyPrice2, buyPrice3, buyPrice4, buyPrice5, buyQuantity1, buyQuantity2, buyQuantity3, buyQuantity4, buyQuantity5;

    private Bundle bundle;
    public TimerHandler timerHandler;
    public Timer timer;
    private String mUserId;
    private RequestServices mServices;
    private List<MarketBean.ResBean> list;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_market_market, container, false);

        //获取Retrofit对象，设置地址
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Constant.URL)
                .build();
        //创建接口
        mServices = retrofit.create(RequestServices.class);
        list = new ArrayList<>();

        Intent intent = getActivity().getIntent();
        //获取登录用户ID
        mUserId = intent.getStringExtra(Constant.USER_ID);
        initRvData();
        initUI(layout);
        bundle = new Bundle();

        return layout;
    }


    private class MyTimerTask extends TimerTask {
        public void run() {
            timerHandler.sendEmptyMessage(0);
        }
    }

    private class TimerHandler extends Handler {
        public void handleMessage(Message msg) {
            //定时器代码实现地方
            initRetrofit();
        }
    }

    private void initDefaultData() {
        if (mOilApplies != null) {
            mTvName.setText(mOilTitle);
            mTvSubName.setText(mOilSubTitle);
            mTvPrice.setText(mOilPrice);
            mTvApplies.setText(mOilAppliesPercent);
            if (Double.parseDouble(mOilApplies) >= 0) {
                mTvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                mTvApplies.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
            } else {
                mTvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                mTvApplies.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            }
        }
    }

    /**
     * 网络框架:Retrofit 解析网页数据
     */
    private void initRetrofit() {

        //实现接口方法(解析美原油)
        Call<ResponseBody> callCL = mServices.getCL();
        callCL.enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    try {
                        //返回的结果保存在response.body()中
                        String result = response.body().string();
                        //onResponse方法是运行在主线程也就是UI线程的，在这里实现具体内容
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            String json = array.getString(i);
                            //解析json数据
                            Gson gson = new Gson();
                            FuturesBean oilBean = gson.fromJson(json, FuturesBean.class);

                            mOilCommodityNo = oilBean.getCommodityNo();//CL
                            mOilNumber = oilBean.getContractNo1();//1801
                            mOilPrice = oilBean.getQLastPrice(); //58.29
                            mOilApplies = oilBean.getQChangeRate(); //1.55116..
                            mOilAppliesPercent = PublicSetting.getChanged(mOilApplies) + "%";
                            String qSwing = oilBean.getQSwing();
                            mOilAmplitudePercent = PublicSetting.getChanged(qSwing) + "%";

                            mOilTitle = Constant.US_OIL + mOilNumber;
                            //CL1801
                            mOilSubTitle = mOilCommodityNo + mOilNumber;
                            mTvOilTitle.setText(mOilTitle);
                            mTvOilPrice.setText(mOilPrice);
                            mTvOilApplies.setText(mOilAppliesPercent);
                            mTvOilSwing.setText(mOilAmplitudePercent);
                            if (mOilApplies != null) {
                                if (Double.parseDouble(mOilApplies) >= 0) {//Double.parseDouble()将字符串转成double类型
                                    mRlOil.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                                } else {
                                    mRlOil.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                }
                            }

                            mOilRiseOrFall = oilBean.getQChangeValue();
                            mOilOpenPrice = oilBean.getQOpeningPrice();
                            mOilHighestPrice = oilBean.getQHighPrice();
                            mOilVolume = oilBean.getQTotalQty();
                            mOilHoldings = oilBean.getQPositionQty();
                            mOilYesterday = oilBean.getQPreSettlePrice();
                            mOilLowest = oilBean.getQLowPrice();


                            mOilSellPrice1 = oilBean.getQAskPrice1();
                            mOilSellPrice2 = oilBean.getQAskPrice2();
                            mOilSellPrice3 = oilBean.getQAskPrice3();
                            mOilSellPrice4 = oilBean.getQAskPrice4();
                            mOilSellPrice5 = oilBean.getQAskPrice5();

                            mOilSellQuantity1 = oilBean.getQAskQty1();
                            mOilSellQuantity2 = oilBean.getQAskQty2();
                            mOilSellQuantity3 = oilBean.getQAskQty3();
                            mOilSellQuantity4 = oilBean.getQAskQty4();
                            mOilSellQuantity5 = oilBean.getQAskQty5();

                            mOilBuyPrice1 = oilBean.getQBidPrice1();
                            mOilBuyPrice2 = oilBean.getQBidPrice2();
                            mOilBuyPrice3 = oilBean.getQBidPrice3();
                            mOilBuyPrice4 = oilBean.getQBidPrice4();
                            mOilBuyPrice5 = oilBean.getQBidPrice5();

                            mOilBuyQuantity1 = oilBean.getQBidQty1();
                            mOilBuyQuantity2 = oilBean.getQBidQty2();
                            mOilBuyQuantity3 = oilBean.getQBidQty3();
                            mOilBuyQuantity4 = oilBean.getQBidQty4();
                            mOilBuyQuantity5 = oilBean.getQBidQty5();

                            initDefaultData();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        //实现接口方法(解析美黄金)
        Call<ResponseBody> callGC = mServices.getGC();
        callGC.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    try {
                        //返回的结果保存在response.body()中
                        String result = response.body().string();
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            String json = array.getString(i);
                            Gson gson = new Gson();
                            FuturesBean goldBean = gson.fromJson(json, FuturesBean.class);
                            mGoldCommodityNo = goldBean.getCommodityNo();//GC
                            mGoldNumber = goldBean.getContractNo1();//1712
                            mGoldPrice = goldBean.getQLastPrice();//1279.7
                            mGoldApplies = goldBean.getQChangeRate(); //0.510525..
                            mGoldAppliesPercent = PublicSetting.getChanged(mGoldApplies) + "%";
                            String qSwing = goldBean.getQSwing();//1.382344..
                            mGoldAmplitudePercent = PublicSetting.getChanged(qSwing) + "%";

                            mGoldSubTitle = mGoldCommodityNo + mGoldNumber;
                            mGoldTitle = Constant.US_GOLD + mGoldNumber;
                            mTvGoldTitle.setText(mGoldTitle);
                            mTvGoldPrice.setText(mGoldPrice);
                            mTvGoldApplies.setText(mGoldAppliesPercent);
                            mTvGoldSwing.setText(mGoldAmplitudePercent);
                            if (mGoldApplies != null) {
                                if (Double.parseDouble(mGoldApplies) >= 0) {//Double.parseDouble()将字符串转成double类型
                                    mRlGold.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                                } else {
                                    mRlGold.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                }
                            }
                            mGoldRiseOrFall = goldBean.getQChangeValue();
                            mGoldOpenPrice = goldBean.getQOpeningPrice();
                            mGoldHighestPrice = goldBean.getQHighPrice();
                            mGoldVolume = goldBean.getQTotalQty();
                            mGoldHoldings = goldBean.getQPositionQty();
                            mGoldYesterday = goldBean.getQPreSettlePrice();
                            mGoldLowest = goldBean.getQLowPrice();

                            mGoldSellPrice1 = goldBean.getQAskPrice1();
                            mGoldSellPrice2 = goldBean.getQAskPrice2();
                            mGoldSellPrice3 = goldBean.getQAskPrice3();
                            mGoldSellPrice4 = goldBean.getQAskPrice4();
                            mGoldSellPrice5 = goldBean.getQAskPrice5();

                            mGoldSellQuantity1 = goldBean.getQAskQty1();
                            mGoldSellQuantity2 = goldBean.getQAskQty2();
                            mGoldSellQuantity3 = goldBean.getQAskQty3();
                            mGoldSellQuantity4 = goldBean.getQAskQty4();
                            mGoldSellQuantity5 = goldBean.getQAskQty5();

                            mGoldBuyPrice1 = goldBean.getQBidPrice1();
                            mGoldBuyPrice2 = goldBean.getQBidPrice2();
                            mGoldBuyPrice3 = goldBean.getQBidPrice3();
                            mGoldBuyPrice4 = goldBean.getQBidPrice4();
                            mGoldBuyPrice5 = goldBean.getQBidPrice5();

                            mGoldBuyQuantity1 = goldBean.getQBidQty1();
                            mGoldBuyQuantity2 = goldBean.getQBidQty2();
                            mGoldBuyQuantity3 = goldBean.getQBidQty3();
                            mGoldBuyQuantity4 = goldBean.getQBidQty4();
                            mGoldBuyQuantity5 = goldBean.getQBidQty5();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        //实现接口方法(解析恒生指数)
        Call<ResponseBody> callHSI = mServices.getHSI();
        callHSI.enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    try {
                        String result = response.body().string();
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            String json = array.getString(i);
                            Gson gson = new Gson();
                            FuturesBean hsiBean = gson.fromJson(json, FuturesBean.class);
                            mHsiCommodityNo = hsiBean.getCommodityNo();//HSI
                            mHsiNumber = hsiBean.getContractNo1();//1712
                            mHsiPrice = hsiBean.getQLastPrice();//1279.7
                            mHsiApplies = hsiBean.getQChangeRate(); //0.510525..
                            mHsiAppliesPercent = PublicSetting.getChanged(mHsiApplies) + "%";
                            String qSwing = hsiBean.getQSwing();//1.382344..
                            mHsiAmplitudePercent = PublicSetting.getChanged(qSwing) + "%";

                            mHsiSubTitle = mHsiCommodityNo + mHsiNumber;
                            mHsiTitle = Constant.HANG_SEND + mHsiNumber;
                            mTvHsiTitle.setText(mHsiTitle);
                            mTvHsiPrice.setText(mHsiPrice);
                            mTvHsiApplies.setText(mHsiAppliesPercent);
                            mTvHsiSwing.setText(mHsiAmplitudePercent);
                            if (mHsiApplies != null) {
                                if (Double.parseDouble(mHsiApplies) >= 0) {//Double.parseDouble()将字符串转成double类型
                                    mRlHsi.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                                } else {
                                    mRlHsi.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                }
                            }
                            mHsiRiseOrFall = hsiBean.getQChangeValue();
                            mHsiOpenPrice = hsiBean.getQOpeningPrice();
                            mHsiHighestPrice = hsiBean.getQHighPrice();
                            mHsiVolume = hsiBean.getQTotalQty();
                            mHsiHoldings = hsiBean.getQPositionQty();
                            mHsiYesterday = hsiBean.getQPreSettlePrice();
                            mHsiLowest = hsiBean.getQLowPrice();

                            mHsiSellPrice1 = hsiBean.getQAskPrice1();
                            mHsiSellPrice2 = hsiBean.getQAskPrice2();
                            mHsiSellPrice3 = hsiBean.getQAskPrice3();
                            mHsiSellPrice4 = hsiBean.getQAskPrice4();
                            mHsiSellPrice5 = hsiBean.getQAskPrice5();

                            mHsiSellQuantity1 = hsiBean.getQAskQty1();
                            mHsiSellQuantity2 = hsiBean.getQAskQty2();
                            mHsiSellQuantity3 = hsiBean.getQAskQty3();
                            mHsiSellQuantity4 = hsiBean.getQAskQty4();
                            mHsiSellQuantity5 = hsiBean.getQAskQty5();

                            mHsiBuyPrice1 = hsiBean.getQBidPrice1();
                            mHsiBuyPrice2 = hsiBean.getQBidPrice2();
                            mHsiBuyPrice3 = hsiBean.getQBidPrice3();
                            mHsiBuyPrice4 = hsiBean.getQBidPrice4();
                            mHsiBuyPrice5 = hsiBean.getQBidPrice5();

                            mHsiBuyQuantity1 = hsiBean.getQBidQty1();
                            mHsiBuyQuantity2 = hsiBean.getQBidQty2();
                            mHsiBuyQuantity3 = hsiBean.getQBidQty3();
                            mHsiBuyQuantity4 = hsiBean.getQBidQty4();
                            mHsiBuyQuantity5 = hsiBean.getQBidQty5();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }


    private void initRvData() {
        mRvData = new ArrayList<>();
        mRvData.add("美原油  ");
        mRvData.add(" 美黄金  ");
        mRvData.add(" 恒生指数  ");
    }

    private View initUI(View layout) {

        fm = getFragmentManager();

        mTvOilTitle = (TextView) layout.findViewById(R.id.tv_markets_oil_title);
        mTvOilPrice = (TextView) layout.findViewById(R.id.tv_markets_oil_price);
        mTvOilApplies = (TextView) layout.findViewById(R.id.tv_markets_oil_applies);
        mTvOilSwing = (TextView) layout.findViewById(R.id.tv_markets_oil_swing);
        mTvGoldTitle = (TextView) layout.findViewById(R.id.tv_markets_gold_title);
        mTvGoldPrice = (TextView) layout.findViewById(R.id.tv_markets_gold_price);
        mTvGoldApplies = (TextView) layout.findViewById(R.id.tv_markets_gold_applies);
        mTvGoldSwing = (TextView) layout.findViewById(R.id.tv_markets_gold_swing);
        mTvHsiTitle = (TextView) layout.findViewById(R.id.tv_markets_hsi_title);
        mTvHsiPrice = (TextView) layout.findViewById(R.id.tv_markets_hsi_price);
        mTvHsiApplies = (TextView) layout.findViewById(R.id.tv_markets_hsi_applies);
        mTvHsiSwing = (TextView) layout.findViewById(R.id.tv_markets_hsi_swing);
        mTvKline = (TextView) layout.findViewById(R.id.tv_markets_kline);
        mTvShare = (TextView) layout.findViewById(R.id.tv_markets_share);
        mTvDish = (TextView) layout.findViewById(R.id.tv_markets_dish);
        mTvFive = (TextView) layout.findViewById(R.id.tv_markets_five);
        mTvName = (TextView) layout.findViewById(R.id.tv_markets_name);
        mTvSubName = (TextView) layout.findViewById(R.id.tv_markets_sub_name);
        mTvApplies = (TextView) layout.findViewById(R.id.tv_markets_applies);
        mTvPrice = (TextView) layout.findViewById(R.id.tv_markets_price);

        mTvKline.setOnClickListener(this);
        mTvShare.setOnClickListener(this);
        mTvDish.setOnClickListener(this);
        mTvFive.setOnClickListener(this);
//        layout.findViewById(R.id.iv_markets_more).setOnClickListener(this);

        initRv(layout);
//        initPopWin(inflater, container);

        layout.findViewById(R.id.rl_markets_data).setOnClickListener(this);
        mFlState = (FrameLayout) layout.findViewById(R.id.fl_markets);
        mLlState = (LinearLayout) layout.findViewById(R.id.ll_markets_data);

        mRlOil = (RelativeLayout) layout.findViewById(R.id.rl_markets_oil);
        mRlGold = (RelativeLayout) layout.findViewById(R.id.rl_markets_gold);
        mRlHsi = (RelativeLayout) layout.findViewById(R.id.rl_markets_hsi);


        //K线/五档等模块下划线
        mUnderlineKline = layout.findViewById(R.id.view_markets_underline_kline);
        mUnderlineShare = layout.findViewById(R.id.view_markets_underline_share);
        mUnderlineDish = layout.findViewById(R.id.view_markets_underline_dish);
        mUnderlineFive = layout.findViewById(R.id.view_markets_underline_five);
        mDivider = layout.findViewById(R.id.view_markets_divider);

        return layout;
    }

    /**
     * 初始化Rv
     */
    private void initRv(View layout) {
        RecyclerView rvMarkets = (RecyclerView) layout.findViewById(R.id.rv_markets);
        //设置固定大小
        rvMarkets.setHasFixedSize(true);
        //创建线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //方向
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //设置布局管理器
        rvMarkets.setLayoutManager(layoutManager);
        rvMarkets.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        MyRvAdapter rvAdapter = new MyRvAdapter();
        rvMarkets.setAdapter(rvAdapter);
        //TODO rv行点击事件
        rvAdapter.setOnItemClickListener(new MyRvAdapter.OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (fragState) {
                    mLlState.setVisibility(View.GONE);
                    mFlState.setVisibility(View.GONE);
                    mDivider.setVisibility(View.GONE);
                    fragState = false;
                }

                switch (position) {
                    case 0:
                        initDefaultData();
                        break;
                    case 1:
                        mTvName.setText(mGoldTitle);
                        mTvSubName.setText(mGoldSubTitle);
                        mTvPrice.setText(mGoldPrice);
                        mTvApplies.setText(mGoldAppliesPercent);
                        if (mGoldApplies != null) {
                            if (Double.parseDouble(mGoldApplies) >= 0) {
                                mTvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                                mTvApplies.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                            } else {
                                mTvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                mTvApplies.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                            }
                        }
                        break;

                    case 2:

                        mTvName.setText(mHsiTitle);
                        mTvSubName.setText(mHsiSubTitle);
                        mTvPrice.setText(mHsiPrice);
                        mTvApplies.setText(mHsiAppliesPercent);
                        if (mHsiApplies != null) {
                            if (Double.parseDouble(mHsiApplies) >= 0) {
                                mTvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                                mTvApplies.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                            } else {
                                mTvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                mTvApplies.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                            }
                        }

                        break;
                }

            }
        });
    }

    /**
     * Rv适配器
     */
    static class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyViewHolder> {

        public MyRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_markets, parent, false);
            MyViewHolder holder = new MyViewHolder(layout);
            return holder;
        }

        public void onBindViewHolder(final MyRvAdapter.MyViewHolder holder, int position) {
            holder.tv.setText(mRvData.get(position));
            /**
             * 实现rv行点击事件步骤：
             * 4.如果设置了回调，则设置点击事件
             */
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }

        public int getItemCount() {
            return mRvData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv;

            MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.rv_markets_name);
            }
        }

        /**
         * 实现rv行点击事件步骤：
         * 设置接口回调
         * 1.自定义接口OnItemClickListener
         * 2.声明一个接口的变量mOnItemClickListener
         * 3.
         */
        interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        private OnItemClickListener mOnItemClickListener;

        void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_markets_data:
                //判断不为空
                if (!mTvName.getText().toString().trim().isEmpty()) {
                    if (mOilSubTitle != null) {
                        if (mOilSubTitle.equals(mTvSubName.getText().toString())) {
                            EventBus.getDefault().post(new DishEvent(
                                    mOilBuyPrice1,//买价
                                    mOilRiseOrFall,//跌涨额
                                    mOilOpenPrice,//开盘
                                    mOilHighestPrice,//最高
                                    mOilVolume,//成交量
                                    mOilHoldings,//持仓量
                                    mOilSellPrice1,//卖价
                                    mOilAppliesPercent,//涨跌幅
                                    mOilYesterday,//昨结
                                    mOilLowest,//最低
                                    mOilAmplitudePercent//振幅
                            ));
                            EventBus.getDefault().post(new FiveEvent(
                                    mOilSellPrice1, mOilSellQuantity1,
                                    mOilSellPrice2, mOilSellQuantity2,
                                    mOilSellPrice3, mOilSellQuantity3,
                                    mOilSellPrice4, mOilSellQuantity4,
                                    mOilSellPrice5, mOilSellQuantity5,
                                    mOilBuyPrice1, mOilBuyQuantity1,
                                    mOilBuyPrice2, mOilBuyQuantity2,
                                    mOilBuyPrice3, mOilBuyQuantity3,
                                    mOilBuyPrice4, mOilBuyQuantity4,
                                    mOilBuyPrice5, mOilBuyQuantity5
                            ));
                        }
                    }
                    if (mGoldSubTitle != null) {
                        if (mGoldSubTitle.equals(mTvSubName.getText().toString())) {
                            //传数据给盘口
                            EventBus.getDefault().post(new DishEvent(
                                    mGoldBuyPrice1,
                                    mGoldRiseOrFall,
                                    mGoldOpenPrice,
                                    mGoldHighestPrice,
                                    mGoldVolume,
                                    mGoldHoldings,
                                    mGoldSellPrice1,
                                    mGoldAppliesPercent,
                                    mGoldYesterday,
                                    mGoldLowest,
                                    mGoldAmplitudePercent
                            ));
                            EventBus.getDefault().post(new FiveEvent(
                                    mGoldSellPrice1, mGoldSellQuantity1,
                                    mGoldSellPrice2, mGoldSellQuantity2,
                                    mGoldSellPrice3, mGoldSellQuantity3,
                                    mGoldSellPrice4, mGoldSellQuantity4,
                                    mGoldSellPrice5, mGoldSellQuantity5,
                                    mGoldBuyPrice1, mGoldBuyQuantity1,
                                    mGoldBuyPrice2, mGoldBuyQuantity2,
                                    mGoldBuyPrice3, mGoldBuyQuantity3,
                                    mGoldBuyPrice4, mGoldBuyQuantity4,
                                    mGoldBuyPrice5, mGoldBuyQuantity5
                            ));
                        }
                    }
                    if (mHsiSubTitle != null) {
                        if (mHsiSubTitle.equals(mTvSubName.getText().toString())) {
                            //传数据给盘口
                            EventBus.getDefault().post(new DishEvent(
                                    mHsiBuyPrice1,
                                    mHsiRiseOrFall,
                                    mHsiOpenPrice,
                                    mHsiHighestPrice,
                                    mHsiVolume,
                                    mHsiHoldings,
                                    mHsiSellPrice1,
                                    mHsiAppliesPercent,
                                    mHsiYesterday,
                                    mHsiLowest,
                                    mHsiAmplitudePercent
                            ));
                            EventBus.getDefault().post(new FiveEvent(
                                    mHsiSellPrice1, mHsiSellQuantity1,
                                    mHsiSellPrice2, mHsiSellQuantity2,
                                    mHsiSellPrice3, mHsiSellQuantity3,
                                    mHsiSellPrice4, mHsiSellQuantity4,
                                    mHsiSellPrice5, mHsiSellQuantity5,
                                    mHsiBuyPrice1, mHsiBuyQuantity1,
                                    mHsiBuyPrice2, mHsiBuyQuantity2,
                                    mHsiBuyPrice3, mHsiBuyQuantity3,
                                    mHsiBuyPrice4, mHsiBuyQuantity4,
                                    mHsiBuyPrice5, mHsiBuyQuantity5
                            ));
                        }
                    }
                }
                if (fragState) {
                    mLlState.setVisibility(View.GONE);
                    mFlState.setVisibility(View.GONE);
                    mDivider.setVisibility(View.GONE);
                    fragState = false;
                } else {
                    mLlState.setVisibility(View.VISIBLE);
                    mFlState.setVisibility(View.VISIBLE);
                    mDivider.setVisibility(View.VISIBLE);
                    setChoiceItem(0);
                    fragState = true;
                }
                break;

            case R.id.tv_markets_kline:
                setChoiceItem(0);
                break;
            case R.id.tv_markets_share:
                setChoiceItem(1);
                break;
            case R.id.tv_markets_dish:
                setChoiceItem(2);
                break;
            case R.id.tv_markets_five:
                setChoiceItem(3);
                break;
        }
    }

    /**
     * 判断当前显示是哪个模块
     */
    public void setChoiceItem(int index) {
        FragmentTransaction ft = fm.beginTransaction();
        setAttribute(ft);

        switch (index) {
            case 0://当前选中为: K线
                choiceKline(ft);
                break;
            case 1://当前选中为: 分时
                choiceShare(ft);
                break;
            case 2://当前选中为: 盘口
                choiceDish(ft);
                break;
            case 3://当前选中为: 五档
                choiceFive(ft);
                break;
        }
        ft.commit();
    }

    /**
     * 五档
     */
    private void choiceFive(FragmentTransaction ft) {

        //设置背景:黑色
        mTvFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
        //显示下划线
        mUnderlineFive.setVisibility(View.VISIBLE);
        //如果Fragment为空，则创建一个并添加到界面上
        if (mFragFive == null) {
            mFragFive = new FiveFragment();
            String nameContent = mTvSubName.getText().toString().trim();
            //判断不为空
            if (!nameContent.isEmpty()) {
                if (mOilSubTitle != null) {
                    if (mOilSubTitle.equals(nameContent)) {
                        sellPrice1 = mOilSellPrice1;
                        sellPrice2 = mOilSellPrice2;
                        sellPrice3 = mOilSellPrice3;
                        sellPrice4 = mOilSellPrice4;
                        sellPrice5 = mOilSellPrice5;
                        sellQuantity1 = mOilSellQuantity1;
                        sellQuantity2 = mOilSellQuantity2;
                        sellQuantity3 = mOilSellQuantity3;
                        sellQuantity4 = mOilSellQuantity4;
                        sellQuantity5 = mOilSellQuantity5;
                        buyPrice1 = mOilBuyPrice1;
                        buyPrice2 = mOilBuyPrice2;
                        buyPrice3 = mOilBuyPrice3;
                        buyPrice4 = mOilBuyPrice4;
                        buyPrice5 = mOilBuyPrice5;
                        buyQuantity1 = mOilBuyQuantity1;
                        buyQuantity2 = mOilBuyQuantity2;
                        buyQuantity3 = mOilBuyQuantity3;
                        buyQuantity4 = mOilBuyQuantity4;
                        buyQuantity5 = mOilBuyQuantity5;
                        commodityNo = mOilCommodityNo;
                        number = mOilNumber;
                    }
                }
                if (mGoldSubTitle != null) {
                    if (mGoldSubTitle.equals(nameContent)) {
                        sellPrice1 = mGoldSellPrice1;
                        sellPrice2 = mGoldSellPrice2;
                        sellPrice3 = mGoldSellPrice3;
                        sellPrice4 = mGoldSellPrice4;
                        sellPrice5 = mGoldSellPrice5;
                        sellQuantity1 = mGoldSellQuantity1;
                        sellQuantity2 = mGoldSellQuantity2;
                        sellQuantity3 = mGoldSellQuantity3;
                        sellQuantity4 = mGoldSellQuantity4;
                        sellQuantity5 = mGoldSellQuantity5;
                        buyPrice1 = mGoldBuyPrice1;
                        buyPrice2 = mGoldBuyPrice2;
                        buyPrice3 = mGoldBuyPrice3;
                        buyPrice4 = mGoldBuyPrice4;
                        buyPrice5 = mGoldBuyPrice5;
                        buyQuantity1 = mGoldBuyQuantity1;
                        buyQuantity2 = mGoldBuyQuantity2;
                        buyQuantity3 = mGoldBuyQuantity3;
                        buyQuantity4 = mGoldBuyQuantity4;
                        buyQuantity5 = mGoldBuyQuantity5;
                        commodityNo = mGoldCommodityNo;
                        number = mGoldNumber;
                    }
                }
                if (mHsiSubTitle != null) {
                    if (mHsiSubTitle.equals(nameContent)) {
                        sellPrice1 = mHsiSellPrice1;
                        sellPrice2 = mHsiSellPrice2;
                        sellPrice3 = mHsiSellPrice3;
                        sellPrice4 = mHsiSellPrice4;
                        sellPrice5 = mHsiSellPrice5;
                        sellQuantity1 = mHsiSellQuantity1;
                        sellQuantity2 = mHsiSellQuantity2;
                        sellQuantity3 = mHsiSellQuantity3;
                        sellQuantity4 = mHsiSellQuantity4;
                        sellQuantity5 = mHsiSellQuantity5;
                        buyPrice1 = mHsiBuyPrice1;
                        buyPrice2 = mHsiBuyPrice2;
                        buyPrice3 = mHsiBuyPrice3;
                        buyPrice4 = mHsiBuyPrice4;
                        buyPrice5 = mHsiBuyPrice5;
                        buyQuantity1 = mHsiBuyQuantity1;
                        buyQuantity2 = mHsiBuyQuantity2;
                        buyQuantity3 = mHsiBuyQuantity3;
                        buyQuantity4 = mHsiBuyQuantity4;
                        buyQuantity5 = mHsiBuyQuantity5;
                        commodityNo = mHsiCommodityNo;
                        number = mHsiNumber;
                    }
                }
            }
            bundle.putString("SELL_PRICE1", sellPrice1);
            bundle.putString("SELL_PRICE2", sellPrice2);
            bundle.putString("SELL_PRICE3", sellPrice3);
            bundle.putString("SELL_PRICE4", sellPrice4);
            bundle.putString("SELL_PRICE5", sellPrice5);

            bundle.putString("SELL_QUANTITY1", sellQuantity1);
            bundle.putString("SELL_QUANTITY2", sellQuantity2);
            bundle.putString("SELL_QUANTITY3", sellQuantity3);
            bundle.putString("SELL_QUANTITY4", sellQuantity4);
            bundle.putString("SELL_QUANTITY5", sellQuantity5);

            bundle.putString("BUY_PRICE1", buyPrice1);
            bundle.putString("BUY_PRICE2", buyPrice2);
            bundle.putString("BUY_PRICE3", buyPrice3);
            bundle.putString("BUY_PRICE4", buyPrice4);
            bundle.putString("BUY_PRICE5", buyPrice5);

            bundle.putString("BUY_QUANTITY1", buyQuantity1);
            bundle.putString("BUY_QUANTITY2", buyQuantity2);
            bundle.putString("BUY_QUANTITY3", buyQuantity3);
            bundle.putString("BUY_QUANTITY4", buyQuantity4);
            bundle.putString("BUY_QUANTITY5", buyQuantity5);
            bundle.putString("COMMODITY_NO", commodityNo);
            bundle.putString("NUMBER", number);
            bundle.putString("USER_ID", mUserId);

            mFragFive.setArguments(bundle);
            ft.add(R.id.fl_markets, mFragFive);
        } else {
            //否则就显示出来
            ft.show(mFragFive);
        }
    }

    /**
     * 盘口
     */
    private void choiceDish(FragmentTransaction ft) {

        //设置背景:黑色
        mTvDish.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
        //显示下划线
        mUnderlineDish.setVisibility(View.VISIBLE);

        //如果Fragment为空，则创建一个并添加到界面上
        if (mFragDish == null) {
            mFragDish = new DishFragment();
            String nameContent = mTvSubName.getText().toString().trim();
            //判断不为空
            if (!nameContent.isEmpty()) {
                if (mOilSubTitle != null) {
                    if (mOilSubTitle.equals(nameContent)) {
                        buyPrice = mOilBuyPrice1;
                        riseOrFall = mOilRiseOrFall;
                        openPrice = mOilOpenPrice;
                        highest = mOilHighestPrice;
                        volume = mOilVolume;
                        holdings = mOilHoldings;
                        sellPrice = mOilSellPrice1;
                        appliesPercent = mOilAppliesPercent;
                        yesterday = mOilYesterday;
                        lowest = mOilLowest;
                        amplitudePercent = mOilAmplitudePercent;
                        commodityNo = mOilCommodityNo;
                        number = mOilNumber;
                    }
                }
                if (mGoldSubTitle != null) {
                    if (mGoldSubTitle.equals(nameContent)) {
                        buyPrice = mGoldBuyPrice1;
                        riseOrFall = mGoldRiseOrFall;
                        openPrice = mGoldOpenPrice;
                        highest = mGoldHighestPrice;
                        volume = mGoldVolume;
                        holdings = mGoldHoldings;
                        sellPrice = mGoldSellPrice1;
                        appliesPercent = mGoldAppliesPercent;
                        yesterday = mGoldYesterday;
                        lowest = mGoldLowest;
                        amplitudePercent = mGoldAmplitudePercent;
                        commodityNo = mGoldCommodityNo;
                        number = mGoldNumber;
                    }
                }
                if (mHsiSubTitle != null) {
                    if (mHsiSubTitle.equals(nameContent)) {
                        buyPrice = mHsiBuyPrice1;
                        riseOrFall = mHsiRiseOrFall;
                        openPrice = mHsiOpenPrice;
                        highest = mHsiHighestPrice;
                        volume = mHsiVolume;
                        holdings = mHsiHoldings;
                        sellPrice = mHsiSellPrice1;
                        appliesPercent = mHsiAppliesPercent;
                        yesterday = mHsiYesterday;
                        lowest = mHsiLowest;
                        amplitudePercent = mHsiAmplitudePercent;
                        commodityNo = mHsiCommodityNo;
                        number = mHsiNumber;
                    }
                }
            }
            bundle.putString("BUY_PRICE", buyPrice);
            bundle.putString("RISE_OR_FALL", riseOrFall);
            bundle.putString("OPEN_PRICE", openPrice);
            bundle.putString("HIGHEST_PRICE", highest);
            bundle.putString("VOLUME", volume);
            bundle.putString("HOLDINGS", holdings);
            bundle.putString("SELL_PRICE", sellPrice);
            bundle.putString("APPLIES_PERCENT", appliesPercent);
            bundle.putString("YESTERDAY", yesterday);
            bundle.putString("LOWEST", lowest);
            bundle.putString("AMPLITUDE_PERCENT", amplitudePercent);
            bundle.putString("COMMODITY_NO", commodityNo);
            bundle.putString("NUMBER", number);
            bundle.putString("USER_ID", mUserId);
            Log.e("BUY_PRICE", commodityNo + "1");
            mFragDish.setArguments(bundle);
            ft.add(R.id.fl_markets, mFragDish);
        } else {
            //否则就显示出来
            ft.show(mFragDish);
        }

    }

    /**
     * 分时
     */
    private void choiceShare(FragmentTransaction ft) {
        //设置背景:黑色
        mTvShare.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
        //显示下划线
        mUnderlineShare.setVisibility(View.VISIBLE);
        //如果Fragment为空，则创建一个并添加到界面上
        if (mFragShare == null) {
            mFragShare = new TimeShareFragment();
            String nameContent = mTvSubName.getText().toString().trim();
            //判断不为空
            if (!nameContent.isEmpty()) {
                if (mOilSubTitle != null) {
                    if (mOilSubTitle.equals(nameContent)) {
                        commodityNo = mOilCommodityNo;
                        number = mOilNumber;
                        buyPrice = mOilBuyPrice1;
                        sellPrice = mOilSellPrice1;
                    }
                }
                if (mGoldSubTitle != null) {
                    if (mGoldSubTitle.equals(nameContent)) {
                        commodityNo = mGoldCommodityNo;
                        number = mGoldNumber;
                        buyPrice = mGoldBuyPrice1;
                        sellPrice = mGoldSellPrice1;
                    }
                }
                if (mHsiSubTitle != null) {
                    if (mHsiSubTitle.equals(nameContent)) {
                        commodityNo = mHsiCommodityNo;
                        number = mHsiNumber;
                        buyPrice = mHsiBuyPrice1;
                        sellPrice = mHsiSellPrice1;
                    }
                }
            }
            bundle.putString("COMMODITY_NO", commodityNo);
            bundle.putString("NUMBER", number);
            bundle.putString("BUY_PRICE", buyPrice);
            bundle.putString("SELL_PRICE", sellPrice);
            bundle.putString("USER_ID", mUserId);
            mFragShare.setArguments(bundle);
            ft.add(R.id.fl_markets, mFragShare);
        } else {
            //否则就显示出来
            ft.show(mFragShare);
        }
    }

    /**
     * K线
     */
    private void choiceKline(FragmentTransaction ft) {
        //设置背景:黑色
        mTvKline.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
        //显示下划线
        mUnderlineKline.setVisibility(View.VISIBLE);
        //如果Fragment为空，则创建一个并添加到界面上
        if (mFragKline == null) {
            mFragKline = new KLineFragment();
            String nameContent = mTvSubName.getText().toString().trim();
            //判断不为空
            if (!nameContent.isEmpty()) {
                if (mOilSubTitle != null) {
                    if (mOilSubTitle.equals(nameContent)) {
                        commodityNo = mOilCommodityNo;
                        number = mOilNumber;
                        buyPrice = mOilBuyPrice1;
                        sellPrice = mOilSellPrice1;
                    }
                }
                if (mGoldSubTitle != null) {
                    if (mGoldSubTitle.equals(nameContent)) {
                        commodityNo = mGoldCommodityNo;
                        number = mGoldNumber;
                        buyPrice = mGoldBuyPrice1;
                        sellPrice = mGoldSellPrice1;
                    }
                }
                if (mHsiSubTitle != null) {
                    if (mHsiSubTitle.equals(nameContent)) {
                        commodityNo = mHsiCommodityNo;
                        number = mHsiNumber;
                        buyPrice = mHsiBuyPrice1;
                        sellPrice = mHsiSellPrice1;
                    }
                }
            }
            bundle.putString("COMMODITY_NO", commodityNo);
            bundle.putString("NUMBER", number);
            bundle.putString("BUY_PRICE", buyPrice);
            bundle.putString("SELL_PRICE", sellPrice);
            bundle.putString("USER_ID", mUserId);
            mFragKline.setArguments(bundle);
            ft.add(R.id.fl_markets, mFragKline);
        } else {
            //否则就显示出来
            ft.show(mFragKline);
        }
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡还原为默认(字体灰色,背景白色), 隐藏frag和下划线
     */
    private void setAttribute(FragmentTransaction ft) {
        //字体:灰色
        mTvKline.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvShare.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvDish.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvFive.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        //背景:白色
        mTvKline.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvShare.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvDish.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvFive.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        //隐藏下划线
        mUnderlineKline.setVisibility(View.GONE);
        mUnderlineShare.setVisibility(View.GONE);
        mUnderlineDish.setVisibility(View.GONE);
        mUnderlineFive.setVisibility(View.GONE);
        //隐藏Fragment
        if (mFragKline != null) {
            ft.hide(mFragKline);
        }
        if (mFragShare != null) {
            ft.hide(mFragShare);
        }
        if (mFragDish != null) {
            ft.hide(mFragDish);
        }
        if (mFragFive != null) {
            ft.hide(mFragFive);
        }
    }

    //    onResume 返回的时候会再调一次
    public void onResume() {
        Log.e("onResume: ", "onResume");
        super.onResume();
        timerHandler = new TimerHandler();//实例化TimerHandler
        timer = new Timer();//设置定时器Timer
        //delay:0 : 获取数据无延迟
        //period:1000 : 每一秒刷新一次数据
        timer.schedule(new MyTimerTask(), 0, 1000);//1秒刷新一次(0表示无延迟,1000表示隔2000ms)
    }

    //    onStop:关闭程序时调用
    public void onStop() {
        super.onStop();
        Log.e("onStop: ", "onStop");
        //关闭程序时需关闭定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //onPause:跳转到其他页面时,隐藏当前页面时调用
    public void onPause() {
        super.onPause();
        Log.e("onPause: ", "onPause");
        //关闭页面时需关闭定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
