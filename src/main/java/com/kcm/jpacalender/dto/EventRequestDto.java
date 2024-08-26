package com.kcm.jpacalender.dto;


import lombok.Getter;

@Getter
public class EventRequestDto {

    //String username;
    //Long userid; // 5단계에서 수정, 8단계로 인해 불필요해짐
    String title;
    String content;

}