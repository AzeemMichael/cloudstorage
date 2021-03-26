package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdate(Authentication authentication, CredentialForm credentialForm, RedirectAttributes redirectAttributes) {
        User authUser = this.userService.getUserByUsername(authentication.getName());
        credentialForm.setUserId(authUser.getUserId());

        try {
            if (credentialForm.getCredentialId() == null) {
                this.credentialService.createCredential(credentialForm);
                redirectAttributes.addFlashAttribute("message", "Credentials Saved!");
            } else {
                this.credentialService.updateCredential(credentialForm);
                redirectAttributes.addFlashAttribute("message", "Credentials Updated!");
            }
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }

        credentialForm.setUrl("");
        credentialForm.setUsername("");
        credentialForm.setPassword("");
        return "redirect:/home#nav-credentials";
    }

    @GetMapping("delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes) {
        // debated point: should we 404 on an unknown noteId?
        // or should we just return a nice (200 or 204) in all cases?
        // we're doing the latter
        this.credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("message", String.format("Credentials successfully deleted!"));
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/home#nav-credentials";
    }
}
