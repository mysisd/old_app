package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.testyingtou.yingtouproject.BuyActivity;
import com.testyingtou.yingtouproject.R;
import com.testyingtou.yingtouproject.SellActivity;

public class KLineFragment extends Fragment implements View.OnClickListener {


    private String commodityNo, number, sellPrice, buyPrice,mUserId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_kline, container, false);
        initWebView(layout);
        LinearLayout llBottom = (LinearLayout) layout.findViewById(R.id.bottom);
        llBottom.findViewById(R.id.rl_buy).setOnClickListener(this);
        llBottom.findViewById(R.id.rl_sell).setOnClickListener(this);
        Bundle bundle = getArguments();

        commodityNo = bundle.getString("COMMODITY_NO");
        number = bundle.getString("NUMBER");
        sellPrice = bundle.getString("SELL_PRICE");
        buyPrice = bundle.getString("BUY_PRICE");
        mUserId = bundle.getString("USER_ID");
        return layout;
    }

    private void initWebView(View layout) {
        WebView webView = (WebView) layout.findViewById(R.id.wv_kline);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://xmyttz.com/rx/rx/rx_data");
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
