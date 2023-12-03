package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.WikiPage;

import java.util.List;

public interface WikiPageService {

    List<WikiPage> searchWikiPages(String searchValue);
}
