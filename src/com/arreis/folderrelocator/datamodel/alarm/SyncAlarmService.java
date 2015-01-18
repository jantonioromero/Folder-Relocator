package com.arreis.folderrelocator.datamodel.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.arreis.folderrelocator.R;
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
					showSyncNotification(folderSync);
					Toast.makeText(getApplicationContext(), "Scheduled sync", Toast.LENGTH_SHORT).show();
					folderSync.runSynchronization();
					hideNotification(folderSync);
				}
			}
		}
	}
	
	private void showSyncNotification(FolderSync sync)
	{
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle(getString(R.string.message_syncInProgress_title)).setContentText(String.format(getString(R.string.message_syncInProgress_description), sync.getSourcePath(), sync.getDestinationPath()));
		
//		Intent resultIntent = new Intent(this, ResultActivity.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		stackBuilder.addParentStack(ResultActivity.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		mBuilder.setContentIntent(resultPendingIntent);
		
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify((int) sync.getId(), builder.build());
	}
	
	private void hideNotification(FolderSync sync)
	{
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel((int) sync.getId());
	}
}
