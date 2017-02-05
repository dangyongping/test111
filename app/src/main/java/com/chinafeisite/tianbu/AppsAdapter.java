package com.chinafeisite.tianbu;

import java.util.List;


import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Apps 适配器类
 */
public class AppsAdapter extends BaseAdapter
{
	private final String TAG = "AppsAdapter";

	// Activity 的 Context
	private Context m_context = null;

	// 布局泵
	private LayoutInflater m_layInflater = null;

	// Apps 列表
	private List<ResolveInfo> m_listApps = null;

	/**
	 * 构造函数
	 * @param context
	 * @param listApps
	 */
	public AppsAdapter(Context context, List<ResolveInfo> listApps)
	{
		try
		{
			// Activity 的 Context
			m_context = context;

			// Apps 列表
			m_listApps = listApps;

			// 布局泵
			m_layInflater = LayoutInflater.from(context);
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public int getCount()
	{
		try
		{
			return m_listApps.size();
		}
		catch (Exception e)
		{
		}

		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		try
		{
			return m_listApps.get(position);
		}
		catch (Exception e)
		{
		}

		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		try
		{
			final ItemView itemView;

			if (convertView != null)
			{
				// 项视图
				itemView = (ItemView) convertView.getTag();
			}
			else
			{
				// 加载网格项
				convertView = m_layInflater.inflate(R.layout.item_grid_app, null);

				// 项视图
				itemView = new ItemView();
				itemView.setImgIcon((ImageView) convertView.findViewById(R.id.imgIcon));
				itemView.setTxtLabel((TextView) convertView.findViewById(R.id.txtLabel));

				// 设置网格项
				convertView.setTag(itemView);
			}

			// App 信息
			ResolveInfo info = m_listApps.get(position);

			// 设置 App 图标
			Drawable icon = info.activityInfo.loadIcon(m_context.getPackageManager());
			itemView.getImgIcon().setImageDrawable(icon);

			// 设置 App 标签
			CharSequence label = info.activityInfo.loadLabel(m_context.getPackageManager());
			itemView.getTxtLabel().setText(label);
//			itemView.getTxtLabel().getPaint().setFakeBoldText(true);

			return convertView;
		}
		catch (Exception e)
		{
			Log.d(TAG, String.format("getView: %s", e));
		}

		return null;
	}

	/**
	 * 项视图类
	 */
	private class ItemView
	{
		// 图标
		private ImageView m_imgIcon = null;
		public ImageView getImgIcon() { return m_imgIcon; }
		public void setImgIcon(ImageView img) { m_imgIcon = img; }

		// 标签
		private TextView m_txtLabel = null;
		public TextView getTxtLabel() { return m_txtLabel; }
		public void setTxtLabel(TextView txt) { m_txtLabel = txt; }
	}
}
