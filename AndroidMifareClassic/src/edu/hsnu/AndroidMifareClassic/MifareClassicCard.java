package edu.hsnu.AndroidMifareClassic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Environment;

public class MifareClassicCard {
	private byte[][] key_a;
	private byte[][] key_b;
	private byte[] uid;
	private int size;
	private int sCount;
	private int bCount;
	private int[] bCountInSector;
	private boolean auth = true;
	private MifareClassicBlock[] block;
	private MifareClassic mfc;
	
	public MifareClassicCard( Tag tag ) throws Exception {
		mfc = MifareClassic.get(tag);
		uid = tag.getId();
		size = mfc.getSize();
		sCount = mfc.getSectorCount();
		bCount = mfc.getBlockCount();
		bCountInSector = new int[sCount];
		key_a = new byte[sCount][6];
		key_b = new byte[sCount][6];
		block = new MifareClassicBlock[bCount];
		for (int i = 0; i < sCount; i++) {
			bCountInSector[i] = mfc.getBlockCountInSector(i);
		}
		for (int i = 0; i < bCount; i++) {
			block[i] = new MifareClassicBlock( mfc.blockToSector(i), i, null);
		}
		
		String filename= byteToString(uid).replaceAll(" ", "") + ".mfd";
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mfd/" + filename );
		byte[] buffer = new byte[16];
		try {
			FileInputStream input = new FileInputStream(file);
			for (int i = 0; i < sCount; i++) {
				input.skip( (bCountInSector[i] - 1) * 16 );
				input.read(buffer, 0, 16);
				System.arraycopy(buffer, 0, key_a[i], 0, 6);
				System.arraycopy(buffer, 10, key_b[i], 0, 6);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			for (int i = 0; i < sCount; i++) {
				System.arraycopy(MifareClassic.KEY_DEFAULT, 0, key_a[i], 0, 6);
				System.arraycopy(MifareClassic.KEY_DEFAULT, 0, key_b[i], 0, 6);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("READ_FILE_ERROR");
		}
		
		try {
			mfc.connect();
			int bId = 0;
			for (int i = 0; i < sCount; i++) {
				auth &= mfc.authenticateSectorWithKeyA(i, key_a[i]);
				if( !auth ) break;
				for (int j = 0; j < bCountInSector[i]; j++, bId++ ) {
					buffer = mfc.readBlock(bId);
					if (j == bCountInSector[i] - 1) {
						System.arraycopy(key_a[i], 0, buffer, 0, 6);
						System.arraycopy(key_b[i], 0, buffer, 10, 6);
						block[bId].isKey();
					}
					block[bId].setBlock(buffer);
				}
				auth &= mfc.authenticateSectorWithKeyB(i, key_b[i]);
				if( !auth ) break;
			}
			mfc.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("CONNECT_ERROR");
		}
		
		if (auth && !file.exists()) {
			try { 
				file.createNewFile();
				FileOutputStream output = new FileOutputStream(file);
				buffer = new byte[16];
				for (int i = 0; i < sCount; i++) {
					for (int j = 1; j < bCountInSector[i]; j++) {
						output.write(buffer);
					}
					output.write(key_a[i]);
					output.write(buffer,0,4);
					output.write(key_b[i]);
				}
			} catch (IOException e) { 
				e.printStackTrace();
				throw new Exception("WRITE_FILE_ERROR");
			}
		}
	}
	
	private void readBlock( int bId ) throws IOException {
		int sId = block[bId].getSectorId();
		try {
			block[bId].setBlock(mfc.readBlock(bId));
		} catch (IOException e) {
			e.printStackTrace();
			mfc.authenticateSectorWithKeyA(sId, key_a[sId]);
			try { block[bId].setBlock(mfc.readBlock(bId)); }
			catch (IOException ex) {
				ex.printStackTrace();
				mfc.close();
				if( ex.getMessage().equals("Transceive failed")) throw ex;
				throw new IOException("PERMISSION");
			}
		}
	}
	
	public void writeBlock( int bId, String blkData ) throws IOException {
		byte[] blk = stringToByte( 16, blkData );
		int sId = block[bId].getSectorId(); 
		mfc.connect();
		mfc.authenticateSectorWithKeyA(sId, key_a[sId]);
		
		try {
			mfc.writeBlock(bId, blk);
		} catch (IOException e) {
			e.printStackTrace();
			mfc.authenticateSectorWithKeyB(sId, key_b[sId]);
			try { mfc.writeBlock(bId, blk); }
			catch (IOException ex) {
				ex.printStackTrace();
				mfc.close();
				if( ex.getMessage().equals("Transceive failed")) throw ex;
				throw new IOException("PERMISSION");
			} 
		}
		
		readBlock(bId);
		
		mfc.close();
	}
	
	public void increment( int bId, int value ) throws IOException {
		int sId = block[bId].getSectorId(); 
		mfc.connect();
		mfc.authenticateSectorWithKeyA(sId, key_a[sId]);
		
		try {
			mfc.increment( bId, value);
			mfc.transfer(bId);
		} catch (IOException e) {
			e.printStackTrace();
			mfc.authenticateSectorWithKeyB(sId, key_b[sId]);
			try {
				mfc.increment( bId, value);
				mfc.transfer(bId);
			} catch (IOException ex) {
				ex.printStackTrace();
				mfc.close();
				if( ex.getMessage().equals("Transceive failed")) throw ex;
				throw new IOException("PERMISSION");
			}
		}
		
		readBlock( bId );
		
		mfc.close();
	}
	
	public void decrement( int bId, int value ) throws IOException {
		int sId = block[bId].getSectorId(); 
		mfc.connect();
		mfc.authenticateSectorWithKeyA(sId, key_a[sId]);
		
		try {
			mfc.decrement( bId, value);
			mfc.transfer(bId);
		} catch (IOException e) {
			e.printStackTrace();
			mfc.authenticateSectorWithKeyB(sId, key_b[sId]);
			try {
				mfc.decrement( bId, value);
				mfc.transfer(bId);
			} catch (IOException ex) {
				ex.printStackTrace();
				mfc.close();
				if( ex.getMessage().equals("Transceive failed")) throw ex;
				throw new IOException("PERMISSION");
			}
		}
		
		readBlock( bId );
		
		mfc.close();
	}
	
	public void setValue( int bId, int value ) throws IOException {
		String blank = "00000000ffffffff0000000000ff00ff";
		writeBlock(bId, blank);
		increment(bId, value);
	}
	
	public boolean isAuth() {
		return auth;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getSectorCount() {
		return sCount;
	}
	
	public int getBlockCount() {
		return bCount;
	}
	
	public int getBlockCountInSector( int sId ) {
		return bCountInSector[sId];
	}
	
	public String getUid() {
		return byteToString(uid);
	}
	
	public MifareClassicBlock getBlock( int bId ) {
		return block[bId];
	}
	
	class MifareClassicBlock {
		private byte[] block;
		private int sector;
		private int blockId;
		private boolean isKey;
		
		public MifareClassicBlock( int sId, int bId, byte[] blk ) {
			setBlock(blk);
			sector = sId;
			blockId = bId;
			isKey = false;
		}
		
		public void isKey() {
			isKey = true;
		}
		
		public void setBlock( byte[] blk ) {
			block = blk;
		}
		
		public byte[] getData() {
			return block;
		}
		
		public int getValue() {
			if( !isValueBlock() ) return Integer.MIN_VALUE;
			return byteArrayToInt( block, 0);
		}
		
		public int getSectorId() {
			return sector;
		}
		
		public int getBlockId() {
			return blockId;
		}
		
		public String toString() {
			return byteToString( block );
		}
		
		public boolean isValueBlock() {
			int[] value = { 0, 0, 0 };
			for (int i = 0; i < 3; i++ ) {
				value[i] = byteArrayToInt( block, i * 4 );
			}

			return ( value[0] == value[2] && value[0] == ~value[1] ) & 
					( block[12] == block[14] && block[13] == block[15] && block[12] == (~block[13] & 0xff) );
		}
		
		public boolean canWrite() {
			if ( blockId == 0 ) return false;
			return !isKey;
		}
	}
	
	private String byteToString( byte[] bytes) {
		StringBuilder sBuilder = new StringBuilder();
		
		for (byte b : bytes) {
			sBuilder.append( Integer.toHexString( ( b & 0xff ) + 0x100 ).substring(1) + " ");
		}
		
		return sBuilder.toString();
	}
	
	private byte[] stringToByte( int count, String data ) {
		byte[] bytes = new byte[count];
		
		for ( int i = 0; i < count; i++ ) {
			String b = data.substring(i * 2, i * 2 + 2 );
			bytes[i] = (byte) Integer.parseInt(b, 16);
		}
		
		return bytes;
	}
	
	public static int byteArrayToInt(byte[] b, int offset ) {
		ByteBuffer buffer = ByteBuffer.wrap( b, offset, 4 );
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }
	
	public static short byteArrayToShort(byte[] b, int offset ) {
        ByteBuffer buffer = ByteBuffer.wrap( b, offset, 2 );
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }
}
