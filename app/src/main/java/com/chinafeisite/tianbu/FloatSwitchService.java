package com.chinafeisite.tianbu;

//import java.util.Locale;

import android.app.Service;
//import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
//import android.os.Handler;
import android.os.IBinder;
//import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
//import android.widget.Toast;

/**
 * 开关悬浮窗服务类
 */
public class FloatSwitchService extends Service
{
	// 需要权限 android.permission.SYSTEM_ALERT_WINDOW,
	// 并且需要在 AndroidManifest.xml 中添加如下内容:
	// <service android:name="com.chinafeisite.tianbu.FloatSwitchService" />

	
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
			// 创建开关悬浮窗
			FloatManager.createSwitch(getApplicationContext());
			Log.d("FloatSwitchLayout", String.format("FloatSwitch: %S", String.valueOf(theApp.isFloatOn())));
		}
		catch (Exception e)
		{
		}	
	}

}

/**
 * 开关悬浮窗布局类
 */
class FloatSwitchLayout extends LinearLayout
{
	private final String TAG = "FloatSwitchLayout";
	
	/** 地区更改 */
	private static final int MSG_LOCALE_CHANGED = 0;
	
	// 布局
	private LinearLayout m_laySwitch = null;

	/** 是否可以触摸 */
	private static boolean ms_bTouch = true;
	
	// 宽度和高度
	private int m_nViewWidth = 0;
	private int m_nViewHeight = 0;
	public int getViewWidth() { return m_nViewWidth; }
	public int getViewHeight() { return m_nViewHeight; }

	/** 底部悬浮窗开图片 */
	private Bitmap    m_bmpOn_ = null;
	/** 底部悬浮窗关图片 */
	private Bitmap    m_bmpOff = null;
	/** 资源 */
	private Resources m_res    = null;
	
//	private int m_nTpCounter = 0;
//	private boolean m_nTpSwitch = false;
		
	/**
	 * 构造函数
	 * @param context
	 */
	public FloatSwitchLayout(Context context)
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
	public FloatSwitchLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 初始化
		init(context);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		try
		{
			float x = 0;
			float y = 0;
			int nAction = 0;
			
			try
			{
				// 在视图中的坐标
				x = event.getX();
				y = event.getY();

				// 根据比例计算坐标
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();
				if (fWR <= 0) fWR = 1;
				if (fHR <= 0) fHR = 1;
				x /= fWR;
				y /= fHR;
				x += theApp.getSwitchX1();
				y += theApp.getSwitchY1();
				
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
					
					// 不可以触摸
					Log.d(TAG, "Touch: " + ms_bTouch);
					if (!ms_bTouch) break;
					ms_bTouch = false;
				
					
					// 按下的坐标
					theApp.setDownX(x);
					theApp.setDownY(y);
					
					// 应答 E2
//					theApp.responseE2((int)x, (int)y);
					
					// 悬浮窗开关
					theApp.setFloatOn(!theApp.isFloatOn());
					Log.d(TAG, String.format("FloatSwitch: %S", String.valueOf(theApp.isFloatOn())));
					try
					{
						// 开
						if (theApp.isFloatOn())
						{
							//开悬浮窗
							theApp.responseE2(0, 0, 88);
							
							if(!theApp.RECYCLE_BMP)
							{
								// 设置背景图片
								m_laySwitch.setBackgroundResource(R.drawable.float_on);
							}
							else
							{
								// 设置背景图片
								if (m_bmpOn_ != null && !m_bmpOn_.isRecycled())
								{
//									m_bmpOn_.recycle();
//									m_bmpOn_ = null;
								}
								m_bmpOn_ = BitmapFactory.decodeResource(m_res, R.drawable.float_on);
								m_laySwitch.setBackgroundDrawable(new BitmapDrawable(m_bmpOn_));
							}
							// 启动顶部悬浮窗
							Intent intentTop = new Intent(getContext(), FloatTopService.class);
							getContext().startService(intentTop);
							
							// 启动底部悬浮窗
							Intent intentBottom = new Intent(getContext(), FloatBottomService.class);
							getContext().startService(intentBottom);
						}
						// 关
						else
						{	
							//关悬浮窗
							theApp.responseE2(0, 0, 87);
							
							//清除osd
							theApp.clearAllosd(false,true,false);	
							theApp.clearAllosd(false,false,true);	
													
							// 移除顶部悬浮窗
							FloatManager.removeTop(getContext());
							
//							theApp.MYSLog(TAG, "removeTop");
							
							// 移除底部悬浮窗
							FloatManager.removeBottom(getContext());
						
//							theApp.MYSLog(TAG, "removeBottom");
							if(!theApp.RECYCLE_BMP)
							{
								// 设置背景图片
								m_laySwitch.setBackgroundResource(R.drawable.float_off);
							}
							else
							{
								// 设置背景图片
								if (m_bmpOff != null && !m_bmpOff.isRecycled())
								{
//									m_bmpOff.recycle();
//									m_bmpOff = null;
								}
								m_bmpOff = BitmapFactory.decodeResource(m_res, R.drawable.float_off);
								m_laySwitch.setBackgroundDrawable(new BitmapDrawable(m_bmpOff));
							}
							// 停止顶部悬浮窗
							Intent intentTop = new Intent(getContext(), FloatTopService.class);
							getContext().stopService(intentTop);
										
							// 停止底部悬浮窗
							Intent intentBottom = new Intent(getContext(), FloatBottomService.class);
							getContext().stopService(intentBottom);
							
						}
					}
					catch(Exception e)
					{
						
					}
					
					try
					{
						// 线程
						new Thread()
						{
							@Override
							public void run()
							{
								try
								{
									// 休眠 2000 毫秒
									Thread.sleep(500);
									
									// 可以触摸
									ms_bTouch = true;
									Log.d(TAG, "Touch: " + ms_bTouch);
								}
								catch (Exception e)
								{
								}
							}
						}.start(); // 启动线程
					}
					catch(Exception e)
					{
					}					
					break;
					
				// 主点和辅点移动
				case MotionEvent.ACTION_MOVE:

					// 正在移动
					theApp.setMoving(true);
					
					// 响应移动
					if (theApp.isResponseMove())
					{
						// 按下的坐标
						float dwx = theApp.getDownX();
						float dwy = theApp.getDownY();
						
						// 坐标差值
						float dx = Math.abs(dwx - x);
						float dy = Math.abs(dwy - y);
						
						// 检查差值
						if (dx > theApp.MOVE_DELTA || dy > theApp.MOVE_DELTA)
						{
							// 移动时间
							theApp.setMoveTime(0);
							
							// 不应答移动
							theApp.setResponseMove(false);
							
							// 应答 E2
//							theApp.responseE2((int)x, (int)y);
						}
					}
					break;

				// 主点抬起
				// 辅点抬起
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
//					theApp.responseE3((int)x, (int)y);
					break;
					
				// 动作取消
				case MotionEvent.ACTION_CANCEL:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
//					theApp.responseE3((int)x, (int)y);
					break;
					
				// 超出范围
				case MotionEvent.ACTION_OUTSIDE:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
//					theApp.responseE3((int)x, (int)y);
					break;
					
				// 默认
				default:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
//					theApp.responseE3((int)x, (int)y);
					break;
				}
			}
			catch (Exception e)
			{
			}
			
