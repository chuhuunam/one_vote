package com.example.one_vote_service.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String toHHmmDDMMyyyy(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static Timestamp toTimeStamp(Date time){
		Timestamp ts = new Timestamp(time.getTime());
		return ts;
	}

	public static String toTime(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("dd/MM/yyyy").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String toHHmm(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("HH:mm").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public static String toDD(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("dd").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String toHHDDMMyyyy(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("HH:mm dd/MM/yyyy").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String toHH(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("HH").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String toMM(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("mm").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String toMouth(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("MM").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public static String toYear(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("yyyy").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public static String toDDMMyyyy(Timestamp time) {
		try {
			return time == null ? null : (new SimpleDateFormat("yyyy-MM-dd").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public static String toDDMMyyyy1(Date time) {
		try {
			return time == null ? null : (new SimpleDateFormat("yyyy-MM-dd").format(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
}
