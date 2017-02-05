package com.chinafeisite.tianbu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 列表项适配器
 */
public class ListItemAdapter extends BaseAdapter
{
	private final String TAG = "ListItemAdapter";

	// 旧的加载位置
	private int m_nLoadPosOld = -1;
	
	// 布局泵
	private LayoutInflater m_layInflater = null;
	
	// 选择项
	private int m_nSelectItem = -1;
	public void setSelectItem(final int n) { m_nSelectItem = n; }
	
	// 图像加载监听器
	private ImageLoadingListener m_listener = new AnimateFirstDisplayListener();
	
	// 显示图像参数
	private DisplayImageOptions m_options = null;
	public void setOptions(DisplayImageOptions options) { m_options = options; }

	// 图像 URL
	private String[] m_strImageUrls = null;
	public void setImageUrls(String[] strImageUrls) { m_strImageUrls = strImageUrls; }
	
	// 图像加载器
	private ImageLoader m_imageLoader = null;
	public void setImageLoader(ImageLoader imageLoader) { m_imageLoader = imageLoader; }
	
	/**
	 * 构造函数
	 * @param context
	 */
	public ListItemAdapter(Context context)
	{
		try
		{
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
			return m_strImageUrls.length;
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
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
			String strFileName = m_strImageUrls[position];

			// 最后一个反斜杠的位置
			int nPos = strFileName.lastIndexOf("/") + 1;
			if (nPos <= 0 || nPos >= strFileName.length()) strFileName = String.format("Item %d", position + 1);
			
			// 截取文件名
			strFileName = strFileName.substring(nPos);
			
			// 文件名
			rowView.getTxtName().setText(strFileName);
			
			String strImageUrl = "drawable://";
			
			if (position == m_nSelectItem)
			{
				// 选中
				strImageUrl += R.drawable.btn_radio_on;
				rowView.getTxtName().getPaint().setFakeBoldText(true);
				rowView.getLayRow().setBackgroundColor(Color.LTGRAY);
//				Log.d(TAG, String.format("m_strImageUrls[%d]: %s", position, m_strImageUrls[position]));
			}
			else
			{
				// 未选中
				strImageUrl += R.drawable.btn_radio_off;
				rowView.getTxtName().getPaint().setFakeBoldText(false);
				rowView.getLayRow().setBackgroundResource(R.color.item_bg);
			}

			if (m_imageLoader != null)
			{
				// 检测加载位置
				if (m_nLoadPosOld != position)
				{
					// 加载位置
					m_nLoadPosOld = position;

					// 显示图像
					m_imageLoader.displayImage(strImageUrl, rowView.getImgRadio(), m_options, m_listener);
				}
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
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

	/**
	 * 图像加载监听器
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
	{
		// 图像链表
		static final List<String> m_listImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
		{
			if (loadedImage != null)
			{
				ImageView imageView = (ImageView)view;
				boolean bFirstDisplay = !m_listImages.contains(imageUri);
				
				if (bFirstDisplay)
				{
					FadeInBitmapDisplayer.animate(imageView, 100);
					m_listImages.add(imageUri);
				}
			}
		}
	}
}
