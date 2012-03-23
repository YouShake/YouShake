package com.hackanoon.youshake.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Top {

	public final static String YOUTUBE_BASE_URL =
			"https://gdata.youtube.com/feeds/api/standardfeeds/";

	public static String[] getTopRated() {
		return getRSSUrls(YOUTUBE_BASE_URL + "top_rated");
	}

	public static String[] getTopFavorites() {
		return getRSSUrls(YOUTUBE_BASE_URL + "top_favorites");
	}

	public static String[] getMostViewed() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_viewed");
	}

	public static String[] getMostShared() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_shared");
	}

	public static String[] getMostPopular() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_popular");
	}

	public static String[] getMostRecent() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_recent");
	}

	public static String[] getMostDiscussed() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_discussed");
	}

	public static String[] getMostResponded() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_responded");
	}

	public static String[] getRecentlyFeatured() {
		return getRSSUrls(YOUTUBE_BASE_URL + "recently_featured");
	}

	public static String[] getOnTheWeb() {
		return getRSSUrls(YOUTUBE_BASE_URL + "on_the_web");
	}

	public static String[] mix(int count) {
		if (count > 100) {
			count = 100;
		}
		Random rnd = new Random();
		String[] result = new String[count];
		int i = 0;
		int round = 0;
		while (i < count && round < count * 3) {
			round++;
			double d = rnd.nextDouble();
			if (d < 0.1) {
				int c = (int)Math.min(d * 70, 6);
				String url = null;
				switch(c) {
				case 0: url = fromArray(getTopRated(), rnd, result); break;
				case 1: url = fromArray(getTopFavorites(), rnd, result); break;
				case 2: url = fromArray(getMostViewed(), rnd, result); break;
				case 3: url = fromArray(getMostShared(), rnd, result); break;
				case 4: url = fromArray(getMostPopular(), rnd, result); break;
				case 5: url = fromArray(getMostDiscussed(), rnd, result); break;
				case 6: url = fromArray(getMostResponded(), rnd, result); break;
				}
				if (url != null) {
					result[i++] = url;
				}
				continue;
			}
			if (d < 0.2) {
				String url = fromArray(getRecentlyFeatured(), rnd, result);
				if (url != null) {
					result[i++] = url;
				}
				continue;
			}
			String url = null;
			if (rnd.nextBoolean()) {
				url = fromArray(getMostRecent(), rnd, result);
			} else {
				url = fromArray(getOnTheWeb(), rnd, result);
			}
			if (url != null) {
				result[i++] = url;
			}
		}
		ArrayList<String> res = new ArrayList<String>();
		for (String s: result) {
			if (s != null) {
				res.add(s);
			}
		}
		return res.toArray(new String[res.size()]);
	}

	private static String fromArray(String src[], Random rnd, String[] blacklist) {
		if (src.length == 0) {
			return null;
		}
		String url = src[rnd.nextInt(src.length)];
		for (String entry: blacklist) {
			if (entry != null && entry.equals(url)) {
				return null;
			}
		}
		return url;
	}

	private static class TEntry {
		public String[] result;
		public long time = System.currentTimeMillis();
	}

	private static HashMap<String, TEntry> CALL_CACHE =
			new HashMap<String, Top.TEntry>();

	private final static String[] getRSSUrls(String url) {
		long time = System.currentTimeMillis();
		TEntry cached = CALL_CACHE.get(url);
		if (cached != null && time - cached.time < 30 * 60 * 1000) {
			return cached.result;
		}
		ArrayList<String> result = new ArrayList<String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(url);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//entry/link");
			NodeList links = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			int length = links.getLength();
			for (int i = 0; i < length; i++) {
				Node item = links.item(i);
				NamedNodeMap attributes = item.getAttributes();
				if (attributes.getNamedItem("rel") == null) {
					continue;
				}
				if (attributes.getNamedItem("href") == null) {
					continue;
				}
				if (!attributes.getNamedItem("rel").getTextContent().equals("alternate")) {
					continue;
				}
				result.add(attributes.getNamedItem("href").getTextContent());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		cached = new TEntry();
		cached.result = result.toArray(new String[result.size()]);
		CALL_CACHE.put(url, cached);
		return cached.result;
	}

}
