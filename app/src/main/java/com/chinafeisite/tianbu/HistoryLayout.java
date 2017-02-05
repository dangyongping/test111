package com.chinafeisite.tianbu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 历史记录布局
 */
public class HistoryLayout extends RelativeLayout implements OnClickListener
{
	private final String TAG = "HistoryLayout";

	// 视图
	private View m_view = null;
	
	// Activity 的 Context
	private Context m_context = null;

	// 日期
	private int  m_nYear   = 0; // 当前年份
	private int  m_nMonth  = 0; // 当前月份
	private int  m_nDay    = 0; // 当天日期
	private int  m_nWeek   = 0; // 当天星期
	private int  m_nWeeks  = 0; // 当前是本年第几周
	private long m_lMonth1 = 0; // 本月 1 日的时间
	private long m_lMonday = 0; // 本周一的时间
	private long m_lNextMd = 0; // 下周一的时间
	private long m_lNextMt = 0; // 下月 1 日的时间

	// 周一的时间链表
	private List<Long> m_listMonday = new ArrayList<Long>();
	
	// 每月 1 日的时间
	private List<Long> m_listMonth1 = new ArrayList<Long>();
	
	// 周目标
	private float m_fWeekTgt = 0;
	
	// 字符串
	private String m_strHour     = "时";
	private String m_strWeek     = "周";
	private String m_strMonth    = "月";
	private String m_strYear     = "年";
	private String m_strTarget   = "目标";
	private String m_strCompare  = "比较";
	private String m_strThisTime = "本次";
	private String m_strAverTime = "平均/次";
	private String m_strLastTime = "上次";
	private String m_strBestTime = "最佳次";
	private String m_strThisWeek = "本周";
	private String m_strAverWeek = "平均/周";
	private String m_strLastWeek = "上周";
	private String m_strBestWeek = "最佳周";
	private String m_strThisMont = "本月";
	private String m_strAverMont = "平均/月";
	private String m_strLastMont = "上月";
	private String m_strBestMont = "最佳月";
	private String m_strThisYear = "本年";

	// 按钮
	private Button m_btnDist1 = null;
	private Button m_btnDist2 = null;
	private Button m_btnCalo1 = null;
	private Button m_btnCalo2 = null;
	private Button m_btnStep1 = null;
	private Button m_btnStep2 = null;
	private Button m_btnTime1 = null;
	private Button m_btnTime2 = null;

	// 视图
	private HourView m_distHourView = null;
	private WeekView m_distWeekView = null;
	private MontView m_distMontView = null;
	private YearView m_distYearView = null;
	private HourComp m_distHourComp = null;
	private WeekComp m_distWeekComp = null;
	private MontComp m_distMontComp = null;
	private YearComp m_distYearComp = null;
	private HourView m_caloHourView = null;
	private WeekView m_caloWeekView = null;
	private MontView m_caloMontView = null;
	private YearView m_caloYearView = null;
	private HourComp m_caloHourComp = null;
	private WeekComp m_caloWeekComp = null;
	private MontComp m_caloMontComp = null;
	private YearComp m_caloYearComp = null;
	private HourView m_stepHourView = null;
	private WeekView m_stepWeekView = null;
	private MontView m_stepMontView = null;
	private YearView m_stepYearView = null;
	private HourComp m_stepHourComp = null;
	private WeekComp m_stepWeekComp = null;
	private MontComp m_stepMontComp = null;
	private YearComp m_stepYearComp = null;
	private HourView m_timeHourView = null;
	private WeekView m_timeWeekView = null;
	private MontView m_timeMontView = null;
	private YearView m_timeYearView = null;
	private HourComp m_timeHourComp = null;
	private WeekComp m_timeWeekComp = null;
	private MontComp m_timeMontComp = null;
	private YearComp m_timeYearComp = null;
	
	// 视图布局
	private LinearLayout m_layView11 = null;
	private LinearLayout m_layView12 = null;
	private LinearLayout m_layView13 = null;
	private LinearLayout m_layView14 = null;
	private LinearLayout m_layView21 = null;
	private LinearLayout m_layView22 = null;
	private LinearLayout m_layView23 = null;
	private LinearLayout m_layView24 = null;
	
	// 数据
	private static int ms_nData = HistoryLayout.DATA_LOCA;
	private static int ms_nPrevData = HistoryLayout.DATA_LOCA;
	public static void setData(final int n) { ms_nData = n; }
	public static int getPrevData() { return ms_nPrevData; }
	
	// 模式
	private static int ms_nMode = HistoryLayout.MODE_DIST;
	
	// 最大最小值
	private static final float MAX = Float.MAX_VALUE;
	private static final float MIN = Float.MIN_VALUE;
	
	// 数据
	public static final int DATA_LOCA = 0; // 本地
	public static final int DATA_SERV = 1; // 服务器
	
	// 模式
	private static final int MODE_DIST = 0; // 距离
	private static final int MODE_CALO = 1; // 卡路里
	private static final int MODE_STEP = 2; // 步数
	private static final int MODE_TIME = 3; // 时间
	
