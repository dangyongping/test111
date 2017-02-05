package com.chinafeisite.tianbu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.chinafeisite.tianbu.okhttpUtils.MyCallback;
import com.chinafeisite.tianbu.okhttpUtils.OkhttpTool;
import com.chinafeisite.tianbu.okhttpUtils.StringPaser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 2016/10/17.
 */

public class PushReceiver
        extends PushMessageReceiver {
    private static final String TAG = "PushReceiver";
    static Context mContext;
    boolean isBind;
    static SharedPreferences sp;
    static String durationTime;
    static String openId;


    public static void setopenId(String openId) {
        openId = openId;
    }

    public static String getopenId() {
        return openId;
    }

    Timer fiveMinuteTimer;
    Timer timeoutTimer;
    Timer finallyTimer;
    static String reNewId;
    static String machineId;
    static String appointId;
    private boolean hasRenew = false;
    static String isRenew = "";
    static boolean isOpen = false;
    private String mBdurationTime = "";
    private SharedPreferences spp;
    private SharedPreferences sp2;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;
    private Long endTime;
    private Long mEndTime;
    String title;
    String interf = "";
    int i = 1;
    int a = 1;
    @Override
    public void onBind(Context context,
                       int errorCode,
                       String appid,
                       String userId,
                       String channelId,
                       String requestId) {

           if (!isBind) {
               Log.d(TAG, "S------->>>errorCode" + errorCode + ",channelId" + channelId);
               this.mContext = context;
               sp = context.getSharedPreferences("banpao", Context.MODE_PRIVATE);
               machineId = sp.getString("id", "");
               Log.d(TAG, "%%%%%%%%%%%%%%" + "machineId" + machineId);
               if (machineId == null || "".equals(machineId)) {
                   MainActivity.checkID(MainActivity.getLocalMacAddress(), "", channelId, sp);
               } else {
                   MainActivity.checkID(MainActivity.getLocalMacAddress(), machineId, channelId, sp);
               }
               isBind = true;
           }
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
        isBind = false;
    }

    @Override
    public void onSetTags(Context context,
                          int i,
                          List<String> list,
                          List<String> list1,
                          String s) {
    }

    @Override
    public void onDelTags(Context context,
                          int i,
                          List<String> list,
                          List<String> list1,
                          String s) {
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
    }


    //透传消息  s推送的消息  s1  自定义内容,为空或者json字符串
    @Override
    public void onMessage(Context context, String s, String s1) {
        //刚进来的时候把相关参数初始化(保存预约参数的sp)
        spp = context.getSharedPreferences("huancun", Context.MODE_PRIVATE);
        editor = spp.edit();
        editor.putBoolean("hasRenew", false);
        editor.putLong("mEndTime", 0);
        editor.putString("mBdurationTime", "");
        editor.commit();

        sp2 = context.getSharedPreferences("cacheAppointment", Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        System.out.println("S------->>>" + s + "@@@@@@@@@@@@@@@@@@@@@@@@@");

        String msg = "";
        try {
            JSONObject jsonObject = new JSONObject(s);

            //确定支付后,如果返回的是isSuccess为false,进来在这里进行判断,
            Boolean isSuccess = jsonObject.optBoolean("isSuccess", true);
            System.out.println("S------->>>" + isSuccess + "@@@@@@isSuccess@@@@@@@@@@@@@@@@@@@");
            if (!isSuccess) {
                isOpen = false;
            }

            //解析     首次和后面续约都会解析的
            title = jsonObject.optString("title");
            msg = jsonObject.optString("msg");
            openId = jsonObject.optString("openId");
            setopenId(openId);
            //扫码推送结果
            if ("appointInfo".equals(title)) {
//                if (editor2 != null) {
//                    editor2.putString("Appointment0", s);
//                    editor2.commit();
//                }
                a = 1;
                isOpen = jsonObject.optBoolean("canOpen");

                appointId = jsonObject.optString("appointId");
                isRenew = "Frist";
                durationTime = jsonObject.optString("durationTime"); //约定锻炼时间
                endTime = jsonObject.getLong("endTime");  //结束时间

                String strAA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime));
                System.out.println("S------->>>" + "=====endTime" + endTime + "==结束时间" + strAA + "当前时间:" + "=系统时间+" + getsystemTime());
            } else if ("renewInfo".equals(title)) {         //重新续约信息
//                if (i <= 2) {
//                    String name = "Appointment" + i;
//                    editor2.putString(name, s);
//                    editor2.commit();
//                    i = i + 1;
//                }

                isOpen = jsonObject.optBoolean("canOpen");
                reNewId = jsonObject.optString("reNewId");
                hasRenew = true;  //在确定续费后,推送过来设置hasRenew已续费
                isRenew = "second";
                mBdurationTime = jsonObject.optString("durationTime", "");
                endTime = jsonObject.optLong("endTime");    //结束时间

                String strCC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime));
                System.out.println("S------->>>" + "@@@@@@@@@@@@@@@@@@@@strCC" + strCC);

                //保存这两个参数hasRenew和mBdurationTime,目的是为了在后面的run()方法中可以获取
                editor.putString("mBdurationTime", mBdurationTime);
                editor.putBoolean("hasRenew", true);
                editor.commit();
            } else if("machineInfo".equals(title)){
                int xx = theApp.getJpgPathNum();
                String ss = Integer.toString(xx);
                String thirdApp = theApp.getThirdAPP();
                System.out.println("S------->>>" + "aaaaaaaaaa" + ss + "....." + xx + "========" + theApp.getTopActivity());
                if (xx == 1) {
                    interf = "扫码界面";
                    System.out.println("S------->>>" + "=======+++++++++++当前在扫码界面");
                } else if (xx == 2) {
                    if (thirdApp == "场景模式") {
                        interf = "场景模式";
                        System.out.println("S------->>>" + "=======+++++++++++当前在场景模式");
                    } else {
                        interf = "主界面";
                        System.out.println("S------->>>" + "=======+++++++++++当前在主界面");
                    }
                } else if (xx == 5) {
                    interf = "登山模式";
                    System.out.println("S------->>>" + "=======+++++++++++当前在登山模式");
                } else if (xx == 7) {
                    interf = "燃脂模式";
                    System.out.println("S------->>>" + "=======+++++++++++当前在燃脂模式");
                } else if (xx == 8) {
                    interf = "距离模式";
                    System.out.println("S------->>>" + "=======+++++++++++当前在距离模式");
                } else if (xx == 9) {
                    interf = "时间模式";
                    System.out.println("S------->>>" + "=======+++++++++++当前在时间模式");
                } else if (xx == 10) {
                    interf = "心率";
                    System.out.println("S------->>>" + "=======+++++++++++当前在心率");
                } else if (xx == 11) {
                    interf = "测脂模式";
                    System.out.println("S------->>>" + "=======+++++++++++当前在测脂模式");
                } else if (xx == 12) {
                    interf = "二维码结果页面";
                    System.out.println("S------->>>" + "=======+++++++++++当前在二维码结果页面");
                } else if (xx == 16) {
                    interf = "设置框";
                    System.out.println("S------->>>" + "=======+++++++++++当前在设置框");
                } else if (xx == 43) {
                    interf = "工厂设置";
                    System.out.println("S------->>>" + "=======+++++++++++当前在工厂设置");
                } else {

                    if (thirdApp == "亿峰网") {
                        interf = "亿峰网";
                        System.out.println("S------->>>" + "=======+++++++++++当前在亿峰网");
                    } else if (thirdApp == "爱奇艺") {
                        interf = "爱奇艺";
                        System.out.println("S------->>>" + "=======+++++++++++当前在爱奇艺");
                    } else if (thirdApp == "QQ音乐") {
                        interf = "QQ音乐";
                        System.out.println("S------->>>" + "=======+++++++++++当前在QQ音乐");
                    } else if (thirdApp == "多人竞赛") {
                        interf = "多人竞赛";
                        System.out.println("S------->>>" + "=======+++++++++++当前在多人竞赛");
                    } else if (thirdApp == "蓝牙") {
                        interf = "设置主界面";
                        System.out.println("S------->>>" + "=======+++++++++++当前在设置主界面");
                    } else {
                        System.out.println("S------->>>" + "=======+++++++++++当前在" + thirdApp);
                    }
                }
                String Appointment0 = sp2.getString("Appointment0", "没有找到相关记录");
                String Appointment1 = sp2.getString("Appointment1", "没有找到相关记录");
                String Appointment2 = sp2.getString("Appointment2", "没有找到相关记录");
                System.out.println("S------->>>" + "此刻的预约信息:Appointment0" + Appointment0 + ",Appointment1" + Appointment1 + ",Appointment2" + Appointment2);
                return;
            }
        } catch (JSONException e) {
            System.out.println("S------->>>" + "=======+++++++++++" + e.toString());
            e.printStackTrace();
        }

        if (isOpen) {  //扫码之后,服务器返回的信息,我们的机子进行判断,是否开机
            theApp.setLogin(isOpen);
            theApp.responseE0();//查询模块位置状态 命令（E0H），可作为握手用。
            //提前5分钟定时器代码
            Date date = new Date();
            if (endTime - date.getTime() >= 2 * 60 * 1000) {
                fiveMinuteTimer = new Timer();
                fiveMinuteTimer.schedule(new fiveMinuteTask(openId,endTime), new Date(endTime - 2 * 60 * 1000));
                System.out.println("S------->>>" + "=======+++++++++++时间多于5分钟");
            } else {
                System.out.println("S------->>>" + "=======+++++++++++时间少于于5分钟");
            }
            //endtime定时器
            if (isRenew == "Frist" | hasRenew) {//2种情况:1.首次扫码执行;2.续费后执行
                timeoutTimer = new Timer();
                timeoutTimer.schedule(timeoutTask, new Date(endTime));
            }
        } else {
            //提示 msg信息
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("尊敬的客户: " + msg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow()
                    .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }
    }

    TimerTask timeoutTask = new TimerTask() {
        @Override
        public void run() {

            mBdurationTime = spp.getString("mBdurationTime", "");
            hasRenew = spp.getBoolean("hasRenew",
                    mBdurationTime == ""
                            ? false
                            : true);
            System.out.println("S------->>>" + "durationTime" + mBdurationTime + "--mEndTime" + mEndTime + "--hasRenew" + hasRenew);

            //发送该次跑步数据
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String mm = theApp.baseUrl + theApp.sendDataUrl + "?appointId=" + appointId + "&reNewId=" + reNewId + "&openid=" + openId + "&speed=" + theApp.getSpeed() + "&distance=" + theApp.getDistance() + "&heartRate=" + theApp.getPulse() + "&calorie=" + theApp.getCal();

            //到点发送预约或者续约信息
            Map map = new HashMap();
            if ("appointInfo".equals(title)) {//判断如果是预约的发送预约号,续约的就发送续约
                map.put("appointId", appointId);
                System.out.println("S------->>>" + "预约信息,系统时间:" + getsystemTime() + mm);
            }else if("renewInfo".equals(title)){
                map.put("reNewId", reNewId);
                System.out.println("S------->>>" + "续约信息,系统时间:" + getsystemTime() + mm);
            }
            map.put("openid", openId);
            map.put("speed", theApp.getSpeed());
            map.put("distance", theApp.getDistance());
            map.put("heartRate", theApp.getPulse());
            map.put("calorie", theApp.getCal());
            String json = JSON.toJSONString(map);
            OkhttpTool.doget(theApp.baseUrl + theApp.sendDataUrl + "?info=" + json,
                    new MyCallback(new StringPaser() {
                    }));

            if (!hasRenew) {//没有续约
                System.out.println("S------->>>" + "到点了---停机停机");
                //查询终端机器情况
                editor.clear().commit();
                if (editor2 != null) {
                    editor2.clear().commit();
                }
                i = 1;
                //到点停机
                theApp.setLogin(false);
                theApp.responseE0();
                //theApp.responseKey(0, 0, 90);//应答 E2
                theApp.responseE8();
                theApp.responseE8();
                theApp.responseE8();
                theApp.responseE8();
                theApp.responseE8();
                isOpen = false;

                Looper.prepare();
                Toast.makeText(mContext, "到点了,要关机了.....", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            hasRenew = false;
            //续约一次后相关参数保留在spp中,如果下次不续约后,执行到终点代码里,照样可以从spp中读取到保留的参数值,
            editor = spp.edit();
            editor.putBoolean("hasRenew", false);
            editor.putLong("mEndTime", 0);
            editor.putString("mBdurationTime", "");
            editor.commit();
        }
    };

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {}
    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {}

    public class fiveMinuteTask extends TimerTask {
        String openid;
        Long endTime;

        public fiveMinuteTask(String openId,Long endTime) {
            this.openid = openId;
            this.endTime = endTime;
        }

        @Override
        public void run() {
            System.out.println("S------->>>" + "5分钟到了" + "=系统时间+" + getsystemTime());

            machineId = sp.getString("id", "");
            System.out.println("S------->>>canBook" + theApp.baseUrl + theApp.canBookUrl + "?openId=" + openid + "&machineId=" + machineId);
            //请求是否可以预约
            OkhttpTool.doget(theApp.baseUrl + theApp.canBookUrl + "?openId=" + openid + "&machineId=" + machineId,
                    new MyCallback(new StringPaser()) {
                        @Override
                        public void onSuccess(String string) {
                            System.out.println("S------->>>" + "onSuccess");
                            try {
                                JSONObject jsonObject = new JSONObject(string);
                                String canBook = jsonObject.getString("canBook");
                                System.out.println("S------->>>" + "是否可以续约" + canBook);
                                canBook = "true";
                                if ("true".equals(canBook)) {
                                    //如果可以续费,弹出续费框
                                    MainActivity.checkRenew(openid, machineId, mContext, endTime);//, endTime
                                }
                            } catch (Exception e) {
                                System.out.println("S------->>>" + "Exception" + e.toString());
                            }
                        }

                        @Override
                        public void onFailed() {
                            super.onFailed();
                            System.out.println("S------->>>" + "onFailed");
                        }
                    });


        }
    }

    public String getsystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}