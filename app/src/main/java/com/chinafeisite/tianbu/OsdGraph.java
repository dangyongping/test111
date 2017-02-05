package com.chinafeisite.tianbu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * OSD 图片类
 */
class OsdBitmap
{
	// 坐标
	private int m_nX1 = 0;
	private int m_nY1 = 0;
	private int m_nX2 = 0;
	private int m_nY2 = 0;
	public int getX1() { return m_nX1; }
	public int getY1() { return m_nY1; }
	public int getX2() { return m_nX2; }
	public int getY2() { return m_nY2; }
	public void setX1(final int x) { m_nX1 = x; }
	public void setY1(final int y) { m_nY1 = y; }
	public void setX2(final int x) { m_nX2 = x; }
	public void setY2(final int y) { m_nY2 = y; }
	
	// 是否已绘制
//	private boolean m_bDrawn = false;
//	public boolean isDrawn() { return m_bDrawn; }
//	public void setDrawn(final boolean b) { m_bDrawn = b; }
	
	// 图片
	private Bitmap m_bmp = null;
	public Bitmap getBmp() { return m_bmp; }
	
	// 路径
	private String m_strPath = "";
	public String getPath() { return m_strPath; }
	public void setPath(final String str) { m_strPath = str; }
	
	/**
	 * 构造函数
	 * @param bmp
	 */
	public OsdBitmap(Bitmap bmp)
	{
		m_bmp = bmp;
	}
	
	/**
	 * 回收
	 */
	public void recycle()
	{
		try
		{
			// 回收
//			m_bmp.recycle();
//			m_bmp = null;
			
			// 垃圾回收
//			System.gc();
//			System.runFinalization();
//			Runtime.getRuntime().gc();
		}
		catch (Exception e)
		{
		}
	}
}

/**
 * OSD 图形类
 */
public class OsdGraph
{
	// 双精度的精度
	public static final double DBL_PRE = 0.000001;
	
	// 颜色
	public final static int BLACK   = 1; // 黑色, (  0,   0,   0)
	public final static int WHITE   = 2; // 白色, (255, 255, 255)
	public final static int RED     = 3; // 红色, (255,   0,   0)
	public final static int GREEN   = 4; // 绿色, (  0, 255,   0)
	public final static int BLUE    = 5; // 蓝色, (  0,   0, 255)
	public final static int YELLOW  = 6; // 黄色, (255, 255,   0)
	public final static int ORANGE  = 7; // 橙色, (255, 128,   0)
	public final static int MAGENTA = 8; // 紫色, (128,   0, 128)

	// 画笔
	protected Paint m_paint = new Paint();
	
	// 颜色
	protected int m_nColor = BLACK;
	public void setColor(final int n) { if (n >= BLACK && n <= MAGENTA) m_nColor = n; }
	
