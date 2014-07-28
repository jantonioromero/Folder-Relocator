package com.arreis.folderrelocator.datamodel;

import java.io.Serializable;

public class FolderSync implements Serializable, Cloneable
{
	private static final long serialVersionUID = 7199431748208435892L;
	
	private String mAlias;
	private String mSourcePath;
	private String mDestinationPath;
	private boolean mIncludeSubdirectories;
	private boolean mMoveFiles;
	
	public FolderSync(String alias, String sourcePath, String destinationPath, boolean includeSubdirectories, boolean moveFiles)
	{
		this.mAlias = alias;
		this.mSourcePath = sourcePath;
		this.mDestinationPath = destinationPath;
		this.mIncludeSubdirectories = includeSubdirectories;
		this.mMoveFiles = moveFiles;
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
		this(null, null, null, false, false);
	}
	
	public FolderSync duplicate()
	{
		return new FolderSync(mAlias, mSourcePath, mDestinationPath, mIncludeSubdirectories, mMoveFiles);
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
