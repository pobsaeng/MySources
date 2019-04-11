/*
 * Created on 27 �.�. 2548
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.ApplicationContext;

import com.ie.icon.common.util.AmountUtil;


/**
 * @author Boing
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MessageUtil {
	private static final String ENCODE_DEFAULT = "ISO8859_1";
	private static final String ENCODE_TYPE = "UTF-8";
	
	public static boolean isCorrectDigit(String s) {
	    for (int i=0;i<s.length();i++) {
	        if (Character.isDigit(s.charAt(i)) || s.charAt(i)=='.') {
	        } else {
	            return false;
	        }
	    }
        return true;
	}
	
	public static String out(BigDecimal bd) {
		return AmountUtil.displayAmount(bd);
	}
	
	public static String out(Date date) {
		if ( date == null )
			date = new Date();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	public static String outDateTime(Date date) {
		if ( date == null )
			date = new Date();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		return formatter.format(date);
	}
	
	public static BigDecimal in(String str) {
	    String s = "";
	    for (int i=0;i<str.length();i++) {
	        char c = str.charAt(i);
	        if (c != ',') {
	            s = s + c;
	        }
	    }
	    //return new BigDecimal (str.replace(',',' ').);
	    return new BigDecimal (s);
	}
	
    public static boolean verifyTextInput(String text) {
    	char[] chars = new char[text.length()];
    	text.getChars(0, chars.length, chars, 0);
    	for (int i = 0; i < chars.length; i++) {
    		if (!('0' <= chars[i] && chars[i] <= '9')) {
    			return false;
    		}
    	}
		return true;
    }
    
    public static String padCenter(String text, int width) {
        String output="";
        String space="";

        if (text.length() <= width) {
            long txtlen = text.length()/2;
            long linelen = width/2;
            for (int i=0;i<linelen-txtlen;i++) {
                space = space + " ";
            }
            output  = space + text;
        } else {
            output = text.substring(0,width);
        }
        
        return output;
    }
    public static String padRight(String text, int length) {
        String output="";
        String space="";
        if (text.length() <= length) {
            for (int i=0;i<length-text.length();i++) {
                space = space + " ";
            }
            output  = text + space;
        } else {
            output = text.substring(0,length);
        }
        
        return output;
    }
    
    public static String padLeftWith(String text, int length, String pad) {
        String output="";
        String space="";
        if (text.length() <= length) {
            for (int i=0;i<length-text.length();i++) {
                space = space + pad;
            }
            output  = space + text;
        } else {
            output = text.substring(0,length);
        }
        
        return output;
    }
    
	public static String displayMessage(ApplicationContext context, String code) {
		Locale locale = (Locale)context.getBean("locale");
		String str = context.getMessage(code, null, code, locale);
		return encode(str);
	}
	
	public static String displayMessage(ApplicationContext context, String code, Object[] objects) {
		Locale locale = (Locale)context.getBean("locale");
		
		for ( int index = 0; index < objects.length; index++ ) {
			if ( objects[index] instanceof String )
				objects[index] = encodeDefault((String)objects[index]);
		}
		String str = context.getMessage(code, objects, code, locale);
		return encode(str);
	}
	
	public static String encode( String input ) {
		try {
			input = new String( input.getBytes( ENCODE_DEFAULT ), ENCODE_TYPE )  ;
	    }
	    catch ( UnsupportedEncodingException ex ) {
	    	ex.printStackTrace();
	    }
	    return input;
	}

	public static String encodeDefault( String input ) {
		try {
			input = new String( input.getBytes( ENCODE_TYPE ), ENCODE_DEFAULT )  ;
	    }
	    catch ( UnsupportedEncodingException ex ) {
	    	ex.printStackTrace();
	    }
	    return input;
	}
}