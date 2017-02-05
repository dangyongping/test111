package com.chinafeisite.tianbu;

//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.widget.VideoView;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 视频布局类
 */
public class VideoLayout extends RelativeLayout implements OnItemClickListener
{
	private final String TAG = "VideoLayout";

	// 显示时间
	private static final int SHOW_TIME = 5000;
	
	// 显示时间
	private static int ms_nShowTime = 0;
	public static void setShowTime(final int n) { ms_nShowTime = Math.abs(n); }

	// 索引
	private int m_nIndex = -1;
	
	// 当前播放位置
//	private long m_lCurPos = -1;
	private int m_lCurPos = -1;
	
	// 是否正处于播放界面
	private boolean m_bPlay = false;
	
	// 是否已暂停
	private boolean m_bPause = false;
	
	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 列表
	private ListView m_lv = null;
	
	// 适配器
	private ListItemAdapter m_adapter = null;
	
	// 图像 URL
	private String[] m_strImageUrls = null;
	
	// 显示图像参数
	private DisplayImageOptions m_options = null;
	
	// 视频布局
	private RelativeLayout m_layVideo = null;
	
	// 视频视图 vtm
//	private VideoView m_videoView = null;
//	public VideoView getVideoView() { return m_videoView; }
	
	// 视频视图
	private VideoViewEx m_videoView = null;
	public VideoViewEx getVideoView() { return m_videoView; }
		
	// 路径
	private String m_strPath = "";
	private List<String> m_listPath = null;
	public void setListPath(List<String> list) { m_listPath = list; }

	// 图像加载器
	private ImageLoader m_imageLoader = null;
	public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }

	// 消息
	private static final int MSG_UPDATE_LIST = 0; // 刷新列表
	
	/**
	 * 构造函数
	 * @param context
	 */
	public VideoLayout(Context context)
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
	public VideoLayout(Context context, AttributeSet attrs)
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
	public VideoLayout(Context context, AttributeSet attrs, int defStyle)
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
			TextView textview ;
			
			// 索引
			m_nIndex = position;
			textview = (TextView) view.findViewById(R.id.txtName);
			textview.setTextColor(Color.WHITE);			

			// 设置选择项
			m_adapter.setSelectItem(m_nIndex);
			m_adapter.notifyDataSetChanged();
//			Log.d(TAG, String.format("m_adapter.setSelectItem(%d): ", m_nIndexMusic));
			
			// 路径
			m_strPath = m_listPath.get(m_nIndex);


			try
			{
				MusicLayout.stopMusic();
			}
			catch (Exception e)
			{
			}	
			
			// 播放
			play();
		}
		catch (Exception e)
		{
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
//		float x = 0;
//		float y = 0;
		int nAction = 0;
		
		try
		{
			// 在视图中的坐标
//			x = event.getX();
//			y = event.getY();
//			Log.d(TAG, String.format("Touch - X: %d, Y: %d", (int)x, (int)y));
			
			// 触摸动作
			nAction = event.getAction() & MotionEvent.ACTION_MASK;
			
			// 未处于播放界面
			if (!m_bPlay) return true;
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
				
				// 显示进度条
				showProgress();
				break;
			}
		}
		catch (Exception e)
		{
		}
		
		// 此处必须返回 true, 否则无法完全响应所有的 MotionEvent
		return true;
//		return super.onTouchEvent(event);
	}
	
	/**
	 * 播放完成监听器
	 */
	private io.vov.vitamio.MediaPlayer.OnCompletionListener m_onCompletionListener =
			new io.vov.vitamio.MediaPlayer.OnCompletionListener()
	{
		@Override
		public void onCompletion(io.vov.vitamio.MediaPlayer mp)
		{
			// 下一曲
			next();
		}
	};
	/**
	 * 播放完成监听器
	 */
	private MediaPlayer.OnCompletionListener my_onCompletionListener =
			new MediaPlayer.OnCompletionListener()
	{
		@Override
		public void onCompletion(MediaPlayer mp)
		{
			// 下一曲
			next();
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_video, null);
			
			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));

			// 列表视图
			m_lv = (ListView)m_view.findViewById(R.id.lvVideo);
			m_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			m_lv.setOnItemClickListener(this);
			m_lv.setItemsCanFocus(true);
			
			// 视频布局
			m_layVideo = (RelativeLayout)m_view.findViewById(R.id.layVideo);
