package com.hcp.objective.service.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcp.objective.component.quartz.AbstractQuartzManager;


@Service
public class ClusterQuartzManagerService extends AbstractQuartzManager {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ClusterQuartzManagerService.class);
	public static final int STATE_RUN = 1; // ����״̬
	public static final int STATE_WAIT = 0; // �ȴ�״̬
	private String jobFactoryKey = null; // ÿ������ʵ��һ������KEY
	private int jobState = -1; // �÷���ʵ��״̬

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
