package com.ie.icon.common.util;

import java.text.DecimalFormat;
import java.util.Random;

public class Luhn9999 {

    public static void main(String[] args) {
    	// send XX + XX.. = pos + seq
    	String posNo = "001".substring(1);
    	long prmtnSeq = 9999;
    	DecimalFormat df = new DecimalFormat("0000");
    	String luhnParam = posNo + prmtnSeq;
    	String luhnParamDF = posNo + df.format(prmtnSeq);
    	
    	System.out.println("--- luhnParam     : " + luhnParam);
    	System.out.println("--- luhnParam[DF] : " + luhnParamDF);
    	System.out.println("--- luhnGen       : " + Luhn9999.luhnGeneratorN(luhnParam));
        System.out.println("--- luhnGen[DF]   : " + Luhn9999.luhnGeneratorN(luhnParamDF));
    }
    public static String luhnGeneratorN(String ccNumber) {
        String ccNumberValue = Luhn9999.luhnGenerator(ccNumber);
        while(ccNumberValue.length()<7){
            ccNumberValue = Luhn9999.luhnGenerator(ccNumberValue);
        }
        return ccNumberValue;
    }
    public static String luhnGenerator(String ccNumber) {
        String ccNumberValue = "";
        for (int i = 0; i < 10; i++) {
            ccNumberValue = ccNumber + "" + i;
            boolean result = luhnCheck(ccNumberValue);
            if (result) {
                break;
            }
        }

        return ccNumberValue;
    }

    public static boolean luhnCheck(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
    public static int getRandom(int max,int min){
        Random rand=new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }
}