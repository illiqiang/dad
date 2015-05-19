package primefaces.common;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static Logger log = LoggerFactory.getLogger(Util.class);
	private static String MD5 = "MD5";

	public static List<String> allMonth = new ArrayList<>();
	public static List<String> allYear = new ArrayList<>();
	public static List<String> allHour = new ArrayList<>();
	static {
		allMonth.add("01");
		allMonth.add("02");
		allMonth.add("03");
		allMonth.add("04");
		allMonth.add("05");
		allMonth.add("06");
		allMonth.add("07");
		allMonth.add("08");
		allMonth.add("09");
		allMonth.add("10");
		allMonth.add("11");
		allMonth.add("12");
		
		allYear.add("2015");
		allYear.add("2016");
		allYear.add("2017");
		allYear.add("2018");
		allYear.add("2019");
		allYear.add("2020");
		allYear.add("2021");
		allYear.add("2022");
		allYear.add("2023");
		allYear.add("2024");
		allHour.add("00");allHour.add("01");allHour.add("02");allHour.add("03");allHour.add("04");
		allHour.add("05");allHour.add("06");allHour.add("07");allHour.add("08");allHour.add("09");
		allHour.add("10");allHour.add("11");allHour.add("12");allHour.add("13");allHour.add("14");
		allHour.add("15");allHour.add("16");allHour.add("17");allHour.add("18");allHour.add("19");
		allHour.add("20");allHour.add("21");allHour.add("22");allHour.add("23");
	}

	public static String getMd5(String data) {
		try {
			return getMd5(data.getBytes("utf-8"));
		} catch (Exception e) {
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
