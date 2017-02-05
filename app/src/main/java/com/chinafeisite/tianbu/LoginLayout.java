package com.chinafeisite.tianbu;

import java.util.LinkedList;
import java.util.List;

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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 登陆布局类
 */
public class LoginLayout extends RelativeLayout implements OnClickListener
{
	private final String TAG = "LoginLayout";

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;
	
	// 控件
	private Button m_btnEntry = null;
	private Button m_btnReturn = null;
	private Button m_btnSignin = null;
	private EditText m_edtAccount = null;
	private EditText m_edtPassword = null;
	
	/**
	 * 网站网址
	 */
	private String m_strUrl = theApp.getUrl();
		
	/**
	 * 登陆的账号信息
	 */
	private AccountInfo m_aiAcountInfo = null;
	
	/**
	 * 上次锻炼的信息
	 */
//	private TreadmillsInfo m_tmiLastExercise = null;

	// 消息
	private static final int MSG_TOAST = 0; // Toast

	/**
	 * 构造函数
	 * @param context
	 */
	public LoginLayout(Context context)
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
	public LoginLayout(Context context, AttributeSet attrs)
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
	public LoginLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	public void onClick(View view)
	{
		// 确定
		if (view.getId() == R.id.btnEntry)
		{
			login();
		}
		
		// 注册
		if (view.getId() == R.id.btnSignIn)
		{
			signIn();
		}
		
		// 返回
		if (view.getId() == R.id.btnReturn)
		{
			// 隐藏
			LoginLayout.this.setVisibility(View.INVISIBLE);
			((MainActivity)theApp.getCurActivity()).hideViewLogin();
			((MainActivity)theApp.getCurActivity()).hideViewSignIn();
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_login, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			// 控件
			m_btnEntry  = (Button)m_view.findViewById(R.id.btnEntry);
			m_btnReturn = (Button)m_view.findViewById(R.id.btnReturn);
			m_btnSignin = (Button)m_view.findViewById(R.id.btnSignIn);
			m_btnEntry.setOnClickListener(this);
			m_btnReturn.setOnClickListener(this);
			m_btnSignin.setOnClickListener(this);
			
			m_edtAccount = (EditText)findViewById(R.id.edtAccount);
			m_edtPassword = (EditText)findViewById(R.id.edtPassword);
			m_edtAccount.setText(theApp.getAccount());
			m_edtPassword.setText(theApp.getPassword());
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 检测字符串
	 * @param str
	 * @return
	 */
	private boolean checkStr(final String str)
	{
		try
		{
			boolean b = false;
			
			String strBase = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			
			int nLen1 = str.length();
			int nLen2 = strBase.length();
			
			for (int i = 0; i < nLen1; i++)
			{
				b = false;
				
				char ch1 = str.charAt(i);
				
				for (int j = 0; j < nLen2; j++)
				{
					char ch2 = strBase.charAt(j);
					
					if (ch1 == ch2)
					{
						b = true;
						break;
					}
				}
				
				if (!b)
				{
					break;
				}
			}
			
			return b;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 登陆
	 */
	private void login()
	{
		try
		{
			// 检测是否连接网络
			if (false == checkInternetStatus())
			{
				return;
			}
			
			// 账号密码
			final String strAccount = m_edtAccount.getText().toString().trim();
	    	final String strPassword = m_edtPassword.getText().toString().trim();
	    	final String strTypeName = String.valueOf(theApp.getAccType());
	    	
        	// 登陆成功或失败的提示信息
        	final String strLoginOk = m_context.getResources().getText(R.string.treadmill_login_success).toString();
        	final String strLoginFalse = m_context.getResources().getText(R.string.treadmill_login_false).toString();
        	
        	// 检测账号是否为空
        	if ("".equals(strAccount))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_input_account);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	}
        	
        	// 检测密码是否为空
        	if ("".equals(strPassword))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_input_pwd);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	} 
        	
        	// 检测帐号类型
        	if ("".equals(strTypeName))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_input_member_typename);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	}
        	
