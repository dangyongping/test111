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
 * 注册布局类
 */
public class SignInLayout extends RelativeLayout implements OnClickListener
{
	private final String TAG = "LoginLayout";

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;
	
	// 控件
	private Button m_btnOK = null;
	private Button m_btnReturn = null;
	private EditText m_edtAccount = null;
	private EditText m_edtPassword1 = null;
	private EditText m_edtPassword2 = null;
	
	/**
	 * 网站网址
	 */
	private String m_strUrl = theApp.getUrl();

	/**
	 * Toast
	 */
	private static final int MSG_TOAST = 0;

	/**
	 * 构造函数
	 * @param context
	 */
	public SignInLayout(Context context)
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
	public SignInLayout(Context context, AttributeSet attrs)
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
	public SignInLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	public void onClick(View view)
	{
		// 确定
		if (view.getId() == R.id.btnOK)
		{
			signIn();
		}
		
		// 返回
		if (view.getId() == R.id.btnReturn)
		{
			// 隐藏
			SignInLayout.this.setVisibility(View.INVISIBLE);
			((MainActivity)theApp.getCurActivity()).hideViewSignIn();
			
			// 布局视图登陆
			((MainActivity)theApp.getCurActivity()).layoutViewLogin();
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_sign_in, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			// 控件
			m_btnOK = (Button)m_view.findViewById(R.id.btnOK);
			m_btnReturn = (Button)m_view.findViewById(R.id.btnReturn);
			m_btnOK.setOnClickListener(this);
			m_btnReturn.setOnClickListener(this);
			
			m_edtAccount = (EditText)findViewById(R.id.edtAccount);
			m_edtPassword1 = (EditText)findViewById(R.id.edtPassword1);
			m_edtPassword2 = (EditText)findViewById(R.id.edtPassword2);
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
        	
        	// 提取账号密码
        	final String strAccount = m_edtAccount.getText().toString().trim();
        	final String strPassword1 = m_edtPassword1.getText().toString().trim();
        	final String strPassword2 = m_edtPassword2.getText().toString().trim();
        	
        	// 注册成功或失败的提示信息
        	final String strSigninOk = m_context.getResources().getText(R.string.treadmill_sign_in_success).toString();
        	final String strSigninFalse = m_context.getResources().getText(R.string.treadmill_sign_in_false).toString();
        	final String strSigninExist = m_context.getResources().getText(R.string.treadmill_sign_in_exist).toString();
        	
        	// 检测账号是否为空
        	if ("".equals(strAccount))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_input_account);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	}
        	
        	// 检测密码是否为空
        	if ("".equals(strPassword1) || "".equals(strPassword2))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_input_pwd);
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
        	if (!checkStr(strPassword1) || !checkStr(strPassword2) ||
        		!strPassword1.equals(strPassword2))
        	{
        		String strToast = (String)m_context.getResources().getText(R.string.treadmill_pwd_error);
        		Toast.makeText(m_context, strToast, Toast.LENGTH_LONG).show();
        		return;
        	}

        	// 线程提交注册信息
            new Thread() 
            {  
                public void run()
                {
                	// 注册接口
                	String strUrl = m_strUrl + "checkUser?";
                	boolean bIsSuccess = false, bIsExist = false;
                	
                	// 注册参数
                	List<BasicNameValuePair> listParams = new LinkedList<BasicNameValuePair>();
                	listParams.add(new BasicNameValuePair("username", strAccount));
                	listParams.add(new BasicNameValuePair("password", strPassword1));
                	listParams.add(new BasicNameValuePair("type", "1")); 

                	// http://www.ttpaobu.com/checkUser?username=abcdef&password=123456&type=1
                	
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
    	                String strResponse= httpClient.execute(getMethod, responseHandler);
    	                
    	                // 转成 utf-8 的编码, 若还是乱码则用 unicode 编码
    	                strResponse = new String(strResponse.getBytes("ISO-8859-1"),"UTF-8");

    	                // 返回值转 json 数据格式
                        JSONObject json = new JSONObject(strResponse);

                        // {"birthday":"1986-01-01","email":"","gender":0,"height":170,"id":0,"nickname":"abcdef","password":"123456","property":0,"targetWeight":60,"target_size_week":35,"target_type_week":0,"userId":236,"weight":60}
                        
                        // 判断是否注册成功
                        if (json.has("userId"))
                        {
                        	bIsSuccess = true;
                        }  
                        else
                        {
                        	// 判断账号已经被注册
                    		bIsExist = true;
                        	bIsSuccess = false;
                        }            
    				} 
    				catch (Exception e) 
    				{
    				}
    				
    				if (bIsSuccess)
    				{
    					// 用户名和密码
    					theApp.setAccount(strAccount);
    					theApp.setPassword(strPassword1);
    				}
    				
    				// 最后的提示信息
    				final String strMsg = bIsSuccess ? strSigninOk : (bIsExist ? strSigninExist : strSigninFalse);
					Message msg = m_handler.obtainMessage();
					msg.what = MSG_TOAST;
					msg.obj = strMsg;
					m_handler.sendMessage(msg);
					
                }}.start();
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

					// 注册成功
					String strSignInOk = m_context.getResources().getText(R.string.treadmill_sign_in_success).toString();
					
					if (strMsg.equals(strSignInOk))
					{
						// 隐藏
						SignInLayout.this.setVisibility(View.INVISIBLE);
						((MainActivity)theApp.getCurActivity()).hideViewSignIn();
						
						// 布局视图登陆
						((MainActivity)theApp.getCurActivity()).layoutViewLogin();
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
