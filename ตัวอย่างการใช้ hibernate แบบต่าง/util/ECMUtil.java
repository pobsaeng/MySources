package com.ie.icon.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.ie.icon.constant.ECMConstant;
import com.ie.icon.domain.sale.DataDocumentObj;
import com.ie.icon.domain.sale.Refund;
import com.ie.icon.domain.sale.TaxInvoice;

public class ECMUtil {
	private static final String ENCODE_DEFAULT = "ISO8859_1";
	private static final String ENCODE_TYPE = "UTF-8";
	private static final String ENCODE_TYPE_TIS = "TIS-620";
	public static final String AMPERSAND = "&";
	public static final String AMPERSANDREPLACEMENT = "&amp;";
	public static final String LESSTHAN = "<";
	public static final String LESSTHANREPLACEMENT = "&lt;";
	public static final String GREATERTHAN = ">";
	public static final String GREATERTHANREPLACEMENT = "&gt;";
	public static final String APOSTROPHE = "'";
	public static final String APOSTROPHEREPLACEMENT = "&apos;";
	public static final String QUOTATIONMARK = "\"";
	public static final String QUOTATIONMARKREPLACEMENT = "&quot;";	
	public static final int LENGTHESCAPE = 5;
	public static String[] strEscape = new String[LENGTHESCAPE];
	public static String[] strEscapeReplacement = new String[LENGTHESCAPE];		
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static String genXML(TaxInvoice taxinvoice , String type , String taxId) throws IOException{
		DataDocumentObj docObj = setData(taxinvoice ,type ,taxId);
		PrintWriter out = new PrintWriter(new FileWriter(docObj.getPath()+docObj.getFilename()+docObj.getXtension()));
		out.println("<?xml version=\"1.0\" encoding=\""+ECMConstant.ENCODING+"\"?>");
		out.println("<document-information>");
		out.println("	<document-type>"+docObj.getDocType()+"</document-type>");
		out.println("	<document-id>"+docObj.getDocID()+"</document-id>");
		out.println("	<document-date>"+docObj.getDocDate()+"</document-date>");
		out.println("	<document-tax-invoice>"+docObj.getDocTaxInvoice()+"</document-tax-invoice>");
		writeHeader(out,docObj);
		writeFooter(out, docObj);
		out.println("</document-information>");
		out.close();
		System.out.println("write xml file "+docObj.getFilename()+" complete");		
		return docObj.getFilename();
	}


	public static void writeHeader(PrintWriter out,DataDocumentObj docObj){
		out.println("	<header>");
		out.println("		<store>");
		out.println("			<store-info>");
		out.println("				<store-id>"+docObj.getStroeID()+"</store-id>");
		out.println("				<store-name>"+StringUtil.Unicode2ASCII(docObj.getStroeName())+"</store-name>");
		out.println("			</store-info>");
		out.println("		</store>");		
		
		out.println("		<customer>");
		out.println("			<customer-info>");
		out.println("				<id>"+docObj.getCustomerID()+"</id>");
		out.println("				<name>"+changeEscape(StringUtil.Unicode2ASCII(docObj.getCustomerName()))+"</name>");
		out.println("			</customer-info>");
		out.println("		</customer>");			


		out.println("		<operator>");
		out.println("			<id>"+docObj.getOperatorID()+"</id>");
		out.println("		</operator>");	
		
		out.println("	</header>");
	}
	public static void writeFooter(PrintWriter out,DataDocumentObj docObj){
		out.println("	<footer>");
		out.println("		<tax-invoice>");
		out.println("			<value-noadded-tax></value-noadded-tax>");
		out.println("			<value-added-tax></value-added-tax>");
		out.println("			<value-novat-tax></value-novat-tax>");
		String valueTot = (docObj.getType().equals(ECMConstant.FULLTAX)) ? docObj.getValueTot() : "";
		String totalDisft = (docObj.getType().equals(ECMConstant.FULLTAX)) ? docObj.getTotalDis() : "";
		out.println("			<value-total>"+valueTot+"</value-total>");
		out.println("			<value-total-discount>"+totalDisft+"</value-total-discount>");		
		out.println("		</tax-invoice>");

		out.println("		<credit-note>");
		out.println("			<value-tax-invoice></value-tax-invoice>");
		out.println("			<value-verify></value-verify>");
		String valueDif = (docObj.getType().equals(ECMConstant.CN))||(docObj.getType().equals(ECMConstant.REFUND)) ? docObj.getValueDif() : "";
		String totalDiscn = (docObj.getType().equals(ECMConstant.CN))||(docObj.getType().equals(ECMConstant.REFUND)) ? docObj.getTotalDis() : "";
		out.println("			<value-diff>"+valueDif+"</value-diff>");
		out.println("			<value-added-tax></value-added-tax>");
		out.println("			<value-noadded-tax></value-noadded-tax>");
		out.println("			<value-total-discount>"+totalDiscn+"</value-total-discount>");		
		out.println("		</credit-note>");		
		out.println("	</footer>");
	}	

