package com.hcp.objective.service;

import org.junit.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.persistence.bean.BatchJob;

public class QuartzTester extends BaseSpringTestCase {

	@Autowired
	SingleQuartzManager singleQuartzManager;

	@Test
	public void test1() throws InterruptedException {
		BatchJob batchJob = new BatchJob();
		batchJob.setId((long) 123);
		batchJob.setName("test");
		batchJob.setType("workflow");
		batchJob.setInterval(0.5);
		try {
			singleQuartzManager.create(batchJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread.sleep(30000); // Sleep 30 Seconds (0.5*60*1.000 = 30.000)

	}

	@Test
	public void testadd() throws InterruptedException {
		// abstractQuartzManager.addJob("testjob1", "trigger1", TestJob.class, "0/3 * * * * ?");
		// Thread.sleep(240000); // Sleep 4 minutes (4*60*1.000 = 240.000)
	}
}
