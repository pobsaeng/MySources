import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

public class TestLog1 {
	static final Logger logger = Logger.getLogger(TestLog1.class); 
	 
	public static void main(String[] args){
		
		PropertyConfigurator.configure("src/log4j.properties");
		logger.fatal("Fatal"); 
		logger.error("Error"); 
		logger.warn("Warn"); 
		logger.info("Info");
		logger.debug("Debug");   		

	}
}
