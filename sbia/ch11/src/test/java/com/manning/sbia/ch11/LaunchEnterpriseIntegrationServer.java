/**
 * 
 */
package com.manning.sbia.ch11;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * @author acogoluegnes
 */
public class LaunchEnterpriseIntegrationServer {
	
	public static final String PICKUP_DIR = System.getProperty("java.io.tmpdir")
		+File.separator+"sbia";
	
	public static void main(String[] args) throws Exception {
		File pickUpDir = new File(PICKUP_DIR);
		if(pickUpDir.exists()) {
			FileUtils.cleanDirectory(pickUpDir);
		}
		System.setProperty("product.import.pickup.dir", PICKUP_DIR);
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(8080);
		connector.setHost("127.0.0.1");
		server.addConnector(connector);

		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/enterpriseintegration");
		wac.setWar("./src/main/webapp");
		server.setHandler(wac);
		server.setStopAtShutdown(true);
		server.start();
		
		System.out.println("**** enterprise integration server launched!");
	}
	
}
