package com.hcp.objective.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

import com.hcp.objective.persistence.context.impl.HcpPersistenceContext;

@Configuration
public class DataSourceConfig {

	// The persistence schema name would determined by deployment environment
	// 1. Dev : [NO SCHEMA]
	// 2. HANA : MMDB
	public static final String PERSISTENCE_SCHEMA_NAME = "MMDB";

	public static final String PERSISTENCE_UNIT_NAME = "HcpDefaultPersistenceUnit";

	@Autowired
	protected HcpPersistenceContext dataSourceContext;

	protected DataSource dataSource;

	protected EntityManagerFactory entityManagerFactory;

	protected EntityManager entityManager;

	private PlatformTransactionManager annotationDrivenTransactionManager;

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(true)
	public synchronized DataSource preferentialDataSource() {
		if (this.dataSource != null) {
			return this.dataSource;
		}
		final DataSource dataSource = this.dataSourceContext.getDataSource();
		try (final Connection connection = dataSource.getConnection()) {
			return this.dataSource = dataSource;
		} catch (SQLException ex) {
		    ex.printStackTrace();
		    return null;
		}
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(true)
	public synchronized EntityManagerFactory entityManagerFactory(){
		if (this.entityManagerFactory == null) {
			try {
				HashMap<String, Object> properties = new HashMap<String, Object>();
				properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, this.preferentialDataSource());
				this.entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return this.entityManagerFactory;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(false)
	public synchronized PlatformTransactionManager annotationDrivenTransactionManager() {
		if (this.annotationDrivenTransactionManager == null) {
			final JpaTransactionManager jpaTxManager = new JpaTransactionManager(this.entityManagerFactory());
			jpaTxManager.setJpaDialect(new EclipseLinkJpaDialect());
			this.annotationDrivenTransactionManager = jpaTxManager;
		}
		return this.annotationDrivenTransactionManager;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Primary
	@Lazy(false)
	public synchronized EntityManager entityManager() {
		if (this.entityManager == null) {
			this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(this.entityManagerFactory());
		}
		return this.entityManager;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(true)
	public JdbcTemplate jdbcTemplate() throws SQLException {
		return new JdbcTemplate(this.preferentialDataSource());
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(true)
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
		return new NamedParameterJdbcTemplate(this.preferentialDataSource());
	}

}
