package com.olaaref.mintshop.dao;

import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.PersistentLogins;

@Repository("persistentTokenRepository")
@Transactional
public class PersistentTokenDaoImpl implements PersistentTokenRepository {
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		PersistentLogins logins = new PersistentLogins();
		logins.setUsername(token.getUsername());
		logins.setSeries(token.getSeries());
		logins.setToken(token.getTokenValue());
		logins.setLastUsed(token.getDate());
		Session session = entityManager.unwrap(Session.class); 
		session.save(logins);
		//sessionFactory.getCurrentSession().save(logins);
		logger.info("PersistentTokenRepository ==> Save User : " + logins.getUsername());
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		
		//Session session = sessionFactory.getCurrentSession();
		Session session = entityManager.unwrap(Session.class);
		
		PersistentLogins logins = session.get(PersistentLogins.class, series);
		
		logins.setToken(tokenValue);
		logins.setLastUsed(lastUsed);
		
		session.update(logins);
		
		logger.info("PersistentTokenRepository ==> Update User : " + logins.getUsername());
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		Session session = entityManager.unwrap(Session.class);
		PersistentLogins logins = session.get(PersistentLogins.class, seriesId);
		//PersistentLogins logins = sessionFactory.getCurrentSession().get(PersistentLogins.class, seriesId);
		
		if(logins != null) {
			logger.info("PersistentTokenRepository ==> Get User : " + logins.getUsername());
			return new PersistentRememberMeToken(logins.getUsername(), logins.getSeries(), logins.getToken(), logins.getLastUsed());
		}
		
		logger.info("PersistentTokenRepository ==> No User Exist.");
		return null;
	}

	@Override
	@Transactional
	public void removeUserTokens(String username) {
		Session session = entityManager.unwrap(Session.class);
		logger.info("PersistentTokenRepository ==> Remove User : " + username);
		//sessionFactory.getCurrentSession()
		session
				.createQuery("delete from PersistentLogins where username=:userName")
				.setParameter("userName", username)
				.executeUpdate();
		

	}

}
