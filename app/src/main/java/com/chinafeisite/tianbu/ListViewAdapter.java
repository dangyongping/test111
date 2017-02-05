package com.chinafeisite.tianbu;

import java.util.List;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 列表视图适配器
 */
public class ListViewAdapter extends BaseAdapter
{
	private final String TAG = "ListViewAdapter";
	
	// 位图
	private Bitmap m_bmpOn = null;
	private Bitmap m_bmpOff = null;
	
	// 布局泵
	private LayoutInflater m_layInflater = null;

	// 选择项
	private int m_nSelectItem = -1;
	public void setSelectItem(final int n) { m_nSelectItem = n; }
	
	// 列表
	private List<String> m_list = null;
	public void setList(List<String> list) { m_list = list; }
	
	/**
	 * 构造函数
	 * @param context
	 * @param list
	 */
	public ListViewAdapter(Context context, List<String> list)
	{
		try
		{
			// 数据列表
			m_list = list;
			
			// 布局泵
			m_layInflater = LayoutInflater.from(context);
			
			// 位图
			Resources res = context.getResources();
			m_bmpOn  = BitmapFactory.decodeResource(res, R.drawable.btn_radio_on);
			m_bmpOff = BitmapFactory.decodeResource(res, R.drawable.btn_radio_off);
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
			if (m_list != null) return m_list.size();
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
			if (m_list != null) return m_list.get(position);
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
			final RowView rowView;
			
			if (convertView != null)
			{
				// 行视图
				rowView = (RowView)convertView.getTag();
			}
			else
			{
				// 加载列表行
				convertView = m_layInflater.inflate(R.layout.list_row, null);

				// 行视图
				rowView = new RowView();
				rowView.setTxtName((TextView)convertView.findViewById(R.id.txtName));
				rowView.setImgRadio((ImageView)convertView.findViewById(R.id.imgRadio));
				rowView.setLayRow((LinearLayout)convertView.findViewById(R.id.layRow));

				// 设置列表行
				convertView.setTag(rowView);
			}
			
			// 文件名
			rowView.getTxtName().setText(m_list.get(position));
			
			if (position == m_nSelectItem)
			{
				// 选中
				rowView.getTxtName().setTextColor(Color.WHITE);//xwx
				rowView.getImgRadio().setImageBitmap(m_bmpOn);
				rowView.getTxtName().getPaint().setFakeBoldText(true);
				rowView.getLayRow().setBackgroundResource(R.color.item_bg);
				Log.d(TAG, String.format("m_list.get(%d): %s", position, m_list.get(position)));
			}
			else
			{
				// 未选中
				rowView.getTxtName().setTextColor(Color.WHITE);//xwx
				rowView.getImgRadio().setImageBitmap(m_bmpOff);
				rowView.getTxtName().getPaint().setFakeBoldText(false);
				rowView.getLayRow().setBackgroundColor(Color.TRANSPARENT);
			}
		}
		catch (Exception e)
		{
		}
		
		return convertView;
	}

	/**
	 * 行视图类
	 */
	private class RowView
	{
		// 文件名
		private TextView m_txtName = null;
		public TextView getTxtName() { return m_txtName; }
		public void setTxtName(TextView txt) { m_txtName = txt; }
		
		// 单选按钮
		private ImageView m_imgRadio = null;
		public ImageView getImgRadio() { return m_imgRadio; }
		public void setImgRadio(ImageView img) { m_imgRadio = img; }
		
		// 行布局
		private LinearLayout m_layRow = null;
		public LinearLayout getLayRow() { return m_layRow; }
		public void setLayRow(LinearLayout lay) { m_layRow = lay; }
	}
}
