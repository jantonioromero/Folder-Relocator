package com.arreis.folderrelocator;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.arreis.folderrelocator.FolderSyncCell.FolderSyncCellListener;
import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncDatabaseHelper;
import com.arreis.folderrelocator.datamodel.alarm.SyncAlarmManager;

public class FolderSyncListFragment extends Fragment implements FolderSyncCellListener
{
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	
	private View mNoSyncsView;
	private ListView mListView;
	
	private SyncListAdapter mAdapter;
	private ArrayList<FolderSync> mFolderSyncs;
	
	public interface Callbacks
	{
		public void deselectAllItems();
		
		public void onItemSelected(int position);
		
		public void onItemUpdated(int position);
	}
	
	private static Callbacks sDummyCallbacks = new Callbacks()
	{
		@Override
		public void onItemSelected(int position)
		{
		}
		
		@Override
		public void onItemUpdated(int position)
		{
		}
		
		@Override
		public void deselectAllItems()
		{
		}
	};
	
	public FolderSyncListFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_foldersync_list, container, false);
		
		mNoSyncsView = rootView.findViewById(R.id.view_noSyncs);
		((Button) rootView.findViewById(R.id.button_newSync)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mCallbacks.onItemSelected(-1);
			}
		});
		
		mAdapter = new SyncListAdapter();
		mListView = (ListView) rootView.findViewById(R.id.listView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				mCallbacks.onItemSelected(position);
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				final FolderSync selectedSync = mFolderSyncs.get(position);
				new AlertDialog.Builder(getActivity()).setMessage(String.format(getString(R.string.message_deleteFolderSync), selectedSync.getAlias())).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						new FolderSyncDatabaseHelper(getActivity()).delete(selectedSync);
						SyncAlarmManager.cancelAlarm(getActivity(), selectedSync);
						updateDBDataAndRefresh();
						
						((FolderSyncListFragment.Callbacks) getActivity()).deselectAllItems();
					}
				}).setNegativeButton(android.R.string.no, null).create().show();
				
				return false;
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
		{
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		if (!(activity instanceof Callbacks))
		{
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		
		mCallbacks = (Callbacks) activity;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		updateDBDataAndRefresh();
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		
		mCallbacks = sDummyCallbacks;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION)
		{
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}
	
	public void setActivateOnItemClick(boolean activateOnItemClick)
	{
		mListView.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}
	
	private void setActivatedPosition(int position)
	{
		if (position == ListView.INVALID_POSITION)
		{
			mListView.setItemChecked(mActivatedPosition, false);
		}
		else
		{
			mListView.setItemChecked(position, true);
		}
		
		mActivatedPosition = position;
	}
	
	public void updateDBDataAndRefresh()
	{
		mFolderSyncs = new FolderSyncDatabaseHelper(getActivity()).getFolderSyncs(true);
		mAdapter.notifyDataSetChanged();
		
		SyncAlarmManager.setBootReceiverEnabled(getActivity(), mFolderSyncs.size() > 0);
	}
	
	private class SyncListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			int res = 0;
			
			if (mFolderSyncs != null)
				res = mFolderSyncs.size();
			
			mNoSyncsView.setVisibility(res == 0 ? View.VISIBLE : View.GONE);
			
			return res;
		}
		
		@Override
		public FolderSync getItem(int position)
		{
			return mFolderSyncs.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			FolderSyncCell res = (FolderSyncCell) convertView;
			if (res == null)
			{
				res = (FolderSyncCell) LayoutInflater.from(getActivity()).inflate(R.layout.cell_foldersync, null);
				res.setListener(FolderSyncListFragment.this);
			}
			
			res.setFolderSync(getItem(position));
			
			return res;
		}
	}
	
	@Override
	public void syncButtonPressed(FolderSync sync)
	{
		// TODO: Bloquear resto de syncs mientras dure el proceso
		SyncAlarmManager.setAlarm(getActivity(), sync);
		sync.runSynchronization();
	}
}
