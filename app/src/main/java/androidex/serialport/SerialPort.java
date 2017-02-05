package androidex.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;
import android.util.Log;

/**
 * 串口类
 */
public class SerialPort
{
	private static final String TAG = "SerialPort";

	// 退出
	private boolean m_bExit = false;
	
	private Handler m_handler = null;
	private FileDescriptor m_fd = null;
	private FileInputStream m_fis = null;
	private FileOutputStream m_fos = null;
	public InputStream getInputStream() { return m_fis; }
	public OutputStream getOutputStream() { return m_fos; }
	
	public SerialPort() {}
	public SerialPort(File device, int nBaudrate, int nFlags) throws SecurityException, IOException
	{
		// Check access permission
		if (!device.canRead() || !device.canWrite())
		{
			try
			{
				// Missing read/write permission, trying to chmod the file
/*				Process su = Runtime.getRuntime().exec("/system/bin/su");
				String strCmd666 = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
				su.getOutputStream().write(strCmd666.getBytes());
				
				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite())
				{
					throw new SecurityException();
				}
*/			
				// Missing read/write permission, trying to chmod the file
/*				Process su = Runtime.getRuntime().exec("/system/bin/su");
				String strCmd777 = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
				su.getOutputStream().write(strCmd666.getBytes());
				
				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite())
				{
					throw new SecurityException();
				}
*/				
				// Missing read/write permission, trying to chmod the file
/*				Process xbinsu = Runtime.getRuntime().exec("/system/xbin/su");
				String strCmd666 = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
				xbinsu.getOutputStream().write(strCmd666.getBytes());
				
				if ((xbinsu.waitFor() != 0) || !device.canRead() || !device.canWrite())
				{
					throw new SecurityException();
				}
*/				
				// Missing read/write permission, trying to chmod the file
				Process xbinsu = Runtime.getRuntime().exec("/system/xbin/su");
				String strCmd777 = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
				xbinsu.getOutputStream().write(strCmd777.getBytes());
				
				if ((xbinsu.waitFor() != 0) || !device.canRead() || !device.canWrite())
				{
					throw new SecurityException();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		// 打开
		m_fd = open(device.getAbsolutePath(), nBaudrate, nFlags);
		
		if (m_fd == null)
		{
			Log.e(TAG, "Native open returns null.");
			throw new IOException();
		}
		
		m_fis = new FileInputStream(m_fd);
		m_fos = new FileOutputStream(m_fd);
	}

	// 定义 JNI 方法
	public native String getTAG();
	private native static FileDescriptor open(String strPath, int nBaudrate, int nFlags);
	public native void close();

	static
	{
		// 加载"lib_serial_port.so"库
		System.loadLibrary("_serial_port");
	}
}
