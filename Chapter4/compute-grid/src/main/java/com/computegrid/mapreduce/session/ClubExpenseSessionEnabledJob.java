package com.computegrid.mapreduce.session;

import static com.computegrid.mapreduce.IgniteMapReduceTest.CLUB_JOB_CACHE;
import static com.computegrid.mapreduce.IgniteMapReduceTest.PLAYER_JOB_CACHE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobContext;
import org.apache.ignite.compute.ComputeJobSibling;
import org.apache.ignite.compute.ComputeTaskSession;
import org.apache.ignite.resources.JobContextResource;
import org.apache.ignite.resources.TaskSessionResource;

import com.computegrid.query.dto.SoccerPlayer;
import com.computegrid.query.dto.SoccerPlayerKey;

public class ClubExpenseSessionEnabledJob extends ComputeJobAdapter {

	private final String clubName;
	private final IgniteCache<SoccerPlayerKey, SoccerPlayer> playerCache;
	
	 @TaskSessionResource
     private ComputeTaskSession session;
     
     @JobContextResource
     private ComputeJobContext jobContext;

	public ClubExpenseSessionEnabledJob(String clubName) {
		this.clubName = clubName;
		playerCache = Ignition.ignite().getOrCreateCache(PLAYER_JOB_CACHE);
	}

	@Override
	public Object execute() throws IgniteException {
		Map<String, Object> resultMap = new HashMap<>();
		SqlFieldsQuery fieldQry = new SqlFieldsQuery("SELECT SUM(p.salary) from SoccerPlayer p, \"" + CLUB_JOB_CACHE
				+ "\".SoccerClub c where p.clubId = c.id AND c.name=?");

		FieldsQueryCursor<List<?>> result = playerCache.query(fieldQry.setArgs(clubName));
		double sum = 0.00D;
		Iterator<List<?>> iterator = result.iterator();
		if (iterator.hasNext()) {
			List<?> next = iterator.next();
			sum = next != null && !next.isEmpty() ? (double) next.get(0) : 0.0d;
			System.out.println(String.format("Sum of total salary for club %s is %s", clubName, sum));
		}
		
		 session.setAttribute(jobContext.getJobId(), "SUMMATION");
		 
		 for (ComputeJobSibling sibling : session.getJobSiblings()) {
			try {
				System.err.println(String.format("WAITING for %s to complete SUMMATION", sibling.getJobId()));
				session.waitForAttribute(sibling.getJobId(), "SUMMATION", 100);
				System.err.println(String.format("DONE for %s to complete SUMMATION", sibling.getJobId()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }

		 System.err.println(String.format("Going to compute max salary for job= %s", jobContext.getJobId()));
		 
		fieldQry = new SqlFieldsQuery("SELECT MAX(p.salary) from SoccerPlayer p, \"" + CLUB_JOB_CACHE
				+ "\".SoccerClub c where p.clubId = c.id AND c.name=? ");
		result = playerCache.query(fieldQry.setArgs(clubName));
		iterator = result.iterator();
		double higestSalary = 0.00;
		if (iterator.hasNext()) {
			List<?> next = iterator.next();
			higestSalary = next != null && !next.isEmpty() ? (double) next.get(0) : 0.00d;
			System.out.println(String.format("Richest player of club %s is %s", clubName, higestSalary));
		}
		
		resultMap.put("sum", sum);
		resultMap.put("top", higestSalary);
		resultMap.put("clubName", clubName);
		
		return resultMap;
	}

	private static final long serialVersionUID = 1L;

}
