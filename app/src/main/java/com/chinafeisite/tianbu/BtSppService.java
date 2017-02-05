package com.chinafeisite.tianbu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 蓝牙串口服务类
 */
public class BtSppService
{
	// 调试
	private static final boolean DEBUG = false;
	private static final String  TAG   = "BluetoothSPPService";

	// 创建服务器端套接字时的 SDP 记录名称
	private static final String NAME = "BluetoothSPP";
	
	// 成员变量
	private int m_nState;
	private Handler m_handler;
	private JAcceptThread m_acceptThread;
	private JConnectThread m_connectThread;
	private JConnectedThread m_connectedThread;
	private final BluetoothAdapter m_btAdapter;
	
	// 是否已停止
	private static boolean ms_bStop = false;

	// 当前连接状态
	public static final int STATE_NONE       = 0; // 无
	public static final int STATE_LISTEN     = 1; // 监听
	public static final int STATE_CONNECTING = 2; // 正在连接
	public static final int STATE_CONNECTED  = 3; // 已连接
	public static final int STATE_LOST       = 4; // 断开连接

	// 蓝牙串口服务消息
	public static final int MSG_BT_STATE_CHANGE = 1; // 状态改变
	public static final int MSG_BT_READ         = 2; // 读数据
	public static final int MSG_BT_WRITE        = 3; // 写数据
	public static final int MSG_BT_DEVICE_NAME  = 4; // 设备名称
	public static final int MSG_BT_TOAST        = 5;

	// 接收句柄键值
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST       = "toast";
	
	private static String ms_strDeviceLost    = "设备连接中断。";
	private static String ms_strUnableConnect = "无法连接到设备。";
	
	public static void setDeviceLost(final String str) { ms_strDeviceLost = str; }
	public static void setUnableConnect(final String str) { ms_strUnableConnect = str; }

	// 蓝牙串口服务 UUID
	private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// 读时间, 毫秒
	private static int ms_nReadTime = 0;
	public static int getReadTime() { return ms_nReadTime; }
	public static void addReadTime() { ms_nReadTime++; }
//	public static void resetReadTime() { ms_nReadTime = 0; }
	
	// 正在读
	private static boolean ms_bReading = false;
	public static boolean isReading() { return ms_bReading; }
	public static void setReading(final boolean b) { ms_bReading = b; if (b) ms_nReadTime = 0; }
	
	// 延迟
	private static int ms_nDelay = 50;
	public static final int MAX_DELAY = 2000;
	public static int getDelay() { return ms_nDelay; }
	public static void setDelay(final int n) { ms_nDelay = (n >= 0 && n <= MAX_DELAY) ? n : ms_nDelay; }
	
	// 缓冲区
//	private static int ms_nBytes = 0;                                // 缓冲区字节数
//	public static final int BUFFER_SIZE = 64;                        // 缓冲区大小
	public static final int BUFFER_SIZE = 1024;                      // 缓冲区大小
//	private static byte[] ms_buffer = new byte[BUFFER_SIZE];         // 缓冲区
//	private static StringBuffer ms_strBuffer = new StringBuffer();   // 字符串缓冲区
//	private static List<Byte> ms_listBuffer = new ArrayList<Byte>(); // 缓冲区链表
//	public static int getBytes() { return ms_nBytes; }
//	public static void setBytes(final int n) { ms_nBytes = n; }
//	public static byte[] getBuffer() { return ms_buffer; }
//	public static void setBuffer(final int i, final byte b) { if (i < 0 || i > BUFFER_SIZE - 1) return; ms_buffer[i] = b; }
//	public static StringBuffer getStrBuffer() { return ms_strBuffer; }
//	public static List<Byte> getListBuffer() { return ms_listBuffer; }

	/**
	 * 构造函数
	 * @param context
	 * @param handler
	 */
	public BtSppService(Context context, Handler handler)
	{
		m_btAdapter = BluetoothAdapter.getDefaultAdapter();
		m_nState    = STATE_NONE;
		m_handler   = handler;
	}
	
