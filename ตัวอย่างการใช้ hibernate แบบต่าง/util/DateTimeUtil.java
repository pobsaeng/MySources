package com.ie.icon.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * @author Nitipol Thammapusit
 * @date Jan 29, 2004
 */
public class DateTimeUtil {

	public static Timestamp getCurrentTimestamp() {
		Calendar cal_d = Calendar.getInstance(Locale.US);
		Timestamp ts = new Timestamp(cal_d.getTime().getTime());
		
		return ts;

	}
	
	public static Date getCurrentDateTime() {
		Calendar cal_d = Calendar.getInstance(Locale.US);
	
		return cal_d.getTime();

	}
	
	public static Date getDate() {
		Calendar cal_d = Calendar.getInstance(Locale.US);
		Calendar o = Calendar.getInstance(Locale.US);
		cal_d.clear();
		cal_d.set(Calendar.YEAR,  o.get(Calendar.YEAR));
		cal_d.set(Calendar.MONTH,  o.get(Calendar.MONTH));
		cal_d.set(Calendar.DATE,  o.get(Calendar.DATE));

		return cal_d.getTime();

	}
	
	public static Date getTime() {
		Calendar cal_t = Calendar.getInstance(Locale.US);
		Calendar o = Calendar.getInstance(Locale.US);
		cal_t.clear();
		cal_t.set(Calendar.HOUR,  o.get(Calendar.HOUR));
		cal_t.set(Calendar.MINUTE,  o.get(Calendar.MINUTE));
		cal_t.set(Calendar.SECOND,  o.get(Calendar.SECOND));

		return cal_t.getTime();


	}
	public static Calendar getCalendar(int date, int month, int year, int hour, int minute, int second)
	{
		try {
			Calendar c = Calendar.getInstance(Locale.US);
			c.set(year, month-1, date, hour, minute, second);
			return c;
		}catch(Exception ex){ex.printStackTrace(); 
			ex.printStackTrace();
			return null;
		}
	}
		
	/**
	* Returns current date in format "dd Mon yyyy, hh:mm AM".
	*/
	public static String getCurrentDate() {
		String dateStr = "";

		Calendar c = Calendar.getInstance(Locale.US);
		
		// Format the current time
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss:SS");
		//Logger.getLogger(" ").log(Level.WARNING,"currentdate=" + c.getTime());
		
		dateStr = formatter.format(c.getTime());
		
		return dateStr;
	}
	public static String getDate(String format) {
		String dateStr = "";

		Calendar c = Calendar.getInstance(Locale.US);
		
		// Format the current time
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		//Date currentTime = new Date();
		dateStr = formatter.format(c.getTime());
		
		return dateStr;
	}
	
	public static String getDate(String format, Date d) {
			String dateStr = "";
	
			// Format the current time
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			//Date currentTime = new Date();
			dateStr = formatter.format(d);
		
			return dateStr;
		}
	
	
	public static Date getDateFromString(String strDate,String strMonth,String strYear) {
		Date date = null;
		try {
			
			Calendar cal = Calendar.getInstance();
		
			int day = Integer.parseInt( strDate );
			int month = Integer.parseInt( strMonth ) - 1;
			int year = Integer.parseInt( strYear );
		
			cal.set(year,month,day);
			date = cal.getTime();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	

		return date;
	}
	
		
	public static Date getDateFromString(String strDate) {
		// Remove any white spaces before start processing
		strDate = strDate.trim();
		int dateLength = strDate.length();
		Date date = null;
		
		if(dateLength == 10) {
			try {
				StringTokenizer sToken = new StringTokenizer(strDate,"/-");
				
				String token1 = sToken.nextToken();
				String token2 = sToken.nextToken();
				String token3 = sToken.nextToken();
				
				if(token3.length() == 4) {
					date = getDateFromString(token1,token2,token3);
				} else if(token1.length() == 4) {
					date = getDateFromString(token3,token2,token1);
				}
			} catch(Exception e) { /* return null */}
			
		} else if(dateLength == 8) {
			
			String day = "";
			String month = "";
			String year = "";
			
			try {
				int yearTemp = Integer.parseInt( strDate.substring(4,8) );
					
				
				if(yearTemp > 1300) {
	
					day = strDate.substring(0,2);
					month = strDate.substring(2,4);
					year = strDate.substring(4,8);
	
				} else if(yearTemp < 1300) {
					
					year = strDate.substring(0,4);
					month = strDate.substring(4,6);
					day = strDate.substring(6,8);
	
				}
				
				date = getDateFromString(day,month,year);
			} catch(Exception e) { date = null; }			
		}
		
		return date;
	}	
	
	public static String parseXmlDateStringFormat(Date aDate) {
		// Convert the java.util.Date object into date string format "YYYY-MM-DD"
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(aDate);
	}
/*	
	public static org.exolab.castor.types.Date parseCastorDate(Date aDate) {
		// Return castor date type from java.util.Date
		org.exolab.castor.types.Date castorDate = null;
		try {
			castorDate = org.exolab.castor.types.Date.parseDate(parseXmlDateStringFormat(aDate));
		}
		catch (Exception e) {
		}
		return castorDate;
	}
*/
	public static String convertToString(Date aDate, String format) {
		if ( aDate == null )
			return "";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(aDate);
	}
	
	public static boolean before(Date d1, Date d2) {
		if ( d1 == null || d2 == null )
			return false;

		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		d1 = c.getTime();
		
		c = Calendar.getInstance();
		c.setTime(d2);		
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		d2 = c.getTime();
		
		return d1.before(d2);
	}
	
	public static boolean beforeOrEqual(Date d1, Date d2) {
		// Compare time only
		
		if ( d1 == null || d2 == null )
			return false;

		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		d1 = c.getTime();
		
		c = Calendar.getInstance();
		c.setTime(d2);		
		d2 = c.getTime();
		
		if (d1.before(d2) || d1.equals(d2)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean validate(String strDate) {
	    if ( strDate == null || strDate.length() != 8 )
	        return false;

	    try {
	        new Integer(strDate);
	    } catch(Exception e) {
	        return false;
	    }
	    
		int year = Integer.parseInt(strDate.substring(0, 4));
		int month = Integer.parseInt(strDate.substring(4, 6)) - 1;
	    int day = Integer.parseInt(strDate.substring(6, 8));
	    
	    Calendar c = Calendar.getInstance(Locale.US);
	    c.set(year, month, day);
		
	    if ( day != c.get(Calendar.DATE) || month != c.get(Calendar.MONTH) || year != c.get(Calendar.YEAR) )
	        return false;
	    
	    return true;
	}
	public static String beforeCurrentDateStr(){
		String DATE_FORMAT = "dd-MM-yyyy";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		c1.roll(Calendar.DATE, false);
		return sdf.format(c1.getTime()); 
	}
	public static Date beforeCurrentDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();

	}	
	public static Date getDateFromStringClrTime(String strDate,String strMonth,String strYear) {
		Date date = null;
		try {
			
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.clear();
			int day = Integer.parseInt( strDate );
			int month = Integer.parseInt( strMonth ) - 1;
			int year = Integer.parseInt( strYear );
		
			cal.set(year,month,day);
			date = cal.getTime();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	

		return date;
	}	
	
	public static int getDayofWeek(){
		Calendar calendar = Calendar.getInstance(Locale.US);
//		calendar.set(Calendar.DAY_OF_YEAR,0);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		return weekday;
	}
	
	public static Date truncate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
