package com.arreis.folderrelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncManager;

public class FolderSyncListActivity extends FragmentActivity implements FolderSyncListFragment.Callbacks
{
	private static final int REQUEST_NEW_FOLDERSYNC = 10;
	private static final int REQUEST_EDIT_FOLDERSYNC = 11;
	
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
		
		// TODO: If exposing deep links into your app, handle intents here.
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
			case R.id.menu_autosync:
			{
				if (mTwoPane)
				{
					ConfigureAutoSyncFragment fragment = new ConfigureAutoSyncFragment();
					getSupportFragmentManager().beginTransaction().replace(R.id.foldersync_detail_container, fragment).commit();
				}
				else
				{
					Intent intent = new Intent(this, ConfigureAutoSyncActivity.class);
					startActivity(intent);
				}
			}
				break;
			
			case R.id.menu_newSync:
			{
				onItemSelected(-1);
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
			folderSync = FolderSyncManager.getFolderSyncs(this).get(position);
		
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
}
