package com.rest;

import com.rest.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MainApplication {
    private static final String API_URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = getAllUsers(restTemplate);
        String sessionId = extractSessionId(response);

        String part1 = saveUser(restTemplate, sessionId);
        String part2 = updateUser(restTemplate, sessionId);
        String part3 = deleteUser(restTemplate, sessionId);

        String finalCode = part1 + part2 + part3;
        System.out.println("Итоговый код: " + finalCode);

        System.exit(0);
    }

    private static ResponseEntity<String> getAllUsers(RestTemplate restTemplate) {
        return restTemplate.getForEntity(API_URL, String.class);
    }

    private static String extractSessionId(ResponseEntity<String> response) {
        HttpHeaders headers = response.getHeaders();
        return headers.getFirst(HttpHeaders.SET_COOKIE);
    }

    private static String saveUser(RestTemplate restTemplate, String sessionId) {
        User newUser = new User(3L, "James", "Brown", (byte) 30);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private static String updateUser(RestTemplate restTemplate, String sessionId) {
        User updateUser = new User(3L, "Thomas", "Shelby", (byte) 35);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<User> request = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.PUT, request, String.class);
        return response.getBody();
    }

    private static String deleteUser(RestTemplate restTemplate, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL + "/3", HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }
}
