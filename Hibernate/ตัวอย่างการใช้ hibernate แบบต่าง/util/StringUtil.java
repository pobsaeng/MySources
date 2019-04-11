/*
 * Created on Oct 11, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtil {

	public static String substring(String in, int beginIndex, int endIndex) {
		String out = null;
		if (in != null) {
			out = in.substring(beginIndex, endIndex).trim();

			if ("".equals(out) || out.length() == 0) {
				out = null;
			}
		}
		return out;
	}
	/**
	 * UNICODE to ASCII
	 * @param unicode String
	 * @return String
	 */
	public static String Unicode2ASCII(String unicode) {
		StringBuffer ascii = new StringBuffer(unicode);
		int code;
		for (int i = 0; i < unicode.length(); i++) {
			code = (int) unicode.charAt(i);
			if ((0xE01 <= code) && (code <= 0xE5B)) {
				ascii.setCharAt(i, (char) (code - 0xD60));
			}
		}
		return ascii.toString();
	}

	/**
	 * ASCII to UNICODE
	 * @param ascii String
	 * @return String
	 */
	public static String ASCII2Unicode(String ascii) {
		StringBuffer unicode = new StringBuffer(ascii);
		int code;
		for (int i = 0; i < ascii.length(); i++) {
			code = (int) ascii.charAt(i);
			if ((0xA1 <= code) && (code <= 0xFB)) {
				unicode.setCharAt(i, (char) (code + 0xD60));
			}
		}
		return unicode.toString();
	}

	public static String leftTirmZero(String msg) {
		if (msg != null) {
			for (int i = 0; i < msg.length(); i++) {
				if (msg.charAt(i) != '0') {
					return msg.substring(i, msg.length());
				}
			}
		}
		return msg;
	}

	public static String displayEmptyString(String msg) {
		if (msg == null) {
			return "";
		}else{
			return msg;
		}
	}
	public static String displaySpecialCharacter(String msg) {
		if (msg == null) {
			return "";
		}else{
			String retMsg = msg.replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
			
			return retMsg;
		}
	}
	
	/** isEmailValid: Validate email address using Java reg ex.  
	* This method checks if the input string is a valid email address.  
	* @param email String. Email address to validate  
	* @return boolean: true if email address is valid, false otherwise.  
	*/  
	  
	public static boolean isEmailValid(String email){   
		boolean isValid = false;   
	  
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";   
		CharSequence inputStr = email;   
		//	Make the comparison case-insensitive.   
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);   
		Matcher matcher = pattern.matcher(inputStr);   
		if(matcher.matches()){   
				isValid = true;   
		}   
		return isValid;   
	}
	
	public static String String2Unicode(String str) {
		StringBuffer ostr = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if ((ch >= 0x0020) && (ch <= 0x007e)) {
				ostr.append(ch);
			} else {
				ostr.append("\\u"); // standard unicode format.
				String hex = Integer.toHexString(str.charAt(i) & 0xFFFF);
				for (int j = 0; j < 4 - hex.length(); j++)
					// Prepend zeros because unicode requires 4 digits
					ostr.append("0");
				ostr.append(hex.toLowerCase());
			}
		}
		return (new String(ostr));
	}
	
}