//			m_layVideo.setVisibility(View.GONE);
			
			if (theApp.VITAMIO)
			{
				// 视频视图
			/*	m_videoView = (VideoView)m_view.findViewById(R.id.videoView);
				m_videoView.setOnCompletionListener(m_onCompletionListener);
				m_videoView.setVideoChroma(io.vov.vitamio.MediaPlayer.VIDEOCHROMA_RGB565);
				m_videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);
	//			m_videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN, 0);
				//设置高质量
				m_videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);			
	//			m_videoView.setMediaController(new io.vov.vitamio.widget.MediaController(m_context));*/
			}
			else
			{
				// 视频视图
				m_videoView = (VideoViewEx)m_view.findViewById(R.id.videoView);
			/*	m_videoView = new VideoViewEx(m_context);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);			
                m_videoView.setLayoutParams(params);
                m_layVideo.addView(m_videoView);*/		
				m_videoView.setOnCompletionListener(my_onCompletionListener);
//				m_videoView.setVideoChroma(io.vov.vitamio.MediaPlayer.VIDEOCHROMA_RGB565);
//				m_videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);
//				m_videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN, 0);
				//设置高质量
//				m_videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);			
//				m_videoView.setMediaController(new io.vov.vitamio.widget.MediaController(m_context));				
			}
			// 多媒体标记
			theApp.setVideoMark(theApp.MARK_LIST);
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 显示进度条
	 */
	private void showProgress()
	{
		try
		{
			// 未显示
			if (!FloatVideoService.isShow())
			{
				try
				{
					// 启动显示线程
					ms_nShowTime = 0;
					new ShowThread().start();
				}
				catch (Exception e)
				{
				}
				
				// 启动视频悬浮窗
				FloatVideoLayout.setVideoLayout(this);
				Intent intentVideo = new Intent(m_context, FloatVideoService.class);
				m_context.startService(intentVideo);
			}
			// 已显示
			else
			{
				// 移除视频悬浮窗
				FloatManager.removeVideo(m_context);

				// 停止视频悬浮窗
				Intent intentVideo = new Intent(m_context, FloatVideoService.class);
				m_context.stopService(intentVideo);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 初始化列表视图
	 */
	public void initListView()
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
			
			// 适配器
			m_adapter = new ListItemAdapter(m_context);
			m_adapter.setSelectItem(m_nIndex);
			m_adapter.setOptions(m_options);
			m_adapter.setImageUrls(m_strImageUrls);
			m_adapter.setImageLoader(m_imageLoader);
			
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
			// 多媒体标记
			theApp.setVideoMark(theApp.MARK_PLAY);
			
			// 隐藏列表视图
			m_lv.setVisibility(View.GONE);
			
			//显示Toast
			theApp.setToast(true);
			
			// 设置视频布局
/*			int nW = getWidth();
			int nH = getHeight();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(nW, nH);
			m_layVideo.setLayoutParams(params);*/
			m_layVideo.setVisibility(View.VISIBLE);
			
			if (m_videoView != null && m_videoView.isPlaying())
			{
				// 停止
				if (!m_bPause) m_videoView.stopPlayback();
			}

			// 文件
			File file = new File(m_strPath);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;

			// 正处于播放界面
			m_bPlay = true;
			
			// 检测当前播放位置
			if (m_lCurPos > 0)
			{
				// 播放
				m_bPause = false;
				if (m_videoView != null) m_videoView.seekTo(m_lCurPos);
				if (m_videoView != null) m_videoView.start();
			}
			else
			{
				// 播放
				m_bPause = false;
				if (m_videoView != null) m_videoView.setVideoPath(m_strPath);
				if (m_videoView != null) m_videoView.start();
			}

			try
			{
				// 启动显示线程
				ms_nShowTime = 0;
				new ShowThread().start();
			}
			catch (Exception e)
			{
			}
			
			// 设置播放
			if (FloatManager.getLayFloatVideo() != null) FloatManager.getLayFloatVideo().setPlay();
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
			if (m_videoView != null)
			{
				// 暂停
				m_bPause = true;
				m_videoView.pause();

				// 当前播放位置
				m_lCurPos = m_videoView.getCurrentPosition();
				Log.d(TAG, String.format("pause: %d", m_lCurPos));
				
				try
				{
					// 未显示
					if (!FloatVideoService.isShow())
					{
						// 启动视频悬浮窗
						FloatVideoLayout.setVideoLayout(this);
						Intent intentVideo = new Intent(m_context, FloatVideoService.class);
						m_context.startService(intentVideo);
						
						// 设置播放
						if (FloatManager.getLayFloatVideo() != null) FloatManager.getLayFloatVideo().setPlay();
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
	
	/**
	 * 停止
	 */
	public void stop()
	{
		try
		{
			// 未处于播放界面
			m_bPlay = false;
			
			// 多媒体标记
//			theApp.setVideoMark(theApp.MARK_LIST);
			theApp.setVideoMark(theApp.MARK_NONE);
			
			// 显示列表视图
			m_lv.setVisibility(View.VISIBLE);
			
			// 设置视频布局
/*			int nW = 1;
			int nH = 1;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(nW, nH);
			m_layVideo.setLayoutParams(params);*/
			m_layVideo.setVisibility(View.GONE);
			
			if (m_videoView != null)
			{
				try
				{
					// 设置空 URI
//					m_videoView.setNullUri();
					m_videoView.setVideoPath(null);	
					m_videoView.setVideoURI(null);
					Log.d(TAG, "setVideoPath");
									
				}
				catch (Exception e)
				{
				}
				
				// 停止
				m_strPath = null;
				m_lCurPos = -1;
				m_bPause = false;
				m_videoView.stopPlayback();
				Log.d(TAG, "stop");

				try
				{
					// 已显示
					if (FloatVideoService.isShow())
					{
						// 移除视频悬浮窗
						FloatManager.removeVideo(m_context);

						// 停止视频悬浮窗
						Intent intentVideo = new Intent(m_context, FloatVideoService.class);
						m_context.stopService(intentVideo);
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
			
			// 播放
			play();
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
			
			// 播放
			play();
			
			// 移除视频悬浮窗
			FloatManager.removeVideo(m_context);

			// 停止视频悬浮窗
			Intent intentVideo = new Intent(m_context, FloatVideoService.class);
			m_context.stopService(intentVideo);
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
		try
		{
			if (!m_bPlay) return m_bPlay;
			if (m_videoView != null) return m_videoView.isPlaying();
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
			// 刷新列表
			case MSG_UPDATE_LIST:

				try
				{
					// 设置选择项
					m_adapter.setSelectItem(m_nIndex);
					m_adapter.notifyDataSetChanged();
					m_lv.setSelection(m_nIndex);
//					Log.d(TAG, String.format("m_adapter.setSelectItem(%d): ", m_nIndex));
				}
				catch (Exception e)
				{
				}
				break;
			}
		}
	};
	
	/**
	 * 显示线程
	 */
	private class ShowThread extends Thread
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
					
					// 显示时间
					ms_nShowTime++;

					// 显示时间等于或超过时间间隔
					if ((ms_nShowTime >= SHOW_TIME) &&
						(ms_nShowTime %  SHOW_TIME == 0))
					{
						try
						{
							// 正在播放
							if (m_videoView.isPlaying())
							{
								// 已显示
								if (FloatVideoService.isShow())
								{
									// 移除视频悬浮窗
									FloatManager.removeVideo(m_context);
	
									// 停止视频悬浮窗
									Intent intentVideo = new Intent(m_context, FloatVideoService.class);
									m_context.stopService(intentVideo);
								}
							}
						}
						catch (Exception e)
						{
						}
						
						Log.d(TAG, "ShowThread return");
						return;
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
