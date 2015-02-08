package com.arreis.folderrelocator.explorer;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager.LayoutParams;

public class FolderListActivityDialog extends FolderListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		int dialogHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 640, getResources().getDisplayMetrics());
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, dialogHeight);
	}
}
