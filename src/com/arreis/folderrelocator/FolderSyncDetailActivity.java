package com.arreis.folderrelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.arreis.folderrelocator.datamodel.FolderSync;

public class FolderSyncDetailActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_foldersync_detail);
		
		FolderSync syncToEdit = (FolderSync) getIntent().getSerializableExtra(FolderSyncDetailFragment.ARG_FOLDERSYNC);
		
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setTitle(syncToEdit == null ? R.string.newSync : R.string.editSync);
		
		FolderSyncDetailFragment fragment = new FolderSyncDetailFragment();
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
