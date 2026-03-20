package com.acme.financial.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/diagnostics")
public class DiagnosticController {

    @Value("${spring.mail.password}")
    private String apiKey;

    @GetMapping("/smtp")
    public Map<String, Object> testApi() {
        Map<String, Object> response = new HashMap<>();
        try {
            URL url = new URL("https://api.brevo.com/v3/smtp/statistics/reports");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("api-key", apiKey);
            
            int code = conn.getResponseCode();
            if (code == 200) {
                response.put("status", "SUCCESS");
                response.put("message", "Authenticated with Brevo REST API successfully!");
            } else {
                response.put("status", "FAILURE");
                response.put("error_code", code);
                response.put("message", "Brevo API rejected the key. Check MAIL_PASS in Render.");
                response.put("advice", "Please verify your MAIL_USER and MAIL_PASS in Render environment variables.");
            }
        } catch (Exception e) {
            response.put("status", "FAILURE");
            response.put("error", e.getMessage());
        }
        return response;
    }
}
