package com.kcm.jpacalender.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserRequestDto {

    String username;
    @Email
    String email;
    String password;
}