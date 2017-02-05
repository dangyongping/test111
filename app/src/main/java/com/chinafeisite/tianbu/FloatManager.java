package com.chinafeisite.tianbu;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 悬浮窗管理器类
 */
public class FloatManager
{
	private static final String TAG = "FloatManager";
	
	// 窗口管理器
	private static WindowManager ms_wm = null;
	public static WindowManager getWM() { return ms_wm; }
	
	// 视频悬浮窗
	private static FloatVideoLayout ms_layFloatVideo = null;
	private static WindowManager.LayoutParams ms_paramsFloatVideo = null;
	public static FloatVideoLayout getLayFloatVideo() { return ms_layFloatVideo; }
	
	// 顶部悬浮窗
	private static FloatTopLayout ms_layFloatTop = null;
	private static WindowManager.LayoutParams ms_paramsFloatTop = null;
	
	// 底部悬浮窗
	private static FloatBottomLayout ms_layFloatBottom = null;
	private static WindowManager.LayoutParams ms_paramsFloatBottom = null;
	
	// 开关悬浮窗
	private static FloatSwitchLayout ms_layFloatSwitch = null;
	private static WindowManager.LayoutParams ms_paramsFloatSwitch = null;
	
	// 顶部悬浮窗开关
	private static FloatTopSwitchLayout ms_layFloatTopSwitch = null;
	private static WindowManager.LayoutParams ms_paramsFloatTopSwitch = null;
	public static FloatTopSwitchLayout getLayFloatTopSwitch() { return ms_layFloatTopSwitch; }
	public static WindowManager.LayoutParams getParamsFloatTopSwitch() { return ms_paramsFloatTopSwitch; }
	
	// 底部悬浮窗开关
	private static FloatBottomSwitchLayout ms_layFloatBottomSwitch = null;
	private static WindowManager.LayoutParams ms_paramsFloatBottomSwitch = null;
	public static FloatBottomSwitchLayout getLayFloatBottomSwitch() { return ms_layFloatBottomSwitch; }
	public static WindowManager.LayoutParams getParamsFloatBottomSwitch() { return ms_paramsFloatBottomSwitch; }
	