	/**
	 * 构造函数
	 * @param context
	 */
	public HistoryLayout(Context context)
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
	public HistoryLayout(Context context, AttributeSet attrs)
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
	public HistoryLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	public void onClick(View view)
	{
		try
		{
			// 距离
			if (view.getId() == R.id.btnDist1 || view.getId() == R.id.btnDist2)
			{
				ms_nMode = MODE_DIST;
			}
			
			// 卡路里
			if (view.getId() == R.id.btnCalorie1 || view.getId() == R.id.btnCalorie2)
			{
				ms_nMode = MODE_CALO;
			}
			
			// 步数
			if (view.getId() == R.id.btnStep1 || view.getId() == R.id.btnStep2)
			{
				ms_nMode = MODE_STEP;
			}
			
			// 时间
			if (view.getId() == R.id.btnTime1 || view.getId() == R.id.btnTime2)
			{
				ms_nMode = MODE_TIME;
			}
			
			// 设置模式
			setMode();
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
			// 前一种数据
			ms_nPrevData = ms_nData;
			
			// Activity 的 Context
			m_context = context;

			// 从布局文件中加载视图
			m_view = LayoutInflater.from(context).inflate(R.layout.layout_history, null);

			// 添加视图
			addView(m_view, new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			
			// 字符串
			m_strHour     = m_context.getString(R.string.history_hour);
			m_strWeek     = m_context.getString(R.string.history_week);
			m_strMonth    = m_context.getString(R.string.history_month);
			m_strYear     = m_context.getString(R.string.history_year);
			m_strTarget   = m_context.getString(R.string.history_target);
			m_strCompare  = m_context.getString(R.string.history_compare);
			m_strThisTime = m_context.getString(R.string.history_this_time);
			m_strAverTime = m_context.getString(R.string.history_average_time);
			m_strLastTime = m_context.getString(R.string.history_last_time);
			m_strBestTime = m_context.getString(R.string.history_best_time);
			m_strThisWeek = m_context.getString(R.string.history_this_week);
			m_strAverWeek = m_context.getString(R.string.history_average_week);
			m_strLastWeek = m_context.getString(R.string.history_last_week);
			m_strBestWeek = m_context.getString(R.string.history_best_week);
			m_strThisMont = m_context.getString(R.string.history_this_month);
			m_strAverMont = m_context.getString(R.string.history_average_month);
			m_strLastMont = m_context.getString(R.string.history_last_month);
			m_strBestMont = m_context.getString(R.string.history_best_month);
			m_strThisYear = m_context.getString(R.string.history_this_year);
			
			// 按钮
			m_btnDist1 = (Button)m_view.findViewById(R.id.btnDist1);
			m_btnDist2 = (Button)m_view.findViewById(R.id.btnDist2);
			m_btnCalo1 = (Button)m_view.findViewById(R.id.btnCalorie1);
			m_btnCalo2 = (Button)m_view.findViewById(R.id.btnCalorie2);
			m_btnStep1 = (Button)m_view.findViewById(R.id.btnStep1);
			m_btnStep2 = (Button)m_view.findViewById(R.id.btnStep2);
			m_btnTime1 = (Button)m_view.findViewById(R.id.btnTime1);
			m_btnTime2 = (Button)m_view.findViewById(R.id.btnTime2);
			m_btnDist1.setOnClickListener(this);
			m_btnDist2.setOnClickListener(this);
			m_btnCalo1.setOnClickListener(this);
			m_btnCalo2.setOnClickListener(this);
			m_btnStep1.setOnClickListener(this);
			m_btnStep2.setOnClickListener(this);
			m_btnTime1.setOnClickListener(this);
			m_btnTime2.setOnClickListener(this);
			
			// 视图布局
			m_layView11 = (LinearLayout)m_view.findViewById(R.id.layView11);
			m_layView12 = (LinearLayout)m_view.findViewById(R.id.layView12);
			m_layView13 = (LinearLayout)m_view.findViewById(R.id.layView13);
			m_layView14 = (LinearLayout)m_view.findViewById(R.id.layView14);
			m_layView21 = (LinearLayout)m_view.findViewById(R.id.layView21);
			m_layView22 = (LinearLayout)m_view.findViewById(R.id.layView22);
			m_layView23 = (LinearLayout)m_view.findViewById(R.id.layView23);
			m_layView24 = (LinearLayout)m_view.findViewById(R.id.layView24);
			
			// 设置模式
			setMode();
			
			// 宽度和高度比例
			float fWR = theApp.getWidthRatio();
			float fHR = theApp.getHeightRatio();
			
			LinearLayout.LayoutParams params =
					new LinearLayout.LayoutParams((int)(64 * fWR), (int)(64 * fHR));
			params.setMargins((int)(16 * fWR), (int)(16 * fHR), (int)(16 * fWR), (int)(16 * fHR));
			m_btnDist1.setLayoutParams(params);
			m_btnDist2.setLayoutParams(params);
			m_btnCalo1.setLayoutParams(params);
			m_btnCalo2.setLayoutParams(params);
			m_btnStep1.setLayoutParams(params);
			m_btnStep2.setLayoutParams(params);
			m_btnTime1.setLayoutParams(params);
			m_btnTime2.setLayoutParams(params);

			// 获取日期
			if (m_nYear <= 0) getDate();
			
			// 获取周目标
			if (m_fWeekTgt <= 0) getWeekTgt();
		}
		catch (Exception e)
		{
			Log.d(TAG, "init: %s", e);
		}
	}
	
	/**
	 * 获取日期
	 */
	private void getDate()
	{
		try
		{
			// 日期
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			m_nYear  = calendar.get(Calendar.YEAR);
			m_nMonth = calendar.get(Calendar.MONTH) + 1;
			m_nDay   = calendar.get(Calendar.DAY_OF_MONTH);
			m_nWeek  = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			m_nWeek = (m_nWeek == 0) ? 7 : m_nWeek;
//			Log.d(TAG, String.format("Week: %d", m_nWeek));
			
			// 下周一的时间
			m_lNextMd = UserData.getNextMonday();
			
			// 下月 1 日的时间
			m_lNextMt = UserData.getNextMonth1();
			
			// 获取当前是本年第几周
			m_nWeeks = UserData.getWeeks();
			
			// 获取本年 1 月 1 日的时间
			long lYear1 = UserData.getYear1();
			
			// 周一的时间链表
			m_listMonday.clear();
			
			// 每月 1 日的时间
			m_listMonth1.clear();
			
			for (int i = m_nWeeks - 1; i > 0; i--)
			{
				// 获取上周星期一的时间
				long m1 = UserData.getPrevWeek1(i);
				if (m1 < lYear1) m1 = lYear1;
				if (i != m_nWeeks - 1 && m1 == lYear1) continue;
				m_listMonday.add(m1);
			}
			
			for (int i = m_nMonth - 1; i > 0; i--)
			{
				// 获取上月 1 日的时间
				long m1 = UserData.getPrevMonth1(i);
				m_listMonth1.add(m1);
			}
			
			// 本周一的时间
			m_lMonday = UserData.getMonday();
			m_listMonday.add(m_lMonday);
			
			// 本月 1 日的时间
			m_lMonth1 = UserData.getMonth1();
			m_listMonth1.add(m_lMonth1);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 获取周目标
	 */
	private void getWeekTgt()
	{
		try
		{
			// 本地
			List<String> listUser = theApp.getListUserL();
			
			// 服务器
			if (ms_nData == DATA_SERV)
			{
				listUser = theApp.getListUserS();
			}
			
			// 用户编号, 00: 家庭访客
			String strUser = theApp.i2Str(theApp.getAccType(), 2);
			
			// 数量
			int nIndex = -1;
			int nSize = listUser.size();
			
			for (int i = 0; i < nSize; i++)
			{
				// 检测用户数据
				if (listUser.get(i).startsWith(strUser))
				{
					nIndex = i;
					break;
				}
			}
			
			if (nIndex >= 0 && nIndex < nSize)
			{
				List<String> strArray = new ArrayList<String>();

				// 分割字符串
				int nSizeStr = theApp.splitString(listUser.get(nIndex), ",", strArray);
				
				if (nSizeStr >= 5)
				{
					// 周目标
					String strWeekTgt = strArray.get(4);
					m_fWeekTgt = theApp.str2int(strWeekTgt);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 设置模式
	 */
	private void setMode()
	{
		try
		{
			// 距离
			if (ms_nMode == MODE_DIST)
			{
				m_btnDist1.setVisibility(View.GONE);
				m_btnDist2.setVisibility(View.VISIBLE);
				m_btnCalo1.setVisibility(View.VISIBLE);
				m_btnCalo2.setVisibility(View.GONE);
				m_btnStep1.setVisibility(View.VISIBLE);
				m_btnStep2.setVisibility(View.GONE);
				m_btnTime1.setVisibility(View.VISIBLE);
				m_btnTime2.setVisibility(View.GONE);
			}
			
			// 卡路里
			if (ms_nMode == MODE_CALO)
			{
				m_btnDist1.setVisibility(View.VISIBLE);
				m_btnDist2.setVisibility(View.GONE);
				m_btnCalo1.setVisibility(View.GONE);
				m_btnCalo2.setVisibility(View.VISIBLE);
				m_btnStep1.setVisibility(View.VISIBLE);
				m_btnStep2.setVisibility(View.GONE);
				m_btnTime1.setVisibility(View.VISIBLE);
				m_btnTime2.setVisibility(View.GONE);
			}
			
			// 步数
			if (ms_nMode == MODE_STEP)
			{
				m_btnDist1.setVisibility(View.VISIBLE);
				m_btnDist2.setVisibility(View.GONE);
				m_btnCalo1.setVisibility(View.VISIBLE);
				m_btnCalo2.setVisibility(View.GONE);
				m_btnStep1.setVisibility(View.GONE);
				m_btnStep2.setVisibility(View.VISIBLE);
				m_btnTime1.setVisibility(View.VISIBLE);
				m_btnTime2.setVisibility(View.GONE);
			}
			
			// 时间
			if (ms_nMode == MODE_TIME)
			{
				m_btnDist1.setVisibility(View.VISIBLE);
				m_btnDist2.setVisibility(View.GONE);
				m_btnCalo1.setVisibility(View.VISIBLE);
				m_btnCalo2.setVisibility(View.GONE);
				m_btnStep1.setVisibility(View.VISIBLE);
				m_btnStep2.setVisibility(View.GONE);
				m_btnTime1.setVisibility(View.GONE);
				m_btnTime2.setVisibility(View.VISIBLE);
			}
			
			// 布局视图
			layoutView();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 布局视图
	 */
	private void layoutView()
	{
		try
		{
			// 距离
			if (ms_nMode == MODE_DIST)
			{
				// 视图
				if (m_distHourView == null) m_distHourView = new HourView(m_context, ms_nMode);
				if (m_distWeekView == null) m_distWeekView = new WeekView(m_context, ms_nMode);
				if (m_distMontView == null) m_distMontView = new MontView(m_context, ms_nMode);
				if (m_distYearView == null) m_distYearView = new YearView(m_context, ms_nMode);
				if (m_distHourComp == null) m_distHourComp = new HourComp(m_context, ms_nMode);
				if (m_distWeekComp == null) m_distWeekComp = new WeekComp(m_context, ms_nMode);
				if (m_distMontComp == null) m_distMontComp = new MontComp(m_context, ms_nMode);
				if (m_distYearComp == null) m_distYearComp = new YearComp(m_context, ms_nMode);
				
				// 视图布局
				m_layView11.removeAllViews();
				m_layView12.removeAllViews();
				m_layView13.removeAllViews();
				m_layView14.removeAllViews();
				m_layView21.removeAllViews();
				m_layView22.removeAllViews();
				m_layView23.removeAllViews();
				m_layView24.removeAllViews();
				m_layView11.addView(m_distHourView);
				m_layView12.addView(m_distWeekView);
				m_layView13.addView(m_distMontView);
				m_layView14.addView(m_distYearView);
				m_layView21.addView(m_distHourComp);
				m_layView22.addView(m_distWeekComp);
				m_layView23.addView(m_distMontComp);
				m_layView24.addView(m_distYearComp);
				
				// 刷新
				m_distHourView.invalidate();
				m_distWeekView.invalidate();
				m_distMontView.invalidate();
				m_distYearView.invalidate();
				m_distHourComp.invalidate();
				m_distWeekComp.invalidate();
				m_distMontComp.invalidate();
				m_distYearComp.invalidate();
			}
			
			// 卡路里
			if (ms_nMode == MODE_CALO)
			{
				// 视图
				if (m_caloHourView == null) m_caloHourView = new HourView(m_context, ms_nMode);
				if (m_caloWeekView == null) m_caloWeekView = new WeekView(m_context, ms_nMode);
				if (m_caloMontView == null) m_caloMontView = new MontView(m_context, ms_nMode);
				if (m_caloYearView == null) m_caloYearView = new YearView(m_context, ms_nMode);
				if (m_caloHourComp == null) m_caloHourComp = new HourComp(m_context, ms_nMode);
				if (m_caloWeekComp == null) m_caloWeekComp = new WeekComp(m_context, ms_nMode);
				if (m_caloMontComp == null) m_caloMontComp = new MontComp(m_context, ms_nMode);
				if (m_caloYearComp == null) m_caloYearComp = new YearComp(m_context, ms_nMode);
				
				// 视图布局
				m_layView11.removeAllViews();
				m_layView12.removeAllViews();
				m_layView13.removeAllViews();
				m_layView14.removeAllViews();
				m_layView21.removeAllViews();
				m_layView22.removeAllViews();
				m_layView23.removeAllViews();
				m_layView24.removeAllViews();
				m_layView11.addView(m_caloHourView);
				m_layView12.addView(m_caloWeekView);
				m_layView13.addView(m_caloMontView);
				m_layView14.addView(m_caloYearView);
				m_layView21.addView(m_caloHourComp);
				m_layView22.addView(m_caloWeekComp);
				m_layView23.addView(m_caloMontComp);
				m_layView24.addView(m_caloYearComp);
				
				// 刷新
				m_caloHourView.invalidate();
				m_caloWeekView.invalidate();
				m_caloMontView.invalidate();
				m_caloYearView.invalidate();
				m_caloHourComp.invalidate();
				m_caloWeekComp.invalidate();
				m_caloMontComp.invalidate();
				m_caloYearComp.invalidate();
			}
			
			// 步数
			if (ms_nMode == MODE_STEP)
			{
				// 视图
				if (m_stepHourView == null) m_stepHourView = new HourView(m_context, ms_nMode);
				if (m_stepWeekView == null) m_stepWeekView = new WeekView(m_context, ms_nMode);
				if (m_stepMontView == null) m_stepMontView = new MontView(m_context, ms_nMode);
				if (m_stepYearView == null) m_stepYearView = new YearView(m_context, ms_nMode);
				if (m_stepHourComp == null) m_stepHourComp = new HourComp(m_context, ms_nMode);
				if (m_stepWeekComp == null) m_stepWeekComp = new WeekComp(m_context, ms_nMode);
				if (m_stepMontComp == null) m_stepMontComp = new MontComp(m_context, ms_nMode);
				if (m_stepYearComp == null) m_stepYearComp = new YearComp(m_context, ms_nMode);
				
				// 视图布局
				m_layView11.removeAllViews();
				m_layView12.removeAllViews();
				m_layView13.removeAllViews();
				m_layView14.removeAllViews();
				m_layView21.removeAllViews();
				m_layView22.removeAllViews();
				m_layView23.removeAllViews();
				m_layView24.removeAllViews();
				m_layView11.addView(m_stepHourView);
				m_layView12.addView(m_stepWeekView);
				m_layView13.addView(m_stepMontView);
				m_layView14.addView(m_stepYearView);
				m_layView21.addView(m_stepHourComp);
				m_layView22.addView(m_stepWeekComp);
				m_layView23.addView(m_stepMontComp);
				m_layView24.addView(m_stepYearComp);
				
				// 刷新
				m_stepHourView.invalidate();
				m_stepWeekView.invalidate();
				m_stepMontView.invalidate();
				m_stepYearView.invalidate();
				m_stepHourComp.invalidate();
				m_stepWeekComp.invalidate();
				m_stepMontComp.invalidate();
				m_stepYearComp.invalidate();
			}
			
			// 时间
			if (ms_nMode == MODE_TIME)
			{
				// 视图
				if (m_timeHourView == null) m_timeHourView = new HourView(m_context, ms_nMode);
				if (m_timeWeekView == null) m_timeWeekView = new WeekView(m_context, ms_nMode);
				if (m_timeMontView == null) m_timeMontView = new MontView(m_context, ms_nMode);
				if (m_timeYearView == null) m_timeYearView = new YearView(m_context, ms_nMode);
				if (m_timeHourComp == null) m_timeHourComp = new HourComp(m_context, ms_nMode);
				if (m_timeWeekComp == null) m_timeWeekComp = new WeekComp(m_context, ms_nMode);
				if (m_timeMontComp == null) m_timeMontComp = new MontComp(m_context, ms_nMode);
				if (m_timeYearComp == null) m_timeYearComp = new YearComp(m_context, ms_nMode);
				
				// 视图布局
				m_layView11.removeAllViews();
				m_layView12.removeAllViews();
				m_layView13.removeAllViews();
				m_layView14.removeAllViews();
				m_layView21.removeAllViews();
				m_layView22.removeAllViews();
				m_layView23.removeAllViews();
				m_layView24.removeAllViews();
				m_layView11.addView(m_timeHourView);
				m_layView12.addView(m_timeWeekView);
				m_layView13.addView(m_timeMontView);
				m_layView14.addView(m_timeYearView);
				m_layView21.addView(m_timeHourComp);
				m_layView22.addView(m_timeWeekComp);
				m_layView23.addView(m_timeMontComp);
				m_layView24.addView(m_timeYearComp);
				
				// 刷新
				m_timeHourView.invalidate();
				m_timeWeekView.invalidate();
				m_timeMontView.invalidate();
				m_timeYearView.invalidate();
				m_timeHourComp.invalidate();
				m_timeWeekComp.invalidate();
				m_timeMontComp.invalidate();
				m_timeYearComp.invalidate();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 获取最小最大值和间隔
	 * @param max
	 * @return
	 */
	private float[] getMinMaxInter(final float min, final float max)
	{
		try
		{
			int n = 1;
			int nMax = 10;
			if (max <= 0) return null;
			if (min > max) return null;
			
			long lMax = (long)max;
			String strMax = String.valueOf(lMax);
			
			int nLen = strMax.length();
			if (nLen < 1) return null;

			float[] value = new float[3];
			
			float base = (float)Math.pow(10, nLen - 1);

			for (int i = 1; i <= 10; i++)
			{
				if (max > base * i)
				{
					n = (int)(base / 10) * i;
					n = (n < 1) ? 1 : n;
					break;
				}
			}

			for (int i = n; i <= 1000000; )
			{
				boolean b = false;
				
				if (!b && 0 <= min && max <= 5 * i)
				{
					b = true;
					
					value[0] = 0;
					value[1] = 5 * i;
					value[2] = 1 * i;
				}
				
				if (!b && 5 * i <= min && max <= 10 * i)
				{
					b = true;
					
					value[0] =  5 * i;
					value[1] = 10 * i;
					value[2] =  1 * i;
				}
				
				if (!b && 0 <= min && max <= 10 * i)
				{
					b = true;
					
					value[0] =  0;
					value[1] = 10 * i;
					value[2] =  2 * i;
				}
				
				if (b)
				{
					String strValue2 = String.valueOf((long)value[2]);
					int nLen2 = strValue2.length();

					int nValue2 = Integer.valueOf(strValue2);
					
					if (nLen2 == 1)
					{
						if (2 < nValue2 && nValue2 < 5)
						{
							nValue2 = 5;
						}
					}
					
					if (nLen2 > 1)
					{
						strValue2 = strValue2.substring(0, 1);

						nValue2 = Integer.valueOf(strValue2);
						
						if (1 < nValue2 && nValue2 < 5)
						{
							nValue2 = 1;
						}
						
						if (5 <= nValue2 && nValue2 < 10)
						{
							nValue2 = 10;
						}
					}
					
					strValue2 = String.valueOf(nValue2);
					
					for (int j = 0; j < nLen2 - 1; j++)
					{
						strValue2 += "0";
					}
					
					value[2] = Float.valueOf(strValue2);
					
					if (value[2] >= value[1])
					{
						value[2] /= 10.0f;
					}
					
					float delta = value[1] - value[0];
					
					if (delta > 0 && delta >= value[1] && delta > value[2])
					{
						n = (int)(delta / value[2]);
						
						float maxV = value[2] * (n + 1);
						
						if (value[1] < maxV && maxV < value[1] + value[2])
						{
							value[1] = maxV;
						}
					}
					
					delta = value[1] - value[0];
					
					if (delta > 0 && delta >= value[1] && delta > value[2])
					{
						n = (int)(delta / value[2]);
						
						while (n > nMax)
						{
							value[2] *= 2;
							
							n = (int)(delta / value[2]);
						}
					}
					
					return value;
				}
				
//				Log.d(TAG, String.format("min = %d, max = %d, i = %d", (int)min, (int)max, i));
				
				if (i < 10)
				{
					i += 1;
				}
				else if (i < 100)
				{
					i += 10;
				}
				else if (i < 1000)
				{
					i += 100;
				}
				else if (i < 10000)
				{
					i += 1000;
				}
				else if (i < 100000)
				{
					i += 10000;
				}
				else
				{
					i += 100000;
				}
				
//				Log.d(TAG, String.format("i = %d", i));
			}
			
			return null;
		}
		catch (Exception e)
		{
		}
		
		return null;
	}
	
	/**
	 * 时_视图类
	 */
	private class HourView extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = MAX; // 最小
		
		// 文本大小
		private float m_tsH = 16;
		private float m_ts_ =  8;
		private float m_tsT = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swZ = 3;
		private float m_swT = 3;
		
		// 路径
		private Path m_path = new Path();
		
		// 画笔
		private Paint m_paintTextH = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineZ = new Paint();
		private Paint m_paintLineT = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();
		
		/**
		 * 构造函数
		 * @param context
		 */
		public HourView(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsH *= fHR;
				m_ts_ *= fHR;
				m_tsT *= fHR;

				m_paintTextH.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextH.setColor(Color.BLACK); // 设置颜色
				m_paintTextH.setTextSize(m_tsH);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;
				m_swZ *= fHR;
				m_swT *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineZ.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineZ.setStrokeWidth(m_swZ);            // 设置笔宽
				m_paintLineZ.setStyle(Paint.Style.FILL);       // 填充
				
				m_paintLineT.setColor(Color.GREEN);      // 设置颜色
				m_paintLineT.setStrokeWidth(m_swT);      // 设置笔宽
				m_paintLineT.setStyle(Paint.Style.FILL); // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = m_fWeekTgt * 1000 / 7 / 24;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;
				m_min = (value < m_min) ? value : m_min;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 获取日期
					int nYear  = data.getYear();
					int nMonth = data.getMonth();
					int nDay   = data.getDay();
					
					if (nYear != m_nYear || nMonth != m_nMonth || nDay != m_nDay)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 距离
						double distH = data1.getDist();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 距离
								distH += data2.getDist();
							}
						}
						
						// 本时的距离
						data1.setDistH(distH);
						
						// 值
						value = (float)distH * 1000;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 卡路里
						long caloH = data1.getCalo();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 卡路里
								caloH += data2.getCalo();
							}
						}
						
						// 本时的卡路里
						data1.setCaloH(caloH);
						
						// 值
						value = (float)caloH;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 步数
						long stepH = data1.getStep();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 步数
								stepH += data2.getStep();
							}
						}
						
						// 本时的步数
						data1.setStepH(stepH);
						
						// 值
						value = (float)stepH;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 时间
						long timeH = data1.getTime();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 时间
								timeH += data2.getTime();
							}
						}
						
						// 本时的时间
						data1.setTimeH(timeH);
						
						// 值
						value = (float)timeH;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 时
				float hx = 20 * fWR;
				float hy = 30 * fHR;
				canvas.drawText(m_strHour, hx, hy, m_paintTextH);
				
				// 横坐标轴
				float lx11 = 40 * fWR;
				float ly11 = nH - 20 * fHR;
				float lx12 = nW - 20 * fWR;
				float ly12 = nH - 20 * fHR;
				float ldx1 = (lx12 - lx11) / 24;
				float ldx12 = ldx1 / 2;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				for (int i = 0; i < 24; i++)
				{
					// 小时
					String strHour = String.valueOf(i);
					float tw = m_paintText_.measureText(strHour);
					canvas.drawText(strHour, lx11 + i * ldx1 + ldx12 - tw / 2, ly11 + m_ts_ + m_sw_, m_paintText_);
				}
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 纵坐标
				float lx21 = 20 * fWR;
				float ly21 = nH - 20 * fHR;
				float ly22 = 20 * fHR + m_tsH + 2 * m_ts_;
				float ldy2 = (ly21 - ly22) / nNum;
				
				for (int i = 1; i <= nNum; i++)
				{
					// 纵坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					canvas.drawText(strValue, lx21, ly21 - i * ldy2, m_paintText_);
				}

				float dy = 0;
				float deltaValue = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					deltaValue = m_tgt - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;
					float tx = lx11;
					float ty = ly11 - dy + m_tsT / 2 - m_swT / 2;
					float tx1 = lx11;
					float ty1 = ly11 - dy;
					float tx2 = lx12;
					float ty2 = ly11 - dy;
					float ts2 = m_tsT / 2;
					float ts25 = ts2 * 1.5f;
					float tw = m_paintTextT.measureText(m_strTarget);
					canvas.drawLine(tx1, ty1, tx2, ty2, m_paintLineT);
					m_path.moveTo(tx1 - ts2, ty1 - ts25);
					m_path.lineTo(tx1 + ts2 + tw, ty1 - ts25);
					m_path.lineTo(tx1 + 2.5f * ts2 + tw, ty1);
					m_path.lineTo(tx1 + ts2 + tw, ty1 + ts25);
					m_path.lineTo(tx1 - ts2, ty1 + ts25);
					m_path.close();
					canvas.drawPath(m_path, m_paintLineT);
					canvas.drawText(m_strTarget, tx, ty, m_paintTextT);
				}
				
				int k = 0;
				float lx1 = 0;
				float ly1 = 0;
				float lx2 = 0;
				float ly2 = 0;
				
				// 数量
				int nSize = m_listUserData.size();
				
				for (int i = 0; i < 24; i++)
				{
					UserData data = null;
					
					for (int j = 0; j < nSize; j++)
					{
						data = m_listUserData.get(j);
						
						// 时
						int nHour = data.getHour();
						
						if (nHour == i + 1)
						{
							break;
						}
						
						if (j == nSize - 1)
						{
							data = null;
						}
					}
					
					if (data == null) continue;
					
					k++;
					
					float value = 0;
					
					// 距离
					if (m_nMode == MODE_DIST)
					{
						// 本时的距离
						value = (float)data.getDistH() * 1000;
					}
					
					// 卡路里
					if (m_nMode == MODE_CALO)
					{
						// 本时的卡路里
						value = (float)data.getCaloH();
					}
					
					// 步数
					if (m_nMode == MODE_STEP)
					{
						// 本时的步数
						value = (float)data.getStepH();
					}
					
					// 时间
					if (m_nMode == MODE_TIME)
					{
						// 本时的时间
						value = (float)data.getTimeH();
					}
					
					// 差值
					deltaValue = value - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;

					lx2 = lx11 + i * ldx1 + m_ts_ / 2;
					ly2 = ly11 - dy;
					
					if (k > 1)
					{
						// 连线
						canvas.drawLine(lx1, ly1, lx2, ly2, m_paintLineZ);
					}

					lx1 = lx2;
					ly1 = ly2;
					
					// 坐标点
					canvas.drawCircle(lx2, ly2, m_swZ, m_paintLineZ);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 周_视图类
	 */
	private class WeekView extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = MAX; // 最小
		
		// 文本大小
		private float m_tsW = 16;
		private float m_ts_ = 12;
		private float m_tsT = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swT = 3;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 路径
		private Path m_path = new Path();
		
		// 画笔
		private Paint m_paintTextW = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineT = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();
		
		/**
		 * 构造函数
		 * @param context
		 */
		public WeekView(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsW *= fHR;
				m_ts_ *= fHR;
				m_tsT *= fHR;

				m_paintTextW.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextW.setColor(Color.BLACK); // 设置颜色
				m_paintTextW.setTextSize(m_tsW);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;
				m_swT *= fHR;
				m_swL *= fHR;
				m_swH *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineT.setColor(Color.GREEN);      // 设置颜色
				m_paintLineT.setStrokeWidth(m_swT);      // 设置笔宽
				m_paintLineT.setStyle(Paint.Style.FILL); // 填充
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = m_fWeekTgt / 7;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;
				m_min = (value < m_min) ? value : m_min;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 星期和时间
					int  nWeek = data.getWeek();
					long lTime = data.getCurTime();
					
					if (nWeek > m_nWeek)
					{
						continue;
					}
					
					if (lTime < m_lMonday)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 距离
						double distD = data1.getDist();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 距离
								distD += data2.getDist();
							}
						}
						
						// 本日的距离
						data1.setDistD(distD);
						
						// 值
						value = (float)distD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 卡路里
						long caloD = data1.getCalo();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 卡路里
								caloD += data2.getCalo();
							}
						}
						
						// 本日的卡路里
						data1.setCaloD(caloD);
						
						// 值
						value = (float)caloD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 步数
						long stepD = data1.getStep();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 步数
								stepD += data2.getStep();
							}
						}
						
