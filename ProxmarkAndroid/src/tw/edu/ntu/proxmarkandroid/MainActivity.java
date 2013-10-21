package tw.edu.ntu.proxmarkandroid;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Thread mThread1;
	private Thread mThread2;
//	private MifareClassic mifare;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int ch = msg.what;

			if (ch % 2 == 0) {
				ch /= 2;
				String str = "" + (char)ch;
				if (mIsRecord) {
					mTraceString += (char)ch;
				}
				outTextView.append(str);
			} else {
				ch = (ch-1)/2;
				String str = "" + (char)ch;
				errTextView.append(str);
			}
			
		};
	};
	
	
	private TextView outTextView;
	private TextView errTextView;
	private Button startButton;
	private Button quitButton;
	private Button readerButton;
	private Button snoopButton;
	private Button listButton;
	private Button uploadButton;
	private Button connectButton;
	private Button transButton;
	
	private Process ps;
	private DataOutputStream dos;
	
	// for record trace and upload to server.
	private boolean mIsRecord;
	private String mTraceString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create UI view and set listeners.
		outTextView = (TextView) findViewById(R.id.outTextView);
		errTextView = (TextView) findViewById(R.id.errTextView);
		
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(this);

		quitButton = (Button) findViewById(R.id.quitButton);
		quitButton.setOnClickListener(this);
		
		readerButton = (Button) findViewById(R.id.readerButton);
		readerButton.setOnClickListener(this);
		
		snoopButton = (Button) findViewById(R.id.snoopButton);
		snoopButton.setOnClickListener(this);
		
		listButton = (Button) findViewById(R.id.listButton);
		listButton.setOnClickListener(this);
		
		uploadButton = (Button) findViewById(R.id.uploadButton);
		uploadButton.setOnClickListener(this);
		
		connectButton = (Button) findViewById(R.id.connectButton);
		connectButton.setOnClickListener(this);
		
		transButton = (Button) findViewById(R.id.transButton);
		transButton.setOnClickListener(this);
		
		mIsRecord = false;
		
		try {
	        ps = Runtime.getRuntime().exec("su\n");
	        OutputStream os = ps.getOutputStream();
	        final InputStream is = ps.getInputStream();
	        final InputStream es = ps.getErrorStream();
	        
	        mThread1 = new Thread(new Runnable() {
				
	        	private String TAG = "output";
	        	
				@Override
				public void run() {
					try {
						int ch = is.read();
						while (true) {
							if (ch != -1) {
								Log.d(TAG, "!! " + ch + (char)ch);
								mHandler.sendEmptyMessage(ch * 2);
							}
							ch = is.read();
						}
					} catch (IOException e) {
	                    e.printStackTrace();
                    }
				}
			});
	        
	        mThread2 = new Thread(new Runnable() {
				
	        	private String TAG = "error";
	        	
				@Override
				public void run() {
					try {
						int ch = es.read();
						while (true) {
							if (ch != -1) {
								Log.d(TAG, "!! " + ch + (char)ch);
								mHandler.sendEmptyMessage(ch * 2 + 1);
							}
							ch = es.read();
						}
					} catch (IOException e) {
	                    e.printStackTrace();
                    }
				}
			});
	        
	        mThread1.start();
	        mThread2.start();
	        
	        dos = new DataOutputStream(os);
	        
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_reset_item:
			errTextView.setText("");
			outTextView.setText("");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onClick(View v) {
		StringBuilder sb;
		
		switch (v.getId()) {
		case R.id.startButton:
			try {
	            dos.writeBytes("proxmark3\n");
            } catch (IOException e) {
	            e.printStackTrace();
            }
			break;

		case R.id.quitButton:
			try {
	            dos.writeBytes("quit\n");
            } catch (IOException e) {
	            e.printStackTrace();
            }
			break;
			
		case R.id.readerButton:
			try {
	            dos.writeBytes("hf 14a reader\n");
	            if (!mIsRecord) {
	            	mIsRecord = true;
	            	mTraceString = "";
	            }
            } catch (IOException e) {
	            e.printStackTrace();
            }
			break;
		case R.id.snoopButton:
			try {
	            dos.writeBytes("hf 14a snoop\n");
	            if (!mIsRecord) {
	            	mIsRecord = true;
	            	mTraceString = "";
	            }
            } catch (IOException e) {
	            e.printStackTrace();
            }
			break;
		case R.id.listButton:
			try {
				if (!mIsRecord) {
	            	mIsRecord = true;
	            	mTraceString = "";
	            }
	            dos.writeBytes("hf 14a list\n");
            } catch (IOException e) {
	            e.printStackTrace();
            }
			break;
		case R.id.uploadButton:
			if (mIsRecord) {
				mIsRecord = false;
				UploadAsyncTask mAsyncTask = new UploadAsyncTask(mTraceString) {        
					@Override
					protected void onPostExecute(Void result) {
						mTraceString = "";
						super.onPostExecute(result);
					}
				};
				mAsyncTask.execute();
			} else {
				Toast.makeText(this, "snoop first", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.connectButton:
			UploadAsyncTask mAsyncTask = new UploadAsyncTask("test") {        
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
				}
			};
			mAsyncTask.execute();
			break;
		case R.id.transButton:
			
			break;
		default:
			break;
		}
    }
}
