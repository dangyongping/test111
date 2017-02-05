package com.chinafeisite.tianbu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.chinafeisite.tianbu.BrowserLayout.WebPageLabel;
import com.chinafeisite.tianbu.competition.WheelView;
import com.chinafeisite.tianbu.okhttpUtils.MyCallback;
import com.chinafeisite.tianbu.okhttpUtils.OkhttpTool;
import com.chinafeisite.tianbu.okhttpUtils.StringPaser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

//import android.media.MediaPlayer;

//import dalvik.system.VMRuntime;
/**
 * 主 Activity 类
 */
@SuppressLint({ "InlinedApi", "WrongViewCast" })
public class MainActivity extends FragmentActivity
{
	private final static String TAG = "MainActivity";

	// 缩放数量
	private int m_nScaleJpg = 0;
	private int m_nScalePng = 0;

	// 缩放比例
	private double m_scaleW = 1.0;
	private double m_scaleH = 1.0;

	// 时间
	private long m_lStartTime = 0;
	private long m_lEndTime = 0;

	// 连接设备名称
	private static String ms_strConnectedDevice = "";

	// 蓝牙适配器
	private BluetoothAdapter m_btAdapter = null;

	// Activity 的 Context
	public static Context m_context = null;

	// 进度对话框
	private ProgressDialog m_progDlg = null;

	//定义一个WifiManager对象
	private static WifiManager mWifiManager;
	private static int ms_wifiState =0;

	//TB数据
//	private static UserData tbdata;

	// 图像加载器
	private ImageLoader m_imageLoader = ImageLoader.getInstance();

	// 插入, 卸载或拔出 SD 卡接收器
	private EjectSDCardReceiver m_ejectSDCardReceiver = null;

	// 是否可以进入wifi设置
	private static boolean ms_bWifiSetup = false;

	// 布局
	private WiFiLayout        m_layWiFi        = null;
	private MusicLayout       m_layMusic       = null;
	private VideoLayout       m_layVideo       = null;
	private SetupLayout       m_laySetup       = null;
	private BrowserLayout     m_layBrowser     = null;
//    private MapLayout m_layoutMap = null;
//	private BrowserLayoutOld  m_layBrowserOld  = null;
	private AppsVpiLayout     m_layAppsVpi     = null;
	private AppsGridLayout    m_layAppsGrid    = null;
	private PictureGridLayout m_layPictureGrid = null;
//	private PictureListLayout m_layPictureList = null;
	private RelativeLayout    m_layContent     = null;
	private RelativeLayout    m_layVideoMain   = null;
	private static AlertDialog mDialog;
	private ComponentName mComponet;
	//private TextView mTv_time;

	public WiFiLayout getWiFiLayout() { return m_layWiFi; }
	private LoginLayout       m_layLogin       = null;
	private SignInLayout      m_laySignIn      = null;
	private HistoryLayout     m_layHistory     = null;
	public HistoryLayout getHistoryLayout() { return m_layHistory; }

	// 消息
	private static final int MSG_TOAST    =   0; // Toast

	private static final int MSG_QUERY_OK    =  11; // 查询成功
	private static final int MSG_DOWNLOAD    =  21; // 下载提示
	private static final int MSG_RESUME   = 101; // 恢复
	private static final int MSG_REDRAW   = 102; // 重绘
	private static final int MSG_INIT_M   = 103; // 初始化音乐
	private static final int MSG_INIT_V   = 104; // 初始化视频
	private static final int MSG_INIT_P   = 105; // 初始化图片
	private static final int MSG_INIT_A   = 106; // 初始化 Apps
	private static final int MSG_EXT_LIST = 107; // 获取扩展 SDCard 列表
	private static final int MSG_COPY     = 108;  // 拷贝
	private static final int MSG_COPY_OK  = 109;  //拷贝完成
	private static final int MSG_UNZIP_OK = 110;	//解压完成
	private static final int MSG_GET_WIFI = 111; // 获取wifi强度
	private static final int MSG_SHUTDWON =112; //关机
	private static final int MSG_REUNZIP = 113;	//重新解压
	private static final int MSG_RECOPY = 114;	//重新拷贝
	private static final int MSG_LOAD_SEVER = 115; //连接服务器
	private static final int MSG_SETID = 116; //连接服务器

	// 系统apk编号
//	private static final int ApkNum[ ]    =  {0}; // Toast
	public static final int NUM_ = 6;

	private static String baseURL = "http://www.tb-tech.cn/index.php?m=Httptest&a=runnerdata";

	private static String baseURL_Rsl = "http://www.tb-tech.cn/index.php?m=Httptest&a=wechatdata";

	private static String baseURL_Pwl = "http://www.tb-tech.cn/index.php?m=Httptest&a=powerdata";

	private static String macURL = null;
	// 图片丢失提示
	private static String ms_strPicToastEn = "System file is missing, please reboot the system!!!";
	private static String ms_strPicToastCh = "系统文件丢失请重新启动！！！";
	private static String ms_strPoweroffCh = "电源关闭中，请稍后...";
	private static String ms_strPoweroffEn = "Power Off, Please Waiting ...";
	//图片数量
//	private static int nPicNum = 0;
//	private static boolean nWifiFlag = false;

	//ch340参数
//	private static final String ACTION_USB_PERMISSION = null;
//	private static final String ACTION_USB_PERMISSION = "com.wch.wchusbdriver.USB_PERMISSION";
	public static CH340AndroidDriver uartInterface_340;
	private static final String ACTION_USB_PERMISSION = "com.chinafeisite.tianbu.USB_PERMISSION";
	public static final int baudRate = 57600; /* baud rate */
	public static final byte stopBit = 1 ;/* 1:1stop bits, 2:2 stop bits */
	public static final byte dataBit = 8 ; /* 8:8bit, 7: 7bit 6: 6bit 5: 5bit*/
	public static final byte parity = 0 ; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
	public static final byte flowControl = 0; /* 0:none, 1: flow control(CTS,RTS) */

	//设置虚拟机初始大小与gc效率
//	private final static float TARGET_HEAP_UTILIZATION = 0.75f;
	Handler mHandler;
	TextView mTv_time;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		try
		{
			super.onCreate(savedInstanceState);
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			// Activity 的 Context
			m_context = this;

			// 去除标题栏, 设置全屏
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			// SDK_INT
			int SDK_INT = android.os.Build.VERSION.SDK_INT;
			String strSDK = String.format("android.os.Build.VERSION.SDK_INT: %d", SDK_INT);
			Log.d(TAG, strSDK);
			Log.d(TAG, "onCreate");
			//旋转180
		//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			try
			{
				//取得WifiManager对象
				mWifiManager=(WifiManager) m_context.getSystemService(Context.WIFI_SERVICE);

				if (theApp.USE_TBSEVER) openWifi();

	//			theApp.setKey(String.valueOf((int) (Math.random() * 65535)));

	//			Log.d(TAG, theApp.getKey());

				//设置虚拟机初始堆内存大小和GC效率
	//			VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
			}
			catch (Exception e)
			{}
			/*****************************************************/
			try
			{
				// API11
				if (SDK_INT >= 11)
//				if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					// 需要在 AndroidManifest.xml 的 application 中添加如下内容:
					// android:hardwareAccelerated="true"

					// 开启硬件加速
					getWindow().setFlags(0x01000000, 0x01000000);
//					getWindow().setFlags(
//							WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//							WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
				}
			}
			catch (Exception e)
			{
			}
			/**************************************************************/
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				// 设置内容视图
				setContentView(R.layout.activity_main_my);
			}
			// 使用 VITAMIO
			else
			{
				// 设置内容视图
				setContentView(R.layout.activity_main);
			}
			PushSettings.enableDebugMode(MainActivity.this, true);
			//百度云推送初始化
			PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"jGHFP7suVWG627HZh10MAuAi");


			// 当前 Activity
			theApp.setCurActivity(this);

			try
			{
				// 创建
				if (!theApp.isCreated()) theApp.onCreate_(this);
			}
			catch (Exception e)
			{
			}

		mTv_time =  (TextView) findViewById(R.id.tv_time);
			Timer timer = new Timer();
			timer.schedule(mTask,1000,1000);
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch(msg.what){
						case 1111:
							Bundle bundle = msg.getData();
							String LL    = bundle.getString("time");
							mTv_time.setText(LL);
						case 1112:
					}
					super.handleMessage(msg);
				}
			};

			// 获取属性
			SharedPreferences sp = getPreferences(MODE_PRIVATE);
			theApp.setPref(sp);
			theApp.readPref();
			if (theApp.USE_TBSEVER)
			{
				theApp.setPrefStatus(sp);

				// 读共享属
				theApp.readPrefStatus();
			}

		}
		catch (Exception e)
		{
		}
		/**************************************************************/
		try
		{
			// 需要在 AndroidManifest.xml 中添加如下内容:
/*
			<activity
	            android:name="io.vov.vitamio.activity.InitActivity"
	            android:configChanges="orientation|keyboardHidden|navigation"
	            android:launchMode="singleTop"
	            android:theme="@android:style/Theme.NoTitleBar"
	            android:windowSoftInputMode="stateAlwaysHidden" >
	        </activity>
*/
			// 不使用 VITAMIO
			if (theApp.VITAMIO)
			{
				try
				{
					// 检测 Vitamio
					if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)) return;
				}
				catch (Exception e)
				{

				}
			}
		}
		catch (Exception e)
		{
		}
		/**************************************************************/
		// 未初始化
		if (!theApp.isInit())
		{
			try
			{
				// 设置日志路径
				String strLogDir = getLogDir();
				if (strLogDir != null) JLog.setPath(strLogDir);
			}
			catch (Exception e)
			{
			}
		}

		// 未初始化
		if (!theApp.isInit())
		{
			try
			{
				// 获取扩展 SD 卡
				String strExtSDCard = getExtSDCard();
				Log.d(TAG, "ExtSDCard: " + strExtSDCard);

				// 获取路径
				String strDefPath = getDefPath();
				//设置路径
				theApp.setDefPath(strDefPath);
				String strmovieDefPath = getmovieDefPath();
				theApp.setmovieDefPath(strmovieDefPath);

				if (theApp.USE_ZIP)
				{
					//检测zip文件
					showProgDlg();

				}
				else
				{
					// 拷贝文件夹
					copyFolder();
				}
			}
			catch (Exception e)
			{
			}
		}
//		EjectSDCardReceiver.registerReceiver(m_context);

		// 未初始化
		if (!theApp.isInit())
		{
			try
			{
				// 显示通知
				theApp.showNotify(this);

				// 客户文件名
//				String strDefPath = theApp.getDefPath();
//				String strFileName = strDefPath + "customer";
//				theApp.mkdir(strFileName);

				// 读 "SETUP.txt" 文件
				readSetupFile();

				// 只允许调节媒体音量
				setVolumeControlStream(AudioManager.STREAM_MUSIC);

				// 加载声音
				new Thread() { public void run() { loadSound(); } }.start();
			}
			catch (Exception e)
			{
			}
		}

		// 未初始化
		if (!theApp.isInit())
		{
			try
			{
				new Thread()
				{
					public void run()
					{
						// 获取媒体音量
						int nSV = theApp.getStreamVolume(MainActivity.this);
						theApp.setStreamVolume(nSV);

						// 获取屏幕亮度
						int nSB = theApp.getBrightness(MainActivity.this);
						theApp.setScreenBrightness(nSB);

					}
				}.start();

				// 启动移动线程
				new MoveThread().start();

				// 启动获取wifi强度线程
				if (theApp.WIFI_DETEC) { new CheckWifiThread().start();	}

				//启动服务器线程
//				if (theApp.USE_TBSEVER) { new LoadSeverThread().start();}

				// 启动系统信息线程
//				new SysInfoThread().start();

				// 启动拔插 SD 卡监听线程
//				new EjectSDCardThread().start();
			}
			catch (Exception e)
			{
			}
		}
		/**************************************************************/
		try
		{
			// 获取资源
			Resources res = getResources();

			// 获取显示度量
			DisplayMetrics dm = res.getDisplayMetrics();

			// 屏幕宽度和高度
			theApp.setWidthPixels(dm.widthPixels);
			theApp.setHeightPixels(dm.heightPixels);
			Log.d(TAG, "WidthPixels: " + dm.widthPixels);
			Log.d(TAG, "HeightPixels: " + dm.heightPixels);

			// 未初始化
			if (!theApp.isInit())
			{
				try
				{
					// 获取 00.jpg 底图
//					get00Jpg();

					// 检测 picture 文件夹
//					checkPicture();

					// picture 文件夹路径
					if (theApp.getPicturePath().equals(""))
					{
						String strPath = theApp.getDefPath() + "picture/";
						theApp.setPicturePath(strPath);
					}
				}
				catch (Exception e)
				{

				}
			}

/********xwx**********/
			// 使用蓝牙
			if (theApp.isUseBt())
			{
				try
				{
					// 设备连接中断
					String strDeviceLost = getString(R.string.strDeviceLost);
					BtSppService.setDeviceLost(strDeviceLost);

					// 无法连接到设备
					String strUnableConnect = getString(R.string.strUnableConnect);
					BtSppService.setUnableConnect(strUnableConnect);

					// 获取本地蓝牙适配器
					m_btAdapter = BluetoothAdapter.getDefaultAdapter();

					if (m_btAdapter == null)
					{
						// 不使用蓝牙
						theApp.setUseBt(false);

						// 不支持蓝牙。
//						Toast.makeText(this, R.string.strBtNotAvailable, Toast.LENGTH_SHORT).show();
					}

					// 启动蓝牙
					if (theApp.isUseBt()) startBt();
					Log.d(TAG, "startBt");
				}
				catch (Exception e)
				{
				}
			}

			// 未初始化
			if (!theApp.isInit())
			{
				// 不使用蓝牙
				if (!theApp.isUseBt())
				{
					try
					{
						if (theApp.CH340)
						{
							boolean flags;
							uartInterface_340 = new CH340AndroidDriver(
								(UsbManager) getSystemService(Context.USB_SERVICE), this,
								ACTION_USB_PERMISSION);

						/*	if(2 == uartInterface_340.ResumeUsbList())
							{
								Toast.makeText(m_context, "close device", Toast.LENGTH_SHORT).show();
								uartInterface_340.CloseDevice();
								Log.d(TAG, "Enter onResume Error");
							}
							*/
							if(uartInterface_340.isConnected())
							{
								flags = uartInterface_340.UartInit();
								if(!flags)
								{
									Log.d(TAG, "Init Uart Error");
									Toast.makeText(m_context, "Init Uart Error", Toast.LENGTH_SHORT).show();
								}
								else
								{
									if(uartInterface_340.SetConfig(57600, dataBit, stopBit, parity, flowControl))
									{
										Toast.makeText(m_context, "Init Uart OK", Toast.LENGTH_SHORT).show();
										Log.d(TAG, "Configed");
									}
								}
							}
							else
							{
								Log.d(TAG, "Connect faild");
								Toast.makeText(m_context, "Not Connect", Toast.LENGTH_SHORT).show();
							}
						}
						else
						{
							// 设置串口
							String strErr = theApp.setSerialPort();
							if (strErr != null) Toast.makeText(this, strErr, Toast.LENGTH_LONG).show();
						}
					}
					catch (Exception e)
					{
					}
				}
			}
/***********************启动读串口线程********************************************/
			// 未初始化
			if (!theApp.isInit())
			{
				// 不使用蓝牙
				if (!theApp.isUseBt())
				{
					try
					{
						if (theApp.CH340)
						{
							// 启动读串口线程
							new ReadSpThread().start();
						}
						else
						{
							// 检测串口
							if (theApp.getSP() != null)
							{
								// 启动读串口线程
								new ReadSpThread().start();

								// 已启动    xwx2014-10-11
	//							started();
						//dyp请求能否运行,能否登录,能否显示二维码等然后写进sp
								if (theApp.USE_TBSEVER) loadInternet(0);

							}
						}
					}
					catch (Exception e)
					{
					}
				}
			}
/********xwx**********/
			setLang();
		/*	if (!theApp.isInit())
			{
				// 设置语言
				new Thread()
				{
					public void run()
					{
						try
						{
							// 延迟 1500 毫秒
							Thread.sleep(2000);
							setLang();
						}
						catch (Exception e)
						{
						}
					}
				}.start();
			}
				*/
			if (theApp.USE_ZIP)
			{
				//获取存储数量
				theApp.setStoragePicNum(theApp.PIC_NUM);
			}
			// 界面语言
/*			String strLang = Locale.getDefault().getLanguage();
			if (strLang.equals("zh")) strLang = "ch";
			if (theApp.DEBUG) strLang = "en";
			theApp.setLang(strLang);
//			Log.d(TAG, "Language: " + strLang);
*/
			// 语言
			// 英文, en
			// 中文, ch
			// 繁体中文, tw
			// 韩语, kr
			// 日语, jp
			// 俄语, ru
			// 德语, de
			// 法语, fr
			// 西班牙语, sp
			// 葡萄牙语, pt
			// 意大利语, it
			// 波兰语, pl
			// 其它语言 1, q1
			// 其它语言 2, q2
			// 其它语言 3, q3

			// 未初始化
			if (!theApp.isInit())
			{
				// 加载 00.jpg 底图
				new Thread() { public void run() { load00Jpg(); } }.start();

				//获取图片数量
				new Thread() { public void run() { loadPicNum(); } }.start();


			}
		}
		catch (Exception e)
		{
		}


	try {
		// JPG 视图
		JpgView jpgView = (JpgView) findViewById(R.id.jpgView);
		theApp.setJpgView(jpgView);

		Matrix matrix = new Matrix();
		theApp.setMatrix(matrix);

		//			 tbdata = new UserData();

		// OSD 视图
		OsdView1 osdView1 = (OsdView1) findViewById(R.id.osdView1);
		OsdView2 osdView2 = (OsdView2) findViewById(R.id.osdView2);
		OsdView3 osdView3 = (OsdView3) findViewById(R.id.osdView3);
		OsdView4 osdView4 = (OsdView4) findViewById(R.id.osdView4);
		theApp.setOsdView1(osdView1);
		theApp.setOsdView2(osdView2);
		theApp.setOsdView3(osdView3);
		theApp.setOsdView4(osdView4);

		VideoViewEx view;
		// 不使用 VITAMIO
		if (!theApp.VITAMIO) {
			// 视频视图
			/*	VideoViewEx videoView = (VideoViewEx)findViewById(R.id.videoViewMain);
//				videoView.setOnCompletionListener((OnCompletionListener) m_onCompletionListener_my);
				videoView.setLooping(true);
				theApp.setVideoViewMy(videoView);*/

		/*		view = new VideoViewEx(m_context);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                view.setLayoutParams(params);
                view.setLooping(true);
                theApp.setVideoViewMy(view);	*/
		}
		// 使用 VITAMIO
		else {
			// 视频视图
			VideoView videoView = (VideoView) findViewById(R.id.videoViewMain);
			videoView.setOnCompletionListener(m_onCompletionListener);
			videoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
			videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);

			//设置高质量
			//			videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
			theApp.setVideoView(videoView);
		}

		// 视频布局
		m_layVideoMain = (RelativeLayout) findViewById(R.id.layVideoMain);

		//			m_layVideoMain.addView(view);

		theApp.setVideoLayout(m_layVideoMain);

		// 设置 JPG 图片
		//			theApp.setJpg();

		// 绘制图片
		theApp.drawBmp();
	} catch (Exception e) {
	}

/*
		// 使用蓝牙
		if (theApp.isUseBt())
		{
			try
			{
				// 设备连接中断
				String strDeviceLost = getString(R.string.strDeviceLost);
				BtSppService.setDeviceLost(strDeviceLost);

				// 无法连接到设备
				String strUnableConnect = getString(R.string.strUnableConnect);
				BtSppService.setUnableConnect(strUnableConnect);

				// 获取本地蓝牙适配器
				m_btAdapter = BluetoothAdapter.getDefaultAdapter();

				if (m_btAdapter == null)
				{
					// 不使用蓝牙
					theApp.setUseBt(false);

					// 不支持蓝牙。
//					Toast.makeText(this, R.string.strBtNotAvailable, Toast.LENGTH_SHORT).show();
				}

				// 启动蓝牙
				if (theApp.isUseBt()) startBt();
				Log.d(TAG, "startBt");
			}
			catch (Exception e)
			{
			}
		}

		// 未初始化
		if (!theApp.isInit())
		{
			// 不使用蓝牙
			if (!theApp.isUseBt())
			{
				try
				{
					// 设置串口
					String strErr = theApp.setSerialPort();
					if (strErr != null) Toast.makeText(this, strErr, Toast.LENGTH_LONG).show();
				}
				catch (Exception e)
				{
				}
			}
		}

		// 未初始化
		if (!theApp.isInit())
		{
			// 不使用蓝牙
			if (!theApp.isUseBt())
			{
				try
				{
					// 检测串口
					if (theApp.getSP() != null)
					{
						// 启动读串口线程
						new ReadSpThread().start();

						// 已启动
						started();
					}
				}
				catch (Exception e)
				{
				}
			}
		}
		*/
		try
		{
			// 设置透明度
//			theApp.getPaint().setAlpha(0xee);
//			theApp.getPaint().setAlpha(0xf7);
			theApp.getPaint().setAlpha(0xff);
		}
		catch (Exception e)
		{
		}

		try
		{
			// 开始时间
			m_lStartTime = System.nanoTime();

			// 94ms
//			System.gc();

			// 1ms
			System.runFinalization();

			// 98ms
//			Runtime.getRuntime().gc();

			// 174ms
//			System.gc();
//			System.runFinalization();
//			Runtime.getRuntime().gc();

			// 结束时间
			m_lEndTime = System.nanoTime();

			// 时间差
			double dt = Math.abs(m_lEndTime - m_lStartTime) / 10.0E5;
			Log.d(TAG, "DeltaTime: " + dt + "ms");
		}
		catch (Exception e)
		{
		}

		try
		{
			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				// 从布局文件中加载视图
				View viewMain = LayoutInflater.from(this).inflate(R.layout.activity_main_my, null);
				// 禁用长按
				viewMain.setLongClickable(false);
			}
			else
			{
				// 从布局文件中加载视图
				View viewMain = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
				// 禁用长按
				viewMain.setLongClickable(false);
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
		if (!theApp.isInit())
		{
			new Thread()
			{
				public void run()
				{
					try
					{
						Thread.sleep(1 * 500);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// 已启动   xwx 2014-10-11
					started();
					if (theApp.USE_ZIP)
					{
						//获取存储数量
//						theApp.setStoragePicNum(theApp.PIC_NUM);

						//开始比较图片数量
						theApp.setCheckNume(true);
					}
				}
			}.start();
		}

	}

	//获取网络时间
	TimerTask mTask = new TimerTask() {
		@Override
		public void run() {
			gg();
		}
	};
	public void gg() {
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				//当前时间
				try {
					URL           url = new URL("http://www.ntsc.ac.cn");
					URLConnection uc  = url.openConnection();
					uc.setConnectTimeout(10000);
					uc.connect();
					long    id     = uc.getDate();
					Date    dd     = new Date(id);
					Long    LL     = dd.getTime();
					Message msg    = Message.obtain();
					Bundle  bundle = new Bundle();
					bundle.putLong("time", LL);
					msg.setData(bundle);//bundle传值，耗时，效率低
					mHandler.sendMessage(msg);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();*/

		SimpleDateFormat formatter =   new    SimpleDateFormat    ("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		Date         curDate   =   new    Date(System.currentTimeMillis());//获取当前时间
		String       time       =    formatter.format(curDate);
		Message msg    = Message.obtain();
		msg.what = 1111;
		Bundle  bundle = new Bundle();
		bundle.putString("time", time);
		if (theApp.getJpgPathNum()==00){

		}

		msg.setData(bundle);//bundle传值，耗时，效率低
		mHandler.sendMessage(msg);
	}
	/**************************************************************/
	@Override
	public synchronized void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");

		if (theApp.CH340)
		{
			if(2 == uartInterface_340.ResumeUsbList())
			{
				uartInterface_340.CloseDevice();
				Log.d(TAG, "Enter onResume Error");
			}
		}
		// 前台
		theApp.setBackground(0);

		//清空视频路径
		theApp.setVideoPath("");

		// 已初始化
		if (theApp.isInit())
		{
			// 应答 E0
			if (theApp.isE0()) theApp.responseE0();
		}

		// 设置语言 xwx
		if (theApp.isInit())
		setLang();

		// 初始化缓冲区
		theApp.initBuffer();

		try
		{
			// 检测蓝牙串口服务
			if (theApp.getBtSpp() != null)
			{
				// 检测状态
				if (theApp.getBtSpp().getState() == BtSppService.STATE_NONE)
				{
					// 启动蓝牙串口服务
					theApp.getBtSpp().start();
				}
			}
		}
		catch (Exception e)
		{

		}
		try
		{
			//判断隐藏软键盘是否弹出
			if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
			{
				//隐藏软键盘
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			}
		}
		catch (Exception e)
		{

		}
		try
		{
			// 不使用 VITAMIO
		/*	if (!theApp.VITAMIO)
			{
				// 设置内容视图
				setContentView(R.layout.activity_main_my);
			}
			// 使用 VITAMIO
			else
			{
				// 设置内容视图
				setContentView(R.layout.activity_main);
			}*/

			// JPG 视图
			JpgView jpgView = (JpgView)findViewById(R.id.jpgView);
			theApp.setJpgView(jpgView);

			// OSD 视图
			OsdView1 osdView1 = (OsdView1)findViewById(R.id.osdView1);
			OsdView2 osdView2 = (OsdView2)findViewById(R.id.osdView2);
			OsdView3 osdView3 = (OsdView3)findViewById(R.id.osdView3);
			OsdView4 osdView4 = (OsdView4)findViewById(R.id.osdView4);
			theApp.setOsdView1(osdView1);
			theApp.setOsdView2(osdView2);
			theApp.setOsdView3(osdView3);
			theApp.setOsdView4(osdView4);

		/*	VideoViewEx view;

			// 不使用 VITAMIO
			if (!theApp.VITAMIO)
			{
				view = new VideoViewEx(m_context);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                view.setLayoutParams(params);
                view.setLooping(true);
                theApp.setVideoViewMy(view);

			}
			// 使用 VITAMIO
			else
			{
				// 视频视图
				VideoView videoView = (VideoView)findViewById(R.id.videoViewMain);
				videoView.setOnCompletionListener(m_onCompletionListener);
				videoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
				videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);

				//设置高质量
	//			videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
				theApp.setVideoView(videoView);
			}

			// 视频布局
			m_layVideoMain = (RelativeLayout)findViewById(R.id.layVideoMain);

			m_layVideoMain.addView(view);

			theApp.setVideoLayout(m_layVideoMain);*/

			if (!theApp.getDaInitstatus()) initDaVideo();

//			m_layVideoMain.setVisibility(View.VISIBLE);

			// 设置 JPG 图片
			theApp.setJpg();

			//	停止DA电影
//			pause();

			// 停止视频
			theApp.stopVideo();
		}
		catch (Exception e)
		{
		}
		/**************************************************************/
		try
		{
			// 已初始化
			if (theApp.isInit())
			{
				new Thread()
				{
					public void run()
					{
						try
						{
							// 延迟 200 毫秒
							Log.d(TAG, "Time: " + System.nanoTime());
							Thread.sleep(200);
						}
						catch (Exception e)
						{
						}

						// 恢复
						Log.d(TAG, "Time: " + System.nanoTime());
						Log.d(TAG, "MSG_RESUME");
						m_handler.sendEmptyMessage(MSG_RESUME);
					}
				}.start();

				if(!theApp.USE_ZIP)
				{
					//获取图片数量
					if(theApp.isSetLan())
					{

						theApp.SetLan(false);

						new Thread() { public void run() { loadPicNum(); } }.start();
					}
				}

			}
		}
		catch (Exception e)
		{
		}
		/**************************************************************/
		try
		{
			// 已初始化
			if (theApp.isInit())
			{
				new Thread()
				{
					public void run()
					{
						try
						{
							// 延迟 100 毫秒
							Log.d(TAG, "MSG_REDRAW - sleep");
							Thread.sleep(100);
						}
						catch (Exception e)
						{
						}

						// 重绘
						m_handler.sendEmptyMessage(MSG_REDRAW);
					}
				}.start();
			}
		}
		catch (Exception e)
		{
		}

		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 已加载 Apps
			if (theApp.isLoadApps())
			{
				// 使用视图页指示器
				if (theApp.USE_VPI)
				{
					// 初始化 Apps
					if (m_layAppsVpi == null) initApps();
				}
				// 不使用视图页指示器
				else
				{
					// 初始化 Apps
					if (m_layAppsGrid == null) initApps();
				}
			}

			// 已加载音乐
			if (theApp.isLoadMusic())
			{
				// 初始化音乐
				if (m_layMusic == null) initMusic();
			}

			// 已加载视频
			if (theApp.isLoadVideo())
			{
				// 初始化视频
				if (m_layVideo == null) initVideo();
			}

			// 已加载图片
			if (theApp.isLoadPicture())
			{
				// 初始化图片
				if (m_layPictureGrid == null) initPicture();
//				if (m_layPictureList == null) initPicture();
			}

			// 已初始化浏览器
			if (theApp.isInitBrowser())
			{
				// 初始化浏览器
				if(!theApp.isClear())
				{
		//			if (m_layBrowser == null) initBrowser();
	//				if (m_layBrowserOld == null) initBrowser();
				}
			}
		}
		catch (Exception e)
		{
		}

		try
		{
			// 插入, 卸载或拔出 SD 卡接收器
			if (m_ejectSDCardReceiver != null) m_ejectSDCardReceiver.unregisterReceiver(this);
			m_ejectSDCardReceiver = new EjectSDCardReceiver();
			m_ejectSDCardReceiver.registerReceiver(this);
		}
		catch (Exception e)
		{
		}

		try
		{
			if (theApp.isStartFloat())
			{
				//清除悬浮窗osd
				theApp.clearAllosd(false,true,false);
				theApp.clearAllosd(false,false,true);

				// 关闭悬浮窗
				theApp.setStartFloat(false);

				// 关闭悬浮窗
				theApp.setFloatOn(false);
				theApp.setFloatTopOn(false);
				theApp.setFloatBottomOn(false);

				// 移除顶部, 底部和开关悬浮窗
				FloatManager.removeTBS(this);

				// 停止顶部, 底部和开关悬浮窗服务
				FloatManager.stopTBSService(this);
			}
		}
		catch (Exception e)
		{
		}

		if(theApp.isClear())
		{
			try
			{
				//清除标记
				theApp.setClear(false);

				// 清除布局
				clearLayout();

				//清除浏览器布局
//				clearLayoutBrowse();

				//清除视频布局
//				clearLayoutVideo();

				//	停止DA电影
//				pause();
			}
			catch (Exception e)
			{
			}
		}

		// 已初始化
		theApp.setInit(true);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");

		// 后台
		theApp.setBackground(1);

        //设置视频播放未初始化
        theApp.setDaInitstatus(false);

		try
		{
			// 注销接收器
			if (m_ejectSDCardReceiver != null) m_ejectSDCardReceiver.unregisterReceiver(this);
		}
		catch (Exception e)
		{
		}


		try
		{
			if (theApp.isStartApp())
			{

				//已清除浏览器
//				theApp.setClear(true);

				// 检测是否在前台运行
				if (!theApp.isRunningForeground(this))
				{
					Log.d(TAG, "!isRunningForeground");
					theApp.TLog(TAG, "!isRunningForeground");
					theApp.SLog(TAG, "!isRunningForeground");

					// 未启动悬浮窗
					if (!theApp.isStartFloat())
					{
						// 启动悬浮窗
						theApp.setStartFloat(true);
						Log.d(TAG, "StartFloat");
						theApp.TLog(TAG, "StartFloat");
						theApp.SLog(TAG, "StartFloat");

						// 启用悬浮窗单个开关
						if (theApp.FLOAT_SWITCH)
						{
							// 开启悬浮窗
							theApp.setFloatOn(true);

							// 启动开关悬浮窗
							Intent intentSwitch = new Intent(this, FloatSwitchService.class);
							startService(intentSwitch);
						}
						// 不启用悬浮窗单个开关
						else
						{
							// 开启顶部悬浮窗
							theApp.setFloatTopOn(true);

							// 开启底部悬浮窗
							theApp.setFloatBottomOn(true);

							// 启动顶部悬浮窗开关
							Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
							startService(intentTopSwitch);

							// 启动底部悬浮窗开关
							Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
							startService(intentBottomSwitch);
						}

						// 启动顶部悬浮窗
						Intent intentTop = new Intent(this, FloatTopService.class);
						startService(intentTop);

						// 启动底部悬浮窗
						Intent intentBottom = new Intent(this, FloatBottomService.class);
						startService(intentBottom);
					}
				}
			}
		}
		catch (Exception e)
		{
		}

		// 暂停
		pause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			// 返回键
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				// 退出
				if (theApp.DEBUG)	exit();
		        Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
		        intent.addCategory(Intent.CATEGORY_HOME);
		        this.startActivity(intent);
		        theApp.clearOsd3(0,0,1920,1080);
//				exit();
				return true;
			}

			// 音量减小
			if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
			{
				// 媒体音量
				int nSV = theApp.getStreamVolume() - 1;
				theApp.setStreamVolume(this, nSV);
				theApp.setStreamVolume(nSV);

				return super.onKeyDown(keyCode, event);
			}

			// 音量增大
			if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
			{
				// 媒体音量
				int nSV = theApp.getStreamVolume() + 1;
				theApp.setStreamVolume(this, nSV);
				theApp.setStreamVolume(nSV);

				return super.onKeyDown(keyCode, event);
			}
		}
		catch (Exception e)
		{
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//if(!PushReceiver.isOpen){return false; }//dyp由扫码返回值获得isOpen的值,
		//if (!theApp.getLogin()){return false;}//不允许登录的时候让touch事件
		boolean nSend = true;
		//获取服务器端状态
		if (theApp.QRLOGIN )//是否使用扫码登录,默认false,使用扫码
		{
			if (theApp.QRLOCK)//是否开启进入wifi
			{
				if (!theApp.getRunstatus()) nSend = false; //||!theApp.getLogin()
			}
			else
			{
				if (!theApp.getRunstatus()) nSend = false;
			}

		}
		else//dyp不使用扫码登录
		{
			if (!theApp.getRunstatus()) nSend = false;
		}
		try
		{
			float x = 0;
			float y = 0;
			int nAction = 0;

			try
			{
				// 在视图中的坐标
				x = event.getX();
				y = event.getY();
//				Log.d(TAG, String.format("Touch - X: %d, Y: %d", (int)x, (int)y));
				/*if(theApp.getJpgPathNum()==01&&!PushReceiver.isOpen){
					if (x>100|y>100){
						return false;
					}
				}*/
				// 根据比例计算坐标
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();
				if (fWR <= 0) fWR = 1;
				if (fHR <= 0) fHR = 1;
				x /= fWR;
				y /= fHR;

				// 触摸动作
				nAction = event.getAction() & MotionEvent.ACTION_MASK;
			}
			catch (Exception e)
			{
			}

			switch (nAction)
			{
			// 主点按下
			// 辅点按下
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:

				// 按下的坐标
				theApp.setDownX(x);
				theApp.setDownY(y);

				// 应答 E2
//				Log.d(TAG, String.format("Touch ACTION_DOWN - X: %d, Y: %d", (int)x, (int)y));

//				if (theApp.USE_TBSEVER) uploadTBData(tbdata);

				if (nSend) theApp.responseE2((int)x, (int)y);
				break;

			// 主点和辅点移动
			case MotionEvent.ACTION_MOVE:

				// 正在移动
				theApp.setMoving(true);

				// 响应移动
				if (theApp.isResponseMove())
				{
					// 按下的坐标
					float dwx = theApp.getDownX();
					float dwy = theApp.getDownY();

					// 坐标差值
					float dx = Math.abs(dwx - x);
					float dy = Math.abs(dwy - y);

					// 检查差值
					if (dx > theApp.MOVE_DELTA || dy > theApp.MOVE_DELTA)
					{
						// 移动时间
						theApp.setMoveTime(0);

						// 不应答移动
						theApp.setResponseMove(false);

						// 应答 E2
//						Log.d(TAG, String.format("Touch ACTION_MOVE - X: %d, Y: %d", (int)x, (int)y));
						if (nSend) theApp.responseE2((int)x, (int)y);
					}
				}
				break;

			// 主点抬起
			// 辅点抬起
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:

				Log.i(TAG, "onTouchEvent: x="+event.getX()+"y="+event.getY());
				// 停止移动
				theApp.setMoving(false);
				//dyp,说明点击了"竞赛",进入竞赛模式
				if(theApp.getJpgPathNum()==02&&x>150&&x<650&&y>237&&y<625){
					Intent intent = new Intent(MainActivity.this,CompetitionActivity.class);
					startActivity(intent);
					//enterCompetitionＭode();
				}
				//进入场景模式
				if(theApp.getJpgPathNum()==02&&x>662&&x<909&&y>437&&y<622){
					theApp.setThirdAPP("场景模式");
					Intent intent = new Intent(MainActivity.this,SenceActivity.class);
					startActivity(intent);
					//enterCompetitionＭode();
				}

				if (theApp.WIFI_DETEC)
				{
					//wifi 开关
					//大胡子 if (x>134&&x<286&&y>663&&y<720)
					//天展  if (x>5&&x<100&&y>5&&y<100)
					//舒华  if (x>1180&&x<1280&&y>5&&y<100)
					if (theApp.CUSTOMER_DHZ)
					{
						if (x>134&&x<286&&y>663&&y<720)
						{
							try
							{
								if (theApp.getJpgPathNum() == 1 && theApp.QRLOCK ) openWifiSetup();
								else openWifi();
							}
							catch (Exception e)
							{
							}
//							break;
						}
					}
					if (theApp.CUSTOMER_TZ)
					{
						if (x>0&&x<100&&y>0&&y<100)
						{
							try
							{
								if (theApp.getJpgPathNum() == 1 && theApp.QRLOCK ) {
									final EditText ett = new EditText(m_context);
									final AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
									builder.setTitle("请输入密码").setView(ett);
									builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											final String textPassword = ett.getText().toString();
											if (textPassword.equals("")) {
												Toast toast = Toast.makeText(getApplicationContext(), "密码不能为空,请重新输入", Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
												InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
											} else if(textPassword.equals("9876")){
												openWifiSetup();
												dialog.dismiss();
											}else {
												Toast toast = Toast.makeText(getApplicationContext(), "密码不正确,请重新输入", Toast.LENGTH_SHORT);
												toast.setGravity(Gravity.CENTER, 0, 0);
												toast.show();
												InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
											}

										}
									});
									builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
											InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
											imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
										}
									});
										InputMethodManager imm = (InputMethodManager)m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(ett.getWindowToken(),0);
									builder.show();
									}else{openWifi();}
							}
							catch (Exception e)
							{
							}
//							break;
						}
					}
					if (theApp.CUSTOMER_SH)
					{
						if (x>1180&&x<1280&&y>0&&y<100)
						{
							try
							{
								if (theApp.getJpgPathNum() == 1 && theApp.QRLOCK ) openWifiSetup();
								else openWifiSetup();
							}
							catch (Exception e)
							{
							}
						//	break;
						}
					}
				}
				// 应答 E3
