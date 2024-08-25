# SpringJpaCalender

---

## Calendar v2.0

> 개발 기간 2024.08.19 ~ 2024.08.21

## 정보

JPA를 활용한 upgrade 일정 관리 앱 서버 입니다.

## 배포 주소

미배포

## 소개

[김창민](https://github.com/Rlackdals981010)

## 프로젝트 소개

- 개인용 일정을 저장하는 캘린더 입니다.
- 회원가입, 로그인을 지원합니다.
- 외부 정보를 호출 할 수 있습니다.
- 단일 일정에는 할일, 담당자명, 생성일, 최종 수정일이 저장됩니다.
- 작성, 단일조회, 범위조회, 수정, 삭제 기능을 지원합니다.

---

## 요구 사항

### 비기능적 요구사항

1. 모든 테이블을 ID를 갖는다
2. 3 Layer Architecture형태를 갖는다
3. CRUD 기능은 JPA를 통해 개발한다.
4. JDBC, Spirng Security는 사용하지 않는다.
5. 인증/인가는 JWT를 이용한다.
6. Entity 연간관계는 양방향으로 구현한다.

### 기능적 요구사항

- 일정 CRU
    - 일정은 저장, 단전 조회, 수정하는 기능을 갖는다.
    - 일정은 작성 유저명, 할일 제목, 할일 내용, 작성일, 수정일 필드를 포함한다.


- 댓글 CRUD
    - 일정에 댓글을 달 수 있다.
        - 댓글과 일정은 연관관계를 갖는다.
    - 댓글은 저장, 단건 조회, 전체 조회, 수정, 삭제 기능을 갖는다.
    - 댓글은 내용, 작성일, 수정일, 작성 유저명 필드를 갖는다.


- 일정 페이징 조회
    - 일정을 Spring Data JPA의 Pageble, Page 인터페이스를 활용해서 페이지네이션 구현
        - 페이지 번호와 페이지 크리가 쿼리 파라미터로 전달된다.
        - 할일 제목, 할일 내용, 댓글 개수, 일정 작성일, 일정 수정일, 일정 작성 유저명 필드를 조회한다.
        - 디폴트 페이지 크기는 10이다.
    - 수정일을 기준으로 내림차순 정렬한다.


- 일정 삭제
    - 일정 삭제시 일정 댓글로 동시 삭제한다.


- 유저 CRUD
    - 유저는 저장, 단건 조회, 전체 조회, 수정, 삭제 기능을 갖는다.
        - 유저명, 이메일, 작성일, 수정일 필드를 갖는다.
    - 일정은 유저명이 아닌 유저 고유 식별자 필드로 교체된다.
    - 일정을 작성한 유저는 추가로 일정 담당 유저를 배치할 수 있다.
        - 유저와 일정은 N:M 관계이나, @ManyToMany는 사용할 수 없다.


- 일정 조회 개선
    - 일정 단건 조회 시 담당 유저들의 고유 식별자, 유저명, 이메일이 추가로 포함된다.
    - 일정 전체 조회 시 담당 유저 정보가 포함되지 않는다.
        - JPA의 지연 로딩을 사용한다.

# Stacks

![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![스프링](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![mysql](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)

---

# API

---

## 1~4 단계

## 일정

| 기능        | Method   | URL                     | request | response    | 상태코드                       |
|-----------|----------|-------------------------|---------|-------------|----------------------------|
| 일정 작성     | `POST`   | `/events`               | Body    | 등록 정보       | `200 : 정상작성`,`404 : 작성 불가` |
| 선택한 일정 조회 | `GET`    | `/events/{eventId}`     | query   | 단건 응답 정보    | `200 : 정상조회`,`404 : 조회불가`  |
| 선택한 일정 수정 | `PUT`    | `/events/{eventId}`     | Body    | 수정 정보       | `200 : 정상수정`               |
| 전체 일정 조회  | `GET`    | `/events/{page}/{size}` | Param   | 페이지네이션 된 일정 | `200 : 정상조회`,`404: 조회불가`   |
| 선택한 일정 삭제 | `DELETE` | `/events/{eventId}`     | query   | -           | `200 : 정상수정`               |

## 댓글

| 기능        | Method   | URL                     | request | response | 상태코드         |
|-----------|----------|-------------------------|---------|----------|--------------|
| 댓글 저장     | `POST`   | `/comments/{eventId}`   | Body    | 등록 정보    | `200 : 정상작성` |
| 댓글 단건 조회  | `GET`    | `/comments/{commentId}` | query   | 단건 응답 정보 | `200 : 정상조회` |
| 댓글 전체 조회  | `GET`    | `/comments/`            | -       | 전체 응답 정보 | `200 : 정상조회` |
| 선택한 댓글 수정 | `PUT`    | `/comments/{commentId}` | Body    | 수정 id    | `200 : 정상수정` |
| 선택한 댓글 삭제 | `DELETE` | `/comments/{commentId}` | query   | -        | `200 : 정상삭제` |

---

## 5~6 단계

## 일정

| 기능        | Method   | URL                          | request | response    | 상태코드                       |
|-----------|----------|------------------------------|---------|-------------|----------------------------|
| 일정 작성     | `POST`   | `/events`                    | Body    | 등록 정보       | `200 : 정상작성`,`404 : 작성 불가` |
| 선택한 일정 조회 | `GET`    | `/events/{eventId}`          | query   | 단건 응답 정보    | `200 : 정상조회`,`404 : 조회불가`  |
| 선택한 일정 수정 | `PUT`    | `/events/{eventId}`          | Body    | 수정 정보       | `200 : 정상수정`               |
| 전체 일정 조회  | `GET`    | `/events/?page=?&size=?`     | Param   | 페이지네이션 된 일정 | `200 : 정상조회`,`404: 조회불가`   |
| 선택한 일정 삭제 | `DELETE` | `/events/{eventId}`          | query   | -           | `200 : 정상수정`               |
| 일정 유저 배치  | `PUT`    | `/events/{eventId}/{userId}` | query   | -           | `200 : 정상처리`               |

## 댓글

| 기능        | Method   | URL                     | request | response | 상태코드         |
|-----------|----------|-------------------------|---------|----------|--------------|
| 댓글 저장     | `POST`   | `/comments`             | Body    | 등록 정보    | `200 : 정상작성` |
| 댓글 단건 조회  | `GET`    | `/comments/{commentId}` | query   | 단건 응답 정보 | `200 : 정상조회` |
| 댓글 전체 조회  | `GET`    | `/comments`             | -       | 전체 응답 정보 | `200 : 정상조회` |
| 선택한 댓글 수정 | `PUT`    | `/comments/{commentId}` | Body    | 수정 id    | `200 : 정상수정` |
| 선택한 댓글 삭제 | `DELETE` | `/comments/{commentId}` | query   | -        | `200 : 정상삭제` |

## 유저

| 기능       | Method   | URL               | request | response | 상태코드         |
|----------|----------|-------------------|---------|----------|--------------|
| 유저 저장    | `POST`   | `/users`          | Body    | 단일 유저 정보 | `200 : 정상조회` |
| 유저 단건 조회 | `GET`    | `/users/{userId}` | query   | 단일 유저 정보 | `200 : 정상조회` |
| 유저 전체 조회 | `GET`    | `/users/all`      | -       | 전체 유저 정보 | `200 : 정상조회` |
| 유저 수정    | `PUT`    | `/users/{userId}` | Body    | 단일 유저 정보 | `200 : 정상조회` |
| 유저 삭제    | `DELETE` | `/users/{userId}` | Param   | -        | `200 : 정상조회` |


---
## 8단계 이후

## 일정

| 기능        | Method   | URL                          | request | response    | 상태코드                       |
|-----------|----------|------------------------------|---------|-------------|----------------------------|
| 일정 작성     | `POST`   | `/events`                    | Body    | 등록 정보       | `200 : 정상작성`,`404 : 작성 불가` |
| 선택한 일정 조회 | `GET`    | `/events/{eventId}`          | query   | 단건 응답 정보    | `200 : 정상조회`,`404 : 조회불가`  |
| 선택한 일정 수정 | `PUT`    | `/events/{eventId}`          | Body    | 수정 정보       | `200 : 정상수정`               |
| 전체 일정 조회  | `GET`    | `/events/?page=?&size=?`     | Param   | 페이지네이션 된 일정 | `200 : 정상조회`,`404: 조회불가`   |
| 선택한 일정 삭제 | `DELETE` | `/events/{eventId}`          | query   | -           | `200 : 정상수정`               |
| 일정 유저 배치  | `PUT`    | `/events/{eventId}/{userId}` | query   | -           | `200 : 정상처리`               |

## 댓글

| 기능        | Method   | URL                     | request | response | 상태코드         |
|-----------|----------|-------------------------|---------|----------|--------------|
| 댓글 저장     | `POST`   | `/comments`             | Body    | 등록 정보    | `200 : 정상작성` |
| 댓글 단건 조회  | `GET`    | `/comments/{commentId}` | query   | 단건 응답 정보 | `200 : 정상조회` |
| 댓글 전체 조회  | `GET`    | `/comments`             | -       | 전체 응답 정보 | `200 : 정상조회` |
| 선택한 댓글 수정 | `PUT`    | `/comments/{commentId}` | Body    | 수정 id    | `200 : 정상수정` |
| 선택한 댓글 삭제 | `DELETE` | `/comments/{commentId}` | query   | -        | `200 : 정상삭제` |

## 유저

| 기능       | Method   | URL             | request | response | 상태코드         |
|----------|----------|-----------------|---------|----------|--------------|
| 유저 저장    | `POST`   | `/users/create` | Body    | 단일 유저 정보 | `200 : 정상조회` |
| 유저 단건 조회 | `GET`    | `/users`        | query   | 단일 유저 정보 | `200 : 정상조회` |
| 유저 전체 조회 | `GET`    | `/users/all`    | -       | 전체 유저 정보 | `200 : 정상조회` |
| 유저 수정    | `PUT`    | `/users`        | Body    | 단일 유저 정보 | `200 : 정상조회` |
| 유저 삭제    | `DELETE` | `/users`        | Param   | -        | `200 : 정상조회` |

## 로그인

| 기능  | Method | URL      | request | response | 상태코드                                                           |
|-----|--------|----------|---------|----------|----------------------------------------------------------------|
| 로그인 | `POST` | `/login` | Body    | -        | `200 : 정상접속`,`400 : 토큰 미보유`,`401 : 이메일/비밀번호 불일치`,`401 : 토큰 만료` |

---

# ERD

![VER_1](https://github.com/user-attachments/assets/71afbdc3-5e63-41c5-9733-885b6da99930)
![VER_2](https://github.com/user-attachments/assets/bd9c6a9a-9d73-4d90-8a06-215a42e006b3)
<img width="448" alt="스크린샷 2024-08-23 오후 12 20 48" src="https://github.com/user-attachments/assets/79f89e63-1b59-4434-94d4-065309b115fa">




---

# SQL

---

## 1~4 단계

```
CREATE TABLE events
(
    event_id     BIGINT       PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    title        VARCHAR(255) NOT NULL,
    content      VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL

);

CREATE TABLE comments
(
    comment_id  BIGINT       PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    comment      VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events (event_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id)
    
);

```

## 5~6 단계

```

CREATE TABLE events
(
    event_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    content      VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL

);

CREATE TABLE users
(
    user_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL

);

#USER-EVENT 중간 테이블
CREATE TABLE comments
(
    comment_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    comment      VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL,
    event_id     BIGINT       NOT NULL,
    user_id      BIGINT       Not NULL,
    FOREIGN KEY (event_id) REFERENCES events (event_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE posts
(
    post_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events (event_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

# 7 단계 추가
ALTER TABLE users ADD password VARCHAR(255) NOT NULL ;

# 9단계 추가
ALTER TABLE users ADD role VARCHAR(255) NOT NULL ;

# 10단계 추가
ALTER TABLE events ADD weather VARCHAR(255);
```


---
# 오류 
0823 : 날씨 API 조회시 전체 데이터 넘어옴

~~0823 : Header에 JWT 토큰이 없음~~해결 : `res.setHeader(jwtUtil.AUTHORIZATION_HEADER, token);`를 통해서 Hear에 담음
