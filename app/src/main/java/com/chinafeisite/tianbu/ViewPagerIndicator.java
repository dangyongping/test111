package com.chinafeisite.tianbu;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * 视图页指示器接口
 */
interface IViewPagerIndicator extends ViewPager.OnPageChangeListener
{
	/**
	 * Bind the indicator to a ViewPager.
	 * @param view
	 */
	void setViewPager(ViewPager view);

	/**
	 * Bind the indicator to a ViewPager.
	 * @param view
	 * @param initialPosition
	 */
	void setViewPager(ViewPager view, int initialPosition);

	/**
	 * <p>
	 * Set the current page of both the ViewPager and indicator.
	 * </p>
	 * <p>
	 * This <strong>must</strong> be used if you need to set the page before the
	 * views are drawn on screen (e.g., default start page).
	 * </p>
	 * @param item
	 */
	void setCurrentItem(int item);

	/**
	 * Set a page change listener which will receive forwarded events.
	 * @param listener
	 */
	void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

	/**
	 * Notify the indicator that the fragment list has changed.
	 */
	void notifyDataSetChanged();
}

/**
 * 视图页指示器类
 */
public class ViewPagerIndicator extends View implements IViewPagerIndicator
{
	private static final int INVALID_POINTER = -1;

	private float mRadius;
	private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;
	private int mCurrentPage;
	private int mSnapPage;
	private float mPageOffset;
	private int mScrollState;
	private int mOrientation;
	private boolean mCentered;
	private boolean mSnap;

	private int mTouchSlop;
	private float mLastMotionX = -1;
	private int mActivePointerId = INVALID_POINTER;
	private boolean mIsDragging;

	public ViewPagerIndicator(Context context)
	{
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.vpiViewPagerIndicatorStyle);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		if (isInEditMode()) return;

		try
		{
			// Load defaults from resources
			final Resources res = getResources();
			final int defaultPageColor = res.getColor(R.color.default_view_pager_indicator_page_color);
			final int defaultFillColor = res.getColor(R.color.default_view_pager_indicator_fill_color);
			final int defaultOrientation = res.getInteger(R.integer.default_view_pager_indicator_orientation);
			final int defaultStrokeColor = res.getColor(R.color.default_view_pager_indicator_stroke_color);
			final float defaultStrokeWidth = res.getDimension(R.dimen.default_view_pager_indicator_stroke_width);
			final float defaultRadius = res.getDimension(R.dimen.default_view_pager_indicator_radius);
			final boolean defaultCentered = res.getBoolean(R.bool.default_view_pager_indicator_centered);
			final boolean defaultSnap = res.getBoolean(R.bool.default_view_pager_indicator_snap);

			// Retrieve styles attributes
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, defStyle, 0);

			mCentered = a.getBoolean(R.styleable.ViewPagerIndicator1_centered, defaultCentered);
			mOrientation = a.getInt(R.styleable.ViewPagerIndicator1_android_orientation, defaultOrientation);
			mPaintPageFill.setStyle(Style.FILL);
			mPaintPageFill.setColor(a.getColor(R.styleable.ViewPagerIndicator1_pageColor, defaultPageColor));
			mPaintStroke.setStyle(Style.STROKE);
			mPaintStroke.setColor(a.getColor(R.styleable.ViewPagerIndicator1_strokeColor, defaultStrokeColor));
			mPaintStroke.setStrokeWidth(a.getDimension(R.styleable.ViewPagerIndicator1_strokeWidth, defaultStrokeWidth));
			mPaintFill.setStyle(Style.FILL);
			mPaintFill.setColor(a.getColor(R.styleable.ViewPagerIndicator1_fillColor, defaultFillColor));
			mRadius = a.getDimension(R.styleable.ViewPagerIndicator1_radius, defaultRadius);
			mSnap = a.getBoolean(R.styleable.ViewPagerIndicator1_snap, defaultSnap);

			Drawable background = a.getDrawable(R.styleable.ViewPagerIndicator1_android_background);
			
			if (background != null)
			{
				setBackgroundDrawable(background);
			}

			a.recycle();

