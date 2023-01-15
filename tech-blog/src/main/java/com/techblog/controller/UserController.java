package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.PasswordRequest;
import com.techblog.dto.UserDto;
import com.techblog.exception.UserNotFoundException;
import com.techblog.model.Role;
import com.techblog.model.User;
import com.techblog.repository.UserRepository;
import com.techblog.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> registerNormalUser(@Valid @RequestBody UserDto userDto)
    {
        return new ResponseEntity<>(userServices.createUser(userDto), HttpStatus.CREATED);
    }
    @PostMapping("/signup/admin")
    public ResponseEntity<UserDto> registerAdminUser(@Valid @RequestBody UserDto userDto)
    {
        return new ResponseEntity<>(userServices.createAdminUser(userDto), HttpStatus.CREATED);
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile()
    {
        return new ResponseEntity<>(userServices.getUserById(getLoginUser().getId()),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id)
    {
        if(checkIsRoleAdmin()) {
            return new ResponseEntity<>(userServices.getUserById(id), HttpStatus.OK);
        }
        else if (getLoginUser().getId()==id)
        {
            return new ResponseEntity<>(userServices.getUserById(id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return new ResponseEntity<>(userServices.getAllUsers(),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable Integer id)
    {
        if(checkIsRoleAdmin()) {
            return new ResponseEntity<>(userServices.updateUser(userDto, id), HttpStatus.OK);
        }
        else if (getLoginUser().getId()==id)
        {
            return new ResponseEntity<>(userServices.updateUser(userDto, id), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id)
    {
        userServices.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse("user deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable Integer id,@Valid @RequestBody PasswordRequest passwordRequest){
        if(getLoginUser().getId()==id){
            if(userServices.changePassword(id,passwordRequest)){
                return new ResponseEntity<>(new ApiResponse("Password Changed successfully",true,String.valueOf(HttpStatus.OK),Instant.now()),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ApiResponse("Wrong password " ,false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean checkIsRoleAdmin()
    {
        User user=getLoginUser();
        Set<Role> roles=user.getRoles();
        for(Role role:roles)
        {
            if(role.getName().equals("ROLE_ADMIN"))
            {
             return true;
            }
        }
        return false;
    }
    public User getLoginUser()
    {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
