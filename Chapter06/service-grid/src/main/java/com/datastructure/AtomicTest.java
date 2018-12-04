package com.datastructure;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteAtomicReference;
import org.apache.ignite.IgniteAtomicStamped;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class AtomicTest {

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(false);
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {

			IgniteAtomicLong atomicLong = ignite.atomicLong("myLong", // Atomic long name.
					0, // Initial value.
					true // Create if it does not exist.
			);

			//Add two
			System.out.println("The new value is - "+atomicLong.addAndGet(2));

			IgniteAtomicReference<String> ref = ignite.atomicReference("myRef", // Reference name.
					"myValue", // Initial value for atomic reference.
					true // Create if it does not exist.
			);

			System.out.println("did it update? " + ref.compareAndSet("notMyValue", "new Value")
					+ " , the ref value is -" + ref.get());
			System.out.println("did it update? " + ref.compareAndSet("myValue", "new Value") + " , the ref value is -"
					+ ref.get());
			
			IgniteAtomicStamped<String, Integer> stamp = ignite.atomicStamped("myStamp", "my init value", 0, true);
			System.out.println(stamp.get());
			stamp.set("new value", 1);
			System.out.println(stamp.get());
			System.out.println("did it update? " +stamp.compareAndSet("value", "2nd update", 1, 2)+" , value is ="+ stamp.get());
			System.out.println("did it update? " +stamp.compareAndSet("new value", "2nd update", 1, 2)+" , value is ="+ stamp.get());

		}

	}

}
