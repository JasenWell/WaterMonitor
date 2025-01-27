package com.hjh.baselib.entity;

/**
 * MD5加密
 * @author hjh
 *  2014-12-9 下午9:37:22
 */
public final class MD5 {

	public static String getMD5(String instr) {
		String s = null;
		
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(instr.getBytes());
			byte tmp[] = md.digest(); 
			char str[] = new char[16 * 2]; 
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
}
