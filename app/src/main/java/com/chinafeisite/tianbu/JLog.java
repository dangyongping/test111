package com.chinafeisite.tianbu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志类
 */
public class JLog
{
	// TAG 最大长度
//	private static final int TAG_MAX_LEN = 32;
	
	// 日志最大长度, 2MB
	private static final long LOG_MAX_LEN = 2 * 1024 * 1204;
	
	// 日志路径
	private static String m_strPath = "";
	
	/**
	 * 生成路径
	 * @param strFileName
	 */
	private static void mkdir(final String strFileName)
	{
		try
		{
			// 文件
			File file = new File(strFileName);
			
			// 检测文件
			if (!file.exists() || !file.isDirectory())
			{
				// 生成路径
				file.mkdir();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 删除文件
	 * @param strFileName
	 * @return
	 */
	private static String deleteFile(final String strFileName)
	{
		String str = null;
		
		// 文件
		File file = new File(strFileName);
		
		// 检测文件
		if (!file.exists())
		{
			str = strFileName + " does not exist.";
			return str;
		}
		
		// 检测文件
		if (!file.isFile())
		{
			str = strFileName + " is not a file.";
			return str;
		}
		
		try
		{
			// 删除文件
			file.delete();
		}
		catch (Exception e)
		{
			return "Delete file error.";
		}
		
		return null;
	}
	
	/**
	 * 检测路径
	 * @return
	 */
	private static boolean checkPath()
	{
		try
		{
			// 文件
			File file = new File(m_strPath);
			
			// 检测文件
			if (file == null || !file.exists() || !file.isFile()) return false;
			
			return true;
		}
		catch (Exception e)
		{
		}
		
		return false;
	}
	
	/**
	 * 设置路径
	 * 
	 * @param str
	 */
	public static void setPath(final String str)
	{
		try
		{
			// 路径
			m_strPath = str;
			mkdir(m_strPath);
			if (!m_strPath.endsWith("/")) m_strPath += "/";
			m_strPath += "log.txt";
			
			// 文件
			File file = new File(m_strPath);
			
			// 检测文件
			if (!file.exists() || !file.isFile())
			{
				// 新建文件
				file.createNewFile();
				file = new File(m_strPath);
				
				// 输出流
				String strText = "";
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(strText.getBytes());
				fos.flush();
				fos.close();
			}
			
			// 检测长度
			long len = file.length();
			if (len < LOG_MAX_LEN) return;
			
			// 删除文件
			deleteFile(m_strPath);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 写调试日志
	 * @param strTag
	 * @param strMsg
	 */
	public static void d(final String strTag, final String strMsg)
	{
		try
		{
			if (strMsg == null || strMsg.length() < 1) return;
			
			String strTAG = strTag;
			if (strTAG == null) strTAG = "";
/*			if (strTAG.length() > TAG_MAX_LEN) strTAG = strTAG.substring(0, TAG_MAX_LEN);

			// 长度
			int nLen = strTAG.length();
					
			if (nLen < TAG_MAX_LEN)
			{
				for (int i = 0; i < TAG_MAX_LEN - nLen; i++)
				{
					strTAG += " ";
				}
			}
*/			
			// 日期和时间
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
			String strTime = sdf.format(new Date());
			
			// 日志
			String strLevel = "D";
			String strLog = strLevel + " " + strTime + " " + strTAG + " " + strMsg + " \r\n";
			System.out.println(strLog);
			
			// 检测路径
			if (!checkPath()) return;
			
			// 随机访问文件, 读写方式
			RandomAccessFile file = new RandomAccessFile(m_strPath, "rw");

			// 文件指针移到文件尾
			file.seek(file.length());
			
			// 写入字节
			file.writeBytes(strLog);
//			strLog = "\r\n";
//			file.writeBytes(strLog);
			file.close();
		}
		catch (Exception e)
		{
		}
	}
}
