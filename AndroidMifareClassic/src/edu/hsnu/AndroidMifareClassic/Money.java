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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Money extends Activity {

	private TextView tvMoney;
	private EditText edtIncrement;
	private EditText edtDecrement;
	private EditText edtValue;
	private Button btnIncrement;
	private Button btnDecrement;
	private Button btnValue;
	private int money;
	private int inc;
	private int dec;
	private int i_value;
	private EasyCard ezCard = EasyCardOperation.ezCard;
	
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
		setContentView(R.layout.money);
		initView();
	}
	
	private void initView() {
		tvMoney = (TextView) findViewById(R.id.tvMoney);
		edtIncrement = (EditText) findViewById(R.id.edtIncrementMoney);
		edtDecrement = (EditText) findViewById(R.id.edtDecrementMoney);
		edtValue = (EditText) findViewById(R.id.edtMoney);
		btnIncrement = (Button) findViewById(R.id.btnIncrementMoney);
		btnDecrement = (Button) findViewById(R.id.btnDecrementMoney);
		btnValue = (Button) findViewById(R.id.btnMoney);
		
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
		
		btnDecrement.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				v.setEnabled(false);
				try {
					ezCard.decrement(dec);
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
					ezCard.increment(inc);
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
					ezCard.setMoney(i_value);
					update();
				} catch (IOException e) {
					handleException(e);
				}
				v.setEnabled(true);
			}
		});
		
		update();
	}
	
	private void update() {
		money = ezCard.getMoney();
		tvMoney.setText( money + "" );
	}
	
	private void handleException( Exception e ) {
		if (e.getMessage().equals("PERMISSION")) {
			Toast.makeText(Money.this, R.string.permission, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(Money.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
			getParent().setResult(-1);
			finish();
		}
	}
}
