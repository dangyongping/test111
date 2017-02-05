package com.chinafeisite.tianbu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.chinafeisite.tianbu.okhttpUtils.CrashHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.zip.CRC32;

import androidex.serialport.SerialPort;
import io.vov.vitamio.widget.VideoView;

//import java.io.DataOutputStream;
//import java.lang.reflect.Method;
//import android.widget.Toast;
//import com.chinafeisite.tianbu.CH340AndroidDriver;

  
/**
 * 应用程序类
 */
public class
theApp extends Application
{
	private static final String TAG = "theApp";

	static  Long currentTime = 0L;
	private ActivityManager mAm;
	private static List<RunningTaskInfo> mRti;

	public  static Long getCurrentTime(){return currentTime;}
	public static  void setCurrentTime(long LL){
		currentTime = LL;
	}

	//当前进入的第三方app
	private static String thirdApp;
	public static void setThirdAPP(String XX){thirdApp = XX;}
	public static String getThirdAPP(){return thirdApp;}

	// 是否预约时间是否结束
	private static boolean timeout = false;
	public static boolean getTimeout() { return timeout; }
	public static void setTimeout(final boolean n) { ms_nLogin = n; }

	//CH340驱动类
//	public static CH340AndroidDriver uartInterface_340;
	
	//CH341驱动类
	public static CH340AndroidDriver uartInterface_341;	
	
	//是否启动滑动
//	public static final boolean SCROLL = true;
	public static final boolean SCROLL = true;

	//----------------------------------------------------------------------------------------------------
	//是否是DEmo
//	public static final boolean DEMO = true;
	public static final boolean DEMO = false;
	
	// 调试
//	public static final boolean DEBUG = true;
	public static final boolean DEBUG = false;
	
	// 是否启用串口调试
//	public static final boolean SDEBUG = true;
	public static final boolean SDEBUG = false;
	
	// 是否启用文本调试
//	public static final boolean TDEBUG = true;
	public static final boolean TDEBUG = false;
		
	//是否使用回收
//	public static final boolean RECYCLE_BMP = true;
	public static final boolean RECYCLE_BMP = false;
	
	//是否使用回收
//	public static final boolean RECYCLE_BOTTOM = true;
	public static final boolean RECYCLE_BOTTOM = false;
	
	//是否使用回收
//	public static final boolean RECYCLE_TOP= true;
	public static final boolean RECYCLE_TOP = false;
	
	//是否使用回收
//	public static final boolean RECYCLE_MID= true;
	public static final boolean RECYCLE_MID = false;
	
	// 是否缩放图片
//	public static final boolean SCALED_BMP = true;
	public static final boolean SCALED_BMP = false;

	// 是否缩放坐标
//	public static final boolean SCALED_XY = true;
	public static final boolean SCALED_XY = false;
	
	// 是否使用zip压缩文件
	public static final boolean USE_ZIP = true;
//	public static final boolean USE_ZIP = false;

	// 是否使用视图页指示器
	public static final boolean USE_VPI = true;
//	public static final boolean USE_VPI = false;
	
	// 是否启用自定义 WiFi
//	public static final boolean USE_WIFI = true;
	public static final boolean USE_WIFI = false;
	
	//是否使用wifi检测
	public static final boolean WIFI_DETEC = true;
//	public static final boolean WIFI_DETEC = false;
	
	//是否使用扫码登录
//	public static final boolean QRLOGIN = true;
	public static final boolean QRLOGIN = false;
	
	//是否开启首页进wifi设置
	public static final boolean QRLOCK= true;
	//public static final boolean QRLOCK = false;
	
	//客户定义
//	public static final boolean CUSTOMER_DHZ = true;
	public static final boolean CUSTOMER_DHZ = false;
	public static final boolean CUSTOMER_TZ = true;
//	public static final boolean CUSTOMER_TZ = false;
//	public static final boolean CUSTOMER_SH = true;
	public static final boolean CUSTOMER_SH = false;
	
	// 是否启用悬浮窗单个开关
	public static final boolean FLOAT_SWITCH = true;
//	public static final boolean FLOAT_SWITCH = false;
		
	// 是否启用视频加速
//	public static final boolean USE_VIDEOADJUST = true;
	public static final boolean USE_VIDEOADJUST = false;
	
	// 是否启用TB服务器
	public static final boolean USE_TBSEVER = true;
//	public static final boolean USE_TBSEVER = false;
		
	// 是否启用万年青
//	public static final boolean WNQ= true;
	public static final boolean WNQ = false;	

	// 是否启用USB转串口
//	public static final boolean CH340= true;
	public static final boolean CH340 = false;

	// 是否启用RK3188转串口
	public static final boolean RK3188= true;
//	public static final boolean RK3188 = false;	
	
	// 是否启用悬浮窗背景
//	public static final boolean USE_FLOAT_BACK= true;
	public static final boolean USE_FLOAT_BACK = false;	

	// 是否启用系统播放器
	public static final boolean  VPLAYER= true;
//	public static final boolean VPLAYER = false;	
	
	// 是否启用系统设置
//	public static final boolean  SETING= true;
//	public static final boolean SETING = false;

	// 是否生成结果二维码
	public static final boolean RSLUTQR = true;
//	public static final boolean RSLUTQR = false;
	
	// 是否使用 VITAMIO
//	public static final boolean VITAMIO = true;
	public static final boolean VITAMIO = false;

	// 是否使用卡存储视频
	// public static final boolean MOVIE_IN_CARD =true;
		public static final boolean MOVIE_IN_CARD =false;
	
	//是否有顶部悬浮窗
//	public static final boolean NOTOP = true;
	public static final boolean NOTOP = false;
	//----------------------------------------------------------------------------------------------------
    //CH340权限
	public static final String ACTION_USB_PERMISSION = "com.chinafeisite.tianbu.USB_PERMISSION";
	
	//是否有底部悬浮窗
//	public static final boolean NOBOTTOM = true;
	public static final boolean NOBOTTOM = false;
	//----------------------------------------------------------------------------------------------------
	// 绘制标记
	public static final int MARK_JPG  =  1; // JPG
	public static final int MARK_OSD1 =  2; // OSD1
	public static final int MARK_OSD2 =  4; // OSD2
	public static final int MARK_OSD3 =  8; // OSD3
	public static final int MARK_OSD4 =  12; // OSD4
//	public static final int MARK_OSD  = 14; // OSD
//	public static final int MARK_ALL  = 15; // 全部
	
	// 多媒体标记
	public static final int MARK_NONE  = 0; // 无
	public static final int MARK_MUSIC = 1; // 音乐
	public static final int MARK_VIDEO = 2; // 视频
	public static final int MARK_PICTU = 3; // 图片
	public static final int MARK_LIST  = 5; // 列表
	public static final int MARK_PLAY  = 6; // 播放
	
	//图片数量
//	public static final int PIC_NUM  = 276; //AD185
//	public static final int PIC_NUM  = 334; //MF
//	public static final int PIC_NUM  = 325; //ASJ185
//	public static final int PIC_NUM  = 292; //BDL9600
//	public static final int PIC_NUM  = 433; //BDL9600加二维码
//	public static final int PIC_NUM  = 434; //BDL9600-扫码登录
//	public static final int PIC_NUM  = 61; //BDL力量
//	public static final int PIC_NUM  = 349; //BDL906
//	public static final int PIC_NUM  = 275; //BLTW-4核
//	public static final int PIC_NUM  = 343; //HX156
//	public static final int PIC_NUM  = 177; //HW
//	public static final int PIC_NUM  = 177; //156通用
//	public static final int PIC_NUM  = 353; //DHZ185带TV
//	public static final int PIC_NUM  = 385; //DHZ185通用
//	public static final int PIC_NUM  = 251; //DHZ9100
//	public static final int PIC_NUM  = 463; //顶康21.5
//	public static final int PIC_NUM  = 478; //正兴V8
//	public static final int PIC_NUM  = 634; //正兴V12
//	public static final int PIC_NUM  = 765; //正兴V8-四核
//	public static final int PIC_NUM  = 258; //正兴520T
//	public static final int PIC_NUM  = 333; //MBH
//	public static final int PIC_NUM  = 394; //MBH-9900-扫码
	public static final int PIC_NUM  = 397; //MBH-LE2000
//	public static final int PIC_NUM  = 334; //MBH-服务器
//	public static final int PIC_NUM  = 334; //MBH-MS
//	public static final int PIC_NUM  = 155; //MBH-5812
//	public static final int PIC_NUM  = 268; //MBH-力量
//	public static final int PIC_NUM  = 185; //贝爱卫浴
//	public static final int PIC_NUM  = 263; //宝德龙8800
//	public static final int PIC_NUM  = 293; //wnq156
//	public static final int PIC_NUM  = 341; //HX-I5
//	public static final int PIC_NUM  = 211; //LD215
//	public static final int PIC_NUM  = 205; //LD215-ch-ru
//	public static final int PIC_NUM  = 265; //力道185
//	public static final int PIC_NUM  = 423; //铭扬156
//	public static final int PIC_NUM  = 324; //MBH-JW
//	public static final int PIC_NUM  = 392; //LD
//	public static final int PIC_NUM  = 394; //LD服务器
//	public static final int PIC_NUM  = 583; //SH116-CH
//	public static final int PIC_NUM  = 548; //SH116-EN
//	public static final int PIC_NUM  = 222; //BDL6808
//	public static final int PIC_NUM  = 358; //BDL实体机
//	public static final int PIC_NUM  = 326; //BDL806
//	public static final int PIC_NUM  = 265; //JX185
//	public static final int PIC_NUM  = 449; //JG185
//	public static final int PIC_NUM  = 264; //ZD116
//	public static final int PIC_NUM  = 278; //ZD185
//	public static final int PIC_NUM  = 221; //CJ-185
//	public static final int PIC_NUM  = 247; //CJ-116	
//	public static final int PIC_NUM  = 375; //ZLK-185
//	public static final int PIC_NUM  = 471; //ZLK-185
//	public static final int PIC_NUM  = 451; //TR-185
//	public static final int PIC_NUM  = 451; //BLH-185
//	public static final int PIC_NUM  = 231; //TZ-101
//	public static final int PIC_NUM  = 365; //TZ-185-4核
//	public static final int PIC_NUM  = 364; //TZ-185-菲斯特变频器
//	public static final int PIC_NUM  = 303; //TZ-215
//	public static final int PIC_NUM  = 367; //TZ-215-微信登录
//	public static final int PIC_NUM  = 432; //BDL-NEW-185菲斯特变频器
//	public static final int PIC_NUM  = 410; //MBH-NEW-185 菲斯特变频器
//	public static final int PIC_NUM  = 432; //BDL-NEW-156菲斯特变频器
//	public static final int PIC_NUM  = 376; //诺德健185	
//	public static final int PIC_NUM  = 345; //汇祥心电图
//	public static final int PIC_NUM  = 181; //华尔斯
	
	// 缓冲区
	private static int ms_nBytes = 0;                        // 缓冲区字节数
	public static final int BUFFER_SIZE = 1024;              // 缓冲区大小
	private static byte[] ms_buffer = new byte[BUFFER_SIZE]; // 缓冲区

	// 指令常量
	public static final int HEAD_LEN = 2; // 头部长度
	public static final int TAIL_LEN = 2; // 尾部长度
	public static final int COMM_BYTE_NUM = 25; // 指令的字节数
	public static final int RESP_BYTE_NUM = 16; // 响应的字节数
	public static final int RESP_BUFF_NUM = 11; // 响应的缓冲区字节数
	public static final int[] HEAD = { 0xAA, 0x55 }; // 头部指令
	public static final int[] TAIL = { 0xC3, 0x3C }; // 尾部指令
	public static final byte[] HEAD_BYTE = { (byte) 0xAA, (byte) 0x55 }; // 头部指令字节
	public static final byte[] TAIL_BYTE = { (byte) 0xC3, (byte) 0x3C }; // 尾部指令字节
	private static int old_HRy=0;
	private static final int[] HRy =new int[9];
	private static int HR_num=0;
	
	// 角度转弧度的系数
	final static double PIDiv180 = 0.017453292519943295769236907684886;
	//--------------------------------力量器械运动结果--------------------------------------------------------  
	// 运动时间
	private static String ms_nPowerTime = "";
	private String sBaseUrl2;


	public static String getPowerTime() { return ms_nPowerTime; }
	public static void setPowerTime(final String str) { ms_nPowerTime = str; }
	
	// 平均高度
	private static String ms_nPowerHeight = "";
	public static String getPowerHeight() { return ms_nPowerHeight; }
	public static void setPowerHeight(final String str) { ms_nPowerHeight = str; }

	// 平均重量
	private static String ms_nPowerWeight = "";
	public static String getPowerWeight() { return ms_nPowerWeight; }
	public static void setPowerWeight(final String str) { ms_nPowerWeight = str; }

	// 爆发力
	private static String ms_nPower = "";
	public static String getPower() { return ms_nPower; }
	public static void setPower(final String str) { ms_nPower = str; }

	// 平均功率
	private static String ms_nAvgPower = "";
	public static String getAvgPower() { return ms_nAvgPower; }
	public static void setAvgPower(final String str) { ms_nAvgPower = str; }

	// 频次
	private static String ms_nFrq = "";
	public static String getFrq() { return ms_nFrq; }
	public static void setFrq(final String str) { ms_nFrq = str; }

	// 次数
	private static String ms_nNumber = "";
	public static String getNumber() { return ms_nNumber; }
	public static void setNumber(final String str) { ms_nNumber = str; }
			
	//----------------------------------------------------------------------------------------------------
	
	// 运动时间
	private static String ms_nRunTime = "";
	public static String getRunTime() { return ms_nRunTime; }
	public static void setRunTime(final String str) { ms_nRunTime = str; }
	
	// 使用时间
	private static String ms_nUseTime = "";
	public static String getUseTime() { return ms_nUseTime; }
	public static void setUseTime(final String str) { ms_nUseTime = str; }
	
	// 页面停留时间
	private static String ms_nPageTime = "";
	public static String getPageTime() { return ms_nPageTime; }
	public static void setPageTime(final String str) { ms_nPageTime = str; }
		
	// 运动距离
	private static String ms_nDistance = "";
	public static String getDistance() { return ms_nDistance; }
	public static void setDistance(final String str) { ms_nDistance = str; }
	
	// 速度
	private static String ms_nSpeed = "";
	public static String getSpeed() { return ms_nSpeed; }
	public static void setSpeed(final String str) { ms_nSpeed = str; }
	
	// 步数
	private static String ms_nPace = "";
	public static String getPace() { return ms_nPace; }
	public static void setPace(final String str) { ms_nPace = str; }
	
	// 心率
	private static String ms_nPulse = "";
	public static String getPulse() { return ms_nPulse; }
	public static void setPulse(final String str) { ms_nPulse = str; }
	
	// 卡路里
	private static String ms_nCal = "";
	public static String getCal() { return ms_nCal; }
	public static void setCal(final String str) { ms_nCal = str; }		
		
	// 开关机次数
	private static String ms_nTimes = "";
	public static String getTimes() { return ms_nTimes; }
	public static void setTimes(final String str) { ms_nTimes = str; }

	// 开关机状态
	private static String ms_nStatus = "";
	public static String getStatus() { return ms_nStatus; }
	public static void setStatus(final String str) { ms_nStatus = str; }
	
	// 51版本号
	private static String ms_nVersion = "";
	public static String getVersion() { return ms_nVersion; }
	public static void setVersion(final String str) { ms_nVersion = str; }
	
	// 获取mac地址
	private static String ms_nMacAdress = null;
	public static String getMacAdress() { return ms_nMacAdress; }
	public static void setMacAdress(final String str) { ms_nMacAdress = str; }
		
	// 设定运行状态
	private static boolean ms_nRun = true;
	public static boolean getRunstatus() { return ms_nRun; }
	public static void setRunstatus(final boolean n) { ms_nRun = n; }

	// 设定CH341状态
	private static boolean ms_nReceive = true;
	public static boolean getReceivestatus() { return ms_nReceive; }
	public static void setReceivestatus(final boolean n) { ms_nReceive = n; }

	// 设定DA视频初始化状态
	private static boolean ms_nDaInit = false;
	public static boolean getDaInitstatus() { return ms_nDaInit; }
	public static void setDaInitstatus(final boolean n) { ms_nDaInit = n; }
	
	// 设定显示TOAST状态
	private static boolean ms_nToast = false;
	public static boolean getToast() { return ms_nToast; }
	public static void setToast(final boolean n) { ms_nToast = n; }

	// 是否允许登录
	private static boolean ms_nLogin = false;
	public static boolean getLogin() { return ms_nLogin; }
	public static void setLogin(final boolean n) { ms_nLogin = n; }

	// 是否显示二维码
	private static boolean ms_nQrcode= false;
	public static boolean getQrcode() { return ms_nQrcode; }
	public static void setQrcode(final boolean n) { ms_nQrcode = n; }
	
	// 是否上传运动结果
	private static int ms_nLoginRsult = 0;
	public static int getLoginRsult() { return ms_nLoginRsult; }
	public static void setLoginRsult(final int n) { ms_nLoginRsult = n; }

	// 是否初始化CH340
	private static boolean ms_nCH340= false;
	public static boolean getCH340() { return ms_nCH340; }
	public static void setCH340(final boolean n) { ms_nCH340 = n; }
		
	// 关机次数
	private static int ms_nPowerTimes = 0;
	public static int getPowerTimes() { return ms_nPowerTimes; }
	public static void setPowerTimes(final int n) { ms_nPowerTimes = n; }		

	//请求次数
	private static int ms_nResponse = 0;
	public static int getResponse() { return ms_nResponse; }
	public static void setResponse(final int n) { ms_nResponse = n; }	
		
	// 故障次数
	private static String ms_nErrTimes = "";
	public static String getErrTimes() { return ms_nErrTimes; }
	public static void setErrTimes(final String str) { ms_nErrTimes = str; }

	// 故障代码
	private static String ms_nErr = "";
	public static String getErr() { return ms_nErr; }
	public static void setErr(final String str) { ms_nErr = str; }
	
	// 闲置时间
	private static String ms_nIdleTime = "";
	public static String getIdleTime() { return ms_nIdleTime; }
	public static void setIdleTime(final String str) { ms_nIdleTime = str; }
	
	// 生成随机key
	private static String ms_nKey = "1";
	public static String getKey() { return ms_nKey; }
	public static void setKey(final String key) { ms_nKey = key; }
	
	// 登录解锁状态, 0: 不设置, 1: 设置为锁定状态
	private static int ms_nLock = 0;
	public static int getLock() { return ms_nLock; }
	public static void setLock(final int n) { ms_nLock = n; }
		
	//----------------------------------------------------------------------------------------------------
	// 网站地址
	private static String ms_strUrl = "http://hyu1931060001.my3w.com/index.php?";//"www.ttpaobu.com/";
//	private static String ms_strUrl = "http://192.168.0.168/";
	public static String getUrl() { return ms_strUrl; }
	
	// 登陆的帐号信息
	private static AccountInfo ms_acountInfo = null;
	public static AccountInfo getAccInfo() { return ms_acountInfo; }
	public static void setAccInfo(final AccountInfo ai) { ms_acountInfo = ai; }
	
	// 用户名
	private static String ms_strAccount = "";
	public static String getAccount() { return ms_strAccount; }
	public static void setAccount(final String str) { ms_strAccount = str; }
	
	// 密码
	public static String ms_strPassword = "";
	public static String getPassword() { return ms_strPassword; }
	public static void setPassword(final String str) { ms_strPassword = str; }


	// 帐号类型
	private static int ms_nAccType = 0;
	public static int getAccType() { return ms_nAccType; }
	public static void setAccType(final int n) { ms_nAccType = n; }
	
	// 数据数量
	private static int ms_nDataSize = 0;
	public static int getDataSize() { return ms_nDataSize; }
	public static void setDataSize(final int n) { ms_nDataSize = n; }
	
	// 旋转角度
	private static float ms_nAngle = 0;
	public static float getAngle() { return ms_nAngle; }
	public static void setAngle (final float n) { ms_nAngle = n; }

	// 旋转画布大小
	private static float ms_nHeight = 0;
	public static float getHeight() { return ms_nHeight; }
	public static void setHeight (final float n) { ms_nHeight = n; }
	
	/***** add 图片比较  2014-08-18 *****/
	// 图片数量
	private static int ms_nPicNum = 0;
	public static int getPicNum() { return ms_nPicNum; }
	public static void setPicNum (final int n) { ms_nPicNum = n; }
	
	// 比较时间
	private static int ms_nCheckTime = 0;
	public static int getCheckTime() { return ms_nCheckTime; }
	public static void setCheckTime(final int n) { ms_nCheckTime = n; }
	
	// 图片计数
	private static int ms_nCounter = 0;
	public static int getCounter() { return ms_nCounter; }
	public static void setCounter (final int n) { ms_nCounter = n; }
	
	// 是否开始比较图片
	private static boolean ms_bCheckNume = false;
	public static boolean isCheckNume() { return ms_bCheckNume; }
	public static void setCheckNume (final boolean b) { ms_bCheckNume = b; }
	
	// 是否在拷贝图片
	private static boolean ms_bCopyPicture = false;
	public static boolean isCopyPicture() { return ms_bCopyPicture; }
	public static void setCopyPicture (final boolean b) { ms_bCopyPicture = b; }
		
	// 存储图片数量
	private static int ms_nStoragePicNum = 0;
	public static int getStorageNum() { return ms_nStoragePicNum; }
	public static void setStoragePicNum (final int n) { ms_nStoragePicNum = n; }
	
	// 打开ch340设备
//	public static CH340AndroidDriver uartInterface_340;
//	private static CH340AndroidDriver uartInterface_340;
//	public static CH340AndroidDriver getCH340() { return uartInterface_340; }
//	public static void setCH340 (final CH340AndroidDriver n) { uartInterface_340 = n; }	
	
	// 检测网络计数
	private static int ms_niCounter = 0;
	public static int getiCounter() { return ms_niCounter; }
	public static void setiCounter (final int n) { ms_niCounter = n; }	
	/********************************/	
	
	// 是否已登陆服务器
	private static boolean ms_bLogined = false;
	public static boolean isLogined() { return ms_bLogined; }
	public static void setLogined(final boolean b) { ms_bLogined = b; }

	// 是否清除动画图片
	private static boolean ms_bClearAction = false;
	
	// 是否清除动画图片
	private static boolean ms_bOsd4 = false;
	
	//BMP 哈希表
	public static HashMap<String , Bitmap> ms_maps = new HashMap<String, Bitmap>();
	
	// User 链表
	private static List<String> ms_listUserL = new ArrayList<String>(); // 本地
	private static List<String> ms_listUserS = new ArrayList<String>(); // 服务器
	private static List<UserData> ms_listUserDataL = new ArrayList<UserData>(); // 本地
	private static List<UserData> ms_listUserDataS = new ArrayList<UserData>(); // 服务器
	public static List<String> getListUserL() { return ms_listUserL; }
	public static List<String> getListUserS() { return ms_listUserS; }
	public static List<UserData> getListUserDataL() { return ms_listUserDataL; }
	public static List<UserData> getListUserDataS() { return ms_listUserDataS; }
	
	// 共享属性
	private static SharedPreferences ms_pref = null;
	public static void setPref(SharedPreferences sp) { ms_pref = sp; }
	
	// 存储文件个数
	private static SharedPreferences ms_pref_picnum = null;
	public static void setPrefPicNum(SharedPreferences sp) { ms_pref_picnum = sp; }
	
	// 服务器状态
	private static SharedPreferences ms_pref_status = null;
	public static void setPrefStatus(SharedPreferences sp) { ms_pref_status = sp; }	

	// 属性文件中的键
	private static final String PREF_KEY_UNITS    = "Units";   // 公英制单位
	private static final String PREF_KEY_BT_STATE = "BtState"; // 蓝牙开关状态
	private static final String PREF_KEY_MUSIC_PM = "MusicPM"; // 音乐播放模式
	private static final String PREF_KEY_PICNUM   = "PicNum";   // 图片数量
	private static final String PREF_KEY_STATUS  = "Status";   // 服务器状态
	private static final String PREF_KEY_POWERTIMES  = "PowerTimes";   // 关机次数
	private static final String PREF_KEY_QRCODE  = "QrCode";   // 二维码状态
	private static final String PREF_KEY_MAC  = "MACADRESS";   // MAC地址
	//----------------------------------------------------------------------------------------------------
	// 移动差值
	public static final int MOVE_DELTA = 3;
	
	// 帐号类型数量
	public static final int ACC_TYPE_NUM = 9;
	
	// 按下的坐标
	private static float ms_fDownX = 0;
	private static float ms_fDownY = 0;
	public static float getDownX() { return ms_fDownX; }
	public static float getDownY() { return ms_fDownY; }
	public static void setDownX(final float x) { ms_fDownX = x; }
	public static void setDownY(final float y) { ms_fDownY = y; }
	
	// 移动时间
	private static int ms_nMoveTime = 0;
	public static int getMoveTime() { return ms_nMoveTime; }
	public static void setMoveTime(final int n) { ms_nMoveTime = n; }
	
	// 等待时间
	private static int ms_nWaiteTime = 0;
	public static int getWaiteTime() { return ms_nWaiteTime; }
	public static void setWaiteTime(final int n) { ms_nWaiteTime = n; }
			
	// 是否正在移动
	private static boolean ms_bMoving = false;
	public static boolean isMoving() { return ms_bMoving; }
	public static void setMoving(final boolean b) { ms_bMoving = b; }
	
	// 是否响应移动
	private static boolean ms_bResponseMove = false;
	public static boolean isResponseMove() { return ms_bResponseMove; }
	public static void setResponseMove(final boolean b) { ms_bResponseMove = b; }

	// 触摸时间
	/*private static int ms_nTouchTime = 0;
	public static int getTouchTime() { return ms_nTouchTime; }
	public static void setTouchTime(final int n) { ms_nTouchTime = n; }

	// 是否正在触摸
	private static boolean ms_bTouch = false;
	public static boolean isTouch() { return ms_bTouch; }
	public static void setTouch(final boolean b) { ms_bTouch = b; }

	
	// 是否响应触摸
	private static boolean ms_bResponseTouch = true;
	public static boolean isResponseTouch() { return ms_bResponseTouch; }
	public static void setResponseTouch(final boolean b) { ms_bResponseTouch = b; }*/
	
	//----------------------------------------------------------------------------------------------------
	// 是否显示系统设置
	private static boolean ms_bShowSApk = false;
	public static boolean isShowSApk() { return ms_bShowSApk; }
	public static void setShowSApk(final boolean b) { ms_bShowSApk = b; }
	
	// 是否进行语言设置
	private static boolean ms_bSetLan = false;
	public static boolean isSetLan() { return ms_bSetLan; }
	public static void SetLan(final boolean b) { ms_bSetLan = b; }
	
	// 是否已初始化
	private static boolean ms_bInit = false;
	public static boolean isInit() { return ms_bInit; }
	public static void setInit(final boolean b) { ms_bInit = b; }
	
	// 是否初始化CH340
	private static boolean ms_bInitCh340 = false;
	public static boolean isInitCh340() { return ms_bInitCh340; }
	public static void setInitCh340(final boolean b) { ms_bInitCh340 = b; }
		
	// 是否回E0
	private static boolean ms_bE0 = false;
	public static boolean isE0() { return ms_bE0; }
	public static void setE0(final boolean b) { ms_bE0 = b; }
		
	// 是否已创建
	private static boolean ms_bCreated = false;
	public static boolean isCreated() { return ms_bCreated; }
	
	// 是否已退出
	private static boolean ms_bExit = false;
	public static boolean isExit() { return ms_bExit; }
	public static void setExit(final boolean b) { ms_bExit = b; }
	
	// 是否已启动 App
	private static boolean ms_bStartApp = false;
	public static boolean isStartApp() { return ms_bStartApp; }
	public static void setStartApp(final boolean b) { ms_bStartApp = b; }
	
	// 是否开启音效
	private static boolean ms_bVolume = true;
	public static boolean isVolume() { return ms_bVolume; }
	public static void setVolume(final boolean b) { ms_bVolume = b; }

	// 是否开启悬浮窗
	private static boolean ms_bFloatOn = false;
	public static boolean isFloatOn() { return ms_bFloatOn; }
	public static void setFloatOn(final boolean b) { ms_bFloatOn = b; }

	// 是否开启wifi
	private static boolean ms_bWifi = false;
	public static boolean isWifiOn() { return ms_bWifi; }
	public static void setWifiOn(final boolean b) { ms_bWifi = b; }

	// 是否关机
	private static boolean ms_bShutdown = false;
	public static boolean isShutdown() { return ms_bShutdown; }
	public static void setShutdown(final boolean b) { ms_bShutdown = b; }
		
	// 是否清除浏览器
	private static boolean ms_bClearBrowse = false;
	public static boolean isClear() { return ms_bClearBrowse; }
	public static void setClear(final boolean b) { ms_bClearBrowse = b; }
	
	//是否在浏览器状态
	private static boolean ms_bBrowseState = false;
	public static boolean isBrowse() { return ms_bBrowseState; }
	public static void setBrowse(final boolean b) { ms_bBrowseState = b; }
		
	// 是否开启顶部悬浮窗
	private static boolean ms_bFloatTopOn = false;
	public static boolean isFloatTopOn() { return ms_bFloatTopOn; }
	public static void setFloatTopOn(final boolean b) { ms_bFloatTopOn = b; }

	// 是否开启底部悬浮窗
	private static boolean ms_bFloatBottomOn = false;
	public static boolean isFloatBottomOn() { return ms_bFloatBottomOn; }
	public static void setFloatBottomOn(final boolean b) { ms_bFloatBottomOn = b; }
	
	// 是否已计算 JPG 的 CRC32
	private static boolean ms_bJpgCrc32 = false;
	public static boolean isJpgCrc32() { return ms_bJpgCrc32; }
	public static void setJpgCrc32(final boolean b) { ms_bJpgCrc32 = b; }
	
	// 是否已加载声音
	private static boolean ms_bLoadSound = false;
	public static boolean isLoadSound() { return ms_bLoadSound; }
	public static void setLoadSound(final boolean b) { ms_bLoadSound = b; }
	
	// 是否是磁控车
	private static boolean ms_bCkc = false;
	public static boolean isCkc() { return ms_bCkc; }
	public static void setCkc(final boolean b) { ms_bCkc = b; }
	
	// 是否已加载 Apps
	private static boolean ms_bLoadApps = false;
	public static boolean isLoadApps() { return ms_bLoadApps; }
	public static void setLoadApps(final boolean b) { ms_bLoadApps = b; }
	
	// 是否已加载
	private static boolean ms_bLoadMusic = false;
	private static boolean ms_bLoadVideo = false;
	private static boolean ms_bLoadPicture = false;
	public static boolean isLoadMusic() { return ms_bLoadMusic; }
	public static boolean isLoadVideo() { return ms_bLoadVideo; }
	public static boolean isLoadPicture() { return ms_bLoadPicture; }
	public static void setLoadMusic(final boolean b) { ms_bLoadMusic = b; }
	public static void setLoadVideo(final boolean b) { ms_bLoadVideo = b; }
	public static void setLoadPicture(final boolean b) { ms_bLoadPicture = b; }

	// 是否正在加载
	private static boolean ms_bLoadingMusic = false;
	private static boolean ms_bLoadingVideo = false;
	private static boolean ms_bLoadingPicture = false;
	public static boolean isLoadingMusic() { return ms_bLoadingMusic; }
	public static boolean isLoadingVideo() { return ms_bLoadingVideo; }
	public static boolean isLoadingPicture() { return ms_bLoadingPicture; }
	public static void setLoadingMusic(final boolean b) { ms_bLoadingMusic = b; }
	public static void setLoadingVideo(final boolean b) { ms_bLoadingVideo = b; }
	public static void setLoadingPicture(final boolean b) { ms_bLoadingPicture = b; }
	
	// 是否已初始化浏览器
	private static boolean ms_bInitBrowser = false;
	public static boolean isInitBrowser() { return ms_bInitBrowser; }
	public static void setInitBrowser(final boolean b) { ms_bInitBrowser = b; }
	
	// 是否为后台, 0: 前台, 1:后台
	private static int ms_nBackground = 0;
	public static int getBackground() { return ms_nBackground; }
	public static void setBackground(final int n) { ms_nBackground = n; }
	
	// 公英制单位, 0: 公制, 1: 英制
	private static int ms_nUnits = 0;
	public static int getUnits() { return ms_nUnits; }
	public static void setUnits(final int n) { ms_nUnits = n; }
	
	// 蓝牙开关状态, 0: 关闭, 1: 开启
	private static int ms_nBtState = 0;
	public static int getBtState() { return ms_nBtState; }
	public static void setBtState(final int n) { ms_nBtState = n; }
	
	// 是否已启动悬浮窗
	private static boolean ms_bStartFloat = false;
	public static boolean isStartFloat() { return ms_bStartFloat; }
	public static void setStartFloat(final boolean b) { ms_bStartFloat = b; /*if (b) responseE0();*/ }


	// 地图标记
	public static final int MARK_IN  = 0; // 开启地图
	public static final int MARK_OUT = 1; // 关闭地图

	private static int ms_nMapInMark = MARK_IN;
	private static int ms_nMapOutMark = MARK_OUT;


	private static int ms_nMapMark = MARK_OUT;
	public static int getMs_nMapMark() { return ms_nMapMark; }
	public static void setMs_nMapMark(final int n) { ms_nMapMark = n; }

	// 是否已进入AV
	private static boolean ms_bAvStatus = false;
	public static boolean getAvStatus() { return ms_bAvStatus; }
	public static void setAvStatus(final boolean b) { ms_bAvStatus = b; }
	
	// 多媒体标记
	private static int ms_nMediaMark = MARK_NONE;
	public static int getMediaMark() { return ms_nMediaMark; }
	public static void setMediaMark(final int n) { ms_nMediaMark = n; }
	
	// 多媒体标记
	private static int ms_nMusicMark = MARK_NONE;
	private static int ms_nVideoMark = MARK_NONE;
	private static int ms_nPictuMark = MARK_NONE;
	public static int getMusicMark() { return ms_nMusicMark; }
	public static int getVideoMark() { return ms_nVideoMark; }
	public static int getPictuMark() { return ms_nPictuMark; }
	public static void setMusicMark(final int n) { ms_nMusicMark = n; }
	public static void setVideoMark(final int n) { ms_nVideoMark = n; }
	public static void setPictuMark(final int n) { ms_nPictuMark = n; }

	// 视频当前位置
	private static int  ms_nVideoCurPos = 0;
	public static int getVideoCurPos() { return ms_nVideoCurPos; }
	public static void setVideoCurPos(final int n) { ms_nVideoCurPos = (int)n; }
	
	// 系统版本
	private static String ms_strSysVer = "1.0";
	public static String getSysVer() { return ms_strSysVer; }
	public static void setSysVer(final String str) { ms_strSysVer = str; }
	
	// 界面语言, 中文(简体)
	private static String ms_strLang = "ch";
	public static String getLang() { return ms_strLang; }
	public static void setLang(final String str) { ms_strLang = str; }
	
	// 屏幕宽度和高度
//	private static int ms_nWidthPixels = 1280;
//	private static int ms_nHeightPixels = 720;
	
	// 屏幕宽度和高度
//	private static int ms_nWidthPixels = 1280;
//	private static int ms_nHeightPixels = 800;	
	
	//RK3188 屏幕宽度和高度
//	private static int ms_nWidthPixels = 1366;
//	private static int ms_nHeightPixels = 768;

	//RK3188 屏幕宽度和高度
	private static int ms_nWidthPixels = 1920;
	private static int ms_nHeightPixels = 1080;
	
	public static int getWidthPixels() { return ms_nWidthPixels; }
	public static int getHeightPixels() { return ms_nHeightPixels; }
	public static void setWidthPixels(final int n) { ms_nWidthPixels = n; }
	public static void setHeightPixels(final int n) { ms_nHeightPixels = n; }
	
	// 媒体音量
	private static int ms_nStreamVolume = 0;
	public static int getStreamVolume() { return ms_nStreamVolume; }
	public static void setStreamVolume(final int n) { ms_nStreamVolume = n ; }
	
	// 屏幕亮度
	private static int ms_nScreenBrightness = 0;
	public static int getScreenBrightness() { return ms_nScreenBrightness; }
	public static void setScreenBrightness(final int n) { ms_nScreenBrightness = n; }
	
	// picture 文件夹路径
	private static String ms_strPicturePath = "";
	public static String getPicturePath() { return ms_strPicturePath; }
	public static void setPicturePath(final String str) { ms_strPicturePath = str; }
	
	// JPG 路径编号
	private static int ms_nJpgPathNum = 1;
	public static int getJpgPathNum() { return ms_nJpgPathNum; }
	public static void setJpgPathNum(final int n) { ms_nJpgPathNum = n; }
	
	// JPG 名称编号
	private static int ms_nJpgNameNum = 0;
	public static int getJpgNameNum() { return ms_nJpgNameNum; }
	public static void setJpgNameNum(final int n) { ms_nJpgNameNum = n; }
	
	// 宽度和高度比例
	private static float ms_fWidthRatio  = 1.0f;
	private static float ms_fHeightRatio = 1.0f;
	public static float getWidthRatio() { return ms_fWidthRatio; }
	public static float getHeightRatio() { return ms_fHeightRatio; }
	public static void setWidthRatio(final float f) { ms_fWidthRatio = f; }
	public static void setHeightRatio(final float f) { ms_fHeightRatio = f; }
	
	// 00.jpg 的宽度和高度
//	private static int ms_nW00Jpg = 1280;
//	private static int ms_nH00Jpg = 720;

	// 00.jpg 的宽度和高度
//	private static int ms_nW00Jpg = 1280;
//	private static int ms_nH00Jpg = 800;
	
	//RK3188 00.jpg 的宽度和高度
//	private static int ms_nW00Jpg = 1366;
//	private static int ms_nH00Jpg = 768;

	//RK3188 00.jpg 的宽度和高度
	private static int ms_nW00Jpg = 1920;
	private static int ms_nH00Jpg = 1080;
	
	public static int getW00Jpg() { return ms_nW00Jpg; }
	public static int getH00Jpg() { return ms_nH00Jpg; }
	public static void setW00Jpg(final int n) { ms_nW00Jpg = n; }
	public static void setH00Jpg(final int n) { ms_nH00Jpg = n; }

	// 视频坐标
	private static int ms_nVideoL = 1;
	private static int ms_nVideoT = 1;
	private static int ms_nVideoX1 = 0;
	private static int ms_nVideoY1 = 0;
//	private static int ms_nVideoX2 = 1280;
//	private static int ms_nVideoY2 = 720;

//	private static int ms_nVideoX2 = 1280;
//	private static int ms_nVideoY2 = 800;
	
	//RK3188
//	private static int ms_nVideoX2 = 1366;
//	private static int ms_nVideoY2 = 768;
	
	//RK3188
	private static int ms_nVideoX2 = 1920;
	private static int ms_nVideoY2 = 1080;
	
//	private static int ms_nVideoX1 = 0;
//	private static int ms_nVideoY1 = 0;
//	private static int ms_nVideoX2 = 0;
//	private static int ms_nVideoY2 = 0;
	
	public static int getVideoX1() { return ms_nVideoX1; }
	public static int getVideoY1() { return ms_nVideoY1; }
	public static int getVideoX2() { return ms_nVideoX2; }
	public static int getVideoY2() { return ms_nVideoY2; }
	public static void setVideoX1(final int n) { ms_nVideoX1 = n; }
	public static void setVideoY1(final int n) { ms_nVideoY1 = n; }
	public static void setVideoX2(final int n) { ms_nVideoX2 = n; }
	public static void setVideoY2(final int n) { ms_nVideoY2 = n; }
	
	//F3小图路径
//	private static String ms_strPath1 = "";
//	private static String ms_strPath2 = "";
//	private static String ms_strPath3 = "";
//	private static String ms_strPath4 = "";
//	private static String ms_strPath5 = "";
//	private static String ms_strPath6 = "";	
//	private static String ms_strPath7 = "";
//	private static String ms_strPath8 = "";
//	private static String ms_strPath9 = "";	
	
	//F1小图路径
//	private static String ms_strPathf1 = "";
//	private static String ms_strPathf2 = "";	
//	private static OsdBitmap ms_osd1 = null;
//	private static OsdBitmap ms_osd2 = null;
	
	// OSD 坐标
	private static int ms_nOsd11X = 0;
	private static int ms_nOsd11Y = 0;
	private static int ms_nOsd12X = 0;
	private static int ms_nOsd12Y = 0;
	private static int ms_nOsd21X = 0;
	private static int ms_nOsd21Y = 0;
	private static int ms_nOsd22X = 0;
	private static int ms_nOsd22Y = 0;
	private static int ms_nOsd23X = 0;
	private static int ms_nOsd23Y = 0;
	private static int ms_nOsd24X = 0;
	private static int ms_nOsd24Y = 0;
	private static int ms_nOsd25X = 0;
	private static int ms_nOsd25Y = 0;
	private static int ms_nOsd26X = 0;
	private static int ms_nOsd26Y = 0;
	private static int ms_nOsd27X = 0;
	private static int ms_nOsd27Y = 0;
	private static int ms_nOsd28X = 0;
	private static int ms_nOsd28Y = 0;
	private static int ms_nOsd29X = 0;
	private static int ms_nOsd29Y = 0;
	public static int getOsd11X() { return ms_nOsd11X; }
	public static int getOsd11Y() { return ms_nOsd11Y; }
	public static int getOsd12X() { return ms_nOsd12X; }
	public static int getOsd12Y() { return ms_nOsd12Y; }
	public static int getOsd21X() { return ms_nOsd21X; }
	public static int getOsd21Y() { return ms_nOsd21Y; }
	public static int getOsd22X() { return ms_nOsd22X; }
	public static int getOsd22Y() { return ms_nOsd22Y; }
	public static int getOsd23X() { return ms_nOsd23X; }
	public static int getOsd23Y() { return ms_nOsd23Y; }
	public static int getOsd24X() { return ms_nOsd24X; }
	public static int getOsd24Y() { return ms_nOsd24Y; }
	public static int getOsd25X() { return ms_nOsd25X; }
	public static int getOsd25Y() { return ms_nOsd25Y; }
	public static int getOsd26X() { return ms_nOsd26X; }
	public static int getOsd26Y() { return ms_nOsd26Y; }
	public static int getOsd27X() { return ms_nOsd27X; }
	public static int getOsd27Y() { return ms_nOsd27Y; }
	public static int getOsd28X() { return ms_nOsd28X; }
	public static int getOsd28Y() { return ms_nOsd28Y; }
	public static int getOsd29X() { return ms_nOsd29X; }
	public static int getOsd29Y() { return ms_nOsd29Y; }
	public static void setOsd11X(final int n) { ms_nOsd11X = n; }
	public static void setOsd11Y(final int n) { ms_nOsd11Y = n; }
	public static void setOsd12X(final int n) { ms_nOsd12X = n; }
	public static void setOsd12Y(final int n) { ms_nOsd12Y = n; }
	public static void setOsd21X(final int n) { ms_nOsd21X = n; }
	public static void setOsd21Y(final int n) { ms_nOsd21Y = n; }
	public static void setOsd22X(final int n) { ms_nOsd22X = n; }
	public static void setOsd22Y(final int n) { ms_nOsd22Y = n; }
	public static void setOsd23X(final int n) { ms_nOsd23X = n; }
	public static void setOsd23Y(final int n) { ms_nOsd23Y = n; }
	public static void setOsd24X(final int n) { ms_nOsd24X = n; }
	public static void setOsd24Y(final int n) { ms_nOsd24Y = n; }
	public static void setOsd25X(final int n) { ms_nOsd25X = n; }
	public static void setOsd25Y(final int n) { ms_nOsd25Y = n; }
	public static void setOsd26X(final int n) { ms_nOsd26X = n; }
	public static void setOsd26Y(final int n) { ms_nOsd26Y = n; }
	public static void setOsd27X(final int n) { ms_nOsd27X = n; }
	public static void setOsd27Y(final int n) { ms_nOsd27Y = n; }
	public static void setOsd28X(final int n) { ms_nOsd28X = n; }
	public static void setOsd28Y(final int n) { ms_nOsd28Y = n; }
	public static void setOsd29X(final int n) { ms_nOsd29X = n; }
	public static void setOsd29Y(final int n) { ms_nOsd29Y = n; }
	
	// 顶部悬浮窗高度, 左上角和右下角坐标
//	private static int ms_nTopH  = 94;
//	private static int ms_nTopX1 = 0;
//	private static int ms_nTopY1 = 0;
//	private static int ms_nTopX2 = 1280;

	
	//RK3188 1280-800-6808
//	private static int ms_nTopH  = 105;
//	private static int ms_nTopX1 = 0;
//	private static int ms_nTopY1 = 0;
//	private static int ms_nTopX2 = 1280;
	
	//RK3188 ZX-V8-1366*768
//	private static int ms_nTopH  = 81;
//	private static int ms_nTopX1 = 0;
//	private static int ms_nTopY1 = 0;
//	private static int ms_nTopX2 = 1366;
	
	//RK3188 BLTW -1366*768
//	private static int ms_nTopH  = 107;
//	private static int ms_nTopX1 = 0;
//	private static int ms_nTopY1 = 0;
//	private static int ms_nTopX2 = 1366;
	
	//RK3188 TZ185 -1366*768
//	private static int ms_nTopH  = 101;
//	private static int ms_nTopX1 = 0;
//	private static int ms_nTopY1 = 0;
//	private static int ms_nTopX2 = 1366;
	
	//RK3188-9900-ms-1920*1080
	private static int ms_nTopH  = 142;
	private static int ms_nTopX1 = 0;
	private static int ms_nTopY1 = 0;
	private static int ms_nTopX2 = 1920;
	
	private static int ms_nTopY2 = ms_nTopH + 1;
	public static int getTopH() { return ms_nTopH; }
	public static int getTopX1() { return ms_nTopX1; }
	public static int getTopY1() { return ms_nTopY1; }
	public static int getTopX2() { return ms_nTopX2; }
	public static int getTopY2() { return ms_nTopY2; }
	public static void setTopH(final int n) { ms_nTopH = n; }
	public static void setTopX1(final int n) { ms_nTopX1 = n; }
	public static void setTopY1(final int n) { ms_nTopY1 = n; }
	public static void setTopX2(final int n) { ms_nTopX2 = n; }
	public static void setTopY2(final int n) { ms_nTopY2 = n; }
	
	// 底部悬浮窗高度, 左上角和右下角坐标 xwx
	//舒华156
//	private static int ms_nBottomH  = 120;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 600 - 1;	
	
	//其它
//	private static int ms_nBottomH  = 74;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 646 - 1;	
//	private static int ms_nBottomX2 = 1280;
//	private static int ms_nBottomY2 = 720;

	//其它
//	private static int ms_nBottomH  = 74;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 646 - 1;	
//	private static int ms_nBottomX2 = 1280;
//	private static int ms_nBottomY2 = 800;

	//RK3188-1280*800-6808
//	private static int ms_nBottomH  = 82;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 718 - 1;	
//	private static int ms_nBottomX2 = 1280;
//	private static int ms_nBottomY2 = 800;
	
	//RK3188-MBH5812
//	private static int ms_nBottomH  = 108;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 660 - 1;	
//	private static int ms_nBottomX2 = 1366;
//	private static int ms_nBottomY2 = 768;
	
	//RK3188-ZX-V8
//	private static int ms_nBottomH  = 90;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 678 - 1;	
//	private static int ms_nBottomX2 = 1366;
//	private static int ms_nBottomY2 = 768;
	
	//RK3188-TZ185
//	private static int ms_nBottomH  = 79;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 689 - 1;
//	private static int ms_nBottomX2 = 1366;
//	private static int ms_nBottomY2 = 768;
	
	//RK3188 - DHZ9100
//	private static int ms_nBottomH  = 108;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 972 - 1;		
//	private static int ms_nBottomX2 = 1920;
//	private static int ms_nBottomY2 = 1080;

	//RK3188 - MBH9900
//	private static int ms_nBottomH  = 112;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 968 - 1;		
//	private static int ms_nBottomX2 = 1920;
//	private static int ms_nBottomY2 = 1080;
	
	//RK3188 - WNQ156
//	private static int ms_nBottomH  = 115;
//	private static int ms_nBottomX1 = 0;
//	private static int ms_nBottomY1 = 965 - 1;		
//	private static int ms_nBottomX2 = 1920;
//	private static int ms_nBottomY2 = 1080;

	//RK3188 - v12
	private static int ms_nBottomH  = 127;
	private static int ms_nBottomX1 = 0;
	private static int ms_nBottomY1 = 953 - 1;
	private static int ms_nBottomX2 = 1920;
	private static int ms_nBottomY2 = 1080;

	public static int getBottomH() { return ms_nBottomH; }
	public static int getBottomX1() { return ms_nBottomX1; }
	public static int getBottomY1() { return ms_nBottomY1; }
	public static int getBottomX2() { return ms_nBottomX2; }
	public static int getBottomY2() { return ms_nBottomY2; }
	public static void setBottomH(final int n) { ms_nBottomH = n; }
	public static void setBottomX1(final int n) { ms_nBottomX1 = n; }
	public static void setBottomY1(final int n) { ms_nBottomY1 = n; }
	public static void setBottomX2(final int n) { ms_nBottomX2 = n; }
	public static void setBottomY2(final int n) { ms_nBottomY2 = n; }
	
	// 开关悬浮窗左上角坐标
//	private static int ms_nSwitchX1 = 1280;
//	private static int ms_nSwitchY1 = 220;
	
	//RK3188
//	private static int ms_nSwitchX1 = 1366;
//	private static int ms_nSwitchY1 = 220;

	//RK3188
	private static int ms_nSwitchX1 = 1860;
	private static int ms_nSwitchY1 = 220;

	public static int getSwitchX1() { return ms_nSwitchX1; }
	public static int getSwitchY1() { return ms_nSwitchY1; }
	public static void setSwitchX1(final int n) { ms_nSwitchX1 = n; }
	public static void setSwitchY1(final int n) { ms_nSwitchY1 = n; }
	
	// 画笔
	private static Paint ms_paint = new Paint();
	public static Paint getPaint() { return ms_paint; }
	
	// 默认路径
	private static String ms_strDefPath = "";
	public static String getDefPath() { return ms_strDefPath; }
	public static void setDefPath(final String str) { ms_strDefPath = str; }
	
	// 默认路径
	private static String ms_moviestrDefPath = "";
	public static String getmovieDefPath() { return ms_moviestrDefPath; }
	public static void setmovieDefPath(final String str) { ms_moviestrDefPath = str; }
		
	// JPG 图片路径
	private static String ms_strJpgPath = "";
	public static String getJpgPath() { return ms_strJpgPath; }
	public static void setJpgPath(final String str) { ms_strJpgPath = str; }
	
	// 视频路径
	private static String ms_strVideoPath = "";
	public static String getVideoPath() { return ms_strVideoPath; }
	public static void setVideoPath(final String str) { ms_strVideoPath = str; }
	
	// 音乐路径
	private static String ms_strMusicPath = "";
	public static String getMusicPath() { return ms_strMusicPath; }
	public static void setMusicPath(final String str) { ms_strMusicPath = str; }
	
	// 视频播放速率
	private static float ms_fVideoSpeed = 1.0f;
	private static float ms_fVideoSpeedOld = 1.0f;
	public static float getVideoSpeed() { return ms_fVideoSpeed; }
		
	// 声音路径
	private static String ms_strSoundPath = "";
	public static String getSoundPath() { return ms_strSoundPath; }
	public static void setSoundPath(final String str) { ms_strSoundPath = str; }
	
	// 客户文件名
	private static String ms_strCustFN = "";
	public static String getCustFN() { return ms_strCustFN; }
	public static void setCustFN(final String str) { ms_strCustFN = str; }
	
	// 客户数据
	private static String ms_strCustData = "";
	public static String getCustData() { return ms_strCustData; }
	public static void setCustData(final String str) { ms_strCustData = str; }
	
	// 当前 Activity
	private static Activity ms_curActivity = null;
	public static Activity getCurActivity() { return ms_curActivity; }
	public static void setCurActivity(Activity activity) { ms_curActivity = activity; }
	
	// 视频布局
	private static RelativeLayout ms_layVideo = null;
	public static RelativeLayout getVideoLayout() { return ms_layVideo; }
	public static void setVideoLayout(RelativeLayout layout) { ms_layVideo = layout; }

	// JPG 视图
	private static JpgView ms_jpgView = null;
	public static JpgView getJpgView() { return ms_jpgView; }
	public static void setJpgView(JpgView jpgView) { ms_jpgView = jpgView; }
	
	//Matrix对象
	private static Matrix mMatrix = null;
	public static Matrix getMatrix() { return mMatrix; }
	public static void setMatrix(Matrix matrix) { mMatrix = matrix; }
	
	// OSD 视图
	private static OsdView1 ms_osdView1 = null;
	private static OsdView2 ms_osdView2 = null;
	private static OsdView3 ms_osdView3 = null;
	private static OsdView4 ms_osdView4 = null;
	private static OsdView1 ms_osdViewTop1 = null;
	private static OsdView2 ms_osdViewTop2 = null;
	private static OsdView1 ms_osdViewBottom1 = null;
	private static OsdView2 ms_osdViewBottom2 = null;
	public static OsdView1 getOsdView1() { return ms_osdView1; }
	public static OsdView2 getOsdView2() { return ms_osdView2; }
	public static OsdView3 getOsdView3() { return ms_osdView3; }
	public static OsdView4 getOsdView4() { return ms_osdView4; }
	public static OsdView1 getOsdViewTop1() { return ms_osdViewTop1; }
	public static OsdView2 getOsdViewTop2() { return ms_osdViewTop2; }
	public static OsdView1 getOsdViewBottom1() { return ms_osdViewBottom1; }
	public static OsdView2 getOsdViewBottom2() { return ms_osdViewBottom2; }
	public static void setOsdView1(OsdView1 osdView) { ms_osdView1 = osdView; }
	public static void setOsdView2(OsdView2 osdView) { ms_osdView2 = osdView; }
	public static void setOsdView3(OsdView3 osdView) { ms_osdView3 = osdView; }
	public static void setOsdView4(OsdView4 osdView) { ms_osdView4 = osdView; }
	public static void setOsdViewTop1(OsdView1 osdView) { ms_osdViewTop1 = osdView; }
	public static void setOsdViewTop2(OsdView2 osdView) { ms_osdViewTop2 = osdView; }
	public static void setOsdViewBottom1(OsdView1 osdView) { ms_osdViewBottom1 = osdView; }
	public static void setOsdViewBottom2(OsdView2 osdView) { ms_osdViewBottom2 = osdView; }
	
	// 视频视图
	private static VideoViewEx ms_videoViewMy = null;
	public static VideoViewEx getVideoViewMy() { return ms_videoViewMy; }
	public static void setVideoViewMy(VideoViewEx videoView) { ms_videoViewMy = videoView; }
	
	// 视频视图
	private static io.vov.vitamio.widget.VideoView ms_videoView = null;
	public static io.vov.vitamio.widget.VideoView getVideoView() { return ms_videoView; }
	public static void setVideoView(VideoView videoView) { ms_videoView = videoView; }
	
	// JPG 图片
	private static Bitmap ms_bmpJpg = null;  
	
	// 媒体播放器
	private static MediaPlayer ms_mediaPlayer = null;
	public static MediaPlayer getMediaPlayer() { return ms_mediaPlayer; }
	public static void setMediaPlayer(MediaPlayer mp) { ms_mediaPlayer = mp; }
	
	// 输入输出流
	private static InputStream ms_inputStream = null;
	private static OutputStream ms_outputStream = null;
	public static InputStream getInput() { return ms_inputStream; }
	public static OutputStream getOutput() { return ms_outputStream; }

	// 通知管理器
	private static NotificationManager ms_notifyMgr = null;
	public static NotificationManager getNotifyMgr() { return ms_notifyMgr; }
	
	// OSD 链表
	private static List<OsdBitmap> ms_listOsd1 = new ArrayList<OsdBitmap>();
	private static List<OsdBitmap> ms_listOsd2 = new ArrayList<OsdBitmap>();
	private static List<OsdGraph>  ms_listOsd3 = new ArrayList<OsdGraph>();
	private static List<OsdBitmap> ms_listOsd4 = new ArrayList<OsdBitmap>();
	private static List<OsdBitmap> ms_listOsdTop1 = new ArrayList<OsdBitmap>();
	private static List<OsdBitmap> ms_listOsdTop2 = new ArrayList<OsdBitmap>();
	private static List<OsdBitmap> ms_listOsdBottom1 = new ArrayList<OsdBitmap>();
	private static List<OsdBitmap> ms_listOsdBottom2 = new ArrayList<OsdBitmap>();
	
	// 数据路径
	private static List<String> ms_listMusicPath = new ArrayList<String>();
	private static List<String> ms_listVideoPath = new ArrayList<String>();
	private static List<String> ms_listPicturePath = new ArrayList<String>();
	private static List<ResolveInfo> ms_listApps = new ArrayList<ResolveInfo>();
	public static List<ResolveInfo> getListApps() { return ms_listApps; }
	public static List<String> getListMusicPath() { return ms_listMusicPath; }
	public static List<String> getListVideoPath() { return ms_listVideoPath; }
	public static List<String> getListPicturePath() { return ms_listPicturePath; }
	public static void setListApps(List<ResolveInfo> list) { ms_listApps = list; }
//	public static void newListMusicPath() { ms_listMusicPath = new ArrayList<String>(); }
//	public static void newListVideoPath() { ms_listVideoPath = new ArrayList<String>(); }
//	public static void newListPicturePath() { ms_listPicturePath = new ArrayList<String>(); }

	// 扩展 SD 卡列表
	private static List<String> ms_listExtStorage = new ArrayList<String>();
	public static List<String> getListExtStorage() { return ms_listExtStorage; }
	
	// JPG CRC32 哈希表
	private static HashMap<String, Long> ms_jpgMap = new HashMap<String, Long>();
	public static HashMap<String, Long> getJpgMap() { return ms_jpgMap; }
	
	// 声音哈希表
	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, String> ms_soundMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String> getSoundMap() { return ms_soundMap; }

	// 声音池
	private static SoundPool ms_soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	public static SoundPool getSoundPool() { return ms_soundPool; }
	public static void setSoundPool(SoundPool soundPool) { ms_soundPool = soundPool; }
	
	//----------------------------------------------------------------------------------------------------
	// 是否使用蓝牙
	private static boolean ms_bIsUseBt = false;
	public static boolean isUseBt() { return ms_bIsUseBt; }
	public static void setUseBt(final boolean b) { ms_bIsUseBt = b; }
	
	// 蓝牙是否可用
	private static boolean ms_bIsBtEnabled = false;
	public static boolean isBtEnabled() { return ms_bIsBtEnabled; }
	public static void setBtEnabled(final boolean b) { ms_bIsBtEnabled = b; }

	// 蓝牙地址
	private static String ms_strBtAdd = "";
	public static String getBtAdd() { return ms_strBtAdd; }
	public static void setBtAdd(final String str) { ms_strBtAdd = str; }
	
	// 蓝牙设备
	private static String ms_strBtDevice = "";
	public static String getBtDevice() { return ms_strBtDevice; }
	public static void setBtDevice(final String str) { ms_strBtDevice = str; }
	
	// 蓝牙串口服务
	private static BtSppService ms_btSpp = null;
	public static BtSppService getBtSpp() { return ms_btSpp; }

	// 串口
	private static SerialPort ms_serialPort = null;
	public static SerialPort getSP() { return ms_serialPort; }
	
	// 波特率
	private static int ms_nBaudrate = 57600;
//	private static int ms_nBaudrate = 19200;
	public static int getBaudrate() { return ms_nBaudrate; }

	// 蓝牙状态
	private static int bluetooth_state = 0;
	public static int getBluetoothState() { return bluetooth_state; }
	
	//二维码大小 QR
//	private static int QR_WIDTH = 200; 
//	private static int	QR_HEIGHT = 200;
	
	//迈宝赫
//	private static int QR_WIDTH = 130; 
//	private static int	QR_HEIGHT = 130;	
	//----------------------------------------------------------------------------------------------------

	public static String baseUrl = "http://dev.vrun.sh.cn/vrun/machineCtrl";
	public static String gameUrl = "http://dev.vrun.sh.cn/vrun/matchCtrl";
	public static String getIdUrl = "";
	public static String qrcodeUrl = "";
	public static String saveIdUrl = "/saveMachineId";
	public static String initIdUrl = "";
	public static String sendDataUrl = "/getRunningInfo";
	public static String saveAppointUrl = "/saveAppoint";
	public static String canBookUrl = "/canBook";
	public static String pushRenewInfoUrl = "/pushRenewInfo";
	//读取班跑配置文件
	public static void readProperties(){

		Properties properties = new Properties();
		try {
			File file = new File("/system/banpao.properties");
			FileInputStream fis = new FileInputStream(file);
			properties.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}

		baseUrl = properties.getProperty("baseUrl","http://dev.vrun.sh.cn/vrun/machineCtrl");
		getIdUrl = properties.getProperty("getIdUrl","");
		qrcodeUrl = properties.getProperty("qrcode","");
		saveIdUrl = properties.getProperty("saveId","/saveMachineId");
		initIdUrl = properties.getProperty("initId","");
		sendDataUrl = properties.getProperty("sendData","/getRunningInfo");
		saveAppointUrl = properties.getProperty("saveAppoint","/saveAppoint");
		canBookUrl = properties.getProperty("canBook","/canBook");
	}


	/**
	 * 配置
	 */
	public static class Config
	{
		// 开发者模式
		public static final boolean DEVELOPER_MODE = false;
	}
	private static ComponentName mComponet;
	@Override
	@SuppressWarnings("unused")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void onCreate()
	{
		String strActivity = android.content.Context.ACTIVITY_SERVICE;
		mAm = (ActivityManager)(getSystemService(strActivity));
		mRti = mAm.getRunningTasks(1);



		//全局异常捕获
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		//地图初始化
		//SDKInitializer.initialize(getApplicationContext());

		//读取班跑配置文件
		readProperties();
		try
		{
			if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			{
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
			}
		}
		catch (Exception e)
		{
		}

		super.onCreate();

		try
		{
			// 初始化图像加载器
			initImageLoader(getApplicationContext());
		}
		catch (Exception e)
		{
		}
		
		// 是否已创建
		ms_bCreated = true;
	}
	//顶部类名
	public static String getTopActivity(){

		// 获取正在运行的任务信息
		// 需要权限 android.permission.GET_TASKS

		if (mRti != null)
		{mComponet = mRti.get(0).topActivity;}
		return mComponet.getClassName();
	}
	//顶部包名
	public  static  String getTopPkgName(){
		return mComponet.getPackageName();
	}
	/**
	 * 创建
	 * @param context
	 */
	@SuppressWarnings("unused")
	public static void onCreate_(Context context)
	{
		try
		{
			if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			{
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
			}
		}
		catch (Exception e)
		{
		}

		try
		{
			// 初始化图像加载器
			initImageLoader(context);
		}
		catch (Exception e)
		{
		}
		
		// 是否已创建
		ms_bCreated = true;
	}
	
	/**
	 * 初始化图像加载器
	 * @param context
	 */
	public static void initImageLoader(Context context)
	{
		// This configuration tuning is custom. You can tune every option,
		// you may tune some of them, or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this); method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
//			.writeDebugLogs() // Remove for release app
			.build();
		
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 写调试日志
	 * @param strTag
	 * @param strMsg
	 */
	public static void TLog(final String strTag, final String strMsg)
	{
		// 启用调试
		if (DEBUG)
		{
			// 启用调试
			if (TDEBUG)
			{
				try
				{
					// 写调试日志
					JLog.d(strTag, strMsg);
				}
				catch (Exception e)
				{
				}
			}
		}
	}
	
	/**
	 * 写调试日志
	 * @param strTag
	 * @param strMsg
	 */
	public static void TsLog(final String strTag, final String strMsg)
	{
		// 启用调试
//	xwx	if (DEBUG)
		{
			// 启用调试
			if (TDEBUG)
			{
				try
				{
					// 写调试日志
					JLog.d(strTag, strMsg);
				}
				catch (Exception e)
				{
				}
			}
		}
	}
		
	/**
	 * 写串口日志
	 * @param strTag
	 * @param strMsg
	 */
	public static void SLog(final String strTag, final String strMsg)
	{
		// 启用调试
		if (DEBUG)
		{
			// 启用串口调试
			if (SDEBUG)
			{
				try
				{
					// 检测消息
					if (strTag == null || strMsg == null || strMsg.length() <= 0) return;
					
					// 发送字节
					String strSend = "\r\n" + strTag + " " + strMsg;
//					String strSend = strTag + " " + strMsg + "\r\n";
					byte[] send = strSend.getBytes();
					
					// 使用蓝牙
					if (ms_bIsUseBt)
					{
						// 写串口
						if (ms_btSpp != null) ms_btSpp.write(send);
					}
					// 不使用蓝牙
					else
					{
						// 写串口
						if (ms_outputStream != null) ms_outputStream.write(send);
					}
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	/**
	 * 检测字符串是否全部为"0"
	 * @param str
	 * @return
	 */
	private static boolean isZero(final String str)
	{
		if (str.equals(""))
		{
			return false;
		}

		int nLen = str.length();

		for (int i = 0; i < nLen; i++)
		{
			if (str.charAt(i) != '0')
			{
				return false;
			}
		}

		return true;
	}
		
	/**
	 * 双精度值转为字符串
	 * @param d 双精度值
	 * @param n 小数点后的位数, 不足则在数值后补"0"
	 * @return 双精度值的字符串
	 */
	private static String d2Str(final double d, final int n)
	{
		String str = String.format("%.15f", d);

		switch (n)
		{
		case 0:
			str = String.format("%.0f", d);
			break;

		case 1:
			str = String.format("%.1f", d);
			break;

		case 2:
			str = String.format("%.2f", d);
			break;

		case 3:
			str = String.format("%.3f", d);
			break;

		case 4:
			str = String.format("%.4f", d);
			break;

		case 5:
			str = String.format("%.5f", d);
			break;

		case 6:
			str = String.format("%.6f", d);
			break;

		case 7:
			str = String.format("%.7f", d);
			break;

		case 8:
			str = String.format("%.8f", d);
			break;

		case 9:
			str = String.format("%.9f", d);
			break;

		case 10:
			str = String.format("%.10f", d);
			break;

		case 11:
			str = String.format("%.11f", d);
			break;

		case 12:
			str = String.format("%.12f", d);
			break;

		case 13:
			str = String.format("%.13f", d);
			break;

		case 14:
			str = String.format("%.14f", d);
			break;

		case 15:
			str = String.format("%.15f", d);
			break;
		}

		String strTemp = str;

		// 去掉负号和小数点
		strTemp = strTemp.replace("-", "");
		strTemp = strTemp.replace(".", "");

		// 检测字符串是否全部为"0"
		if (isZero(strTemp))
		{
			if (str.charAt(0) == '-')
			{
				// 去掉负号
				str = str.substring(1);
			}
		}

		return str;
	}
	
/**
 * 写串口日志
 * @param strTag
 * @param strMsg
 */
public static void MYSLog(final String strTag, final String strMsg)
{
	// 启用调试
//	if (DEBUG)
	{
		// 启用串口调试
//		if (SDEBUG)
		{
			try
			{
				// 检测消息
				if (strTag == null || strMsg == null || strMsg.length() <= 0) return;
				
				// 发送字节
				String strSend = "\r\n" + strTag + " " + strMsg;
//				String strSend = strTag + " " + strMsg + "\r\n";
				byte[] send = strSend.getBytes();
				
				// 使用蓝牙
				if (ms_bIsUseBt)
				{
					// 写串口
					if (ms_btSpp != null) ms_btSpp.write(send);
				}
				// 不使用蓝牙
				else
				{
					// 写串口
					if (ms_outputStream != null) ms_outputStream.write(send);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
}	
/**
 * 进入网页
 */
public static void BrowseToUlr(String ulr)
{
	try
	{
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
		
			// 关机
			((MainActivity)theApp.ms_curActivity).gotopage(ulr);
		}

	}
	catch (Exception e)
	{
	}
}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 读共享属性
	 */
	public static void readPref()
	{
		try
		{
			SharedPreferences sp = ms_pref;
			if (sp == null) return;

			// 公英制单位
			ms_nUnits = sp.getInt(PREF_KEY_UNITS, 0);
			
			// 蓝牙开关状态
			ms_nBtState = sp.getInt(PREF_KEY_BT_STATE, 0);
			
			// 音乐播放模式
			int nPlayMode = sp.getInt(PREF_KEY_MUSIC_PM, MusicLayout.PLAY_MODE_LIST);
			MusicLayout.setPlayMode(nPlayMode);
			
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写共享属性
	 */
	public static void writePref()
	{
		try
		{
			SharedPreferences sp = ms_pref;
			if (sp == null) return;
			
			// 获取编辑器
			SharedPreferences.Editor editor = sp.edit();
			
			// 公英制单位
			editor.putInt(PREF_KEY_UNITS, ms_nUnits);
			
			// 蓝牙开关状态
			editor.putInt(PREF_KEY_BT_STATE, ms_nBtState);
			
			// 音乐播放模式
			int nPlayMode = MusicLayout.getPlayMode();
			editor.putInt(PREF_KEY_MUSIC_PM, nPlayMode);			
			
			// 提交修改
			editor.commit();
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 读共享属性
	 */
	public static void readPrefPicNum()
	{
		try
		{
			SharedPreferences sp = ms_pref_picnum;
			if (sp == null) return;

			// 图片个数
			ms_nPicNum = sp.getInt(PREF_KEY_PICNUM, 0);
			
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写共享属性
	 */
	public static void writePrefPicNum()
	{
		try
		{
			SharedPreferences sp = ms_pref_picnum;
			if (sp == null) return;
			
			// 获取编辑器
			SharedPreferences.Editor editor = sp.edit();
			
			// 公英制单位
			editor.putInt(PREF_KEY_PICNUM, ms_nPicNum);
			
			// 提交修改
			editor.commit();
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 读共享属性
	 */
	public static void readPrefStatus()
	{
		try
		{
			SharedPreferences sp = ms_pref_status;
			if (sp == null) return;

			// /服务器状态
			ms_nRun = sp.getBoolean(PREF_KEY_STATUS, true);
			
			//关机次数
			ms_nPowerTimes = sp.getInt(PREF_KEY_POWERTIMES, 0);
			
			//二维码状态
			ms_nQrcode = sp.getBoolean(PREF_KEY_QRCODE, false);
			
			//MAC地址
			ms_nMacAdress=sp.getString(PREF_KEY_MAC,null);
			
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写共享属性
	 */
	public static void writePrefStatus()
	{
		try
		{
			SharedPreferences sp = ms_pref_status;
			if (sp == null) return;
			
			// 获取编辑器
			SharedPreferences.Editor editor = sp.edit();
			
			//服务器状态
			editor.putBoolean(PREF_KEY_STATUS, ms_nRun);
			
			//关机次数
			editor.putInt(PREF_KEY_POWERTIMES, ms_nPowerTimes);

			//二维码状态
			editor.putBoolean(PREF_KEY_QRCODE, ms_nQrcode);
			
			//MAC地址
			editor.putString(PREF_KEY_MAC,ms_nMacAdress);
			
			// 提交修改
			editor.commit();
		}
		catch (Exception e)
		{
		}
	}		
	//----------------------------------------------------------------------------------------------------
	/**
	 * 检测是否在前台运行
	 * @param context
	 * @return
	 */
	public static boolean isRunningForeground(Context context)
	{
		try
		{
			// 包名
			String strPkgName = context.getPackageName();
			
			// 获取顶层 Activity
			String strTopActivity = getTopActivity(context);

			if (strPkgName != null && strTopActivity != null && strTopActivity.startsWith(strPkgName))
			{
				Log.d(TAG, strPkgName + ": isRunningForeGround");
				return true;
			}
			
			Log.d(TAG, strPkgName + ": isRunningBackGround");
			return false;
		}
		catch (Exception e)
		{
		}

		return false;
	}
	/**
	 * 获取当前位置
	 * @return
	 */
	public static int getCurrentPosition()
	{
		// 不使用 VITAMIO
		if (!theApp.VITAMIO)
		{
			if (ms_videoViewMy != null) return (int)(ms_videoViewMy.getCurrentPosition()/1000);
		}
		else
		{
			if (ms_videoView != null) return (int)(ms_videoView.getCurrentPosition()/1000);
		}
		
		return 0;
	}
	
	/**
	 * 获取顶层 Activity
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context)
	{
		try
		{
			String strTopActivity = null;
			
			String strActivity = android.content.Context.ACTIVITY_SERVICE;
			ActivityManager am = (ActivityManager)(context.getSystemService(strActivity));
			
			// 获取正在运行的任务信息
			// 需要权限 android.permission.GET_TASKS
			List<RunningTaskInfo> rti = am.getRunningTasks(1);
			
			if (rti != null)
			{
				ComponentName componet = rti.get(0).topActivity;
				strTopActivity = componet.getClassName();
				Log.d(TAG, strTopActivity + ": isTopActivity");
			}

			return strTopActivity;
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 设置系统语言
	 */
	public static void setSystemLang()
	{
		try
		{
			// 获取资源
			Resources resources = ms_curActivity.getResources();
			
			// 获取配置
			Configuration config = resources.getConfiguration();
			
			// 获取屏幕参数
			DisplayMetrics metrics = resources.getDisplayMetrics();
			
			//土耳其语
			Locale newlocale = new Locale("tr"); 
			
			//俄语
			Locale rulocale = new Locale("ru"); 
			
			//西班牙语
			Locale eslocale = new Locale("es"); 	
			
			//葡萄牙语
			Locale ptlocale = new Locale("pt"); 

			//波兰语
			Locale pllocale = new Locale("pl"); 

			//瑞典语
			Locale svlocale = new Locale("sv"); 
			
			//阿拉伯语
			Locale arlocale = new Locale("ar"); 		
			
			// 本地化
			if (ms_strLang.equals("en")) config.locale = Locale.US;             // 英文
			if (ms_strLang.equals("ch")) config.locale = Locale.SIMPLIFIED_CHINESE;  // 中文, ch
			if (ms_strLang.equals("tw")) config.locale = Locale.TRADITIONAL_CHINESE; // 繁体中文, tw
			if (ms_strLang.equals("ko")) config.locale = Locale.KOREAN;              // 韩语, ko
			if (ms_strLang.equals("ja")) config.locale = Locale.JAPANESE;            // 日语, ja
			if (ms_strLang.equals("ru")) config.locale = rulocale;             // 俄语, ru
			if (ms_strLang.equals("de")) config.locale = Locale.GERMAN;              // 德语, de
			if (ms_strLang.equals("fr")) config.locale = Locale.FRENCH;              // 法语, fr
			if (ms_strLang.equals("es")) config.locale = eslocale;             // 西班牙语, sp
			if (ms_strLang.equals("pt")) config.locale = ptlocale;             // 葡萄牙语, pt
			if (ms_strLang.equals("it")) config.locale = Locale.ITALIAN;             // 意大利语, it
			if (ms_strLang.equals("pl")) config.locale = pllocale;             // 波兰语, pl
			if (ms_strLang.equals("sv")) config.locale = svlocale;             // 瑞典语, sv
			if (ms_strLang.equals("ar")) config.locale = arlocale;             // 阿拉伯语, ar
			if (ms_strLang.equals("q3")) config.locale = Locale.US;             // 其它语言 3, q3
			if (ms_strLang.equals("tr")) config.locale = newlocale;             // 土耳其语, tr
			
			TLog(TAG, "Language: " + ms_strLang);
			SLog(TAG, "Language: " + ms_strLang);
			
			// 更新配置
			resources.updateConfiguration(config, metrics);
			TLog(TAG, "updateConfiguration");
			SLog(TAG, "updateConfiguration");
			
			//----------------------------------------------------------------------------------------------------
			Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
			Log.d("amnType", activityManagerNative.toString());
			TLog("amnType", activityManagerNative.toString());
			SLog("amnType", activityManagerNative.toString());

			Object amObj = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
			Log.d("amType", amObj.getClass().toString());
			TLog("amType", amObj.getClass().toString());
			SLog("amType", amObj.getClass().toString());

			Object configObj = amObj.getClass().getMethod("getConfiguration").invoke(amObj);
			Log.d("configType", configObj.getClass().toString());
			TLog("configType", configObj.getClass().toString());
			SLog("configType", configObj.getClass().toString());
//			configObj.getClass().getDeclaredField("locale").set(configObj, Locale.CHINA);
			configObj.getClass().getDeclaredField("locale").set(configObj, config.locale);
			configObj.getClass().getDeclaredField("userSetLocale").setBoolean(configObj, true);

			// 需要权限 android.permission.CHANGE_CONFIGURATION
			amObj.getClass().getMethod("updateConfiguration", android.content.res.Configuration.class).invoke(amObj, configObj);
			TLog("getMethod", "updateConfiguration");
			SLog("getMethod", "updateConfiguration");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.d(TAG, e.toString());
			TLog(TAG, e.toString());
			SLog(TAG, e.toString());
		}
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * 设置波特率
	 * @param n
	 */
	public static void setBaudrate(final int n)
	{
		if (n ==   1200 ||
			n ==   2400 ||
			n ==   4800 ||
			n ==   9600 ||
			n ==  19200 ||
			n ==  38400 ||
			n ==  57600 ||
			n == 115200)
		{
			ms_nBaudrate = n;
		}
	}
	
	/**
	 * 设置蓝牙串口服务
	 * @param context
	 * @param handler
	 */
	public static void setupBtSpp(Context context, Handler handler)
	{
		try
		{
			ms_btSpp = new BtSppService(context, handler);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 设置串口
	 * @return 异常
	 */
	public static String setSerialPort()
	{
		String strError = null;
	
		try
		{
			// 串口
			String strDevice = "/dev/ttyS1";
			
			if (RK3188) strDevice = "/dev/ttyS3";
								
			File fileDevice = new File(strDevice);
			ms_serialPort = new SerialPort(fileDevice, ms_nBaudrate, 0);

			if (ms_serialPort != null)
			{
				// 输入输出流
				ms_inputStream = ms_serialPort.getInputStream();
				ms_outputStream = ms_serialPort.getOutputStream();
			}
		}
		catch (SecurityException e)
		{
			strError = "You do not have read/write permission to the serial port.";
		}
		catch (IOException e)
		{
			strError = "The serial port can not be opened for an unknown reason.";
		}
		
		return strError;
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 生成路径
	 * @param strFileName
	 */
	public static void mkdir(final String strFileName)
	{
		try
		{
			// 文件
			File file = new File(strFileName);
			
			// 检测文件
			if (!file.exists() || !file.isDirectory())
			{
				// 生成路径
				file.mkdir();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 计算文件的 CRC32
	 * @param strPath
	 * @return
	 */
	public static long fileCRC32(final String strPath)
	{
		try
		{
			int nLen = 0;
			byte[] buffer = new byte[1024];
			
			CRC32 crc32 = new CRC32();
			
			FileInputStream fis = new FileInputStream(strPath);
			
			while ((nLen = fis.read(buffer)) > 0)
			{
				crc32.update(buffer, 0, nLen);
			}

			if (fis != null) fis.close();
			
			long lCRC32 = crc32.getValue();
			return lCRC32;
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	/**
	 * 解码图像
	 * @param strFile
	 * @return
	 */
	public static Bitmap decodeBitmap(final String strFile)
	{
/*		try
		{
			InputStream is = new FileInputStream(strFile);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
	        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
	        return BitmapFactory.decodeStream(is, null, options);
		}
		catch (Exception e)
		{
		}
		
		return null;
*/
		Bitmap bmp = null;
		FileInputStream fis = null;
		
        if (ms_maps.containsKey(strFile)) 
        {

            bmp = ms_maps.get(strFile);

        }
        else
        {
			try
			{
				// 输入流
				fis = new FileInputStream(strFile);
				
				// 选项
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inDither = false;
				options.inPurgeable = true;
				options.inInputShareable = true;
				options.inTempStorage = new byte[32 * 1024];
		        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
		        
		        // 解码
				bmp = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
				
				if (bmp != null) ms_maps.put(strFile, bmp);
				
			}
			catch (IOException e)
			{
			}
			finally
			{
				try
				{
					if (fis != null) fis.close();
				}
				catch (IOException e)
				{
				}
			}
        }
		return bmp;
	}
	
	/**
	 * 从Assets中解码图像
	 * @param strFile
	 * @return
	 */
	Bitmap decodeBitmapFromAssets(final String strFile)
	{
		Bitmap bmp = null;
		FileInputStream fis = null;
		AssetManager am = getResources().getAssets();
		
		try
		{
			// 输入流
			fis = (FileInputStream) am.open(strFile);
			
			// 选项
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[32 * 1024];
	        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
	        
	        // 解码
			bmp = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
		}
		catch (IOException e)
		{
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
			}
			catch (IOException e)
			{
			}
		}
		
		return bmp;
	}
				
	//----------------------------------------------------------------------------------------------------
	/**
	 * 播放视频
	 */
	public static void playVideo()
	{
		try
		{

			try
			{
				MusicLayout.stopMusic();
			}
			catch (Exception e)
			{
			}	
			
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				ms_videoViewMy.start();
			}
			else
			{
				ms_videoView.start();
				
				// 设置播放速率		
				if (theApp.USE_VIDEOADJUST) ms_videoView.setPlaybackSpeed(ms_fVideoSpeed);	
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 播放视频
	 * @param bPlay 是否播放
	 * @param bStop 是否停止
	 */
	public static void playVideo(final boolean bPlay, final boolean bStop)
	{
		try
		{
			int nL = theApp.getVideoX1();
			int nT = theApp.getVideoY1();
			int nW = theApp.getVideoX2() - nL;
			int nH = theApp.getVideoY2() - nT;
			
			// 屏幕宽度和高度
			int nWP = ms_nWidthPixels;
			int nHP = ms_nHeightPixels;			
			int nR = nWP - nL - nW;
			int nB = nHP - nT - nH;		
			
			if (theApp.USE_VIDEOADJUST)
			{	
				// 屏幕宽度和高度
				 nWP = ms_nWidthPixels;
				 nHP = ms_nHeightPixels;
				
				nL = (nL < 0 || nL > nWP) ? 0 : nL;
				nT = (nT < 0 || nT > nHP) ? 0 : nT;
				nW = (nW < 0) ? 1 : nW;
				nH = (nH < 0) ? 1 : nH;
				nW = (nW > nWP - nL) ? nWP - nL : nW;
				nH = (nH > nHP - nT) ? nHP - nT : nH;
				
				if (nL == ms_nVideoL) nL += 1;
				if (nT == ms_nVideoT) nT += 1;
				ms_nVideoL = nL;
				ms_nVideoT = nT;
			}	
			// 文件
			File file = new File(ms_strVideoPath);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;
			
			if (!getDaInitstatus())
			{			
				if(	ms_curActivity != null && ms_curActivity instanceof MainActivity)
				{
					((MainActivity)ms_curActivity).initDaVideo();
				}				
				
			}
			// 播放
			if (bPlay)
			{
				// 不停止
				if (!bStop)
				{
					// 正在播放
					if (isPlayingVideo())
					{
						try
						{
							// 延迟 1000 毫秒
							Thread.sleep(1000);
						}
						catch (Exception e)
						{
						}
					}
				}
				// 停止
				else
				{
					// 正在播放
					if (isPlayingVideo())
					{
						// 停止视频
						theApp.stopVideo();
						
						try
						{
							// 延迟 1000 毫秒
							Thread.sleep(1000);
						}
						catch (Exception e)
						{
						}
					}
				}
				// 不使用 VITAMIO
				if (!theApp.VITAMIO)
				{
					// 设置视频路径
					theApp.getVideoViewMy().setVideoPath(ms_strVideoPath);
				}
				// 使用 VITAMIO
				else
				{
					// 设置视频路径
					theApp.getVideoView().setVideoPath(ms_strVideoPath);
				}
			}
			
			if(	ms_curActivity != null && ms_curActivity instanceof MainActivity)
			{
				((MainActivity)ms_curActivity).clearLayoutVideo();
			}			
			
			// 设置视频布局
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(nW, nH);
			params.setMargins(nL, nT, nR, nB);
			ms_layVideo.setLayoutParams(params);
			
			//打开视频布局
//			if (ms_layVideo != null) ms_layVideo.setVisibility(View.VISIBLE);
			
//			Log.d(TAG, String.format("LayoutParams - Width: %d, Height: %d", nW, nH));
//			Log.d(TAG, String.format("Margins - Left: %d, Top: %d, Right: %d, Bottom: %d", nL, nT, nR, nB));
			
			if (theApp.USE_VIDEOADJUST)
			{
				// 相同的视频
				if (!bPlay && !bStop)
				{
					try
					{
						// 使用 VITAMIO
						if (theApp.VITAMIO)
						{
							// 速率已改变
							if (ms_fVideoSpeed != ms_fVideoSpeedOld)
							{
								// 设置播放速率
								ms_videoView.setPlaybackSpeed(ms_fVideoSpeed);
							}
						}
					}
					catch (Exception e)
					{
					}
				}
			}
			
			// 播放
			if (bPlay)
			{
				// 没有播放
				if (!isPlayingVideo())
				{
					// 播放视频
					playVideo();
//					Log.d(TAG, "playVideo:" + ms_strVideoPath);
				}
				
				// 检测视频当前位置
				if (ms_nVideoCurPos > 0)
				{
					// 跳转视频
					seekToVideo(ms_nVideoCurPos);
//					Log.d(TAG, "Video Position: " + ms_nVideoCurPos);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 暂停视频
	 */
	public static void pauseVideo()
	{
		try
		{
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				// 视频当前位置
				ms_nVideoCurPos = (int)ms_videoViewMy.getCurrentPosition();
				
				ms_videoViewMy.pause();
			}
			else
			{
				// 视频当前位置
				ms_nVideoCurPos = (int)ms_videoView.getCurrentPosition();
				
				ms_videoView.pause();
			}

		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 跳转视频
	 * @param msec
	 */
	public static void seekToVideo(int msec)
	{
		try
		{
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				if (msec > 0) ms_videoViewMy.seekTo(msec);
			}
			else
			{
				if (msec > 0) ms_videoView.seekTo((long)msec);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 停止视频
	 */
	public static void stopVideo()
	{
		try
		{
			// 不使用 VITAMIO
//			if (theApp.VITAMIO)
			{
				int nL = 0;
				int nT = 0;
				int nW = 1;
				int nH = 1;
	
				// 设置视频布局
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(nW, nH);
				params.setMargins(nL, nT, 0, 0);
				ms_layVideo.setLayoutParams(params);
			}
//			if (ms_layVideo != null) ms_layVideo.stop();
//			if (ms_layVideo != null) ms_layVideo.setVisibility(View.INVISIBLE);

			
			// 视频当前位置
			ms_nVideoCurPos = 0;
			
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				// 停止播放
				ms_videoViewMy.stopPlayback();
	//			Log.d(TAG, "stopPlayback");
		
				// 设置视频 URI
				ms_videoViewMy.setVideoURI(null);
				ms_videoViewMy.setVideoPath(null);
			}
			else
			{
				// 停止播放
				ms_videoView.stopPlayback();
	//			Log.d(TAG, "stopPlayback");
		
				// 设置视频 URI
				ms_videoView.setVideoURI(null);
				ms_videoView.setVideoPath(null);
			}
			
			if (!theApp.VITAMIO)
			{
				//清除视频布局
//				if (ms_layVideo != null) ms_layVideo.setVisibility(View.INVISIBLE);
			}
			
			// 垃圾回收
//			System.gc();
			System.runFinalization();
//			Runtime.getRuntime().gc();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 是否正在播放视频
	 * @return
	 */
	public static boolean isPlayingVideo()
	{
		try
		{
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				return ms_videoViewMy.isPlaying();
			}
			else
			{
				return ms_videoView.isPlaying();
			}
			
		}
		catch (Exception e)
		{
		}

		return false;
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 播放音乐
	 */
	public static void playMusic()
	{
		try
		{
			// 关闭音效
			if (!ms_bVolume) return;
			
			// 停止音乐
			if (ms_mediaPlayer.isPlaying()) stopMusic();

			// 文件
			File file = new File(ms_strMusicPath);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;

			// 播放
			Uri uri = Uri.parse(ms_strMusicPath);
			ms_mediaPlayer = MediaPlayer.create(ms_curActivity, uri);
			ms_mediaPlayer.start();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 暂停音乐
	 */
	public static void pauseMusic()
	{
		try
		{
			// 暂停音乐
			if (ms_mediaPlayer.isPlaying()) ms_mediaPlayer.pause();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 停止音乐
	 */
	public static void stopMusic()
	{
		try
		{
			// 停止音乐
			ms_mediaPlayer.stop();
			ms_mediaPlayer.release();
			ms_mediaPlayer = null;
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 播放声音
	 */
	public static void playSound()
	{
		try
		{
			// 关闭音效
			if (!ms_bVolume) return;
			
			// 未加载声音
			if (!ms_bLoadSound) return;
			
			// 文件
			File file = new File(ms_strSoundPath);
	
			// 检测文件
			if (!file.exists() || !file.isFile()) return;
			
			// 声音哈希表大小
			int nSoundID = 0;
			int nSize = ms_soundMap.size();
			
			for (int i = 0; i < nSize; i++)
			{
				// 音声 ID
				nSoundID = i + 1;
				String strPath = ms_soundMap.get(nSoundID);
				if (strPath.equals(ms_strSoundPath)) break;
			}
			
			// 播放声音
			ms_soundPool.play(nSoundID, 1.0f, 1.0f, 1, 0, 1.0f);
//			Log.d(TAG, "SoundID: " + nSoundID);
//			Log.d(TAG, "playSound:" + ms_strSoundPath);
		}
		catch (Exception e)
		{
		}
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 获取媒体音量
	 * @param activity
	 * @return
	 */
	public static int getStreamVolume(Activity activity)
	{
		try
		{
			AudioManager am = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
			return am.getStreamVolume(AudioManager.STREAM_MUSIC);
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	/**
	 * 设置媒体音量
	 * @param activity
	 * @param nVolume
	 */
	public static void setStreamVolume(Activity activity, final int nVolume)
	{
		try
		{
			AudioManager am = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, nVolume, AudioManager.FLAG_PLAY_SOUND);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 检测是否开启自动调节亮度
	 * @param activity
	 * @return
	 */
	public static boolean isAutoBrightness(Activity activity)
	{
		boolean bAuto = false;
		
		try
		{
			ContentResolver cr = activity.getContentResolver();
			bAuto = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE) ==
					Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		}
		catch (SettingNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return bAuto;
	}

	/**
	 * 开启自动调节亮度
	 * @param activity
	 */
	public static void startAutoBrightness(Activity activity)
	{
		try
		{
			Settings.System.putInt(activity.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 关闭自动调节亮度
	 * 
	 * @param activity
	 */
	public static void closeAutoBrightness(Activity activity)
	{
		try
		{
			Settings.System.putInt(activity.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取亮度
	 * @param activity
	 * @return
	 */
	public static int getBrightness(Activity activity)
	{
		try
		{
			ContentResolver cr = activity.getContentResolver();
			return Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		}
		catch (SettingNotFoundException e)
		{
		}
		
		return 0;
	}

	/**
	 * 设置亮度
	 * @param activity
	 * @param nValue
	 */
	public static void setBrightness(Activity activity, final int nValue)
	{
		try
		{
			// 关闭自动调节亮度
			closeAutoBrightness(activity);
			
			// 设置亮度
			WindowManager.LayoutParams params = activity.getWindow().getAttributes();
			params.screenBrightness = nValue / 255f;
			activity.getWindow().setAttributes(params);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 保存亮度
	 * @param resolver
	 * @param nValue
	 */
	public static void saveBrightness(Activity activity, final int nValue)
	{
		try
		{
			// 需要权限 android.permission.WRITE_SETTINGS
			ContentResolver cr = activity.getContentResolver();
			Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
			android.provider.Settings.System.putInt(cr, "screen_brightness", nValue);
			cr.notifyChange(uri, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------------------------------------
	/**
	 * 检测是否位于顶部悬浮窗
	 * @param osd
	 * @return
	 */
	private static boolean inTop(OsdBitmap osd)
	{
		if (osd == null) return false;
		
		// 检测坐标
		if (ms_nTopX1 <= osd.getX1() && ms_nTopY1 <= osd.getY1() &&
			ms_nTopX2 >= osd.getX2() && ms_nTopY2 >= osd.getY2())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 检测是否位于顶部悬浮窗
	 * @param nX1
	 * @param nY1
	 * @param nX2
	 * @param nY2
	 * @return
	 */
	private static boolean inTop(final int nX1, final int nY1, final int nX2, final int nY2)
	{
		// 检测坐标
		if (ms_nTopX1 <= nX1 && ms_nTopY1 <= nY1 &&
			ms_nTopX2 >= nX2 && ms_nTopY2 >= nY2)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 检测是否位于底部悬浮窗
	 * @param osd
	 * @return
	 */
	private static boolean inBottom(OsdBitmap osd)
	{
		if (osd == null) return false;
		
		// 检测坐标
		if (ms_nBottomX1 <= osd.getX1() && ms_nBottomY1 <= osd.getY1() &&
			ms_nBottomX2 >= osd.getX2() && ms_nBottomY2 >= osd.getY2())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 检测是否位于底部悬浮窗
	 * @param nX1
	 * @param nY1
	 * @param nX2
	 * @param nY2
	 * @return
	 */
	private static boolean inBottom(final int nX1, final int nY1, final int nX2, final int nY2)
	{
		// 检测坐标
		if (ms_nBottomX1 <= nX1 && ms_nBottomY1 <= nY1 &&
			ms_nBottomX2 >= nX2 && ms_nBottomY2 >= nY2)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取响应的 P10 字节
	 * @return
	 */
	private static byte getResponseP10()
	{
		// 前后台
		int nB = ms_nBackground;
		
		// 媒体
		int nM = MARK_NONE;
		
		// 多媒体标记
		if (ms_nMediaMark == MARK_VIDEO && ms_nVideoMark == MARK_LIST) nM = 3;
		if (ms_nMediaMark == MARK_VIDEO && ms_nVideoMark == MARK_PLAY) nM = 4;
		if (ms_nMediaMark == MARK_MUSIC && ms_nMusicMark == MARK_LIST) nM = 5;
		if (ms_nMediaMark == MARK_MUSIC && ms_nMusicMark == MARK_PLAY) nM = 6;
		if (ms_nMediaMark == MARK_PICTU && ms_nPictuMark == MARK_LIST) nM = 7;
		if (ms_nMediaMark == MARK_PICTU && ms_nPictuMark == MARK_PLAY) nM = 8;

		if (ms_nMapMark == MARK_IN ) nM = 2;

		// 高低位
		int nH = nB;
		int nL = nM;
		
		// 已启动悬浮窗
		if (ms_bStartFloat) nL = 9;
		
		int nP10 = nH * 10 + nL;
		byte[] p10 = int2bcd(nP10);
		
		return p10[1];
	}
	
	/**
	 * 添加 OSD 图片
	 * @param nNum 链表编号
	 * @param bmp osd 图片
	 */
	public static void addOsdBmp(final int nNum, OsdBitmap osd1, boolean bAnimation)
	{
		try
		{
			if (osd1 == null) return;
			
			List<OsdBitmap> listOsd = null;
		//	theApp.MYSLog(TAG,"bbbb"+nNum);
			if (nNum != 1 && nNum != 2 && nNum != 4) return;

			// 启用悬浮窗单个开关
			if (FLOAT_SWITCH)
			{
				// 关闭悬浮窗
				if (!ms_bFloatOn)
				{
					if (nNum == 1) listOsd = ms_listOsd1;
					if (nNum == 2) listOsd = ms_listOsd2;
					if (nNum == 4) listOsd = ms_listOsd4;
				}
				// 开启悬浮窗
				else
				{
					// 检测是否位于顶部悬浮窗
					if (inTop(osd1))
					{
						if (nNum == 1) listOsd = ms_listOsdTop1;
						if (nNum == 2) listOsd = ms_listOsdTop2;
						
					}
					// 检测是否位于底部悬浮窗
					else if (inBottom(osd1))
					{
						if (nNum == 1) listOsd = ms_listOsdBottom1;
						if (nNum == 2) listOsd = ms_listOsdBottom2;
						
					}
				}
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 打开顶部悬浮窗
				if (ms_bFloatTopOn)
				{
					// 检测是否位于顶部悬浮窗
					if (inTop(osd1))
					{
						if (nNum == 1) listOsd = ms_listOsdTop1;
						else if (nNum == 2) listOsd = ms_listOsdTop2;
					}
				}
				
				// 打开底部悬浮窗
				if (ms_bFloatBottomOn)
				{
					// 检测是否位于底部悬浮窗
					if (inBottom(osd1))
					{
						if (nNum == 1) listOsd = ms_listOsdBottom1;
						else if (nNum == 2) listOsd = ms_listOsdBottom2;
					}
				}
				
				// 关闭顶部和底部悬浮窗
				if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
				{
					if (nNum == 1) listOsd = ms_listOsd1;
					if (nNum == 2) listOsd = ms_listOsd2;
					if (nNum == 4) listOsd = ms_listOsd4;
				}
			}
//			theApp.MYSLog(TAG,"cccc"+listOsd);
			if (listOsd == null) return;
		//	theApp.MYSLog(TAG,"dddd");
			// 图片数量
			int nSize = listOsd.size();
			
//			theApp.MYSLog(TAG,"dddd"+nSize);
			
//			Log.d(TAG, String.format("OsdBitmap Num: - %d", nNum));
//			Log.d(TAG, String.format("OsdBitmap Size: - %d", nSize));
			
			for (int i = 0; i < nSize; i++)
			{
				// OSD 图片
				OsdBitmap osd2 = listOsd.get(i);
	
				// 坐标
				int nX11 = osd1.getX1();
				int nY11 = osd1.getY1();
				int nX12 = osd1.getX2();
				int nY12 = osd1.getY2();
				int nX21 = osd2.getX1();
				int nY21 = osd2.getY1();
				int nX22 = osd2.getX2();
				int nY22 = osd2.getY2();
				
				// 检测坐标
				if (nX11 <= nX21 && nY11 <= nY21 && nX12 >= nX22 && nY12 >= nY22)
				{
					i--;
					nSize--;
					
					// 移除 OSD 图片
					osd2.recycle();
					listOsd.remove(osd2);
//					Log.d(TAG, String.format("Remove OSD - %d, %d, %d, %d, %d, %d, %d, %d", nX11, nY11, nX12, nY12, nX21, nY21, nX22, nY22));
				}
				
				// 检测路径
				if (bAnimation && osd1.getPath().equals(osd2.getPath()))
				{
					// 检测坐标
					if (nX11 != nX21 || nY11 != nY21 || nX12 != nX22 || nY12 != nY22)
					{
						i--;
						nSize--;
						
						// 移除 OSD 图片
						osd2.recycle();
						listOsd.remove(osd2);
//						Log.d(TAG, String.format("Remove OSD - %d, %d, %d, %d, %d, %d, %d, %d", nX11, nY11, nX12, nY12, nX21, nY21, nX22, nY22));
					}
				}						
			}
							

			// 添加 OSD 图片
			listOsd.add(osd1);
//			nSize = listOsd.size();
//			theApp.MYSLog(TAG,"listOsd="+listOsd.size());
//			theApp.MYSLog(TAG,"ms_listOsd4="+ms_listOsd4.size());
//			Log.d(TAG, String.format("OsdBitmap Size: - %d", nSize));
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 显示通知
	 * @param activity
	 */
//	@SuppressWarnings("deprecation")
	public static void showNotify(Activity activity)
	{
		try
		{
			if (activity == null) return;
			if (!(activity instanceof MainActivity)) return;
			
			// 应用程序名称
			String strAppName = activity.getString(R.string.strAppName);
			
			// 清除通知
			if (ms_notifyMgr != null) ms_notifyMgr.cancel(R.drawable.ic_launcher);
			
			// 通知管理器
			ms_notifyMgr = (NotificationManager)activity.getSystemService(NOTIFICATION_SERVICE);
			
			// 通知
			Notification notify = new Notification(R.drawable.ic_launcher, strAppName, System.currentTimeMillis());
			
			// 应用程序 Intent
			Intent appIntent = new Intent(Intent.ACTION_MAIN);
			appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			appIntent.setComponent(new ComponentName(activity.getPackageName(), activity.getPackageName() + "." + activity.getLocalClassName()));
			
			// 设置启动模式
			appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	
			PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, appIntent, 0);
	
			// 正在后台运行
			String strRunning = activity.getString(R.string.strRunning);
			
			notify.flags = Notification.FLAG_NO_CLEAR;
			notify.setLatestEventInfo(activity, strAppName, strAppName + " " + strRunning, contentIntent);
	
			ms_notifyMgr.notify(R.drawable.ic_launcher, notify);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 拷贝文件夹
	 * @param strFolder1 源文件夹
	 * @param strFolder2 目标文件夹
	 */
	public static void copyFolder(final String strFolder1, final String strFolder2)
	{
		try
		{
			// 生成路径
			mkdir(strFolder2);
			
			// 源文件
			File file1 = new File(strFolder1);
	
			// 检测是否为文件夹
			if (!file1.exists() ||
				!file1.isDirectory() ||
				file1.listFiles() == null ||
				file1.listFiles().length <= 0) return;
			
			// 源文件列表
			File[] files1 = file1.listFiles();
			
			for (int i = 0; i < files1.length; i++)
			{
				// 文件
				file1 = files1[i];

				// 文件名
				String strName = file1.getName();
				
				// 文件
				if (file1.isFile())
				{
					// 目标文件
					String strFile2 = strFolder2 + strName;
					File file2 = new File(strFile2);
					Log.d(TAG, "CopyFile: " + strFile2);
					TLog(TAG, "CopyFile: " + strFile2);
					SLog(TAG, "CopyFile: " + strFile2);
					
					// 拷贝文件
					copyFile(file1, file2);
				}
				
				// 文件夹
				if (file1.isDirectory())
				{
					// 源文件夹
					String strFile1 = strFolder1 + strName;
					if (!strFile1.endsWith("/")) strFile1 += "/";
					
					// 目标文件夹
					String strFile2 = strFolder2 + strName;
					if (!strFile2.endsWith("/")) strFile2 += "/";
					Log.d(TAG, "CopyFolder: " + strFile2);
					TLog(TAG, "CopyFolder: " + strFile2);
					SLog(TAG, "CopyFolder: " + strFile2);
					
					// 拷贝文件夹
					copyFolder(strFile1, strFile2);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 拷贝文件
	 * @param file1 源文件
	 * @param file2 目标文件
	 * @return
	 */
	public static String copyFile(File file1, File file2)
	{
		String str = null;
		
		if (file1 == null)
		{
			str = "The source file is null.";
			return str;
		}
		
		if (file2 == null)
		{
			str = "The target file is null.";
			return str;
		}
		
		if (!file1.exists())
		{
			str = file1.getName() + " does not exist.";
			return str;
		}
		
		if (!file1.isFile())
		{
			str = file1.getName() + " is not a file.";
			return str;
		}
		
		if (!file1.canRead())
		{
			str = file1.getName() + " could not be read.";
			return str;
		}
		
		try
		{
			if (!file2.getParentFile().exists())
			{
				file2.getParentFile().mkdirs();
			}
		}
		catch (Exception ex)
		{
			return file2.getName() +  " make directory error.";
		}
		
		try
		{
			if (file2.exists())
			{
				file2.delete();
			}
		}
		catch (Exception ex)
		{
			return file2.getName() +  " delete file error.";
		}

		if (file2.exists() && !file2.canWrite())
		{
			str = file2.getName() + " could not be write.";
			return str;
		}
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try
		{
			fis = new FileInputStream(file1);
			fos = new FileOutputStream(file2);

			int nRead = 0;
			byte[] bytes = new byte[1024];
			
			while ((nRead = fis.read(bytes)) > 0)
			{
				fos.write(bytes, 0, nRead);
			}
		}
		catch (Exception ex)
		{
			return "Copy file error.";
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
				if (fos != null) fos.close();
			}
			catch (Exception ex)
			{
				return "Copy file error.";
			}
		}
		
		return str;
	}
	
	/**
	 * 拷贝字符串链表
	 * @param list1 源链表
	 * @param list2 目标目标
	 */
	public static void copyStrList(List<String> list1, List<String> list2)
	{
		try
		{
			// 检测链表
			if (list2 == null) return;

			// 清空目标链表
			list2.clear();

			// 检测链表
			if (list1 == null) return;

			// 数量
			int nSize1 = list1.size();
			if (nSize1 < 1) return;
			
			for (int i = 0; i < nSize1; i++)
			{
				// 字符串
				String str1 = (String)list1.get(i);
				String str2 = new String(str1);
				
				// 添加
				SLog(TAG, "AddString: " + str2);
				list2.add(str2);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 检测字符串已包含在链表中
	 * @param list 链表
	 * @param str 字符串
	 * @return
	 */
	public static boolean checkList(List<String> list, final String str)
	{
		try
		{
			if (list == null || str == null) return false;
	
			boolean bContains = list.contains(str);
//			if (!bContains) SLog(TAG, str);
			return bContains;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 检测文件是否为 UTF-8 编码
	 * @param file
	 * @return
	 */
	private static boolean isUTF8(File file)
	{
		FileInputStream is = null;
		BufferedInputStream bis = null;

		try
		{
			// 输入流
			is = new FileInputStream(file);
			bis = new BufferedInputStream(is);

			// 标记当前位置
			bis.mark(3);

			// 读取前 3 个字节
			byte[] first3Bytes = new byte[3];
			bis.read(first3Bytes);

			// 如果前三字节为 0xEFBBBF, 则为带签名的 UTF-8
			if (first3Bytes[0] == (byte)0xEF &&
				first3Bytes[1] == (byte)0xBB &&
				first3Bytes[2] == (byte)0xBF)
			{
				// 关闭
				if (bis != null) bis.close();
				return true;
			}
		}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				// 关闭
				if (bis != null) bis.close();
			}
			catch (Exception e)
			{
			}
		}
		
		return false;
	}

	/**
	 * 检测是否为整型字符串
	 * @param str
	 * @return
	 */
	private static boolean isIntStr(final String str)
	{
		try
		{
			if (str == null) return false;
			
			int nLen = str.length();
			if (nLen < 1) return false;
			
			for (int i = 0; i < nLen; i++)
			{
				char ch = str.charAt(i);
				
				if (ch < '0' || ch > '9') return false;
			}
			
			return true;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}

	/**
	 * 检测是否为双精度字符串
	 * @param str
	 * @return
	 */
	private static boolean isDblStr(final String str)
	{
		try
		{
			if (str == null) return false;
			
			int nLen = str.length();
			if (nLen < 1) return false;
			
			for (int i = 0; i < nLen; i++)
			{
				char ch = str.charAt(i);
				
				if (ch != '.' && (ch < '0' || ch > '9')) return false;
			}
			
			return true;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
		
	/**
	 * 写文本文件
	 * @param strFileName
	 * @param strText
	 * @param bAppend
	 */
	/*public static void writeTxtFile(final String strFileName, final String strText, final boolean bAppend)
	{
		try
		{
			// 直接写入
			if (!bAppend)
			{
				// 文件
				File file = new File(strFileName);

				// 检测文件
				if (!file.exists() || !file.isFile())
				{
					// 新建文件
					file.createNewFile();
					file = new File(strFileName);
				}
				
				// 输出流
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(strText.getBytes());
				fos.flush();
				fos.close();
			}
			// 追加写入
			else
			{
				// 随机访问文件, 读写方式
				RandomAccessFile file = new RandomAccessFile(strFileName, "rw");

				// 文件指针移到文件尾
				file.seek(file.length());
				
				// 写入字节
				file.writeBytes(strText);
				file.close();
			}
		}
		catch (Exception ex)
		{
		}
	}*/
	
	/**
	 * 分割字符串
	 * @param str 字符串
	 * @param strSeparator 分割符
	 * @param strArray 字符串数组
	 * @return 字符串数组中元素的个数
	 */
	public static int splitString(final String str, final String strSeparator, List<String> strArray)
	{
		try
		{
			int nSize = 0;
			int nStart = 0;
			
			String strTemp = "";
			
			if (str == null || strSeparator == null)
			{
				return 0;
			}
			
			// 长度
			int nLen = str.length();
			
			if (nLen <= 0)
			{
				return 0;
			}
			
			if (strSeparator.length() <= 0)
			{
				return 0;
			}
			
			if (strArray != null)
			{
				// 清空
				strArray.clear();
			}
			else
			{
				strArray = new ArrayList<String>();
			}
			
			// 查找子字符串的第一个字索引值
			int nPos = str.indexOf(strSeparator);
			
			if (nPos != -1)
			{
				while (nPos != -1)
				{
					if (nStart >= 0 && nPos < nLen)
					{
						// 提取起始位置位于 nStart 的长度为 nPos - nStart 个字符的子字符串
						strTemp = str.substring(nStart, nPos);
			
						// 在数组的末尾添加一个元素
						strArray.add(strTemp);
			
						nStart = nPos + strSeparator.length();
			
						// 从位置 nStart 开始查找子字符串的第一个字索引值
						nPos = str.indexOf(strSeparator, nStart);
					}
				}
				
				// 检测字符的计数值
				if (nStart < nLen)
				{
					// 提取起始位置位于 nStart 的长度为 nLen - nStart 个字符的子字符串
					strTemp = str.substring(nStart);
		
					// 在数组的末尾添加一个元素
					strArray.add(strTemp);
				}
		
				// 获取数组中的元素个数
				nSize = (int)strArray.size();
			}
			
			return nSize;
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	/**
	 * 读文本行
	 * @param br 读缓冲器
	 * @return 字符串
	 */
	private static String readLine(BufferedReader br)
	{
		if (br == null) return null;
		
		String str = "";
		
		try
		{
			// 读文本行
			str = br.readLine();
			
			if (str != null)
			{
				// 替换换行回车
				str = str.replaceAll("\r|\n", "");
				
				// 检测长度
				if (str.length() > 1)
				{
					char c = str.charAt(0);
					String strC = String.valueOf(c);
					
					// 当编码格式为 UTF-8 时, 读取的第一个字符为 65279, 调试时无法显示该字符
					if (c == 65279 || (strC != null && strC.hashCode() == 65279))
					{
						str = str.substring(1);
					}
				}
			}
			else
			{
				return null;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return str;
	}
	
	/**
	 * 写文本行
	 * @param bw 写缓冲器
	 * @param strLine 字符串行
	 */
	private static void writeLine(BufferedWriter bw, final String strLine)
	{
		try
		{
			if (bw == null) return;
			
			String str = strLine + "\r\n";
			
			try
			{
				// 写文本
				bw.write(str);
			}
			catch (IOException e)
			{
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 读 user.txt 文件
	 */
	public static void readUserFile()
	{
		try
		{
			// 文件名
			String strFileName = ms_strDefPath + "user.txt";
			
			// 文件
			File file = new File(strFileName);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;
			
			// 字符集编码
			String strCsEnc = "GBK";
			
			// 检测文件是否为 UTF-8 编码
			if (isUTF8(file)) strCsEnc = "UTF-8";

			String strLine = null;
			
			// 读文件
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, strCsEnc);
			BufferedReader br = new BufferedReader(isr);
			
			try
			{
				// 清空链表
				ms_listUserL.clear();
				
				// 读文本行
				while ((strLine = readLine(br)) != null)
				{
					// User 链表
					ms_listUserL.add(strLine);
				}
			}
			catch (Exception e)
			{
			}
			finally
			{
				try
				{
					// 关闭
					if (br != null) br.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 读 data.txt 文件
	 */
	public static void readDataFile()
	{
		try
		{
			// 文件名
			String strFileName = ms_strDefPath + "data.txt";
			
			// 文件
			File file = new File(strFileName);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;
			
			// 字符集编码
			String strCsEnc = "GBK";
			
			// 检测文件是否为 UTF-8 编码
			if (isUTF8(file)) strCsEnc = "UTF-8";

			String strLine = null;
			List<String> listUserData = new ArrayList<String>();
			
			// 读文件
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, strCsEnc);
			BufferedReader br = new BufferedReader(isr);
			
			try
			{
				// 读文本行
				while ((strLine = readLine(br)) != null)
				{
					// User 链表
					listUserData.add(strLine);
				}
			}
			catch (Exception e)
			{
			}
			finally
			{
				try
				{
					// 关闭
					if (br != null) br.close();
				}
				catch (IOException e)
				{
				}
			}
			
			// 数量
			int nSizeStr = listUserData.size();
			if (nSizeStr <= 0) return;

			// 清空链表
			ms_listUserDataL.clear();
			
			List<String> strArray = new ArrayList<String>();
			
			for (int i = 0; i < nSizeStr; i++)
			{
				UserData user = new UserData();
				
				// 分割字符串
				int nSize = splitString(listUserData.get(i), ",", strArray);
				if (nSize < 6) continue;
				
				// 用户编号
				String strNum = strArray.get(0);
				if (!strIsInt(strNum)) continue;
				int nNum = str2int(strNum);
				user.setNum(nNum);
				
				// 当前时间
				String strCurTime = strArray.get(1);
				long lTime = time2long(strCurTime);
				user.setCurTime(lTime);
				
				// 距离
				String strDist = strArray.get(2);
				double dist = str2dbl(strDist);
				user.setDist(dist);
				
				// 步数
				String strStep = strArray.get(3);
				int nStep = str2int(strStep);
				user.setStep(nStep);
				
				// 卡路里
				String strCalorie = strArray.get(4);
				int nCalorie = str2int(strCalorie);
				user.setCalo(nCalorie);
				
				// 锻炼时长
				String strTime = strArray.get(5);
				int nTime = str2int(strTime);
				user.setTime(nTime);
				
				if (nSize >= 7)
				{
					// 是否已经上传
					boolean bUploaded = false;
					String strUploaded = strArray.get(6);
					if (strUploaded.equals("1")) bUploaded = true;
					user.setUploaded(bUploaded);
				}
				
				// 添加
//				user.add(ms_listUserData);
				ms_listUserDataL.add(user);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写 data.txt 文件
	 */
	public static void writeDataFile()
	{
		try
		{
			// 文件名
			String strFileName = ms_strDefPath + "data.txt";
			
			BufferedWriter bw = null;
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			
			// 文件
			File file = new File(strFileName);
			
			try
			{
				// 写文件
				fos = new FileOutputStream(file);
				osw = new OutputStreamWriter(fos, "GBK");
				bw = new BufferedWriter(osw);
				
				try
				{
					// 数量
					int nSize = ms_listUserDataL.size();
					
					for (int i = 0; i < nSize; i++)
					{
						// 数据
						UserData data = ms_listUserDataL.get(i);
						
						// 用户编号
						int nNum = data.getNum();
						String strUser = String.valueOf(nNum);
						
						// 时间
						long lCurTime = data.getCurTime();
						String strCurTime = String.valueOf(lCurTime);
						
						// 距离
						double dist = data.getDist();
						String strDist = String.format("%.3f", dist);
						
						// 步数
						int nStep = data.getStep();
						String strStep = String.format("%d", nStep);
						
						// 卡路里
						int nCalo = data.getCalo();
						String strCalo = String.format("%d", nCalo);
						
						// 锻炼时长
						int nTime = data.getTime();
						String strTime = String.format("%d", nTime);
						
						// 是否已经上传
						String strUploaded = "0";
						if (data.isUploaded()) strUploaded = "1";
						
						// 数据
						String strData = strUser + "," + strCurTime + "," + strDist + "," + strStep + "," + strCalo + "," + strTime + "," + strUploaded;
						writeLine(bw, strData);
					}
				}
				catch (Exception e)
				{
				}
				finally
				{
					// 关闭
					if (bw != null) bw.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写 user.txt 文件
	 * @param strUser 用户数据
	 */
	private static void writeUserFile(final String strUser)
	{
		try
		{
			int nIndex = -1;
			
			if (strUser == null || strUser.length() < 3) return;
			
			// 前 3 个字符
			String strUser3 = strUser.substring(0, 3);
			
			// 读 user.txt 文件
			readUserFile();
			
			// 数量
			int nSize = ms_listUserL.size();
			
			for (int i = 0; i < nSize; i++)
			{
				// 检测用户数据
				if (ms_listUserL.get(i).startsWith(strUser3))
				{
					nIndex = i;
					break;
				}
			}
			
			if (nIndex == -1)
			{
				// 添加用户数据
				ms_listUserL.add(strUser);
			}
			else
			{
				if (nIndex < 0 || nIndex > nSize - 1) return;
				
				// 设置用户数据
				ms_listUserL.set(nIndex, strUser);
			}
			
			// 文件名
			String strFileName = ms_strDefPath + "user.txt";
			
			BufferedWriter bw = null;
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			
			// 文件
			File file = new File(strFileName);
			
			try
			{
				// 写文件
				fos = new FileOutputStream(file);
				osw = new OutputStreamWriter(fos, "GBK");
				bw = new BufferedWriter(osw);
				
				try
				{
					String strLine = "";

					// 数量
					nSize = ms_listUserL.size();
					
					for (int i = 0; i < nSize; i++)
					{
						// 数据
						strLine = ms_listUserL.get(i);
						writeLine(bw, strLine);
					}
				}
				catch (Exception e)
				{
				}
				finally
				{
					// 关闭
					if (bw != null) bw.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写文本文件
	 * @param strFileName
	 * @param strText
	 * @param bAppend
	 */
	public static void writeTxtFile(final String strFileName, final String strText, final boolean bAppend)
	{
		try
		{
			// 直接写入
			if (!bAppend)
			{
				// 文件
				File file = new File(strFileName);

				// 检测文件
				if (!file.exists() || !file.isFile())
				{
					// 新建文件
					file.createNewFile();
					file = new File(strFileName);
				}
				
				// 输出流
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(strText.getBytes());
				fos.flush();
				fos.close();
			}
			// 追加写入
			else
			{
				// 随机访问文件, 读写方式
				RandomAccessFile file = new RandomAccessFile(strFileName, "rw");

				// 文件指针移到文件尾
				file.seek(file.length());
				
				// 写入字节
				file.writeBytes(strText);
				file.close();
			}
		}
		catch (Exception ex)
		{
		}
	}
		
	//----------------------------------------------------------------------------------------------------
	/**
	 * 绘制图片
	 */
	public static void drawBmp()
	{
		try
		{
			// 启用悬浮窗单个开关
			if (FLOAT_SWITCH)
			{
				// 关闭悬浮窗
				if (!ms_bFloatOn)
				{
					ms_jpgView.invalidate();
					ms_osdView1.invalidate();
					ms_osdView2.invalidate();
					ms_osdView3.invalidate();
					ms_osdView4.invalidate();
				}
				// 打开悬浮窗
				else
				{
					ms_osdViewTop1.invalidate();
					ms_osdViewTop2.invalidate();
					ms_osdViewBottom1.invalidate();
					ms_osdViewBottom2.invalidate();
				}
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 打开顶部悬浮窗
				if (ms_bFloatTopOn)
				{
					ms_osdViewTop1.invalidate();
					ms_osdViewTop2.invalidate();
				}
				
				// 打开底部悬浮窗
				if (ms_bFloatBottomOn)
				{
					ms_osdViewBottom1.invalidate();
					ms_osdViewBottom2.invalidate();
				}
				
				// 关闭顶部和底部悬浮窗
				if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
				{
					ms_jpgView.invalidate();
					ms_osdView1.invalidate();
					ms_osdView2.invalidate();
					ms_osdView3.invalidate();
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 绘制图片
	 * @param canvas
	 * @param nMark
	 */
	public static void drawBmp(Canvas canvas, final int nMark)
	{
		if (canvas == null) return;
		
		try
		{
			// 绘制 JPG
			if ((nMark & MARK_JPG) > 0)
			{
				// 绘制 JPG 图片
				if (ms_bmpJpg != null) canvas.drawBitmap(ms_bmpJpg, 0, 0, null);
			}
			
			// 绘制 OSD1
			if ((nMark & MARK_OSD1) > 0)
			{
				// 图片数量
				int nSize1 = ms_listOsd1.size();
				
				for (int i = 0; i < nSize1; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsd1.get(i);
					
					// 坐标
					int nX1 = osd.getX1();
					int nY1 = osd.getY1();
	
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
				}
			}

			// 绘制 OSD2
			if ((nMark & MARK_OSD2) > 0)
			{
				// 图片数量
				int nSize2 = ms_listOsd2.size();
				
				for (int i = 0; i < nSize2; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsd2.get(i);
					
					// 坐标
					int nX1 = osd.getX1();
					int nY1 = osd.getY1();
	
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
				}
			}

			// 绘制 OSD3
			if ((nMark & MARK_OSD3) > 0)
			{
				// 图形数量
				int nSize3 = ms_listOsd3.size();
				
				for (int i = 0; i < nSize3; i++)
				{
					// OSD 图形
					OsdGraph osd = ms_listOsd3.get(i);
					
					// 绘制
					if (osd != null) osd.draw(canvas);
				}
			}

			// 绘制 OSD4
			if ((nMark & MARK_OSD4) > 0)
			{
				// 图片数量
				int nSize4 = ms_listOsd4.size();
//				float mAngle = 0.0f;
//				theApp.MYSLog(TAG,"nSize4="+ms_listOsd4.size());
				for (int i = 0; i < nSize4; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsd4.get(i);
					
					// 坐标
					int nX1 = osd.getX1();
					int nY1 = osd.getY1();
					
					//重置Matrix
//					theApp.getMatrix().reset(); 
					
					//设置旋转的角度
//					theApp.getMatrix().setRotate(getAngle());          
					
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
					
				    //构建经过处理的新的Bitmap
//			        Bitmap mBitmapRotate = Bitmap.createBitmap(bmp, 0, 0, 
//			        		bmp.getWidth(), bmp.getHeight(), getMatrix(), true);	
			        
//					if (mBitmapRotate != null) canvas.drawBitmap(mBitmapRotate, nX1, nY1, ms_paint);
				}
				
//				mAngle ++;
			}			
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * 绘制图片
	 * @param canvas
	 * @param nMark
	 */
	public static void drawBmpTop(Canvas canvas, final int nMark)
	{
		if (canvas == null) return;
		
		try
		{
			// 绘制 OSD1
			if ((nMark & MARK_OSD1) > 0)
			{
				// 图片数量
				int nSize1 = ms_listOsdTop1.size();
				
				for (int i = 0; i < nSize1; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsdTop1.get(i);
					
					// 坐标
					int nX1 = osd.getX1() - ms_nTopX1;
					int nY1 = osd.getY1() - ms_nTopY1;
	
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
				}
			}

			// 绘制 OSD2
			if ((nMark & MARK_OSD2) > 0)
			{
				// 图片数量
				int nSize2 = ms_listOsdTop2.size();
				
				for (int i = 0; i < nSize2; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsdTop2.get(i);
					
					// 坐标
					int nX1 = osd.getX1() - ms_nTopX1;
					int nY1 = osd.getY1() - ms_nTopY1;
	
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * 绘制图片
	 * @param canvas
	 * @param nMark
	 */
	public static void drawBmpBottom(Canvas canvas, final int nMark)
	{
		if (canvas == null) return;
		
		try
		{
			// 绘制 OSD1
			if ((nMark & MARK_OSD1) > 0)
			{
				// 图片数量
				int nSize1 = ms_listOsdBottom1.size();
				
				for (int i = 0; i < nSize1; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsdBottom1.get(i);
					
					// 坐标
					int nX1 = osd.getX1() - ms_nBottomX1;
					int nY1 = osd.getY1() - ms_nBottomY1;
	
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
				}
			}

			// 绘制 OSD2
			if ((nMark & MARK_OSD2) > 0)
			{
				// 图片数量
				int nSize2 = ms_listOsdBottom2.size();
				
				for (int i = 0; i < nSize2; i++)
				{
					// OSD 图片
					OsdBitmap osd = ms_listOsdBottom2.get(i);
					
					// 坐标
					int nX1 = osd.getX1() - ms_nBottomX1;
					int nY1 = osd.getY1() - ms_nBottomY1;
	
					// 绘制图片
					Bitmap bmp = osd.getBmp();
					if (bmp != null) canvas.drawBitmap(bmp, nX1, nY1, ms_paint);
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * 设置 JPG 图片
	 */
	public static void setJpg()
	{
		try
		{
			// JPG 路径
			String strJpgPath = "";
			
			// 路径编号
			String strPathNum = i2Str(ms_nJpgPathNum, 2);
			
			// 名称编号
			String strNameNum = i2Str(ms_nJpgNameNum, 2);

			// JPG 路径
//			strJpgPath = ms_strDefPath + "picture/" + ms_strLang + "/";
			strJpgPath = ms_strPicturePath + ms_strLang + "/";
			strJpgPath += strPathNum + "/" + strNameNum + ".jpg";	
			
			// 文件
			File file = new File(strJpgPath);
			
//			Toast.makeText(ms_curActivity, strJpgPath, Toast.LENGTH_LONG).show();
			
//			theApp.SLog(TAG, strJpgPath);
			
			// 检测文件
			if (!file.exists() || !file.isFile()) 
			{
				//图片文件不存在发送0x75键值
//				theApp.responseKey(0, 0, 75);
				
				return;	
			}
			
			// JPG 图片未切换
			if (strJpgPath.equals(ms_strJpgPath)) return;			
			
			try
			{
				// 已计算 JPG 的 CRC32
				if (ms_bJpgCrc32)
				{
					// 文件的 CRC32
					long lCrc32_1 = ms_jpgMap.get(strJpgPath);
					long lCrc32_2 = ms_jpgMap.get(ms_strJpgPath);
					if (lCrc32_1 == lCrc32_2) return;
				}
			}
			catch (Exception e)
			{
//				theApp.SLog(TAG, e.toString());
			}
			
			// JPG 图片路径
			ms_strJpgPath = strJpgPath;
			
			if (ms_bmpJpg != null)
			{
				// 回收旧的图片
//				ms_bmpJpg.recycle();
//				ms_bmpJpg = null;
				
				// 垃圾回收
//				System.gc();
//				System.runFinalization();
//				Runtime.getRuntime().gc();
			}
			
			// JPG 图片
			Bitmap bmp = decodeBitmap(strJpgPath);
			
//			Toast.makeText(ms_curActivity, "jpg路径"+strJpgPath, Toast.LENGTH_LONG).show();

			// 不缩放图片
			if (!theApp.SCALED_BMP)
			{
				// JPG 图片
				ms_bmpJpg = bmp;
			}
			// 缩放图片
			else
			{
				// 宽度和高度
				int nWB = bmp.getWidth();
				int nHB = bmp.getHeight();
				float fWR = ms_fWidthRatio;
				float fHR = ms_fHeightRatio;

				// 检测宽度和高度比例
				if (fWR != 1.0 && fHR != 1.0)
				{
					// 缩放图片
					Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
					
					// JPG 图片
					ms_bmpJpg = bmpNew;
					
					if (bmp != null && bmp != bmpNew)
					{
						// 回收旧的图片
						bmp.recycle();
						bmp = null;
						
						// 垃圾回收
//						System.gc();
						System.runFinalization();
//						Runtime.getRuntime().gc();
					}
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 设置 OSD1 图片
	 * @param nPathNum1 路径编号 1
	 * @param nNameNum1 名称编号 1
	 * @param nPathNum2 路径编号 2
	 * @param nNameNum2 名称编号 2
	 */
	public static void setBmpOsd1(final int nPathNum1, final int nNameNum1, final int nPathNum2, final int nNameNum2, boolean bAnimation,boolean bOsd4)
	{
		try
		{
			// 路径
			String strPath1 = "";
			String strPath2 = "";
			
			// 编号
			String strPathNum1 = i2Str(nPathNum1, 2);
			String strNameNum1 = i2Str(nNameNum1, 4);
			String strPathNum2 = i2Str(nPathNum2, 2);
			String strNameNum2 = i2Str(nNameNum2, 4);

			// 路径
//			strPath1 = ms_strDefPath + "picture/" + ms_strLang + "/";
//			strPath2 = ms_strDefPath + "picture/" + ms_strLang + "/";
			strPath1 = ms_strPicturePath + ms_strLang + "/";
			strPath2 = ms_strPicturePath + ms_strLang + "/";
			strPath1 += strPathNum1 + "/" + strNameNum1 + ".png";
			strPath2 += strPathNum2 + "/" + strNameNum2 + ".png";

			// 路径
			boolean bPath1 = true;
			boolean bPath2 = true;
			
			// 文件
			File file1 = new File(strPath1);
			File file2 = new File(strPath2);

			// 检测文件
			if (!file1.exists() || !file1.isFile()) 		bPath1 = false;
			if (!file2.exists() || !file2.isFile())			bPath2 = false;

		/*	if(!bPath1||!bPath2)
			{
				//图片文件不存在发送0x76键值
				if (!strNameNum1.equals("0000")&&!strNameNum2.equals("0000"))
				theApp.responseKey(0, 0, 76);
			}*/
			
			// 宽度和高度比例
			float fWR = ms_fWidthRatio;
			float fHR = ms_fHeightRatio;
			
			// OSD1 图片 1
			if (bPath1)
			{
				// OSD1 图片 1
				Bitmap bmp = decodeBitmap(strPath1);
				
				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setPath(strPath1);
				osd.setX1(theApp.getOsd11X());
				osd.setY1(theApp.getOsd11Y());
				osd.setX2(theApp.getOsd11X() + bmp.getWidth());
				osd.setY2(theApp.getOsd11Y() + bmp.getHeight());
//				theApp.MYSLog(TAG,"aaaa");
				
				// 添加 OSD 图片
				if (bOsd4) addOsdBmp(4, osd, bAnimation);
				else addOsdBmp(1, osd, bAnimation);
			}
			
			// OSD1 图片 2
			if (bPath2)
			{
				// OSD1 图片 1
				Bitmap bmp = decodeBitmap(strPath2);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setPath(strPath2);
				osd.setX1(theApp.getOsd12X());
				osd.setY1(theApp.getOsd12Y());
				osd.setX2(theApp.getOsd12X() + bmp.getWidth());
				osd.setY2(theApp.getOsd12Y() + bmp.getHeight());
				
				// 添加 OSD 图片
//				addOsdBmp(4, osd, bAnimation);
				if (bOsd4) addOsdBmp(4, osd, bAnimation);
				else addOsdBmp(1, osd, bAnimation);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 设置 OSD2 图片
	 * @param buffer
	 */
	public static void setBmpOsd2(byte[] buffer)
	{
		try
		{
			if (buffer == null) return;
			if (buffer.length < COMM_BYTE_NUM) return;
	
			// JPG 路径编号
			int nJpgPathNum = bcd2int(buffer[3]);
			theApp.setJpgPathNum(nJpgPathNum);
			
			// JPG 名称编号
			int nJpgNameNum = bcd2int(buffer[4]);
			theApp.setJpgNameNum(nJpgNameNum);
	
			// 设置 JPG 图片
			setJpg();
	
			// 宽度和高度比例
			float fWR = ms_fWidthRatio;
			float fHR = ms_fHeightRatio;
			
			// OSD2 坐标
			int nX11 = bcd2int(buffer[5]);
			int nX12 = bcd2int(buffer[6]);
			int nY11 = bcd2int(buffer[7]);
			int nY12 = bcd2int(buffer[8]);
			int nX1 = (int)((nX11 * 100 + nX12) * fWR);
			int nY1 = (int)((nY11 * 100 + nY12) * fHR);
			
			if (theApp.SCALED_XY)
			{
				 nX1 = (int)((nX11 * 100 + nX12) * fWR + 0.5);
				 nY1 = (int)((nY11 * 100 + nY12) * fHR + 0.5);
			}
			
			// 间距
			int nSpace = bcd2int(buffer[9]);
			
			// 路径编号
			int nPathNum = bcd2int(buffer[11]);
			String strPathNum = i2Str(nPathNum, 2);
			
			// 名称编号
			int nNameNum1 = bcd2int(buffer[12]);
			int nNameNum2 = bcd2int(buffer[13]);
			int nNameNum3 = bcd2int(buffer[14]);
			int nNameNum4 = bcd2int(buffer[15]);
			int nNameNum5 = bcd2int(buffer[16]);
			int nNameNum6 = bcd2int(buffer[17]);
			int nNameNum7 = bcd2int(buffer[18]);
			int nNameNum8 = bcd2int(buffer[19]);
			int nNameNum9 = bcd2int(buffer[20]);
			
			//是否是osd4
			int nOsd4 = bcd2int(buffer[21]);
			if (nOsd4 == 4) ms_bOsd4 = true;
			else ms_bOsd4 = false;
				
			// 编号
			String strNameNum1 = i2Str(nNameNum1, 2);
			String strNameNum2 = i2Str(nNameNum2, 2);
			String strNameNum3 = i2Str(nNameNum3, 2);
			String strNameNum4 = i2Str(nNameNum4, 2);
			String strNameNum5 = i2Str(nNameNum5, 2);
			String strNameNum6 = i2Str(nNameNum6, 2);
			String strNameNum7 = i2Str(nNameNum7, 2);
			String strNameNum8 = i2Str(nNameNum8, 2);
			String strNameNum9 = i2Str(nNameNum9, 2);
	
			// 路径
			String strPath1 = "";
			String strPath2 = "";
			String strPath3 = "";
			String strPath4 = "";
			String strPath5 = "";
			String strPath6 = "";
			String strPath7 = "";
			String strPath8 = "";
			String strPath9 = "";
	
			// 路径
/*			strPath1 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath2 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath3 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath4 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath5 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath6 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath7 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath8 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;
			strPath9 = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum;*/
			strPath1 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath2 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath3 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath4 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath5 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath6 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath7 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath8 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			strPath9 = ms_strPicturePath + ms_strLang + "/" + strPathNum;
			
			// 路径
			strPath1 += "/" + strNameNum1 + ".png";
			strPath2 += "/" + strNameNum2 + ".png";
			strPath3 += "/" + strNameNum3 + ".png";
			strPath4 += "/" + strNameNum4 + ".png";
			strPath5 += "/" + strNameNum5 + ".png";
			strPath6 += "/" + strNameNum6 + ".png";
			strPath7 += "/" + strNameNum7 + ".png";
			strPath8 += "/" + strNameNum8 + ".png";
			strPath9 += "/" + strNameNum9 + ".png";
			
			// 修正 PNG 图片路径
			strPath1 = correctPngPath(nNameNum1, strPath1);
			strPath2 = correctPngPath(nNameNum2, strPath2);
			strPath3 = correctPngPath(nNameNum3, strPath3);
			strPath4 = correctPngPath(nNameNum4, strPath4);
			strPath5 = correctPngPath(nNameNum5, strPath5);
			strPath6 = correctPngPath(nNameNum6, strPath6);
			strPath7 = correctPngPath(nNameNum7, strPath7);
			strPath8 = correctPngPath(nNameNum8, strPath8);
			strPath9 = correctPngPath(nNameNum9, strPath9);
			
			// 路径
			boolean bPath1 = true;
			boolean bPath2 = true;
			boolean bPath3 = true;
			boolean bPath4 = true;
			boolean bPath5 = true;
			boolean bPath6 = true;
			boolean bPath7 = true;
			boolean bPath8 = true;
			boolean bPath9 = true;
	
			// 文件
			File file1 = new File(strPath1);
			File file2 = new File(strPath2);
			File file3 = new File(strPath3);
			File file4 = new File(strPath4);
			File file5 = new File(strPath5);
			File file6 = new File(strPath6);
			File file7 = new File(strPath7);
			File file8 = new File(strPath8);
			File file9 = new File(strPath9);
	
			// 检测文件
			if (!file1.exists() || !file1.isFile()) bPath1 = false;
			if (!file2.exists() || !file2.isFile()) bPath2 = false;
			if (!file3.exists() || !file3.isFile()) bPath3 = false;
			if (!file4.exists() || !file4.isFile()) bPath4 = false;
			if (!file5.exists() || !file5.isFile()) bPath5 = false;
			if (!file6.exists() || !file6.isFile()) bPath6 = false;
			if (!file7.exists() || !file7.isFile()) bPath7 = false;
			if (!file8.exists() || !file8.isFile()) bPath8 = false;
			if (!file9.exists() || !file9.isFile()) bPath9 = false;
			
			//图片文件不存在发送0x76键值
		/*	String strPathNone = ms_strDefPath + "picture/" + ms_strLang + "/" + strPathNum+ "/" + "0000" + ".png";
			if(!bPath1||!bPath2||!bPath3||!bPath4||!bPath5||!bPath6||!bPath7||!bPath8||!bPath9)
			{		
				if (!strPath1.equals(strPathNone)&&!strPath2.equals(strPathNone)&&!strPath3.equals(strPathNone)&&!strPath4.equals(strPathNone)
				&&!strPath5.equals(strPathNone)&&!strPath6.equals(strPathNone)&&!strPath7.equals(strPathNone)&&!strPath8.equals(strPathNone)&&!strPath9.equals(strPathNone))
				
					theApp.responseKey(0, 0, 76);
			}*/
			
			// 宽度和高度
			int nW1 = 0;
			int nW2 = 0;
			int nW3 = 0;
			int nW4 = 0;
			int nW5 = 0;
			int nW6 = 0;
			int nW7 = 0;
			int nW8 = 0;
			int nH1 = 0;
			int nH2 = 0;
			int nH3 = 0;
			int nH4 = 0;
			int nH5 = 0;
			int nH6 = 0;
			int nH7 = 0;
			int nH8 = 0;
			
			// 等高度(Y 坐标相同)
			if (buffer[10] == (byte)0x00)
			{
				// OSD2 Y 坐标
				theApp.setOsd21Y(nY1);
				theApp.setOsd22Y(nY1);
				theApp.setOsd23Y(nY1);
				theApp.setOsd24Y(nY1);
				theApp.setOsd25Y(nY1);
				theApp.setOsd26Y(nY1);
				theApp.setOsd27Y(nY1);
				theApp.setOsd28Y(nY1);
				theApp.setOsd29Y(nY1);
			}
			
			// 等宽度(X 坐标相同)
			if (buffer[10] == (byte)0x01)
			{
				// OSD2 X 坐标
				theApp.setOsd21X(nX1);
				theApp.setOsd22X(nX1);
				theApp.setOsd23X(nX1);
				theApp.setOsd24X(nX1);
				theApp.setOsd25X(nX1);
				theApp.setOsd26X(nX1);
				theApp.setOsd27X(nX1);
				theApp.setOsd28X(nX1);
				theApp.setOsd29X(nX1);
			}
			
			// OSD2 图片 1
			if (bPath1)
			{
				// OSD2 图片 1
				Bitmap bmp = decodeBitmap(strPath1);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW1 = bmp.getWidth();
				nH1 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd21X(nX1);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd21Y(nY1);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd21X());
				osd.setY1(theApp.getOsd21Y());
				osd.setX2(theApp.getOsd21X() + bmp.getWidth());
				osd.setY2(theApp.getOsd21Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 2
			if (bPath2)
			{
				// OSD2 图片 2
				Bitmap bmp = decodeBitmap(strPath2);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
					
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW2 = bmp.getWidth();
				nH2 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd22X(theApp.getOsd21X() + nW1 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd22Y(theApp.getOsd21Y() + nH1 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd22X());
				osd.setY1(theApp.getOsd22Y());
				osd.setX2(theApp.getOsd22X() + bmp.getWidth());
				osd.setY2(theApp.getOsd22Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 3
			if (bPath3)
			{
				// OSD2 图片 3
				Bitmap bmp = decodeBitmap(strPath3);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW3 = bmp.getWidth();
				nH3 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd23X(theApp.getOsd22X() + nW2 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd23Y(theApp.getOsd22Y() + nH2 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd23X());
				osd.setY1(theApp.getOsd23Y());
				osd.setX2(theApp.getOsd23X() + bmp.getWidth());
				osd.setY2(theApp.getOsd23Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 4
			if (bPath4)
			{
				// OSD2 图片 4
				Bitmap bmp = decodeBitmap(strPath4);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW4 = bmp.getWidth();
				nH4 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd24X(theApp.getOsd23X() + nW3 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd24Y(theApp.getOsd23Y() + nH3 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd24X());
				osd.setY1(theApp.getOsd24Y());
				osd.setX2(theApp.getOsd24X() + bmp.getWidth());
				osd.setY2(theApp.getOsd24Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 5
			if (bPath5)
			{
				// OSD2 图片 5
				Bitmap bmp = decodeBitmap(strPath5);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW5 = bmp.getWidth();
				nH5 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd25X(theApp.getOsd24X() + nW4 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd25Y(theApp.getOsd24Y() + nH4 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd25X());
				osd.setY1(theApp.getOsd25Y());
				osd.setX2(theApp.getOsd25X() + bmp.getWidth());
				osd.setY2(theApp.getOsd25Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 6
			if (bPath6)
			{
				// OSD2 图片 6
				Bitmap bmp = decodeBitmap(strPath6);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW6 = bmp.getWidth();
				nH6 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd26X(theApp.getOsd25X() + nW5 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd26Y(theApp.getOsd25Y() + nH5 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd26X());
				osd.setY1(theApp.getOsd26Y());
				osd.setX2(theApp.getOsd26X() + bmp.getWidth());
				osd.setY2(theApp.getOsd26Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 7
			if (bPath7)
			{
				// OSD2 图片 7
				Bitmap bmp = decodeBitmap(strPath7);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW7 = bmp.getWidth();
				nH7 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd27X(theApp.getOsd26X() + nW6 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd27Y(theApp.getOsd26Y() + nH6 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd27X());
				osd.setY1(theApp.getOsd27Y());
				osd.setX2(theApp.getOsd27X() + bmp.getWidth());
				osd.setY2(theApp.getOsd27Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 8
			if (bPath8)
			{
				// OSD2 图片 8
				Bitmap bmp = decodeBitmap(strPath8);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// 宽度和高度
				nW8 = bmp.getWidth();
				nH8 = bmp.getHeight();
	
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd28X(theApp.getOsd27X() + nW7 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd28Y(theApp.getOsd27Y() + nH7 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd28X());
				osd.setY1(theApp.getOsd28Y());
				osd.setX2(theApp.getOsd28X() + bmp.getWidth());
				osd.setY2(theApp.getOsd28Y() + bmp.getHeight());
				
				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
			
			// OSD2 图片 9
			if (bPath9)
			{
				// OSD2 图片 9
				Bitmap bmp = decodeBitmap(strPath9);

				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// OSD2 X 坐标
				if (buffer[10] == (byte)0x00) theApp.setOsd29X(theApp.getOsd28X() + nW8 + nSpace);
				
				// OSD2 Y 坐标
				if (buffer[10] == (byte)0x01) theApp.setOsd29Y(theApp.getOsd28Y() + nH8 + nSpace);
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setX1(theApp.getOsd29X());
				osd.setY1(theApp.getOsd29Y());
				osd.setX2(theApp.getOsd29X() + bmp.getWidth());
				osd.setY2(theApp.getOsd29Y() + bmp.getHeight());

				// 添加 OSD 图片
				if (ms_bOsd4) addOsdBmp(4, osd, false);
				else addOsdBmp(2, osd, false);
			}
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 设置 OSD4图片
	 * @param nPathNum1 路径编号 1
	 * @param nNameNum1 名称编号 1
	 * @param nPathNum2 路径编号 2
	 * @param nNameNum2 名称编号 2
	 */
	public static void setBmpOsd4(final int nPathNum1, final int nNameNum1, final int nPathNum2, final int nNameNum2, boolean bAnimation,boolean bOsd4)
	{

		try
		{
			// 路径
			String strPath1 = "";
			String strPath2 = "";
			
			// 编号
			String strPathNum1 = i2Str(nPathNum1, 2);
			String strNameNum1 = i2Str(nNameNum1, 4);
			String strPathNum2 = i2Str(nPathNum2, 2);
			String strNameNum2 = i2Str(nNameNum2, 4);

			// 路径
//			strPath1 = ms_strDefPath + "picture/" + ms_strLang + "/";
//			strPath2 = ms_strDefPath + "picture/" + ms_strLang + "/";
			strPath1 = ms_strPicturePath + ms_strLang + "/";
			strPath2 = ms_strPicturePath + ms_strLang + "/";
			strPath1 += strPathNum1 + "/" + strNameNum1 + ".png";
			strPath2 += strPathNum2 + "/" + strNameNum2 + ".png";

			// 路径
			boolean bPath1 = true;
//			boolean bPath2 = true;
			
			// 文件
			File file1 = new File(strPath1);
			File file2 = new File(strPath2);

			// 检测文件
			if (!file1.exists() || !file1.isFile()) 		bPath1 = false;
//			if (!file2.exists() || !file2.isFile())			bPath2 = false;

		/*	if(!bPath1||!bPath2)
			{
				//图片文件不存在发送0x76键值
				if (!strNameNum1.equals("0000")&&!strNameNum2.equals("0000"))
				theApp.responseKey(0, 0, 76);
			}*/
			
			// 宽度和高度比例
//			float fWR = ms_fWidthRatio;
//			float fHR = ms_fHeightRatio;
			
			// OSD1 图片 1
			if (bPath1)
			{				
				// OSD1 图片 1
				Bitmap m_bmp1 = decodeBitmap(strPath1);

				// 视图的宽度和高度
				int nWV = theApp.getOsd11X()*2 + m_bmp1.getWidth();
				int nHV = theApp.getOsd11Y()*2 + m_bmp1.getHeight();
				
				// 旋转图片的角度和弧度
				float fDH = (float)theApp.getAngle(); 
				float fRH = (float)(fDH * PIDiv180);

				// 旋转图片的宽度和高度
				int nWH1 = m_bmp1.getWidth();
				int nHH1 = m_bmp1.getHeight();
	
				// 旋转图片矩阵
				Matrix mtxH = new Matrix();
				mtxH.setRotate(fDH);
				
				// 旋转后的时针图片
				Bitmap m_bmp2 = Bitmap.createBitmap(m_bmp1, 0, 0, nWH1, nHH1, mtxH, true);
	
				// 旋转后图片的宽度和高度
				int nWH2 = m_bmp2.getWidth();
				int nHH2 = m_bmp2.getHeight();
				
				// 旋转后图片的左上角坐标
				float fLH = (nWV - nWH2) / 2;
				float fTH = (nHV - nHH2) / 2;			

				// 轴心的坐标
				int nPivotXH0 = theApp.getOsd12X();
				int nPivotYH0 = theApp.getOsd12Y();
				
				// 轴心的偏移量
				int nOffsetYH0 = nPivotYH0 - nHH1 / 2;
				
				// 时针轴心的偏移坐标
				float fOffsetXH = nPivotXH0 - nWH2 / 2;
				float fOffsetYH = nPivotYH0 - nHH2 / 2;
				
				// 旋转后图片角度的正弦和正弦
				float fSinH = (float)Math.sin(fRH);
				float fCosH = (float)Math.cos(fRH);

				// 旋转后图片轴心的偏移量
				fOffsetXH = nOffsetYH0 * fSinH;
				fOffsetYH = nOffsetYH0 * fCosH;

				// 旋转后图片的左上角坐标
				fLH += fOffsetXH;
				fTH -= fOffsetYH;	
				
			/*	//重置Matrix
				theApp.getMatrix().reset(); 
				
				//设置旋转的角度
				theApp.getMatrix().setRotate(getAngle(),theApp.getOsd12X(),theApp.getOsd12Y());  
				
			    //构建经过处理的新的Bitmap
		        Bitmap mBitmapRotate = Bitmap.createBitmap(bmp, 0, 0, 
		        		bmp.getWidth(), bmp.getHeight(), getMatrix(), true);	
		        bmp = mBitmapRotate;*/

				// OSD 图片
				OsdBitmap osd = new OsdBitmap(m_bmp2);
				osd.setPath(strPath1);
				osd.setX1((int)fLH);
				osd.setY1((int)fTH);
				osd.setX2((int)fLH + m_bmp2.getWidth());
				osd.setY2((int)fTH + m_bmp2.getHeight());
//				theApp.MYSLog(TAG,"aaaa");
				
				// 添加 OSD 图片
				if (bOsd4) addOsdBmp(4, osd, bAnimation);
				else addOsdBmp(1, osd, bAnimation);
			}
			
			// OSD1 图片 2
		/*	if (bPath2)
			{
				// OSD1 图片 1
				Bitmap bmp = decodeBitmap(strPath2);
				
				//重置Matrix
				theApp.getMatrix().reset(); 
				
				//设置旋转的角度
				theApp.getMatrix().setRotate(getAngle());  
				
			    //构建经过处理的新的Bitmap
		        Bitmap mBitmapRotate = Bitmap.createBitmap(bmp, 0, 0, 
		        		bmp.getWidth(), bmp.getHeight(), getMatrix(), true);	
		        bmp = mBitmapRotate;
		        
				// 缩放图片
				if (theApp.SCALED_BMP)
				{
					// 检测宽度和高度比例
					if (fWR != 1.0 && fHR != 1.0)
					{
						// 宽度和高度
						int nWB = bmp.getWidth();
						int nHB = bmp.getHeight();
						
						// 缩放图片
						Bitmap bmpNew = Bitmap.createScaledBitmap(bmp, (int)(nWB * fWR), (int)(nHB * fHR), false);
						
						if (bmp != null && bmp != bmpNew)
						{
							// 回收旧的图片
							bmp.recycle();
							bmp = null;
							
							// 垃圾回收
//							System.gc();
							System.runFinalization();
//							Runtime.getRuntime().gc();
						}
	
						// 缩放图片
						bmp = bmpNew;
					}
				}
				
				// OSD 图片
				OsdBitmap osd = new OsdBitmap(bmp);
				osd.setPath(strPath2);
				osd.setX1(theApp.getOsd12X());
				osd.setY1(theApp.getOsd12Y());
				osd.setX2(theApp.getOsd12X() + bmp.getWidth());
				osd.setY2(theApp.getOsd12Y() + bmp.getHeight());
				
				// 添加 OSD 图片
//				addOsdBmp(4, osd, bAnimation);
				if (bOsd4) addOsdBmp(4, osd, bAnimation);
				else addOsdBmp(1, osd, bAnimation);
			}*/
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 清 OSD3
	 * @param nX1
	 * @param nY1
	 * @param nX2
	 * @param nY2
	 */
	public static void clearOsd3(final int nX1, final int nY1, final int nX2, final int nY2)
	{
		// 图形数量
		int nSize3 = ms_listOsd3.size();
		
		for (int i = 0; i < nSize3; i++)
		{
			// OSD 图形
			OsdGraph osd = ms_listOsd3.get(i);
			
			// 判断是否与矩形相交
			if (osd.interOfRect(nX1, nY1, nX2, nY2))
			{
				i--;
				nSize3--;
				
				// 移除 OSD 图形
				ms_listOsd3.remove(osd);
			}
		}
	}
	
	/**
	 * 修正 PNG 图片路径
	 * @param nNameNum 名称编号
	 * @param strPath 路径
	 * @return
	 */
	public static String correctPngPath(final int nNameNum, final String strPath)
	{
		String strPathEx = strPath;

		// 文件
		File file = new File(strPathEx);

		// 检测文件
		if (!file.exists() || !file.isFile())
		{
			// 图片错误
//			String strErr = "Picture error: " + strPathEx;
//			Log.d(TAG, strErr);
			
			// 最后一个斜杠的位置
			int nPos = strPathEx.lastIndexOf("/");
			
			if (nPos > 0 && nPos < strPathEx.length())
			{
				// 编号
				String strNameNum = i2Str(nNameNum, 4);
				
				// 截取路径
				strPathEx = strPathEx.substring(0, nPos);
				
				// 修正路径
				strPathEx += "/" + strNameNum + ".png";
			}
		}
		
		return strPathEx;
	}
	
	//----------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------
	/**
	 * double 值转为 int 值(进行四舍五入)
	 * @param d double 值
	 * @return int 值
	 */
	public static int d2i(final double d)
	{
		int i = 0;

		// 检测是否超出 int 范围
		if (d < Integer.MIN_VALUE || d > Integer.MAX_VALUE)
		{
			return 0;
		}

		if (d < 0.0)
		{
			i = (int)(d - 0.5);
		}
		else
		{
			i = (int)(d + 0.5);
		}

		return i;
	}
	
	/**
	 * 整型转为字符串
	 * @param n1 整型
	 * @param n2 位数, 不足则在数值前补"0"
	 * @return
	 */
	public static String i2Str(final int n1, final int n2)
	{
		String str = String.valueOf(n1);
		
		for (int i = str.length(); i < n2; i++)
		{
			str = "0" + str;
		}
		
		return str;
	}
	
	/**
	 * 长整型转为字符串
	 * @param n1 整型
	 * @param n2 位数, 不足则在数值前补"0"
	 * @return
	 */
	public static String l2Str(final long l, final int n2)
	{
		String str = String.valueOf(l);
		
		for (int i = str.length(); i < n2; i++)
		{
			str = "0" + str;
		}
		
		return str;
	}
	
	/**
	 * 字符串转为 int
	 * @param str
	 * @return
	 */
	public static int str2int(final String str)
	{
		try
		{
			if (str == null) return 0;

			// 检测是否为整型字符串
			if (!isIntStr(str)) return 0;
			
			try
			{
				int n = Integer.valueOf(str);
				return n;
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	/**
	 * 字符串转为 double
	 * @param str
	 * @return
	 */
	public static double str2dbl(final String str)
	{
		try
		{
			if (str == null) return 0;

			// 检测是否为双精度字符串
			if (!isDblStr(str)) return 0;
			
			try
			{
				double d = Double.valueOf(str);
				return d;
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	/**
	 * 时间转为 l
	 * @param str
	 * @return
	 */
	public static long time2long(final String str)
	{
		try
		{
			if (str == null) return 0;
			int nLen = str.length();
			if (nLen < 1) return 0;
			
			String strTime = "";
			
			for (int i = 0; i < nLen; i++)
			{
				char ch = str.charAt(i);
				
				if (ch >= '0' && ch <= '9')
				{
					strTime += ch;
				}
			}
			
			if (strTime.equals("")) strTime = "0";
			
			try
			{
				long l = Long.valueOf(strTime);
				return l;
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	/**
	 * 字符串是否为 int
	 * @param str
	 * @return
	 */
	private static boolean strIsInt(final String str)
	{
		try
		{
			if (str == null) return false;

			// 检测是否为整型字符串
			if (!isIntStr(str)) return false;
			
			try
			{
				Integer.valueOf(str);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 整型转为 2 位 BCD 字节码
	 * @param nn
	 * @return
	 */
	public static byte[] int2bcd(final int n)
	{
		byte[] b = new byte[2];
		if (n <= 0) return b;

		int n10_1 = 10;
		int n10_2 = 100;
		int n10_3 = 1000;

		int n3 = (n / n10_3);
		int n2 = (n - n3 * n10_3) / n10_2;
		int n1 = (n - n3 * n10_3 - n2 * n10_2) / n10_1;
		int n0 = (n - n3 * n10_3 - n2 * n10_2 - n1 * n10_1);
		
		int i0 = (n3 << 4) | n2;
		int i1 = (n1 << 4) | n0;
		
		b[0] = (byte)i0;
		b[1] = (byte)i1;
		
		return b;
	}
	
	/**
	 * BCD 字节码转为整型
	 * @param b
	 */
	public static int bcd2int(final byte b)
	{
		// byte 转为 int
		int n = byte2int(b);
		
		try
		{
			final char chDec[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
			
			StringBuilder sb = new StringBuilder(2);
			
			sb.append(chDec[(n & 0xf0) >>> 4]);
			sb.append(chDec[(n & 0x0f)]);
			
			n = Integer.valueOf(sb.toString());
		}
		catch (Exception e)
		{
		}
		
		return n;
	}
	
	/**
	 * byte 转为 int
	 * @param b
	 * @return
	 */
	public static int byte2int(final byte b)
	{
		int n = b & 0xff;
//		int n = (b < 0) ? (256 + b) : b;
		
		return n;
	}

	/**
	 * 字节数组转为字符串
	 * @param bytes
	 * @return
	 */
	public static String bytes2HexStr(byte[] bytes)
	{
		final char chHex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		
		for (int i = 0; i < bytes.length; i++)
		{
			sb.append(chHex[(bytes[i] & 0xf0) >>> 4]);
			sb.append(chHex[(bytes[i] & 0x0f)]);
		}
		
		return sb.toString();
	}
	
	/**
	 * 格式化十六进制字符串
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String formatHexStr(final String str)
	{
		String strTemp1 = str;
		String strTemp2 = "";

		// 去掉空格
		strTemp1 = strTemp1.replace(" ", "");

		// 获取长度
		int nLen1 = strTemp1.length();

		for (int i = 0; i < nLen1; i++)
		{
			strTemp2 += strTemp1.charAt(i);

			if ((i + 1) % 2 == 0 && i < nLen1 - 1)
			{
				strTemp2 += " ";
			}
		}
		
		return strTemp2;
	}
	
	/**
	 * 判断字符串是否相等(不区分大小写)
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(final String str1, final String str2)
	{
		if (str1 == null || str2 == null) return false;
		if (str1.equals(str2)) return true;
		
		String strL1 = str1.toLowerCase();
		String strU1 = str1.toUpperCase();
		String strL2 = str2.toLowerCase();
		String strU2 = str2.toUpperCase();
		
		if (str1.equals(strL2) || str1.equals(strU2)) return true;
		if (strL1.equals(strL2) || strL1.equals(strU2)) return true;
		if (strU1.equals(strL2) || strU1.equals(strU2)) return true;
		
		return false;
	}

	//----------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------
	/**
	 * 读缓冲区
	 */
	public static void readBuffer()
	{
		try
		{
			// 清空缓冲区
			if (ms_nBytes != COMM_BYTE_NUM) clearBuffer();
			
			// 缓冲区字节数
			if (ms_nBytes < COMM_BYTE_NUM) return;
			if(theApp.DEMO)
			{
				// 解析指令
				if (parseCommandDemo(ms_buffer))
				{
					// 指令正确
	//				Log.d(TAG, "COMM_OK");
				}
				else
				{
					// 指令错误
	//				Log.d(TAG, "MSG_COMM_ERROR");
				}
			}
			else
			{
				// 解析指令
				if (parseCommand(ms_buffer))
				{
					// 指令正确
	//				Log.d(TAG, "COMM_OK");
				}
				else
				{
					// 指令错误
	//				Log.d(TAG, "MSG_COMM_ERROR");
				}			
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 读缓冲区
	 * @param nBytes
	 * @param buffer
	 */
	public static void readBuffer(final int nBytes, final byte[] buffer)
	{
		if (nBytes < 1 || buffer == null || buffer.length < nBytes) return;
		
		for (int i = 0; i < nBytes; i++)
		{
			int j = ms_nBytes + i;
			
			if (j >= 0 && j < BUFFER_SIZE)
			{
				// 缓冲区
				ms_buffer[j] = buffer[i];
//				Log.d(TAG, "ms_buffer[" + j + "]: " + ms_buffer[j]);
			}
		}
		
		// 缓冲区字节数
		ms_nBytes += nBytes;
		
		int nCount = 0;
		int nMaxCount = BUFFER_SIZE / COMM_BYTE_NUM + 1;
		
		// 循环执行
		while (nCount < nMaxCount && ms_nBytes >= COMM_BYTE_NUM)
		{
			nCount++;
//			Log.d(TAG, "ReadBuffer Count: " + nCount);
			
			// 读缓冲区
			readBuffer();
			
			if (nCount == nMaxCount - 1)
			{
				// 清空缓冲区
				ms_nBytes = 0;
				ms_buffer = new byte[BUFFER_SIZE];
			}
		}
	}
	
	/**
	 * 初始化缓冲区
	 */
	public static void initBuffer()
	{
		// 清空缓冲区
		ms_nBytes = 0;
		ms_buffer = new byte[BUFFER_SIZE];
	}
	
	/**
	 * 清空OSD
	 * 
	 */
	public static void clearAllosd(boolean tread ,boolean top ,boolean bottom )
	{
		// 清 OSD1
		List<OsdBitmap> listOsd1 = null;

		// 启用悬浮窗单个开关
		if (FLOAT_SWITCH)
		{
			// 关闭悬浮窗
			if (tread)
			{
				listOsd1 = ms_listOsd1;
			}
			// 开启悬浮窗
			else
			{
				// 检测是否位于顶部悬浮窗
				if (top)
				{
					listOsd1 = ms_listOsdTop1;
				}
				// 检测是否位于底部悬浮窗
				if (bottom)
				{
					listOsd1 = ms_listOsdBottom1;
				}
			}
		}
		// 不启用悬浮窗单个开关
		else
		{
			// 打开顶部悬浮窗
			if (ms_bFloatTopOn)
			{
				// 检测是否位于顶部悬浮窗
//						if (inTop(nX1, nY1, nX2, nY2))
				{
					listOsd1 = ms_listOsdTop1;
				}
			}
			
			// 打开底部悬浮窗
			if (ms_bFloatBottomOn)
			{
				// 检测是否位于底部悬浮窗
//						if (inBottom(nX1, nY1, nX2, nY2))
				{
					listOsd1 = ms_listOsdBottom1;
				}
			}
			
			// 关闭顶部和底部悬浮窗
			if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
			{
				listOsd1 = ms_listOsd1;
			}
		}
		
		if (listOsd1!= null)
		{
			// 图片数量
			int nSize1 = listOsd1.size();
			
			for (int i = 0; i < nSize1; i++)
			{
				// OSD 图片
				OsdBitmap osd = listOsd1.get(i);
				{
					i--;
					nSize1--;
					
					// 移除 OSD 图片
					osd.recycle();
					listOsd1.remove(osd);
				}
			}
		}
		
	// 清 OSD2
		List<OsdBitmap> listOsd2 = null;

		// 启用悬浮窗单个开关
		if (FLOAT_SWITCH)
		{
			// 关闭悬浮窗
			if (tread)
			{
				listOsd2 = ms_listOsd2;
			}
			// 开启悬浮窗
			else
			{
				// 检测是否位于顶部悬浮窗
				if (top)
				{
					listOsd2 = ms_listOsdTop2;
				}
				// 检测是否位于底部悬浮窗
				if (bottom)
				{
					listOsd2 = ms_listOsdBottom2;
				}
			}
		}
		// 不启用悬浮窗单个开关
		else
		{
			// 打开顶部悬浮窗
			if (ms_bFloatTopOn)
			{
				// 检测是否位于顶部悬浮窗
//						if (inTop(nX1, nY1, nX2, nY2))
				{
					listOsd2 = ms_listOsdTop2;
				}
			}
			
			// 打开底部悬浮窗
			if (ms_bFloatBottomOn)
			{
				// 检测是否位于底部悬浮窗
//						if (inBottom(nX1, nY1, nX2, nY2))
				{
					listOsd2 = ms_listOsdBottom2;
				}
			}
			
			// 关闭顶部和底部悬浮窗
			if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
			{
				listOsd2 = ms_listOsd2;
			}
		}

		if (listOsd2 != null)
		{
			// 图片数量
			int nSize2 = listOsd2.size();
			
			for (int i = 0; i < nSize2; i++)
			{
				// OSD 图片
				OsdBitmap osd = listOsd2.get(i);

				{
					i--;
					nSize2--;
					
					// 移除 OSD 图片
					osd.recycle();
					listOsd2.remove(osd);
				}
			}
		}
		
	// 清 OSD4
		List<OsdBitmap> listOsd4 = null;
		
		// 启用悬浮窗单个开关
		if (FLOAT_SWITCH)
		{
			// 关闭悬浮窗
			if (tread)
			{
				listOsd4 = ms_listOsd4;
			}
			// 开启悬浮窗
			else
			{
				// 检测是否位于顶部悬浮窗
				if (top)
				{
					listOsd2 = ms_listOsdTop2;
				}
				// 检测是否位于底部悬浮窗
				if (bottom)
				{
					listOsd2 = ms_listOsdBottom2;
				}
			}
		}

		if (listOsd4 != null)
		{
			// 图片数量
			int nSize4 = listOsd4.size();
			
			for (int i = 0; i < nSize4; i++)
			{
				// OSD 图片
				OsdBitmap osd = listOsd4.get(i);

				{
					i--;
					nSize4--;
					
					// 移除 OSD 图片
					osd.recycle();
					listOsd4.remove(osd);
				}
			}
		}		
}	
	/**
	 * 清空缓冲区
	 */
	public static void clearBuffer()
	{
		byte[] mark = HEAD_BYTE;
		
		try
		{
			int nMark = -1;
			boolean bMark = false;
			
			// 检测标记
			if (mark == null) return;
			
			// 缓冲区长度
			int nLenBuff = ms_nBytes;
			
			// 标记长度
			int nLenMark = mark.length;
			if (nLenMark < 1) return;
			
			for (int i = 0; i <= nLenBuff - nLenMark; i++)
			{
				int j = 0;
				bMark = false;
				
				for (j = 0; j < nLenMark; j++)
				{
					int k = i + j;
					
					if (mark[j] == ms_buffer[k])
					{
						bMark = true;
					}
					else
					{
						bMark = false;
						break;
					}
				}
				
				if (bMark && j == nLenMark)
				{
					nMark = i;

					// 检测缓冲区字节数
					if (ms_nBytes >= COMM_BYTE_NUM)
					{
						// 解析指令
						if (parseCommand_(ms_buffer, i))
						{
							break;
						}
					}
				}
			}
			
			if (nMark == -1 || (bMark && nMark == 0))
			{
				return;
			}
			
			// 缓冲区字节数
			int nBytes = ms_nBytes;
			nBytes -= nMark;
			
			if (nBytes < 1)
			{
				// 清空缓冲区
				ms_nBytes = 0;
				ms_buffer = new byte[BUFFER_SIZE];
				return;
			}
			
			byte[] buffer = new byte[nBytes];

			// 长度
			int nLen = Math.min(nBytes, nLenBuff);

			// 数组拷贝
			System.arraycopy(ms_buffer, nMark, buffer, 0, nLen);
			
			// 清空缓冲区
			ms_nBytes = nBytes;
			ms_buffer = new byte[BUFFER_SIZE];

			// 数组拷贝
			System.arraycopy(buffer, 0, ms_buffer, 0, nBytes);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 清空缓冲区
	 * @param nByteNum
	 */
	public static void clearBuffer(final int nByteNum)
	{
		if (nByteNum < 1 || nByteNum >= BUFFER_SIZE) return;

		// 缓冲区字节数
		ms_nBytes -= nByteNum;

		if (ms_nBytes < 1)
		{
			// 清空缓冲区
			ms_nBytes = 0;
			ms_buffer = new byte[BUFFER_SIZE];
			return;
		}
		
		byte[] buffer = new byte[ms_nBytes];

		// 长度
		int nLen = Math.min(ms_nBytes, BUFFER_SIZE);

		// 数组拷贝
		System.arraycopy(ms_buffer, nByteNum, buffer, 0, nLen);
		
		// 缓冲区
		ms_buffer = new byte[BUFFER_SIZE];

		// 数组拷贝
		System.arraycopy(buffer, 0, ms_buffer, 0, ms_nBytes);
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 检测 HEAD
	 * @param buffer
	 * @return
	 */
	public static boolean checkHEAD(byte[] buffer)
	{
		if (buffer == null) return false;
		if (buffer.length < HEAD_LEN) return false;
		
		if (buffer[0] != (byte)HEAD[0] ||
			buffer[1] != (byte)HEAD[1])
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 检测 TAIL
	 * @param buffer
	 * @return
	 */
	public static boolean checkTAIL(byte[] buffer)
	{
		if (buffer == null) return false;
		if (buffer.length < TAIL_LEN) return false;
		
		int nLen = buffer.length;
		
		if (buffer[nLen - 2] != (byte)TAIL[0] ||
			buffer[nLen - 1] != (byte)TAIL[1])
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 计算字节数组的校验和
	 * @param nStart 开始索引
	 * @param nEnd 结束索引
	 * @param bytes 字节数组
	 * @return 校验和
	 */
	public static byte checkSum(final int nStart, final int nEnd, byte[] bytes)
	{
		int nSum = 0;
		
		if (bytes == null) return 0;
		
		int nLen = bytes.length;
		
		if (nStart < 0 || nEnd < 0 || nStart > nEnd || nStart >= nLen || nEnd >= nLen)
		{
			return 0;
		}
		
		for (int i = nStart; i <= nEnd; i++)
		{
			nSum += byte2int(bytes[i]);
		}
		
		int n = (nSum >> 0) & 0xFF; // 低 8 位, 即低字节
		
		byte b = (byte)(n);
		
		return b;
	}
	
	//----------------------------------------------------------------------------------------------------	
	/**
	 * 解析 C0
	 * @param buffer 缓冲区
	 */
	public static void parseC0(byte[] buffer)
	{
		// 参数
		int nP2 = bcd2int(buffer[5]);
		int nP3 = bcd2int(buffer[6]);
		int nBright = nP2*100 + nP3;
		if (nBright>255) nBright=255;
		
		// 设置屏幕亮度
//		ms_nScreenBrightness = nBright;

//		setBrightness(ms_curActivity, ms_nScreenBrightness);
		setBrightness(ms_curActivity, nBright);
//		Log.d(TAG, String.format("ScreenBrightness: %d", ms_nScreenBrightness));
		
		// 保存亮度
//		saveBrightness(ms_curActivity, ms_nScreenBrightness);	
		
		// 应答 C0
		responseC0();	

	}
	
	/**
	 * 解析 C1
	 * @param buffer 缓冲区
	 */
	public static void parseC1(byte[] buffer)
	{
		// 参数
//		int nP2 = bcd2int(buffer[5]);
		
		// 应答 C1
		responseC1();	
		
		//关机
		theApp.setShutdown(true);
		
	/*	if(	ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			((MainActivity)ms_curActivity).shutdown();
		}	*/
	}
	/**
	 * 解析 C2
	 * @param buffer 缓冲区
	 */
	public static void parseC2(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 参数
		int nP2 = bcd2int(buffer[5]);

		// 旧的语言
		String strLangOld = ms_strLang;
		
		switch (nP2)
		{
		// 英文, en
		// AA 55 C2 97 00 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 69 C3 3C
		case 10:
			ms_strLang = "en";
			break;

		// 中文, ch
		case 11:
			ms_strLang = "ch";
			break;

		// 繁体中文, tw
		case 12:
			ms_strLang = "tw";
			break;

		// 韩语, ko
		case 13:
			ms_strLang = "ko";
			break;

		// 日语, ja
		case 14:
			ms_strLang = "ja";
			break;

		// 俄语, ru
		case 15:
			ms_strLang = "ru";
			break;

		// 德语, de
		case 16:
			ms_strLang = "de";
			break;

		// 法语, fr
		case 17:
			ms_strLang = "fr";
			break;

		// 西班牙语, es
		case 18:
			ms_strLang = "es";
			break;

		// 葡萄牙语, pt
		case 19:
			ms_strLang = "pt";
			break;

		// 意大利语, it
		case 20:
			ms_strLang = "it";
			break;

		// 波兰语, pl
		case 21:
			ms_strLang = "pl";
			break;

		// 瑞典语, sv
		case 22:
			ms_strLang = "sv";
			break;

		// 阿拉伯语, ar
		case 23:
			ms_strLang = "ar";
			break;

		// 其它语言 3, q3
		case 24:
			ms_strLang = "q3";
			break;
			
		//土耳其语, tr
		case 25:
			ms_strLang = "tr";
			break;
			
		// 亮度增加
		// AA 55 C2 97 00 55 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 AE C3 3C
		case 55:
			
			// 设置屏幕亮度
			ms_nScreenBrightness += 10;
			if (ms_nScreenBrightness > 255) ms_nScreenBrightness = 255;
			setBrightness(ms_curActivity, ms_nScreenBrightness);
			Log.d(TAG, String.format("ScreenBrightness: %d", ms_nScreenBrightness));
			
			// 保存亮度
			saveBrightness(ms_curActivity, ms_nScreenBrightness);
			break;

		// 亮度减少
		// AA 55 C2 97 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 AF C3 3C
		case 56:
			
			// 设置屏幕亮度
			ms_nScreenBrightness -= 10;
			if (ms_nScreenBrightness < 30) ms_nScreenBrightness = 30;
			setBrightness(ms_curActivity, ms_nScreenBrightness);
			Log.d(TAG, String.format("ScreenBrightness: %d", ms_nScreenBrightness));
			
			// 保存亮度
			saveBrightness(ms_curActivity, ms_nScreenBrightness);
			break;
			
		// 公英转换, 公制
		// AA 55 C2 97 00 60 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 B9 C3 3C
		case 60:
			ms_nUnits = 0;
			break;

		// 公英转换, 英制
		// AA 55 C2 97 00 61 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BA C3 3C
		case 61:
			ms_nUnits = 1;
			break;
			
		// 蓝牙开关, 开
		// AA 55 C2 97 00 62 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BB C3 3C
		case 62:
			ms_nBtState = 1;
			break;
			
		// 蓝牙开关, 关
		// AA 55 C2 97 00 63 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BC C3 3C
		case 63:
			ms_nBtState = 0;
			break;
		case 64:
			setLock(1);
			break;
		}

		//----------------------------------------------------------------------------------------------------
/*		if (nP2 == 10) ms_strLang = "en"; // 英文, en
		if (nP2 == 11) ms_strLang = "ch"; // 中文, ch
		if (nP2 == 12) ms_strLang = "tw"; // 繁体中文, tw
		if (nP2 == 13) ms_strLang = "ko"; // 韩语, ko
		if (nP2 == 14) ms_strLang = "ja"; // 日语, ja
		if (nP2 == 15) ms_strLang = "ru"; // 俄语, ru
		if (nP2 == 16) ms_strLang = "de"; // 德语, de
		if (nP2 == 17) ms_strLang = "fr"; // 法语, fr
		if (nP2 == 18) ms_strLang = "es"; // 西班牙语, sp
		if (nP2 == 19) ms_strLang = "pt"; // 葡萄牙语, pt
		if (nP2 == 20) ms_strLang = "it"; // 意大利语, it
		if (nP2 == 21) ms_strLang = "pl"; // 波兰语, pl
		if (nP2 == 22) ms_strLang = "sv"; // 瑞典语, sv
		if (nP2 == 23) ms_strLang = "ar"; // 阿拉伯语, ar	
		if (nP2 == 24) ms_strLang = "q3"; // 其它语言 3, q3
		if (nP2 == 25) ms_strLang = "tr"; // 土耳其语, tr
*/
		// 路径
//		String strPath = ms_strDefPath + "picture/" + ms_strLang + "/";
		String strPath = ms_strPicturePath + ms_strLang + "/";

		// 文件
		File file = new File(strPath);
		
		// 检测文件
		if (!file.exists() || !file.isDirectory())
		{
			// 英文
			ms_strLang = "en";
		}
		
		if (10 <= nP2 && nP2 <= 25)
		{
			// 语言已更改
			if (!ms_strLang.equals(strLangOld))
			{
				try
				{
					// 检测是否为 MainActivity
					if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
					{
						// 清除布局
						((MainActivity)ms_curActivity).clearLayout();
						
						//已清楚浏览器
						theApp.setClear(true);
					}
				}
				catch (Exception e)
				{
				}
				
				// 设置系统语言
				setSystemLang();
				
				theApp.SetLan(true);
				
				theApp.setInitCh340(true);
			}
			
			//刷新图片数量
//			MainActivity.loadPicNum();
		}


		// 应答 C2
		responseC2();
	}
	/**
	 * 解析 C3
	 * @param buffer 缓冲区
	 */
	public static void parseC3(byte[] buffer)
	{
		// 参数
//		int nP2 = bcd2int(buffer[5]);
				
		if(	ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			((MainActivity)ms_curActivity).clearAppLayout();	
		}
	
		//清空app列表
		theApp.getListApps().clear();
		
//		theApp.MYSLog(TAG,"test"+theApp.getListApps());
		
		//显示系统应用
		theApp.setShowSApk(true);		
		
		// 应答 C3
		responseC3();	
	
	}	
	
	/**
	 * 解析 C4
	 * @param buffer 缓冲区
	 */
	public static void parseC4(byte[] buffer)
	{
		// 参数
		int nP0 = bcd2int(buffer[5]);
		int nP1 = bcd2int(buffer[6]);		
		int nP2 = nP0*100 + nP1 ;
		
		if (!theApp.USE_ZIP)
		{
			//获取存储数量
			theApp.setStoragePicNum(nP2);
			
			//开始比较图片数量
			theApp.setCheckNume(true);		
		}		
		// 应答 C4
		responseC4();	
	
	}	
	
	/**
	 * 设置图片数量
	 * @param buffer 缓冲区
	 */
/*	public static void setNandPicNum(int nP2)
	{		
		//获取存储数量
		theApp.setStoragePicNum(nP2);
		
		//开始比较图片数量
		theApp.setCheckNume(true);		
				
		// 应答 C4
		responseC4();	
	
	}	*/
	
	/**
	 * 获取蓝牙状态
	 */
	public static void getBlueToothState(){

		int state = BluetoothAdapter.getDefaultAdapter().getState();

		switch (state){
			//关闭
			case BluetoothAdapter.STATE_OFF:
				bluetooth_state = 0;
				break;
			//开启
			case BluetoothAdapter.STATE_ON:
				bluetooth_state = 1;
				break;
			//正在连接
			case BluetoothAdapter.STATE_CONNECTING:
				bluetooth_state = 2;
				break;
			////已经连接
			case BluetoothAdapter.STATE_CONNECTED:
				bluetooth_state = 3;
				break;
			//断开连接
			case BluetoothAdapter.STATE_DISCONNECTED:
				bluetooth_state = 4;
				break;
			//正在关闭
			case BluetoothAdapter.STATE_TURNING_OFF:
				bluetooth_state = 5;
				break;
			//正在启动
			case BluetoothAdapter.STATE_TURNING_ON:
				bluetooth_state = 6;
				break;
		}
	}

	public static int distance=0;
	/**
	 * 解析 D0
	 * @param buffer 缓冲区
	 */
	public static void parseD0(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 参数
		int nP1 = bcd2int(buffer[4]);

		distance = byte2int(buffer[6])*256 + byte2int(buffer[7]);

		// 在打开第三方 App 之后, 切换本程序的 Activity 至顶层
		// AA 55 D0 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 68 C3 3C
		if (nP1 == 0)
		{
		}
		
		// 播放 U 盘或者卡上电影命令
		// AA 55 D0 98 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6C C3 3C
		if (nP1 == 4)
		{
			// 多媒体标记, 视频
			ms_nMediaMark = MARK_VIDEO;
		}
		
		// 播放 U 盘或者卡上音乐命令
		// AA 55 D0 98 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6E C3 3C
		if (nP1 == 6)
		{
			// 多媒体标记, 音乐
			ms_nMediaMark = MARK_MUSIC;
		}
		
		// 播放 U 盘或者卡上图片命令
		// AA 55 D0 98 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 70 C3 3C
		if (nP1 == 8)
		{
			// 多媒体标记, 图片
			ms_nMediaMark = MARK_PICTU;
		}
		
		if (nP1 != 4 && nP1 != 6 && nP1 != 8)
		{
			// 多媒体标记, 无
			ms_nMediaMark = MARK_NONE;
		}
		
		// 进入 Internet 页面
		// AA 55 D0 98 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 78 C3 3C
		if (nP1 == 10)
		{
		}
		
		// 进入 APP 预览并启动悬浮窗
		// AA 55 D0 98 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7A C3 3C
		if (nP1 == 12)
		{
		}
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 布局视图
			((MainActivity)ms_curActivity).layoutView(0xD0, nP0, nP1);
		}
		
		// 应答 D0
		responseD0(nP0, nP1);
	}
	
	/**
	 * 解析 D2
	 * @param buffer 缓冲区
	 */
	public static void parseD2(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 停止媒体
			// AA 55 D2 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6A C3 3C
			((MainActivity)ms_curActivity).stopMedia();
		}
		
		// 应答 D2
		responseDX((byte)0xD2, nP0);
	}
	
	/**
	 * 解析 D3
	 * @param buffer 缓冲区
	 */
	public static void parseD3(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 上一曲媒体
			// AA 55 D3 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6B C3 3C
			((MainActivity)ms_curActivity).prevMedia();
		}
		
		// 应答 D3
		responseDX((byte)0xD3, nP0);
	}
	
	/**
	 * 解析 D4
	 * @param buffer 缓冲区
	 */
	public static void parseD4(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 下一曲媒体
			// AA 55 D4 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6C C3 3C
			((MainActivity)ms_curActivity).nextMedia();
		}
		
		// 应答 D4
		responseDX((byte)0xD4, nP0);
	}
	
	/**
	 * 解析 D5
	 * @param buffer 缓冲区
	 */
	public static void parseD5(byte[] buffer)
	{
//		if (buffer == null) return;
//		00000000if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 暂停媒体
			// AA 55 D5 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6D C3 3C
			((MainActivity)ms_curActivity).pauseMedia();
		}
		
		// 应答 D5
		responseDX((byte)0xD5, nP0);
	}
	
	/**
	 * 解析 D6
	 * @param buffer 缓冲区
	 */
	public static void parseD6(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 播放媒体
			// AA 55 D6 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6E C3 3C
			((MainActivity)ms_curActivity).playMedia();
		}
		
		// 应答 D6
		responseDX((byte)0xD6, nP0);
	}
	
	/**
	 * 解析 D9
	 * @param buffer 缓冲区
	 */
	public static void parseD9(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// 98
		int nP0 = bcd2int(buffer[3]);
		if (nP0 != 98) return;
		
		// 参数
		int nP2 = bcd2int(buffer[5]);
		
		// 标签
		// AA 55 D9 98 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 72 C3 3C
		if (nP2 == 1)
		{
		}
		
		// 网址
		// AA 55 D9 98 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 73 C3 3C
		if (nP2 == 2)
		{
		}
		
		// 后退
		// AA 55 D9 98 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 74 C3 3C
		if (nP2 == 3)
		{
		}
		
		// 收藏夹
		// AA 55 D9 98 00 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 75 C3 3C
		if (nP2 == 4)
		{
		}
		
		// 收藏
		// AA 55 D9 98 00 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 76 C3 3C
		if (nP2 == 5)
		{
		}
		
		// TV 播放
		// AA 55 D9 98 00 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 77 C3 3C
		if (nP2 == 6)
		{
		}
		
		// 音量减小
		// AA 55 D9 98 00 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 78 C3 3C
		if (nP2 == 7)
		{
		}
		
		// 音量增大
		// AA 55 D9 98 00 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 79 C3 3C
		if (nP2 == 8)
		{
		}
		
		// 退出多媒体播放页面, 进入多媒体文件列表界面
		// AA 55 D9 98 00 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7A C3 3C
		if (nP2 == 9)
		{
		}
		
		// 调节音量
		// AA 55 D9 98 00 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 81 C3 3C
		if (nP2 == 10)
		{
		}
		if (nP2 == 19)
		{
			setThirdAPP("QQ音乐");
		}
		if (nP2 == 20)
		{setThirdAPP("爱奇艺");
		}
		if (nP2 == 25)
		{setThirdAPP("蓝牙");
		}
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			if ((1 <= nP2 && nP2 <= 8)|| (11 <= nP2 && nP2 <= 30))
			{
				// 浏览器动作
				((MainActivity)ms_curActivity).doBrowser(nP2);
			}
			
			if (nP2 == 9)
			{
				// 后退媒体
				((MainActivity)ms_curActivity).backMedia();
			}
			
			if (nP2 == 10)
			{
				// 调节音量
				((MainActivity)ms_curActivity).volumeSame();
			}
		}
		
		// 应答 D0
		responseD9(nP0, nP2);
	}
	
	/**
	 * 解析 DA
	 * @param buffer 缓冲区
	 */
	public static void parseDA(byte[] buffer)
	{
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;
		
		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		theApp.setJpgPathNum(nJpgPathNum);
//		Log.d(TAG, "JpgPathNum: " + nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = bcd2int(buffer[4]);
		theApp.setJpgNameNum(nJpgNameNum);
//		Log.d(TAG, "JpgNameNum: " + nJpgNameNum);

		// 设置 JPG 图片
		setJpg();
		
		// 视频名称
		int nVideoName = bcd2int(buffer[6]);
		String strVideoName = i2Str(nVideoName, 2);
		
		// 视频后缀
		int nVideoPostfix = bcd2int(buffer[7]);
		String strVideoPostfix = "";
		if (nVideoPostfix == 0) strVideoPostfix = ".avi";
		if (nVideoPostfix == 1) strVideoPostfix = ".mpg";
		if (nVideoPostfix == 2) strVideoPostfix = ".dat";
		if (nVideoPostfix == 3) strVideoPostfix = ".mov";
		if (nVideoPostfix == 4) strVideoPostfix = ".mp4";
		if (nVideoPostfix == 5) strVideoPostfix = ".rmvb";
		if (nVideoPostfix == 6) strVideoPostfix = ".mkv";

		// 视频路径
		//String strVideoPath = ms_strDefPath + "movie/" + ms_strLang + "/";
		String strVideoPath = ms_moviestrDefPath + "movie/" + ms_strLang + "/";
		
		// 文件
		File file = new File(strVideoPath);
		
//		theApp.MYSLog(TAG, strPath);
		
		// 检测文件
		if (!file.exists() || !file.isDirectory())
		{
			//strVideoPath = ms_strDefPath + "movie/" + "en" + "/";
			strVideoPath = ms_moviestrDefPath + "movie/" + "en" + "/";
		}
		
		strVideoPath += strVideoName + strVideoPostfix;
//		theApp.setVideoPath(strVideoPath);
//		Log.d(TAG, "VideoPath: " + strVideoPath);

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;	
		
		// 视频坐标
		int nX11 = bcd2int(buffer[8]);
		int nX12 = bcd2int(buffer[9]);
		int nY11 = bcd2int(buffer[10]);
		int nY12 = bcd2int(buffer[11]);
		int nX21 = bcd2int(buffer[12]);
		int nX22 = bcd2int(buffer[13]);
		int nY21 = bcd2int(buffer[14]);
		int nY22 = bcd2int(buffer[15]);
		theApp.setVideoX1((int)((nX11 * 100 + nX12) * fWR));
		theApp.setVideoY1((int)((nY11 * 100 + nY12) * fHR));
		theApp.setVideoX2((int)((nX21 * 100 + nX22) * fWR));
		theApp.setVideoY2((int)((nY21 * 100 + nY22) * fHR));
		
		if (theApp.USE_VIDEOADJUST)
		{	
			// 屏幕宽度和高度
			int nWP = ms_nWidthPixels;
			int nHP = ms_nHeightPixels;
	
			ms_nVideoX1 = (ms_nVideoX1 < 0 || ms_nVideoX1 > nWP) ? 0 : ms_nVideoX1;
			ms_nVideoY1 = (ms_nVideoY1 < 0 || ms_nVideoY1 > nHP) ? 0 : ms_nVideoY1;
			ms_nVideoX2 = (ms_nVideoX2 < 0) ? 1 : ms_nVideoX2;
			ms_nVideoY2 = (ms_nVideoY2 < 0) ? 1 : ms_nVideoY2;
			ms_nVideoX2 = (ms_nVideoX2 > nWP) ? nWP : ms_nVideoX2;
			ms_nVideoY2 = (ms_nVideoY2 > nHP) ? nHP : ms_nVideoY2;
			
			// 播放速度
			// AA 55 DA 01 00 00 01 00 01 00 01 00 06 00 04 00 00 12 00 99 99 00 2C C3 3C
			// AA 55 DA 01 00 00 01 00 01 00 01 00 06 00 04 00 00 24 00 99 99 00 3E C3 3C
			// AA 55 DA 01 00 00 01 00 01 00 01 00 06 00 04 00 00 48 00 99 99 00 62 C3 3C
			int nP17 = bcd2int(buffer[17]); // 0 ~ 48 帧速
			nP17 = (nP17 == 0) ? 24 : nP17;
			float speed = (float)nP17 / 24; // 速度为 [0.5-2]
			speed = (speed < 0.5f) ? 0.5f : speed;
			speed = (speed > 2.0f) ? 2.0f : speed;
			ms_fVideoSpeedOld = ms_fVideoSpeed;
			ms_fVideoSpeed = speed;
		}

		// 前台
		if (ms_nBackground == 0)
		{
			// 播放
			if (buffer[16] == (byte) 0x00)
			{
				//显示Toast
				theApp.setToast(true);
				
				// 没有播放
				if (!isPlayingVideo())
				{
					// 视频路径
					theApp.setVideoPath(strVideoPath);

					// 播放视频
					playVideo(true, true);
				}
				// 正在播放
				else
				{
					// 相同的视频
					if (ms_strVideoPath.equals(strVideoPath))
					{
						// 视频路径
						theApp.setVideoPath(strVideoPath);

						// 播放视频
						playVideo(false, false);
					}
					// 不相同的视频
					else
					{
						try
						{
							// 延迟 1000 毫秒
							Thread.sleep(1000);
						}
						catch (Exception e)
						{
						}

						// 视频当前位置
						theApp.setVideoCurPos(0);

						// 视频路径
						theApp.setVideoPath(strVideoPath);

						// 播放视频
						playVideo(true, false);
					}
				}
			}
			
			// 停止
			if (buffer[16] == (byte)0x01)
			{
				//停止显示Toast
				theApp.setToast(false);
				
				// 视频路径
				theApp.setVideoPath(null);
			//	theApp.setVideoPath(strVideoPath);

				// 停止视频
				if (isPlayingVideo()) stopVideo();
			}
			
			// 暂停
			if (buffer[16] == (byte)0x02)
			{
				// 视频路径
				theApp.setVideoPath(strVideoPath);

				// 暂停视频
				if (isPlayingVideo()) pauseVideo();
			}
		}
		
		// 音乐名称
		int nMusic1 = bcd2int(buffer[19]);
		int nMusic2 = bcd2int(buffer[20]);
		int nMusicName = nMusic1 * 100 + nMusic2;
		String strMusicName = i2Str(nMusicName, 4);

		// 音乐路径
		String strMusicPath = ms_strDefPath + "speech/" + ms_strLang + "/";
		strMusicPath += strMusicName + ".mp3";
		theApp.setMusicPath(strMusicPath);
//		Log.d(TAG, "MusicPath: " + strMusicPath);
		
		try
		{
			// 播放音乐
			if (!ms_mediaPlayer.isPlaying()) playMusic();
		}
		catch (Exception e)
		{
		}
		
		// 应答 DA
		responseDA();
	}
	
	/**
	 * 解析 DB
	 * @param buffer 缓冲区
	 */
	public static void parseDB(byte[] buffer)
	{	
		//网站编号
		int nUrlNum = byte2int(buffer[5]);//bcd2int(buffer[5]);
		int nBpNum = byte2int(buffer[6]);//bcd2int(buffer[5]);//奔跑场景模式视频网址

		//
		String nUrl = "https://www.baidu.com/";

		if(nUrlNum==0x16){setThirdAPP("多人竞赛");}

		if (nBpNum != 0){
			switch (nBpNum){
				case 0x01:
				nUrl = "http://121.12.98.180/xdispatch/7xqi17.com1.z0.glb.clouddn.com/BM10KM3.mp4";
				break;
				case 0x02:
				nUrl =  "http://125.39.21.14/xdispatch/7xqi17.com1.z0.glb.clouddn.com/baiyu-38Min-720P-1.5M.mp4";
				break;
				case 0x03:
				nUrl = "http://125.39.21.9/xdispatch/7xqi17.com1.z0.glb.clouddn.com/tuershan-38Min-720P-1.5M-v2.mp4";
				break;
				case 0x04:
				nUrl = "http://oj9am3239.bkt.clouddn.com/nuoergai-720P-37Min-1.5M-ME.mp4";
				break;
				case 0x05:
				nUrl = "http://oj9am3239.bkt.clouddn.com/XMall.mp4";
				break;
				case 0x06:
					nUrl = "http://oj9am3239.bkt.clouddn.com/SZ10KM.mp4";
					break;
				case 0x07:
					nUrl = "http://oj9am3239.bkt.clouddn.com/yala-30min-720P-1.5M-ME.mp4";
					break;
				case 0x08:
					nUrl = "http://oj9am3239.bkt.clouddn.com/haizishan-35min-1080P-1.5M.mp4";
					break;
				case 0x09:
					nUrl = "http://oj9am3239.bkt.clouddn.com/zhagana-30min-720P-1.5M-ME.mp4";
					break;
				case 0x0a:
					nUrl = "http://oj9am3239.bkt.clouddn.com/yangmaiyong-45min-720P-1.5M.mp4";
					break;
				case 0x0b:
					nUrl = "http://oj9am3239.bkt.clouddn.com/ganzi-32min-720P-VBR1.5M.mp4";
					break;
				case 0x0c:
					nUrl = "http://oj9am3239.bkt.clouddn.com/wolong-45min-720P-1.5M.mp4";
					break;
				case 0x0d:
					nUrl = "http://oj9am3239.bkt.clouddn.com/10km_2.mp4";
					break;
				case 0x0e:
					nUrl = "http://oj9am3239.bkt.clouddn.com/Mini_1.mp4";
					break;
				case 0x0f:
					nUrl = "http://oj9am3239.bkt.clouddn.com/bj5km.mp4";
					break;
				default:
					return;
			}
		}else{
		switch (nUrlNum)
		{
		//淘宝
			case 0x01:
				nUrl = "https://www.taobao.com/";
				break;
	   //百度
			case 0x02:
				nUrl = "https://www.baidu.com/";
				break;
				
	  //新浪微博		
			case 0x03:
				nUrl = "http://weibo.com/";
				break;	
				
	  //hao123	
			case 0x04:
				nUrl = "http://www.hao123.com/";
				break;	
				
	  //百度音乐
			case 0x05:
				nUrl = "http://music.baidu.com/";
				break;	
				
	  //qq音乐	
			case 0x06:
				nUrl = "http://y.qq.com/";
				break;	
				
	  //酷狗音乐		
			case 0x07:
				nUrl = "http://www.kugou.com/";
				break;	
				
	  //beats		
			case 0x08:
				nUrl = "http://zhan.renren.com/mosheng?tagId=114774&checked=true";
				break;	
				
	  //billboard		
			case 0x09:
				nUrl = "http://www.billboard.com/";
				break;	
				
	  //优酷		
			case 0x10:
				nUrl = "http://www.youku.com/";
				break;	
				
	  //爱奇艺		
			case 0x11:
				nUrl = "http://www.iqiyi.com/";
				break;	
				
	  //腾讯视频		
			case 0x12:
				nUrl = "http://v.qq.com/";
				break;	
				
	  //youtube		
			case 0x13:
				nUrl = "http://www.youtube.com/";
				break;		
				
	  //vaobad		
			case 0x14:
				nUrl = "http://tv.sohu.com/";
				break;	
				
	  //大疆无人机	
			case 0x15:
				nUrl = "http://www.dji.com/cn/showcase";
				break;
				
	  //多人竞赛
			//case 0x16:
				//nUrl = "http://wx.tb-tech.cn/game/login.html";
				//setThirdAPP("多人竞赛");
				//break;
	  //新浪 
			case 0x17:
				nUrl = "http://www.sina.com/";
				break;
	  //京东 
			case 0x18:
				nUrl = "http://www.jd.com/";
				break;
	  //土豆网 
			case 0x19:
				nUrl = "http://www.tudou.com/";
				break;	
	  //天猫网 
			case 0x20:
				nUrl = "http://www.tmall.com/";
				break;	
	  //facebook
			case 0x21:
				nUrl = "http://www.facebook.com/";
				break;
	  //cnn
			case 0x22:
				nUrl = "http://edition.cnn.com/";
				break;	
	  //BBC
			case 0x23:
				nUrl = "http://www.bbc.com/";
				break;
	  //bloomberg 
			case 0x24:
				nUrl = "http://www.bloomberg.com/";
				break;	
	  //google
			case 0x25:
				nUrl = "http://www.google.com/";
				break;
	  //ESPN
			case 0x26:
				nUrl = "http://espn.go.com/";
				break;	
	  //NBA
			case 0x27:
				nUrl = "http://www.nba.com/";
				break;
	  //FIFA 
			case 0x28:
				nUrl = "http://www.fifa.com/";
				break;
			//亿峰网
			case 0x29:
				nUrl = "http://www.yifengke.com/newwebsite/yifengke";
				setThirdAPP("亿峰网");
				break;

			default:
				return;
		}}
		
		BrowseToUlr(nUrl);
		
		// 应答 DB
		responseDB();
	}
		
	/**
	 * 解析 F0
	 * @param buffer 缓冲区
	 */
	public static void parseF0(byte[] buffer)
	{
		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		
		// JPG 名称编号
		int nJpgNameNum = bcd2int(buffer[4]);

		// 启用悬浮窗单个开关
		if (FLOAT_SWITCH)
		{
			// 关闭悬浮窗
			if (!ms_bFloatOn)
			{
				// 设置 JPG 图片
				setJpg();
			}
			// 打开悬浮窗
			else
			{
				// 打开悬浮窗时, 贴左上角, 然后清除
				// AA 55 F1 98 00 00 32 00 09 01 00 07 00 00 00 00 00 00 00 00 00 00 CC C3 3C
				// AA 55 F0 98 00 00 00 00 00 00 32 00 09 01 19 00 80 00 00 00 00 00 5D C3 3C

				// 打开悬浮窗时, 贴左下角, 然后清除
				// AA 55 F1 98 00 00 32 06 45 01 00 07 00 00 00 00 00 00 00 00 00 00 0E C3 3C
				// AA 55 F0 98 00 00 00 00 00 00 32 06 45 01 19 07 16 00 00 00 00 00 3C C3 3C
			}
		}
		// 不启用悬浮窗单个开关
		else
		{
			// 关闭顶部和底部悬浮窗
			if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
			{
				// 设置 JPG 图片
				setJpg();
			}
		}

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// 检测编号
		if (nJpgPathNum == ms_nJpgPathNum && nJpgNameNum == ms_nJpgNameNum)
		{
			// 清除区域的坐标
			int nX11 = bcd2int(buffer[9]);
			int nX12 = bcd2int(buffer[10]);
			int nY11 = bcd2int(buffer[11]);
			int nY12 = bcd2int(buffer[12]);
			int nX21 = bcd2int(buffer[13]);
			int nX22 = bcd2int(buffer[14]);
			int nY21 = bcd2int(buffer[15]);
			int nY22 = bcd2int(buffer[16]);
			int nX1 = (int)((nX11 * 100 + nX12) * fWR);
			int nY1 = (int)((nY11 * 100 + nY12) * fHR);
			int nX2 = (int)((nX21 * 100 + nX22) * fWR);
			int nY2 = (int)((nY21 * 100 + nY22) * fHR);
			
			if (theApp.SCALED_XY)
			{
				nX1 = (int)((nX11 * 100 + nX12) * fWR + 0.5);
				nY1 = (int)((nY11 * 100 + nY12) * fHR + 0.5);
				nX2 = (int)((nX21 * 100 + nX22) * fWR + 0.5);
				nY2 = (int)((nY21 * 100 + nY22) * fHR + 0.5);			
			}
			// 清 OSD1
			if (buffer[17] == (byte)0x00 || buffer[17] == (byte)0x01)
			{
				List<OsdBitmap> listOsd1 = null;

				// 启用悬浮窗单个开关
				if (FLOAT_SWITCH)
				{
					// 关闭悬浮窗
					if (!ms_bFloatOn)
					{
						listOsd1 = ms_listOsd1;
					}
					// 开启悬浮窗
					else
					{
						// 检测是否位于顶部悬浮窗
						if (inTop(nX1, nY1, nX2, nY2))
						{
							listOsd1 = ms_listOsdTop1;
						}
						// 检测是否位于底部悬浮窗
						else if (inBottom(nX1, nY1, nX2, nY2))
						{
							listOsd1 = ms_listOsdBottom1;
						}
					}
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 打开顶部悬浮窗
					if (ms_bFloatTopOn)
					{
						// 检测是否位于顶部悬浮窗
						if (inTop(nX1, nY1, nX2, nY2))
						{
							listOsd1 = ms_listOsdTop1;
						}
					}
					
					// 打开底部悬浮窗
					if (ms_bFloatBottomOn)
					{
						// 检测是否位于底部悬浮窗
						if (inBottom(nX1, nY1, nX2, nY2))
						{
							listOsd1 = ms_listOsdBottom1;
						}
					}
					
					// 关闭顶部和底部悬浮窗
					if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
					{
						listOsd1 = ms_listOsd1;
					}
				}
				
				if (listOsd1!= null)
				{
					// 图片数量
					int nSize1 = listOsd1.size();
					
					for (int i = 0; i < nSize1; i++)
					{
						// OSD 图片
						OsdBitmap osd = listOsd1.get(i);
						
						// 坐标
						int x1 = osd.getX1();
						int y1 = osd.getY1();
						int x2 = osd.getX2();
						int y2 = osd.getY2();
						
						// 判断两个矩形是否相交
						if (OsdGraph.inter2Rect(x1, y1, x2, y2, nX1, nY1, nX2, nY2))
						{
							i--;
							nSize1--;
							
							// 移除 OSD 图片
							osd.recycle();
							listOsd1.remove(osd);
						}
					}
				}
			}

			// 清 OSD2
			if (buffer[17] == (byte)0x00 || buffer[17] == (byte)0x02)
			{
				List<OsdBitmap> listOsd2 = null;

				// 启用悬浮窗单个开关
				if (FLOAT_SWITCH)
				{
					// 关闭悬浮窗
					if (!ms_bFloatOn)
					{
						listOsd2 = ms_listOsd2;
					}
					// 开启悬浮窗
					else
					{
						// 检测是否位于顶部悬浮窗
						if (inTop(nX1, nY1, nX2, nY2))
						{
							listOsd2 = ms_listOsdTop2;
						}
						// 检测是否位于底部悬浮窗
						else if (inBottom(nX1, nY1, nX2, nY2))
						{
							listOsd2 = ms_listOsdBottom2;
						}
					}
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 打开顶部悬浮窗
					if (ms_bFloatTopOn)
					{
						// 检测是否位于顶部悬浮窗
						if (inTop(nX1, nY1, nX2, nY2))
						{
							listOsd2 = ms_listOsdTop2;
						}
					}
					
					// 打开底部悬浮窗
					if (ms_bFloatBottomOn)
					{
						// 检测是否位于底部悬浮窗
						if (inBottom(nX1, nY1, nX2, nY2))
						{
							listOsd2 = ms_listOsdBottom2;
						}
					}
					
					// 关闭顶部和底部悬浮窗
					if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
					{
						listOsd2 = ms_listOsd2;
					}
				}

				if (listOsd2 != null)
				{
					// 图片数量
					int nSize2 = listOsd2.size();
					
					for (int i = 0; i < nSize2; i++)
					{
						// OSD 图片
						OsdBitmap osd = listOsd2.get(i);
						
						// 坐标
						int x1 = osd.getX1();
						int y1 = osd.getY1();
						int x2 = osd.getX2();
						int y2 = osd.getY2();
						
						// 判断两个矩形是否相交
						if (OsdGraph.inter2Rect(x1, y1, x2, y2, nX1, nY1, nX2, nY2))
						{
							i--;
							nSize2--;
							
							// 移除 OSD 图片
							osd.recycle();
							listOsd2.remove(osd);
						}
					}
				}
			}

			// 清 OSD3
			if (buffer[17] == (byte)0x00 || buffer[17] == (byte)0x03)
			{
				// 清 OSD3
				clearOsd3(nX1, nY1, nX2, nY2);
			}

			// 清 OSD4
			if (buffer[17] == (byte)0x00 || buffer[17] == (byte)0x04)
			{
				List<OsdBitmap> listOsd4 = null;

				listOsd4 = ms_listOsd4;

				if (listOsd4 != null)
				{
					// 图片数量
					int nSize4 = listOsd4.size();
					
					for (int i = 0; i < nSize4; i++)
					{
						// OSD 图片
						OsdBitmap osd = listOsd4.get(i);
						
						// 坐标
						int x1 = osd.getX1();
						int y1 = osd.getY1();
						int x2 = osd.getX2();
						int y2 = osd.getY2();
						
						// 判断两个矩形是否相交
						if (OsdGraph.inter2Rect(x1, y1, x2, y2, nX1, nY1, nX2, nY2))
						{
							i--;
							nSize4--;
							
							// 移除 OSD 图片
							osd.recycle();
							listOsd4.remove(osd);
						}
					}
				}
			}
						
		}

		// 应答 F0
		responseF0(nJpgPathNum, nJpgNameNum);
	}
	
	/**
	 * 解析 F1
	 * @param buffer 缓冲区
	 */
	public static void parseF1(byte[] buffer)
	{
		// 参数
		int nP0 = bcd2int(buffer[3]);

		int nP1 = bcd2int(buffer[4]);
		// JPG 路径编号
		int nJpgPathNum = nP0;
		theApp.setJpgPathNum(nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = nP1;
		theApp.setJpgNameNum(nJpgNameNum);

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// OSD1 坐标 1
		int nX11 = bcd2int(buffer[5]);
		int nX12 = bcd2int(buffer[6]);
		int nY11 = bcd2int(buffer[7]);
		int nY12 = bcd2int(buffer[8]);
		theApp.setOsd11X((int)((nX11 * 100 + nX12) * fWR));
		theApp.setOsd11Y((int)((nY11 * 100 + nY12) * fHR));
		if (theApp.SCALED_XY)
		{
			theApp.setOsd11X((int)((nX11 * 100 + nX12) * fWR + 0.5));
			theApp.setOsd11Y((int)((nY11 * 100 + nY12) * fHR + 0.5));		
		}
		
		// OSD1 路径编号 1
		int nOsdPathNum1 = bcd2int(buffer[9]);
		
		// OSD1 名称编号 1
		int nOsdNameNum11 = bcd2int(buffer[10]);
		int nOsdNameNum12 = bcd2int(buffer[11]);
		int nOsdNameNum1 = nOsdNameNum11 * 100 + nOsdNameNum12;
		
		if (!theApp.getQrcode()) 
		{
			if (nOsdNameNum1 == 9999)
				nOsdNameNum1 = 0;
		}
		 
		// OSD1 坐标 2
		int nX21 = bcd2int(buffer[12]);
		int nX22 = bcd2int(buffer[13]);
		int nY21 = bcd2int(buffer[14]);
		int nY22 = bcd2int(buffer[15]);
		theApp.setOsd12X((int)((nX21 * 100 + nX22) * fWR));
		theApp.setOsd12Y((int)((nY21 * 100 + nY22) * fHR));

		if (theApp.SCALED_XY)
		{
			theApp.setOsd12X((int)((nX21 * 100 + nX22) * fWR + 0.5));
			theApp.setOsd12Y((int)((nY21 * 100 + nY22) * fHR + 0.5));		
		}
		
		// OSD1 路径编号 2
		int nOsdPathNum2 = bcd2int(buffer[16]);
		
		// OSD1 名称编号 1
		int nOsdNameNum21 = bcd2int(buffer[17]);
		int nOsdNameNum22 = bcd2int(buffer[18]);
		int nOsdNameNum2 = nOsdNameNum21 * 100 + nOsdNameNum22;

		//dyp在登录界面屏蔽触摸事件,只能通过扫码进入
		if (buffer[1]==85&&buffer[2]==-15&&buffer[3]==1&&buffer[4]==0){
			//if(!PushReceiver.mTimeout){return;}
			PushReceiver.isOpen = false;
		}

		if (!theApp.getQrcode())
		{
			if (nOsdNameNum2 == 9999)
			nOsdNameNum2 = 0;
		}
		// 启用悬浮窗单个开关
		if (FLOAT_SWITCH)
		{
			// 关闭悬浮窗
			if (!ms_bFloatOn)
			{
				// 设置 JPG 图片
				setJpg();
			}
			// 打开悬浮窗
			else
			{
				// 打开悬浮窗时, 贴左上角, 然后清除
				// AA 55 F1 98 00 00 32 00 09 01 00 07 00 00 00 00 00 00 00 00 00 00 CC C3 3C
				// AA 55 F0 98 00 00 00 00 00 00 32 00 09 01 19 00 80 00 00 00 00 00 5D C3 3C

				// 打开悬浮窗时, 贴左下角, 然后清除
				// AA 55 F1 98 00 00 32 06 45 01 00 07 00 00 00 00 00 00 00 00 00 00 0E C3 3C
				// AA 55 F0 98 00 00 00 00 00 00 32 06 45 01 19 07 16 00 00 00 00 00 3C C3 3C
			}
		}
		// 不启用悬浮窗单个开关
		else
		{
			// 关闭顶部和底部悬浮窗
			if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
			{
				// 设置 JPG 图片
				setJpg();
			}
		}
		
		//是否清除动画图片
		int nPlay = bcd2int((byte)(buffer[21]& 0x02));
		if (nPlay == 2) ms_bClearAction = true;
		else ms_bClearAction = false;
		
		int nOsd4= bcd2int((byte)(buffer[21]& 0x40));
		
		// 设置 OSD1 图片
		if (nOsd4 == 40)
			setBmpOsd1(nOsdPathNum1, nOsdNameNum1, nOsdPathNum2, nOsdNameNum2,ms_bClearAction,true);
		else
			setBmpOsd1(nOsdPathNum1, nOsdNameNum1, nOsdPathNum2, nOsdNameNum2,ms_bClearAction,false);
		
		// 声音名称
		int nSound1 = bcd2int(buffer[19]);
		int nSound2 = bcd2int(buffer[20]);
		int nSoundName = nSound1 * 100 + nSound2;
		String strSoundName = i2Str(nSoundName, 4);

		
		// 声音路径
		String strSoundPath = ms_strDefPath + "speech/" + ms_strLang + "/";
		strSoundPath += strSoundName + ".mp3";
		theApp.setSoundPath(strSoundPath);
//		Log.d(TAG, "SoundPath: " + strSoundPath);
		
		// 播放声音
		playSound();

		
		// MainActivity
		// AA 55 F1 01 00 02 47 01 91 01 01 01 04 67 01 91 01 02 01 00 01 00 D2 C3 3C
		if (1 <= nP0 && nP0 <= 90)
		{
			nP1 = -1;
		}
		
		// 自由模式
		// AA 55 F1 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 89 C3 3C
		if (nP0 == 98)
		{
		}
		
		// 配置
		// AA 55 F1 97 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 88 C3 3C
		if (nP0 == 97)
		{
		}
		
		// 实景模式
		// AA 55 F1 95 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 86 C3 3C
		if (nP0 == 95)
		{
		}
		
		// 检测是否为 MainActivity
		if (ms_curActivity != null && ms_curActivity instanceof MainActivity)
		{
			// 布局视图
			((MainActivity)ms_curActivity).layoutView(0xF1, nP0, nP1);
		}


		// 应答 F1
		responseF1();
	}
	
	/**
	 * 解析 F3
	 * @param buffer 缓冲区
	 */
	public static void parseF3(byte[] buffer)
	{
		// 设置 OSD2 图片
		setBmpOsd2(buffer);
		
		// 应答 F3
		responseFX((byte)0xF3);
	}
	
	/**
	 * 解析 F5
	 * @param buffer 缓冲区
	 */
	public static void parseF5(byte[] buffer)
	{
		// AA 55 F5 01 00 02 70 01 00 03 11 C4 CF B7 C7 CA C0 BD E7 B1 AD 32 4C C3 3C
//		if (buffer == null) return;
//		if (buffer.length < COMM_BYTE_NUM) return;

		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		theApp.setJpgPathNum(nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = bcd2int(buffer[4]);
		theApp.setJpgNameNum(nJpgNameNum);

		// 设置 JPG 图片
		setJpg();

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// 左边第 1 个字符的左上角坐标
		int nX11 = bcd2int(buffer[5]);
		int nX12 = bcd2int(buffer[6]);
		int nY11 = bcd2int(buffer[7]);
		int nY12 = bcd2int(buffer[8]);
		float x1 = (nX11 * 100 + nX12) * fWR;
		float y1 = (nY11 * 100 + nY12) * fHR;
		
		// 字体颜色
		int nColor = bcd2int(buffer[9]);
		
		// 字符间距
		int nSpace = bcd2int(buffer[10]);
		
		// 字符
		String str1 = "";
		String str2 = "";
		String str3 = "";
		String str4 = "";
		String str5 = "";
		byte[] gb1 = new byte[2];
		byte[] gb2 = new byte[2];
		byte[] gb3 = new byte[2];
		byte[] gb4 = new byte[2];
		byte[] gb5 = new byte[2];
		gb1[0] = buffer[11];
		gb1[1] = buffer[12];
		gb2[0] = buffer[13];
		gb2[1] = buffer[14];
		gb3[0] = buffer[15];
		gb3[1] = buffer[16];
		gb4[0] = buffer[17];
		gb4[1] = buffer[18];
		gb5[0] = buffer[19];
		gb5[1] = buffer[20];

//		byte[] gb = null;
//		String str = "南非世界杯";
		
		try
		{
//			gb = str.getBytes("GB2312");
			
			str1 = new String(gb1, "GB2312");
			str2 = new String(gb2, "GB2312");
			str3 = new String(gb3, "GB2312");
			str4 = new String(gb4, "GB2312");
			str5 = new String(gb5, "GB2312");
		}
		catch (Exception e)
		{
		}
		
		// 字体大小
		int nSize = bcd2int(buffer[21]);
		
		// 坐标
		float x2 = x1 + nSize + nSpace;
		float x3 = x2 + nSize + nSpace;
		float x4 = x3 + nSize + nSpace;
		float x5 = x4 + nSize + nSpace;
		float y2 = y1;
		float y3 = y1;
		float y4 = y1;
		float y5 = y1;
		
		// OSD 字符串
		OsdString osd1 = new OsdString();
		OsdString osd2 = new OsdString();
		OsdString osd3 = new OsdString();
		OsdString osd4 = new OsdString();
		OsdString osd5 = new OsdString();
		osd1.setColor(nColor);
		osd1.setXY(x1, y1);
		osd1.setStr(str1);
		osd1.setSize(nSize);
		osd2.setColor(nColor);
		osd2.setXY(x2, y2);
		osd2.setStr(str2);
		osd2.setSize(nSize);
		osd3.setColor(nColor);
		osd3.setXY(x3, y3);
		osd3.setStr(str3);
		osd3.setSize(nSize);
		osd4.setColor(nColor);
		osd4.setXY(x4, y4);
		osd4.setStr(str4);
		osd4.setSize(nSize);
		osd5.setColor(nColor);
		osd5.setXY(x5, y5);
		osd5.setStr(str5);
		osd5.setSize(nSize);
		
		// 添加 OSD
		ms_listOsd3.add(osd1);
		ms_listOsd3.add(osd2);
		ms_listOsd3.add(osd3);
		ms_listOsd3.add(osd4);
		ms_listOsd3.add(osd5);
		
		// 应答 F5
		responseFX((byte)0xF5);
	}
	
	/**
	 * 解析 F6
	 * @param buffer 缓冲区
	 */
	public static void parseF6(byte[] buffer)
	{
		// AA 55 F6 01 00 43 00 00 00 10 01 07 02 05 01 07 02 05 04 09 01 56 CC C3 3C
		if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;

		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		theApp.setJpgPathNum(nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = bcd2int(buffer[4]);
		theApp.setJpgNameNum(nJpgNameNum);

		// 设置 JPG 图片
		setJpg();

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// 线宽, 高 4 位
		int nWidth = (buffer[5] >> 4) & 0xf;
		
		// 颜色, 低 4 位
		int nColor = (buffer[5] >> 0) & 0xf;

		// 坐标
		int nX11 = bcd2int(buffer[6]);
		int nX12 = bcd2int(buffer[7]);
		int nY11 = bcd2int(buffer[8]);
		int nY12 = bcd2int(buffer[9]);
		int nX21 = bcd2int(buffer[10]);
		int nX22 = bcd2int(buffer[11]);
		int nY21 = bcd2int(buffer[12]);
		int nY22 = bcd2int(buffer[13]);
		float x11 = (nX11 * 100 + nX12) * fWR;
		float y11 = (nY11 * 100 + nY12) * fHR;
		float x12 = (nX21 * 100 + nX22) * fWR;
		float y12 = (nY21 * 100 + nY22) * fHR;
		
		// 坐标
		nX11 = bcd2int(buffer[14]);
		nX12 = bcd2int(buffer[15]);
		nY11 = bcd2int(buffer[16]);
		nY12 = bcd2int(buffer[17]);
		nX21 = bcd2int(buffer[18]);
		nX22 = bcd2int(buffer[19]);
		nY21 = bcd2int(buffer[20]);
		nY22 = bcd2int(buffer[21]);
		float x21 = (nX11 * 100 + nX12) * fWR;
		float y21 = (nY11 * 100 + nY12) * fHR;
		float x22 = (nX21 * 100 + nX22) * fWR;
		float y22 = (nY21 * 100 + nY22) * fHR;
		
		// OSD 直线
		OsdLine osd1 = new OsdLine();
		OsdLine osd2 = new OsdLine();
		osd1.setColor(nColor);
		osd1.setWidth(nWidth);
		osd1.setXY(x11, y11, x12, y12);
		osd2.setColor(nColor);
		osd2.setWidth(nWidth);
		osd2.setXY(x21, y21, x22, y22);
		
		// 添加 OSD
		ms_listOsd3.add(osd1);
		ms_listOsd3.add(osd2);
		
		// 应答 F6
		responseFX((byte)0xF6);
	}
	
	/**
	 * 解析 F7
	 * @param buffer 缓冲区
	 */
	public static void parseF7(byte[] buffer)
	{
		// AA 55 F7 01 00 14 00 05 00 06 01 07 02 03 01 50 02 08 02 88 00 56 5F C3 3C
		// AA 55 F7 01 04 04 00 05 00 06 01 07 02 03 01 50 02 08 02 88 00 56 53 C3 3C
		if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;

		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		theApp.setJpgPathNum(nJpgPathNum);
		
		// 线宽
		int nWidth = bcd2int(buffer[4]);

		// 设置 JPG 图片
		setJpg();

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// 填充, 高 4 位
		int nFill = (buffer[5] >> 4) & 0xf;
		
		// 颜色, 低 4 位
		int nColor = (buffer[5] >> 0) & 0xf;
		
		// 清 OSD 左上角坐标
		int nX11 = bcd2int(buffer[6]);
		int nX12 = bcd2int(buffer[7]);
		int nY11 = bcd2int(buffer[8]);
		int nY12 = bcd2int(buffer[9]);
		int nX1 = (int)((nX11 * 100 + nX12) * fWR);
		int nY1 = (int)((nY11 * 100 + nY12) * fHR);
		
		// 清 OSD 宽度和高度
		int nW1 = bcd2int(buffer[10]);
		int nW2 = bcd2int(buffer[11]);
		int nH1 = bcd2int(buffer[12]);
		int nH2 = bcd2int(buffer[13]);
		int nW = (int)((nW1 * 100 + nW2) * fWR);
		int nH = (int)((nH1 * 100 + nH2) * fHR);
		
		// 清 OSD 右下角坐标
		int nX2 = nX1 + nW;
		int nY2 = nY1 + nH;
		
		// 清 OSD3
		clearOsd3(nX1, nY1, nX2, nY2);
		
		// 矩形左上角坐标
		nX11 = bcd2int(buffer[14]);
		nX12 = bcd2int(buffer[15]);
		nY11 = bcd2int(buffer[16]);
		nY12 = bcd2int(buffer[17]);
		nX1 = (int)((nX11 * 100 + nX12) * fWR);
		nY1 = (int)((nY11 * 100 + nY12) * fHR);
		
		// 矩形宽度和高度
		nW1 = bcd2int(buffer[18]);
		nW2 = bcd2int(buffer[19]);
		nH1 = bcd2int(buffer[20]);
		nH2 = bcd2int(buffer[21]);
		nW = (int)((nW1 * 100 + nW2) * fWR);
		nH = (int)((nH1 * 100 + nH2) * fHR);
		
		// 矩形右下角坐标
		nX2 = nX1 + nW;
		nY2 = nY1 + nH;
		
		// OSD 矩形
		OsdRect osd = new OsdRect();
		osd.setColor(nColor);
		osd.setFill(nFill);
		osd.setWidth(nWidth);
		osd.setXY(nX1, nY1, nX2, nY2);
		
		// 添加 OSD
		ms_listOsd3.add(osd);
		
		// 应答 F7
		responseFX((byte)0xF7);
	}
	
	/**
	 * 解析 F8
	 * @param buffer 缓冲区
	 */
	public static void parseF8(byte[] buffer)
	{
		// AA 55 F8 01 00 15 00 00 00 00 04 00 01 80 02 00 01 40 01 13 00 00 EA C3 3C
		// AA 55 F8 01 00 05 00 00 00 00 04 00 01 80 02 00 01 40 01 13 00 00 DA C3 3C
		if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;

		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		theApp.setJpgPathNum(nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = bcd2int(buffer[4]);
		theApp.setJpgNameNum(nJpgNameNum);

		// 设置 JPG 图片
		setJpg();

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// 填充, 高 4 位
		int nFill = (buffer[5] >> 4) & 0xf;
		
		// 颜色, 低 4 位
		int nColor = (buffer[5] >> 0) & 0xf;
		
		// 清 OSD 左上角坐标
		int nX11 = bcd2int(buffer[6]);
		int nX12 = bcd2int(buffer[7]);
		int nY11 = bcd2int(buffer[8]);
		int nY12 = bcd2int(buffer[9]);
		int nX1 = (int)((nX11 * 100 + nX12) * fWR);
		int nY1 = (int)((nY11 * 100 + nY12) * fHR);
		
		// 清 OSD 宽度和高度
		int nW1 = bcd2int(buffer[10]);
		int nW2 = bcd2int(buffer[11]);
		int nH1 = bcd2int(buffer[12]);
		int nH2 = bcd2int(buffer[13]);
		int nW = (int)((nW1 * 100 + nW2) * fWR);
		int nH = (int)((nH1 * 100 + nH2) * fHR);
		
		// 清 OSD 右下角坐标
		int nX2 = nX1 + nW;
		int nY2 = nY1 + nH;
		
		// 清 OSD3
		clearOsd3(nX1, nY1, nX2, nY2);
		
		// 圆心坐标
		nX11 = bcd2int(buffer[14]);
		nX12 = bcd2int(buffer[15]);
		nY11 = bcd2int(buffer[16]);
		nY12 = bcd2int(buffer[17]);
		int nX = (int)((nX11 * 100 + nX12) * fWR);
		int nY = (int)((nY11 * 100 + nY12) * fHR);
		
		// 半径
		int nR1 = bcd2int(buffer[18]);
		int nR2 = bcd2int(buffer[19]);
		int nRadius = (int)((nR1 * 100 + nR2) * fWR);
		
		// OSD 圆
		OsdCircle osd = new OsdCircle();
		osd.setColor(nColor);
		osd.setFill(nFill);
		osd.setRadius(nRadius);
		osd.setXY(nX, nY);
		
		// 添加 OSD
		ms_listOsd3.add(osd);
		
		// 应答 F8
		responseFX((byte)0xF8);
	}
	
	/**
	 * 显示字符串
	 * @param str     要显示的字符串
	 * @param x1                第一个字符的左上角x坐标
	 * @param y1                第一个字符的左上角y坐标
	 * @param Color   字符串的颜色
	 * @param Size    字体大小
	 */
	public static void showMyosd(String str,float x1,float y1,byte Color,byte Size)
	{
		
		// 字体颜色
		int nColor = bcd2int(Color);

		// 字体大小
		int nSize = bcd2int(Size);
		
		// OSD 字符串
		OsdString osd = new OsdString();
		osd.setColor(nColor);
		osd.setXY(x1, y1);
		osd.setStr(str);
		osd.setSize(nSize);
		
		// 添加 OSD
		ms_listOsd3.add(osd);

	}
		
	/**
	 * 解析 F9
	 * @param buffer 缓冲区
	 */
	public static void parseF9(byte[] buffer)
	{
		// AA 55 F9 01 00 02 70 02 00 03 11 41 42 43 44 45 46 47 48 49 4A 22 5B C3 3C
		if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;

		// JPG 路径编号
		int nJpgPathNum = bcd2int(buffer[3]);
		theApp.setJpgPathNum(nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = bcd2int(buffer[4]);
		theApp.setJpgNameNum(nJpgNameNum);

		// 设置 JPG 图片
		setJpg();

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// 左边第 1 个字符的左上角坐标
		int nX11 = bcd2int(buffer[5]);
		int nX12 = bcd2int(buffer[6]);
		int nY11 = bcd2int(buffer[7]);
		int nY12 = bcd2int(buffer[8]);
		float x1 = (nX11 * 100 + nX12) * fWR;
		float y1 = (nY11 * 100 + nY12) * fHR;
		
		// 字体颜色
		int nColor = bcd2int(buffer[9]);
		
		// 字符间距
		int nSpace = bcd2int(buffer[10]);
		
		// 字符
		String str1 = String.valueOf((char)(buffer[11]));
		String str2 = String.valueOf((char)(buffer[12]));
		String str3 = String.valueOf((char)(buffer[13]));
		String str4 = String.valueOf((char)(buffer[14]));
		String str5 = String.valueOf((char)(buffer[15]));
		String str6 = String.valueOf((char)(buffer[16]));
		String str7 = String.valueOf((char)(buffer[17]));
		String str8 = String.valueOf((char)(buffer[18]));
		String str9 = String.valueOf((char)(buffer[19]));
		String str0 = String.valueOf((char)(buffer[20]));

		// 字体大小
		int nSize = bcd2int(buffer[21]);
		
		// 检测字符间距
		if (nSpace == 0)
		{
			// 字符串
			String str = str1 + str2 + str3 + str4 + str5 + str6 + str7 + str8 + str9 + str0;
			
			// OSD 字符串
			OsdString osd = new OsdString();
			osd.setColor(nColor);
			osd.setXY(x1, y1);
			osd.setStr(str);
			osd.setSize(nSize);
			
			// 添加 OSD
			ms_listOsd3.add(osd);
		}
		else
		{
			// 坐标
			float x2 = x1 + nSize + nSpace;
			float x3 = x2 + nSize + nSpace;
			float x4 = x3 + nSize + nSpace;
			float x5 = x4 + nSize + nSpace;
			float x6 = x5 + nSize + nSpace;
			float x7 = x6 + nSize + nSpace;
			float x8 = x7 + nSize + nSpace;
			float x9 = x8 + nSize + nSpace;
			float x0 = x9 + nSize + nSpace;
			float y2 = y1;
			float y3 = y1;
			float y4 = y1;
			float y5 = y1;
			float y6 = y1;
			float y7 = y1;
			float y8 = y1;
			float y9 = y1;
			float y0 = y1;
			
			// OSD 字符串
			OsdString osd1 = new OsdString();
			OsdString osd2 = new OsdString();
			OsdString osd3 = new OsdString();
			OsdString osd4 = new OsdString();
			OsdString osd5 = new OsdString();
			OsdString osd6 = new OsdString();
			OsdString osd7 = new OsdString();
			OsdString osd8 = new OsdString();
			OsdString osd9 = new OsdString();
			OsdString osd0 = new OsdString();
			osd1.setColor(nColor);
			osd1.setXY(x1, y1);
			osd1.setStr(str1);
			osd1.setSize(nSize);
			osd2.setColor(nColor);
			osd2.setXY(x2, y2);
			osd2.setStr(str2);
			osd2.setSize(nSize);
			osd3.setColor(nColor);
			osd3.setXY(x3, y3);
			osd3.setStr(str3);
			osd3.setSize(nSize);
			osd4.setColor(nColor);
			osd4.setXY(x4, y4);
			osd4.setStr(str4);
			osd4.setSize(nSize);
			osd5.setColor(nColor);
			osd5.setXY(x5, y5);
			osd5.setStr(str5);
			osd5.setSize(nSize);
			osd6.setColor(nColor);
			osd6.setXY(x6, y6);
			osd6.setStr(str6);
			osd6.setSize(nSize);
			osd7.setColor(nColor);
			osd7.setXY(x7, y7);
			osd7.setStr(str7);
			osd7.setSize(nSize);
			osd8.setColor(nColor);
			osd8.setXY(x8, y8);
			osd8.setStr(str8);
			osd8.setSize(nSize);
			osd9.setColor(nColor);
			osd9.setXY(x9, y9);
			osd9.setStr(str9);
			osd9.setSize(nSize);
			osd0.setColor(nColor);
			osd0.setXY(x0, y0);
			osd0.setStr(str0);
			osd0.setSize(nSize);
			
			// 添加 OSD
			ms_listOsd3.add(osd1);
			ms_listOsd3.add(osd2);
			ms_listOsd3.add(osd3);
			ms_listOsd3.add(osd4);
			ms_listOsd3.add(osd5);
			ms_listOsd3.add(osd6);
			ms_listOsd3.add(osd7);
			ms_listOsd3.add(osd8);
			ms_listOsd3.add(osd9);
			ms_listOsd3.add(osd0);
		}
		
		// 应答 F9
		responseFX((byte)0xF9);
	}
	
	/**
	 * 解析 FA
	 * @param buffer 缓冲区
	 */
	public static void parseFA(byte[] buffer)
	{
		// 参数
		int nP0 = bcd2int(buffer[3]);
		int nP1 = bcd2int(buffer[4]);

		// JPG 路径编号
		int nJpgPathNum = nP0;
		theApp.setJpgPathNum(nJpgPathNum);
		
		// JPG 名称编号
		int nJpgNameNum = nP1;
		theApp.setJpgNameNum(nJpgNameNum);

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// OSD1 坐标 1
		int nX11 = bcd2int(buffer[5]);
		int nX12 = bcd2int(buffer[6]);
		int nY11 = bcd2int(buffer[7]);
		int nY12 = bcd2int(buffer[8]);
		theApp.setOsd11X((int)((nX11 * 100 + nX12) * fWR));
		theApp.setOsd11Y((int)((nY11 * 100 + nY12) * fHR));
		if (theApp.SCALED_XY)
		{
			theApp.setOsd11X((int)((nX11 * 100 + nX12) * fWR + 0.5));
			theApp.setOsd11Y((int)((nY11 * 100 + nY12) * fHR + 0.5));		
		}
		// OSD1 路径编号 1
		int nOsdPathNum1 = bcd2int(buffer[9]);
		
		// OSD1 名称编号 1
		int nOsdNameNum11 = bcd2int(buffer[10]);
		int nOsdNameNum12 = bcd2int(buffer[11]);
		int nOsdNameNum1 = nOsdNameNum11 * 100 + nOsdNameNum12;
		
		// OSD1 坐标 2
		int nX21 = bcd2int(buffer[12]);
		int nX22 = bcd2int(buffer[13]);
		int nY21 = bcd2int(buffer[14]);
		int nY22 = bcd2int(buffer[15]);
		theApp.setOsd12X((int)((nX21 * 100 + nX22) * fWR));
		theApp.setOsd12Y((int)((nY21 * 100 + nY22) * fHR));
		if (theApp.SCALED_XY)
		{
			theApp.setOsd12X((int)((nX21 * 100 + nX22) * fWR + 0.5));
			theApp.setOsd12Y((int)((nY21 * 100 + nY22) * fHR + 0.5));		
		}
		// OSD1 路径编号 2
		int nOsdPathNum2 = bcd2int(buffer[16]);
		
		// OSD1 名称编号 1
		int nOsdNameNum21 = bcd2int(buffer[17]);
		int nOsdNameNum22 = bcd2int(buffer[18]);
		int nOsdNameNum2 = nOsdNameNum21 * 100 + nOsdNameNum22;

		// 启用悬浮窗单个开关
		if (FLOAT_SWITCH)
		{
			// 关闭悬浮窗
			if (!ms_bFloatOn)
			{
				// 设置 JPG 图片
				setJpg();
			}
			// 打开悬浮窗
			else
			{
				// 打开悬浮窗时, 贴左上角, 然后清除
				// AA 55 F1 98 00 00 32 00 09 01 00 07 00 00 00 00 00 00 00 00 00 00 CC C3 3C
				// AA 55 F0 98 00 00 00 00 00 00 32 00 09 01 19 00 80 00 00 00 00 00 5D C3 3C

				// 打开悬浮窗时, 贴左下角, 然后清除
				// AA 55 F1 98 00 00 32 06 45 01 00 07 00 00 00 00 00 00 00 00 00 00 0E C3 3C
				// AA 55 F0 98 00 00 00 00 00 00 32 06 45 01 19 07 16 00 00 00 00 00 3C C3 3C
			}
		}
		// 不启用悬浮窗单个开关
		else
		{
			// 关闭顶部和底部悬浮窗
			if (!ms_bFloatTopOn && !ms_bFloatBottomOn)
			{
				// 设置 JPG 图片
				setJpg();
			}
		}
		
		//是否清除动画图片
		int nPlay = bcd2int((byte)(buffer[21]& 0x02));
		if (nPlay == 2) ms_bClearAction = true;
		else ms_bClearAction = false;
		
		int nOsd4= bcd2int((byte)(buffer[21]& 0x40));
	
		// 声音名称
		int nAngle1 = bcd2int(buffer[19]);
		int nAngle2 = bcd2int(buffer[20]);
		int nAngle = nAngle1 * 100 + nAngle2;
		setAngle(nAngle);
		
		// 设置 OSD1 图片
		if (nOsd4 == 40)
			setBmpOsd4(nOsdPathNum1, nOsdNameNum1, nOsdPathNum2, nOsdNameNum2,ms_bClearAction,true);
		else
			setBmpOsd1(nOsdPathNum1, nOsdNameNum1, nOsdPathNum2, nOsdNameNum2,ms_bClearAction,false);
		


		// 应答 FA
		responseFX((byte)0xFA);
	}
		
	/**
	 * 解析 FB
	 * @param buffer 缓冲区
	 */
	public static void parseFB(byte[] buffer)
	{
		// AA 55 FB 00 00 00 00 00 01 13 10 25 11 13 00 50 15 00 30 00 05 01 03 C3 3C
		// AA 55 FB 00 00 06 65 05 15 05 05 30 60 60 00 03 50 01 20 00 00 02 F0 C3 3C
		if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;
		
		// 数据段标识
		int nMark = bcd2int(buffer[21]);
		
		// 01 写文件前部分数据
		if (nMark == 1)
		{
			// 文件名
			int nFN1 = bcd2int(buffer[5]);
			int nFN2 = bcd2int(buffer[6]);
			int nFN3 = bcd2int(buffer[7]);
			int nFN4 = bcd2int(buffer[8]);
			
			// 文件名
			String strFN1 = i2Str(nFN1, 2);
			String strFN2 = i2Str(nFN2, 2);
			String strFN3 = i2Str(nFN3, 2);
			String strFN4 = i2Str(nFN4, 2);
			String strFN  = strFN1 + strFN2 + strFN3 + strFN4;

			// 客户文件名
			String strFileName = ms_strDefPath + "customer/";
			mkdir(strFileName);
			strFileName = ms_strDefPath + "customer/" + strFN + ".csv";
			theApp.setCustFN(strFileName);
			
			// 日期
			int nY = bcd2int(buffer[9]) + 2000;
			int nM = bcd2int(buffer[10]);
			int nD = bcd2int(buffer[11]);
			String strYMD = i2Str(nY, 4) + i2Str(nM, 2) + i2Str(nD, 2) + ",";
			
			// 开始时间
			int nSH = bcd2int(buffer[12]);
			int nSM = bcd2int(buffer[13]);
			String strHM = i2Str(nSH, 2) + ":" + i2Str(nSM, 2) + ",";
			
			// 运动时间, min
			int nT1 = bcd2int(buffer[14]);
			int nT2 = bcd2int(buffer[15]);
			int nTM = nT1 * 100 + nT2;
			String strTM = String.valueOf(nTM) + ",";
			
			// 运动里程, m
			int nM1 = bcd2int(buffer[16]);
			int nM2 = bcd2int(buffer[17]);
			int nMM = nM1 * 100 + nM2;
			String strMM = String.valueOf(nMM) + ",";
			
			// 消耗卡路里
			int nC1 = bcd2int(buffer[18]);
			int nC2 = bcd2int(buffer[19]);
			int nCC = nC1 * 100 + nC2;
			String strCC = String.valueOf(nCC) + ",";
			
			// 消耗点数
			int nPt = bcd2int(buffer[20]);
			String strPt = String.valueOf(nPt) + ",";
			
			// 客户数据
			String strData = strYMD + strHM + strTM + strMM + strCC + strPt;
			theApp.setCustData(strData);
		}
		
		// 02 写文件后部分数据
		if (nMark == 2)
		{
			// 检测客户文件名和客户数据
			if (ms_strCustFN.equals("") || ms_strCustData.equals(""))
			{
				// 应答 FB
				responseFX((byte)0xFB);
				return;
			}
			
			// 累积运动时间
			int nT1 = bcd2int(buffer[5]);
			int nT2 = bcd2int(buffer[6]);
			int nT3 = bcd2int(buffer[7]);
			int nTT = nT1 * 10000 + nT2 * 100 + nT3;
			String strTT = String.valueOf(nTT) + ",";
			
			// 累积运动里程
			int nM1 = bcd2int(buffer[8]);
			int nM2 = bcd2int(buffer[9]);
			int nM3 = bcd2int(buffer[10]);
			int nTM = nM1 * 10000 + nM2 * 100 + nM3;
			String strTM = String.valueOf(nTM) + ",";
			
			// 累积消耗卡路里
			int nC1 = bcd2int(buffer[11]);
			int nC2 = bcd2int(buffer[12]);
			int nC3 = bcd2int(buffer[13]);
			int nTC = nC1 * 10000 + nC2 * 100 + nC3;
			String strTC = String.valueOf(nTC) + ",";
			
			// 累积消耗点数
			int nP1 = bcd2int(buffer[14]);
			int nP2 = bcd2int(buffer[15]);
			int nP3 = bcd2int(buffer[16]);
			int nTP = nP1 * 10000 + nP2 * 100 + nP3;
			String strTP = String.valueOf(nTP) + ",";
			
			// 运动次数
			int nN1 = bcd2int(buffer[17]);
			int nN2 = bcd2int(buffer[18]);
			int nSN = nN1 * 100 + nN2;
			String strSN = String.valueOf(nSN) + "\r\n";
			
			// 客户数据
			String strData = ms_strCustData;
			strData += strTT + strTM + strTC + strTP + strSN;
			
			// 写文本文件
			writeTxtFile(ms_strCustFN, strData, true);
			
			// 客户文件名和客户数据
			ms_strCustFN = "";
			ms_strCustData = "";
		}
		
		// 应答 FB
		responseFX((byte)0xFB);
	}
	
	/**
	 * 解析 FC
	 * @param buffer 缓冲区
	 */
	public static void parseFC(byte[] buffer)
	{
		// AA 55 F6 01 00 43 00 00 00 10 01 07 02 05 01 07 02 05 04 09 01 56 CC C3 3C
		if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		//颜色
		int nColor = 4;
		
		//X坐标
		int nCoodrateX = 40;//500;
		
		//线宽
		int nWidth = 1;
		
		//间隔
		int nDiss = 3;
		
		boolean nShowOsd1 = false;
		
		HRy[0] = (int)((bcd2int(buffer[3])*100+ bcd2int(buffer[4]))* fHR);
		HRy[1] = (int)((bcd2int(buffer[5])*100+ bcd2int(buffer[6]))* fHR);
		HRy[2] = (int)((bcd2int(buffer[7])*100+ bcd2int(buffer[8]))* fHR);
		HRy[3] = (int)((bcd2int(buffer[9])*100+ bcd2int(buffer[10]))* fHR);	
		HRy[4] = (int)((bcd2int(buffer[11])*100+ bcd2int(buffer[12]))* fHR);
		HRy[5] = (int)((bcd2int(buffer[13])*100+ bcd2int(buffer[14]))* fHR);
		HRy[6] = (int)((bcd2int(buffer[15])*100+ bcd2int(buffer[16]))* fHR);
//		HRy[7] = (int)((bcd2int(buffer[17])*100+ bcd2int(buffer[18]))* fHR);
//		HRy[8] = (int)((bcd2int(buffer[19])*100+ bcd2int(buffer[20]))* fHR);
		HR_num = bcd2int(buffer[21]);
		if (HR_num != 0) nShowOsd1 = true;
		else nShowOsd1 = false;

		OsdLine osd1 = new OsdLine();
		OsdLine osd2 = new OsdLine();
		OsdLine osd3 = new OsdLine();
		OsdLine osd4 = new OsdLine();
		OsdLine osd5 = new OsdLine();
		OsdLine osd6 = new OsdLine();
		OsdLine osd7 = new OsdLine();
//		OsdLine osd8 = new OsdLine();	
//		OsdLine osd9 = new OsdLine();
		osd1.setColor(nColor);
		osd1.setWidth(nWidth);
		osd1.setXY(nCoodrateX+0*nDiss+7*HR_num*nDiss, old_HRy, nCoodrateX+1*nDiss+7*HR_num*nDiss, HRy[0]);
//		osd1.setXY(nCoodrateX+0*nDiss+9*HR_num, old_HRy, nCoodrateX+1*nDiss+9*HR_num, HRy[0]);
		
		osd2.setColor(nColor);
		osd2.setWidth(nWidth);
		osd2.setXY(nCoodrateX+1*nDiss+7*HR_num*nDiss, HRy[0], nCoodrateX+2*nDiss+7*HR_num*nDiss, HRy[1]);
//		osd2.setXY(nCoodrateX+1*nDiss+9*HR_num, HRy[0], nCoodrateX+2*nDiss+9*HR_num, HRy[1]);
		
		osd3.setColor(nColor);
		osd3.setWidth(nWidth);
		osd3.setXY(nCoodrateX+2*nDiss+7*HR_num*nDiss, HRy[1], nCoodrateX+3*nDiss+7*HR_num*nDiss, HRy[2]);
//		osd3.setXY(nCoodrateX+2*nDiss+9*HR_num, HRy[1], nCoodrateX+3*nDiss+9*HR_num, HRy[2]);
		
		osd4.setColor(nColor);
		osd4.setWidth(nWidth);
		osd4.setXY(nCoodrateX+3*nDiss+7*HR_num*nDiss, HRy[2], nCoodrateX+4*nDiss+7*HR_num*nDiss, HRy[3]);
//		osd4.setXY(nCoodrateX+3*nDiss+9*HR_num, HRy[2], nCoodrateX+4*nDiss+9*HR_num, HRy[3]);	
		
		osd5.setColor(nColor);
		osd5.setWidth(nWidth);
		osd5.setXY(nCoodrateX+4*nDiss+7*HR_num*nDiss, HRy[3], nCoodrateX+5*nDiss+7*HR_num*nDiss, HRy[4]);
//		osd5.setXY(nCoodrateX+4*nDiss+9*HR_num, HRy[3], nCoodrateX+5*nDiss+9*HR_num, HRy[4]);
		
		osd6.setColor(nColor);
		osd6.setWidth(nWidth);
		osd6.setXY(nCoodrateX+5*nDiss+7*HR_num*nDiss, HRy[4], nCoodrateX+6*nDiss+7*HR_num*nDiss, HRy[5]);
//		osd6.setXY(nCoodrateX+5*nDiss+9*HR_num, HRy[4], nCoodrateX+6*nDiss+9*HR_num, HRy[5]);
		
		osd7.setColor(nColor);
		osd7.setWidth(nWidth);
		osd7.setXY(nCoodrateX+6*nDiss+7*HR_num*nDiss, HRy[5], nCoodrateX+7*nDiss+7*HR_num*nDiss, HRy[6]);
//		osd7.setXY(nCoodrateX+6*nDiss+9*HR_num, HRy[5], nCoodrateX+7*nDiss+9*HR_num, HRy[6]);
		
//		osd8.setColor(nColor);
//		osd8.setWidth(nWidth);
//		osd8.setXY(nCoodrateX+7*nDiss+9*HR_num, HRy[6], nCoodrateX+8*nDiss+9*HR_num, HRy[7]);
//		osd8.setXY(nCoodrateX+7*nDiss+9*HR_num, HRy[6], nCoodrateX+8*nDiss+9*HR_num, HRy[7]);
		
//		osd9.setColor(nColor);
//		osd9.setWidth(nWidth);
//		osd9.setXY(nCoodrateX+8*nDiss+9*HR_num, HRy[7], nCoodrateX+9*nDiss+9*HR_num, HRy[8]);
//		osd9.setXY(nCoodrateX+8*nDiss+9*HR_num, HRy[7], nCoodrateX+9*nDiss+9*HR_num, HRy[8]);
						
//		old_HRy=HRy[8];
		old_HRy=HRy[6];
	
		// 添加 OSD
		if (nShowOsd1) ms_listOsd3.add(osd1);
		ms_listOsd3.add(osd2);
		ms_listOsd3.add(osd3);
		ms_listOsd3.add(osd4);
		ms_listOsd3.add(osd5);
		ms_listOsd3.add(osd6);
		ms_listOsd3.add(osd7);
//		ms_listOsd3.add(osd8);
//		ms_listOsd3.add(osd9);	
		
		// 应答 FC
		responseFX((byte)0xFC);
	}
		
	/**
	 * 解析 FD
	 * @param buffer 缓冲区
	 */
	public static void parseFD(byte[] buffer)
	{
		// AA 55 FD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FD C3 3C
		// AA 55 FD 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FE C3 3C
		// AA 55 FD 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF C3 3C
		
		// 上传 user.txt
		// AA 55 FD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FD C3 3C
		// AA 55 FD 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FE C3 3C
		// AA 55 FD 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF C3 3C
		// AA 55 FD 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C3 3C
		// AA 55 FD 00 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 C3 3C
		// AA 55 FD 00 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 C3 3C
		// AA 55 FD 00 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 C3 3C
		// AA 55 FD 00 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 C3 3C
		// AA 55 FD 00 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 C3 3C
		
		// 上传 data.txt
		// AA 55 FD 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FE C3 3C
		// AA 55 FD 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF C3 3C
		// AA 55 FD 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C3 3C
		// AA 55 FD 01 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 C3 3C
		// AA 55 FD 01 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 C3 3C
		// AA 55 FD 01 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 C3 3C
		// AA 55 FD 01 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 C3 3C
		// AA 55 FD 01 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 C3 3C
		// AA 55 FD 01 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 06 C3 3C
		
		// 运动时间
		// P0 高位
		// P1 低位

		int nP0 = bcd2int(buffer[3]);
		int nP1 = bcd2int(buffer[4]);
		int nStime = nP0 * 100 + nP1 ;
		String strStime = String.format("%d", nStime);
		theApp.setRunTime(strStime);
		
		// 使用时间
		// P2 高位
		// P3 低位
		int nP2 = bcd2int(buffer[5]);
		int nP3 = bcd2int(buffer[6]);
		int nUtime =nP2 * 100 + nP3;
		String strUtime = String.format("%d", nUtime);	
		theApp.setUseTime(strUtime);
		
		// 页面停留时间
		// P4 高位
		// P5 低位
		int nP4 = bcd2int(buffer[7]);
		int nP5 = bcd2int(buffer[8]);
		int nPtime =nP4 * 100 + nP5;
		String strPtime = String.format("%d", nPtime);
		theApp.setPageTime(strPtime);
		
		// 运动距离
		// P6 高位
		// P7 低位		
		int nP6 = bcd2int(buffer[9]);
		int nP7 = bcd2int(buffer[10]);
//		int nDis =nP6 * 100 + nP7;
		double dist = nP6 + nP7 / 100.0 ;
		String strDist = String.format("%.2f", dist);
//		String strDis = String.format("%d", nDis);
		theApp.setDistance(strDist);
		
		// 开关机次数
		// P8 高位
		// P9 低位	
		int nP8 = bcd2int(buffer[11]);
		int nP9 = bcd2int(buffer[12]);
		int nPcounter = nP8 * 100 + nP9;
		nPcounter = theApp.getPowerTimes() + nPcounter;
		String strPcounter = String.format("%d", nPcounter);
		theApp.setTimes(strPcounter);
		theApp.setPowerTimes(nPcounter);
		theApp.writePrefStatus();
		
		// 开关机状态
		int nP10 = bcd2int(buffer[13]);
		int nPstatus =nP10;
		String strPstatus = String.format("%d", nPstatus);
		theApp.setStatus(strPstatus);
		
		// 51版本号
		int nP11 = byte2int(buffer[14]);
		double nVersion = nP11 / 10.0;
		String strnVersion = String.format("%.1f", nVersion);
		theApp.setVersion(strnVersion);		
		
		// 故障代码
		int nP12 = bcd2int(buffer[15]);
		int nErr = nP12;
		String strnErr = String.format("%d", nErr);
		theApp.setErr(strnErr);	
		
		// 故障次数
		int nP13 = bcd2int(buffer[16]);
		int nP14 = bcd2int(buffer[17]);
		int nErrtimes = nP13 * 100 + nP14;
		String strnErrtimes = String.format("%d", nErrtimes);
		theApp.setErrTimes(strnErrtimes);	
		
		// 闲置时间
		int nP15 = bcd2int(buffer[18]);
		int nP16 = bcd2int(buffer[19]);
		int nIdletime = nP15 * 100 + nP16;
		String strnIdletime = String.format("%d", nIdletime);
		theApp.setIdleTime(strnIdletime);	
		
//		MainActivity.loadInternet();
//		theApp.setiCounter(30*1000);
		
	/*	if (buffer == null) return;
		if (buffer.length < COMM_BYTE_NUM) return;

		int nP0 = bcd2int(buffer[3]);

		// 用户数据
		UserData user = new UserData();
		
		// 用户编号, 00: 家庭访客
		int nP1 = bcd2int(buffer[4]);
		int nNum = nP1;
		String strUser = i2Str(nP1, 2);
		
		// 帐号类型
		ms_nAccType = nP1;
		
		// 未登陆
		if (!ms_bLogined)
		{
			// 布局视图登陆
			((MainActivity)ms_curActivity).layoutViewLogin();
		}
		// 已登陆
		else
		{
			// 当 P0 为 01 时, 上传 data.txt 里为 P1 用户编号的数据
			// 当 P0 为 02 时, 上传 user.txt 和 data.txt 里为 P1 用户编号的数据
			if (nP0 == 1 || nP0 == 2)
			{
				// 用户数量
				int nSizeUser = ms_listUserL.size();
				
				if (nSizeUser < 1)
				{
					// 读 user.txt 文件
					readUserFile();
				}
				
				// 数据数量
				int nSizeData = ms_listUserDataL.size();

				if (nSizeData < 1)
				{
					// 读 data.txt 文件
					readDataFile();
				}

				// 数量
				nSizeData = ms_listUserDataL.size();

				for (int i = nSizeData - 1; i >= 0; i--)
				{
					UserData data = ms_listUserDataL.get(i);
					
					// 检测用户编号且未上传数据
					if (nNum == data.getNum() && !data.isUploaded())
					{
						// 上传数据
						((MainActivity)ms_curActivity).uploadData(data);
					}
				}
			}
			
			// 当 P0 为 00 时, 上传 user.txt 里为 P1 用户编号的数据
			// 当 P0 为 02 时, 上传 user.txt 和 data.txt 里为 P1 用户编号的数据
			if (nP0 == 0 || nP0 == 2)
			{
				int nIndex = -1;
				boolean bUpload = false;

				// 用户数量
				int nSizeUser = ms_listUserL.size();
				
				if (nSizeUser < 1)
				{
					// 读 user.txt 文件
					readUserFile();
				}
				
				// 数据数量
				int nSizeData = ms_listUserDataL.size();

				if (nSizeData < 1)
				{
					// 读 data.txt 文件
					readDataFile();
				}
				
				// 数量
				nSizeUser = ms_listUserL.size();
				
				for (int i = 0; i < nSizeUser; i++)
				{
					// 检测用户数据
					if (ms_listUserL.get(i).startsWith(strUser))
					{
						nIndex = i;
						break;
					}
				}
				
				if (nIndex >= 0 && nIndex < nSizeUser)
				{
					List<String> strArray = new ArrayList<String>();

					// 分割字符串
					int nSizeStr = splitString(ms_listUserL.get(nIndex), ",", strArray);
					
					if (nSizeStr >= 5)
					{
						// 上传
						bUpload = true;
						
						// 年龄
						String strAge = strArray.get(1);
						int nAge = str2int(strAge);
						user.setAge(nAge);
						
						// 身高
						String strHeight = strArray.get(2);
						int nHeight = str2int(strHeight);
						user.setHeight(nHeight);
						
						// 体重
						String strWeight = strArray.get(3);
						int nWeight = str2int(strWeight);
						user.setWeight(nWeight);
						
						// 周目标
						String strWeekTarget = strArray.get(4);
						int nWeekTarget = str2int(strWeekTarget);
						user.setWeekTarget(nWeekTarget);
					}
				}
				
				if (bUpload)
				{
					// 上传用户
					((MainActivity)ms_curActivity).uploadUser(user);
				}
			}
		}*/

		// 应答 FD
		responseFX((byte)0xFD);
	}	
	
	/**
	* 设置 自定义 图片
	* @param bmp
	*/
	public static void setMyOsd2(Bitmap bmp,int x,int y)	
	{
		//舒华
		//int wifi_x1 = 1242;
		//int wifi_y1 = 5;
		
		//天展
		//int wifi_x1 = 5;
		//int wifi_y1 = 5;
		
		//大胡子
//		int wifi_x1 = 200;
//		int wifi_y1 = 300;
		    
		try
		{			
			// OSD 图片
			OsdBitmap osd = new OsdBitmap(bmp);
			osd.setX1(x);
			osd.setY1(y);
			osd.setX2(x + bmp.getWidth());
			osd.setY2(y + bmp.getHeight());
			
			// 添加 OSD 图片
			theApp.addOsdBmp(2, osd, false);
		
		}
		catch (Exception e)
		{
		}
	}
				
	/**
	 * 生成二维码图片
	 * @param url
	 */
	 public static void createQRImage(String url,int QRX,int QRY,int QR_WIDTH,int QR_HEIGHT )
	 {
	    try
	    {
	        //判断URL合法性
	        if (url == null || "".equals(url) || url.length() < 1)
	        {
	            return;
	        }
	        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	        
	        //图像数据转换，使用了矩阵转换
	        BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	        
	        //下面这里按照二维码的算法，逐个生成二维码的图片，
	        //两个for循环是图片横列扫描的结果
	        for (int y = 0; y < QR_HEIGHT; y++)
	        {
	            for (int x = 0; x < QR_WIDTH; x++)
	            {
	                if (bitMatrix.get(x, y))
	                {
	                    pixels[y * QR_WIDTH + x] = 0xff000000;
	                }
	                else
	                {
	                    pixels[y * QR_WIDTH + x] = 0xffffffff;
	                }
	            }
	        }
	        
	        //生成二维码图片的格式，使用ARGB_8888
	        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
	        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	        
	        //显示到一个ImageView上面
	        setMyOsd2(bitmap,QRX,QRY);
	    }
	    catch (WriterException e)
	    {
	        e.printStackTrace();
	    }
	}		
	/**
	 * md5加密
	 * @param string 字符串
	 */	 
	 public static String md5(String string)
	 {
		    byte[] hash;
		    try 
		    {
		        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		    } 
		    catch (NoSuchAlgorithmException e) 
		    {
		        throw new RuntimeException("Huh, MD5 should be supported?", e);
		    } 
		    catch (UnsupportedEncodingException e) 
		    {
		        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		    }

		    StringBuilder hex = new StringBuilder(hash.length * 2);
		    for (byte b : hash) 
		    {
		        if ((b & 0xFF) < 0x10) hex.append("0");
		        hex.append(Integer.toHexString(b & 0xFF));
		    }
		    return hex.toString();
	}



	/**
	 * 解析 FE
	 * @param buffer 缓冲区
	 */
	public static void parseFE(byte[] buffer)
	{
		// 参数
//		int nP0 = bcd2int(buffer[3]);
		int nP1 = bcd2int(buffer[4]);

		// 宽度和高度比例
		float fWR = ms_fWidthRatio;
		float fHR = ms_fHeightRatio;
		
		// OSD1 坐标 1
		int nX11 = bcd2int(buffer[5]);
		int nX12 = bcd2int(buffer[6]);
		int nY11 = bcd2int(buffer[7]);
		int nY12 = bcd2int(buffer[8]);
		
		theApp.setOsd11X((int)((nX11 * 100 + nX12) * fWR));
		theApp.setOsd11Y((int)((nY11 * 100 + nY12) * fHR));
		
		if (theApp.SCALED_XY)
		{
			theApp.setOsd11X((int)((nX11 * 100 + nX12) * fWR + 0.5));
			theApp.setOsd11Y((int)((nY11 * 100 + nY12) * fHR + 0.5));		
		}	
		
		int Time = bcd2int(buffer[9])*100+bcd2int(buffer[10]);
		int speedH = bcd2int(buffer[11]);
		int speedL = bcd2int(buffer[12]);
		int Pace = bcd2int(buffer[13])*10000+bcd2int(buffer[14])*100+bcd2int(buffer[15]);
		
		int DistanceH = bcd2int(buffer[16]);
		int DistanceL = bcd2int(buffer[17]);	
		double dist = DistanceH + DistanceL / 100.0 ;
		String strDist = String.format("%.2f", dist);
		
		int Calorie = bcd2int(buffer[18])*100+bcd2int(buffer[19]);
		int Pulse = bcd2int(buffer[20])*100+bcd2int(buffer[21]);
		
		//重量kg
		int Weight = byte2int(buffer[9]);

		//高度cm
		int Height = byte2int(buffer[10]);
		//时间s
		int PowerTime = (byte2int(buffer[11])<<8)|byte2int(buffer[12]);
		//热量
		int PowerCal = (byte2int(buffer[13])<<8)|byte2int(buffer[14]);
		//爆发力
		int Power = (byte2int(buffer[15])<<8)|byte2int(buffer[16]);
		//频次
		int Frq = byte2int(buffer[17]);
		//次数
		int Number = (byte2int(buffer[18])<<8)|byte2int(buffer[19]);
		//平均功率
		int AvgPower = (byte2int(buffer[20])<<8)|byte2int(buffer[21]);

		try
		{
			if (nP1 == 0)
			{
				// 平均速度
				String strSpeed = "";
				if (theApp.getLang().equals("ch"))
					strSpeed = "速度:  " + String.valueOf(speedH)+"."+ String.valueOf(speedL)+ "公里/小时" + "\n";
				else
					strSpeed = "Speed:  " + String.valueOf(speedH)+"."+ String.valueOf(speedL)+ "km/h" + "\r\n";
				theApp.setSpeed(String.valueOf(speedH)+"."+ String.valueOf(speedL));
				
				// 步数
				String strPace = "";
				if (theApp.getLang().equals("ch"))
					strPace = "步数:  " + String.valueOf(Pace)+"步" + "\n";
				else
					strPace = "Pace:  " + String.valueOf(Pace)+ "\r\n";
				theApp.setPace(String.valueOf(Pace));	
				
				// 距离
				String strDistance = "";
				if (theApp.getLang().equals("ch"))
					strDistance = "距离:  " + strDist +"公里" + "\n";
				else
					strDistance = "Distance:  " + strDist + "km" + "\r\n";
				theApp.setDistance(strDist);
				
				// 时间
				String strTime = "";
				if (theApp.getLang().equals("ch"))
					strTime = "时间:   " + String.valueOf(Time)+ "分钟" + "\n";
				else
					strTime = "Time:  " + String.valueOf(Time)+ "min" + "\r\n";
				theApp.setRunTime(String.valueOf(Time));
				
				// 卡路里
				String strCal = "";
				if (theApp.getLang().equals("ch"))
					strCal = "卡路里:   " + String.valueOf(Calorie)+ "千卡" + "\n";
				else
					strCal = "Calorie:  " + String.valueOf(Calorie)+ "kcal" + "\r\n";
				theApp.setCal(String.valueOf(Calorie));
				
				// 心率
				String strPulse = "";
				if (theApp.getLang().equals("ch"))
					strPulse = "心率:   " + String.valueOf(Pulse)+"\n";
				else
					strPulse = "Pulse:  " + String.valueOf(Pulse)+"\r\n";
				theApp.setPulse(String.valueOf(Pulse));

				String string = strSpeed + strPace + strDistance + strTime + strCal + strPulse + "MAC:" + theApp.getMacAdress();

				if (theApp.QRLOGIN)
				{
					theApp.setLoginRsult(1);
					
					MainActivity.loadInternet(1);
				}
				//生成二维码图片
				if (theApp.RSLUTQR) createQRImage(string,theApp.getOsd11X(),theApp.getOsd11Y(),(int)(200 * fWR),(int)(200 * fHR));
				
			}	
			else if (nP1 == 2)
			{
				// 平均高度
				String strHeight = "";
				if (theApp.getLang().equals("ch"))
					strHeight = "高度:  " + String.valueOf(Height) + "cm" + "\n";
				else
					strHeight = "Height:  " + String.valueOf(Height)+ "cm" + "\r\n";
				theApp.setPowerHeight(String.valueOf(Height));
				
				// 平均重量
				String strWeight = "";
				if (theApp.getLang().equals("ch"))
					strWeight = "平均重量:  " + String.valueOf(Weight) +"公斤" + "\n";
				else
					strWeight = "AverageWeight:  " + String.valueOf(Weight)+ "kg" +  "\r\n";
				theApp.setPowerWeight(String.valueOf(Weight));	
				
				// 爆发力
				String strPower = "";
				if (theApp.getLang().equals("ch"))
					strPower = "最大爆发力:  " + String.valueOf(Power) +"瓦" + "\n";
				else
					strPower = "MaxExplosive:  " + String.valueOf(Power) + "w" + "\r\n";
				theApp.setPower(String.valueOf(Power));
				
				// 时间
				String strPowerTime = "";
				if (theApp.getLang().equals("ch"))
					strPowerTime = "时间:   " + String.valueOf(PowerTime)+ "秒" + "\n";
				else
					strPowerTime = "Time:  " + String.valueOf(PowerTime)+ "s" + "\r\n";
				theApp.setPowerTime(String.valueOf(PowerTime));
				
				// 卡路里
				String strPowerCal = "";
				if (theApp.getLang().equals("ch"))
					strPowerCal = "卡路里:   " + String.valueOf(PowerCal)+ "卡" + "\n";
				else
					strPowerCal = "Calorie:  " + String.valueOf(PowerCal)+ "cal" + "\r\n";
				theApp.setCal(String.valueOf(PowerCal));
				
				// 频次
				String strFrq = "";
				if (theApp.getLang().equals("ch"))
					strFrq = "频次:   " + String.valueOf(Frq)+ "次/分" +"\n";
				else
					strFrq = "Frequency:  " + String.valueOf(Frq)+"\r\n";
				theApp.setFrq(String.valueOf(Frq));
				
				// 次数
				String strNumber = "";
				if (theApp.getLang().equals("ch"))
					strNumber = "次数:   " + String.valueOf(Number)+ "次" +"\n";
				else
					strNumber = "Number:  " + String.valueOf(Number)+"\r\n";
				theApp.setNumber(String.valueOf(Number));
				
				// 平均功率
				String strAvgPower = "";
				if (theApp.getLang().equals("ch"))
					strAvgPower = "平均功率:   " + String.valueOf(AvgPower)+ "瓦" +"\n";
				else
					strAvgPower = "Average Power:  " + String.valueOf(AvgPower)+ "w" +"\r\n";
				theApp.setAvgPower(String.valueOf(AvgPower));
										
				String string = strHeight + strWeight + strPower + strPowerTime + strPowerCal + strFrq +
					strNumber  + strAvgPower + "MAC:" + theApp.getMacAdress();
				System.out.println("力量--"+string);

				if (theApp.QRLOGIN)
				{
					theApp.setLoginRsult(2);
					
					MainActivity.loadInternet(2);
				}
				//生成二维码图片
				if (theApp.RSLUTQR) createQRImage(string,theApp.getOsd11X(),theApp.getOsd11Y(),200,200);
				
			}
			else if (nP1 == 3)
			{
				// 平均速度
				String strSpeed = "";
				if (theApp.getLang().equals("ch"))
					strSpeed = "速度:  " + String.valueOf(speedH)+"."+ String.valueOf(speedL)+ "公里/小时" + "\n";
				else
					strSpeed = "Speed:  " + String.valueOf(speedH)+"."+ String.valueOf(speedL)+ "km/h" + "\r\n";
				theApp.setSpeed(String.valueOf(speedH)+"."+ String.valueOf(speedL));
				
				// 步数
				String strPace = "";
				if (theApp.getLang().equals("ch"))
					strPace = "步数:  " + String.valueOf(Pace)+"步" + "\n";
				else
					strPace = "Pace:  " + String.valueOf(Pace)+ "\r\n";
				theApp.setPace(String.valueOf(Pace));	
				
				// 距离
				String strDistance = "";
				if (theApp.getLang().equals("ch"))
					strDistance = "距离:  " + strDist +"公里" + "\n";
				else
					strDistance = "Distance:  " + strDist + "km" + "\r\n";
				theApp.setDistance(strDist);
				
				// 时间
				String strTime = "";
				if (theApp.getLang().equals("ch"))
					strTime = "时间:   " + String.valueOf(Time)+ "分钟" + "\n";
				else
					strTime = "Time:  " + String.valueOf(Time)+ "min" + "\r\n";
				theApp.setRunTime(String.valueOf(Time));
				
				// 卡路里
				String strCal = "";
				if (theApp.getLang().equals("ch"))
					strCal = "卡路里:   " + String.valueOf(Calorie)+ "千卡" + "\n";
				else
					strCal = "Calorie:  " + String.valueOf(Calorie)+ "kcal" + "\r\n";
				theApp.setCal(String.valueOf(Calorie));
				
				// 心率
				String strPulse = "";
				if (theApp.getLang().equals("ch"))
					strPulse = "心率:   " + String.valueOf(Pulse)+"\n";
				else
					strPulse = "Pulse:  " + String.valueOf(Pulse)+"\r\n";
				theApp.setPulse(String.valueOf(Pulse));
				
				String string = strSpeed + strPace + strDistance + strTime + strCal + strPulse + "MAC:" + theApp.getMacAdress();

				if (theApp.QRLOGIN)
				{
					theApp.setLoginRsult(1);
					
					MainActivity.loadInternet(1);
				}
				//生成二维码图片
//				if (theApp.RSLUTQR) createQRImage(string,theApp.getOsd11X(),theApp.getOsd11Y(),200,200);				
			}
			else if (nP1 == 4)
			{
				// 平均速度
				String strSpeed = "";
				if (theApp.getLang().equals("ch"))
					strSpeed = "速度:  " + String.valueOf(speedH)+"."+ String.valueOf(speedL)+ "公里/小时" + "\n";
				else
					strSpeed = "Speed:  " + String.valueOf(speedH)+"."+ String.valueOf(speedL)+ "km/h" + "\r\n";
				theApp.setSpeed(String.valueOf(speedH)+"."+ String.valueOf(speedL));

				// 步数
				String strPace = "";
				if (theApp.getLang().equals("ch"))
					strPace = "步数:  " + String.valueOf(Pace)+"步" + "\n";
				else
					strPace = "Pace:  " + String.valueOf(Pace)+ "\r\n";
				theApp.setPace(String.valueOf(Pace));

				// 距离
				String strDistance = "";
				if (theApp.getLang().equals("ch"))
					strDistance = "距离:  " + strDist +"公里" + "\n";
				else
					strDistance = "Distance:  " + strDist + "km" + "\r\n";
				theApp.setDistance(strDist);

				// 时间
				String strTime = "";
				if (theApp.getLang().equals("ch"))
					strTime = "时间:   " + String.valueOf(Time)+ "分钟" + "\n";
				else
					strTime = "Time:  " + String.valueOf(Time)+ "min" + "\r\n";
				theApp.setRunTime(String.valueOf(Time));

				// 卡路里
				String strCal = "";
				if (theApp.getLang().equals("ch"))
					strCal = "卡路里:   " + String.valueOf(Calorie)+ "千卡" + "\n";
				else
					strCal = "Calorie:  " + String.valueOf(Calorie)+ "kcal" + "\r\n";
				theApp.setCal(String.valueOf(Calorie));

				// 心率
				String strPulse = "";
				if (theApp.getLang().equals("ch"))
					strPulse = "心率:   " + String.valueOf(Pulse)+"\n";
				else
					strPulse = "Pulse:  " + String.valueOf(Pulse)+"\r\n";
				theApp.setPulse(String.valueOf(Pulse));

				String string = strSpeed + strPace + strDistance + strTime + strCal + strPulse + "MAC:" + theApp.getMacAdress();
				System.out.println("跑步---"+string);
			}
			else
			{
				/*if(	ms_curActivity != null && ms_curActivity instanceof MainActivity)
				{
					if (!((MainActivity)ms_curActivity).checkInternetStatus())
						((MainActivity)ms_curActivity).openWifiSetup();
				}*/

				int key = 0;
				key = (int) (Math.random() * 65535);
				theApp.setKey(String.valueOf(key));

//				String key = String.valueOf(theApp.getKey()) ;

				String string ="#" + md5(theApp.getKey()) + "#" + theApp.getMacAdress() + "#";

				//生成二维码图片
//				createQRImage(string,theApp.getOsd11X(),theApp.getOsd11Y(),160,150);
//				createQRImage(string,theApp.getOsd11X(),theApp.getOsd11Y(),100,100);
				// dyp生成登录界面的二维码
				//createQRImage(getMacAdress(),theApp.getOsd11X(),theApp.getOsd11Y(),160,150);

				Log.d(TAG, "========="+PushReceiver.machineId);
				//createQRImage(PushReceiver.machineId,theApp.getOsd11X(),theApp.getOsd11Y(),160,150);
				createQRImage(PushReceiver.machineId,theApp.getOsd11X(),theApp.getOsd11Y(),365,365);
				//createQRImage("dyp",theApp.getOsd11X(),theApp.getOsd11Y(),160,150);
				theApp.setLoginRsult(0);
			}
		}
		catch (Exception e)
		{
		}		

		// 应答 FE
		responseFX((byte)0xFE);
	}		
	//----------------------------------------------------------------------------------------------------
	/**
	 * 应答 C0
	 */
	public static void responseC0()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// C0
		buffer[0] = (byte)0xC0;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 C1
	 */
	public static void responseC1()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// C1
		buffer[0] = (byte)0xC1;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
			
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 C2
	 */
	public static void responseC2()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// C2
		buffer[0] = (byte)0xC2;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
			
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 C3
	 */
	public static void responseC3()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// C3
		buffer[0] = (byte)0xC3;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
				
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 C4
	 */
	public static void responseC4()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// C4
		buffer[0] = (byte)0xC4;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
				
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
				
	/**
	 * 应答 D0
	 * @param nP0
	 * @param nP1
	 */
	public static void responseD0(final int nP0, final int nP1)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// D0
		buffer[0] = (byte)0xD0;

		// 参数
		byte[] b1 = int2bcd(nP0);
		buffer[1] = b1[1];
		
		// 参数
		byte[] b2 = int2bcd(nP1);
		buffer[2] = b2[1];
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 屏幕亮度
		/*float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];*/
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
	
		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	/**
	 * 应答 D9
	 * @param nP0
	 * @param nP1
	 */
	public static void responseD9(final int nP0, final int nP1)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// D9
		buffer[0] = (byte)0xD9;

		// 参数
		byte[] b1 = int2bcd(nP0);
		buffer[1] = b1[1];
		
		// 参数
		byte[] b2 = int2bcd(nP1);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
			
		// 屏幕亮度
		/*float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];*/
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	/**
	 * 应答 DX
	 * @param nP0
	 */
	public static void responseDX(final byte DX, final int nP0)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// DX
		buffer[0] = DX;

		// 参数
		byte[] b1 = int2bcd(nP0);
		buffer[1] = b1[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
				
		// 屏幕亮度
		/*float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];*/
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 DA
	 */
	public static void responseDA()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// DA
		buffer[0] = (byte)0xDA;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}

	/**
	 * 应答 DB
	 */
	public static void responseDB()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// DB
		buffer[0] = (byte)0xDB;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
		
	/**
	 * 应答 E8
	 */

	public static void responseE8()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];

		// E8
		buffer[0] = (byte)0xE8;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];

		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];

		System.out.println("theApp.getLogin()----------->>>> " +theApp.getLogin());

		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;

		/*//判断预约时间是否结束
		if (theApp.getTimeout()) buffer[5] = (byte)0x00;//时间结束了
		else buffer[5] = (byte)0x01; //时间还没结束*/


		/*// 公英制单位
		byte[] bU = int2bcd(ms_nUnits);
		buffer[7] = bU[1];*/

		// 屏幕亮度
		//		float fValue = ms_nScreenBrightness;
		//		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		//		buffer[7] = sb[1];
		//		Log.d(TAG, "ScreenBrightness: " + fValue);
		//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
		//		int nVolume = ms_nStreamVolume;
		//		byte[] v = int2bcd(nVolume);
		//		buffer[9] = v[1];
		//		Log.d(TAG, "StreamVolume: " + nVolume);
		//		Log.d(TAG, "StreamVolume: " + buffer[9]);

		// 前后台
		//		byte[] bg = int2bcd(ms_nBackground);
		//		buffer[10] = bg[1];

		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();

		// 写应答
		writeResponse(buffer);
	}
	/**
	 * 应答 E0
	 */
	public static void responseE0()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];

		// E0
		buffer[0] = (byte)0xE0;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];

		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];

		System.out.println("theApp.getLogin()----------->>>> " +theApp.getLogin());

		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;

		/*//判断预约时间是否结束
		if (theApp.getTimeout()) buffer[5] = (byte)0x00;//时间结束了
		else buffer[5] = (byte)0x01; //时间还没结束*/


		/*// 公英制单位
		byte[] bU = int2bcd(ms_nUnits);
		buffer[7] = bU[1];*/

		// 屏幕亮度
		//		float fValue = ms_nScreenBrightness;
		//		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		//		buffer[7] = sb[1];
		//		Log.d(TAG, "ScreenBrightness: " + fValue);
		//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
		//		int nVolume = ms_nStreamVolume;
		//		byte[] v = int2bcd(nVolume);
		//		buffer[9] = v[1];
		//		Log.d(TAG, "StreamVolume: " + nVolume);
		//		Log.d(TAG, "StreamVolume: " + buffer[9]);

		// 前后台
		//		byte[] bg = int2bcd(ms_nBackground);
		//		buffer[10] = bg[1];

		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();

		// 写应答
		writeResponse(buffer);
	}

	/**
	 * 应答 EA
	 */
	public static void responseEA()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// EA
		buffer[0] = (byte)0xEA;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		// 日期
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int nYear  = calendar.get(Calendar.YEAR);
		int nMonth = calendar.get(Calendar.MONTH) + 1;
		int nDay   = calendar.get(Calendar.DAY_OF_MONTH);
		int nWeek  = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		// 年
		byte[] bY = int2bcd(nYear);
		buffer[3] = bY[0];
		buffer[4] = bY[1];
		
		// 月
		byte[] bM = int2bcd(nMonth);
		buffer[5] = bM[1];
		
		// 日
		byte[] bD = int2bcd(nDay);
		buffer[6] = bD[1];
		
		// 星期
		byte[] bW = int2bcd(nWeek);
		buffer[7] = bW[1];
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 EB
	 */
	public static void responseEB()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// EB
		buffer[0] = (byte)0xEB;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];

		// 获取当前时间
		Date date = new Date(System.currentTimeMillis());
		int nH = date.getHours();
		int nM = date.getMinutes();
		int nS = date.getSeconds();
		
		// 时
		byte[] bH = int2bcd(nH);
		buffer[3] = bH[1];
		
		// 分
		byte[] bM = int2bcd(nM);
		buffer[4] = bM[1];
		
		// 秒
		byte[] bS = int2bcd(nS);
		buffer[5] = bS[1];
		
		// 视频当前位置
//		ms_nVideoCurPos = (int)ms_videoView.getCurrentPosition();
		int nVideoCurPos = theApp.getCurrentPosition();
		byte[] bVT = int2bcd(nVideoCurPos);
		if (nVideoCurPos >= 100)
		{
			buffer[6] = bVT[0];	
			buffer[7] = bVT[1];	
		}
		else
		{
			buffer[7] = bVT[1];
		}
//		theApp.MYSLog(TAG, "strLang" + nVideoCurPos);
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 E2
	 * @param nX 触摸点 X 坐标
	 * @param nY 触摸点 Y 坐标
	 * @param nKey 键值
	 */
	public static void responseKey(final int nX, final int nY, final int nKey)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// E2
		buffer[0] = (byte)0xE2;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		// 触摸点坐标
		byte[] x = int2bcd(nX);
		byte[] y = int2bcd(nY);
		buffer[3] = x[0];
		buffer[4] = x[1];
		buffer[5] = y[0];
		buffer[6] = y[1];
		
		// 屏幕亮度
/*		float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);
*/
		// 蓝牙开关
//		int nBT = 63;
//		if (ms_nBtState == 1) nBT = 62;
		byte[] bt = int2bcd(nKey);
		buffer[7] = bt[1];
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	/**
	 * 应答 E2 场景模式视频
	 * @param nX 触摸点 X 坐标
	 * @param nY 触摸点 Y 坐标
	 * @param nKey 键值
	 */
	public static void responseE2s( final byte nKey)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];

		// E2
		buffer[0] = (byte)0xE2;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		//buffer[1] = b1[1];
		buffer[1] =(byte)0x45;

		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		//buffer[2] = b2[1];
		buffer[2] = 0;


		// 触摸点坐标
		/*byte[] x = int2bcd(nX);
		byte[] y = int2bcd(nY);*/
		buffer[3] = 0;
		buffer[4] = 0;
		buffer[5] = 0;
		buffer[6] = nKey;

		// 屏幕亮度
/*		float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);
*/
		// 蓝牙开关
		//		int nBT = 63;
		//		if (ms_nBtState == 1) nBT = 62;


		if (theApp.getLogin())
			buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
		//		Log.d(TAG, "StreamVolume: " + nVolume);
		//		Log.d(TAG, "StreamVolume: " + buffer[9]);

		// 前后台
		//		byte[] bg = int2bcd(ms_nBackground);
		//		buffer[10] = bg[1];

		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();

		// 写应答
		writeResponse(buffer);
	}
	/**
	 * 应答 E2
	 * @param nX 触摸点 X 坐标
	 * @param nY 触摸点 Y 坐标
	 * @param nKey 键值
	 */
	public static void responseE2(final int nX, final int nY, final int nKey)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// E2
		buffer[0] = (byte)0xE2;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		// 触摸点坐标
		byte[] x = int2bcd(nX);
		byte[] y = int2bcd(nY);
		buffer[3] = x[0];
		buffer[4] = x[1];
		buffer[5] = y[0];
		buffer[6] = y[1];
		
		// 屏幕亮度
/*		float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);
*/
		// 蓝牙开关
//		int nBT = 63;
//		if (ms_nBtState == 1) nBT = 62;
		byte[] bt = int2bcd(nKey);
		buffer[7] = bt[1];
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
		
		// 媒体音量
		int nVolume = ms_nStreamVolume;
		byte[] v = int2bcd(nVolume);
		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();

		// 写应答
		writeResponse(buffer);
	}
				
	/**
	 * 应答 E2
	 * @param nX 触摸点 X 坐标
	 * @param nY 触摸点 Y 坐标
	 */
	public static void responseE2(final int nX, final int nY)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// E2
		buffer[0] = (byte)0xE2;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		// 触摸点坐标
		byte[] x = int2bcd(nX);
		byte[] y = int2bcd(nY);
		buffer[3] = x[0];
		buffer[4] = x[1];
		buffer[5] = y[0];
		buffer[6] = y[1];

		Log.d(TAG, "x坐标"+nX+",Y坐标"+nY);
		/*if (buffer[1]==24&&buffer[2]==0){
			if(nX<1862&&nX>1814&&nY>994&&nY<1044){
				PushReceiver.isOpen = false;
				Log.d(TAG, "x坐标"+nX+",Y坐标"+nY);
			}}*/

		if (theApp.getLogin())
				buffer[7] = (byte)0x01;
				else buffer[7] = (byte)0x00;


		// 屏幕亮度
		/*float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];*/
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 蓝牙开关
		/*	int nBT = 63;
		if (ms_nBtState == 1) nBT = 62;
		byte[] bt = int2bcd(nBT);
		buffer[7] = bt[1];*/
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
	
		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];		
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		System.out.printf("??????"+"buffer[0]"+buffer[0]+"buffer[1]"+buffer[1]+"buffer[2]"+buffer[2]+"buffer[3]"+buffer[3]+"buffer[4]"+buffer[4]+"buffer[5]"+buffer[5]+"buffer[6]"+buffer[6]+"buffer[7]"+buffer[7]+"buffer[8]"+buffer[8]+"buffer[9]"+buffer[9]);
		Log.d(TAG, "??????"+"buffer[0]"+buffer[0]+"buffer[1]"+buffer[1]+"buffer[2]"+buffer[2]+"buffer[3]"+buffer[3]+"buffer[4]"+buffer[4]+"buffer[5]"+buffer[5]+"buffer[6]"+buffer[6]+"buffer[7]"+buffer[7]+"buffer[8]"+buffer[8]+"buffer[9]"+buffer[9]);

		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 E3
	 * @param nX 触摸点 X 坐标
	 * @param nY 触摸点 Y 坐标
	 */
	public static void responseE3(final int nX, final int nY)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// E3
		buffer[0] = (byte)0xE3;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		// 触摸点坐标
		byte[] x = int2bcd(nX);
		byte[] y = int2bcd(nY);
		buffer[3] = x[0];
		buffer[4] = x[1];
		buffer[5] = y[0];
		buffer[6] = y[1];

		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		// 屏幕亮度
	/*	float fValue = ms_nScreenBrightness;
		byte[] sb = int2bcd((int)(10f * fValue / 255f));
		buffer[7] = sb[1];*/
//		Log.d(TAG, "ScreenBrightness: " + fValue);
//		Log.d(TAG, "ScreenBrightness: " + buffer[7]);

		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];		
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 F0
	 * @param nJpgPathNum JPG 路径编号
	 * @param nJpgNameNum JPG 名称编号
	 */
	public static void responseF0(final int nJpgPathNum, final int nJpgNameNum)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// F0
		buffer[0] = (byte)0xF0;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 F1
	 */
	public static void responseF1()
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// F1
		buffer[0] = (byte)0xF1;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];
	
		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 应答 FX
	 * @param FX
	 */
	public static void responseFX(final byte FX)
	{
		// 缓冲区
		byte[] buffer = new byte[RESP_BUFF_NUM];
		
		// FX
		buffer[0] = FX;

		// JPG 路径编号
		byte[] b1 = int2bcd(ms_nJpgPathNum);
		buffer[1] = b1[1];
		
		// JPG 名称编号
		byte[] b2 = int2bcd(ms_nJpgNameNum);
		buffer[2] = b2[1];
		
		if (theApp.getLogin()) buffer[7] = (byte)0x01;
		else buffer[7] = (byte)0x00;
		
		// 语言
		int nLeng = 0;
		if (equals("en", ms_strLang)) nLeng =  0; // 英文
		if (equals("ch", ms_strLang)) nLeng =  1; // 中文
		if (equals("tw", ms_strLang)) nLeng =  2; // 繁体中文, tw
		if (equals("ko", ms_strLang)) nLeng =  3; // 韩语, ko
		if (equals("ja", ms_strLang)) nLeng =  4; // 日语, ja
		if (equals("ru", ms_strLang)) nLeng =  5; // 俄语, ru
		if (equals("de", ms_strLang)) nLeng =  6; // 德语, de
		if (equals("fr", ms_strLang)) nLeng =  7; // 法语, fr
		if (equals("es", ms_strLang)) nLeng =  8; // 西班牙语, es
		if (equals("pt", ms_strLang)) nLeng =  9; // 葡萄牙语, pt
		if (equals("it", ms_strLang)) nLeng = 10; // 意大利语, it
		if (equals("pl", ms_strLang)) nLeng = 11; // 波兰语, pl
		if (equals("sv", ms_strLang)) nLeng = 12; // 瑞典语, sv
		if (equals("ar", ms_strLang)) nLeng = 13; // 阿拉伯语, ar
		if (equals("q3", ms_strLang)) nLeng = 14; // 其它语言 3, q3
		if (equals("tr", ms_strLang)) nLeng = 25; // 土耳其语, tr
		byte[] leng = int2bcd(nLeng);
		buffer[8] = leng[1];

		//蓝牙状态
		getBlueToothState();
		int blueToothState = bluetooth_state;
		byte[] v = int2bcd(blueToothState);
		buffer[9] = v[1];
		// 媒体音量
//		int nVolume = ms_nStreamVolume;
//		byte[] v = int2bcd(nVolume);
//		buffer[9] = v[1];
//		Log.d(TAG, "StreamVolume: " + nVolume);
//		Log.d(TAG, "StreamVolume: " + buffer[9]);
		
		// 前后台
//		byte[] bg = int2bcd(ms_nBackground);
//		buffer[10] = bg[1];
		
		// 获取响应的 P10 字节
		buffer[10] = getResponseP10();
		
		// 写应答
		writeResponse(buffer);
	}
	
	/**
	 * 写应答
	 * @param buffer 缓冲区
	 */
	public static void writeResponse(byte[] buffer)
	{
		try
		{
			int i = 0;
			if (buffer == null) return;
			if (buffer.length < RESP_BUFF_NUM) return;
			
			byte[] send = new byte[RESP_BYTE_NUM];
			
			// 头部指令字节
			send[i++] = HEAD_BYTE[0];
			send[i++] = HEAD_BYTE[1];
			
			send[i++] = buffer[0];
			send[i++] = buffer[1];
			send[i++] = buffer[2];
			send[i++] = buffer[3];
			send[i++] = buffer[4];
			send[i++] = buffer[5];
			send[i++] = buffer[6];
			send[i++] = buffer[7];
			send[i++] = buffer[8];
			send[i++] = buffer[9];
			send[i++] = buffer[10];
			
			// 校验和
			send[i++] = checkSum(2, 12, send);
			
			// 尾部指令字节
			send[i++] = TAIL_BYTE[0];
			send[i++] = TAIL_BYTE[1];

			// 使用蓝牙
			if (ms_bIsUseBt)
			{
				// 写串口
				if (ms_btSpp != null) ms_btSpp.write(send);
			}
			// 不使用蓝牙
			else
			{
				try
				{
					if (theApp.CH340)
					{
						MainActivity.uartInterface_340.WriteData(send,RESP_BYTE_NUM);
					}
					else
					{
						// 写串口
						if (ms_outputStream != null) ms_outputStream.write(send);
					}
				}
				catch (Exception e)
				{
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * 解析指令
	 * @param buffers 缓冲区
	 * @return
	 */
	public static boolean parseCommandDemo(byte[] buffers)
	{
		if (buffers == null) return false;
		if (buffers.length < COMM_BYTE_NUM) return false;
		
		byte[] buffer = new byte[COMM_BYTE_NUM];

		// 数组拷贝
		System.arraycopy(buffers, 0, buffer, 0, COMM_BYTE_NUM);
		
		// 指令
		String strComm = bytes2HexStr(buffer);
		strComm = formatHexStr(strComm);
//		Log.d(TAG, "Command: " + strComm);
		
		// 检测 HEAD 和 TAIL
		if (!checkHEAD(buffer) || !checkTAIL(buffer))
		{
			// 清空缓冲区
			ms_nBytes = 0;
			ms_buffer = new byte[BUFFER_SIZE];
			
			// 头或尾错误
//			Log.d(TAG, "HEAD or TAIL error.");
		}
		else
		{
			// 校验和
			byte b = checkSum(2, 21, buffer);
//			Log.d(TAG, "CheckSum: " + byte2HexStr(b));

			if (b != buffer[22])
			{
				try
				{
					// 清空缓冲区
					clearBuffer(COMM_BYTE_NUM);
					
					// 校验和错误
//					Log.d(TAG, "CheckSum error.");
				}
				catch (Exception e)
				{
				}
			}
			else
			{
				// C2, 系统设置命令
				if (buffer[2] == (byte)0xC2)
				{
					// 解析 C2
					parseC2(buffer);
				}
				// C1, 关机命令
				else if (buffer[2] == (byte)0xC1)
				{
					// 解析 C1
					parseC1(buffer);
				}	
				// C0, 亮度设置
				else if (buffer[2] == (byte)0xC0)
				{
					// 解析 C0
					parseC0(buffer);
				}					
		/*		// D0, 播放 U 盘或者卡上多媒体文件
				else if (buffer[2] == (byte)0xD0)
				{
					// 解析 D0
					parseD0(buffer);
				}
				// D2, 停止多媒体命令
				else if (buffer[2] == (byte)0xD2)
				{
					// 解析 D2
					parseD2(buffer);
				}
				// D3, 上一曲命令
				else if (buffer[2] == (byte)0xD3)
				{
					// 解析 D3
					parseD3(buffer);
				}
				// D4, 下一曲命令
				else if (buffer[2] == (byte)0xD4)
				{
					// 解析 D4
					parseD4(buffer);
				}
				// D5, 暂停命令
				else if (buffer[2] == (byte)0xD5)
				{
					// 解析 D5
					parseD5(buffer);
				}
				// D6, 播放命令
				else if (buffer[2] == (byte)0xD6)
				{
					// 解析 D6
					parseD6(buffer);
				}
				// D9, 多媒体方向键和音量加减命令
				else if (buffer[2] == (byte)0xD9)
				{
					// 解析 D9
					parseD9(buffer);
				}*/
				// DA, 窗口播放电影的命令
				else if (buffer[2] == (byte)0xDA)
				{
					// 解析 DA
					parseDA(buffer);
				}
				// E0, 查询当前模块当前位置的命令
				// AA 55 E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E0 C3 3C
				else if (buffer[2] == (byte)0xE0)
				{
					//判断是跑步机还是磁控车
					if (buffer[6] == (byte)0x01)
					{
						setCkc(true);
					}
					else
					{
						setCkc(false);
					}
					
					// 应答 E0
					responseE0();
				}
				// EA, 时间查询命令
				// AA 55 EA 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 EA C3 3C
				else if (buffer[2] == (byte)0xEA)
				{
					// 应答 EA
					responseEA();
				}
				// EB, 时间查询命令
				// AA 55 EB 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 EB C3 3C
				else if (buffer[2] == (byte)0xEB)
				{
					// 应答 EB
					responseEB();
				}
				// F0, 清定义区域 OSD(OSD1, OSD2, OSD3)
				else if (buffer[2] == (byte)0xF0)
				{
					// 解析 F0
					parseF0(buffer);
				}
				// F1, 综合操作命令, 调大图, 一次贴两个小图(NANDFALSH 上), 打开一个语音( NANDFALSH 上)
				else if (buffer[2] == (byte)0xF1)
				{
					// 解析 F1
					parseF1(buffer);
				}
				// F3, 等高度或等宽度调界面, 刷新状态命令, 一次最多可以刷 10 个状态
				else if (buffer[2] == (byte)0xF3)
				{
					// 解析 F3
					parseF3(buffer);
				}
				// F5, GB2312 OSD 显示命令
				else if (buffer[2] == (byte)0xF5)
				{
					// 解析 F5
					parseF5(buffer);
				}
				// F6, 画线条命令
				else if (buffer[2] == (byte)0xF6)
				{
					// 解析 F6
					parseF6(buffer);
				}
				// F7, 画矩形命令
				else if (buffer[2] == (byte)0xF7)
				{
					// 解析 F7
					parseF7(buffer);
				}
				// F8, 画圆命令
				else if (buffer[2] == (byte)0xF8)
				{
					// 解析 F8
					parseF8(buffer);
				}
				// F9, 字符串显示命令
				else if (buffer[2] == (byte)0xF9)
				{
					// 解析 F9
					parseF9(buffer);
				}
				// FB, 字符串显示命令
				else if (buffer[2] == (byte)0xFB)
				{
					// 解析 FB
					parseFB(buffer);
				}
				
				// 清空缓冲区
				clearBuffer(COMM_BYTE_NUM);
				
				// 绘制图片
				drawBmp();

				return true;
			}
		}
		
		return false;
	}
	/**
	 * 解析指令
	 * @param buffers 缓冲区
	 * @return
	 */
	public static boolean parseCommand(byte[] buffers)
	{
		if (buffers == null) return false;
		if (buffers.length < COMM_BYTE_NUM) return false;
		
		byte[] buffer = new byte[COMM_BYTE_NUM];

		// 数组拷贝
		System.arraycopy(buffers, 0, buffer, 0, COMM_BYTE_NUM);
		
		// 指令
		String strComm = bytes2HexStr(buffer);
		strComm = formatHexStr(strComm);
//		Log.d(TAG, "Command: " + strComm);
		
		// 检测 HEAD 和 TAIL
		if (!checkHEAD(buffer) || !checkTAIL(buffer))
		{
			// 清空缓冲区
			ms_nBytes = 0;
			ms_buffer = new byte[BUFFER_SIZE];
			
			// 头或尾错误
//			Log.d(TAG, "HEAD or TAIL error.");
		}
		else
		{
			// 校验和
			byte b = checkSum(2, 21, buffer);
//			Log.d(TAG, "CheckSum: " + byte2HexStr(b));

			if (b != buffer[22])
			{
				try
				{
					// 清空缓冲区
					clearBuffer(COMM_BYTE_NUM);
					
					// 校验和错误
//					Log.d(TAG, "CheckSum error.");
				}
				catch (Exception e)
				{
				}
			}
			else
			{				
				// C2, 系统设置命令
				if (buffer[2] == (byte)0xC2)
				{
					// 解析 C2
					parseC2(buffer);
				}
				// C1, 关机命令
				else if (buffer[2] == (byte)0xC1)
				{
					// 解析 C1
					parseC1(buffer);
				}
				// C0, 亮度设置
				else if (buffer[2] == (byte)0xC0)
				{
					// 解析 C0
					parseC0(buffer);
				}					
				// C3, 显示系统apk命令
				else if (buffer[2] == (byte)0xC3)
				{
					// 解析 C3
					parseC3(buffer);
				}	
				// C4, 校验图片个数
				else if (buffer[2] == (byte)0xC4)
				{
					// 解析 C4
					parseC4(buffer);
				}					
				// D0, 播放 U 盘或者卡上多媒体文件
				else if (buffer[2] == (byte)0xD0)
				{
					// 解析 D0
					parseD0(buffer);
				}
				// D2, 停止多媒体命令
				else if (buffer[2] == (byte)0xD2)
				{
					// 解析 D2
					parseD2(buffer);
				}
				// D3, 上一曲命令
				else if (buffer[2] == (byte)0xD3)
				{
					// 解析 D3
					parseD3(buffer);
				}
				// D4, 下一曲命令
				else if (buffer[2] == (byte)0xD4)
				{
					// 解析 D4
					parseD4(buffer);
				}
				// D5, 暂停命令
				else if (buffer[2] == (byte)0xD5)
				{
					// 解析 D5
					parseD5(buffer);
				}
				// D6, 播放命令
				else if (buffer[2] == (byte)0xD6)
				{
					// 解析 D6
					parseD6(buffer);
				}
				// D9, 多媒体方向键和音量加减命令
				else if (buffer[2] == (byte)0xD9)
				{
					// 解析 D9
					parseD9(buffer);
				}
				// DA, 窗口播放电影的命令
				else if (buffer[2] == (byte)0xDA)
				{
					// 解析 DA
					parseDA(buffer);
				}
				// DB,打开网页
				else if (buffer[2] == (byte)0xDB)
				{
					// 解析 DB
					parseDB(buffer);
				}				
				// E0, 查询当前模块当前位置的命令
				// AA 55 E0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E0 C3 3C
				else if (buffer[2] == (byte)0xE0)
				{
					//判断是跑步机还是磁控车
					if (buffer[6] == (byte)0x01)
					{
						setCkc(true);
					}
					else
					{
						setCkc(false);
					}
					
					// 应答 E0
					if (theApp.isE0()) responseE0();
					
				}
				// EA, 时间查询命令
				// AA 55 EA 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 EA C3 3C
				else if (buffer[2] == (byte)0xEA)
				{
					// 应答 EA
					responseEA();
				}
				// EB, 时间查询命令
				// AA 55 EB 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 EB C3 3C
				else if (buffer[2] == (byte)0xEB)
				{
					// 应答 EB
					responseEB();
				}
				// F0, 清定义区域 OSD(OSD1, OSD2, OSD3)
				else if (buffer[2] == (byte)0xF0)
				{
					// 解析 F0
					parseF0(buffer);
				}
				// F1, 综合操作命令, 调大图, 一次贴两个小图(NANDFALSH 上), 打开一个语音( NANDFALSH 上)
				else if (buffer[2] == (byte)0xF1)
				{
					// 解析 F1
					parseF1(buffer);
				}			
				// F3, 等高度或等宽度调界面, 刷新状态命令, 一次最多可以刷 10 个状态
				else if (buffer[2] == (byte)0xF3)
				{
					// 解析 F3
					parseF3(buffer);
				}
				// F5, GB2312 OSD 显示命令
				else if (buffer[2] == (byte)0xF5)
				{
					// 解析 F5
					parseF5(buffer);
				}
				// F6, 画线条命令
				else if (buffer[2] == (byte)0xF6)
				{
					// 解析 F6
					parseF6(buffer);
				}
				// F7, 画矩形命令
				else if (buffer[2] == (byte)0xF7)
				{
					// 解析 F7
					parseF7(buffer);
				}
				// F8, 画圆命令
				else if (buffer[2] == (byte)0xF8)
				{
					// 解析 F8
					parseF8(buffer);
				}
				// F9, 字符串显示命令
				else if (buffer[2] == (byte)0xF9)
				{
					// 解析 F9
					parseF9(buffer);
				}
				// FA, 图形旋转命令
				else if (buffer[2] == (byte)0xFA)
				{
					// 解析 FA
					parseFA(buffer);
				}					
				// FB, 字符串显示命令
				else if (buffer[2] == (byte)0xFB)
				{
					// 解析 FB
					parseFB(buffer);
				}
				// FC, 心电图显示命令
				else if (buffer[2] == (byte)0xFC)
				{
					// 解析 FC
					parseFC(buffer);
				}				
				// FD, 上传登录
				else if (buffer[2] == (byte)0xFD)
				{
					// 解析 FD
					parseFD(buffer);
				}
				// FE, 生成二维码
				else if (buffer[2] == (byte)0xFE)
				{
					// 解析 FE
					parseFE(buffer);
				}
												
				// 清空缓冲区
				clearBuffer(COMM_BYTE_NUM);
				
				// 绘制图片
				drawBmp();

				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 解析指令
	 * @param buffers 缓冲区
	 * @return
	 */
	public static boolean parseCommand_(byte[] buffers, final int nStart)
	{
		if (buffers == null) return false;
		if (buffers.length < COMM_BYTE_NUM) return false;
		
		// 缓冲区长度
		int nLen = buffers.length;
		
		byte[] buffer = new byte[COMM_BYTE_NUM];
		
		// 长度
		nLen = Math.min(COMM_BYTE_NUM, nLen);
		
		// 数组拷贝
		System.arraycopy(buffers, nStart, buffer, 0, nLen);
		
		// 指令
		String strComm = bytes2HexStr(buffer);
		strComm = formatHexStr(strComm);
//		Log.d(TAG, "Command: " + strComm);

		// 检测 HEAD 和 TAIL
		if (!checkHEAD(buffer) || !checkTAIL(buffer))
		{
			// 头或尾错误
//			Log.d(TAG, "HEAD or TAIL error.");
		}
		else
		{
			// 校验和
			byte b = checkSum(2, 21, buffer);
//			Log.d(TAG, "CheckSum: " + byte2HexStr(b));

			if (b == buffer[22])
			{
				return true;
			}
			else
			{
				try
				{
					// 校验和错误
//					Log.d(TAG, "CheckSum error.");
				}
				catch (Exception e)
				{
				}
			}
		}
		
		return false;
	}
}

/**
 * JPG 视图类
 */
class JpgView extends View
{
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public JpgView(Context context)
	{
		super(context);
		
		// 初始化
		init_();
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public JpgView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 初始化
		init_();
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public JpgView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init_();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (canvas == null) return;
		
		super.onDraw(canvas);
		
		try
		{
			if (isInEditMode()) return;

			// 绘制 JPG
			theApp.drawBmp(canvas, theApp.MARK_JPG);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 初始化
	 */
	private void init_()
	{
		try
		{
			if (isInEditMode()) return;

			// 设置 JPG 图片
//			theApp.setJpg();

			// 使视图无效
			invalidate();
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * OSD1 视图类
 */
class OsdView1 extends View
{
	// 是否为顶部或底部悬浮窗
	private boolean m_bTop = false;
	private boolean m_bBottom = false;
	public boolean isTop() { return m_bTop; }
	public boolean isBottom() { return m_bBottom; }
	public void setTop(final boolean b) { m_bTop = b; }
	public void setBottom(final boolean b) { m_bBottom = b; }
	
	/**
	 * 构造函数
	 * @param context
	 */
	public OsdView1(Context context)
	{
		super(context);
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public OsdView1(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public OsdView1(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (canvas == null) return;
		
		super.onDraw(canvas);
		
		try
		{
			if (isInEditMode()) return;

			// 启用悬浮窗单个开关
			if (theApp.FLOAT_SWITCH)
			{
				// 关闭悬浮窗
				if (!theApp.isFloatOn())
				{
					// 绘制 OSD1
					theApp.drawBmp(canvas, theApp.MARK_OSD1);
				}
				// 打开悬浮窗
				else
				{
					// 顶部悬浮窗
					if (m_bTop)
					{
						// 绘制 OSD1
						theApp.drawBmpTop(canvas, theApp.MARK_OSD1);
					}
					// 底部悬浮窗
					else if (m_bBottom)
					{
						// 绘制 OSD1
						theApp.drawBmpBottom(canvas, theApp.MARK_OSD1);
					}
				}
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 打开顶部悬浮窗
				if (theApp.isFloatTopOn() && m_bTop)
				{
					// 绘制 OSD1
					theApp.drawBmpTop(canvas, theApp.MARK_OSD1);
				}
				
				// 打开底部悬浮窗
				if (theApp.isFloatBottomOn() && m_bBottom)
				{
					// 绘制 OSD1
					theApp.drawBmpBottom(canvas, theApp.MARK_OSD1);
				}

				// 关闭顶部和底部悬浮窗
				if (!theApp.isFloatTopOn() && !m_bTop &&
					!theApp.isFloatBottomOn() && !m_bBottom)
				{
					// 绘制 OSD1
					theApp.drawBmp(canvas, theApp.MARK_OSD1);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * OSD2 视图类
 */
class OsdView2 extends View
{
	// 是否为顶部或底部悬浮窗
	private boolean m_bTop = false;
	private boolean m_bBottom = false;
	public boolean isTop() { return m_bTop; }
	public boolean isBottom() { return m_bBottom; }
	public void setTop(final boolean b) { m_bTop = b; }
	public void setBottom(final boolean b) { m_bBottom = b; }
	
	/**
	 * 构造函数
	 * @param context
	 */
	public OsdView2(Context context)
	{
		super(context);
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public OsdView2(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public OsdView2(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (canvas == null) return;
		
		super.onDraw(canvas);
		
		try
		{
			if (isInEditMode()) return;

			// 启用悬浮窗单个开关
			if (theApp.FLOAT_SWITCH)
			{
				// 关闭悬浮窗
				if (!theApp.isFloatOn())
				{
					// 绘制 OSD2
					theApp.drawBmp(canvas, theApp.MARK_OSD2);
				}
				// 打开悬浮窗
				else
				{
					// 顶部悬浮窗
					if (m_bTop)
					{
						// 绘制 OSD2
						theApp.drawBmpTop(canvas, theApp.MARK_OSD2);
					}
					// 底部悬浮窗
					else if (m_bBottom)
					{
						// 绘制 OSD2
						theApp.drawBmpBottom(canvas, theApp.MARK_OSD2);
					}
				}
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 打开顶部悬浮窗
				if (theApp.isFloatTopOn() && m_bTop)
				{
					// 绘制 OSD2
					theApp.drawBmpTop(canvas, theApp.MARK_OSD2);
				}
				
				// 打开底部悬浮窗
				if (theApp.isFloatBottomOn() && m_bBottom)
				{
					// 绘制 OSD2
					theApp.drawBmpBottom(canvas, theApp.MARK_OSD2);
				}

				// 关闭顶部和底部悬浮窗
				if (!theApp.isFloatTopOn() && !m_bTop &&
					!theApp.isFloatBottomOn() && !m_bBottom)
				{
					// 绘制 OSD2
					theApp.drawBmp(canvas, theApp.MARK_OSD2);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * OSD3 视图类
 */
class OsdView3 extends View
{
	/**
	 * 构造函数
	 * @param context
	 */
	public OsdView3(Context context)
	{
		super(context);
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public OsdView3(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public OsdView3(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (canvas == null) return;
		
		super.onDraw(canvas);
		
		try
		{
			if (isInEditMode()) return;

			// 绘制 OSD3
			theApp.drawBmp(canvas, theApp.MARK_OSD3);
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * OSD4 视图类
 */
class OsdView4 extends View
{
	/**
	 * 构造函数
	 * @param context
	 */
	public OsdView4(Context context)
	{
		super(context);
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public OsdView4(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public OsdView4(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (canvas == null) return;
		
		super.onDraw(canvas);
		
		try
		{
			if (isInEditMode()) return;

			// 绘制 OSD4
			theApp.drawBmp(canvas, theApp.MARK_OSD4);
		}
		catch (Exception e)
		{
		}
	}
}

//----------------------------------------------------------------------------------------------------
/**
* 用户数据类
*/
class UserData
{
private final String TAG = "UserData";

// 用户编号
private int m_nNum = 0;
public int getNum() { return m_nNum; }
public void setNum(final int n) { m_nNum = n; }

// 年龄
private int m_nAge = 0;
public int getAge() { return m_nAge; }
public void setAge(final int n) { m_nAge = n; }

// 身高
private int m_nHeight = 0;
public int getHeight() { return m_nHeight; }
public void setHeight(final int n) { m_nHeight = n; }

// 体重
private int m_nWeight = 0;
public int getWeight() { return m_nWeight; }
public void setWeight(final int n) { m_nWeight = n; }

// 周目标, 千米
private int m_nWeekTarget = 0;
public int getWeekTarget() { return m_nWeekTarget; }
public void setWeekTarget(final int n) { m_nWeekTarget = n; }

// 当前时间
private long m_lCurTime = 0;
public long getCurTime() { return m_lCurTime; }
public void setCurTime(final long l) { m_lCurTime = l; }

// 是否已经上传
private boolean m_bUploaded = false;
public boolean isUploaded() { return m_bUploaded; }
public void setUploaded(final boolean b) { m_bUploaded = b; }

// 用户名
private String m_strAccount = "";
public String getAccount() { return m_strAccount; }
public void setAccount(final String str) { m_strAccount = str; }

// 步数
private int  m_nStep  = 0;
private long m_lStepH = 0; // 本时的步数
private long m_lStepD = 0; // 本日的步数
private long m_lStepM = 0; // 本月的步数
private long m_lStepY = 0; // 本年的步数
public int  getStep () { return m_nStep;  }
public long getStepH() { return m_lStepH; }
public long getStepD() { return m_lStepD; }
public long getStepM() { return m_lStepM; }
public long getStepY() { return m_lStepY; }
public void setStep (final int  n) { m_nStep  = n; }
public void setStepH(final long l) { m_lStepH = l; }
public void setStepD(final long l) { m_lStepD = l; }
public void setStepM(final long l) { m_lStepM = l; }
public void setStepY(final long l) { m_lStepY = l; }

// 卡路里, Calorie
private int m_nCalo = 0;
private long m_lCaloH = 0; // 本时的卡路里
private long m_lCaloD = 0; // 本日的卡路里
private long m_lCaloM = 0; // 本月的卡路里
private long m_lCaloY = 0; // 本年的卡路里
public int  getCalo () { return m_nCalo;  }
public long getCaloH() { return m_lCaloH; }
public long getCaloD() { return m_lCaloD; }
public long getCaloM() { return m_lCaloM; }
public long getCaloY() { return m_lCaloY; }
public void setCalo (final int  n) { m_nCalo  = n; }
public void setCaloH(final long l) { m_lCaloH = l; }
public void setCaloD(final long l) { m_lCaloD = l; }
public void setCaloM(final long l) { m_lCaloM = l; }
public void setCaloY(final long l) { m_lCaloY = l; }

// 锻炼时长
private int  m_nTime  = 0;
private long m_lTimeH = 0; // 本时的时长
private long m_lTimeD = 0; // 本日的时长
private long m_lTimeM = 0; // 本月的时长
private long m_lTimeY = 0; // 本年的时长
public int  getTime () { return m_nTime;  }
public long getTimeH() { return m_lTimeH; }
public long getTimeD() { return m_lTimeD; }
public long getTimeM() { return m_lTimeM; }
public long getTimeY() { return m_lTimeY; }
public void setTime (final int  n) { m_nTime  = n; }
public void setTimeH(final long l) { m_lTimeH = l; }
public void setTimeD(final long l) { m_lTimeD = l; }
public void setTimeM(final long l) { m_lTimeM = l; }
public void setTimeY(final long l) { m_lTimeY = l; }

// 距离
private double m_dist  = 0.0;
private double m_distH = 0.0; // 本时的距离
private double m_distD = 0.0; // 本日的距离
private double m_distM = 0.0; // 本月的距离
private double m_distY = 0.0; // 本年的距离
public double getDist () { return m_dist;  }
public double getDistH() { return m_distH; }
public double getDistD() { return m_distD; }
public double getDistM() { return m_distM; }
public double getDistY() { return m_distY; }
public void setDist (final double d) { m_dist  = d; }
public void setDistH(final double d) { m_distH = d; }
public void setDistD(final double d) { m_distD = d; }
public void setDistM(final double d) { m_distM = d; }
public void setDistY(final double d) { m_distY = d; }

/**
 * 获取年
 * @return
 */
public int getYear()
{
	try
	{
		String strTime = theApp.l2Str(m_lCurTime, 14);
		
		if (strTime.length() < 4) return 0;
		
		strTime = strTime.substring(0, 4);
		
		int nYear = theApp.str2int(strTime);
		return nYear;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取月
 * @return
 */
public int getMonth()
{
	try
	{
		String strTime = theApp.l2Str(m_lCurTime, 14);
		
		if (strTime.length() < 6) return 0;
		
		strTime = strTime.substring(4, 6);
		
		int nMonth = theApp.str2int(strTime);
		return nMonth;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取日
 * @return
 */
public int getDay()
{
	try
	{
		String strTime = theApp.l2Str(m_lCurTime, 14);
		
		if (strTime.length() < 8) return 0;
		
		strTime = strTime.substring(6, 8);
		
		int nDay = theApp.str2int(strTime);
		return nDay;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取时
 * @return
 */
public int getHour()
{
	try
	{
		String strTime = theApp.l2Str(m_lCurTime, 14);
		
		if (strTime.length() < 10) return 0;
		
		strTime = strTime.substring(8, 10);
		
		int nHour = theApp.str2int(strTime);
		return nHour;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取星期
 * @return
 */
public int getWeek()
{
	try
	{
		return date2Week(getYear(), getMonth(), getDay());
	}
	catch (Exception e)
	{
	}
	
	return 1;
}

/**
 * 获取当前是本年第几周
 * @return
 */
public static int getWeeks()
{
	try
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int nWeeks = calendar.get(Calendar.WEEK_OF_YEAR);
		return nWeeks;
	}
	catch (Exception e)
	{
	}
	
	return 1;
}

/**
 * 获取本周星期一的时间
 * @return 年月日时分秒, 20140324000000
 */
public static long getMonday()
{
	try
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int nMonday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, -nMonday);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取本周星期一的时间
 * @return 年月日时分秒, 20140331000000
 */
public static long getNextMonday()
{
	try
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int nMonday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, -nMonday + 7);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取上周星期一的时间
 * @param nWeek 上几周, 1: 上一周, 2: 上两周
 * @return 年月日时分秒, 20140317000000
 */
public static long getPrevWeek1(final int nWeek)
{
	try
	{
		if (nWeek < 1 || nWeek > 51) return 0;
		
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int nMonday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, -nMonday - 7 * nWeek);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取本月 1 日的时间
 * @return 年月日时分秒, 20140301000000
 */
public static long getMonth1()
{
	try
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendar.set(Calendar.DATE, 1);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取上月 1 日的时间
 * @param nMonth 上几周, 1: 上一个月, 2: 上两个月
 * @return 年月日时分秒, 20140301000000
 */
public static long getPrevMonth1(final int nMonth)
{
	try
	{
		if (nMonth < 1 || nMonth > 11) return 0;
		
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, -nMonth);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取下月 1 日的时间
 * @return 年月日时分秒, 20140401000000
 */
public static long getNextMonth1()
{
	try
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, 1);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 获取本年 1 月 1 日的时间
 * @return 年月日时分秒, 20140101000000
 */
public static long getYear1()
{
	try
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		int nY = calendar.get(Calendar.YEAR);
		int nM = calendar.get(Calendar.MONTH) + 1;
		int nD = calendar.get(Calendar.DAY_OF_MONTH);
		String strY = theApp.i2Str(nY, 4);
		String strM = theApp.i2Str(nM, 2);
		String strD = theApp.i2Str(nD, 2);
		String strT = strY + strM + strD;
		
		long lT = Long.valueOf(strT);
		long lTime = lT * 1000000L;
		return lTime;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 日期转为星期
 * @param nYear 年, 2014
 * @param nMonth 月, 3
 * @param nDay 日, 26
 * @return 1: 星期一, 2: 星期二, 3: 星期三, 4: , 星期四, 5: 星期五, 6: 星期六, 7: 星期日
 */
public static int date2Week(final int nYear, final int nMonth, final int nDay)
{
	try
	{
		// 日期
		int nW = 1;
		int nY = nYear;
		int nM = nMonth;
		int nD = nDay;
		if (nY < 0 || nM <= 0 || nD <= 0 || nM > 12 || nD > 31) return 0;

		// 一月, 二月当作前一年的十三, 十四月
		if (nM == 1 || nM == 2)
		{
			nM += 12;
			nY--;
		}

		// 1752 年 9 月 3 日前
		if ((nY <  1752) ||
			(nY == 1752 && nM < 9) ||
			(nY == 1752 && nM == 9 && nD < 3))
		{
			nW = (nD + 2 * nM + 3 * (nM + 1) / 5 + nY + nY / 4 + 5) % 7;
		}
		// 1752 年 9 月 3 日后
		else
		{
			nW = (nD + 2 * nM + 3 * (nM + 1) / 5 + nY + nY / 4 - nY / 100 + nY / 400) % 7;
		}

		// 星期
		nW += 1;
		return nW;
	}
	catch (Exception e)
	{
	}
	
	return 0;
}

/**
 * 添加
 * @param list
 */
public void add(List<UserData> list)
{
	try
	{
		if (list == null) return;

		boolean bSet = false;
		
		// 数量
		int nSize = list.size();
		
		for (int i = 0; i < nSize; i++)
		{
			// 用户数据
			UserData user = list.get(i);
			
			// 检测用户编号
			if (m_nNum != user.m_nNum) continue;
			
			// 检测时间
			if (m_lCurTime >= user.m_lCurTime)
			{
				// 已设置
				bSet = true;
				
				// 设置
				list.set(i, this);
			}
		}
		
		// 未设置
		if (!bSet)
		{
			// 添加
			list.add(this);
		}
	}
	catch (Exception e)
	{
	}
}

/**
 * 上传用户
 * @return
 */
public boolean uploadUser()
{
	try
	{
		if (theApp.getAccInfo() == null) return false;
		
		boolean bIsSuccess = false;
		
		try
		{
			// 数据
			int    nAge    = m_nAge;
			double dHeight = (double)m_nHeight / 100.0; // 米
			double dWeight = m_nWeight;
			double dGoal   = m_nWeekTarget; // 千米

			// 上传接口
			String strUrl = theApp.getUrl();
			strUrl = strUrl + "updateMember.ct?";

			// 登陆参数
			List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
			listParams.add(new BasicNameValuePair("member.id", "" + theApp.getAccInfo().getNMemberId()));
			listParams.add(new BasicNameValuePair("member.age", "" + nAge));
			listParams.add(new BasicNameValuePair("member.height", "" + dHeight));
			listParams.add(new BasicNameValuePair("member.weight", "" + dWeight));
			listParams.add(new BasicNameValuePair("member.goal", "" + dGoal));
			listParams.add(new BasicNameValuePair("member.typeName", "" + theApp.getAccType()));

			// 参数转 utf-8 的 url 编码
			strUrl = strUrl + URLEncodedUtils.format(listParams, "UTF-8");

			// 访问网站超时设置
			BasicHttpParams httpParams = new BasicHttpParams();

			// 设置请求超时时间(毫秒)
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

			// 设置等待数据超时时间(毫秒)
			HttpConnectionParams.setSoTimeout(httpParams, 3000);

			// 生成 HttpClient
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// 访问方式
			HttpGet getMethod = new HttpGet(strUrl);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			// 网站响应及返回值
			String strResponse = httpClient.execute(getMethod, responseHandler);

			// 转成 utf-8 的编码, 若还是乱码则用 unicode 编码
			strResponse = new String(strResponse.getBytes("ISO-8859-1"), "UTF-8");

			// 返回值转 json 数据格式
			JSONObject json = new JSONObject(strResponse);

			// 判断是否登陆成功
			if (json.has("success"))
			{
				bIsSuccess = json.getBoolean("success");
			}
			
			Log.d(TAG, String.valueOf(bIsSuccess));
			return bIsSuccess;
		}
		catch (Exception e)
		{
			Log.d(TAG, "error:" + e.getMessage());
		}
	}
	catch (Exception e)
	{
	}
	
	return false;
}

/**
 * 上传数据
 * @return
 */
public boolean uploadData()
{
	try
	{
		if (theApp.getAccInfo() == null) return false;
		
		boolean bIsSuccess = false;
		
		// 已上传数据
		if (m_bUploaded) return true;
		
		try
		{
			// 数据
			int nExerciseDuration   = m_nTime;
			int nRunningSteps       = m_nStep;
			int nConsumeCalorie     = m_nCalo;
			double dRunningDistance = m_dist * 1000; // 米
			
			// 上传接口
			String strUrl = theApp.getUrl();
			strUrl = strUrl + "treadmillDataUpload.ct?";
			
			// 登陆参数
			List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
			listParams.add(new BasicNameValuePair("member.id", "" + theApp.getAccInfo().getNMemberId()));
			listParams.add(new BasicNameValuePair("treadmill.exerciseDuration", "" + nExerciseDuration));
			listParams.add(new BasicNameValuePair("treadmill.runningDistance", "" + dRunningDistance));
			listParams.add(new BasicNameValuePair("treadmill.runningSteps", "" + nRunningSteps));
			listParams.add(new BasicNameValuePair("treadmill.consumeCalorie", "" + nConsumeCalorie));

			// 参数转 utf-8 的 url 编码
			strUrl = strUrl + URLEncodedUtils.format(listParams, "UTF-8");

			// 访问网站超时设置
			BasicHttpParams httpParams = new BasicHttpParams();

			// 设置请求超时时间(毫秒)
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

			// 设置等待数据超时时间(毫秒)
			HttpConnectionParams.setSoTimeout(httpParams, 3000);

			// 生成 HttpClient
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// 访问方式
			HttpGet getMethod = new HttpGet(strUrl);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			// 网站响应及返回值
			String strResponse = httpClient.execute(getMethod, responseHandler);

			// 转成 utf-8 的编码, 若还是乱码则用 unicode 编码
			strResponse = new String(strResponse.getBytes("ISO-8859-1"), "UTF-8");

			// 返回值转 json 数据格式
			JSONObject json = new JSONObject(strResponse);

			// 判断是否登陆成功
			if (json.has("success"))
			{
				bIsSuccess = json.getBoolean("success");
			}

			if (bIsSuccess)
			{
				// 已上传
				m_bUploaded = true;
			}
							
			Log.d(TAG, String.valueOf(bIsSuccess));
			return bIsSuccess;
		}
		catch (Exception e)
		{
			Log.d(TAG, "error:" + e.getMessage());
		}
	}
	catch (Exception e)
	{
	}
	
	return false;
}

/**
 * 上传TB数据
 * @return
 */
public boolean uploadTbdata()
{
	try
	{
//		if (theApp.getAccInfo() == null) return false;
		
		boolean bIsSuccess = false;
		
		try
		{
			// 数据
//			int    nAge    = m_nAge;
//			double dHeight = (double)m_nHeight / 100.0; // 米
//			double dWeight = m_nWeight;
//			double dGoal   = m_nWeekTarget; // 千米

			// 上传接口
			String strUrl = theApp.getUrl();
			strUrl = strUrl + "m=Httptest&a=runnerdata";

			// 登陆参数
			List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
			listParams.add(new BasicNameValuePair("&modle=", "F0010108070500"));
			listParams.add(new BasicNameValuePair("&mac_address=", "777777777777"));
			listParams.add(new BasicNameValuePair("&inverter=", "dHeight"));
//			listParams.add(new BasicNameValuePair("member.weight", "" + dWeight));
//			listParams.add(new BasicNameValuePair("member.goal", "" + dGoal));
//			listParams.add(new BasicNameValuePair("member.typeName", "" + theApp.getAccType()));

			// 参数转 utf-8 的 url 编码
			strUrl = strUrl + URLEncodedUtils.format(listParams, "UTF-8");

			// 访问网站超时设置
			BasicHttpParams httpParams = new BasicHttpParams();

			// 设置请求超时时间(毫秒)
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

			// 设置等待数据超时时间(毫秒)
			HttpConnectionParams.setSoTimeout(httpParams, 3000);

			// 生成 HttpClient
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// 访问方式
			HttpGet getMethod = new HttpGet(strUrl);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			// 网站响应及返回值
			String strResponse = httpClient.execute(getMethod, responseHandler);

			// 转成 utf-8 的编码, 若还是乱码则用 unicode 编码
			strResponse = new String(strResponse.getBytes("ISO-8859-1"), "UTF-8");

			// 返回值转 json 数据格式
			JSONObject json = new JSONObject(strResponse);

			// 判断是否登陆成功
			if (json.has("success"))
			{
				bIsSuccess = json.getBoolean("success");
			}
			
			Log.d(TAG, String.valueOf(bIsSuccess));
			return bIsSuccess;
		}
		catch (Exception e)
		{
			Log.d(TAG, "error:" + e.getMessage());
		}
	}
	catch (Exception e)
	{
	}
	
	return false;
}

}

/**
* 用户数据时间比较器
*/
class UserDataTimeComparator implements Comparator<UserData>
{
	@Override
	public int compare(UserData arg1, UserData arg2)
	{
		try
		{
			// 时间
			long lTime1 = arg1.getCurTime();
			long lTime2 = arg2.getCurTime();
			
			if (lTime1 < lTime2)
			{
				return -1;
			}
			
			if (lTime1 > lTime2)
			{
				return 1;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
}

/**
* 用户数据步数比较器
*/
class UserDataStepComparator implements Comparator<UserData>
{
	@Override
	public int compare(UserData arg1, UserData arg2)
	{
		try
		{
			// 步数
			int nStep1 = arg1.getStep();
			int nStep2 = arg2.getStep();
			
			if (nStep1 < nStep2)
			{
				return -1;
			}
			
			if (nStep1 > nStep2)
			{
				return 1;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
}

/**
* 用户数据距离比较器
*/
class UserDataDistComparator implements Comparator<UserData>
{
	@Override
	public int compare(UserData arg1, UserData arg2)
	{
		try
		{
			// 距离
			double dist1 = arg1.getDist();
			double dist2 = arg2.getDist();
			
			if (dist1 < dist2)
			{
				return -1;
			}
			
			if (dist1 > dist2)
			{
				return 1;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
}

/**
* 用户数据卡路里比较器
*/
class UserDataCaloComparator implements Comparator<UserData>
{
	@Override
	public int compare(UserData arg1, UserData arg2)
	{
		try
		{
			// 卡路里
			int nCalo1 = arg1.getCalo();
			int nCalo2 = arg2.getCalo();
			
			if (nCalo1 < nCalo2)
			{
				return -1;
			}
			
			if (nCalo1 > nCalo2)
			{
				return 1;
			}
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
}
