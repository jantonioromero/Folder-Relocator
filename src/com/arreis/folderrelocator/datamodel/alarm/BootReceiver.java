package com.arreis.folderrelocator.datamodel.alarm;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncDatabaseHelper;

public class BootReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		ArrayList<FolderSync> folderSyncs = new FolderSyncDatabaseHelper(context).getFolderSyncs();
		for (FolderSync sync : folderSyncs)
		{
			SyncAlarmManager.setAlarm(context, sync);
		}
	}
	
}
