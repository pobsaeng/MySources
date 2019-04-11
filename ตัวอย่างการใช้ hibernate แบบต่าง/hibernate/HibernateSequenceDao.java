package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import com.ie.icon.dao.SequenceDao;
import com.ie.icon.domain.CashAdjustSequence;
import com.ie.icon.domain.Sequence;

public class HibernateSequenceDao extends HibernateCommonDao implements SequenceDao {

	public void update(Sequence sequence) throws DataAccessException {
		getHibernateTemplate().update(sequence);
	}

	public Sequence getSequence(String sequenceName) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Sequence.class);
		criteria.add(Restrictions.eq("sequenceName", sequenceName));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 1 )
			return (Sequence)result.get(0);
		else
			return null;
	}
	
	public long getNextValue(String sequenceName) throws Exception {
		long seq = 0;
		//Wait Dear lock
		synchronized(HibernateSequenceDao.class){
			Sequence sequence = getSequence(sequenceName);
			if ( sequence == null ){
				throw new Exception("Can not find sequence.");
			}
			seq = sequence.getStart();
			int next = sequence.getStart() + sequence.getIncrement();
			if ( next > sequence.getMaxValue() ){
				sequence.setStart(sequence.getMinValue());
			}
			else{
				sequence.setStart(next);
			}	
			update(sequence);
		}
		return seq;
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.SequenceDao#getValue(java.lang.String)
	 */
	public long getValue(String sequenceName) throws Exception {
		long seq = 0;
		Sequence sequence = getSequence(sequenceName);
		
		if ( sequence == null )
			throw new Exception("Can not find sequence.");

		seq = sequence.getStart();
		
		return seq;
	}

	public List getCshAdjSeq(String sequenceName, String year, String month)throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(CashAdjustSequence.class);
		criteria.add(Restrictions.eq("seqTypId", sequenceName));
		criteria.add(Restrictions.eq("yearId", year));
		criteria.add(Restrictions.eq("monthId", month));	
		List aaa = getHibernateTemplate().findByCriteria(criteria);
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public void updateCshAdjSeq(CashAdjustSequence sequence)throws DataAccessException{		
		getHibernateTemplate().update(sequence);
	}
}
