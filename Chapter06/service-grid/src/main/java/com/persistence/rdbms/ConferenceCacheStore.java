package com.persistence.rdbms;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.resources.SpringResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ConferenceCacheStore extends CacheStoreAdapter<Long, Conference> {
	@SpringResource(resourceName = "dataSource")
	private DataSource dataSource;

	@SpringResource(resourceName = "jdbcTemplate")
	private JdbcTemplate jdbc;

	@Override
	public Conference load(Long key) throws CacheLoaderException {
		System.out.println(jdbc);

		RowMapper<Conference> rowMapper = new BeanPropertyRowMapper<Conference>(Conference.class);
		return jdbc.queryForObject(String.format("SELECT * FROM CONFERENCE WHERE ID=%s", key), rowMapper);
	}

	@Override
	public void write(Entry<? extends Long, ? extends Conference> entry) throws CacheWriterException {
		Conference conf = entry.getValue();
		System.out.println(entry);

		jdbc.update("INSERT INTO CONFERENCE(id, name, startDateTime, endDateTime) VALUES(?, ?, ? ,?)", conf.getId(),
				conf.getName(), conf.getStartDateTime(), conf.getEndDateTime());
	}

	@Override
	public void delete(Object key) throws CacheWriterException {
		jdbc.update("DELETE FROM CONFERENCE WHERE ID =?", key);

	}

}
