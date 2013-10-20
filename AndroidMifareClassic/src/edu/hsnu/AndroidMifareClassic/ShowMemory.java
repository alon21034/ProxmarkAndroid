package edu.hsnu.AndroidMifareClassic;

import java.io.File;
import java.io.FileWriter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.BufferType;

public class ShowMemory extends Activity {

	private MifareClassicCard mfc = AndroidMifareClassic.mfc;
	private File path;
	private Time time = new Time();
	private LinearLayout view;
	private ScrollView sView;
	private TextView tvMemory;
	private LinearLayout layout;
	private Button btnDump;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		time.switchTimezone(Time.getCurrentTimezone());
		
		view = new LinearLayout(this);
		sView = new ScrollView(this);
		tvMemory = new TextView(this);
		layout = new LinearLayout(this);
		btnDump = new Button(this);
		
		path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mfd/dump" );
		if( !path.exists() )
        	path.mkdir();
		
		tvMemory.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		tvMemory.setText(Html.fromHtml(sBuilder().toString()),BufferType.SPANNABLE);
		btnDump.setText("Dump");
		btnDump.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				time.setToNow();
				String timeStamp = time.format2445().replaceAll("T", ".");
				String filename = path.getAbsolutePath() + '/' + mfc.getUid().replaceAll(" ", "") + " - " + timeStamp + ".txt";
				File file = new File(filename);
				try {
					file.createNewFile();
					FileWriter writer = new FileWriter(file);
					for (int i = 0; i < mfc.getBlockCount(); i++) {
						writer.write(mfc.getBlock(i).toString()+'\n');
					}
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(ShowMemory.this, filename + "\n" + R.string.write_file_error, Toast.LENGTH_SHORT).show();
				}
				
				Toast.makeText(ShowMemory.this, filename, Toast.LENGTH_SHORT).show();
			}
		});
		
		layout.addView(btnDump, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity( Gravity.CENTER );
		
		sView.addView(tvMemory);
		
		view.setOrientation( LinearLayout.VERTICAL );
		view.addView(sView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 0.2f ));
		view.addView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 0.8f ));
		
		setContentView(view);
	}
	
	private String sectorString( int sId, int bId ) {
		StringBuilder sBuilder = new StringBuilder();
		int bCount = mfc.getBlockCountInSector(sId);
		
		sBuilder.append("<b>Sector " + sId + ":</b><br>" )
			.append("<font face='monospace'>");
		for (int i = bId; i < bId + bCount; i++) {
			sBuilder.append( mfc.getBlock(i).toString().replaceAll(" ", "") + "<br>" );
		}
		sBuilder.append("</font><br>");
		
		return sBuilder.toString();
	}
	
	private StringBuilder sBuilder() {
		StringBuilder stringBuilder = new StringBuilder();
		
		int bId = 0;
		for (int i = 0; i < mfc.getSectorCount(); i++ ) {
			stringBuilder.append(sectorString(i, bId));
			bId += mfc.getBlockCountInSector(i);
		}
		
		return stringBuilder;
	}

}
