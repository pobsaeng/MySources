package com.ie.icon.common.util;

import java.text.DecimalFormat;

import com.ie.icon.constant.SystemConfigConstant;

public class UserIncentiveUtil {

	/**
	 * @param args
	 */
	private static DecimalFormat df = new DecimalFormat("000000");
	
	public static String formatUserId(String userId) {
		
		String returnUserId = "";
		String empPrefix = "";
		String pcPrefix = "";
		if(SystemConfigConstant.EMP_CODE_PREFIX != null){
			empPrefix = SystemConfigConstant.EMP_CODE_PREFIX;
		}
		if(SystemConfigConstant.PC_CODE_PREFIX != null){
			pcPrefix = SystemConfigConstant.PC_CODE_PREFIX;
		}
		
		//String singleSaleSystem = PropertyUtil.getProperty(ECMConstant.PROPERTY_FILE, "company");
		
		if (userId != null && !userId.trim().equalsIgnoreCase("")) {
			if (SystemConfigConstant.CHECK_SYSTEM != null) {
				returnUserId = getFormat(userId.trim(), empPrefix, pcPrefix);
//				if (SystemConfigConstant.CHECK_SYSTEM.startsWith("HP")) {
//					//...HomePro >> HP00
//					returnUserId = getFormat(userId.trim(), "", "A");
//				} else if (SystemConfigConstant.CHECK_SYSTEM.startsWith("MH")){
//					//...MegaHome >> MH00
//					returnUserId = getFormat(userId.trim(), "M", "MA");
//				}
			}
		}
		
		return returnUserId; 
	}
	
	private static String getFormat(String userId, String prefixEMP, String prefixPC) {
		String returnUserId = userId;
		
		if (userId.length()==6 && userId.substring(0, 1).equals("9")) {
			returnUserId = prefixPC + userId.substring(1);
		} else if (userId.length() < 6) {
			try {
				returnUserId = prefixEMP + df.format(Long.valueOf(userId).longValue());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				returnUserId = "";
			}			
		} else if (userId.length() > 6) {
			returnUserId = returnUserId.substring(0, 6);
		}else {
			returnUserId = prefixEMP + returnUserId;
		}
		return returnUserId;
	}
}
