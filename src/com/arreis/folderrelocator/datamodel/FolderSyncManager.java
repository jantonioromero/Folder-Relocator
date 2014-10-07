package com.arreis.folderrelocator.datamodel;

import java.util.ArrayList;

import android.content.Context;

import com.arreis.util.APersistenceManager;

public class FolderSyncManager
{
	private static final String kFolderSyncFileName = "folder_syncs.dat";
	
	private static ArrayList<FolderSync> sFolderSyncs;
	
	public static ArrayList<FolderSync> getFolderSyncs(Context context)
	{
		return getFolderSyncs(context, false);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<FolderSync> getFolderSyncs(Context context, boolean reloadFromFile)
	{
		if (reloadFromFile || sFolderSyncs == null)
		{
			sFolderSyncs = (ArrayList<FolderSync>) APersistenceManager.readFromFileInternal(context, kFolderSyncFileName);
		}
		
		if (sFolderSyncs == null)
		{
			sFolderSyncs = new ArrayList<FolderSync>();
		}
		
		return sFolderSyncs;
	}
	
	public static void addSync(Context context, FolderSync sync)
	{
		getFolderSyncs(context).add(sync);
	}
	
	public static void editSync(Context context, FolderSync sync)
	{
		FolderSync target = findSync(context, sync.getId());
		
		if (target != null)
			target.update(sync);
	}
	
	public static void removeSync(Context context, FolderSync sync)
	{
		FolderSync target = findSync(context, sync.getId());
		
		if (target != null)
			getFolderSyncs(context).remove(target);
	}
	
	public static void saveSyncList(Context context)
	{
		APersistenceManager.writeToFileInternal(context, kFolderSyncFileName, sFolderSyncs);
	}
	
	private static FolderSync findSync(Context context, long id)
	{
		FolderSync res = null;
		
		ArrayList<FolderSync> syncList = getFolderSyncs(context);
		for (FolderSync sync : syncList)
		{
			if (id == sync.getId())
			{
				res = sync;
				break;
			}
		}
		
		return res;
	}
}
