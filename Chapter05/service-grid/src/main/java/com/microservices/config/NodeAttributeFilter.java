package com.microservices.config;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;


public class NodeAttributeFilter implements IgnitePredicate<ClusterNode>{
	private static final long serialVersionUID = 1L;
	private final String attributeName;
   
    public NodeAttributeFilter(String attributeName) {
		super();
		this.attributeName = attributeName;
	}


	public boolean apply(ClusterNode node) {
        Boolean attributeValue = node.attribute(attributeName);

        return attributeValue != null && attributeValue;
    }
}
