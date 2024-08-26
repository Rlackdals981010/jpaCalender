package com.kcm.jpacalender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcm.jpacalender.dto.EventRequestDto;
import com.kcm.jpacalender.dto.EventResponseDto;

import com.kcm.jpacalender.dto.UserResponseDto;
import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.entity.Post;
import com.kcm.jpacalender.entity.User;
import com.kcm.jpacalender.exception.NoAuthenticationUser;
import com.kcm.jpacalender.repository.EventRepository;
import com.kcm.jpacalender.repository.PostRepository;
import com.kcm.jpacalender.repository.UserRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kcm.jpacalender.entity.UserRoleEnum.USER;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final RestTemplate restTemplate;

    public EventService(EventRepository eventRepository, UserRepository userRepository, PostRepository postRepository, RestTemplateBuilder builder) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.restTemplate = builder.build();
    }


    public EventResponseDto createEvent(User user,EventRequestDto eventRequestDto) {
        Event event = new Event(eventRequestDto);
        eventRepository.save(event);  // 엔티티를 먼저 저장하여 createdAt이 설정되도록 함

        String weather = getWeather(event.getCreatedAt());
        event.setWeather(weather);

        setUserToEvent(event.getId(), user.getId()); // 5단계. post랑 연관관계

        return new EventResponseDto(event);
    }



    public Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("존재 하지 않는 일정입니다.")
        );
    }

    @Transactional
    public EventResponseDto updateEvent(User user,Long eventId, EventRequestDto eventRequestDto) {
        if(user.getRole()==USER){
            throw new NoAuthenticationUser("권한이 없습니다.");
        }
        Event updateEvent = findEvent(eventId);
        updateEvent.update(user,eventRequestDto);
        return new EventResponseDto(updateEvent);
    }

    @Transactional(readOnly = true)
    public EventResponseDto printEvent(Long eventId) {
        Event event = findEvent(eventId);

        List<UserResponseDto> users = event.getPostList().stream()
                .map(post -> {
                    User user = post.getUser();
                    return new UserResponseDto(user); // UserResponseDto로 변환합니다.
                })
                .collect(Collectors.toList());

        return new EventResponseDto(event, users);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDto> printEvents(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        return eventRepository.findAll(pageable)
                .map(event -> new EventResponseDto(event))
                .getContent();
    }

    public Long deleteEvent(User user,Long eventId) {
        if(user.getRole()==USER){
            throw new NoAuthenticationUser("권한이 없습니다.");
        }
        Event deleteEvent = findEvent(eventId);
        eventRepository.delete(deleteEvent);
        return eventId;
    }

    public void setUserToEvent(Long eventId, Long userId) { // 5단계. post랑 연관관계
        Event setEvent = findEvent(eventId);
        User setUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));

        Post post = new Post();
        post.setEvent(setEvent);
        post.setUser(setUser);

        postRepository.save(post);
    }

    public String getWeather(LocalDateTime date) {
        // 날짜를 "MM-dd" 형식으로 포맷팅
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd"));

        URI uri = UriComponentsBuilder
                .fromUriString("https://f-api.github.io")
                .path("/f-api/weather.json")
                .queryParam("date", formattedDate)
                .encode()
                .build()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        String responseBody = responseEntity.getBody();

        // Jackson ObjectMapper를 사용하여 JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> weatherData = null;
        try {
            weatherData = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, String>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 예외 처리: 로그를 남기거나 기본 값을 반환할 수 있습니다.
            return "Error processing weather data";
        }

        // 날짜에 맞는 날씨 정보를 찾기
        for (Map<String, String> entry : weatherData) {
            if (entry.get("date").equals(formattedDate)) {
                return entry.get("weather");
            }
        }

        // 날짜에 맞는 날씨 정보가 없는 경우
        return "No weather data available for the specified date";
    }
}
