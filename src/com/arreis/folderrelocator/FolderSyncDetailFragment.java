package com.arreis.folderrelocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.arreis.folderrelocator.datamodel.FolderSync;

public class FolderSyncDetailFragment extends Fragment
{
	public static final String ARG_FOLDERSYNC = "ARG_FOLDERSYNC";
	
	private static final int REQUEST_SOURCEPATH = 10;
	private static final int REQUEST_DESTINATIONPATH = 11;
	
	private EditText mAliasEdit;
	private Button mSourceButton;
	private Button mDestinationButton;
	private CheckBox mIncludeSubDirsCheck;
	private CheckBox mMoveFilesCheck;
	
	private FolderSync mFolderSync;
	private FolderSync mTempFolderSync;
	
	public FolderSyncDetailFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ARG_FOLDERSYNC))
		{
			mFolderSync = (FolderSync) getArguments().getSerializable(ARG_FOLDERSYNC);
		}
		else
		{
			mFolderSync = new FolderSync();
		}
		
		mTempFolderSync = mFolderSync.duplicate();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_foldersync_detail, container, false);
		
		mAliasEdit = (EditText) rootView.findViewById(R.id.edit_alias);
		
		mSourceButton = (Button) rootView.findViewById(R.id.button_source);
		mSourceButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), FolderListActivity.class);
				// TODO: Pasar ruta inicial, si hay alguna
				startActivityForResult(intent, REQUEST_SOURCEPATH);
			}
		});
		
		mDestinationButton = (Button) rootView.findViewById(R.id.button_destination);
		mDestinationButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), FolderListActivity.class);
				// TODO: Pasar ruta inicial, si hay alguna
				startActivityForResult(intent, REQUEST_DESTINATIONPATH);
			}
		});
		
		mIncludeSubDirsCheck = (CheckBox) rootView.findViewById(R.id.check_includeSubDirs);
		mIncludeSubDirsCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				mTempFolderSync.setIncludeSubdirectories(!mTempFolderSync.getIncludeSubdirectories());
				updateUI();
			}
		});
		
		mMoveFilesCheck = (CheckBox) rootView.findViewById(R.id.check_moveFiles);
		mMoveFilesCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				mTempFolderSync.setMoveFiles(!mTempFolderSync.getMoveFiles());
				updateUI();
			}
		});
		
		updateUI();
		
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_SOURCEPATH)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				mTempFolderSync.setSourcePath(data.getStringExtra(FolderListActivity.ARG_SELECTEDPATH));
				updateUI();
			}
		}
		else if (requestCode == REQUEST_DESTINATIONPATH)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				mTempFolderSync.setDestinationPath(data.getStringExtra(FolderListActivity.ARG_SELECTEDPATH));
				updateUI();
			}
		}
		else
			super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void updateUI()
	{
		mAliasEdit.setText(mFolderSync.getAlias() == null ? "" : mTempFolderSync.getAlias());
		mSourceButton.setText(mTempFolderSync.getSourcePath() == null ? getString(R.string.syncSource) : String.format("%s - %s", getString(R.string.syncSource), mTempFolderSync.getSourcePath()));
		mDestinationButton.setText(mTempFolderSync.getDestinationPath() == null ? getString(R.string.syncDestination) : String.format("%s - %s", getString(R.string.syncDestination), mTempFolderSync.getDestinationPath()));
		
	}
}
