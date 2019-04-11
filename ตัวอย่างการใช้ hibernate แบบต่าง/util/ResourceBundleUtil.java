package com.ie.icon.common.util;

import java.util.*;

public class ResourceBundleUtil {
	
	private Locale locale;
	private ResourceBundle posUIResourceBundle;
	private ResourceBundle messageResourceBundle;
	private ResourceBundle edcResponseResourceBundle;
	private ResourceBundle cashResourceBundle;
	private ResourceBundle posConfigResourceBundle;
	
	public ResourceBundleUtil(Locale locale) {
		
        this.locale = locale;
        
		System.out.println(">>> ResourceBundleUtil @ locale=" + locale.getLanguage());
		this.posUIResourceBundle = ResourceBundle.getBundle("posUI", locale);
		this.messageResourceBundle = ResourceBundle.getBundle("message", locale);
		this.cashResourceBundle = ResourceBundle.getBundle("cash", locale);
		this.edcResponseResourceBundle = ResourceBundle.getBundle("edcResponse", locale);
		this.posConfigResourceBundle = ResourceBundle.getBundle("posConfig");
	}
	
	public String getPosUIText(String key) {
		String result = "";
		
		try {
			result = this.posUIResourceBundle.getString(key).trim();
		}
		catch (Exception e) {
			result = "?P?" + key + "?P?";
		}
		return result;
	}

	public String getMessageText(String key) {
		String result = "";
		
		try {
			result = this.messageResourceBundle.getString(key).trim();
		}
		catch (Exception e) {
			result = "?M?" + key + "?M?";
		}
		return result;
	}

	public String getEDCRespText(String key) {
		String result = "";
		
		try {
			result = this.edcResponseResourceBundle.getString(key).trim();
		}
		catch (Exception e) {
			result = "?E?" + key + "?E?";
		}
		return result;
	}
	
	public String getCashText(String key) {
		String result = "";
		
		try {
			result = this.cashResourceBundle.getString(key).trim();
		}
		catch (Exception e) {
			result = "?C?" + key + "?C?";
		}
		return result;
	}	
	
	public String getPosConfigText(String key) {
		String result = "";
		
		try {
			result = this.posConfigResourceBundle.getString(key).trim();
		}
		catch (Exception e) {
			result = "?PC?" + key + "?PC?";
		}
		return result;
	}	
	
	public Enumeration getKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
