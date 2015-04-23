package com.dad.device.server.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dad.common.entity.Device;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.entity.PollutantsRtdData;
import com.dad.common.util.DateUtil;

public class PatternUtil {

	public static final String bodyDataDelimiter = ";";
	// head data
	public static final Pattern pwPtn = Pattern.compile("(PW=(.*?);)+");
	public static final Pattern mnPtn = Pattern.compile("(MN=(.*?);)+");
	public static final Pattern stPtn = Pattern.compile("(ST=(.*?);)+");
	public static final Pattern cnPtn = Pattern.compile("(CN=(.*?);)+");
	
	public static final Pattern dataTimePtn = Pattern
			.compile("(DataTime=(\\d+);)+");

	// rtd Data
	public static final Pattern rtdPtn = Pattern
			.compile("(([^,]*?)-Rtd=([^,]*)(,{0,1}))+");
	public static final Pattern zsrtdPtn = Pattern
			.compile("(([^,]*?)-ZsRtd=([^,]*)(,{0,1}))+");
	public static final Pattern flagPtn = Pattern
			.compile("(([^,]*?)-Flag=([^,]*)(,{0,1}))+");

	// count data
	public static final Pattern minPtn = Pattern
			.compile("(([^,]*?)-Min=([^,]*)(,{0,1}))+");
	public static final Pattern zsminPtn = Pattern
			.compile("(([^,]*?)-ZsMin=([^,]*)(,{0,1}))+");
	public static final Pattern maxPtn = Pattern
			.compile("(([^,]*?)-Max=([^,]*)(,{0,1}))+");
	public static final Pattern zsmaxPtn = Pattern
			.compile("(([^,]*?)-ZsMax=([^,]*)(,{0,1}))+");
	public static final Pattern avgPtn = Pattern
			.compile("(([^,]*?)-Avg=([^,]*)(,{0,1}))+");
	public static final Pattern zsavgPtn = Pattern
			.compile("(([^,]*?)-ZsAvg=([^,]*)(,{0,1}))+");
	public static final Pattern couPtn = Pattern
			.compile("(([^,]*?)-Cou=([^,]*)(,{0,1}))+");
	
	
/*	private static Map<String, String> reNameMap = new HashMap<String, String>();
	static {
		reNameMap.put("GX01", "S09");
		reNameMap.put("ZF2", "B11");
	}*/
	
	public static String getString(Pattern p, String parse) {
		Matcher m = p.matcher(parse);
		if (m.find()) {
			return m.group(2);
		} else {
			return null;
		}
	}

	public static Device parseHeader(String head) {
		Device d = new Device();
		d.setDeviceId(getString(mnPtn, head));
		d.setPw(getString(pwPtn, head));
		d.setSt(getString(stPtn, head));
		return d;
	}

	public static String[] getCodeAndData(Pattern p, String data) {
		String[] result = null;
		Matcher m = p.matcher(data);
		if (m.find()) {
			result = new String[2];
			result[0] = m.group(2);
			result[1] = m.group(3);
		}
		return result;
	}

	public static ArrayList<PollutantsRtdData> parseRtdData(String body,
			String dateTime, String deviceId) throws ParseException {
		ArrayList<PollutantsRtdData> prtdDatas = new ArrayList<>();
		String[] datas = body.split(bodyDataDelimiter);
		Date time = null;
		try {
			time = DateUtil.getDateByString(dateTime, DateUtil.simpleDatefmt);
		} catch (java.text.ParseException e) {
			throw new ParseException("dataTime error", e);
		}

		for (String data : datas) {
			PollutantsRtdData rtdData = new PollutantsRtdData();
			rtdData.setDataTime(time);

			rtdData.setDeviceId(deviceId);
			String[] rtds = getCodeAndData(rtdPtn, data);

			boolean isEmpty = true;

			if (rtds != null) {
				rtdData.setCode(rtds[0]);
				rtdData.setRtd(Double.valueOf(rtds[1]));
				isEmpty = false;
			}

			String[] zsrtds = getCodeAndData(zsrtdPtn, data);
			if (zsrtds != null) {
				rtdData.setCode(zsrtds[0]);
				rtdData.setZsRtd(Double.valueOf(zsrtds[1]));
				isEmpty = false;
			}

			String[] flags = getCodeAndData(flagPtn, data);
			if (flags != null) {
				rtdData.setCode(flags[0]);
				rtdData.setFlag(flags[1]);
				isEmpty = false;
			}

			if (!isEmpty) {
				prtdDatas.add(rtdData);
			}
		}
		return prtdDatas;
	}

