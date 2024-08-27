package com.kcm.jpacalender.entity;

import com.kcm.jpacalender.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter

@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;
    @Column(nullable = false,length = 20)
    private String username;
    @Column(nullable = false,length = 200)
    private String comment;

    @ManyToOne //2단계. 여러 댓글이 1개의 일정에 묶인다.
    @JoinColumn(name = "event_id")
    private Event event;

    public Comment(CommentRequestDto commentRequestDto) {
        this.username = commentRequestDto.getUsername();
        this.comment = commentRequestDto.getComment();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.username = commentRequestDto.getUsername();
        this.comment = commentRequestDto.getComment();
    }

    public void linkEvent(Event event){
        this.event = event;
    }
}
