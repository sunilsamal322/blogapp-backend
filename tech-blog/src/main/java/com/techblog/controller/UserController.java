package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.UserDto;
import com.techblog.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/users")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto)
    {
        return new ResponseEntity<>(userServices.createUser(userDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id)
    {
        return new ResponseEntity<>(userServices.getUserById(id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return new ResponseEntity<>(userServices.getAllUsers(),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable Integer id)
    {
        return new ResponseEntity<>(userServices.updateUser(userDto,id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id)
    {
        userServices.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse("user deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }
}
