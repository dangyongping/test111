package com.chinafeisite.tianbu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 浏览器布局类
 */
public class BrowserLayoutOld extends RelativeLayout
{
	/**
	 * Activity 的 Context
	 */
	private Context m_context = null;

	/**
	 * 消息处理器
	 */
	private Handler m_handler = new Handler();
	
	/**
	 * 装载浏览器 webview 的视图
	 */
	private View m_viewOwner = null;
	
	/**
	 * 装载浏览器 webview
	 */
	private WebView m_wvBrowser = null;
	
	/**
	 * 装载 webview 和 进度条的视图
	 */
	private View m_vLayoutWebView = null;
	
	/**
	 * 网页加载进度条视图
	 */
	private View m_vWebLoading = null;
	
	/**
	 * 装载输入 URL 网址的布局
	 */
	private LinearLayout m_layoutBrowserUrl = null;
	
	/**
	 * 装载常用标签的网格表
	 */
	private GridView m_gvBrowserNews = null;
	
	/**
	 * 装载购物标签的网格表
	 */
	private GridView m_gvBrowserShopping = null;
	
	/**
	 * 装载收藏标签的网格表
	 */
	private GridView m_gvBrowserFavourite = null;
	
	/**
	 * 输入 URL 网址编辑框
	 */
	private EditText m_edtUrlAddress = null;
	
	/**
	 * 输入 URL 网址的确定按钮
	 */
	private Button m_btnUrlGo = null;
	
	/**
	 * 保存收藏标签配置文件的路径
	 */
	private String m_strFavoriteConfigPath = "";
	
	/**
	 * 常用标签的网格表数据适配器对象
	 */
	private AdapterNewsList m_adapterNews = new AdapterNewsList();
	
	/**
	 * 购物标签的网格表数据适配器对象
	 */
	private AdapterNewsList m_adapterShopping = new AdapterNewsList();
	
	/**
	 * 收藏标签的网格表数据适配器对象
	 */
	private AdapterFavouriteList m_adapterFavourite = new AdapterFavouriteList();
	
