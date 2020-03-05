package com.scalablecapital.librarydetector;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

public class JavaScriptLibraryDetector {
    private static final int OUTPUT_SIZE = 5;

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ENTER A SEARCH TERM");
        String searchTerm = scanner.nextLine();
        WebCrawler webCrawler = new WebCrawler();
        List<String> searchResults = webCrawler.getSearchResultLinks(searchTerm);
        Map<String, Integer> jsLibraries = webCrawler.detectJSLibraries(searchResults);
        List<Entry<String, Integer>> topJSLibraries = jsLibraries.entrySet().stream().sorted(comparing(Entry::getValue, reverseOrder())).limit(OUTPUT_SIZE).collect(toList());
        System.out.println("TOP JS LIBRARIES ARE : ");
        for (int i = 0; i < topJSLibraries.size(); i++) {
            System.out.println(topJSLibraries.get(i).getKey());
        }
    }
}
