package com.hcp.objective;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringAppConfig4Test.class)
public class BaseSpringTestCase {

	public static final Logger logger = LoggerFactory.getLogger(SpringAppConfig4Test.class);

	@Test
	public void savePersonTest() {
		assertEquals("Ram", "Ram");
	}
}
