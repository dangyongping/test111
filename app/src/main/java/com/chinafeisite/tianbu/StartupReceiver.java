package com.chinafeisite.tianbu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 启动接收器类
 */
public class StartupReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
//		Intent mainIntent = new Intent(context, MainActivity.class);
		
		// 需要在 AndroidManifest.xml 中添加如下内容:
/*		
		<receiver android:name="com.chinafeisite.tianbu.StartupReceiver" >
	        <intent-filter>
	            <action android:name="android.intent.action.BOOT_COMPLETED" />
	        </intent-filter>
	    </receiver>
*/		
		// 在广播接收器中显示 Activity, 必须设置 FLAG_ACTIVITY_NEW_TASK 标志
//		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(mainIntent);
	}
}
