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
	
	public static void saveFolderSyncs(Context context)
	{
		APersistenceManager.writeToFileInternal(context, kFolderSyncFileName, sFolderSyncs);
	}
}
