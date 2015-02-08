package com.arreis.folderrelocator.explorer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arreis.folderrelocator.R;

public class FolderCell extends LinearLayout
{
	public interface IFolderCellListener
	{
		public void folderCellSelectButtonPressed(int index);
	}
	
	public enum FolderCellType
	{
		NONE, INTERNAL_ROOT, EXTERNAL_ROOT, FOLDER
	}
	
	private TextView mTextView;
	private ImageView mIconImageView;
	private Button mSelectButton;
	
	private int mIndex;
	private IFolderCellListener mListener;
	
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
		
		mTextView = (TextView) findViewById(R.id.text_title);
		mIconImageView = (ImageView) findViewById(R.id.image_icon);
		mSelectButton = (Button) findViewById(R.id.button_select);
		
		mSelectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mListener != null)
					mListener.folderCellSelectButtonPressed(mIndex);
			}
		});
	}
	
	public void setListener(IFolderCellListener listener)
	{
		this.mListener = listener;
	}
	
	public void setFolderName(String name, int index)
	{
		mIndex = index;
		
		mTextView.setText(name);
		mTextView.setVisibility(View.VISIBLE);
		mIconImageView.setVisibility(View.GONE);
	}
	
	public void setFolderType(FolderCellType type)
	{
		mSelectButton.setVisibility(type == FolderCellType.FOLDER ? View.VISIBLE : View.GONE);
		
		switch (type)
		{
			case INTERNAL_ROOT:
				mIconImageView.setImageResource(R.drawable.ic_device);
				mIconImageView.setVisibility(View.VISIBLE);
				break;
			
			case EXTERNAL_ROOT:
				mIconImageView.setImageResource(R.drawable.ic_card);
				mIconImageView.setVisibility(View.VISIBLE);
				break;
			
			case FOLDER:
				mIconImageView.setImageResource(R.drawable.ic_folder);
				mIconImageView.setVisibility(View.VISIBLE);
				break;
			
			default:
				mIconImageView.setVisibility(View.INVISIBLE);
				break;
		}
	}
}
