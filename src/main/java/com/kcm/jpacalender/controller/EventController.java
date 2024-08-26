package com.kcm.jpacalender.controller;

import com.kcm.jpacalender.dto.EventRequestDto;
import com.kcm.jpacalender.dto.EventResponseDto;
import com.kcm.jpacalender.entity.User;
import com.kcm.jpacalender.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventSerivce;

    public EventController(EventService eventSerivce) {
        this.eventSerivce = eventSerivce;
    }

    //8단계. 로그인한 user가 event를 생성하는 방식으로 변경
    @PostMapping()
    public EventResponseDto createEvent(@RequestBody EventRequestDto eventRequestDto, HttpServletRequest req){
        User user = (User) req.getAttribute("user");

        return eventSerivce.createEvent(user,eventRequestDto);
    }

    @GetMapping("/{eventId}")
    public EventResponseDto printEvent(@PathVariable Long eventId){
        //return new EventResponseDto(eventSerivce.findEvent(eventId));
        return eventSerivce.printEvent(eventId);
    }

    //3단계. 페이징 추가
    @GetMapping()
    public List<EventResponseDto> printEvents(@RequestParam(value = "page") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size){
        return eventSerivce.printEvents(page, size);

    }

    //8단계. 로그인한 user가 event를 수정하는 방식으로 변경
    @PutMapping("/{eventId}")
    public EventResponseDto updateEvent(@PathVariable Long eventId, @RequestBody EventRequestDto eventRequestDto,HttpServletRequest req){
        User user = (User) req.getAttribute("user");
        return eventSerivce.updateEvent(user,eventId, eventRequestDto);
    }

    //4단계. 일정 삭제 추가
    @DeleteMapping("{eventId}")
    public Long deleteEvent(@PathVariable Long eventId){
        return eventSerivce.deleteEvent(eventId);
    }


}
