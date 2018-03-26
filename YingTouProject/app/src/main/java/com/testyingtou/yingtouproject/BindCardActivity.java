package com.testyingtou.yingtouproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import utils.GetJsonDataUtil;
import utils.JsonBean;
import utils.PublicSetting;

public class BindCardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvChoiceCity;
    private ArrayList<JsonBean> mProvinceItem = new ArrayList<>();
    private ArrayList<ArrayList<String>> mCityItem = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> mAreaItem = new ArrayList<>();
    private EditText mEtBank, mEtName, mEtId, mEtAddress, mEtPhone, mEtPassWord;
    private int[] b = new int[]{19, 19, 19, 19};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        findViewById(R.id.iv_card_back).setOnClickListener(this);
        initUI();

    }

    private void initUI() {
        mTvChoiceCity = (TextView) findViewById(R.id.tv_card_city);
        mTvChoiceCity.setOnClickListener(this);

        initSpinnerBank();
        findViewById(R.id.btn_card_binding).setOnClickListener(this);
        mEtBank = (EditText) findViewById(R.id.et_card_bank);
        mEtName = (EditText) findViewById(R.id.et_card_name);
        mEtId = (EditText) findViewById(R.id.et_card_id);
        mEtAddress = (EditText) findViewById(R.id.et_card_address);
        mEtPhone = (EditText) findViewById(R.id.et_card_phone);
        mEtPassWord = (EditText) findViewById(R.id.et_card_password);
    }

    /**
     * 下拉银行列表
     */
    private void initSpinnerBank() {
        Spinner spinnerBank = (Spinner) findViewById(R.id.spinner_card_bank);
        final String[] spinnerData = getResources().getStringArray(R.array.bank);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(spinnerAdapter);
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //动态设置et的最大输入位数
                mEtBank.setFilters(new InputFilter[]{new InputFilter.LengthFilter(b[position])});
                mEtBank.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_card_back:
                finish();
                break;
            case R.id.tv_card_city:
                initJsonData();
                ShowPickerView();
                break;
            case R.id.btn_card_binding:
                if (isEmptied()) {
                    String phone = mEtPhone.getText().toString().trim();
                    if (!PublicSetting.isMobile(phone)) {
                        Toast.makeText(this, "手机号不合法", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PublicSetting.autoDelay("成功", this);
                }

                break;
        }
    }

    /**
     * 判断银行卡等信息是否为空
     * @return
     */
    private boolean isEmptied() {
        if (mEtBank.getText().toString().trim().equals("")) {
            Toast.makeText(this, "银行卡卡号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtId.getText().toString().trim().equals("")) {
            Toast.makeText(this, "身份证不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtAddress.getText().toString().trim().equals("")) {
            Toast.makeText(this, "支行地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPhone.getText().toString().trim().equals("")) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPassWord.getText().toString().trim().equals("")) {
            Toast.makeText(this, "交易密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                mTvChoiceCity.setText(text);
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
}
