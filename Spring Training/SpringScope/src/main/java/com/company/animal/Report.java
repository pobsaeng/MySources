/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.animal;

/**
 *
 * @author totoland
 */
public abstract class Report {
    
    public String getReportName(){
        return "Report001";
    }
    
    public String getReportCode(){
        return "Code001";
    }
    
    public abstract void createReport();
}
