package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private NotesService notesService;
    private UserService userService;

    public NoteController(NotesService notesService, UserService userService) {
        this.notesService = notesService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateNote(Authentication authentication, NoteForm noteForm, RedirectAttributes redirectAttributes) {
        User authUser = this.userService.getUserByUsername(authentication.getName());
        noteForm.setUserId(authUser.getUserId());

        try {
            if (noteForm.getNoteId() == null) {
                this.notesService.createNote(noteForm);
                redirectAttributes.addFlashAttribute("message", String.format("Note \"%s\" successfully saved!", noteForm.getNoteTitle()));
            } else {
                this.notesService.updateNote(noteForm);
                redirectAttributes.addFlashAttribute("message", String.format("Note \"%s\" successfully updated!", noteForm.getNoteTitle()));
            }
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        return "redirect:/home#nav-notes";
    }

    @GetMapping("delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        // debated point: should we 404 on an unknown noteId?
        // or should we just return a nice (200 or 204) in all cases?
        // we're doing the latter
        this.notesService.deleteNote(noteId);
        redirectAttributes.addFlashAttribute("message", String.format("Note successfully deleted!"));
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/home#nav-notes";
    }
}