	/**
	 * 获取缓冲区字节
	 * @param i
	 * @return
	 */
/*	public static byte getBuffer(final int i)
	{
		if (i < 0 || i > BUFFER_SIZE - 1)
		{
			return (byte)0;
		}
		else
		{
			return ms_buffer[i];
		}
	}
*/	
	/**
	 * 清空缓冲区
	 */
/*	public static void clearBuffer()
	{
		ms_nBytes = 0;
		ms_buffer = new byte[BUFFER_SIZE];
//		ms_strBuffer = new StringBuffer();
//		ms_listBuffer.clear();
	}
*/
	/**
	 * 清空缓冲区
	 * @param nByteNum
	 */
/*	public static void clearBuffer(final int nByteNum)
	{
		if (nByteNum < 1 || nByteNum >= BUFFER_SIZE) return;
		
		// 缓冲区字节数
		ms_nBytes -= nByteNum;
		
		if (ms_nBytes < 1)
		{
			// 清空缓冲区
			clearBuffer();
			return;
		}
		
		byte[] buffer = new byte[ms_nBytes];
		
		for (int i = 0, j = nByteNum; i < ms_nBytes && j < BUFFER_SIZE; i++, j++)
		{
			buffer[i] = ms_buffer[j];
		}

		// 缓冲区
		ms_buffer = new byte[BUFFER_SIZE];
		
		for (int i = 0; i < ms_nBytes; i++)
		{
			ms_buffer[i] = buffer[i];
		}
	}
*/
	/**
	 * 清空缓冲区
	 * @param mark 标记
	 */
/*	public static void clearBuffer(byte[] mark)
	{
		try
		{
			int nMark = -1;
			boolean bMark = false;
			
			// 检测标记
			if (mark == null)
			{
				// 清空缓冲区
				clearBuffer();
				return;
			}
			
			// 缓冲区长度
			int nLenBuff = BUFFER_SIZE;
			
			// 标记长度
			int nLenMark = mark.length;
			
			if (nLenMark < 1)
			{
				// 清空缓冲区
				clearBuffer();
				return;
			}
			
			for (int i = 0; i <= nLenBuff - nLenMark; i++)
			{
				int j = 0;
				bMark = false;
				
				for (j = 0; j < nLenMark; j++)
				{
					int k = i + j;
					
					if (mark[j] == ms_buffer[k])
					{
						bMark = true;
					}
					else
					{
						bMark = false;
						break;
					}
				}
				
				if (bMark && j == nLenMark)
				{
					nMark = i;
					break;
				}
			}
			
			if (nMark == -1 || (bMark && nMark == 0))
			{
				return;
			}
			
			// 缓冲区字节数
			ms_nBytes -= nMark;
			
			if (ms_nBytes < 1)
			{
				// 清空缓冲区
				clearBuffer();
				return;
			}
			
			byte[] buffer = new byte[ms_nBytes];
			
			for (int i = 0, j = nMark; i < ms_nBytes && j < nLenBuff; i++, j++)
			{
				buffer[i] = ms_buffer[j];
			}

			// 缓冲区
			ms_buffer = new byte[BUFFER_SIZE];
			
			for (int i = 0; i < ms_nBytes; i++)
			{
				ms_buffer[i] = buffer[i];
			}
		}
		catch (Exception e)
		{
		}
	}
*/	
	/**
	 * 清空缓冲区
	 * @param mark 标记
	 * @param nCommByteNum 指令的字节数
	 */
/*	public static void clearBuffer(byte[] mark, final int nCommByteNum)
	{
		try
		{
			int nMark = -1;
			boolean bMark = false;
			
			// 检测标记
			if (mark == null)
			{
				// 清空缓冲区
				clearBuffer();
				return;
			}
			
			// 缓冲区长度
			int nLenBuff = BUFFER_SIZE;
			
			// 标记长度
			int nLenMark = mark.length;
			
			if (nLenMark < 1)
			{
				// 清空缓冲区
				clearBuffer();
				return;
			}
			
			for (int i = 0; i <= nLenBuff - nLenMark; i++)
			{
				int j = 0;
				bMark = false;
				
				for (j = 0; j < nLenMark; j++)
				{
					int k = i + j;
					
					if (mark[j] == ms_buffer[k])
					{
						bMark = true;
					}
					else
					{
						bMark = false;
						break;
					}
				}
				
				if (bMark && j == nLenMark)
				{
					if (nMark > -1 && i - nMark >= nCommByteNum)
					{
						break;
					}
					
					nMark = i;
				}
			}
			
			if (nMark == -1 || (bMark && nMark == 0))
			{
				return;
			}
			
			// 缓冲区字节数
			ms_nBytes -= nMark;
			
			if (ms_nBytes < 1)
			{
				// 清空缓冲区
				clearBuffer();
				return;
			}
			
			byte[] buffer = new byte[ms_nBytes];
			
			for (int i = 0, j = nMark; i < ms_nBytes && j < nLenBuff; i++, j++)
			{
				buffer[i] = ms_buffer[j];
			}

			// 缓冲区
			ms_buffer = new byte[BUFFER_SIZE];
			
			for (int i = 0; i < ms_nBytes; i++)
			{
				ms_buffer[i] = buffer[i];
			}
		}
		catch (Exception e)
		{
		}
	}
*/
	/**
	 * 设置处理器
	 * @param handler
	 */
	public void setHandler(Handler handler)
	{
		m_handler = null;
		m_handler = handler;
	}

