package com.kcm.jpacalender.service;

import com.kcm.jpacalender.dto.EventRequestDto;
import com.kcm.jpacalender.dto.EventResponseDto;

import com.kcm.jpacalender.dto.UserResponseDto;
import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.entity.Post;
import com.kcm.jpacalender.entity.User;
import com.kcm.jpacalender.repository.EventRepository;
import com.kcm.jpacalender.repository.PostRepository;
import com.kcm.jpacalender.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public EventService(EventRepository eventRepository,UserRepository userRepository,PostRepository postRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public EventResponseDto createEvent(User user,EventRequestDto eventRequestDto) {
        Event event = new Event(eventRequestDto);

        EventResponseDto eventResponseDto = new EventResponseDto(eventRepository.save(event));
        setUserToEvent(event.getId(), user.getId()); // 5단계. post랑 연관관계

        return eventResponseDto;
    }

    public Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("존재 하지 않는 일정입니다.")
        );
    }

    @Transactional
    public EventResponseDto updateEvent(User user,Long eventId, EventRequestDto eventRequestDto) {
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

    public Long deleteEvent(Long eventId) {
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
}
