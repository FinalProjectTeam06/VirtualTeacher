package com.example.finalprojectvirtualteacher.controllers;


import com.example.finalprojectvirtualteacher.models.Comment;
import com.example.finalprojectvirtualteacher.services.contacts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getAll(){
        return commentService.getAll();
    }





}
