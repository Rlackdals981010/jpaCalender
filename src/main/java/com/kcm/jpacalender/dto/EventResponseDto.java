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


    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.userName = event.getUsername();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.created_date = event.getCreatedAt();
        this.updated_date = event.getModifiedAt();
    }
}