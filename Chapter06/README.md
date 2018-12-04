Chapter 6
=========================
Now is the time for us to focus on the other important aspects of building a highly scalable and  performant system.  We know that traditional RDBMS are ACID compliant. However,  bottleneck for high scalability & performance, whereas NoSQL databases solve this scalability problem but they are not ACID compliant. This chapter explores the following topics to build an ACID compliant, high performance, scalable, and highly available system
## Projects
 * Writing data to a  persistent datastore 
     * Native
	 * RDBMS
	 * Cassandra
 * Ignite's ACID compliant transactional model
 * Ignite's distributed data structure 
    * Queue & Set
	* ID generator
	* Atomic Types
	* CountDown Latch
	* Semaphore