	/**
	 * 获取窗口管理器
	 * @param context
	 * @return
	 */
	private static WindowManager getWindowManager(Context context)
	{
		try
		{
			if (ms_wm == null)
			{
				// 获取窗口服务
				ms_wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			}
			
			return ms_wm;
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 创建视频悬浮窗
	 * @param context
	 */
	public static void createVideo(Context context)
	{
		try
		{
			// 获取窗口管理器
			WindowManager wm = getWindowManager(context);
			
			// 宽度和高度
			int nWidth = wm.getDefaultDisplay().getWidth();
//			int nHeight = wm.getDefaultDisplay().getHeight();
//			Log.d(TAG, String.format("Width: %d, Height: %d", nWidth, nHeight));
			
			if (ms_layFloatVideo == null)
			{
				// 悬浮窗布局
				ms_layFloatVideo = new FloatVideoLayout(context);
				
				if (ms_paramsFloatVideo == null)
				{
					// 悬浮窗布局参数
					ms_paramsFloatVideo = new LayoutParams();

					// 宽度和高度
					int nViewWidth = ms_layFloatVideo.getViewWidth();
					int nViewHeight = ms_layFloatVideo.getViewHeight();
					
					// 设置参数
					ms_paramsFloatVideo.type = LayoutParams.TYPE_SYSTEM_ERROR;
					ms_paramsFloatVideo.format = PixelFormat.RGBA_8888;
					ms_paramsFloatVideo.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					ms_paramsFloatVideo.gravity = Gravity.LEFT | Gravity.TOP;
					ms_paramsFloatVideo.x = (nWidth - nViewWidth) / 2;
					ms_paramsFloatVideo.y = 0;
					ms_paramsFloatVideo.width = nViewWidth;
					ms_paramsFloatVideo.height = nViewHeight;
				}
				
				// 添加视图
				wm.addView(ms_layFloatVideo, ms_paramsFloatVideo);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 创建开关悬浮窗
	 * @param context
	 */
	public static void createSwitch(Context context)
	{
		try
		{
			// 获取窗口管理器
			WindowManager wm = getWindowManager(context);
			
			// 宽度和高度
			int nWidth = wm.getDefaultDisplay().getWidth();
			int nHeight = wm.getDefaultDisplay().getHeight();
//			Log.d(TAG, String.format("Width: %d, Height: %d", nWidth, nHeight));
			
			if (ms_layFloatSwitch == null)
			{
				// 悬浮窗布局
				ms_layFloatSwitch = new FloatSwitchLayout(context);
				
				if (ms_paramsFloatSwitch == null)
				{
					// 悬浮窗布局参数
					ms_paramsFloatSwitch = new LayoutParams();

					// 宽度和高度
					int nViewWidth = ms_layFloatSwitch.getViewWidth();
					int nViewHeight = ms_layFloatSwitch.getViewHeight();
					
					// 设置参数
					ms_paramsFloatSwitch.type = LayoutParams.TYPE_SYSTEM_ERROR;
					ms_paramsFloatSwitch.format = PixelFormat.RGBA_8888;
					ms_paramsFloatSwitch.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					ms_paramsFloatSwitch.gravity = Gravity.LEFT | Gravity.TOP;
			
					//1280*800
//					ms_paramsFloatSwitch.x = 1240;//nWidth - nViewWidth;
					
					//1366*768
//					ms_paramsFloatSwitch.x = 1326;//nWidth - nViewWidth;
					
					//1920*1080
					ms_paramsFloatSwitch.x = 1860;//nWidth - nViewWidth;
					
					ms_paramsFloatSwitch.y = 240;//nHeight / 2 - nViewHeight / 2;
					ms_paramsFloatSwitch.width = nViewWidth;
					ms_paramsFloatSwitch.height = nViewHeight;
				}
				
				// 添加视图
				wm.addView(ms_layFloatSwitch, ms_paramsFloatSwitch);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 创建顶部悬浮窗
	 * @param context
	 */
	public static void createTop(Context context)
	{
		try
		{
			// 获取窗口管理器
			WindowManager wm = getWindowManager(context);
			
			// 宽度和高度
//			int nWidth = wm.getDefaultDisplay().getWidth();
//			int nHeight = wm.getDefaultDisplay().getHeight();
//			Log.d(TAG, String.format("Width: %d, Height: %d", nWidth, nHeight));
			
			if (ms_layFloatTop == null)
			{
				// 悬浮窗布局
				ms_layFloatTop = new FloatTopLayout(context);
				
				if (ms_paramsFloatTop == null)
				{
					// 悬浮窗布局参数
					ms_paramsFloatTop = new LayoutParams();

					// 宽度和高度
					int nViewWidth = ms_layFloatTop.getViewWidth();
					int nViewHeight = ms_layFloatTop.getViewHeight();
					
					// 设置参数
					ms_paramsFloatTop.type = LayoutParams.TYPE_PHONE;
					ms_paramsFloatTop.format = PixelFormat.RGBA_8888;
					ms_paramsFloatTop.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					ms_paramsFloatTop.gravity = Gravity.LEFT | Gravity.TOP;
					ms_paramsFloatTop.x = 0;
					ms_paramsFloatTop.y = 0;
					ms_paramsFloatTop.width = nViewWidth;
					ms_paramsFloatTop.height = nViewHeight;
				}
				
				// 添加视图
				wm.addView(ms_layFloatTop, ms_paramsFloatTop);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 创建顶部悬浮窗开关
	 * @param context
	 */
	public static void createTopSwitch(Context context)
	{
		try
		{
			// 获取窗口管理器
			WindowManager wm = getWindowManager(context);
			
			// 宽度和高度
			float fWR = theApp.getWidthRatio();
//			float fHR = theApp.getHeightRatio();
			int nWidth = wm.getDefaultDisplay().getWidth();
//			int nHeight = wm.getDefaultDisplay().getHeight();
//			Log.d(TAG, String.format("Width: %d, Height: %d", nWidth, nHeight));
			
			if (ms_layFloatTopSwitch == null)
			{
				// 悬浮窗布局
				ms_layFloatTopSwitch = new FloatTopSwitchLayout(context);
				
				if (ms_paramsFloatTopSwitch == null)
				{
					// 悬浮窗布局参数
					ms_paramsFloatTopSwitch = new LayoutParams();

					// 宽度和高度
					int nViewWidth = ms_layFloatTopSwitch.getViewWidth();
					int nViewHeight = ms_layFloatTopSwitch.getViewHeight();
					
					// 设置参数
					int nX = nWidth - (int)(nViewWidth + 15 * fWR);
					int nY = theApp.getTopH();
					ms_paramsFloatTopSwitch.type = LayoutParams.TYPE_SYSTEM_ERROR;
					ms_paramsFloatTopSwitch.format = PixelFormat.RGBA_8888;
					ms_paramsFloatTopSwitch.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					ms_paramsFloatTopSwitch.gravity = Gravity.LEFT | Gravity.TOP;
					ms_paramsFloatTopSwitch.x = nX;
					ms_paramsFloatTopSwitch.y = nY;
					ms_paramsFloatTopSwitch.width = nViewWidth;
					ms_paramsFloatTopSwitch.height = nViewHeight;
					theApp.SLog(TAG, String.format("Top - X: %d, Y: %d, W: %d, H: %d", nX, nY, nViewWidth, nViewHeight));
				}
				
				// 添加视图
				wm.addView(ms_layFloatTopSwitch, ms_paramsFloatTopSwitch);
				theApp.SLog(TAG, "createTopSwitch");
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 创建底部悬浮窗
	 * @param context
	 */
	public static void createBottom(Context context)
	{
		try
		{
			// 获取窗口管理器
			WindowManager wm = getWindowManager(context);
			
			// 宽度和高度
//			int nWidth = wm.getDefaultDisplay().getWidth();
			int nHeight = wm.getDefaultDisplay().getHeight();
//			Log.d(TAG, String.format("Width: %d, Height: %d", nWidth, nHeight));
			
			if (ms_layFloatBottom == null)
			{
				Log.d(TAG, "ms_layFloatBottom");
				// 悬浮窗布局
				ms_layFloatBottom = new FloatBottomLayout(context);
				
				if (ms_paramsFloatBottom == null)
				{
					// 悬浮窗布局参数
					ms_paramsFloatBottom = new LayoutParams();

					// 宽度和高度
					int nViewWidth = ms_layFloatBottom.getViewWidth();
					int nViewHeight = ms_layFloatBottom.getViewHeight();
					
					// 设置参数
					ms_paramsFloatBottom.type = LayoutParams.TYPE_PHONE;
					ms_paramsFloatBottom.format = PixelFormat.RGBA_8888;
					ms_paramsFloatBottom.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					ms_paramsFloatBottom.gravity = Gravity.LEFT | Gravity.TOP;
					ms_paramsFloatBottom.x = 0;
					ms_paramsFloatBottom.y = nHeight - nViewHeight;
					ms_paramsFloatBottom.width = nViewWidth;
					ms_paramsFloatBottom.height = nViewHeight;
				}
				
				// 添加视图
				wm.addView(ms_layFloatBottom, ms_paramsFloatBottom);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 创建底部悬浮窗开关
	 * @param context
	 */
	public static void createBottomSwitch(Context context)
	{
		try
		{
			// 获取窗口管理器
			WindowManager wm = getWindowManager(context);
			
			// 宽度和高度
			float fWR = theApp.getWidthRatio();
//			float fHR = theApp.getHeightRatio();
			int nWidth = wm.getDefaultDisplay().getWidth();
			int nHeight = wm.getDefaultDisplay().getHeight();
//			Log.d(TAG, String.format("Width: %d, Height: %d", nWidth, nHeight));
			
			if (ms_layFloatBottomSwitch == null)
			{
				// 悬浮窗布局
				ms_layFloatBottomSwitch = new FloatBottomSwitchLayout(context);
				
				if (ms_paramsFloatBottomSwitch == null)
				{
					// 悬浮窗布局参数
					ms_paramsFloatBottomSwitch = new LayoutParams();

					// 宽度和高度
					int nViewWidth = ms_layFloatBottomSwitch.getViewWidth();
					int nViewHeight = ms_layFloatBottomSwitch.getViewHeight();
					
					// 设置参数
					int nX = nWidth - (int)(nViewWidth + 15 * fWR);
					int nY = nHeight - theApp.getBottomH() - nViewHeight;
					ms_paramsFloatBottomSwitch.type = LayoutParams.TYPE_SYSTEM_ERROR;
					ms_paramsFloatBottomSwitch.format = PixelFormat.RGBA_8888;
					ms_paramsFloatBottomSwitch.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					ms_paramsFloatBottomSwitch.gravity = Gravity.LEFT | Gravity.TOP;
					ms_paramsFloatBottomSwitch.x = nX;
					ms_paramsFloatBottomSwitch.y = nY;
					ms_paramsFloatBottomSwitch.width = nViewWidth;
					ms_paramsFloatBottomSwitch.height = nViewHeight;
					theApp.SLog(TAG, String.format("Bottom - X: %d, Y: %d, W: %d, H: %d", nX, nY, nViewWidth, nViewHeight));
				}
				
				// 添加视图
				wm.addView(ms_layFloatBottomSwitch, ms_paramsFloatBottomSwitch);
				theApp.SLog(TAG, "createBottomSwitch");
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除所有悬浮窗
	 * @param context
	 */
	public static void removeAll(Context context)
	{
		try
		{
			// 移除视频悬浮窗
			removeVideo(context);
			
			// 移除顶部, 底部和开关悬浮窗
			removeTBS(context);
			
			// 移除顶部, 底部和开关悬浮窗
			removeTBS_(context);			
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除顶部, 底部和开关悬浮窗
	 * @param context
	 */
	public static void removeTBS(Context context)
	{
		try
		{
			// 移除开关悬浮窗
			removeSwitch(context);
			
			// 移除顶部悬浮窗
			removeTop(context);
			removeTopSwitch(context);
			
			// 移除底部悬浮窗
			removeBottom(context);
			removeBottomSwitch(context);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除顶部, 底部和开关悬浮窗
	 * @param context
	 */
	public static void removeTBS_(Context context)
	{
		try
		{
			// 移除开关悬浮窗
			removeSwitch_(context);	
			
			// 移除顶部悬浮窗
			removeTop_(context);	
			
			// 移除底部悬浮窗
			removeBottom_(context);

		}
		catch (Exception e)
		{
		}
	}
		
	/**
	 * 停止所有悬浮窗服务
	 * @param context
	 */
	public static void stopAllService(Context context)
	{
		try
		{
			// 停止视频悬浮窗
			Intent intentVideo = new Intent(context, FloatVideoService.class);
			context.stopService(intentVideo);

			// 停止顶部, 底部和开关悬浮窗服务
			stopTBSService(context);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 停止顶部, 底部和开关悬浮窗服务
	 * @param context
	 */
	public static void stopTBSService(Context context)
	{
		try
		{
			// 停止开关悬浮窗
			Intent intentSwitch = new Intent(context, FloatSwitchService.class);
			context.stopService(intentSwitch);
			
			// 停止顶部悬浮窗
			Intent intentTop = new Intent(context, FloatTopService.class);
			Intent intentTopSwitch = new Intent(context, FloatTopSwitchService.class);
			context.stopService(intentTop);
			context.stopService(intentTopSwitch);
			
			// 停止底部悬浮窗
			Intent intentBottom = new Intent(context, FloatBottomService.class);
			Intent intentBottomSwitch = new Intent(context, FloatBottomSwitchService.class);
			context.stopService(intentBottom);
			context.stopService(intentBottomSwitch);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除视频悬浮窗
	 * @param context
	 */
	public static void removeVideo(Context context)
	{
		try
		{
			if (ms_layFloatVideo != null)
			{
				// 销毁
				ms_layFloatVideo.onDestroy();
				
				// 获取窗口管理器
				WindowManager wm = getWindowManager(context);
				
				// 移除视图
				wm.removeView(ms_layFloatVideo);
				ms_layFloatVideo = null;
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除开关悬浮窗
	 * @param context
	 */
	private static void removeSwitch(Context context)
	{
		try
		{
			if (ms_layFloatSwitch != null)
			{
				// 获取窗口管理器
				WindowManager wm = getWindowManager(context);
				
				// 回收
				ms_layFloatSwitch.recycle();
				
				// 移除视图
				wm.removeView(ms_layFloatSwitch);
				ms_layFloatSwitch = null;
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除开关悬浮窗
	 * @param context
	 */
	private static void removeSwitch_(Context context)
	{
		try
		{
			if (ms_layFloatSwitch != null)
			{
				// 回收
				ms_layFloatSwitch.recycle();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除顶部悬浮窗
	 * @param context
	 */
	public static void removeTop_(Context context)
	{
		try
		{
			if (ms_layFloatTop != null)
			{
				// 回收
				ms_layFloatTop.recycle();
	
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除底部悬浮窗
	 * @param context
	 */
	public static void removeBottom_(Context context)
	{
		try
		{
			if (ms_layFloatBottom != null)
			{
				// 回收
				ms_layFloatBottom.recycle();
			}
		}
		catch (Exception e)
		{
		}
	}
			
	/**
	 * 移除顶部悬浮窗
	 * @param context
	 */
	public static void removeTop(Context context)
	{
		try
		{
			if (ms_layFloatTop != null)
			{
				// 获取窗口管理器
				WindowManager wm = getWindowManager(context);
				
				// 回收
				ms_layFloatTop.recycle();
				
				// 移除视图
				wm.removeView(ms_layFloatTop);
				ms_layFloatTop = null;
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除顶部悬浮窗开关
	 * @param context
	 */
	private static void removeTopSwitch(Context context)
	{
		try
		{
			if (ms_layFloatTopSwitch != null)
			{
				// 获取窗口管理器
				WindowManager wm = getWindowManager(context);
				
				// 回收
				ms_layFloatTopSwitch.recycle();
				
				// 移除视图
				wm.removeView(ms_layFloatTopSwitch);
				ms_layFloatTopSwitch = null;
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除底部悬浮窗
	 * @param context
	 */
	public static void removeBottom(Context context)
	{
		try
		{
			if (ms_layFloatBottom != null)
			{
				// 获取窗口管理器
				WindowManager wm = getWindowManager(context);
				
				// 回收
				ms_layFloatBottom.recycle();
				
				// 移除视图
				wm.removeView(ms_layFloatBottom);
				ms_layFloatBottom = null;
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 移除底部悬浮窗开关
	 * @param context
	 */
	private static void removeBottomSwitch(Context context)
	{
		try
		{
			if (ms_layFloatBottomSwitch != null)
			{
				// 获取窗口管理器
				WindowManager wm = getWindowManager(context);
				
				// 回收
				ms_layFloatBottomSwitch.recycle();
						
				// 移除视图
				wm.removeView(ms_layFloatBottomSwitch);
				ms_layFloatBottomSwitch = null;
			}
		}
		catch (Exception e)
		{
		}
	}
}
