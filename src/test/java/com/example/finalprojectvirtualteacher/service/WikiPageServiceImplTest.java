package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.models.WikiPage;
import com.example.finalprojectvirtualteacher.services.WikiPageServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class WikiPageServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WikiPageServiceImpl wikiPageService;


    @Test
    public void testSearchWikiPages() throws IOException, InterruptedException {

        // Mocked response data
        String mockSearchResponse = "{ \"query\": { \"search\": [ { \"title\": \"MockTitle\", \"pageid\": 123 } ] } }";
        String mockSnippetResponse = "{ \"query\": { \"pages\": [ { \"revisions\": [ { \"*\": \"MockContent\" } ] } ] } }";
        String mockUrlResponse = "{ \"query\": { \"pages\": [ { \"fullurl\": \"https://mockurl.com\" } ] } }";



//       Mockito.when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
//                .thenReturn(HttpResponse.BodyHandlers.ofString().createResponse(mockSearchResponse))
//                .thenReturn(HttpResponse.BodyHandlers.ofString().createResponse(mockSnippetResponse))
//                .thenReturn(HttpResponse.BodyHandlers.ofString().createResponse(mockUrlResponse));

        // Mocking ObjectMapper behavior
        Mockito.when(objectMapper.readTree(mockSearchResponse)).thenReturn(Mockito.mock(JsonNode.class));
        Mockito.when(objectMapper.readTree(mockSnippetResponse)).thenReturn(Mockito.mock(JsonNode.class));
        Mockito.when(objectMapper.readTree(mockUrlResponse)).thenReturn(Mockito.mock(JsonNode.class));

        // Testing the searchWikiPages method
        List<WikiPage> wikiPages = wikiPageService.searchWikiPages("MockValue");

        // Assertions or verifications based on expected behavior
        assertEquals(1, wikiPages.size());
        assertEquals("MockTitle", wikiPages.get(0).getTitle());
        assertEquals("MockContent", wikiPages.get(0).getContentSnippet());
        assertEquals("https://mockurl.com", wikiPages.get(0).getFullUrl());
    }
}

