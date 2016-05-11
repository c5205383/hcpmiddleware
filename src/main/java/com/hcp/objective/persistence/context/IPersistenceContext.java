package com.hcp.objective.persistence.context;

import javax.sql.DataSource;

import org.springframework.core.Ordered;

public interface IPersistenceContext extends Ordered {

	DataSource getDataSource();

	String getDriverName();

	String getUrl();

	String getUsername();

	String getPassword();
}
