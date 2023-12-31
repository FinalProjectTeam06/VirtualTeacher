package com.example.finalprojectvirtualteacher.controllers;


import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.services.contacts.AssignmentService;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import com.example.finalprojectvirtualteacher.services.contacts.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {


    private final LectureService lectureService;
    private final AuthenticationHelper authenticationHelper;
    private final AssignmentService assignmentService;
    private final NoteService noteService;

    @Autowired
    public LectureRestController(LectureService lectureService, AuthenticationHelper authenticationHelper, AssignmentService assignmentService, NoteService noteService) {
        this.lectureService = lectureService;
        this.authenticationHelper = authenticationHelper;
        this.assignmentService = assignmentService;
        this.noteService = noteService;
    }


    @GetMapping()
    public List<Lecture> getAll() {
        return lectureService.getAll();
    }

    @GetMapping("/{id}")
    public Lecture getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return lectureService.getById(id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    public Lecture create(@RequestHeader HttpHeaders headers, @Valid @RequestBody LectureDto lectureDto) {
        try {
            User creator = authenticationHelper.tryGetUser(headers);
            return lectureService.create(lectureDto, creator);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
    @PutMapping("/{lectureId}")
    public Lecture update(@RequestHeader HttpHeaders headers,@PathVariable int lectureId,
                          @Valid @RequestBody LectureDto lectureDto) {
        try {
            User creator = authenticationHelper.tryGetUser(headers);
            return lectureService.update(lectureDto, creator, lectureId);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.delete(id, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{lectureId}/note")
    public Note createNote(@RequestHeader HttpHeaders httpHeaders, @RequestBody String text, @PathVariable int lectureId) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            return noteService.create(new Note(text));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{lectureId}/assignment")
    public Lecture submitAssignment(@RequestHeader HttpHeaders httpHeaders,
                                    @RequestBody MultipartFile file,
                                    @PathVariable int lectureId) {
        try {
            User user=authenticationHelper.tryGetUser(httpHeaders);
            lectureService.getById(lectureId);
            return assignmentService.submitAssignment(user, lectureId, file);
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
