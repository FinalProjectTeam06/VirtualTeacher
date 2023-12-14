package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.models.WikiPage;
import com.example.finalprojectvirtualteacher.services.WikiPageServiceImpl;
import com.example.finalprojectvirtualteacher.services.contacts.WikiPageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class WikiPageServiceImplTest {

    private WikiPageService wikiPageService;

    @BeforeEach
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        wikiPageService = new WikiPageServiceImpl(objectMapper);
    }

    @Test
    public void testSearchWikiPages() {
        String searchValue = "Java";
        List<WikiPage> wikiPages = wikiPageService.searchWikiPages(searchValue);

        assertNotNull(wikiPages);
        assertFalse(wikiPages.isEmpty());

        for (WikiPage wikiPage : wikiPages) {
            assertNotNull(wikiPage.getTitle());
            assertNotNull(wikiPage.getContentSnippet());
            assertNotNull(wikiPage.getFullUrl());
        }
    }


}