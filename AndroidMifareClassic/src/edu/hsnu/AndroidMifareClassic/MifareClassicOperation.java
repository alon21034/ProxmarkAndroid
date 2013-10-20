package edu.hsnu.AndroidMifareClassic;

import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TabHost;

public class MifareClassicOperation extends TabActivity {

	private NfcAdapter nfcAdapter;
	private PendingIntent pIntent;
	public static int write_block;
	public static String write_data;
	public static int value_block;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		write_block = 0;
		write_data = "";
		value_block = 0;
		
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pIntent = PendingIntent.getActivity(this, 0, 
        		new Intent(this, AndroidMifareClassic.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;
		
		intent = new Intent().setClass( this, ShowMemory.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabSpec = tabHost.newTabSpec("memory")
					.setIndicator(getResources().getString(R.string.memory)).setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass( this, WriteBlock.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabSpec = tabHost.newTabSpec("write")
					.setIndicator(getResources().getString(R.string.write)).setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass( this, ValueBlock.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabSpec = tabHost.newTabSpec("value")
					.setIndicator(getResources().getString(R.string.value)).setContent(intent);
		tabHost.addTab(tabSpec);
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
