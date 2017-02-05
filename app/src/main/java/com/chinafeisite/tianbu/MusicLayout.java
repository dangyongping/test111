package com.chinafeisite.tianbu;

import java.io.File;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 音乐布局类
 */
public class MusicLayout extends RelativeLayout implements OnClickListener, OnItemClickListener, OnSeekBarChangeListener
{
	private final String TAG = "MusicLayout";

	// 索引
	private int m_nIndex = -1;

	// 当前播放位置
	private static int m_nCurPos = -1;
	
	// 持续时间
	private long m_lDuration = -1;
	
	// 是否正处于播放界面
	private boolean m_bPlay = false;
	
	// 是否正在拖拽进度条
	private boolean m_bDragging = false;

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 列表
	private ListView m_lv = null;
	
	// 随机数
	private Random m_rand = new Random();
	
	// 播放界面
	private Button m_btnPrev = null;
	private Button m_btnNext = null;
	private Button m_btnPlay = null;
	private Button m_btnPause = null;
	private Button m_btnRand1 = null;
	private Button m_btnRand2 = null;
	private Button m_btnList1 = null;
	private Button m_btnList2 = null;
	private Button m_btnLoop1 = null;
	private Button m_btnLoop2 = null;
	private SeekBar m_seekBar = null;
	private TextView m_txtName = null;
	private TextView m_txtTotal = null;
	private TextView m_txtCurrent = null;
	private RelativeLayout m_layPlay = null;

	// 适配器
	private ListItemAdapter m_adapter = null;
	
	// 图像 URL
	private String[] m_strImageUrls = null;
	
	// 显示图像参数
	private DisplayImageOptions m_options = null;
	
	// 媒体播放器
	private static android.media.MediaPlayer m_mediaPlayer = null;
	public static android.media.MediaPlayer getMediaPlayer() { return m_mediaPlayer; }
//	public static void setMediaPlayer(android.media.MediaPlayer b) {  m_mediaPlayer = b ; }
	// 是否开启音效
//	private boolean m_bVolume = true;
//	public void setVolume(final boolean b) { m_bVolume = b; }
	
	// 播放模式
	private static int ms_nPlayMode = MusicLayout.PLAY_MODE_LIST;
	public static int getPlayMode() { return ms_nPlayMode; }
	public static void setPlayMode(final int n) { ms_nPlayMode = n; }

	// 路径
	private String m_strPath = "";
	private List<String> m_listPath = null;
	public void setListPath(List<String> list) { m_listPath = list; }

