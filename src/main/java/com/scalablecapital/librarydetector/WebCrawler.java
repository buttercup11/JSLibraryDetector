package com.scalablecapital.librarydetector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WebCrawler {
    private HashSet<String> visitedLinks;

    public WebCrawler() {
        visitedLinks = new HashSet<>();
    }

    private String constructRootURL(String url) {
        String[] urlParts = url.split("/");
        String rootURL = urlParts[0] + "//" + urlParts[2];
        return rootURL;
    }

    private String getLibraryNameFromLink(String url) {
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length - 1];
    }

    private List<String> extractLinksFromWebPage(String url) {
        List<String> links = new ArrayList<>();
        try {
            Document htmlDocument = Jsoup.connect(url).get();
            Elements pageLinks = htmlDocument.select("a[href]");
            for (Element pageLink : pageLinks) {
                String link = pageLink.attr("abs:href");
                String rootURL = constructRootURL(link);
                if (!visitedLinks.contains(rootURL) && link.contains("https") && !link.contains("google")) {
                    links.add(link);
                    visitedLinks.add(rootURL);
                }
            }
        } catch (IOException e) {
            System.out.println("Error parsing URL : " + url + " Error Message : " + e.getMessage());
        }
        return links;
    }

    public List<String> getSearchResultLinks(String searchTerm) {
        String url = "https://www.google.com/search?q=" + searchTerm;
        visitedLinks.add(constructRootURL(url));
        List<String> searchResults = extractLinksFromWebPage(url);
        return searchResults;
    }

    public Map<String, Integer> detectJSLibraries(List<String> urlList) {
        Map<String, Integer> topJSLibraryList = new HashMap<>();
        for (String url : urlList) {
            try {
                Document htmlDocument = Jsoup.connect(url).get();
                Elements scripts = htmlDocument.select("script[src*=.js]");
                for (Element script : scripts) {
                    String library = getLibraryNameFromLink(script.attr("abs:src"));
                    int count = topJSLibraryList.containsKey(library) ? topJSLibraryList.get(library) : 0;
                    topJSLibraryList.put(library, count + 1);
                }
            } catch (IOException e) {
                System.out.println("Error detecting JS libraries for URL : " + url + " Error Message : " + e.getMessage());
            }
        }

        return topJSLibraryList;
    }
}
