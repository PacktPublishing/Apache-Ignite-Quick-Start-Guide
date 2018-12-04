package com.transaction;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.transactions.Transaction;

public class TransactionHelloWorld {

	private static final String MY_ATOMIC_CACHE = "myAtomicCache";
	private static final String MY_TRANSACTIONAL_CACHE = "myTransactionalCache";

	public static void main(String[] args) {

		CacheConfiguration<Long, String> myTransactionalCacheConfig = new CacheConfiguration<Long, String>();
		myTransactionalCacheConfig.setName(MY_TRANSACTIONAL_CACHE);
		myTransactionalCacheConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

		CacheConfiguration<Long, String> myAtomicCacheConfig = new CacheConfiguration<Long, String>();
		myAtomicCacheConfig.setName(MY_ATOMIC_CACHE);
		myAtomicCacheConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setCacheConfiguration(myTransactionalCacheConfig, myAtomicCacheConfig);

		// Starting Ignite
		Ignite ignite = Ignition.start(cfg);
		IgniteCache<Long, String> txCache = ignite.getOrCreateCache(MY_TRANSACTIONAL_CACHE);
		IgniteCache<Long, String> atomicCache = ignite.getOrCreateCache(MY_ATOMIC_CACHE);
		IgniteTransactions transactions = ignite.transactions();
		Transaction tx = transactions.txStart();
		try {
			txCache.put(2l, "Value1");
			txCache.put(3l, "Value2");

			atomicCache.put(2l, "Value1");
			atomicCache.put(3l, "Value2");
			throw new RuntimeException("Failing the tx");
		} catch (Exception e) {
			System.out.println("rolling back transaction");
			tx.rollback();
		} finally {
			tx.close();
		}
		System.out.println("key 2 in Tx Cache = " + txCache.get(2l));
		System.out.println("key 3 in Tx Cache = " + txCache.get(3l));
		System.out.println("key 2 in Atomic Cache = " + atomicCache.get(2l));
		System.out.println("key 3 in Atomic Cache = " + atomicCache.get(3l));
	}

}
