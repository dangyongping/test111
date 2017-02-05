package com.chinafeisite.tianbu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chinafeisite.tianbu.okhttpUtils.MyCallback;
import com.chinafeisite.tianbu.okhttpUtils.OkhttpTool;
import com.chinafeisite.tianbu.okhttpUtils.StringPaser;
/*import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/*
 *  @项目名：  RK3188-BP
 *  @包名：    com.chinafeisite.tianbu
 *  @文件名:   GameRoomActivity
 *  @创建者:   Administrator
 *  @创建时间:  2016/12/24 12:41
 *  @描述：    TODO
 */
public class GameRoomActivity
        extends Activity {
    private static final String TAG = "GameRoomActivity";
    //用户状态
    private String state = "0";
    private int distance = 0;

    public String getState() {
        return state;
    }

    public void setState(String mstate) {
        this.state = mstate;
    }

    private ListView mLv_gameState;
    private Timer mTimer;
    Handler handler;
    Adapter mAdpter;
    private String mRoomNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameroom);
        initData();
        new Thread(r).start();
        //list = getStateInfo1(); //初始化数据
        mAdpter = new Adapter();
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//每秒进行判断,假如按开始,准备,取消准备的 时候将state置为相应状态,在这里进行判断
                if (state == "1" | state == "2") {
                    getStateInfo2(state);
                } else {
                    getStateInfo1();
                }
                // TODO: 2017/1/12  第一步:获取getStateInfor()的数据源

                // TODO: 2017/1/12  第二步:获取liveRank()的数据源
                liveRank();
                // TODO: 2017/1/12  第三步:获取最终的数据源

                //第四步:将数据源赋予listview
                //mAdpter.setDate(mList);

//                得到该房间马拉松的距离,假设10km
                if (distance>=10 ) {
                    //发送指令是否停机??

                }
            }
        };

    }

    private void initData() {
        Intent intent = this.getIntent();
        mRoomNum = intent.getStringExtra("roomNum");
        String Tag = intent.getStringExtra("Tag");
        if (Tag.equals("myself")) {
//            自己创建的房间
            joinCreat_Room(mRoomNum);

        } else {
//            别人创建的房间
            joinRoom(mRoomNum);
        }
    }


    private void init() {
        Button button = (Button) findViewById(R.id.bttttt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLv_gameState = (ListView) findViewById(R.id.lv_gameState);
        mLv_gameState.setAdapter(mAdpter);
    }


    private class Adapter
            extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                View view = View.inflate(GameRoomActivity.this, R.layout.item_lv_gameroom, null);
                viewHolder.tv_num = (TextView) view.findViewById(R.id.tv_num);
                viewHolder.headimg_item_lv = (ImageView) findViewById(R.id.headimg_item_lv);
                viewHolder.tv_name_item_lv = (TextView) view.findViewById(R.id.tv_name_item_lv);
                viewHolder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();

            return convertView;
        }
        class ViewHolder {
            TextView tv_num;
            ImageView headimg_item_lv;
            TextView tv_name_item_lv;
            TextView tv_distance;
        }
    }


    /**
     * 状态更新(带有state参数)
     **/
    private void getStateInfo2(String STATE) {
        String url = theApp.gameUrl + "getStateInfo?openId=" + PushReceiver.getopenId() + "&roomNum=" + mRoomNum + "&&state=" + STATE;
        OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
            @Override
            public void onSuccess(String string) {
//                try {
//                    JSONArray jsonArray =new JSONArray(string);
//                    for (int i = 0 ; i < jsonArray.length();i++){
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        String roomNum =  jsonObject.optString("roomNum");
//                        Boolean isSuccess = jsonObject.optBoolean("isSuccess");
//                        openId
//		                state
//	                    canStart			true/false(当state为2时  有这个参数   如果是false   需要返回msg)
//		                msg
//
//		                if(state=="2"){
//
//		                }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "createRoom异常"+e.toString());
//                }
            }
            @Override
            public void onFailed() {
                super.onFailed();
            }
        });
    }

    /**
     * 状态更新(没有state参数)
     **/
    private void getStateInfo1() {
        String url = theApp.gameUrl + "getStateInfo?openId=" + PushReceiver.getopenId() + "&roomNum=" + mRoomNum;
        OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONArray jsonArray = new JSONArray(string);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String roomNum = jsonObject.optString("roomNum");
                        Boolean isSuccess = jsonObject.optBoolean("isSuccess");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "createRoom异常" + e.toString());
                }
            }
            @Override
            public void onFailed() {
                super.onFailed();
            }
        });

    }


    /***
     * 实时排名
     */
    private void liveRank() {
//        String url = theApp.gameUrl + "/liveRank?openId=" + PushReceiver.getopenId() + "&roomNum=" + mRoomNum + "&distance=" + DISTANCE;
//        OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
//            @Override
//            public void onSuccess(String string) {
//                try {
//                    JSONArray jsonArray = new JSONArray(string);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        String roomNum = jsonObject.optString("roomNum");
//                        Boolean isSuccess = jsonObject.optBoolean("isSuccess");
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "createRoom异常" + e.toString());
//                }
//            }
//
//            @Override
//            public void onFailed() {
//                super.onFailed();
//            }
//        });
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1 * 1000);
                    handler.sendMessage(handler.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 获取房间内成员信息
     */

    private void joinRoom(final String ROOMNUM) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = theApp.gameUrl + "/joinRoom?roomNum=" + ROOMNUM;
                Log.d(TAG, "joinRoom: ===" + url);
                OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
                    @Override
                    public void onSuccess(String string) {
                        try {
                            JSONArray jsonArray = new JSONArray(string);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String nickName = jsonObject.optString("nickName");
                                String headImg = jsonObject.optString("headImg");
                                String isReady = jsonObject.optString("isReady");
                                Log.d(TAG,
                                        "joinRoom:" + "nickName>" + nickName + ",headImg>" + headImg + ",isReady>" + isReady);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "joinRoom异常" + e.toString());
                        }
                    }

                    @Override
                    public void onFailed() {
                        super.onFailed();
                    }
                });
            }
        }).start();
    }

    /**
     * *获取房间内成员信息
     */

    private void joinCreat_Room(final String ROOMNUM) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String url = theApp.gameUrl + "/joinRoom?openId=OPENID&roomNum=ROOMNUM";
