package com.example.Food.delivery.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenVerifierService {

    @Value("${google.clientId}")
    private String clientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void init() throws Exception {
        verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, Exception {
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new Exception("Invalid Google ID Token");
        }
        return idToken.getPayload();
    }
}
