package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import utils.FiveEvent;

public class FiveFragment extends Fragment implements View.OnClickListener {


    private TextView mTvSellPrice1, mTvSellPrice2, mTvSellPrice3, mTvSellPrice4, mTvSellPrice5,//卖价1.2.3.4.5
            mTvSellQuantity1, mTvSellQuantity2, mTvSellQuantity3, mTvSellQuantity4, mTvSellQuantity5,//卖量1.2.3.4.5
            mTvBuyPrice1, mTvBuyPrice2, mTvBuyPrice3, mTvBuyPrice4, mTvBuyPrice5,//买价1.2.3.4.5
            mTvBuyQuantity1, mTvBuyQuantity2, mTvBuyQuantity3, mTvBuyQuantity4, mTvBuyQuantity5;//,买量1.2.3.4.5
    private String commodityNo, number,  sellPrice, buyPrice,mUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_five, container, false);
        EventBus.getDefault().register(this);//注册

        initUI(layout);
        Bundle bundle = getArguments();
        commodityNo = bundle.getString("COMMODITY_NO");
        number = bundle.getString("NUMBER");
        sellPrice = bundle.getString("SELL_PRICE1");
        buyPrice = bundle.getString("BUY_PRICE1");
        mUserId = bundle.getString("USER_ID");
        mTvSellPrice1.setText(sellPrice);
        mTvSellPrice2.setText(bundle.getString("SELL_PRICE2"));
        mTvSellPrice3.setText(bundle.getString("SELL_PRICE3"));
        mTvSellPrice4.setText(bundle.getString("SELL_PRICE4"));
        mTvSellPrice5.setText(bundle.getString("SELL_PRICE5"));
        mTvSellQuantity1.setText(bundle.getString("SELL_QUANTITY1"));
        mTvSellQuantity2.setText(bundle.getString("SELL_QUANTITY2"));
        mTvSellQuantity3.setText(bundle.getString("SELL_QUANTITY3"));
        mTvSellQuantity4.setText(bundle.getString("SELL_QUANTITY4"));
        mTvSellQuantity5.setText(bundle.getString("SELL_QUANTITY5"));

        mTvBuyPrice1.setText(buyPrice);
        mTvBuyPrice2.setText(bundle.getString("BUY_PRICE2"));
        mTvBuyPrice3.setText(bundle.getString("BUY_PRICE3"));
        mTvBuyPrice4.setText(bundle.getString("BUY_PRICE4"));
        mTvBuyPrice5.setText(bundle.getString("BUY_PRICE5"));
        mTvBuyQuantity1.setText(bundle.getString("BUY_QUANTITY1"));
        mTvBuyQuantity2.setText(bundle.getString("BUY_QUANTITY2"));
        mTvBuyQuantity3.setText(bundle.getString("BUY_QUANTITY3"));
        mTvBuyQuantity4.setText(bundle.getString("BUY_QUANTITY4"));
        mTvBuyQuantity5.setText(bundle.getString("BUY_QUANTITY5"));

        return layout;
    }

    private void initUI(View layout) {
        mTvSellPrice1 = (TextView) layout.findViewById(R.id.tv_five_sell1);
        mTvSellPrice2 = (TextView) layout.findViewById(R.id.tv_five_sell2);
        mTvSellPrice3 = (TextView) layout.findViewById(R.id.tv_five_sell3);
        mTvSellPrice4 = (TextView) layout.findViewById(R.id.tv_five_sell4);
        mTvSellPrice5 = (TextView) layout.findViewById(R.id.tv_five_sell5);
        mTvSellQuantity1 = (TextView) layout.findViewById(R.id.tv_five_sell_quantity1);
        mTvSellQuantity2 = (TextView) layout.findViewById(R.id.tv_five_sell_quantity2);
        mTvSellQuantity3 = (TextView) layout.findViewById(R.id.tv_five_sell_quantity3);
        mTvSellQuantity4 = (TextView) layout.findViewById(R.id.tv_five_sell_quantity4);
        mTvSellQuantity5 = (TextView) layout.findViewById(R.id.tv_five_sell_quantity5);
        mTvBuyPrice1 = (TextView) layout.findViewById(R.id.tv_five_buy1);
        mTvBuyPrice2 = (TextView) layout.findViewById(R.id.tv_five_buy2);
        mTvBuyPrice3 = (TextView) layout.findViewById(R.id.tv_five_buy3);
        mTvBuyPrice4 = (TextView) layout.findViewById(R.id.tv_five_buy4);
        mTvBuyPrice5 = (TextView) layout.findViewById(R.id.tv_five_buy5);
        mTvBuyQuantity1 = (TextView) layout.findViewById(R.id.tv_five_buy_quantity1);
        mTvBuyQuantity2 = (TextView) layout.findViewById(R.id.tv_five_buy_quantity2);
        mTvBuyQuantity3 = (TextView) layout.findViewById(R.id.tv_five_buy_quantity3);
        mTvBuyQuantity4 = (TextView) layout.findViewById(R.id.tv_five_buy_quantity4);
        mTvBuyQuantity5 = (TextView) layout.findViewById(R.id.tv_five_buy_quantity5);
        LinearLayout llBottom = (LinearLayout) layout.findViewById(R.id.bottom);
        llBottom.findViewById(R.id.rl_buy).setOnClickListener(this);
        llBottom.findViewById(R.id.rl_sell).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FiveEvent fiveEvent) {
        mTvSellPrice1.setText(fiveEvent.getSell1());
        mTvSellPrice2.setText(fiveEvent.getSell2());
        mTvSellPrice3.setText(fiveEvent.getSell3());
        mTvSellPrice4.setText(fiveEvent.getSell4());
        mTvSellPrice5.setText(fiveEvent.getSell5());
        mTvSellQuantity1.setText(fiveEvent.getSellQuantity1());
        mTvSellQuantity2.setText(fiveEvent.getSellQuantity2());
        mTvSellQuantity3.setText(fiveEvent.getSellQuantity3());
        mTvSellQuantity4.setText(fiveEvent.getSellQuantity4());
        mTvSellQuantity5.setText(fiveEvent.getSellQuantity5());
        mTvBuyPrice1.setText(fiveEvent.getBuy1());
        mTvBuyPrice2.setText(fiveEvent.getBuy2());
        mTvBuyPrice3.setText(fiveEvent.getBuy3());
        mTvBuyPrice4.setText(fiveEvent.getBuy4());
        mTvBuyPrice5.setText(fiveEvent.getBuy5());
        mTvBuyQuantity1.setText(fiveEvent.getBuyQuantity1());
        mTvBuyQuantity2.setText(fiveEvent.getBuyQuantity2());
        mTvBuyQuantity3.setText(fiveEvent.getBuyQuantity3());
        mTvBuyQuantity4.setText(fiveEvent.getBuyQuantity4());
        mTvBuyQuantity5.setText(fiveEvent.getBuyQuantity5());


    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除注册
    }

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
