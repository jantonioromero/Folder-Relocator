package com.arreis.folderrelocator.datamodel.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

import com.arreis.folderrelocator.datamodel.FolderSync;

public class SyncAlarmManager
{
	private static final String ALARM_INTENT_ACTION = "com.arreis.folderrelocator.datamodel.alarm.ACTION";
	
	public static void setBootReceiverEnabled(Context context, boolean enabled)
	{
		ComponentName receiver = new ComponentName(context, BootReceiver.class);
		PackageManager pm = context.getPackageManager();
		
		pm.setComponentEnabledSetting(receiver, enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}
	
	public static void setAlarm(Context context, FolderSync sync)
	{
		if (sync.getId() != -1)
		{
			// Cancel previous alarm with the same id
			cancelAlarm(context, sync);
			
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			Intent intent = new Intent(context, SyncAlarmService.class);
			intent.setAction(ALARM_INTENT_ACTION);
			intent.putExtra(SyncAlarmService.EXTRA_LONG_ALARMID, sync.getId());
			PendingIntent pi = PendingIntent.getService(context, (int) sync.getId(), intent, 0);
			
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * sync.getRepeatInterval(), 1000 * sync.getRepeatInterval(), pi);
		}
	}
	
	public static void cancelAlarm(Context context, FolderSync sync)
	{
		PendingIntent pi = PendingIntent.getService(context, (int) sync.getId(), new Intent(context, SyncAlarmService.class), 0);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pi);
	}
}
