package com.kcm.jpacalender.controller;

import com.kcm.jpacalender.dto.EventRequestDto;
import com.kcm.jpacalender.dto.EventResponseDto;
import com.kcm.jpacalender.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventSerivce;

    public EventController(EventService eventSerivce) {
        this.eventSerivce = eventSerivce;
    }

    @PostMapping()
    public EventResponseDto createEvent(@RequestBody EventRequestDto eventRequestDto){
        return eventSerivce.createEvent(eventRequestDto);
    }

    @GetMapping("/{eventId}")
    public EventResponseDto printEvent(@PathVariable Long eventId){
        return new EventResponseDto(eventSerivce.findEvent(eventId));

    }

    @PutMapping("/{eventId}")
    public EventResponseDto updateEvent(@PathVariable Long eventId, @RequestBody EventRequestDto eventRequestDto){
        return eventSerivce.updateEvent(eventId, eventRequestDto);
    }

}
