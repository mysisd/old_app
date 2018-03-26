package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testyingtou.yingtouproject.R;

public class TransactFragment extends Fragment implements View.OnClickListener {

    private TextView mTvOpenPosition, mTvMoney, mTvEntrust, mTvDeal;
    private View mUnderlinePosition, mUnderlineMoney, mUnderlineEntrust, mUnderlineDeal;
    private OpenPositionFragment mFragOpenPosition;
    private MoneyTransactFragment mFragMoney;
    private EntrustFragment mFragEntrust;
    private DealFragment mFragDeal;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_transact, container, false);
        initUI(layout);
        setChoiceItem(0);
        return layout;
    }

    private void initUI(View layout) {
        mTvOpenPosition = (TextView) layout.findViewById(R.id.tv_transact_open_position);
        mTvMoney = (TextView) layout.findViewById(R.id.tv_transact_money);
        mTvEntrust = (TextView) layout.findViewById(R.id.tv_transact_entrust);
        mTvDeal = (TextView) layout.findViewById(R.id.tv_transact_deal);

        mUnderlinePosition = layout.findViewById(R.id.view_transact_underline_open_position);
        mUnderlineMoney = layout.findViewById(R.id.view_transact_underline_money);
        mUnderlineEntrust = layout.findViewById(R.id.view_transact_underline_entrust);
        mUnderlineDeal = layout.findViewById(R.id.view_transact_underline_deal);

        mTvOpenPosition.setOnClickListener(this);
        mTvMoney.setOnClickListener(this);
        mTvEntrust.setOnClickListener(this);
        mTvDeal.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_transact_open_position:
                setChoiceItem(0);
                break;
            case R.id.tv_transact_money:
                setChoiceItem(1);
                break;
            case R.id.tv_transact_entrust:
                setChoiceItem(2);
                break;
            case R.id.tv_transact_deal:
                setChoiceItem(3);
                break;
        }
    }

    private void setChoiceItem(int index) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        setAttribute(ft);
        switch (index) {
            case 0://当前选中为: 持仓
                //设置背景:黑色
                mTvOpenPosition.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                //显示下划线
                mUnderlinePosition.setVisibility(View.VISIBLE);
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragOpenPosition == null) {
                    mFragOpenPosition = new OpenPositionFragment();
                    ft.add(R.id.frag_transact, mFragOpenPosition);
                } else {
                    //否则就显示出来
                    ft.show(mFragOpenPosition);
                }
                break;
            case 1://当前选中为: 账户资金
                mTvMoney.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                //显示下划线
                mUnderlineMoney.setVisibility(View.VISIBLE);
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragMoney == null) {
                    mFragMoney = new MoneyTransactFragment();
                    ft.add(R.id.frag_transact, mFragMoney);
                } else {
                    //否则就显示出来
                    ft.show(mFragMoney);
                }
                break;
            case 2://当前选中为: 委托
                mTvEntrust.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                //显示下划线
                mUnderlineEntrust.setVisibility(View.VISIBLE);
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragEntrust == null) {
                    mFragEntrust = new EntrustFragment();
                    ft.add(R.id.frag_transact, mFragEntrust);
                } else {
                    //否则就显示出来
                    ft.show(mFragEntrust);
                }
                break;
            case 3://当前选中为: 成交
                mTvDeal.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                //显示下划线
                mUnderlineDeal.setVisibility(View.VISIBLE);
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragDeal == null) {
                    mFragDeal = new DealFragment();
                    ft.add(R.id.frag_transact, mFragDeal);
                } else {
                    //否则就显示出来
                    ft.show(mFragDeal);
                }
                break;
        }
        ft.commit();
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡还原为默认(字体灰色,背景白色), 隐藏frag和下划线
     */
    private void setAttribute(FragmentTransaction ft) {
        //字体:灰色
        mTvOpenPosition.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvMoney.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvEntrust.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvDeal.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        //背景:白色
        mTvOpenPosition.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvMoney.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvEntrust.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvDeal.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        //隐藏下划线
        mUnderlinePosition.setVisibility(View.GONE);
        mUnderlineMoney.setVisibility(View.GONE);
        mUnderlineEntrust.setVisibility(View.GONE);
        mUnderlineDeal.setVisibility(View.GONE);
        //隐藏Fragment
        if (mFragOpenPosition != null) {
            ft.hide(mFragOpenPosition);
        }
        if (mFragMoney != null) {
            ft.hide(mFragMoney);
        }
        if (mFragEntrust != null) {
            ft.hide(mFragEntrust);
        }
        if (mFragDeal != null) {
            ft.hide(mFragDeal);
        }
    }


}
