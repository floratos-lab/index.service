package org.geworkbench.service.index.service.impl;

import org.geworkbench.service.index.service.IndexService;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService {

	private static final String serverAddress = "http://afdev.c2b2.columbia.edu:9090";

	/**
	 * Returns service url string associated with the query
	 * @param query
	 * @return service url
	 */
	public String find(String query) {
		
    	if (query==null) return "";
    	
    	if (query.equalsIgnoreCase("demand"))		return serverAddress + "/demand-server/services";

    	else if (query.equalsIgnoreCase("viper"))	return serverAddress + "/viper-server/services";
    	
    	return "";
    }
}
