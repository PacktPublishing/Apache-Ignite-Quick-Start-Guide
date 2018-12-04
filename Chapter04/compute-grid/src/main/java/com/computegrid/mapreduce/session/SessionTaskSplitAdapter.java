package com.computegrid.mapreduce.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;

@ComputeTaskSessionFullSupport
public class SessionTaskSplitAdapter extends ComputeTaskSplitAdapter<String[], Map<String, Object>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Map<String,Object> reduce(List<ComputeJobResult> results) throws IgniteException {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String,String> topSalaries = new HashMap<>();
		double sum = 0;
		for (ComputeJobResult res : results) {
			Map<String, Object> clubData = res.<Map<String, Object>>getData();
			
			topSalaries.put(""+clubData.get("clubName"), String.format("%.2f", (double)clubData.get("top")));
			sum += (double) clubData.get("sum");
		}

		resultMap.put("sum",String.format("%.2f",  sum));
		resultMap.put("top", topSalaries);
		return resultMap;
	}

	@Override
	protected Collection<? extends ComputeJob> split(int gridSize, String[] clubs) throws IgniteException {
		List<ComputeJob> jobs = new ArrayList<>();
		for (String club : clubs) {
			jobs.add(new ClubExpenseSessionEnabledJob(club));
		}
		return jobs;
	}

}
