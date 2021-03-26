package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FileController {
    private FilesService filesService;
    private UserService userService;

    public FileController(FilesService filesService, UserService userService) {
        this.filesService = filesService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User authUser = userService.getUserByUsername(authentication.getName());
            filesService.storeFile(fileUpload, authUser.getUserId());
            redirectAttributes.addFlashAttribute("message", String.format("You successfully uploaded \"%s\"!", fileUpload.getOriginalFilename()));
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/home";
    }

    @GetMapping("/delete/{fileId}")
    public String delete(@PathVariable Integer fileId, RedirectAttributes redirectAttributes) {
        // debated point: should we 404 on an unknown fileId?
        // or should we just return a nice (200 or 204) in all cases?
        // we're doing the latter
        filesService.delete(fileId);
        redirectAttributes.addFlashAttribute("message", String.format("File successfully deleted!"));
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/home";
    }

    @GetMapping("/show/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> show(@PathVariable String fileName) {
        File file = filesService.getFileByFilename(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getFileName()))
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .header(HttpHeaders.CONTENT_LENGTH, file.getFileSize())
                .body(file.getFileData());
    }
}
