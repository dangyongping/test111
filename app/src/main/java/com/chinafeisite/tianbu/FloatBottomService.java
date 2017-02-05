package com.chinafeisite.tianbu;

import android.app.Service;
import android.content.BroadcastReceiver;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 底部悬浮窗服务类
 */
public class FloatBottomService extends Service
{
	// 需要权限 android.permission.SYSTEM_ALERT_WINDOW,
	// 并且需要在 AndroidManifest.xml 中添加如下内容:
	// <service android:name="com.chinafeisite.tianbu.FloatBottomService" />

	private final String TAG = "FloatBottomService";

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
			// 创建底部悬浮窗
			FloatManager.createBottom(getApplicationContext());	
			Log.d(TAG, "onCreate");
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
			Log.d(TAG, "onDestroy");
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * 底部悬浮窗布局类
 */
class FloatBottomLayout extends LinearLayout
{
//	private final String TAG = "FloatBottomLayout";
	
	// 布局
	private FrameLayout m_layBottom = null;

	// 宽度和高度
	private int m_nViewWidth = 0;
	private int m_nViewHeight = 0;
	public int getViewWidth() { return m_nViewWidth; }
	public int getViewHeight() { return m_nViewHeight; }
	
	/** 底部悬浮窗开图片 */
	private Bitmap    m_bmpBottom = null;

	/** 资源 */
	private Resources m_res    = null;	
		
	/**
	 * 构造函数
	 * @param context
	 */
	public FloatBottomLayout(Context context)
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
	public FloatBottomLayout(Context context, AttributeSet attrs)
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

				if (theApp.SCALED_XY)
				{
					x += theApp.getBottomX1();
					y += theApp.getBottomY1();
					x /= fWR;
					y /= fHR;					
				}
				else
				{
					x /= fWR;
					y /= fHR;
					x += theApp.getBottomX1();
					y += theApp.getBottomY1();
				}
				
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
					if (theApp.CUSTOMER_DHZ)
					{
						if (x>134&&x<286&&y>663&&y<720)
						{
							// wifi开关
	//						theApp.setWifiOn(!theApp.isWifiOn());
							try
							{
								MainActivity.openWifi();			
							}
							catch (Exception e)
							{
							}
							break;
						}
					}
					// 应答 E2
					theApp.responseE2((int)x, (int)y);
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
							theApp.responseE2((int)x, (int)y);
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
					theApp.responseE3((int)x, (int)y);
				break;
				
				// 动作取消
				case MotionEvent.ACTION_CANCEL:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
					theApp.responseE3((int)x, (int)y);
					break;
					
				// 超出范围
				case MotionEvent.ACTION_OUTSIDE:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
					theApp.responseE3((int)x, (int)y);
					break;
					
				// 默认
				default:
					
					// 停止移动
					theApp.setMoving(false);
					
					// 应答 E3
					theApp.responseE3((int)x, (int)y);
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
	@SuppressWarnings("deprecation")
	private void init(Context context)
	{
		try
		{
			// 资源
			m_res = context.getResources();
			
			// 从布局文件中加载视图
			View view = LayoutInflater.from(context).inflate(R.layout.float_bottom, this);

			// 布局
			m_layBottom = (FrameLayout)view.findViewById(R.id.layBottom);
			if(theApp.RECYCLE_BMP)
			{
				//根据语言设置背景图片
				if (m_bmpBottom != null && !m_bmpBottom.isRecycled())
				{
//					m_bmpBottom.recycle();
	//				m_bmpBottom = null;
				}
				
				if (theApp.getLang().equals("ch"))	
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom);
				else if (theApp.getLang().equals("tr"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_tr);
				else if (theApp.getLang().equals("de"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_de);
				else if (theApp.getLang().equals("ja"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_jp);
				else if (theApp.getLang().equals("pt"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_pt);	
				else if (theApp.getLang().equals("ko"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_kr);	
				else if (theApp.getLang().equals("sv"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_sv);
				else if (theApp.getLang().equals("ar"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_ar);	
				else if (theApp.getLang().equals("it"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_it);
				else if (theApp.getLang().equals("es"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_sp);
				else if (theApp.getLang().equals("fr"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_fr);	
				else if (theApp.getLang().equals("ru"))
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_ru);				
				else
					m_bmpBottom = BitmapFactory.decodeResource(m_res, R.drawable.float_bottom_en);		        
		        setBackgroundDrawable(new BitmapDrawable(m_bmpBottom)); 
			}
			else
			{
				if (theApp.USE_FLOAT_BACK)
				{
					//根据语言设置背景图片
					if (theApp.getLang().equals("ch"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom);
					}
					else if (theApp.getLang().equals("tr"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_tr);
					}
					else if (theApp.getLang().equals("de"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_de);
					}	
					else if (theApp.getLang().equals("fr"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_fr);
					}	
					else if (theApp.getLang().equals("ja"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_jp);
					}
					else if (theApp.getLang().equals("ko"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_kr);
					}
					else if (theApp.getLang().equals("ru"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_ru);
					}
					else if (theApp.getLang().equals("ar"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_ar);
					}
					else if (theApp.getLang().equals("sv"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_sv);
					}	
					else if (theApp.getLang().equals("pt"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_pt);
					}
					else if (theApp.getLang().equals("it"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_it);
					}
					else if (theApp.getLang().equals("es"))
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_sp);
					}				
					else
					{
						m_layBottom.setBackgroundResource(R.drawable.float_bottom_en);
					}
				}
			}		
			// 宽度和高度
			m_nViewWidth = m_layBottom.getLayoutParams().width;
			m_nViewHeight = m_layBottom.getLayoutParams().height;
//			Log.d(TAG, String.format("ViewWidth: %d, ViewHeight: %d", m_nViewWidth, m_nViewHeight));

			// OSD 视图
			OsdView1 osdView1 = (OsdView1)view.findViewById(R.id.osdView1);
			OsdView2 osdView2 = (OsdView2)view.findViewById(R.id.osdView2);
			osdView1.setBottom(true);
			osdView2.setBottom(true);
			theApp.setOsdViewBottom1(osdView1);
			theApp.setOsdViewBottom2(osdView2);
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
			if(theApp.RECYCLE_BOTTOM)
			{
				// 回收
				if (m_bmpBottom != null && !m_bmpBottom.isRecycled())
				{
					m_bmpBottom.recycle();
					m_bmpBottom = null ;
	//				theApp.MYSLog("FloatBottomService", "m_bmpBottom");
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
//			if(theApp.RECYCLE_BOTTOM)
			{
				// 回收
				if (m_bmpBottom != null && !m_bmpBottom.isRecycled())
				{
					m_bmpBottom.recycle();
					m_bmpBottom = null ;
	//				theApp.MYSLog("FloatBottomService", "m_bmpBottom");
				}
			}

		}
		catch (Exception e)
		{
		}
	}		
}
