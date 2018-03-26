package utils;

import android.app.Application;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.testyingtou.yingtouproject.MainActivity;
import com.testyingtou.yingtouproject.R;
import com.zxy.recovery.core.Recovery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class App extends Application{
    public static List<?> images = new ArrayList<>();
    public static List<String> title = new ArrayList<>();
    public static int H, W;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        H = dm.heightPixels;
        W = dm.widthPixels;
        Fresco.initialize(this);
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .init(this);
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List imageList = Arrays.asList(urls);
        images = new ArrayList<>(imageList);
        List titleList = Arrays.asList(tips);
        title = new ArrayList<>(titleList);
    }
}
