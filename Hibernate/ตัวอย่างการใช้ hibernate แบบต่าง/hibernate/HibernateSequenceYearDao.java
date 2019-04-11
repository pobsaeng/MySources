package com.ie.icon.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.ie.icon.dao.SequenceYearDao;
import com.ie.icon.domain.SequenceYear;

public class HibernateSequenceYearDao extends HibernateCommonDao implements SequenceYearDao {

	public void create(SequenceYear sequenceYear) throws DataAccessException {
		getHibernateTemplate().save(sequenceYear);
	}

	public int getNextValue(String name, String year) throws DataAccessException {
		int seq = 0;
		SequenceYear sequenceYear = getSequenceYear(name, year);
		
		if ( sequenceYear == null ) {
			sequenceYear = new SequenceYear();
			sequenceYear.getSequenceYearId().setName(name);
			sequenceYear.getSequenceYearId().setYear(year);
			create(sequenceYear);
		}

		seq = sequenceYear.getStart();
		sequenceYear.setStart(sequenceYear.getStart() + 1);
		update(sequenceYear);
		
		return seq;
	}

	public SequenceYear getSequenceYear(String name, String year) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(SequenceYear.class);
		criteria.add(Restrictions.eq("id.name", name));
		criteria.add(Restrictions.eq("id.year", year));
		List result = getHibernateTemplate().findByCriteria(criteria);
		
		if ( result.size() == 1 )
			return (SequenceYear)result.get(0);
		else
			return null;
	}

	public void update(SequenceYear sequenceYear) throws DataAccessException {
		getHibernateTemplate().update(sequenceYear);
	}

}
