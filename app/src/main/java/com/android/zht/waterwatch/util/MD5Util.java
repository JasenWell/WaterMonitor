package com.android.zht.waterwatch.util;

import java.security.MessageDigest;

public class MD5Util {
	// MD5加码。32位     
	 public static String getLowerMD5(String inStr) {     
	  MessageDigest md5 = null;     
	  try {     
	   md5 = MessageDigest.getInstance("MD5");     
	  } catch (Exception e) {     
	   System.out.println(e.toString());     
	   e.printStackTrace();     
	   return "";     
	  }     
	  char[] charArray = inStr.toCharArray();     
	  byte[] byteArray = new byte[charArray.length];     
	    
	  for (int i = 0; i < charArray.length; i++)     
	   byteArray[i] = (byte) charArray[i];     
	    
	  byte[] md5Bytes = md5.digest(byteArray);     
	    
	  StringBuffer hexValue = new StringBuffer();     
	    
	  for (int i = 0; i < md5Bytes.length; i++) {     
	   int val = ((int) md5Bytes[i]) & 0xff;     
	   if (val < 16)     
	    hexValue.append("0");     
	   hexValue.append(Integer.toHexString(val));     
	  }     
	    
	  return hexValue.toString();     
	 }     
	 
	 public static String getUpperMD5(String instr) {
		String s = null;
		
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(instr.getBytes());
			byte tmp[] = md.digest(); 
			char str[] = new char[16*2 ]; 
			int k = 0;
			for (int i = 0; i < 16; i++) {
										
				byte byte0 = tmp[i]; 
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str).toUpperCase(); 

		} catch (Exception e) {

		}
		return s;
	}


	    
	 // 可逆的加密算法     
	 public static String encode(String inStr) {     
	  // String s = new String(inStr);     
	  char[] a = inStr.toCharArray();     
	  for (int i = 0; i < a.length; i++) {     
	   a[i] = (char) (a[i] ^ 't');     
	  }     
	  String s = new String(a);     
	  return s;     
	 }     
	    
	 // 加密后解密     
	 public static String decode(String inStr) {     
	  char[] a = inStr.toCharArray();     
	  for (int i = 0; i < a.length; i++) {     
	   a[i] = (char) (a[i] ^ 't');     
	  }     
	  String k = new String(a);     
	  return k;     
	 }     
}
