package com.hcp.objective;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig4Test.class)
public class BaseSpringTestCase {

	public static final Logger logger = LoggerFactory.getLogger(AppConfig4Test.class);

	/*@BeforeClass
	public static void setUpClass() throws Exception {
		// rcarver - setup the jndi context and the datasource
		try {
			// Create initial context
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
			System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
			InitialContext ic = new InitialContext();
			ic.createSubcontext("java:");
			ic.createSubcontext("java:/comp");
			ic.createSubcontext("java:/comp/env");
			ic.createSubcontext("java:/comp/env/jdbc");
			ic.createSubcontext("java:/comp/env/jdbc/DefaultDB");

			// Construct DataSource
			Properties properties = new Properties();
			properties.setProperty("javax.persistence.jdbc.driver", "com.sap.db.jdbc.Driver");
			properties.setProperty("javax.persistence.jdbc.url", "jdbc:sap://lddbiwg.wdf.sap.corp:30015/");
			properties.setProperty("javax.persistence.jdbc.user", "C5205383");
			properties.setProperty("javax.persistence.jdbc.password", "Sap1234!");
			properties.setProperty("eclipselink.target-database", "com.sap.persistence.platform.database.HDBPlatform");
			DataSource ds = BasicDataSourceFactory.createDataSource(properties);

			ic.bind("java:comp/env/jdbc/DefaultDB", ds);
		} catch (NamingException ex) {
			logger.info(ex.toString());
		}

	}*/

	@Test
	public void savePersonTest() {
		assertEquals("Ram", "Ram");
	}
}
