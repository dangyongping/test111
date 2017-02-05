package com.chinafeisite.tianbu;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 顶部悬浮窗服务类
 */
public class FloatTopService extends Service
{
	// 需要权限 android.permission.SYSTEM_ALERT_WINDOW,
	// 并且需要在 AndroidManifest.xml 中添加如下内容:
	// <service android:name="com.chinafeisite.tianbu.FloatTopService" />

	private final String TAG = "FloatTopService";

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
			// 创建顶部悬浮窗
			FloatManager.createTop(getApplicationContext());
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
 * 顶部悬浮窗布局类
 */
class FloatTopLayout extends LinearLayout
{
//	private final String TAG = "FloatTopLayout";

	// 布局
	private FrameLayout m_layTop = null;

	// 宽度和高度
	private int m_nViewWidth = 0;
	private int m_nViewHeight = 0;
	public int getViewWidth() { return m_nViewWidth; }
	public int getViewHeight() { return m_nViewHeight; }
	
	/** 底部悬浮窗开图片 */
	private Bitmap    m_bmpTop = null;

	/** 资源 */
	private Resources m_res    = null;	
	
	/**
	 * 构造函数
	 * @param context
	 */
	public FloatTopLayout(Context context)
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
	public FloatTopLayout(Context context, AttributeSet attrs)
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
					x += theApp.getTopX1();
					y += theApp.getTopY1();
					x /= fWR;
					y /= fHR;					
				}
				else
				{
					x /= fWR;
					y /= fHR;
					x += theApp.getTopX1();
					y += theApp.getTopY1();
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
					
					//wifi开关
					//天展  if (x>5&&x<100&&y>5&&y<100)
					//舒华  if (x>1180&&x<1280&&y>5&&y<100)
					if (theApp.CUSTOMER_TZ)
					{
						if (x>5&&x<100&&y>0&&y<100)
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
					if (theApp.CUSTOMER_SH)
					{
						if (x>1180&&x<1200&&y>0&&y<100)
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
			View view = LayoutInflater.from(context).inflate(R.layout.float_top, this);

			// 布局
			m_layTop = (FrameLayout)view.findViewById(R.id.layTop);
	
			if(theApp.RECYCLE_BMP)
			{
				//根据语言设置背景图片
//				if (m_bmpTop != null && !m_bmpTop.isRecycled()) m_bmpTop.recycle();
				
				if (theApp.getLang().equals("ch"))
				{
					if(theApp.isCkc())
						m_bmpTop = BitmapFactory.decodeResource(m_res, R.drawable.float_top_ckc);				
					else
						m_bmpTop = BitmapFactory.decodeResource(m_res, R.drawable.float_top);
				}
				else
				{
					if(theApp.isCkc())
						m_bmpTop = BitmapFactory.decodeResource(m_res, R.drawable.float_top_ckc_en);
					else
						m_bmpTop = BitmapFactory.decodeResource(m_res, R.drawable.float_top_en);
				}
				m_layTop.setBackgroundDrawable(new BitmapDrawable(m_bmpTop));			
			}
			else
			{
				if (theApp.USE_FLOAT_BACK)
				{
					if (theApp.getLang().equals("ch"))
					{
						if(theApp.isCkc())
						{
							m_layTop.setBackgroundResource(R.drawable.float_top_ckc);					
						}
						else
						{
							m_layTop.setBackgroundResource(R.drawable.float_top);
						}
					}
					else if (theApp.getLang().equals("tr"))
					{
						if(theApp.isCkc())
						{
							m_layTop.setBackgroundResource(R.drawable.float_top_ckc);					
						}
						else
						{
							m_layTop.setBackgroundResource(R.drawable.float_top_tr);
						}
					}	
					else if (theApp.getLang().equals("de"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_de);
					}
					else if (theApp.getLang().equals("fr"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_fr);
					}
					else if (theApp.getLang().equals("ru"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_ru);
					}	
					else if (theApp.getLang().equals("ja"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_jp);
					}	
					else if (theApp.getLang().equals("ko"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_kr);
					}	
					else if (theApp.getLang().equals("it"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_it);
					}
					else if (theApp.getLang().equals("es"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_sp);
					}
					else if (theApp.getLang().equals("pt"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_pt);
					}
					else if (theApp.getLang().equals("sv"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_sv);
					}
					else if (theApp.getLang().equals("ar"))
					{
						m_layTop.setBackgroundResource(R.drawable.float_top_ar);
					}			
					else
					{
						if(theApp.isCkc())
						{
							m_layTop.setBackgroundResource(R.drawable.float_top_ckc_en);
						}
						else
						{
							m_layTop.setBackgroundResource(R.drawable.float_top_en);
						}
					}
				}
			}
			// 宽度和高度
			m_nViewWidth = m_layTop.getLayoutParams().width;
			m_nViewHeight = m_layTop.getLayoutParams().height;
//			Log.d(TAG, String.format("ViewWidth: %d, ViewHeight: %d", m_nViewWidth, m_nViewHeight));
			
			// OSD 视图
			OsdView1 osdView1 = (OsdView1)view.findViewById(R.id.osdView1);
			OsdView2 osdView2 = (OsdView2)view.findViewById(R.id.osdView2);
			osdView1.setTop(true);
			osdView2.setTop(true);
			theApp.setOsdViewTop1(osdView1);
			theApp.setOsdViewTop2(osdView2);
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
			if(theApp.RECYCLE_TOP)
			{
				// 回收
				if (m_bmpTop != null && !m_bmpTop.isRecycled()) 
				{
					m_bmpTop.recycle();
					m_bmpTop = null;
	//				theApp.MYSLog("FloatTopService", "m_bmpTop");
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
				if (m_bmpTop != null && !m_bmpTop.isRecycled()) 
				{
					m_bmpTop.recycle();
					m_bmpTop = null;
	//				theApp.MYSLog("FloatTopService", "m_bmpTop");
				}
			}
		}
		catch (Exception e)
		{
		}
	}	
}
