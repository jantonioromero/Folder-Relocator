package com.arreis.folderrelocator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConfigureAutoSyncFragment extends Fragment
{
	public static final String ARG_ITEM_ID = "item_id";
	
	public ConfigureAutoSyncFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ARG_ITEM_ID))
		{
			// TODO: Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_foldersync_detail, container, false);
		
		// TODO: Show the dummy content as text in a TextView.
//		if (mItem != null)
//		{
//			((TextView) rootView.findViewById(R.id.foldersync_detail)).setText(mItem.content);
//		}
		
		return rootView;
	}
}
