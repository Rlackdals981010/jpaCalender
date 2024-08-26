package com.kcm.jpacalender.entity;

import com.kcm.jpacalender.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String password;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Post> postList = new ArrayList<>();


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void update(UserRequestDto userRequestDto) {
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
    }
}