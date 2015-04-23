package primefaces.common;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
	
	private static Logger log = LoggerFactory.getLogger(Util.class);
	private static String MD5 = "MD5";
	
	public static String getMd5(String data) {
		try {
			return getMd5(data.getBytes("utf-8"));
		}  catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static String getMd5(byte[] datas) throws Exception {
		MessageDigest md = MessageDigest.getInstance(MD5);
		md.update(datas);
		byte[] b = md.digest();
		return bytesToHexString(b);
	}
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getMd5("123456"));
	}
}
