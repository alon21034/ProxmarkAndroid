package edu.hsnu.AndroidMifareClassic;

import java.io.File;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class AndroidMifareClassic extends Activity {
	
	private NfcAdapter nfcAdapter;
	private PendingIntent pIntent;
	private TextView tvTip;
	private TextView tvTagInfo;
	private Button btnMfc;
	private Button btnEasyCard;
	private File path;
	public static MifareClassicCard mfc = null;
	
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mfd" );
        if( !path.exists() )
        	path.mkdir();
		
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pIntent = PendingIntent.getActivity(this, 0, 
        		new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        onNewIntent(getIntent());
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == -1) {
			tvTip.setText(R.string.no_tag);
			tvTagInfo.setText("");
			btnMfc.setEnabled(false);
			btnEasyCard.setEnabled(false);
			mfc = null;
		}
	}

	private void initView() {
		tvTip = (TextView) findViewById(R.id.tvTip);
		tvTagInfo = (TextView) findViewById(R.id.tvTagInfo);
		btnMfc = (Button) findViewById(R.id.btnMfc);
		btnEasyCard = (Button) findViewById(R.id.btnEasyCard);
		
		btnMfc.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				startActivityForResult(new Intent( AndroidMifareClassic.this, MifareClassicOperation.class )
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0 );
			}
		});
		
		btnEasyCard.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				startActivityForResult(new Intent( AndroidMifareClassic.this, EasyCardOperation.class )
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0 );
			}
		});
		
		btnMfc.setEnabled(false);
		btnEasyCard.setEnabled(false);
	}

	
	protected void onResume() {
		nfcAdapter.enableForegroundDispatch( this, pIntent, null, null);
		super.onResume();
	}
	
	
	protected void onPause() {
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		super.onPause();
	}

	
	protected void onNewIntent(Intent intent) {
		if ( intent != null && intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			NfcA nfcA = null;
			byte[] uid = tag.getId();
			String[] tech = tag.getTechList();
			String rfid;
			StringBuilder sBuilder = new StringBuilder();
			boolean isNfcA = false;
			boolean isMifareClassic = false;
			
			tvTip.setText(R.string.tag_info);
			sBuilder.append("<br><b>UID (" + uid.length +" bytes)</b><br>")
				.append(byteToString(uid)+"<br><br>");
			
			for( String technology: tech 	) {
				if ( technology.equals( NfcA.class.getName() )) {
					isNfcA = true;
				}
			}
			
			if ( isNfcA ) {
				nfcA = NfcA.get(tag);
				rfid = "ISO/IEC 14443 Type A";
			}
			else
				rfid = "Others";
			
			sBuilder.append("<b>" + getResources().getString(R.string.rfid_tech) +"</b><br>")
				.append(rfid+"<br><br>");
			
			for( String technology: tech 	) {
				if ( technology.equals( MifareClassic.class.getName() ) ) {
					isMifareClassic = true;
				}
			}
			
			if ( isMifareClassic ) {
				try {
					mfc = new MifareClassicCard(tag);
				} catch (Exception e) {
					String msg = e.getMessage();
					if( msg != null ) {
						if ( msg.equals("READ_FILE_ERROR")) {
							mfc = null;
							sBuilder.append("<font color='red'>")
								.append( getResources().getString(R.string.read_file_error) + "</font><br><br>");
						}
						else if (msg.equals("CONNECT_ERROR")) {
							mfc = null;
							sBuilder.append("<font color='red'>")
								.append( getResources().getString(R.string.connect_error) + "</font><br><br>");
						}
						else if (msg.equals("WRITE_FILE_ERROR")) {
							sBuilder.append("<font color='red'>")
								.append( getResources().getString(R.string.write_file_error) + "</font><br><br>");
						}
					}
				}
			} else {
				mfc = null;
				if (tech[0].equals( MifareUltralight.class.getName() )) {
					sBuilder.append("<b>" + getResources().getString(R.string.tag_type) +"</b><br>")
					.append("MifareUltralight<br><br>");
				} else if(tech[0].equals( NdefFormatable.class.getName() )) {
					sBuilder.append("<b>" + getResources().getString(R.string.tag_type) +"</b><br>")
					.append("NdefFormatable<br><br>");
				} else {
					sBuilder.append("<b>" + getResources().getString(R.string.tag_type) +"</b><br>")
						.append("Others<br><br>");
				}
				sBuilder.append("<font color='red'>")
					.append( getResources().getString(R.string.wrong_type) + "</font><br><br>");
				
				btnMfc.setEnabled(false);
				btnEasyCard.setEnabled(false);
			}
			
			if ( mfc != null ) {
				int size = mfc.getSize();
				
				sBuilder.append("<b>" + getResources().getString(R.string.tag_type) +"</b><br>")
					.append("Mifare Classic");
				
				switch (size) {
				case MifareClassic.SIZE_1K:
					sBuilder.append(" 1K<br><br>");
					break;
				case MifareClassic.SIZE_2K:
					sBuilder.append(" 2K<br><br>");
					break;
				case MifareClassic.SIZE_4K:
					sBuilder.append(" 4K<br><br>");
					break;
				default:
					sBuilder.append(" MINI<br><br>");
					break;
				}
				
				sBuilder.append("<b>" + getResources().getString(R.string.memory_size) +"</b><br>")
					.append(size + " bytes<br><br>");
				
				if ( mfc.isAuth() ) {
					btnMfc.setEnabled(true);
					if( EasyCard.isEasyCard(mfc) ) {
						btnEasyCard.setEnabled(true);
					}
					else {
						sBuilder.append("<font color='red'>")
							.append( getResources().getString(R.string.ez_format) + "</font><br><br>");
						btnEasyCard.setEnabled(false);
					}
				} else {
					sBuilder.append("<font color='red'>")
						.append( getResources().getString(R.string.auth_error) + "</font><br><br>");
					btnMfc.setEnabled(false);
					btnEasyCard.setEnabled(false);
				}
			}
			
			if ( isNfcA ) {
				sBuilder.append("<b>ATQA</b><br>")
					.append( byteToString(nfcA.getAtqa()) + "<br><br>")
					.append("<b>SAK</b><br>")
					.append(Integer.toHexString( ( nfcA.getSak() & 0xff ) + 0x100 ).substring(1))
					.append("<br><br>");
			}
			
			sBuilder.append("<b>" + getResources().getString(R.string.tech_class) +"</b><br>");
			for (String string : tech) {
				sBuilder.append(string + "<br>");
			}
			
			tvTagInfo.setText(Html.fromHtml(sBuilder.toString()),BufferType.SPANNABLE);
		}
	}
	
	public static String byteToString( byte[] bytes) {
		StringBuilder sBuilder = new StringBuilder();
		
		for (byte b : bytes) {
			sBuilder.append( Integer.toHexString( ( b & 0xff ) + 0x100 ).substring(1) + " ");
		}
		
		return sBuilder.toString();
	}
	
}
