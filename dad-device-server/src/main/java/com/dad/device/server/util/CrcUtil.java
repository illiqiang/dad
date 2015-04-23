package com.dad.device.server.util;

public class CrcUtil {
	
	public static final String hexString = "%04X";
	public static final int first = 0xffff;
	public static final int plynm = 0xa001;
	
	public static String getCrcHexString(char[] data) {
		int i, j, len = data.length;
		int temp1;
		int temp = first;

		for (i = 0; i < len; i++) {
			temp = data[i] ^ ((temp >> 8) & 0xFF);
			for (j = 0; j < 8; j++) {
				temp1 = temp & 0x01;
				temp >>= 1;
				if (temp1 == 1)
					temp = temp ^ plynm;
			}
		}
		return String.format(hexString, temp);
	}
	
	public static boolean checkCrc(String text, String crcCode) {
		return getCrcHexString(text.toCharArray()).equalsIgnoreCase(crcCode);
	}
	
	public static void main(String[] args) {
		String a ="##0334ST=32;CN=2051;PW=123456;MN=88888880000001;CP=&&DataTime=20150203225000;001-Cou=0.00,001-Min=7.00,001-Avg=7.00,001-Max=7.00;B01-Cou=4.14,B01-Min=6.90,B01-Avg=6.90,B01-Max=6.90;B11-Cou=0.00,B11-Min=123456.00,B11-Max=123456.00;060-Cou=0.05,060-Min=0.00,060-Avg=12.14,060-Max=12.25;011-Cou=0.05,011-Min=12.38,011-Avg=12.38,011-Max=12.38&&8EC1";
		
		String text = a.substring(6, a.length()-4);
		System.out.println(CrcUtil.checkCrc(text, a.substring(a.length()-4)));
		System.out.println(a.substring(a.length()-4));
		System.out.println(text.substring(0, text.length()-2));
		
	}
}
