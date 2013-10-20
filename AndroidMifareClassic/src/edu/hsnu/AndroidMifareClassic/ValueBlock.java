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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ValueBlock extends Activity {
	
	private TextView tvValue;
	private EditText edtIncrement;
	private Spinner spinBlkNum;
	private EditText edtDecrement;
	private EditText edtValue;
	private Button btnIncrement;
	private Button btnDecrement;
	private Button btnValue;
	private Button btnFormat;
	private LinearLayout layValue;
	private LinearLayout layFormat;
	private int blkNum;
	private int value;
	private int inc;
	private int dec;
	private int i_value;
	private String[] blkNumArray;
	private MifareClassicCard mfc = AndroidMifareClassic.mfc;
	
	private OnFocusChangeListener edtTextLostFocus = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if( !hasFocus) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				EditText edtText = (EditText) v;
				
				imm.hideSoftInputFromWindow( getWindow().getDecorView().getWindowToken(), 0);
				if (edtText.getText().length() == 0) {
					edtText.setText("0");
				}
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.value);
		blkNumArray = getResources().getStringArray(R.array.blknum);
		
		initView();
	}
	
	private void initView() {
		spinBlkNum = (Spinner) findViewById(R.id.spinBlkNum);
		tvValue = (TextView) findViewById(R.id.tvValue);
		edtIncrement = (EditText) findViewById(R.id.edtIncrement);
		edtDecrement = (EditText) findViewById(R.id.edtDecrement);
		edtValue = (EditText) findViewById(R.id.edtValue);
		btnIncrement = (Button) findViewById(R.id.btnIncrement);
		btnDecrement = (Button) findViewById(R.id.btnDecrement);
		btnValue = (Button) findViewById(R.id.btnValue);
		btnFormat = (Button) findViewById(R.id.btnFormat);
		layValue = (LinearLayout) findViewById(R.id.layValue);
		layFormat = (LinearLayout) findViewById(R.id.layFormat);
		
		edtIncrement.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					inc = 0;
					btnIncrement.setEnabled(false);
				} else {
					inc = Integer.parseInt(s.toString());
					btnIncrement.setEnabled(true);
				}
			}
		});
		
		edtDecrement.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					dec = 0;
					btnDecrement.setEnabled(false);
				} else {
					dec = Integer.parseInt(s.toString());
					btnDecrement.setEnabled(true);
				}
			}
		});
		
		edtValue.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					i_value = 0;
					btnValue.setEnabled(false);
				} else {
					i_value = Integer.parseInt(s.toString());
					btnValue.setEnabled(true);
				}
			}
		});
		
		edtDecrement.setText("0");
		edtIncrement.setText("0");
		edtValue.setText("0");
		
		edtDecrement.setOnFocusChangeListener(edtTextLostFocus);
		edtIncrement.setOnFocusChangeListener(edtTextLostFocus);
		edtValue.setOnFocusChangeListener(edtTextLostFocus);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, blkNumArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinBlkNum.setAdapter(adapter);
		spinBlkNum.setOnItemSelectedListener( new OnItemSelectedListener() {

			
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				blkNum = Integer.parseInt(blkNumArray[position]);
				update();
				MifareClassicOperation.value_block = position;
			}

			
			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(0);
			}
		});
		
		btnDecrement.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				v.setEnabled(false);
				try {
					mfc.decrement(blkNum, dec);
					update();
				} catch (IOException e) {
					handleException(e);
				}
				v.setEnabled(true);
			}
		});
		
		btnIncrement.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				v.setEnabled(false);
				try {
					mfc.increment(blkNum, inc);
					update();
				} catch (IOException e) {
					handleException(e);
				}
				v.setEnabled(true);
			}
		});
		
		btnValue.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				v.setEnabled(false);
				try {
					mfc.setValue(blkNum, i_value);
					update();
				} catch (IOException e) {
					handleException(e);
				}
				v.setEnabled(true);
			}
		});
		
		btnFormat.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				try {
					mfc.setValue(blkNum, 0);
					update();
				} catch (IOException e) {
					handleException(e);
				}
			}
		});
		
		spinBlkNum.setSelection(MifareClassicOperation.value_block);
	}
	
	private void update() {
		if( mfc.getBlock(blkNum).isValueBlock() ) {
			layFormat.setVisibility(View.GONE);
			layValue.setVisibility(View.VISIBLE);
			value = mfc.getBlock(blkNum).getValue();
			tvValue.setText( value + "" );
		} else {
			layFormat.setVisibility(View.VISIBLE);
			layValue.setVisibility(View.GONE);
		}
	}
	
	private void handleException( Exception e ) {
		if (e.getMessage().equals("PERMISSION")) {
			Toast.makeText(ValueBlock.this, R.string.permission, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(ValueBlock.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
			getParent().setResult(-1);
			finish();
		}
	}
}
