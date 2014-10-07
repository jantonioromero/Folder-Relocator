package com.arreis.folderrelocator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arreis.folderrelocator.datamodel.FolderSync;

public class FolderSyncCell extends LinearLayout
{
	public interface FolderSyncCellListener
	{
		public void syncButtonPressed(FolderSync sync);
	}
	
	private FolderSyncCellListener mListener;
	private FolderSync mFolderSync;
	
	private TextView mTextView;
	private ImageButton mSyncButton;
	
//	public FolderSyncCell(Context context, AttributeSet attrs, int defStyle)
//	{
//		super(context, attrs, defStyle);
//	}
	
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
		mSyncButton = (ImageButton) findViewById(R.id.sync_button);
		mSyncButton.setFocusable(false); // Workaround for ImageButton inside cell. Does not work from XML.
		
		mSyncButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mListener != null)
					mListener.syncButtonPressed(mFolderSync);
			}
		});
	}
	
	public void setListener(FolderSyncCellListener listener)
	{
		mListener = listener;
	}
	
	public void setFolderSync(FolderSync sync)
	{
		mFolderSync = sync;
		mTextView.setText(sync.getAlias());
	}
}
