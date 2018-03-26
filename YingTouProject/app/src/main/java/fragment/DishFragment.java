package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testyingtou.yingtouproject.BuyActivity;
import com.testyingtou.yingtouproject.R;
import com.testyingtou.yingtouproject.SellActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import utils.DishEvent;


public class DishFragment extends Fragment implements View.OnClickListener {

    private TextView mTvBuy, mTvRiseOrFall, mTvOpen, mTvHighest, mTvVolume, mTvHoldings, mTvSell, mTvApplies, mTvYesterday, mTvLowest, mTvAmplitude;
    private String commodityNo, number, sellPrice, buyPrice,mUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//订阅
        View layout = inflater.inflate(R.layout.fragment_dish, container, false);

        initUI(layout);
        Bundle bundle = getArguments();
        mUserId = bundle.getString("USER_ID");
        commodityNo = bundle.getString("COMMODITY_NO");
        number = bundle.getString("NUMBER");
        buyPrice = bundle.getString("BUY_PRICE");
        sellPrice = bundle.getString("SELL_PRICE");
        mTvBuy.setText(buyPrice);
        mTvRiseOrFall.setText(bundle.getString("RISE_OR_FALL"));
        mTvOpen.setText(bundle.getString("OPEN_PRICE"));
        mTvHighest.setText(bundle.getString("HIGHEST_PRICE"));
        mTvVolume.setText(bundle.getString("VOLUME"));
        mTvHoldings.setText(bundle.getString("HOLDINGS"));
        mTvSell.setText(sellPrice);
        mTvApplies.setText(bundle.getString("APPLIES_PERCENT"));
        mTvYesterday.setText(bundle.getString("YESTERDAY"));
        mTvLowest.setText(bundle.getString("LOWEST"));
        mTvAmplitude.setText(bundle.getString("AMPLITUDE_PERCENT"));
        return layout;
    }

    private void initUI(View layout) {
        mTvBuy = (TextView) layout.findViewById(R.id.tv_dish_buy);
        mTvRiseOrFall = (TextView) layout.findViewById(R.id.tv_dish_rise_fall);
        mTvOpen = (TextView) layout.findViewById(R.id.tv_dish_open);
        mTvHighest = (TextView) layout.findViewById(R.id.tv_dish_highest);
        mTvVolume = (TextView) layout.findViewById(R.id.tv_dish_volume);
        mTvHoldings = (TextView) layout.findViewById(R.id.tv_dish_holdings);
        mTvSell = (TextView) layout.findViewById(R.id.tv_dish_sell);
        mTvApplies = (TextView) layout.findViewById(R.id.tv_dish_applies);
        mTvYesterday = (TextView) layout.findViewById(R.id.tv_dish_yesterday);
        mTvLowest = (TextView) layout.findViewById(R.id.tv_dish_lowest);
        mTvAmplitude = (TextView) layout.findViewById(R.id.tv_dish_amplitude);
        LinearLayout llBottom = (LinearLayout) layout.findViewById(R.id.bottom);
        llBottom.findViewById(R.id.rl_buy).setOnClickListener(this);
        llBottom.findViewById(R.id.rl_sell).setOnClickListener(this);
    }


    /**
     * ThreadMode总共四个：
     * MAIN       ui主线程
     * BACKGROUND 后台线程
     * POSTING    和发布者处在同一个线程
     * ASYNC      异步线程
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DishEvent dishEvent) {
        mTvBuy.setText(dishEvent.getBuy());
        mTvRiseOrFall.setText(dishEvent.getRiseOrFall());
        mTvOpen.setText(dishEvent.getOpen());
        mTvHighest.setText(dishEvent.getHighest());
        mTvVolume.setText(dishEvent.getVolume());
        mTvHoldings.setText(dishEvent.getHoldings());
        mTvSell.setText(dishEvent.getSell());
        mTvApplies.setText(dishEvent.getApplies());
        mTvYesterday.setText(dishEvent.getYesterday());
        mTvLowest.setText(dishEvent.getLowest());
        mTvAmplitude.setText(dishEvent.getAmplitude());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_buy:
                Intent intent = new Intent(getActivity(), BuyActivity.class);
                intent.putExtra("COMMODITY_NO", commodityNo);
                intent.putExtra("NUMBER", number);
                intent.putExtra("SELL_PRICE", sellPrice);
                intent.putExtra("BUY_PRICE", buyPrice);
                intent.putExtra("USER_ID", mUserId);
                startActivity(intent);
                break;
            case R.id.rl_sell:
                Intent intent1 = new Intent(getActivity(), SellActivity.class);
                intent1.putExtra("COMMODITY_NO", commodityNo);
                intent1.putExtra("NUMBER", number);
                intent1.putExtra("SELL_PRICE", sellPrice);
                intent1.putExtra("BUY_PRICE", buyPrice);
                intent1.putExtra("USER_ID", mUserId);
                startActivity(intent1);
                break;

        }
    }
}