//                String url = theApp.gameUrl + "/joinRoom?openId="+PushReceiver.getopenId()+"&roomNum="+ROOMNUM;
                String url = theApp.gameUrl + "/joinRoom?openId=" + 3 + "&roomNum=" + ROOMNUM;
                Log.d(TAG, "run: joinCreat_Room==" + url);
                OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
                    @Override
                    public void onSuccess(String string) {
                        try {
                            JSONArray jsonArray = new JSONArray(string);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String nickName = jsonObject.optString("nickName");
                                String headImg = jsonObject.optString("headImg");
                                String isReady = jsonObject.optString("isReady");
                                Log.d(TAG,
                                        "joinRoom:" + "nickName>" + nickName + ",headImg>" + headImg + ",isReady>" + isReady);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "joinRoom异常" + e.toString());
                        }
                    }

                    @Override
                    public void onFailed() {
                        super.onFailed();
                    }
                });
            }
        }).start();
    }
//    public static  void getRunningInfo(){
//        String mm = theApp.baseUrl + theApp.sendDataUrl + "?appointId=" + appointId + "&reNewId=" + reNewId + "&openid=" + openId + "&speed=" + theApp.getSpeed() + "&distance=" + theApp.getDistance() + "&heartRate=" + theApp.getPulse() + "&calorie=" + theApp.getCal();
//
//        System.out.println("S------->>>" + "到点了打印getRunningInfo+url" + mm);
//
//        Map map = new HashMap();
//        map.put("appointId", appointId);
//        map.put("openid", openId);
//        map.put("speed", theApp.getSpeed());
//        map.put("distance", theApp.getDistance());
//        map.put("heartRate", theApp.getPulse());
//        map.put("calorie", theApp.getCal());
//        String json = JSON.toJSONString(map);
//        OkhttpTool.doget(theApp.baseUrl + theApp.sendDataUrl + "?info=" + json,
//                new MyCallback(new StringPaser() {
//                }));
//    }

}
