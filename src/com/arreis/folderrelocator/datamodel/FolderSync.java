package com.arreis.folderrelocator.datamodel;

import java.io.Serializable;

public class FolderSync implements Serializable, Cloneable
{
	private static final long serialVersionUID = 7199431748208435892L;
	
	private long mId;
	private String mAlias;
	private String mSourcePath;
	private String mDestinationPath;
	private boolean mIncludeSubdirectories;
	private boolean mMoveFiles;
	private long mRepeatInteval;
	
	public FolderSync(long id, String alias, String sourcePath, String destinationPath, boolean includeSubdirectories, boolean moveFiles, long repeatInterval)
	{
		this.mId = id;
		this.mAlias = alias;
		this.mSourcePath = sourcePath;
		this.mDestinationPath = destinationPath;
		this.mIncludeSubdirectories = includeSubdirectories;
		this.mMoveFiles = moveFiles;
		mRepeatInteval = repeatInterval;
	}
	
	public FolderSync()
	{
		this(-1, null, null, null, false, false, 0);
	}
	
	public FolderSync duplicate()
	{
		return new FolderSync(mId, mAlias, mSourcePath, mDestinationPath, mIncludeSubdirectories, mMoveFiles, mRepeatInteval);
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
	
	public void runSynchronization()
	{
		
	}
}
