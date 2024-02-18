package com.example.lab1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab1ApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetNews() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/news?category=general", Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetNewsInvalidCategory() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/news?category=invalid", Object.class);
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected status code not to be INTERNAL_SERVER_ERROR");
    }
}