	/**
	 * 浏览器布局类构造函数
	 */
	public BrowserLayoutOld(Context context)
	{
		super(context);

		// 保存 Activity 的 context
		m_context = context;

		// 从布局文件中加载浏览器模块视图
		m_viewOwner = LayoutInflater.from(context).inflate(R.layout.layout_browser_old, null);
		
		// 将浏览器视图加载到自定义布局类视图中
		addView(m_viewOwner, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		try
		{
			// 获取浏览器模块各个控件对象
			m_wvBrowser = (WebView) m_viewOwner.findViewById(R.id.wvBrowser);
			m_vLayoutWebView = (View) m_viewOwner.findViewById(R.id.layoutWebView);
			m_layoutBrowserUrl = (LinearLayout) m_viewOwner.findViewById(R.id.layoutBrowserUrl);
			m_gvBrowserNews = (GridView) m_viewOwner.findViewById(R.id.gvBrowserNews);
			m_gvBrowserShopping = (GridView) m_viewOwner.findViewById(R.id.gvBrowserShopping);
			m_gvBrowserFavourite = (GridView) m_viewOwner.findViewById(R.id.gvBrowserFavourite);
			m_edtUrlAddress = (EditText) m_viewOwner.findViewById(R.id.edtUrlAddress);
			m_btnUrlGo = (Button) m_viewOwner.findViewById(R.id.btnUrlGo);
		}
		catch (Exception ex)
		{
			android.util.Log.e("error", "get_BrowserLayout:" + ex.toString());
		}

		try
		{
			// 从布局文件中加载进度条视图
			m_vWebLoading = (View) m_viewOwner.findViewById(R.id.layout_loading);
			//LayoutInflater.from(context).inflate(R.layout.layout_browser_loading, null);
/*
			// 将进度条加载到浏览器中
			m_wvBrowser.addView(m_vWebLoading, new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
*/			
			// 先隐藏
			m_vWebLoading.setVisibility(View.GONE);
		}
		catch (Exception ex)
		{
			android.util.Log.e("error", "get_BrowserLoading:" + ex.toString());
		}
	}
	
	/**
	 * 浏览器加入 Activity 且设置后配置文件目录后, 初始化浏览器各项参数
	 */
	public void initBrowser()
	{
		// 初始化输入网页的 UI 及打开网页的事件
		initBrowserLoadUrlUi();

		// 初始化各类标签及打开从标签打开网页的事件
		initBrowserWebPageLabels();
	}
	
	/**
	 * 设置收藏标签保存图标的位置
	 * @param strPath 收藏标签配置文件保存的路径
	 */
	public void setFavoriteConfigPath(String strPath)
	{
		this.m_strFavoriteConfigPath = strPath;
	}
	
	/**
	 * 隐藏浏览器自定义布局类内的所有控件
	 */
	private void hideAllChild()
	{
		if (null != m_vLayoutWebView)
		{
			m_vLayoutWebView.setVisibility(View.GONE);
		}
		
		if (null != m_wvBrowser)
		{
			m_wvBrowser.setVisibility(View.GONE);
		}
		
		if (null != m_layoutBrowserUrl)
		{
			m_layoutBrowserUrl.setVisibility(View.GONE);
		}
		
		if (null != m_gvBrowserNews)
		{
			m_gvBrowserNews.setVisibility(View.GONE);
		}
		
		if (null != m_gvBrowserShopping)
		{
			m_gvBrowserShopping.setVisibility(View.GONE);
		}
		
		if (null != m_gvBrowserFavourite)
		{
			m_gvBrowserFavourite.setVisibility(View.GONE);
		}
		
		// 若当前收藏标签显示删除按钮, 则同时隐藏删除按钮
		if (null != m_adapterFavourite)
		{
			m_adapterFavourite.setBEnableDelete(false);
			m_adapterFavourite.notifyDataSetChanged();
		}
	}

	/**
	 * 隐藏浏览器自定义布局类内的所有控件及本身对象
	 */
	public void hideAllChildAndOwer()
	{
		hideAllChild();
		this.setVisibility(View.GONE);
	}

	/**
	 * 显示常用标签网格表
	 */
	public void doBrowserNews()
	{
		hideAllChild();
		
		if (null != m_gvBrowserNews)
		{
			m_gvBrowserNews.setVisibility(View.VISIBLE);
		}
		
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示购物标签网格表
	 */
	public void doBrowserShopping()
	{
		hideAllChild();
		
		if (null != m_gvBrowserShopping)
		{
			m_gvBrowserShopping.setVisibility(View.VISIBLE);
		}
		
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示收藏标签网格表
	 */
	public void doBrowserFavourite()
	{
		hideAllChild();
		
		if (null != m_gvBrowserFavourite)
		{
			m_gvBrowserFavourite.setVisibility(View.VISIBLE);
		}
		
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示输入网址界面
	 */
	public void doBrowserLoadUrl()
	{
		hideAllChild();
		
		if (null != m_layoutBrowserUrl)
		{
			m_layoutBrowserUrl.setVisibility(View.VISIBLE);
		}
		
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 退到浏览器浏览过的上页
	 */
	public void doBrowserGoBackPage()
	{		
		hideAllChild();
		
		// 检测是否连接网络
		if (false == checkInternetStatus())
		{
			return;
		}
		
		if (null != m_wvBrowser)
		{			
			// Toast 提示
			String strMsg = "";
			
			// 获取网页网址
			String strUrl =  m_wvBrowser.getOriginalUrl();
			
			// 检测浏览器当前是否有网址
			if (null != strUrl && false == "".equals(strUrl.trim()))
			{				
				// 检测是否可以后退
				if (true == m_wvBrowser.canGoBack())
				{
					// 后退一页
					m_wvBrowser.goBack();
				}
				else
				{					
					// 退到最后一页的提示文字
					strMsg = m_context.getString(R.string.browser_url_goBackLast);
					
					// 界面提示
					Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
				}
				
				m_wvBrowser.setVisibility(View.VISIBLE);
				m_vLayoutWebView.setVisibility(View.VISIBLE);
			}
			else
			{
				// 尚未浏览网页的提示文字
				strMsg = m_context.getString(R.string.browser_url_noBrowsingHistory);
				
				// 界面提示
				Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
			}
		}
		
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 退到浏览器浏览过的下页
	 */
	public void doBrowserGoForwardPage()
	{
		hideAllChild();
		
		// 检测是否连接网络
		if (false == checkInternetStatus())
		{
			return;
		}
		
		if (null != m_wvBrowser)
		{			
			// Toast 提示
			String strMsg = "";
			
			// 获取网页网址
			String strUrl = m_wvBrowser.getOriginalUrl();
			
			// 检测浏览器当前是否有网址
			if (null != strUrl && false == "".equals(strUrl.trim()))
			{				
				// 检测是否可以前进
				if (true == m_wvBrowser.canGoForward())
				{
					// 前进一页
					m_wvBrowser.goForward();
				}
				else
				{
					// 前进到最后一页的提示文字
					strMsg = m_context.getString(R.string.browser_url_goForwardLast);
					
					// 界面提示
					Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
				}
				
				m_wvBrowser.setVisibility(View.VISIBLE);
				m_vLayoutWebView.setVisibility(View.VISIBLE);
			}
			else
			{
				// 尚未浏览网页的提示文字
				strMsg = m_context.getString(R.string.browser_url_noBrowsingHistory);
				
				// 界面提示
				Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
			}
		}
		
		this.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 将当前正在浏览的网页添加到收藏标签
	 */
	public void doBrowserAddCurrentPageToFavourite()
	{
		boolean bPathExist = false;

		// 判断收藏标签配置文件的路径是否存在
		try
		{
			File file = new File(m_strFavoriteConfigPath);
			
			if (null != file && file.exists())
			{
				bPathExist = true;
			}
		}
		catch (Exception ex)
		{
			android.util.Log.e("error", "check_path_favourite:" + ex.toString());
		}
		
		// 路径不存在则不添加到收藏标签, 路径是在外部主程序创建
		if (false == bPathExist)
		{
			return;
		}

		// 对浏览器可视范围截图并保存图片
		Bitmap bmpWeb = null;
		FileOutputStream fosSave = null;
		String strImagePath = "";
		String strTitle = "";
		String strUrl = "";
		boolean bSave = false;
		
		// 判断浏览器当前是否处于可见状态
		if (null != m_wvBrowser && View.VISIBLE == m_wvBrowser.getVisibility() &&
				View.VISIBLE == this.getVisibility())
		{
			try
			{
				// 获取网页 URL 网址
				strUrl = m_wvBrowser.getOriginalUrl();
				
				// 获取网页标题
				strTitle = m_wvBrowser.getTitle();
				
				// 浏览器 WebView 对象转成 View 对象
				View vBrowser = m_wvBrowser;
				
				// 收藏标签图像缩略图的大小
				int nwidthThumb = 170, nheightThumb = 150;
				int nWidth = vBrowser.getWidth(), nHeight = vBrowser.getHeight();
				
				// 按缩略图长宽比例计算需要截图的大小
				if (nWidth > nHeight)
				{
					nWidth = (int) (nHeight * (nwidthThumb / (float) nheightThumb));
					nWidth = nWidth > vBrowser.getWidth() ? vBrowser.getWidth() : nWidth;
				}
				else
				{
					nHeight = (int) (nWidth * (nheightThumb / (float) nwidthThumb));
				}
				
				// 计算出的截图大小要大于 48*48 的大小才截图, 只对应大分辨率屏幕，若小于这个分辨率截出缩放后的图像显示不清楚
				if (nWidth > 48 && nHeight > 48)
				{
					// 生成位图
					bmpWeb = Bitmap.createBitmap(nWidth, nHeight,
							Bitmap.Config.ARGB_8888);
					
					// 浏览图绘制到位图中
					vBrowser.draw(new Canvas(bmpWeb));
					
					// 位图缩放比例
					float width_scale = ((float) nwidthThumb) / nWidth;
					float height_scale = ((float) nheightThumb) / nHeight;
					
					// 生成位图缩放矩阵
					Matrix matrix = new Matrix();
					matrix.postScale(width_scale, height_scale);
					
					// 生成缩放后的位图
					Bitmap bmp_thumb = Bitmap.createBitmap(bmpWeb, 0, 0, nWidth,
							nHeight, matrix, true);

					// 保存位置的文件名, UUID 全球唯一标识码保证文件名不重名
					String filename = "" + UUID.randomUUID().toString() + (".jpg");
					strImagePath = m_strFavoriteConfigPath + "/" + filename;

					// 保存位图到文件
					fosSave = new FileOutputStream(strImagePath);
					bmp_thumb.compress(Bitmap.CompressFormat.JPEG, 100, fosSave);
					
					// 重新设置文件
					strImagePath = filename;

					// 关闭句柄和释放资源
					fosSave.flush();
					fosSave.close();
					bmpWeb.recycle();
					bmp_thumb.recycle();
					bSave = true;
					
					// 垃圾回收
					System.gc();
					System.runFinalization();
					Runtime.getRuntime().gc();
				}
			}
			catch (Exception ex)
			{
				android.util.Log.e("error", "add_favourite:" + ex.toString());
			}
		}

		// 检测截图是否成功
		if (true == bSave)
		{
			// 生成网页标签对象
			WebPageLabel value = new WebPageLabel();
			value.setStrTitle(strTitle);
			value.setStrUrl(strUrl);
			value.setStrImageFileName(strImagePath);
			
			// 收藏夹配置文件路径
			String strPathConfig = m_strFavoriteConfigPath;
			
			// 收藏夹配置文件名
			String strFileNameConfig = "favourite.xml";
			
			// 从位图文件读取位置并检测读取是否成功
			if (true == value.readDrawableFromFile(strPathConfig))
			{
				// 添加到收藏标签网格表适配器
				m_adapterFavourite.addListHasmap(value);

				// 保存到配置文件
				List<WebPageLabel> favourite_list = m_adapterFavourite.getListHasmap();
				saveFavouriteConfigToFile(favourite_list, strPathConfig, strFileNameConfig);
				
				// 已经添加收藏夹的提示文字
				String strMsg = m_context.getString(R.string.browser_url_alreadyAddToFavourite);
				
				// 界面提示
				Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 检测网络状态
	 * @return
	 */
/*	private boolean checkInternetStatus()
	{
		boolean bSuc = false;
		
		try
		{
			// WIFI 管理器
			WifiManager wm = (WifiManager)m_context.getSystemService(Context.WIFI_SERVICE);
			
			// 检测 WIFI 是否可用
			if (wm != null && wm.isWifiEnabled())
			{
				bSuc = true;
			}
		}
		catch (Exception e)
		{
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
*/	
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
	 * 初始化输入网页的 UI 及打开网页的事件
	 */
//	@SuppressLint({ "SetJavaScriptEnabled", "DefaultLocale" })
	private void initBrowserLoadUrlUi()
	{
		if (null != m_wvBrowser)
		{
			// 获取浏览器 WebView 的配置信息
			WebSettings bsBrowser = m_wvBrowser.getSettings();

			// 可以执行 js
			bsBrowser.setJavaScriptEnabled(true);

			// 禁止多窗口显示网页
			bsBrowser.setSupportMultipleWindows(false);

			// 页面是否可以进行缩放。
			bsBrowser.setSupportZoom(false);

			// 是否阻止图像的显示
			bsBrowser.setBlockNetworkImage(false);

			// 优先使用缓存
//			bsBrowser.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);

			// 加载桌面版的网页
//			bsBrowser.setUserAgent(1);

			// 可以使用 Cookie
			CookieManager.getInstance().setAcceptCookie(true);

			// 在控件内打开新网页而不弹出新窗口
			m_wvBrowser.setWebViewClient(new WebViewClient()
			{
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url)
				{
					// 在本对象内打开网页
					view.loadUrl(url);
					return true;
//					return super.shouldOverrideUrlLoading(view, url);
				}

				@Override
				public void onPageFinished(WebView view, String url)
				{
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					
					// 隐藏进度条
					if (null != m_vWebLoading)
					{
						m_vWebLoading.setVisibility(View.GONE);
					}
				}

				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl)
				{
					// 隐藏进度条
					if (null != m_vWebLoading)
					{
						m_vWebLoading.setVisibility(View.GONE);
					}
				}
			});
		}
		
		// 检测输入  URL 网址的编辑框对象是否为空
		if (null != m_edtUrlAddress)
		{
			// 在输入点击回车键打开网页
			m_edtUrlAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
			
			// 编辑框按键侦听事件
			m_edtUrlAddress.setOnKeyListener(new OnKeyListener()
			{
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					// 判断是否为回车键
					if (KeyEvent.KEYCODE_ENTER == keyCode &&
							event.getAction() == KeyEvent.ACTION_DOWN)
					{
						// 调用确定按钮的点击事件
						m_btnUrlGo.performClick();
						return true;
					}
					
					return false;
				}
			});
		}
		
		// 检测输入  URL 网址的确定按钮对象是否为空
		if (null != m_btnUrlGo)
		{
			// 确定按钮添加点击侦听事件
			m_btnUrlGo.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{					
					// 检测是否连接网络
					if (false == checkInternetStatus())
					{
						return;
					}
					
					if (null != m_wvBrowser)
					{
						// 检测网址是否为空
						String strUrl = m_edtUrlAddress.getText().toString().trim();
						
						if (false == "".equals(strUrl))
						{
							// 检测是否以 http:// 开对, 否则添加 http://
							if (false == strUrl.toLowerCase().startsWith("http://"))
							{
								strUrl = "http://" + strUrl;
							}
							
							// 隐藏其他界面
							hideAllChild();
							
							// 停止当前网页的加载(网速慢可能网页还没加载完, 需要停止)
							m_wvBrowser.stopLoading();

							// 显示加载进度条
							m_vWebLoading.setVisibility(View.VISIBLE);
							
							// 加载网页
							m_wvBrowser.loadUrl(strUrl);
							m_wvBrowser.setVisibility(View.VISIBLE);
							m_vLayoutWebView.setVisibility(View.VISIBLE);
						}
					}
				}
			});
		}
	}
	
	/**
	 * 初始化各类标签及打开从标签打开网页的事件
	 */
	private void initBrowserWebPageLabels()
	{
		// 常用标签
		if (null != m_gvBrowserNews)
		{
			// 常用标签网格表添加数据适配器
			m_gvBrowserNews.setAdapter(m_adapterNews);

			// 网格表内单元格点击侦听事件
			m_gvBrowserNews.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					// 获取当前点中的标签
					WebPageLabel value = m_adapterNews.getValueOfIndex(arg2);
					
					// 检测是否连接网络
					if (false == checkInternetStatus())
					{
						return;
					}
					
					if (null != m_wvBrowser)
					{
						// 浏览器加载当前点中标签的网址
						hideAllChild();
						m_wvBrowser.stopLoading();

						// 显示加载进度条
						m_vWebLoading.setVisibility(View.VISIBLE);
						
						// 加载网页
						m_wvBrowser.loadUrl(value.getStrUrl());
						m_wvBrowser.setVisibility(View.VISIBLE);
						m_vLayoutWebView.setVisibility(View.VISIBLE);
					}
				}
			});
		}

		// 购物标签到界面
		if (null != m_gvBrowserShopping)
		{
			// 购物标签网格表添加数据适配器
			m_gvBrowserShopping.setAdapter(m_adapterShopping);

			// 网格表内单元格点击侦听事件
			m_gvBrowserShopping.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					// 获取当前点中的标签
					WebPageLabel value = m_adapterShopping.getValueOfIndex(arg2);
					
					// 检测是否连接网络
					if (false == checkInternetStatus())
					{
						return;
					}
					
					if (null != m_wvBrowser)
					{
						// 浏览器加载当前点中标签的网址
						hideAllChild();
						m_wvBrowser.stopLoading();

						// 显示加载进度条
						m_vWebLoading.setVisibility(View.VISIBLE);
						
						// 加载网页
						m_wvBrowser.loadUrl(value.getStrUrl());
						m_wvBrowser.setVisibility(View.VISIBLE);
						m_vLayoutWebView.setVisibility(View.VISIBLE);
					}

				}
			});
		}

