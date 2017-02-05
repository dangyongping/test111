package com.chinafeisite.tianbu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * WiFi 布局类
 */
public class WiFiLayout extends RelativeLayout implements OnClickListener, OnItemClickListener, OnItemLongClickListener, OnCheckedChangeListener
{
	// 需要如下几个权限:
	// <uses-permission android:name="android.permission.INTERNET" />
    // <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    // <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
	// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    // <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE" />

	private final String TAG = "WiFiLayout";

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 项的索引
	private int m_nItemIndex = -1;
	
	// 是否为长按
	private boolean m_bLongClick = false;

	// SSID
	private String m_strSSID = "";
	public String getSSID() { return m_strSSID; }
	public void setSSID(final String str) { m_strSSID = str; }
	
	// WiFi 信息
	private WifiInfo m_wifiInfo = null;
	
	// WiFi 管理器
	private WifiManager m_wifiMgr = null;
	public WifiManager getWiFiMgr() { return m_wifiMgr; }

	// 控件
	private Button m_btnScan = null;
	private ListView m_lvWiFi = null;
	private SwitchButton m_btnWLAN = null;
	private EditText m_edtPassword = null;
	private TextView m_txtPassword = null;
	private CheckBox m_chkShowPassword = null;

	// 字符串
	private String m_strScan             = "Scan";
	private String m_strScanning         = "Scanning...";
	private String m_strSaved            = "Saved";
	private String m_strCancel           = "Cancel";
	private String m_strConnect          = "Connect";
	private String m_strConnected        = "Connected";
	private String m_strDisconnect       = "Disconnect";
	private String m_strNotConnected     = "Not connected";
	private String m_strNotInRange       = "Not in range";
	private String m_strPassword         = "Password";
	private String m_strShowPassword     = "Show password";
	private String m_strForgetNetwork    = "Forget network";
	private String m_strConnectToNetwork = "Connect to network";

	// WiFi 接收器
	private WiFiReceiver m_wiFiReceiver = null;
	
	// 选择的 WiFi 配置
	private WifiConfiguration m_wcSelect = null;
	public WifiConfiguration getWcSelect() { return m_wcSelect; }

	// 扫描结果链表
	private List<ScanResult> m_listResults = null;
	public List<ScanResult> getListResults() { return m_listResults; }
	
	// 映射链表
	private List<Map<String, Object>> m_listMap = null;
	
	// 加密类型
	public static final int NO_PASSWORD = 0;
	public static final int WEP         = 1;
	public static final int WPA         = 2;
	
	// 对话框
	public static final int DIALOG_PASSWORD = 0; // 输入密码
	
	// 菜单选项
	public static final int ITEM_CONNECT    = 0; // 连接
	public static final int ITEM_DISCONNECT = 1; // 断开
	public static final int ITEM_FORGET     = 2; // 忘记网络
	public static final int ITEM_CANCEL     = 3; // 取消
	
	// 键值
	private static final String KEY_SSID   = "SSID";  // SSID
	private static final String KEY_STATUS = "Status"; // 状态
	private static final String KEY_LEVEL  = "Level"; // 强度等级
	
	// 消息
	private static final int MSG_START_SCAN       = 0; // 开始扫描
	private static final int MSG_START_SCAN_WIFI  = 1; // 开始扫描 WiFi
	private static final int MSG_SHOW_SCAN_RESULT = 2; // 显示扫描结果
	
