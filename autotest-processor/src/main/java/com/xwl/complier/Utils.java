package com.xwl.complier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static String getParamsList(String method) {
		Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
		Matcher matcher = pattern.matcher(method);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}
}