			final ViewConfiguration configuration = ViewConfiguration.get(context);
			mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		}
		catch (Exception e)
		{
		}
	}

	public void setCentered(boolean centered)
	{
		try
		{
			mCentered = centered;
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	public boolean isCentered()
	{
		return mCentered;
	}

	public void setPageColor(int pageColor)
	{
		try
		{
			mPaintPageFill.setColor(pageColor);
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	public int getPageColor()
	{
		return mPaintPageFill.getColor();
	}

	public void setFillColor(int fillColor)
	{
		try
		{
			mPaintFill.setColor(fillColor);
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	public int getFillColor()
	{
		return mPaintFill.getColor();
	}

	public void setOrientation(int orientation)
	{
		try
		{
			switch (orientation)
			{
			case HORIZONTAL:
			case VERTICAL:
				mOrientation = orientation;
				requestLayout();
				break;

			default:
				throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
			}
		}
		catch (Exception e)
		{
		}
	}

	public int getOrientation()
	{
		return mOrientation;
	}

	public void setStrokeColor(int strokeColor)
	{
		try
		{
			mPaintStroke.setColor(strokeColor);
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	public int getStrokeColor()
	{
		return mPaintStroke.getColor();
	}

	public void setStrokeWidth(float strokeWidth)
	{
		try
		{
			mPaintStroke.setStrokeWidth(strokeWidth);
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	public float getStrokeWidth()
	{
		return mPaintStroke.getStrokeWidth();
	}

	public void setRadius(float radius)
	{
		mRadius = radius;
		invalidate();
	}

	public float getRadius()
	{
		return mRadius;
	}

	public void setSnap(boolean snap)
	{
		mSnap = snap;
		invalidate();
	}

	public boolean isSnap()
	{
		return mSnap;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		try
		{
			if (mViewPager == null)
			{
				return;
			}
			
			final int count = mViewPager.getAdapter().getCount();
			
//			theApp.MYSLog("IViewPager","3333"+count); 
			
			if (count == 0)
			{
				return;
			}

			if (mCurrentPage >= count)
			{
				setCurrentItem(count - 1);
				return;
			}

			int longSize;
			int longPaddingBefore;
			int longPaddingAfter;
			int shortPaddingBefore;
			
			if (mOrientation == HORIZONTAL)
			{
				longSize = getWidth();
				longPaddingBefore = getPaddingLeft();
				longPaddingAfter = getPaddingRight();
				shortPaddingBefore = getPaddingTop();
			}
			else
			{
				longSize = getHeight();
				longPaddingBefore = getPaddingTop();
				longPaddingAfter = getPaddingBottom();
				shortPaddingBefore = getPaddingLeft();
			}

			final float threeRadius = mRadius * 3;
			final float shortOffset = shortPaddingBefore + mRadius;
			float longOffset = longPaddingBefore + mRadius;
			
			if (mCentered)
			{
				longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);
			}

			float dX;
			float dY;

			float pageFillRadius = mRadius;
			
			if (mPaintStroke.getStrokeWidth() > 0)
			{
				pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
			}

			// Draw stroked circles
			for (int iLoop = 0; iLoop < count; iLoop++)
			{
				float drawLong = longOffset + (iLoop * threeRadius);
				
				if (mOrientation == HORIZONTAL)
				{
					dX = drawLong;
					dY = shortOffset;
				}
				else
				{
					dX = shortOffset;
					dY = drawLong;
				}
				
				// Only paint fill if not completely transparent
				if (mPaintPageFill.getAlpha() > 0)
				{
					canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
				}

				// Only paint stroke if a stroke width was non-zero
				if (pageFillRadius != mRadius)
				{
					canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
				}
			}

			// Draw the filled circle according to the current scroll
			float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
			
			if (!mSnap)
			{
				cx += mPageOffset * threeRadius;
			}
			
			if (mOrientation == HORIZONTAL)
			{
				dX = longOffset + cx;
				dY = shortOffset;
			}
			else
			{
				dX = shortOffset;
				dY = longOffset + cx;
			}
			
			canvas.drawCircle(dX, dY, mRadius, mPaintFill);
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (super.onTouchEvent(ev))
		{
			return true;
		}

		if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0))
		{
			return false;
		}

		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			mLastMotionX = ev.getX();
			break;

		case MotionEvent.ACTION_MOVE:
		{
			final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
			final float x = MotionEventCompat.getX(ev, activePointerIndex);
			final float deltaX = x - mLastMotionX;

			if (!mIsDragging)
			{
				if (Math.abs(deltaX) > mTouchSlop)
				{
					mIsDragging = true;
				}
			}

			if (mIsDragging)
			{
				mLastMotionX = x;

				if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag())
				{
					mViewPager.fakeDragBy(deltaX);
				}
			}
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (!mIsDragging)
			{
				final int count = mViewPager.getAdapter().getCount();
				final int width = getWidth();
				final float halfWidth = width / 2f;
				final float sixthWidth = width / 6f;

				if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth))
				{
					if (action != MotionEvent.ACTION_CANCEL)
					{
						mViewPager.setCurrentItem(mCurrentPage - 1);
					}

					return true;
				}
				else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth))
				{
					if (action != MotionEvent.ACTION_CANCEL)
					{
						mViewPager.setCurrentItem(mCurrentPage + 1);
					}

					return true;
				}
			}

			mIsDragging = false;
			mActivePointerId = INVALID_POINTER;
			if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
			break;

		case MotionEventCompat.ACTION_POINTER_DOWN:
		{
			final int index = MotionEventCompat.getActionIndex(ev);
			mLastMotionX = MotionEventCompat.getX(ev, index);
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		}

		case MotionEventCompat.ACTION_POINTER_UP:
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

			if (pointerId == mActivePointerId)
			{
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
			}

			mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
			break;
		}

		return true;
	}

	@Override
	public void setViewPager(ViewPager view)
	{
		try
		{
			if (mViewPager == view)
			{
				return;
			}

			if (mViewPager != null)
			{
				mViewPager.setOnPageChangeListener(null);
			}

			if (view.getAdapter() == null)
			{
				throw new IllegalStateException("ViewPager does not have adapter instance.");
			}

			mViewPager = view;
			mViewPager.setOnPageChangeListener(this);
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition)
	{
		try
		{
			setViewPager(view);
			setCurrentItem(initialPosition);
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void setCurrentItem(int item)
	{
		try
		{
			if (mViewPager == null)
			{
				throw new IllegalStateException("ViewPager has not been bound.");
			}

			mViewPager.setCurrentItem(item);
			mCurrentPage = item;
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void notifyDataSetChanged()
	{
		try
		{
			invalidate();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		try
		{
			mScrollState = state;

			if (mListener != null)
			{
				mListener.onPageScrollStateChanged(state);
			}
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		try
		{
			mCurrentPage = position;
			mPageOffset = positionOffset;
			invalidate();

			if (mListener != null)
			{
				mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void onPageSelected(int position)
	{
		try
		{
			if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE)
			{
				mCurrentPage = position;
				mSnapPage = position;
				invalidate();
			}

			if (mListener != null)
			{
				mListener.onPageSelected(position);
			}
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener)
	{
		mListener = listener;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		try
		{
			if (mOrientation == HORIZONTAL)
			{
				setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
			}
			else
			{
				setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
			}
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Determines the width of this view
	 * @param measureSpec A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureLong(int measureSpec)
	{
		int result = 0;

		try
		{
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null))
			{
				// We were told how big to be
				result = specSize;
			}
			else
			{
				// Calculate the width according the views count
				final int count = mViewPager.getAdapter().getCount();
				result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * mRadius) + (count - 1) * mRadius + 1);

				// Respect AT_MOST value if that was what is called for by measureSpec
				if (specMode == MeasureSpec.AT_MOST)
				{
					result = Math.min(result, specSize);
				}
			}
		}
		catch (Exception e)
		{
		}

		return result;
	}

	/**
	 * Determines the height of this view
	 * @param measureSpec A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureShort(int measureSpec)
	{
		int result = 0;

		try
		{
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			if (specMode == MeasureSpec.EXACTLY)
			{
				// We were told how big to be
				result = specSize;
			}
			else
			{
				// Measure the height
				result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);

				// Respect AT_MOST value if that was what is called for by measureSpec
				if (specMode == MeasureSpec.AT_MOST)
				{
					result = Math.min(result, specSize);
				}
			}
		}
		catch (Exception e)
		{
		}

		return result;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		try
		{
			SavedState savedState = (SavedState) state;
			super.onRestoreInstanceState(savedState.getSuperState());
			mCurrentPage = savedState.currentPage;
			mSnapPage = savedState.currentPage;
			requestLayout();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		try
		{
			Parcelable superState = super.onSaveInstanceState();
			SavedState savedState = new SavedState(superState);
			savedState.currentPage = mCurrentPage;
			return savedState;
		}
		catch (Exception e)
		{
		}

		return super.onSaveInstanceState();
	}

	/**
	 * 保存状态类
	 */
	static class SavedState extends BaseSavedState
	{
		int currentPage;

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		private SavedState(Parcel in)
		{
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPage);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
		{
			@Override
			public SavedState createFromParcel(Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size)
			{
				return new SavedState[size];
			}
		};
	}
}
