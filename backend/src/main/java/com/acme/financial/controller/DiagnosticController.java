package com.acme.financial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/diagnostics")
public class DiagnosticController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/smtp")
    public Map<String, Object> testSmtp() {
        Map<String, Object> response = new HashMap<>();
        try {
            if (mailSender instanceof JavaMailSenderImpl) {
                ((JavaMailSenderImpl) mailSender).testConnection();
                response.put("status", "SUCCESS");
                response.put("message", "Handshake with Brevo SMTP relay established successfully!");
                response.put("host", ((JavaMailSenderImpl) mailSender).getHost());
            } else {
                response.put("status", "UNKNOWN");
                response.put("message", "MailSender is not a JavaMailSenderImpl instance.");
            }
        } catch (Exception e) {
            response.put("status", "FAILURE");
            response.put("error", e.getMessage());
            response.put("advice", "Please verify your MAIL_USER and MAIL_PASS in Render. Ensure they match your Brevo SMTP 'Login' and 'API Key'.");
            System.err.println(">>> [DIAGNOSTIC FAILURE] SMTP Connection failed: " + e.getMessage());
        }
        return response;
    }
}
