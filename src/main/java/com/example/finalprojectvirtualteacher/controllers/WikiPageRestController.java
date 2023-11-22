package com.example.finalprojectvirtualteacher.controllers;

import com.example.finalprojectvirtualteacher.models.WikiPage;
import com.example.finalprojectvirtualteacher.services.WikiPageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WikiPageRestController {

    private final WikiPageServiceImpl wikiPageService;

    @Autowired
    public WikiPageRestController(WikiPageServiceImpl wikiPageService) {
        this.wikiPageService = wikiPageService;
    }

    @GetMapping("/search")
    public List<WikiPage> searchWikiPages(@RequestParam String searchValue) {
        return wikiPageService.searchWikiPages(searchValue);
    }
}