package com.arreis.folderrelocator;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.arreis.folderrelocator.FolderSyncCell.FolderSyncCellListener;
import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncDatabaseHelper;

public class FolderSyncListFragment extends ListFragment implements FolderSyncCellListener
{
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private ArrayList<FolderSync> mFolderSyncs;
	private SyncListAdapter mAdapter;
	
	public interface Callbacks
	{
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
	};
	
	public FolderSyncListFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mAdapter = new SyncListAdapter();
		setListAdapter(mAdapter);
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
	public void onListItemClick(ListView listView, View view, int position, long id)
	{
		super.onListItemClick(listView, view, position, id);
		
		mCallbacks.onItemSelected(position);
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
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}
	
	private void setActivatedPosition(int position)
	{
		if (position == ListView.INVALID_POSITION)
		{
			getListView().setItemChecked(mActivatedPosition, false);
		}
		else
		{
			getListView().setItemChecked(position, true);
		}
		
		mActivatedPosition = position;
	}
	
	public void updateDBDataAndRefresh()
	{
		mFolderSyncs = new FolderSyncDatabaseHelper(getActivity()).getFolderSyncs(true);
		mAdapter.notifyDataSetChanged();
		
	}
	
	private class SyncListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			int res = 0;
			
			if (mFolderSyncs != null)
				res = mFolderSyncs.size();
			
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
			}
			
			res.setFolderSync(getItem(position));
			
			return res;
		}
	}
	
	@Override
	public void syncButtonPressed(FolderSync sync)
	{
		// TODO: Bloquear resto de syncs mientras dure el proceso
		sync.runSynchronization();
	}
}
