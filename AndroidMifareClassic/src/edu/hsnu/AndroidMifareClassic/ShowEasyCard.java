package edu.hsnu.AndroidMifareClassic;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class ShowEasyCard extends Activity {

	private ScrollView view;
	private TextView tvData;
	private EasyCard ezCard = EasyCardOperation.ezCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = new ScrollView(this);
		tvData = new TextView(this);
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("<b>UID</b><br>" + ezCard.getUid() + "<br><br>" );
		sBuilder.append( "<b>" + getResources().getString(R.string.identify) + "</b><br>" + ezCard.getIdentity() + "<br><br>" );
		if( !ezCard.getExpiredDate().isEmpty() )
			sBuilder.append( "<b>" + getResources().getString(R.string.expired) + "</b><br>" + ezCard.getExpiredDate() + "<br><br>" );
		sBuilder.append( "<b>" + getResources().getString(R.string.money) + "</b><br>" + ezCard.getMoney() + "<br><br>" );
		sBuilder.append( "<b>" + getResources().getString(R.string.use) + "</b><br>" + ezCard.getUseTimes() + "<br><br>" );
		if( !ezCard.getLastIncrementDate().isEmpty() )
			sBuilder.append( "<b>" + getResources().getString(R.string.last_inc) + "</b><br>" )
				.append( ezCard.getLastIncrement() + " ( " + ezCard.getLastIncrementDate() + " )<br>" );
		
		tvData.setPadding( 5, 5, 5, 5);
		tvData.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		tvData.setText(Html.fromHtml(sBuilder.toString()),BufferType.SPANNABLE);
		
		view.addView(tvData);
		
		setContentView(view);
	}
	
}
