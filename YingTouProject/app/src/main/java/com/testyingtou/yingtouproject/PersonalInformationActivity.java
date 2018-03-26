package com.testyingtou.yingtouproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import utils.GetJsonDataUtil;
import utils.JsonBean;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private int sexIndex;
    private String[] sexItem = new String[]{"男", "女"};
    private TextView mtvSex;
    private SexOnClick sexOnClick = new SexOnClick();
    private ArrayList<JsonBean> mProvinceItem = new ArrayList<>();
    private ArrayList<ArrayList<String>> mCityItem = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> mAreaItem = new ArrayList<>();
    private TextView mTvAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        findViewById(R.id.rl_information_sex).setOnClickListener(this);
        findViewById(R.id.iv_information_back).setOnClickListener(this);
        findViewById(R.id.rl_information_address).setOnClickListener(this);
        findViewById(R.id.rl_information_pwd).setOnClickListener(this);
        mtvSex = (TextView) findViewById(R.id.tv_information_sex);
        mTvAddress = (TextView) findViewById(R.id.tv_information_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_information_sex:
                showDialog(sexItem, sexIndex, sexOnClick);
                break;
            case R.id.rl_information_address:
                initJsonData();
                ShowPickerView();
                break;
            case R.id.iv_information_back:
                finish();
                break;
            case R.id.rl_information_pwd:
                startActivity(new Intent(this,ModifyLoginPwdActivity.class));
                break;
        }
    }
    /**
     * 解析数据
     */
    private void initJsonData() {
        //获取assets目录下的json文件数据
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");
        //用gson 转成实体
        ArrayList<JsonBean> jsonBeen = parseData(JsonData);
        mProvinceItem = jsonBeen;
        //遍历省份
        for (int i = 0; i < jsonBeen.size(); i++) {
            //该省的城市列表（第二级）
            ArrayList<String> cityList = new ArrayList<>();
            //该省的所有地区列表（第三级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();
            //遍历该省份的所有城市
            for (int j = 0; j < jsonBeen.get(i).getCity().size(); j++) {
                String cityName = jsonBeen.get(i).getCity().get(j).getName();
                //添加城市
                cityList.add(cityName);
                //该城市的所有地区列表
                ArrayList<String> cityAreaList = new ArrayList<>();
                List<String> areaList = jsonBeen.get(i).getCity().get(j).getArea();
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (areaList == null || areaList.size() == 0) {
                    cityAreaList.add("");
                } else {
                    for (int k = 0; k < areaList.size(); k++) {
                        String areaName = areaList.get(k);
                        //添加该城市所有地区数据
                        cityAreaList.add(areaName);
                    }
                }
                //添加该省所有地区数据
                province_AreaList.add(cityAreaList);
            }
            //添加城市数据
            mCityItem.add(cityList);
            //添加地区数据
            mAreaItem.add(province_AreaList);
        }
    }

    /**
     * 弹出城市选择器
     */
    private void ShowPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是省市区的选中位置
                String text = mProvinceItem.get(options1).getPickerViewText() +
                        mCityItem.get(options1).get(options2) +
                        mAreaItem.get(options1).get(options2).get(options3);
                //设置tv标题
                mTvAddress.setText(text);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK) //分割线
                .setTextColorCenter(Color.BLACK)//设置选中项文字颜色
                .setContentTextSize(20)//字体
                .build();

        pvOptions.setPicker(mProvinceItem, mCityItem, mAreaItem);//省市区三级选择器
//        pvOptions.setPicker(mProvinceItem, mCityItem,);//省市二级选择器
//        pvOptions.setPicker(mProvinceItem);//省一级选择器
        pvOptions.show();
    }

    /**
     * 解析gson
     */
    private ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void showDialog(String[] item, int index, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setSingleChoiceItems(item, index, onClickListener)
                .setPositiveButton("确定", onClickListener)
                .setNegativeButton("取消", onClickListener)
                .show();
    }

    class SexOnClick implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            //which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
            if (which >= 0) {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                sexIndex = which;
            } else {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mtvSex.setText(sexItem[sexIndex]);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                }
            }
        }
    }

}
