package com.arreis.folderrelocator.explorer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arreis.folderrelocator.R;
import com.arreis.folderrelocator.R.id;
import com.arreis.util.AFileListManager;

public class FolderCell extends FrameLayout
{
	private TextView mTextView;
	private ImageView mUpIconImageView;
	
	public FolderCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public FolderCell(Context context)
	{
		super(context);
	}
	
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		
		mTextView = (TextView) findViewById(R.id.textView);
		mUpIconImageView = (ImageView) findViewById(R.id.upIcon_image);
	}
	
	public void setFolderName(String name)
	{
		if (AFileListManager.DIRECTORY_UP.equals(name))
		{
			mTextView.setVisibility(View.GONE);
			mUpIconImageView.setVisibility(View.VISIBLE);
		}
		else
		{
			mTextView.setText(name);
			mTextView.setVisibility(View.VISIBLE);
			mUpIconImageView.setVisibility(View.GONE);
		}
	}
}
