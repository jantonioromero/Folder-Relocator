package com.arreis.folderrelocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncDatabaseHelper;
import com.arreis.folderrelocator.datamodel.alarm.SyncAlarmManager;
import com.arreis.folderrelocator.explorer.FolderListActivity;
import com.arreis.folderrelocator.explorer.FolderListActivityDialog;
import com.arreis.util.AFileListManager.AFileCopyDestinationFileExistsBehavior;

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
	private RadioButton mRenameRadioButton;
	private RadioButton mOverwriteRadioButton;
	private RadioButton mDoNotCopyRadioButton;
	private CheckBox mAutoSyncCheck;
	private Spinner mAutoSyncSpinner;
	
	private String[] mAutoSyncIntervalStrings;
	private int[] mAutoSyncIntervalValues;
	
	private FolderSync mFolderSync;
	private FolderSync mTempFolderSync;
	
	public FolderSyncDetailFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (getArguments() != null && getArguments().containsKey(ARG_FOLDERSYNC))
		{
			mFolderSync = (FolderSync) getArguments().getSerializable(ARG_FOLDERSYNC);
		}
		else
		{
			mFolderSync = new FolderSync();
		}
		
		mTempFolderSync = mFolderSync.duplicate();
		
		mAutoSyncIntervalStrings = getResources().getStringArray(R.array.autosync_intervals);
		
		String[] autoSyncValues = getResources().getStringArray(R.array.autosync_interval_values);
		int count = autoSyncValues.length;
		mAutoSyncIntervalValues = new int[count];
		for (int i = 0; i < count; i++)
		{
			mAutoSyncIntervalValues[i] = Integer.parseInt(autoSyncValues[i]);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_foldersync_detail, container, false);
		
		mAliasEdit = (EditText) rootView.findViewById(R.id.edit_alias);
		mAliasEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				mTempFolderSync.setAlias(mAliasEdit.getText().toString());
			}
		});
		
		mSourceButton = (Button) rootView.findViewById(R.id.button_source);
		mSourceButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				goToFolderList(true);
			}
		});
		
		mDestinationButton = (Button) rootView.findViewById(R.id.button_destination);
		mDestinationButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				goToFolderList(false);
			}
		});
		
		mIncludeSubDirsCheck = (CheckBox) rootView.findViewById(R.id.check_includeSubDirs);
		mIncludeSubDirsCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				mTempFolderSync.setIncludeSubdirectories(isChecked);
//				updateUI();
			}
		});
		
		mMoveFilesCheck = (CheckBox) rootView.findViewById(R.id.check_moveFiles);
		mMoveFilesCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				mTempFolderSync.setMoveFiles(isChecked);
