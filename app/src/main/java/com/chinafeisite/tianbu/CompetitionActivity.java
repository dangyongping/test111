package com.chinafeisite.tianbu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafeisite.tianbu.competition.PageHelper;
import com.chinafeisite.tianbu.competition.adapter.Badapter;
import com.chinafeisite.tianbu.okhttpUtils.MyCallback;
import com.chinafeisite.tianbu.okhttpUtils.OkhttpTool;
import com.chinafeisite.tianbu.okhttpUtils.StringPaser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompetitionActivity
        extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener
{
    public static final String TAG = "CompetitionActivity";
    private GridView mGv_gameRoom;
    //private JSONArray mRoomList; //真实数据
    private List<String> mRoomList;//假数据
    private PageHelper<String> mPageDaoImpl;
    private TextView mBtnPreItem;
    private TextView mBtnNextItem;
    private TextView mTvPageNo;

    //被选着的索引
    private int selectIndex = 0;


    String sceneName = "";        //场景名
    String nickName = "";    //昵称
    String headImgUrl = "";            //头像
    String roomNum = "";    //房间号
    String password = "";        //密码
    private EditText room_num;
    private EditText item_password;
    private String mStr;
    private Adapter mAdapter;
    private Button mBtn_queding;
    private Button mBtn_cancel;
    private List<String> sData;
    private List<String> mAaaList;
    private View mChildView;
    private HorizontalScrollView mScrollView;
    private Button mEncryButton;
    private GridView mGv_getSence;
    private List<String> mMmlist;
    private Badapter mSenceAdapter;
    private int currentPosition;
    private JSONArray mJsonArray;
    private JSONArray mSenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        enterCompetitionＭode();
        jiashuju();
        init();
//        initData();
    }

  /*  private void initData() {
        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

    private void jiashuju() {
        //mRoomList = new ArrayList<String>();
        sData = new ArrayList<String>();
        for (int i = 0; i < 40; i++) {
            sData.add(i + "");
        }
    }

    private void init() {
        mSenceAdapter = new Badapter();//获取场景adapter
        mGv_gameRoom = (GridView) findViewById(R.id.gv_gameRoom);
        mAdapter = new Adapter();
        mGv_gameRoom.setAdapter(mAdapter);
        //条目点击事件
        mGv_gameRoom.setOnItemClickListener(this);
        //返回键
        Button back = (Button) findViewById(R.id.back);
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

        Button btnCreatRoom = (Button) findViewById(R.id.creatRoom);//创建房间
        btnCreatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求场景信息
                getSceneNews();
                showdCreatRoomDialog();

            }
        });
        //进入房间
        Button btnEnterRoom = (Button) findViewById(R.id.enterRoom);
        btnEnterRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterRoomDialog();
            }
        });
    }

    //场景的adapter
    private class Badapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mSenceList != null) {
                return mSenceList.length();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mSenceList != null) {
                try {
                    mSenceList.get(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void setDate(JSONArray senceList) {
            Log.d(TAG, "setDate: 执行了");
            mSenceList = senceList;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "======getView");
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(CompetitionActivity.this, R.layout.childview, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_scene_name = (TextView) convertView.findViewById(R.id.tv_scene_name);
                viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            try {
                JSONObject object = (JSONObject) mSenceList.get(position);
                viewHolder.tv_scene_name.setText(object.optString("sceneName"));
                if (currentPosition == position) {
                    viewHolder.iv_select.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        public class ViewHolder {
            TextView tv_scene_name;
            ImageView iv_select;
        }
    }

    /**
     * 获取场景信息网络请求
     */
    private void getSceneNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = theApp.gameUrl + "/getSceneList";
                Log.d(TAG, "getSceneNews=url+" + url);
                OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
                    @Override
                    public void onSuccess(String string) {
                        Log.d(TAG, "getSceneNews+onSuccess" + string);
                        super.onSuccess(string);
                        try {
                            mSenceList = new JSONArray(string);
                            mSenceAdapter.setDate(mSenceList);
                           /* for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String     sceneName  = jsonObject.optString("sceneName");
                                String     sceneImg   = jsonObject.optString("sceneImg");
                                int id = jsonObject.optInt("id");
                                double distance = jsonObject.optDouble("distance");
                                Log.d(TAG,
                                      "sceneName" + sceneName + ",sceneImg" + sceneImg + ",distance" + distance);
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "异常" + e.toString());

                        }
                    }

                    @Override
                    public void onFailed() {
                        super.onFailed();
                        Log.d(TAG, "getSceneNews+onFailed");

                    }
                });
            }
        }).start();
    }

    /**
     * *显示创建房间(或者选择场景的)的dialog
     */
    private void showdCreatRoomDialog() {

        Log.d(TAG, "=========showdCreatRoomialog");
        final AlertDialog mDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionActivity.this);
        View view = View.inflate(CompetitionActivity.this,
                R.layout.dialog_creat,
                null);
        Button btn1_cancel = (Button) view.findViewById(R.id.btn1_cancel);
        mGv_getSence = (GridView) view.findViewById(R.id.gv_getSence);
        mGv_getSence.setAdapter(mSenceAdapter);
        mGv_getSence.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                for (int i = 0; i < parent.getCount(); i++) {
                    ImageView iv_select = (ImageView) mGv_getSence.getChildAt(i).findViewById(R.id.iv_select);
                    if (position == i) {//当前选中的Item
                        iv_select.setVisibility(View.VISIBLE);
                    } else {
                        iv_select.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(false);
        mDialog = builder.create();
        mDialog.show();
        btn1_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        Button btn1_queding = (Button) view.findViewById(R.id.btn1_queding);
        btn1_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = (JSONObject) mSenceList.get(currentPosition);
                    String currentSceneName = jsonObject.optString("sceneName");
                    Log.d(TAG, "现在选中 了:" + currentPosition + "场景名" + currentSceneName);
                    showCreatPasswordDialog(currentSceneName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        DisplayMetrics d = CompetitionActivity.this.getResources()
                .getDisplayMetrics();
        Log.d(TAG, "++++++++++++" + d.heightPixels * 0.58 + "=====" + d.widthPixels * 0.62);
        WindowManager.LayoutParams params = mDialog.getWindow()
                .getAttributes();
        params.width = (int) (d.widthPixels * 0.62);
        params.height = (int) (d.heightPixels * 0.58);
        mDialog.getWindow()
                .setAttributes(params);
    }

    /**
     * 创建密码的dialog
     */
    private void showCreatPasswordDialog(final String currentSceneName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionActivity.this);
        View view = LayoutInflater.from(CompetitionActivity.this)
                .inflate(R.layout.creatpassworddialog, null);
        mEncryButton = (Button) view.findViewById(R.id.encryButton);//是否加密的button
        // Boolean isEncry = true; //是否加密的标记
        mEncryButton.setTag(true);
        mEncryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isEncry = (Boolean) mEncryButton.getTag();
                if (isEncry) {
                    mEncryButton.setText("公开");
                    mEncryButton.setTag(false);
                } else {
                    mEncryButton.setText("加密");
                    mEncryButton.setTag(true);
                }
            }
        });
        builder.setView(view);
        //布局文件的组件初始化
        initPasswordDialog(view);
        builder.setCancelable(false);
        final Dialog dialog = builder.create();
        dialog.show();
        mBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否加密
                Boolean isEncry = (Boolean) mEncryButton.getTag();
                String str_password = item_password.getText()
                        .toString()
                        .trim();
                if (isEncry) {//加密
                    //判断是否为空
                    if (TextUtils.isEmpty(str_password)) {
                        Toast toast = Toast.makeText(CompetitionActivity.this,
                                "密码不能为空",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                } else {//不加密
                    str_password = "";
                }
                //mSenceList
                Log.d(TAG, "密码创建后,打印场景信息:str_password=" + str_password);
                //创建房间接口
                createRoom(str_password, currentSceneName);
            }


        });
    }

    /**
     * 创建房间的接口
     */
    private void createRoom(final String str_password, final String currentSceneName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // String url = theApp.gameUrl + "/createRoom?openId=" + PushReceiver.getopenId() + "&sceneName=" + currentSceneName + "&password=" + str_password;
                String url = theApp.gameUrl + "/createRoom?openId=" + 3 + "&sceneName=" + currentSceneName + "&password=" + str_password;
                Log.d(TAG, "创建房间的接口,打印url:" + url);
                OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
                    @Override
                    public void onSuccess(String string) {
                        Log.d(TAG, "createRoom+onSuccess"+string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            String msg = jsonObject.optString("msg");
                            String roomNum = jsonObject.optString("roomNum");
                            Boolean isSuccess = jsonObject.optBoolean("isSuccess");
                            if (isSuccess) {//成功的话调用joinRoom接口
//                                    joinRoom(roomNum);
//                                    "myself"表示自己创建的房间
                                gotoGameRoomActivity(roomNum,"myself");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "createRoom异常" + e.toString());
                        }
                    }

                    @Override
                    public void onFailed() {
                        super.onFailed();
                        Log.d(TAG, "createRoom+onFailed");
                    }
                });
            }
        }).start();
    }


    /***
     * 显示进入房间的Dialog
     */
    private void showEnterRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionActivity.this);
        View view = LayoutInflater.from(CompetitionActivity.this)
                .inflate(R.layout.searchroomdialog, null);
        builder.setView(view);
        //布局文件的组件初始化
        initPasswordDialog(view);
        builder.setCancelable(false);
        final Dialog dialog = builder.create();
        dialog.show();
        mBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtn_queding.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String str_room_num = item_password.getText()
                        .toString()
                        .trim();
                //判断是否为空
                if (TextUtils.isEmpty(str_room_num)) {
                    Toast toast = Toast.makeText(CompetitionActivity.this,
                            "房间号不能为空",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                //判断是否存在该房间
                // TODO: 2016/12/22
                if (!sData.contains(str_room_num)) {
                    Toast toast = Toast.makeText(CompetitionActivity.this,
                            "房间号不存在",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                ;
                //弹出输入密码dailog,将房间号传入
                showInputPasswordDialog(str_room_num);
                dialog.dismiss();

            }


        });
    }

    /**
     * 输入密码dialog*
     */
    private void showInputPasswordDialog(String str_room_num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionActivity.this);
        View view = LayoutInflater.from(CompetitionActivity.this)
                .inflate(R.layout.passworddialog,
                        null);
        TextView password_roomNum = (TextView) view.findViewById(R.id.password_roomNum);
        password_roomNum.setText("房间号:" + str_room_num);
        builder.setView(view);
        //布局文件的组件初始化
        initPasswordDialog(view);
        builder.setCancelable(false);
        final Dialog dialog = builder.create();
        dialog.show();
        mBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_room_num = item_password.getText()
                        .toString()
                        .trim();
                if (TextUtils.isEmpty(str_room_num)) {
                    Toast toast = Toast.makeText(CompetitionActivity.this,
                            "密码不能为空",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                //判断密码是否正确
                // TODO: 2016/12/19
               /* if (){
                    Toast toast = Toast.makeText(CompetitionActivity.this, "密码不正确,请重新输入", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }*/
                //进入房间 "others" 表示别人创建的房间
                gotoGameRoomActivity(str_room_num,"others");
                dialog.dismiss();
            }
        });
    }

    private void gotoGameRoomActivity(String roomNum,String Tag) {
        Intent intent = new Intent(CompetitionActivity.this,
                GameRoomActivity.class);
//        intent.putExtra("roomNum",roomNum);
//        intent.putExtra("Tag",Tag);
        Bundle bundle=new Bundle();
        bundle.putString("roomNum", roomNum);
        bundle.putString("Tag",Tag);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * gridview的adapter
     */

    public class Adapter
            extends BaseAdapter {

        Context mContext;
        //private  List<String> sData;//listview的数据源

        /* public Adapter(List<String> strings,Context context) {
              sData = strings;
             mContext = context;
         }*/
        @Override
        public int getCount() {
            if (sData != null) {
                // return mRoomList.length();
                return sData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
         /*  if (mRoomList!=null){
               try {
                   return mRoomList.get(position);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               return null;
           }*/
            return sData.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(CompetitionActivity.this)
                        .inflate(R.layout.gridview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mHeadimg1 = (ImageView) convertView.findViewById(R.id.headimg);
                viewHolder.mScene_name = (TextView) convertView.findViewById(R.id.house_name);
                viewHolder.mHouse_num = (TextView) convertView.findViewById(R.id.house_num);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
        /*
            JSONObject room = null;
            try {
                room = (JSONObject) mRoomList.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            roomNum = room.optString("roomNum");     //房间号
            sceneName = room.optString("sceneName"); //场景
            password = room.optString("password");  //放进密码
            headImgUrl = room.optString("headImg"); //头像
            Log.d(TAG, "position:"+position+"roomNum"+roomNum+"sceneName"+sceneName+"password"+password);
            //Picasso.with(CompetitionActivity.this).load(headImgUrl).transform(new CircleTransform());
            viewHolder.mScene_name.setText(sceneName);
            viewHolder.mHouse_num.setText(roomNum);*/
            viewHolder.mHouse_num.setText(sData.get(position));
            return convertView;
        }

        private class ViewHolder {
            ImageView mHeadimg;
            ImageView mHeadimg1;
            TextView mScene_name;
            TextView mHouse_num;
        }

       /* public void setData(List<String> strings) {
            sData = strings;
            notifyDataSetChanged();
        }*/
    }



    /**
     * 进入竞赛模式方法
     */

    private void enterCompetitionＭode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // String baseUrl = "http://dev.vrun.sh.cn/vrun/matchCtrl";
                String url = theApp.gameUrl + "/getRoomList";
                System.out.print("dddddddddd=getRoomList+url=" + url);
                OkhttpTool.doget(url, new MyCallback(new StringPaser()) {
                    @Override
                    public void onSuccess(String string) {
                        super.onSuccess(string);
                    }

                    @Override
                    public void onFailed() {
                        super.onFailed();
                    }
                    /*@Override
                   public void onResponse(Response response)
                            throws IOException
                    {
                        Log.d(TAG, "dddddddddd=response" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response.body()
                                                                           .string());
                            //房间信息
                            //mRoomList = jsonObject.getJSONArray("roomList");
                            //用户信息
                           // JSONArray userInfo = jsonObject.getJSONArray("userInfo");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "dddddddd+JSONException" + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                        Log.d(TAG, "dddddddd" + "=onFailure");
                    }*/
                });
            }
        }).start();
    }

    /**
     * huo获取场景信息的方法
     */


    private void initPasswordDialog(View view) {
        //room_num = (EditText)view.findViewById(R.id.room_num);
        item_password = (EditText) view.findViewById(R.id.item_password);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(item_password.getWindowToken(), 0);
        mBtn_queding = (Button) view.findViewById(R.id.btn_queding);
        mBtn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final Button btn0 = (Button) view.findViewById(R.id.btn0);
        final Button btn1 = (Button) view.findViewById(R.id.btn1);
        final Button btn2 = (Button) view.findViewById(R.id.btn2);
        final Button btn3 = (Button) view.findViewById(R.id.btn3);
        final Button btn4 = (Button) view.findViewById(R.id.btn4);
        final Button btn5 = (Button) view.findViewById(R.id.btn5);
        final Button btn6 = (Button) view.findViewById(R.id.btn6);
        final Button btn7 = (Button) view.findViewById(R.id.btn7);
        final Button btn8 = (Button) view.findViewById(R.id.btn8);
        final Button btn9 = (Button) view.findViewById(R.id.btn9);
        final Button btnC = (Button) view.findViewById(R.id.btnC);
        mBtn_queding.setOnClickListener(this);
        mBtn_cancel.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnC.setOnClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //弹出输入密码框
        //showInputPasswordDialog();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_queding:
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn0:
                mStr = item_password.getText()
                        .toString();
                mStr += "0";
                item_password.setText(mStr);
                break;
            case R.id.btn1:
                mStr = item_password.getText()
                        .toString();
                mStr += "1";
                item_password.setText(mStr);
                break;
            case R.id.btn2:
                mStr = item_password.getText()
                        .toString();
                mStr += "2";
                item_password.setText(mStr);
                break;
            case R.id.btn3:
                mStr = item_password.getText()
                        .toString();
                mStr += "3";
                item_password.setText(mStr);
                break;
            case R.id.btn4:
                mStr = item_password.getText()
                        .toString();
                mStr += "4";
                item_password.setText(mStr);
                break;
            case R.id.btn5:
                mStr = item_password.getText()
                        .toString();
                mStr += "5";
                item_password.setText(mStr);
                break;
            case R.id.btn6:
                mStr = item_password.getText()
                        .toString();
                mStr += "6";
                item_password.setText(mStr);
                break;
            case R.id.btn7:
                mStr = item_password.getText()
                        .toString();
                mStr += "7";
                item_password.setText(mStr);
                break;
            case R.id.btn8:
                mStr = item_password.getText()
                        .toString();
                mStr += "8";
                item_password.setText(mStr);
                break;
            case R.id.btn9:
                mStr = item_password.getText()
                        .toString();
                mStr += "9";
                item_password.setText(mStr);
                break;
            case R.id.btnC:
                mStr = item_password.getText()
                        .toString();
                try {
                    item_password.setText(mStr.substring(0, mStr.length() - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                    item_password.setText("");
                }
                break;
        }

    }


}
