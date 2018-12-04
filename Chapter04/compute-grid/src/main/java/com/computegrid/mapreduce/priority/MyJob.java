package com.computegrid.mapreduce.priority;

import java.util.UUID;

import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;

public class MyJob extends ComputeJobAdapter {
	private static final long serialVersionUID = 1L;
	private final String jobName;

	public MyJob(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public Object execute() throws IgniteException {
		System.out.println(String.format("executing job %s", jobName));
		try {
			if (jobName.contains("bar")) {
				Thread.sleep(100000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(String.format("Finished job %s", jobName));
		return UUID.randomUUID().toString();
	}

}
