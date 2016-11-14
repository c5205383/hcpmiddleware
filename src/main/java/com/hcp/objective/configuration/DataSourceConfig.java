package com.hcp.objective.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfig {

	public static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	public static final String PERSISTENCE_SCHEMA_NAME = "MMDB";
	public static final String PERSISTENCE_UNIT_NAME = "HcpDefaultPersistenceUnit";

	protected EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private PlatformTransactionManager annotationDrivenTransactionManager;

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(true)
	public synchronized EntityManagerFactory entityManagerFactory() {
		if (this.entityManagerFactory == null) {
			try {
				this.entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return this.entityManagerFactory;

	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Primary
	@Lazy(false)
	public synchronized EntityManager entityManager() {
		if (this.entityManager == null && this.entityManagerFactory() != null) {
			this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(this.entityManagerFactory());
		}
		return this.entityManager;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(false)
	public synchronized PlatformTransactionManager annotationDrivenTransactionManager() {
		if (this.annotationDrivenTransactionManager == null && this.entityManagerFactory() != null) {
			final JpaTransactionManager jpaTxManager = new JpaTransactionManager(this.entityManagerFactory());
			jpaTxManager.setJpaDialect(new EclipseLinkJpaDialect());
			this.annotationDrivenTransactionManager = jpaTxManager;
		}
		return this.annotationDrivenTransactionManager;
	}
}
