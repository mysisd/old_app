package fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testyingtou.yingtouproject.R;

public class MarketFragment extends Fragment implements View.OnClickListener {
    private TextView mTvChoice;
    private TextView mTvMarkets;
    private MarketChoiceFragment mFragChoice;
    private MarketMarketFragment mFragMarkets;
    FragmentManager fm;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = initUI(inflater, container);
        setChoiceItem(0);
        return layout;
    }

    @NonNull
    private View initUI(LayoutInflater inflater, ViewGroup container) {
        View layout = inflater.inflate(R.layout.fragment_market, container, false);
        fm = getFragmentManager();
        mTvChoice = (TextView) layout.findViewById(R.id.tv_market_choice);
        mTvMarkets = (TextView) layout.findViewById(R.id.tv_market_markets);
        mTvChoice.setOnClickListener(this);
        mTvMarkets.setOnClickListener(this);
        return layout;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_market_choice:
                setChoiceItem(0);
                break;
            case R.id.tv_market_markets:
                setChoiceItem(1);
                break;
        }
    }

    public void setChoiceItem(int index) {
        FragmentTransaction ft = fm.beginTransaction();
        setAttribute(ft);
        switch (index) {
            case 0:
                mTvChoice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                mTvChoice.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragChoice == null) {
                    mFragChoice = new MarketChoiceFragment();
                    ft.add(R.id.frag_market, mFragChoice);
                } else {
                    //否则就显示出来
                    ft.show(mFragChoice);
                }
                break;
            case 1:
                mTvMarkets.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                mTvMarkets.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));
                if (mFragMarkets == null) {
                    mFragMarkets = new MarketMarketFragment();
                    ft.add(R.id.frag_market, mFragMarkets);
                } else {
                    ft.show(mFragMarkets);
                }
                break;
        }
        ft.commit();
    }

    /**
     * 点击时候设置各个tv的字体颜色、背景颜色及frag的隐藏
     */
    private void setAttribute(FragmentTransaction ft) {
        mTvChoice.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvChoice.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvMarkets.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvMarkets.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        if (mFragChoice != null) {
            ft.hide(mFragChoice);
        }
        if (mFragMarkets != null) {
            ft.hide(mFragMarkets);
        }
    }
}
