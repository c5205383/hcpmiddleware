package com.hcp.objective.persistence.context.impl;


import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.hcp.objective.persistence.context.IPersistenceContext;



@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Lazy(true)
public class HcpPersistenceContext implements IPersistenceContext {

	protected static DataSource hcpDataSource;

	protected static final String JNDI_ENV_DS = "java:comp/env/jdbc/DefaultDB";

	@Resource
	@Autowired
	private InitialContext initialContext;

	@Override
	public synchronized DataSource getDataSource() {
		if (hcpDataSource == null) {
			try {
				hcpDataSource = (DataSource) this.initialContext
						.lookup(JNDI_ENV_DS);
			} catch (NamingException ex) {
				return null;
			} catch (ClassCastException ex) {
				return null;
			}
		}
		return hcpDataSource;
	}

	@Override
	public String getDriverName() {
		throw new UnsupportedOperationException(
				"Can't get information from HCP embbed data source!");
	}

	public String getJndiDataResourceName() {
		return JNDI_ENV_DS;
    }

	@Override
	public String getUrl() {
		throw new UnsupportedOperationException(
				"Can't get information from HCP embbed data source!");
	}

	@Override
	public String getUsername() {
		throw new UnsupportedOperationException(
				"Can't get information from HCP embbed data source!");
	}

	@Override
	public String getPassword() {
		throw new UnsupportedOperationException(
				"Can't get information from HCP embbed data source!");
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
