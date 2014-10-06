package com.arreis.folderrelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.arreis.folderrelocator.datamodel.FolderSync;

public class FolderSyncDetailActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foldersync_detail);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		FolderSyncDetailFragment fragment = new FolderSyncDetailFragment();
		FolderSync syncToEdit = (FolderSync) getIntent().getSerializableExtra(FolderSyncDetailFragment.ARG_FOLDERSYNC);
		if (syncToEdit != null)
		{
			Bundle arguments = new Bundle();
			arguments.putSerializable(FolderSyncDetailFragment.ARG_FOLDERSYNC, syncToEdit);
			fragment.setArguments(arguments);
		}
		getSupportFragmentManager().beginTransaction().add(R.id.foldersync_detail_container, fragment).commit();
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
