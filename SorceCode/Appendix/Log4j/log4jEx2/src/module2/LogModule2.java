package module2;
import org.apache.log4j.Logger;

public class LogModule2 {
		static final Logger logger = Logger.getLogger(LogModule2.class); 
		
		public void doModule2(){
			logger.fatal("Fatal-Module2"); 
			logger.error("Error-Module2"); 
			logger.warn("Warn-Module2"); 
			logger.info("Info-Module2");
			logger.debug("Debug-Module2");
		}
	}

