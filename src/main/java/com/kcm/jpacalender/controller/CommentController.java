package com.kcm.jpacalender.controller;

import com.kcm.jpacalender.dto.CommentRequestDto;
import com.kcm.jpacalender.dto.CommentResponseDto;
import com.kcm.jpacalender.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{eventId}") //생성
    public CommentResponseDto createComment(@PathVariable Long eventId, @RequestBody CommentRequestDto commentRequestDto){
        return commentService.createComment(eventId,commentRequestDto);
    }

    @GetMapping("/{commentId}") // 단건 조회
    public CommentResponseDto printComment(@PathVariable Long commentId){
        return new CommentResponseDto(commentService.findComment(commentId));
    }

    @GetMapping() // 전체 조회
    public List<CommentResponseDto> printComments(){
        return commentService.printComments();
    }

    @PutMapping("/{commentId}") // 수정
    public Long updateComment(@PathVariable Long commentId,@RequestBody CommentRequestDto commentRequestDto){
        return commentService.updateComment(commentId,commentRequestDto);
    }

    @DeleteMapping("/{commentId}") // 삭제
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }
}