	public static DataDocumentObj setData(TaxInvoice taxinvoice , String type , String taxId){
		DataDocumentObj docObj =null;
		if (taxinvoice!=null) {
			docObj  = new DataDocumentObj();
			docObj.setStroeID(taxinvoice.getStore().getStoreId());
			docObj.setStroeName(taxinvoice.getStore().getName());
			docObj.setOperatorID(taxinvoice.getCreateUserId()); //oper id
			docObj.setTotalDis(AmountUtil.displayAmount(taxinvoice.getTotalDiscountAmount()));
			docObj.setType(type);
			docObj.setDocTaxInvoice(taxId); 
			docObj.setDocID(taxinvoice.getTaxInvoiceId());
			docObj.setDocDate(DateTimeUtil.getDate("yyyyMMdd_HHmmss", taxinvoice.getIssueDate()));
			docObj.setCustomerID(taxinvoice.getCustomer().getSapId());
			docObj.setCustomerName(taxinvoice.getCustomerName());
			docObj.setEncoding(ECMConstant.ENCODING);
			docObj.setXtension(ECMConstant.XML_XTENSION);

			if (type.equals(ECMConstant.FULLTAX)) {
				//FULLTAX	
				docObj.setDocType(ECMConstant.FULLTAX_TYPE);
//				docObj.setValueTot(AmountUtil.displayAmount(taxinvoice.getTotalTransactionAmount()));
				docObj.setValueTot(AmountUtil.displayAmount(taxinvoice.getNetTransactionAmount()));
			} else {
				//CN	
				docObj.setDocType(ECMConstant.CN_TYPE);
//				docObj.setValueDif(AmountUtil.displayAmount(taxinvoice.getTotalTransactionAmount()));
				docObj.setValueDif(AmountUtil.displayAmount(taxinvoice.getNetTransactionAmount()));
			}
			String path = PropertyUtil.getProperty( ECMConstant.PROPERTY_FILE, ECMConstant.PATH);
			String filename =  DateTimeUtil.getDate("yyyyMMdd_HHmmss",DateTimeUtil.getCurrentDateTime())+docObj.getStroeID()+"_"+docObj.getDocType()+"_"+docObj.getDocID();
			docObj.setFilename(filename);
			docObj.setPath(path);			
		}
		return docObj;
	}
	public static String getPath(){		
		return PropertyUtil.getProperty( ECMConstant.PROPERTY_FILE, ECMConstant.PATH);	
	}
	
	// TOEY BEGIN ADD
	public static String getAbbTaxPath(){		
		return PropertyUtil.getProperty( ECMConstant.PROPERTY_FILE, ECMConstant.ABBTAX_FILE_PATH);	
	}
	public static String getFullTaxPath(){		
		return PropertyUtil.getProperty( ECMConstant.PROPERTY_FILE, ECMConstant.FULLTAX_FILE_PATH);	
	}
	// TOEY END ADD
	
