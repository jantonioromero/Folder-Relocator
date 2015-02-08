package com.arreis.folderrelocator.explorer;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arreis.folderrelocator.BaseActivity;
import com.arreis.folderrelocator.R;
import com.arreis.folderrelocator.explorer.FolderCell.FolderCellType;
import com.arreis.folderrelocator.explorer.FolderCell.IFolderCellListener;
import com.arreis.util.AFileListManager.AFileListShowBackMode;
import com.arreis.util.AFileListManager.AFileListShowFilesMode;
import com.arreis.util.APublicFileListManager;

public class FolderListActivity extends BaseActivity implements IFolderCellListener
{
	public static final String ARG_REQUESTISSOURCE = "ARG_REQUESTISSOURCE";
	public static final String ARG_SELECTEDPATH = "ARG_SELECTEDPATH";
	
	private TextView mCurrentDirTextView;
	private Button mSelectButton;
	
	private boolean mRequestIsSource;
	private APublicFileListManager mFileListManager;
	private FolderListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
//		if (getResources().getBoolean(R.bool.asTablet))
//		{
//			setTheme(R.style.dialog);
//			
////			requestWindowFeature(Window.FEATURE_ACTION_BAR);
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//			LayoutParams params = getWindow().getAttributes();
//			params.height = 850;
//			params.width = 850; //fixed width
//			params.alpha = 1.0f;
//			params.dimAmount = 0.5f;
//			getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//		}
		
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_CANCELED);
		
		String initialPath = null;
		if (savedInstanceState != null)
		{
			initialPath = savedInstanceState.getString(ARG_SELECTEDPATH);
			mRequestIsSource = savedInstanceState.getBoolean(ARG_REQUESTISSOURCE, false);
		}
		else
		{
			initialPath = getIntent().getStringExtra(ARG_SELECTEDPATH);
			mRequestIsSource = getIntent().getBooleanExtra(ARG_REQUESTISSOURCE, false);
		}
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(String.format(getString(R.string.choosing), getString(mRequestIsSource ? R.string.syncSource : R.string.syncDestination)));
		
		mFileListManager = new APublicFileListManager(AFileListShowFilesMode.DIRECTORIES_ONLY);
		mFileListManager.setShowBackMode(AFileListShowBackMode.NEVER);
		
		if (initialPath != null)
		{
			mFileListManager.traverseToDirectory(new File(initialPath));
		}
		
		setContentView(R.layout.activity_folderlist);
		
		mCurrentDirTextView = (TextView) findViewById(R.id.textView);
		
		mSelectButton = (Button) findViewById(R.id.select_button);
		mSelectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selectPathAndFinish(mFileListManager.getCurrentDirectory());
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_folderlist, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.menu_newDirectory).setVisible(mFileListManager.getDepthLevel() > 0);
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
			{
				finish();
//				overridePendingTransition(android.R.animator.fade_in, android.R.animator.fade_out);
				return true;
			}
			
			case R.id.menu_newDirectory:
			{
				View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_newdirectory, null);
				final EditText newFolderEdit = (EditText) dialogView.findViewById(R.id.edit_newFolder);
				new AlertDialog.Builder(FolderListActivity.this).setTitle(R.string.dialog_newDirectory_title).setView(dialogView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						boolean mkdirOk = false;
						String dirName = newFolderEdit.getText().toString();
						if (dirName.matches("^[a-zA-Z0-9-_.]*$"))
						{
							mkdirOk = mFileListManager.mkdir(dirName);
							mFileListManager.getFileList(true);
							mAdapter.notifyDataSetChanged();
						}
						
						if (mkdirOk == false)
						{
							Toast.makeText(FolderListActivity.this, R.string.error_newFolderError, Toast.LENGTH_SHORT).show();
						}
					}
				}).setNegativeButton(android.R.string.cancel, null).create().show();
				
				return true;
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void updateUI()
	{
		if (mFileListManager.getDepthLevel() == 0)
		{
			mCurrentDirTextView.setVisibility(View.GONE);
		}
		else
		{
			mCurrentDirTextView.setVisibility(View.VISIBLE);
			mCurrentDirTextView.setText(mFileListManager.getCurrentDirectory().getAbsolutePath());
		}
		
		mSelectButton.setVisibility(mFileListManager.getDepthLevel() >= 2 ? View.VISIBLE : View.GONE); // Prevent user from selecting a base public directory
		supportInvalidateOptionsMenu();
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
				cell.setListener(FolderListActivity.this);
			}
			
			cell.setFolderName(getItem(position), position);
			
			if (mFileListManager.getDepthLevel() == 0)
				cell.setFolderType(position == 0 ? FolderCellType.INTERNAL_ROOT : FolderCellType.EXTERNAL_ROOT);
			else
				cell.setFolderType(FolderCellType.FOLDER);
			
			return cell;
		}
	}
	
	@Override
	public void folderCellSelectButtonPressed(int index)
	{
		selectPathAndFinish(mFileListManager.getFileList().get(index));
	}
	
	private void selectPathAndFinish(File path)
	{
		Intent data = new Intent();
		data.putExtra(ARG_SELECTEDPATH, path.getAbsolutePath());
		setResult(RESULT_OK, data);
		finish();
		
	}
}
