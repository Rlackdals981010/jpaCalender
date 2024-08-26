package com.kcm.jpacalender.service;

import com.kcm.jpacalender.dto.EventRequestDto;
import com.kcm.jpacalender.dto.EventResponseDto;
import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;    }


    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        Event event = new Event(eventRequestDto);

        EventResponseDto eventResponseDto = new EventResponseDto(eventRepository.save(event));

        return eventResponseDto;
    }

    public Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()->
                new IllegalArgumentException("존재 하지 않는 일정입니다.")
        );
    }

    @Transactional
    public EventResponseDto updateEvent(Long eventId, EventRequestDto eventRequestDto) {
        Event updateEvent = findEvent(eventId);
        updateEvent.update(eventRequestDto);
        return new EventResponseDto(updateEvent);
    }

    public List<EventResponseDto> printEvents(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        return eventRepository.findAll(pageable)
                .map(event -> new EventResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getContent(),
                        event.getCommentList().size(),
                        event.getCreatedAt(),
                        event.getModifiedAt(),
                        event.getUsername()
                ))
                .getContent();
    }
}