	// WiFi 扫描等待时间, 毫秒
	public static final int WIFI_SCAN_WAITING_TIME = 500;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public WiFiLayout(Context context)
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
	public WiFiLayout(Context context, AttributeSet attrs)
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
	public WiFiLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}
	
	@Override
	public void onClick(View view)
	{
		// 扫描
		if (view.getId() == R.id.btnScan)
		{
			// 开始扫描 WiFi
			startScanWiFi();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		try
		{
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return;
			
			if (isChecked)
			{
				// WiFi 已关闭
				if (!m_wifiMgr.isWifiEnabled())
				{
					// WiFi 开关状态, 开启
					m_wifiMgr.setWifiEnabled(true);

					// 重新开始扫描 WiFi
					restartScanWiFi();
				}
			}
			else
			{
				// WiFi 已开启
				if (m_wifiMgr.isWifiEnabled())
				{
					// WiFi 开关状态, 关闭
					m_wifiMgr.setWifiEnabled(false);
					
					// 禁用"扫描"按钮
					m_btnScan.setEnabled(false);
					m_btnScan.setText(m_strScan);
					
					// 隐藏"WiFi"列表
					m_lvWiFi.setVisibility(View.INVISIBLE);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 显示对话框
	 * @param id
	 * @return
	 */
    public Dialog showDialog(int id)
	{
		try
		{
			// 输入密码对话框
			if (id == DIALOG_PASSWORD)
			{
				// SSID
				if (m_strSSID == null) return null;
				
				// 布局泵
				LayoutInflater layInflater = LayoutInflater.from(m_context);
				
				// 输入密码视图
				final View viewPassword = layInflater.inflate(R.layout.wifi_password, null);

				// 控件
				m_edtPassword = (EditText)viewPassword.findViewById(R.id.edtPassword);
				m_txtPassword = (TextView)viewPassword.findViewById(R.id.txtPassword);
				m_chkShowPassword = (CheckBox)viewPassword.findViewById(R.id.chkShowPassword);
				if (m_txtPassword != null) m_txtPassword.setText(m_strPassword);
				if (m_chkShowPassword != null) m_chkShowPassword.setText(m_strShowPassword);
				
				try
				{
					// 获取资源
					Resources res = getResources();

					// 获取显示度量
					DisplayMetrics dm = res.getDisplayMetrics();
					
					// 屏幕宽度
					int nWP = dm.widthPixels;
					
					// 文本框宽度
					int nWT = m_edtPassword.getWidth();
					
					if (nWT < nWP / 2)
					{
						// 设置宽度
						m_edtPassword.setWidth(nWP / 2);
						Log.d(TAG, "setWidth");
						
						// 设置选择位置
						String str = m_edtPassword.getText().toString();
						m_edtPassword.setSelection(str.length());
					}
				}
				catch (Exception e)
				{
				}
				
				// 显示密码
				m_chkShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
					{
						if (m_edtPassword == null) return;
						
						if (!isChecked)
						{
							// 不显示密码
							int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
							m_edtPassword.setInputType(inputType);
						}
						else
						{
							// 显示密码
							int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
							m_edtPassword.setInputType(inputType);
						}
						
						// 设置选择位置
						String str = m_edtPassword.getText().toString();
						m_edtPassword.setSelection(str.length());
					}
				});
				
				// 显示密码输入视图
				new AlertDialog.Builder(m_context)
					.setTitle(m_strSSID)
	                .setView(viewPassword)
					.setPositiveButton(m_strConnect, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						// SSID
						String strSSID = m_strSSID;
						if (strSSID == null) return;
						
						// 获取扫描结果
						if (m_listResults == null) return;
						ScanResult result = checkListResult(m_listResults, m_strSSID);
						if (result == null) return;
						
						// 密码
						String strPassword = m_edtPassword.getText().toString();
						
						// 获取加密类型
						int nType = getEncryption(result);
						
						// 创建 WiFi 配置
						WifiConfiguration wc = createWiFiConfig(strSSID, strPassword, nType);
						if (wc == null) return;
						
						// 连接到 WiFi
						boolean bConnect = connectTo(wc);
						Log.d(TAG, "ConnectTo: " + bConnect);
						
						if (bConnect)
						{
							// 重新开始扫描 WiFi
							restartScanWiFi();
						}
					}
				})
				.setNegativeButton(m_strCancel, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						// 重新开始扫描 WiFi
						restartScanWiFi();
					}
				})
				.create().show();
			}
		}
		catch (Exception e)
		{
		}
		
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		try
		{
			// 选择的 WiFi 配置
			m_wcSelect = null;

			// 单击
			m_bLongClick = false;
			
			// 项的索引
			m_nItemIndex = position;

			// 映射链表
			if (m_listMap == null || m_listMap.size() < 1) return;
			
			// 检测项的索引
			if (m_nItemIndex < 0 || m_nItemIndex >= m_listMap.size()) return;
			
			// SSID
			String strSSID = (String)m_listMap.get(m_nItemIndex).get(KEY_SSID);
			if (strSSID == null) return;
			
			// 选择的 WiFi 配置
			m_wcSelect = isInConfig(strSSID);

			// 已在配置列表中
			if (m_wcSelect != null)
			{
				// 显示上下文菜单
				m_lvWiFi.showContextMenu();
			}
			// 未在配置列表中
			else
			{
				// SSID
				m_strSSID = strSSID;
				if (m_strSSID == null) return;
				
				// 获取扫描结果
				if (m_listResults == null) return;
				ScanResult result = checkListResult(m_listResults, m_strSSID);
				if (result == null) return;

				// 获取加密类型
				int nType = getEncryption(result);
				
				if (nType == NO_PASSWORD)
				{
					// 显示上下文菜单
					m_lvWiFi.showContextMenu();
					return;
				}
				
				// 显示输入密码对话框
				showDialog(DIALOG_PASSWORD);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		try
		{
			// 选择的 WiFi 配置
			m_wcSelect = null;

			// 长按
			m_bLongClick = true;
			
			// 项的索引
			m_nItemIndex = position;
			
			// 显示上下文菜单
			m_lvWiFi.showContextMenu();
		}
		catch (Exception e)
		{
		}
		
		return true;
	}

	private OnCreateContextMenuListener m_onCreateContextMenuListener = new OnCreateContextMenuListener()
	{
		@Override
		public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
		{
			try
			{
				if (view != m_lvWiFi) return;
				
				// 映射链表
				if (m_listMap == null || m_listMap.size() < 1) return;
				
				// 检测项的索引
				if (m_nItemIndex < 0 || m_nItemIndex >= m_listMap.size()) return;
				
				// SSID
				String strSSID = (String)m_listMap.get(m_nItemIndex).get(KEY_SSID);
				if (strSSID == null) return;
				
				// 选择的 WiFi 配置
				m_wcSelect = isInConfig(strSSID);
				
				// 未在配置列表中
				if (m_wcSelect == null)
				{
					// 单击
					if (!m_bLongClick)
					{
						// 菜单标题
						menu.setHeaderTitle(strSSID);
						
						// 连接和取消
						menu.add(0, ITEM_CONNECT, 0, m_strConnect);
						menu.add(0, ITEM_CANCEL,  1, m_strCancel);
					}
					// 长按
					else
					{
						// 菜单标题
						menu.setHeaderTitle(strSSID);
						
						// 连接到网络和取消
						menu.add(0, ITEM_CONNECT, 0, m_strConnectToNetwork);
						menu.add(0, ITEM_CANCEL,  1, m_strCancel);
					}
				}
				// 已在配置列表中
				else
				{
					// 未连接
					if (!isConnected(strSSID, null))
					{
						// 单击
						if (!m_bLongClick)
						{
							// 菜单标题
							menu.setHeaderTitle(strSSID);
							
							// 连接和取消
							menu.add(0, ITEM_CONNECT, 0, m_strConnect);
							menu.add(0, ITEM_CANCEL,  1, m_strCancel);
						}
						// 长按
						else
						{
							// 菜单标题
							menu.setHeaderTitle(strSSID);
							
							// 连接到网络, 忘记网络和取消
							menu.add(0, ITEM_CONNECT, 0, m_strConnectToNetwork);
							menu.add(0, ITEM_FORGET,  1, m_strForgetNetwork);
							menu.add(0, ITEM_CANCEL,  2, m_strCancel);
						}
					}
					// 已连接
					else
					{
						// 单击
						if (!m_bLongClick)
						{
							// 菜单标题
							menu.setHeaderTitle(strSSID);
							
							// 断开和取消
							menu.add(0, ITEM_DISCONNECT, 0, m_strDisconnect);
							menu.add(0, ITEM_CANCEL,     1, m_strCancel);
						}
						// 长按
						else
						{
							// 菜单标题
							menu.setHeaderTitle(strSSID);
							
							// 忘记网络和取消
							menu.add(0, ITEM_FORGET, 0, m_strForgetNetwork);
							menu.add(0, ITEM_CANCEL, 1, m_strCancel);
						}
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	};
	
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_wifi, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			try
			{
				// 扫描
				m_btnScan = (Button)m_view.findViewById(R.id.btnScan);
				m_btnScan.setOnClickListener(this);
				
				// 列表视图
				m_lvWiFi  = (ListView)m_view.findViewById(R.id.lvWiFi);
				m_lvWiFi.setOnItemClickListener(this);
				m_lvWiFi.setOnItemLongClickListener(this);
				m_lvWiFi.setOnCreateContextMenuListener(m_onCreateContextMenuListener);
				m_lvWiFi.setVisibility(View.INVISIBLE);
				
				// WLAN
				m_btnWLAN = (SwitchButton)m_view.findViewById(R.id.btnWLAN);
				m_btnWLAN.setOnCheckedChangeListener(this);
			}
			catch (Exception e)
			{
			}
			
			try
			{
				// 字符串
				m_strScan             = m_context.getString(R.string.strWiFiScan);
				m_strScanning         = m_context.getString(R.string.strWiFiScanning);
				m_strSaved            = m_context.getString(R.string.strWiFiSaved);
				m_strCancel           = m_context.getString(R.string.strWiFiCancel);
				m_strConnect          = m_context.getString(R.string.strWiFiConnect);
				m_strConnected        = m_context.getString(R.string.strWiFiConnected);
				m_strDisconnect       = m_context.getString(R.string.strWiFiDisconnect);
				m_strNotConnected     = m_context.getString(R.string.strWiFiNotConnected);
				m_strNotInRange       = m_context.getString(R.string.strWiFiNotInRange);
				m_strPassword         = m_context.getString(R.string.strWiFiPassword);
				m_strShowPassword     = m_context.getString(R.string.strWiFiShowPassword);
				m_strForgetNetwork    = m_context.getString(R.string.strWiFiForgetNetwork);
				m_strConnectToNetwork = m_context.getString(R.string.strWiFiConnectToNetwork);
			}
			catch (Exception e)
			{
			}
			
			try
			{
				// WiFi 管理器
				m_wifiMgr = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);
				if (m_wifiMgr == null) return;

				// 获取 WIFI 信息
				m_wifiInfo = m_wifiMgr.getConnectionInfo();

				// WiFi 已关闭
				if (!m_wifiMgr.isWifiEnabled())
				{
					// 禁用"扫描"按钮
					m_btnScan.setEnabled(false);
					
					// WiFi 开关状态, 关闭
					m_btnWLAN.setChecked(false);
				}
				// WiFi 已开启
				else
				{
					// WiFi 开关状态, 开启
					m_btnWLAN.setChecked(true);
					
					// 重新开始扫描 WiFi
					restartScanWiFi();
				}
			}
			catch (Exception e)
			{
				Log.d(TAG, e.toString());
			}
			
			try
			{
				// WiFi 接收器
				if (m_wiFiReceiver != null) m_wiFiReceiver.unregisterReceiver(m_context);
				m_wiFiReceiver = new WiFiReceiver();
				m_wiFiReceiver.registerReceiver(m_context);
			}
			catch (Exception e)
			{
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 退出
	 */
	public void exit()
	{
		try
		{
			// 注销接收器
			if (m_wiFiReceiver != null) m_wiFiReceiver.unregisterReceiver(m_context);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 休眠
	 * @param nMilliseconds
	 */
	public void sleep(final int nMilliseconds)
	{
		try
		{
			Thread.sleep(nMilliseconds);
		}
		catch (InterruptedException ex)
		{
		}
	}
	
	/**
	 * 开始扫描
	 */
	private void startScan()
	{
		try
		{
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return;
			
			// 开始扫描
			m_wifiMgr.startScan();
			
			// 休眠
			sleep(WIFI_SCAN_WAITING_TIME);
			
			// 显示扫描结果
			m_handler.sendEmptyMessage(MSG_SHOW_SCAN_RESULT);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 开始扫描 WiFi
	 */
	private void startScanWiFi()
	{
		try
		{
			// 正在扫描
			m_btnScan.setEnabled(false);
			m_btnScan.setText(m_strScanning);
			
			// 开始扫描
			m_handler.sendEmptyMessage(MSG_START_SCAN);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 重新开始扫描 WiFi
	 */
	public void restartScanWiFi()
	{
		try
		{
			// WiFi 管理器
			if (m_wifiMgr == null) m_wifiMgr = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);

			// 获取 WIFI 信息
			if (m_wifiMgr != null) m_wifiInfo = m_wifiMgr.getConnectionInfo();
		}
		catch (Exception e)
		{
		}
		
		try
		{
			new Thread()
			{
				public void run()
				{
					try
					{
						// 休眠
						sleep(WIFI_SCAN_WAITING_TIME);

						// 开始扫描 WiFi
						m_handler.sendEmptyMessage(MSG_START_SCAN_WIFI);
					}
					catch (Exception e)
					{
					}
				}
			}.start();
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * 显示扫描结果
	 */
	public void showScanResult()
	{
		try
		{
			// 扫描
			m_btnScan.setEnabled(true);
			m_btnScan.setText(m_strScan);
			
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return;
			
			// 获取扫描结果
			m_listResults = m_wifiMgr.getScanResults();
			if (m_listResults == null || m_listResults.size() < 1) return;
			
			// 按信号强度排序
			Collections.sort(m_listResults, new WiFiSignalLevelComparator());
			
			// 映射链表
			m_listMap = new ArrayList<Map<String, Object>>();

			// 获取 WIFI 信息
			m_wifiInfo = m_wifiMgr.getConnectionInfo();
			
			if (m_wifiInfo != null)
			{
				// SSID
				String strSSID = m_wifiInfo.getSSID();
				
				if (strSSID != null && strSSID.length() > 0)
				{
					// 去除引号
					strSSID = removeQuotes(strSSID);
					
					if (strSSID != null && strSSID.length() > 0 && !strSSID.equals("0x"))
					{
						// 计算信号强度
						int nRSSI = WifiManager.calculateSignalLevel(m_wifiInfo.getRssi(), 4);
	
						// 映射项
						Map<String, Object> mapItem = new HashMap<String, Object>();
	
						// 添加映射项
						addMapItem(mapItem, strSSID, nRSSI);
						
						// 添加列表项
						if (!checkList(m_listMap, mapItem)) m_listMap.add(mapItem);
					}
				}
			}
			
			for (ScanResult result : m_listResults)
			{
				// SSID
				String strSSID = result.SSID;

				if (strSSID != null && strSSID.length() > 0 && !strSSID.equals("0x"))
				{
					// 映射项
					Map<String, Object> mapItem = new HashMap<String, Object>();
					
					// 添加映射项
					addMapItem(mapItem, result);
					
					// 添加列表项
					if (!checkList(m_listMap, mapItem)) m_listMap.add(mapItem);
				}
			}
			
			// 检测数量
			if (m_listMap.size() < 1) return;
//			Log.d(TAG, String.format("Size: %d", listMap.size()));
			
			// 显示"WiFi"列表
			m_lvWiFi.setVisibility(View.VISIBLE);
			
			// 适配器
			SimpleAdapter sa = new SimpleAdapter(
					m_context,
					m_listMap,
					R.layout.wifi_list_row,
					new String[] { KEY_SSID, KEY_STATUS, KEY_LEVEL },
					new int[] { R.id.txtSSID, R.id.txtStatus, R.id.imgLevel });

			// 设置适配器
			m_lvWiFi.setAdapter(sa);
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	
	/**
	 * 添加映射项
	 * @param mapItem
	 * @param strSSID
	 * @param nRSSI
	 */
	private void addMapItem(Map<String, Object> mapItem, final String strSSID, final int nRSSI)
	{
		try
		{
			// SSID
			if (strSSID == null) return;
			mapItem.put(KEY_SSID, strSSID);
			
			// 信号强度
			int nLevel  = R.drawable.wifi0;
			int nLevel0 = R.drawable.wifi0;
			int nLevel1 = R.drawable.wifi1;
			int nLevel2 = R.drawable.wifi2;
			int nLevel3 = R.drawable.wifi3;
			if (nRSSI == 0) nLevel = nLevel0;
			if (nRSSI == 1) nLevel = nLevel1;
			if (nRSSI == 2) nLevel = nLevel2;
			if (nRSSI == 3) nLevel = nLevel3;
			mapItem.put(KEY_LEVEL, nLevel);
//			Log.d(TAG, String.format("RSSI: %d", nRSSI));
			
			// 已连接
			mapItem.put(KEY_STATUS, m_strConnected);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 添加映射项
	 * @param mapItem
	 * @param result
	 */
	private void addMapItem(Map<String, Object> mapItem, ScanResult result)
	{
		try
		{
			if (result == null || mapItem == null) return;
			
			// SSID
			String strSSID = result.SSID;
			if (strSSID == null) return;
			mapItem.put(KEY_SSID, strSSID);
			
			// 信号强度
			int nLevel  = R.drawable.wifi0;
			int nLevel0 = R.drawable.wifi0;
			int nLevel1 = R.drawable.wifi1;
			int nLevel2 = R.drawable.wifi2;
			int nLevel3 = R.drawable.wifi3;
			int nRSSI = WifiManager.calculateSignalLevel(result.level, 4); // 计算信号强度
			if (nRSSI == 0) nLevel = nLevel0;
			if (nRSSI == 1) nLevel = nLevel1;
			if (nRSSI == 2) nLevel = nLevel2;
			if (nRSSI == 3) nLevel = nLevel3;
			mapItem.put(KEY_LEVEL, nLevel);
//			Log.d(TAG, String.format("RSSI: %d", nRSSI));
			
			boolean bSSIDSame = false;
			boolean bBSSIDSame = false;

			// SSID
			if (m_wifiInfo != null &&
				m_wifiInfo.getSSID() != null &&
				m_wifiInfo.getSSID().equals(strSSID))
			{
				bSSIDSame = true;
			}

			// BSSID
			String strBSSID = result.BSSID;
			if (strBSSID == null) return;
			
			// BSSID
			if (m_wifiInfo != null &&
				m_wifiInfo.getBSSID() != null &&
				m_wifiInfo.getBSSID().equals(strBSSID))
			{
				bBSSIDSame = true;
			}
			
			// SSID 或 BSSID 相同
			if (bSSIDSame || bBSSIDSame)
			{
				// 已连接
				mapItem.put(KEY_STATUS, m_strConnected);
			}
			else
			{
				// 未连接
				mapItem.put(KEY_STATUS, m_strNotConnected);
				
				if (m_wifiMgr != null)
				{
					// 检测链表
					if (checkList(m_wifiMgr.getConfiguredNetworks(), result))
					{
						// 已保存
						mapItem.put(KEY_STATUS, m_strSaved);
					}
				}
				
				// 信号强度为 0
				if (nRSSI == 0)
				{
					// 不在范围内
					mapItem.put(KEY_STATUS, m_strNotInRange);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 检测扫描结果链表
	 * @param list
	 * @param strSSID
	 * @return
	 */
	public ScanResult checkListResult(List<ScanResult> list, final String strSSID)
	{
		try
		{
			if (strSSID == null) return null;
			if (list == null || list.size() < 1) return null;
			
			for (ScanResult result : list)
			{
				// SSID
				String strSSID1 = result.SSID;
				String strSSID2 = strSSID;

				// 去除引号
				strSSID1 = removeQuotes(strSSID1);
				strSSID2 = removeQuotes(strSSID2);
				if (strSSID1 == null ) return null;
				if (strSSID2 == null ) return null;
				
				if (strSSID1.equals(strSSID2))
				{
//					Log.d(TAG, strSSID1);
					return result;
				}
			}
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 检测 WiFi 配置链表
	 * @param list
	 * @param strSSID
	 * @return
	 */
	private WifiConfiguration checkListWC(List<WifiConfiguration> list, final String strSSID)
	{
		try
		{
			if (strSSID == null) return null;
			if (list == null || list.size() < 1) return null;
			
			for (WifiConfiguration wc : list)
			{
				// SSID
				String strSSID1 = wc.SSID;
				String strSSID2 = strSSID;

				// 去除引号
				strSSID1 = removeQuotes(strSSID1);
				strSSID2 = removeQuotes(strSSID2);
				if (strSSID1 == null ) return null;
				if (strSSID2 == null ) return null;
				
				if (strSSID1.equals(strSSID2))
				{
//					Log.d(TAG, strSSID1);
					return wc;
				}
			}
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 检测链表
	 * @param list
	 * @param result
	 * @return
	 */
	private boolean checkList(List<WifiConfiguration> list, ScanResult result)
	{
		try
		{
			if (result == null) return false;
			if (list == null || list.size() < 1) return false;
			
			for (WifiConfiguration wc : list)
			{
				// SSID
				String strSSID1 = wc.SSID;
				String strSSID2 = result.SSID;

				// 去除引号
				strSSID1 = removeQuotes(strSSID1);
				strSSID2 = removeQuotes(strSSID2);
				if (strSSID1 == null ) return false;
				if (strSSID2 == null ) return false;
				
				if (strSSID1.equals(strSSID2))
				{
//					Log.d(TAG, strSSID1);
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 检测链表
	 * @param listMap
	 * @param mapItem
	 * @return
	 */
	private boolean checkList(List<Map<String, Object>> listMap, Map<String, Object> mapItem)
	{
		try
		{
			if (mapItem == null) return false;
			if (listMap == null || listMap.size() < 1) return false;
			
			for (Map<String, Object> item : listMap)
			{
				// SSID
				String strSSID1 = (String)item.get(KEY_SSID);
				String strSSID2 = (String)mapItem.get(KEY_SSID);

				// 去除引号
				strSSID1 = removeQuotes(strSSID1);
				strSSID2 = removeQuotes(strSSID2);
				if (strSSID1 == null ) return false;
				if (strSSID2 == null ) return false;
				
				if (strSSID1.equals(strSSID2))
				{
//					Log.d(TAG, strSSID1);
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 获取加密类型
	 * @param result
	 * @return
	 */
	public int getEncryption(ScanResult result)
	{
		try
		{
			if (result == null) return NO_PASSWORD;

			String strCapabilities = result.capabilities;
			if (result.capabilities == null) return NO_PASSWORD;

			if (strCapabilities.contains("WPA"))
			{
				return WPA;
			}
			else if (strCapabilities.contains("WEP"))
			{
				return WEP;
			}
		}
		catch (Exception e)
		{
		}

		return NO_PASSWORD;
	}

	/**
	 * 创建 WiFi 配置
	 * @param strSSID
	 * @param strPassword
	 * @param nType
	 * @return
	 */
	private WifiConfiguration createWiFiConfig(final String strSSID, final String strPassword, final int nType)
	{
		try
		{
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return null;

			// WiFi 配置
			WifiConfiguration wc = new WifiConfiguration();
			wc.allowedAuthAlgorithms.clear();
			wc.allowedGroupCiphers.clear();
			wc.allowedKeyManagement.clear();
			wc.allowedPairwiseCiphers.clear();
			wc.allowedProtocols.clear();
			
			// SSID
			String strSSID_ = strSSID;
			
			// 去除引号
			strSSID_ = removeQuotes(strSSID_);
			if (strSSID_ == null ) return null;

			// SSID
			wc.SSID = "\"" + strSSID_ + "\"";

			// 检测是否已存在指定的 SSID
			WifiConfiguration tempWC = isExsits(strSSID_);
			
			if (tempWC != null)
			{
				// 移除已存在的网络
				m_wifiMgr.removeNetwork(tempWC.networkId);
			}

			// NO_PASSWORD
			if (nType == NO_PASSWORD)
			{
				wc.wepKeys[0] = "";
				wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				wc.wepTxKeyIndex = 0;
			}
			
			// WEP
			if (nType == WEP)
			{
				wc.hiddenSSID = true;
				wc.wepKeys[0] = "\"" + strPassword + "\"";
				wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
				wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				wc.wepTxKeyIndex = 0;
			}
			
			// WPA
			if (nType == WPA)
			{
				wc.hiddenSSID = true;
				wc.preSharedKey = "\"" + strPassword + "\"";
				wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				wc.status = WifiConfiguration.Status.ENABLED;
			}
			
			return wc;
		}
		catch (Exception e)
		{
		}
		
		return null;
	}

	/**
	 * 检测是否已存在指定的 SSID
	 * @param strSSID
	 * @return
	 */
	public WifiConfiguration isExsits(final String strSSID)
	{
		try
		{
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return null;

			// 获取 WiFi 配置
			List<WifiConfiguration> listWC = m_wifiMgr.getConfiguredNetworks();
			
			for (WifiConfiguration wc : listWC)
			{
				// SSID
				String strSSID1 = wc.SSID;
				String strSSID2 = strSSID;

				// 去除引号
				strSSID1 = removeQuotes(strSSID1);
				strSSID2 = removeQuotes(strSSID2);
				if (strSSID1 == null ) return null;
				if (strSSID2 == null ) return null;
				
				if (strSSID1.equals(strSSID2))
				{
					return wc;
				}
			}
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 去除引号
	 * @param strSSID
	 * @return
	 */
	private String removeQuotes(final String strSSID)
	{
		try
		{
			if (strSSID == null) return null;
			
			// SSID
			String strSSID_ = strSSID;
			
			if (strSSID_.length() > 2)
			{
				// 去除引号
				if (strSSID_.startsWith("\"")) strSSID_ = strSSID_.substring(1);
				if (strSSID_.endsWith("\""))   strSSID_ = strSSID_.substring(0, strSSID_.length() - 1);
			}
			
			return strSSID_;
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 断开连接
	 * @param wc
	 * @return
	 */
	public boolean disconnect(WifiConfiguration wc)
	{
		try
		{
			if (wc == null) return false;
			
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return false;

			int nNetID = wc.networkId;
			
			boolean bDisable = m_wifiMgr.disableNetwork(nNetID);
			Log.d(TAG, "DisableNetwork: " + bDisable);
			
			boolean bDisconnect = m_wifiMgr.disconnect();
			Log.d(TAG, "Disconnect: " + bDisconnect);
			
			return bDisconnect;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 连接到 WiFi
	 * @param wc
	 * @return
	 */
	public boolean connectTo(WifiConfiguration wc)
	{
		try
		{
			if (wc == null) return false;
			
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return false;

			boolean bSet = m_wifiMgr.setWifiEnabled(true);
			Log.d(TAG, "SetWifiEnabled: " + bSet);
			
			int nNetID = m_wifiMgr.addNetwork(wc);
			Log.d(TAG, "AddNetwork: " + nNetID);
			
			boolean bSave = m_wifiMgr.saveConfiguration();
			Log.d(TAG, "SaveConfiguration: " + bSave);
			
			boolean bEnable = m_wifiMgr.enableNetwork(nNetID, true);
			Log.d(TAG, "EnableNetwork: " + bEnable);
			
			return bEnable;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 检测是否已在配置列表中
	 * @param strSSID
	 * @return
	 */
	private WifiConfiguration isInConfig(final String strSSID)
	{
		try
		{
			// 检测 WiFi 管理器
			if (m_wifiMgr == null) return null;

			// 获取 WiFi 配置
			List<WifiConfiguration> listWC = m_wifiMgr.getConfiguredNetworks();
			if (listWC == null || listWC.size() < 1) return null;

			// 选择的 WiFi 配置
			WifiConfiguration wc = checkListWC(listWC, strSSID);
			
			// 检查链表
			if (wc != null) return wc;
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 检测是否已连接
	 * @param strSSID
	 * @param strBSSID
	 * @return
	 */
	private boolean isConnected(final String strSSID, final String strBSSID)
	{
		try
		{
			if (strSSID == null) return false;
			
			// 获取 WIFI 信息
			m_wifiInfo = m_wifiMgr.getConnectionInfo();
			if (m_wifiInfo == null) return false;
			if (m_wifiInfo.getSSID() == null) return false;
			
			// SSID
			String strSSID1 = strSSID;
			String strSSID2 = m_wifiInfo.getSSID();

			// 去除引号
			strSSID1 = removeQuotes(strSSID1);
			strSSID2 = removeQuotes(strSSID2);
			if (strSSID1 == null ) return false;
			if (strSSID2 == null ) return false;
			
			// 检测 SSID
			if (strSSID1.equals(strSSID2)) return true;
			
			// 检测 BSSID
			if (strBSSID == null) return false;
			if (m_wifiInfo.getBSSID() == null) return false;
			
			// BSSID
			String strBSSID1 = strBSSID;
			String strBSSID2 = m_wifiInfo.getBSSID();
			
			// 检测 BSSID
			if (strBSSID1.equals(strBSSID2)) return true;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}

	/**
	 * 消息处理器
	 */
	private Handler m_handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 开始扫描
			case MSG_START_SCAN:
				startScan();
				break;
				
			// 开始扫描 WiFi
			case MSG_START_SCAN_WIFI:
				startScanWiFi();
				break;
				
			// 显示扫描结果
			case MSG_SHOW_SCAN_RESULT:
				showScanResult();
				break;
			}
		}
	};

	/**
	 * WiFi 信号强度比较器
	 */
	private class WiFiSignalLevelComparator implements Comparator<ScanResult>
	{
		@Override
		public int compare(ScanResult arg1, ScanResult arg2)
		{
			try
			{
				ScanResult result1 = (ScanResult)arg1;
				ScanResult result2 = (ScanResult)arg2;

				if (m_wifiInfo != null)
				{
					// SSID
					String strSSID = m_wifiInfo.getSSID();

					// BSSID
					String strBSSID = m_wifiInfo.getBSSID();
					
					if (strSSID != null && strSSID.equals(result1.SSID))
					{
//						Log.d(TAG, "SSID: " + strSSID);
						return -1;
					}
					
					if (strBSSID != null && strBSSID.equals(result1.BSSID))
					{
//						Log.d(TAG, "BSSID: " + strBSSID);
						return -1;
					}
				}
				
				return WifiManager.compareSignalLevel(result2.level, result1.level);
			}
			catch (Exception e)
			{
			}
			
			return 0;
		}
	}

	/**
	 * WiFi 接收器类
	 */
	private class WiFiReceiver extends BroadcastReceiver
	{
		private String TAG = "WiFiReceiver";
		private boolean m_bIsRegisterReceiver = false;

		@Override
		public void onReceive(Context context, Intent intent)
		{
			try
			{
				// RSSI 改变
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION))
				{
					Log.d(TAG, "RSSI_CHANGED_ACTION");
					
					// WiFi 已开启
					if (m_wifiMgr != null && m_wifiMgr.isWifiEnabled())
					{
						// 重新开始扫描 WiFi
						restartScanWiFi();
					}
				}
				// 网络状态改变
				else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
				{
					NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

					// 网络连接断开
					if (info.getState().equals(NetworkInfo.State.DISCONNECTED))
					{
						Log.d(TAG, "DISCONNECTED");
						
						// WiFi 已开启
						if (m_wifiMgr != null && m_wifiMgr.isWifiEnabled())
						{
							// 重新开始扫描 WiFi
							restartScanWiFi();
						}
					}

					// 连接到 WiFi 网络
					if (info.getState().equals(NetworkInfo.State.CONNECTING))
					{
						Log.d(TAG, "CONNECTING");
						
						// WiFi 已开启
						if (m_wifiMgr != null && m_wifiMgr.isWifiEnabled())
						{
							// 重新开始扫描 WiFi
							restartScanWiFi();
						}
					}
				}
				else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
				{
					// WiFi 状态
					int nWiFiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

					// 启用 WiFi
					if (nWiFiState == WifiManager.WIFI_STATE_ENABLED)
					{
						Log.d(TAG, "WIFI_STATE_ENABLED");
						
						// WiFi 已开启
						if (m_wifiMgr != null && m_wifiMgr.isWifiEnabled())
						{
							// 重新开始扫描 WiFi
							restartScanWiFi();
						}
					}

					// 禁用 WiFi
					if (nWiFiState == WifiManager.WIFI_STATE_DISABLED)
					{
						Log.d(TAG, "WIFI_STATE_DISABLED");
						
						// WiFi 开关状态, 关闭
						m_btnWLAN.setChecked(false);
						
						// 禁用"扫描"按钮
						m_btnScan.setEnabled(false);
						m_btnScan.setText(m_strScan);
						
						// 隐藏"WiFi"列表
						m_lvWiFi.setVisibility(View.INVISIBLE);
					}
				}
			}
			catch (Exception e)
			{
			}
		}

		/**
		 * 注册接收器
		 * @param context
		 */
		public void registerReceiver(Context context)
		{
			try
			{
				if (!m_bIsRegisterReceiver)
				{
					m_bIsRegisterReceiver = true;

					IntentFilter filter = new IntentFilter();
					filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
					filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
					filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
					Log.d(TAG, "RegisterReceiver");
					context.registerReceiver(WiFiReceiver.this, filter);
				}
			}
			catch (Exception e)
			{
			}
		}

		/**
		 * 注销接收器
		 * @param context
		 */
		public void unregisterReceiver(Context context)
		{
			try
			{
				if (m_bIsRegisterReceiver)
				{
					m_bIsRegisterReceiver = false;
					Log.d(TAG, "UnregisterReceiver");
					context.unregisterReceiver(WiFiReceiver.this);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
}
