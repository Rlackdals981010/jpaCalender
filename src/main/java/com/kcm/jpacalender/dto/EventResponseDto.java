package com.kcm.jpacalender.dto;

import com.kcm.jpacalender.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventResponseDto {


    private Long id;
//    private String userName;
    private Long userId; // 5단계에서 수정
    private String title;
    private String content;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;

    private Integer commentCount;


    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.userId = event.getUser_id();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.created_date = event.getCreatedAt();
        this.updated_date = event.getModifiedAt();
    }

    //5단계에서 userName -> userId로 수정
    public EventResponseDto(Long event_id,String title, String content, Integer commentCount, LocalDateTime createdAt, LocalDateTime modifiedAt, Long userId){
        this.id = event_id;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.created_date = createdAt;
        this.updated_date = modifiedAt;
        this.userId = userId;
    }
}