		// 收藏标签
		if (null != m_gvBrowserFavourite)
		{
			// 收藏标签网格表添加数据适配器
			m_gvBrowserFavourite.setAdapter(m_adapterFavourite);

			// 网格表内单元格点击侦听事件
			m_gvBrowserFavourite.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					// 获取当前点中的标签
					WebPageLabel value = m_adapterFavourite.getValueOfIndex(arg2);
					
					if (true != m_adapterFavourite.getBEnableDelete())
					{						
						// 检测是否连接网络
						if (false == checkInternetStatus())
						{
							return;
						}
						
						if (null != m_wvBrowser)
						{
							// 浏览器加载当前点中标签的网址
							hideAllChild();
							m_wvBrowser.stopLoading();

							// 显示加载进度条
							m_vWebLoading.setVisibility(View.VISIBLE);
							
							// 加载网页
							m_wvBrowser.loadUrl(value.getStrUrl());
							m_wvBrowser.setVisibility(View.VISIBLE);
							m_vLayoutWebView.setVisibility(View.VISIBLE);
						}
					}
					else
					{
						try
						{
							// 删除指定位置的标签
							m_adapterFavourite.deleteListHasmap(arg2);
							
							// 获取缩略图存放的路径
							String strImagePath = value.getStrImageFileName();
							
							// 删除缩略图
							File file = new File(strImagePath);
							
							if (file.exists())
							{
								file.delete();
							}
							
							// 释放缩略图引用
							value.getDaImage().setCallback(null);

							// 收藏夹配置文件路径
							String strPathConfig = m_strFavoriteConfigPath;
							
							// 收藏夹配置文件名
							String strFileNameConfig = "favourite.xml";
							
							// 重新保存配置文件
							saveFavouriteConfigToFile(m_adapterFavourite.getListHasmap(), strPathConfig, strFileNameConfig);

							// 删除完成通知 UI 更新界面
							m_handler.post(new Runnable()
							{
								public void run()
								{
									// 通知 UI 更新界面
									m_adapterFavourite.notifyDataSetChanged();
									
									// 已经从收藏夹移除的提示文字
									String strMsg = m_context.getString(R.string.browser_url_alreadyRemoveFromFavourite);
									
									// 界面提示
									Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
								}
							});
						}
						catch (Exception ex)
						{
							android.util.Log.e("error", "del_thumbnail:" + ex.toString());
						}
					}
				}
			});

			// 网格表内单元格长按侦听事件, 用于显示删除收藏标签按钮
			m_gvBrowserFavourite.setOnItemLongClickListener(new OnItemLongClickListener()
			{
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					// 调用线程消息发送器访问 UI
					m_handler.post(new Runnable()
					{
						public void run()
						{
							// 更新收藏标签网格表
							m_adapterFavourite.setBEnableDelete(true);
							m_adapterFavourite.notifyDataSetChanged();
						}
					});

					return true;
				}

			});

			// 开启线程加载收藏标签到界面
			new Thread()
			{
				public void run()
				{
					// 收藏夹配置文件路径
					String strPathConfig = m_strFavoriteConfigPath;
					
					// 收藏夹配置文件名
					String strFileNameConfig = "favourite.xml";
					
					// 从收藏标签配置文件读取配置信息和收藏标签列表
					List<WebPageLabel> listLabels = readFavouriteConfigFromFile(strPathConfig, strFileNameConfig);
					
					if (null != listLabels && 0 < listLabels.size())
					{
						// 将读到的收藏标签列表添加到网格表适配器中
						m_adapterFavourite.addListHasmap(listLabels);
					}

				}
			}.start();
		}
	}

	/**
	 * 创建一个空的标签, 提供外部创建用
	 * @return 空的标签
	 */
	public WebPageLabel createEmptyWebPageLabel()
	{
		WebPageLabel label = new WebPageLabel();
		return label;
	}
	
	/**
	 * 设置常用网页标签
	 * @param listLabels 标签列表
	 */
	public void setNewsWebPageLabels(List<WebPageLabel> listLabels)
	{
		try
		{
			// 清空所有标签
			m_adapterNews.clearListHasmap();
			
			// 添加标签加载到网格表适配器
			if (null != listLabels && listLabels.size() > 0)
			{
				m_adapterNews.addListHasmap(listLabels);
			}
			
			// 显示常用标签网格表
			doBrowserNews();
		}
		catch (Exception ex)
		{
			android.util.Log.e("error",
					"setNewsWebPageLabels:" + ex.toString());
		}
	}
	
	/**
	 * 设置常用网页标签
	 * @param strXmlFile 网页标签 XML 文件
	 */
	public void setNewsWebPageLabels(String strXmlFile)
	{
		List<WebPageLabel> listLabels = new ArrayList<WebPageLabel>();

		try
		{
			// 清空所有标签
			m_adapterNews.clearListHasmap();
			
			// 添加标签加载到网格表适配器
			if (null != listLabels && listLabels.size() > 0)
			{
				m_adapterNews.addListHasmap(listLabels);
			}
		}
		catch (Exception ex)
		{
			android.util.Log.e("error",
					"setNewsWebPageLabels:" + ex.toString());
		}
	}
	
	/**
	 * 设置购物网页标签
	 * @param listLabels 标签列表
	 */
	public void setShoppingWebPageLabels(List<WebPageLabel> listLabels)
	{
		try
		{
			// 清空所有标签
			m_adapterShopping.clearListHasmap();
			
			// 添加标签加载到网格表适配器
			if (null != listLabels && listLabels.size() > 0)
			{
				m_adapterShopping.addListHasmap(listLabels);
			}
		}
		catch (Exception ex)
		{
			android.util.Log.e("error",
					"setShoppingWebPageLabels:" + ex.toString());
		}
	}
	
	/**
	 * 设置购物网页标签
	 * @param strXmlFile 网页标签 XML 文件
	 */
	public void setShoppingWebPageLabels(String strXmlFile)
	{
		List<WebPageLabel> listLabels = new ArrayList<WebPageLabel>();

		try
		{
			// 清空所有标签
			m_adapterShopping.clearListHasmap();
			
			// 添加标签加载到网格表适配器
			if (null != listLabels && listLabels.size() > 0)
			{
				m_adapterShopping.addListHasmap(listLabels);
			}
		}
		catch (Exception ex)
		{
			android.util.Log.e("error",
					"setShoppingWebPageLabels:" + ex.toString());
		}
	}
	
	/**
	 * 保存网页标签到配置文件
	 * @param listLabels 标签列表
	 * @param strPathConfig 配置文件路径
	 * @param strFileNameConfig 配置文件名
	 */
	private void saveFavouriteConfigToFile(List<WebPageLabel> listLabels, String strPathConfig, String strFileNameConfig)
	{
		// 完整路径
		strFileNameConfig = strPathConfig + "/" + strFileNameConfig;
		
		// 收藏标签列表为空则返回, 若数量为零也要保存相当于清空配置文件的内容
		if (null == listLabels)
		{
			return;
		}

		// 保存列表到配置文件
		try
		{
			// 文件对象句柄
			File fileConfig = new File(strFileNameConfig);
			
			if (null != fileConfig)
			{
				// 文件流
				FileOutputStream outStream = new FileOutputStream(fileConfig);
				
				// 保存成 xml 格式
				XmlSerializer serializer = Xml.newSerializer();
				
				// UTF-8 的数据格式
				serializer.setOutput(outStream, "UTF-8");
				serializer.startDocument("UTF-8", true);
				
				// xml 根标记开始标记
				serializer.startTag(null, "datas");
				
				// 根标记下的元素, 只保存网页的标题, 网址, 图标路径
				for (WebPageLabel value : listLabels)
				{
					serializer.startTag(null, "data");

					serializer.startTag(null, "title");
					serializer.text(value.getStrTitle());
					serializer.endTag(null, "title");

					serializer.startTag(null, "url");
					serializer.text(value.getStrUrl());
					serializer.endTag(null, "url");

					serializer.startTag(null, "imagePath");
					serializer.text(value.getStrImageFileName());
					serializer.endTag(null, "imagePath");

					serializer.endTag(null, "data");
				}
				
				// 根标记结束标记
				serializer.endTag(null, "datas");
				serializer.endDocument();
				outStream.flush();
				outStream.close();
			}
		}
		catch (Exception ex)
		{
			android.util.Log.e("error",
					"save_favourite_to_config:" + ex.toString());
		}
	}
	
	/**
	 * 从配置文件读取标签
	 * @param strPathConfig 配置文件路径
	 * @param strFileNameConfig 配置文件名
	 * @return 标签列表
	 */
	private List<WebPageLabel> readFavouriteConfigFromFile(String strPathConfig, String strFileNameConfig)
	{
		List<WebPageLabel> listLabels = null;
		
		// 完整路径
		strFileNameConfig = strPathConfig + "/" + strFileNameConfig;
		
		try
		{
			// 文件对象句柄
			File fileConfig = new File(strFileNameConfig);
			if (null != fileConfig)
			{
				// 文件流
				InputStream inputStream = new FileInputStream(fileConfig);
				WebPageLabel value = null;
				
				// xml及编码格式
				XmlPullParser pullParser = Xml.newPullParser();
				pullParser.setInput(inputStream, "UTF-8");
				int event = pullParser.getEventType();
				
				// 获取每一行的节点
				while (event != XmlPullParser.END_DOCUMENT)
				{
					switch (event)
					{
					case XmlPullParser.START_DOCUMENT:
						
						// xml文档开始, 创建列表对象
						listLabels = new ArrayList<WebPageLabel>();
						break;
						
					// 元素节点开始
					case XmlPullParser.START_TAG:
						
						// 判断是否为 data 标记的元素
						if ("data".equals(pullParser.getName()))
						{
							value = new WebPageLabel();
						}

						// 判断是否为 title 标记
						if ("title".equals(pullParser.getName()))
						{
							value.setStrTitle(pullParser.nextText());
						}
						
						// 判断是否为 url 标记
						if ("url".equals(pullParser.getName()))
						{
							value.setStrUrl(pullParser.nextText());
						}
						
						// 判断是否为 imagePath 标记
						if ("imagePath".equals(pullParser.getName()))
						{
							value.setStrImageFileName(pullParser.nextText());
						}
						break;

					// 元素结束标记
					case XmlPullParser.END_TAG:

						// 判断是否为 data 元素结束
						if ("data".equals(pullParser.getName()))
						{
							// 将标签对象添加到列表中
							if (null != value
									&& true == value.readDrawableFromFile(strPathConfig))
							{
								listLabels.add(value);
							}
							value = null;
						}
						break;
					}
					
					// xml下一行
					event = pullParser.next();
				}
				
				// 关闭文件流
				inputStream.close();
			}
			
		}
		catch (Exception ex)
		{
			android.util.Log.e("error",
					"read_favourite_from_config:" + ex.toString());
		}
		
		return listLabels;
	}
	
	/**
	 * 网页标签类
	 */
	public class WebPageLabel
	{
		/**
		 * 网页标题
		 */
		private String m_strTitle = "";
		
		/**
		 * 网页网址
		 */
		private String m_strUrl = "";
		
		/**
		 * 网页缩略图
		 */
		private Drawable m_daImage = null;
		
		/**
		 * 网页缩略图存放路径
		 */
		private String m_strImageFileName = "";
		
		/**
		 * 获取网页标题
		 * @return
		 */
		public String getStrTitle(){return m_strTitle;}
		
		/**
		 * 设置网页标题
		 * @param strTitle
		 */
		public void setStrTitle(String strTitle){m_strTitle = strTitle;}
		
		/**
		 * 获取网页网址
		 * @return
		 */
		public String getStrUrl(){return m_strUrl;}
		
		/**
		 * 设置网页网址
		 * @param strUrl
		 */
		public void setStrUrl(String strUrl){m_strUrl = strUrl;}
		
		/**
		 * 获取网页缩略图
		 * @return
		 */
		public Drawable getDaImage(){return m_daImage;}
		
		/**
		 * 设置网页缩略图
		 * @param daImage
		 */
		public void setDaImage(Drawable daImage){m_daImage = daImage;}
		
		/**
		 * 获取网页缩略图存放的文件名
		 * @return
		 */
		public String getStrImageFileName(){return m_strImageFileName;}
		
		/**
		 * 设置网页缩略图存放的文件名
		 * @param strImagePath
		 */
		public void setStrImageFileName(String strImagePath){m_strImageFileName = strImagePath;}

		/**
		 * 网页标签构造函数
		 */
		public WebPageLabel()
		{
		}

		/**
		 * 读取网页缩略图
		 * @param strPathImage 缩略图所在的路径
		 * @return 读取是否成功
		 */
		public boolean readDrawableFromFile(String strPath)
		{
			boolean bSuc = false;
			
			// 读取网页缩略图
			m_daImage = getThumbnailFromFile(strPath, m_strImageFileName);
			
			// 判断 drawable 对象是否为空, 为空则表明读取失败
			if (null != m_daImage)
			{
				bSuc = true;
			}
			
			return bSuc;
		}

		/**
		 * 根据文件名读取网页缩略图
		 * @param strImagePath 图片文件路径
		 * @param strImageFileName 图片文件名
		 * @return 网页缩略图
		 */
		public Drawable getThumbnailFromFile(String strImagePath, String strImageFileName)
		{
			Drawable daImage = null;
			Bitmap bmpImage = null;
			
			try
			{
				// 文件的绝对路径
				String strImageFile = strImagePath + "/" + strImageFileName;
				
				// 从文件读入到位图
				bmpImage = theApp.decodeBitmap(strImageFile);
				
				// 判断位图的大小
				if (bmpImage.getWidth() > 0 && bmpImage.getHeight() > 0)
				{
					// 位图转成 drawable 形式
					daImage = new BitmapDrawable(bmpImage);
				}
			}
			catch (Exception ex)
			{
				android.util.Log.e("error",
						"get_image_thumbnail:" + ex.toString());
			}
			finally
			{
				// 垃圾回收
				System.gc();
				System.runFinalization();
				Runtime.getRuntime().gc();
			}
			
			return daImage;
		}
	}
	
	/**
	 * 常用标签和购物标签适配器
	 */
	public class AdapterNewsList extends BaseAdapter
	{	
		/**
		 * 适配器内的标签列表
		 */
		private List<WebPageLabel> m_listHasMap = new ArrayList<WebPageLabel>();
		
		/**
		 * 常用标签和购物标签适配器结构函数
		 */
		public AdapterNewsList()
		{
		}
		
		/**
		 * 添加网页标签到列表
		 * @param value 网页标签
		 */
		public void addListHasmap(WebPageLabel value)
		{
			m_listHasMap.add(value);
		}

		/**
		 * 添加网页标签集合到列表
		 * @param values 网页标签集合
		 */
		public void addListHasmap(List<WebPageLabel> values)
		{
			m_listHasMap.addAll(values);
		}

		/**
		 * 删除列表内指定对象的网页标签
		 * @param value 网页标签
		 */
		public void deleteListHasmap(WebPageLabel value)
		{
			if (true == m_listHasMap.contains(value))
			{
				m_listHasMap.remove(value);
			}
		}

		/**
		 * 删除列表内指定位置的网页标签
		 * @param position 网页标签的位置
		 */
		public void deleteListHasmap(int position)
		{
			if (0 <= position && position < m_listHasMap.size())
			{
				m_listHasMap.remove(position);
			}
		}

		/**
		 * 将网页标签插入到列表的指定位置
		 * @param position 网页标签的位置
		 * @param value 网页标签
		 */
		public void insertListHasmap(int position, WebPageLabel value)
		{
			// 判断给定的位置是否在列表范围内
			if (0 <= position && position <= m_listHasMap.size())
			{
				m_listHasMap.add(position, value);
			}
			else
			{
				// 不在列表范围内则插入到首位
				m_listHasMap.add(0, value);
			}
		}

		/**
		 * 获取适配器内的网页标签列表
		 * @return 网页标签列表
		 */
		public List<WebPageLabel> getListHasmap()
		{
			return m_listHasMap;
		}

		/**
		 * 获取适配器内指定位置的网页标签
		 * @param position 网页标签的位置
		 * @return 网页标签
		 */
		public WebPageLabel getValueOfIndex(int position)
		{
			WebPageLabel value = null;
			
			// 判断给定的位置是否在列表范围内
			if (0 <= position && position < m_listHasMap.size())
			{
				value = m_listHasMap.get(position);
			}
			
			return value;
		}

		/**
		 * 清空适配器内的网页标签
		 */
		public void clearListHasmap()
		{
			m_listHasMap.clear();
		}

		/**
		 * 获取数量, BaseAdapter 类重载
		 * @return 列表数量
		 */
		@Override
		public int getCount()
		{
			return m_listHasMap.size();
		}

		/**
		 * 获取指定位置的对象, BaseAdapter 类重载
		 * @param position 位置
		 * @return 列表内指定位置的对象
		 */
		@Override
		public Object getItem(int position)
		{
			return position;
		}

		/**
		 * 获取指定位置的对象的 ID, BaseAdapter 类重载
		 * @param position 位置
		 * @return 列表内指定位置的对象的ID
		 */
		@Override
		public long getItemId(int position)
		{
			return position;
		}

		/**
		 * 获取网格表指定位置的单元格视图, BaseAdapter 类重载
		 * @param position 位置
		 * @param view 单元格视图
		 * @param parent 单元格父视图
		 * @return 修改过后的单元格视图
		 */
		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			// 检测单元格视图是否为空
			if (null == view)
			{
				// 从布局文件中加载视图
				view = LayoutInflater.from(m_context).inflate(
						R.layout.gridview_item_browser_news, null);
			}
			
			// 从布局文件加载不成功则返回
			if (null == view)
			{
				return view;
			}
			
			// 获取当前单元格位置对象的网页标签
			final WebPageLabel value = m_listHasMap.get(position);
			
			if (null != value)
			{
				// 显示网页标签的图标
				ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
				ivImage.setImageDrawable(value.getDaImage());
			}

			return view;
		}
	}

	/**
	 * 收藏标签适配器类
	 */
	public class AdapterFavouriteList extends BaseAdapter
	{
		/**
		 * 适配器内的标签列表
		 */
		private List<WebPageLabel> m_listHasMap = new ArrayList<WebPageLabel>();
		
		/**
		 * 是否显示删除按钮
		 */
		private boolean bEnableDelete = false;

		/**
		 * 收藏标签适配器结构函数
		 */
		public AdapterFavouriteList()
		{

		}
		
		/**
		 * 是否显示删除按钮
		 * @return
		 */
		public boolean getBEnableDelete(){return bEnableDelete;}
				
		/**
		 * 设置是否显示删除按钮
		 * @param bEnable 是否显示
		 */
		public void setBEnableDelete(boolean bEnable)
		{
			bEnableDelete = bEnable;
		}
		
		/**
		 * 添加网页标签到列表
		 * @param value 网页标签
		 */
		public void addListHasmap(WebPageLabel value)
		{
			m_listHasMap.add(value);
		}

		/**
		 * 添加网页标签集合到列表
		 * @param values 网页标签集合
		 */
		public void addListHasmap(List<WebPageLabel> values)
		{
			m_listHasMap.addAll(values);
		}

		/**
		 * 删除列表内指定对象的网页标签
		 * @param value 网页标签
		 */
		public void deleteListHasmap(WebPageLabel value)
		{
			if (true == m_listHasMap.contains(value))
			{
				m_listHasMap.remove(value);
			}
		}

		/**
		 * 删除列表内指定位置的网页标签
		 * @param position 网页标签的位置
		 */
		public void deleteListHasmap(int position)
		{
			if (0 <= position && position < m_listHasMap.size())
			{
				m_listHasMap.remove(position);
			}
		}

		/**
		 * 将网页标签插入到列表的指定位置
		 * @param position 网页标签的位置
		 * @param value 网页标签
		 */
		public void insertListHasmap(int position, WebPageLabel value)
		{
			// 判断给定的位置是否在列表范围内
			if (0 <= position && position <= m_listHasMap.size())
			{
				m_listHasMap.add(position, value);
			}
			else
			{
				// 不在列表范围内则插入到首位
				m_listHasMap.add(0, value);
			}
		}

		/**
		 * 获取适配器内的网页标签列表
		 * @return 网页标签列表
		 */
		public List<WebPageLabel> getListHasmap()
		{
			return m_listHasMap;
		}

		/**
		 * 获取适配器内指定位置的网页标签
		 * @param position 网页标签的位置
		 * @return 网页标签
		 */
		public WebPageLabel getValueOfIndex(int position)
		{
			WebPageLabel value = null;
			
			// 判断给定的位置是否在列表范围内
			if (0 <= position && position < m_listHasMap.size())
			{
				value = m_listHasMap.get(position);
			}
			
			return value;
		}

		/**
		 * 清空适配器内的网页标签
		 */
		public void clearListHasmap()
		{
			m_listHasMap.clear();
		}

		/**
		 * 获取数量, BaseAdapter类重载
		 * @return 列表数量
		 */
		@Override
		public int getCount()
		{
			return m_listHasMap.size();
		}

		/**
		 * 获取指定位置的对象, BaseAdapter 类重载
		 * @param position 位置
		 * @return 列表内指定位置的对象
		 */
		@Override
		public Object getItem(int position)
		{
			return position;
		}

		/**
		 * 获取指定位置的对象的 ID, BaseAdapter 类重载
		 * @param position 位置
		 * @return 列表内指定位置的对象的 ID
		 */
		@Override
		public long getItemId(int position)
		{
			return position;
		}

		/**
		 * 获取网格表指定位置的单元格视图, BaseAdapter 类重载
		 * @param position 位置
		 * @param view 单元格视图
		 * @param parent 单元格父视图
		 * @return 修改过后的单元格视图
		 */
		public View getView(int position, View view, ViewGroup parent)
		{
			// 检测单元格视图是否为空
			if (null == view)
			{
				// 从布局文件中加载视图
				view = LayoutInflater.from(m_context).inflate(
						R.layout.gridview_item_browser_favourite, null);
			}
			
			// 从布局文件加载不成功则返回
			if (null == view)
			{
				return view;
			}
			
			// 获取当前单元格位置对象的网页标签
			final int nIndex = position;
			final WebPageLabel value = m_listHasMap.get(nIndex);
			
			if (null != value)
			{
				// 获取显示标签的控件
				ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
				TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
				ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

				// 标签缩略图
				ivImage.setImageDrawable(value.getDaImage());
				
				// 标签标题(只显示七个汉字)
				txtTitle.setText(value.getStrTitle());
				
				// 是否显示删除按钮
				ivDelete.setVisibility(bEnableDelete ? View.VISIBLE : View.GONE);
			}

			return view;
		}
	}
}
