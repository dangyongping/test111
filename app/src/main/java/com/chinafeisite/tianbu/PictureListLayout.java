package com.chinafeisite.tianbu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 图片布局类
 */
public class PictureListLayout extends RelativeLayout implements OnItemClickListener
{
	private final String TAG = "PictureListLayout";

	// 时间
	private static long ms_lTime = 0;
	
	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 列表
	private ListView m_lv = null;
	private List<String> m_list = null;
	
	// 适配器
//	private ListItemAdapter m_adapter = null;
	private ListViewAdapter m_adapter = null;
	
	// 索引
	private int m_nIndex = -1;
	
	// 图像视图
	private ImageView m_imgView = null;
	
	// 播放线程
	private PlayThread m_playThread = null;

	// 图像 URL
//	private String[] m_strImageUrls = null;
	
	// 图像布局
	private RelativeLayout m_layImage = null;
	
	// 是否正在播放
	private static boolean ms_bPlaying = false;
	
	// 显示图像参数
//	private DisplayImageOptions m_options = null;
	
	// 是否已退出
	private static boolean ms_bExit = false;
	public static void setExit(final boolean b) { ms_bExit = b; }

	// 旧的位图
	private List<Bitmap> m_listBmpOld = new ArrayList<Bitmap>();
	
	// 路径
	private String m_strPath = "";
	private List<String> m_listPath = null;
	public void setListPath(List<String> list) { m_listPath = list; }
	
	// 图像加载器
//	private ImageLoader m_imageLoader = null;
//	public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }
	
	// 消息
	private static final int MSG_UPDATE_LIST = 0; // 刷新列表
	private static final int MSG_UPDATE_VIEW = 1; // 刷新视图
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public PictureListLayout(Context context)
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
	public PictureListLayout(Context context, AttributeSet attrs)
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
	public PictureListLayout(Context context, AttributeSet attrs, int defStyle)
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

			// 设置选择项
			m_adapter.setSelectItem(m_nIndex);
			m_adapter.notifyDataSetChanged();
//			Log.d(TAG, String.format("m_adapter.setSelectItem(%d): ", m_nIndexMusic));
			
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_picture_list, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));

			// 列表视图
			m_lv = (ListView)m_view.findViewById(R.id.lvPicture);
			m_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			m_lv.setOnItemClickListener(this);
			m_lv.setItemsCanFocus(true);
			
			// 图像视图
			m_imgView = (ImageView)m_view.findViewById(R.id.imageView);
			
			// 图像布局
			m_layImage = (RelativeLayout)m_view.findViewById(R.id.layImage);
			m_layImage.setVisibility(View.INVISIBLE);
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 回收
	 */
	private void recycle()
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
//			System.gc();
			System.runFinalization();
//			Runtime.getRuntime().gc();
		}
		catch (Exception e)
		{
			Log.d(TAG, "recycle: %s", e);
		}
	}
	
	/**
	 * 显示
	 */
	private void show()
	{
		// 隐藏列表视图
		m_lv.setVisibility(View.GONE);

		// 显示图像布局
		m_layImage.setVisibility(View.VISIBLE);
		
		// 文件
		File file = new File(m_strPath);

		// 检测文件
		if (!file.exists() || !file.isFile()) return;
		
		// 回收
		recycle();
		
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
		m_imgView.setImageBitmap(bmp);
	}
	
	/**
	 * 初始化列表视图
	 */
	public void initListView()
	{
		try
		{
			// 列表
			m_list = new ArrayList<String>();

			// 数量
			int nSize = m_listPath.size();
			
			// 图像 URL
//			m_strImageUrls = new String[nSize];
			
			for (int i = 0; i < nSize; i++)
			{
				// 文件名
				String strFileName = m_listPath.get(i);
				
				// 图像 URL
//				m_strImageUrls[i] = "file://" + strFileName;

				// 最后一个反斜杠的位置
				int nPos = strFileName.lastIndexOf("/") + 1;
				if (nPos <= 0 || nPos >= strFileName.length()) continue;
				
				// 截取文件名
				strFileName = strFileName.substring(nPos);
				
				// 添加列表项
				m_list.add(strFileName);
			}
			
			// 索引
			if (m_nIndex == -1) m_nIndex = 0;
			
			// 路径
			m_strPath = m_listPath.get(m_nIndex);

			// 显示图像参数
/*			m_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.ALPHA_8)
				.build();
*/			
			// 适配器
//			m_adapter = new ListItemAdapter(m_context);
			m_adapter = new ListViewAdapter(m_context, m_list);
			m_adapter.setSelectItem(m_nIndex);
//			m_adapter.setOptions(m_options);
//			m_adapter.setImageUrls(m_strImageUrls);
//			m_adapter.setImageLoader(m_imageLoader);
			
			// 设置适配器
			if (m_lv != null) m_lv.setAdapter(m_adapter);
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
			ms_bPlaying = false;
			
			// 显示列表视图
			m_lv.setVisibility(View.VISIBLE);

			// 隐藏图像布局
			m_layImage.setVisibility(View.INVISIBLE);
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
			
			// 索引
			m_nIndex--;
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;

			// 刷新列表
			m_handler.sendEmptyMessage(MSG_UPDATE_LIST);

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
			
			// 索引
			m_nIndex++;
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;

			// 刷新列表
			m_handler.sendEmptyMessage(MSG_UPDATE_LIST);

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
			
			// 索引
			m_nIndex++;
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;

			// 刷新列表
			m_handler.sendEmptyMessage(MSG_UPDATE_LIST);

			// 路径
			m_strPath = m_listPath.get(m_nIndex);
		}
		catch (Exception e)
		{
		}
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
			// 刷新列表
			case MSG_UPDATE_LIST:

				try
				{
					// 设置选择项
					m_adapter.setSelectItem(m_nIndex);
					m_adapter.notifyDataSetChanged();
					m_lv.setSelection(m_nIndex);
//					Log.d(TAG, String.format("m_adapter.setSelectItem(%d): ", ms_nIndex));
				}
				catch (Exception e)
				{
				}
				break;
				
			// 更新视图
			case MSG_UPDATE_VIEW:
				
				try
				{
					if (m_imgView != null) m_imgView.setVisibility(View.INVISIBLE);
					if (m_imgView != null) m_imgView.setVisibility(View.VISIBLE);
					if (m_imgView != null) m_imgView.invalidate();
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
						
						// 时间等于或超过 1 秒且间隔 1 秒
						if ((ms_lTime >= 1 * 1000) &&
							(ms_lTime %  1 * 1000 == 0))
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
}
