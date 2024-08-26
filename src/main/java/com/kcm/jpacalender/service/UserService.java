package com.kcm.jpacalender.service;

import com.kcm.jpacalender.config.PasswordEncoder;
import com.kcm.jpacalender.dto.LoginRequestDto;
import com.kcm.jpacalender.dto.UserRequestDto;
import com.kcm.jpacalender.dto.UserResponseDto;
import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.entity.Post;
import com.kcm.jpacalender.entity.User;
import com.kcm.jpacalender.jwt.JwtUtil;
import com.kcm.jpacalender.repository.EventRepository;
import com.kcm.jpacalender.repository.PostRepository;
import com.kcm.jpacalender.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, EventRepository eventRepository, PostRepository postRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //7단계. 비밀번호 추가
    public UserResponseDto createUser(UserRequestDto userRequestDto, HttpServletResponse res) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        //이메일 중복 확인
        String email = userRequestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 Email입니다.");
        }

        User createUser = new User(username, password, email);

        userRepository.save(createUser);
        String token = jwtUtil.createToken(createUser.getId());
        jwtUtil.addJwtToCookie(token,res);

        return new UserResponseDto(createUser);
    }

    public User printUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

    }

    public List<UserResponseDto> printUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User updateuser = printUser(userId);
        updateuser.update(userRequestDto);
        return new UserResponseDto(updateuser);
    }

    public void deleteUser(Long userId) {
        User deleteUser = printUser(userId);
        userRepository.delete(deleteUser);
    }

    public void placeUser(Long userId, Long eventId, Long placeUserId) {
        User placeUser=userRepository.findById(placeUserId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));
        Event placeEvent=eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("해당 일정은 없습니다."));

        Post post = new Post();
        post.setEvent(placeEvent);
        post.setUser(placeUser);
        postRepository.save(post);
    }

    public void logIn(LoginRequestDto loginRequestDto, HttpServletResponse res) {

    }
}