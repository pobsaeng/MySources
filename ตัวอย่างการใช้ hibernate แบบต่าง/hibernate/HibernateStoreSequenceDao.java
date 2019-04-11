package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.StoreSequenceDao;
import com.ie.icon.domain.StoreSequence;

public class HibernateStoreSequenceDao extends HibernateCommonDao implements StoreSequenceDao {

	public void update(StoreSequence sequence) throws DataAccessException {
		getHibernateTemplate().update(sequence);
	}

	public StoreSequence getStoreSequence(String sequenceName) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoreSequence.class);
		criteria.add(Restrictions.eq("sequenceName", sequenceName));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 1 )
			return (StoreSequence)result.get(0);
		else
			return null;
	}
	
	public long getNextValue(String sequenceName) throws Exception {
		long seq = 0;
		StoreSequence sequence = getStoreSequence(sequenceName);
		
		if ( sequence == null )
			throw new Exception("Can not find sequence.");

		seq = sequence.getStart();
			
		int next = sequence.getStart() + sequence.getIncrement();
		if ( next > sequence.getMaxValue() )
			sequence.setStart(sequence.getMinValue());
		else
			sequence.setStart(next);
			
		update(sequence);
		
		return seq;
	}

	/* (non-Javadoc)
	 * @see com.ie.icon.dao.SequenceDao#getValue(java.lang.String)
	 */
	public long getValue(String sequenceName) throws Exception {
		long seq = 0;
		StoreSequence sequence = getStoreSequence(sequenceName);
		
		if ( sequence == null )
			throw new Exception("Can not find sequence.");

		seq = sequence.getStart();
		
		return seq;
	}
}
