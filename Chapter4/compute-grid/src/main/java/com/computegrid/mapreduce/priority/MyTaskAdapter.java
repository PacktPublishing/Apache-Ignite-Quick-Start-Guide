package com.computegrid.mapreduce.priority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ignite.IgniteException;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.internal.GridTaskSessionImpl;
import org.apache.ignite.resources.TaskSessionResource;

@ComputeTaskSessionFullSupport
public class MyTaskAdapter extends ComputeTaskAdapter<List<String>, List<String>> {

	private static final long serialVersionUID = 1L;
	@TaskSessionResource
	private GridTaskSessionImpl taskSes = null;

	@Override
	public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid, List<String> jobNames)
			throws IgniteException {
		Map<ComputeJob, ClusterNode> map = new HashMap<>();

		Iterator<ClusterNode> it = subgrid.iterator();
		if (jobNames.contains("car1") || jobNames.contains("car2")) {
			taskSes.setAttribute("grid.task.priority", 10);
		} else {
			taskSes.setAttribute("grid.task.priority", 5);
		}

		for (final String jobName : jobNames) {
			// If we used all nodes, restart the iterator.
			if (!it.hasNext())
				it = subgrid.iterator();

			ClusterNode node = it.next();
			map.put(new MyJob(jobName), node);
		}

		return map;
	}

	@Override
	public List<String> reduce(List<ComputeJobResult> results) throws IgniteException {
		List<String> uuids = new ArrayList<>();

		for (ComputeJobResult res : results)
			uuids.add(res.<String>getData());

		return uuids;
	}

}
