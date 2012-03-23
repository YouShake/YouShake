package com.hackanoon.youshake.backend;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONArray;

import com.hackanoon.youshake.youtube.Top;

@Path("/video")
public class YoutubeTest {

	@GET
	@Path("random/{count}")
	@Produces("application/json")
	public JSONArray mix(@PathParam("count") int count) {
		return new JSONArray(Arrays.asList(Top.mix(count)));
	}

}
