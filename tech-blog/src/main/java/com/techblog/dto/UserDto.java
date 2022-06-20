package com.techblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techblog.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Integer id;
    @NotEmpty
    @Size(min = 2,max = 20,message = "first name length must be 2-20 characters")
    private String firstName;
    @NotEmpty
    @Size(min = 2,max = 20,message = "last name length must be 2-20 characters")
    private String lastName;
    @Email(message = "email address is invalid")
    private String email;
    @NotEmpty
    @Size(min=6,max=15,message = "password length must be 6-15 characters")
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant accountCreatedTime;
    private Set<RoleDto> roles=new HashSet<>();
}
