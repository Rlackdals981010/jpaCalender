package com.kcm.jpacalender.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController{

    // 8단계 로그인 예외처리 1
    // 이메일, 비밀번호 불일치 401
    @ExceptionHandler(IncorrectEmailException.class)
    public ResponseEntity<String> handledIncorrectPasswordException(IncorrectEmailException x) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(x.getMessage());
    }
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handledIncorrectPasswordException(IncorrectPasswordException x) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(x.getMessage());
    }
    // 8단계 로그인 예외처리 2
    // 토큰 미존재 400
    @ExceptionHandler(NoTokenException.class)
    public ResponseEntity<String> handledIncorrectPasswordException(NoTokenException x) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(x.getMessage());
    }

    // 8단계 로그인 예외처리 3
    // 유효기간 만료 토큰 401
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handledIncorrectPasswordException(ExpiredJwtException x) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(x.getMessage());
    }

    // 9단계 권한 예외 처리 1
    // 권한 없는 유저 403
    @ExceptionHandler(NoAuthenticationUser.class)
    public ResponseEntity<String> handledIncorrectPasswordException(NoAuthenticationUser x) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(x.getMessage());
    }
}