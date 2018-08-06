package com.packt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListener;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicyFactory;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class DataGridTest {

	private static final String POJO_CACHE = "pojoCache";

	public static void main(String[] args) throws InterruptedException {
		
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		CacheConfiguration<Key, Pojo> pojoCacheConfig = new CacheConfiguration<>();
		pojoCacheConfig.setName(POJO_CACHE);
		pojoCacheConfig.setCacheMode(CacheMode.REPLICATED);
		pojoCacheConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);
		pojoCacheConfig.setOnheapCacheEnabled(true);
		pojoCacheConfig.setEvictionPolicyFactory(new LruEvictionPolicyFactory<Key, Pojo>(80));
		Factory<CacheEntryListener<Key, Pojo>> cacheEntryCreatedListenerFactory = new Factory<CacheEntryListener<Key,Pojo>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public CacheEntryListener<Key, Pojo> create() {
				CacheEntryCreatedListener<Key, Pojo> listener = new CacheEntryCreatedListener<Key, Pojo>() {

					@Override
					public void onCreated(Iterable<CacheEntryEvent<? extends Key, ? extends Pojo>> events)
							throws CacheEntryListenerException {
						System.out.println("In Created Events Listener");
						events.forEach(e -> { System.out.println("Created event is  = " +e.getValue());});
						
					}
				};
				return listener;
			}
		};
		Factory<CacheEntryEventFilter<Key, Pojo>> filterFactory = new Factory<CacheEntryEventFilter<Key,Pojo>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public CacheEntryEventFilter<Key, Pojo> create() {
				CacheEntryEventFilter<Key, Pojo> filter = new CacheEntryEventFilter<Key, Pojo>() {

					@Override
					public boolean evaluate(CacheEntryEvent<? extends Key, ? extends Pojo> event)
							throws CacheEntryListenerException {
						
						Key key = event.getKey();
						boolean filtertedTrue = key.getKey() %2 ==0;
						if(filtertedTrue) {
						   System.out.println("Filtered key=  "+key.getKey());
						}else {
							 System.out.println("Excluding key=  "+key.getKey()+" from Filter");
						}
						return filtertedTrue;
					}
				};
				return filter;
			}
		};
		pojoCacheConfig.addCacheEntryListenerConfiguration(new MutableCacheEntryListenerConfiguration<>(cacheEntryCreatedListenerFactory, filterFactory, true, true));
		
		cfg.setCacheConfiguration(pojoCacheConfig);
		
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<Key, Pojo> cache = ignite.getOrCreateCache(POJO_CACHE);
   
			Set<Key> keys = new HashSet<>();
			for (int i = 1; i <= 10; i++) {
				Key key = new Key(i);
				//cache.put(key, new Pojo(String.format("Value %s", i)));
				keys.add(key);
				
			}
			
			cache.invokeAll(keys, new CacheEntryProcessor<Key, Pojo, Object>() {

				private static final long serialVersionUID = 1L;

				@Override
				public Object process(MutableEntry<Key, Pojo> entry, Object... arguments)
						throws EntryProcessorException {
					Pojo value = new Pojo(entry.getKey().getKey()+" updated");
					entry.setValue(value);
					return value;
				}
			});
			

			for (int i = 1; i <= 10; i++)
				System.out.println("Fetched -> key=" + i + ", value = " + cache.get(new Key(i)));
			
			Thread.sleep(5000);
		}
	}

}
class Key implements Serializable{
	private Integer key;

	public Key(Integer key) {
		super();
		this.key = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public Integer getKey() {
		return key;
	}
	
}
class Pojo implements Serializable{
	
	private String value;

	public Pojo(String value) {
		super();
		this.value = value;
	}

	@Override
	public String toString() {
		return "Pojo [value=" + value + "]";
	}

	public String getValue() {
		return value;
	}
	
	
}
