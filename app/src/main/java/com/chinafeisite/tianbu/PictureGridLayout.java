package com.chinafeisite.tianbu;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 图片布局类
 */
public class PictureGridLayout extends RelativeLayout implements OnItemClickListener, OnScrollListener
{
	private final String TAG = "PictureLayout";

	// 索引
	private int m_nIndex = -1;
	
	// 时间
	private static long ms_lTime = 0;
	
	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 网格视图
	private GridView m_gridView = null;
	
	// 图像视图
//	private ImageView m_imgView = null;
	
	// 图像布局
//	private RelativeLayout m_layImage = null;
	
	// 视图页
	private ViewPager m_viewPager = null;
	
	// 视图页布局
	private RelativeLayout m_layPager = null;
	
	// 播放线程
	private PlayThread m_playThread = null;
	
	// 是否正在显示
	private boolean m_bShowing = false;
	
	// 是否正在播放
	private static boolean ms_bPlaying = false;
	
	// 是否已退出
	private static boolean ms_bExit = false;
	public static void setExit(final boolean b) { ms_bExit = b; }

	// 旧的位图
//	private List<Bitmap> m_listBmpOld = new ArrayList<Bitmap>();
	
	// 路径
	private String m_strPath = "";
	private List<String> m_listPath = null;
	public void setListPath(List<String> list) { m_listPath = list; }

	// 图像 URL
	private String[] m_strImageUrls = null;

	// 图像适配器
	private ImageAdapter m_adapter = null;
	private ImagePagerAdapter m_adapterPager = null;
	
	// 显示图像参数
	private DisplayImageOptions m_options = null;
	private DisplayImageOptions m_optionsPager = null;
	
