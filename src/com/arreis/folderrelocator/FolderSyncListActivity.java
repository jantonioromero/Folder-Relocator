package com.arreis.folderrelocator;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncDatabaseHelper;

public class FolderSyncListActivity extends ActionBarActivity implements FolderSyncListFragment.Callbacks
{
//	private static final int REQUEST_NEW_FOLDERSYNC = 10;
//	private static final int REQUEST_EDIT_FOLDERSYNC = 11;
	
	private static final String PREFS_FIRST_RUN = "com.arreis.FolderRelocator.PREFS_FIRST_RUN";
	
	private static final int MAX_SYNCS = 10;
	
	private boolean mTwoPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foldersync_list);
		
		if (findViewById(R.id.foldersync_detail_container) != null)
		{
			mTwoPane = true;
			((FolderSyncListFragment) getSupportFragmentManager().findFragmentById(R.id.foldersync_list)).setActivateOnItemClick(true);
		}
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean(PREFS_FIRST_RUN, true))
		{
			prefs.edit().putBoolean(PREFS_FIRST_RUN, false).commit();
			showTutorial();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_foldersync_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
//			case R.id.menu_autosync:
//			{
//				if (mTwoPane)
//				{
//					ConfigureAutoSyncFragment fragment = new ConfigureAutoSyncFragment();
//					getSupportFragmentManager().beginTransaction().replace(R.id.foldersync_detail_container, fragment).commit();
//				}
//				else
//				{
//					Intent intent = new Intent(this, ConfigureAutoSyncActivity.class);
//					startActivity(intent);
//				}
//			}
//				break;
//			
			case R.id.menu_newSync:
			{
				if (new FolderSyncDatabaseHelper(this).getFolderSyncs().size() < MAX_SYNCS)
				{
					onItemSelected(-1);
				}
				else
				{
					new AlertDialog.Builder(this).setMessage(String.format(getString(R.string.error_tooManySyncs), MAX_SYNCS)).setPositiveButton(android.R.string.ok, null).create().show();
				}
			}
				break;
			
			default:
				return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	@Override
	public void onItemSelected(int position)
	{
		FolderSync folderSync = null;
		if (position != -1)
//			folderSync = FolderSyncManager.getFolderSyncs(this).get(position);
			folderSync = new FolderSyncDatabaseHelper(this).getFolderSyncs().get(position);
		
		if (mTwoPane)
		{
			FolderSyncDetailFragment fragment = new FolderSyncDetailFragment();
			if (folderSync != null)
			{
				Bundle arguments = new Bundle();
				arguments.putSerializable(FolderSyncDetailFragment.ARG_FOLDERSYNC, folderSync);
				fragment.setArguments(arguments);
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.foldersync_detail_container, fragment).commit();
		}
		else
		{
			Intent intent = new Intent(this, FolderSyncDetailActivity.class);
			if (folderSync != null)
			{
				intent.putExtra(FolderSyncDetailFragment.ARG_FOLDERSYNC, folderSync);
			}
			startActivity(intent);
		}
	}
	
	@Override
	public void onItemUpdated(int position)
	{
		((FolderSyncListFragment) getSupportFragmentManager().findFragmentById(R.id.foldersync_list)).updateDBDataAndRefresh();
	}
	
	private void showTutorial()
	{
		
	}
}
