package com.hcp.objective.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.persistence.bean.FormFolder;
import com.hcp.objective.service.quartz.SingleQuartzManagerService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuartzTester extends BaseSpringTestCase {

	@Autowired
	SingleQuartzManagerService singleQuartzManager;

	@Autowired
	FormFolderService oService;

	BatchJob job1;
	BatchJob job2;

	@Before
	public void prepareMockData() {
		job1 = new BatchJob();
		job1.setId((long) 123);
		job1.setName("test");
		job1.setType("FormFolder");
		job1.setInterval(0.0);

		job2 = new BatchJob();
		job2.setId((long) 1234);
		job2.setName("test2");
		job2.setType("WorkFlow");
		job2.setInterval(-1.0);
	}

	//@Test
	public void test000() {
		List<FormFolder> results = oService.findAll();

		Assert.assertEquals(false, results.isEmpty());

		System.out.println(results.size());

		for (FormFolder folder : results) {
			System.out.println(folder.getFolderName());
		}

	}

	/**
	 * Create
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test001() throws InterruptedException {
		singleQuartzManager.create(job1);
		// singleQuartzManager.create(job2);
	}

	/**
	 * Resume
	 * 
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	// @Test
	public void test003() throws SchedulerException, InterruptedException {
		singleQuartzManager.resume(job1);
	}

	/**
	 * Pause
	 * 
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	// @Test
	public void test002() throws SchedulerException, InterruptedException {
		singleQuartzManager.pause(job1);
	}

	/**
	 * Delete
	 * 
	 * @throws InterruptedException
	 * @throws SchedulerException
	 */
	// @Test
	public void test004() throws InterruptedException, SchedulerException {
		singleQuartzManager.delete(job1);
	}

	@After
	public void sleep() throws InterruptedException {
		Thread.sleep(30000); // Sleep 30 Seconds (0.5*60*1.000 = 30.000)
	}
}
