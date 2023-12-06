package com.example.finalprojectvirtualteacher.controllers.mvc;


import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityDuplicateException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.helpers.mappers.NoteMapper;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.NoteDto;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import com.example.finalprojectvirtualteacher.services.contacts.NoteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class NoteMvcController {

    private final NoteService noteService;
    private final AuthenticationHelper authenticationHelper;
    private  final NoteMapper noteMapper;
    private final LectureService lectureService;

    @Autowired
    public NoteMvcController(NoteService noteService, AuthenticationHelper authenticationHelper,
                             NoteMapper noteMapper, LectureService lectureService) {
        this.noteService = noteService;
        this.authenticationHelper = authenticationHelper;
        this.noteMapper = noteMapper;
        this.lectureService = lectureService;
    }
    @ModelAttribute("isTeacher")
    public boolean populateIsTeacher(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            return (user.getRole().getId() == 2 || user.getRole().getId() == 3);
        } catch (AuthorizationException e) {
            return false;
        }
    }
    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try{
            User user = authenticationHelper.tryGetCurrentUser(session);
            return (user.getRole().getId()==3);
        } catch (AuthorizationException e) {
            return false;
        }
    }

    @PostMapping("/course/{courseId}/lecture/{lectureId}/notes/new")
    public String createNewNote(@PathVariable int courseId,
                                @PathVariable int lectureId,
                                @Valid @ModelAttribute("note")NoteDto noteDto,
                                BindingResult bindingResult,
                                Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return String.format("redirect:/courses/%d/lecture/%d", courseId,lectureId);
        }
        try {
           User authUser = authenticationHelper.tryGetCurrentUser(session);
            Lecture lecture = lectureService.getById(lectureId);
            Note newNote = noteMapper.dtoToObj(noteDto, lecture, authUser);
            noteService.create(newNote);
            return String.format("redirect:/courses/%d/lecture/%d", courseId, lectureId);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/courses/{courseId}/lectures/{lectureId}/notes/{noteId}/update")
    public String showEditNotePage(@PathVariable int courseId,
                                   @PathVariable int lectureId,
                                   @PathVariable int noteId,
                                   Model model,
                                   HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            Note note = noteService.getById(noteId);
            NoteDto noteDto = noteMapper.objToDto(note);
            model.addAttribute("note",noteDto);
            return "NoteUpdate";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{lectureId}/notes/{noteId}/update")
    public String updateNote (@PathVariable int courseId,
                              @PathVariable int lectureId,
                              @PathVariable int noteId,
                              @ModelAttribute("note") NoteDto noteDto,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession session) {

        try {
            User user =authenticationHelper.tryGetCurrentUser(session);
            Note originalNote = noteService.getById(noteId);
            Note note = noteMapper.dtoToObjForUpdate(originalNote,noteDto);
            noteService.update(note,user);
            return "redirect:/users/myNotes";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
// todo - da napravq stranica Not Found
    @GetMapping("/courses/{courseId}/lectures/{lectureId}/notes/{noteId}/delete")
    public String deleteNote(@PathVariable int courseId,
                             @PathVariable int lectureId,
                             @PathVariable int noteId,
                             Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            noteService.delete(user,noteId);
            return "redirect:/users/myNotes";
        } catch (EntityNotFoundException e ) {
            model.addAttribute("error", e.getMessage());
            return "NotFound";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

















    }


