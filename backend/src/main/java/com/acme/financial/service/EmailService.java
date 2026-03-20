package com.acme.financial.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Value("${mail.from}")
    private String fromEmail;

    @Value("${spring.mail.password}") // This is the API Key in Render environment variables
    private String apiKey;

    @Async
    public void sendEmail(String to, String subject, String content) {
        System.out.println(">>> [API EMAIL] Starting Dispatch to: " + to);
        try {
            URL url = new URL("https://api.brevo.com/v3/smtp/email");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("api-key", apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Manual JSON construction to avoid external library conflicts
            String jsonPayload = String.format(
                "{\"sender\":{\"email\":\"%s\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"textContent\":\"%s\"}",
                fromEmail, to, subject, content
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                System.out.println(">>> [API SUCCESS] Email sent via Brevo HTTP REST API (unblockable port 443)");
            } else {
                System.err.println(">>> [API ERROR] Brevo rejection code: " + code);
            }
        } catch (Exception e) {
            System.err.println(">>> [API CRITICAL] Handshake to Brevo API failed: " + e.getMessage());
        }
    }
}
