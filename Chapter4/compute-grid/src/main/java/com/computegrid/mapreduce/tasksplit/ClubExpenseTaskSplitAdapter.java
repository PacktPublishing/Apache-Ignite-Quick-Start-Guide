package com.computegrid.mapreduce.tasksplit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;

import com.computegrid.mapreduce.ClubExpenseCalculatorJob;

public class ClubExpenseTaskSplitAdapter extends ComputeTaskSplitAdapter<String[], Double> {

	private static final long serialVersionUID = 1L;

	@Override
	public Double reduce(List<ComputeJobResult> results) throws IgniteException {
		double sum = 0;

	    for (ComputeJobResult res : results)
	      sum += res.<Double>getData();

	    return sum;
	}

	@Override
	protected Collection<? extends ComputeJob> split(int gridSize, String[] clubs) throws IgniteException {
		List<ComputeJob> jobs = new ArrayList<>();
		for (String club : clubs) {
			jobs.add(new ClubExpenseCalculatorJob(club));
		}
		return jobs;
	}

}
