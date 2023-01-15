package com.techblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PasswordRequest {

    @NotBlank
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!&#%]).{6,15}$",message ="password should must must contain digit,lower case alphabet,uppercase alphabet and at least one special character(@,$,!,&), and password length should be 6-15 characters")
    private String newPassword;
}
