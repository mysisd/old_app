package com.testyingtou.yingtouproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CapitalDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_details);
        findViewById(R.id.tv_capital_international).setOnClickListener(this);
        findViewById(R.id.tv_capital_international_open).setOnClickListener(this);
        findViewById(R.id.iv_capital_back).setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_capital_international:
                startActivity(new Intent(this, BindCardActivity.class));
                break;
            case R.id.tv_capital_international_open:
                startActivity(new Intent(this, BindCardActivity.class));
                break;
            case R.id.iv_capital_back:
                finish();
                break;
        }
    }
}
