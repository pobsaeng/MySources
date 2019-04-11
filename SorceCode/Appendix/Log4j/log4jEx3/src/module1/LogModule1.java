package module1;
import org.apache.log4j.xml.DOMConfigurator; 
import org.apache.log4j.Logger;
import module2.LogModule2;


public class LogModule1 {
	static final Logger logger = Logger.getLogger(LogModule1.class); 
	 
	public static void main(String[] args){
		 
		DOMConfigurator.configure("src/log4j.xml"); 
		LogModule1 module1 = new LogModule1();
		LogModule2 module2 = new LogModule2();
		module1.doModule1();
		module2.doModule2();		
	}
	
	public void doModule1(){
		logger.fatal("Fatal-Module1"); 
		logger.error("Error-Module1"); 
		logger.warn("Warn-Module1"); 
		logger.info("Info-Module1");
		logger.debug("Debug-Module1");
	}
}
