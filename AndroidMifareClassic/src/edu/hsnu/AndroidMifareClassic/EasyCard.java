package edu.hsnu.AndroidMifareClassic;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class EasyCard {
	
	private MifareClassicCard mfc;
	private EasyCardRecord[] record = new EasyCardRecord[6];
	private LayoutInflater inflater;
	private Resources res;

	public EasyCard( Context context ) {
		mfc = AndroidMifareClassic.mfc;
		inflater = LayoutInflater.from(context);
		res = context.getResources();
		
		record[0] = new EasyCardRecord( 1, mfc.getBlock(16).getData());
		record[1] = new EasyCardRecord( 2, mfc.getBlock(17).getData());
		record[2] = new EasyCardRecord( 3, mfc.getBlock(18).getData());
		record[3] = new EasyCardRecord( 4, mfc.getBlock(20).getData());
		record[4] = new EasyCardRecord( 5, mfc.getBlock(21).getData());
		record[5] = new EasyCardRecord( 6, mfc.getBlock(22).getData());
		
		sort();
		for( int i = 0; i < record.length; i++ )
			record[i].setIndex( i + 1 );
	}
	
	public static boolean isEasyCard( MifareClassicCard mfc ) {
		if (mfc.getBlock(8).isValueBlock() && mfc.getBlock(9).isValueBlock() &&
				mfc.getBlock(8).getValue() == mfc.getBlock(9).getValue() )
			return true;
		return false;
	}
	
	public int getMoney() {
		return mfc.getBlock(8).getValue();
	}
	
	public void writeBlock( int bId, String blkData ) throws IOException {
		mfc.writeBlock(bId, blkData);
	}
	
	public void increment( int value ) throws IOException {
		mfc.increment( 8, value );
		mfc.setValue( 9, mfc.getBlock(8).getValue() );
	}
	
	public void decrement( int value ) throws IOException {
		mfc.decrement( 8, value );
		mfc.setValue( 9, mfc.getBlock(8).getValue() );
	}
	
	public void setMoney( int value ) throws IOException {
		mfc.setValue( 8, value );
		mfc.setValue( 9, mfc.getBlock(8).getValue() );
	}
	
	public String getExpiredDate() {
		return getDate(60, 1);
	}
	
	public String getIdentity() {
		String identity;
		
		switch ( mfc.getBlock(60).getData()[0] ) {
		case 0:
			identity = "普通";
			break;
		case 5:
			identity = "學生";
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			identity = "優待";
		default:
			identity = "";
			break;
		}
		
		return identity;
	}
	
	public int getUseTimes() {
		return (short) Math.max(MifareClassicCard.byteArrayToShort( mfc.getBlock(26).getData(), 0 ),
				MifareClassicCard.byteArrayToShort( mfc.getBlock(12).getData(), 0 ) );
	}
	
	public int getLastIncrement() {
		return MifareClassicCard.byteArrayToShort( mfc.getBlock(10).getData(), 6 );
	}
	
	public String getLastIncrementDate() {
		return getDate(10, 1);
	}
	
	public String getUid() {
		return mfc.getUid();
	}
	
	public EasyCardRecord getRecord( int id ) {
		return record[id];
	}
	
	class EasyCardRecord {
		private int recordId;
		private int index;
		private int cost;
		private int remain;
		private int general;
		private int stationId;
		private long unixTime;
		private String station;
		
		public EasyCardRecord( int i, byte[] data ) {
			index = i;
			recordId = data[0];
			unixTime = (long) MifareClassicCard.byteArrayToInt(data, 1 ) * 1000 - TimeZone.getDefault().getRawOffset();
			stationId = data[11];
			cost = (int) MifareClassicCard.byteArrayToShort(data, 6 );
			remain = (int) MifareClassicCard.byteArrayToShort(data, 8 );
			general = data[14];
		}
		
		public View getView() {
			StringBuilder sBuilder = new StringBuilder();
			
			View view = inflater.inflate( R.layout.record, null );
			TextView tvCost = (TextView) view.findViewById(R.id.tvCost);
			TextView tvRemain = (TextView) view.findViewById(R.id.tvRemain);
			TextView tvRecord = (TextView) view.findViewById(R.id.tvRecord);
			TextView tvRecordId = (TextView) view.findViewById(R.id.tvRecordId);
			TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
			
			tvRecordId.setText( res.getString(R.string.record) + index );
			tvCost.setText( res.getString(R.string.cost) + ":" + cost );
			tvRemain.setText( res.getString(R.string.money) + ":" + remain );
			if( unixTime == -TimeZone.getDefault().getRawOffset() ) tvDate.setVisibility(View.INVISIBLE);
			else tvDate.setText(getDate());
			
			switch ( general ) {
			case 0x02:
				sBuilder.append("捷運  " + station + "站  ");
				if (cost == 0) sBuilder.append("進站");
				else sBuilder.append("出站");
				break;
			case 0x11:
				sBuilder.append("大都會客運");
				break;
			case 0x18:
				sBuilder.append("首都客運");
				break;
			case 0x4f:
				sBuilder.append("統一超商");
				break;
			case 0x53:
				sBuilder.append("全家便利商店");
				break;
			default:
				sBuilder.append("未知");
				break;
			}
			
			tvRecord.setText(sBuilder.toString());
			
			return view;
		}
		
		public String toString() {
			StringBuilder sBuilder = new StringBuilder();
			
			sBuilder.append( res.getString(R.string.record) + index + '\n' );

			switch ( general ) {
			case 0x02:
				sBuilder.append("捷運  " + station + "站  ");
				if (cost == 0) sBuilder.append("進站");
				else sBuilder.append("出站");
				break;
			case 0x11:
				sBuilder.append("大都會客運");
				break;
			case 0x18:
				sBuilder.append("首都客運");
				break;
			case 0x4f:
				sBuilder.append("統一超商");
				break;
			case 0x53:
				sBuilder.append("全家便利商店");
				break;
			default:
				sBuilder.append("未知");
				break;
			}
			
			sBuilder.append('\n').append( res.getString(R.string.cost) + ":" + cost + '\t')
				.append(res.getString(R.string.money) + ":" + remain + '\n' )
				.append( getDate() + "\n\n" );
			
			return sBuilder.toString();
		}
		
		public void setIndex( int i) {
			index = i;
		}
		
		public int getStationId() {
			if( general == 0x02 ) return stationId;
			else return -1;
		}
		
		public void setStation( String string ) {
			station = string;
		}
		
		public int getIndex() {
			return index;
		}
		
		public int getRecorId() {
			return recordId;
		}
		
		public long getUnixTime() {
			return unixTime;
		}
		
		public String getDate() {
			Date date = new Date(unixTime);
			return date.toLocaleString();
		}
		
	}
	
	private String getDate( int bId, int offset ) {
		long unixTime = (long) MifareClassicCard.byteArrayToInt( mfc.getBlock(bId).getData(), offset );
		if( unixTime == 0 ) return "";
		unixTime = unixTime * 1000 - TimeZone.getDefault().getRawOffset();
		Date date = new Date(unixTime);
		return date.toLocaleString();
	}
	
	private void sort() {
		for ( int turn = 0; turn < 6; turn++) {
		    int j = turn;
		    for ( int i = turn + 1; i < 6; i++) {
		        if( record[i].getRecorId() < record[j].getRecorId() ) j = i;
		        else if( record[i].getRecorId() == record[j].getRecorId() && record[i].getUnixTime() < record[j].getUnixTime() )  j = i;
		    }
		
		    if ( j != turn ) {
		    	EasyCardRecord temp = record[turn];
		    	record[turn] = record[j];
		    	record[j] = temp;
		    }
		}
	}
	
}
