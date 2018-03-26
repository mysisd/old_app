package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.testyingtou.yingtouproject.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import utils.App;
import utils.DividerItemDecoration;
import utils.GlideImageLoader;

public class DynamicsConsultingFragment extends Fragment implements OnBannerListener {

    private RecyclerView mRvConsulting;
    private List<String> mRvData;
    private Integer[] images = new Integer[]{R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3, R.mipmap.bg4, R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3, R.mipmap.bg4};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dynamics_consulting, container, false);
        initRvData();
        initUI(layout);
        return layout;
    }

    private void initUI(View layout) {
        initBanner(layout);
        initRv(layout);
    }

    /**
     * 设置banner
     */
    private void initBanner(View layout) {
        Banner banner = (Banner) layout.findViewById(R.id.banner);
        banner.setImages(App.images)
                .setBannerTitles(App.title)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
    }

    /**
     * RecyclerView
     */
    private void initRv(View layout) {
        mRvConsulting = (RecyclerView) layout.findViewById(R.id.rv_consulting);
        //设置固定大小
        mRvConsulting.setHasFixedSize(true);
        //创建线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRvConsulting.setLayoutManager(layoutManager);
        //设置并添加水平分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRvConsulting.addItemDecoration(itemDecoration);
        mRvConsulting.setAdapter(new RvConsultingAdapter());
    }


    class RvConsultingAdapter extends RecyclerView.Adapter<RvConsultingAdapter.MyViewHolder> {

        @Override
        public RvConsultingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_rv_consulting, parent, false);
            MyViewHolder holder = new MyViewHolder(inflate);
            return holder;
        }

        @Override
        public void onBindViewHolder(RvConsultingAdapter.MyViewHolder holder, int position) {
            holder.mTvTitle.setText(mRvData.get(position));
            holder.mIvimg.setImageResource(images[position]);

            String date = getCurTime();
            holder.mTvTime.setText(date);

        }

        @Override
        public int getItemCount() {
            return mRvData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTvTitle;
            private final TextView mTvTime;
            private final ImageView mIvimg;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTvTitle = (TextView) itemView.findViewById(R.id.tv_item_consulting_title);
                mIvimg = (ImageView) itemView.findViewById(R.id.iv_item_consulting_image);
                mTvTime = (TextView) itemView.findViewById(R.id.tv_item_consulting_time);
            }
        }
    }

    /**
     * 获取当前年月日
     */
    private String getCurTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new java.util.Date());
    }

    private void initRvData() {
        mRvData = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            mRvData.add("李嘉诚又双叒叕卖楼了！大撤离的真正原因曝光！" + i);
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getActivity(), "你点击了：" + position, Toast.LENGTH_SHORT).show();
    }
}