						// 本日的步数
						data1.setStepD(stepD);
						
						// 值
						value = (float)stepD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 时间
						long timeD = data1.getTime();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 时间
								timeD += data2.getTime();
							}
						}
						
						// 本日的时间
						data1.setTimeD(timeD);
						
						// 值
						value = (float)timeD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 周
				float wx = 20 * fWR;
				float wy = 30 * fHR;
				canvas.drawText(m_strWeek, wx, wy, m_paintTextW);
				
				// 横坐标轴
				float lx11 = 40 * fWR;
				float ly11 = nH - 20 * fHR;
				float lx12 = nW - 20 * fWR;
				float ly12 = nH - 20 * fHR;
				float ldx1 = (lx12 - lx11) / 7;
				float ldx12 = ldx1 / 2;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				for (int i = 0; i < 7; i++)
				{
					// 星期几
					String strDay = String.valueOf(i + 1);
					float tw = m_paintText_.measureText(strDay);
					canvas.drawText(strDay, lx11 + i * ldx1 + ldx12 - tw / 2, ly11 + m_ts_ + m_sw_, m_paintText_);
				}
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 纵坐标
				float lx21 = 20 * fWR;
				float ly21 = nH - 20 * fHR;
				float ly22 = 20 * fHR + m_tsW + 2 * m_ts_;
				float ldy2 = (ly21 - ly22) / nNum;
				
				for (int i = 1; i <= nNum; i++)
				{
					// 纵坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					canvas.drawText(strValue, lx21, ly21 - i * ldy2, m_paintText_);
				}
				
				float dy = 0;
				float deltaValue = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					deltaValue = m_tgt - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;
					float tx = lx11;
					float ty = ly11 - dy + m_tsT / 2 - m_swL / 2;
					float tx1 = lx11;
					float ty1 = ly11 - dy;
					float tx2 = lx12;
					float ty2 = ly11 - dy;
					float ts2 = m_tsT / 2;
					float ts25 = ts2 * 1.5f;
					float tw = m_paintTextT.measureText(m_strTarget);
					canvas.drawLine(tx1, ty1, tx2, ty2, m_paintLineT);
					m_path.moveTo(tx1 - ts2, ty1 - ts25);
					m_path.lineTo(tx1 + ts2 + tw, ty1 - ts25);
					m_path.lineTo(tx1 + 2.5f * ts2 + tw, ty1);
					m_path.lineTo(tx1 + ts2 + tw, ty1 + ts25);
					m_path.lineTo(tx1 - ts2, ty1 + ts25);
					m_path.close();
					canvas.drawPath(m_path, m_paintLineT);
					canvas.drawText(m_strTarget, tx, ty, m_paintTextT);
				}
				
				float lx1 = 0;
				float ly1 = nH - 20 * fHR - m_swL;
				float lx2 = 0;
				float ly2 = 0;
				
				// 数量
				int nSize = m_listUserData.size();
				
				for (int i = 0; i < 7; i++)
				{
					UserData data = null;
					
					for (int j = 0; j < nSize; j++)
					{
						data = m_listUserData.get(j);
						
						// 星期
						int nWeek = data.getWeek();
						
						if (nWeek == i + 1)
						{
							break;
						}
						
						if (j == nSize - 1)
						{
							data = null;
						}
					}
					
					if (data == null) continue;
					
					float value = 0;
					
					// 距离
					if (m_nMode == MODE_DIST)
					{
						// 距离
						value = (float)data.getDistD();
					}
					
					// 卡路里
					if (m_nMode == MODE_CALO)
					{
						// 卡路里
						value = (float)data.getCaloD();
					}
					
					// 步数
					if (m_nMode == MODE_STEP)
					{
						// 步数
						value = (float)data.getStepD();
					}
					
					// 时间
					if (m_nMode == MODE_TIME)
					{
						// 时间
						value = (float)data.getTimeD();
					}
					
					// 差值
					deltaValue = value - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;

					lx1 = lx11 + i * ldx1 + 2 * m_swL;
					lx2 = lx1 + ldx1 - 2 * m_swL;
					ly2 = ly11 - dy;
					
					if (ly1 - ly2 < 1)
					{
						ly2 = ly1 - 1;
					}
					
					if (value < m_tgt)
					{
						canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineL);
					}
					else
					{
						canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineH);
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 月_视图类
	 */
	private class MontView extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = MAX; // 最小
		
		// 文本大小
		private float m_tsM = 16;
		private float m_ts_ =  5;
		private float m_ts1 = 12;
		private float m_tsT = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swT = 3;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 路径
		private Path m_path = new Path();
		
		// 画笔
		private Paint m_paintTextM = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintText1 = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineT = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();
		
		/**
		 * 构造函数
		 * @param context
		 */
		public MontView(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsM *= fHR;
				m_ts_ *= fHR;
				m_ts1 *= fHR;
				m_tsT *= fHR;

				m_paintTextM.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextM.setColor(Color.BLACK); // 设置颜色
				m_paintTextM.setTextSize(m_tsM);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小

				m_paintText1.setAntiAlias(true);    // 设置抗锯齿
				m_paintText1.setColor(Color.BLACK); // 设置颜色
				m_paintText1.setTextSize(m_ts1);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;
				m_swT *= fHR;
				m_swL *= fHR;
				m_swH *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineT.setColor(Color.GREEN);      // 设置颜色
				m_paintLineT.setStrokeWidth(m_swT);      // 设置笔宽
				m_paintLineT.setStyle(Paint.Style.FILL); // 填充
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = m_fWeekTgt / 7;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;
				m_min = (value < m_min) ? value : m_min;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 获取日期
					int nYear  = data.getYear();
					int nMonth = data.getMonth();
					int nDay   = data.getDay();
					
					if (nYear != m_nYear || nMonth != m_nMonth || nDay > m_nDay)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 距离
						double distD = data1.getDist();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 距离
								distD += data2.getDist();
							}
						}
						
						// 本日的距离
						data1.setDistD(distD);
						
						// 值
						value = (float)distD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 卡路里
						long caloD = data1.getCalo();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 卡路里
								caloD += data2.getCalo();
							}
						}
						
						// 本日的卡路里
						data1.setCaloD(caloD);
						
						// 值
						value = (float)caloD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 步数
						long stepD = data1.getStep();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 步数
								stepD += data2.getStep();
							}
						}
						
						// 本日的步数
						data1.setStepD(stepD);
						
						// 值
						value = (float)stepD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 日
						int nDay1 = data1.getDay();

						// 时间
						long timeD = data1.getTime();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 日
							int nDay2 = data2.getDay();
							
							if (nDay1 == nDay2)
							{
								// 时间
								timeD += data2.getTime();
							}
						}
						
						// 本日的时间
						data1.setTimeD(timeD);
						
						// 值
						value = (float)timeD;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 月
				float mx = 20 * fWR;
				float my = 30 * fHR;
				canvas.drawText(m_strMonth, mx, my, m_paintTextM);
				
				// 横坐标轴
				float lx11 = 40 * fWR;
				float ly11 = nH - 20 * fHR;
				float lx12 = nW - 20 * fWR;
				float ly12 = nH - 20 * fHR;
				float ldx1 = (lx12 - lx11) / 31;
				float ldx12 = ldx1 / 2;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				for (int i = 0; i < 31; i++)
				{
					// 日
					String strDay = String.valueOf(i + 1);
					float tw = m_paintText_.measureText(strDay);
					canvas.drawText(strDay, lx11 + i * ldx1 + ldx12 - tw / 2, ly11 + m_ts_ + m_sw_, m_paintText_);
				}
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 纵坐标
				float lx21 = 20 * fWR;
				float ly21 = nH - 20 * fHR;
				float ly22 = 20 * fHR + m_tsM + 2 * m_ts_;
				float ldy2 = (ly21 - ly22) / nNum;
				
				for (int i = 1; i <= nNum; i++)
				{
					// 纵坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					canvas.drawText(strValue, lx21, ly21 - i * ldy2, m_paintText1);
				}

				float dy = 0;
				float deltaValue = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					deltaValue = m_tgt - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;
					float tx = lx11;
					float ty = ly11 - dy + m_tsT / 2 - m_swL / 2;
					float tx1 = lx11;
					float ty1 = ly11 - dy;
					float tx2 = lx12;
					float ty2 = ly11 - dy;
					float ts2 = m_tsT / 2;
					float ts25 = ts2 * 1.5f;
					float tw = m_paintTextT.measureText(m_strTarget);
					canvas.drawLine(tx1, ty1, tx2, ty2, m_paintLineT);
					m_path.moveTo(tx1 - ts2, ty1 - ts25);
					m_path.lineTo(tx1 + ts2 + tw, ty1 - ts25);
					m_path.lineTo(tx1 + 2.5f * ts2 + tw, ty1);
					m_path.lineTo(tx1 + ts2 + tw, ty1 + ts25);
					m_path.lineTo(tx1 - ts2, ty1 + ts25);
					m_path.close();
					canvas.drawPath(m_path, m_paintLineT);
					canvas.drawText(m_strTarget, tx, ty, m_paintTextT);
				}
				
				float lx1 = 0;
				float ly1 = nH - 20 * fHR - m_swL;
				float lx2 = 0;
				float ly2 = 0;
				
				// 数量
				int nSize = m_listUserData.size();
				
				for (int i = 0; i < 31; i++)
				{
					UserData data = null;
					
					for (int j = 0; j < nSize; j++)
					{
						data = m_listUserData.get(j);
						
						// 日
						int nDay = data.getDay();
						
						if (nDay == i + 1)
						{
							break;
						}
						
						if (j == nSize - 1)
						{
							data = null;
						}
					}
					
					if (data == null) continue;

					float value = 0;
					
					// 距离
					if (m_nMode == MODE_DIST)
					{
						// 距离
						value = (float)data.getDistD();
					}
					
					// 卡路里
					if (m_nMode == MODE_CALO)
					{
						// 卡路里
						value = (float)data.getCaloD();
					}
					
					// 步数
					if (m_nMode == MODE_STEP)
					{
						// 步数
						value = (float)data.getStepD();
					}
					
					// 时间
					if (m_nMode == MODE_TIME)
					{
						// 时间
						value = (float)data.getTimeD();
					}
					
					// 差值
					deltaValue = value - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;

					lx1 = lx11 + i * ldx1 + m_swL;
					lx2 = lx1 + ldx1 - m_swL;
					ly2 = ly11 - dy;
					
					if (ly1 - ly2 < 1)
					{
						ly2 = ly1 - 1;
					}
					
					if (value < m_tgt)
					{
						// 距离
						canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineL);
					}
					else
					{
						// 距离
						canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineH);
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 年_视图类
	 */
	private class YearView extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = MAX; // 最小
		
		// 文本大小
		private float m_tsY = 16;
		private float m_ts_ = 12;
		private float m_tsT = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swT = 3;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 路径
		private Path m_path = new Path();
		
		// 画笔
		private Paint m_paintTextY = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineT = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();
		
		/**
		 * 构造函数
		 * @param context
		 */
		public YearView(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsY *= fHR;
				m_ts_ *= fHR;
				m_tsT *= fHR;

				m_paintTextY.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextY.setColor(Color.BLACK); // 设置颜色
				m_paintTextY.setTextSize(m_tsY);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;
				m_swT *= fHR;
				m_swL *= fHR;
				m_swH *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineT.setColor(Color.GREEN);      // 设置颜色
				m_paintLineT.setStrokeWidth(m_swT);      // 设置笔宽
				m_paintLineT.setStyle(Paint.Style.FILL); // 填充
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = (m_fWeekTgt / 7) * 31;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;
				m_min = (value < m_min) ? value : m_min;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 年
					int nYear = data.getYear();
					
					if (nYear != m_nYear)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 月
						int nMonth1 = data1.getMonth();

						// 距离
						double distY = data1.getDist();
						double distM = data1.getDist();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 月
							int nMonth2 = data2.getMonth();

							// 距离
							distY += data2.getDist();
							
							if (nMonth1 == nMonth2)
							{
								// 距离
								distM += data2.getDist();
							}
						}
						
						// 本年的距离
						data1.setDistY(distY);
						
						// 本月的距离
						data1.setDistM(distM);
						
						// 值
						value = (float)distM;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 月
						int nMonth1 = data1.getMonth();

						// 卡路里
						long caloY = data1.getCalo();
						long caloM = data1.getCalo();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 月
							int nMonth2 = data2.getMonth();

							// 卡路里
							caloY += data2.getCalo();
							
							if (nMonth1 == nMonth2)
							{
								// 卡路里
								caloM += data2.getCalo();
							}
						}
						
						// 本年的卡路里
						data1.setCaloY(caloY);
						
						// 本月的卡路里
						data1.setCaloM(caloM);
						
						// 值
						value = (float)caloM;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 月
						int nMonth1 = data1.getMonth();

						// 步数
						long stepY = data1.getStep();
						long stepM = data1.getStep();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 月
							int nMonth2 = data2.getMonth();

							// 步数
							stepY += data2.getStep();
							
							if (nMonth1 == nMonth2)
							{
								// 步数
								stepM += data2.getStep();
							}
						}
						
						// 本年的步数
						data1.setStepY(stepY);
						
						// 本月的步数
						data1.setStepM(stepM);
						
						// 值
						value = (float)stepM;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 月
						int nMonth1 = data1.getMonth();

						// 时间
						long timeY = data1.getTime();
						long timeM = data1.getTime();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 月
							int nMonth2 = data2.getMonth();

							// 时间
							timeY += data2.getTime();
							
							if (nMonth1 == nMonth2)
							{
								// 时间
								timeM += data2.getTime();
							}
						}
						
						// 本年的时间
						data1.setTimeY(timeY);
						
						// 本月的时间
						data1.setTimeM(timeM);
						
						// 值
						value = (float)timeM;
						m_max = (value > m_max) ? value : m_max;
						m_min = (value < m_min) ? value : m_min;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 年
				float yx = 20 * fWR;
				float yy = 30 * fHR;
				canvas.drawText(m_strYear, yx, yy, m_paintTextY);
				
				// 横坐标轴
				float lx11 = 40 * fWR;
				float ly11 = nH - 20 * fHR;
				float lx12 = nW - 20 * fWR;
				float ly12 = nH - 20 * fHR;
				float ldx1 = (lx12 - lx11) / 12;
				float ldx12 = ldx1 / 2;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				for (int i = 0; i < 12; i++)
				{
					// 月
					String strMonth = String.valueOf(i + 1);
					float tw = m_paintText_.measureText(strMonth);
					canvas.drawText(strMonth, lx11 + i * ldx1 + ldx12 - tw / 2, ly11 + m_ts_ + m_sw_, m_paintText_);
				}
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 纵坐标
				float lx21 = 20 * fWR;
				float ly21 = nH - 20 * fHR;
				float ly22 = 20 * fHR + m_tsY + 2 * m_ts_;
				float ldy2 = (ly21 - ly22) / nNum;
				
				for (int i = 1; i <= nNum; i++)
				{
					// 纵坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					canvas.drawText(strValue, lx21, ly21 - i * ldy2, m_paintText_);
				}

				float dy = 0;
				float deltaValue = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					deltaValue = m_tgt - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;
					float tx = lx11;
					float ty = ly11 - dy + m_tsT / 2 - m_swL / 2;
					float tx1 = lx11;
					float ty1 = ly11 - dy;
					float tx2 = lx12;
					float ty2 = ly11 - dy;
					float ts2 = m_tsT / 2;
					float ts25 = ts2 * 1.5f;
					float tw = m_paintTextT.measureText(m_strTarget);
					canvas.drawLine(tx1, ty1, tx2, ty2, m_paintLineT);
					m_path.moveTo(tx1 - ts2, ty1 - ts25);
					m_path.lineTo(tx1 + ts2 + tw, ty1 - ts25);
					m_path.lineTo(tx1 + 2.5f * ts2 + tw, ty1);
					m_path.lineTo(tx1 + ts2 + tw, ty1 + ts25);
					m_path.lineTo(tx1 - ts2, ty1 + ts25);
					m_path.close();
					canvas.drawPath(m_path, m_paintLineT);
					canvas.drawText(m_strTarget, tx, ty, m_paintTextT);
				}
				
				float lx1 = 0;
				float ly1 = nH - 20 * fHR - m_swL;
				float lx2 = 0;
				float ly2 = 0;
				
				// 数量
				int nSize = m_listUserData.size();
				
				for (int i = 0; i < 31; i++)
				{
					UserData data = null;
					
					for (int j = 0; j < nSize; j++)
					{
						data = m_listUserData.get(j);
						
						// 月
						int nMonth = data.getMonth();
						
						if (nMonth == i + 1)
						{
							break;
						}
						
						if (j == nSize - 1)
						{
							data = null;
						}
					}
					
					if (data == null) continue;

					float value = 0;
					
					// 距离
					if (m_nMode == MODE_DIST)
					{
						// 距离
						value = (float)data.getDistM();
					}
					
					// 卡路里
					if (m_nMode == MODE_CALO)
					{
						// 卡路里
						value = (float)data.getCaloM();
					}
					
					// 步数
					if (m_nMode == MODE_STEP)
					{
						// 步数
						value = (float)data.getStepM();
					}
					
					// 时间
					if (m_nMode == MODE_TIME)
					{
						// 时间
						value = (float)data.getTimeM();
					}
					
					// 差值
					deltaValue = value - m_min;
					dy = deltaValue * (ly21 - ly22) / delta;

					lx1 = lx11 + i * ldx1 + m_swL;
					lx2 = lx1 + ldx1 - m_swL;
					ly2 = ly11 - dy;
					
					if (ly1 - ly2 < 1)
					{
						ly2 = ly1 - 1;
					}
					
					if (value < m_tgt)
					{
						canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineL);
					}
					else
					{
						canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineH);
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 时_比较_视图类
	 */
	private class HourComp extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_thi = 0;   // 本次
		private float m_ave = 0;   // 平均
		private float m_pre = 0;   // 上次
		private float m_bes = 0;   // 最佳
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = 0;   // 最小
		
		// 文本大小
		private float m_tsC = 16;
		private float m_tsT = 12;
		private float m_ts_ = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 画笔
		private Paint m_paintTextC = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();

		/**
		 * 构造函数
		 * @param context
		 */
		public HourComp(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsC *= fHR;
				m_tsT *= fHR;
				m_ts_ *= fHR;
				
				m_paintTextC.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextC.setColor(Color.BLACK); // 设置颜色
				m_paintTextC.setTextSize(m_tsC);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = m_fWeekTgt * 1000 / 7 / 24;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 获取日期
					int nYear  = data.getYear();
					int nMonth = data.getMonth();
					int nDay   = data.getDay();
					
					if (nYear != m_nYear || nMonth != m_nMonth || nDay != m_nDay)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();

				float valueT = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 距离
						double distH = data1.getDist();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 距离
								distH += data2.getDist();
							}
						}
						
						// 本时的距离
						data1.setDistH(distH);
						
						// 总距离
						valueT += (distH * 1000);
						
						if (i == nSize - 2)
						{
							// 上次
							m_pre = (float)distH * 1000;
						}
						
						if (i == nSize - 1)
						{
							// 本次
							m_thi = (float)distH * 1000;
						}
						
						// 值
						value = (float)distH * 1000;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 卡路里
						long caloH = data1.getCalo();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 卡路里
								caloH += data2.getCalo();
							}
						}
						
						// 本时的卡路里
						data1.setCaloH(caloH);
						
						// 总卡路里
						valueT += caloH;
						
						if (i == nSize - 2)
						{
							// 上次
							m_pre = (float)caloH;
						}
						
						if (i == nSize - 1)
						{
							// 本次
							m_thi = (float)caloH;
						}
						
						// 值
						value = (float)caloH;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 步数
						long stepH = data1.getStep();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 步数
								stepH += data2.getStep();
							}
						}
						
						// 本时的步数
						data1.setStepH(stepH);
						
						// 总步数
						valueT += stepH;
						
						if (i == nSize - 2)
						{
							// 上次
							m_pre = (float)stepH;
						}
						
						if (i == nSize - 1)
						{
							// 本次
							m_thi = (float)stepH;
						}
						
						// 值
						value = (float)stepH;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时
						int nHour1 = data1.getHour();

						// 时间
						long timeH = data1.getTime();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时
							int nHour2 = data2.getHour();
							
							if (nHour1 == nHour2)
							{
								// 时间
								timeH += data2.getTime();
							}
						}
						
						// 本时的时间
						data1.setTimeH(timeH);
						
						// 总时间
						valueT += timeH;
						
						if (i == nSize - 2)
						{
							// 上次
							m_pre = (float)timeH;
						}
						
						if (i == nSize - 1)
						{
							// 本次
							m_thi = (float)timeH;
						}
						
						// 值
						value = (float)timeH;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}
				
				if (nSize > 0)
				{
					// 平均/次
					m_ave = valueT / nSize;
				}
				
				// 最佳次
				m_bes = m_max;

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 比较
				float cx = 20 * fWR;
				float cy = 30 * fHR;
				canvas.drawText(m_strCompare, cx, cy, m_paintTextC);
				
				// 本次
				float tx1 = 20 * fWR;
				float ty1 = 60 * fHR;
				canvas.drawText(m_strThisTime, tx1, ty1, m_paintTextT);
				
				// 平均/次
				float tx2 = 20 * fWR;
				float ty2 = 90 * fHR;
				canvas.drawText(m_strAverTime, tx2, ty2, m_paintTextT);
				
				// 上次
				float tx3 =  20 * fWR;
				float ty3 = 120 * fHR;
				canvas.drawText(m_strLastTime, tx3, ty3, m_paintTextT);
				
				// 最佳次
				float tx4 =  20 * fWR;
				float ty4 = 150 * fHR;
				canvas.drawText(m_strBestTime, tx4, ty4, m_paintTextT);
				
				// 横坐标轴
				float lx11 = 60 * fWR;
				float ly11 = nH - 30 * fHR;
				float lx12 = nW - 30 * fWR;
				float ly12 = nH - 30 * fHR;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 横坐标
				float lx21 = 60 * fWR;
				float ly21 = nH - 30 * fHR;
				float lx22 = nW - 30 * fWR;
				float ldx2 = (lx22 - lx21) / nNum;
				
				for (int i = 0; i <= nNum; i++)
				{
					// 横坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					float tw = m_paintText_.measureText(strValue);
					canvas.drawText(strValue, lx21 + i * ldx2 - tw / 2, ly21 + m_ts_ + m_sw_, m_paintText_);
				}
				
				// 数量
				int nSize = m_listUserData.size();
				if (nSize < 1) return;
				
				float x11 =  60 * fWR;
				float y11 =  70 * fHR - m_tsT / 3;
				float x12 = 0;
				float y12 =  50 * fHR - m_tsT / 3;
				float x21 =  60 * fWR;
				float y21 = 100 * fHR - m_tsT / 3;
				float x22 = 0;
				float y22 =  80 * fHR - m_tsT / 3;
				float x31 =  60 * fWR;
				float y31 = 130 * fHR - m_tsT / 3;
				float x32 = 0;
				float y32 = 110 * fHR - m_tsT / 3;
				float x41 =  60 * fWR;
				float y41 = 160 * fHR - m_tsT / 3;
				float x42 = 0;
				float y42 = 140 * fHR - m_tsT / 3;
				
				// 差值
				float deltaDist1 = m_thi - m_min;
				float deltaDist2 = m_ave - m_min;
				float deltaDist3 = m_pre - m_min;
				float deltaDist4 = m_bes - m_min;
				float dx1 = deltaDist1 * (lx22 - lx21) / delta;
				float dx2 = deltaDist2 * (lx22 - lx21) / delta;
				float dx3 = deltaDist3 * (lx22 - lx21) / delta;
				float dx4 = deltaDist4 * (lx22 - lx21) / delta;

				x12 = x11 + dx1;
				x22 = x21 + dx2;
				x32 = x31 + dx3;
				x42 = x41 + dx4;

				float tgt = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					tgt = m_tgt;
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					// 平均
					tgt = m_ave;
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					// 平均
					tgt = m_ave;
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					// 平均
					tgt = m_ave;
				}
				
				if (x12 - x11 < 1)
				{
					x12 = x11 + 1;
				}
				
				if (x22 - x21 < 1)
				{
					x22 = x21 + 1;
				}
				
				if (x32 - x31 < 1)
				{
					x32 = x31 + 1;
				}
				
				if (x42 - x41 < 1)
				{
					x42 = x41 + 1;
				}
				
				// 本次
				if (m_thi < tgt)
				{
					canvas.drawRect(x11, y12, x12, y11, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x11, y12, x12, y11, m_paintLineH);
				}
				
				// 平均/次
				if (m_ave < tgt)
				{
					canvas.drawRect(x21, y22, x22, y21, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x21, y22, x22, y21, m_paintLineH);
				}
				
				// 上次
				if (m_pre < tgt)
				{
					canvas.drawRect(x31, y32, x32, y31, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x31, y32, x32, y31, m_paintLineH);
				}
				
				// 最佳次
				if (m_bes < tgt)
				{
					canvas.drawRect(x41, y42, x42, y41, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x41, y42, x42, y41, m_paintLineH);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 周_比较_视图类
	 */
	private class WeekComp extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_thi = 0;   // 本次
		private float m_ave = 0;   // 平均
		private float m_pre = 0;   // 上次
		private float m_bes = 0;   // 最佳
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = 0;   // 最小
		
		// 文本大小
		private float m_tsC = 16;
		private float m_tsT = 12;
		private float m_ts_ = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 画笔
		private Paint m_paintTextC = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();

		/**
		 * 构造函数
		 * @param context
		 */
		public WeekComp(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsC *= fHR;
				m_tsT *= fHR;
				m_ts_ *= fHR;
				
				m_paintTextC.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextC.setColor(Color.BLACK); // 设置颜色
				m_paintTextC.setTextSize(m_tsC);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = m_fWeekTgt;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 获取日期
					int nYear = data.getYear();
					
					if (nYear != m_nYear)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();

				float valueT = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < m_nWeeks; i++)
					{
						// 当周的距离
						double distW = 0;
						
						// 周一的时间
						long lMonday = m_listMonday.get(i);
						long lNextMonday = 0;
						
						if (i < m_nWeeks - 1)
						{
							lNextMonday = m_listMonday.get(i + 1);
						}
						else
						{
							lNextMonday = m_lNextMd;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonday || lTime > lNextMonday)
							{
								continue;
							}
							
							// 距离
							distW += data.getDist();
						}
						
						// 总距离
						valueT += distW;
						
						if (i == m_nWeeks - 2)
						{
							// 上周
							m_pre = (float)distW;
						}
						
						if (i == m_nWeeks - 1)
						{
							// 本周
							m_thi = (float)distW;
						}
						
						// 值
						value = (float)distW;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < m_nWeeks; i++)
					{
						// 当周的卡路里
						long caloW = 0;
						
						// 周一的时间
						long lMonday = m_listMonday.get(i);
						long lNextMonday = 0;
						
						if (i < m_nWeeks - 1)
						{
							lNextMonday = m_listMonday.get(i + 1);
						}
						else
						{
							lNextMonday = m_lNextMd;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonday || lTime > lNextMonday)
							{
								continue;
							}
							
							// 卡路里
							caloW += data.getCalo();
						}
						
						// 总卡路里
						valueT += caloW;
						
						if (i == m_nWeeks - 2)
						{
							// 上周
							m_pre = (float)caloW;
						}
						
						if (i == m_nWeeks - 1)
						{
							// 本周
							m_thi = (float)caloW;
						}
						
						// 值
						value = (float)caloW;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < m_nWeeks; i++)
					{
						// 当周的步数
						long stepW = 0;
						
						// 周一的时间
						long lMonday = m_listMonday.get(i);
						long lNextMonday = 0;
						
						if (i < m_nWeeks - 1)
						{
							lNextMonday = m_listMonday.get(i + 1);
						}
						else
						{
							lNextMonday = m_lNextMd;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonday || lTime > lNextMonday)
							{
								continue;
							}
							
							// 步数
							stepW += data.getStep();
						}
						
						// 总步数
						valueT += stepW;
						
						if (i == m_nWeeks - 2)
						{
							// 上周
							m_pre = (float)stepW;
						}
						
						if (i == m_nWeeks - 1)
						{
							// 本周
							m_thi = (float)stepW;
						}
						
						// 值
						value = (float)stepW;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < m_nWeeks; i++)
					{
						// 当周的时间
						long timeW = 0;
						
						// 周一的时间
						long lMonday = m_listMonday.get(i);
						long lNextMonday = 0;
						
						if (i < m_nWeeks - 1)
						{
							lNextMonday = m_listMonday.get(i + 1);
						}
						else
						{
							lNextMonday = m_lNextMd;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonday || lTime > lNextMonday)
							{
								continue;
							}
							
							// 时间
							timeW += data.getTime();
						}
						
						// 总时间
						valueT += timeW;
						
						if (i == m_nWeeks - 2)
						{
							// 上周
							m_pre = (float)timeW;
						}
						
						if (i == m_nWeeks - 1)
						{
							// 本周
							m_thi = (float)timeW;
						}
						
						// 值
						value = (float)timeW;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}
				
				if (m_nWeeks > 0)
				{
					// 平均/次
					m_ave = valueT / m_nWeeks;
				}
				
				// 最佳次
				m_bes = m_max;

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 比较
				float cx = 20 * fWR;
				float cy = 30 * fHR;
				canvas.drawText(m_strCompare, cx, cy, m_paintTextC);
				
				// 本周
				float tx1 = 20 * fWR;
				float ty1 = 60 * fHR;
				canvas.drawText(m_strThisWeek, tx1, ty1, m_paintTextT);
				
				// 平均/周
				float tx2 = 20 * fWR;
				float ty2 = 90 * fHR;
				canvas.drawText(m_strAverWeek, tx2, ty2, m_paintTextT);
				
				// 上周
				float tx3 =  20 * fWR;
				float ty3 = 120 * fHR;
				canvas.drawText(m_strLastWeek, tx3, ty3, m_paintTextT);
				
				// 最佳周
				float tx4 =  20 * fWR;
				float ty4 = 150 * fHR;
				canvas.drawText(m_strBestWeek, tx4, ty4, m_paintTextT);
				
				// 横坐标轴
				float lx11 = 60 * fWR;
				float ly11 = nH - 30 * fHR;
				float lx12 = nW - 30 * fWR;
				float ly12 = nH - 30 * fHR;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 横坐标
				float lx21 = 60 * fWR;
				float ly21 = nH - 30 * fHR;
				float lx22 = nW - 30 * fWR;
				float ldx2 = (lx22 - lx21) / nNum;
				
				for (int i = 0; i <= nNum; i++)
				{
					// 横坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					float tw = m_paintText_.measureText(strValue);
					canvas.drawText(strValue, lx21 + i * ldx2 - tw / 2, ly21 + m_ts_ + m_sw_, m_paintText_);
				}
				
				// 数量
				int nSize = m_listUserData.size();
				if (nSize < 1) return;
				
				float x11 =  60 * fWR;
				float y11 =  70 * fHR - m_tsT / 3;
				float x12 = 0;
				float y12 =  50 * fHR - m_tsT / 3;
				float x21 =  60 * fWR;
				float y21 = 100 * fHR - m_tsT / 3;
				float x22 = 0;
				float y22 =  80 * fHR - m_tsT / 3;
				float x31 =  60 * fWR;
				float y31 = 130 * fHR - m_tsT / 3;
				float x32 = 0;
				float y32 = 110 * fHR - m_tsT / 3;
				float x41 =  60 * fWR;
				float y41 = 160 * fHR - m_tsT / 3;
				float x42 = 0;
				float y42 = 140 * fHR - m_tsT / 3;
				
				// 差值
				float deltaDist1 = m_thi - m_min;
				float deltaDist2 = m_ave - m_min;
				float deltaDist3 = m_pre - m_min;
				float deltaDist4 = m_bes - m_min;
				float dx1 = deltaDist1 * (lx22 - lx21) / delta;
				float dx2 = deltaDist2 * (lx22 - lx21) / delta;
				float dx3 = deltaDist3 * (lx22 - lx21) / delta;
				float dx4 = deltaDist4 * (lx22 - lx21) / delta;

				x12 = x11 + dx1;
				x22 = x21 + dx2;
				x32 = x31 + dx3;
				x42 = x41 + dx4;

				float tgt = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					tgt = m_tgt;
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					// 平均
					tgt = m_ave;
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					// 平均
					tgt = m_ave;
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					// 平均
					tgt = m_ave;
				}
				
				if (x12 - x11 < 1)
				{
					x12 = x11 + 1;
				}
				
				if (x22 - x21 < 1)
				{
					x22 = x21 + 1;
				}
				
				if (x32 - x31 < 1)
				{
					x32 = x31 + 1;
				}
				
				if (x42 - x41 < 1)
				{
					x42 = x41 + 1;
				}
				
				// 本周
				if (m_thi < tgt)
				{
					canvas.drawRect(x11, y12, x12, y11, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x11, y12, x12, y11, m_paintLineH);
				}
				
				// 平均/周
				if (m_ave < tgt)
				{
					canvas.drawRect(x21, y22, x22, y21, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x21, y22, x22, y21, m_paintLineH);
				}
				
				// 上周
				if (m_pre < tgt)
				{
					canvas.drawRect(x31, y32, x32, y31, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x31, y32, x32, y31, m_paintLineH);
				}
				
				// 最佳周
				if (m_bes < tgt)
				{
					canvas.drawRect(x41, y42, x42, y41, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x41, y42, x42, y41, m_paintLineH);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 月_比较_视图类
	 */
	private class MontComp extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_thi = 0;   // 本次
		private float m_ave = 0;   // 平均
		private float m_pre = 0;   // 上次
		private float m_bes = 0;   // 最佳
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = 0;   // 最小
		
		// 文本大小
		private float m_tsC = 16;
		private float m_tsT = 12;
		private float m_ts_ = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 画笔
		private Paint m_paintTextC = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();

		/**
		 * 构造函数
		 * @param context
		 */
		public MontComp(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsC *= fHR;
				m_tsT *= fHR;
				m_ts_ *= fHR;
				
				m_paintTextC.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextC.setColor(Color.BLACK); // 设置颜色
				m_paintTextC.setTextSize(m_tsC);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = (m_fWeekTgt / 7) * 31;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 获取日期
					int nYear = data.getYear();
					
					if (nYear != m_nYear)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();

				float valueT = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < m_nMonth; i++)
					{
						// 当月的距离
						double distM = 0;
						
						// 每月 1 日的时间
						long lMonth1 = m_listMonth1.get(i);
						long lNextMonth1 = 0;
						
						if (i < m_nMonth - 1)
						{
							lNextMonth1 = m_listMonth1.get(i + 1);
						}
						else
						{
							lNextMonth1 = m_lNextMt;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonth1 || lTime > lNextMonth1)
							{
								continue;
							}
							
							// 距离
							distM += data.getDist();
						}
						
						// 总距离
						valueT += distM;
						
						if (i == m_nMonth - 2)
						{
							// 上月
							m_pre = (float)distM;
						}
						
						if (i == m_nMonth - 1)
						{
							// 本月
							m_thi = (float)distM;
						}
						
						// 值
						value = (float)distM;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < m_nMonth; i++)
					{
						// 当月的卡路里
						long caloM = 0;
						
						// 每月 1 日的时间
						long lMonth1 = m_listMonth1.get(i);
						long lNextMonth1 = 0;
						
						if (i < m_nMonth - 1)
						{
							lNextMonth1 = m_listMonth1.get(i + 1);
						}
						else
						{
							lNextMonth1 = m_lNextMt;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonth1 || lTime > lNextMonth1)
							{
								continue;
							}
							
							// 卡路里
							caloM += data.getCalo();
						}
						
						// 总卡路里
						valueT += caloM;
						
						if (i == m_nMonth - 2)
						{
							// 上月
							m_pre = (float)caloM;
						}
						
						if (i == m_nMonth - 1)
						{
							// 本月
							m_thi = (float)caloM;
						}
						
						// 值
						value = (float)caloM;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < m_nMonth; i++)
					{
						// 当月的步数
						long stepM = 0;
						
						// 每月 1 日的时间
						long lMonth1 = m_listMonth1.get(i);
						long lNextMonth1 = 0;
						
						if (i < m_nMonth - 1)
						{
							lNextMonth1 = m_listMonth1.get(i + 1);
						}
						else
						{
							lNextMonth1 = m_lNextMt;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonth1 || lTime > lNextMonth1)
							{
								continue;
							}
							
							// 步数
							stepM += data.getStep();
						}
						
						// 总步数
						valueT += stepM;
						
						if (i == m_nMonth - 2)
						{
							// 上月
							m_pre = (float)stepM;
						}
						
						if (i == m_nMonth - 1)
						{
							// 本月
							m_thi = (float)stepM;
						}
						
						// 值
						value = (float)stepM;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < m_nMonth; i++)
					{
						// 当月的时间
						long timeM = 0;
						
						// 每月 1 日的时间
						long lMonth1 = m_listMonth1.get(i);
						long lNextMonth1 = 0;
						
						if (i < m_nMonth - 1)
						{
							lNextMonth1 = m_listMonth1.get(i + 1);
						}
						else
						{
							lNextMonth1 = m_lNextMt;
						}
						
						for (int j = 0; j < nSize; j++)
						{
							UserData data = m_listUserData.get(j);

							// 时间
							long lTime = data.getCurTime();
							
							if (lTime < lMonth1 || lTime > lNextMonth1)
							{
								continue;
							}
							
							// 时间
							timeM += data.getTime();
						}
						
						// 总时间
						valueT += timeM;
						
						if (i == m_nMonth - 2)
						{
							// 上月
							m_pre = (float)timeM;
						}
						
						if (i == m_nMonth - 1)
						{
							// 本月
							m_thi = (float)timeM;
						}
						
						// 值
						value = (float)timeM;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}
				
				if (m_nMonth > 0)
				{
					// 平均/次
					m_ave = valueT / m_nMonth;
				}
				
				// 最佳次
				m_bes = m_max;

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 比较
				float cx = 20 * fWR;
				float cy = 30 * fHR;
				canvas.drawText(m_strCompare, cx, cy, m_paintTextC);
				
				// 本月
				float tx1 = 20 * fWR;
				float ty1 = 60 * fHR;
				canvas.drawText(m_strThisMont, tx1, ty1, m_paintTextT);
				
				// 平均/月
				float tx2 = 20 * fWR;
				float ty2 = 90 * fHR;
				canvas.drawText(m_strAverMont, tx2, ty2, m_paintTextT);
				
				// 上月
				float tx3 =  20 * fWR;
				float ty3 = 120 * fHR;
				canvas.drawText(m_strLastMont, tx3, ty3, m_paintTextT);
				
				// 最佳月
				float tx4 =  20 * fWR;
				float ty4 = 150 * fHR;
				canvas.drawText(m_strBestMont, tx4, ty4, m_paintTextT);
				
				// 横坐标轴
				float lx11 = 60 * fWR;
				float ly11 = nH - 30 * fHR;
				float lx12 = nW - 30 * fWR;
				float ly12 = nH - 30 * fHR;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 横坐标
				float lx21 = 60 * fWR;
				float ly21 = nH - 30 * fHR;
				float lx22 = nW - 30 * fWR;
				float ldx2 = (lx22 - lx21) / nNum;
				
				for (int i = 0; i <= nNum; i++)
				{
					// 横坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					float tw = m_paintText_.measureText(strValue);
					canvas.drawText(strValue, lx21 + i * ldx2 - tw / 2, ly21 + m_ts_ + m_sw_, m_paintText_);
				}
				
				// 数量
				int nSize = m_listUserData.size();
				if (nSize < 1) return;
				
				float x11 =  60 * fWR;
				float y11 =  70 * fHR - m_tsT / 3;
				float x12 = 0;
				float y12 =  50 * fHR - m_tsT / 3;
				float x21 =  60 * fWR;
				float y21 = 100 * fHR - m_tsT / 3;
				float x22 = 0;
				float y22 =  80 * fHR - m_tsT / 3;
				float x31 =  60 * fWR;
				float y31 = 130 * fHR - m_tsT / 3;
				float x32 = 0;
				float y32 = 110 * fHR - m_tsT / 3;
				float x41 =  60 * fWR;
				float y41 = 160 * fHR - m_tsT / 3;
				float x42 = 0;
				float y42 = 140 * fHR - m_tsT / 3;
				
				// 差值
				float deltaDist1 = m_thi - m_min;
				float deltaDist2 = m_ave - m_min;
				float deltaDist3 = m_pre - m_min;
				float deltaDist4 = m_bes - m_min;
				float dx1 = deltaDist1 * (lx22 - lx21) / delta;
				float dx2 = deltaDist2 * (lx22 - lx21) / delta;
				float dx3 = deltaDist3 * (lx22 - lx21) / delta;
				float dx4 = deltaDist4 * (lx22 - lx21) / delta;

				x12 = x11 + dx1;
				x22 = x21 + dx2;
				x32 = x31 + dx3;
				x42 = x41 + dx4;

				float tgt = 0;
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					tgt = m_tgt;
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					// 平均
					tgt = m_ave;
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					// 平均
					tgt = m_ave;
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					// 平均
					tgt = m_ave;
				}
				
				if (x12 - x11 < 1)
				{
					x12 = x11 + 1;
				}
				
				if (x22 - x21 < 1)
				{
					x22 = x21 + 1;
				}
				
				if (x32 - x31 < 1)
				{
					x32 = x31 + 1;
				}
				
				if (x42 - x41 < 1)
				{
					x42 = x41 + 1;
				}
				
				// 本月
				if (m_thi < tgt)
				{
					canvas.drawRect(x11, y12, x12, y11, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x11, y12, x12, y11, m_paintLineH);
				}
				
				// 平均/月
				if (m_ave < tgt)
				{
					canvas.drawRect(x21, y22, x22, y21, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x21, y22, x22, y21, m_paintLineH);
				}
				
				// 上月
				if (m_pre < tgt)
				{
					canvas.drawRect(x31, y32, x32, y31, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x31, y32, x32, y31, m_paintLineH);
				}
				
				// 最佳月
				if (m_bes < tgt)
				{
					canvas.drawRect(x41, y42, x42, y41, m_paintLineL);
				}
				else
				{
					canvas.drawRect(x41, y42, x42, y41, m_paintLineH);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * 年_比较_视图类
	 */
	private class YearComp extends View
	{
		// 模式
		private int m_nMode = HistoryLayout.MODE_DIST;
		
		private float m_int = 0;   // 间隔
		private float m_tgt = 0;   // 目标
		private float m_max = MIN; // 最大
		private float m_min = 0;   // 最小
		
		// 文本大小
		private float m_tsC = 16;
		private float m_tsT = 12;
		private float m_ts_ = 12;

		// 笔宽
		private float m_sw_ = 2;
		private float m_swL = 3;
		private float m_swH = 3;
		
		// 画笔
		private Paint m_paintTextC = new Paint();
		private Paint m_paintTextT = new Paint();
		private Paint m_paintText_ = new Paint();
		private Paint m_paintLine_ = new Paint();
		private Paint m_paintLineL = new Paint();
		private Paint m_paintLineH = new Paint();

		// User 链表
		private List<UserData> m_listUserData = new ArrayList<UserData>();

		/**
		 * 构造函数
		 * @param context
		 */
		public YearComp(Context context, final int nMode)
		{
			super(context);
			
			try
			{
				// 模式
				m_nMode = nMode;
				
				// 高度比例
				float fHR = theApp.getHeightRatio();
				
				// 文本大小
				m_tsC *= fHR;
				m_tsT *= fHR;
				m_ts_ *= fHR;
				
				m_paintTextC.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextC.setColor(Color.BLACK); // 设置颜色
				m_paintTextC.setTextSize(m_tsC);    // 设置文本大小

				m_paintTextT.setAntiAlias(true);    // 设置抗锯齿
				m_paintTextT.setColor(Color.BLACK); // 设置颜色
				m_paintTextT.setTextSize(m_tsT);    // 设置文本大小

				m_paintText_.setAntiAlias(true);    // 设置抗锯齿
				m_paintText_.setColor(Color.BLACK); // 设置颜色
				m_paintText_.setTextSize(m_ts_);    // 设置文本大小
				
				// 笔宽
				m_sw_ *= fHR;

				m_paintLine_.setColor(Color.BLACK); // 设置颜色
				m_paintLine_.setStrokeWidth(m_sw_); // 设置笔宽
				
				m_paintLineL.setColor(Color.rgb(98, 161, 204)); // 设置颜色
				m_paintLineL.setStrokeWidth(m_swL);             // 设置笔宽
				m_paintLineL.setStyle(Paint.Style.FILL);        // 填充
				
				m_paintLineH.setColor(Color.rgb(229, 176, 8)); // 设置颜色
				m_paintLineH.setStrokeWidth(m_swH);            // 设置笔宽
				m_paintLineH.setStyle(Paint.Style.FILL);       // 填充

				// 获取日期
				if (m_nYear <= 0) getDate();
				
				// 获取周目标
				if (m_fWeekTgt <= 0) getWeekTgt();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 目标
					m_tgt = (m_fWeekTgt / 7) * 365;
				}
				
				// 值
				float value = m_tgt;
				m_max = (value > m_max) ? value : m_max;

				// 本地
				List<UserData> listUserData = theApp.getListUserDataL();
				
				// 服务器
				if (ms_nData == DATA_SERV)
				{
					listUserData = theApp.getListUserDataS();
				}
				
				// 数量
				int nSize = listUserData.size();
				
				for (int i = 0; i < nSize; i++)
				{
					UserData data = listUserData.get(i);
					
					// 检测用户编号
					if (data.getNum() != theApp.getAccType())
					{
						continue;
					}
					
					// 年
					int nYear = data.getYear();
					
					if (nYear != m_nYear)
					{
						continue;
					}
					
					// 添加
					m_listUserData.add(data);
				}
				
				// 按时间排序
				Collections.sort(m_listUserData, new UserDataTimeComparator());
				
				// 数量
				nSize = m_listUserData.size();
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 距离
						double distY = data1.getDist();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 距离
							distY += data2.getDist();
						}
						
						// 本年的距离
						data1.setDistY(distY);
						
						// 值
						value = (float)distY;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 卡路里
						long caloY = data1.getCalo();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 卡路里
							caloY += data2.getCalo();
						}
						
						// 本年的卡路里
						data1.setCaloY(caloY);
						
						// 值
						value = (float)caloY;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 步数
						long stepY = data1.getStep();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 步数
							stepY += data2.getStep();
						}
						
						// 本年的步数
						data1.setStepY(stepY);
						
						// 值
						value = (float)stepY;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					for (int i = 0; i < nSize; i++)
					{
						UserData data1 = m_listUserData.get(i);
						
						// 时间
						long timeY = data1.getTime();
						
						for (int j = i + 1; j < nSize; j++)
						{
							UserData data2 = m_listUserData.get(j);
							
							// 时间
							timeY += data2.getTime();
						}
						
						// 本年的时间
						data1.setTimeY(timeY);
						
						// 值
						value = (float)timeY;
						m_max = (value > m_max) ? value : m_max;
					}
				}
				
				if (m_min == m_max)
				{
					m_max += 1;
				}

				// 获取最小最大值和间隔
				float[] value3 = getMinMaxInter(m_min, m_max);
				
				if (value3 != null)
				{
					m_min = value3[0];
					m_max = value3[1];
					m_int = value3[2];
				}
			}
			catch (Exception e)
			{
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if (canvas == null) return;
			
			super.onDraw(canvas);
			
			try
			{
				if (isInEditMode()) return;
				
				// 宽度和高度
				int nW = getWidth();
				int nH = getHeight();
				if (nW < 1 || nH < 1) return;

				// 宽度和高度比例
				float fWR = theApp.getWidthRatio();
				float fHR = theApp.getHeightRatio();

				// 比较
				float cx = 20 * fWR;
				float cy = 30 * fHR;
				canvas.drawText(m_strCompare, cx, cy, m_paintTextC);
				
				// 本年
				float tx =  20 * fWR;
				float ty = 120 * fHR;
				canvas.drawText(m_strThisYear, tx, ty, m_paintTextT);
				
				// 横坐标轴
				float lx11 = 60 * fWR;
				float ly11 = nH - 30 * fHR;
				float lx12 = nW - 30 * fWR;
				float ly12 = nH - 30 * fHR;
				canvas.drawLine(lx11, ly11, lx12, ly12, m_paintLine_);
				
				if (m_min >= m_max || m_int <= 0) return;

				float delta = m_max - m_min;
				int nNum = (int)(delta / m_int);
				if (nNum < 1) return;
				
				// 横坐标
				float lx21 = 60 * fWR;
				float ly21 = nH - 30 * fHR;
				float lx22 = nW - 30 * fWR;
				float ldx2 = (lx22 - lx21) / nNum;
				
				for (int i = 0; i <= nNum; i++)
				{
					// 横坐标
					int nValue = (int)(m_min + m_int * i);
					String strValue = String.valueOf(nValue);
					if (strValue.length() > 3) strValue = strValue.substring(0, 3);
					float tw = m_paintText_.measureText(strValue);
					canvas.drawText(strValue, lx21 + i * ldx2 - tw / 2, ly21 + m_ts_ + m_sw_, m_paintText_);
				}
				
				// 数量
				int nSize = m_listUserData.size();
				if (nSize < 1) return;
				
				float lx1 = 60 * fWR;
				float ly1 = 140 * fHR - m_tsT / 3;
				float lx2 = 0;
				float ly2 = 100 * fHR - m_tsT / 3;

				float tgt = 0;
				float value = 0;
				
				UserData data = m_listUserData.get(0);
				
				// 距离
				if (m_nMode == MODE_DIST)
				{
					// 距离
					tgt = m_tgt;
					value = (float)data.getDistY();
				}
				
				// 卡路里
				if (m_nMode == MODE_CALO)
				{
					// 卡路里
					value = (float)data.getCaloY();
				}
				
				// 步数
				if (m_nMode == MODE_STEP)
				{
					// 步数
					value = (float)data.getStepY();
				}
				
				// 时间
				if (m_nMode == MODE_TIME)
				{
					// 时间
					value = (float)data.getTimeY();
				}
				
				// 差值
				float deltaValue = value - m_min;
				float dx = deltaValue * (lx22 - lx21) / delta;

				lx2 = lx1 + dx;
				
				if (lx2 - lx1 < 1)
				{
					lx2 = lx1 + 1;
				}
				
				if (value < tgt)
				{
					canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineL);
				}
				else
				{
					canvas.drawRect(lx1, ly2, lx2, ly1, m_paintLineH);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
}