	public static String getRefundPath(){		
		return PropertyUtil.getProperty( ECMConstant.PROPERTY_FILE, ECMConstant.REFUND_FILE_PATH);	
	}
	  private static String encode( String input ) {
	    try {
	    	return (input!=null) ? new String( input.getBytes( ENCODE_DEFAULT ), ENCODE_TYPE_TIS ) : ""  ;
	    }
	    catch ( UnsupportedEncodingException ex ) {
	      ex.printStackTrace();
	    }
	    return input;
	  }	 	
	public static String getProperty(String propFile, String name) {
		try {
			ResourceBundle rs = PropertyResourceBundle.getBundle(propFile);
			String ret = rs.getString(name);
			
			if ( ret != null )
				ret = encode(ret.trim());
			else
				ret = rs.getString("default");
			return ret;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void writeDocumentToFileSystem(ByteArrayInputStream byteArrayInputStream, String exportFile) throws Exception {
		//Use the Java I/O libraries to write the exported content to the file system.
		byte byteArray[] = new byte[byteArrayInputStream.available()];
		//Create a new file that will contain the exported result.
		File file = new File(exportFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArrayInputStream.available());
		int x = byteArrayInputStream.read(byteArray, 0, byteArrayInputStream.available());
		byteArrayOutputStream.write(byteArray, 0, x);
		byteArrayOutputStream.writeTo(fileOutputStream);
		//Close streams.
		byteArrayInputStream.close();
		byteArrayOutputStream.close();
		fileOutputStream.close();
		
	}  	
	public static String changeEscape(String str) {
		initialArr();
		if (str!=null && !str.equals("")) {
			for (int i = 0; i < strEscape.length; i++) {
				str=str.replaceAll(strEscape[i], strEscapeReplacement[i]);
			}
		}
		return str;
	}
	public static void initialArr () {
		strEscape[0] = AMPERSAND;
		strEscape[1] = LESSTHAN;
		strEscape[2] = GREATERTHAN;
		strEscape[3] = APOSTROPHE;
		strEscape[4] = QUOTATIONMARK;
		strEscapeReplacement[0] = AMPERSANDREPLACEMENT;
		strEscapeReplacement[1] = LESSTHANREPLACEMENT;
		strEscapeReplacement[2] = GREATERTHANREPLACEMENT;
		strEscapeReplacement[3] = APOSTROPHEREPLACEMENT;
		strEscapeReplacement[4] = QUOTATIONMARKREPLACEMENT;		
	}
	
	public static String  genRefundXML(Refund taxinvoice , String type , String taxId) throws IOException{
		DataDocumentObj docObj = setRefundData(taxinvoice ,type ,taxId);
		PrintWriter out = new PrintWriter(new FileWriter(docObj.getPath()+docObj.getFilename()+docObj.getXtension()));
		out.println("<?xml version=\"1.0\" encoding=\""+ECMConstant.ENCODING+"\"?>");
		out.println("<document-information>");
		out.println("	<document-type>"+docObj.getDocType()+"</document-type>");
		out.println("	<document-id>"+docObj.getDocID()+"</document-id>");
		out.println("	<document-date>"+docObj.getDocDate()+"</document-date>");
		out.println("	<document-tax-invoice>"+docObj.getDocTaxInvoice()+"</document-tax-invoice>");
		writeHeader(out,docObj);
		writeFooter(out, docObj);
		out.println("</document-information>");
		out.close();
		System.out.println("write xml file "+docObj.getFilename()+" complete");		
		return docObj.getFilename();
	}


	private static DataDocumentObj setRefundData(Refund taxinvoice, String type, String taxId) {
		DataDocumentObj docObj =null;
		if (taxinvoice!=null) {
			docObj  = new DataDocumentObj();
			docObj.setStroeID(taxinvoice.getStore().getStoreId());
			docObj.setStroeName(taxinvoice.getStore().getName());
			docObj.setOperatorID(taxinvoice.getCreateUserId()); //oper id
			docObj.setTotalDis(AmountUtil.displayAmount(taxinvoice.getTotalDiscountAmount()));
			docObj.setType(type);
			docObj.setDocTaxInvoice(taxId); 
			docObj.setDocID(taxinvoice.getRefundId());
			docObj.setDocDate(DateTimeUtil.getDate("yyyyMMdd_HHmmss", taxinvoice.getIssueDate()));
			docObj.setCustomerID(taxinvoice.getCustomer().getSapId());
			docObj.setCustomerName(taxinvoice.getCustomerName());
			docObj.setEncoding(ECMConstant.ENCODING);
			docObj.setXtension(ECMConstant.XML_XTENSION);
			//
			docObj.setValueDif(AmountUtil.displayAmount(taxinvoice.getNetTransactionAmount()));

				
				docObj.setDocType(ECMConstant.REFUND_TYPE);
//				docObj.setValueTot(AmountUtil.displayAmount(taxinvoice.getTotalTransactionAmount()));
				docObj.setValueTot(AmountUtil.displayAmount(taxinvoice.getNetTransactionAmount()));
			
			String path = PropertyUtil.getProperty( ECMConstant.PROPERTY_FILE, ECMConstant.PATH);
			String filename =  DateTimeUtil.getDate("yyyyMMdd_HHmmss",DateTimeUtil.getCurrentDateTime())+docObj.getStroeID()+"_"+docObj.getDocType()+"_"+docObj.getDocID();
			docObj.setFilename(filename);
			docObj.setPath(path);			
		}
		return docObj;
	}
	
	public static String getPropertyForCN(String propFile, String name) {
		try {
			ResourceBundle rs = PropertyResourceBundle.getBundle(propFile);
			String ret = rs.getString(name);
			if(ret!=null) {
				return (ret!=null) ? new String( ret.getBytes( ENCODE_DEFAULT ), ENCODE_TYPE ) : ""  ;
			} else {
				ret = rs.getString("default");
				return ret;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFileName(TaxInvoice taxinvoice , String type) throws IOException{
		String filename="";
		if (type.equals(ECMConstant.FULLTAX)) {
			type = ECMConstant.FULLTAX_TYPE;
		} else {
			type=ECMConstant.CN_TYPE;
		}
		filename =  DateTimeUtil.getDate("yyyyMMdd_HHmmss",DateTimeUtil.getCurrentDateTime())+taxinvoice.getStore().getStoreId()+"_"+type+"_"+taxinvoice.getTaxInvoiceId();
		
		return filename;
	}
	// pook add method 25/02/2014
	public static String genXML(TaxInvoice taxinvoice , String type , String taxId,String fileName) throws IOException{
		DataDocumentObj docObj = setData(taxinvoice ,type ,taxId);
		//PrintWriter out = new PrintWriter(new FileWriter(docObj.getPath()+docObj.getFilename()+docObj.getXtension()));
		PrintWriter out = new PrintWriter(new FileWriter(docObj.getPath()+fileName+docObj.getXtension()));
		out.println("<?xml version=\"1.0\" encoding=\""+ECMConstant.ENCODING+"\"?>");
		out.println("<document-information>");
		out.println("	<document-type>"+docObj.getDocType()+"</document-type>");
		out.println("	<document-id>"+docObj.getDocID()+"</document-id>");
		out.println("	<document-date>"+docObj.getDocDate()+"</document-date>");
		out.println("	<document-tax-invoice>"+docObj.getDocTaxInvoice()+"</document-tax-invoice>");
		writeHeader(out,docObj);
		writeFooter(out, docObj);
		out.println("</document-information>");
		out.close();
		System.out.println("write xml file "+docObj.getFilename()+" complete");		
		return docObj.getFilename();
	}
	// pook add method 25/02/2014
	public static String  genRefundXML(Refund taxinvoice , String type , String taxId,String filename) throws IOException{
		DataDocumentObj docObj = setRefundData(taxinvoice ,type ,taxId);
		PrintWriter out = new PrintWriter(new FileWriter(docObj.getPath()+filename+docObj.getXtension()));
		out.println("<?xml version=\"1.0\" encoding=\""+ECMConstant.ENCODING+"\"?>");
		out.println("<document-information>");
		out.println("	<document-type>"+docObj.getDocType()+"</document-type>");
		out.println("	<document-id>"+docObj.getDocID()+"</document-id>");
		out.println("	<document-date>"+docObj.getDocDate()+"</document-date>");
		out.println("	<document-tax-invoice>"+docObj.getDocTaxInvoice()+"</document-tax-invoice>");
		writeHeader(out,docObj);
		writeFooter(out, docObj);
		out.println("</document-information>");
		out.close();
		System.out.println("write xml file "+docObj.getFilename()+" complete");		
		return docObj.getFilename();
	}
	
}
