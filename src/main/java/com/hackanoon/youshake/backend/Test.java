package com.hackanoon.youshake.backend;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

/**
 * Jersey Demo
 */
@Path("/hello")
public class Test {

	private static final Logger LOGGER = Logger.getLogger(Test.class);

	/**
	 * @return
	 */
	@GET
	@Produces("text/plain")
	public String doGet() {
		return "hello";
	}

	/**
	 * @param xml
	 * @return
	 */
	@POST
	@Produces("application/xml")
	@Consumes({ "application/x-www-form-urlencoded", "multipart/form-data" })
	public String doPost(@FormParam("xml") String xml) {
		if (xml == null) {
			LOGGER.error("Expected 'xml' parameter was not found in the POST");
			return null;
		}
		return xml;
	}
}
