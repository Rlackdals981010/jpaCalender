package com.kcm.jpacalender.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    //생성, 쿠키에 저장, JWT의 subString, 검증, 사용자 정보 가져오기
    //////////////////////////////////////////////////
    //기본 설정
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //////////////////////////////////////////////////

    //생성
    public String createToken(Long userId){
        Date date = new Date(); // 발급일

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(Long.toString(userId))//ID
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))//만료 시각
                        .setIssuedAt(date) //생성일
                        .signWith(key,signatureAlgorithm) //암호와 정책
                        .compact(); //토큰 생성
    }

    //쿠키에 토큰 넣어 보내기
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            res.setHeader(AUTHORIZATION_HEADER, token);//헤더에도 담아

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

}