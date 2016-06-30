package com.hcp.objective.service.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterQuartzManager extends AbstractQuartzManager {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ClusterQuartzManager.class);
	public static final int STATE_RUN = 1; // 运行状态
	public static final int STATE_WAIT = 0; // 等待状态
	public static final String SCHEDULER_CHECK_JOB = "SCHEDULER_CHECK_JOB"; // 定时检查任务Id
	private String jobFactoryKey = null; // 每个服务实例一个单独KEY
	private int jobState = -1; // 该服务实例状态

	public boolean isRun() {
		return jobState == STATE_RUN;
	}

	public String getJobFactoryKey() {
		return jobFactoryKey;
	}

	public void setJobFactoryKey(String jobFactoryKey) {
		this.jobFactoryKey = jobFactoryKey;
	}
}
