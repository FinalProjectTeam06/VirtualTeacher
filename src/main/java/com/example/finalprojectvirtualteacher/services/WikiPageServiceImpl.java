package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.WikiPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class WikiPageServiceImpl {

    private static final String API_BASE_URL = "https://www.wikipedia.org/w/api.php";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;


    public WikiPageServiceImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<WikiPage> searchWikiPages(String searchValue) {
        try {
            URI searchUri = UriComponentsBuilder.fromUriString(API_BASE_URL)
                    .path("/w/api.php")
                    .queryParam("action", "query")
                    .queryParam("list", "search")
                    .queryParam("srsearch", searchValue)
                    .queryParam("utf8", "")
                    .queryParam("format", "json")
                    .queryParam("srlimit", 3)
                    .build()
                    .toUri();

            String searchResponse = sendRequest(searchUri);

            List<String> titles = parseTitles(searchResponse);
            List<Integer> pageIds = parsePageIds(searchResponse);


            List<String> snippets = new ArrayList<>();
            for (String title : titles) {
                URI snippetUri = UriComponentsBuilder.fromUriString(API_BASE_URL)
                        .path("/w/api.php")
                        .queryParam("action", "query")
                        .queryParam("prop", "revisions")
                        .queryParam("rvprop", "content")
                        .queryParam("titles", title)
                        .queryParam("format", "json")
                        .build()
                        .toUri();

                String snippetResponse = sendRequest(snippetUri);
                snippets.add(parseSnippet(snippetResponse));
            }


            List<String> fullUrls = new ArrayList<>();
            for (Integer pageId : pageIds) {
                URI urlUri = UriComponentsBuilder.fromUriString(API_BASE_URL)
                        .path("/w/api.php")
                        .queryParam("action", "query")
                        .queryParam("pageids", pageId)
                        .queryParam("prop", "info")
                        .queryParam("inprop", "url")
                        .queryParam("format", "json")
                        .build()
                        .toUri();

                String urlResponse = sendRequest(urlUri);
                fullUrls.add(parseFullUrl(urlResponse));
            }
            List<WikiPage> wikiPages = new ArrayList<>();
            for (int i = 0; i < titles.size(); i++) {
                WikiPage wikiPage = new WikiPage();
                wikiPage.setTitle(titles.get(i));
                wikiPage.setContentSnippet(snippets.get(i));
                wikiPage.setFullUrl(fullUrls.get(i));
                wikiPages.add(wikiPage);
            }

            return wikiPages;

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String sendRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private List<String> parseTitles(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        List<String> titles = new ArrayList<>();

        JsonNode searchNode = jsonNode.path("query").path("search");
        for (JsonNode result : searchNode) {
            titles.add(result.path("title").asText());
        }

        return titles;
    }

    private List<Integer> parsePageIds(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        List<Integer> pageIds = new ArrayList<>();

        JsonNode searchNode = jsonNode.path("query").path("search");
        for (JsonNode result : searchNode) {
            pageIds.add(result.path("pageid").asInt());
        }

        return pageIds;
    }

    private String parseSnippet(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode pagesNode = jsonNode.path("query").path("pages");

        JsonNode pageNode = pagesNode.elements().next();

        JsonNode revisionsNode = pageNode.path("revisions");
        JsonNode revisionNode = revisionsNode.elements().next();


        Iterator<String> fieldNames = revisionNode.fieldNames();
        String firstField = fieldNames.next();

        return revisionNode.path(firstField).asText();
    }

    private String parseFullUrl(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode pagesNode = jsonNode.path("query").path("pages");

        JsonNode pageNode = pagesNode.elements().next();

        return pageNode.path("fullurl").asText();
    }
}