package tw.edu.ntu.proxmarkandroid;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

public class UploadAsyncTask extends AsyncTask<Void, Void, Void> {

	private enum AuthState{
		WAIT,
		CHOOSE_CARD,
		AUTH,
		NT,
		NRAR,
		AT
	}; 
	
	private final static int SERVER_PORT = 11111;
	private final static String SERVER_IP = "140.112.175.112";
	
	protected ArrayList<String> session;
	
	protected String uuid;
	protected String nt;
	protected String nr;
	protected String ar;
	protected String at;
	
	public UploadAsyncTask(String param) {
		super.onPreExecute();
		session = new ArrayList<String>();
		fetch(param);
		session.clear();
		session.add(uuid);
		session.add(nt);
		session.add(nr);
		session.add(ar);
		session.add(at);
		
		for (int i = 0 ; i < session.size() ; ++i) {
			Log.d("!!", "!! res: " + session.get(i));
		}
	}
	
	@Override
    protected Void doInBackground(Void... params) {
//		Socket socket;
//		try {
//			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
//			Log.d("TCP", "C: Connecting...");
//
//			try {
//				socket = new Socket(serverAddr, 11111);
//				
//				try {
//					for (int i = 0 ; i < session.size() ; ++i) {
//						Log.d("TCP", "C: Sending: '" + session.get(i) + "'");
//						PrintWriter out = new PrintWriter(new BufferedWriter(
//								new OutputStreamWriter(socket.getOutputStream())),
//								true);
//						out.println(session.get(i));
//					}
//					
//					PrintWriter out = new PrintWriter(new BufferedWriter(
//							new OutputStreamWriter(socket.getOutputStream())),
//							true);
//					out.println("tttt");
//				} catch (Exception e) {
//					Log.e("TCP", "S: Error", e);
//				} finally {
//
//					socket.close();
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		} catch (UnknownHostException e1) {
//			e1.printStackTrace();
//		}
		return null;
    }
	
	private String fetch(String str) {
		AuthState state = AuthState.WAIT;
		String[] arr = str.split("\n");
		for(int i = 0 ; i < arr.length ; ++i) {
			session.add(arr[i]);
		}
		
		// remove 0~7
		for (int i = 0 ; i < 8 ; i++)
			session.remove(i);
		
		for (int i = 0 ; i < session.size() ; ++i) {
			String data = session.get(i);
			switch (state) {
			case WAIT:
				if (getInput(data, 1).equals("93") && getInput(data, 2).equals("70")) {
					uuid = getInput(data, 3) + getInput(data, 4) + getInput(data, 5) + getInput(data, 6);
					state = AuthState.CHOOSE_CARD;
 				}
				break;
			case CHOOSE_CARD:
				if (getInput(data, 1).equals("60")) {
					state = AuthState.AUTH;
				}
				break;
			case AUTH:
				nt = "" + getInput(data, 1) + getInput(data, 2) + getInput(data, 3) + getInput(data, 4);
				state = AuthState.NT;
				break;
			case NT:
				nr = "" + getInput(data, 1) + getInput(data, 2) + getInput(data, 3) + getInput(data, 4);
				ar = "" + getInput(data, 5) + getInput(data, 6) + getInput(data, 7) + getInput(data, 8);
				state = AuthState.NRAR;
				break;
			case NRAR:
				at = "" + getInput(data, 1) + getInput(data, 2) + getInput(data, 3) + getInput(data, 4);
				state = AuthState.AT;
				break;
			case AT:
				i = session.size(); // break;
				break;
			default:
				
				break;
			}
		}
		
		return str;
	}
	
	private String getInput(String input, int index) {
		switch (index) {
		case 0:
			return "" + input.charAt(16) + input.charAt(17) + input.charAt(18);
		default:
			if (input.length() < index * 4 + 18) {
				return "";
			} else {
				return "" + input.charAt(index * 4 + 16) + input.charAt(index * 4 + 17);
			}
		}
	}
	
	private boolean isDigit(char ch) {
		return (ch >= '0'  && ch <= '9') || (ch >= 'a' && ch <= 'f');
	}
}