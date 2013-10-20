package edu.hsnu.AndroidMifareClassic;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class EasyCardStationProvider extends Activity {
    
	interface StationSchema {
		String TABLE_NAME = "StationTable";
		String _ID = "_id";
		String STATION_NAME = "station_name";
		String STATION_ID = "station_id";
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        stationInsert();
    }
    
    private void stationInsert() {
    	Uri uri = Uri.parse( "content://edu.hsnu.AndroidMifareClassic.StationProvider" );
    	ContentValues value = new ContentValues();
    	value.put(StationSchema.STATION_ID, 71 );     value.put(StationSchema.STATION_NAME,  "淡水"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 70 );     value.put(StationSchema.STATION_NAME,  "紅樹林"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 69 );     value.put(StationSchema.STATION_NAME,  "竹圍"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 68 );     value.put(StationSchema.STATION_NAME,  "關渡"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 67 );     value.put(StationSchema.STATION_NAME,  "忠義"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 65 );     value.put(StationSchema.STATION_NAME,  "新北投"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 64 );     value.put(StationSchema.STATION_NAME,  "北投"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 63 );     value.put(StationSchema.STATION_NAME,  "奇岩"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 62 );     value.put(StationSchema.STATION_NAME,  "唭哩岸"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 61 );     value.put(StationSchema.STATION_NAME,  "石牌"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 60 );     value.put(StationSchema.STATION_NAME,  "明德"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 59 );     value.put(StationSchema.STATION_NAME,  "芝山"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 58 );     value.put(StationSchema.STATION_NAME,  "士林"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 57 );     value.put(StationSchema.STATION_NAME,  "劍潭"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 56 );     value.put(StationSchema.STATION_NAME,  "圓山"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 55 );     value.put(StationSchema.STATION_NAME,  "民權西路"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 54 );     value.put(StationSchema.STATION_NAME,  "雙連"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 53 );     value.put(StationSchema.STATION_NAME,  "中山"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 83 );     value.put(StationSchema.STATION_NAME,  "新埔"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 84 );     value.put(StationSchema.STATION_NAME,  "江子翠"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 85 );     value.put(StationSchema.STATION_NAME,  "龍山寺"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 86 );     value.put(StationSchema.STATION_NAME,  "西門"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 88 );     value.put(StationSchema.STATION_NAME,  "善導寺"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 89 );     value.put(StationSchema.STATION_NAME,  "忠孝新生"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 91 );     value.put(StationSchema.STATION_NAME,  "忠孝敦化"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 92 );     value.put(StationSchema.STATION_NAME,  "國父紀念館"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 93 );     value.put(StationSchema.STATION_NAME,  "市政府"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 94 );     value.put(StationSchema.STATION_NAME,  "永春"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 95 );     value.put(StationSchema.STATION_NAME,  "後山埤"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 96 );     value.put(StationSchema.STATION_NAME,  "昆陽"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 97 );     value.put(StationSchema.STATION_NAME,  "南港"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 19 );     value.put(StationSchema.STATION_NAME,  "動物園"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 18 );     value.put(StationSchema.STATION_NAME,  "木柵"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 17 );     value.put(StationSchema.STATION_NAME,  "萬芳社區"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 16 );     value.put(StationSchema.STATION_NAME,  "萬芳醫院"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 15 );     value.put(StationSchema.STATION_NAME,  "辛亥"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 14 );     value.put(StationSchema.STATION_NAME,  "麟光"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 13 );     value.put(StationSchema.STATION_NAME,  "六張犁"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 12 );     value.put(StationSchema.STATION_NAME,  "科技大樓"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 11 );     value.put(StationSchema.STATION_NAME,  "大安"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 9 );     value.put(StationSchema.STATION_NAME,  "南京東路"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 8 );     value.put(StationSchema.STATION_NAME,  "中山國中"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 10 );     value.put(StationSchema.STATION_NAME,  "忠孝復興"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 33 );     value.put(StationSchema.STATION_NAME,  "新店"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 48 );     value.put(StationSchema.STATION_NAME,  "南勢角"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 47 );     value.put(StationSchema.STATION_NAME,  "景安"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 46 );     value.put(StationSchema.STATION_NAME,  "永安市場"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 45 );     value.put(StationSchema.STATION_NAME,  "頂溪"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 34 );     value.put(StationSchema.STATION_NAME,  "新店區公所"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 35 );     value.put(StationSchema.STATION_NAME,  "七張"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 36 );     value.put(StationSchema.STATION_NAME,  "大坪林"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 37 );     value.put(StationSchema.STATION_NAME,  "景美"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 38 );     value.put(StationSchema.STATION_NAME,  "萬隆"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 39 );     value.put(StationSchema.STATION_NAME,  "公館"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 40 );     value.put(StationSchema.STATION_NAME,  "台電大樓"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 41 );     value.put(StationSchema.STATION_NAME,  "古亭"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 42 );     value.put(StationSchema.STATION_NAME,  "中正紀念堂"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 43 );     value.put(StationSchema.STATION_NAME,  "小南門"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 50 );     value.put(StationSchema.STATION_NAME,  "台大醫院"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 51 );     value.put(StationSchema.STATION_NAME,  "台北車站"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 66 );     value.put(StationSchema.STATION_NAME,  "復興崗"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 79 );     value.put(StationSchema.STATION_NAME,  "海山"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 77 );     value.put(StationSchema.STATION_NAME,  "永寧"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 78 );     value.put(StationSchema.STATION_NAME,  "土城"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 82 );     value.put(StationSchema.STATION_NAME,  "板橋"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 81 );     value.put(StationSchema.STATION_NAME,  "府中"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 80 );     value.put(StationSchema.STATION_NAME,  "亞東醫院"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 32 );     value.put(StationSchema.STATION_NAME,  "小碧潭"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 7 );     value.put(StationSchema.STATION_NAME,  "松山機場"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 21 );     value.put(StationSchema.STATION_NAME,  "大直"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 22 );     value.put(StationSchema.STATION_NAME,  "劍南路"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 23 );     value.put(StationSchema.STATION_NAME,  "西湖"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 24 );     value.put(StationSchema.STATION_NAME,  "港墘"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 25 );     value.put(StationSchema.STATION_NAME,  "文德"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 26 );     value.put(StationSchema.STATION_NAME,  "內湖"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 27 );     value.put(StationSchema.STATION_NAME,  "大湖公園"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 28 );     value.put(StationSchema.STATION_NAME,  "葫洲"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 29 );     value.put(StationSchema.STATION_NAME,  "東湖"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 30 );     value.put(StationSchema.STATION_NAME,  "南港軟體園區"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 31 );     value.put(StationSchema.STATION_NAME,  "南港展覽館"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 174 );     value.put(StationSchema.STATION_NAME,  "蘆洲"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 175 );     value.put(StationSchema.STATION_NAME,  "三民高中"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 176 );     value.put(StationSchema.STATION_NAME,  "徐匯中學"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 177 );     value.put(StationSchema.STATION_NAME,  "三和國中"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 178 );     value.put(StationSchema.STATION_NAME,  "三重國小"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 128 );     value.put(StationSchema.STATION_NAME,  "大橋頭"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 130 );     value.put(StationSchema.STATION_NAME,  "中山國小"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 131 );     value.put(StationSchema.STATION_NAME,  "行天宮"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 132 );     value.put(StationSchema.STATION_NAME,  "松江南京"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 127 );     value.put(StationSchema.STATION_NAME,  "台北橋"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 126 );     value.put(StationSchema.STATION_NAME,  "菜寮"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 125 );     value.put(StationSchema.STATION_NAME,  "三重"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 124 );     value.put(StationSchema.STATION_NAME,  "先嗇宮"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 123 );     value.put(StationSchema.STATION_NAME,  "頭前庄"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 122 );     value.put(StationSchema.STATION_NAME,  "新莊"  );     getContentResolver().insert(uri, value );     value.clear();
  	  value.put(StationSchema.STATION_ID, 121 );     value.put(StationSchema.STATION_NAME,  "輔大"  );     getContentResolver().insert(uri, value );     value.clear();
    	
    	Toast.makeText( this, "Done.", Toast.LENGTH_SHORT).show();
    }
    
}