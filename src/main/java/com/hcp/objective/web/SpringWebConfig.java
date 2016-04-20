package com.hcp.objective.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.hcp.objective.SpringConfig;

@Configuration
@EnableWebMvc
public class SpringWebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static final String SERVLET_MAPPING_PATTERN = "/hcp/*";
	private static final String SERVLET_MAPPING_PATTERN_LOGIN = "/login/*";

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addListener(RequestContextListener.class);
		super.onStartup(servletContext);
	}

	/**
	 * In a spring 3.2 / servlet 3 environment, you will have some variety of
	 * DispatcherServlet initializer class which is the java equivalent of
	 * web.xml; in my case it's the
	 * AbstractAnnotationConfigDispatcherServletInitializer. Adding the
	 * following code will enable dispatchOptionsRequest:
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setInitParameter("dispatchOptionsRequest", "true");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { SERVLET_MAPPING_PATTERN,SERVLET_MAPPING_PATTERN_LOGIN };
	}
}