	public static List<PollutantsCountData> parseCountData(String body,
			String dateTime, String deviceId) throws ParseException {
		List<PollutantsCountData> result = new ArrayList<>();
		String[] datas = body.split(bodyDataDelimiter);
		Date time = null;
		try {
			time = DateUtil.getDateByString(dateTime, DateUtil.simpleDatefmt);
		} catch (java.text.ParseException e) {
			throw new ParseException("dataTime error", e);
		}
		for (String data : datas) {
			PollutantsCountData countData = new PollutantsCountData();
			countData.setDeviceId(deviceId);
			countData.setDataTime(time);
			String[] couds = getCodeAndData(couPtn, data);
			boolean isEmpty = true;
			if (couds != null) {
				countData.setCode(couds[0]);
				countData.setCou(Double.valueOf(couds[1]));
				isEmpty = false;
			}

			String[] mins = getCodeAndData(minPtn, data);
			if (mins != null) {
				countData.setCode(mins[0]);
				countData.setMin(Double.valueOf(mins[1]));
				isEmpty = false;
			}

			String[] zsMins = getCodeAndData(zsminPtn, data);
			if (zsMins != null) {
				countData.setCode(zsMins[0]);
				countData.setZsMin(Double.valueOf(zsMins[1]));
				isEmpty = false;
			}

			String[] maxs = getCodeAndData(maxPtn, data);
			if (maxs != null) {
				countData.setCode(maxs[0]);
				countData.setMax(Double.valueOf(maxs[1]));
				isEmpty = false;
			}

			String[] zsMaxs = getCodeAndData(zsmaxPtn, data);
			if (zsMaxs != null) {
				countData.setCode(zsMaxs[0]);
				countData.setZsMax(Double.valueOf(zsMaxs[1]));
				isEmpty = false;
			}

			String[] avgs = getCodeAndData(avgPtn, data);
			if (avgs != null) {
				countData.setCode(avgs[0]);
				countData.setAvg(Double.valueOf(avgs[1]));
				isEmpty = false;
			}

			String[] zsAvgs = getCodeAndData(zsavgPtn, data);
			if (zsAvgs != null) {
				countData.setCode(zsAvgs[0]);
				countData.setZsAvg(Double.valueOf(zsAvgs[1]));
				isEmpty = false;
			}

			if (!isEmpty) {
				result.add(countData);
			}

		}
		return result;
	}
	
/*	private static String renameCode(String code) {
		String newCode =reNameMap.get(code);
		return newCode == null ? code : newCode;
	}
*/
	public static void main(String[] args) throws ParseException {

		String rtd = "DataTime=20150331184300;01-Rtd=18.32,01-ZsRtd=20.32,01-Flag=N;02-Rtd=92.41,02-ZsRtd=102.52,02-Flag=N;03-Rtd=75.56,03-ZsRtd=83.82,03-Flag=N;S02-Rtd=230400.00,S02-Flag=N;GX01-Rtd=230400.00,GX01-Flag=N;B02-Rtd=6.48,B02-Flag=N;S07-Rtd=32.00,S07-Flag=N;S01-Rtd=8.38,S01-Flag=N;S03-Rtd=131.25,S03-Flag=N;S08-Rtd=0.000000,S08-Flag=N";
		System.out.println(parseRtdData(rtd,"20150331184300", "111"));

		String count = "DataTime=20150331184000;S01-Min=8.38,S01-Avg=8.38,S01-Max=8.38;S03-Min=131.25,S03-Avg=131.25,S03-Max=131.25;S08-Min=0.000000,S08-Avg=0.000000,S08-Max=0.000000;01-Cou=0.07,01-Min=18.32,01-Avg=18.32,01-Max=18.32,01-ZsMin=20.32,01-ZsAvg=20.32,01-ZsMax=20.32;02-Cou=0.36,02-Min=92.41,02-Avg=92.41,02-Max=92.41,02-ZsMin=102.52,02-ZsAvg=102.52,02-ZsMax=102.52;03-Cou=0.29,03-Min=75.56,03-Avg=75.56,03-Max=75.56,03-ZsMin=83.82,03-ZsAvg=83.82,03-ZsMax=83.82;S02-Min=230400.00,S02-Avg=230400.00,S02-Max=230400.00;GX01-Cou=38400.00,GX01-Min=230400.00,GX01-Avg=230400.00,GX01-Max=230400.00;B02-Cou=3888.00,B02-Min=6.48,B02-Avg=6.48,B02-Max=6.48;S07-Min=32.00,S07-Avg=32.00,S07-Max=32.00";
		System.out.println(parseCountData(count, "20150331184000", "80001"));

		String count2 = "DataTime=20150331170000;S01-Min=8.38,S01-Avg=8.62,S01-Max=12.00;S03-Min=30.00,S03-Avg=126.19,S03-Max=131.25;S08-Min=0.000000,S08-Avg=0.069470,S08-Max=0.120000;01-Cou=12.36,01-Min=18.32,01-Avg=22.13,01-Max=85.00,01-ZsMin=20.72,01-ZsAvg=25.03,01-ZsMax=96.12;02-Cou=54.61,02-Min=92.41,02-Avg=97.79,02-Max=100.00,02-ZsMin=104.50,02-ZsAvg=110.59,02-ZsMax=113.09;03-Cou=35.89,03-Min=5.00,03-Avg=64.27,03-Max=75.56,03-ZsMin=5.65,03-ZsAvg=72.68,03-ZsMax=85.45;S02-Min=2880000.00,S02-Avg=2880000.00,S02-Max=2880000.00;GX01-Cou=2880000.00,GX01-Min=2880000.00,GX01-Avg=2880000.00,GX01-Max=2880000.00;B02-Cou=558432.00,B02-Min=92.80,B02-Avg=155.12,B02-Max=184.40;S07-Min=32.00,S07-Avg=32.00,S07-Max=32.00";
		System.out.println(parseCountData(count2, "20150331170000",  "80002"));
	}
}
