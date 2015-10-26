package com.example.listviewrefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private int index = 0;
    private static final int REFRESH_FINISH = 1; //设置一个刷新完成的标记量
    private SwipeRefreshLayout mSwipe;
    private ListView listView;
    private String[] arrays = new String[]{"Hello", "I am a student", "I love android", "love open Resource"};
    private ArrayAdapter<String> adapter;
    private List<String> data = new ArrayList<>();//asList是数组和集合之间的桥梁，不能直接用于初始化List接口变量，否则在加数据的时候会崩掉

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_FINISH:
                    data.add(getData());
                    adapter.notifyDataSetChanged();//提示数据适配器数据发生改变从而更新适配器里的数据
                    mSwipe.setRefreshing(false);//隐藏刷新进度条
                    break;
            }
        }
    };

    public String getData() {//循环获取数组中的信息
        String re = arrays[index];
        index = (index + 1) % arrays.length;
        return re;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe_ly);
        listView = (ListView) findViewById(R.id.lv);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        mSwipe.setOnRefreshListener(this);

        mSwipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //这里设置加载动画的颜色可选项，最多设置4种
    }


    @Override
    public void onRefresh() {//应该这么使用：网络访问或者其他的费时操作在这里边，最后发送message就ok
        //说明一下，这个demo里更新ui的东东不一定非要用handler，也可以直接在这里更新ui，只是没有了延迟的效果，但在
        //项目里肯定还是需要子线程来实现的

        handler.sendEmptyMessageDelayed(REFRESH_FINISH, 4000);//发送一个空消息并添加延迟时间
    }


 /*   public void onRefresh(){//或者采用这种方式也行，

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipe.setRefreshing(false);
            }
        },4000);
    }*/
}
