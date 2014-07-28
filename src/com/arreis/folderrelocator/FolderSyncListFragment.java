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
import android.widget.TextView;

import com.arreis.folderrelocator.datamodel.FolderSync;
import com.arreis.folderrelocator.datamodel.FolderSyncManager;

public class FolderSyncListFragment extends ListFragment
{
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private ArrayList<FolderSync> mFolderSyncs;
	
	public interface Callbacks
	{
		public void onItemSelected(int position);
	}
	
	private static Callbacks sDummyCallbacks = new Callbacks()
	{
		@Override
		public void onItemSelected(int position)
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
		
		mFolderSyncs = FolderSyncManager.getFolderSyncs(getActivity(), true);
		setListAdapter(new FileListAdapter());
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
	
	private class FileListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return mFolderSyncs.size();
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
			TextView res = (TextView) convertView;
			if (res == null)
			{
				res = (TextView) LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_activated_1, null);
			}
			
			res.setText(getItem(position).getAlias());
			
			return res;
		}
	}
}
