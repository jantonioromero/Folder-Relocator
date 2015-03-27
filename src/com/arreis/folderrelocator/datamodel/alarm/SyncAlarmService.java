package com.arreis.folderrelocator.datamodel.alarm;

import android.app.IntentService;
import android.content.Intent;

import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncDatabaseHelper;

public class SyncAlarmService extends IntentService
{
	public static final String EXTRA_LONG_ALARMID = "com.arreis.folderrelocator.datamodel.alarm.EXTRA_ALARMID";
	
	public SyncAlarmService()
	{
		super("AlarmService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		long syncId = intent.getLongExtra(EXTRA_LONG_ALARMID, -1);
		if (syncId != -1)
		{
			FolderSync folderSync = new FolderSyncDatabaseHelper(getApplicationContext()).getFolderSync(syncId);
			if (folderSync != null)
			{
				if (folderSync.getRepeatInterval() == 0)
				{
					SyncAlarmManager.cancelAlarm(getApplicationContext(), folderSync);
				}
				else
				{
//					Toast.makeText(getApplicationContext(), "Scheduled sync", Toast.LENGTH_SHORT).show();
					folderSync.runSynchronization(this);
				}
			}
		}
	}
}