	/**
	 * 绘制
	 * @param canvas
	 */
	public void draw(Canvas canvas)
	{
		if (canvas == null) return;
		
		try
		{
			// 获取颜色
			int nColor = getColor();
	
			m_paint.setColor(nColor);   // 设置颜色
			m_paint.setAntiAlias(true); // 设置抗锯齿
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 获取颜色
	 * @return
	 */
	protected int getColor()
	{
		int nColor = 0;
		
		if (m_nColor ==   BLACK) nColor = getColor(  0,   0,   0); // 黑色
		if (m_nColor ==   WHITE) nColor = getColor(255, 255, 255); // 白色
		if (m_nColor ==     RED) nColor = getColor(255,   0,   0); // 红色
		if (m_nColor ==   GREEN) nColor = getColor(  0, 255,   0); // 绿色
		if (m_nColor ==    BLUE) nColor = getColor(  0,   0, 255); // 蓝色
		if (m_nColor ==  YELLOW) nColor = getColor(255, 255,   0); // 黄色
		if (m_nColor ==  ORANGE) nColor = getColor(255, 128,   0); // 橙色
		if (m_nColor == MAGENTA) nColor = getColor(128,   0, 128); // 紫色
		
		return nColor;
	}
	
	/**
	 * 获取颜色
	 * @param nR
	 * @param nG
	 * @param nB
	 * @return
	 */
	private int getColor(final int nR, final int nG, final int nB)
	{
		// ARGB
		return (0xff << 24) | (nR << 16) | (nG << 8) | nB;
	}
	
	/**
	 * 判断是否与矩形相交
	 * @param x1 矩形左上角的 X 坐标
	 * @param y1 矩形左上角的 Y 坐标
	 * @param x2 矩形右下角的 X 坐标
	 * @param y2 矩形右下角的 Y 坐标
	 * @return
	 */
	protected boolean interOfRect(final double x1, final double y1, final double x2, final double y2)
	{
		return false;
	}
	
	/**
	 * 判断平面上的两条线段是否相交
	 * @param x1 第一条线段起点 X 坐标
	 * @param y1 第一条线段起点 Y 坐标
	 * @param x2 第一条线段端点 X 坐标
	 * @param y2 第一条线段端点 Y 坐标
	 * @param x3 第二条线段起点 X 坐标
	 * @param y3 第二条线段起点 Y 坐标
	 * @param x4 第二条线段端点 X 坐标
	 * @param y4 第二条线段端点 Y 坐标
	 * @return
	 */
	public static boolean inter2Line(final double x1, final double y1, final double x2, final double y2,
			final double x3, final double y3, final double x4, final double y4)
	{
		boolean bInter = false;
		double A1, B1, C1, A2, B2, C2, x = 0, y = 0;
		double delta, maxX1, minX1, maxY1, minY1, maxX2, minX2, maxY2, minY2;

		// 修正值
		delta = DBL_PRE;

		// 第一条线段的 X 坐标修正范围
		if (x1 >= x2)
		{
			maxX1 = x1 + delta;
			minX1 = x2 - delta;
		}
		else
		{
			maxX1 = x2 + delta;
			minX1 = x1 - delta;
		}

		// 第一条线段的 Y 坐标修正范围
		if (y1 >= y2)
		{
			maxY1 = y1 + delta;
			minY1 = y2 - delta;
		}
		else
		{
			maxY1 = y2 + delta;
			minY1 = y1 - delta;
		}

		// 第二条线段的 X 坐标修正范围
		if (x3 >= x4)
		{
			maxX2 = x3 + delta;
			minX2 = x4 - delta;
		}
		else
		{
			maxX2 = x4 + delta;
			minX2 = x3 - delta;
		}

		// 第二条线段的 Y 坐标修正范围
		if (y3 >= y4)
		{
			maxY2 = y3 + delta;
			minY2 = y4 - delta;
		}
		else
		{
			maxY2 = y4 + delta;
			minY2 = y3 - delta;
		}

		// 第一条线段的直线方程系数, A1*x + B1*y + C1 = 0
		A1 = y2 - y1;
		B1 = x1 - x2;
		C1 = -A1 * x1 - B1 * y1;

		// 第二条线段的直线方程系数, A2*x + B2*y + C2 = 0
		A2 = y4 - y3;
		B2 = x3 - x4;
		C2 = -A2 * x3 - B2 * y3;

		// 约束条件
		if (A1 == 0 && A2 == 0)
		{
			bInter = false;
		}
		else if (B1 == 0 && B2 == 0)
		{
			bInter = false;
		}
		else if (A1 == A2 && B1 == B2)
		{
			bInter = false;
		}
		else
		{
			// 交点坐标
			x = (B2 * C1 - B1 * C2) / (A2 * B1 - A1 * B2);
			y = (A2 * C1 - A1 * C2) / (A1 * B2 - A2 * B1);

			// 判断点是否在修正范围之内
			if ((x <= maxX1 && x >= minX1) && (x <= maxX2 && x >= minX2) &&
				(y <= maxY1 && y >= minY1) && (y <= maxY2 && y >= minY2))
			{
				bInter = true;
			}
			else
			{
				bInter = false;
			}
		}

		return bInter;
	}
	
	/**
	 * 判断两个矩形是否相交
	 * @param x11 第一个矩形左上角的 X 坐标
	 * @param y11 第一个矩形左上角的 Y 坐标
	 * @param x12 第一个矩形右下角的 X 坐标
	 * @param y12 第一个矩形右下角的 Y 坐标
	 * @param x21 第二个矩形左上角的 X 坐标
	 * @param y21 第二个矩形左上角的 Y 坐标
	 * @param x22 第二个矩形右下角的 X 坐标
	 * @param y22 第二个矩形右下角的 Y 坐标
	 * @return
	 */
	public static boolean inter2Rect(final double x11, final double y11, final double x12, final double y12,
			final double x21, final double y21, final double x22, final double y22)
	{
		if (x11 > x22 || x12 < x21 || y11 > y22 || y12 < y21)
		{
			// 不相交
			return false;
		}
		
		if (x21 >= x11 && x22 <= x12 &&
			y21 >= y11 && y22 <= y12)
		{
			// 第一个矩形包含第二个矩形
			return true;
		}

		if (x11 >= x21 && x12 <= x22 &&
			y11 >= y21 && y12 <= y22)
		{
			// 第二个矩形包含第一个矩形
			return true;
		}
		
		// 第一个矩形的顶边线是否与第二个矩形相交
		if (inter2Line(x11, y11, x12, y11, x21, y21, x22, y21)) return true; // 第二个矩形的顶边线
		if (inter2Line(x11, y11, x12, y11, x21, y22, x22, y22)) return true; // 第二个矩形的底边线
		if (inter2Line(x11, y11, x12, y11, x21, y21, x21, y22)) return true; // 第二个矩形的左边线
		if (inter2Line(x11, y11, x12, y11, x22, y21, x22, y22)) return true; // 第二个矩形的右边线
		
		// 第一个矩形的底边线是否与第二个矩形相交
		if (inter2Line(x11, y12, x12, y12, x21, y21, x22, y21)) return true; // 第二个矩形的顶边线
		if (inter2Line(x11, y12, x12, y12, x21, y22, x22, y22)) return true; // 第二个矩形的底边线
		if (inter2Line(x11, y12, x12, y12, x21, y21, x21, y22)) return true; // 第二个矩形的左边线
		if (inter2Line(x11, y12, x12, y12, x22, y21, x22, y22)) return true; // 第二个矩形的右边线
		
		// 第一个矩形的左边线是否与第二个矩形相交
		if (inter2Line(x11, y11, x11, y12, x21, y21, x22, y21)) return true; // 第二个矩形的顶边线
		if (inter2Line(x11, y11, x11, y12, x21, y22, x22, y22)) return true; // 第二个矩形的底边线
		if (inter2Line(x11, y11, x11, y12, x21, y21, x21, y22)) return true; // 第二个矩形的左边线
		if (inter2Line(x11, y11, x11, y12, x22, y21, x22, y22)) return true; // 第二个矩形的右边线
		
		// 第一个矩形的右边线是否与第二个矩形相交
		if (inter2Line(x12, y11, x12, y12, x21, y21, x22, y21)) return true; // 第二个矩形的顶边线
		if (inter2Line(x12, y11, x12, y12, x21, y22, x22, y22)) return true; // 第二个矩形的底边线
		if (inter2Line(x12, y11, x12, y12, x21, y21, x21, y22)) return true; // 第二个矩形的左边线
		if (inter2Line(x12, y11, x12, y12, x22, y21, x22, y22)) return true; // 第二个矩形的右边线
		
		return false;
	}
}

/**
 * OSD 直线类
 */
class OsdLine extends OsdGraph
{
	// 线宽
	private int m_nWidth = 1;
	public void setWidth(final int n) { if (n > 0) m_nWidth = n; }
	
	// 坐标
	private float m_x1 = 0;
	private float m_y1 = 0;
	private float m_x2 = 0;
	private float m_y2 = 0;
	public int getX1() { return (int)m_x1; }
	public int getY1() { return (int)m_y1; }
	public int getX2() { return (int)m_x2; }
	public int getY2() { return (int)m_y2; }
	public void setXY(final float x1, final float y1, final float x2, final float y2) { m_x1 = x1; m_y1 = y1; m_x2 = x2; m_y2 = y2; }
	
	/**
	 * 绘制
	 * @param canvas
	 */
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		try
		{
			// 设置笔宽
			m_paint.setStrokeWidth(m_nWidth);
			
			// 绘制直线
			canvas.drawLine(m_x1, m_y1, m_x2, m_y2, m_paint);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 判断是否与矩形相交
	 * @param x1 矩形左上角的 X 坐标
	 * @param y1 矩形左上角的 Y 坐标
	 * @param x2 矩形右下角的 X 坐标
	 * @param y2 矩形右下角的 Y 坐标
	 * @return
	 */
	protected boolean interOfRect(final double x1, final double y1, final double x2, final double y2)
	{
		// 包含
		if (m_x1 >= x1 && m_y1 >= y1 && m_x1 <= x2 && m_y1 <= y2 &&
			m_x2 >= x1 && m_y2 >= y1 && m_x2 <= x2 && m_y2 <= y2) return true;
		
		// 是否与矩形相交
		if (inter2Line(m_x1, m_y1, m_x2, m_y2, x1, y1, x2, y1)) return true; // 顶边线
		if (inter2Line(m_x1, m_y1, m_x2, m_y2, x1, y2, x2, y2)) return true; // 底边线
		if (inter2Line(m_x1, m_y1, m_x2, m_y2, x1, y1, x1, y2)) return true; // 左边线
		if (inter2Line(m_x1, m_y1, m_x2, m_y2, x2, y1, x2, y2)) return true; // 右边线
		
		return false;
	}
}

/**
 * OSD 矩形类
 */
class OsdRect extends OsdGraph
{
	// 填充
	private int m_nFill = 0;
	public void setFill(final int n) { if (n == 0 || n == 1) m_nFill = n; }
	
	// 线宽
	private int m_nWidth = 1;
	public void setWidth(final int n) { if (n > 0) m_nWidth = n; }
	
	// 坐标
	private float m_x1 = 0;
	private float m_y1 = 0;
	private float m_x2 = 0;
	private float m_y2 = 0;
	public int getX1() { return (int)m_x1; }
	public int getY1() { return (int)m_y1; }
	public int getX2() { return (int)m_x2; }
	public int getY2() { return (int)m_y2; }
	public void setXY(final float x1, final float y1, final float x2, final float y2) { m_x1 = x1; m_y1 = y1; m_x2 = x2; m_y2 = y2; }
	
	/**
	 * 绘制
	 * @param canvas
	 */
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		try
		{
			// 填充
			if (m_nFill == 1)
			{
				// 线宽
				m_nWidth = 1;
				
				// 填充
				m_paint.setStyle(Paint.Style.FILL);
			}
			else
			{
				// 笔画
				m_paint.setStyle(Paint.Style.STROKE);
			}
			
			// 设置笔宽
			m_paint.setStrokeWidth(m_nWidth);
			
			// 绘制矩形
			canvas.drawRect(m_x1, m_y1, m_x2, m_y2, m_paint);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 判断是否与矩形相交
	 * @param x1 矩形左上角的 X 坐标
	 * @param y1 矩形左上角的 Y 坐标
	 * @param x2 矩形右下角的 X 坐标
	 * @param y2 矩形右下角的 Y 坐标
	 * @return
	 */
	protected boolean interOfRect(final double x1, final double y1, final double x2, final double y2)
	{
		return inter2Rect(m_x1, m_y1, m_x2, m_y2, x1, y1, x2, y2);
	}
}

/**
 * OSD 圆类
 */
class OsdCircle extends OsdGraph
{
	// 填充
	private int m_nFill = 0;
	public void setFill(final int n) { if (n == 0 || n == 1) m_nFill = n; }
	
	// 线宽
	private int m_nWidth = 1;
//	public void setWidth(final int n) { if (n > 0) m_nWidth = n; }
	
	// 半径
	private int m_nRadius = 1;
	public int getRadius() { return m_nRadius; }
	public void setRadius(final int n) { if (n > 0) m_nRadius = n; }
	
	// 圆心坐标
	private float m_x = 0;
	private float m_y = 0;
	public int getX() { return (int)m_x; }
	public int getY() { return (int)m_y; }
	public void setXY(final float x, final float y) { m_x = x; m_y = y; }
	
	/**
	 * 绘制
	 * @param canvas
	 */
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		try
		{
			// 填充
			if (m_nFill == 1)
			{
				// 线宽
				m_nWidth = 1;
				
				// 填充
				m_paint.setStyle(Paint.Style.FILL);
			}
			else
			{
				// 笔画
				m_paint.setStyle(Paint.Style.STROKE);
			}
			
			// 设置笔宽
			m_paint.setStrokeWidth(m_nWidth);
			
			// 绘制圆
			canvas.drawCircle(m_x, m_y, m_nRadius, m_paint);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 判断是否与矩形相交
	 * @param x1 矩形左上角的 X 坐标
	 * @param y1 矩形左上角的 Y 坐标
	 * @param x2 矩形右下角的 X 坐标
	 * @param y2 矩形右下角的 Y 坐标
	 * @return
	 */
	protected boolean interOfRect(final double x1, final double y1, final double x2, final double y2)
	{
		// 圆心包含在矩形内
		if (m_x >= x1 && m_y >= y1 && m_x <= x2 && m_y <= y2) return true;
		
		return false;
	}
}

/**
 * OSD 字符串类
 */
class OsdString extends OsdGraph
{
	// 字体大小
	private int m_nSize = 16;
	public int getSize() { return m_nSize; }
	public void setSize(final int n) { m_nSize = n; }
	
	// 坐标
	private float m_x = 0;
	private float m_y = 0;
	public int getX() { return (int)m_x; }
	public int getY() { return (int)m_y; }
	public void setXY(final float x, final float y) { m_x = x; m_y = y; }
	
	// 字符串
	private String m_str = "";
	public void setStr(final String str) { m_str = str; }

	/**
	 * 绘制
	 * @param canvas
	 */
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		try
		{
			// 设置文本大小
			m_paint.setTextSize(m_nSize);
			
			// 绘制字符串
			canvas.drawText(m_str, m_x, m_y, m_paint);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 判断是否与矩形相交
	 * @param x1 矩形左上角的 X 坐标
	 * @param y1 矩形左上角的 Y 坐标
	 * @param x2 矩形右下角的 X 坐标
	 * @param y2 矩形右下角的 Y 坐标
	 * @return
	 */
	protected boolean interOfRect(final double x1, final double y1, final double x2, final double y2)
	{
		// 字符串宽度
		float w = m_paint.measureText(m_str);
		
		// 字符串矩形的坐标
		double sx1 = m_x - m_nSize;
		double sy1 = m_y;
		double sx2 = sx1 + w;
		double sy2 = sy1 + m_nSize;

		// 判断两个矩形是否相交或包含
		return inter2Rect(sx1, sy1, sx2, sy2, x1, y1, x2, y2);
	}
}