//				Log.d(TAG, String.format("Touch ACTION_UP - X: %d, Y: %d", (int)x, (int)y));
				if (nSend) theApp.responseE3((int)x, (int)y);
				break;

			// 动作取消
			case MotionEvent.ACTION_CANCEL:

				// 停止移动
				theApp.setMoving(false);

				// 应答 E3
//				Log.d(TAG, String.format("Touch ACTION_CANCEL - X: %d, Y: %d", (int)x, (int)y));
				if (nSend) theApp.responseE3((int)x, (int)y);
				break;

			// 超出范围
			case MotionEvent.ACTION_OUTSIDE:

				// 停止移动
				theApp.setMoving(false);

				// 应答 E3
//				Log.d(TAG, String.format("Touch ACTION_OUTSIDE - X: %d, Y: %d", (int)x, (int)y));
				if (nSend) theApp.responseE3((int)x, (int)y);
				break;

			// 默认
			default:

				// 停止移动
				theApp.setMoving(false);

				// 应答 E3
//				Log.d(TAG, String.format("Touch default - X: %d, Y: %d", (int)x, (int)y));
				if (nSend) theApp.responseE3((int)x, (int)y);
				break;
			}

			return true;
		}
		catch (Exception e)
		{
		}

		return true;

		// 在视图中的坐标
/*		float x = event.getX();
		float y = event.getY();

//		Log.d(TAG, String.format("Touch - X: %d, Y: %d", (int)x, (int)y));

		// 根据比例计算坐标
		float fWR = theApp.getWidthRatio();
		float fHR = theApp.getHeightRatio();
		if (fWR <= 0) fWR = 1;
		if (fHR <= 0) fHR = 1;
		x /= fWR;
		y /= fHR;

		// 点击屏幕
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
//			Log.d(TAG, String.format("Touch ACTION_DOWN - X: %d, Y: %d", (int)x, (int)y));

			// 应答 E2
			theApp.responseE2((int)x, (int)y);
		}

		// 离开屏幕
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
//			Log.d(TAG, String.format("Touch ACTION_UP - X: %d, Y: %d", (int)x, (int)y));

			// 停止移动
			ms_bMoving = false;

			// 应答 E3
			theApp.responseE3((int)x, (int)y);
		}

		// 移动
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			// 正在移动
			ms_bMoving = true;

			// 响应移动
			if (ms_bResponseMove)
			{
//				Log.d(TAG, String.format("Touch ACTION_MOVE - X: %d, Y: %d", (int)x, (int)y));

				// 移动时间
				ms_nMoveTime = 0;

				// 不应答移动
				ms_bResponseMove = false;

				// 应答 E2
				theApp.responseE2((int)x, (int)y);
			}
		}

		// 此处必须返回 true, 否则无法完全响应所有的 MotionEvent
		return true;*/
