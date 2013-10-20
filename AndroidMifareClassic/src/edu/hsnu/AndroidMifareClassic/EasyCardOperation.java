package edu.hsnu.AndroidMifareClassic;

import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TabHost;

public class EasyCardOperation extends TabActivity {

	private NfcAdapter nfcAdapter;
	private PendingIntent pIntent;
	public static EasyCard ezCard = null;
	
	interface StationSchema {
		String TABLE_NAME = "StationTable";
		String _ID = "_id";
		String STATION_NAME = "station_name";
		String STATION_ID = "station_id";
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		ezCard = new EasyCard( EasyCardOperation.this );
		queryStation();
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pIntent = PendingIntent.getActivity(this, 0, 
        		new Intent(this, AndroidMifareClassic.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;
		
		intent = new Intent().setClass( this, ShowEasyCard.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabSpec = tabHost.newTabSpec("data")
					.setIndicator(getResources().getString(R.string.data)).setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass( this, ShowRecords.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabSpec = tabHost.newTabSpec("record")
					.setIndicator(getResources().getString(R.string.record)).setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass( this, Money.class ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		tabSpec = tabHost.newTabSpec("money")
					.setIndicator(getResources().getString(R.string.money)).setContent(intent);
		tabHost.addTab(tabSpec);
		
	}
	
    private void queryStation() {
    	String uriString = "content://edu.hsnu.AndroidMifareClassic.StationProvider/station/";
    	Uri uri;
    	
    	for (int i = 0; i < 6; i++) {
			if( ezCard.getRecord(i).getStationId() != -1 ) {
				uri = Uri.parse(uriString + ezCard.getRecord(i).getStationId() );
				
				Cursor cursor = managedQuery(uri, null, null, null, null);
				if(cursor != null) {
		        	cursor.moveToFirst();
					ezCard.getRecord(i).setStation( cursor.getString(1));
					cursor.close();
		        }
		        else {
		        	ezCard.getRecord(i).setStation("Unknown");
		        }
			}
		}
    }
    
	@Override
	protected void onResume() {
		nfcAdapter.enableForegroundDispatch( this, pIntent, null, null);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		super.onPause();
	}

}
