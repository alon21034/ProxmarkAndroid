package edu.hsnu.AndroidMifareClassic;

import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ShowRecords extends Activity {

	private EasyCard ezCard = EasyCardOperation.ezCard;
	private File path;
	private Time time;
	private LinearLayout view;
	private ScrollView sView;
	private LinearLayout layout;
	private Button btnDump;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		time = new Time();
		time.switchTimezone(Time.getCurrentTimezone());
		
		view = new LinearLayout(this);
		sView = new ScrollView(this);
		layout = new LinearLayout(this);
		btnDump = new Button(this);
		
		path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mfd/record" );
		if( !path.exists() )
        	path.mkdir();
		
		btnDump.setText("Dump");
		btnDump.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				time.setToNow();
				String timeStamp = time.format2445().replaceAll("T", ".");
				String filename = path.getAbsolutePath() + '/' + ezCard.getUid().replaceAll(" ", "") + " - " + timeStamp + ".txt";
				File file = new File(filename);
				try {
					file.createNewFile();
					FileWriter writer = new FileWriter(file);
					writer.write("UID:" + ezCard.getUid() + '\n' );
					writer.write( getResources().getString(R.string.identify) + ":" + ezCard.getIdentity() + '\n' );
					if( !ezCard.getExpiredDate().isEmpty() )
						writer.write( getResources().getString(R.string.expired) + ":" + ezCard.getExpiredDate() + '\n' );
					writer.write( getResources().getString(R.string.money) + ":" + ezCard.getMoney() + '\n' );
					writer.write( getResources().getString(R.string.use) + ":" + ezCard.getUseTimes() + '\n' );
					if( !ezCard.getLastIncrementDate().isEmpty() ) {
						writer.write( getResources().getString(R.string.last_inc) + ":" + ezCard.getLastIncrement() );
						writer.write( "( " + ezCard.getLastIncrementDate() + " )\n\n" );
					}
					for( int i = 0; i < 6; i++ ) {
						writer.write(ezCard.getRecord(i).toString() + '\n' );
					}
					
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(ShowRecords.this, filename + "\n" + R.string.write_file_error, Toast.LENGTH_SHORT).show();
				}
				
				Toast.makeText(ShowRecords.this, filename, Toast.LENGTH_SHORT).show();
			}
		});
		
		sView.addView(layoutRecords());
		
		layout.addView(btnDump, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity( Gravity.CENTER );
		
		view.setOrientation( LinearLayout.VERTICAL );
		view.addView(sView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 0.2f ));
		view.addView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 0.8f ));
		
		setContentView(view);
	}
	
	private LinearLayout layoutRecords() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation( LinearLayout.VERTICAL );
		for (int i = 0; i < 6; i++)
			layout.addView(ezCard.getRecord(i).getView());
		
		return layout;
	}

}