        	// 检测帐号
        	if (!checkStr(strAccount))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_account_error);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	}
        	
        	// 检测密码
        	if (!checkStr(strPassword))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_pwd_error);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	}

        	// 线程提交登陆信息
            new Thread() 
            {  
                public void run()
                {
                	// 登陆接口
                	String strUrl = m_strUrl + "checkUser?";
                	boolean bIsSuccess = false;
                	int nTypeName = Integer.valueOf(strTypeName);
                	
                	// 登陆参数
                	List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>(); 
                	listParams.add(new BasicNameValuePair("username", strAccount));
                	listParams.add(new BasicNameValuePair("password", strPassword));
                	
                	// http://www.ttpaobu.com/checkUser?username=abcdef&password=123456
                	
                	// 参数转 utf-8  的 url 编码
                	strUrl = strUrl + URLEncodedUtils.format(listParams, "UTF-8");  
                	
    				try 
    				{	            	
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
    	                strResponse = new String(strResponse.getBytes("ISO-8859-1"),"UTF-8");

    	                // 返回值转 json 数据格式
                        JSONObject json = new JSONObject(strResponse);
                        
                        // {"birthday":"1986-01-01","email":"","gender":0,"height":170,"id":0,"nickname":"abcdef","password":"123456","property":0,"targetWeight":60,"target_size_week":35,"target_type_week":0,"userId":236,"weight":60}
                        
                        // 判断是否登陆成功
                        if (json.has("userId"))
                        {
                        	bIsSuccess = true;
                        	
                        	// 判断是否登陆成功
                        	if (bIsSuccess)
                        	{
                        		m_aiAcountInfo = new AccountInfo();
                        		
                    			m_aiAcountInfo.setNMemberTypeName(nTypeName);
                        		
                        		// 用户 id
                        		if (json.has("userId"))
                        		{
                        			m_aiAcountInfo.setNUserId(json.getInt("userId"));
                        		}
                        		
                        		// 身高
                        		if (json.has("height"))
                        		{
                        			int nValue = json.getInt("height");
                        			double dValue = (double)nValue / 100.0;
                        			m_aiAcountInfo.setDHeight(dValue);
                        		}
                        		
                        		// 体重
                        		if (json.has("weight"))
                        		{
                        			double dValue = json.getDouble("weight");
                        			m_aiAcountInfo.setDWeight(dValue);
                        		}
                        	}
                        }
    				}
    				catch (Exception e) 
    				{
    	                android.util.Log.e(TAG, "error:" + e.getMessage());
    				}
    				
    				if (bIsSuccess)
    				{
    					// 已登陆
    					theApp.setLogined(true);
    					
    					// 用户名和密码
    					theApp.setAccount(strAccount);
    					theApp.setPassword(strPassword);
    					theApp.setAccInfo(m_aiAcountInfo);
    				}
    				
    				// 最后的提示信息
    				final String strMsg = bIsSuccess ? strLoginOk : strLoginFalse;
					Message msg = m_handler.obtainMessage();
					msg.what = MSG_TOAST;
					msg.obj = strMsg;
					m_handler.sendMessage(msg);
					
                }}.start();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 注册
	 */
	private void signIn()
	{
		try
		{
			// 检测是否连接网络
			if (false == checkInternetStatus())
			{
				return;
			}
			
			// 布局视图注册
			((MainActivity)theApp.getCurActivity()).layoutViewSignIn();
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * 检测网络状态
	 * @return
	 */
	private boolean checkInternetStatus()
	{
		boolean bSuc = false;
		ConnectivityManager connManager = null;
		
        try
        {
        	// 检测是否为 wifi 上网方式
            connManager = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
            		connManager = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);                    
            	}
            	
                NetworkInfo mMOBILE = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (mMOBILE.isConnected())
                {
                	bSuc = true;
                }
            }
    		catch(Exception ex)
    		{
    			android.util.Log.e("error", "checkInternetStatus_wifi:" + ex.toString());
    		}
        }
        
        // 检测失败
        if (false == bSuc)
        {			
			// 设备尚未连接网络
			String strMsg = m_context.getString(R.string.treadmill_netUnconnect);
			
			// 界面提示
			Toast.makeText(m_context, strMsg, Toast.LENGTH_LONG).show();
        }
        
        return bSuc;
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
			// Toast
			case MSG_TOAST:
				try
				{
					// 消息
					String strMsg = msg.obj.toString();
					Toast.makeText(m_context, strMsg, Toast.LENGTH_LONG).show();

					String strLoginOk = m_context.getResources().getText(R.string.treadmill_login_success).toString();
					
					if (strMsg.equals(strLoginOk))
					{
						// 应答 EC
//						theApp.responseEC();
						
						// 登陆的帐号信息
						if (theApp.getAccInfo() != null)
						{
							// 清空用户链表
							theApp.getListUserS().clear();
							
							// 成员编号
							int nUser = theApp.getAccInfo().getNMemberTypeName();
							String strUser = theApp.i2Str(nUser, 2);
							
							// 年龄
							int nAge = theApp.getAccInfo().getNAge();
							String strAge = String.format("%d", nAge);
							
							// 身高
							double dHeight = theApp.getAccInfo().getDHeight();
							int nHeight = (int)(dHeight * 100);
							String strHeight = String.format("%d", nHeight);
							
							// 体重
							int nWeight = (int)theApp.getAccInfo().getDWeight();
							String strWeight = String.format("%d", nWeight);
							
							// 周目标
							int nWeekTarget = (int)theApp.getAccInfo().getDGoal();
							String strWeekTarget = String.format("%d", nWeekTarget);
							
							// 用户
							strUser += "," + strAge + "," + strHeight + "," + strWeight + "," + strWeekTarget;
							theApp.getListUserS().add(strUser);
						}
						
						if (((MainActivity)theApp.getCurActivity()).getHistoryLayout() != null)
						{
							// 移除历史记录
							((MainActivity)theApp.getCurActivity()).removeHistory();
						}
						
						// 下载本年数据
						((MainActivity)theApp.getCurActivity()).downloadDataYear();
						
						// 隐藏
						LoginLayout.this.setVisibility(View.INVISIBLE);
						((MainActivity)theApp.getCurActivity()).hideViewLogin();
						((MainActivity)theApp.getCurActivity()).hideViewSignIn();
					}
				}
				catch (Exception e)
				{
				}
				break;
			}
		}
	};
}
