package edu.hsnu.AndroidMifareClassic;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class StationProvider extends ContentProvider {
	
	public static final String PROVIDER_NAME = "edu.hsnu.AndroidMifareClassic.StationProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER_NAME + "/station");
	
	DBConnection helper;
	SQLiteDatabase db;
	interface StationSchema {
		String TABLE_NAME = "StationTable";
		String _ID = "_id";
		String STATION_NAME = "station_name";
		String STATION_ID = "station_id";
	}
	
	private static final int STATIONS = 1;
    private static final int STATION_ID = 2;   
	
	private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "station", STATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "station/#", STATION_ID);      
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(StationSchema.TABLE_NAME, null ,null);
		db.close();
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
	    SQLiteDatabase db = helper.getWritableDatabase();
	    
	    long rowID = db.insert(StationSchema.TABLE_NAME, null, values);
	    db.close();
	    
	    if (rowID != -1) {
	    	Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
	    	getContext().getContentResolver().notifyChange(_uri, null);
	    	return _uri;                
	    }
	    
	    throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		helper = new DBConnection(this.getContext());
		return (helper == null)? false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	    sqlBuilder.setTables(StationSchema.TABLE_NAME);
	    
	    if (uriMatcher.match(uri) == STATION_ID)
	         sqlBuilder.appendWhere(StationSchema.STATION_ID + " = " + uri.getPathSegments().get(1)); 
	      
		db = helper.getReadableDatabase();
        Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		int count = 0;
		
		switch (uriMatcher.match(uri)){
		    case STATIONS:
	            count = db.update(
	            	StationSchema.TABLE_NAME, 
	                values,
	                selection, 
	                selectionArgs);
	            break;
	        case STATION_ID:                
	            count = db.update(
	            	StationSchema.TABLE_NAME, 
	                values,
	                StationSchema._ID + " = " + uri.getPathSegments().get(1) + 
	                (!TextUtils.isEmpty(selection) ? " AND (" + 
	                    selection + ')' : ""), 
	                selectionArgs);
	            break;
	        default: throw new IllegalArgumentException(
	            "Unknown URI " + uri);    
	    }
		
	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}
	
	public static class DBConnection extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "StationDB";
		private static final int DATABASE_VERSION = 1;
		
		private DBConnection(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + StationSchema.TABLE_NAME + " (" 
			+ StationSchema._ID  + " INTEGER primary key autoincrement, " 
			+ StationSchema.STATION_NAME + " text not null, " 
			+ StationSchema.STATION_ID + " INTEGER not null "+ ");";
			db.execSQL(sql);	
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + StationSchema.TABLE_NAME);	
		}
	}

}
