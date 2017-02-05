package com.chinafeisite.tianbu;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.CompoundButton;

/**
 * 开关按钮类
 */
public class SwitchButton extends CompoundButton
{
//	private final String TAG = "SwitchButton";

	// 左右侧文本布局
	private Layout m_layLeft = null;
	private Layout m_layRight = null;

	// 背景
	private Drawable m_drawableSwitch = null;
	private Drawable m_drawableBackground = null;
	
	// 宽度和高度
	private int m_nWidth = 250;
	private int m_nHeight = 60;
	private int m_nFixedWidth = 360;
	private int m_nSwitchMinWidth = 250;
	public int getFixedWidth() { return m_nFixedWidth; }
	public void setFixedWidth(final int n) { m_nFixedWidth = n; }

	// 间距
	private int m_nInnerPadding = 20;
	private int m_nSwitchPadding = 32;

	// 文本颜色
	private int m_nTextColorChecked = 0;
	private int m_nTextColorUnChecked = 0;

	// 监听器
	private OnClickListener m_onClickListener = null;
	
	// 左右侧文本
	private String m_strTextLeft = "On";
	private String m_strTextRight = "Off";
	public String getTextLeft() { return m_strTextLeft; }
	public String getTextRight() { return m_strTextRight; }

	/**
	 * 构造函数
	 * @param context
	 */
	public SwitchButton(Context context)
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
	public SwitchButton(Context context, AttributeSet attrs)
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
	public SwitchButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		// 初始化
		init(context);
	}

	@Override
	protected void drawableStateChanged()
	{
		super.drawableStateChanged();

		try
		{
			// 可绘制状态
			int[] nDrawableState = getDrawableState();
			if (m_drawableSwitch != null) m_drawableSwitch.setState(nDrawableState);

			// 更新
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		try
		{
			// 左右坐标
			int nRight = getWidth() - getPaddingRight();
			int nLeft = nRight - m_nWidth;

			// 绘制背景
			m_drawableBackground.setBounds(nLeft, 0, nRight, m_nHeight);
			m_drawableBackground.draw(canvas);

			// 开
			if (isChecked())
			{
				m_drawableSwitch.setBounds(nLeft, 0, nLeft + (m_nWidth / 2), m_nHeight);
			}
			// 关
			else
			{
				m_drawableSwitch.setBounds(nLeft + m_nWidth / 2, 0, nRight, m_nHeight);
			}

			m_drawableSwitch.draw(canvas);

			// 保存画布
			canvas.save();

			// 开
			if (isChecked())
			{
				// 绘制左侧文本
				getPaint().setColor(isChecked() ? m_nTextColorChecked : m_nTextColorUnChecked);
				canvas.translate(nLeft + (m_nWidth / 2 - m_layLeft.getWidth()) / 2, (m_nHeight - m_layLeft.getHeight()) / 2);
				m_layLeft.draw(canvas);
				canvas.restore();
			}
			// 关
			else
			{
				// 绘制右侧文本
				getPaint().setColor(!isChecked() ? m_nTextColorChecked : m_nTextColorUnChecked);
				canvas.translate(nLeft + (m_nWidth / 2 - m_layRight.getWidth()) / 2 + m_nWidth / 2, (m_nHeight - m_layRight.getHeight()) / 2);
				m_layRight.draw(canvas);
				canvas.restore();
			}
			
/*			Log.d(TAG, String.format("ID: %d\r\nTextLeft: %s\r\nTextRight: %s",
					m_nID,
					m_strTextLeft,
					m_strTextRight));*/
		}
		catch (Exception e)
		{
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		try
		{
			// 创建左右布局
			if (m_layLeft == null) m_layLeft = makeLayout(m_strTextLeft);
			if (m_layRight == null) m_layRight = makeLayout(m_strTextRight);

			// 最大文本宽度
			final int nMaxTextWidth = Math.max(m_layLeft.getWidth(), m_layRight.getWidth());

			// 实际宽度
			int nActualWidth = Math.max(m_nSwitchMinWidth, nMaxTextWidth * 2 + getPaddingLeft() + getPaddingRight() + m_nInnerPadding * 4);

			// 高度
			final int nSwitchHeight = Math.max(m_drawableBackground.getIntrinsicHeight(), m_drawableSwitch.getIntrinsicHeight());

			// 宽度和高度
			m_nWidth = nActualWidth;
			m_nHeight = nSwitchHeight;

			if (getText() != null)
			{
				// 实际宽度
				nActualWidth += makeLayout(getText().toString()).getWidth() + m_nSwitchPadding;
			}
			
			// 固定宽度
			if (m_nFixedWidth > nActualWidth) nActualWidth = m_nFixedWidth;

			if (m_nFixedWidth > 0)
			{
				// 宽度和高度
				m_nWidth = nActualWidth;
				m_nHeight = nSwitchHeight;
			}

			// 设置视图尺寸
			setMeasuredDimension(nActualWidth, nSwitchHeight);
			return;
		}
		catch (Exception e)
		{
		}
		
		// 设置视图尺寸
		setMeasuredDimension(m_nWidth, m_nHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				try
				{
					performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					setChecked(!isChecked());
					invalidate();

					// call the onClickListener
					if (m_onClickListener != null) m_onClickListener.onClick(this);
				}
				catch (Exception e)
				{
				}
				return false;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public int getCompoundPaddingRight()
	{
		try
		{
			// 间距
			int nPadding = super.getCompoundPaddingRight() + m_nWidth;
			if (!TextUtils.isEmpty(getText())) nPadding += m_nSwitchPadding;

			return nPadding;
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}

	@Override
	public void setOnClickListener(OnClickListener l)
	{
		super.setOnClickListener(l);

		m_onClickListener = l;
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context)
	{
		try
		{
			// 文本颜色
			m_nTextColorChecked = Color.argb(0xff, 0xff, 0xff, 0xff);
			m_nTextColorUnChecked = Color.argb(0xff, 0x18, 0x18, 0x18);
			
			// 背景
			m_drawableSwitch = context.getResources().getDrawable(R.drawable.switch_activated);
			m_drawableBackground = context.getResources().getDrawable(R.drawable.switch_bg);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 生成布局
	 * @param strText
	 * @return
	 */
	private Layout makeLayout(final String strText)
	{
		try
		{
			return new StaticLayout(strText, getPaint(),
					(int)FloatMath.ceil(Layout.getDesiredWidth(strText, getPaint())),
					Layout.Alignment.ALIGN_NORMAL, 1f, 0, true);
		}
		catch (Exception e)
		{
		}
		
		return null;
	}

	/**
	 * 设置左侧文本
	 * @param strTextLeft
	 */
	public void setTextLeft(final String strTextLeft)
	{
		try
		{
			// 左侧文本
			if (strTextLeft == null) return;
			m_strTextLeft = strTextLeft;
			
			// 请求布局
			requestLayout();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 设置右侧文本
	 * @param strTextRight
	 */
	public void setTextRight(final String strTextRight)
	{
		try
		{
			// 右侧文本
			if (strTextRight == null) return;
			m_strTextRight = strTextRight;

			// 请求布局
			requestLayout();
		}
		catch (Exception e)
		{
		}
	}
}
