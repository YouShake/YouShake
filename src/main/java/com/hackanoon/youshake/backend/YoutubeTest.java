package com.hackanoon.youshake.backend;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONArray;

import com.hackanoon.youshake.youtube.Top;

@Path("/youtube")
public class YoutubeTest {

	@GET
	@Path("top")
	@Produces("application/json")
	public JSONArray top() {
		return new JSONArray(Arrays.asList(Top.getTopRated()));
	}

}
