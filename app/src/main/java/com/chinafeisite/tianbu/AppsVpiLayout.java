package com.chinafeisite.tianbu;

import java.util.List;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Apps 视图页指示器布局类
 */
public class AppsVpiLayout extends RelativeLayout
{
	// 网格行列数
	private int m_nRow = 3;
	private int m_nColumn = 8;
	
	// 视图
//	private View m_view = null;
	
	// Activity 的 Context
//	private Context m_context = null;
	
	// 视图页
	private ViewPager m_vp = null;
	
	// 视图页指示器
	private ViewPagerIndicator m_vpi = null;
	
	// Apps 网格片段适配器类
	private AppsGridFragmentAdapter m_adapter = null;
	
	// Activity
	private FragmentActivity m_activity = null;
	public void setActivity(FragmentActivity activity) { m_activity = activity; }

	// Apps 列表
	private List<ResolveInfo> m_listApps = null;
	public void setListApps(List<ResolveInfo> list) { m_listApps = list; }
	
	/**
	 * 构造函数
	 * @param context
	 */
	public AppsVpiLayout(Context context)
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
	public AppsVpiLayout(Context context, AttributeSet attrs)
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
	public AppsVpiLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
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
//			m_context = context;

			// 从布局文件中加载视图
/*			m_view = LayoutInflater.from(context).inflate(R.layout.layout_apps_vpi, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));*/
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 初始化视图页指示器
	 */
	public void initVPI()
	{
		try
		{
			if (m_activity == null) return;
			if (m_listApps == null) return;

			// 网格行列数
			m_nRow = (m_nRow < 1) ? 1 : m_nRow;
			m_nColumn = (m_nColumn < 1) ? 1 : m_nColumn;
			
			// App 数量
			int nSize = m_listApps.size();
			
//			theApp.MYSLog("AppsVpiLayout","2222"+ m_listApps.size());
			
			// 项数量
			int nItemNum = m_nRow * m_nColumn;
			
			// 网格数量
			int nGridNum = (int)((float)nSize / nItemNum) + 1;
			
//			theApp.MYSLog("AppsVpiLayout","1111"+ nItemNum);
			
			// Apps 网格片段适配器类
			FragmentManager fm = m_activity.getSupportFragmentManager();
//			FragmentManager fm = getChildFragmentManager();
			m_adapter = new AppsGridFragmentAdapter(fm, m_listApps);
			
			// 设置数量
			if (m_adapter != null) m_adapter.setItemNum(nItemNum);
			if (m_adapter != null) m_adapter.setGridNum(nGridNum);
			
			// 视图页
			m_vp = (ViewPager)m_activity.findViewById(R.id.vpApps);
			
			// 设置适配器
			if (m_vp != null && m_adapter != null) m_vp.setAdapter(m_adapter);
			
			// 视图页指示器
			m_vpi = (ViewPagerIndicator)m_activity.findViewById(R.id.vpiApps);
			
			// 设置视图页
			if (m_vp != null && m_vpi != null) m_vpi.setViewPager(m_vp);
			
/*			if (m_view == null) return;
			if (m_activity == null) return;
			if (m_listApps == null) return;
			
			// 网格行列数
			m_nRow = (m_nRow < 1) ? 1 : m_nRow;
			m_nColumn = (m_nColumn < 1) ? 1 : m_nColumn;
			
			// App 数量
			int nSize = m_listApps.size();
			
			// 项数量
			int nItemNum = m_nRow * m_nColumn;
			
			// 网格数量
			int nGridNum = (int)((float)nSize / nItemNum) + 1;
			
			// Apps 网格片段适配器类
			FragmentManager fm = m_activity.getSupportFragmentManager();
			m_adapter = new AppsGridFragmentAdapter(fm, m_listApps);
			
			// 设置数量
			if (m_adapter != null) m_adapter.setItemNum(nItemNum);
			if (m_adapter != null) m_adapter.setGridNum(nGridNum);
			
			// 视图页
			m_vp = (ViewPager)m_view.findViewById(R.id.vpApps);
			
			// 设置适配器
			if (m_vp != null && m_adapter != null) m_vp.setAdapter(m_adapter);
			
			// 视图页指示器
			m_vpi = (ViewPagerIndicator)m_view.findViewById(R.id.vpiApps);
			
			// 设置视图页
			if (m_vp != null && m_vpi != null) m_vpi.setViewPager(m_vp);*/
		}
		catch (Exception e)
		{
		}
	}
}
