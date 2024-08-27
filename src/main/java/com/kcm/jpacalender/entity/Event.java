package com.kcm.jpacalender.entity;


import com.kcm.jpacalender.dto.EventRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;




import java.util.ArrayList;
import java.util.*;


@Entity
@Getter

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
    @Column(nullable = false, length = 30)
    private String title;
    @Column(nullable = false, length = 200)
    private String content;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})//2단계. 하나의 본문에 여러 댓글이 달린다., 4단계. 영속성 전이를 이용한 연관 관계 객체 삭제. 6단계. 디폴트가 LAZY라 직접 접근을 명시해야지 Post테이블에 접근
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})//2단계. 하나의 본문에 여러 댓글이 달린다., 4단계. 영속성 전이를 이용한 연관 관계 객체 삭제
    private List<Post> postList = new ArrayList<>();

    // 10단계 날씨 필드 추가
    private String weather;

    public Event(User user, EventRequestDto eventRequestDto) {
        this.user_id = user.getId();// 5단계에서 name -> id
        this.title = eventRequestDto.getTitle();
        this.content = eventRequestDto.getContent();
    }

    public void update(User user,EventRequestDto eventRequestDto) {
        this.user_id = user.getId();
        this.title = eventRequestDto.getTitle();
        this.content = eventRequestDto.getContent();
    }

    public void installWeather(String weather){
        this.weather = weather;
    }



}
