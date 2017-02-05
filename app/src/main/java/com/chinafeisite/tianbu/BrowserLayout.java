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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 浏览器布局类
 */
public class BrowserLayout extends RelativeLayout
{
	private final String TAG = "BrowserLayout";

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
	private CustomWebView mCurrentWebView = null;
	
	/**
	 * 暂停
	 */
	public void onPause()
	{
		try
		{			
			mCurrentWebView.loadData("", "text/html", "utf-8");
			mCurrentWebView.loadUrl("");
			mCurrentWebView.stopLoading();
		}
		catch (Exception e)
		{
		}
	
		try
		{
			mCurrentWebView.doOnPause();
		}
		catch (Exception e)
		{
		}
	}

	// GO 按钮
	private ImageButton mGoButton;
	
	// 进度条
	private ProgressBar mProgressBar;
	private Drawable mCircularProgress;

	// URL
	private TextWatcher mUrlTextWatcher = null;
	private AutoCompleteTextView mUrlEditText = null;
	private View mCustomView;
	private GestureDetector mGestureDetector;
	private ImageButton mPreviousButton;
	private Bitmap mDefaultVideoPoster = null;
	private View mVideoProgressView = null;
	private FrameLayout mCustomViewContainer;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;
	
	// 是否显示自定义视图
	private boolean m_bShowCustomView = false;

