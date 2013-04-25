package org.geworkbench.service.index.service;

public interface IndexService {

	/**
	 * Returns service url string associated with the query
	 * @param query
	 * @return service url
	 */
    String find(String query);
}
