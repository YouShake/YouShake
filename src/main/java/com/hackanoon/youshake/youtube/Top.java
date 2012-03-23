package com.hackanoon.youshake.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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

	public static String[] getTopFavorite() {
		return getRSSUrls(YOUTUBE_BASE_URL + "top_favorite");
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

	public static String[] getMostFeatured() {
		return getRSSUrls(YOUTUBE_BASE_URL + "most_featured");
	}

	public static String[] getRecentlyFeatured() {
		return getRSSUrls(YOUTUBE_BASE_URL + "recently_featured");
	}

	public static String[] getOnTheWeb() {
		return getRSSUrls(YOUTUBE_BASE_URL + "on_the_web");
	}

	private final static String[] getRSSUrls(String url) {
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
		return result.toArray(new String[result.size()]);
	}

}
