package com.computegrid.mapreduce;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.compute.ComputeJobAdapter;

import com.computegrid.query.dto.SoccerPlayer;
import com.computegrid.query.dto.SoccerPlayerKey;
import static com.computegrid.mapreduce.IgniteMapReduceTest.*;

import java.util.Iterator;
import java.util.List;

public class ClubExpenseCalculatorJob extends ComputeJobAdapter {

	private final String clubName;
	private final IgniteCache<SoccerPlayerKey, SoccerPlayer> playerCache;

	public ClubExpenseCalculatorJob(String clubName) {
		this.clubName = clubName;
		playerCache = Ignition.ignite().getOrCreateCache(PLAYER_JOB_CACHE);
	}

	@Override
	public Object execute() throws IgniteException {
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

		return sum;
	}

	private static final long serialVersionUID = 1L;

}
