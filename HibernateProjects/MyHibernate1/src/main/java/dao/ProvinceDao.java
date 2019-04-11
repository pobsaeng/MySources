package dao;

import entity.Province;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author totoland
 */
public class ProvinceDao implements Serializable{
    private static final long serialVersionUID = -2126495922133172462L;
    
    public List<Province>findAll(){
    
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        return session.createSQLQuery("SELECT * FROM province").addEntity(Province.class).list();
        
    }
    
}
