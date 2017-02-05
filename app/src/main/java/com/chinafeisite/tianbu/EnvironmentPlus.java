package com.chinafeisite.tianbu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

/**
 * An {@link android.os.Environment} wrapper that adds
 * support for phones that have multiple external storage directories.
 */
public class EnvironmentPlus extends android.os.Environment
{
/*	public class MyClass extends Activity
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			if (EnvironmentPlus.doesExtraExternalStorageDirectoryExist(this))
			{
				// Found Two Or More
			}
			else
			{
				// There Is Only One
			}

			// Gets All Of The Storage Paths
			final File[] paths = EnvironmentPlus.getExternalStorageList(this);
		}
	}
*/
	/**
	 * SDCard File List
	 */
	private static File[] mStorageList = null;
	public static File[] getStorageList() { return mStorageList; }

	/**
	 * List Of The Potential Volume Daemons
	 */
	private static final File[] mVolumeDaemonList = new File[]
	{
		new File(getRootDirectory(), "etc/vold.fstab"),
		new File(getRootDirectory(), "etc/vold.conf")
	};

	/**
	 * Returns an array of files containing the paths to all of the external
	 * storage directories (Emulated/Removable). As a fall back, it reads in the volume daemon file
	 * and parses the contents.
	 * <p/>
	 * <b>Note:</b> This method takes advantage of a hidden method inside {@link StorageManager} which
	 * was not introduced until API 14 (ICE_CREAM_SANDWICH/4.0.1).
	 * <p/>
	 *
	 * @param context {@link Context} used to get StorageManager
	 */
	public static final File[] getExternalStorageList(Context context)
	{
		mStorageList = null;
		
		if (mStorageList == null)
		{
			try
			{
				mStorageList = (Build.VERSION.SDK_INT >= 14) ?
						getExternalStorageList((StorageManager)context.getSystemService(Context.STORAGE_SERVICE)) :
						getVolumeDaemonExternalStorageList();
			}
			catch (Exception e)
			{
			}
		}

		return mStorageList;
	}

	/**
	 * Checks to see if more than one external storage directory exists.
	 *
	 * @param context {@link Context} used to get StorageManager
	 * @return
	 */
	public static final boolean doesExtraExternalStorageDirectoryExist(Context context)
	{
		getExternalStorageList(context);
		
		return mStorageList != null ? mStorageList.length >= 2 : false;
	}

	/**
	 * Returns an array of files containing the paths to all of the external
	 * storage directories (Emulated/Removable) provided by the volume daemon config file.
	 *
	 * @return
	 */
	public static final File[] getVolumeDaemonExternalStorageList()
	{
		for (File daemon : mVolumeDaemonList)
		{
			try
			{
				if (daemon.exists() && daemon.canRead())
				{
					final String[] stringArray = readFileIntoStringArray(daemon);
					final List<File> fileList = new ArrayList<File>();
					
					for (String str : stringArray)
					{
						final File f = new File(str.split(" ")[2].split(":")[0]);
						
						if (!doesFileExistInList(fileList, f))
						{
							fileList.add(f);
						}
					}
					
					return (!fileList.isEmpty() ? fileList.toArray(new File[fileList.size()]) : null);
				}
			}
			catch (Exception e)
			{
			}
		}

		return null;
	}

	private static final File[] getExternalStorageList(StorageManager storageManager) throws Exception
	{
		final Method method = storageManager.getClass().getMethod("getVolumePaths");
		final String[] strList = (String[]) method.invoke(storageManager);
		final List<File> fileList = new ArrayList<File>();

		for (String path : strList)
		{
			final File file = new File(path);
			
			if (!doesFileExistInList(fileList, file))
			{
				fileList.add(file);
			}
		}

		return (!fileList.isEmpty() ? fileList.toArray(new File[fileList.size()]) : null);
	}