//		return super.onTouchEvent(event);
	}

	public void enterCompetitionＭode() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String aaUrl = "http://dev.vrun.sh.cn/vrun/matchCtrl";
				String urlroom = aaUrl+"/getRoomList";
				Log.d(TAG, "dddddddddd url=:"+urlroom);
				OkhttpTool.doget(urlroom, new MyCallback(new StringPaser()){
					@Override
					public void onResponse(Response response)
							throws IOException
					{
						Log.d(TAG, "dddddddddd+onResponse");
						String responStr = response.body().string();
						//super.onResponse(response);
						String     sceneName  = "";		//场景名
						String     nickName   = "";	//昵称
						String     headImg    = "";			//头像
						String     roomNum    = "";	//房间号
						String     password   = "";		//密码
						try {
							JSONObject jsonObject = new JSONObject(responStr);
							//房间信息

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Request request, IOException e) {
						Log.d(TAG, "获取房间数量失败");
						super.onFailure(request, e);
					}
				});
			}
		}).start();

			}

	/**
	 * 视频播放完成监听器
	 */
	private MediaPlayer.OnCompletionListener m_onCompletionListener_my = new MediaPlayer.OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mp)
		{
//			Log.d(TAG, "OnCompletionListener");

			// 停止视频
//			stopVideo();
		}
	};

	/**
	 * 视频播放完成监听器
	 */
	private io.vov.vitamio.MediaPlayer.OnCompletionListener m_onCompletionListener = new io.vov.vitamio.MediaPlayer.OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mp)
		{
//			Log.d(TAG, "OnCompletionListener");

			// 停止视频
//			stopVideo();

			// 视频当前位置
			theApp.setVideoCurPos(0);

			// 播放视频
			theApp.playVideo(true, false);
		}
	};

	/**
	 * 接收数据事件
	 * @param nBytes
	 * @param buffer
	 */
	protected void onDataReceived(final int nBytes, final byte[] buffer)
	{
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				if (nBytes > 0)
				{
					// 读缓冲区
					theApp.readBuffer(nBytes, buffer);

					// 调试
					if (theApp.DEBUG)
					{
						try
						{
							byte[] readBuf = new byte[nBytes];

							for (int i = 0; i < nBytes; i++)
							{
								// 缓冲区
								readBuf[i] = buffer[i];
							}

							// 构造字符串
							String strRead = new String(readBuf, 0, nBytes);

							if (strRead.equals("exit"))
							{
								// 退出
								exit();
							}

/*							final int[] EXIT = { 69, 88, 73, 84 };

							if (strRead.toUpperCase().equals("EXIT") ||
								(readBuf[0] == EXIT[0] &&
								 readBuf[1] == EXIT[1] &&
								 readBuf[2] == EXIT[2] &&
								 readBuf[3] == EXIT[3]))
							{
								// 退出
								exit();
							}*/
						}
						catch (Exception e)
						{
						}
					}
				}
			}
		});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		try
		{
			if (m_layWiFi == null) return super.onContextItemSelected(item);

			switch (item.getItemId())
			{
			// 连接
			case WiFiLayout.ITEM_CONNECT:

				// 检测选择的 WiFi 配置
				if (m_layWiFi.getWcSelect() != null)
				{
					// 连接到 WiFi
					boolean bConnect = m_layWiFi.connectTo(m_layWiFi.getWcSelect());
					Log.d(TAG, "ConnectTo: " + bConnect);

					// 连接成功
					if (bConnect)
					{
						// 重新开始扫描 WiFi
						m_layWiFi.restartScanWiFi();
					}
					// 连接不成功
					else
					{
						// 休眠
						m_layWiFi.sleep(WiFiLayout.WIFI_SCAN_WAITING_TIME);

						// SSID
						m_layWiFi.setSSID(m_layWiFi.getWcSelect().SSID);
						if (m_layWiFi.getSSID() == null) return super.onContextItemSelected(item);

						// 检测是否已存在指定的 SSID
						WifiConfiguration tempWC = m_layWiFi.isExsits(m_layWiFi.getSSID());

						if (tempWC != null)
						{
							// 移除已存在的网络
							m_layWiFi.getWiFiMgr().removeNetwork(tempWC.networkId);
						}

						// 获取扫描结果
						if (m_layWiFi.getListResults() == null) return super.onContextItemSelected(item);
						ScanResult result = m_layWiFi.checkListResult(m_layWiFi.getListResults(), m_layWiFi.getSSID());
						if (result == null) return super.onContextItemSelected(item);

						// 获取加密类型
						int nType = m_layWiFi.getEncryption(result);

						if (nType == WiFiLayout.NO_PASSWORD)
						{
							return super.onContextItemSelected(item);
						}

						// 显示输入密码对话框
						m_layWiFi.showDialog(WiFiLayout.DIALOG_PASSWORD);
					}
				}
				break;

			// 断开
			case WiFiLayout.ITEM_DISCONNECT:

				// 检测选择的 WiFi 配置
				if (m_layWiFi.getWcSelect() != null)
				{
					// 断开连接
					if (m_layWiFi.disconnect(m_layWiFi.getWcSelect()))
					{
						// 休眠
						m_layWiFi.sleep(WiFiLayout.WIFI_SCAN_WAITING_TIME);

						// 重新开始扫描 WiFi
						m_layWiFi.restartScanWiFi();
					}
				}
				break;

			// 忘记网络
			case WiFiLayout.ITEM_FORGET:

				// 检测选择的 WiFi 配置
				if (m_layWiFi.getWcSelect() != null)
				{
					// 检测 WiFi 管理器
					if (m_layWiFi.getWiFiMgr() != null)
					{
						// 移除已存在的网络
						boolean bRemove = m_layWiFi.getWiFiMgr().removeNetwork(m_layWiFi.getWcSelect().networkId);

						if (bRemove)
						{
							// 休眠
							m_layWiFi.sleep(WiFiLayout.WIFI_SCAN_WAITING_TIME);

							// 重新开始扫描 WiFi
							m_layWiFi.restartScanWiFi();
						}
					}
				}
				break;

			// 取消
			case WiFiLayout.ITEM_CANCEL:
				break;
		    }
		}
		catch (Exception e)
		{
		}

		return super.onContextItemSelected(item);
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * 退出
	 */
	private void exit()
	{
		try
		{
			// 移除所有悬浮窗
			FloatManager.removeAll(this);

			// 停止所有悬浮窗服务
			FloatManager.stopAllService(this);
		}
		catch (Exception e)
		{
		}

		try
		{
			// 注销接收器
			if (m_ejectSDCardReceiver != null) m_ejectSDCardReceiver.unregisterReceiver(this);
		}
		catch (Exception e)
		{
		}

		try
		{
			// 清除通知
			if (theApp.getNotifyMgr() != null) theApp.getNotifyMgr().cancel(R.drawable.ic_launcher);
		}
		catch (Exception e)
		{
		}

		try
		{
			// 退出
			theApp.setExit(true);
			PictureGridLayout.setExit(true);
			PictureListLayout.setExit(true);
//			Log.d(TAG, "Exit");

			// 写共享属性
			theApp.writePref();

			// 停止音乐
			theApp.stopMusic();

			// 停止视频
			theApp.stopVideo();

			// 使用蓝牙
			if (theApp.isUseBt())
			{
				// 停止蓝牙串口服务
				if (theApp.getBtSpp() != null) theApp.getBtSpp().stop();

				// 蓝牙不可用
				if (!theApp.isBtEnabled())
				{
					// 关闭蓝牙
					if (m_btAdapter != null) m_btAdapter.disable();
				}
			}
			// 使用串口
			else
			{
				// 关闭串口
				if (theApp.getSP() != null) theApp.getSP().close();
			}

			try
			{
				// 释放声音
				if (theApp.isLoadSound()) theApp.getSoundPool().release();
			}
			catch (Exception e)
			{
				Log.d(TAG, "theApp.getSoundPool().release() :%s", e);
			}

			// 结束
			finish();

			// 垃圾回收
			System.gc();
			System.runFinalization();
			Runtime.getRuntime().gc();
		}
		catch (Exception e)
		{
		}
		finally
		{
			// 退出
			Log.d(TAG, "System.exit(0)");
			System.exit(0);
		}
	}

	/**
	 * 恢复
	 */
	private void resume()
	{
	}

	/**
	 * 重绘
	 */
	private void redraw()
	{
		// 绘制图片
		Log.d(TAG, "MSG_REDRAW - theApp.drawBmp()");
		theApp.drawBmp();
	}

	/**
	 * 暂停
	 */
	private void pause()
	{
		try
		{
			// 没有播放
			if (!theApp.isPlayingVideo())
			{
				// 视频当前位置
				theApp.setVideoCurPos(0);
				Log.d(TAG, "Video Position: " + theApp.getVideoCurPos());
			}
			// 正在播放
			else
			{
				// 视频当前位置
//				int nVideoCurPos = theApp.getCurrentPosition();

				// 视频路径
				theApp.setVideoPath(null);

				// 停止视频
				theApp.stopVideo();

				// 视频当前位置
				theApp.setVideoCurPos(0);
//				theApp.setVideoCurPos(nVideoCurPos);
//				Log.d(TAG, "Video Position: " + theApp.getVideoCurPos());
			}

//			theApp.setVideoViewMy(null);
		}
		catch (Exception e)
		{
		}

		// 应答 E0
		theApp.responseE0();

		// 结束
//		finish();
	}

	/**
	 * 启动蓝牙
	 */
	private void startBt()
	{
		try
		{
			// 检测蓝牙适配器
			if (m_btAdapter != null)
			{
				// 检测蓝牙适配器是否可用
				if (!m_btAdapter.isEnabled())
				{
					// 蓝牙不可用
					theApp.setBtEnabled(false);

					try
					{
						// 打开蓝牙
						if (m_btAdapter.enable())
						{
							// 延迟 3 秒
							Thread.sleep(3 * 1000);

							// 设置蓝牙串
							theApp.setupBtSpp(this, m_handler);
						}
						else
						{
							// 无法启用蓝牙。
							Toast.makeText(this, R.string.strBtNotEnabled, Toast.LENGTH_SHORT).show();
						}
					}
					catch (Exception e)
					{
					}
				}
				else
				{
					// 蓝牙可用
					theApp.setBtEnabled(true);

					// 检测蓝牙串口服务
					if (theApp.getBtSpp() == null)
					{
						// 设置蓝牙串口
						theApp.setupBtSpp(this, m_handler);

						// 自动连接
						autoConnect();
					}
					else
					{
						// 设置蓝牙串口处理器
						theApp.getBtSpp().setHandler(m_handler);
					}
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 已启动
	 */
	private void started()
	{
		// 应答 E0
		theApp.responseE0();

		//可以回e0
		theApp.setE0(true);
	}

	/**
	 * 设置语言
	 */
	private void setLang()
	{
		try
		{
			// 界面语言
			String strLang = Locale.getDefault().getLanguage();
			if (strLang.equals("zh")) strLang = "ch";
//			if (strLang.equals("es")) strLang = "";
			if (theApp.DEBUG) strLang = "en";
			// xwx
//			Toast.makeText(this, strLang, Toast.LENGTH_SHORT).show();
			theApp.setLang(strLang);

	//		theApp.MYSLog(TAG, strLang);

			// 路径
			String strPath = theApp.getDefPath() + "picture/" + strLang + "/";

	     /*   Toast toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
	        toast.getView().getBackground().setAlpha(0);//设置透明度
	        toast.show();*/

			//
//			Toast.makeText(this, strPath, Toast.LENGTH_LONG).show();

			// 文件
			File file = new File(strPath);

	//		theApp.MYSLog(TAG, strPath);
			Log.d(TAG, "path=  " + strPath);
			Log.d(TAG, "file.exists:  " + file.exists() + "  file.isDirectory:  "+ file.isDirectory());
			Log.d(TAG, "file:  " + file.getAbsolutePath());

			// 检测文件
			if (!file.exists() || !file.isDirectory())
			{
//				Toast.makeText(this, "no file", Toast.LENGTH_LONG).show();
				// 英文
				strLang = "en";
				theApp.setLang(strLang);
	//			theApp.MYSLog(TAG, "exit");
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 自动连接
	 */
	private void autoConnect()
	{
		try
		{
			// 蓝牙地址
			String strAddress = theApp.getBtAdd();

			// 获取本地蓝牙适配器
			BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

			// 检测蓝牙适配器
			if (btAdapter != null)
			{
				// 检测蓝牙适配器是否可用
				if (btAdapter.isEnabled())
				{
					// 获取远程蓝牙设备
					BluetoothDevice btDevice = btAdapter.getRemoteDevice(strAddress);

					// 尝试连接设备
					Log.d(TAG, "connect");
					if (theApp.getBtSpp() != null) theApp.getBtSpp().connect(btDevice);
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取图片路径
	 * @param strPath
	 * @return
	 */
/*	private String getBmpPath(final String strPath)
	{
		// jpg
		String strJpgPath = strPath + ".jpg";

		// 文件
		File fileJpg = new File(strJpgPath);

		// 检测文件
		if (fileJpg.exists() && fileJpg.isFile())
		{
			return strJpgPath;
		}

		// png
		String strPngPath = strPath + ".png";

		// 文件
		File filePng = new File(strPngPath);

		// 检测文件
		if (filePng.exists() && filePng.isFile())
		{
			return strPngPath;
		}

		// 调试
		if (theApp.DEBUG)
		{
			// 图片错误
			String strErr = "";
			strErr += "Picture error: " + strJpgPath + "\r\n";
			strErr += "Picture error: " + strPngPath;
//			Toast.makeText(this, strErr, Toast.LENGTH_SHORT).show();
		}

		Log.d(TAG, "Picture error: " + strJpgPath);
		Log.d(TAG, "Picture error: " + strPngPath);
		return null;
	}
*/
	//----------------------------------------------------------------------------------------------------

	/**
	 * 获取 SDCard 路径
	 * @return
	 */
	private String getSDCard()
	{
		String str = "/mnt/internal_sd";

    	try
		{
    		// 检测扩展存储是否可用
    		if (!isExteranlStorageAvailable())
    		{
    			return str;
    		}
    		else
    		{
    			// 获取 SD 卡的路径
    			str = Environment.getExternalStorageDirectory().getPath();
    		}
		}
		catch (Exception e)
		{
			return "/mnt/internal_sd";
		}

		return str;
	}

	/**
	 * 生成多级路径
	 * @param strFileName
	 */
	private void mkdirs(final String strFileName)
	{
		try
		{
			// 文件
			File file = new File(strFileName);

			// 检测文件
			if (!file.exists() || !file.isDirectory())
			{
				// 生成多级路径
				// java.io.File.mkdir() 只能生成单级路径
				boolean b = file.mkdirs();
				Log.d(TAG, "" + b);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取包名目录
	 * @return
	 */
	private String getPkgDir()
	{
		try
		{
			// 分隔符
			String strS = File.separator;

			// 获取 SDCard 路径
			String strSD = "/mnt/internal_sd";//getSDCard();

			// 获取包名
			String strPkg = getPackageName();

			// 目录
			String strDir = strSD + strS + "Android" + strS + "data" + strS + strPkg + strS;
			mkdirs(strDir);

			return strDir;
		}
		catch (Exception e)
		{
		}

		return "";
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgDlg()
	{
		try
		{
//			nWifiFlag = false;
			// 请等待...
			String strWait = "Please wait...";
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWait);

			// 拷贝
			m_handler.sendEmptyMessage(MSG_COPY);
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 拷贝文件
	 * @param input 输入流
	 * @param file 目标文件
	 * @param bReWrite 是否重写
	 * @return
	 */
	private String copyFile(InputStream input, File file, final boolean bReWrite)
	{
		String str = null;

		if (file == null)
		{
			str = "The target file is null.";
			return str;
		}

		try
		{
			if (!file.getParentFile().exists())
			{
				file.getParentFile().mkdirs();
			}
		}
		catch (Exception ex)
		{
			return file.getName() +  " make directory error.";
		}

		try
		{
			if (file.exists() && bReWrite)
			{
				file.delete();
			}
		}
		catch (Exception ex)
		{
			return file.getName() +  " delete file error.";
		}

		if (file.exists() && !file.canWrite())
		{
			str = file.getName() + " could not be write.";
			return str;
		}

		OutputStream output = null;

		try
		{
			output = new FileOutputStream(file);

			int nRead = 0;
			byte[] bytes = new byte[1024];

			while ((nRead = input.read(bytes)) > 0)
			{
				output.write(bytes, 0, nRead);
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
				if (input != null) input.close();
				if (output != null) output.close();
			}
			catch (Exception ex)
			{
				return "Copy file error.";
			}
		}

		return str;
	}

	/**
	 * 拷贝 Zip 文件
	 */
	private void copyZipFile()
	{
		try
		{
			// 文件
//			String strFileName = getPkgDir() + "TB-TECH-HMI";
			String strFileName = theApp.getDefPath();
			File file = new File(strFileName);

			// 检测文件夹
			if (file.exists() && file.isDirectory())
			{
				// 解压完成
				m_handler.sendEmptyMessage(MSG_UNZIP_OK);
				return;
			}
			//开始拷贝图片
			theApp.setCopyPicture(true);

			// 拷贝 Zip 文件
			copyZipFile("TB-TECH-HMI.zip");
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 拷贝 Zip 文件
	 * @param strName 文件名
	 */
	private void copyZipFile(final String strName)
	{
		try
		{
			if (strName == null || strName.length() < 1) return;

			// 线程
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						// 拷贝 Zip 文件
						copyZipFile_(strName);
					}
					catch (Exception e)
					{
					}
					finally
					{
						// 拷贝完成
						m_handler.sendEmptyMessage(MSG_COPY_OK);
					}
				}
			}.start(); // 启动线程
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 拷贝 Zip 文件
	 * @param strName 文件名
	 */
	private void copyZipFile_(final String strName)
	{
		try
		{
			InputStream input = null;

			if (strName == null || strName.length() < 1) return;

			try
			{
				// 资源管理器
				AssetManager am = getAssets();

				// 输入流
				input = am.open(strName);

				// 文件
				String strFileName = getPkgDir() + strName;
				File file = new File(strFileName);

				// 拷贝文件
				copyFile(input, file, true);
			}
			catch (Exception e)
			{
			}
			finally
			{
				try
				{
					// 关闭输入流
					if (input != null) input.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 解压 Zip 文件
	 */
	private void unzip()
	{
		try
		{
			// 线程
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						// 目标目录
//						String strDestDir = getPkgDir() + "TB-TECH-HMI";
						String strDestDir = theApp.getDefPath();

						// Zip 文件
						String strZipFileName = getPkgDir() + "TB-TECH-HMI.zip";
//						String strZipFileName = theApp.getDefPath() + "TB-TECH-HMI.zip";
						// 解压 Zip 文件
						unzip(strZipFileName, strDestDir);

					}
					catch (Exception e)
					{
					}
					finally
					{
						//发送解压完成命令
//						theApp.responseKey(0, 0, 77);

						//解压完成
//						theApp.setCopyPicture(false);

						// 解压完成
						m_handler.sendEmptyMessage(MSG_UNZIP_OK);
					}
				}
			}.start(); // 启动线程
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 删除 TB-TECH-HMI文件夹
	 */
	@SuppressWarnings("unused")
	private void deleteTB()
	{
		try
		{
			// 线程
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						String strDestDir = theApp.getDefPath();

						File file = new File(strDestDir);
						if (file.exists() && file.isDirectory())
						{
							mydelete(file);
						}
//						showProgDlg();
					}
					catch (Exception e)
					{
					}
					finally
					{
						try
						{
							// 休眠 500 毫秒
//							Thread.sleep(10000);
							// 拷贝
//							m_handler.sendEmptyMessage(MSG_RECOPY);

						}
						catch(Exception e)
						{
						}
					}
				}
			}.start(); // 启动线程
		}
		catch(Exception e)
		{
		}
	}
	/**
	 * 删除 文件和文件夹
	 */
	@SuppressWarnings("unused")
	private void mydelete(File file)
	{
		try
		{
			if (file.isFile() && file.exists())
			{
				file.delete();
				return;
	        }
			if(file.isDirectory())
			{
				File[] childFiles = file.listFiles();
				if (childFiles == null || childFiles.length == 0)
				{
					file.delete();
					return;
				}
				for (int i = 0; i < childFiles.length; i++)
				{
					mydelete(childFiles[i]);
				}
				file.delete();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "发生异常，删除文件失败！", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 解压 Zip 文件
	 * @param strZipFileName Zip 文件
	 * @param strDestDir 目标目录
	 * @return 是否解压成功
	 * @throws IOException
	 */
	private boolean unzip(final String strZipFileName, final String strDestDir) throws IOException
	{
		boolean bException = false;

		ZipFile zipFile = null;

		try
		{
			ZipEntry zipEntry = null;

			// 目标目录
			if (strDestDir == null) return false;
			File fileDest = new File(strDestDir);
			fileDest.mkdirs();

			// Zip 文件
			if (strZipFileName == null) return false;
			zipFile = new ZipFile(strZipFileName);

			// 条目枚举
			Enumeration<?> enumEntries = zipFile.entries();

			while (enumEntries.hasMoreElements())
			{
				InputStream input = null;
				FileOutputStream output = null;

				// Zip 条目
				zipEntry = (ZipEntry)enumEntries.nextElement();
				String strEntryName = zipEntry.getName();

				try
				{
					// 目录
					if (zipEntry.isDirectory())
					{
						// 条目名称
						String strName = strEntryName;
						strName = strName.substring(0, strName.length() - 1);

						// 创建目录
						File file = new File(strDestDir + File.separator + strName);
						file.mkdirs();
					}
					// 文件
					else
					{
						int nBytes = -1;
						byte[] buffer = new byte[1024];

						int nIndex = strEntryName.lastIndexOf("\\");

						if (nIndex != -1)
						{
							// 创建目录
							File file = new File(strDestDir + File.separator + strEntryName.substring(0, nIndex));
							file.mkdirs();
						}

						nIndex = strEntryName.lastIndexOf("/");

						if (nIndex != -1)
						{
							// 创建目录
							File file = new File(strDestDir + File.separator + strEntryName.substring(0, nIndex));
							file.mkdirs();
						}

						// 输入输出流
						File file = new File(strDestDir + File.separator + zipEntry.getName());
						input  = zipFile.getInputStream(zipEntry);
						output = new FileOutputStream(file);

						// 读数据
						while ((nBytes = input.read(buffer)) != -1)
						{
							// 写数据
							output.write(buffer, 0, nBytes);
						}

						// 冲刷
						output.flush();
					}
				}
				catch (IOException e)
				{
					bException = true;
					e.printStackTrace();
					throw new IOException("Unzip failure: " + e.toString());
				}
				finally
				{
					try
					{
						// 关闭输入流
						if (input != null) input.close();
					}
					catch (IOException e)
					{
						bException = true;
						e.printStackTrace();
					}

					try
					{
						// 关闭输出流
						if (output != null) output.close();
					}
					catch (IOException e)
					{
						bException = true;
						e.printStackTrace();
					}
				}
			}
		}
		catch (IOException e)
		{
			bException = true;
			e.printStackTrace();
			throw new IOException("Unzip failure: " + e.toString());
		}
		finally
		{
			// 异常
			if (bException)
			{
				try
				{
					// 关闭 Zip 文件
					if (zipFile != null) zipFile.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				Toast.makeText(this, "ZIP解压失败！", Toast.LENGTH_LONG).show();
				return false;
			}

			try
			{
				// 关闭 Zip 文件
				if (zipFile != null) zipFile.close();
				if (theApp.USE_ZIP)
				{
					//延时2.5秒
//					Delay(2500);
				}
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 设置控件值
	 */
	private void setCtrlValue()
	{
		try
		{
			// 关闭进度对话框
			if (m_progDlg != null) m_progDlg.dismiss();

		}
		catch (Exception e)
		{
		}

		String strZipFileName = getPkgDir() + "TB-TECH-HMI.zip";

		File file = new File(strZipFileName);
		try
		{
			if(file.isFile() && file.exists())
			{

			//	deleteFile(strZipFileName);

				if(file.delete())
				{
					Toast.makeText(this, "ZIP删除成功！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(this, "ZIP删除失败！", Toast.LENGTH_LONG).show();
				}
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "发生异常，删除文件失败！", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 拷贝文件夹
	 */
	private void copyFolder()
	{
		try
		{
			// 目标文件夹
			String strFolder2 = theApp.getDefPath();

			// 目标文件
			File file2 = new File(strFolder2);

			// 检测是否为文件夹
			if (file2.exists() &&
				file2.isDirectory() &&
				file2.listFiles() != null &&
				file2.listFiles().length > 0) return;

			// 源文件夹
			String strFolder1 = getExtSDCardTB();

			if (strFolder1 == null)
			{
				// 请插入 U 盘！
				String str = getString(R.string.strPlzInsertU);
				Toast.makeText(this, str, Toast.LENGTH_LONG).show();
				theApp.TLog(TAG, "Please insert U disk!");
				theApp.SLog(TAG, "Please insert U disk!");
				return;
			}

			// 拷贝文件夹
			copyFolder(true);
		}
		catch (Exception e)
		{
		}
	}


	/**
	 * 延时
	 * @param b
	 */
	private void Delay(final int b)
	{
		try
		{
			// 线程
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						// 休眠 2000 毫秒
						Thread.sleep(b);

						//重新计算图片数量
						loadPicNum();

						// 休眠 500 毫秒
						Thread.sleep(500);

						if (!theApp.USE_ZIP)
						{
							//拷贝完成
							theApp.setCopyPicture(false);
						}

					}
					catch (Exception e)
					{
					}
				}
			}.start(); // 启动线程
		}
		catch(Exception e)
		{
		}
	}


	/**
	 * 拷贝文件夹
	 * @param b
	 */
	private void copyFolder(final boolean b)
	{
		try
		{
			//开始拷贝图片
			theApp.setCopyPicture(true);

			// 请耐心加载...
			String strWait = getString(R.string.strWait);
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWait + "...");

			// 启动线程
			new Thread()
			{
				public void run()
				{
					try
					{
						// 源文件夹
						String strFolder1 = getExtSDCardTB();
						theApp.TLog(TAG, "strFolder1: " + strFolder1);
						theApp.SLog(TAG, "strFolder1: " + strFolder1);

						if (strFolder1 != null)
						{
							// 目标文件夹
							String strFolder2 = theApp.getDefPath();
							theApp.TLog(TAG, "strFolder2: " + strFolder2);
							theApp.SLog(TAG, "strFolder2: " + strFolder2);

							// 拷贝文件夹
							theApp.copyFolder(strFolder1, strFolder2);
						}
					}
					catch (Exception e)
					{
					}
					finally
					{

						//延时2.5秒
						Delay(2500);

						// 关闭进度对话框
						if (m_progDlg != null) m_progDlg.dismiss();


						//发送拷贝完成命令
//						theApp.responseKey(0, 0, 77);
					}
				}
			}.start(); // 启动线程
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取 可用内存
	 */
    private long getAvailMemory(Context context)
    {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);

        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        Log.d(TAG, "可用内存---->>>" + mi.availMem / (1024 * 1024));

        return mi.availMem / (1024 * 1024);
    }

	/**
	 * 获取 图片数量
	 */
	private  void loadPicNum()
	{
		try
		{
			// 语言路径
			String strLangPath = theApp.getDefPath() + "picture/" + theApp.getLang() + "/";
//			Log.d(TAG, "LangPath: " + strLangPath);

			//清零
			theApp.setCounter(0);
//			theApp.setPicNum(theApp.getCounter()) ;

			// 语言文件夹
			File folderLang = new File(strLangPath);

			// 检测是否为文件夹
			if (!folderLang.isDirectory()) return;

			// 编号
			File[] filesNum = folderLang.listFiles();

			// 编号数量
			int nLenNum = filesNum.length;
//			Log.d(TAG, "LenNum: " + nLenNum);

			for (int i = nLenNum - 1; i >= 0; i--)
			{
				// 编号文件夹
				File folderNum = filesNum[i];

				// 检测是否为文件夹
				if (!folderNum.isDirectory()) continue;

				// 图片
				File[] filesBmp = folderNum.listFiles();

				// 图片数量
				int nLenBmp = filesBmp.length;
//				Log.d(TAG, "LenBmp: " + nLenBmp);

				for (int j = nLenBmp - 1; j >= 0; j--)
				{
					// 图片
					File fileBmp = filesBmp[j];

					// 路径
					String strPath = fileBmp.getPath();

					// 检测文件
					if (!fileBmp.exists() || !fileBmp.isFile()) continue;

					// 计算图片个数
					if (strPath.endsWith(".jpg") || strPath.endsWith(".JPG") || strPath.endsWith(".png") || strPath.endsWith(".PNG"))
					{
						theApp.setCounter(theApp.getCounter() + 1);
//						nPicNum ++;
					}

				}
			}

			theApp.setPicNum(theApp.getCounter()) ;

			theApp.setCounter(0);

		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 获取 00.jpg 底图
	 */
	private void get00Jpg()
	{
		try
		{
			// 语言路径
			String strLangPath = theApp.getDefPath() + "picture/" + theApp.getLang() + "/";
//			Log.d(TAG, "LangPath: " + strLangPath);

			// 语言文件夹
			File folderLang = new File(strLangPath);

			// 检测是否为文件夹
			if (!folderLang.isDirectory()) return;

			// 编号
			File[] filesNum = folderLang.listFiles();

			// 编号数量
			int nLenNum = filesNum.length;
//			Log.d(TAG, "LenNum: " + nLenNum);

			for (int i = nLenNum - 1; i >= 0; i--)
			{
				// 编号文件夹
				File folderNum = filesNum[i];

				// 检测是否为文件夹
				if (!folderNum.isDirectory()) continue;

				// 图片
				File[] filesBmp = folderNum.listFiles();

				// 图片数量
				int nLenBmp = filesBmp.length;
//				Log.d(TAG, "LenBmp: " + nLenBmp);

				for (int j = nLenBmp - 1; j >= 0; j--)
				{
					// 图片
					File fileBmp = filesBmp[j];

					// 路径
					String strPath = fileBmp.getPath();

					// 检测文件
					if (!fileBmp.exists() || !fileBmp.isFile()) continue;

					// 检测是否为 00.jpg 底图
					if (!strPath.endsWith("00.jpg") && !strPath.endsWith("00.JPG")) continue;

					// 解码图像
					InputStream is = new FileInputStream(strPath);
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(is, null, options);

					try
					{
						// 关闭输入流
						if (is != null) is.close();
					}
					catch (Exception e)
					{
					}

					// 00.jpg 的宽度和高度
					int nWB = options.outWidth;
					int nHB = options.outHeight;
					theApp.setW00Jpg(nWB);
					theApp.setH00Jpg(nHB);
					return;
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	/**
	 * 检测网络状态
	 * @return
	 */
	private boolean checkInternetState()
	{
		boolean bSuc = false;
		ConnectivityManager connManager = null;

        try
        {
        	// 检测是否为 wifi 上网方式
            connManager = (ConnectivityManager)m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected())
            {
            	bSuc = true;
            }
        }
		catch(Exception ex)
		{
			android.util.Log.e("error", "checkInternetStatus_wifi:" + ex.toString());
		}

        if (false == bSuc)
        {
			try
			{
				// 检测是否为手机卡上网方式
				if (null == connManager)
				{
					connManager = (ConnectivityManager)m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
				}

				NetworkInfo mMOBILE = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

				if (mMOBILE.isConnected())
				{
					bSuc = true;
				}
			}
			catch (Exception ex)
			{
				android.util.Log.e("error", "checkInternetStatus_wifi:" + ex.toString());
			}
        }

        // 检测失败
        if (false == bSuc)
        {
			// 退到最后一页的提示文字
			String strMsg = m_context.getString(R.string.browser_url_netUnconnect);

			// 界面提示
			Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
        }

        return bSuc;
	}

	/**
	 * 检测 00.jpg 底图
	 * @param strPicturePath
	 * @return
	 */
	private boolean check00Jpg(final String strPicturePath)
	{
		try
		{
			// 语言路径
			String strLangPath = strPicturePath + theApp.getLang() + "/";
//			Log.d(TAG, "LangPath: " + strLangPath);

			// 语言文件夹
			File folderLang = new File(strLangPath);

			// 检测是否为文件夹
			if (!folderLang.isDirectory()) return false;

			// 编号
			File[] filesNum = folderLang.listFiles();

			// 编号数量
			int nLenNum = filesNum.length;
//			Log.d(TAG, "LenNum: " + nLenNum);

			for (int i = nLenNum - 1; i >= 0; i--)
			{
				// 编号文件夹
				File folderNum = filesNum[i];

				// 检测是否为文件夹
				if (!folderNum.isDirectory()) continue;

				// 图片
				File[] filesBmp = folderNum.listFiles();

				// 图片数量
				int nLenBmp = filesBmp.length;
//				Log.d(TAG, "LenBmp: " + nLenBmp);

				for (int j = nLenBmp - 1; j >= 0; j--)
				{
					// 图片
					File fileBmp = filesBmp[j];

					// 路径
					String strPath = fileBmp.getPath();

					// 检测文件
					if (!fileBmp.exists() || !fileBmp.isFile()) continue;

					// 检测是否为 00.jpg 底图
					if (strPath.endsWith("00.jpg") || strPath.endsWith("00.JPG"))
					{
						return true;
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

		return false;
	}

	/**
	 * 加载 00.jpg 底图
	 */
	private void load00Jpg()
	{
		try
		{
			boolean bRedraw = false;

			// 语言路径
//			String strLangPath = theApp.getDefPath() + "picture/" + theApp.getLang() + "/";
			String strLangPath = theApp.getPicturePath() + theApp.getLang() + "/";
//			Log.d(TAG, "LangPath: " + strLangPath);

			// 语言文件夹
			File folderLang = new File(strLangPath);

			// 检测是否为文件夹
			if (!folderLang.isDirectory()) return;

			// 编号
			File[] filesNum = folderLang.listFiles();

			// 编号数量
			int nLenNum = filesNum.length; //有几个类似01,02的文件夹
//			Log.d(TAG, "LenNum: " + nLenNum);

			for (int i = nLenNum - 1; i >= 0; i--)
			{
				// 编号文件夹
				File folderNum = filesNum[i]; //类似01,02这种文件夹

				// 检测是否为文件夹
				if (!folderNum.isDirectory()) continue;

				// 图片
				File[] filesBmp = folderNum.listFiles();//每个01,02文件夹里面的图片

				// 图片数量
				int nLenBmp = filesBmp.length;//01,02里面的图片数量
//				Log.d(TAG, "LenBmp: " + nLenBmp);

				for (int j = nLenBmp - 1; j >= 0; j--)
				{
					// 图片
					File fileBmp = filesBmp[j];

					// 路径
					String strPath = fileBmp.getPath(); //每张图片路径

					// 检测文件
					if (!fileBmp.exists() || !fileBmp.isFile()) continue;

					// 检测是否为 00.jpg 底图
					if (!strPath.endsWith("00.jpg") && !strPath.endsWith("00.JPG")) continue;

					// 未重绘
					if (!bRedraw)
					{
						// 已重绘
						bRedraw = true;

						// 解码图像
						InputStream is = new FileInputStream(strPath);
						final BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(is, null, options);

						// 宽度和高度
						int nWB = options.outWidth;
						int nHB = options.outHeight;
						int nWP = theApp.getWidthPixels();//RK3188 屏幕宽度和高度
						int nHP = theApp.getHeightPixels();//RK3188 屏幕宽度和高度
						if (nWB <= 0) nWB = 1;
						if (nHB <= 0) nHB = 1;

						//设置缩放的分辨率
						if (theApp.SCALED_XY)
						{
							nWP = 1280;
							nHP = 720;
						}

						// 宽度和高度比例
						float fWR = (float)nWP / nWB;
						float fHR = (float)nHP / nHB;

						if (theApp.SCALED_XY)//缩放坐标
						{
							fWR = (float) nWB / nWP ;
							fHR = (float) nHB / nHP ;
						}
						if (fWR <= 0) fWR = 1;
						if (fHR <= 0) fHR = 1;
						if (theApp.getWidthRatio()  == 1.0f) theApp.setWidthRatio(fWR);
						if (theApp.getHeightRatio() == 1.0f) theApp.setHeightRatio(fHR);
						fWR = theApp.getWidthRatio();
						fHR = theApp.getHeightRatio();
						Log.d(TAG, String.format("WidthRatio: %f, HeightRatio: %f", fWR, fHR));
						theApp.SLog(TAG, String.format("WidthRatio: %f, HeightRatio: %f", fWR, fHR));

						// 缩放图片
						if (theApp.SCALED_BMP) //if () theApp.SCALED_XY
						{
							// 检测宽度和高度比例
							if (fWR != 1.0 && fHR != 1.0)
							{
								// 顶部悬浮窗高度, 左上角和右下角坐标
								int nTopH  = (int)(theApp.getTopH()  * fHR);
								int nTopX1 = (int)(theApp.getTopX1() * fWR);
								int nTopY1 = (int)(theApp.getTopY1() * fHR);
								int nTopX2 = (int)(theApp.getTopX2() * fWR);
								int nTopY2 = (int)(theApp.getTopY2() * fHR);
								theApp.setTopH(nTopH);
								theApp.setTopX1(nTopX1);
								theApp.setTopY1(nTopY1);
								theApp.setTopX2(nTopX2);
								theApp.setTopY2(nTopY2);
								theApp.SLog(TAG, "TopH: " + nTopH);
								theApp.SLog(TAG, "TopX1: " + nTopX1);
								theApp.SLog(TAG, "TopY1: " + nTopY1);
								theApp.SLog(TAG, "TopX2: " + nTopX2);
								theApp.SLog(TAG, "TopY2: " + nTopY2);

								// 底部悬浮窗高度, 左上角和右下角坐标
								int nBottomH  = (int)(theApp.getBottomH()  * fHR);
								int nBottomX1 = (int)(theApp.getBottomX1() * fWR);
								int nBottomY1 = (int)(theApp.getBottomY1() * fHR);
								int nBottomX2 = (int)(theApp.getBottomX2() * fWR);
								int nBottomY2 = (int)(theApp.getBottomY2() * fHR);
								theApp.setBottomH(nBottomH);
								theApp.setBottomX1(nBottomX1);
								theApp.setBottomY1(nBottomY1);
								theApp.setBottomX2(nBottomX2);
								theApp.setBottomY2(nBottomY2);
								theApp.SLog(TAG, "BottomH: " + nBottomH);
								theApp.SLog(TAG, "BottomX1: " + nBottomX1);
								theApp.SLog(TAG, "BottomY1: " + nBottomY1);
								theApp.SLog(TAG, "BottomX2: " + nBottomX2);
								theApp.SLog(TAG, "BottomY2: " + nBottomY2);

								// 开关悬浮窗左上角坐标
								int nSwitchX1 = (int)(theApp.getSwitchX1() * fWR);
								int nSwitchY1 = (int)(theApp.getSwitchY1() * fHR);
								theApp.setSwitchX1(nSwitchX1);
								theApp.setSwitchY1(nSwitchY1);
							}
						}

						// 设置 JPG 图片
						theApp.setJpg();

						// 重绘
//						m_handler.sendEmptyMessage(MSG_REDRAW);
					}

					// 计算文件的 CRC32
					long lCrc32 = theApp.fileCRC32(strPath);

					// JPG CRC32 哈希表
					theApp.getJpgMap().put(strPath, lCrc32);
//					Log.d(TAG, String.format("strPath: %s\r\nlCrc32: %d", strPath, lCrc32));
				}
			}

			// 检测 JPG CRC32 哈希表大小
			if (theApp.getJpgMap().size() > 0)
			{
				// 已计算 JPG 的 CRC32
				theApp.setJpgCrc32(true);
				Log.d(TAG, String.format("JpgMap: %d", theApp.getJpgMap().size()));
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 加载声音
	 */
	private void loadSound()
	{
		// SoundPool 加载声音为异步方式, 因此在 play 之前必须先 load
		try
		{
			// 声音路径
			String strSpeechPath = theApp.getDefPath() + "speech/";

			// 声音文件夹
			File fileSpeech = new File(strSpeechPath);

			// 检测是否为文件夹
			if (fileSpeech.isDirectory())
			{
				// 语言
				File[] filesSpeech = fileSpeech.listFiles();
				int nLenSpeech = filesSpeech.length;

				for (int i = 0; i < nLenSpeech; i++)
				{
					// 语言文件夹
					File fileLang = filesSpeech[i];

					// 检测是否为文件夹
					if (fileLang.isDirectory())
					{
						// 声音
						File[] files = fileLang.listFiles();
						int nLen = files.length;

						for (int j = 0; j < nLen; j++)
						{
							// 文件
							File file = files[j];

							// 路径
							String strPath = file.getPath();

							// 检测文件
							if (!file.exists() || !file.isFile())
							{
								// 声音错误
//								String strErr = "Sound error: " + strPath;
//								Log.d(TAG, strErr);
//								if (theApp.DEBUG) Toast.makeText(this, strErr, Toast.LENGTH_SHORT).show();
								continue;
							}

							// 加载声音
							int nSoundID = theApp.getSoundPool().load(strPath, 1);
//							Log.d(TAG, "SoundID: " + nSoundID);
//							Log.d(TAG, "SoundPath: " + strPath);

							// 声音哈希表
							theApp.getSoundMap().put(nSoundID, strPath);
						}
					}
				}
			}

			// 已加载声音
			if (theApp.getSoundMap().size() > 0) theApp.setLoadSound(true);
			Log.d(TAG, "LoadSound: " + theApp.isLoadSound());
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载 Apps
	 */
	private void loadApps()
	{
		try
		{
			// 请等待加载...
			String strWaitLoad = getString(R.string.strWaitLoad);
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWaitLoad + "...");

			// 启动线程
			new Thread()
			{
				public void run()
				{
					try
					{
						// 加载 Apps
						loadApps(true);
					}
					catch (Exception e)
					{
					}
					finally
					{
						// 关闭进度对话框
						if (m_progDlg != null) m_progDlg.dismiss();

						// 初始化 Apps
						m_handler.sendEmptyMessage(MSG_INIT_A);
					}
				}
			}.start(); // 启动线程
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载 Apps
	 * @param b
	 */
	private void loadApps(final boolean b)
	{
		try
		{
			// 未加载 Apps
			theApp.setLoadApps(false);

			// 获取包名
			String strPkgName = getPackageName();

			// 包管理器
			PackageManager pm = getPackageManager();

			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 获取 Apps 列表
			theApp.setListApps(pm.queryIntentActivities(mainIntent, 0));

			// 数量
			int nSize = theApp.getListApps().size();
//			theApp.MYSLog(TAG,"true"+nSize);

			for (int i = 0; i < nSize; i++)
			{
				// App 信息
				ResolveInfo info = theApp.getListApps().get(i);

				// App 包名
				String strPkg = info.activityInfo.packageName;
//				String strPkgSet = "com.android.settings";
//				String strPkgFb = "com.fb.FileBrower";
//				String strPkgAp = "com.gsoft.appinstall";
//				String strPkgUp = "com.example.Upgrade";
//				String strPkgVp = "com.farcore.videoplayer";
//				||	strPkg.contains(strPkgUp)|| strPkg.contains(strPkgVp)
//				theApp.MYSLog(TAG,strPkg);
/*				 if(strPkg.contains(strPkgName)||strPkg.contains(strPkgSet)||strPkg.contains(strPkgFb)||
					strPkg.contains(strPkgAp))
				 {
						// 移除本 App
						theApp.getListApps().remove(info);
						i--;
				 }*/

				// 检测是否为本 App
				if (strPkg.equals(strPkgName))
				{
					// 移除本 App
					theApp.getListApps().remove(info);
					nSize = theApp.getListApps().size();
					i--;
					continue;
				}

				if (!theApp.isShowSApk())
				{
					//系统设置
					String strPkgSet = "com.android.settings";

					if (strPkg.equals(strPkgSet))
					{
//						theApp.MYSLog(TAG,"setings");

						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

					//文件管理
					String strPkgFb = "com.android.rk";

					if (strPkg.equals(strPkgFb))
					{
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

					//应用安装
					String strPkgAp = "com.android.apkinstaller";

					if (strPkg.equals(strPkgAp))
					{
//						theApp.MYSLog(TAG,"ap");
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

					//AV IN
					String strPkgAV = "com.example.cameratutorial";

					if (strPkg.equals(strPkgAV))
					{
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

				}
				if (!theApp.VPLAYER)
				{
					String strPkgVp = "android.rk.RockVideoPlayer";

					if (strPkg.equals(strPkgVp))
					{
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}
				}

				if (theApp.NOTOP)
				{
					if (theApp.getLang().equals("en"))
					{
						String strPkgYK = "com.youku.phone";

						if (strPkg.equals(strPkgYK))
						{
							// 移除
							theApp.getListApps().remove(info);
							nSize = theApp.getListApps().size();
							i--;
							continue;
						}
					}
					if (theApp.getLang().equals("en"))
					{
						String strPkgKG = "com.kugou.android";

						if (strPkg.equals(strPkgKG))
						{
							// 移除
							theApp.getListApps().remove(info);
							nSize = theApp.getListApps().size();
							i--;
							continue;
						}
					}
				}
			/*	String strPkgGa = "com.android.gallery3d";

				if (strPkg.equals(strPkgGa))
				{
					// 移除
					theApp.getListApps().remove(info);
					nSize = theApp.getListApps().size();
					i--;
					continue;
				}	*/

				String strPkgFs = "com.adobe.flashplayer";

				if (strPkg.equals(strPkgFs))
				{
					// 移除
					theApp.getListApps().remove(info);
					nSize = theApp.getListApps().size();
					i--;
					continue;
				}

				if (theApp.WNQ)
				{
					String strPkgClk = "com.android.deskclock";

					if (strPkg.equals(strPkgClk))
					{
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

					String strPkgCalendar = "com.android.calendar";

					if (strPkg.equals(strPkgCalendar))
					{
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

					String strPkgCalculator = "com.android.calculator2";

					if (strPkg.equals(strPkgCalculator))
					{
						// 移除
						theApp.getListApps().remove(info);
						nSize = theApp.getListApps().size();
						i--;
						continue;
					}

				}
				String strPkgPe = "com.android.contacts";

				if (strPkg.equals(strPkgPe))
				{
					// 移除
					theApp.getListApps().remove(info);
					nSize = theApp.getListApps().size();
					i--;
					continue;
				}

				String strPkgEmail = "com.android.email";

				if (strPkg.equals(strPkgEmail))
				{
					// 移除
					theApp.getListApps().remove(info);
					nSize = theApp.getListApps().size();
					i--;
					continue;
				}


			}
			// 已加载 Apps
			theApp.setLoadApps(true);
			Log.d(TAG, "LoadApps: " + theApp.isLoadApps());
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载音乐
	 */
	private void loadMusic()
	{
		try
		{
			// 正在加载音乐
			theApp.setLoadingMusic(true);

			// 请等待加载...
			String strWaitLoad = getString(R.string.strWaitLoad);
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWaitLoad + "...");

			// 启动线程
			new Thread()
			{
				public void run()
				{
					try
					{
						// 加载音乐
						loadMusic(true);
					}
					catch (Exception e)
					{
					}
					finally
					{
						// 关闭进度对话框
						if (m_progDlg != null) m_progDlg.dismiss();

						// 初始化音乐 xwx
						m_handler.sendEmptyMessage(MSG_INIT_M);
					}
				}
			}.start(); // 启动线程
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载音乐
	 * @param b
	 */
	private void loadMusic(final boolean b)
	{
		try
		{
			// 未加载音乐
			theApp.setLoadMusic(false);

			// 获取 SDCard 路径
			String strPath = getSDCardPath();

			// 文件
			File file = new File(strPath);

			// 检测是否为文件夹
			if (!file.exists() ||!file.isDirectory() ||file.listFiles() == null ||file.listFiles().length <= 0)
			{
                file = new File("/mnt/usb_storage");
                if (!file.exists() || !file.isDirectory() || file.listFiles() == null || file.listFiles().length <= 0)
                {
//                    System.out.println(" file.listFiles");
                    theApp.getListMusicPath().clear();
                    theApp.setLoadMusic(true);
                    return;
                }
			}

			// 音乐路径
			theApp.getListMusicPath().clear();
			theApp.SLog(TAG, "ListMusicPathSize: " + theApp.getListMusicPath().size());

			// 加载音乐
			loadMusic(theApp.getListMusicPath(), file.listFiles());
			theApp.SLog(TAG, "ListMusicPathSize: " + theApp.getListMusicPath().size());

			try
			{
				// 检测扩展 SD 卡列表
				if (theApp.getListExtStorage() != null && theApp.getListExtStorage().size() > 0)
				{
					// 数量
					int nSize = theApp.getListExtStorage().size();

					for (int i = 0; i < nSize; i++)
					{
						// 路径
						String strExtPath = theApp.getListExtStorage().get(i);
						if (strExtPath == null) continue;

						// 文件
						File fileExt = new File(strExtPath);

						// 检测是否为文件夹
						if (!fileExt.exists() ||
							!fileExt.isDirectory() ||
							fileExt.listFiles() == null ||
							fileExt.listFiles().length <= 0) continue;

						// 加载视频
						Log.d(TAG, "loadMusic - " + strExtPath);
						theApp.TLog(TAG, "loadMusic - " + strExtPath);
						theApp.SLog(TAG, "loadMusic - " + strExtPath);
						loadMusic(theApp.getListMusicPath(), fileExt.listFiles());
					}

					theApp.SLog(TAG, "ListMusicPathSize: " + theApp.getListMusicPath().size());
				}
			}
			catch (Exception e)
			{
				Log.d(TAG, e.toString());
				theApp.TLog(TAG, e.toString());
				theApp.SLog(TAG, e.toString());
			}

			// 排序
			Collections.sort(theApp.getListMusicPath(), String.CASE_INSENSITIVE_ORDER);

			// 数量
			int nSize = theApp.getListMusicPath().size();
			if (nSize < 1) return;

			// 已加载音乐
			theApp.setLoadMusic(true);
			Log.d(TAG, "SetLoadMusic: " + String.valueOf(theApp.isLoadMusic()));
			theApp.TLog(TAG, "SetLoadMusic: " + String.valueOf(theApp.isLoadMusic()));
			theApp.SLog(TAG, "SetLoadMusic: " + String.valueOf(theApp.isLoadMusic()));
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载音乐
	 * @param listPath
	 * @param files
	 */
	private void loadMusic(List<String> listPath, File[] files)
	{
		try
		{
			if (listPath == null) return;
			if (files == null || files.length <= 0) return;

			for (int i = 0; i < files.length; i++)
			{
				// 文件
				File file = files[i];

				// 检测文件
				if (!file.exists()) continue;

				// 文件
				if (file.isFile())
				{
					// 获取文件路径
					String strPath = file.getPath();

					// 忽略以下文件夹
					int nPos = strPath.indexOf("TB-TECH-HMI");
					if (nPos > -1 && nPos < strPath.length()) continue;

					// 获取扩展名
					String strExt = getFileExt(strPath);

					// 扩展名一致
					if (strExt.equals(".mp3") ||
						strExt.equals(".wav") ||
						strExt.equals(".wma") ||
						strExt.equals(".ogg"))
					{
						// 添加路径
						if (!theApp.checkList(listPath, strPath)) listPath.add(strPath);
					}
				}

				// 文件夹
				if (file.isDirectory())
				{
					// 加载音乐, 递归调用
					loadMusic(listPath, file.listFiles());
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载视频
	 */
	private void loadVideo()
	{
		try
		{
			// 正在加载视频
			theApp.setLoadingVideo(true);

			// 请等待加载...
			String strWaitLoad = getString(R.string.strWaitLoad);
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWaitLoad + "...");

			// 启动线程
			new Thread()
			{
				public void run()
				{
					try
					{
						// 加载视频
						loadVideo(true);
					}
					catch (Exception e)
					{
					}
					finally
					{
						// 关闭进度对话框
						if (m_progDlg != null) m_progDlg.dismiss();

						// 加载视频
						m_handler.sendEmptyMessage(MSG_INIT_V);
					}
				}
			}.start(); // 启动线程
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载视频
	 * @param b
	 */
	private void loadVideo(final boolean b)
	{
		try
		{
			// 未加载视频
			theApp.setLoadVideo(false);

			// 获取 SDCard 路径
			String strPath = getSDCardPath();

			// 文件
			File file = new File(strPath);

			// 检测是否为文件夹
			if (!file.exists() ||!file.isDirectory() ||file.listFiles() == null ||file.listFiles().length <= 0)
			{
                file = new File("/mnt/usb_storage");
                if (!file.exists() || !file.isDirectory() || file.listFiles() == null || file.listFiles().length <= 0)
                {
                    theApp.getListVideoPath().clear();
                    theApp.setLoadVideo(true);
                    return;
                }
			}

			// 视频路径
			theApp.getListVideoPath().clear();
			theApp.SLog(TAG, "ListVideoPathSize: " + theApp.getListVideoPath().size());

			// 加载视频
			loadVideo(theApp.getListVideoPath(), file.listFiles());
			theApp.SLog(TAG, "ListVideoPathSize: " + theApp.getListVideoPath().size());

			try
			{
				// 检测扩展 SD 卡列表
				if (theApp.getListExtStorage() != null && theApp.getListExtStorage().size() > 0)
				{
					// 数量
					theApp.SLog(TAG, "ListExtStorageSize: " + theApp.getListExtStorage().size());
					int nSize = theApp.getListExtStorage().size();

					for (int i = 0; i < nSize; i++)
					{
						// 路径
						String strExtPath = theApp.getListExtStorage().get(i);
						if (strExtPath == null) continue;
						theApp.SLog(TAG, "ExtPath: " + strExtPath);

						// 文件
						File fileExt = new File(strExtPath);

						// 检测是否为文件夹
						if (!fileExt.exists() ||
							!fileExt.isDirectory() ||
							fileExt.listFiles() == null ||
							fileExt.listFiles().length <= 0) continue;

						// 加载视频
						Log.d(TAG, "loadVideo - " + strExtPath);
						theApp.TLog(TAG, "loadVideo - " + strExtPath);
						theApp.SLog(TAG, "loadVideo - " + strExtPath);
						loadVideo(theApp.getListVideoPath(), fileExt.listFiles());
					}

					theApp.SLog(TAG, "ListVideoPathSize: " + theApp.getListVideoPath().size());
				}
			}
			catch (Exception e)
			{
				Log.d(TAG, e.toString());
				theApp.TLog(TAG, e.toString());
				theApp.SLog(TAG, e.toString());
			}

			// 排序
			Collections.sort(theApp.getListVideoPath(), String.CASE_INSENSITIVE_ORDER);

			// 数量
			int nSize = theApp.getListVideoPath().size();
			if (nSize < 1) return;

			// 已加载视频
			theApp.setLoadVideo(true);
			Log.d(TAG, "SetLoadVideo: " + String.valueOf(theApp.isLoadVideo()));
			theApp.TLog(TAG, "SetLoadVideo: " + String.valueOf(theApp.isLoadVideo()));
			theApp.SLog(TAG, "SetLoadVideo: " + String.valueOf(theApp.isLoadVideo()));
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载视频
	 * @param listPath
	 * @param files
	 */
	private void loadVideo(List<String> listPath, File[] files)
	{
		try
		{
			if (listPath == null) return;
			if (files == null || files.length <= 0) return;

			for (int i = 0; i < files.length; i++)
			{
				// 文件
				File file = files[i];

				// 检测文件
				if (!file.exists()) continue;

				// 文件
				if (file.isFile())
				{
					// 获取文件路径
					String strPath = file.getPath();

					// 忽略以下文件夹
					int nPos = strPath.indexOf("TB-TECH-HMI");
					if (nPos > -1 && nPos < strPath.length()) continue;

					// 获取扩展名
					String strExt = getFileExt(strPath);

					// 扩展名一致
					if (strExt.equals(".avi") ||
						strExt.equals(".mp4") ||
						strExt.equals(".mpg") ||
//						strExt.equals(".dat") ||
						strExt.equals(".mov") ||
						strExt.equals(".mkv") ||
						strExt.equals(".3gp") ||
						strExt.equals(".divx") ||
						strExt.equals(".rm") ||
						strExt.equals(".rmvb") ||
						strExt.equals(".vob"))
					{
						// 添加路径
						if (!theApp.checkList(listPath, strPath)) listPath.add(strPath);
					}
				}

				// 文件夹
				if (file.isDirectory())
				{
					// 加载视频, 递归调用
					loadVideo(listPath, file.listFiles());
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	/**
	 *  获取mac地址
	 */
	public static String getLocalMacAddress() {
        String Mac="";
        try{

            String path="sys/class/net/wlan0/address";
            if((new File(path)).exists())
            {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if(byteCount>0)
                {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
            }
            Log.v("daming.zou***wifi**mac11**", ""+Mac);
            if(Mac==null||Mac.length()==0)
            {
                path="sys/class/net/eth0/address";
                FileInputStream fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if(byteCount_name>0)
                {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
            }
            Log.v("daming.zou***eth0**mac11**", ""+Mac);

            if(Mac.length()==0||Mac==null){
                return "";
            }
        }catch(Exception io){
            Log.v("daming.zou**exception*", ""+io.toString());
        }

        Log.v("xulongheng*Mac", Mac);
        return Mac.trim();
//      WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//      WifiInfo info = wifi.getConnectionInfo();
//      if (info.getMacAddress() != null) {
//          return info.getMacAddress().toString();
//      }
    }
	/**
	 * 获取mac地址16进制数
	 */
	public static void getHexMac()
	{
//		boolean bIsSuccess = false;
		String mac = null;
		int [] m = new int[6];
		try
		{
			mac = theApp.getMacAdress();
			if (mac != null && mac.length() != 0)
			{
				m[0]= theApp.str2int(mac.substring(0, 1));
				m[1]= theApp.str2int(mac.substring(3, 4));
				m[2]= theApp.str2int(mac.substring(6, 7));
				m[3]= theApp.str2int(mac.substring(9, 10));
				m[4]= theApp.str2int(mac.substring(12, 13));
				m[5]= theApp.str2int(mac.substring(15, 16));
				Log.d(TAG, "MAC: " + String.valueOf(m[0]) + String.valueOf(m[1]) +
						String.valueOf(m[2]) + String.valueOf(m[3]) + String.valueOf(m[4])
						+ String.valueOf(m[5]));
			}

		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 上传TB数据
	 * @param data
	 */
	public static void loadInternet(final int state)
	{
//		boolean bIsSuccess = false;
		String url = null;
		try
		{
			if (theApp.getMacAdress() == null || theApp.getMacAdress().length() == 0)
			{
				macURL = getLocalMacAddress();//获取mac地址
				theApp.setMacAdress(macURL);
				theApp.writePrefStatus();
			}
			String AndroidVer = "1.1";
//			String modle = "M0018008070600"; //mbh 9800 M0018008070600
//			String modle = "M0018108070600"; //mbh s906 M001808070600
//			String modle = "M0018108090600"; //mbh s906 奕力触摸屏
//			String modle = "M0017007090900"; //mbh 9900 M0018108070600
//			String modle = "M0019009090900"; //mbh LE2000
//			String modle = "M0015005030600"; //迈宝赫5812
//			String modle = "M0017007060600"; //mbh MS
//			String modle = "M0038008090900"; //美能达18.5
//			String modle = "N0018008070600"; //LD917
//			String modle = "N0018008090600"; //LD917 奕力触摸屏
//			String modle = "N0018108070600"; //LD1800A
//			String modle = "N0019009060600"; //LD215
//			String modle = "M0019009060600"; //MBH215
//			String modle = "A0018008070600"; //AFD
//			String modle = "A0028008070600"; //奥圣佳
//			String modle = "B0038008090601"; //伯利恒18.5-奕力
//			String modle = "B0048008090601"; //布莱特威
//			String modle = "B0053004010600"; //贝爱10.1
//			String modle = "D0039009060600"; //顶康21.5
//			String modle = "D0018108090600"; //大胡子18.5
//			String modle = "D0017007060800"; //大胡子9100
//			String modle = "H0010001080800"; //HX-I5
//			String modle = "H0017007090900"; //HX156
//			String modle = "H0053004010600"; //HES-华尔斯10.1
//			String modle = "H0063004010600"; //红外10.1
//			String modle = "S0015005030600"; //舒华11.6
//			String modle = "B0013004010600"; //宝德龙力量10.1
//			String modle = "B0017007060600"; //宝德龙9600
			String modle = "B0018008060600"; //宝德龙8800 185
//			String modle = "B0019009060600"; //宝德龙215
//			String modle = "W0017007090900"; //wnq156
//			String modle = "M0027007060600"; //铭扬156
//			String modle = "L0038008060600"; //力道 185
//			String modle = "C0015005030600"; //驰健11.6
//			String modle = "C0018008070601"; //cj18.5
//			String modle = "C0018008090601"; //cj18.5-奕力
//			String modle = "Z0019009060600"; //正星V12
//			String modle = "Z0018008070601"; //正星V8
//			String modle = "Z0018008090601"; //正星V8-奕力
//			String modle = "Z0013004010601"; //正星V8-奕力
//			String modle = "T0028008070601"; //铁人18.5
//			String modle = "T0018008070600"; //天展18.5
//			String modle = "T0018008090600"; //天展18.5奕力触摸屏
//			String modle = "T0019009060600"; //天展21.5
//			String modle = "T0013004010600"; //天展10.1
//			String modle = "N0028008070601"; //诺德健18.5
//			String modle = "N0028008090601"; //诺德健18.5奕力触摸屏
//			String modle = "Y0037007060600"; //优步156
//			String modle = "Z0028008070601"; //中大18.5
//			String modle = "Z0078008070601"; //卓力康18.5
//			String modle = "J0058008070601"; //精工18.5
//			String modle = "J0048008070601"; //军霞18.5

			String lock = String.valueOf(theApp.getLock()) ;//解锁状态
//			String key = theApp.md5(theApp.getKey());
//			StringBuffer sb = new StringBuffer(baseURL);

			if (state == 0)
			{
				StringBuffer sb = new StringBuffer(baseURL);
	            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
				sb.append("&modle=").append(modle).append("&mac_address=").append(theApp.getMacAdress())
				.append("&run_time=").append(theApp.getRunTime()).append("&use_time=").append(theApp.getUseTime())
				.append("&page_time=").append(theApp.getPageTime()).append("&run_distance=").append(theApp.getDistance())
				.append("&poweron_times=").append(theApp.getTimes()).append("&power_status=").append(theApp.getStatus())
				.append("&dsp_version=").append(theApp.getVersion()).append("&android_version=").append(AndroidVer).append("&error_times=")
				.append(theApp.getErrTimes()).append("&error=").append(theApp.getErr()).append("&idle_time=").append(theApp.getIdleTime())
				.append("&key=").append(theApp.md5(theApp.getKey())).append("&login=").append(lock);
				url = sb.toString();
				sb.delete(0,sb.length()-1);
			}
			else if (state == 1)
			{
				StringBuffer sb = new StringBuffer(baseURL_Rsl);
	            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
				sb.append("&run_time=").append(theApp.getRunTime()).append("&mac_address=").append(theApp.getMacAdress())
//				.append("&use_time=").append(theApp.getUseTime())
				.append("&run_distance=").append(theApp.getDistance())
				.append("&speed=").append(theApp.getSpeed()).append("&pace=").append(theApp.getPace())
				.append("&pulse=").append(theApp.getPulse()).append("&cal=").append(theApp.getCal());
				url = sb.toString();
				sb.delete(0,sb.length()-1);
			}
			else if (state == 2)
			{
				StringBuffer sb = new StringBuffer(baseURL_Pwl);
	            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
				sb.append("&time=").append(theApp.getPowerTime()).append("&mac_address=").append(theApp.getMacAdress())
				.append("&number=").append(theApp.getNumber()).append("&weight=").append(theApp.getPowerWeight())
				.append("&height=").append(theApp.getPowerHeight()).append("&power=").append(theApp.getPower())
				.append("&frq=").append(theApp.getFrq()).append("&cal=").append(theApp.getCal())
				.append("&avg_power=").append(theApp.getAvgPower());
				url = sb.toString();
				sb.delete(0,sb.length()-1);
//				theApp.showMyosd("powerdata = "+ url,300,200,(byte)0x03,(byte)0x22);
			}

//			theApp.showMyosd("ulr = "+ url,400,200,(byte)0x03,(byte)0x22);

          /*  String url = baseURL + "&modle=" + modle + "&mac_address=" + macURL
            		 + "&run_time=" + theApp.getRunTime() + "&use_time=" + theApp.getUseTime() + "&page_time=" + theApp.getPageTime() + "&run_distance="
            		 + theApp.getDistance() + "&poweron_times=" + theApp.getTimes() + "&power_status=" + theApp.getStatus() + "&dsp_version=" + theApp.getVersion()
            		 + "&android_version=" + "1.0" + "&error_times=" + theApp.getErrTimes() + "&error=" + theApp.getErr() + "&idle_time=" + theApp.getIdleTime()
            		 + "&key=" + key + "&login=" + lock;*/

            // 生成请求对象
            HttpGet httpGet = new HttpGet(url);
			// 访问网站超时设置
			BasicHttpParams httpParams = new BasicHttpParams();

			// 设置请求超时时间(毫秒)
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);

			// 设置等待数据超时时间(毫秒)
			HttpConnectionParams.setSoTimeout(httpParams, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
//            HttpClient httpClient = new DefaultHttpClient();

            // 发送请求
            try
            {

                HttpResponse response = httpClient.execute(httpGet);

                //请求成功
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    //取得返回的字符串
                    String strResult = EntityUtils.toString(response.getEntity());

        			// 转成 utf-8 的编码, 若还是乱码则用 unicode 编码
                    strResult = new String(strResult.getBytes("ISO-8859-1"), "UTF-8");

        			// 返回值转 json 数据格式
        			JSONObject json = new JSONObject(strResult);

        			//String result = json.getString("result");

        			String status = json.getString("status");

        			String qrcode = json.getString("qrcode");

        			String login = json.getString("login");

//        			theApp.clearOsd3(200,200,300,450);

//        			theApp.showMyosd("key= "+theApp.md5(theApp.getKey()),200,200,(byte)0x03,(byte)0x22);

//        			theApp.showMyosd("Login= "+login,200,300,(byte)0x03,(byte)0x22);

//        			theApp.showMyosd("status=" +status,200,400,(byte)0x03,(byte)0x22);

        			if (status.equals("enable")) theApp.setRunstatus(true);//dyp设置运行状态

        			if (status.equals("disable")) theApp.setRunstatus(false);

        			if (login.equals("enable"))
        			{
        				//theApp.responseE2(0, 0, 0x1f);
        				theApp.setLogin(true);//dyp设置登录状态
        			}
        			else if (login.equals("disable"))
        			{
        				theApp.setLogin(false);
        				theApp.setLock(0);//登录解锁状态, 0: 不设置,
        			}

        			if (qrcode.equals("on")) theApp.setQrcode(true);//是否显示二维码
        			if (qrcode.equals("off")) theApp.setQrcode(false);
        			theApp.writePrefStatus();//写共享属性
        			theApp.setResponse(0);////请求次数
        			// 判断是否登陆成功
        	/*		if (result.equals("success"))
//        			if (json.has("success"))
        			{
        				Toast.makeText(m_context, "连接服务器成功", Toast.LENGTH_SHORT).show();
//        				bIsSuccess = json.getBoolean("success");
        			}
        			else
        				Toast.makeText(m_context, "连接服务器失败", Toast.LENGTH_SHORT).show();

        			if (status.equals("enabale"))
        			{
        				Toast.makeText(m_context, "跑步机状态开启", Toast.LENGTH_SHORT).show();
        			}
        			else
        				Toast.makeText(m_context, "跑步机状态关闭", Toast.LENGTH_SHORT).show();*/

                }
                /*   else
                {
                	theApp.setResponse(theApp.getResponse()+ 1);

                	if (theApp.getResponse() >= 45 && theApp.getResponse() < 50)
                	{
                		closewifi();
                	}
                	else if (theApp.getResponse() >= 50)
                	{
                		openWifi();

                		if (theApp.getResponse() > 55)
                		theApp.setResponse(0);
                	}

                }  */

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 加载图片
	 */
	private void loadPicture()
	{
		try
		{
			// 正在加载图片
			theApp.setLoadingPicture(true);

			// 请等待加载...
			String strWaitLoad = getString(R.string.strWaitLoad);
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWaitLoad + "...");

			// 启动线程
			new Thread()
			{
				public void run()
				{
					try
					{
						// 加载图片
						loadPicture(true);
					}
					catch (Exception e)
					{
					}
					finally
					{
						// 关闭进度对话框
						if (m_progDlg != null) m_progDlg.dismiss();

						// 初始化图片
						m_handler.sendEmptyMessage(MSG_INIT_P);
					}
				}
			}.start(); // 启动线程
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载图片
	 * @param b
	 */
	private void loadPicture(final boolean b)
	{
		try
		{
			// 未加载图片
			theApp.setLoadPicture(false);

			// 获取 SDCard 路径
			String strPath = getSDCardPath();

			// 文件
			File file = new File(strPath);

			// 检测是否为文件夹
			if (!file.exists() ||!file.isDirectory() ||file.listFiles() == null ||file.listFiles().length <= 0)
			{
                file = new File("/mnt/usb_storage");
                if (!file.exists() || !file.isDirectory() || file.listFiles() == null || file.listFiles().length <= 0)
                {

                    theApp.getListPicturePath().clear();
                    theApp.setLoadPicture(true);
                    return;
                }
			}
			// 图片路径
//			theApp.newListPicturePath();
			theApp.getListPicturePath().clear();
			theApp.SLog(TAG, "ListPicturePathSize: " + theApp.getListPicturePath().size());

			// 加载图片
			loadPicture(theApp.getListPicturePath(), file.listFiles());
			theApp.SLog(TAG, "ListPicturePathSize: " + theApp.getListPicturePath().size());

			try
			{
				// 检测扩展 SD 卡列表
				if (theApp.getListExtStorage() != null && theApp.getListExtStorage().size() > 0)
				{
					// 数量
					int nSize = theApp.getListExtStorage().size();

					for (int i = 0; i < nSize; i++)
					{
						// 路径
						String strExtPath = theApp.getListExtStorage().get(i);
						if (strExtPath == null) continue;

						// 文件
						File fileExt = new File(strExtPath);

						// 检测是否为文件夹
						if (!fileExt.exists() ||
							!fileExt.isDirectory() ||
							fileExt.listFiles() == null ||
							fileExt.listFiles().length <= 0) continue;

						// 加载视频
						Log.d(TAG, "loadPicture - " + strExtPath);
						theApp.TLog(TAG, "loadPicture - " + strExtPath);
						theApp.SLog(TAG, "loadPicture - " + strExtPath);
						loadPicture(theApp.getListPicturePath(), fileExt.listFiles());
					}

					theApp.SLog(TAG, "ListPicturePathSize: " + theApp.getListPicturePath().size());
				}
			}
			catch (Exception e)
			{
				Log.d(TAG, e.toString());
				theApp.TLog(TAG, e.toString());
				theApp.SLog(TAG, e.toString());
			}

			// 排序
			Collections.sort(theApp.getListPicturePath(), String.CASE_INSENSITIVE_ORDER);

			// 数量
			int nSize = theApp.getListPicturePath().size();
			if (nSize < 1) return;

			// 已加载图片
			theApp.setLoadPicture(true);
			Log.d(TAG, "SetLoadPicture: " + String.valueOf(theApp.isLoadPicture()));
			theApp.TLog(TAG, "SetLoadPicture: " + String.valueOf(theApp.isLoadPicture()));
			theApp.SLog(TAG, "SetLoadPicture: " + String.valueOf(theApp.isLoadPicture()));
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 加载图片
	 * @param listPath
	 * @param files
	 */
	private void loadPicture(List<String> listPath, File[] files)
	{
		try
		{
			if (listPath == null) return;
			if (files == null || files.length <= 0) return;

			for (int i = 0; i < files.length; i++)
			{
				// 文件
				File file = files[i];

				// 检测文件
				if (!file.exists()) continue;

				// 文件
				if (file.isFile())
				{
					// 获取文件路径
					String strPath = file.getPath();

					// 忽略以下文件夹
					int nPos1 = strPath.indexOf("TB-TECH-HMI");
					int nPos2 = strPath.indexOf("thumbnails");
					int nPos3 = strPath.indexOf(".thumbnails");
					if (nPos1 > -1 && nPos1 < strPath.length()) continue;
					if (nPos2 > -1 && nPos2 < strPath.length()) continue;
					if (nPos3 > -1 && nPos3 < strPath.length()) continue;

					// 获取扩展名
					String strExt = getFileExt(strPath);

					// 扩展名一致
					if (strExt.equals(".jpg") ||
						strExt.equals(".png") ||
						strExt.equals(".gif") ||
						strExt.equals(".bmp"))
					{
						// 添加路径
						if (!theApp.checkList(listPath, strPath)) listPath.add(strPath);
					}
				}

				// 文件夹
				if (file.isDirectory())
				{
					// 加载图片, 递归调用
					loadPicture(listPath, file.listFiles());
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 检测 picture 文件夹
	 */
	private void checkPicture()
	{
		try
		{
			// 宽度和高度
			int nW  = 1;
			int nH  = 1;
			int nW0 = theApp.getW00Jpg();
			int nH0 = theApp.getH00Jpg();
			int nWP = theApp.getWidthPixels();
			int nHP = theApp.getHeightPixels();
			if (nW0 < 1 || nH0 < 1) get00Jpg();
			nW = Math.max(nWP, nHP);
			nH = Math.min(nWP, nHP);

			//设置缩放的分辨率
			if (theApp.SCALED_XY)
			{
				nWP = 1280;
				nHP = 720;
			}

			// 分辨率
			String strResolution = "_" + String.valueOf(nW) + "_" + String.valueOf(nH);

			// picture 文件夹路径
			String strPath = theApp.getDefPath() + "picture" + strResolution + "/";

			// 文件
			File file = new File(strPath);

			// 比例
			if (nW0 < 1 || nH0 < 1) return;
			m_scaleW = (double)nWP / nW0;
			m_scaleH = (double)nHP / nH0;

			if (theApp.SCALED_XY)
			{
				m_scaleW = (float) nW0 / nWP ;
				m_scaleH = (float) nH0 / nHP ;
			}

			// 宽度和高度比例
			if (theApp.getWidthRatio() == 1.0f) theApp.setWidthRatio((float)m_scaleW);
			if (theApp.getHeightRatio() == 1.0f) theApp.setHeightRatio((float)m_scaleH);

			// 检测文件夹
			if (file.exists() &&
				file.isDirectory() &&
				file.listFiles() != null &&
				file.listFiles().length > 0)
			{
				// 检测 00.jpg 底图
				if (check00Jpg(strPath))
				{
					// picture 文件夹路径
					theApp.setPicturePath(strPath);
					return;
				}
			}

			// 文件夹不存在
			if (!file.exists() || !file.isDirectory())
			{
				try
				{
					// 文件夹
					String strSrcFolder = theApp.getDefPath() + "picture/";
					String strTgtFolder = strPath;

					// 拷贝图片文件夹
					copyPictureFolder_(strSrcFolder, strTgtFolder);
					System.out.println("JPG number: " + m_nScaleJpg);
					System.out.println("PNG number: " + m_nScalePng);

					// picture 文件夹路径
					theApp.setPicturePath(strPath);
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

	/**
	 * 拷贝图片文件夹
	 * @param strSrcFolder 源文件夹
	 * @param strTgtFolder 目标文件夹
	 */
	private void copyPictureFolder_(final String strSrcFolder, final String strTgtFolder)
	{
		try
		{
			// 请耐心等待...
			String strWait = getString(R.string.strWait);
			if (m_progDlg != null) m_progDlg.dismiss();
			m_progDlg = ProgressDialog.show(this, "", strWait + "...");

			// 启动线程
			new Thread()
			{
				public void run()
				{
					try
					{
						// 拷贝文件夹
						String str = copyPictureFolder(strSrcFolder, strTgtFolder);
						System.out.println("CopyFolder: " + str);

						// 加载 00.jpg 底图
						load00Jpg();
					}
					catch (Exception e)
					{
					}
					finally
					{
						// 关闭进度对话框
						if (m_progDlg != null) m_progDlg.dismiss();
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 拷贝图片文件夹
	 * @param strSrcFolder 源文件夹
	 * @param strTgtFolder 目标文件夹
	 * @return
	 */
	private String copyPictureFolder(final String strSrcFolder, final String strTgtFolder)
	{
		String str = null;

		try
		{
			// 生成路径
			theApp.mkdir(strTgtFolder);

			// 源文件
			File fileSrc = new File(strSrcFolder);

			// 检测是否为文件夹
			if (!fileSrc.exists() ||
				!fileSrc.isDirectory() ||
				fileSrc.listFiles() == null ||
				fileSrc.listFiles().length <= 0)
			{
				String strError = "Directory error!";
				return strError;
			}

			// 源文件列表
			File[] filesSrc = fileSrc.listFiles();

			for (int i = 0; i < filesSrc.length; i++)
			{
				// 文件
				fileSrc = filesSrc[i];

				// 文件名
				String strName = fileSrc.getName();

				// 路径分隔符
				String strSeparator = File.separator;

				// 文件
				if (fileSrc.isFile())
				{
					// 目标文件
					String strTgtFile = "";

					if (strTgtFolder.endsWith(strSeparator))
					{
						strTgtFile = strTgtFolder + strName;
					}
					else
					{
						strTgtFile = strTgtFolder + strSeparator + strName;
					}

					// 目标文件
					File fileTgt = new File(strTgtFile);

					// 扩展名
					String strExt = getFileExt(strTgtFile);
					if (!strExt.equals(".jpg") && !strExt.equals(".png")) continue;

					// 拷贝图片文件
					String strCopy = copyPictureFile(fileSrc, fileTgt);

					if (strCopy != null)
					{
						return strCopy;
					}
				}

				// 文件夹
				if (fileSrc.isDirectory())
				{
					// 源文件夹
					String strSrcFile = "";

					if (strSrcFolder.endsWith(strSeparator))
					{
						strSrcFile = strSrcFolder + strName;
					}
					else
					{
						strSrcFile = strSrcFolder + strSeparator + strName;
					}

					// 目标文件夹
					String strTgtFile = "";

					if (strTgtFolder.endsWith(strSeparator))
					{
						strTgtFile = strTgtFolder + strName;
					}
					else
					{
						strTgtFile = strTgtFolder + strSeparator + strName;
					}

					// 拷贝图片文件夹
					if (copyPictureFolder(strSrcFile, strTgtFile) != null)
					{
						String strError = "Copy folder error!";
						return strError;
					}
				}
			}
		}
		catch (Exception e)
		{
			return e.toString();
		}

		return str;
	}

	/**
	 * 拷贝图片文件
	 * @param fileSrc 源文件
	 * @param fileTgt 目标文件
	 */
	private String copyPictureFile(File fileSrc, File fileTgt)
	{
		String str = null;

		if (fileSrc == null)
		{
			str = "The source file is null.";
			return str;
		}

		if (fileTgt == null)
		{
			str = "The target file is null.";
			return str;
		}

		if (!fileSrc.exists())
		{
			str = fileSrc.getName() + " does not exist.";
			return str;
		}

		if (!fileSrc.isFile())
		{
			str = fileSrc.getName() + " is not a file.";
			return str;
		}

		if (!fileSrc.canRead())
		{
			str = fileSrc.getName() + " could not be read.";
			return str;
		}

		try
		{
			if (!fileTgt.getParentFile().exists())
			{
				fileTgt.getParentFile().mkdirs();
			}
		}
		catch (Exception ex)
		{
			return fileTgt.getName() +  " make directory error.";
		}

		try
		{
			if (fileTgt.exists())
			{
				fileTgt.delete();
			}
		}
		catch (Exception ex)
		{
			return fileTgt.getName() +  " delete file error.";
		}

		if (fileTgt.exists() && !fileTgt.canWrite())
		{
			str = fileTgt.getName() + " could not be write.";
			return str;
		}

		try
		{
			// 扩展名
			String strExt = getFileExt(fileSrc.getPath());

			if (!strExt.equals(".jpg") && !strExt.equals(".png"))
			{
				return fileSrc.getPath() + " is not a jpg or png file.";
			}

			// jpg
			if (strExt.equals(".jpg"))
			{
				// 缩放 JPG
				if (scaleJpg(fileSrc.getPath(), fileTgt.getPath(), m_scaleW, m_scaleH))
				{
					m_nScaleJpg++;
//					System.out.println("JPG number: " + m_nScaleJpg);
				}
				else
				{
					return fileSrc.getPath() + " scale error.";
				}
			}

			// png
			if (strExt.equals(".png"))
			{
				// 缩放 PNG
				if (scalePng(fileSrc.getPath(), fileTgt.getPath(), m_scaleW, m_scaleH))
				{
					m_nScalePng++;
//					System.out.println("PNG number: " + m_nScalePng);
				}
				else
				{
					return fileSrc.getPath() + " scale error.";
				}
			}
		}
		catch (Exception e)
		{
			return e.toString();
		}

		return str;
	}

	/**
	 * 缩放 JPG
	 * @param strSrcFile 源文件
	 * @param strTgtFile 目标文件
	 * @param scaleW 宽度缩放比例
	 * @param scaleH 高度缩放比例
	 * @return
	 */
	private boolean scaleJpg(final String strSrcFile, final String strTgtFile, final double scaleW, final double scaleH)
	{
		try
		{
			if (scaleW < 0.0 || scaleH < 0.0) return false;
			if (strSrcFile == null  || strTgtFile == null) return false;

			// 源文件
			File fileSrc = new File(strSrcFile);

			// 检测文件
			if (!fileSrc.exists() || !fileSrc.isFile() || fileSrc.length() < 1) return false;

			// 解码图像
			Bitmap bmpSrc = theApp.decodeBitmap(strSrcFile);

			// 矩阵
			Matrix matrix = new Matrix();
			matrix.postScale((float)scaleW, (float)scaleH);

			// 宽度和高度
			int nSrcW = bmpSrc.getWidth();
			int nSrcH = bmpSrc.getHeight();

			// 目标文件
			File fileTgt = new File(strTgtFile);

			// 写目标图像
			OutputStream os = new FileOutputStream(fileTgt);

			// 目标图像
			Bitmap bmpTgt = Bitmap.createBitmap(bmpSrc, 0, 0, nSrcW, nSrcH, matrix, false);

			// 压缩
			bmpTgt.compress(Bitmap.CompressFormat.JPEG, 38, os);
//			bmpTgt.compress(Bitmap.CompressFormat.JPEG, 100, os);

			// 关闭输出流
			if (os != null) os.flush();
			if (os != null) os.close();

			// 回收图像
			bmpSrc.recycle();
			bmpTgt.recycle();
			bmpSrc = null;
			bmpTgt = null;

			// 垃圾回收
			System.runFinalization();
			return true;
		}
		catch (Exception e)
		{
		}

		return false;
	}

	/**
	 * 缩放 PNG
	 * @param strSrcFile 源文件
	 * @param strTgtFile 目标文件
	 * @param scaleW 宽度缩放比例
	 * @param scaleH 高度缩放比例
	 * @return
	 */
	private boolean scalePng(final String strSrcFile, final String strTgtFile, final double scaleW, final double scaleH)
	{
		try
		{
			if (scaleW < 0.0 || scaleH < 0.0) return false;
			if (strSrcFile == null  || strTgtFile == null) return false;

			// 源文件
			File fileSrc = new File(strSrcFile);

			// 检测文件
			if (!fileSrc.exists() || !fileSrc.isFile() || fileSrc.length() < 1) return false;

			// 解码图像
			Bitmap bmpSrc = theApp.decodeBitmap(strSrcFile);

			// 矩阵
			Matrix matrix = new Matrix();
			matrix.postScale((float)scaleW, (float)scaleH);

			// 宽度和高度
			int nSrcW = bmpSrc.getWidth();
			int nSrcH = bmpSrc.getHeight();

			// 目标文件
			File fileTgt = new File(strTgtFile);

			// 写目标图像
			OutputStream os = new FileOutputStream(fileTgt);

			// 目标图像
			Bitmap bmpTgt = Bitmap.createBitmap(bmpSrc, 0, 0, nSrcW, nSrcH, matrix, false);

			// 压缩
			bmpTgt.compress(Bitmap.CompressFormat.PNG, 0, os);

			// 关闭输出流
			if (os != null) os.flush();
			if (os != null) os.close();

			// 回收图像
			bmpSrc.recycle();
			bmpTgt.recycle();
			bmpSrc = null;
			bmpTgt = null;

			// 垃圾回收
			System.runFinalization();
			return true;
		}
		catch (Exception e)
		{
		}

		return false;
	}

	/**
	 * 重新加载音乐, 视频和图片
	 */
	private void reloadMVP()
	{
		try
		{
			// 已加载音乐
			if (theApp.isLoadMusic())
			{
				// 正在播放
				if (m_layMusic != null && m_layMusic.isPlaying())
				{
					// 停止
					m_layMusic.stop();
					m_layMusic.setVisibility(View.INVISIBLE);
				}

				if (m_layMusic != null) m_layMusic.setVisibility(View.INVISIBLE);

				// 加载音乐
				loadMusic();
			}

			// 已加载视频
			if (theApp.isLoadVideo())
			{
				// 正在播放
				if (m_layVideo != null && m_layVideo.isPlaying())
				{
					// 停止
					m_layVideo.stop();
					if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);
				}

				if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);

				// 加载视频
				loadVideo();
			}

			// 已加载图片
			if (theApp.isLoadPicture())
			{
				// 正在播放
				if (m_layPictureGrid != null && m_layPictureGrid.isPlaying())
				{
					// 停止
					m_layPictureGrid.stop();
					if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);
				}

				if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);

				// 加载图片
				loadPicture();
			}
		}
		catch (Exception e)
		{
		}
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * 初始化 Apps
	 */
	private void initApps()
	{
		try
		{
			// 使用视图页指示器
			if (theApp.USE_VPI)
			{
				// Apps 布局
				if (m_layAppsVpi != null) m_layAppsVpi = null;
				m_layAppsVpi = new AppsVpiLayout(this);

				// 设置 Activity
				m_layAppsVpi.setActivity(this);

				// Apps 列表
				m_layAppsVpi.setListApps(theApp.getListApps());

//				theApp.MYSLog(TAG,"1111"+theApp.getListApps());

				// 初始化视图页指示器
				m_layAppsVpi.initVPI();

				// 显示 Apps
				if (m_layAppsVpi != null) m_layAppsVpi.setVisibility(View.VISIBLE);

				// 视图页
				ViewPager vp = (ViewPager)findViewById(R.id.vpApps);
				if (vp != null) vp.setVisibility(View.VISIBLE);

				// 视图页指示器
				ViewPagerIndicator vpi = (ViewPagerIndicator)findViewById(R.id.vpiApps);
				if (vpi != null) vpi.setVisibility(View.VISIBLE);

				// 容器布局
/*				if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

				// 宽度和高度
				int nW = m_layContent.getWidth();
				int nH = m_layContent.getHeight();

				// 移除 Apps 布局
				if (m_layAppsVpi != null) m_layContent.removeView(m_layAppsVpi);

				// Apps 布局
				if (m_layAppsVpi != null) m_layAppsVpi = null;
				m_layAppsVpi = new AppsVpiLayout(this);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

				if (params.width < 1 || params.height < 1)
				{
					params = new RelativeLayout.LayoutParams(nW, nH);
				}

				// 添加 Apps 布局
				m_layContent.addView(m_layAppsVpi, params);

				// 设置 Activity
				m_layAppsVpi.setActivity(this);

				// Apps 列表
				m_layAppsVpi.setListApps(theApp.getListApps());

				// 初始化视图页指示器
				m_layAppsVpi.initVPI();*/
			}
			// 不使用视图页指示器
			else
			{
				// 容器布局
				if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

				// 宽度和高度
				int nW = m_layContent.getWidth();
				int nH = m_layContent.getHeight();

				// 移除 Apps 布局
				if (m_layAppsGrid != null) m_layContent.removeView(m_layAppsGrid);

				// Apps 布局
				if (m_layAppsGrid != null) m_layAppsGrid = null;
				m_layAppsGrid = new AppsGridLayout(this);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

				if (params.width < 1 || params.height < 1)
				{
					params = new RelativeLayout.LayoutParams(nW, nH);
				}

				// 添加 Apps 布局
				m_layContent.addView(m_layAppsGrid, params);

				// Apps 列表
				m_layAppsGrid.setListApps(theApp.getListApps());

				// 初始化网格视图
				m_layAppsGrid.initGridView();
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化音乐
	 */
	private void initMusic()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除音乐布局
			if (m_layMusic != null) m_layContent.removeView(m_layMusic);

			// 音乐布局
			if (m_layMusic != null) m_layMusic = null;
			m_layMusic = new MusicLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加音乐布局
			m_layContent.addView(m_layMusic, params);

			// 是否开启音效
//			m_layMusic.setVolume(theApp.isVolume());

			// 音乐路径
			m_layMusic.setListPath(theApp.getListMusicPath());

			// 图像加载器
			if (m_imageLoader == null) m_imageLoader = ImageLoader.getInstance();
			m_layMusic.setImageLoader(m_imageLoader);

			// 初始化列表视图
			m_layMusic.initListView();

			// 应答 E0
			theApp.responseE0();

			// 已加载音乐
			theApp.setLoadingMusic(false);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化视频
	 */
	private void initVideo()
	{
		try
		{
			//清除视频布局
			clearLayoutDaVideo();

			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除视频布局
			if (m_layVideo != null) m_layContent.removeView(m_layVideo);

			// 视频布局
			if (m_layVideo != null) m_layVideo = null;
			m_layVideo = new VideoLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加视频布局
			m_layContent.addView(m_layVideo, params);

			// 视频路径
			m_layVideo.setListPath(theApp.getListVideoPath());

			// 图像加载器
			if (m_imageLoader == null) m_imageLoader = ImageLoader.getInstance();
			m_layVideo.setImageLoader(m_imageLoader);

			// 初始化列表视图
			m_layVideo.initListView();

			// 应答 E0
			theApp.responseE0();

			// 已加载视频
			theApp.setLoadingVideo(false);
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 初始化DA视频
	 */
	public void initDaVideo()
	{
		try
		{
			VideoViewEx view;
			view = new VideoViewEx(m_context);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            view.setLayoutParams(params);
            view.setLooping(true);
            theApp.setVideoViewMy(view);

			// 视频布局
			m_layVideoMain = (RelativeLayout)findViewById(R.id.layVideoMain);

			m_layVideoMain.addView(view);

			theApp.setVideoLayout(m_layVideoMain);

			theApp.setDaInitstatus(true);
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 初始化配置
	 */
	private void initSetup()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除配置布局
			if (m_laySetup != null) m_layContent.removeView(m_laySetup);

			// 配置布局
			if (m_laySetup != null) m_laySetup = null;
			m_laySetup = new SetupLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加配置布局
			m_layContent.addView(m_laySetup, params);

			// Activity
			m_laySetup.setActivity(this);

			// 初始化配置
			m_laySetup.initSetup();

			// 启用自定义 WiFi
			if (theApp.USE_WIFI)
			{
				// 初始化 WiFi
				initWiFi();
			}

			// 应答 E0
			theApp.responseE0();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化 WiFi
	 */
	public void initWiFi()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除 WiFi 布局
			if (m_layWiFi != null) m_layContent.removeView(m_layWiFi);

			// WiFi 布局
			if (m_layWiFi != null) m_layWiFi.exit();
			if (m_layWiFi != null) m_layWiFi = null;
			m_layWiFi = new WiFiLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加 WiFi 布局
			m_layContent.addView(m_layWiFi, params);

			// 隐藏 WiFi 布局
			m_layWiFi.setVisibility(View.INVISIBLE);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化图片
	 */
	@SuppressWarnings("deprecation")
	private void initPicture()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);
			m_layContent.setVisibility(View.VISIBLE);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除图片布局
			if (m_layPictureGrid != null) m_layContent.removeView(m_layPictureGrid);

			// 图片布局
			if (m_layPictureGrid != null) m_layPictureGrid = null;
			m_layPictureGrid = new PictureGridLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加图片布局
			m_layContent.addView(m_layPictureGrid, params);

			// 图片路径
			m_layPictureGrid.setListPath(theApp.getListPicturePath());

			// 图像加载器
			if (m_imageLoader == null) m_imageLoader = ImageLoader.getInstance();
			m_layPictureGrid.setImageLoader(m_imageLoader);

			// 初始化网格视图
			m_layPictureGrid.initGridView();

			// 应答 E0
			theApp.responseE0();

			// 已加载图片
			theApp.setLoadingPicture(false);

			// 容器布局
/*			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);
			m_layContent.setVisibility(View.VISIBLE);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除图片布局
			if (m_layPictureList != null) m_layContent.removeView(m_layPictureList);

			// 图片布局
			if (m_layPictureList != null) m_layPictureList = null;
			m_layPictureList = new PictureListLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加图片布局
			m_layContent.addView(m_layPictureList, params);

			// 图片路径
			m_layPictureList.setListPath(theApp.getListPicturePath());

			// 图像加载器
//			if (m_imageLoader == null) m_imageLoader = ImageLoader.getInstance();
//			m_layPictureList.setImageLoader(m_imageLoader);

			// 初始化列表视图
			m_layPictureList.initListView();

			// 应答 E0
			theApp.responseE0();

			// 已加载图片
			theApp.setLoadingPicture(false);*/
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化浏览器
	 */
	private void initBrowser()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);
//			if (theApp.DEBUG) m_layContent.setBackgroundColor(Color.GREEN);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除浏览器布局
			if (m_layBrowser != null) m_layContent.removeView(m_layBrowser);

			// 浏览器布局
			if (m_layBrowser != null) m_layBrowser = null;
			m_layBrowser = new BrowserLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加浏览器布局
			m_layContent.addView(m_layBrowser, params);

			// 获取路径
			String strDefPath = theApp.getDefPath();

			// 收藏标签配置文件和缩略图存放的位置
			String strFavorite = strDefPath + "favorite";
			theApp.mkdir(strFavorite);

			// 收藏夹目录
			m_layBrowser.setFavoriteConfigPath(strFavorite);

			// 初始化浏览器
			m_layBrowser.initBrowser();

			// 初始化浏览常用标签
			initBrowserNewsWebPageLabels(m_layBrowser);

			// 初始化浏览购物标签
			initBrowserShoppingWebPageLabels(m_layBrowser);

			// 显示常用标签网格表
			m_layBrowser.doBrowserNews();

			// 已初始化浏览器
			theApp.setInitBrowser(true);

			// 应答 E0
			theApp.responseE0();
/*
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);
//			if (theApp.DEBUG) m_layContent.setBackgroundColor(Color.GREEN);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 移除浏览器布局
			if (m_layBrowserOld != null) m_layContent.removeView(m_layBrowserOld);

			// 浏览器布局
			if (m_layBrowserOld != null) m_layBrowserOld = null;
			m_layBrowserOld = new BrowserLayoutOld(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加浏览器布局
			m_layContent.addView(m_layBrowserOld, params);

			// 获取路径
			String strDefPath = theApp.getDefPath();

			// 收藏标签配置文件和缩略图存放的位置
			String strFavorite = strDefPath + "favorite";
			theApp.mkdir(strFavorite);

			// 收藏夹目录
			m_layBrowserOld.setFavoriteConfigPath(strFavorite);

			// 初始化浏览器
			m_layBrowserOld.initBrowser();

			// 初始化浏览常用标签
			initBrowserNewsWebPageLabels(m_layBrowserOld);

			// 初始化浏览购物标签
			initBrowserShoppingWebPageLabels(m_layBrowserOld);

			// 显示常用标签网格表
			m_layBrowserOld.doBrowserNews();

			// 已初始化浏览器
			theApp.setInitBrowser(true);

			// 应答 E0
			theApp.responseE0();*/
		}
		catch (Exception e)
		{
		}
	}

	/** xwx
	 * 初始化浏览器的常用标签
	 * @param layoutBrowser 浏览器布局
	 */
	private void initBrowserNewsWebPageLabels(final BrowserLayout layoutBrowser)
//	private void initBrowserNewsWebPageLabels(final BrowserLayoutOld layoutBrowser)
	{
		// 判断浏览器模块是否为空
		if (null != layoutBrowser)
		{
			// 开启线程加载常用标签到界面
			new Thread()//xwx
			{
				public void run()
				{
					// 从内部资源中加载标签信息
					final List<WebPageLabel> listLabels = new ArrayList<WebPageLabel>();

					try
					{
						WebPageLabel value = null;
						if (theApp.getLang().equals("ch"))
						{
							if (theApp.WNQ)
							{
								value = layoutBrowser.createEmptyWebPageLabel();
								value.setStrTitle("万年青");
								value.setStrUrl("http://www.wnq.com/");
								value.setDaImage(m_context.getResources()
										.getDrawable(R.drawable.news_wnq));
								listLabels.add(value);
							}
							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("搜狐");
							value.setStrUrl("http://www.sohu.com/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_sohu));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("新浪");
							value.setStrUrl("http://www.sina.com.cn/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_sina));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("腾讯");
							value.setStrUrl("http://www.qq.com/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_qq));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("百度");;
							value.setStrUrl("http://www.baidu.com");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_baidu));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("网易");
							value.setStrUrl("http://www.163.com/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_163));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("凤凰网");
							value.setStrUrl("http://www.ifeng.com/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_ifeng));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("淘宝");
							value.setStrUrl("http://www.taobao.com/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_taobao));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("谷歌");
							value.setStrUrl("http://www.google.com.hk/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_google));
							listLabels.add(value);

						}
						else
						{
							if (theApp.WNQ)
							{
								value = layoutBrowser.createEmptyWebPageLabel();
								value.setStrTitle("wnq");
								value.setStrUrl("http://www.wnq.com/");
								value.setDaImage(m_context.getResources()
										.getDrawable(R.drawable.news_wnq));
								listLabels.add(value);
							}

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("新浪");
							value.setStrUrl("http://www.sina.com.cn/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_sina));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("腾讯");
							value.setStrUrl("http://www.qq.com/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_qq));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("百度");;
							value.setStrUrl("http://www.baidu.com");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_baidu));
							listLabels.add(value);

							value = layoutBrowser.createEmptyWebPageLabel();
							value.setStrTitle("谷歌");
							value.setStrUrl("http://www.google.com.hk/");
							value.setDaImage(m_context.getResources()
									.getDrawable(R.drawable.news_google));
							listLabels.add(value);

						}
					}
					catch (Exception ex)
					{
						android.util.Log.e("error", "get_url_label:" + ex.toString());
					}

					// 加载完成, 调用线程消息发送器访问 UI
					m_handler.post(new Runnable()
					{
						public void run()
						{
							// 添加标签加载到网格表适配器
							layoutBrowser.setNewsWebPageLabels(listLabels);
						}
					});
				}
			}.start();
		}
	}

	/**
	 * 初始化浏览器的购物标签
	 * @param layoutBrowser 浏览器模块
	 */
	private void initBrowserShoppingWebPageLabels(final BrowserLayout layoutBrowser)
//	private void initBrowserShoppingWebPageLabels(final BrowserLayoutOld layoutBrowser)
	{
		// 判断浏览器布局是否为空
		if (null != layoutBrowser)
		{
			// 开启线程加载购物标签到界面
			new Thread()
			{
				public void run()
				{
					// 从内部资源中加载标签信息
					final List<WebPageLabel> listLabels = new ArrayList<WebPageLabel>();

					try
					{
						WebPageLabel value = null;

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("PPS");
						value.setStrUrl("http://www.pps.tv/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_pps));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("腾讯");
						value.setStrUrl("http://v.qq.com/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_qqvedio));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("奇艺");
						value.setStrUrl("http://www.iqiyi.com/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_qiyi));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("优酷");
						value.setStrUrl("http://www.youku.com/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_youku));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("乐视");
						value.setStrUrl("http://www.letv.com/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_letv));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("土豆");
						value.setStrUrl("http://movie.tudou.com/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_tudou));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("酷狗音乐");
						value.setStrUrl("http://www.kugou.com/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_kugou));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("酷我音乐");
						value.setStrUrl("http://www.kuwo.cn/");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_kuwo));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("QQ音乐");
						value.setStrUrl("http://y.qq.com/#type=index");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_qqmusic));
						listLabels.add(value);

						value = layoutBrowser.createEmptyWebPageLabel();
						value.setStrTitle("百度音乐");
						value.setStrUrl("http://music.baidu.com/pc/index.html");
						value.setDaImage(m_context.getResources()
								.getDrawable(R.drawable.shopping_baidu));
						listLabels.add(value);

					}
					catch (Exception ex)
					{
						android.util.Log.e("error", "get_url_label:" + ex.toString());
					}

					// 加载完成, 调用线程消息发送器访问 UI
					m_handler.post(new Runnable()
					{
						public void run()
						{
							// 添加标签加载到网格表适配器
							layoutBrowser.setShoppingWebPageLabels(listLabels);
						}
					});
				}
			}.start();
		}
	}

	/**
	 * 清除布局
	 */
	public void clearAppLayout()
	{
		try
		{
			// 未加载 Apps
			theApp.setLoadApps(false);

			// 使用视图页指示器
			if (theApp.USE_VPI)
			{
				// 隐藏 Apps
				if (m_layAppsVpi != null) m_layAppsVpi.setVisibility(View.INVISIBLE);

				// 视图页
				ViewPager vp = (ViewPager)findViewById(R.id.vpApps);
				if (vp != null) vp.setVisibility(View.INVISIBLE);

				// 视图页指示器
				ViewPagerIndicator vpi = (ViewPagerIndicator)findViewById(R.id.vpiApps);
				if (vpi != null) vpi.setVisibility(View.INVISIBLE);

				// 移除 Apps 布局
//				if (m_layAppsVpi != null) m_layContent.removeView(m_layAppsVpi);
				if (m_layAppsVpi != null) m_layAppsVpi = null;
			}
			// 不使用视图页指示器
			else
			{
				// 移除 Apps 布局
				if (m_layAppsGrid != null) m_layContent.removeView(m_layAppsGrid);
				if (m_layAppsGrid != null) m_layAppsGrid = null;
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 清除布局
	 */
	public void clearLayout()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);
			if (m_layContent == null) return;

			// 移除 WiFi 布局
			if (m_layWiFi != null) m_layContent.removeView(m_layWiFi);
			if (m_layWiFi != null) m_layWiFi.exit();
			if (m_layWiFi != null) m_layWiFi = null;

			// 移除音乐布局
			theApp.setLoadMusic(false);
			if (m_layMusic != null) m_layContent.removeView(m_layMusic);
			if (m_layMusic != null) m_layMusic = null;

			// 移除视频布局
			theApp.setLoadVideo(false);
			if (m_layVideo != null) m_layContent.removeView(m_layVideo);
			if (m_layVideo != null) m_layVideo = null;

			// 移除配置布局
			if (m_laySetup != null) m_layContent.removeView(m_laySetup);
			if (m_laySetup != null) m_laySetup = null;

			// 移除浏览器布局
			if (m_layBrowser != null) m_layContent.removeView(m_layBrowser);
			if (m_layBrowser != null) m_layBrowser = null;

			// 移除图片布局
			theApp.setLoadPicture(false);
			if (m_layPictureGrid != null) m_layContent.removeView(m_layPictureGrid);
			if (m_layPictureGrid != null) m_layPictureGrid = null;

//            //移除地图布局
//            if (m_layoutMap != null) {
//                m_layContent.removeView(m_layoutMap);
//                m_layoutMap = null;
//            }
			// 未加载 Apps
			theApp.setLoadApps(false);

			// 使用视图页指示器
			if (theApp.USE_VPI)
			{
				// 隐藏 Apps
				if (m_layAppsVpi != null) m_layAppsVpi.setVisibility(View.INVISIBLE);

				// 视图页
				ViewPager vp = (ViewPager)findViewById(R.id.vpApps);
				if (vp != null) vp.setVisibility(View.INVISIBLE);

				// 视图页指示器
				ViewPagerIndicator vpi = (ViewPagerIndicator)findViewById(R.id.vpiApps);
				if (vpi != null) vpi.setVisibility(View.INVISIBLE);

				// 移除 Apps 布局
				if (m_layAppsVpi != null) m_layContent.removeView(m_layAppsVpi);
				if (m_layAppsVpi != null) m_layAppsVpi = null;
			}
			// 不使用视图页指示器
			else
			{
				// 移除 Apps 布局
				if (m_layAppsGrid != null) m_layContent.removeView(m_layAppsGrid);
				if (m_layAppsGrid != null) m_layAppsGrid = null;
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 清除视频布局
	 */
	public void clearLayoutVideo()
	{
		try
		{
			// 移除视频布局
			theApp.setLoadVideo(false);
			if (m_layVideo != null) m_layContent.removeView(m_layVideo);
			if (m_layVideo != null) m_layVideo = null;
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 清除DA视频布局
	 */
	public void clearLayoutDaVideo()
	{
		try
		{
			// 移除视频布局
			 if (theApp.getVideoLayout() != null)
			 {

                theApp.getVideoLayout().removeAllViews();

//                theApp.getVideoLayout().setVisibility(View.INVISIBLE);

                //设置视频播放未初始化
                theApp.setDaInitstatus(false);
			 }
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	/**
	 * 清除浏览器布局
	 */
	public void clearLayoutBrowse()
	{
		try
		{
			// 移除浏览器布局
			if (m_layBrowser != null) m_layContent.removeView(m_layBrowser);
			if (m_layBrowser != null) m_layBrowser = null;
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * 获取扩展名
	 * @param strName
	 * @return
	 */
	private String getFileExt(final String strName)
	{
		int nIndex = strName.lastIndexOf(".");

		if (nIndex < 0 || nIndex >= strName.length())
		{
			return "";
		}

		return strName.substring(nIndex).toLowerCase();
	}

	/**
	 * 检测文件是否为 UTF-8 编码
	 * @param file
	 * @return
	 */
	private boolean isUTF8(File file)
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
	 * 关闭缓冲
	 * @param br 缓冲
	 */
	private void br_close(BufferedReader br)
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

	/**
	 * 读文本行
	 * @param br 读缓冲器
	 * @return 字符串
	 */
	private String readLine(BufferedReader br)
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
		catch (Exception e)
		{
		}

		return str;
	}

	/**
	 * 读 "SETUP.txt" 文件
	 */
	private void readSetupFile()
	{
		// 文件名
		String strFileName = theApp.getDefPath() + "SETUP.txt";

		// 文件
		File file = new File(strFileName);

		// 检测文件
		if (!file.exists() || !file.isFile()) return;

		// 字符集编码
		String strCsEnc = "GBK";

		// 检测文件是否为 UTF-8 编码
		if (isUTF8(file))
		{
			strCsEnc = "UTF-8";
		}

		String str = "";
		String strLine = null;
		BufferedReader br = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;

		try
		{
			// 读文件
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, strCsEnc);
			br = new BufferedReader(isr);

			try
			{
				// 读文本行
				while ((strLine = readLine(br)) != null)
				{
					try
					{
						// 蓝牙地址
						if (strLine.startsWith("BtAdd="))
						{
							str = strLine;

							// 查找等号
							int nPos = str.indexOf("=") + 1;

							if (nPos > -1 && nPos < str.length())
							{
								// 蓝牙地址
								str = str.substring(nPos);
								str = str.toUpperCase();
								theApp.setBtAdd(str);
							}
						}
					}
					catch (Exception e)
					{
					}

					try
					{
						// 波特率
						if (strLine.startsWith("Baudrate="))
						{
							str = strLine;

							// 查找等号
							int nPos = str.indexOf("=") + 1;

							if (nPos > -1 && nPos < str.length())
							{
								// 波特率
								str = str.substring(nPos);
								int nBaudrate = Integer.valueOf(str);
								theApp.setBaudrate(nBaudrate);
							}
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
			finally
			{
				// 检测蓝牙地址
				if (theApp.getBtAdd().equals(""))
				{
					// 不使用蓝牙
					theApp.setUseBt(false);
				}

				// 关闭缓冲
				br_close(br);
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 检测扩展存储是否可用
	 * @return
	 */
	private boolean isExteranlStorageAvailable()
	{
    	boolean bAvailable = false;

    	try
		{
    		bAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		}
		catch (Exception e)
		{
		}

    	return bAvailable;
	}

	/**
	 * 获取日志目录
	 * @return
	 */
	private String getLogDir()
	{
		try
		{
			// 获取 SDCard 路径
			String strSDCard = getSDCardPath();

			// 获取包名
			String strPkgName = getPackageName();

			// 日志目录
			String strLogDir = strSDCard + "/Android/data/" + strPkgName;
			return strLogDir;
		}
		catch (Exception e)
		{
		}

		return null;
	}

	/**
	 * 获取 SDCard 路径
	 * @return
	 */
	private String getSDCardPath()
	{
		String str = "/mnt/external_sd";

    	try
		{
    		// 检测扩展存储是否可用
    		if (!isExteranlStorageAvailable())
    		{
    			return str;
    		}
    		else
    		{
    			// 获取 SD 卡的路径
    			str = Environment.getExternalStorageDirectory().getPath();
    		}
		}
		catch (Exception e)
		{
			return "/mnt/external_sd";
		}

    	str = "/mnt/external_sd";
    	return str;
	}

	/**
	 * 获取默认路径
	 * @return
	 */
	private String getDefPath()
	{
		boolean b = false;
		String str = "TB-TECH-HMI";
		String strSDCard = "/system";//"/mnt/internal_sd";//getSDCardPath();
		String strFile = strSDCard + "/" + str + "/";

		// 获取包名
		String strPkg = getPackageName();

//		theApp.mkdir(strFile);

		if (theApp.USE_ZIP)
		{
			// 目录
//			strFile = strSDCard +  "/"  + "Android" +  "/"  + "data" +  "/"  + strPkg + "/" + str + "/";
			strFile =  "/"  + "system" +  "/"  + str + "/";
		}
		// 文件夹
		File file = new File(strSDCard);
		if (!file.exists() || !file.isDirectory()) return strFile;

		// 文件夹
		file = new File(strFile);
		if (file.exists() && file.isDirectory()) return strFile;

		try
		{
			// 文件列表
			File[] files = file.listFiles();
			int nLen = files.length;

			for (int i = 0; i < nLen; i++)
			{
				String strL = str.toLowerCase();
				String strU = str.toUpperCase();

				String strName = files[i].getName();

				if (strL.equals(strName))
				{
					b = true;
					str = strL;
					break;
				}

				if (strU.equals(strName))
				{
					b = true;
					str = strU;
					break;
				}
			}

			if (!b) return strFile;
		}
		catch (Exception e)
		{
		}

		if (theApp.USE_ZIP)
//			return strSDCard +  "/"  + "Android" +  "/"  + "data" +  "/"  + strPkg + "/" + str + "/";
			return  "/"  + "system" +  "/" + str + "/";
		else
			return strSDCard + "/" + str + "/";
	}

	/**
	 * 获取默认路径
	 * @return
	 */
	private String getmovieDefPath()
	{
		boolean b = false;
		String str = "TB-TECH-HMI";
		String strSDCard = "/mnt/internal_sd";//getSDCardPath();

		if (theApp.MOVIE_IN_CARD) strSDCard = "/mnt/usb_storage2";//"/mnt/external_sd";

		String strFile = strSDCard + "/" + str + "/";

		// 获取包名
		String strPkg = getPackageName();

//		theApp.mkdir(strFile);

		// 文件夹
		File file = new File(strSDCard);
		if (!file.exists() || !file.isDirectory()) return strFile;

		// 文件夹
		file = new File(strFile);
		if (file.exists() && file.isDirectory()) return strFile;

		try
		{
			// 文件列表
			File[] files = file.listFiles();
			int nLen = files.length;

			for (int i = 0; i < nLen; i++)
			{
				String strL = str.toLowerCase();
				String strU = str.toUpperCase();

				String strName = files[i].getName();

				if (strL.equals(strName))
				{
					b = true;
					str = strL;
					break;
				}

				if (strU.equals(strName))
				{
					b = true;
					str = strU;
					break;
				}
			}

			if (!b) return strFile;
		}
		catch (Exception e)
		{
		}

		return strSDCard + "/" + str + "/";
	}

	/**
	 * 获取 SD 卡的总容量
	 * @param strPath
	 * @return
	 */
	private long getSDTotalSize(final String strPath)
	{
		try
		{
			// 检测是否为文件夹
			File file = new File(strPath);
			if (!file.exists() || !file.isDirectory()) return 0;

			// 文件系统状态
			android.os.StatFs statFs = new android.os.StatFs(file.getPath());

			// 获取 SDCard 上 Block 总数
			long lTotalBlocks = statFs.getBlockCount();

			// 获取 SDCard 上每个 Block 的 Size
			long lBlockSize = statFs.getBlockSize();

			// 获取可供程序使用的 Block 的数量
			long lAvailaBlock = statFs.getAvailableBlocks();

			// 获取剩下的所有 Block 的数量(包括预留的一般程序无法使用的块)
//			long lFreeBlock = statFs.getFreeBlocks();

			// 计算 SDCard 总容量大小
			long lSDTotalSize   = lTotalBlocks * lBlockSize;
			long lSDTotalSizeMB = lTotalBlocks * lBlockSize / 1024 / 1024;

			// 计算 SDCard 剩余大小 MB
			long lSDFreeSizeMB = lAvailaBlock * lBlockSize / 1024 / 1024;
			Log.d(TAG, String.format("TotalSize: %dMB, FreeSize: %dMB", lSDTotalSizeMB, lSDFreeSizeMB));

			return lSDTotalSize;
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}

		return 0;
	}

	/**
	 * 获取 SDCard 列表
	 * @return
	 */
	private File[] getStorageList()
	{
		try
		{
			File[] storageList = EnvironmentPlus.getStorageList();

			if (storageList == null)
			{
				theApp.TLog(TAG, "EnvironmentPlus.getStorageList() == null");
			}
			else if (storageList.length <= 0)
			{
				theApp.TLog(TAG, "EnvironmentPlus.getStorageList().length <= 0");
			}
			else
			{
				// 获取所有的 Storage 路径列表
				storageList = EnvironmentPlus.getExternalStorageList(this);

				if (storageList == null)
				{
					theApp.TLog(TAG, "storageList == null");
				}
				else if (storageList.length <= 0)
				{
					theApp.TLog(TAG, "storageList.length <= 0");
				}

				return storageList;
			}
		}
		catch (Exception e)
		{
		}

		return null;
	}

	/**
	 * 获取扩展 SDCard 列表
	 * @return
	 */
	private void getExtStorageList()
	{
		try
		{
			// 获取 SDCard 路径
			String strSDCard = getSDCardPath();

			// 文件列表
//			File[] files = new File("/storage/external_storage").listFiles();
			File[] files = new File("/mnt/external_sd").listFiles();

			if (files == null)
			{
//				Log.d(TAG, "(files == null)");
//				theApp.TLog(TAG, "(files == null)");
//				theApp.SLog(TAG, "(files == null)");
				files = new File("/mnt/usb_storage").listFiles();
				if (files == null)
				return;
			}

			int nLen = files.length;

			if (nLen <= 0)
			{
//				Log.d(TAG, "(nLen <= 0)");
//				theApp.TLog(TAG, "(nLen <= 0)");
//				theApp.SLog(TAG, "(nLen <= 0)");
				return;
			}

			// 扩展 SD 卡列表
			List<String> listExtStorage = new ArrayList<String>();

			for (int i = 0; i < nLen; i++)
			{
				// 路径
				String strPath = files[i].getAbsolutePath();

				// 检测是否为文件夹
				File file = new File(strPath);
				if (!file.exists() || !file.isDirectory()) continue;

				// 检测是否为扩展 SD 卡
				if (!strPath.equalsIgnoreCase(strSDCard) && strPath.toLowerCase().contains("sd"))
				{
					// 获取 SD 卡的总容量
					long lSDTotalSize = getSDTotalSize(strPath);

					if (lSDTotalSize > 0)
					{
						// 添加路径
						listExtStorage.add(strPath);
//						Log.d(TAG, "listExtStorage.add - " + strPath);
//						theApp.TLog(TAG, "listExtStorage.add - " + strPath);
//						theApp.SLog(TAG, "listExtStorage.add - " + strPath);
					}
				}
			}

			// 第一次获取
			boolean bFirstGet = false;

			// 扩展 SD 卡列表
			if (theApp.getListExtStorage() == null) return;
			if (theApp.getListExtStorage().size() <= 0) bFirstGet = true;
//			theApp.SLog(TAG, String.format("ListExtStorageSize: %d", theApp.getListExtStorage().size()));

			// 第一次获取
			if (bFirstGet)
			{
				// 扩展 SD 卡列表
				theApp.copyStrList(listExtStorage, theApp.getListExtStorage());
				theApp.SLog(TAG, "FirstGet");
				return;
			}

			// 数量
			int nSize1 = listExtStorage.size();
			int nSize2 = theApp.getListExtStorage().size();

			if (nSize1 > 0 && nSize1 != nSize2)
			{
				Log.d(TAG, "(nSize1 > 0 && nSize1 != nSize2)");
				theApp.TLog(TAG, "(nSize1 > 0 && nSize1 != nSize2)");
				theApp.SLog(TAG, "(nSize1 > 0 && nSize1 != nSize2)");

				// 扩展 SD 卡列表
				theApp.copyStrList(listExtStorage, theApp.getListExtStorage());

				// 重置媒体
				resetMedia();

				// 设置媒体标记, 无
/*				theApp.setMediaMark(theApp.MARK_NONE);

				// 未加载音乐
				theApp.setLoadMusic(false);

				// 未加载视频
				theApp.setLoadVideo(false);

				// 未加载图片
				theApp.setLoadPicture(false);

				// 隐藏音乐
				theApp.setMusicMark(theApp.MARK_NONE);
				if (m_layMusic != null) m_layMusic.stop();
				if (m_layMusic != null) m_layMusic.setVisibility(View.INVISIBLE);

				// 隐藏视频
				if (m_layVideo != null) m_layVideo.stop();
				if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);

				// 隐藏图片
				if (m_layPictureGrid != null) m_layPictureGrid.stop();
				if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);*/
				return;
			}

			for (int i = 0; i < nSize1; i++)
			{
				String strPath1 = listExtStorage.get(i);
				String strPath2 = theApp.getListExtStorage().get(i);

				if (!strPath1.equals(strPath2))
				{
					Log.d(TAG, "(!strPath1.equals(strPath2)");
					theApp.TLog(TAG, "(!strPath1.equals(strPath2)");
					theApp.SLog(TAG, "(!strPath1.equals(strPath2)");

					// 扩展 SD 卡列表
					theApp.copyStrList(listExtStorage, theApp.getListExtStorage());

					// 重置媒体
					resetMedia();

					// 设置媒体标记, 无
/*					theApp.setMediaMark(theApp.MARK_NONE);

					// 未加载音乐
					theApp.setLoadMusic(false);

					// 未加载视频
					theApp.setLoadVideo(false);

					// 未加载图片
					theApp.setLoadPicture(false);

					// 隐藏音乐
					theApp.setMusicMark(theApp.MARK_NONE);
					if (m_layMusic != null) m_layMusic.stop();
					if (m_layMusic != null) m_layMusic.setVisibility(View.INVISIBLE);

					// 隐藏视频
					if (m_layVideo != null) m_layVideo.stop();
					if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);

					// 隐藏图片
					if (m_layPictureGrid != null) m_layPictureGrid.stop();
					if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);*/
					return;
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取扩展 SD 卡
	 * @return
	 */
	private String getExtSDCard()
	{
		try
		{
			// 获取 SDCard 路径
			String strSDCard = getSDCardPath();

			// 获取所有的 Storage 路径列表
			final File[] filesPaths = EnvironmentPlus.getExternalStorageList(this);

			if (filesPaths != null && filesPaths.length > 0)
			{
				int nLen = filesPaths.length;

				for (int i = 0; i < nLen; i++)
				{
					String strPath = filesPaths[i].getPath();
					Log.d(TAG, strPath);

					if (!strPath.equals(strSDCard))
					{
						theApp.TLog(TAG, "!strPath.equals(strSDCard): " + strPath);
						return strPath;
					}
				}
			}

			//----------------------------------------------------------------------------------------------------
			// 获取 SDCard 路径
			strSDCard = strSDCard.toLowerCase();

			File[] files = new File("/mnt").listFiles();
			if (files == null) return null;

			int nLen = files.length;
			if (nLen <= 0) return null;

			for (int i = 0; i < nLen; i++)
			{
				String strFile = files[i].getAbsolutePath().toLowerCase();

				if (!strFile.equalsIgnoreCase(strSDCard) && strFile.toLowerCase().contains("sd"))
				{
					// 获取 SD 卡的总容量
					strFile = files[i].getAbsolutePath();
					long lSDTotalSize = getSDTotalSize(strFile);

					if (lSDTotalSize > 0)
					{
						theApp.TLog(TAG, "lSDTotalSize > 0: " + strFile);
						return strFile;
					}
				}
			}

			//----------------------------------------------------------------------------------------------------
			files = new File("/storage").listFiles();
			if (files == null) return null;

			nLen = files.length;
			if (nLen <= 0) return null;

			for (int i = 0; i < nLen; i++)
			{
				String strFile = files[i].getAbsolutePath().toLowerCase();

				if (!strFile.equalsIgnoreCase(strSDCard) && strFile.toLowerCase().contains("sd"))
				{
					// 获取 SD 卡的总容量
					strFile = files[i].getAbsolutePath();
					long lSDTotalSize = getSDTotalSize(strFile);

					if (lSDTotalSize > 0)
					{
						theApp.TLog(TAG, "lSDTotalSize > 0: " + strFile);
						return strFile;
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

		return null;
	}

	/**
	 * 获取扩展 SD 卡的 TB-TECH-HMI 文件夹
	 * @return
	 */
	private String getExtSDCardTB()
	{
		try
		{
			//----------------------------------------------------------------------------------------------------
			// 获取 SDCard 列表
			File[] storageList = getStorageList();

			if (storageList != null && storageList.length > 0)
			{
				int nLen = storageList.length;
				theApp.TLog(TAG, String.format("storageList.length: %d", nLen));

				for (int i = 0; i < nLen; i++)
				{
					// 扩展 SD 卡
					String strExtSDCard = storageList[i].getPath();
					theApp.TLog(TAG, "strExtSDCard: " + strExtSDCard);

					// TB-TECH-HMI 文件夹
					String strExtSDCardTB = strExtSDCard + "/TB-TECH-HMI/";

					// 文件
					File file = new File(strExtSDCardTB);

					// 检测是否为文件夹
					if (!file.exists() ||
						!file.isDirectory() ||
						file.listFiles() == null ||
						file.listFiles().length <= 0) continue;

					theApp.TLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
					theApp.SLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
					return strExtSDCardTB;
				}
			}

			//----------------------------------------------------------------------------------------------------
			// 获取扩展 SD 卡
			String strExtSDCard = getExtSDCard();

			// TB-TECH-HMI 文件夹
			String strExtSDCardTB = strExtSDCard + "/TB-TECH-HMI/";

			// 文件
			File file = new File(strExtSDCardTB);

			// 检测是否为文件夹
			if (file.exists() &&
				file.isDirectory() &&
				file.listFiles() != null &&
				file.listFiles().length > 0)
			{
				theApp.TLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
				theApp.SLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
				return strExtSDCardTB;
			}

			//----------------------------------------------------------------------------------------------------
			// 获取 SDCard 路径
			String strSDCard = getSDCardPath();

			// 获取所有的 Storage 路径列表
			final File[] filesPaths = EnvironmentPlus.getExternalStorageList(this);

			if (filesPaths != null && filesPaths.length > 0)
			{
				int nLen = filesPaths.length;

				for (int i = 0; i < nLen; i++)
				{
					String strPath = filesPaths[i].getPath();
					Log.d(TAG, strPath);

					if (!strPath.equals(strSDCard))
					{
						// TB-TECH-HMI 文件夹
						strExtSDCardTB = strPath + "/TB-TECH-HMI/";

						// 文件
						file = new File(strExtSDCardTB);

						// 检测是否为文件夹
						if (file.exists() &&
							file.isDirectory() &&
							file.listFiles() != null &&
							file.listFiles().length > 0)
						{
							theApp.TLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
							theApp.SLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
							return strExtSDCardTB;
						}
					}
				}
			}

			//----------------------------------------------------------------------------------------------------
			// 获取 SDCard 路径
			strSDCard = strSDCard.toLowerCase();

			File[] files = new File("/mnt").listFiles();
			if (files == null) return null;

			int nLen = files.length;
			if (nLen <= 0) return null;

			for (int i = 0; i < nLen; i++)
			{
				String strFile = files[i].getAbsolutePath().toLowerCase();

				if (!strFile.equalsIgnoreCase(strSDCard) && strFile.toLowerCase().contains("sd"))
				{
					// 获取 SD 卡的总容量
					strFile = files[i].getAbsolutePath();
					long lSDTotalSize = getSDTotalSize(strFile);

					if (lSDTotalSize > 0)
					{
						// TB-TECH-HMI 文件夹
						strExtSDCardTB = strFile + "/TB-TECH-HMI/";

						// 文件
						file = new File(strExtSDCardTB);

						// 检测是否为文件夹
						if (file.exists() &&
							file.isDirectory() &&
							file.listFiles() != null &&
							file.listFiles().length > 0)
						{
							theApp.TLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
							theApp.SLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
							return strExtSDCardTB;
						}
					}
				}
			}

			//----------------------------------------------------------------------------------------------------
			files = new File("/storage").listFiles();
			if (files == null) return null;

			nLen = files.length;
			if (nLen <= 0) return null;

			for (int i = 0; i < nLen; i++)
			{
				String strFile = files[i].getAbsolutePath().toLowerCase();

				if (!strFile.equalsIgnoreCase(strSDCard) && strFile.toLowerCase().contains("sd"))
				{
					// 获取 SD 卡的总容量
					strFile = files[i].getAbsolutePath();
					long lSDTotalSize = getSDTotalSize(strFile);

					if (lSDTotalSize > 0)
					{
						// TB-TECH-HMI 文件夹
						strExtSDCardTB = strFile + "/TB-TECH-HMI/";

						// 文件
						file = new File(strExtSDCardTB);

						// 检测是否为文件夹
						if (file.exists() &&
							file.isDirectory() &&
							file.listFiles() != null &&
							file.listFiles().length > 0)
						{
							theApp.TLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
							theApp.SLog(TAG, "strExtSDCardTB: " + strExtSDCardTB);
							return strExtSDCardTB;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
		}

		return null;

/*		try
		{
			// 扩展卡
			String strExt = "/storage/external_storage/";

			// 文件
			File file = new File(strExt);

			// 检测是否为文件夹
			if (!file.exists() ||
				!file.isDirectory() ||
				file.listFiles() == null ||
				file.listFiles().length <= 0) return null;

			// 文件列表
			File[] files = file.listFiles();

			int nLen = files.length;

			for (int i = 0; i < nLen; i++)
			{
				// 获取文件路径
				String strPath = file.getPath() + "/TB-TECH-HMI/";

				// 文件
				file = new File(strPath);

				// 检测是否为文件夹
				if (!file.exists() ||
					!file.isDirectory() ||
					file.listFiles() == null ||
					file.listFiles().length <= 0) continue;

				return strPath;
			}

			return "/storage/external_storage/sda1/TB-TECH-HMI/";
		}
		catch (Exception e)
		{
			return null;
		}*/
	}

	/**
	 * 重置媒体
	 */
	private void resetMedia()
	{
		try
		{
			// 未加载音乐
			theApp.setLoadMusic(false);

			// 未加载视频
			theApp.setLoadVideo(false);

			// 未加载图片
			theApp.setLoadPicture(false);

			// 媒体标记, 音乐
			if (theApp.getMediaMark() == theApp.MARK_MUSIC)
			{
				// 音乐标记, 播放
				if (theApp.getMusicMark() == theApp.MARK_PLAY)
				{
					// 停止
					if (m_layMusic != null) m_layMusic.stop();

					// 后退
					if (m_layMusic != null) m_layMusic.back();
				}

				// 音乐标记, 列表
				if (theApp.getMusicMark() == theApp.MARK_LIST)
				{
					// 加载音乐
					loadMusic();
				}

				// 音乐标记, 无
				if (theApp.getMusicMark() == theApp.MARK_NONE)
				{
					// 停止
					if (m_layMusic != null) m_layMusic.stop();
				}
			}

			// 媒体标记, 视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO)
			{
				// 视频标记, 播放
				if (theApp.getVideoMark() == theApp.MARK_PLAY)
				{
					// 停止
//					if (m_layVideo != null) m_layVideo.stop();

					// 后退
					if (m_layVideo != null) m_layVideo.back();
				}

				// 加载视频
				loadVideo();
			}

			// 媒体标记, 图片
			if (theApp.getMediaMark() == theApp.MARK_PICTU)
			{
				// 图片标记, 播放
				if (theApp.getPictuMark() == theApp.MARK_PLAY)
				{
					// 停止
					if (m_layPictureGrid != null) m_layPictureGrid.stop();

					// 后退
					if (m_layPictureGrid != null) m_layPictureGrid.back();
				}

				// 加载图片
				loadPicture();
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取媒体音量
	 * @return
	 */
/*	private int getStreamVolume()
	{
		try
		{
			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			return am.getStreamVolume(AudioManager.STREAM_MUSIC);
		}
		catch (Exception e)
		{
		}

		return 0;
	}
*/
	/**
	 * 获取屏幕亮度
	 * @return
	 */
/*	private int getScreenBrightness()
	{
		try
		{
			ContentResolver cr = getContentResolver();
			return Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		}
		catch (SettingNotFoundException e)
		{
		}

		return 0;
	}
*/
	/**
	 * 设置屏幕亮度
	 * @param nValue
	 */
/*	private void setScreenBrightness(final int nValue)
	{
		try
		{
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.screenBrightness = nValue / 255f;
			getWindow().setAttributes(params);
		}
		catch (Exception e)
		{
		}
	}
*/
	/**
	 * 减小音量
	 */
	private void volumeLower()
	{
		try
		{
			// 获取音频管理器
/*			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

			// 获取音量
			int nCur = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			int nMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			// 设置音量
			nCur -= (nMax / 15);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, nCur, AudioManager.FLAG_PLAY_SOUND);*/
			if (theApp.getStreamVolume()<= 1) return;

			// 获取音频管理器
			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

			if (am.getStreamVolume(AudioManager.STREAM_MUSIC)<= 1) return;

			// 减小音量
			am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
					AudioManager.FX_FOCUS_NAVIGATION_UP);
//			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FX_KEY_CLICK);
			// 获取音量
			int nCur = am.getStreamVolume(AudioManager.STREAM_MUSIC);

			// 设置音量
//			am.setStreamVolume(AudioManager.STREAM_MUSIC, nCur, AudioManager.FLAG_PLAY_SOUND);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, nCur, AudioManager.FX_KEY_CLICK);

			theApp.setStreamVolume(nCur);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 增大音量
	 */
	private void volumeRaise()
	{
		try
		{
			// 获取音频管理器
/*			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

			// 获取音量
			int nCur = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			int nMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			// 设置音量
			nCur += (nMax / 15);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, nCur, AudioManager.FLAG_PLAY_SOUND);*/

			// 获取音频管理器
			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

			// 增大音量
			am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
					AudioManager.FX_FOCUS_NAVIGATION_UP);

			// 获取音量
			int nCur = am.getStreamVolume(AudioManager.STREAM_MUSIC);

			// 设置音量
//			am.setStreamVolume(AudioManager.STREAM_MUSIC, nCur, AudioManager.FLAG_PLAY_SOUND);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, nCur, AudioManager.FX_KEY_CLICK);

			theApp.setStreamVolume(nCur);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 调节音量
	 */
	public void volumeSame()
	{
		try
		{
			// 获取音频管理器
			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			if (theApp.getAvStatus())
			{
				// 调节音量
				am.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_SAME,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
			}
			else
			{
				// 调节音量
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
			}
		}
		catch (Exception e)
		{
		}
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * 切换到桌面
	 */
	private void home()
	{
		try
		{
			// 启动桌面
			Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    intent.addCategory(Intent.CATEGORY_DEFAULT);
//		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(intent);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 关闭对话框
	 */
	public void closeDlg()
	{
		try
		{
			// 关闭对话框
			Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			startActivity(intent);
			sendBroadcast(intent);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 关闭进程
	 */
	public void killProcesses()
	{
		try
		{
			// 使用视图页指示器
			if (theApp.USE_VPI)
			{
				// 关闭启动的 Activity
				// 需要添加权限 android.permission.KILL_BACKGROUND_PROCESSES
				stopService(AppsGridFragment.getStartActivity());
				String strActivity = android.content.Context.ACTIVITY_SERVICE;
				ActivityManager am = (ActivityManager)(getSystemService(strActivity));
				am.killBackgroundProcesses(AppsGridFragment.getStartActivityPkg());

				// 需要采用系统 platform 签名 APK
				// 因为需要用 FORCE_STOP_PACKAGES 权限, 该权限只赋予系统签名级程序
				// manifest 标签添加 android:sharedUserId="android.uid.system"
				// 需要添加权限  android.permission.FORCE_STOP_PACKAGES
				List<RunningAppProcessInfo> rapiList = am.getRunningAppProcesses();

				for (RunningAppProcessInfo rapi : rapiList)
				{
					if (rapi.processName != null &&
							rapi.processName.equals(AppsGridFragment.getStartActivityPkg()))
					{
						Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
						method.invoke(am, AppsGridFragment.getStartActivityPkg());

						Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
						forceStopPackage.setAccessible(true);
						forceStopPackage.invoke(am, AppsGridFragment.getStartActivityPkg());
						break;
					}
				}
			}
			// 不使用视图页指示器
			else
			{
				// 关闭启动的 Activity
				// 需要添加权限 android.permission.KILL_BACKGROUND_PROCESSES
				stopService(AppsGridLayout.getStartActivity());
				String strActivity = android.content.Context.ACTIVITY_SERVICE;
				ActivityManager am = (ActivityManager)(getSystemService(strActivity));
				am.killBackgroundProcesses(AppsGridLayout.getStartActivityPkg());

				// 需要采用系统 platform 签名 APK
				// 因为需要用 FORCE_STOP_PACKAGES 权限, 该权限只赋予系统签名级程序
				// manifest 标签添加 android:sharedUserId="android.uid.system"
				// 需要添加权限  android.permission.FORCE_STOP_PACKAGES
				List<RunningAppProcessInfo> rapiList = am.getRunningAppProcesses();

				for (RunningAppProcessInfo rapi : rapiList)
				{
					if (rapi.processName != null &&
							rapi.processName.equals(AppsGridLayout.getStartActivityPkg()))
					{
						Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
						method.invoke(am, AppsGridLayout.getStartActivityPkg());

						Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
						forceStopPackage.setAccessible(true);
						forceStopPackage.invoke(am, AppsGridLayout.getStartActivityPkg());
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 关闭进程
	 */
	public void killProcessesEx()
	{
		try
		{
			// Activity 类名
			String strCls = null;

			// App 包名
			String strPkg = null;

			// App Intent
			Intent intent = null;

			// 本 App 包名
			String strPkgName = getPackageName();

			try
			{
				String strActivity = android.content.Context.ACTIVITY_SERVICE;
				ActivityManager am = (ActivityManager)(getSystemService(strActivity));

				// 获取正在运行的任务信息
				// 需要权限 android.permission.GET_TASKS
				List<RunningTaskInfo> rti = am.getRunningTasks(1);

				if (rti != null)
				{
					ComponentName componet = rti.get(0).topActivity;
					strCls = componet.getClassName();
					strPkg = componet.getPackageName();
					Log.d(TAG, strCls + ": isTopActivity");
					Log.d(TAG, strPkg + ": isTopActivityPkg");
					theApp.TLog(TAG, strCls + ": isTopActivity");
					theApp.TLog(TAG, strPkg + ": isTopActivityPkg");
					theApp.SLog(TAG, strCls + ": isTopActivity");
					theApp.SLog(TAG, strPkg + ": isTopActivityPkg");

					// App Intent
					intent = new Intent();
					intent.setComponent(componet);
				}
			}
			catch (Exception e)
			{
			}

			if (strCls != null && strPkg != null && intent != null && !strPkg.equals(strPkgName))
			{
				// 关闭启动的 Activity
				// 需要添加权限 android.permission.KILL_BACKGROUND_PROCESSES
				stopService(intent);
				String strActivity = android.content.Context.ACTIVITY_SERVICE;
				ActivityManager am = (ActivityManager)(getSystemService(strActivity));
				am.killBackgroundProcesses(strPkg);

				// 需要采用系统 platform 签名 APK
				// 因为需要用 FORCE_STOP_PACKAGES 权限, 该权限只赋予系统签名级程序
				// manifest 标签添加 android:sharedUserId="android.uid.system"
				// 需要添加权限  android.permission.FORCE_STOP_PACKAGES
				List<RunningAppProcessInfo> rapiList = am.getRunningAppProcesses();

				for (RunningAppProcessInfo rapi : rapiList)
				{
					if (rapi.processName != null &&
							rapi.processName.equals(strPkg))
					{
						Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
						method.invoke(am, strPkg);

						Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
						forceStopPackage.setAccessible(true);
						forceStopPackage.invoke(am, strPkg);
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
			theApp.TLog(TAG, e.toString());
			theApp.SLog(TAG, e.toString());
		}
	}
	/**
	 * 关闭QQ音乐
	 */
	public void killQQMusic()
	{
		try
		{
			String strActivity = android.content.Context.ACTIVITY_SERVICE;

			ActivityManager am = (ActivityManager)(getSystemService(strActivity));

			// 本 App 包名
			String strPkgName = "com.tencent.qqmusic";

			am.killBackgroundProcesses (strPkgName);

		/*	int currentVersion = android.os.Build.VERSION.SDK_INT;

		    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1)
		    {
		        Intent startMain = new Intent(Intent.ACTION_MAIN);
		        startMain.addCategory(Intent.CATEGORY_HOME);
		        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(startMain);
		        System.exit(0);
		    }
		    else
		    {// android2.1
		        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		        am.restartPackage(strPkgName);
		    } 		*/

		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
			theApp.TLog(TAG, e.toString());
			theApp.SLog(TAG, e.toString());
		}
	}

	/**
	 * 关闭所有进程
	 */
	public void killAllProcesses()
	{
		try
		{
			// 本 App 包名
			String strPkgName = getPackageName();

			String strActivity = android.content.Context.ACTIVITY_SERVICE;
			ActivityManager am = (ActivityManager)(getSystemService(strActivity));

			// 获取正在运行的任务信息
			// 需要权限 android.permission.GET_TASKS
			List<RunningAppProcessInfo> rapiList = am.getRunningAppProcesses();

			for (RunningAppProcessInfo rapi : rapiList)
			{
				if (rapi.processName != null &&
					!rapi.processName.contains(strPkgName))
				{
					Log.d(TAG, "killAllProcesses - rapi.processName: " + rapi.processName);
					theApp.TLog(TAG, "killAllProcesses - rapi.processName: " + rapi.processName);
					theApp.SLog(TAG, "killAllProcesses - rapi.processName: " + rapi.processName);

					// 关闭进程
					android.os.Process.killProcess(rapi.pid);
					Log.d(TAG, "killAllProcesses - killProcess");
					theApp.TLog(TAG, "killAllProcesses - killProcess");
					theApp.SLog(TAG, "killAllProcesses - killProcess");

					// 关闭后台进程
					// 需要添加权限 android.permission.KILL_BACKGROUND_PROCESSES
					am.killBackgroundProcesses(rapi.processName);
					Log.d(TAG, "killAllProcesses - killBackgroundProcesses");
					theApp.TLog(TAG, "killAllProcesses - killBackgroundProcesses");
					theApp.SLog(TAG, "killAllProcesses - killBackgroundProcesses");


					try
					{
						Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
						method.invoke(am, rapi.processName);
						Log.d(TAG, "killAllProcesses - forceStopPackage1");
						theApp.TLog(TAG, "killAllProcesses - forceStopPackage1");
						theApp.SLog(TAG, "killAllProcesses - forceStopPackage1");

						Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
						forceStopPackage.setAccessible(true);
						forceStopPackage.invoke(am, rapi.processName);
						Log.d(TAG, "killAllProcesses - forceStopPackage2");
						theApp.TLog(TAG, "killAllProcesses - forceStopPackage2");
						theApp.SLog(TAG, "killAllProcesses - forceStopPackage2");
					}
					catch (Exception e)
					{
						Log.d(TAG, "killAllProcesses - " + e.toString());
						theApp.TLog(TAG, "killAllProcesses - " + e.toString());
						theApp.SLog(TAG, "killAllProcesses - " + e.toString());
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
			theApp.TLog(TAG, e.toString());
			theApp.SLog(TAG, e.toString());
		}
	}

	/**
	 * 关闭所有进程
	 */
	public void killAllProcessesEx()
	{
		try
		{
			// Activity 类名
			String strCls = null;

			// App 包名
			String strPkg = null;

			// App Intent
			Intent intent = null;

			// 本 App 包名
			String strPkgName = getPackageName();

			try
			{
				String strActivity = android.content.Context.ACTIVITY_SERVICE;
				ActivityManager am = (ActivityManager)(getSystemService(strActivity));

				List<RunningAppProcessInfo> rapiList = am.getRunningAppProcesses();

				// 获取正在运行的任务信息
				// 需要权限 android.permission.GET_TASKS
				int nSize = rapiList.size();
				int nMaxNum = nSize < 100 ? 100 : nSize;
				List<RunningTaskInfo> rtiList = am.getRunningTasks(nMaxNum);
				Log.d(TAG, "MaxNum: " + nMaxNum);
				theApp.TLog(TAG, "MaxNum: " + nMaxNum);
				theApp.SLog(TAG, "MaxNum: " + nMaxNum);

				for (RunningTaskInfo rti : rtiList)
				{
					try
					{
						Log.d(TAG, "NumActivities: " + rti.numActivities);
						Log.d(TAG, "NumRunning: " + rti.numRunning);
						theApp.TLog(TAG, "NumActivities: " + rti.numActivities);
						theApp.TLog(TAG, "NumRunning: " + rti.numRunning);
						theApp.SLog(TAG, "NumActivities: " + rti.numActivities);
						theApp.SLog(TAG, "NumRunning: " + rti.numRunning);

						ComponentName componetTop = rti.topActivity;
						strCls = componetTop.getClassName();
						strPkg = componetTop.getPackageName();
						Log.d(TAG, strCls + ": isTopActivity");
						Log.d(TAG, strPkg + ": isTopActivityPkg");
						theApp.TLog(TAG, strCls + ": isTopActivity");
						theApp.TLog(TAG, strPkg + ": isTopActivityPkg");
						theApp.SLog(TAG, strCls + ": isTopActivity");
						theApp.SLog(TAG, strPkg + ": isTopActivityPkg");

						// App Intent
						intent = new Intent();
						intent.setComponent(componetTop);

						if (strCls != null && strPkg != null && intent != null &&
							!strPkg.contains(strPkgName))
						{
							// 关闭启动的 Activity
							// 需要添加权限 android.permission.KILL_BACKGROUND_PROCESSES
							stopService(intent);
							am.killBackgroundProcesses(strPkg);
							Log.d(TAG, "TopActivity - killBackgroundProcesses");
							theApp.TLog(TAG, "TopActivity - killBackgroundProcesses");
							theApp.SLog(TAG, "TopActivity - killBackgroundProcesses");
						}

						ComponentName componetBase = rti.topActivity;
						strCls = componetBase.getClassName();
						strPkg = componetBase.getPackageName();
						Log.d(TAG, strCls + ": isBaseActivity");
						Log.d(TAG, strPkg + ": isBaseActivityPkg");
						theApp.TLog(TAG, strCls + ": isBaseActivity");
						theApp.TLog(TAG, strPkg + ": isBaseActivityPkg");
						theApp.SLog(TAG, strCls + ": isBaseActivity");
						theApp.SLog(TAG, strPkg + ": isBaseActivityPkg");

						// App Intent
						intent = new Intent();
						intent.setComponent(componetBase);

						if (strCls != null && strPkg != null && intent != null &&
							!strPkg.contains(strPkgName))
						{
							// 关闭启动的 Activity
							// 需要添加权限 android.permission.KILL_BACKGROUND_PROCESSES
							stopService(intent);
							am.killBackgroundProcesses(strPkg);
							Log.d(TAG, "BaseActivity - killBackgroundProcesses");
							theApp.TLog(TAG, "BaseActivity - killBackgroundProcesses");
							theApp.SLog(TAG, "BaseActivity - killBackgroundProcesses");
						}

						Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
						method.invoke(am, strPkg);

						Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
						forceStopPackage.setAccessible(true);
						forceStopPackage.invoke(am, strPkg);
					}
					catch (Exception e)
					{
						Log.d(TAG, "killAllProcessesEx - " + e.toString());
						theApp.TLog(TAG, "killAllProcessesEx - " + e.toString());
						theApp.SLog(TAG, "killAllProcessesEx - " + e.toString());
					}
				}
			}
			catch (Exception e)
			{
				Log.d(TAG, "killAllProcessesEx - " + e.toString());
				theApp.TLog(TAG, "killAllProcessesEx - " + e.toString());
				theApp.SLog(TAG, "killAllProcessesEx - " + e.toString());
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
			theApp.TLog(TAG, e.toString());
			theApp.SLog(TAG, e.toString());
		}
	}

	/**
	 * 发送键盘事件
	 * @param nKeyCode
	 */
	public void sendKeyEvent(final int nKeyCode)
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					// 检测是否在前台运行
					if (!theApp.isRunningForeground(MainActivity.this))
					{
						// 需要添加权限  android.permission.INJECT_EVENTS
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(nKeyCode);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
    /**
     * 初始化地图
     */
    private void initMap() {

        // 容器布局
        if (m_layContent == null) m_layContent = (RelativeLayout) findViewById(R.id.layContent);
//			if (theApp.DEBUG) m_layContent.setBackgroundColor(Color.GREEN);

        // 宽度和高度
//        int nW = m_layContent.getWidth();
//        int nH = m_layContent.getHeight();

        // 移除浏览器布局
//        if (m_layoutMap != null) m_layContent.removeView(m_layoutMap);
//
//        // 浏览器布局
//        if (m_layoutMap != null) m_layoutMap = null;

      //  m_layoutMap = new MapLayout(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        if (params.width < 1 || params.height < 1) {
//            params = new RelativeLayout.LayoutParams(nW, nH);
//        }

//
//        m_layoutMap.setLayoutParams(params);
//        // 添加浏览器布局
//        m_layContent.addView(m_layoutMap);

        // 应答 E0
        theApp.responseE0();


    }


//    public void showMap() {
//        // 显示地图
//        if (m_layoutMap != null) {
//            m_layoutMap.setVisibility(View.VISIBLE);
//        } else {
//            initMap();
//
//        }
//
//
//    }

	/**
	 * 布局视图
	 * @param nP1 参数
	 */
	private void layoutView_D0_98(final int nP1)
	{
		try
		{
			//	停止DA电影
//			pause();

            if (nP1 == 14) {
                //显示地图
//                showMap();
                // 应答 E0
                // 视频标记, 列表
                theApp.setMs_nMapMark(theApp.MARK_IN);
                theApp.responseE0();
            } else {
//                // 隐藏地图
//                if (m_layoutMap != null) {
//                    m_layoutMap.setVisibility(View.INVISIBLE);
//                    m_layoutMap.onPause();
//                    clearLayout();
//
//
//                }
            }
			// 播放 U 盘或者卡上电影命令
			if (nP1 == 4)
			{
				// 显示视频
				if (m_layVideo != null) m_layVideo.setVisibility(View.VISIBLE);

				// 未加载视频
				if (!theApp.isLoadVideo())
				{
					// 加载视频
					loadVideo();
				}
				// 已加载视频
				else
				{
					// 初始化视频
					if (m_layVideo == null) initVideo();

					// 视频标记, 列表
					theApp.setVideoMark(theApp.MARK_LIST);

					// 应答 E0
					theApp.responseE0();
				}

				//显示Toast
				theApp.setToast(true);

			}
			else
			{
				// 隐藏视频
				if (m_layVideo != null) m_layVideo.stop();
				if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);
			}

			// 播放 U 盘或者卡上音乐命令
			if (nP1 == 6)
			{
				// 显示音乐
				if (m_layMusic != null) m_layMusic.setVisibility(View.VISIBLE);

				if (m_layMusic != null)
				{
					// 音乐标记, 列表
					theApp.setMusicMark(theApp.MARK_LIST);

					// 正在播放音乐
					if (theApp.getMediaMark() == theApp.MARK_MUSIC)
					{
						// 正在播放
						if (m_layMusic.isPlaying())
						{
							// 显示播放界面
							m_layMusic.showPlay();

							// 显示进度条 xwx
							m_layMusic.ShowMusicProc();
						}
					}
				}

				// 未加载音乐
				if (!theApp.isLoadMusic())
				{
					// 加载音乐
					loadMusic();
				}
				// 已加载音乐
				else
				{
					// 初始化音乐
					if (m_layMusic == null) initMusic();

					// 应答 E0
					theApp.responseE0();

				}
			}
			else
			{
				// 隐藏音乐
				theApp.setMusicMark(theApp.MARK_NONE);
//				if (m_layMusic != null) m_layMusic.stop();
				if (m_layMusic != null) m_layMusic.setVisibility(View.INVISIBLE);
			}

			// 播放 U 盘或者卡上图片命令
			if (nP1 == 8)
			{
				// 显示图片
				if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.VISIBLE);
//				if (m_layPictureList != null) m_layPictureList.setVisibility(View.VISIBLE);

				// 未加载图片
				if (!theApp.isLoadPicture())
				{
					// 加载图片
					loadPicture();
				}
				// 已加载图片
				else
				{
					// 初始化图片
					if (m_layPictureGrid == null) initPicture();
//					if (m_layPictureList == null) initPicture();

					// 图片标记, 列表
					theApp.setPictuMark(theApp.MARK_LIST);

					// 应答 E0
					theApp.responseE0();
				}
			}
			else
			{
				// 隐藏图片
				if (m_layPictureGrid != null) m_layPictureGrid.stop();
				if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);
//				if (m_layPictureList != null) m_layPictureList.stop();
//				if (m_layPictureList != null) m_layPictureList.setVisibility(View.INVISIBLE);
			}

			// 进入 Internet 页面
			if (nP1 == 10)
			{
				// 显示浏览器
				if (m_layBrowser != null) m_layBrowser.setVisibility(View.VISIBLE);
//				if (m_layBrowserOld != null) m_layBrowserOld.setVisibility(View.VISIBLE);

				// 未初始化浏览器
				if (!theApp.isInitBrowser())
				{
					// 初始化浏览器
					initBrowser();
				}
				// 已加载浏览器
				else
				{
					// 初始化浏览器
					if (m_layBrowser == null) initBrowser();
//					if (m_layBrowserOld == null) initBrowser();
				}
			}
			else
			{
				// 隐藏浏览器
				if (m_layBrowser != null) m_layBrowser.setVisibility(View.INVISIBLE);
				if (m_layBrowser != null) m_layBrowser.onPause();
//				if (m_layBrowserOld != null) m_layBrowserOld.setVisibility(View.INVISIBLE);
			}

			// 进入 APP 预览并启动悬浮窗
			if (nP1 == 12)
			{
				// 使用视图页指示器
				if (theApp.USE_VPI)
				{
					// 显示 Apps
					if (m_layAppsVpi != null) m_layAppsVpi.setVisibility(View.VISIBLE);

					// 视图页
					ViewPager vp = (ViewPager)findViewById(R.id.vpApps);
					if (vp != null) vp.setVisibility(View.VISIBLE);

					// 视图页指示器
					ViewPagerIndicator vpi = (ViewPagerIndicator)findViewById(R.id.vpiApps);
					if (vpi != null) vpi.setVisibility(View.VISIBLE);
				}
				// 不使用视图页指示器
				else
				{
					// 显示 Apps
					if (m_layAppsGrid != null) m_layAppsGrid.setVisibility(View.VISIBLE);
				}

				// 未加载 Apps
				if (!theApp.isLoadApps())
				{
//					theApp.MYSLog(TAG,"go to");
					// 加载 Apps
					loadApps();
				}
				// 已加载 Apps
				else
				{
					// 使用视图页指示器
					if (theApp.USE_VPI)
					{
						// 初始化 Apps
						if (m_layAppsVpi == null) initApps();
					}
					// 不使用视图页指示器
					else
					{
						// 初始化 Apps
						if (m_layAppsGrid == null) initApps();
					}
				}
			}
			// 在打开第三方 App 之后, 切换本程序的 Activity 至顶层
			else if (nP1 == 0)
			{
			/*	//场景视频 界面关闭
				SenceActivity.instance.finish();*/

				// 检测是否在前台运行
				if (!theApp.isRunningForeground(this))
				{
					theApp.setAvStatus(false);

	                String broadcastIntent = "com.chinafeisite.tianbu.resume";//自己自定义
	                Intent intent = new Intent(broadcastIntent);
	                sendBroadcast(intent);

	        		try
	        		{
	        			//判断隐藏软键盘是否弹出
	        			if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
	        			{
	        				//隐藏软键盘
	        				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	        			}
	        		}
	        		catch (Exception e)
	        		{

	        		}

					//清除悬浮窗osd
					theApp.clearAllosd(false,true,false);
					theApp.clearAllosd(false,false,true);

					// 关闭悬浮窗
					theApp.setStartFloat(false);

					// 关闭悬浮窗
					theApp.setFloatOn(false);
					theApp.setFloatTopOn(false);
					theApp.setFloatBottomOn(false);

					// 移除顶部, 底部和开关悬浮窗
					FloatManager.removeTBS(this);

					// 停止顶部, 底部和开关悬浮窗服务
					FloatManager.stopTBSService(this);


					//关闭qq音乐
//					killQQMusic();

					// 关闭进程
					killProcessesEx();
					killProcesses();

					// 发送返回按键消息
					sendKeyEvent(KeyEvent.KEYCODE_BACK);
//					sendKeyEvent(KeyEvent.KEYCODE_HOME);

					// 关闭所有进程
//					killAllProcesses();	//xwx
//					killAllProcessesEx();

					// 切换本程序的 Activity 至顶层
					// 需要在 AndroidManifest.xml 中添加 android:launchMode="singleTask" 或 android:launchMode="singleInstance"
				/*	Intent intent = new Intent(this, MainActivity.class);
		            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(intent);*/

		            // 如需设置为 Launcher, 需要添加如下权限:
		            // <category android:name="android.intent.category.HOME" />
		            // <category android:name="android.intent.category.DEFAULT" />

		            // 设置 WiFi
		            if (m_laySetup != null) m_laySetup.setWiFi();
		            return;
				}
				//xwx apk退出时程序运行在前台不关闭悬浮窗
				else
				{
//					if (theApp.isStartFloat())
					{
						//清除悬浮窗osd
						theApp.clearAllosd(false,true,false);
						theApp.clearAllosd(false,false,true);

						// 关闭悬浮窗
						theApp.setStartFloat(false);

						// 关闭悬浮窗
						theApp.setFloatOn(false);
						theApp.setFloatTopOn(false);
						theApp.setFloatBottomOn(false);

						// 移除顶部, 底部和开关悬浮窗
						FloatManager.removeTBS(this);

						// 停止顶部, 底部和开关悬浮窗服务
						FloatManager.stopTBSService(this);

						// 隐藏配置
						if (m_laySetup != null) m_laySetup.setVisibility(View.INVISIBLE);

						// 关闭进程
						killProcessesEx();
						killProcesses();

						// 发送返回按键消息
						sendKeyEvent(KeyEvent.KEYCODE_BACK);


						/*
						// 切换本程序的 Activity 至顶层
						// 需要在 AndroidManifest.xml 中添加 android:launchMode="singleTask" 或 android:launchMode="singleInstance"
						Intent intent = new Intent(this, MainActivity.class);
			            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            startActivity(intent);*/

//			            return;
					}
				}

				// 启用自定义 WiFi
				if (theApp.USE_WIFI)
				{
					// 隐藏 WiFi 布局
					if (m_layWiFi != null) m_layWiFi.setVisibility(View.INVISIBLE);

					// 移除 WiFi 布局
					if (m_layWiFi != null && m_layContent != null) m_layContent.removeView(m_layWiFi);
					if (m_layWiFi != null) m_layWiFi.exit();
					if (m_layWiFi != null) m_layWiFi = null;

					// 显示配置布局
					if (m_laySetup != null) m_laySetup.setVisibility(View.VISIBLE);

		            // 设置 WiFi
		            if (m_laySetup != null) m_laySetup.setWiFi();
				}
			}
			else
			{
				// 关闭悬浮窗
				theApp.setStartFloat(false);

				// 关闭悬浮窗
				theApp.setFloatOn(false);
				theApp.setFloatTopOn(false);
				theApp.setFloatBottomOn(false);

				// 移除顶部, 底部和开关悬浮窗
				FloatManager.removeTBS(this);

				// 停止顶部, 底部和开关悬浮窗服务
				FloatManager.stopTBSService(this);

				// 使用视图页指示器
				if (theApp.USE_VPI)
				{
					// 隐藏 Apps
					if (m_layAppsVpi != null) m_layAppsVpi.setVisibility(View.INVISIBLE);

					// 视图页
					ViewPager vp = (ViewPager)findViewById(R.id.vpApps);
					if (vp != null) vp.setVisibility(View.INVISIBLE);

					// 视图页指示器
					ViewPagerIndicator vpi = (ViewPagerIndicator)findViewById(R.id.vpiApps);
					if (vpi != null) vpi.setVisibility(View.INVISIBLE);
				}
				// 不使用视图页指示器
				else
				{
					// 隐藏 Apps
					if (m_layAppsGrid != null) m_layAppsGrid.setVisibility(View.INVISIBLE);
				}
			}
//			2.场景视频 界面关闭
			SenceActivity.instance.finish();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

//				2.场景视频 界面关闭
				SenceActivity.instance.finish();

	}

	/**
	 * 布局视图
	 * @param nP1 参数
	 */
	private void layoutView_F1_90(final int nP1)
	{
		try
		{
			SenceActivity.instance.finish();
			theApp.setThirdAPP("退出场景视频播放");
			// MainActivity
			if (nP1 == -1)
			{
				//----------------------------------------------------------------------------------------------------
				// 使用视图页指示器
				if (theApp.USE_VPI)
				{
					// 隐藏 Apps
					if (m_layAppsVpi != null) m_layAppsVpi.setVisibility(View.INVISIBLE);

					// 视图页
					ViewPager vp = (ViewPager)findViewById(R.id.vpApps);
					if (vp != null) vp.setVisibility(View.INVISIBLE);

					// 视图页指示器
					ViewPagerIndicator vpi = (ViewPagerIndicator)findViewById(R.id.vpiApps);
					if (vpi != null) vpi.setVisibility(View.INVISIBLE);
				}
				// 不使用视图页指示器
				else
				{
					// 隐藏 Apps
					if (m_layAppsGrid != null) m_layAppsGrid.setVisibility(View.INVISIBLE);
				}

				// 隐藏音乐
				theApp.setMusicMark(theApp.MARK_NONE);
				if (m_layMusic != null) m_layMusic.setVisibility(View.INVISIBLE);

				// 隐藏视频
				if (m_layVideo != null) m_layVideo.stop();
				if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);

				// 隐藏图片
				if (m_layPictureGrid != null) m_layPictureGrid.stop();
				if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);

				// 隐藏浏览器

				if (m_layBrowser != null) m_layBrowser.setVisibility(View.INVISIBLE);
				if (m_layBrowser != null) m_layBrowser.onPause();
//				if (m_layBrowserOld != null) m_layBrowserOld.setVisibility(View.INVISIBLE);

				//----------------------------------------------------------------------------------------------------
				//清除悬浮窗osd
				theApp.clearAllosd(false,true,false);
				theApp.clearAllosd(false,true,false);

				// 关闭悬浮窗
				theApp.setStartFloat(false);

				// 关闭悬浮窗
				theApp.setFloatOn(false);
				theApp.setFloatTopOn(false);
				theApp.setFloatBottomOn(false);

				// 移除顶部, 底部和开关悬浮窗
				FloatManager.removeTBS(this);

				// 停止顶部, 底部和开关悬浮窗服务
				FloatManager.stopTBSService(this);

				//----------------------------------------------------------------------------------------------------
				// 启用自定义 WiFi
				if (theApp.USE_WIFI)
				{
					// 隐藏 WiFi 布局
					if (m_layWiFi != null) m_layWiFi.setVisibility(View.INVISIBLE);

					// 移除 WiFi 布局
					if (m_layWiFi != null && m_layContent != null) m_layContent.removeView(m_layWiFi);
					if (m_layWiFi != null) m_layWiFi.exit();
					if (m_layWiFi != null) m_layWiFi = null;
				}

				//----------------------------------------------------------------------------------------------------
				// 隐藏配置
				if (m_laySetup != null) m_laySetup.setVisibility(View.INVISIBLE);

				//----------------------------------------------------------------------------------------------------
				// 显示 JPG 视图
				RelativeLayout layJpg = (RelativeLayout)findViewById(R.id.layJpg);
				JpgView jpgView = (JpgView)findViewById(R.id.jpgView);
				if (layJpg != null) layJpg.setVisibility(View.VISIBLE);
				if (jpgView != null) jpgView.setVisibility(View.VISIBLE);
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	/**
	 * 浏览器进入网页
	 * @param nP2 参数
	 */
	public void gotopage(String url)
	{
	//	String ms_url = "https://www.baidu.com/";

		try
		{
			// 启动悬浮窗
			theApp.setStartFloat(true);

			// 启用悬浮窗单个开关
			if (theApp.FLOAT_SWITCH)
			{
				// 开启悬浮窗
				theApp.setFloatOn(true);

				// 启动开关悬浮窗
				Intent intentSwitch = new Intent(this, FloatSwitchService.class);
				startService(intentSwitch);
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 开启顶部悬浮窗
				theApp.setFloatTopOn(true);

				// 开启底部悬浮窗
				theApp.setFloatBottomOn(true);

				// 启动顶部悬浮窗开关
				Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
				startService(intentTopSwitch);

				// 启动底部悬浮窗开关
				Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
				startService(intentBottomSwitch);
			}

			// 启动顶部悬浮窗
			if (theApp.NOTOP)
			{
				Intent intentTop = new Intent(this, FloatTopService.class);
				startService(intentTop);
			}

			// 启动底部悬浮窗
			if (!theApp.NOBOTTOM)
			{
				Intent intentBottom = new Intent(this, FloatBottomService.class);
				startService(intentBottom);
			}

	        Intent intent = new Intent();
	        intent.setAction("android.intent.action.VIEW");
	        Uri content_url = Uri.parse(url);
	        intent.setData(content_url);
	        startActivity(intent);

		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 布局视图
	 * @param nP1 参数
	 */
	private void layoutView_F1_95(final int nP1)
	{
		try
		{
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 布局视图
	 * @param nP1 参数
	 */
	private void layoutView_F1_97(final int nP1)
	{
		try
		{
			// MainActivity
			layoutView_F1_90(-1);

			// 初始化配置
			if (m_laySetup == null) initSetup();

			// 显示配置
			if (m_laySetup != null) m_laySetup.setVisibility(View.VISIBLE);
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 布局视图
	 * @param nP1 参数
	 */
	private void layoutView_F1_98(final int nP1)
	{
		try
		{
			// MainActivity
			if (nP1 == 0)
			{
				// 未启动悬浮窗
/*				if (!theApp.isStartFloat())
				{
					// MainActivity
					layoutView_F1_90(-1);
				}
*/
				//----------------------------------------------------------------------------------------------------
				// 隐藏配置
				if (m_laySetup != null) m_laySetup.setVisibility(View.INVISIBLE);

				//----------------------------------------------------------------------------------------------------
				// 显示 JPG 视图
				RelativeLayout layJpg = (RelativeLayout)findViewById(R.id.layJpg);
				JpgView jpgView = (JpgView)findViewById(R.id.jpgView);
				if (layJpg != null) layJpg.setVisibility(View.VISIBLE);
				if (jpgView != null) jpgView.setVisibility(View.VISIBLE);
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 从Assets中解码图像
	 * @param strFile
	 * @return
	 */
	Bitmap decodeBitmapFromAssets(final String strFile)
	{
		Bitmap bmp = null;
		InputStream  fis = null;
		AssetManager am = getResources().getAssets();

		try
		{
			// 输入流
			fis =  am.open(strFile);

			// 选项
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[32 * 1024];
	        options.inPreferredConfig = Bitmap.Config.ALPHA_8;

	        // 解码
			bmp = BitmapFactory.decodeStream(fis, null, options);
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

	/**
	 * 设置 Assets 图片
	 * @param buffer
	 */
	public void setAssetsOsd2(String strPath)
	{
		//舒华
//		int wifi_x1 = 1242;
//		int wifi_y1 = 5;

		//天展
//		int wifi_x1 = 5;
//		int wifi_y1 = 5;

		//大胡子
		int wifi_x1 = 164;
		int wifi_y1 = 683;

	    if (theApp.CUSTOMER_DHZ)
	    {
			wifi_x1 = 164;
			wifi_y1 = 683;
	    }
	    if (theApp.CUSTOMER_TZ)
	    {
			wifi_x1 = 5;
			wifi_y1 = 5;
	    }
	    if (theApp.CUSTOMER_SH)
	    {
			wifi_x1 = 1242;
			wifi_y1 = 5;
			return;
	    }

		try
		{
			// OSD2 图片 1
			Bitmap bmp = decodeBitmapFromAssets(strPath);

			// OSD 图片
			OsdBitmap osd = new OsdBitmap(bmp);
			osd.setX1(wifi_x1);
			osd.setY1(wifi_y1);
			osd.setX2(wifi_x1 + bmp.getWidth());
			osd.setY2(wifi_y1 + bmp.getHeight());

			// 添加 OSD 图片
			theApp.addOsdBmp(2, osd, false);

		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 进入蓝牙设置页面
	 */
    public void openBluetoothSetup()
    {
		try
		{
			// 启动悬浮窗
		/*	theApp.setStartFloat(true);

			// 启用悬浮窗单个开关
			if (theApp.FLOAT_SWITCH)
			{
				//清除osd
//				theApp.clearAllosd(true,false,false);

				// 开启悬浮窗
				theApp.setFloatOn(true);

				// 启动开关悬浮窗
				Intent intentSwitch = new Intent(this, FloatSwitchService.class);
				startService(intentSwitch);
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 开启顶部悬浮窗
				theApp.setFloatTopOn(true);

				// 开启底部悬浮窗
				theApp.setFloatBottomOn(true);

				// 启动顶部悬浮窗开关
				Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
				startService(intentTopSwitch);

				// 启动底部悬浮窗开关
				Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
				startService(intentBottomSwitch);
			}
			*/
			// 启动顶部悬浮窗
//			Intent intentTop = new Intent(this, FloatTopService.class);
//			startService(intentTop);

			// 启动底部悬浮窗
//			Intent intentBottom = new Intent(this, FloatBottomService.class);
//			startService(intentBottom);

	        Intent intent = new Intent();
	        intent.setAction("Settings.ACTION_BLUETOOTH_SETTINGS");
	        startActivity(intent);

		}
		catch (Exception e)
		{
		}

    }
	/**
	 * 获取wifi强度
	 */
	  private void getWifiInfo()
	  {
	        WifiManager wifiManager = (WifiManager) m_context.getSystemService(m_context.WIFI_SERVICE);
	        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

	        if (theApp.getToast())
	        {
		        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		        toast.getView().getBackground().setAlpha(0);//设置透明度
		        toast.show();
	        }

	        if (wifiInfo.getBSSID() != null)
	        {
	            //wifi名称
	//            String ssid = wifiInfo.getSSID();

	            //wifi信号强度
	            int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);

	            //wifi速度
	//            int speed = wifiInfo.getLinkSpeed();

	            //wifi速度单位
	//            String units = WifiInfo.LINK_SPEED_UNITS;

	//            System.out.println("ssid="+ssid+",signalLevel="+signalLevel+",speed="+speed+",units="+units);
	            switch(signalLevel)
	            {
	            	case 0:
	            		setAssetsOsd2("wifi/wifi0.png");
//	            		theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x32);
	            		break;
	            	case 1:
	            		setAssetsOsd2("wifi/wifi1.png");
//	            		theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x32);
	            		break;
	            	case 2:
	            		setAssetsOsd2("wifi/wifi2.png");
//	            		theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x32);
	            		break;
	            	case 3:
	            		setAssetsOsd2("wifi/wifi3.png");
//	            		theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x32);
	            		break;
	            	case 4:
	            		setAssetsOsd2("wifi/wifi4.png");
//	            		theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x32);
	            		break;
	            	default:
	            		break;
	            }
	        }
	        else
	        	setAssetsOsd2("wifi/wifi0.png");

//	        theApp.drawBmp();
//	        ms_osdView2.invalidate();
	  }

	/**
	 * 打开wifi
	 */
    public static void openWifi()
    {
        if(!mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(true);
        }
    }

	/**
	 * 进入wifi设置页面
	 */
    public void openWifiSetup()
    {
		try
		{
			// 启动悬浮窗
		/*	theApp.setStartFloat(true);

			// 启用悬浮窗单个开关
			if (theApp.FLOAT_SWITCH)
			{
				//清除osd
//				theApp.clearAllosd(true,false,false);

				// 开启悬浮窗
				theApp.setFloatOn(true);

				// 启动开关悬浮窗
				Intent intentSwitch = new Intent(this, FloatSwitchService.class);
				startService(intentSwitch);
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 开启顶部悬浮窗
				theApp.setFloatTopOn(true);

				// 开启底部悬浮窗
				theApp.setFloatBottomOn(true);

				// 启动顶部悬浮窗开关
				Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
				startService(intentTopSwitch);

				// 启动底部悬浮窗开关
				Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
				startService(intentBottomSwitch);
			}
			*/
			// 启动顶部悬浮窗
//			Intent intentTop = new Intent(this, FloatTopService.class);
//			startService(intentTop);

			// 启动底部悬浮窗
//			Intent intentBottom = new Intent(this, FloatBottomService.class);
//			startService(intentBottom);
			if (ms_bWifiSetup) return;
			ms_bWifiSetup = true;
	        Intent intent = new Intent();
	        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
	        intent.putExtra("extra_prefs_show_button_bar", true);
	        intent.putExtra("wifi_enable_next_on_connect", true);
	        startActivity(intent);
			try
			{
				// 线程
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							// 休眠 2000 毫秒
							Thread.sleep(2000);

							// 可以触摸
							ms_bWifiSetup = false;
							Log.d(TAG, "ms_bWifiSetup: " + ms_bWifiSetup);
						}
						catch (Exception e)
						{
						}
					}
				}.start(); // 启动线程
			}
			catch(Exception e)
			{
			}

		}
		catch (Exception e)
		{
		}

    }

	/**
	 * 设置wifi
	 */
  public static void setWifi()
  {
      if(mWifiManager.isWifiEnabled())
      {
          mWifiManager.setWifiEnabled(false);
      }
      else
    	  mWifiManager.setWifiEnabled(true);
  }

	/**
	 * 关闭wifi
	 */
    public static void closeWifi()
    {
        if(!mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(false);
        }
    }

	/**
	 * 获取wifi强度
	 * @return
	 */
	  private boolean getWifiState()
	  {
		  return mWifiManager.isWifiEnabled();
	  }

	/**
	 * 关闭wifi
	 */
	public static void closewifi()
	{
		//关闭wifi
		try
		{
			ms_wifiState = mWifiManager.getWifiState();

            if (ms_wifiState == WifiManager.WIFI_STATE_ENABLED)
            {
            	mWifiManager.setWifiEnabled(false);
            }

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 显示关机提示
	 */
	  private void showPowerOff()
	  {
		//显示关机提示
		if (theApp.getLang().equals("ch"))
			theApp.showMyosd(ms_strPoweroffCh,400,150,(byte)0x03,(byte)0x32);
		else
			theApp.showMyosd(ms_strPoweroffEn,400,150,(byte)0x03,(byte)0x32);
	  }

	/**
	 * 关机
	 */
	public void shutdown()
	{
		// manifest 的 application 标签添加 android:sharedUserId="android.uid.system"

		// 后台
		theApp.setBackground(1);

		//关闭wifi
	/*	try
		{
            if (mWifiManager.isWifiEnabled())
            {
            	mWifiManager.setWifiEnabled(false);

				// 延迟 2 秒
				Thread.sleep(2 * 1000);
            }
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}*/

		try
		{
			// 延迟 16 秒
			Thread.sleep(16 * 1000);

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
			sendBroadcast(intent);

//			Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}

	/*	try
		{
			Class<?> clsServiceManager = Class.forName("android.os.ServiceManager");
			Method mthGetService = clsServiceManager.getMethod("getService", java.lang.String.class);
			Object objPowerManager = mthGetService.invoke(null, Context.POWER_SERVICE);
			Class<?> clsStub = Class.forName("android.os.IPowerManager$Stub");
			Method mthAsInterface = clsStub.getMethod("asInterface", android.os.IBinder.class);
			Object objIPowerManager = mthAsInterface.invoke(null, objPowerManager);
			Class<?> clsPowerManager = objIPowerManager.getClass();
			Method mthShutdown = clsPowerManager.getMethod("shutdown", boolean.class, boolean.class);
			mthShutdown.invoke(objIPowerManager, false, true);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
			intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			// 需要添加权限:
			// <uses-permission android:name="android.permission.REBOOT" />

			Process proc = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(proc.getOutputStream());
			os.writeBytes("/system/bin/-p\n");
			os.flush();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot -p" });
			proc.waitFor();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
			DataOutputStream os = new DataOutputStream(proc.getOutputStream());
			os.writeBytes("/system/bin/-p\n");
			os.flush();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}

		try
		{
			Process proc = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(proc.getOutputStream());
			os.writeBytes("echo boot-recovery|dd of=/dev/block/mmcblk0p3 bs=1 seek=6144 && reboot\n");
			os.writeBytes("exit\n");
			os.flush();
			os.close();
			proc.waitFor();
			proc.destroy();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}*/

		// 后台
		theApp.setBackground(1);

		// 退出
		System.exit(0);
	}
	/**
	 * 布局视图
	 * @param nP_ 参数
	 * @param nP0 参数
	 * @param nP1 参数
	 */
	public void layoutView(final int nP_, final int nP0, final int nP1)
	{
		try
		{
			// D0
			if (nP_ == 0xD0)
			{
				// 自由模式
				if (nP0 == 98) layoutView_D0_98(nP1);
			}

			// F1
			if (nP_ == 0xF1)
			{
				// MainActivity
				if (1 <= nP0 && nP0 <= 95) layoutView_F1_90(nP1);

				// 实景模式
//				if (nP0 == 95) layoutView_F1_95(nP1);

				// 配置
				if (nP0 == 97) layoutView_F1_97(nP1);

				// 自由模式
				if (nP0 == 98) layoutView_F1_98(nP1);
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 隐藏视图登陆
	 */
	public void hideViewLogin()
	{
		try
		{
			// 隐藏登陆
			if (m_layLogin != null) m_layLogin.setVisibility(View.INVISIBLE);
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 隐藏视图注册
	 */
	public void hideViewSignIn()
	{
		try
		{
			// 隐藏注册
			if (m_laySignIn != null) m_laySignIn.setVisibility(View.INVISIBLE);
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

/**
 * 检测网络状态
 * @return
 */
boolean checkInternetStatus()
{
	boolean bSuc = false;
	ConnectivityManager connManager = null;

    try
    {
    	// 检测是否为 wifi 上网方式
        connManager = (ConnectivityManager)m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected())
        {
        	bSuc = true;
        }
    }
	catch(Exception ex)
	{
		android.util.Log.e("error", "checkInternetStatus_wifi:" + ex.toString());
	}

    if (false == bSuc)
    {
		try
		{
			// 检测是否为手机卡上网方式
			if (null == connManager)
			{
				connManager = (ConnectivityManager)m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
			}

			NetworkInfo mMOBILE = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (mMOBILE.isConnected())
			{
				bSuc = true;
			}
		}
		catch (Exception ex)
		{
			android.util.Log.e("error", "checkInternetStatus_wifi:" + ex.toString());
		}
    }

	// 检测失败
	if (false == bSuc)
	{
		// 设备尚未连接网络
		String strMsg = m_context.getString(R.string.browser_url_netUnconnect);

		// 界面提示
		Toast.makeText(m_context, strMsg, Toast.LENGTH_LONG).show();
	}

	return bSuc;
}

/**
 * 移除历史记录
 */
public void removeHistory()
{
	try
	{
		// 容器布局
		if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

		// 设置布局参数
		float fHR = theApp.getHeightRatio();
		RelativeLayout.LayoutParams paramsContent =
				new RelativeLayout.LayoutParams(theApp.getWidthPixels(), (int)(552 * fHR));
		paramsContent.setMargins(0, (int)(94 * fHR), 0, 0);
		m_layContent.setLayoutParams(paramsContent);

		// 移除历史记录布局
		if (m_layHistory != null) m_layContent.removeView(m_layHistory);

		// 历史记录布局
		if (m_layHistory != null) m_layHistory = null;
	}
	catch (Exception e)
	{
	}
}

	/**
	 * 初始化登陆
	 */
	public void initLogin()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 设置布局参数
			float fHR = theApp.getHeightRatio();
			RelativeLayout.LayoutParams paramsContent =
					new RelativeLayout.LayoutParams(theApp.getWidthPixels(), (int)(560 * fHR));
			paramsContent.setMargins(0, (int)(80 * fHR), 0, 0);
			m_layContent.setLayoutParams(paramsContent);

			// 移除登陆布局
			if (m_layLogin != null) m_layContent.removeView(m_layLogin);

			// 登陆布局
			if (m_layLogin != null) m_layLogin = null;
			m_layLogin = new LoginLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加登陆布局
			m_layContent.addView(m_layLogin, params);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化注册
	 */
	public void initSignIn()
	{
		try
		{
			// 容器布局
			if (m_layContent == null) m_layContent = (RelativeLayout)findViewById(R.id.layContent);

			// 宽度和高度
			int nW = m_layContent.getWidth();
			int nH = m_layContent.getHeight();

			// 设置布局参数
			float fHR = theApp.getHeightRatio();
			RelativeLayout.LayoutParams paramsContent =
					new RelativeLayout.LayoutParams(theApp.getWidthPixels(), (int)(560 * fHR));
			paramsContent.setMargins(0, (int)(80 * fHR), 0, 0);
			m_layContent.setLayoutParams(paramsContent);

			// 移除注册布局
			if (m_laySignIn != null) m_layContent.removeView(m_laySignIn);

			// 注册布局
			if (m_laySignIn != null) m_laySignIn = null;
			m_laySignIn = new SignInLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

			if (params.width < 1 || params.height < 1)
			{
				params = new RelativeLayout.LayoutParams(nW, nH);
			}

			// 添加注册布局
			m_layContent.addView(m_laySignIn, params);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 布局视图登陆
	 */
	public void layoutViewLogin()
	{
		try
		{
			// 检测网络状态
			if (!checkInternetStatus())
			{
				return;
			}

			// 显示登陆
			if (m_layLogin != null) m_layLogin.setVisibility(View.VISIBLE);

			// 初始化登陆
			if (m_layLogin == null) initLogin();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 布局视图注册
	 */
	public void layoutViewSignIn()
	{
		try
		{
			// 检测网络状态
			if (!checkInternetStatus())
			{
				return;
			}

			// 显示注册
			if (m_laySignIn != null) m_laySignIn.setVisibility(View.VISIBLE);

			// 初始化注册
			if (m_laySignIn == null) initSignIn();
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * 播放媒体
	 */
	public void playMedia()
	{
		try
		{
			// 音乐
		/*	if (theApp.getMediaMark() == theApp.MARK_MUSIC)
			{
				// 播放
				if (m_layMusic != null) m_layMusic.play();
			}*/

			// 视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO)
			{
				// 播放
				if (m_layVideo != null) m_layVideo.play();
			}

			// 图片
			else if (theApp.getMediaMark() == theApp.MARK_PICTU)
			{
				// 播放
				if (m_layPictureGrid != null) m_layPictureGrid.play();
//				if (m_layPictureList != null) m_layPictureList.play();
			}
			else if (theApp.getMediaMark() == theApp.MARK_MUSIC)// 音乐
			{
				// 播放
				if (m_layMusic != null) m_layMusic.play(true);
			}
			else
			{
				// 播放
				if (m_layMusic != null) m_layMusic.play(false);
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 暂停媒体
	 */
	public void pauseMedia()
	{
		try
		{
			// 音乐
		/*	if (theApp.getMediaMark() == theApp.MARK_MUSIC)
			{
				// 暂停
				if (m_layMusic != null) m_layMusic.pause();
			}*/

			// 视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO)
			{
				// 暂停
				if (m_layVideo != null) m_layVideo.pause();
			}

			// 图片
			else if (theApp.getMediaMark() == theApp.MARK_PICTU)
			{
				// 暂停
				if (m_layPictureGrid != null) m_layPictureGrid.pause();
//				if (m_layPictureList != null) m_layPictureList.pause();
			}
			else // 音乐
			{
				// 暂停
				if (m_layMusic != null) m_layMusic.pause();
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 停止媒体
	 */
	public void stopMedia()
	{
		try
		{
			// 音乐
		/*	if (theApp.getMediaMark() == theApp.MARK_MUSIC)
			{
				// 停止
				if (m_layMusic != null) m_layMusic.stop();
			}*/

			// 视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO)
			{
				// 停止
				if (m_layVideo != null) m_layVideo.stop();
			}

			// 图片
			else if (theApp.getMediaMark() == theApp.MARK_PICTU)
			{
				// 停止
				if (m_layPictureGrid != null) m_layPictureGrid.stop();
//				if (m_layPictureList != null) m_layPictureList.stop();
			}
			else // 音乐
			{
				// 停止
				if (m_layMusic != null) m_layMusic.stop();
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 上一曲媒体
	 */
	public void prevMedia()
	{
		try
		{
			// 音乐
		/*	if (theApp.getMediaMark() == theApp.MARK_MUSIC)
			{
				// 上一曲
				if (m_layMusic != null) m_layMusic.prev();
			}*/

			// 视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO)
			{
				// 上一曲
				if (m_layVideo != null) m_layVideo.prev();
			}

			// 图片
			else if (theApp.getMediaMark() == theApp.MARK_PICTU)
			{
				// 上一曲
				if (m_layPictureGrid != null) m_layPictureGrid.prev();
//				if (m_layPictureList != null) m_layPictureList.prev();
			}
			else if (theApp.getMediaMark() == theApp.MARK_MUSIC)// 音乐
			{
				// 上一曲
				if (m_layMusic != null) m_layMusic.prev(true);
			}
			else
			{
				// 上一曲
				if (m_layMusic != null) m_layMusic.prev(false);
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 下一曲媒体
	 */
	public void nextMedia()
	{
		try
		{
			// 音乐
		/*	if (theApp.getMediaMark() == theApp.MARK_MUSIC)
			{
				// 下一曲
				if (m_layMusic != null) m_layMusic.next();
			}*/

			// 视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO)
			{
				// 下一曲
				if (m_layVideo != null) m_layVideo.next();
			}

			// 图片
			else if (theApp.getMediaMark() == theApp.MARK_PICTU)
			{
				// 下一曲
				if (m_layPictureGrid != null) m_layPictureGrid.next();
//				if (m_layPictureList != null) m_layPictureList.next();
			}
			else if (theApp.getMediaMark() == theApp.MARK_MUSIC)// 音乐
			{
				// 下一曲
				if (m_layMusic != null) m_layMusic.next(true);
			}
			else
			{
				// 下一曲
				if (m_layMusic != null) m_layMusic.next(false);
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 后退媒体
	 */
	public void backMedia()
	{
		try
		{
			// 音乐列表
			if (theApp.getMediaMark() == theApp.MARK_MUSIC &&
				theApp.getMusicMark() == theApp.MARK_LIST)
			{
				// 隐藏音乐
				theApp.setMusicMark(theApp.MARK_NONE);
				if (m_layMusic != null) m_layMusic.setVisibility(View.INVISIBLE);

				//清除标记
				theApp.setMediaMark(theApp.MARK_NONE);

			}

			// 视频列表
			if (theApp.getMediaMark() == theApp.MARK_VIDEO &&
				theApp.getVideoMark() == theApp.MARK_LIST)
			{
				// 隐藏视频
				if (m_layVideo != null) m_layVideo.stop();
				if (m_layVideo != null) m_layVideo.setVisibility(View.INVISIBLE);

				//清除标记
				theApp.setMediaMark(theApp.MARK_NONE);

				//显示Toast
				theApp.setToast(false);
			}

			// 图片列表
			if (theApp.getMediaMark() == theApp.MARK_PICTU &&
				theApp.getPictuMark() == theApp.MARK_LIST)
			{
				// 隐藏图片
				if (m_layPictureGrid != null) m_layPictureGrid.stop();
				if (m_layPictureGrid != null) m_layPictureGrid.setVisibility(View.INVISIBLE);

				//清除标记
				theApp.setMediaMark(theApp.MARK_NONE);
			}

			// 播放音乐
			if (theApp.getMediaMark() == theApp.MARK_MUSIC &&
				theApp.getMusicMark() == theApp.MARK_PLAY)
			{
				// 后退
				if (m_layMusic != null) m_layMusic.back();
			}

			// 播放视频
			if (theApp.getMediaMark() == theApp.MARK_VIDEO &&
				theApp.getVideoMark() == theApp.MARK_PLAY)
			{
				// 后退
				if (m_layVideo != null) m_layVideo.back();

				//设置多媒体标记
				theApp.setVideoMark(theApp.MARK_LIST);

				//显示Toast
				theApp.setToast(false);
			}

			// 播放图片
			if (theApp.getMediaMark() == theApp.MARK_PICTU &&
				theApp.getPictuMark() == theApp.MARK_PLAY)
			{
				// 后退
				if (m_layPictureGrid != null) m_layPictureGrid.back();

				//设置多媒体标记
				theApp.setPictuMark(theApp.MARK_LIST);
			}

			// 地图
//			if (theApp.getMs_nMapMark() == theApp.MARK_IN) {
//
//				// 后退
//				if (m_layoutMap != null) {
//
//					m_layoutMap.setVisibility(View.INVISIBLE);
//					m_layoutMap.onPause();
//					clearLayout();
//
//				}
//
//				//设置地图标记
//				theApp.setMs_nMapMark(theApp.MARK_OUT);
//				//清除标记
//				theApp.setMediaMark(theApp.MARK_NONE);
//			}

			// 关闭进度对话框
			if (m_progDlg != null) m_progDlg.dismiss();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 浏览器动作
	 * @param nP2 参数
	 */
	public void doBrowser(final int nP2)
	{
		if (nP2 >= 1 && nP2 <= 5)
		{

			//	停止DA电影
			pause();

			//清除视频布局
			clearLayoutVideo();

			//设置浏览器标志
			theApp.setBrowse(true);

			if(theApp.isClear())
			{
				//清除标记
				theApp.setClear(false);

				// 清除浏览器布局
				clearLayoutBrowse();

				// 初始化浏览器
				if (m_layBrowser == null) { initBrowser(); return; }
	//			if (m_layBrowserOld == null) { initBrowser(); return; }
			}
			else
			{
				// 清除浏览器布局
				clearLayoutBrowse();

				// 初始化浏览器
				if (m_layBrowser == null) { initBrowser(); return; }
	//			if (m_layBrowserOld == null) { initBrowser(); return; }
			}
		}

		// 标签
		if (nP2 == 1)
		{
			// 显示常用标签网格表
			if (m_layBrowser != null) m_layBrowser.doBrowserNews();
//			if (m_layBrowserOld != null) m_layBrowserOld.doBrowserNews();
		}

		// 网址
		if (nP2 == 2)
		{
			// 显示输入网址界面
//			if (m_layBrowser != null) m_layBrowser.doBrowserLoadUrl();
//			if (m_layBrowserOld != null) m_layBrowserOld.doBrowserLoadUrl();
		}

		// 后退
		if (nP2 == 3)
		{
			// 退到浏览器浏览过的上页
			if (m_layBrowser != null) m_layBrowser.doBrowserGoBackPage();
//			if (m_layBrowserOld != null) m_layBrowserOld.doBrowserGoBackPage();
		}

		// 收藏夹
		if (nP2 == 4)
		{
			// 显示收藏标签网格表
			if (m_layBrowser != null) m_layBrowser.doBrowserFavourite();
//			if (m_layBrowserOld != null) m_layBrowserOld.doBrowserFavourite();
		}

		// 收藏
		if (nP2 == 5)
		{
			// 将当前正在浏览的网页添加到收藏标签
			if (m_layBrowser != null) m_layBrowser.doBrowserAddCurrentPageToFavourite();
//			if (m_layBrowserOld != null) m_layBrowserOld.doBrowserAddCurrentPageToFavourite();
		}

		// TV 播放
		// AA 55 D9 98 00 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 77 C3 3C
		if (nP2 == 6)
		{
			// 显示购物标签网格表
//			if (m_layBrowser != null) m_layBrowser.doBrowserShopping();

			try
			{

				// 启动顶部悬浮窗
//				Intent intent = new Intent(this,Shutdown.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				startActivity(intent);


				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					//清除osd
					theApp.clearAllosd(true,false,false);

					// 开启悬浮窗
					theApp.setFloatOn(true);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				Intent intentTop = new Intent(this, FloatTopService.class);
				startService(intentTop);

				// 启动底部悬浮窗
				Intent intentBottom = new Intent(this, FloatBottomService.class);
				startService(intentBottom);

				// 中文
				if (theApp.getLang().equals("ch"))
				{
					// App 包名
					String strPkg = "com.letv.android.client.pad";

					// Activity 类名
					String strCls = "com.letv.android.client.pad.WelcomeActivity";

					// 启动 Activity
					ComponentName componet = new ComponentName(strPkg, strCls);
					Intent intent = new Intent();
					intent.setComponent(componet);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
				}
				else
				{
					// App 包名
					String strPkg = "com.google.android.youtube";

					// Activity 类名
					String strCls = "com.google.android.youtube.app.honeycomb.Shell$HomeActivity";

					// 启动 Activity
					ComponentName componet = new ComponentName(strPkg, strCls);
					Intent intent = new Intent();
					intent.setComponent(componet);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
				}
			}
			catch (Exception e)
			{
			}
		}

		// 音量减小
		if (nP2 == 7)
		{
			// 减小音量
			volumeLower();
		}

		// 音量增大
		if (nP2 == 8)
		{
			// 增大音量
			volumeRaise();
		}

		// 文件管理
		if (nP2 == 11)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				if (!theApp.NOTOP)
				{
					// 启动顶部悬浮窗
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.android.rk";

				// Activity 类名
				String strCls = "com.android.rk.RockExplorer";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}

		// 系统升级
		if (nP2 == 12)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.example.Upgrade";

				// Activity 类名
				String strCls = "com.example.Upgrade.UpgradeActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));

			}
			catch (Exception e)
			{
			}
		}

		// 应用安装
		if (nP2 == 13)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.android.apkinstaller";

				// Activity 类名
				String strCls = "com.android.apkinstaller.apkinstaller";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}

		// 系统设置
		if (nP2 == 14)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.android.settings";

				// Activity 类名
				String strCls = "com.android.settings.Settings";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}

		//  优酷视频
		if (nP2 == 15)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.youku.phone";

				// Activity 类名
				String strCls = "com.youku.phone.ActivityWelcome";

				if (theApp.NOTOP)
				{
					if (theApp.getLang().equals("en"))
					{
						strPkg = "android.rk.RockVideoPlayer";

						// Activity 类名
						strCls = "android.rk.RockVideoPlayer.RockVideoPlayer";
					}
				}
				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}

		//  进入浏览器
		if (nP2 == 16)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.android.browser";

				// Activity 类名
				String strCls = "com.android.browser.BrowserActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}

		//  酷狗音乐
		if (nP2 == 17)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
//				String strPkg = "com.kugou.playerHD";
				String strPkg = "com.kugou.android";

				// Activity 类名
//				String strCls = "com.kugou.playerHD.activity.SplashActivity";
				String strCls = "com.kugou.android.activity.SplashActivity";

				if (theApp.NOTOP)
				{
					if (theApp.getLang().equals("en"))
					{
						// App 包名
						strPkg = "com.android.music";

						// Activity 类名
						strCls = "com.android.music.MusicBrowserActivity";
					}
				}
				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));

			}
			catch (Exception e)
			{
			}
		}
		//  google play音乐
		if (nP2 == 18)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.google.android.music";

				// Activity 类名
				String strCls = "com.android.music.activitymanagement.TopLevelActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}
		//  QQ音乐
		if (nP2 == 19)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.tencent.qqmusicpad";

				// Activity 类名
				String strCls = "com.tencent.qqmusicpad.activity.AppStarterActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}

		//  爱奇艺
		if (nP2 == 20)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}
				// App 包名
				String strPkg = "com.qiyi.video";

				// Activity 类名
				String strCls = "com.qiyi.video.WelcomeActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}
		//  腾讯视频
		if (nP2 == 21)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.tencent.qqlive";

				// Activity 类名
				String strCls = "com.tencent.qqlive.ona.activity.WelcomeActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}
		//  网易云音乐
		if (nP2 == 22)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.netease.cloudmusic";

				// Activity 类名
				String strCls = "com.netease.cloudmusic.activity.LoadingActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}
		//  进入AV IN
		if (nP2 == 23)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				//设置AV状态
				theApp.setAvStatus(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.example.cameratutorial";

				// Activity 类名
				String strCls = "com.example.cameratutorial.MainActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}
		//  进入图片
		if (nP2 == 24)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				// App 包名
				String strPkg = "com.alensw.PicFolder";

				// Activity 类名
				String strCls = "com.alensw.PicFolder.GalleryActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
			}
			catch (Exception e)
			{
			}
		}
		//  进入蓝牙
		if (nP2 == 25)
		{
			try
			{
				// 启动悬浮窗
				theApp.setStartFloat(true);

				// 启用悬浮窗单个开关
				if (theApp.FLOAT_SWITCH)
				{
					// 开启悬浮窗
					theApp.setFloatOn(true);

					//清除osd
					theApp.clearAllosd(true,false,false);

					// 启动开关悬浮窗
					Intent intentSwitch = new Intent(this, FloatSwitchService.class);
					startService(intentSwitch);
				}
				// 不启用悬浮窗单个开关
				else
				{
					// 开启顶部悬浮窗
					theApp.setFloatTopOn(true);

					// 开启底部悬浮窗
					theApp.setFloatBottomOn(true);

					// 启动顶部悬浮窗开关
					Intent intentTopSwitch = new Intent(this, FloatTopSwitchService.class);
					startService(intentTopSwitch);

					// 启动底部悬浮窗开关
					Intent intentBottomSwitch = new Intent(this, FloatBottomSwitchService.class);
					startService(intentBottomSwitch);
				}

				// 启动顶部悬浮窗
				if (!theApp.NOTOP)
				{
					Intent intentTop = new Intent(this, FloatTopService.class);
					startService(intentTop);
				}

				// 启动底部悬浮窗
				if (!theApp.NOBOTTOM)
				{
					Intent intentBottom = new Intent(this, FloatBottomService.class);
					startService(intentBottom);
				}

				 //进入蓝牙设置界面
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));

				// App 包名
			/*	String strPkg = "com.alensw.PicFolder";

				// Activity 类名
				String strCls = "com.alensw.PicFolder.GalleryActivity";

				// 启动 Activity
				ComponentName componet = new ComponentName(strPkg, strCls);
				Intent intent = new Intent();
				intent.setComponent(componet);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));*/
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * 上传用户
	 * @param user
	 */
	public void uploadUser(final UserData user)
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			if (user == null) return;

			// 检测账户信息
			if (theApp.getAccInfo() == null) return;

			// 线程提交登陆信息
			new Thread()
			{
				public void run()
				{
					try
					{
						// 上传用户
						boolean bIsSuccess = user.uploadUser();

						// 上传成功或失败的提示信息
						final String strUploadSuccess = MainActivity.this.getString(R.string.treadmill_upload_success).toString();
						final String strUploadFalse = MainActivity.this.getString(R.string.treadmill_upload_false).toString();

						// 提示信息
						final String strMsg = bIsSuccess ? strUploadSuccess : strUploadFalse;
						Message msg = m_handler.obtainMessage();
						msg.what = MSG_TOAST;
						msg.obj = strMsg;
						m_handler.sendMessage(msg);
					}
					catch (Exception e)
					{
						Log.d(TAG, "error:" + e.getMessage());
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 查询数据数量
	 */
	public void queryDataSize()
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			// 检测账户信息
			if (theApp.getAccInfo() == null) return;

			// 线程提交查询记录信息
			new Thread()
			{
				public void run()
				{
					boolean bIsSuccess = false;

					// 页面参数
//					int nPage = 0;
					int nTotal = 0;
					int nTotalTemp = -1;
					final int nPageSize = 1;
					final int nCurrentPage = 1;

					// 数据数量
					theApp.setDataSize(0);

					try
					{
						// 查询记录接口
						String strUrl = theApp.getUrl() + "queryTreadmillListPage.ct?";

						// 查询记录参数
						List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
						listParams.add(new BasicNameValuePair("member.id", "" + theApp.getAccInfo().getNMemberId()));
						listParams.add(new BasicNameValuePair("m_nCurrentPage", "" + nCurrentPage));
						listParams.add(new BasicNameValuePair("m_nPageSize", "" + nPageSize));

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

						// 判断是否查询成功
						if (json.has("page"))
						{
							bIsSuccess = true;

							if (json.has("total"))
							{
								nTotal = json.getInt("total");
							}
						}
					}
					catch (Exception e)
					{
						android.util.Log.e(TAG, "error:" + e.getMessage());
					}

					// 数据数量
					nTotalTemp = bIsSuccess ? nTotal : -1;
					theApp.setDataSize(nTotalTemp);

					// 查询成功
					if (bIsSuccess && nTotalTemp > 0)
					{
						String strMsg = String.valueOf(nTotalTemp);
						Message msg = m_handler.obtainMessage();
						msg.what = MSG_QUERY_OK;
						msg.obj = strMsg;
						m_handler.sendMessage(msg);
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 下载数据
	 */
	public void downloadData()
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			// 检测账户信息
			if (theApp.getAccInfo() == null) return;

			// 查询数据数量
			queryDataSize();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 下载数据
	 * @param b
	 */
	public void downloadData(final boolean b)
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			// 检测账户信息
			if (theApp.getAccInfo() == null) return;

			// 检测数据数量
			if (theApp.getDataSize() < 1) return;

			// 线程提交查询记录信息
			new Thread()
			{
				public void run()
				{
					boolean bIsSuccess = false;

					// 页面参数
					int nPageSize = 1024;
					int nPageNumber = 1;
					int nCurrentPage = 1;

					try
					{
						// 数据数量
						int nDataSize = theApp.getDataSize();

						// 检测数据数量
						if (nPageSize > nDataSize)
						{
							nPageSize = nDataSize;
						}
						else
						{
							double pn = (double)nDataSize / (double)nPageSize;

							if (pn > (int)pn)
							{
								// 页面数量
								nPageNumber = (int)pn + 1;
							}
						}

						// 清空数据链表
						theApp.getListUserDataS().clear();

						for (int i = 0; i < nPageNumber; i++)
						{
							// 当前页
							nCurrentPage = i;

							// 查询记录接口
							String strUrl = theApp.getUrl() + "queryTreadmillListPage.ct?";

							// 查询记录参数
							List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
							listParams.add(new BasicNameValuePair("member.id", "" + theApp.getAccInfo().getNMemberId()));
							listParams.add(new BasicNameValuePair("m_nCurrentPage", "" + nCurrentPage));
							listParams.add(new BasicNameValuePair("m_nPageSize", "" + nPageSize));

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

							// 判断是否查询成功
							if (json.has("page"))
							{
								bIsSuccess = true;

								if (json.has("rows"))
								{
									JSONArray jsonTreadmills = json.getJSONArray("rows");

									if (null != jsonTreadmills && jsonTreadmills.length() > 0)
									{
										for (int j = 0; j < jsonTreadmills.length(); j++)
										{
											JSONObject jsonTread = jsonTreadmills.getJSONObject(j);

											if (null != jsonTread && jsonTread.has("treadmillId"))
											{
												UserData data = new UserData();

												if (jsonTread.has("uploadTime"))
												{
													// 上传时间
													String strCurTime = jsonTread.getString("uploadTime");
													long lTime = theApp.time2long(strCurTime);
													data.setCurTime(lTime);
												}

												if (jsonTread.has("exerciseDuration"))
												{
													int nValue = 0;

													if (false == "".equals("" + jsonTread.getString("exerciseDuration")))
													{
														// 煅炼时长
														nValue = jsonTread.getInt("exerciseDuration");
													}

													// 时间
													data.setTime(nValue);
												}

												if (jsonTread.has("runningDistance"))
												{
													double dValue = 0;

													if (false == "".equals("" + jsonTread.getString("runningDistance")))
													{
														dValue = jsonTread.getDouble("runningDistance");
													}

													// 距离
													double dist = dValue; // 米
													dist /= 1000; // 千米
													data.setDist(dist);
												}

												if (jsonTread.has("runningSteps"))
												{
													int nValue = 0;

													if (false == "".equals("" + jsonTread.getString("runningSteps")))
													{
														nValue = jsonTread.getInt("runningSteps");
													}

													// 步数
													data.setStep(nValue);
												}

												if (jsonTread.has("consumeCalorie"))
												{
													int nValue = 0;

													if (false == "".equals("" + jsonTread.getString("consumeCalorie")))
													{
														nValue = jsonTread.getInt("consumeCalorie");
													}

													// 卡路里
													data.setCalo(nValue);
												}

												// 添加数据
												theApp.getListUserDataS().add(data);
											}
										}
									}
								}
							}
						}
					}
					catch (Exception e)
					{
						android.util.Log.e(TAG, "error:" + e.getMessage());
					}

					// 下载成功
					if (bIsSuccess)
					{
						String strMsg = MainActivity.this.getString(R.string.treadmill_download_success);
						Message msg = m_handler.obtainMessage();
						msg.what = MSG_TOAST;
						msg.obj = strMsg;
						m_handler.sendMessage(msg);
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 下载本年数据
	 */
	public void downloadDataYear()
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			// 检测账户信息
			if (theApp.getAccInfo() == null) return;

			// 线程提交查询记录信息
			new Thread()
			{
				public void run()
				{
					boolean bIsNull = true;
					boolean bIsSuccess = false;

					try
					{
						// 清空数据链表
						theApp.getListUserDataS().clear();

						// 年份
						final Calendar calendar = Calendar.getInstance();
						calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
						int nYear = calendar.get(Calendar.YEAR);

						for (int i = 0; i < theApp.ACC_TYPE_NUM; i++)
						{
							// 查询记录接口
							String strUrl = theApp.getUrl() + "queryTreadmillListAll.ct?";

	                    	// 查询记录参数
	                    	List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
	                    	listParams.add(new BasicNameValuePair("user.loginName", "" + theApp.getAccount()));
	                    	listParams.add(new BasicNameValuePair("member.typeName", "" + i));
	                    	listParams.add(new BasicNameValuePair("m_nYear", "" + nYear));

	                    	// 参数转 utf-8  的 url 编码
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
	    	                String strResponse= httpClient.execute(getMethod, responseHandler);

	    	                // 转成 utf-8 的编码, 若还是乱码则用 unicode 编码
	    	                strResponse = new String(strResponse.getBytes("ISO-8859-1"),"UTF-8");

	    	                // 返回值转 json 数据格式
	    	                JSONArray jsonTreadmills = new JSONArray(strResponse);

	    	                if (!strResponse.equals("[]"))
	    	                {
	    	                	bIsNull = false;
	    	                }

	                        // 判断是否查询成功
	    	                if (null != jsonTreadmills && jsonTreadmills.length() > 0)
	    	                {
	                        	bIsSuccess = true;

	    	                	for(int j = 0; j < jsonTreadmills.length(); j++)
	    	                	{
	    	                		JSONObject jsonTread = jsonTreadmills.getJSONObject(j);

	    	                		if (null != jsonTread && true == jsonTread.has("treadmillId"))
	    	                		{
	    	                			UserData data = new UserData();

	    	                			// 用户编号
	    	                			data.setNum(i);

										if (jsonTread.has("uploadTime"))
										{
											// 上传时间
											String strCurTime = jsonTread.getString("uploadTime");
											long lTime = theApp.time2long(strCurTime);
											data.setCurTime(lTime);
										}

										if (jsonTread.has("exerciseDuration"))
										{
											int nValue = 0;

											if (false == "".equals("" + jsonTread.getString("exerciseDuration")))
											{
												// 煅炼时长
												nValue = jsonTread.getInt("exerciseDuration");
											}

											// 时间
											data.setTime(nValue);
										}

										if (jsonTread.has("runningDistance"))
										{
											double dValue = 0;

											if (false == "".equals("" + jsonTread.getString("runningDistance")))
											{
												dValue = jsonTread.getDouble("runningDistance");
											}

											// 距离
											double dist = dValue; // 米
											dist /= 1000; // 千米
											data.setDist(dist);
										}

										if (jsonTread.has("runningSteps"))
										{
											int nValue = 0;

											if (false == "".equals("" + jsonTread.getString("runningSteps")))
											{
												nValue = jsonTread.getInt("runningSteps");
											}

											// 步数
											data.setStep(nValue);
										}

										if (jsonTread.has("consumeCalorie"))
										{
											int nValue = 0;

											if (false == "".equals("" + jsonTread.getString("consumeCalorie")))
											{
												nValue = jsonTread.getInt("consumeCalorie");
											}

											// 卡路里
											data.setCalo(nValue);
										}

										// 添加数据
										theApp.getListUserDataS().add(data);
	    	                		}
	    	                	}
	    	                }
						}
					}
					catch (Exception e)
					{
						android.util.Log.e(TAG, "error:" + e.getMessage());
					}

					// 空数据
					if (bIsNull)
					{
					}
					// 非空数据
					else
					{
						// 下载失败
						if (!bIsSuccess)
						{
							String strMsg = MainActivity.this.getString(R.string.treadmill_download_false);
							Message msg = m_handler.obtainMessage();
							msg.what = MSG_DOWNLOAD;
							msg.obj = strMsg;
							m_handler.sendMessage(msg);
						}
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 是否重新下载
	 */
	private void whetherRedownload()
	{
		try
		{
			new AlertDialog.Builder(m_context)
				.setTitle(R.string.treadmill_whether_redownload)
				.setPositiveButton(R.string.treadmill_yes, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					// 下载本年数据
					downloadDataYear();
				}
			})
			.setNegativeButton(R.string.treadmill_no, null)
			.create().show();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 上传数据
	 * @param data
	 */
	public void uploadData(final UserData data)
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			if (data == null) return;

			// 检测账户信息
			if (theApp.getAccInfo() == null) return;

			// 线程提交登陆信息
			new Thread()
			{
				public void run()
				{
					try
					{
						// 上传数据
						boolean bIsSuccess = data.uploadData();

						if (bIsSuccess)
						{
							// 写 data.txt 文件
							theApp.writeDataFile();
						}

						// 上传成功或失败的提示信息
						final String strUploadSuccess = MainActivity.this.getString(R.string.treadmill_upload_success).toString();
						final String strUploadFalse = MainActivity.this.getString(R.string.treadmill_upload_false).toString();

						// 提示信息
						final String strMsg = bIsSuccess ? strUploadSuccess : strUploadFalse;
						Message msg = m_handler.obtainMessage();
						msg.what = MSG_TOAST;
						msg.obj = strMsg;
						m_handler.sendMessage(msg);
					}
					catch (Exception e)
					{
						Log.d(TAG, "error:" + e.getMessage());
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 上传TB数据
	 * @param data
	 */
	public void uploadTBData(final UserData data)
	{
		try
		{

//			if (data == null) return;

			// 线程提交登陆信息
			new Thread()
			{
				public void run()
				{
					try
					{
						// 上传数据
						boolean bIsSuccess = data.uploadTbdata();

						if (bIsSuccess)
						{
							// 写 data.txt 文件
//							theApp.writeDataFile();
							theApp.showMyosd("连接成功",200,300,(byte)0x03,(byte)0x22);
						}
						else
							theApp.showMyosd("连接失败",200,300,(byte)0x03,(byte)0x22);

						// 上传成功或失败的提示信息
						final String strUploadSuccess = MainActivity.this.getString(R.string.treadmill_upload_success).toString();
						final String strUploadFalse = MainActivity.this.getString(R.string.treadmill_upload_false).toString();

						// 提示信息
						final String strMsg = bIsSuccess ? strUploadSuccess : strUploadFalse;
						Message msg = m_handler.obtainMessage();
						msg.what = MSG_TOAST;
						msg.obj = strMsg;
						m_handler.sendMessage(msg);
					}
					catch (Exception e)
					{
						Log.d(TAG, "error:" + e.getMessage());
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 上传 data.txt 文件
	 */
	private void uploadDataTxt()
	{
		try
		{
			// 未登陆
			if (!theApp.isLogined())
			{
				// 布局视图登陆
				layoutViewLogin();
				return;
			}

			// 数量
			int nSizeUserL = theApp.getListUserL().size();

			if (nSizeUserL < 1)
			{
				// 读 user.txt 文件
				theApp.readUserFile();
			}

			// 数量
			int nSizeData = theApp.getListUserDataL().size();

			if (nSizeData < 1)
			{
				// 读 data.txt 文件
				theApp.readDataFile();
			}

			// 线程提交登陆信息
			new Thread()
			{
				public void run()
				{
					try
					{
						boolean bUpload = true;

						// 数量
						int nSizeDataL = theApp.getListUserDataL().size();

						for (int i = 0; i < nSizeDataL; i++)
						{
							// 数据
							UserData data = theApp.getListUserDataL().get(i);

							// 是否已上传数据
							if (!data.isUploaded())
							{
								// 上传数据
								if (!data.uploadData())
								{
									bUpload = false;
									continue;
								}
							}
						}

						if (bUpload)
						{
							// 上传成功
							String strMsg = MainActivity.this.getString(R.string.treadmill_upload_success);
							Message msg = m_handler.obtainMessage();
							msg.what = MSG_TOAST;
							msg.obj = strMsg;
							m_handler.sendMessage(msg);
						}
					}
					catch (Exception e)
					{
						android.util.Log.e(TAG, "error:" + e.getMessage());
					}
				}
			}.start();
		}
		catch (Exception e)
		{
		}
	}

	public static void checkRenew(final String openId , final String machineId, final Context context,final Long endTime){//,Long endTime
		Handler handler = new Handler();
		Long currentTime = new Date().getTime();//程序执行到这里时候的时间,
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mDialog.dismiss();
				//打印看效果
				Date date = new Date();
				System.out.println("S------->>>" + "mDialog要消失,剩下一分钟就要关机了"+date);
			}
		},endTime-currentTime-1000*60*1-500);//剩下一分钟的时候执行消失命令
	//	},1000*60*5-500);
		System.out.println("S------->>>" + "checkRenew");
		show11(openId, machineId, context);
	}

	private static void show11(final String openId, final String machineId, final Context context) {
		String[] PLANETS = new String[]{"15","30","45","60"};
		View outerView = LayoutInflater.from(context).inflate(R.layout.time_dialog, null);
		final WheelView wv        = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
		wv.setOffset(2);
		wv.setItems(Arrays.asList(PLANETS));
		wv.setSeletion(3);
		AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		builder.setTitle("请选择续约时长(单位:分钟)").setView(outerView);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				final String yyy = wv.getSeletedItem();

				//保存预约信息接口
String ddd = theApp.baseUrl + theApp.saveAppointUrl + "?openId=" + openId+"&machineId="+machineId+"&durationTime="+yyy;
				System.out.println("S------->>>" + "确定"+yyy+"有爱"+ddd);
				OkhttpTool.doget(theApp.baseUrl + theApp.saveAppointUrl + "?openId=" + openId+"&machineId="+machineId+"&durationTime="+yyy,new MyCallback(new StringPaser()){
					@Override
					public void onSuccess(String string) {
						try {
							JSONObject jsonObject   = new JSONObject(string);
							boolean      canBook    = jsonObject.optBoolean("canBook");
							String msg          = jsonObject.optString("msg");
							String       reNewId      = jsonObject.optString("reNewId");
							String       startTime    = jsonObject.optString("startTime");
							String       price        = jsonObject.optString("price");
							String       durationTime = yyy;
							String       mmachineId = machineId;
							System.out.println("S------->>>" + "string的值"+string);
							//canBook = true;
							//显示msg
							System.out.println("S------->>>" + "machineId的值"+machineId);
							show22(context,msg,durationTime,mmachineId,openId,startTime,price,canBook);

						}catch (Exception e){
							System.out.println("S------->>>" + "确认发生异常"+e.toString());

						}
					}

					@Override
					public void onFailed() {
						super.onFailed();
						System.out.println("S------->>>" + "onFailed");
					}
			});


			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
//		builder .create()
//				.setCanceledOnTouchOutside(false);
//		builder.setCancelable(false);
//		builder.show();
		mDialog = builder .create();
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.show();
	}

	private static void show22(final Context context, String msg,
							   final String durationTime,final String mmachineId,
							   final String openId, final String startTime, final String price,
							   final Boolean canBook) {
		System.out.println("S------->>>" + "show22"+"machineId"+mmachineId+"mmachineId"+mmachineId);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("您的预约信息");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				if (canBook){
String uu = theApp.baseUrl + theApp.pushRenewInfoUrl+"?machineId="+mmachineId+"" +
		"&durationTime="+durationTime+"&openId="+openId+"&price="+price+"&startTime="+startTime;
					System.out.println("S------->>>" + "uu:"+uu+"=mmachineId="+mmachineId);
					//推送renewInfo接口(pushRenewInfo)
					OkhttpTool.doget(theApp.baseUrl + theApp.pushRenewInfoUrl+"?machineId="+mmachineId+"" +
											 "&durationTime="+durationTime+"&openId="+openId+"&price="+price+"&startTime="+startTime,new MyCallback(new StringPaser()){
						@Override
						public void onSuccess(String string) {
							super.onSuccess(string);
							System.out.println("S------->>>" + "pushRenewInfoUrl+++onSuccess");
							try {
								JSONObject   jsonObject   = new JSONObject(string);
								boolean      isSuccess    = jsonObject.optBoolean("isSuccess");
								String        msg          = jsonObject.optString("msg");
								String       durationTime        = jsonObject.optString("durationTime");
								String       openId    = jsonObject.optString("openId");
								String 		 title        ="renewInfo";

							} catch (JSONException e) {
								e.printStackTrace();
								System.out.println("S------->>>" + "pushRenewInfoUrl+++JSONException"+e.toString());
							}
						}

						@Override
						public void onFailed() {
							super.onFailed();
							System.out.println("S------->>>" + "pushRenewInfoUrl+++onFailed");
						}
					});
				}else{//isSuccess为false时候跳到选择时长界面
					System.out.println("S------->>>" + "pushRenewInfoUrlisSuccess为false时候跳到选择时长界面");
					//checkRenew(openId, mmachineId,context);
					show11(openId, mmachineId,context);

				}
				dialogInterface.dismiss();
			}
		});
		if (canBook){//如果为false,不显示"取消",为true的话才有"取消"
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Toast.makeText(context, "您已取消预约", Toast.LENGTH_LONG)
					 .show();
				dialogInterface.dismiss();
			}
		});}
		AlertDialog dialog =  builder .create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog .getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}



	/*public static void checkRenew(final String openid , final String machineId){
		AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		builder.setMessage("还有5分钟，是否需要续费？");

		builder.setTitle("提示");

		builder.setPositiveButton("续费", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//保存预约信息接口
				OkhttpTool.doget(theApp.baseUrl + theApp.saveAppointUrl + "?openId=" + openid+"&machineId="+machineId,new MyCallback(new StringPaser()){
					@Override
					public void onSuccess(String string) {
						try {
							JSONObject jsonObject = new JSONObject(string);
							boolean isSuccess = jsonObject.getBoolean("isSuccess");
							String msg = jsonObject.getString("msg");
							String reNewId = jsonObject.getString("reNewId");
							String startTime = jsonObject.getString("startTime");
							String price = jsonObject.getString("price");
							String durationTime = "30";

							//dyp
							if (isSuccess){
								//推送renewInfo接口(pushRenewInfo)
								OkhttpTool.doget(theApp.baseUrl + theApp.pushRenewInfoUrl+"?machineId="+machineId+"" +
														 "&durationTime="+durationTime+"&openId="+openid+"&=price"+price+"&startTime="+startTime,new MyCallback(new StringPaser()){
									@Override
									public void onSuccess(String string) {
										super.onSuccess(string);
									}

									@Override
									public void onFailed() {
										super.onFailed();
									}
								});
							}else{

							}

						}catch (Exception e){

						}
					}

					@Override
					public void onFailed() {
						super.onFailed();
					}
				});
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("不需要",null);

		builder.create().show();
	}*/


	/**
	 * 判断本地是否有id
	 */
	public static void checkID(final String mac, final String id, final String channelId, final SharedPreferences sp) {
		Log.d(TAG, "%%%%%%%%%%%%%%checkID");
		new Thread() {
			@Override
			public void run() {
				String url = theApp.baseUrl+"/getMachineId?";
				url += "mac=" + mac + "&" + "id=" + id + "&" + "channelId=" + channelId;
				OkhttpTool.doget(url,new MyCallback(new StringPaser()){
					@Override
					public void onSuccess(String string) {
						Log.d(TAG, "%%%%%%%%%%%%%%onSuccess");
						try {
							JSONObject jsonObject = new JSONObject(string);
							boolean isSuccess = jsonObject.getBoolean("isSuccess");
							String returnId;
							//dyp
							String amachineId = jsonObject.optString("machineId");
							String msg = jsonObject.optString("msg");
							Log.d(TAG, "%%%%%%%%%%%%%%"+amachineId+isSuccess+msg+">>");
							if (isSuccess){
								returnId = jsonObject.optString("id");
								//判断返回的id是否与本地一致
								//if (!returnId.equals(id)){
								if (!amachineId.equals(id)){
									//将本地的id改为传过来的id
									SharedPreferences.Editor ed = sp.edit();
									//ed.putString("id",returnId);
									ed.putString("id",amachineId);
									ed.commit();
									Log.d(TAG, "%%%%%%%%%%%%%%"+">>LLLLLLLL我已经替换本地值");
								}

							}else {

								//弹出设置id界面
								final EditText et = new EditText(m_context);
								et.clearFocus();
								new AlertDialog.Builder(m_context).setTitle("输入场地ID")
										.setIcon(android.R.drawable.ic_dialog_info)
										.setView(et)
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												final String input = et.getText().toString();
												if (input.equals("")) {
													Toast.makeText(m_context, "不能为空！" + input, Toast.LENGTH_LONG).show();
													return;
												}
												else {
													InputMethodManager imm = (InputMethodManager)m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
													imm.hideSoftInputFromWindow(et.getWindowToken(),0);

													OkhttpTool.doget(theApp.baseUrl + theApp.saveIdUrl +"?mac="+mac +"&id="+input+"&channelId="+channelId,
															new MyCallback(new StringPaser()){
																@Override
																public void onSuccess(String string) {
																	Log.d(TAG, "%%%%%%%%%%%%%%"+"saveIdUrl+onSuccess+string"+string);
																	JSONObject jsonObject = null;
																	try {
																		jsonObject = new JSONObject(string);
																		boolean isSuccess = jsonObject.getBoolean("isSuccess");
																		String machineId = jsonObject.optString("machineId");
																		String msg = jsonObject.optString("msg");
																		if (isSuccess){
																			//Toast.makeText(m_context,"保存成功",Toast.LENGTH_LONG).show();
																			Toast toast = Toast.makeText(m_context, msg, Toast.LENGTH_LONG);
																			toast.setGravity(Gravity.CENTER, 0, 0);
																			toast.show();
																			//记录本地
																			SharedPreferences.Editor ed = sp.edit();
																			//ed.putString("id",input);
																			Log.d("MainActivity", "%%%%%%%%%%%%%%saveIdUrl+isSuccess"+machineId);
																			ed.putString("id",machineId);
																			ed.commit();
																		}else {
																			//Toast.makeText(m_context,"保存失败",Toast.LENGTH_LONG).show();
																			Toast toast = Toast.makeText(m_context, msg, Toast.LENGTH_LONG);
																			toast.setGravity(Gravity.CENTER, 0, 0);
																			toast.show();
																		}
																	} catch (JSONException e) {
																		e.printStackTrace();
																	}
																}
																@Override
																public void onFailed() {
																	super.onFailed();
																	Log.d(TAG, "%%%%%%%%%%%%%%"+"saveIdUrl+onFailed");
																}
															});
													dialog.dismiss();
												}
											}
										})
										.setNegativeButton("取消", null)
										.show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.d(TAG, "%%%%%%%%%%%%%%"+"有异常"+e.toString());
						}
					}
					@Override
					public void onFailed() {
						Log.d(TAG, "%%%%%%%%%%%%%%"+"onfailed2");
						super.onFailed();
					}
				});
			}
		}.start();
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * 消息处理器
	 */
	private final Handler m_handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			try
			{
				String str = "";

				switch (msg.what)
				{
				// 恢复
				case MSG_RESUME:
					resume();
					break;

				// 重绘
				case MSG_REDRAW:
					redraw();
					break;

				// 初始化 Apps
				case MSG_INIT_A:
					initApps();
					break;

				// 初始化音乐
				case MSG_INIT_M:
					initMusic();
					break;

				// 初始化视频
				case MSG_INIT_V:
					initVideo();
					break;

				// 初始化图片
				case MSG_INIT_P:
					initPicture();
					break;

				// 获取wifi强度
				case MSG_GET_WIFI:
					getWifiInfo();
					theApp.drawBmp();
					break;

				// 连接服务器
				case MSG_LOAD_SEVER:
//					loadInternet();
					break;


				// 关机
				case MSG_SHUTDWON:
					shutdown();
					break;

				// 获取扩展 SDCard 列表
				case MSG_EXT_LIST:

					// 检测是否正在加载
//					theApp.SLog(TAG, "LoadingMusic: " + String.valueOf(theApp.isLoadingMusic()));
//					theApp.SLog(TAG, "LoadingVideo: " + String.valueOf(theApp.isLoadingVideo()));
//					theApp.SLog(TAG, "LoadingPicture: " + String.valueOf(theApp.isLoadingPicture()));
					if (!theApp.isLoadingMusic() &&
						!theApp.isLoadingVideo() &&
						!theApp.isLoadingPicture())
					{
						// 获取扩展 SDCard 列表
//						Log.d(TAG, "getExtStorageList");
//						theApp.TLog(TAG, "getExtStorageList");
//						theApp.SLog(TAG, "getExtStorageList");
//						getExtStorageList();
					}
					break;

				// 状态改变
				case BtSppService.MSG_BT_STATE_CHANGE:
					switch (msg.arg1)
					{
					// 连接
					case BtSppService.STATE_CONNECTED:

						// 初始化缓冲区
						theApp.initBuffer();

						// 已启动
						started();
						break;

					// 正在连接...
					case BtSppService.STATE_CONNECTING:
						break;

					// 无连接
					case BtSppService.STATE_LISTEN:
					case BtSppService.STATE_NONE:
						break;
					}
					break;

				// 读数据
				case BtSppService.MSG_BT_READ:
					try
					{
						byte[] buffer = (byte[])msg.obj;
						int nBytes = Math.min(msg.arg1, buffer.length);

						if (nBytes > 0)
						{
							// 读缓冲区
							theApp.readBuffer(nBytes, buffer);

							// 调试
							if (theApp.DEBUG)
							{
								try
								{
									byte[] readBuf = new byte[nBytes];

									for (int i = 0; i < nBytes; i++)
									{
										// 缓冲区
										readBuf[i] = buffer[i];
									}

									// 构造字符串
									String strRead = new String(readBuf, 0, nBytes);

									if (strRead.equals("exit"))
									{
										// 退出
										exit();
									}

									if (strRead.equals("resetMedia"))
									{
										// 重置媒体
										resetMedia();
									}

/*									final int[] EXIT = { 69, 88, 73, 84 };

									// 转成大写
									if (strRead.toUpperCase().equals("EXIT") ||
										(readBuf[0] == EXIT[0] &&
										 readBuf[1] == EXIT[1] &&
										 readBuf[2] == EXIT[2] &&
										 readBuf[3] == EXIT[3]))
									{
										// 退出
										exit();
									}*/
								}
								catch (Exception e)
								{
								}
							}
						}
					}
					catch (Exception e)
					{
					}
					break;

				// 写数据
				case BtSppService.MSG_BT_WRITE:
					break;

				// 设备名称
				case BtSppService.MSG_BT_DEVICE_NAME:
					String strConnectedTo = getString(R.string.strConnectedTo);

					// 设备名称
					ms_strConnectedDevice = msg.getData().getString(BtSppService.DEVICE_NAME);
					str = strConnectedTo + ": " + ms_strConnectedDevice;
					Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//					theApp.SLog(TAG, "ConnectedTo: " + ms_strConnectedDevice);

					// 蓝牙设备
					theApp.setBtDevice(ms_strConnectedDevice);
					break;

				// 设备连接中断。
				// 无法连接到设备。
				case BtSppService.MSG_BT_TOAST:
					Toast.makeText(getApplicationContext(), msg.getData().getString(BtSppService.TOAST), Toast.LENGTH_SHORT).show();
					break;

				// 拷贝
				case MSG_COPY:
					// 拷贝 Zip 文件
					copyZipFile();
					break;

				// 拷贝完成
				case MSG_COPY_OK:
					//开始解压
//					theApp.setCopyPicture(true);

					// 解压 Zip 文件
					unzip();
					break;

				// 解压完成
				case MSG_UNZIP_OK:

					// 设置控件值
					setCtrlValue();
//					nWifiFlag = true;
					break;

				// 解压完成
				case MSG_REUNZIP:

					//开始拷贝图片
//					theApp.setCopyPicture(true);

					//发送解压完成命令
//					theApp.responseKey(0, 0, 77);

					// 删除文件
					deleteTB();
					break;

				// 解压完成
				case MSG_RECOPY:

					//检测zip文件
					showProgDlg();

					break;

					case MSG_SETID:

						break;

				}
			}
			catch (Exception e)
			{
			}
		}
	};

	//----------------------------------------------------------------------------------------------------
	/**
	 * 移动线程
	 */
	private class MoveThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();

			// 未退出
			while (!theApp.isExit())
			{
				try
				{
					// 休眠 1 毫秒
					Thread.sleep(1);

					// 获取媒体音量
					/*int nSV = theApp.getStreamVolume(MainActivity.this);
					theApp.setStreamVolume(nSV);*/

					//显示关机提示
					if (theApp.isShutdown())showPowerOff();

					if (theApp.USE_TBSEVER)
					{
						theApp.setiCounter(theApp.getiCounter() + 1);

						if (theApp.getiCounter()>= 1500&&
								(theApp.getiCounter() %  1500 == 0))
						{
							//计数清零
							theApp.setiCounter(0);

							if (theApp.getLoginRsult() == 1)
							{
								loadInternet(1);
								theApp.setLoginRsult(0);
							}
							else if (theApp.getLoginRsult() == 2)
							{
								loadInternet(2);
								theApp.setLoginRsult(0);
							}
							else loadInternet(0);

							//检测网络是否联通，如果联通上传数据
//							if (checkInternetState())
//								uploadTBData(tbdata);
						}
					}
					//关闭wifi
					if (theApp.isShutdown())
					{
						//等待时间
						theApp.setWaiteTime(theApp.getWaiteTime() + 1);

						if (theApp.getWaiteTime() >= 100)
						{
							closewifi();

							if (ms_wifiState == WifiManager.WIFI_STATE_DISABLED)
							{
								if (!getWifiState()) m_handler.sendEmptyMessage(MSG_SHUTDWON);
							}

							theApp.setWaiteTime(0);
						}
//							closeWifi();
					}

					// 正在移动
					if (theApp.isMoving())
					{
						// 移动时间
						theApp.setMoveTime(theApp.getMoveTime() + 1);

						// 移动时间等于或超过 100 毫秒且间隔 100 毫秒
						if ((theApp.getMoveTime() >= 100) &&
							(theApp.getMoveTime() %  100 == 0))
						{
							// 应答移动
							theApp.setResponseMove(true);
//							Log.d(TAG, "ResponseMove: " + theApp.isResponseMove());
						}
					}

					// 开始检测图片数量
					if (theApp.isCheckNume())
					{
						// 比较时间
						theApp.setCheckTime(theApp.getCheckTime() + 1);
						// 比较时间等于或超过 5000 毫秒且间隔 5000 毫秒
					/*	if ((theApp.getCheckTime() >= 15000) &&
							(theApp.getCheckTime() %  15000 == 0))
						{
							int key = 0;
							key = (int) (Math.random() * 65535);
							theApp.setKey(String.valueOf(key));

//							theApp.setGc(true);
						}*/

						// 比较时间等于或超过 5000 毫秒且间隔 5000 毫秒
						if ((theApp.getCheckTime() >= 4000) &&
							(theApp.getCheckTime() %  4000 == 0))
						{
							//获取图片数量
//							loadPicNum();

							//获取内存信息
							/*long memory = */getAvailMemory(m_context);
							/*	if (memory <= 60)
							{
								// 关闭进程
								killProcessesEx();
								killProcesses();
								Intent mainIntent = new Intent(m_context, MainActivity.class);

								// 在广播接收器中显示 Activity, 必须设置 FLAG_ACTIVITY_NEW_TASK 标志
								mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								m_context.startActivity(mainIntent);

							}*/

							// 应答校验
							if (theApp.getStorageNum() != theApp.getPicNum())
							{

								if (!theApp.isCopyPicture())
								{
									if (!theApp.USE_ZIP)
									{
										theApp.responseKey(0, 0, 75);
									}
									else
									{
										//文件丢失提醒
									/*	if (theApp.getLang().equals("ch"))
											theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x22);
										else
											theApp.showMyosd(ms_strPicToastEn,200,300,(byte)0x03,(byte)0x22);	*/


//										theApp.responseKey(0, 0, 75);

										// 重新解压
//										m_handler.sendEmptyMessage(MSG_REUNZIP);

										// 拷贝 Zip 文件
//										copyZipFile("TB-TECH-HMI.zip");

										//开始解压
//										theApp.setCopyPicture(true);
									}
								/*	if (theApp.getLang().equals("ch"))
										theApp.showMyosd(ms_strPicToastCh,200,300,(byte)0x03,(byte)0x22);
									else
										theApp.showMyosd(ms_strPicToastEn,200,300,(byte)0x03,(byte)0x22);*/
	//								theApp.MYSLog(TAG,"mcu" + theApp.getStorageNum());
	//								theApp.MYSLog(TAG,"amlogic" + theApp.getPicNum());
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					return;
				}
			}
		}

		private void showPowerOff() {}
	}
	/**
	 * 检测wifi强度线程
	 */
	private class CheckWifiThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();

			// 未退出
			while (!theApp.isExit())
			{
				try
				{
					Thread.sleep(1 * 1000);
					// 获取wifi强度
					m_handler.sendEmptyMessage(MSG_GET_WIFI);
//					getWifiInfo();
//					theApp.drawBmp();

				}
				catch (Exception e)
				{
					return;
				}
			}
		}
	}
	/**
	 * 连接服务器线程
	 */
	private class LoadSeverThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();

			// 未退出
			while (!theApp.isExit())
			{
				try
				{
					Thread.sleep(2 * 1000);

					// 获取wifi强度
					m_handler.sendEmptyMessage(MSG_LOAD_SEVER);
//					getWifiInfo();

				}
				catch (Exception e)
				{
					return;
				}
			}
		}
	}

	/**
	 * 读串口线程
	 */
	private class ReadSpThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			if (theApp.CH340)
			{
				// 未退出
				while (!theApp.isExit())
				{
					byte[] buffer1 = new byte[theApp.BUFFER_SIZE];

					try
					{
						Thread.sleep(50);
					}
					catch(InterruptedException e)
					{

					}
					// 读输入流
					int nBytes = uartInterface_340.ReadData(buffer1,theApp.BUFFER_SIZE);

					if (nBytes > 0)
					{
					//	Toast.makeText(m_context, "接收到数据", Toast.LENGTH_LONG).show();
						// 接收数据
						onDataReceived(nBytes, buffer1);
					}


				}
			}
			else
			{
				// 未退出
				while (!isInterrupted() && !theApp.isExit())
				{
					try
					{
	//					byte[] buffer = new byte[64];
						byte[] buffer = new byte[theApp.BUFFER_SIZE];

						if (theApp.getInput() == null) return;

						// 读输入流
						int nBytes = theApp.getInput().read(buffer);

						if (nBytes > 0)
						{
							// 接收数据
							onDataReceived(nBytes, buffer);
						}
					}
					catch (IOException e)
					{
						return;
					}
				}
			}
		}
	}

	/**
	 * 系统信息线程
	 */
/*	private class SysInfoThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();

			// 未退出
			while (!theApp.isExit())
			{
				try
				{
					// 休眠 10 秒
					Thread.sleep(10 * 1000);

					// 获取媒体音量
					int nSV = getStreamVolume();
					theApp.setStreamVolume(nSV);

					// 获取屏幕亮度
					// C2 指令已提供设置屏幕亮度的功能, 因此不需要在此处的线程中设置,
					// 否则, 因为线程锁的关系, C2 指令中的设置将不起作用
					int nSB = theApp.getBrightness(MainActivity.this);
					theApp.setScreenBrightness(nSB);
					Log.d(TAG, String.format("ScreenBrightness: %d", nSB));
				}
				catch (Exception e)
				{
					return;
				}
			}
		}
	}*/

	/**
	 * 插入, 卸载或拔出 SD 卡监听线程类
	 */
	private class EjectSDCardThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();

			// 未退出
			while (!theApp.isExit())
			{
				try
				{
					// 休眠 3 秒
					Thread.sleep(3 * 1000);
					// 获取扩展 SDCard 列表
//					Log.d(TAG, "MSG_EXT_LIST");
//					theApp.TLog(TAG, "MSG_EXT_LIST");
//					theApp.SLog(TAG, "MSG_EXT_LIST");
					m_handler.sendEmptyMessage(MSG_EXT_LIST);
				}
				catch (Exception e)
				{
					return;
				}
			}
		}
	}

	/**
	 * 插入, 卸载或拔出 SD 卡接收器类
	 */
	class EjectSDCardReceiver extends BroadcastReceiver
	{
		private String TAG = "EjectSDCardReceiver";
		private boolean m_bIsRegisterReceiver = false;

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String strAction = intent.getAction();
			Log.d(TAG, strAction);
//			Toast.makeText(MainActivity.this, strAction, Toast.LENGTH_LONG).show();
			try
			{
				// 插入, 卸载或拔出 SD 卡
				// 不管拔出和卸载, 都发出 ACTION_MEDIA_EJECT 广播
				if (strAction.equals(Intent.ACTION_MEDIA_EJECT) ||       // 卸载
					strAction.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)/* ||  // 插入
					strAction.equals(Intent.ACTION_MEDIA_BAD_REMOVAL)*/) // 拔出
				{
					// 重新加载音乐, 视频和图片 xwx
					if (theApp.getMediaMark() == theApp.MARK_NONE )
					{
						theApp.setLoadMusic(false);
						theApp.setLoadVideo(false);
						theApp.setLoadPicture(false);

					}
					else
					{
						resetMedia();
//						reloadMVP();
					}
				}
			}
			catch(Exception e)
			{
				return;
			}
		}

		/**
		 * 注册接收器
		 * @param context
		 */
		public  void registerReceiver(Context context)
		{
			if (!m_bIsRegisterReceiver)
			{
				m_bIsRegisterReceiver = true;

				// 需要在 AndroidManifest.xml 中添加如下内容:
/*
				<receiver android:name=".activities.widget.UsbBroadCastReceiver" >
		            <intent-filter android:priority="1000" >
		                <action android:name="android.intent.action.MEDIA_EJECT" />
		                <action android:name="android.intent.action.MEDIA_CHECKING" />
		                <action android:name="android.intent.action.MEDIA_BAD_REMOVAL" />

		                <data android:scheme="file" />
		            </intent-filter>
		        </receiver>
*/
				try
				{
					IntentFilter filter = new IntentFilter();
					filter.addAction(Intent.ACTION_MEDIA_EJECT);       // 卸载
					filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);    // 插入
					filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL); // 拔出
					filter.addDataScheme("file");
					Log.d(TAG, "RegisterReceiver");
					context.registerReceiver(EjectSDCardReceiver.this, filter);
				}
				catch(Exception e)
				{
					return;
				}
			}
		}

		/**
		 * 注销接收器
		 * @param context
		 */
		public void unregisterReceiver(Context context)
		{
			if (m_bIsRegisterReceiver)
			{
				m_bIsRegisterReceiver = false;
				Log.d(TAG, "UnregisterReceiver");
				context.unregisterReceiver(EjectSDCardReceiver.this);
			}
		}
	}

	/**
	 * 语言改变类
	 */
	/*class LocalReceiver extends BroadcastReceiver
	{
		private String TAG = "LocalReceiver";
		private boolean m_bIsRegisterReceiver = false;

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String strAction = intent.getAction();
			Log.d(TAG, strAction);
			try
			{
				if (strAction.equals(Intent.ACTION_LOCALE_CHANGED)) // 语言切换
				{
					// 设置语言 xwx
					setLang();

					// 设置 JPG 图片
					theApp.setJpg();

					try
					{
						// 已初始化
						if (theApp.isInit())
						{
							new Thread()
							{
								public void run()
								{
									try
									{
										// 延迟 100 毫秒
										Log.d(TAG, "MSG_REDRAW - sleep");
										Thread.sleep(100);
									}
									catch (Exception e)
									{
									}
									// 重绘
									m_handler.sendEmptyMessage(MSG_REDRAW);
								}
							}.start();
						}
					}
					catch (Exception e)
					{
					}
				}
			}
			catch(Exception e)
			{
				return;
			}
		}*/

		/**
		 * 注册接收器
		 * @param context
		 */
	/*	public void registerReceiver(Context context)
		{
			if (!m_bIsRegisterReceiver)
			{
				m_bIsRegisterReceiver = true;

				try
				{
					IntentFilter filter = new IntentFilter();
					filter.addAction(Intent.ACTION_LOCALE_CHANGED);
					Log.d(TAG, "RegisterReceiver");
					context.registerReceiver(LocalReceiver.this, filter);
				}
				catch(Exception e)
				{
					return;
				}
			}
		}

		/**
		 * 注销接收器
		 * @param context
		 */
	/*	public void unregisterReceiver(Context context)
		{
			if (m_bIsRegisterReceiver)
			{
				m_bIsRegisterReceiver = false;
				Log.d(TAG, "UnregisterReceiver");
				context.unregisterReceiver(LocalReceiver.this);
			}
		}
	}*/

}
