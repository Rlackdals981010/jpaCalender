package com.kcm.jpacalender.service;

import com.kcm.jpacalender.config.PasswordEncoder;
import com.kcm.jpacalender.dto.LoginRequestDto;
import com.kcm.jpacalender.dto.UserRequestDto;
import com.kcm.jpacalender.dto.UserResponseDto;
import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.entity.Post;
import com.kcm.jpacalender.entity.User;
import com.kcm.jpacalender.entity.UserRoleEnum;
import com.kcm.jpacalender.exception.IncorrectEmailException;
import com.kcm.jpacalender.exception.IncorrectPasswordException;
import com.kcm.jpacalender.jwt.JwtUtil;
import com.kcm.jpacalender.repository.EventRepository;
import com.kcm.jpacalender.repository.PostRepository;
import com.kcm.jpacalender.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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

    // 9 단계. 관리자 판별용 토큰
    @Value("${admin.token}")
    private String ADMIN_TOKEN;

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

        //9 단계 추가. 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(userRequestDto.isAdmin()){
            if (!ADMIN_TOKEN.equals(userRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }


        User createUser = new User(username, password, email,role);

        userRepository.save(createUser);
        String token = jwtUtil.createToken(createUser.getId(),createUser.getRole());
        jwtUtil.addJwtToCookie(token,res);

        return new UserResponseDto(createUser);
    }

    public User printUser(User user) {
        Long userId = user.getId();
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

    }

    public List<UserResponseDto> printUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public UserResponseDto updateUser(User user, UserRequestDto userRequestDto) {
        User updateuser = printUser(user);
        updateuser.update(userRequestDto);
        return new UserResponseDto(updateuser);
    }

    public void deleteUser(User user) {
        User deleteUser = printUser(user);
        userRepository.delete(deleteUser);
    }

    public void placeUser(User user, Long eventId, Long placeUserId) {
        User placeUser=userRepository.findById(placeUserId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));
        Event placeEvent=eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("해당 일정은 없습니다."));

        Post post = Post.createInstance();
        post.linkEventUser(placeEvent,placeUser);

        postRepository.save(post);
    }

    public void logIn(UserRequestDto loginRequestDto,HttpServletResponse res) {
        String userEmail = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        //이메일 확인
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("등록된 이메일이 없습니다.")
        );

        //비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getId(),user.getRole());
        jwtUtil.addJwtToCookie(token,res);

    }
}