	// 图像加载器
	private ImageLoader m_imageLoader = null;
	public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }
	
	// 消息
	private static final int MSG_UPDATE_VIEW = 0; // 刷新视图
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public PictureGridLayout(Context context)
	{
		super(context);
		
		// 初始化
		init(context);
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public PictureGridLayout(Context context, AttributeSet attrs)
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
	public PictureGridLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		try
		{
			// 索引
			m_nIndex = position;

			// 路径
			m_strPath = m_listPath.get(m_nIndex);

			// 显示
			show();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
/*		String strLog = "";
		strLog += "view: " + view.toString() + "\r\n";
		strLog += "firstVisibleItem: " + firstVisibleItem + "\r\n";
		strLog += "visibleItemCount: " + visibleItemCount + "\r\n";
		strLog += "totalItemCount: " + totalItemCount + "\r\n";
		strLog += "getScrollX: " + view.getScrollX() + "\r\n";
		strLog += "getScrollY: " + view.getScrollY() + "\r\n";
		Log.d(TAG, strLog);*/
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
/*		String strLog = "";
		strLog += "view: " + view.toString() + "\r\n";
		strLog += "scrollState: " + scrollState;
		Log.d(TAG, strLog);*/
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context)
	{
		try
		{
			// 未播放
			ms_bPlaying = false;
			
			// Activity 的 Context
			m_context = context;

			// 从布局文件中加载视图
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_picture, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			// 网格视图
			m_gridView = (GridView)m_view.findViewById(R.id.gridView);
			m_gridView.setOnItemClickListener(this);
			m_gridView.setOnScrollListener(this);
			
			// 图像视图
//			m_imgView = (ImageView)m_view.findViewById(R.id.imageView);
			
			// 图像布局
//			m_layImage = (RelativeLayout)m_view.findViewById(R.id.layImage);
//			m_layImage.setVisibility(View.INVISIBLE);

			// 视图页
			m_viewPager = (ViewPager)m_view.findViewById(R.id.viewPager);
			
			// 视图页布局
			m_layPager = (RelativeLayout)m_view.findViewById(R.id.layPager);
			m_layPager.setVisibility(View.INVISIBLE);
			
			// 多媒体标记
			theApp.setPictuMark(theApp.MARK_LIST);
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 回收
	 */
/*	private void recycle()
	{
		try
		{
			// 旧的位图数量
			int nSize = m_listBmpOld.size();
			if (nSize < 3) return;
			
			// 回收
			Bitmap bmp = m_listBmpOld.get(0);
			m_listBmpOld.remove(bmp);
			bmp.recycle();
			bmp = null;
			
			// 垃圾回收
			System.gc();
			System.runFinalization();
			Runtime.getRuntime().gc();
		}
		catch (Exception e)
		{
			Log.d(TAG, "recycle: %s", e);
		}
	}
*/	
	/**
	 * 显示
	 */
	private void show()
	{
		try
		{
			// 正在显示
			m_bShowing = true;
			
			// 多媒体标记
			theApp.setPictuMark(theApp.MARK_PLAY);
			
			// 隐藏网格视图
			m_gridView.setVisibility(View.INVISIBLE);

			// 显示图像布局
//			m_layImage.setVisibility(View.VISIBLE);
			
			// 显示视图页布局
			m_layPager.setVisibility(View.VISIBLE);

			// 文件
			File file = new File(m_strPath);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;
			
			// 设置当前项
			m_viewPager.setCurrentItem(m_nIndex);

			// 回收
	/*		recycle();
			
			// 位图
			Bitmap bmp = theApp.decodeBitmap(m_strPath);
			m_listBmpOld.add(bmp);
			
			// 宽度和高度
			int nW = getWidth();
			int nH = getHeight();
			
			// 设置图像布局
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(nW, nH);
			m_imgView.setLayoutParams(params);
			
			// 图像视图
			m_imgView.setImageBitmap(bmp);*/
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	
	/**
	 * 初始化网格视图
	 */
	public void initGridView()
	{
		try
		{
			// 数量
			int nSize = m_listPath.size();
			
			// 图像 URL
			m_strImageUrls = new String[nSize];
			
			for (int i = 0; i < nSize; i++)
			{
				// 文件名
				String strFileName = m_listPath.get(i);
				
				// 图像 URL
				m_strImageUrls[i] = "file://" + strFileName;
			}
			
			// 列数
/*			int nNumColumns = (nSize + 1) / 2;
			m_gridView.setNumColumns(nNumColumns);
			
			// 宽度和高度
			int nP = 8;
			int nH = 552 - nP * 2;
			int nW = nNumColumns * (260 + nP * 2);
			
			// 设置布局参数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(nW, nH);
			params.setMargins(nP, nP, 0, 0);
			m_gridView.setLayoutParams(params);
*/			
			// 索引
			if (m_nIndex == -1) m_nIndex = 0;
			
			// 路径
			m_strPath = m_listPath.get(m_nIndex);

			// 显示图像参数
			m_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.ALPHA_8)
				.build();

			// 显示图像参数
			m_optionsPager = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.ALPHA_8)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.build();
			
			// 适配器
			m_adapter = new ImageAdapter(m_context);
			m_adapter.setOptions(m_options);
			m_adapter.setImageUrls(m_strImageUrls);
			m_adapter.setImageLoader(m_imageLoader);
			
			// 适配器
			m_adapterPager = new ImagePagerAdapter(m_context);
			m_adapterPager.setOptions(m_optionsPager);
			m_adapterPager.setImageUrls(m_strImageUrls);
			m_adapterPager.setImageLoader(m_imageLoader);
			
			// 设置适配器
			if (m_gridView != null) m_gridView.setAdapter(m_adapter);
			if (m_viewPager != null) m_viewPager.setAdapter(m_adapterPager);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 播放
	 */
	public void play()
	{
		try
		{
			// 显示
			show();

			// 正在播放
			ms_bPlaying = true;
			
			if (m_playThread == null)
			{
				// 启动播放线程
				m_playThread = new PlayThread();
				m_playThread.start();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 暂停
	 */
	public void pause()
	{
		try
		{
			// 暂停
			ms_lTime = 0;
			ms_bPlaying = false;
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 停止
	 */
	public void stop()
	{
		try
		{
			// 停止
			ms_lTime = 0;
			m_bShowing = false;
			ms_bPlaying = false;
			
			// 多媒体标记
//			theApp.setPictuMark(theApp.MARK_LIST);
			theApp.setPictuMark(theApp.MARK_NONE);

			// 显示网格视图
			m_gridView.setVisibility(View.VISIBLE);

			// 隐藏图像布局
//			m_layImage.setVisibility(View.INVISIBLE);
			
			// 隐藏视图页布局
			m_layPager.setVisibility(View.INVISIBLE);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 上一曲
	 */
	public void prev()
	{
		try
		{
			// 数量
			int nSize = m_listPath.size();
			if (nSize < 1) return;
			
			// 获取当前项
			m_nIndex = m_viewPager.getCurrentItem();
			
			// 索引
			m_nIndex--;
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;

			// 路径
			m_strPath = m_listPath.get(m_nIndex);

			// 显示
			show();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 下一曲
	 */
	public void next()
	{
		try
		{
			// 数量
			int nSize = m_listPath.size();
			if (nSize < 1) return;
			
			// 获取当前项
			m_nIndex = m_viewPager.getCurrentItem();
			
			// 索引
			m_nIndex++;
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;

			// 路径
			m_strPath = m_listPath.get(m_nIndex);

			// 显示
			show();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 下一曲
	 * @param bOnlyNext
	 */
	public void next(final boolean bOnlyNext)
	{
		try
		{
			// 数量
			int nSize = m_listPath.size();
			if (nSize < 1) return;
			
			// 获取当前项
			m_nIndex = m_viewPager.getCurrentItem();
			
			// 索引
			m_nIndex++;
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;

			// 路径
			m_strPath = m_listPath.get(m_nIndex);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 后退
	 */
	public void back()
	{
		try
		{
			// 停止
			stop();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 是否正在播放
	 * @return
	 */
	public boolean isPlaying()
	{
		return (m_bShowing || ms_bPlaying);
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
			// 更新视图
			case MSG_UPDATE_VIEW:
				
				try
				{
					// 显示
					show();
					
//					if (m_imgView != null) m_imgView.setVisibility(View.INVISIBLE);
//					if (m_imgView != null) m_imgView.setVisibility(View.VISIBLE);
//					if (m_imgView != null) m_imgView.invalidate();
//					Log.d(TAG, "MSG_UPDATE_VIEW");
				}
				catch (Exception e)
				{
				}
				break;
			}
		}
	};
	
	/**
	 * 播放线程
	 */
	class PlayThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			
			// 未退出
			while (!ms_bExit)
			{
				try
				{
					// 休眠 1 毫秒
					Thread.sleep(1);

					// 正在播放
					if (ms_bPlaying)
					{
						// 时间
						ms_lTime++;
						
						// 时间等于或超过 5 秒且间隔 5 秒
						if ((ms_lTime >= 5 * 1000) &&
							(ms_lTime %  5 * 1000 == 0))
						{
							// 时间
							ms_lTime = 0;
							
							// 下一曲
							next(true);
							
							// 更新视图
							m_handler.sendEmptyMessage(MSG_UPDATE_VIEW);
						}
					}
				}
				catch (Exception e)
				{
					return;
				}
			}
		}
	}
	
	/**
	 * 图像适配器类
	 */
	private class ImageAdapter extends BaseAdapter
	{
		// 加载位置
		private int m_nLoadPos    =  5;
		private int m_nLoadPosOld = -1;
		
		// 布局泵
		private LayoutInflater m_layInflater = null;
		
		// 显示图像参数
		private DisplayImageOptions m_options = null;
		public void setOptions(DisplayImageOptions options) { m_options = options; }

		// 图像 URL
		private String[] m_strImageUrls = null;
		public void setImageUrls(String[] strImageUrls) { m_strImageUrls = strImageUrls; }
		
		// 图像加载器
		private ImageLoader m_imageLoader = null;
		public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }
		
		/**
		 * 构造函数
		 * @param context
		 */
		public ImageAdapter(Context context)
		{
			try
			{
				// 布局泵
				m_layInflater = LayoutInflater.from(context);
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		public int getCount()
		{
			try
			{
				return m_strImageUrls.length;
			}
			catch (Exception e)
			{
			}
			
			return 0;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			try
			{
				final ImageView imageView;
				
				if (convertView != null)
				{
					// 图像视图
					imageView = (ImageView)convertView;
				}
				else
				{
					// 图像视图
					imageView = (ImageView)m_layInflater.inflate(R.layout.item_grid_image, parent, false);
				}

				if (m_imageLoader != null)
				{
					// 数量
					int nSize = m_strImageUrls.length;
					int nSize2 = (nSize + 1) / 2;
					
					// 检测加载位置
					if ((m_nLoadPosOld != position) &&
						((position <= m_nLoadPos) ||
						(nSize2 <= position && position <= nSize + m_nLoadPos)))
					{
						// 加载位置
						m_nLoadPos++;
						m_nLoadPosOld = position;

						// 显示图像
						m_imageLoader.displayImage(m_strImageUrls[position], imageView, m_options);
					}
				}

				return imageView;
			}
			catch (Exception e)
			{
				Log.d(TAG, String.format("getView: %s", e));
			}
			
			return null;
		}
	}

	/**
	 * 图像页面适配器
	 */
	private class ImagePagerAdapter extends PagerAdapter
	{
		// Activity 的 Context
		private Context m_context = null;
		
		// 布局泵
		private LayoutInflater m_layInflater = null;
		
		// 显示图像参数
		private DisplayImageOptions m_options = null;
		public void setOptions(DisplayImageOptions options) { m_options = options; }

		// 图像 URL
		private String[] m_strImageUrls = null;
		public void setImageUrls(String[] strImageUrls) { m_strImageUrls = strImageUrls; }
		
		// 图像加载器
		private ImageLoader m_imageLoader = null;
		public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }

		/**
		 * 构造函数
		 * @param context
		 */
		public ImagePagerAdapter(Context context)
		{
			try
			{
				// Activity 的 Context
				m_context = context;

				// 布局泵
				m_layInflater = LayoutInflater.from(context);
			}
			catch (Exception e)
			{
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public int getCount()
		{
			try
			{
				return m_strImageUrls.length;
			}
			catch (Exception e)
			{
			}

			return 0;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position)
		{
			// 布局
			View imageLayout = m_layInflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			
			// 图像视图
//			ImageView imageView = (ImageView)imageLayout.findViewById(R.id.imageView);
//			imageView.setScaleType(ScaleType.FIT_CENTER);
			PhotoView imageView = (PhotoView)imageLayout.findViewById(R.id.photoView);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			
			// 进度条
			final ProgressBar spinner = (ProgressBar)imageLayout.findViewById(R.id.proLoading);

			m_imageLoader.displayImage(m_strImageUrls[position], imageView, m_options, new SimpleImageLoadingListener()
			{
				@Override
				public void onLoadingStarted(String imageUri, View view)
				{
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
				{
					String strMsg = null;
					
					switch (failReason.getType())
					{
					case IO_ERROR:
						strMsg = "Input/Output error";
						break;
						
					case DECODING_ERROR:
						strMsg = "Image can't be decoded";
						break;
						
					case NETWORK_DENIED:
						strMsg = "Downloads are denied";
						break;
						
					case OUT_OF_MEMORY:
						strMsg = "Out Of Memory error";
						break;
						
					case UNKNOWN:
						strMsg = "Unknown error";
						break;
					}

					Toast.makeText(m_context, strMsg, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
				{
					spinner.setVisibility(View.GONE);
				}
			});

			view.addView(imageLayout, 0);
			
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader)
		{
		}

		@Override
		public Parcelable saveState()
		{
			return null;
		}
	}
}
