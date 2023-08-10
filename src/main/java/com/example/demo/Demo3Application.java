package com.example.demo;

import com.example.demo.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

public class Demo3Application {
    private static String key = "";

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();

        // 1. Получение список всех пользователей
        ResponseEntity<User[]> response = restTemplate.getForEntity(
                "http://94.198.50.185:7081/api/users",
                User[].class);

        // Получаем session id из заголовка set-cookie
        HttpHeaders headers = response.getHeaders();
        String sessionId = headers.getFirst(HttpHeaders.SET_COOKIE);

        // 2. Сохранение пользователя
        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 30);

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.set(HttpHeaders.COOKIE, sessionId);
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> postRequestEntity = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://94.198.50.185:7081/api/users", HttpMethod.POST, postRequestEntity, String.class);
        key+=responseEntity.getBody();

        // 3. Изменение пользователя
        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        HttpEntity<User> putRequestEntity = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("http://94.198.50.185:7081/api/users", HttpMethod.PUT, putRequestEntity, String.class);
        key+=responseEntity2.getBody();

        // 4. Удаление пользователя
        ResponseEntity<String> responseEntity3 = restTemplate.exchange("http://94.198.50.185:7081/api/users/3", HttpMethod.DELETE, new HttpEntity<>(postHeaders), String.class);
        key+=responseEntity3.getBody();

        System.out.println("Итоговый код: " + key);
    }
}
