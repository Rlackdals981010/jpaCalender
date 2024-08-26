package com.kcm.jpacalender.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}