package com.chinafeisite.tianbu;

import java.util.List;
//import java.util.ArrayList;

import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Apps 网格片段适配器类
 */
public class AppsGridFragmentAdapter extends FragmentPagerAdapter
{
	private final String TAG = "AppsGridFragmentAdapter";

	// 项数量
	private int m_nItemNum = 0;
	public void setItemNum(final int n) { m_nItemNum = n; }
	
	// 网格数量
	private int m_nGridNum = 0;
	public void setGridNum(final int n) { m_nGridNum = n; }

	// Apps 列表
	private List<ResolveInfo> m_listApps = null;
	public void setListApps(List<ResolveInfo> list) { m_listApps = list; }

	private List<Fragment> fragments; 
	private FragmentManager FM;
	/**
	 * 构造函数
	 * @param fm
	 */
	public AppsGridFragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	/**
	 * 构造函数
	 * @param fm
	 * @param listApps
	 */
	public AppsGridFragmentAdapter(FragmentManager fm, List<ResolveInfo> listApps)
	{
		this(fm);
		
		// Apps 列表
		m_listApps = listApps;
//		theApp.MYSLog("AppsVpiLayout","2222"+ m_listApps);
	}
	
	public void setFragments(List<Fragment> fragments) {
		if(this.fragments != null){
			FragmentTransaction ft = FM.beginTransaction();
			for(Fragment f:this.fragments){
				ft.remove(f);
			}
			ft.commit();
			ft=null;
			FM.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
//		theApp.MYSLog("AppsVpiLayout","3333"+ m_nGridNum);
		return m_nGridNum;
	}

	@Override
	public Fragment getItem(int position)
	{
		try
		{
//			theApp.MYSLog("AppsVpiLayout","position"+ position);
			if (m_listApps == null) return null;
			
			// 数量
			int nSize = m_listApps.size();
			if (nSize < 1) return null;

			// 起始和结束索引
			int nStart = m_nItemNum * position;
			int nEnd = nStart + (m_nItemNum - 1);
//			theApp.MYSLog("AppsVpiLayout","m_nItemNum"+ m_nItemNum);
//			theApp.MYSLog("AppsVpiLayout","position"+ position);
			if (nStart < 0 || nStart >= nSize) nStart = 0;
			if (nEnd < 0 || nEnd <= nStart || nEnd >= nSize) nEnd = nSize - 1;
//			theApp.MYSLog("AppsVpiLayout","nStart"+ nStart);
//			theApp.MYSLog("AppsVpiLayout","nEnd"+ nEnd);
			
			// Apps 网格片段
//			final AppsGridFragment fragment;
//			fragment = new AppsGridFragment(m_listApps, nStart, nEnd);
//			return fragment;

			// Apps 网格片段
			return AppsGridFragment.newInstance(m_listApps, nStart, nEnd);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		
		return null;
	}
}
