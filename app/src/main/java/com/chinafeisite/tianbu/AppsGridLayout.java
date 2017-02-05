package com.chinafeisite.tianbu;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

/**
 * Apps 网格布局类
 */
public class AppsGridLayout extends RelativeLayout implements OnItemClickListener
{
	private final String TAG = "AppsGridLayout";

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 网格视图
	private GridView m_gridView = null;

	// Apps 适配器
	private AppsAdapter m_adapter = null;
	
	// Apps 列表
	private List<ResolveInfo> m_listApps = null;
	public void setListApps(List<ResolveInfo> list) { m_listApps = list; }
	
	// 启动的 Activity
	private static Intent ms_startActivity = null;
	private static String ms_strStartActivityPkg = "";
	public static Intent getStartActivity() { return ms_startActivity; }
	public static String getStartActivityPkg() { return ms_strStartActivityPkg; }
	
	/**
	 * 构造函数
	 * @param context
	 */
	public AppsGridLayout(Context context)
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
	public AppsGridLayout(Context context, AttributeSet attrs)
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
	public AppsGridLayout(Context context, AttributeSet attrs, int defStyle)
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
			// 启动悬浮窗
			theApp.setStartFloat(true);
			
			// 启用悬浮窗单个开关
			if (theApp.FLOAT_SWITCH)
			{
				//清除osd
				theApp.clearAllosd(true,false,false);	
				
				// 开启悬浮窗
				theApp.setFloatOn(true);
				
				// 启动开关悬浮窗
				Intent intentSwitch = new Intent(m_context, FloatSwitchService.class);
				m_context.startService(intentSwitch);
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 开启顶部悬浮窗
				theApp.setFloatTopOn(true);
				
				// 开启底部悬浮窗
				theApp.setFloatBottomOn(true);
				
				// 启动顶部悬浮窗开关
				Intent intentTopSwitch = new Intent(m_context, FloatTopSwitchService.class);
				m_context.startService(intentTopSwitch);
				
				// 启动底部悬浮窗开关
				Intent intentBottomSwitch = new Intent(m_context, FloatBottomSwitchService.class);
				m_context.startService(intentBottomSwitch);
			}
			
			// 启动顶部悬浮窗
			if (!theApp.NOTOP)
			{
				Intent intentTop = new Intent(m_context, FloatTopService.class);
				m_context.startService(intentTop);
			}
			
			// 启动底部悬浮窗
			if (!theApp.NOBOTTOM)
			{		
				Intent intentBottom = new Intent(m_context, FloatBottomService.class);
				m_context.startService(intentBottom);
			}
		}
		catch (Exception e)
		{
		}
		
		try
		{
			// App 信息
			ResolveInfo info = m_listApps.get(position);
			
			// Activity 类名
			String strCls = info.activityInfo.name;
			
			// App 包名
			String strPkg = info.activityInfo.packageName;
			
			// 启动 Activity
			ComponentName componet = new ComponentName(strPkg, strCls);
			Intent intent = new Intent();
			intent.setComponent(componet);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			m_context.startActivity(intent);
			theApp.setStartApp(true);
			
			// 启动的 Activity
			ms_startActivity = intent;
			ms_strStartActivityPkg = strPkg;
			Log.d(TAG, String.format("startActivity: %s, %s", strPkg, strCls));
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
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_apps_grid, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			// 网格视图
			m_gridView = (GridView)m_view.findViewById(R.id.gridView);
			m_gridView.setOnItemClickListener(this);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 初始化网格视图
	 */
	public void initGridView()
	{
		try
		{
			// 适配器
			m_adapter = new AppsAdapter(m_context, m_listApps);
			
			// 设置适配器
			if (m_gridView != null) m_gridView.setAdapter(m_adapter);
		}
		catch (Exception e)
		{
		}
	}
}
