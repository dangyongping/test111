package com.chinafeisite.tianbu;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 配置布局类
 */
public class SetupLayout extends RelativeLayout implements OnClickListener
{
	private final String TAG = "SetupLayout";

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 控件
	private SwitchButton m_switch0   = null;
	private SwitchButton m_switch1   = null;
	private SwitchButton m_switch2   = null;
	private SeekBar      m_seekBar3  = null;
	private SeekBar      m_seekBar4  = null;
	private TextView     m_txtValue5 = null;
	
	// Activity
	private Activity m_activity = null;
	public void setActivity(Activity activity) { m_activity = activity; }
	
	/**
	 * 构造函数
	 * @param context
	 */
	public SetupLayout(Context context)
	{
		super(context);
		
		// 初始化
		init(context);
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public SetupLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 初始化
		init(context);
	}

	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SetupLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	public void onClick(View view)
	{
		// WiFi
		if (view.getId() == R.id.layRow1)
		{
			try
			{
				// WIFI 管理器
				WifiManager wm = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);
				
				if (wm != null)
				{
					// WIFI 已开启
					if (wm.isWifiEnabled())
					{
						// 启用自定义 WiFi
						if (theApp.USE_WIFI)
						{
							// 检测 WiFi 布局
							if (((MainActivity)m_activity).getWiFiLayout() == null)
							{
								// 初始化 WiFi
								((MainActivity)m_activity).initWiFi();
							}
							
							// 检测 WiFi 布局
							if (((MainActivity)m_activity).getWiFiLayout() != null)
							{
								// 显示 WiFi 布局
								((MainActivity)m_activity).getWiFiLayout().setVisibility(View.VISIBLE);
								
								// 隐藏配置布局
								setVisibility(View.INVISIBLE);
							}
						}
						// 不启用自定义 WiFi
						else
						{
							// 已启动悬浮窗
							theApp.setStartFloat(true);
	
							// 启用悬浮窗单个开关
							if (theApp.FLOAT_SWITCH)
							{
								// 开启悬浮窗
								theApp.setFloatOn(true);
								
								// 启动开关悬浮窗
								Intent intentSwitch = new Intent(m_context, FloatSwitchService.class);
								m_context.startService(intentSwitch);
							}
							// 不启用悬浮窗单个开关
							else
							{
								// 开启顶部悬浮窗
								theApp.setFloatTopOn(true);
								
								// 开启底部悬浮窗
								theApp.setFloatBottomOn(true);
								
								// 启动顶部悬浮窗开关
								Intent intentTopSwitch = new Intent(m_context, FloatTopSwitchService.class);
								m_context.startService(intentTopSwitch);
								
								// 启动底部悬浮窗开关
								Intent intentBottomSwitch = new Intent(m_context, FloatBottomSwitchService.class);
								m_context.startService(intentBottomSwitch);
							}
							
							// 启动顶部悬浮窗
							Intent intentTop = new Intent(m_context, FloatTopService.class);
							m_context.startService(intentTop);
							
							// 启动底部悬浮窗
							Intent intentBottom = new Intent(m_context, FloatBottomService.class);
							m_context.startService(intentBottom);
							
							// 启动 WiFi 设置界面
							m_context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
							theApp.setStartApp(true);//xwx
						}
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context)
	{
		try
		{
			// Activity 的 Context
			m_context = context;

			// 从布局文件中加载视图
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_setup, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			// 控件
			m_switch0   = (SwitchButton)m_view.findViewById(R.id.switchBtn0);
			m_switch1   = (SwitchButton)m_view.findViewById(R.id.switchBtn1);
			m_switch2   = (SwitchButton)m_view.findViewById(R.id.switchBtn2);
			m_seekBar3  = (SeekBar)m_view.findViewById(R.id.seekBar3);
			m_seekBar4  = (SeekBar)m_view.findViewById(R.id.seekBar4);
			m_txtValue5 = (TextView)m_view.findViewById(R.id.txtValue5);
			
			// WiFi
			LinearLayout layRow1 = (LinearLayout)m_view.findViewById(R.id.layRow1);
			layRow1.setOnClickListener(this);
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 初始化配置
	 */
	public void initSetup()
	{
		try
		{
			// 系统版本
			m_txtValue5.setText(theApp.getSysVer());
			
			//----------------------------------------------------------------------------------------------------
			// 公英制单位
			m_switch0.setTextLeft(m_context.getString(R.string.strMetric));
			m_switch0.setTextRight(m_context.getString(R.string.strImperial));
				
			m_switch0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						// 公英制单位, 0: 公制
						if (theApp.getUnits() == 1) theApp.setUnits(0);
					}
					else
					{
						// 公英制单位, 1: 英制
						if (theApp.getUnits() == 0) theApp.setUnits(1);
					}
					
					// 应答 E0
					theApp.responseE0();
				}
			});
			
			// 公英制单位, 0: 公制
			if (theApp.getUnits() == 0)
			{
				if (!m_switch0.isChecked()) m_switch0.setChecked(true);
			}
			
			// 公英制单位, 1: 英制
			if (theApp.getUnits() == 1)
			{
				if (m_switch0.isChecked()) m_switch0.setChecked(false);
			}
			
			//----------------------------------------------------------------------------------------------------
			// WIFI
			m_switch1.setTextLeft(m_context.getString(R.string.strOn));
			m_switch1.setTextRight(m_context.getString(R.string.strOff));
			
			// 需要如下两个权限:
		    // <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
		    // <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />

			m_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					try
					{
						// WIFI 管理器
						WifiManager wm = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);
						
						if (wm != null)
						{
							if (isChecked)
							{
								// WIFI 已关闭
								if (!wm.isWifiEnabled())
								{
									// WIFI 开关状态, 开启
									wm.setWifiEnabled(true);
									
									// 延迟 1 秒
//									Thread.sleep(1 * 1000);
								}
							}
							else
							{
								// WIFI 已开启
								if (wm.isWifiEnabled())
								{
									// WIFI 开关状态, 关闭
									wm.setWifiEnabled(false);
									
									// 延迟 1 秒
//									Thread.sleep(1 * 1000);
								}
							}
						}
					}
					catch (Exception e)
					{
					}
				}
			});
			
			try
			{
				// WIFI 管理器
				WifiManager wm = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);
				
				if (wm != null)
				{
					// WIFI 已开启
					if (wm.isWifiEnabled())
					{
						// WIFI 开关状态, 开启
						if (!m_switch1.isChecked()) m_switch1.setChecked(true);
					}
					// WIFI 已关闭
					else
					{
						// WIFI 开关状态, 关闭
						if (m_switch1.isChecked()) m_switch1.setChecked(false);
					}
				}
			}
			catch (Exception e)
			{
			}
			
			//----------------------------------------------------------------------------------------------------
			// 蓝牙
			m_switch2.setTextLeft(m_context.getString(R.string.strOn));
			m_switch2.setTextRight(m_context.getString(R.string.strOff));
			
			m_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						// 蓝牙开关状态, 1: 开启
						if (theApp.getBtState() == 0) theApp.setBtState(1);
					}
					else
					{
						// 蓝牙开关状态, 0: 关闭
						if (theApp.getBtState() == 1) theApp.setBtState(0);
					}
					
					// 应答 E2
					theApp.responseE2(0, 0);
				}
			});
			
			// 蓝牙开关状态, 1: 开启
			if (theApp.getBtState() == 1)
			{
				if (!m_switch2.isChecked()) m_switch2.setChecked(true);
			}

			// 蓝牙开关状态, 0: 关闭
			if (theApp.getBtState() == 0)
			{
				if (m_switch2.isChecked()) m_switch2.setChecked(false);
			}
			
			//----------------------------------------------------------------------------------------------------
			// 亮度
			int nMaxB = 255;
			int nB = theApp.getBrightness(m_activity);
			int nBP = nB * 255 / nMaxB;
			m_seekBar3.setProgress(nBP);
			Log.d(TAG, String.format("Brightness: %d, progress: %d", nB, nBP));
			
			m_seekBar3.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
			{
				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
					// 亮度
					int nMinB = 30;
					int nMaxB = 255;
					int nB = (int)(nMaxB * progress / 255);
					if (nB < nMinB) nB = nMinB;
					theApp.setBrightness(m_activity, nB);
					theApp.saveBrightness(m_activity, nB);
					Log.d(TAG, String.format("progress: %d, Brightness: %d", progress, nB));
				}
			});
			
			//----------------------------------------------------------------------------------------------------
			// 音量
			int nMaxV = 15;
			int nV = theApp.getStreamVolume(m_activity);
			int nVP = nV * 255 / nMaxV;
			m_seekBar4.setProgress(nVP);
			Log.d(TAG, String.format("Volume: %d, progress: %d", nV, nVP));
			
			m_seekBar4.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
			{
				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
					// 音量
					int nMaxV = 15;
					int nV = (int)(nMaxV * progress / 255);
					theApp.setStreamVolume(m_activity, nV);
					Log.d(TAG, String.format("progress: %d, Volume: %d", progress, nV));
				}
			});
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 设置 WiFi
	 */
	public void setWiFi()
	{
		try
		{
			// WIFI 管理器
			WifiManager wm = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);
			
			if (wm != null)
			{
				// WIFI 已开启
				if (wm.isWifiEnabled())
				{
					// WIFI 开关状态, 开启
					if (!m_switch1.isChecked()) m_switch1.setChecked(true);
				}
				// WIFI 已关闭
				else
				{
					// WIFI 开关状态, 关闭
					if (m_switch1.isChecked()) m_switch1.setChecked(false);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
}
