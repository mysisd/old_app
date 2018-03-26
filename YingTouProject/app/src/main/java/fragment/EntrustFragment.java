package fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.testyingtou.yingtouproject.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Interface.RequestServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.DataBaseField;

public class EntrustFragment extends Fragment {

    private MyRvAdapter mRvAdapter;
    private List<DataBaseField> mDbFieldList;
    private String mUserId;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private TextView mTvDefaultHint;
    private View layout, mViewLine;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_entrust, container, false);
        initUI();

        initRefresh(mRefreshLayout);
        initRetrofit();

        return layout;
    }

    private void initUI() {
        mDbFieldList = new ArrayList<>();
        mTvDefaultHint = (TextView) layout.findViewById(R.id.tv_entrust_default_hint);
        mViewLine = layout.findViewById(R.id.view_entrust_line);
        mRefreshLayout = (SmartRefreshLayout) layout.findViewById(R.id.refreshLayout_entrust);
        Intent intent = getActivity().getIntent();
        //获取登录用户ID
        mUserId = intent.getStringExtra("USER_ID");
        initRv();
    }

    private void initRefresh(SmartRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                /**
                 * 确保刷新动画加载完成后才更新rv数据
                 */
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    public void run() {
                        refreshlayout.finishRefresh();
                        initRetrofit();
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
    }


    private void initRv() {
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_entrust);
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //创建线性布局
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(manager);
        //方向
        manager.setOrientation(OrientationHelper.VERTICAL);
        mRvAdapter = new MyRvAdapter();
        mRecyclerView.setAdapter(mRvAdapter);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://xmyttz.com/rx/user/")
                .build();
        RequestServices services = retrofit.create(RequestServices.class);
        Call<ResponseBody> call = services.getLessUrl("en_list?name=" + mUserId);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.e("json", json);
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<DataBaseField>>() {
                    }.getType();

                    List<DataBaseField> bean = gson.fromJson(json, type);
                    //添加数据之前清空list内容,防止刷新的时候会重复添加集合之前的数据
                    mDbFieldList.clear();
                    //把所有数据添加到list
                    mDbFieldList.add(new DataBaseField("名称", "状态", "买/卖", "委托价", "委托量", "下单时间"));
                    mDbFieldList.addAll(bean);
                    mRvAdapter.notifyDataSetChanged();

                    if (mDbFieldList.size() == 1) {//默认有添加一行,如果json为空,显示和隐藏控件
                        mTvDefaultHint.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mViewLine.setVisibility(View.GONE);
                    } else {
                        mTvDefaultHint.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mViewLine.setVisibility(View.VISIBLE);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * Rv适配器
     */
    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyViewHolder> {

        public MyRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View layout = LayoutInflater.from(context).inflate(R.layout.item_rv_entrustl, parent, false);
            return new MyRvAdapter.MyViewHolder(layout);
        }

        public void onBindViewHolder(final MyRvAdapter.MyViewHolder holder, int position) {

            holder.mTvName.setText(mDbFieldList.get(position).getName());
            holder.mTvStatus.setText(mDbFieldList.get(position).getStatus());
            holder.mTvBuyOrSell.setText(mDbFieldList.get(position).getDeal());
            holder.mTvPrice.setText(mDbFieldList.get(position).getEntrusted_price());
            holder.mTvCount.setText(mDbFieldList.get(position).getEntrust_amount());
            holder.mTvDate.setText(mDbFieldList.get(position).getDate());
        }

        public int getItemCount() {
            return mDbFieldList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView mTvName, mTvStatus, mTvBuyOrSell, mTvPrice, mTvCount, mTvDate;

            MyViewHolder(View itemView) {
                super(itemView);
                mTvName = (TextView) itemView.findViewById(R.id.tv_item_entrust_name);
                mTvStatus = (TextView) itemView.findViewById(R.id.tv_item_entrust_status);
                mTvBuyOrSell = (TextView) itemView.findViewById(R.id.tv_item_entrust_buy_or_sell);
                mTvPrice = (TextView) itemView.findViewById(R.id.tv_item_entrust_price);
                mTvCount = (TextView) itemView.findViewById(R.id.tv_item_entrust_count);
                mTvDate = (TextView) itemView.findViewById(R.id.tv_entrust_item_date);
            }
        }


    }
}