package com.kcm.jpacalender.dto;

import com.kcm.jpacalender.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    Long user_id;
    String userName;
    String email;
    LocalDateTime created_date;
    LocalDateTime updated_date;


    public UserResponseDto(User user) {
        this.user_id = user.getId();
        this.userName = user.getUsername();
        this.email = user.getEmail();
        this.created_date = user.getCreatedAt();
        this.updated_date = user.getModifiedAt();
    }

    public UserResponseDto(Long user_id, String userName, String email) {
        this.user_id = user_id;
        this.userName = userName;
        this.email = email;
    }
}