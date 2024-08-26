package com.kcm.jpacalender.dto;

import com.kcm.jpacalender.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventResponseDto {


    private Long id;
    private String userName;
    private String title;
    private String content;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;

    private Integer commentCount;


    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.userName = event.getUsername();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.created_date = event.getCreatedAt();
        this.updated_date = event.getModifiedAt();
    }

    public EventResponseDto(Long event_id,String title, String content, Integer commentCount, LocalDateTime createdAt, LocalDateTime modifiedAt, String username){
        this.id = event_id;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.created_date = createdAt;
        this.updated_date = modifiedAt;
        this.userName = username;
    }
}