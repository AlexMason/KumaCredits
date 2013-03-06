package org.tantrex.kumacredit.util;

import java.util.Arrays;

public class FunctionHelper {

	public static String[] objectToStringArray(Object[] objArray) {
		return Arrays.asList(objArray).toArray(new String[objArray.length]);
	}
	
	public static Object[] trimArrayStart(int start, Object[] array) {
		if (array.length == 0 || (array.length-start) < 0) {
			return new Object[0];
		}
		
		Object[] tempArray = new Object[array.length - start];
		
		for (int i = 0; i < tempArray.length; i++) {
			tempArray[i] = array[i+start];
		}
		
		return tempArray;
	}
	
	public static String mergeArray(String[] args) {
		if (args.length <= 0 || args == null) {
			return "";
		}
		if (args.length == 1) {
			return args[0];
		}
		
		String combine = "";
		for (String arg : args) {
			combine = combine + " " + arg;
		}
		return combine.trim();
	}
	
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}
	
	public static  String getPrettyStringList(String[] string) {
		String text = "";
		
		for (int i = 0; i < string.length; i++) {
			if (i != 0 && string.length > 2) {
				text += ", ";
			}
			if (i == string.length-1 && string.length > 1) {
				text += "and ";
			}
			
			text += string[i];
		}
		
		return text;
	}
	
}