	// 图像加载器
	private ImageLoader m_imageLoader = null;
	public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }
	
	// 播放模式
	public static final int PLAY_MODE_ONE  = 0; // 单曲播放
	public static final int PLAY_MODE_RAND = 1; // 随机播放
	public static final int PLAY_MODE_LIST = 2; // 列表播放
	public static final int PLAY_MODE_LOOP = 3; // 循环所有
	
	// 消息
	private static final int MSG_UPDATE_LIST = 0; // 刷新列表
	private static final int MSG_SHOW_PROG   = 1; // 显示进度条
	
	/**
	 * 构造函数
	 * @param context
	 */
	public MusicLayout(Context context)
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
	public MusicLayout(Context context, AttributeSet attrs)
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
	public MusicLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	public void onClick(View view)
	{
		// 播放
		if (view.getId() == R.id.btnPlay)
		{
			play(true);
		}
		
		// 暂停
		if (view.getId() == R.id.btnPause)
		{
			pause();
		}
		
		// 上一曲
		if (view.getId() == R.id.btnPrev)
		{
			prev(true);
		}
		
		// 下一曲
		if (view.getId() == R.id.btnNext)
		{
			next(true);
		}
		
		// 随机播放 1
		if (view.getId() == R.id.btnRand1)
		{
			// 设置播放模式
			ms_nPlayMode = PLAY_MODE_RAND;
			setPlayMode();
		}
		
		// 随机播放 2
		if (view.getId() == R.id.btnRand2)
		{
			// 设置播放模式
			ms_nPlayMode = PLAY_MODE_ONE;
			setPlayMode();
		}
		
		// 列表播放 1
		if (view.getId() == R.id.btnList1)
		{
			// 设置播放模式
			ms_nPlayMode = PLAY_MODE_LIST;
			setPlayMode();
		}
		
		// 列表播放 2
		if (view.getId() == R.id.btnList2)
		{
			// 设置播放模式
			ms_nPlayMode = PLAY_MODE_ONE;
			setPlayMode();
		}
		
		// 循环所有 1
		if (view.getId() == R.id.btnLoop1)
		{
			// 设置播放模式
			ms_nPlayMode = PLAY_MODE_LOOP;
			setPlayMode();
		}
		
		// 循环所有 2
		if (view.getId() == R.id.btnLoop2)
		{
			// 设置播放模式
			ms_nPlayMode = PLAY_MODE_ONE;
			setPlayMode();
		}
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
		//		if (m_mediaPlayer != null)
				{
					// 停止
					m_nCurPos = -1;
					m_mediaPlayer.stop();
					m_mediaPlayer.release();
					m_mediaPlayer = null;
//					Log.d(TAG, "stop");
				}
			}
			catch (Exception e)
			{
			}
			
			// 播放
			play(true);
		}
		catch (Exception e)
		{
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if (!fromUser) return;

		try
		{
			// 跳转
			int nPos = (int)(m_lDuration * progress / 1000);
			m_mediaPlayer.seekTo(nPos);

			// 当前时间
			String strTime = time2Str(nPos);
			m_txtCurrent.setText(strTime);
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
	 * 播放完成监听器
	 */
	private android.media.MediaPlayer.OnCompletionListener m_onCompletionListener =
			new android.media.MediaPlayer.OnCompletionListener()
	{
		@Override
		public void onCompletion(android.media.MediaPlayer mp)
		{
			try
			{
				if (m_mediaPlayer != null)
				{
					// 停止
					m_nCurPos = -1;
					m_mediaPlayer.stop();
					m_mediaPlayer.release();
					m_mediaPlayer = null;
//					Log.d(TAG, "stop");

					// 随机播放和循环所有
					if (ms_nPlayMode == PLAY_MODE_RAND ||
						ms_nPlayMode == PLAY_MODE_LOOP)
					{
						// 下一曲
						next(true);
					}
					
					// 列表播放
					if (ms_nPlayMode == PLAY_MODE_LIST)
					{
						// 数量
						int nSize = m_listPath.size();
						
						// 索引
						if (m_nIndex < nSize - 1)
						{
							// 下一曲
							next(true);
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_music, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));

			// 列表视图
			m_lv = (ListView)m_view.findViewById(R.id.lvMusic);
			m_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			m_lv.setOnItemClickListener(this);
			m_lv.setItemsCanFocus(true);
			m_lv.setVisibility(View.VISIBLE);
			
			// 布局
			m_layPlay = (RelativeLayout)m_view.findViewById(R.id.layPlay);
			m_layPlay.setVisibility(View.INVISIBLE);
			
			// 播放界面
			m_btnPrev = (Button)m_view.findViewById(R.id.btnPrev);
			m_btnNext = (Button)m_view.findViewById(R.id.btnNext);
			m_btnPlay = (Button)m_view.findViewById(R.id.btnPlay);
			m_btnPause = (Button)m_view.findViewById(R.id.btnPause);
			m_btnRand1 = (Button)m_view.findViewById(R.id.btnRand1);
			m_btnRand2 = (Button)m_view.findViewById(R.id.btnRand2);
			m_btnList1 = (Button)m_view.findViewById(R.id.btnList1);
			m_btnList2 = (Button)m_view.findViewById(R.id.btnList2);
			m_btnLoop1 = (Button)m_view.findViewById(R.id.btnLoop1);
			m_btnLoop2 = (Button)m_view.findViewById(R.id.btnLoop2);
			m_seekBar = (SeekBar)m_view.findViewById(R.id.seekBar);
			m_txtName = (TextView)m_view.findViewById(R.id.txtName);
			m_txtTotal = (TextView)m_view.findViewById(R.id.txtTotal);
			m_txtCurrent = (TextView)m_view.findViewById(R.id.txtCurrent);
			m_btnPrev.setOnClickListener(this);
			m_btnNext.setOnClickListener(this);
			m_btnPlay.setOnClickListener(this);
			m_btnPause.setOnClickListener(this);
			m_btnRand1.setOnClickListener(this);
			m_btnRand2.setOnClickListener(this);
			m_btnList1.setOnClickListener(this);
			m_btnList2.setOnClickListener(this);
			m_btnLoop1.setOnClickListener(this);
			m_btnLoop2.setOnClickListener(this);
			
			// 进度条
			m_seekBar.setOnSeekBarChangeListener(this);
			m_seekBar.setThumbOffset(1);
			m_seekBar.setProgress(0);
			m_seekBar.setMax(1000);
			
			// 设置播放模式
			setPlayMode();

			// 音乐标记, 列表
			theApp.setMusicMark(theApp.MARK_LIST);
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
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
			
			// 随机播放
			if (ms_nPlayMode == PLAY_MODE_RAND)
			{
				int nRand = m_nIndex;
				
				do
				{
					// 随机数
					nRand = m_rand.nextInt(nSize);
				}
				while (nRand == m_nIndex);
				
				// 索引
				m_nIndex = nRand;
				m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;
			}
			
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
	 * 显示播放进度条
	 */
	public void ShowMusicProc()
	{
		// 显示进度条
		m_handler.sendEmptyMessage(MSG_SHOW_PROG);	
	}
	/**
	 * 播放
	 */
	public void play(boolean show)
	{
		// 关闭音效
//		if (!m_bVolume) return;
		
		try
		{
			// 显示播放界面
			if (show) showPlay();
			
			// 停止
			if (m_mediaPlayer != null && m_mediaPlayer.isPlaying()) m_mediaPlayer.stop();

			// 文件
			File file = new File(m_strPath);

			// 检测文件
			if (!file.exists() || !file.isFile()) return;
			
			// 文件名
			String strFileName = m_strPath;

			// 最后一个反斜杠的位置
			int nPos = strFileName.lastIndexOf("/") + 1;
			if (nPos <= 0 || nPos >= strFileName.length()) return;
			
			// 截取文件名
			strFileName = strFileName.substring(nPos);
			m_txtName.setText(strFileName);
			
			// 显示进度条
			m_handler.sendEmptyMessage(MSG_SHOW_PROG);
			
			if (m_context != null)
			{
				boolean bNew = false;
				
				// 检测当前播放位置
				if (m_nCurPos <= 0)
				{
					bNew = true;
				}
				else
				{
					if (m_mediaPlayer == null)
					{
						bNew = true;
					}
					else
					{
						// 播放
						m_mediaPlayer.seekTo(m_nCurPos);
						m_mediaPlayer.start();
					}
				}
				
				if (bNew)
				{
					try
					{
						if (m_mediaPlayer != null)
						{
							// 停止
							m_nCurPos = -1;
							m_mediaPlayer.stop();
							m_mediaPlayer.release();
							m_mediaPlayer = null;
//							Log.d(TAG, "stop");
						}
					}
					catch (Exception e)
					{
					}

					// 播放
					m_nCurPos = -1;
					Uri uri = Uri.parse(m_strPath);
					m_mediaPlayer = android.media.MediaPlayer.create(m_context, uri);
					m_mediaPlayer.start();
					m_mediaPlayer.setOnCompletionListener(m_onCompletionListener);
//					Log.d(TAG, "play");
				}
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
			if (m_mediaPlayer != null)
			{
				// 暂停
				m_mediaPlayer.pause();

				// 显示"播放"按钮
				m_btnPlay.setVisibility(View.VISIBLE);
				
				// 隐藏"暂停"按钮
				m_btnPause.setVisibility(View.INVISIBLE);
				
				// 当前播放位置
				m_nCurPos = m_mediaPlayer.getCurrentPosition();
				Log.d(TAG, String.format("pause: %d", m_nCurPos));
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
			// 后退
			back();
			
			if (m_mediaPlayer != null)
			{
				// 停止
				m_nCurPos = -1;
				m_mediaPlayer.stop();
				m_mediaPlayer.release();
				m_mediaPlayer = null;
//				Log.d(TAG, "stop");
			}
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * 停止
	 */
	public static void stopMusic()
	{
		try
		{		
			if (m_mediaPlayer != null)
			{
				// 停止
				m_nCurPos = -1;
				m_mediaPlayer.stop();
				m_mediaPlayer.release();
				m_mediaPlayer = null;
//				Log.d(TAG, "stop");
			}
		}
		catch (Exception e)
		{
		}
	}
		
	/**
	 * 上一曲
	 */
	public void prev(boolean show)
	{
		try
		{
			// 数量
			int nSize = m_listPath.size();
			if (nSize < 1) return;

			// 非随机播放
			if (ms_nPlayMode != PLAY_MODE_RAND)
			{
				// 索引
				m_nIndex--;
			}
			// 随机播放
			else
			{
				int nRand = m_nIndex;
				
				do
				{
					// 随机数
					nRand = m_rand.nextInt(nSize);
				}
				while (nRand == m_nIndex);
				
				// 索引
				m_nIndex = nRand;
			}

			// 索引
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;
			
			// 刷新列表
			m_handler.sendEmptyMessage(MSG_UPDATE_LIST);

			// 路径
			m_strPath = m_listPath.get(m_nIndex);
			
			// 播放
			play(show);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 下一曲
	 */
	public void next(boolean show)
	{
		try
		{
			// 数量
			int nSize = m_listPath.size();
			if (nSize < 1) return;
			
			// 非随机播放
			if (ms_nPlayMode != PLAY_MODE_RAND)
			{
				// 索引
				m_nIndex++;
			}
			// 随机播放
			else
			{
				int nRand = m_nIndex;
				
				do
				{
					// 随机数
					nRand = m_rand.nextInt(nSize);
				}
				while (nRand == m_nIndex);
				
				// 索引
				m_nIndex = nRand;
			}
			
			// 索引
			m_nIndex = (m_nIndex < 0 || m_nIndex >= nSize) ? 0 : m_nIndex;
			
			// 刷新列表
			m_handler.sendEmptyMessage(MSG_UPDATE_LIST);

			// 路径
			m_strPath = m_listPath.get(m_nIndex);
			
			// 播放
			play(show);
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
			// 未处于播放界面
			m_bPlay = false;
			
			// 多媒体标记
			theApp.setMusicMark(theApp.MARK_LIST);
			
			// 显示列表视图
			m_lv.setVisibility(View.VISIBLE);
			
			// 隐藏播放布局
			m_layPlay.setVisibility(View.INVISIBLE);
			
			// 移除消息
			m_handler.removeMessages(MSG_SHOW_PROG);
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
			if (m_bPlay) return m_bPlay;
			if (m_mediaPlayer != null) return m_mediaPlayer.isPlaying();
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 显示播放界面
	 */
	public void showPlay()
	{
		// 正处于播放界面
		m_bPlay = true;
		
		// 多媒体标记
		theApp.setMusicMark(theApp.MARK_PLAY);

		// 隐藏列表视图
		m_lv.setVisibility(View.GONE);
		
		// 显示播放布局
		m_layPlay.setVisibility(View.VISIBLE);

		// 隐藏"播放"按钮
		m_btnPlay.setVisibility(View.INVISIBLE);
		
		// 显示"暂停"按钮
		m_btnPause.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置播放模式
	 */
	private void setPlayMode()
	{
		// 单曲播放
		if (ms_nPlayMode == PLAY_MODE_ONE)
		{
			m_btnRand1.setVisibility(View.VISIBLE);
			m_btnRand2.setVisibility(View.INVISIBLE);
			m_btnList1.setVisibility(View.VISIBLE);
			m_btnList2.setVisibility(View.INVISIBLE);
			m_btnLoop1.setVisibility(View.VISIBLE);
			m_btnLoop2.setVisibility(View.INVISIBLE);
		}
		
		// 随机播放
		if (ms_nPlayMode == PLAY_MODE_RAND)
		{
			m_btnRand1.setVisibility(View.INVISIBLE);
			m_btnRand2.setVisibility(View.VISIBLE);
			m_btnList1.setVisibility(View.VISIBLE);
			m_btnList2.setVisibility(View.INVISIBLE);
			m_btnLoop1.setVisibility(View.VISIBLE);
			m_btnLoop2.setVisibility(View.INVISIBLE);
		}
		
		// 列表播放
		if (ms_nPlayMode == PLAY_MODE_LIST)
		{
			m_btnRand1.setVisibility(View.VISIBLE);
			m_btnRand2.setVisibility(View.INVISIBLE);
			m_btnList1.setVisibility(View.INVISIBLE);
			m_btnList2.setVisibility(View.VISIBLE);
			m_btnLoop1.setVisibility(View.VISIBLE);
			m_btnLoop2.setVisibility(View.INVISIBLE);
		}
		
		// 循环所有
		if (ms_nPlayMode == PLAY_MODE_LOOP)
		{
			m_btnRand1.setVisibility(View.VISIBLE);
			m_btnRand2.setVisibility(View.INVISIBLE);
			m_btnList1.setVisibility(View.VISIBLE);
			m_btnList2.setVisibility(View.INVISIBLE);
			m_btnLoop1.setVisibility(View.INVISIBLE);
			m_btnLoop2.setVisibility(View.VISIBLE);
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

			// 持续时间
			long duration = m_mediaPlayer.getDuration();
			m_lDuration = duration;
			
			// 当前播放位置
			long position = m_mediaPlayer.getCurrentPosition();

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
