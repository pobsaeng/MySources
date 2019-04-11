package dao;

import java.util.Arrays;
import java.util.List;
import model.Address;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

public class AddressDAO {

    public void insert(Address addr) throws Exception {
        try {
            Session session = HibernateUtil.getSession();
            session.save(addr);

        } catch (Exception ex) {
            throw ex;
        }
    }

    public void delete(Address addr) throws Exception {
        try {
            Session session = HibernateUtil.getSession();
            session.delete(addr);

        } catch (Exception ex) {
            throw ex;
        }
    }

    public void update(Address addr) throws Exception {
        try {
            Session session = HibernateUtil.getSession();
            session.update(addr);

        } catch (Exception ex) {
            throw ex;
        }
    }

    public Address findByPK(long addrId) throws Exception {

        Address address = null;
        try {
            Session session = HibernateUtil.getSession();
            address = (Address) session.get(Address.class, addrId);

        } catch (Exception ex) {
            throw ex;
        }
        return address;
    }

    public List<Address> findByCustNam(String name) throws Exception {

        List<Address> list = null;
        try {

            Session session = HibernateUtil.getSession();
            Criteria crit = session.createCriteria(Address.class);
            Criteria custCrit = crit.createCriteria("customer");
            custCrit.add(Restrictions.eq("firstname", name));
            if ((!crit.list().isEmpty())) {
                list = toList(crit.list());
            }
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public List<Address> findByCustId(long id) throws Exception {

        List<Address> list = null;
        try {

            Session session = HibernateUtil.getSession();
            Criteria crit = session.createCriteria(Address.class);
            Criteria usrCrit = crit.createCriteria("customer");
            usrCrit.add(Restrictions.eq("id", id));
            if ((!crit.list().isEmpty())) {
                list = toList(crit.list());
            }

        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public static List<Address> toList(final List<?> beans) {
        if (beans == null) {
            return null;
        }
        if (beans.isEmpty()) {
            return null;
        }
        int size = beans.size();
        Address[] list = new Address[size];
        list = (Address[]) beans.toArray(list);
        return Arrays.asList(list);
    }

    public List<Address> findAllAddress() throws Exception {
        Criteria crit = null;
        try {
            Session session = HibernateUtil.getSession();
            crit = session.createCriteria(Address.class);

        } catch (Exception ex) {
            throw ex;
        }
        return crit.list();
    }

    public List<Address> findAllNativeSQL() throws Exception {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();//เริ่มทำ Transaction
        List query = session.createSQLQuery("SELECT * FROM Address").addEntity(Address.class).list();
        return query;
    }
}