	/**
	 * 装载 webview 和 进度条的视图
	 */
	private View m_vLayoutWebView = null;
	
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
	 * 构造函数
	 * @param context
	 */
	public BrowserLayout(Context context)
	{
		super(context);

		// 保存 Activity 的 context
		m_context = context;

		// 从布局文件中加载浏览器模块视图
		m_viewOwner = LayoutInflater.from(context).inflate(R.layout.layout_browser, null);
		
		// 将浏览器视图加载到自定义布局类视图中
		addView(m_viewOwner, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		try
		{
			// 获取浏览器模块各个控件对象
			mCurrentWebView = (CustomWebView)m_viewOwner.findViewById(R.id.wvBrowser);
			m_vLayoutWebView = (View) m_viewOwner.findViewById(R.id.layoutWebView);
			m_gvBrowserNews = (GridView) m_viewOwner.findViewById(R.id.gvBrowserNews);
			m_gvBrowserShopping = (GridView) m_viewOwner.findViewById(R.id.gvBrowserShopping);
			m_gvBrowserFavourite = (GridView) m_viewOwner.findViewById(R.id.gvBrowserFavourite);
		}
		catch (Exception ex)
		{
			android.util.Log.e("error", "get_BrowserLayout:" + ex.toString());
		}
		
		try
		{
			mCustomViewContainer = (FrameLayout)m_viewOwner.findViewById(R.id.custom_content);
			
			mCircularProgress = getResources().getDrawable(R.drawable.spinner);

			// 手势侦测器
			mGestureDetector = new GestureDetector(m_context, new GestureListener());
			
			String[] from = new String[] { UrlSuggestionCursorAdapter.URL_SUGGESTION_TITLE, UrlSuggestionCursorAdapter.URL_SUGGESTION_URL };
			int[] to = new int[] { R.id.AutocompleteTitle, R.id.AutocompleteUrl };

			UrlSuggestionCursorAdapter adapter = new UrlSuggestionCursorAdapter(m_context, R.layout.url_autocomplete_line, null, from, to);

			adapter.setCursorToStringConverter(new CursorToStringConverter()
			{
				@Override
				public CharSequence convertToString(Cursor cursor)
				{
					String aColumnString = cursor.getString(cursor.getColumnIndex(UrlSuggestionCursorAdapter.URL_SUGGESTION_URL));
					return aColumnString;
				}
			});

			mUrlEditText = (AutoCompleteTextView)findViewById(R.id.textUri);
			mUrlEditText.setThreshold(1);
			mUrlEditText.setAdapter(adapter);

			mUrlEditText.setOnKeyListener(new View.OnKeyListener()
			{
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_ENTER)
					{
						hideAllChild();
						
						mCurrentWebView.setVisibility(View.VISIBLE);
						m_vLayoutWebView.setVisibility(View.VISIBLE);
						
						navigateToUrl();
						return true;
					}

					return false;
				}
			});

			mUrlTextWatcher = new TextWatcher()
			{
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
				{
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
				{
				}

				@Override
				public void afterTextChanged(Editable arg0)
				{
					updateGoButton();
				}
			};

			mUrlEditText.addTextChangedListener(mUrlTextWatcher);

			mUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
			{

				@Override
				public void onFocusChange(View v, boolean hasFocus)
				{
					// Select all when focus gained.
					if (hasFocus)
					{
						mUrlEditText.setSelection(0, mUrlEditText.getText().length());
					}
				}
			});

			mUrlEditText.setCompoundDrawablePadding(5);

			// 常用标签
			ImageButton btnNews = (ImageButton)findViewById(R.id.btnNews);
			btnNews.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					doBrowserNews();
				}
			});
			
			// 刷新
			ImageButton btnRefresh = (ImageButton)findViewById(R.id.btnRefresh);
			btnRefresh.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					refresh();
				}
			});
			
			// 添加收藏
			ImageButton btnAdd = (ImageButton)findViewById(R.id.btnAdd);
			btnAdd.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					doBrowserAddCurrentPageToFavourite();
				}
			});
			
			// 显示收藏夹
			ImageButton btnMark = (ImageButton)findViewById(R.id.btnMark);
			btnMark.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					doBrowserFavourite();
				}
			});
			
			mGoButton = (ImageButton)findViewById(R.id.btnGo);
			mGoButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					hideAllChild();
					
					mCurrentWebView.setVisibility(View.VISIBLE);
					m_vLayoutWebView.setVisibility(View.VISIBLE);
					
					if (mCurrentWebView.isLoading())
					{
						mCurrentWebView.stopLoading();
					}
					else if (!mCurrentWebView.isSameUrl(mUrlEditText.getText().toString()))
					{
						navigateToUrl();
					}
					else
					{
						mCurrentWebView.reload();
					}
				}
			});
			
			mProgressBar = (ProgressBar)findViewById(R.id.proLoading);
			mProgressBar.setMax(100);

			// 上一页
			mPreviousButton = (ImageButton)findViewById(R.id.btnPrev);
			mPreviousButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					previous();
				}
			});
		}
		catch (Exception e)
		{
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = 0;
		float y = 0;
		int nAction = 0;

		// 根据比例计算坐标
		float fWR = theApp.getWidthRatio();
		float fHR = theApp.getHeightRatio();
		if (fWR <= 0) fWR = 1;
		if (fHR <= 0) fHR = 1;
		
		try
		{
			// 在视图中的坐标
			x = event.getX();
			y = event.getY();
			x /= fWR;
			y /= fHR;
			theApp.SLog(TAG, String.format("X: %d, Y: %d", (int)x, (int)y));
			
			// 触摸动作
			nAction = event.getAction() & MotionEvent.ACTION_MASK;
		}
		catch (Exception e)
		{
		}
		
		try
		{
			switch (nAction)
			{
			// 主点按下
			// 辅点按下
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				
				float t = mPreviousButton.getTop();
				float l = mPreviousButton.getLeft();
				float w = mPreviousButton.getWidth();
				float h = mPreviousButton.getHeight();
				
				if ((l <= x && x <= l + w) &&
					(t <= y && y <= t + h))
				{
					// 显示自定义视图
					if (m_bShowCustomView)
					{
						// 上一页
						theApp.SLog(TAG, "previous");
						previous();
					}
				}
				break;
			}
		}
		catch (Exception e)
		{
		}
		
		return true;
	}
	
	/**
	 * 刷新
	 */
	private void refresh()
	{
		hideAllChild();
		
		mCurrentWebView.setVisibility(View.VISIBLE);
		m_vLayoutWebView.setVisibility(View.VISIBLE);
		
		mCurrentWebView.reload();
	}
	
	/**
	 * 上一页
	 */
	private void previous()
	{
		theApp.SLog(TAG, "btnPrev - onClick");
		
		// 显示自定义视图
		if (m_bShowCustomView)
		{
			theApp.SLog(TAG, "ShowCustomView");
			
			// 刷新
			refresh();
		}
		else
		{
			// 导航到上一页
			navigatePrevious();
		}
	}

	/**
	 * Update the "Go" button image.
	 */
	private void updateGoButton()
	{
		if (mCurrentWebView.isLoading())
		{
			mUrlEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCircularProgress, null);
			((AnimationDrawable)mCircularProgress).start();
		}
		else
		{
			mUrlEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			((AnimationDrawable)mCircularProgress).stop();
		}
	}

	/**
	 * Navigate to the previous page in history.
	 */
	private void navigatePrevious()
	{
		// Needed to hide toolbars properly.
		mUrlEditText.clearFocus();

		hideKeyboard();
		mCurrentWebView.goBack();
	}

	/**
	 * Navigate to the url given in the url edit text.
	 */
	private void navigateToUrl()
	{
		navigateToUrl(mUrlEditText.getText().toString());
	}

	/**
	 * Navigate to the given url.
	 * 
	 * @param url
	 *            The url.
	 */
	private void navigateToUrl(String url)
	{
		// Needed to hide toolbars properly.
		mUrlEditText.clearFocus();

		if ((url != null) && (url.length() > 0))
		{
			if (url.contains("."))
			{
				url = checkUrl(url);
			}

			hideKeyboard();

			mCurrentWebView.loadUrl(url);
		}
	}
	
	/**
	 * Check en url. Add http:// before if missing.
	 * @param url The url to check.
	 * @return The modified url if necessary.
	 */
	private String checkUrl(String url)
	{
		if ((url != null) && (url.length() > 0))
		{
			if ((!url.startsWith("http://")) &&
				(!url.startsWith("https://")) &&
				(!url.startsWith("file://")))
			{
				url = "http://" + url;
			}
		}

		return url;
	}

	/**
	 * Hide the keyboard
	 */
	private void hideKeyboard()
	{
		InputMethodManager imm = (InputMethodManager)m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mUrlEditText.getWindowToken(), 0);
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
		// 隐藏自定义视图
		hideCustomView();
		
		if (null != m_vLayoutWebView)
		{
			m_vLayoutWebView.setVisibility(View.GONE);
		}
		
		if (null != mCurrentWebView)
		{
			mCurrentWebView.setVisibility(View.GONE);
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
		
		if (null != mCurrentWebView)
		{			
			// Toast 提示
			String strMsg = "";
			
			// 获取网页网址
			String strUrl =  mCurrentWebView.getOriginalUrl();
			
			// 检测浏览器当前是否有网址
			if (null != strUrl && false == "".equals(strUrl.trim()))
			{				
				// 检测是否可以后退
				if (true == mCurrentWebView.canGoBack())
				{
					// 后退一页
					mCurrentWebView.goBack();
				}
				else
				{					
					// 退到最后一页的提示文字
					strMsg = m_context.getString(R.string.browser_url_goBackLast);
					
					// 界面提示
					Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
				}
				
				mCurrentWebView.setVisibility(View.VISIBLE);
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
		
		if (null != mCurrentWebView)
		{			
			// Toast 提示
			String strMsg = "";
			
			// 获取网页网址
			String strUrl = mCurrentWebView.getOriginalUrl();
			
			// 检测浏览器当前是否有网址
			if (null != strUrl && false == "".equals(strUrl.trim()))
			{				
				// 检测是否可以前进
				if (true == mCurrentWebView.canGoForward())
				{
					// 前进一页
					mCurrentWebView.goForward();
				}
				else
				{
					// 前进到最后一页的提示文字
					strMsg = m_context.getString(R.string.browser_url_goForwardLast);
					
					// 界面提示
					Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();
				}
				
				mCurrentWebView.setVisibility(View.VISIBLE);
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
		if (null != mCurrentWebView && View.VISIBLE == mCurrentWebView.getVisibility() &&
				View.VISIBLE == this.getVisibility())
		{
			try
			{
				// 获取网页 URL 网址
				strUrl = mCurrentWebView.getOriginalUrl();
				
				// 获取网页标题
				strTitle = mCurrentWebView.getTitle();
				
				// 浏览器 WebView 对象转成 View 对象
				View vBrowser = mCurrentWebView;
				
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
	 * 显示自定义视图
	 * @param view
	 * @param callback
	 */
	private void showCustomView(View view, WebChromeClient.CustomViewCallback callback)
	{
		try
		{
			mCurrentWebView.setVisibility(View.GONE);
			
			// if a view already exists then immediately terminate the new one
			if (mCustomView != null)
			{
				callback.onCustomViewHidden();
				
				// 请求焦点
				mPreviousButton.requestFocus();
				return;
			}

			mCustomViewContainer.addView(view);
			mCustomView = view;
			mCustomViewCallback = callback;
			mCustomViewContainer.setVisibility(View.VISIBLE);
			
			// 显示自定义视图
			m_bShowCustomView = true;
			
			// 请求焦点
			mPreviousButton.requestFocus();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 隐藏自定义视图
	 */
	private void hideCustomView()
	{
		try
		{
			if (mCustomView == null)
			{
				// 请求焦点
				mPreviousButton.requestFocus();
				return;
			}
			
			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);
			
			// Remove the custom view from its container.
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();
			
			mCurrentWebView.setVisibility(View.VISIBLE);
			
			// 不显示自定义视图
			m_bShowCustomView = false;
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 初始化输入网页的 UI 及打开网页的事件
	 */
	private void initBrowserLoadUrlUi()
	{
		try
		{
			// 设置网页视图客户端
			mCurrentWebView.setWebViewClient(new CustomWebViewClient(this));
			mCurrentWebView.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					hideKeyboard();

					return mGestureDetector.onTouchEvent(event);
				}
			});
			
			mCurrentWebView.setWebChromeClient(new WebChromeClient()
			{
				@Override
				public Bitmap getDefaultVideoPoster()
				{
					if (mDefaultVideoPoster == null)
					{
						mDefaultVideoPoster = BitmapFactory.decodeResource(m_context.getResources(), R.drawable.default_video_poster);
					}

					return mDefaultVideoPoster;
				}

				@Override
				public View getVideoLoadingProgressView()
				{
					if (mVideoProgressView == null)
					{
						LayoutInflater inflater = LayoutInflater.from(m_context);
						mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
					}

					return mVideoProgressView;
				}

				public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
				{
					showCustomView(view, callback);
				}

				@Override
				public void onHideCustomView()
				{
					hideCustomView();
				}

				@Override
				public void onProgressChanged(WebView view, int newProgress)
				{
					((CustomWebView)view).setProgress(newProgress);
					mProgressBar.setProgress(mCurrentWebView.getProgress());
				}
				
				@Override
				public void onRequestFocus(WebView view)
				{
					// 请求焦点
					theApp.SLog(TAG, "onRequestFocus");
					mPreviousButton.requestFocus();
				}
			});
		}
		catch (Exception e)
		{
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
					
					if (null != mCurrentWebView)
					{
						// 浏览器加载当前点中标签的网址
						hideAllChild();
						mCurrentWebView.stopLoading();

						// 加载网页
						mCurrentWebView.loadUrl(value.getStrUrl());
						mCurrentWebView.setVisibility(View.VISIBLE);
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
					
					if (null != mCurrentWebView)
					{
						// 浏览器加载当前点中标签的网址
						hideAllChild();
						mCurrentWebView.stopLoading();

						// 加载网页
						mCurrentWebView.loadUrl(value.getStrUrl());
						mCurrentWebView.setVisibility(View.VISIBLE);
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
						
						if (null != mCurrentWebView)
						{
							// 浏览器加载当前点中标签的网址
							hideAllChild();
							mCurrentWebView.stopLoading();

							// 加载网页
							mCurrentWebView.loadUrl(value.getStrUrl());
							mCurrentWebView.setVisibility(View.VISIBLE);
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
	 * Update the UI: Url edit text, previous/next button state,...
	 */
	private void updateUI()
	{
		mUrlEditText.removeTextChangedListener(mUrlTextWatcher);
		mUrlEditText.setText(mCurrentWebView.getUrl());
		mUrlEditText.addTextChangedListener(mUrlTextWatcher);

		mPreviousButton.setEnabled(mCurrentWebView.canGoBack());
		
		mProgressBar.setProgress(mCurrentWebView.getProgress());

		updateGoButton();
	}

	public void onPageFinished(String url)
	{
		updateUI();

		WebIconDatabase.getInstance().retainIconForPageUrl(mCurrentWebView.getUrl());
	}

	public void onPageStarted(String url)
	{
		mUrlEditText.removeTextChangedListener(mUrlTextWatcher);
		mUrlEditText.setText(url);
		mUrlEditText.addTextChangedListener(mUrlTextWatcher);

		mPreviousButton.setEnabled(false);

		updateGoButton();
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

	/**
	 * Gesture listener implementation.
	 */
	private class GestureListener extends GestureDetector.SimpleOnGestureListener
	{
		@Override
		public boolean onDoubleTap(MotionEvent e)
		{
			mCurrentWebView.zoomIn();
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
	
	/**
	 * Adapter for suggestions.
	 */
	public class UrlSuggestionCursorAdapter extends SimpleCursorAdapter
	{
		public static final String URL_SUGGESTION_ID = "_id";
		public static final String URL_SUGGESTION_TITLE = "URL_SUGGESTION_TITLE";
		public static final String URL_SUGGESTION_URL = "URL_SUGGESTION_URL";
		public static final String URL_SUGGESTION_TYPE = "URL_SUGGESTION_TYPE";

		/**
		 * Constructor
		 * @param context The context.
		 * @param layout The layout.
		 * @param c The Cursor.
		 * @param from Input array.
		 * @param to Output array.
		 */
		public UrlSuggestionCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
		{
			super(context, layout, c, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View superView = super.getView(position, convertView, parent);

			return superView;
		}
	}
	
	/**
	 * 自定义网页视图客户端类
	 */
	private class CustomWebViewClient extends WebViewClient
	{
		private BrowserLayout m_layBrowser = null;

		/**
		 * 构造函数
		 * @param layBrowser
		 */
		public CustomWebViewClient(BrowserLayout layBrowser)
		{
			super();
			
			m_layBrowser = layBrowser;
		}
		
		@Override
		public void onPageFinished(WebView view, String url)
		{
			((CustomWebView)view).notifyPageFinished();
			
			m_layBrowser.onPageFinished(url);

			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			((CustomWebView)view).notifyPageStarted();
			
			m_layBrowser.onPageStarted(url);

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error)
		{
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			// 在本对象内打开网页
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view, final HttpAuthHandler handler, final String host, final String realm)
		{
			String username = null;
			String password = null;

			boolean reuseHttpAuthUsernamePassword = handler.useHttpAuthUsernamePassword();

			if (reuseHttpAuthUsernamePassword && view != null)
			{
				String[] credentials = view.getHttpAuthUsernamePassword(host, realm);
				
				if (credentials != null && credentials.length == 2)
				{
					username = credentials[0];
					password = credentials[1];
				}
			}

			if (username != null && password != null)
			{
				handler.proceed(username, password);
			}
		}
	}
}
