package com.arreis.folderrelocator.datamodel;

import java.io.File;
import java.io.Serializable;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.arreis.folderrelocator.R;
import com.arreis.util.AFileListManager;
import com.arreis.util.AFileListManager.AFileCopyDestinationFileExistsBehavior;

public class FolderSync implements Serializable, Cloneable
{
	private static final long serialVersionUID = 7199431748208435892L;
	
	public static final String BROADCAST_SYNC_ACTION = "com.arreis.folderrelocator.datamodel.FolderSync.BROADCAST_SYNC_ACTION";
	public static final String BROADCAST_SYNC_ARG_STATUS = "com.arreis.folderrelocator.datamodel.FolderSync.BROADCAST_SYNC_ARG_STATUS";
	public static final String BROADCAST_SYNC_ARG_STATUS_STARTED = "com.arreis.folderrelocator.datamodel.FolderSync.BROADCAST_SYNC_ARG_STATUS_STARTED";
	public static final String BROADCAST_SYNC_ARG_STATUS_FINISHED = "com.arreis.folderrelocator.datamodel.FolderSync.BROADCAST_SYNC_ARG_STATUS_FINISHED";
	
	private static boolean mIsSynchronizing;
	
	private long mId;
	private String mAlias;
	private String mSourcePath;
	private String mDestinationPath;
	private AFileCopyDestinationFileExistsBehavior mOnFileExistsBehavior;
	private boolean mIncludeSubdirectories;
	private boolean mMoveFiles;
	private long mRepeatInteval;
	
	public FolderSync(long id, String alias, String sourcePath, String destinationPath, AFileCopyDestinationFileExistsBehavior onFileExistsBehavior, boolean includeSubdirectories, boolean moveFiles, long repeatInterval)
	{
		this.mId = id;
		this.mAlias = alias;
		this.mSourcePath = sourcePath;
		this.mDestinationPath = destinationPath;
		this.mIncludeSubdirectories = includeSubdirectories;
		this.mOnFileExistsBehavior = onFileExistsBehavior;
		this.mMoveFiles = moveFiles;
		mRepeatInteval = repeatInterval;
	}
	
	public FolderSync(long id, String alias, String sourcePath, String destinationPath, int onFileExistsBehaviorIdentifier, boolean includeSubdirectories, boolean moveFiles, long repeatInterval)
	{
		this(id, alias, sourcePath, destinationPath, getBehaviorFromInt(onFileExistsBehaviorIdentifier), includeSubdirectories, moveFiles, repeatInterval);
	}
	
	public FolderSync()
	{
		this(-1, null, null, null, AFileCopyDestinationFileExistsBehavior.RENAME, false, false, 0);
	}
	
	public FolderSync duplicate()
	{
		return new FolderSync(mId, mAlias, mSourcePath, mDestinationPath, mOnFileExistsBehavior, mIncludeSubdirectories, mMoveFiles, mRepeatInteval);
	}
	
	public void setId(long id)
	{
		mId = id;
	}
	
	public long getId()
	{
		return mId;
	}
	
	public String getAlias()
	{
		return mAlias;
	}
	
	public String getSourcePath()
	{
		return mSourcePath;
	}
	
	public String getDestinationPath()
	{
		return mDestinationPath;
	}
	
	public AFileCopyDestinationFileExistsBehavior getOnFileExistsBehavior()
	{
		return mOnFileExistsBehavior;
	}
	
	public boolean getIncludeSubdirectories()
	{
		return mIncludeSubdirectories;
	}
	
	public boolean getMoveFiles()
	{
		return mMoveFiles;
	}
	
	public long getRepeatInterval()
	{
		return mRepeatInteval;
	}
	
	public void setAlias(String mAlias)
	{
		this.mAlias = mAlias;
	}
	
	public void setSourcePath(String mSourcePath)
	{
		this.mSourcePath = mSourcePath;
	}
	
	public void setDestinationPath(String mDestinationPath)
	{
		this.mDestinationPath = mDestinationPath;
	}
	
	public void setOnFileExistsBehavior(AFileCopyDestinationFileExistsBehavior mOnFileExistsBehavior)
	{
		this.mOnFileExistsBehavior = mOnFileExistsBehavior;
	}
	
	public void setIncludeSubdirectories(boolean mIncludeSubdirectories)
	{
		this.mIncludeSubdirectories = mIncludeSubdirectories;
	}
	
	public void setMoveFiles(boolean mMoveFiles)
	{
		this.mMoveFiles = mMoveFiles;
	}
	
	public void setRepeatInterval(long repeatInterval)
	{
		mRepeatInteval = repeatInterval;
	}
	
	public static AFileCopyDestinationFileExistsBehavior getBehaviorFromInt(int identifier)
	{
		AFileCopyDestinationFileExistsBehavior res = AFileCopyDestinationFileExistsBehavior.DO_NOT_COPY;
		switch (identifier)
		{
			case 1:
				res = AFileCopyDestinationFileExistsBehavior.RENAME;
				break;
			
			case 2:
				res = AFileCopyDestinationFileExistsBehavior.OVERWRITE;
				break;
			
			case 0:
			default:
				res = AFileCopyDestinationFileExistsBehavior.DO_NOT_COPY;
				break;
		
		}
		return res;
	}
	
	public static int getIdentifierFromBehavior(AFileCopyDestinationFileExistsBehavior behavior)
	{
		int res = 0;
		switch (behavior)
		{
			case RENAME:
				res = 1;
				break;
			
			case OVERWRITE:
				res = 2;
				break;
			
			case DO_NOT_COPY:
			default:
				res = 0;
				break;
		
		}
		return res;
	}
	
	public static boolean isSinchronizing()
	{
		return mIsSynchronizing;
	}
	
	public synchronized void runSynchronization(Context context)
	{
		mIsSynchronizing = true;
		broadcastSyncStatus(context, false);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ab_logo).setContentTitle(context.getString(R.string.message_syncInProgress_title)).setContentText(String.format(context.getString(R.string.message_syncInProgress_description), getSourcePath(), getDestinationPath()));
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify((int) getId(), builder.build());
		
		AFileListManager.copyFile(new File(mSourcePath), new File(mDestinationPath), mIncludeSubdirectories, mMoveFiles, mOnFileExistsBehavior);
		
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel((int) getId());
		
		mIsSynchronizing = false;
		broadcastSyncStatus(context, true);
	}
	
	private void broadcastSyncStatus(Context context, boolean finished)
	{
		Intent intent = new Intent(BROADCAST_SYNC_ACTION);
		intent.putExtra(BROADCAST_SYNC_ARG_STATUS, finished ? BROADCAST_SYNC_ARG_STATUS_STARTED : BROADCAST_SYNC_ARG_STATUS_FINISHED);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
}
