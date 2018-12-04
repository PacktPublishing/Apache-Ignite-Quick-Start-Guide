package com.computegrid.mapreduce.taskadapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ignite.IgniteException;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;

import com.computegrid.mapreduce.ClubExpenseCalculatorJob;

public class ClubExpenseTaskAdapter extends ComputeTaskAdapter<String[], Double> {

	private static final long serialVersionUID = 1L;

	@Override
	public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid, String[] clubs)
			throws IgniteException {
		Map<ComputeJob, ClusterNode> map = new HashMap<>();

		Iterator<ClusterNode> it = subgrid.iterator();

		for (final String club : clubs) {
			// If we used all nodes, restart the iterator.
			if (!it.hasNext())
				it = subgrid.iterator();

			ClusterNode node = it.next();
			map.put(new ClubExpenseCalculatorJob(club), node);
		}

		return map;
	}

	@Override
	public Double reduce(List<ComputeJobResult> results) throws IgniteException {
		double sum = 0;

		for (ComputeJobResult res : results)
			sum += res.<Double>getData();

		return sum;
	}

}
