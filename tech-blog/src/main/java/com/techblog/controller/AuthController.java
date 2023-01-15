package com.techblog.controller;

import com.techblog.dto.JwtAuthRequest;
import com.techblog.dto.JwtAuthResponse;
import com.techblog.dto.UserDto;
import com.techblog.model.User;
import com.techblog.security.CustomUserDetailsService;
import com.techblog.security.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody JwtAuthRequest jwtAuthRequest)
    {
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtAuthRequest.getEmail().toLowerCase(),jwtAuthRequest.getPassword()));
        String jwtToken=jwtUtils.generateJwtToken(authentication);
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(jwtAuthRequest.getEmail());
        UserDto user=modelMapper.map(userDetails, UserDto.class);
        user.setPassword("");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(new JwtAuthResponse(jwtToken,user), HttpStatus.OK);
    }
}
