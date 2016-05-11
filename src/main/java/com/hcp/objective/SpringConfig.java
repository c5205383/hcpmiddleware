package com.hcp.objective;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringAppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/hcp/*" };
	}

	@Configuration
	@ComponentScan(value = "com.hcp.objective")
	public static class SpringAppConfig {
	}

	@Configuration
	@ComponentScan(basePackages = "com.hcp.objective")
	@EnableWebMvc
	@EnableTransactionManagement
	@EnableJpaRepositories(transactionManagerRef = "annotationDrivenTransactionManager")
	public static class SpringWebConfig extends WebMvcConfigurerAdapter {

		private static final int MAX_UPLOAD_SIZE = 1000000;

		@Bean
		@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
		@Lazy(true)
		public CommonsMultipartResolver multipartResolver() {
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			commonsMultipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
			return commonsMultipartResolver;
		}
	}
}