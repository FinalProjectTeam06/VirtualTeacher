package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.WikiPage;
import com.example.finalprojectvirtualteacher.services.contacts.WikiPageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/wiki")
public class WikiMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final WikiPageService wikiPageService;

    @Autowired
    public WikiMvcController(AuthenticationHelper authenticationHelper, WikiPageService wikiPageService) {
        this.authenticationHelper = authenticationHelper;
        this.wikiPageService = wikiPageService;
    }

@PostMapping("/search")
@ResponseBody
public List<WikiPage> showSearchPage(@RequestParam("searchValue") String searchValue, HttpSession httpSession, Model model) {
    try {
        User user = authenticationHelper.tryGetCurrentUser(httpSession);
        List<WikiPage> result = wikiPageService.searchWikiPages(searchValue);
        model.addAttribute("loggedIn", user);
        model.addAttribute("result", result);
        return result;
    } catch (AuthorizationException e) {
        // Handle exception
        return Collections.emptyList();
    }
}
}
