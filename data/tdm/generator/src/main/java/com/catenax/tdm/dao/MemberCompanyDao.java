/*
 *
 */
package com.catenax.tdm.dao;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.catenax.tdm.model.v1.MemberCompany;

// TODO: Auto-generated Javadoc
/**
 * The Class MemberCompanyDao.
 */
@Repository
@Transactional
public class MemberCompanyDao extends AbstractJpaDao<MemberCompany> implements IGenericDao<MemberCompany> {

	/**
	 * Instantiates a new member company dao.
	 */
	public MemberCompanyDao() {
		super.setClazz(MemberCompany.class);
	}

}
