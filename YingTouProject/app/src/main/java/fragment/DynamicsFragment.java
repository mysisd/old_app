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

public class DynamicsFragment extends Fragment implements View.OnClickListener{
    private FragmentManager fm;
    private TextView mTvAdvertising, mTvConsulting, mTvInformation;
    private DynamicsAdvertisingFragment mFragAdvertising;
    private DynamicsConsultingFragment mFragConsulting;
    private DynamicsInformationFragment mFragInformation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = initUI(inflater, container);
        setChoiceItem(0);
        return layout;
    }

    @NonNull
    private View initUI(LayoutInflater inflater, ViewGroup container) {
        View layout = inflater.inflate(R.layout.fragment_dynamics, container, false);
        fm = getFragmentManager();
        mTvAdvertising = (TextView) layout.findViewById(R.id.tv_dynamics_advertising);
        mTvConsulting = (TextView) layout.findViewById(R.id.tv_dynamics_consulting);
        mTvInformation = (TextView) layout.findViewById(R.id.tv_dynamics_information);
        mTvAdvertising.setOnClickListener(this);
        mTvConsulting.setOnClickListener(this);
        mTvInformation.setOnClickListener(this);
        return layout;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dynamics_advertising:
                setChoiceItem(0);
                break;
            case R.id.tv_dynamics_consulting:
                setChoiceItem(1);
                break;
            case R.id.tv_dynamics_information:
                setChoiceItem(2);
                break;
        }
    }
    public void setChoiceItem(int choiceItem) {
        FragmentTransaction ft = fm.beginTransaction();
        setAttribute(ft);
        switch (choiceItem) {
            case 0:
                mTvAdvertising.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                mTvAdvertising.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragAdvertising == null) {
                    mFragAdvertising = new DynamicsAdvertisingFragment();
                    ft.add(R.id.frag_dynamics, mFragAdvertising);
                } else {
                    //否则就显示出来
                    ft.show(mFragAdvertising);
                }
                break;
            case 1:
                mTvConsulting.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                mTvConsulting.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragConsulting == null) {
                    mFragConsulting = new DynamicsConsultingFragment();
                    ft.add(R.id.frag_dynamics, mFragConsulting);
                } else {
                    //否则就显示出来
                    ft.show(mFragConsulting);
                }
                break;
            case 2:
                mTvInformation.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
                mTvInformation.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));
                //如果Fragment为空，则创建一个并添加到界面上
                if (mFragInformation == null) {
                    mFragInformation = new DynamicsInformationFragment();
                    ft.add(R.id.frag_dynamics, mFragInformation);
                } else {
                    //否则就显示出来
                    ft.show(mFragInformation);
                }
                break;
        }
        ft.commit();
    }

    /**
     * 点击时候设置各个tv的字体颜色、背景颜色及frag的隐藏
     */
    private void setAttribute(FragmentTransaction ft) {
        mTvAdvertising.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvAdvertising.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvConsulting.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvConsulting.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        mTvInformation.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorCommon));
        mTvInformation.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        if (mFragAdvertising != null) {
            ft.hide(mFragAdvertising);
        }
        if (mFragConsulting != null) {
            ft.hide(mFragConsulting);
        }
        if (mFragInformation != null) {
            ft.hide(mFragInformation);
        }
    }
}
