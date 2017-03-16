package com.example.jh.swiperefreshlayout_recyclerview;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 本demo测试SwipeRefreshLayou、RecyclerView实现上拉加载下拉刷新
 * 这里可以引用bufferknife减少繁琐的findviewbyid, 有兴趣可以测试下。
 *
 */
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String names[] = new String[]{"小虎", "小玉", "小美", "小李"};
    private String[] description = new String[]{
            "游戏高手", "美丽女孩", "it女强人","android 开发大神"
    };
    private int imageIds[] = new int[]{
            R.drawable.libai, R.drawable.nongyu, R.drawable.qingzhao,
            R.drawable.tiger};
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    // 适配器
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Handler handler = new Handler();
    boolean isLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        // 初始化控件
        initView();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN
        );
        swipeRefreshLayout.setOnRefreshListener(this);
        // 布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        //
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged方法被执行");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int count = adapter.getItemCount();
                //下面的判断比较列表项是否和可见列表最后一项一致，程序刚启动时，条件满足，自动加载，当加载到列表项多到一页展示不完时，停止自动加载
                //当手指在屏幕上向上滑动时，该条件又满足，然后就加载更多了
                if(lastVisibleItemPosition + 1 == count){
                    Log.e(TAG, "Loading excute");
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    Log.e(TAG, "isRefreshing" + isRefreshing);
                    if(isRefreshing){
                        return;
                    }
                    if(!isRefreshing){
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                              isLoading = false;
                                Log.d(TAG, "load data complete");
                                int count = adapter.getItemCount();
                                // 加载更多数据
                                addData();
                                int count1 = adapter.getItemCount();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 2000);
                    }
                }
            }
        });

        setClickListener();
        addData();

    }

    public void setClickListener() {
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
            }

            @Override
            public void onItemLongClicked(View view, int position) {
            }
        });

    }

    private void createData(){
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("header", imageIds[i]);
            listItem.put("name", names[i]);
            listItem.put("description", description[i]);
            data.add(listItem);
        }
    }


    //下拉刷新时触发该回调函数
    @Override
    public void onRefresh() {
        Log.e(TAG, "onRefresh");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //添加刷新的内容
                Log.e(TAG, "handler.Onrefresh");
                addData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void addData() {
        Log.e(TAG, "addData =" + data.size());
        if(data.size() < 1){
            createData();
        }
        adapter.addData(data);
    }
}
