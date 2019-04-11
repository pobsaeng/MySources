package mapping;

// Generated 4 ¸.¤. 2552, 15:49:53 by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Book.
 * @see mapping.Book
 * @author Hibernate Tools
 */
public class BookHome {

	private static final Log log = LogFactory.getLog(BookHome.class);

	private static final SessionFactory sessionFactory;
	
	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}catch (Throwable ex) {
        	
			ex.printStackTrace();
			System.err.println("Can not Initial SessionFactory!!!" + ex);
            throw new ExceptionInInitializerError(ex);
            
        }
	}


	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Book transientInstance) {
		log.debug("persisting Book instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Book instance) {
		log.debug("attaching dirty Book instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Book instance) {
		log.debug("attaching clean Book instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Book persistentInstance) {
		log.debug("deleting Book instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Book merge(Book detachedInstance) {
		log.debug("merging Book instance");
		try {
			Book result = (Book) sessionFactory.getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Book findById(java.lang.Long id) {
		log.debug("getting Book instance with id: " + id);
		try {
			Book instance = (Book) sessionFactory.getCurrentSession().get(
					"mapping.Book", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Book instance) {
		log.debug("finding Book instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"mapping.Book").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
public static void main(String[] args){
		
		BookHome servicehome = new BookHome();
		Book book = new Book("Title1","Description1");
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		servicehome.persist(book);
		session.getTransaction().commit();
		
	}

}
