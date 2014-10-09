package com.arreis.folderrelocator.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FolderSyncDatabaseHelper extends SQLiteOpenHelper
{
	public static abstract class FolderSyncEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "folderSync";
		public static final String COLUMN_NAME_ALIAS = "alias";
		public static final String COLUMN_NAME_SOURCE_PATH = "sourcePath";
		public static final String COLUMN_NAME_DESTINATION_PATH = "destinationPath";
		public static final String COLUMN_NAME_INCLUDE_SUBDIRECTORIES = "includeSubdirectories";
		public static final String COLUMN_NAME_MOVE_FILES = "moveFiles";
		public static final String COLUMN_NAME_REPEAT_INTERVAL = "repeatInterval";
	}
	
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FolderSyncEntry.TABLE_NAME + " (" + FolderSyncEntry._ID + " INTEGER PRIMARY KEY,"
			+ FolderSyncEntry.COLUMN_NAME_ALIAS + " TEXT, " + FolderSyncEntry.COLUMN_NAME_SOURCE_PATH + " TEXT, "
			+ FolderSyncEntry.COLUMN_NAME_DESTINATION_PATH + " TEXT, " + FolderSyncEntry.COLUMN_NAME_INCLUDE_SUBDIRECTORIES + " INTEGER, "
			+ FolderSyncEntry.COLUMN_NAME_MOVE_FILES + " INTEGER, " + FolderSyncEntry.COLUMN_NAME_REPEAT_INTERVAL + " INTEGER" + ")";
	
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FolderSyncEntry.TABLE_NAME;
	
	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "FolderSync.db";
	
	private static ArrayList<FolderSync> sFolderSyncs;
	
	public FolderSyncDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_ENTRIES);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onUpgrade(db, oldVersion, newVersion);
	}
	
	public long insert(FolderSync sync)
	{
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FolderSyncEntry.COLUMN_NAME_ALIAS, sync.getAlias());
		values.put(FolderSyncEntry.COLUMN_NAME_SOURCE_PATH, sync.getSourcePath());
		values.put(FolderSyncEntry.COLUMN_NAME_DESTINATION_PATH, sync.getDestinationPath());
		values.put(FolderSyncEntry.COLUMN_NAME_INCLUDE_SUBDIRECTORIES, sync.getIncludeSubdirectories() ? 1 : 0);
		values.put(FolderSyncEntry.COLUMN_NAME_MOVE_FILES, sync.getMoveFiles() ? 1 : 0);
		values.put(FolderSyncEntry.COLUMN_NAME_REPEAT_INTERVAL, sync.getRepeatInterval());
		
		long res = db.insert(FolderSyncEntry.TABLE_NAME, null, values);
		db.close();
		
		return res;
	}
	
	public long delete(FolderSync sync)
	{
		SQLiteDatabase db = getWritableDatabase();
		long res = db.delete(FolderSyncEntry.TABLE_NAME, "_ID = ?", new String[] { String.valueOf(sync.getId()) });
		db.close();
		
		return res;
	}
	
	public long update(FolderSync sync)
	{
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FolderSyncEntry.COLUMN_NAME_ALIAS, sync.getAlias());
		values.put(FolderSyncEntry.COLUMN_NAME_SOURCE_PATH, sync.getSourcePath());
		values.put(FolderSyncEntry.COLUMN_NAME_DESTINATION_PATH, sync.getDestinationPath());
		values.put(FolderSyncEntry.COLUMN_NAME_INCLUDE_SUBDIRECTORIES, sync.getIncludeSubdirectories() ? 1 : 0);
		values.put(FolderSyncEntry.COLUMN_NAME_MOVE_FILES, sync.getMoveFiles() ? 1 : 0);
		values.put(FolderSyncEntry.COLUMN_NAME_REPEAT_INTERVAL, sync.getRepeatInterval());
		
		long res = db.update(FolderSyncEntry.TABLE_NAME, values, "_ID = ?", new String[] { String.valueOf(sync.getId()) });
		db.close();
		
		return res;
	}
	
	public ArrayList<FolderSync> getFolderSyncs()
	{
		return getFolderSyncs(false);
	}
	
	public ArrayList<FolderSync> getFolderSyncs(boolean reload)
	{
		if (sFolderSyncs == null || reload)
		{
			sFolderSyncs = new ArrayList<FolderSync>();
			
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = db.query(FolderSyncEntry.TABLE_NAME, new String[] { "*" }, null, null, null, null, null);
			c.moveToFirst();
			
			while (c.isAfterLast() == false)
			{
				sFolderSyncs.add(new FolderSync(c.getLong(c.getColumnIndex(FolderSyncEntry._ID)),
						c.getString(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_ALIAS)),
						c.getString(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_SOURCE_PATH)),
						c.getString(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_DESTINATION_PATH)),
						c.getInt(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_INCLUDE_SUBDIRECTORIES)) == 1,
						c.getInt(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_MOVE_FILES)) == 1,
						c.getLong(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_REPEAT_INTERVAL))));
				
				c.moveToNext();
			}
			
			db.close();
		}
		
		return sFolderSyncs;
	}
	
	public FolderSync getFolderSync(long syncId)
	{
		FolderSync res = null;
		
		if (sFolderSyncs != null)
		{
			for (FolderSync sync : sFolderSyncs)
			{
				if (syncId == sync.getId())
				{
					res = sync;
					break;
				}
			}
		}
		
		if (res == null)
		{
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = db.query(FolderSyncEntry.TABLE_NAME, new String[] { "*" }, FolderSyncEntry._ID + "=?", new String[] { String.valueOf(syncId) }, null, null, null);
			c.moveToFirst();
			
			if (c.isAfterLast() == false)
			{
				res = new FolderSync(c.getLong(c.getColumnIndex(FolderSyncEntry._ID)), c.getString(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_ALIAS)),
						c.getString(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_SOURCE_PATH)),
						c.getString(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_DESTINATION_PATH)),
						c.getInt(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_INCLUDE_SUBDIRECTORIES)) == 1,
						c.getInt(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_MOVE_FILES)) == 1,
						c.getLong(c.getColumnIndex(FolderSyncEntry.COLUMN_NAME_REPEAT_INTERVAL)));
			}
			
			db.close();
		}
		
		return res;
	}
}
