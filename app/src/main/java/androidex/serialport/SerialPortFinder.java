package androidex.serialport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Vector;

import android.util.Log;

public class SerialPortFinder
{
	public class Driver
	{
		public Driver(String name, String root)
		{
			m_strDriverName = name;
			m_strDeviceRoot = root;
		}

		private String m_strDriverName;
		private String m_strDeviceRoot;
		Vector<File> m_vecDevices = null;

		public Vector<File> getDevices()
		{
			if (m_vecDevices == null)
			{
				m_vecDevices = new Vector<File>();
				File fileDev = new File("/dev");
				File[] files = fileDev.listFiles();
				
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].getAbsolutePath().startsWith(m_strDeviceRoot))
					{
						Log.d(TAG, "Found new device: " + files[i]);
						m_vecDevices.add(files[i]);
					}
				}
			}
			
			return m_vecDevices;
		}

		public String getName()
		{
			return m_strDriverName;
		}
	}

	private static final String TAG = "SerialPort";

	private Vector<Driver> m_vecDrivers = null;

	Vector<Driver> getDrivers() throws IOException
	{
		if (m_vecDrivers == null)
		{
			String strLine;
			m_vecDrivers = new Vector<Driver>();
			LineNumberReader lnr = new LineNumberReader(new FileReader("/proc/tty/drivers"));
			
			while ((strLine = lnr.readLine()) != null)
			{
				// Issue 3:
				// Since driver name may contain spaces, we do not extract driver name with split()
				String strDriverName = strLine.substring(0, 0x15).trim();
				String[] strWidth = strLine.split(" +");
				
				if ((strWidth.length >= 5) && (strWidth[strWidth.length - 1].equals("serial")))
				{
					Log.d(TAG, "Found new driver " + strDriverName + " on " + strWidth[strWidth.length - 4]);
					m_vecDrivers.add(new Driver(strDriverName, strWidth[strWidth.length - 4]));
				}
			}
			
			lnr.close();
		}
		
		return m_vecDrivers;
	}

	public String[] getAllDevices()
	{
		// Parse each driver
		Iterator<Driver> itDriv;
		Vector<String> vecDevices = new Vector<String>();
		
		try
		{
			itDriv = getDrivers().iterator();
			
			while (itDriv.hasNext())
			{
				Driver driver = itDriv.next();
				Iterator<File> itDev = driver.getDevices().iterator();
				
				while (itDev.hasNext())
				{
					String strDevice = itDev.next().getName();
					String strValue = String.format("%s (%s)", strDevice, driver.getName());
					vecDevices.add(strValue);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return vecDevices.toArray(new String[vecDevices.size()]);
	}

	public String[] getAllDevicesPath()
	{
		// Parse each driver
		Iterator<Driver> itDriv;
		Vector<String> vecDevices = new Vector<String>();
		
		try
		{
			itDriv = getDrivers().iterator();
			
			while (itDriv.hasNext())
			{
				Driver driver = itDriv.next();
				Iterator<File> itDev = driver.getDevices().iterator();
				
				while (itDev.hasNext())
				{
					String strDevice = itDev.next().getAbsolutePath();
					vecDevices.add(strDevice);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return vecDevices.toArray(new String[vecDevices.size()]);
	}
}
