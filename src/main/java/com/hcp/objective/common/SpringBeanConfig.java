package com.hcp.objective.common;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringBeanConfig {

	/*
	 * Fields
	 */

	/*
	 * Bean Factories
	 */

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public synchronized InitialContext initialContext() throws NamingException {
		return new InitialContext();
	}
}