//				updateUI();
			}
		});
		
		mRenameRadioButton = (RadioButton) rootView.findViewById(R.id.rename_radio);
		mRenameRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mTempFolderSync.setOnFileExistsBehavior(AFileCopyDestinationFileExistsBehavior.RENAME);
				}
			}
		});
		
		mOverwriteRadioButton = (RadioButton) rootView.findViewById(R.id.overwrite_radio);
		mOverwriteRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mTempFolderSync.setOnFileExistsBehavior(AFileCopyDestinationFileExistsBehavior.OVERWRITE);
				}
			}
		});
		
		mDoNotCopyRadioButton = (RadioButton) rootView.findViewById(R.id.doNothing_radio);
		mDoNotCopyRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mTempFolderSync.setOnFileExistsBehavior(AFileCopyDestinationFileExistsBehavior.DO_NOT_COPY);
				}
			}
		});
		
		mAutoSyncCheck = (CheckBox) rootView.findViewById(R.id.check_autosync);
		mAutoSyncCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				mTempFolderSync.setRepeatInterval(isChecked ? 0 : mAutoSyncIntervalValues[mAutoSyncSpinner.getSelectedItemPosition()]);
				mAutoSyncSpinner.setEnabled(isChecked);
			}
		});
		
		mAutoSyncSpinner = (Spinner) rootView.findViewById(R.id.autosync_spinner);
		mAutoSyncSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mAutoSyncIntervalStrings));
		mAutoSyncSpinner.setSelection(0, false);
		mAutoSyncSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				mTempFolderSync.setRepeatInterval(mAutoSyncIntervalValues[position]);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		
		((Button) rootView.findViewById(R.id.save_button)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (checkFields())
				{
					if (mOverwriteRadioButton.isChecked())
					{
						new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_overwrite_title).setMessage(R.string.dialog_overwrite_message).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								saveSync();
							}
						}).setNeutralButton(R.string.dialog_overwrite_changeToRename, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								mRenameRadioButton.setChecked(true);
								saveSync();
							}
						}).setNegativeButton(android.R.string.no, null).create().show();
					}
					else
					{
						saveSync();
					}
				}
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
		mAliasEdit.setText(mTempFolderSync.getAlias() == null ? "" : mTempFolderSync.getAlias());
		mSourceButton.setText(mTempFolderSync.getSourcePath() == null ? getString(R.string.syncSource) : mTempFolderSync.getSourcePath());
		mDestinationButton.setText(mTempFolderSync.getDestinationPath() == null ? getString(R.string.syncDestination) : mTempFolderSync.getDestinationPath());
		mIncludeSubDirsCheck.setChecked(mTempFolderSync.getIncludeSubdirectories());
		mMoveFilesCheck.setChecked(mTempFolderSync.getMoveFiles());
		
		switch (mTempFolderSync.getOnFileExistsBehavior())
		{
			case RENAME:
				mRenameRadioButton.setChecked(true);
				break;
			
			case OVERWRITE:
				mOverwriteRadioButton.setChecked(true);
				break;
			
			case DO_NOT_COPY:
				mDoNotCopyRadioButton.setChecked(true);
				break;
		}
		
		long autosyncInterval = mTempFolderSync.getRepeatInterval();
		if (autosyncInterval == 0)
		{
			mAutoSyncCheck.setChecked(false);
			mAutoSyncSpinner.setEnabled(false);
		}
		else
		{
			mAutoSyncCheck.setChecked(true);
			mAutoSyncSpinner.setEnabled(true);
			
			for (int i = 0; i < mAutoSyncIntervalStrings.length; i++)
			{
				if (autosyncInterval == mAutoSyncIntervalValues[i])
				{
					mAutoSyncSpinner.setSelection(i);
					break;
				}
			}
		}
	}
	
	private boolean checkFields()
	{
		int errorMessageResId = 0;
		
		if (mTempFolderSync.getAlias().length() == 0)
		{
			errorMessageResId = R.string.error_noAlias;
		}
		else if (mTempFolderSync.getSourcePath() == null || mTempFolderSync.getDestinationPath() == null)
		{
			errorMessageResId = R.string.error_noSourceOrDest;
		}
		else if (mTempFolderSync.getSourcePath().equals(mTempFolderSync.getDestinationPath()))
		{
			errorMessageResId = R.string.error_sourceAndDestAreSame;
		}
		
		if (errorMessageResId != 0)
			Toast.makeText(getActivity(), errorMessageResId, Toast.LENGTH_SHORT).show();
		
		return (errorMessageResId == 0);
	}
	
	private void saveSync()
	{
		FolderSyncDatabaseHelper helper = new FolderSyncDatabaseHelper(getActivity());
		long rows = helper.update(mTempFolderSync);
		if (rows == 0)
		{
			long id = helper.insert(mTempFolderSync);
			mTempFolderSync.setId(id);
		}
		
		if (getActivity() instanceof FolderSyncDetailActivity)
			getActivity().finish();
		
		SyncAlarmManager.setAlarm(getActivity(), mTempFolderSync);
		
		if (getActivity() instanceof FolderSyncListFragment.Callbacks)
		{
			((FolderSyncListFragment.Callbacks) getActivity()).onItemUpdated(0);
		}
	}
	
	private void goToFolderList(boolean requestSource)
	{
		Intent intent = new Intent(getActivity(), getResources().getBoolean(R.bool.asTablet) ? FolderListActivityDialog.class : FolderListActivity.class);
		
		if (requestSource)
		{
			intent.putExtra(FolderListActivity.ARG_REQUESTISSOURCE, true);
			intent.putExtra(FolderListActivity.ARG_SELECTEDPATH, mTempFolderSync.getSourcePath());
			startActivityForResult(intent, REQUEST_SOURCEPATH);
		}
		else
		{
			intent.putExtra(FolderListActivity.ARG_REQUESTISSOURCE, false);
			intent.putExtra(FolderListActivity.ARG_SELECTEDPATH, mTempFolderSync.getDestinationPath());
			startActivityForResult(intent, REQUEST_DESTINATIONPATH);
		}
	}
}