	private static final boolean doesFileExistInList(List<File> fileList, File newFile)
	{
		if (newFile == null || fileList == null)
		{
			// File Is Null Or List Is Null
			return true;
		}

		if (!newFile.exists())
		{
			// The File Doesn't Exist
			return true;
		}

		if (!newFile.isDirectory())
		{
			// File Is Not A Directory
			return true;
		}

		if (!newFile.canRead())
		{
			// Can't Read The File
			return true;
		}

		if (newFile.getTotalSpace() <= 0)
		{
			// File Has No Space
			// Filters Usbdisk Out
			return true;
		}

		if (newFile.getName().equalsIgnoreCase("tmp"))
		{
			// This Folder Showed Up On My Droid X, Filter It Out.
			return true;
		}

		if (fileList.contains(newFile))
		{
			// File Is In The List
			return true;
		}

		// Make Sure The File Isn't In The List As A Link Of Some Sort
		// More Of An In Depth Look
		for (File file : fileList)
		{
			if (file.getFreeSpace() == newFile.getFreeSpace() &&
				file.getUsableSpace() == newFile.getUsableSpace())
			{
				// Same Free/Usable Space
				// Must Be Same Files
				return true;
			}
		}

		// File Passed All Of My Tests
		return false;
	}

	private static final String[] readFileIntoStringArray(File file)
	{
		try
		{
			final Scanner scanner = new Scanner(file);
			final List<String> stringList = new ArrayList<String>();
			
			while (scanner.hasNext())
			{
				final String line = scanner.nextLine();
				
				if (line != null)
				{
					if (line.length() > 0)
					{
						if (!line.startsWith("#"))
						{
							stringList.add(line);
						}
					}
				}
			}
			
			return !stringList.isEmpty() ? stringList.toArray(new String[stringList.size()]) : null;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}

/**
 * 获取可移除设备类
 */
class GetRemoveableDevices
{
	private final static String TAG = "GetRemoveableDevice";

	public GetRemoveableDevices()
	{
		try
		{
			String strExternalStorage = "";
			String strInternalStorage = "";
			
			String[] strDirs = GetRemoveableDevices.getDirectories();
			ArrayList<String> listDirectories = new ArrayList<String>();

			for (String strDirectory : strDirs)
			{
				if (!strDirectory.contains("."))
				{
					listDirectories.add(strDirectory);
				}
			}

			if (listDirectories.size() >= 2)
			{
				strInternalStorage = listDirectories.get(0).toString();
				strExternalStorage = listDirectories.get(1).toString();
			}
			else if (listDirectories.size() < 2)
			{
				strInternalStorage = listDirectories.get(0).toString();
				strExternalStorage = null;
			}
			
			Log.d(TAG, "ExternalStorage: " + strExternalStorage);
			Log.d(TAG, "InternalStorage: " + strInternalStorage);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 获取目录
	 * @return
	 */
	public static String[] getDirectories()
	{
		Log.d(TAG, "getStorageDirectories");
		
		File tempFile;
		String strLineRead;
		String[] strSplits;
		String[] strDirectories = null;
		BufferedReader bufferedReader = null;
		ArrayList<String> arrayList = new ArrayList<String>();

		try
		{
			arrayList.clear(); // redundant, but what the hey
			bufferedReader = new BufferedReader(new FileReader("/proc/mounts"));

			while ((strLineRead = bufferedReader.readLine()) != null)
			{
				Log.d(TAG, "lineRead: " + strLineRead);
				
				strSplits = strLineRead.split(" ");

				// System external storage
				if (strSplits[1].equals(Environment.getExternalStorageDirectory().getPath()))
				{
					arrayList.add(strSplits[1]);
					Log.d(TAG, "gesd split 1: " + strSplits[1]);
					continue;
				}

				// skip if not external storage device
				if (!strSplits[0].contains("/dev/block/"))
				{
					continue;
				}

				// skip if mtdblock device
				if (strSplits[0].contains("/dev/block/mtdblock"))
				{
					continue;
				}

				// skip if not in /mnt node
				if (!strSplits[1].contains("/mnt"))
				{
					continue;
				}

				// skip these names
				if (strSplits[1].contains("/secure"))
				{
					continue;
				}

				if (strSplits[1].contains("/mnt/asec"))
				{
					continue;
				}

				// Eliminate if not a directory or fully accessible
				tempFile = new File(strSplits[1]);
				
				if (!tempFile.exists())
				{
					continue;
				}
				
				if (!tempFile.isDirectory())
				{
					continue;
				}
				
				if (!tempFile.canRead())
				{
					continue;
				}
				
				if (!tempFile.canWrite())
				{
					continue;
				}

				// Met all the criteria, assume sdcard
				arrayList.add(strSplits[1]);
			}
		}
		catch (FileNotFoundException e)
		{
		}
		catch (IOException e)
		{
		}
		finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				catch (IOException e)
				{
				}
			}
		}

		// Send list back to caller
		if (arrayList.size() == 0)
		{
			arrayList.add("sdcard not found");
		}
		
		strDirectories = new String[arrayList.size()];
		
		for (int i = 0; i < arrayList.size(); i++)
		{
			strDirectories[i] = arrayList.get(i);
		}
		
		return strDirectories;
	}
}

class StorageOptions
{
	public static int ms_nCount = 0;
	public static String[] ms_strPaths;
	public static String[] ms_strLabels;

//	private static Context ms_context;
	private static ArrayList<String> ms_listVold = new ArrayList<String>();

	public static void determineStorageOptions(Context context)
	{
		try
		{
//			ms_context = context.getApplicationContext();

			readVoldFile();

			testAndCleanList();

			setProperties();
		}
		catch (Exception e)
		{
		}
	}

	private static void readVoldFile()
	{
		/*
		 * Scan the /system/etc/vold.fstab file and look for lines like this:
		 * dev_mount sdcard /mnt/sdcard 1
		 * /devices/platform/s3c-sdhci.0/mmc_host/mmc0
		 * 
		 * When one is found, split it into its elements and then pull out the
		 * path to the that mount point and add it to the arraylist
		 * 
		 * some devices are missing the vold file entirely so we add a path here
		 * to make sure the list always includes the path to the first sdcard,
		 * whether real or emulated.
		 */

		try
		{
			ms_listVold.add("/mnt/sdcard");

			Scanner scanner = new Scanner(new File("/system/etc/vold.fstab"));
			
			while (scanner.hasNext())
			{
				String strLine = scanner.nextLine();
				
				if (strLine.startsWith("dev_mount"))
				{
					String[] strLineElements = strLine.split(" ");
					String strElement = strLineElements[2];

					if (strElement.contains(":"))
					{
						strElement = strElement.substring(0, strElement.indexOf(":"));
					}

					if (strElement.contains("usb"))
					{
						continue;
					}

					// don't add the default vold path
					// it's already in the list.
					if (!ms_listVold.contains(strElement))
					{
						ms_listVold.add(strElement);
					}
				}
			}
		}
		catch (Exception e)
		{
			// swallow - don't care
			e.printStackTrace();
		}
	}

	private static void testAndCleanList()
	{
		/*
		 * Now that we have a cleaned list of mount paths, test each one to make
		 * sure it's a valid and available path. If it is not, remove it from
		 * the list.
		 */

		try
		{
			for (int i = 0; i < ms_listVold.size(); i++)
			{
				String strVoldPath = ms_listVold.get(i);
				File strPath = new File(strVoldPath);
				
				if (!strPath.exists() || !strPath.isDirectory() || !strPath.canWrite())
				{
					ms_listVold.remove(i--);
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	private static void setProperties()
	{
		/*
		 * At this point all the paths in the list should be valid. Build the
		 * public properties.
		 */

		try
		{
			ArrayList<String> labelList = new ArrayList<String>();

//			int j = 0;
			
			if (ms_listVold.size() > 0)
			{
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
				{
					labelList.add("Auto");
				}
				else if (Build.VERSION.SDK_INT < 11)
//				else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
/*				{
					if (Environment.isExternalStorageRemovable())
					{
						labelList.add(ms_context.getString(R.string.text_external_storage) + " 1");
						j = 1;
					}
					else
					{
						labelList.add(ms_context.getString(R.string.text_internal_storage));
					}
				}
				else
				{
					if (!Environment.isExternalStorageRemovable() || Environment.isExternalStorageEmulated())
					{
						labelList.add(ms_context.getString(R.string.text_internal_storage));
					}
					else
					{
						labelList.add(ms_context.getString(R.string.text_external_storage) + " 1");
						j = 1;
					}
				}
*/
				if (ms_listVold.size() > 1)
				{
					for (int i = 1; i < ms_listVold.size(); i++)
					{
//						labelList.add(ms_context.getString(R.string.text_external_storage) + " " + (i + j));
					}
				}
			}

			ms_strLabels = new String[labelList.size()];
			labelList.toArray(ms_strLabels);

			ms_strPaths = new String[ms_listVold.size()];
			ms_listVold.toArray(ms_strPaths);

			ms_nCount = Math.min(ms_strLabels.length, ms_strPaths.length);

			/*
			 * don't need these anymore, clear the lists to reduce memory use and to
			 * prepare them for the next time they're needed.
			 */
			ms_listVold.clear();
		}
		catch (Exception e)
		{
		}
	}
}
