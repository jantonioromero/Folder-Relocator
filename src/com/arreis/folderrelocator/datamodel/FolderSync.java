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
	
	private static long sNextId = 0;
	
	private FolderSync(long id, String alias, String sourcePath, String destinationPath, boolean includeSubdirectories, boolean moveFiles)
	{
		this.mId = id;
		this.mAlias = alias;
		this.mSourcePath = sourcePath;
		this.mDestinationPath = destinationPath;
		this.mIncludeSubdirectories = includeSubdirectories;
		this.mMoveFiles = moveFiles;
	}
	
	public FolderSync(String alias, String sourcePath, String destinationPath, boolean includeSubdirectories, boolean moveFiles)
	{
		this(sNextId++, alias, sourcePath, destinationPath, includeSubdirectories, moveFiles);
	}
	
	public FolderSync(String alias, String sourcePath, String destinationPath)
	{
		this(alias, sourcePath, destinationPath, false, false);
	}
	
	public FolderSync(String sourcePath, String destinationPath)
	{
		this(sourcePath.substring(sourcePath.lastIndexOf('/') + 1), sourcePath, destinationPath, false, false);
	}
	
	public FolderSync()
	{
		this(-1, null, null, null, false, false);
	}
	
	public FolderSync duplicate()
	{
		return new FolderSync(mAlias, mSourcePath, mDestinationPath, mIncludeSubdirectories, mMoveFiles);
	}
	
	public boolean isValid()
	{
		return mId != -1;
	}
	
	public void validate()
	{
		if (mId == -1)
			mId = sNextId++;
	}
	
	public void update(FolderSync otherSync)
	{
		this.mAlias = otherSync.getAlias();
		this.mSourcePath = otherSync.getSourcePath();
		this.mDestinationPath = otherSync.getDestinationPath();
		this.mIncludeSubdirectories = otherSync.getIncludeSubdirectories();
		this.mMoveFiles = otherSync.getMoveFiles();
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
}
