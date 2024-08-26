package com.kcm.jpacalender.entity;


import com.kcm.jpacalender.dto.EventRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Persistent;


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

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "event")//2단계. 하나의 본문에 여러 댓글이 달린다.
    private List<Comment> commentList = new ArrayList<>();

    public Event(EventRequestDto eventRequestDto) {
        this.username = eventRequestDto.getUsername();
        this.title = eventRequestDto.getTitle();
        this.content = eventRequestDto.getContent();
    }

    public void update(EventRequestDto eventRequestDto) {
        this.username = eventRequestDto.getUsername();
        this.title = eventRequestDto.getTitle();
        this.content = eventRequestDto.getContent();
    }

    public void addEventList(Comment comment) { // 2단계. 댓글 추가시 해당 원문에 연결
        this.commentList.add(comment);
        comment.setEvent(this);
    }
}
