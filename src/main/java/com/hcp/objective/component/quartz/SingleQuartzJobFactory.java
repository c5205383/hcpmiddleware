package com.hcp.objective.component.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcp.objective.component.jobexecutor.IExecutor;
import com.hcp.objective.component.jobexecutor.SFFormExecutor;
import com.hcp.objective.component.jobexecutor.SFFormFolderExecutor;
import com.hcp.objective.component.jobexecutor.SFObjectiveExecutor;
import com.hcp.objective.component.jobexecutor.SFWorkFlowExecutor;
import com.hcp.objective.persistence.bean.BatchJob;

@DisallowConcurrentExecution
@Service
public class SingleQuartzJobFactory implements Job {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzJobFactory.class);

	public enum ExecutorContainer {
		SF_WORKFLOW("WorkFlow", SFWorkFlowExecutor.class), SF_FORM("Form", SFFormExecutor.class), SF_FORMFOLDER(
				"FormFolder", SFFormFolderExecutor.class), SF_OBJECTIVE("objective", SFObjectiveExecutor.class);

		private String name;
		private Class<?> clazz;

		private IExecutor executor;

		private ExecutorContainer(String name, Class<?> clazz) {
			this.setName(name);
			this.setClazz(clazz);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static IExecutor getExecutor(String type) {

			for (ExecutorContainer ec : values()) {
				if (ec.getName().equalsIgnoreCase(type)) {
					return ec.getExecutor();
				}
			}
			return null;
		}

		public IExecutor getExecutor() {
			try {
				executor = (IExecutor) clazz.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return executor;
		}

		public void setExecutor(IExecutor executor) {
			this.executor = executor;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}

	}

	private IExecutor executor;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);
			if (batchJob != null) {
				log.info("Job Id:{}, Job Name:{}, Job Type:{}", batchJob.getId(), batchJob.getName(),
						batchJob.getType());
				executor = ExecutorContainer.getExecutor(batchJob.getType());
				if (executor != null) {
					System.out.println("job excuted:" + batchJob.getId() + ", " + batchJob.getName());
					executor.execute();
				}

			}
		} catch (Exception ex) {
			log.error("====================Scheduler-error-begin====================");
			log.error(ex.toString());
			System.out.println(ex.toString());
			StackTraceElement[] element = ex.getStackTrace();
			for (int i = 0; i < element.length; i++) {
				log.error("" + element[i]);
			}
			log.error("====================Scheduler-error-end====================");
		}

	}

}
