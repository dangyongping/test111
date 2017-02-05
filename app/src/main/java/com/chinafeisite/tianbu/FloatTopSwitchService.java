package com.chinafeisite.tianbu;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 顶部悬浮窗开关服务类
 */
public class FloatTopSwitchService extends Service
{
	// 需要权限 android.permission.SYSTEM_ALERT_WINDOW,
	// 并且需要在 AndroidManifest.xml 中添加如下内容:
	// <service android:name="com.chinafeisite.tianbu.FloatTopSwitchService" />

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
			// 创建顶部悬浮窗开关
			FloatManager.createTopSwitch(getApplicationContext());
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * 顶部悬浮窗开关布局类
 */
class FloatTopSwitchLayout extends LinearLayout
{
	private final String TAG = "FloatTopSwitchLayout";

	// 布局
	private LinearLayout m_layTopSwitch = null;

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
	
	/**
	 * 构造函数
	 * @param context
	 */
	public FloatTopSwitchLayout(Context context)
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
	public FloatTopSwitchLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 初始化
		init(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		try
		{
			float x = 0;
			float y = 0;
			int nAction = 0;

			// 宽度和高度比例
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
					
					// 按下的坐标
					theApp.setDownX(x);
					theApp.setDownY(y);
					
					// 应答 E2
//					theApp.responseE2((int)x, (int)y);
					
					// 顶部悬浮窗开关
					theApp.setFloatTopOn(!theApp.isFloatTopOn());
					Log.d(TAG, String.format("FloatTopSwitch: %S", String.valueOf(theApp.isFloatTopOn())));
					
					// 开
					if (theApp.isFloatTopOn())
					{
						if(!theApp.RECYCLE_BMP)
						{
							// 设置背景图片
							m_layTopSwitch.setBackgroundResource(R.drawable.float_top_on);
						}
						else
						{
							// 设置背景图片
							if (m_bmpOn_ != null && !m_bmpOn_.isRecycled()) m_bmpOn_.recycle();
							m_bmpOn_ = BitmapFactory.decodeResource(m_res, R.drawable.float_top_on);
							m_layTopSwitch.setBackgroundDrawable(new BitmapDrawable(m_bmpOn_));
						}
						if (FloatManager.getWM() != null &&
							FloatManager.getLayFloatTopSwitch() != null &&
							FloatManager.getParamsFloatTopSwitch() != null)
						{
							// 设置坐标
							FloatManager.getParamsFloatTopSwitch().y = (int)(94 * fHR);
							
							// 刷新布局
							FloatManager.getWM().updateViewLayout(FloatManager.getLayFloatTopSwitch(), FloatManager.getParamsFloatTopSwitch());
						}
						
						// 启动顶部悬浮窗
						Intent intentTop = new Intent(getContext(), FloatTopService.class);
						getContext().startService(intentTop);
					}
					// 关
					else
					{
						// 移除顶部悬浮窗
						FloatManager.removeTop(getContext());
						if(!theApp.RECYCLE_BMP)
						{
							// 设置背景图片
							m_layTopSwitch.setBackgroundResource(R.drawable.float_top_off);
						}
						else
						{
							// 设置背景图片
							if (m_bmpOff != null && !m_bmpOff.isRecycled()) m_bmpOff.recycle();
							m_bmpOff = BitmapFactory.decodeResource(m_res, R.drawable.float_top_off);
							m_layTopSwitch.setBackgroundDrawable(new BitmapDrawable(m_bmpOff));
						}
						if (FloatManager.getWM() != null &&
							FloatManager.getLayFloatTopSwitch() != null &&
							FloatManager.getParamsFloatTopSwitch() != null)
						{
							// 设置坐标
							FloatManager.getParamsFloatTopSwitch().y = 0;
							
							// 刷新布局
							FloatManager.getWM().updateViewLayout(FloatManager.getLayFloatTopSwitch(), FloatManager.getParamsFloatTopSwitch());
						}
						
						// 停止顶部悬浮窗
						Intent intentTop = new Intent(getContext(), FloatTopService.class);
						getContext().stopService(intentTop);
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
			View view = LayoutInflater.from(context).inflate(R.layout.float_top_switch, this);

			// 布局
			m_layTopSwitch = (LinearLayout)view.findViewById(R.id.layTopSwitch);
			
			// 宽度和高度
			m_nViewWidth = m_layTopSwitch.getLayoutParams().width;
			m_nViewHeight = m_layTopSwitch.getLayoutParams().height;
//			Log.d(TAG, String.format("ViewWidth: %d, ViewHeight: %d", m_nViewWidth, m_nViewHeight));
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
			if(theApp.RECYCLE_BMP)
			{
				// 回收
				if (m_bmpOn_ != null && !m_bmpOn_.isRecycled()) m_bmpOn_.recycle();
				if (m_bmpOff != null && !m_bmpOff.isRecycled()) m_bmpOff.recycle();
			}
		}
		catch (Exception e)
		{
		}
	}	
}
