package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.config.CustomUserDetailsService;
import com.example.springsecurityjwt.config.JwtUtil;
import com.example.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword());

			authenticationManager.authenticate(token);
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtUtil.generateToken(userdetails);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
}
