package com.hcp.objective;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class MyTest {
	public static final String PERSISTENCE_UNIT_NAME = "HcpDefaultPersistenceUnit";

	@Test
	public void test() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.close();
	}

}
