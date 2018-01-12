package com.star.eagleme.utils;

import java.text.DecimalFormat;

/**
 * <pre>
 *     author : star
 *     time   : 2018/01/02
 *     desc   : 数值格式化
 * </pre>
 */
public class ValueUtils {

	public static String getValue(long value) {

		if (value > 100000) {
			double n = value / (double) 10000;
			n = (double) Math.round(n * 100) / 100;
			return String.valueOf(n) + "w";
		}

		return String.valueOf(value);

	}

	public static String getUsValue(long value) {

		DecimalFormat df = new DecimalFormat("#.00");
		if (value >= 100000 && value <= 10000000) {

			float newValue = value / (float) 1000;
			if (newValue > 0) {
				return df.format(newValue) + "K";
			}
			return newValue + "K";

		}
		else if (value >= 10000000 && value < 1000000000) {

			float newValue = value / (float) 1000000;
			if (newValue > 0) {
				return df.format(newValue) + "M";
			}
			return newValue + "M";

		}
		else if (value > 1000000000) {

			float newValue = value / (float) 1000000000;
			if (newValue > 0) {

				return df.format(newValue) + "B";

			}
			return newValue + "B";

		}
		else {
			return value + "";
		}

	}

	public static String getUsKvalue(long value) {

		DecimalFormat df = new DecimalFormat("#.00");
		if (value >= 1000000) {

			float newValue = value / (float) 1000;
			if (newValue > 0) {
				return df.format(newValue) + "K";
			}
			return value + "K";

		}
		else {

			return value + "";

		}

	}

	public static String getKCoinsValue(long value) {
		DecimalFormat df = new DecimalFormat("#.00");
		if (value >= 10000) {

			float newValue = value / (float) 1000;
			if (newValue > 0) {
				return df.format(newValue) + "K";
			}
			return value + "K";

		}
		else {

			return value + "";

		}
	}


}
