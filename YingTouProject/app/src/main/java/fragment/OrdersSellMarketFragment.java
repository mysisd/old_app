package fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.testyingtou.yingtouproject.R;

public class OrdersSellMarketFragment extends Fragment implements View.OnClickListener {


    private EditText mEtNum;
    private String mMaxNum;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_orders_sell_market, container, false);

        mEtNum = (EditText) layout.findViewById(R.id.et_order_sell_market);
        layout.findViewById(R.id.iv_order_sell_market_subtraction).setOnClickListener(this);
        layout.findViewById(R.id.iv_order_sell_market_add).setOnClickListener(this);

        Bundle bundle = getArguments();
        mMaxNum = bundle.getString("MAX_NUM");
        Log.e("mMaxNum", mMaxNum);

        mEtNum.addTextChangedListener(EtChangedListener());
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * et输入内容监听
     */
    private TextWatcher EtChangedListener() {
        return new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                try {//Double.parseDouble()不能为空
                    String content = mEtNum.getText().toString().trim();
                    if (Integer.valueOf(content) > Integer.parseInt(mMaxNum)) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("最多只能选择" + Integer.parseInt(mMaxNum) + "手")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mEtNum.setText(String.valueOf(Integer.parseInt(mMaxNum)));
                                    }
                                })
                                .show();
                    }
                } catch (NumberFormatException e) {

                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_order_sell_market_add:
                if (mEtNum.getText().toString().equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("手数不能为空")
                            .setPositiveButton("确定", null)
                            .show();
                }
                try {
                    if (Integer.valueOf(mEtNum.getText().toString()) + 1 > Integer.parseInt(mMaxNum)) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("最多只能选择" + mMaxNum + "手")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mEtNum.setText(mMaxNum);
                                    }
                                })
                                .show();
                    } else {
                        String content = mEtNum.getText().toString();
                        mEtNum.setText(String.valueOf(Integer.valueOf(content) + 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_order_sell_market_subtraction:
                if (mEtNum.getText().toString().equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("手数不能为空")
                            .setPositiveButton("确定", null)
                            .show();
                }
                try {
                    if (Integer.valueOf(mEtNum.getText().toString()) <= 1) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("最少需选择1手")
                                .setPositiveButton("确定", null)
                                .show();
                    } else {
                        String content = mEtNum.getText().toString();
                        mEtNum.setText(String.valueOf(Integer.valueOf(content) - 1));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
