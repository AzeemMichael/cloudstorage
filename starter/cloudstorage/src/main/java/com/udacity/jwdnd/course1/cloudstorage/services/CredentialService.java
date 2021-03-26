package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void createCredential(CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedSalt);

        Credential newCredential = new Credential(
                credentialForm.getUrl(),
                credentialForm.getUsername(),
                encodedSalt,
                hashedPassword
        );

        newCredential.setUserId(credentialForm.getUserId());
        credentialMapper.addCredential(newCredential);
    }

    public void updateCredential(CredentialForm credentialForm) {
        Credential credential = this.findById(credentialForm.getCredentialId());
        if (credential != null) {
            String hashedPassword = encryptionService.encryptValue(credentialForm.getPassword(), credential.getKey());
            credential.setUrl(credentialForm.getUrl());
            credential.setUsername(credentialForm.getUsername());
            credential.setPassword(hashedPassword);
            credentialMapper.updateCredential(credential);
        } else {
            throw new RuntimeException(String.format("Unable to find credentials for url: \"%s\"", credentialForm.getUrl()));
        }
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    public Credential findById(Integer credentialId) {
        return credentialMapper.findById(credentialId);
    }
    public List<Credential> getAllCredentialsByUserId(Integer userId) {
        return credentialMapper.findAllByUserId(userId);
    }
}
