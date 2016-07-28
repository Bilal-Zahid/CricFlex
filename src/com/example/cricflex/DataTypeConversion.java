package com.example.cricflex;




public class DataTypeConversion {
	
	 public static int PRINTABLE_ASCII_MIN = 0x20; // ' '
	    public static int PRINTABLE_ASCII_MAX = 0x7E; // '~'

	    public static boolean isPrintableAscii(int c) {
	        return c >= PRINTABLE_ASCII_MIN && c <= PRINTABLE_ASCII_MAX;
	    }

	    public static String bytesToHex(byte[] data) {
	        return bytesToHex(data, 0, data.length);
	    }

	    public static String bytesToHex( byte[] data, int offset, int length) {
	        if (length <= 0) {
	            return "";
	        }
	        String hex=new String();
	        int temp = ((int)data[0] );
	      //  StringBuilder hex = new StringBuilder();
	      //  for (int i = offset; i < offset + length; i++) {
	        if(temp<0)
	        hex=String.valueOf(-1*temp);
	        else
	        	hex=String.valueOf(temp);
	            	
	            	
	        
	       // hex.deleteChart(0);
	        return hex;
	    }

	    public static String bytesToAsciiMaybe(byte[] data) {
	        return bytesToAsciiMaybe(data, 0, data.length);
	    }

	    public static String bytesToAsciiMaybe(byte[] data, int offset, int length) {
	        StringBuilder ascii = new StringBuilder();
	        boolean zeros = false;
	        for (int i = offset; i < offset + length; i++) {
	            int c = data[i] & 0xFF;
	            if (isPrintableAscii(c)) {
	                if (zeros) {
	                    return null;
	                }
	                ascii.append((char) c);
	            } else if (c == 0) {
	                zeros = true;
	            } else {
	                return null;
	            }
	        }
	        return ascii.toString();
	    }

	    public static byte[] hexToBytes(String hex) {
	    	int len = hex.length();
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                                 + Character.digit(hex.charAt(i+1), 16));
	        }
	        return data;
	        
	    }
	    

}
