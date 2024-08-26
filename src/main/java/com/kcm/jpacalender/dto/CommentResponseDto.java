package com.kcm.jpacalender.dto;


import com.kcm.jpacalender.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {


    private Long id;
    private String username;
    private String comment;
    private Long event_id;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.comment = comment.getComment();
        this.event_id = comment.getEvent().getId();
        this.created_date = comment.getCreatedAt();
        this.updated_date = comment.getModifiedAt();
    }
}