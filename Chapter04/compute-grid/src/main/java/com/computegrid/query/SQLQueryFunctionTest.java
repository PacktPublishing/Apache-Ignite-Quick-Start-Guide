package com.computegrid.query;

import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QuerySqlFunction;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.computegrid.query.dto.SoccerClub;
import com.computegrid.query.dto.SoccerPlayer;

public class SQLQueryFunctionTest {

	private static final String COUNTRY_CACHE = "my_country";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		CacheConfiguration<Long, Country> funcCacheConf = new CacheConfiguration<>();
		funcCacheConf.setName(COUNTRY_CACHE);
		funcCacheConf.setIndexedTypes(Long.class, Country.class);
		funcCacheConf.setCacheMode(CacheMode.REPLICATED);
		funcCacheConf.setSqlFunctionClasses(SQLQueryFunctionTest.class);

		cfg.setCacheConfiguration(funcCacheConf);

		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<Long, Country> funcCache = Ignition.ignite().getOrCreateCache(COUNTRY_CACHE);

			long id = 1;
			funcCache.put(id, new Country(id++, "USA", 123456));
			funcCache.put(id, new Country(id++, "India", 23489900));
			funcCache.put(id, new Country(id++, "France", 897633));
			funcCache.put(id, new Country(id++, "England", 666666));

			SqlFieldsQuery fieldQry = new SqlFieldsQuery(
					"select id, name from \"my_country\".Country where myUpperCase(name) = upper(?) ");

			System.out.println("Find 'iNdIa' - in cache the name is 'India', so need to make it upper before comp");
			FieldsQueryCursor<List<?>> result = funcCache.query(fieldQry.setArgs("iNdIa"));

			result.forEach(r -> {
				System.out.println("id=" + r.get(0) + " country=" + r.get(1));
			});

		}
	}

	@QuerySqlFunction
	public static String myUpperCase(String name) {
		String upperCase = name.toUpperCase();
		System.out.println(String.format("Called the myUpperCase with %s, returning %s", name, upperCase));
		return upperCase;
	}
}

class Country {
	@QuerySqlField(index = true)
	private long id;
	@QuerySqlField(index = true)
	private String name;
	private long population;

	public Country(long id, String name, long population) {
		super();
		this.id = id;
		this.name = name;
		this.population = population;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", population=" + population + "]";
	}

}
