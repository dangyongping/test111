package com.chinafeisite.tianbu;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 视频悬浮窗服务类
 */
public class FloatVideoService extends Service
{
	// 需要权限 android.permission.SYSTEM_ALERT_WINDOW,
	// 并且需要在 AndroidManifest.xml 中添加如下内容:
	// <service android:name="com.chinafeisite.tianbu.FloatVideoService" />

	private final String TAG = "FloatVideoService";
	
	// 是否已经显示
	private static boolean ms_bShow = false;
	public static boolean isShow() { return ms_bShow; }

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		try
		{
			// 已经显示
			ms_bShow = true;
			
			// 创建视频悬浮窗
			FloatManager.createVideo(getApplicationContext());
		}
		catch (Exception e)
		{
		}
	}
	
	@Override
	public void onDestroy()
	{
		try
		{
			// 未显示
			ms_bShow = false;
			Log.d(TAG, "onDestroy");
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * 视频悬浮窗布局类
 */
class FloatVideoLayout extends LinearLayout implements OnClickListener, OnSeekBarChangeListener
{
	private final String TAG = "FloatVideoLayout";

	// 持续时间
	private long m_lDuration = -1;
	
	// 是否正在拖拽进度条
	private boolean m_bDragging = false;
	
	// 播放界面
	private Button m_btnPlay = null;
	private Button m_btnPause = null;
	private SeekBar m_seekBar = null;
	private TextView m_txtTotal = null;
	private TextView m_txtCurrent = null;
	
	// 视频布局
	private static VideoLayout ms_layVideo = null;
	public static void setVideoLayout(VideoLayout layout) { ms_layVideo = layout; }

	// 布局
	private LinearLayout m_layVideo = null;

	// 宽度和高度
	private int m_nViewWidth = 0;
	private int m_nViewHeight = 0;
	public int getViewWidth() { return m_nViewWidth; }
	public int getViewHeight() { return m_nViewHeight; }
	
	// 消息
	private static final int MSG_SHOW_PROG = 0; // 显示进度条
	
	/**
	 * 构造函数
	 * @param context
	 */
	public FloatVideoLayout(Context context)
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
	public FloatVideoLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 初始化
		init(context);
	}
	
	/**
	 * 销毁
	 */
	public void onDestroy()
	{
		try
		{
			// 移除消息
			m_handler.removeMessages(MSG_SHOW_PROG);
			Log.d(TAG, "onDestroy");
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void onClick(View view)
	{
		try
		{
			// 播放
			if (view.getId() == R.id.btnPlay)
			{
				// 显示时间
				VideoLayout.setShowTime(0);
				
				// 隐藏"播放"按钮
				m_btnPlay.setVisibility(View.INVISIBLE);
				
				// 显示"暂停"按钮
				m_btnPause.setVisibility(View.VISIBLE);

				// 播放
				if (ms_layVideo != null) ms_layVideo.play();
				
				// 显示进度条
				m_handler.sendEmptyMessage(MSG_SHOW_PROG);
			}
			
			// 暂停
			if (view.getId() == R.id.btnPause)
			{
				// 显示时间
				VideoLayout.setShowTime(0);
				
				// 显示"播放"按钮
				m_btnPlay.setVisibility(View.VISIBLE);
				
				// 隐藏"暂停"按钮
				m_btnPause.setVisibility(View.INVISIBLE);

				// 暂停
				if (ms_layVideo != null) ms_layVideo.pause();
				
				// 移除消息
				m_handler.removeMessages(MSG_SHOW_PROG);
			}
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
				break;
			}
		}
		catch (Exception e)
		{
		}
		
		return true;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if (!fromUser) return;

		try
		{
			// 视频视图
			if (ms_layVideo == null) return;
			if (ms_layVideo.getVideoView() == null) return;
			
			// 跳转
			int nPos = (int)(m_lDuration * progress / 1000);
			ms_layVideo.getVideoView().seekTo(nPos);

			// 当前时间
			String strTime = time2Str(nPos);
			m_txtCurrent.setText(strTime);
			
			// 正在播放
			if (ms_layVideo.getVideoView().isPlaying())
			{
				// 隐藏"播放"按钮
				m_btnPlay.setVisibility(View.INVISIBLE);
				
				// 显示"暂停"按钮
				m_btnPause.setVisibility(View.VISIBLE);
			}
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// 正在推拽
		m_bDragging = true;

		// 显示时间
		VideoLayout.setShowTime(0);
		
		// 移除消息
		m_handler.removeMessages(MSG_SHOW_PROG);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// 没有在推拽
		m_bDragging = false;
		
		// 移除消息
		m_handler.removeMessages(MSG_SHOW_PROG);
		
		// 延迟发送消息
		m_handler.sendEmptyMessageDelayed(MSG_SHOW_PROG, 1000);
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context)
	{
		try
		{
			// 从布局文件中加载视图
			View view = LayoutInflater.from(context).inflate(R.layout.float_video, this);

			// 布局
			m_layVideo = (LinearLayout)view.findViewById(R.id.layVideo);
			
			// 宽度和高度
			m_nViewWidth = m_layVideo.getLayoutParams().width;
			m_nViewHeight = m_layVideo.getLayoutParams().height;
//			Log.d(TAG, String.format("ViewWidth: %d, ViewHeight: %d", m_nViewWidth, m_nViewHeight));
			
			// 播放界面
			m_btnPlay = (Button)view.findViewById(R.id.btnPlay);
			m_btnPause = (Button)view.findViewById(R.id.btnPause);
			m_seekBar = (SeekBar)view.findViewById(R.id.seekBar);
			m_txtTotal = (TextView)view.findViewById(R.id.txtTotal);
			m_txtCurrent = (TextView)view.findViewById(R.id.txtCurrent);
			m_btnPlay.setOnClickListener(this);
			m_btnPause.setOnClickListener(this);
			
			// 进度条
			m_seekBar.setOnSeekBarChangeListener(this);
			m_seekBar.setThumbOffset(1);
			m_seekBar.setProgress(0);
			m_seekBar.setMax(1000);
			
			// 显示进度条
			m_handler.sendEmptyMessage(MSG_SHOW_PROG);
			
			// 设置播放
			setPlay();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 设置播放
	 */
	public void setPlay()
	{
		try
		{
			// 视频视图
			if (ms_layVideo == null) return;
			if (ms_layVideo.getVideoView() == null) return;
			
			// 正在播放
			if (ms_layVideo.getVideoView().isPlaying())
			{
				// 隐藏"播放"按钮
				m_btnPlay.setVisibility(View.INVISIBLE);
				
				// 显示"暂停"按钮
				m_btnPause.setVisibility(View.VISIBLE);
			}
			else
			{
				// 显示"播放"按钮
				m_btnPlay.setVisibility(View.VISIBLE);
				
				// 隐藏"暂停"按钮
				m_btnPause.setVisibility(View.INVISIBLE);
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 设置进度条
	 * @return
	 */
	private long setProgress()
	{
		try
		{
			// 正在拖拽
			if (m_bDragging) return 0;
			
			// 视频视图
			if (ms_layVideo == null) return 0;
			if (ms_layVideo.getVideoView() == null) return 0;

			// 持续时间
			long duration = ms_layVideo.getVideoView().getDuration();
			m_lDuration = duration;
			
			// 当前播放位置
			long position = ms_layVideo.getVideoView().getCurrentPosition();

			if (duration > 0)
			{
				// 设置位置
				long pos = 1000L * position / duration;
				m_seekBar.setProgress((int)pos);
			}
			
			// 时间
			m_txtCurrent.setText(time2Str(position));
			m_txtTotal.setText(time2Str(m_lDuration));

			return position;
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}

	/**
	 * 时间转为字符串
	 * @param lTime
	 * @return
	 */
	private String time2Str(final long lTime)
	{
		int nTotalSeconds = (int)(lTime / 1000);
		int nSeconds      = nTotalSeconds % 60;
		int nMinutes      = (nTotalSeconds / 60) % 60;
		int nHours        = nTotalSeconds / 3600;

		return nHours > 0 ? String.format("%02d:%02d:%02d", nHours, nMinutes, nSeconds) :
							String.format("%02d:%02d", nMinutes, nSeconds);
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
			// 显示进度条
			case MSG_SHOW_PROG:
				
				try
				{
					// 设置进度条
					long pos = setProgress();
					
					if (!m_bDragging)
					{
						// 显示进度条
						msg = obtainMessage(MSG_SHOW_PROG);
						
						// 延迟发送消息
						sendMessageDelayed(msg, 1000 - (pos % 1000));
//						Log.d(TAG, msg.toString());
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
