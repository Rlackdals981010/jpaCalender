package com.kcm.jpacalender.entity;


import com.kcm.jpacalender.dto.EventRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
