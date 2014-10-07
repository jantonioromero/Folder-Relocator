package com.arreis.folderrelocator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arreis.folderrelocator.datamodel.FolderSync;

public class FolderSyncCell extends FrameLayout
{
	private TextView mTextView;
	
	public FolderSyncCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public FolderSyncCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public FolderSyncCell(Context context)
	{
		super(context);
	}
	
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		
		mTextView = (TextView) findViewById(R.id.textView);
	}
	
	public void setFolderSync(FolderSync sync)
	{
		mTextView.setText(sync.getAlias());
	}
}
