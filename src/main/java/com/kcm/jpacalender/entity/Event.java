package com.kcm.jpacalender.entity;


import com.kcm.jpacalender.dto.EventRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.util.ArrayList;
import java.util.*;


@Entity
@Getter
@Setter
@Table(name = "events")
@NoArgsConstructor
public class Event extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

//    @Column(nullable = false)
//    private String username;
    @Column(nullable = false) // 5단계에서 수정
    private Long user_id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})//2단계. 하나의 본문에 여러 댓글이 달린다., 4단계. 영속성 전이를 이용한 연관 관계 객체 삭제
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})//2단계. 하나의 본문에 여러 댓글이 달린다., 4단계. 영속성 전이를 이용한 연관 관계 객체 삭제
    private List<Post> postList = new ArrayList<>();

    public Event(EventRequestDto eventRequestDto) {
        this.user_id = eventRequestDto.getUserid();// 5단계에서 name -> id
        this.title = eventRequestDto.getTitle();
        this.content = eventRequestDto.getContent();
    }

    public void update(EventRequestDto eventRequestDto) {
        this.user_id = eventRequestDto.getUserid();// 5단계에서 name -> id
        this.title = eventRequestDto.getTitle();
        this.content = eventRequestDto.getContent();
    }



}
