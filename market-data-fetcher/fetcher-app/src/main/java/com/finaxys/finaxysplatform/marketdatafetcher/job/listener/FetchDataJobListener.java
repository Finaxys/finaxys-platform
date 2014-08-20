/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.job.listener;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.EODJobManager;

// TODO: Auto-generated Javadoc

public class FetchDataJobListener implements JobListener {
	
	private static Logger logger = Logger.getLogger(FetchDataJobListener.class);
	
	public static final String LISTENER_NAME = "fetchDataJobListener";
 
	@Override
	public String getName() {
		return LISTENER_NAME; 
	}
 
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
 
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("jobToBeExecuted");
		System.out.println("Job : " + jobName + " is going to start...");
 
	}
 
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		System.out.println("jobExecutionVetoed");
	}
 
	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		System.out.println("jobWasExecuted");
 
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " is finished...");

		
 
	}
 
}