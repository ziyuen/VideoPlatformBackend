package com.example.controller;

import com.example.message.RegistrationRequest;
import com.example.message.ResponseHandler;
import com.example.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<Object> register(@RequestBody RegistrationRequest request) {
        try {
            registrationService.register(request);
            return ResponseHandler.generateResponse("SUCCESS",
                    HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("fail to sign up, Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
