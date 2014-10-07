package com.arreis.folderrelocator.autosync;

import com.arreis.folderrelocator.FolderSyncListActivity;
import com.arreis.folderrelocator.R;
import com.arreis.folderrelocator.R.id;
import com.arreis.folderrelocator.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class ConfigureAutoSyncActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foldersync_detail);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (savedInstanceState == null)
		{
			Bundle arguments = new Bundle();
			arguments.putString(ConfigureAutoSyncFragment.ARG_ITEM_ID, getIntent().getStringExtra(ConfigureAutoSyncFragment.ARG_ITEM_ID));
			ConfigureAutoSyncFragment fragment = new ConfigureAutoSyncFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.foldersync_detail_container, fragment).commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == android.R.id.home) // Home or Up button
		{
			NavUtils.navigateUpTo(this, new Intent(this, FolderSyncListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
