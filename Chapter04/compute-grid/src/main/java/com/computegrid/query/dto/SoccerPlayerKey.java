package com.computegrid.query.dto;

import java.io.Serializable;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class SoccerPlayerKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long playerId;
	@AffinityKeyMapped
	private final long clubId;

	public SoccerPlayerKey(long playerId, long clubId) {
		super();
		this.playerId = playerId;
		this.clubId = clubId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public long getClubId() {
		return clubId;
	}

	@Override
	public String toString() {
		return "SoccerKey [playerId=" + playerId + ", clubId=" + clubId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		Long result = 1l;
		result = prime * result + clubId;
		result = prime * result + playerId;
		return result.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SoccerPlayerKey other = (SoccerPlayerKey) obj;
		if (clubId != other.clubId)
			return false;
		if (playerId != other.playerId)
			return false;
		return true;
	}
	
}
