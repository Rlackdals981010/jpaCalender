package com.kcm.jpacalender.service;

import com.kcm.jpacalender.dto.CommentRequestDto;
import com.kcm.jpacalender.dto.CommentResponseDto;
import com.kcm.jpacalender.entity.Comment;
import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.repository.CommentRepository;
import com.kcm.jpacalender.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    public CommentService(CommentRepository commentRepository,EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
    }

    public CommentResponseDto createComment(Long eventId, CommentRequestDto commentRequestDto) {
        Event event = findEvent(eventId);
        Comment createComment = new Comment(commentRequestDto);
        createComment.linkEvent(event);
        commentRepository.save(createComment);
        return new CommentResponseDto(createComment);
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
    }

    public List<CommentResponseDto> printComments() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public Long updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment updateComment = findComment(commentId);
        updateComment.update(commentRequestDto);
        return commentId;
    }

    public void deleteComment(Long commentId) {
        Comment deleteComment = findComment(commentId);
        commentRepository.delete(deleteComment);
    }

    public Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()->
                new IllegalArgumentException("존재 하지 않는 일정입니다.")
        );
    }
}