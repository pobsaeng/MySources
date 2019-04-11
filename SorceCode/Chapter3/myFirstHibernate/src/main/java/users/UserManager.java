package users;

import org.hibernate.Session;
import java.util.Iterator;
import util.HibernateUtil;

public class UserManager {

	private void insert(User user) {

		Session session;
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();

	}

	private void update(User user) {

		Session session;
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.update(user);
		session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();
	}

	private void query() {

		Session session;
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Iterator iter = session.createQuery("select id, firstname, surname, age  from User ").iterate();

		while (iter.hasNext()) {
			Object[] tuple = (Object[]) iter.next();
			System.out.println("Id: " + tuple[0]);
			System.out.println("FirstName: " + tuple[1]);
			System.out.println("SurName: " + tuple[2]);
			System.out.println("Age: " + tuple[3]);
		}
		session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();
	}

	private void delete(User user) {

		Session session;
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(user);
		session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();
	}

	public static void main(String[] args) {

		User user1 = new User();
		User user2 = new User();
		user1.setFirstname("FirstName1");
		user1.setSurname("SurName1");
		user1.setAge(10);
		user2.setFirstname("FirstName2");
		user2.setSurname("SurName2");
		user2.setAge(20);

		String strAction = "Insert Record";
		System.out.println("=============" + strAction + "==============");
		UserManager mgr = new UserManager();
		mgr.insert(user1);
		mgr.insert(user2);

		strAction = "Query Record";
		System.out.println("=============" + strAction + "==============");
		mgr.query();

		strAction = "update Record";
		System.out.println("=============" + strAction + "==============");
		user2.setFirstname("ChangedFirstName2");
		mgr.update(user2);

		strAction = "Delete Record";
		System.out.println("=============" + strAction + "==============");
		mgr.delete(user1);

		strAction = "Query Record";
		System.out.println("=============" + strAction + "==============");
		mgr.query();

	}
}
