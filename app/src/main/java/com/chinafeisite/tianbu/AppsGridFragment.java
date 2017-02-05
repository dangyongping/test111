package com.chinafeisite.tianbu;

import java.util.ArrayList;
import java.util.List;



import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * Apps 网格片段类
 */
public class AppsGridFragment extends Fragment implements OnItemClickListener
{
	private final String TAG = "AppsGridFragment";

	// 起始和结束索引
	private int m_nStart = -1;
	private int m_nEnd = -1;

	// Apps 列表
	private List<ResolveInfo> m_listApps = theApp.getListApps();
	public void setListApps(List<ResolveInfo> list) { m_listApps = list; }

	// 启动的 Activity
	private static Intent ms_startActivity = null;
	private static String ms_strStartActivityPkg = "";
	public static Intent getStartActivity() { return ms_startActivity; }
	public static String getStartActivityPkg() { return ms_strStartActivityPkg; }
	
    /**
     * 构造函数
     * @param listApps Apps 列表
     * @param nStart 起始索引
     * @param nEnd 结束索引
     */
/*	public AppsGridFragment(List<ResolveInfo> listApps, final int nStart, final int nEnd)
    {
    	try
    	{
    		if (listApps == null) return;
    		
    		// 数量
    		int nSize = listApps.size();
    		if (nSize < 1) return;
    		if (nStart < 0 || nStart >= nSize) return;
    		if (nEnd < 0 || nEnd <= nStart || nEnd >= nSize) return;
    		
    		// 起始和结束索引
    		m_nStart = nStart;
    		m_nEnd = nEnd;

			// Apps 列表
    		m_listApps = new ArrayList<ResolveInfo>();
			
			for (int i = m_nStart; i <= m_nEnd; i++)
			{
				m_listApps.add(listApps.get(i));
			}
    	}
    	catch (Exception e)
    	{
    	}
    }
*/
	/**
	 * 新建实例
	 * @param listApps
	 * @param nStart
	 * @param nEnd
	 * @return
	 */
    public static AppsGridFragment newInstance(List<ResolveInfo> listApps, final int nStart, final int nEnd)
    {
    	try
    	{
    		if (listApps == null) return null;
    		
    		// 数量
    		int nSize = listApps.size();
    		if (nSize < 1) return null;
    		if (nStart < 0 || nStart >= nSize) return null;
    		if (nEnd < 0 || nEnd <= nStart || nEnd >= nSize) return null;
    		
    		AppsGridFragment fragment = new AppsGridFragment();
    		
    		// 起始和结束索引
    		fragment.m_nStart = nStart;
    		fragment.m_nEnd = nEnd;

			// Apps 列表
    		fragment.m_listApps = new ArrayList<ResolveInfo>();
			
			for (int i = fragment.m_nStart; i <= fragment.m_nEnd; i++)
			{
				fragment.m_listApps.add(listApps.get(i));
			}
			
			return fragment;
    	}
    	catch (Exception e)
    	{
    	}
    	
    	return null;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	try
    	{
			// 从布局文件中加载视图
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_apps_grid, null);
			
			// 网格视图
			GridView gridView = (GridView)view.findViewById(R.id.gridView);
			gridView.setOnItemClickListener(this);

			// 检测 Apps 列表
			if (m_listApps != null)
			{
				// 适配器
				AppsAdapter adapter = new AppsAdapter(getActivity(), m_listApps);
				if (gridView != null) gridView.setAdapter(adapter);
			}
			
			return view;
    	}
    	catch (Exception e)
    	{
    	}
    	
    	return null;
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
				Intent intentSwitch = new Intent(getActivity(), FloatSwitchService.class);
				getActivity().startService(intentSwitch);
			}
			// 不启用悬浮窗单个开关
			else
			{
				// 开启顶部悬浮窗
				theApp.setFloatTopOn(true);
				
				// 开启底部悬浮窗
				theApp.setFloatBottomOn(true);
				
				// 启动顶部悬浮窗开关
				Intent intentTopSwitch = new Intent(getActivity(), FloatTopSwitchService.class);
				getActivity().startService(intentTopSwitch);
				
				// 启动底部悬浮窗开关
				Intent intentBottomSwitch = new Intent(getActivity(), FloatBottomSwitchService.class);
				getActivity().startService(intentBottomSwitch);
			}
			
			// 启动顶部悬浮窗
			if (!theApp.NOTOP)
			{
				Intent intentTop = new Intent(getActivity(), FloatTopService.class);
				getActivity().startService(intentTop);
			}
			
			// 启动底部悬浮窗
			if (!theApp.NOBOTTOM)
			{
				Intent intentBottom = new Intent(getActivity(), FloatBottomService.class);
				getActivity().startService(intentBottom);
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
			getActivity().startActivity(intent);
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
}
