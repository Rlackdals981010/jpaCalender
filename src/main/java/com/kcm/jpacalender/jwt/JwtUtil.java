package com.kcm.jpacalender.jwt;


import com.kcm.jpacalender.entity.UserRoleEnum;
import com.kcm.jpacalender.exception.NoTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    public String createToken(Long userId, UserRoleEnum role){
        Date date = new Date(); // 발급일

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(Long.toString(userId))//ID
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))//만료 시각
                        .setIssuedAt(date) //생성일
                        .signWith(key,signatureAlgorithm) //암호와 정책
                        .compact(); //토큰 생성
    }

    public void addJwtToHear(String token, HttpServletResponse res){
        res.setHeader(AUTHORIZATION_HEADER, token);
    }

    //쿠키에 토큰 넣어 보내기
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            res.setHeader(AUTHORIZATION_HEADER, token);//헤더에도 담아

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");


            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

    //JWT에서 실제 토큰값 추출하기
    public String substringToken(String tokenValue) {
        if(StringUtils.hasText(tokenValue)&&tokenValue.startsWith(BEARER_PREFIX)){
            return tokenValue.substring(7);
        }
        throw new NullPointerException("토큰을 찾을 수 없습니다.");
    }

    // 토큰 검증

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            System.out.println("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException | NoTokenException e) {
            System.out.println("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
