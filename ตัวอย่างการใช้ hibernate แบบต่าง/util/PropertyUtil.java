/*
 * Created on Jan 31, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ie.icon.common.util;

/**
 * @author jintanac
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ie.icon.constant.SessionConstant;


	public class PropertyUtil {
		
		private static final String ENCODE_DEFAULT = "ISO8859_1";
		private static final String ENCODE_TYPE = "UTF-8";
		
		private PropertyUtil() {}
		
		
		private static Locale getUserLocale() {
			try {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
	
				Locale userLocale = (Locale) session.getAttribute(SessionConstant.USER_LOCALE);
	
				return userLocale;
				
			} catch (Throwable e) {
				return null;
			}
		}
		/**
		 * Get string property (with default value)
		 */
		public static String getProperty(String propFile, String name, String defaultValue) {
			try {
				Locale userLocale = getUserLocale();
				
				ResourceBundle rs = null;
				if(userLocale != null) {
					rs = PropertyResourceBundle.getBundle(propFile, userLocale);
				} else {
					rs = PropertyResourceBundle.getBundle(propFile);
				}
				
				String ret = rs.getString(name);
				if (ret == null) {
					return defaultValue;
				}
				else {
					ret = encode( ret );
				}
				return ret;
			}
			catch (Exception e) {
				return defaultValue;
			}
		}
	
		/**
		* Get string property (with default value)
		*/
		

		
		public static String getProperty(String propFile, String name) {
			return getProperty(propFile, name, true);
		}
		
		public static String getProperty(String propFile, String name, boolean encode) {
			try {
				Locale userLocale = getUserLocale();
				
				ResourceBundle rs = null;
				if(userLocale != null) {
					rs = PropertyResourceBundle.getBundle(propFile, userLocale);
				} else {
					rs = PropertyResourceBundle.getBundle(propFile);
				}
				String ret = rs.getString(name);
				
				if ( ret != null ) {
					if(encode) {
						ret = encode(ret.trim());
					} else {
						ret = ret.trim();
					}
				}
				return ret;
			}
			catch (Exception e) {
				//e.printStackTrace();
				return null;
			}
		}
	
		/**
		 * Get boolean property (with no exception thrown)
		 */
		public static boolean getBooleanProperty(String propFile, String name, boolean defaultValue) {
			boolean result = defaultValue;
			try {
				String value = getProperty(propFile, name);
				if (value != null) {
					result = Boolean.valueOf(value).booleanValue();
				}
			}
			catch(Exception t) {
			}
			return result;
		}

		/**
		 * Get boolean property (with no exception thrown)
		 */
		public static boolean getBooleanProperty(String propFile, String name) {
			boolean result = false;
			try {
				String value = getProperty(propFile, name);
				if (value != null) {
					result = Boolean.valueOf(value).booleanValue();
				}
			}
			catch(Exception t) {
			}
		
			return result;
		}

		/**
		 * Get integer property 
		 */
		public static int getIntProperty(String propFile, String name) throws IOException {
			int result;
			try {
				result = Integer.parseInt((String) getProperty(propFile, name).trim());
			}
			catch (NumberFormatException e) {
				throw new IOException("Invalid number format");
			}
			return result;
		}

		/**
		 * Get integer property (with no exception thrown)
		 */
		public static int getIntProperty(String propFile, String name, int defaultValue) {
			int result=defaultValue;
			try {
				result = Integer.parseInt((String) getProperty(propFile, name).trim());
			}
			catch (NumberFormatException e) {
			}
			return result;
		}

		/**
		 * Get long property  with exception thrown
		 */
		public static long getLongProperty(String propFile, String name) throws IOException {
		
			long result;
			try {
				result = Long.parseLong((String) getProperty(propFile, name).trim());
			}
			catch (NumberFormatException e) {
				throw new IOException("Invalid number format");
			}
			return result;
		}

		/**
		 * Get long property ( with no exception thrown)
		 */
		public static long getLongProperty(String propFile, String name, long defaultValue) {
			long result=defaultValue;
			try {
				result = Long.parseLong((String) getProperty(propFile, name).trim());
			}
			catch (NumberFormatException e) {
			}
		
			return result;
		}
		
		  private static String encode( String input ) {
		    try {
		      input = new String( input.getBytes( ENCODE_DEFAULT ), ENCODE_TYPE )  ;
		    }
		    catch ( UnsupportedEncodingException ex ) {
		      ex.printStackTrace();
		    }
		    return input;
		  }
	}

