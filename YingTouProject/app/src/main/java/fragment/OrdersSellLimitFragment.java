package fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.testyingtou.yingtouproject.R;

import java.text.DecimalFormat;

public class OrdersSellLimitFragment extends Fragment implements View.OnClickListener {

    private EditText mEtPrice, mEtNum;
    private String mMaxNum;
    private int maxNum;
    private DecimalFormat df;//保留小数点后两位(不足两位自动补齐)


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_orders_sell_limit, container, false);

        mEtPrice = (EditText) layout.findViewById(R.id.et_limit_sell_price);
        mEtNum = (EditText) layout.findViewById(R.id.et_limit_sell_num);
        layout.findViewById(R.id.iv_limit_sell_price_add).setOnClickListener(this);
        layout.findViewById(R.id.iv_limit_sell_price_subtraction).setOnClickListener(this);
        layout.findViewById(R.id.iv_limit_sell_num_add).setOnClickListener(this);
        layout.findViewById(R.id.iv_limit_sell_num_subtraction).setOnClickListener(this);


        Bundle bundle = getArguments();
        String price = bundle.getString("PRICE");
        String money = bundle.getString("MONEY");

        maxNum = (int) (Double.parseDouble(money) / Double.parseDouble(price));
        mMaxNum = String.valueOf(maxNum);
        mEtPrice.setText(price);

        mEtNum.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                try {//Double.parseDouble()不能为空
                    String content = mEtNum.getText().toString().trim();
                    if (Integer.valueOf(content) >= maxNum) {
                        if (Integer.valueOf(content) == maxNum) {
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("最多只能选择" + maxNum + "手")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mEtNum.setText(String.valueOf(maxNum));
                                        }
                                    })
                                    .show();
                        }
                    } else {

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        return layout;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_limit_sell_price_add:
                try {
                    String etContent = mEtPrice.getText().toString();
                    if (etContent.equals("")) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("价格不能为空")
                                .setPositiveButton("确定", null)
                                .show();
                    }
                    //保留小数点后两位(不足两位自动补齐)
                    df = new DecimalFormat("######0.00");
                    double number = Double.parseDouble(etContent) + 0.01;

                    mEtPrice.setText(df.format(number));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_limit_buy_price_subtraction:
                if (mEtPrice.getText().toString().equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("价格不能为0")
                            .setPositiveButton("确定", null)
                            .show();
                }
                try {
                    if (Double.parseDouble(mEtPrice.getText().toString()) <= 0) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("最少需选择1手")
                                .setPositiveButton("确定", null)
                                .show();
                    } else {
                        double number = Double.parseDouble(mEtPrice.getText().toString()) - 0.01;
                        mEtPrice.setText(df.format(number));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_limit_sell_num_add:

                if (mEtNum.getText().toString().equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("手数不能为空")
                            .setPositiveButton("确定", null)
                            .show();
                }
                try {
                    if (Integer.valueOf(mEtNum.getText().toString()) + 1 > maxNum) {
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
                        mEtNum.setText(String.valueOf(Integer.valueOf(mEtNum.getText().toString()) + 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_limit_sell_num_subtraction:
                if (mEtNum.getText().toString().equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("手数不能为空")
                            .setPositiveButton("确定", null)
                            .show();
                }
                try {//Integer.valueOf()防止为空
                    if (Integer.valueOf(mEtNum.getText().toString()) <= 1) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("最少需选择1手")
                                .setPositiveButton("确定", null)
                                .show();
                    } else {
                        mEtNum.setText(String.valueOf(Integer.valueOf(mEtNum.getText().toString()) - 1));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
