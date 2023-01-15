package com.techblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Integer id;
    @NotEmpty
    @Size(min = 2,max = 15,message = "first name length must be 2-15 characters")
    private String firstName;
    @NotEmpty
    @Size(min = 2,max = 15,message = "last name length must be 2-15 characters")
    private String lastName;
    @NotBlank
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!&#%]).{6,15}$",message ="password should must must contain digit,lower case alphabet,uppercase alphabet and at least one special character(@,$,!,&), and password length should be 6-15 characters")
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss")
    private Date accountCreatedTime;
    private Set<RoleDto> roles=new HashSet<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secretCode;
}
