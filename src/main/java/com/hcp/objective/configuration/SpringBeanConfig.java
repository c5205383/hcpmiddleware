package com.hcp.objective.configuration;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import com.hcp.objective.bean.ApplicationPropertyBean;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringBeanConfig {
	public static final Logger logger = LoggerFactory.getLogger(SpringBeanConfig.class);
	
	@Autowired
	public Environment env;

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public synchronized InitialContext initialContext() throws NamingException {
		return new InitialContext();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ApplicationPropertyBean oAppBean() {
		ApplicationPropertyBean appBean = new ApplicationPropertyBean();
		String sfUserName = null;
		String sfPassword = null;

		sfUserName = env.getProperty("service.username.default");
		sfPassword = env.getProperty("service.password.default");
		appBean.setAuthorization(sfUserName + ":" + sfPassword);
		appBean.setAuthorizationType(env.getProperty("service.authorizationType"));
		appBean.setProxyName(env.getProperty("service.proxy.hostname"));
		appBean.setProxyPort(Integer.parseInt(env.getProperty("service.proxy.port")));
		appBean.setUrl(env.getProperty("service.url"));
		appBean.setQueryUser(sfUserName);
		appBean.setQueryPwd(sfPassword);
		appBean.setContentType(env.getProperty("service.contentType"));
		appBean.setProxy(Boolean.parseBoolean(env.getProperty("service.isProxy")));
		appBean.setCharset(env.getProperty("service.charset"));
		appBean.setCompany(env.getProperty("service.company"));
		// quartz
		appBean.setQuartzState(env.getProperty("quartz.scheduler.state"));
		return appBean;
	}

}
