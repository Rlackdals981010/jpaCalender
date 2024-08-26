package com.kcm.jpacalender.dto;

import com.kcm.jpacalender.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private String weather;


    List<UserResponseDto> users;
    //5단계에서 userName -> userId로 수정
    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.userId = event.getUser_id();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.created_date = event.getCreatedAt();
        this.updated_date = event.getModifiedAt();
        this.commentCount = event.getCommentList().size();
        this.weather = event.getWeather();
    }




    //6단계에서 유저의 고유 식별자, 유저명 , 이메일 추가
    public EventResponseDto(Event event, List<UserResponseDto> users) {
        this.id = event.getId();
        this.userId = event.getUser_id();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.commentCount = event.getCommentList().size();
        this.created_date = event.getCreatedAt();
        this.updated_date = event.getModifiedAt();
        this.users = users;
    }

}