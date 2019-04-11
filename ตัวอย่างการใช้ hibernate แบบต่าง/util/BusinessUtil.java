/*
 * Created on Oct 11, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ie.icon.common.util;

import java.io.*;

public final class BusinessUtil {

  /**
   * UNICODE to ASCII
   * @param unicode String
   * @return String
   */
  public static String Unicode2ASCII( String unicode ) {
    StringBuffer ascii = new StringBuffer( unicode ) ;
    int code ;
    for ( int i = 0 ;i < unicode.length() ;i++ ) {
      code = ( int ) unicode.charAt( i ) ;
      if ( ( 0xE01 <= code ) && ( code <= 0xE5B ) ) {
        ascii.setCharAt( i, ( char ) ( code - 0xD60 ) ) ;
      }
    }
    return ascii.toString() ;
  }

  /**
   * ASCII to UNICODE
   * @param ascii String
   * @return String
   */
  public static String ASCII2Unicode( String ascii ) {
    StringBuffer unicode = new StringBuffer( ascii ) ;
    int code ;
    for ( int i = 0 ;i < ascii.length() ;i++ ) {
      code = ( int ) ascii.charAt( i ) ;
      if ( ( 0xA1 <= code ) && ( code <= 0xFB ) ) {
        unicode.setCharAt( i, ( char ) ( code + 0xD60 ) ) ;
      }
    }
    return unicode.toString() ;
  }

  /**
   *
   * @param input String
   * @return String
   */
  public static String encode( String input ) {
    try {
      input = new String( input.getBytes( "ISO8859_1" ), "UTF-8" )  ;
    }
    catch ( UnsupportedEncodingException ex ) {
      ex.printStackTrace();
    }
    return input;
  }

}
