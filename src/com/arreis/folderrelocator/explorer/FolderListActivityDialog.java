package com.arreis.folderrelocator.explorer;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager.LayoutParams;

import com.arreis.folderrelocator.R;

public class FolderListActivityDialog extends FolderListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		int dialogHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.explorerDialogHeight), getResources().getDisplayMetrics());
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, dialogHeight);
	}
}
