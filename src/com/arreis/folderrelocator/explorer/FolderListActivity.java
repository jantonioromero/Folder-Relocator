package com.arreis.folderrelocator.explorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.arreis.folderrelocator.R;
import com.arreis.util.AFileListManager.AFileListShowBackMode;
import com.arreis.util.AFileListManager.AFileListShowFilesMode;
import com.arreis.util.APublicFileListManager;

public class FolderListActivity extends ActionBarActivity
{
	public static final String ARG_SELECTEDPATH = "ARG_SELECTEDPATH";
	
	private TextView mCurrentDirTextView;
	private Button mSelectButton;
	
	private APublicFileListManager mFileListManager;
	private FolderListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_CANCELED);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		String initialPath = null;
		if (savedInstanceState != null)
			initialPath = savedInstanceState.getString(ARG_SELECTEDPATH);
		else
			initialPath = getIntent().getStringExtra(ARG_SELECTEDPATH);
		
		mFileListManager = new APublicFileListManager(AFileListShowFilesMode.DIRECTORIES_ONLY);
		mFileListManager.setShowBackMode(AFileListShowBackMode.NEVER);
		
		setContentView(R.layout.activity_folderlist);
		
		mCurrentDirTextView = (TextView) findViewById(R.id.textView);
		
		mSelectButton = (Button) findViewById(R.id.select_button);
		mSelectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent data = new Intent();
				data.putExtra(ARG_SELECTEDPATH, mFileListManager.getCurrentDirectory().getAbsolutePath());
				setResult(RESULT_OK, data);
				finish();
			}
		});
		
		mAdapter = new FolderListAdapter();
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				mFileListManager.goIntoItem(position);
				mAdapter.notifyDataSetChanged();
				updateUI();
			}
		});
		
		updateUI();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putString(ARG_SELECTEDPATH, mFileListManager.getCurrentDirectory().getAbsolutePath());
	}
	
	@Override
	public void onBackPressed()
	{
		if (mFileListManager.canGoBack())
		{
			mFileListManager.goBack();
			mAdapter.notifyDataSetChanged();
			updateUI();
		}
		else
		{
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
//				overridePendingTransition(android.R.animator.fade_in, android.R.animator.fade_out);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void updateUI()
	{
		mCurrentDirTextView.setText((mFileListManager.getDepthLevel() == 0) ? "" : mFileListManager.getCurrentDirectory().getAbsolutePath());
		mSelectButton.setEnabled(mFileListManager.getDepthLevel() >= 2); // Prevent user from selecting a base public directory
	}
	
	private class FolderListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			int res = mFileListManager.getFileList().size();
			return res;
		}
		
		@Override
		public String getItem(int position)
		{
			String res = null;
			
			if (mFileListManager.getDepthLevel() == 0)
				res = mFileListManager.getFileList().get(position).getAbsolutePath();
			else
				res = mFileListManager.getFileList().get(position).getName();
			
			return res;
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			FolderCell cell = (FolderCell) convertView;
			if (cell == null)
			{
				cell = (FolderCell) LayoutInflater.from(FolderListActivity.this).inflate(R.layout.cell_folder, null);
			}
			
			cell.setFolderName(getItem(position));
			
			return cell;
		}
	}
}
