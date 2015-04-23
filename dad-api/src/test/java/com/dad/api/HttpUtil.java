package com.dad.api;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * url 中带空格会有问题用"%20"替换
 * 
 * */
public class HttpUtil {
	
	private static String SHA1 = "SHA-1";

	public static String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";

	public static String scret = "";
	public static String get(String path) {

		String text = null;
		try {
			path = path.replaceAll(" ", "%20");
			Document doc = Jsoup.connect(path).userAgent(USERAGENT)
					.ignoreContentType(true).timeout(3000).get();
			text = doc.body().text();
			System.out.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;

	}

	private static String secretCheck(TreeMap<String, String> ap) {

		StringBuilder sb = new StringBuilder(scret);
		for (String key : ap.keySet()) {
			sb.append(key).append(ap.get(key));
		}
		sb.append(scret);
		return getSha1(sb.toString());
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

	public static String getSha1(String arg) {
		String sign = null;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA1);
			byte[] digest = md.digest(arg.toString().getBytes());
			sign = bytesToHexString(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sign;
	}
	
}
