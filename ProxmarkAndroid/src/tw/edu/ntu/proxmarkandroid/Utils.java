package tw.edu.ntu.proxmarkandroid;

public class Utils {

    public static byte[] StringToHex( String s ){
    	byte [] result = new byte[s.length()/2];
    	s = s.toLowerCase();
    	for ( int i = 0; i < s.length()/2 ; i++ )
    		result[i] = (byte)( ( charToInt(s.charAt(2*i))*16 + charToInt(s.charAt( 2*i + 1 )) ) );
    	return result;
    }
    
    public static int charToInt( char c){
    	return c > 58 ? c-87 : c-48 ;
    }
    public static String HexToString (byte[] b)
    {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
            result += " ";
        }
        return result;
    }
	
}
