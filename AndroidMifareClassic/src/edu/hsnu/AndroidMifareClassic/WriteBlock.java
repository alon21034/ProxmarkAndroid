package edu.hsnu.AndroidMifareClassic;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WriteBlock extends Activity {
	
	private TextView tvData;
	private Spinner spinBlkNum;
	private EditText edtWrite;
	private Button btnWrite;
	private Button btnReset;
	private Button btnResetAll;
	private Button btnClear;
    private int blkNum;
	private String[] blkNumArray;
	private MifareClassicCard mfc = AndroidMifareClassic.mfc;

	private TextWatcher textWatcher = new TextWatcher() {
        private int start;
        
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
        }
       
        
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.start = start;
        }
       
        
        public void afterTextChanged(Editable s) {
        	if (start < s.length()) {
            	boolean illegal = false;
            	String lastCharacter = s.subSequence(start, start + 1).toString();
            	
                try {
                	Integer.parseInt(lastCharacter, 16);
    			} catch (Exception e) {
    				illegal = true;
    			}
                
                if ( illegal || s.length() > 32 ) {
                	edtWrite.removeTextChangedListener(textWatcher);
                	s.delete(start, start + 1);
                    edtWrite.setText(s);
                    edtWrite.setSelection(start);
                    edtWrite.addTextChangedListener(textWatcher);
                }
                
                if ( s.length() == 32 ) {
                	btnWrite.setEnabled( true );
                }
                else {
                	btnWrite.setEnabled( false );
				}
                
                MifareClassicOperation.write_data = s.toString();
			}
        }
    };
    
	private OnFocusChangeListener edtTextLostFocus = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if( !hasFocus) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow( getWindow().getDecorView().getWindowToken(), 0);
			}
		}
	};
	
    private void initView() {
		tvData = (TextView) findViewById(R.id.tvData);
		spinBlkNum = (Spinner) findViewById(R.id.spinBlkNum);
		edtWrite = (EditText) findViewById(R.id.edtWrite);
		btnWrite = (Button) findViewById(R.id.btnWrite);
		btnReset = (Button) findViewById(R.id.btnReset);
		btnResetAll = (Button) findViewById(R.id.btnResetAll);
		btnClear = (Button) findViewById(R.id.btnClear);
		
		edtWrite.addTextChangedListener(textWatcher);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, blkNumArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinBlkNum.setAdapter(adapter);
		spinBlkNum.setOnItemSelectedListener( new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				blkNum = Integer.parseInt(blkNumArray[position]);
				tvData.setText( mfc.getBlock(blkNum).toString().replaceAll(" ", ""));
				MifareClassicOperation.write_block = position;
			}
			
			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(0);
			}
		});
		
		edtWrite.setOnFocusChangeListener(edtTextLostFocus);
		edtWrite.setText( MifareClassicOperation.write_data );
		edtWrite.setSelection( edtWrite.getText().length() );
		
		btnWrite.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				toggleLockButton();
				
				try {
					mfc.writeBlock(blkNum, edtWrite.getText().toString() );
					tvData.setText( mfc.getBlock(blkNum).toString().replaceAll(" ", ""));
				} catch (IOException e) {
					handleException(e);
				}
				
				toggleLockButton();
			}
		} );
		
		btnWrite.setEnabled( false );
		
		btnReset.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				edtWrite.setText("");
				toggleLockButton();
				
				StringBuilder sBuilder = new StringBuilder();
				for (int i = 0; i < 32; i++) {
					sBuilder.append('0');
				}
				
				try {
					mfc.writeBlock(blkNum, sBuilder.toString() );
					tvData.setText( mfc.getBlock(blkNum).toString().replaceAll(" ", ""));
				} catch (IOException e) {
					handleException(e);
				}
				
				toggleLockButton();
			}
		});
		
		btnResetAll.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				edtWrite.setText("");
				toggleLockButton();
				
				StringBuilder sBuilder = new StringBuilder();
				for (int i = 0; i < 32; i++) {
					sBuilder.append('0');
				}
				
				try {
					for ( int i = 0; i < mfc.getBlockCount(); i++ ) {
						if ( !mfc.getBlock(i).canWrite() ) continue;
						mfc.writeBlock(i, sBuilder.toString() );
					}
				} catch (IOException e) {
					handleException(e);
				}
				
				tvData.setText( mfc.getBlock(blkNum).toString().replaceAll(" ", ""));
				
				toggleLockButton();
			}
		});
		
		btnClear.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				edtWrite.setText("");
			}
		});
		
		spinBlkNum.setSelection(MifareClassicOperation.write_block);
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		
		blkNumArray = getResources().getStringArray(R.array.blknum);
		initView();
	}
	
	private void handleException( Exception e ) {
		if (e.getMessage().equals("PERMISSION")) {
			Toast.makeText(WriteBlock.this, R.string.permission, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(WriteBlock.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
			getParent().setResult(-1);
			finish();
		}
	}
	
	private void toggleLockButton() {
		btnWrite.setEnabled( !btnWrite.isEnabled() );
		btnReset.setEnabled( !btnReset.isEnabled() );
		btnResetAll.setEnabled( !btnResetAll.isEnabled() );
	}

}
