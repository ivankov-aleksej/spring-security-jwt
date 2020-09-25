package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.model.RegistrationUser;
import com.example.springsecurityjwt.model.ResponseToken;
import com.example.springsecurityjwt.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
public class TestController {

	private RestTemplate restTemplate  = new RestTemplate();

	private static final String REGISTRATION_URL = "http://localhost:8080/register";
	private static final String AUTHENTICATION_URL = "http://localhost:8080/authenticate";
	private static final String HELLO_URL = "http://localhost:8080/helloadmin";

	@GetMapping(value = "/getResponse")
	public String getResponse() throws JsonProcessingException {

		String response = null;
		// create user registration object
		RegistrationUser registrationUser = getRegistrationUser();
		// convert the user registration object to JSON
		String registrationBody = getBody(registrationUser);
		// create headers specifying that it is JSON request
		HttpHeaders registrationHeaders = getHeaders();
		HttpEntity<String> registrationEntity = new HttpEntity<>(registrationBody, registrationHeaders);

		try {
			// Register User
			ResponseEntity<String> registrationResponse = restTemplate.exchange(REGISTRATION_URL, HttpMethod.POST,
					registrationEntity, String.class);
			   // if the registration is successful		
			System.out.println(registrationResponse.getStatusCode().equals(HttpStatus.OK));
			
			if (registrationResponse.getStatusCode().equals(HttpStatus.OK)) {

				// create user authentication object
				User authenticationUser = getAuthenticationUser();
				// convert the user authentication object to JSON
				String authenticationBody = getBody(authenticationUser);
				// create headers specifying that it is JSON request
				HttpHeaders authenticationHeaders = getHeaders();
				HttpEntity<String> authenticationEntity = new HttpEntity<>(authenticationBody,
						authenticationHeaders);

				// Authenticate User and get JWT
				ResponseEntity<ResponseToken> authenticationResponse = restTemplate.exchange(AUTHENTICATION_URL,
						HttpMethod.POST, authenticationEntity, ResponseToken.class);
					
				// if the authentication is successful		
				System.out.println(authenticationResponse.getStatusCode().equals(HttpStatus.OK));
				// if the authentication is successful		
				if (authenticationResponse.getStatusCode().equals(HttpStatus.OK)) {
					String token = "Bearer " + Objects.requireNonNull(authenticationResponse.getBody()).getToken();
					HttpHeaders headers = getHeaders();
					headers.set("Authorization", token);
					HttpEntity<String> jwtEntity = new HttpEntity<>(headers);
					// Use Token to get Response
					ResponseEntity<String> helloResponse = restTemplate.exchange(HELLO_URL, HttpMethod.GET, jwtEntity,
							String.class);
					if (helloResponse.getStatusCode().equals(HttpStatus.OK)) {
						response = helloResponse.getBody();
					}
				}
			}
			
				
		}catch(Exception ex)
		{
			System.out.println("exception");
		}
		return response;
	}
	
	private RegistrationUser getRegistrationUser() {
		RegistrationUser user = new RegistrationUser();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setRole("ROLE_ADMIN");
		return user;
	}

	private User getAuthenticationUser() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		return user;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String getBody(final User user) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(user);
	}
}