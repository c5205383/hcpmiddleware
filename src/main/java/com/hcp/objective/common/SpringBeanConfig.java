package com.hcp.objective.common;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;


@Configuration
@PropertySource("classpath:config.properties")
public class SpringBeanConfig {
	public static final Logger logger = LoggerFactory.getLogger(SpringBeanConfig.class);
	/*
	 * Fields
	 */

	/*
	 * Bean Factories
	 */

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public synchronized InitialContext initialContext() throws NamingException {
		return new InitialContext();
	}
}
