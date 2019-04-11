package com.ie.icon.common.util;

import java.math.BigDecimal;

import com.ie.icon.constant.Constant;

public class VATUtil {
	
	public static BigDecimal getCalculatedVat(BigDecimal netItemAmount, BigDecimal percenTaxRate) {
    	return netItemAmount.multiply(percenTaxRate).divide(percenTaxRate.add(new BigDecimal("100")), 2, BigDecimal.ROUND_HALF_UP);
    }
	
	public static boolean isCalculateVAT(String taxCodeId) {
		return !taxCodeId.equals(Constant.TaxCode.TAXCODEID_NONVAT);
	}
	
	public static BigDecimal getPercentTaxRate(BigDecimal taxRate) {
		return taxRate.multiply(new BigDecimal("100"));
	}
	
	public static String getStrPercentTaxRate(BigDecimal taxRatePercent) {
		return taxRatePercent.intValue() + "%";
	}
}
