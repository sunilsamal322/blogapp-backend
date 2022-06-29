package com.techblog.controller;

import com.techblog.dto.JwtAuthRequest;
import com.techblog.dto.JwtAuthResponse;
import com.techblog.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody JwtAuthRequest jwtAuthRequest)
    {
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtAuthRequest.getEmail(),jwtAuthRequest.getPassword()));
        String jwtToken=jwtUtils.generateJwtToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(new JwtAuthResponse(jwtToken), HttpStatus.OK);
    }
}
