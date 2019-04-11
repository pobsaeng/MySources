package util;
	
	import org.hibernate.SessionFactory;
	import org.hibernate.Session;
	import org.hibernate.HibernateException;
	import org.hibernate.Transaction;
	import org.hibernate.cfg.AnnotationConfiguration;
	
	public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static final ThreadLocal<Session>threadSession = 
				new  ThreadLocal<Session>();
	private static final ThreadLocal<Transaction>threadTransaction =
	new ThreadLocal<Transaction>();

	static {
        try {

        	sessionFactory = 
        			new AnnotationConfiguration().configure().buildSessionFactory();

        	} catch (Throwable ex) {          

        		throw new ExceptionInInitializerError(ex);

        }
	}


	public static Session getSession() {

   			Session session = (Session) threadSession.get();
    		try {
    			if (session == null) {
    				session = sessionFactory.openSession();
    				threadSession.set(session);
    			}

    		} catch (HibernateException ex) {
    			throw ex;
    		}
    		return session;
    
  	}  

	public static void closeSession() {
			try {
					Session session = (Session) threadSession.get();
					threadSession.set(null);
					if (session != null && session.isOpen())
						session.close();
			   		} catch (HibernateException ex) {
			   			throw ex;
			   		}
			  }
		    
	public static void beginTransaction() {
			Transaction tx = (Transaction) threadTransaction.get();
			try {
		    		if (tx == null) {
		    			tx = getSession().beginTransaction();
		    			threadTransaction.set(tx);
		    		}
			} catch (HibernateException ex) {
		   			throw ex;
			}
	}

	public static void commitTransaction() {
    		Transaction tx = (Transaction) threadTransaction.get();
    		try {
	    		if ( tx != null && !tx.wasCommitted()&& !tx.wasRolledBack() ){
	    			tx.commit();
	    		}
	    		threadTransaction.set(null);
   		} catch (HibernateException ex) {
    			rollbackTransaction();
    			throw ex;
    		}
   }
   
    public static void rollbackTransaction() {
    		Transaction tx = (Transaction) threadTransaction.get();
    		try {
    			threadTransaction.set(null);
	    		if ( tx != null && !tx.wasCommitted() && !tx.wasRolledBack() ) {
	    			tx.rollback();
	    		}
    		} catch (HibernateException ex) {
    			throw ex;
    		} 
    }
    
  }