			return true;
		}
		catch (Exception e)
		{
		}
		
		return true;
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context)
	{
		try
		{
			// 资源
			m_res = context.getResources();
				
			// 从布局文件中加载视图
			View view = LayoutInflater.from(context).inflate(R.layout.float_switch, this);

			// 布局
			m_laySwitch = (LinearLayout)view.findViewById(R.id.laySwitch);
			
			// 宽度和高度
			m_nViewWidth = m_laySwitch.getLayoutParams().width;
			m_nViewHeight = m_laySwitch.getLayoutParams().height;
//			Log.d(TAG, String.format("ViewWidth: %d, ViewHeight: %d", m_nViewWidth, m_nViewHeight));
			
			// 启动顶部悬浮窗
			Intent intentTop = new Intent(getContext(), FloatTopService.class);
			getContext().startService(intentTop);
			
			// 启动底部悬浮窗
			Intent intentBottom = new Intent(getContext(), FloatBottomService.class);
			getContext().startService(intentBottom);			
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 回收
	 */
	public void recycle()
	{
		try
		{
			if(theApp.RECYCLE_MID)
			{
				// 回收
				if (m_bmpOn_ != null && !m_bmpOn_.isRecycled())
				{
					m_bmpOn_.recycle();
	//				m_bmpOn_ = null;
				}
				
				if (m_bmpOff != null && !m_bmpOff.isRecycled())
				{
					m_bmpOff.recycle();
	//				m_bmpOff = null;
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 回收
	 */
	public void recycle_()
	{
		try
		{
//			if(theApp.RECYCLE_BMP)
			{
				// 回收
				if (m_bmpOn_ != null && !m_bmpOn_.isRecycled())
				{
					m_bmpOn_.recycle();
					m_bmpOn_ = null;
				}
				
				if (m_bmpOff != null && !m_bmpOff.isRecycled())
				{
					m_bmpOff.recycle();
					m_bmpOff = null;
				}
			}
		}
		catch (Exception e)
		{
		}
	}
/*	 BroadcastReceiver mPowerReceiver = new BroadcastReceiver() 
	 {
        public void onReceive(Context context, Intent intent) 
        {
            if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) 
            {
	//		Toast.makeText(mContext, "my test ???", Toast.LENGTH_LONG).show();
				try
				{
					// 界面语言
//					String strLang = Locale.getDefault().getLanguage();
//					Toast.makeText(getContext(), "Locale: " + strLang, Toast.LENGTH_LONG).show();
					
					// 悬浮窗 关
					theApp.setFloatOn(false);
					{
						// 移除顶部悬浮窗
						FloatManager.removeTop(getContext());
						
						// 移除底部悬浮窗
						FloatManager.removeBottom(getContext());
						
						// 设置背景图片
						m_laySwitch.setBackgroundResource(R.drawable.float_off);
						
						// 停止顶部悬浮窗
						Intent intentTop = new Intent(getContext(), FloatTopService.class);
						getContext().stopService(intentTop);
						
						// 停止底部悬浮窗
						Intent intentBottom = new Intent(getContext(), FloatBottomService.class);
						getContext().stopService(intentBottom);
					}
				}
				catch (Exception e)
				{
//					Log.d(TAG, e.toString());
				}
            }
        }
	  };*/
	
}