	/**
	 * 设置当前状态
	 * @param state
	 */
	private synchronized void setState(int state)
	{
		try
		{
			if (DEBUG) Log.d(TAG, "setState() " + m_nState + " -> " + state);
			
			m_nState = state;
	
			m_handler.obtainMessage(MSG_BT_STATE_CHANGE, state, -1).sendToTarget();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取当前状态
	 */
	public synchronized int getState()
	{
		return m_nState;
	}

	/**
	 * 启动蓝牙串口服务
	 */
	public synchronized void start()
	{
		try
		{
			if (DEBUG) Log.d(TAG, "start");

			// 取消连接线程
			if (m_connectThread != null)
			{
				m_connectThread.cancel();
				m_connectThread = null;
			}

			// 取消已连接线程
			if (m_connectedThread != null)
			{
				m_connectedThread.cancel();
				m_connectedThread = null;
			}

			// 启动监听线程
			if (m_acceptThread == null)
			{
				m_acceptThread = new JAcceptThread();
				m_acceptThread.start();
			}
			
			setState(STATE_LISTEN);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 启动连接线程
	 * @param device 蓝牙设备
	 */
	public synchronized void connect(BluetoothDevice device)
	{
		try
		{
			if (DEBUG) Log.d(TAG, "connect to: " + device);

			// 取消连接线程
			if (m_nState == STATE_CONNECTING)
			{
				if (m_connectThread != null)
				{
					m_connectThread.cancel();
					m_connectThread = null;
				}
			}

			// 取消已连接线程
			if (m_connectedThread != null)
			{
				m_connectedThread.cancel();
				m_connectedThread = null;
			}

			// 启动连接线程
			m_connectThread = new JConnectThread(device);
			m_connectThread.start();
			
			setState(STATE_CONNECTING);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 启动连接线程
	 * @param socket 蓝牙套接字
	 * @param device 蓝牙设备
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device)
	{
		try
		{
			if (DEBUG) Log.d(TAG, "connected");

			if (m_connectThread != null)
			{
				m_connectThread.cancel();
				m_connectThread = null;
			}

			if (m_connectedThread != null)
			{
				m_connectedThread.cancel();
				m_connectedThread = null;
			}

			if (m_acceptThread != null)
			{
				m_acceptThread.cancel();
				m_acceptThread = null;
			}

			m_connectedThread = new JConnectedThread(socket);
			m_connectedThread.start();

			// 发送包含设备名称的消息
			Message msg = m_handler.obtainMessage(MSG_BT_DEVICE_NAME);
			Bundle bundle = new Bundle();
			bundle.putString(DEVICE_NAME, device.getName());
			msg.setData(bundle);
			m_handler.sendMessage(msg);

			setState(STATE_CONNECTED);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 停止所有线程
	 */
	public synchronized void stop()
	{
		try
		{
			if (DEBUG) Log.d(TAG, "stop");
			
			ms_bStop = true;
			
			if (m_connectThread != null)
			{
				m_connectThread.cancel();
				m_connectThread = null;
			}
			
			if (m_connectedThread != null)
			{
				m_connectedThread.cancel();
				m_connectedThread = null;
			}
			
			if (m_acceptThread != null)
			{
				m_acceptThread.cancel();
				m_acceptThread = null;
			}
			
			setState(STATE_NONE);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 以异步方式写入连接线程
	 * @param out 写字节
	 */
	public void write(byte[] out)
	{
		try
		{
			JConnectedThread thread;

			// 同步对象
			synchronized (this)
			{
				if (m_nState != STATE_CONNECTED) return;
				
				thread = m_connectedThread;
			}

			// 异步写入
			if (thread != null) thread.write(out);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 连接失败
	 */
	private void connectionFailed()
	{
		try
		{
			setState(STATE_LISTEN);

			// 发送失败消息
			Message msg = m_handler.obtainMessage(MSG_BT_TOAST);
			Bundle bundle = new Bundle();
			bundle.putString(TOAST, ms_strUnableConnect);
//			bundle.putString(TOAST, "Unable to connect device");
			msg.setData(bundle);
			m_handler.sendMessage(msg);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 连接丢失
	 */
	private void connectionLost()
	{
		try
		{
			setState(STATE_LISTEN);

			// 发送丢失消息
			Message msg = m_handler.obtainMessage(MSG_BT_TOAST);
			Bundle bundle = new Bundle();
			bundle.putString(TOAST, ms_strDeviceLost);
//			bundle.putString(TOAST, "Device connection was lost");
			msg.setData(bundle);
			m_handler.sendMessage(msg);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 接受线程类
	 */
	private class JAcceptThread extends Thread
	{
		// 服务端套接字
		private final BluetoothServerSocket mmServerSocket;

		public JAcceptThread()
		{
			BluetoothServerSocket tmp = null;

			// 创建监听服务端套接字
			try
			{
				if (m_btAdapter != null)
				{
					tmp = m_btAdapter.listenUsingRfcommWithServiceRecord(NAME, SPP_UUID);
				}
			}
			catch (IOException e)
			{
				Log.e(TAG, "listen() failed", e);
			}
			
			mmServerSocket = tmp;
		}

		public void run()
		{
			if (DEBUG) Log.d(TAG, "BEGIN mAcceptThread" + this);
			
			setName("AcceptThread");
			BluetoothSocket socket = null;

			while (m_nState != STATE_CONNECTED)
			{
				try
				{
					if (mmServerSocket != null)
					{
						socket = mmServerSocket.accept();
					}
				}
				catch (IOException e)
				{
					Log.e(TAG, "accept() failed", e);
					break;
				}

				if (socket != null)
				{
					synchronized (BtSppService.this)
					{
						switch (m_nState)
						{
						case STATE_LISTEN:
						case STATE_CONNECTING:
							try
							{
								connected(socket, socket.getRemoteDevice());
							}
							catch (Exception e)
							{
							}
							break;
							
						case STATE_NONE:
						case STATE_CONNECTED:
							try
							{
								socket.close();
							}
							catch (IOException e)
							{
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			
			if (DEBUG) Log.i(TAG, "END mAcceptThread");
		}

		public void cancel()
		{
			if (DEBUG) Log.d(TAG, "cancel " + this);
			
			try
			{
				if (mmServerSocket != null) mmServerSocket.close();
			}
			catch (IOException e)
			{
				Log.e(TAG, "close() of server failed", e);
			}
		}
	}

	/**
	 * 连接线程类
	 */
	private class JConnectThread extends Thread
	{
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public JConnectThread(BluetoothDevice device)
		{
			mmDevice = device;
			BluetoothSocket tmp = null;

			try
			{
				if (device != null)
				{
					tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
				}
			}
			catch (IOException e)
			{
				Log.e(TAG, "create() failed", e);
			}
			
			mmSocket = tmp;
		}

		public void run()
		{
			Log.i(TAG, "BEGIN mConnectThread");
			
			setName("ConnectThread");

			try
			{
				if (m_btAdapter != null)
				{
					m_btAdapter.cancelDiscovery();
				}
			}
			catch (Exception e)
			{
			}

			try
			{
				if (mmSocket != null)
				{
					mmSocket.connect();
				}
			}
			catch (IOException e)
			{
				connectionFailed();
				
				try
				{
					if (mmSocket != null)
					{
						mmSocket.close();
					}
				}
				catch (IOException e2)
				{
					Log.e(TAG, "unable to close() socket during connection failure", e2);
				}
				
				try
				{
					start();
				}
				catch (Exception e3)
				{
				}
				
				return;
			}

			try
			{
				synchronized (BtSppService.this)
				{
					m_connectThread = null;
				}
			}
			catch (Exception e)
			{
			}

			connected(mmSocket, mmDevice);
		}

		public void cancel()
		{
			try
			{
				if (mmSocket != null) mmSocket.close();
			}
			catch (IOException e)
			{
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * 已连接线程类
	 */
	private class JConnectedThread extends Thread
	{
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public JConnectedThread(BluetoothSocket socket)
		{
			Log.d(TAG, "create ConnectedThread");
			
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try
			{
				if (socket != null)
				{
					tmpIn = socket.getInputStream();
					tmpOut = socket.getOutputStream();
				}
			}
			catch (IOException e)
			{
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run()
		{
			Log.i(TAG, "BEGIN mConnectedThread");

			int nBytes = 0;
			byte[] buffer = new byte[BUFFER_SIZE];

//			while (true)
			while (!ms_bStop)
			{
				try
				{
					if (!ms_bStop && mmInStream != null)
					{
						// 读输入流
						nBytes = mmInStream.read(buffer);
						
						if (nBytes > 0)
						{
/*							for (int i = 0; i < nBytes; i++)
							{
								int j = ms_nBytes + i;
								
								if (j >= 0 && j < BUFFER_SIZE)
								{
									// 缓冲区
									ms_buffer[j] = buffer[i];
								}
								
								// 缓冲区链表
//								ms_listBuffer.add(buffer[i]);
							}
*/							
							// 字符串缓冲区
//							String strBuffer = new String(buffer, 0, nBytes);
//							ms_strBuffer.append(strBuffer);
							
							// 缓冲区字节数
//							ms_nBytes += nBytes;

							// 检测缓冲区字节数
//							if (ms_nBytes >= BUFFER_SIZE - 1)
							{
								// 清空缓冲区
//								clearBuffer();
							}
							
							// 发送读数据消息
							m_handler.obtainMessage(MSG_BT_READ, nBytes, -1, buffer).sendToTarget();
							
							// 缓冲区长度
/*							int nLenStr = ms_strBuffer.length();
							int nLenList = ms_listBuffer.size();

							// 检测缓冲区字节数
							if (nLenStr   >= BUFFER_SIZE - 1 ||
								nLenList  >= BUFFER_SIZE - 1 ||
								ms_nBytes >= BUFFER_SIZE - 1)
							{
								// 清空缓冲区
								clearBuffer();
							}

							// 发送读数据消息
							m_handler.obtainMessage(MSG_BT_READ, nBytes, -1, buffer).sendToTarget();*/
						}
					}
				}
				catch (IOException e)
				{
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}
			}
		}

		/**
		 * 写入输出流
		 * @param buffer 字节缓冲器
		 */
		public void write(byte[] buffer)
		{
			try
			{
				if (mmOutStream != null)
				{
					mmOutStream.write(buffer);
				}

				m_handler.obtainMessage(MSG_BT_WRITE, -1, -1, buffer).sendToTarget();
			}
			catch (IOException e)
			{
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel()
		{
			try
			{
				if (mmSocket != null) mmSocket.close();
				if (mmInStream != null) mmInStream.close();
				if (mmOutStream != null) mmOutStream.close();
			}
			catch (IOException e)
			{
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
