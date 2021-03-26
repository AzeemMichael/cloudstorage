package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private FilesService filesService;
    private NotesService notesService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(FilesService filesService, NotesService notesService, UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.filesService = filesService;
        this.notesService = notesService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User authUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("files", this.filesService.getAllFilesByUserId(authUser.getUserId()));
        model.addAttribute("notes", this.notesService.getAllNotes(authUser.getUserId()));
        model.addAttribute("credentials", this.credentialService.getAllCredentialsByUserId(authUser.getUserId()));
        model.addAttribute("encriptionService", encryptionService);
        return "home";
    }
}
