package com.arreis.folderrelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class FolderSyncDetailActivity extends ActionBarActivity
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
			arguments.putString(FolderSyncDetailFragment.ARG_FOLDERSYNC, getIntent().getStringExtra(FolderSyncDetailFragment.ARG_FOLDERSYNC));
			FolderSyncDetailFragment fragment = new FolderSyncDetailFragment();
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
