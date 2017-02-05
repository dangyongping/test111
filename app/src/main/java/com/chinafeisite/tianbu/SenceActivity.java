package com.chinafeisite.tianbu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  @项目名：  RK3188-BP 
 *  @包名：    com.chinafeisite.tianbu
 *  @文件名:   SenceActivity
 *  @创建者:   Administrator
 *  @创建时间:  2017/1/4 11:31
 *  @描述：    TODO
 */
public class SenceActivity extends Activity
        implements AdapterView.OnItemClickListener
{
    String[] urls = {"http://121.12.98.180/xdispatch/7xqi17.com1.z0.glb.clouddn.com/BM10KM3.mp4","http://125.39.21.14/xdispatch/7xqi17.com1.z0.glb.clouddn.com/baiyu-38Min-720P-1.5M.mp4",
                    "http://125.39.21.9/xdispatch/7xqi17.com1.z0.glb.clouddn.com/tuershan-38Min-720P-1.5M-v2.mp4","http://oj9am3239.bkt.clouddn.com/nuoergai-720P-37Min-1.5M-ME.mp4" };
   // 北京马拉松,白玉路,兔儿山路线,诺尔盖大草原
    public static SenceActivity instance = null;
    private static final String TAG = "SenceActivity";
    private SenceAdapter mAdapter;
    private List<Map> mList;
    private String[] mName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sence);
        instance = this;
        init();
    }

    private void init() {
        mName = new String[]{"北京马拉松","白玉路","兔儿山路线","诺尔盖大草原",
                         "厦门马拉松","深圳马拉松","亚拉雪山", "海子山",
                         "扎尕那地质公园","央迈勇","甘孜大雪山","卧龙自然保护区",
                         "上海马拉松10公里","上海马拉松mini赛","北马5公里赛"};
        int[] img = {R.drawable.sence1,R.drawable.sence2,R.drawable.sence3,R.drawable.sence4,
                     R.drawable.xiamen,R.drawable.sz10km,R.drawable.yalaxueshan,R.drawable.haizishan,
                     R.drawable.zhanaina, R.drawable.yangmaiyong,R.drawable.ganzidaxueshan,R.drawable.wolong,
                     R.drawable.shanghai10,R.drawable.shanghaimini,R.drawable.bj5km};

        //声明一个Hash对象并添加数据
       mList = new ArrayList<>();
        for (int i = 0; i < mName.length-1; i++) {
            Map map = new HashMap();
            map.put("name", mName[i]);
            map.put("img", img[i]);

            mList.add(map);
        }
        GridView mGv_Sence = (GridView) findViewById(R.id.gv_Sence);
        mAdapter = new SenceAdapter();
        mGv_Sence.setAdapter(mAdapter);
        //条目点击事件
        mGv_Sence.setOnItemClickListener(this);
        //返回键
        Button back   = (Button) findViewById(R.id.back);
        Button goHome = (Button) findViewById(R.id.goHome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        byte[] nK = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f};
        theApp.setThirdAPP("正在播放场景视频:"+mName[position]);
        theApp.responseE2s(nK[position]);
        Log.d("======", "onItemClick"+position+"===nK[position]=="+nK[position]);
    }

    private class SenceAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if (mList!=null){
                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList!=null){
                return mList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder =null;
            if (convertView == null) {
                convertView = View.inflate(SenceActivity.this, R.layout.sence_xml, null);
                viewHolder = new ViewHolder();
                viewHolder.tv= (TextView) convertView.findViewById(R.id.tv);
                viewHolder.iv_sencelist= (ImageView) convertView.findViewById(R.id.iv_sencelist);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();

            Map<String,Object> map = mList.get(position);
            String name = (String) map.get("name");
            int img = (int) map.get("img");
            viewHolder.tv.setText(name);
            viewHolder.iv_sencelist.setImageResource(img);
            return convertView;
        }
        public class ViewHolder{
            TextView  tv;
            ImageView iv_sencelist;
        }
        }

}
