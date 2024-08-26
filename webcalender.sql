#1단계
CREATE TABLE events
(
    event_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    title        VARCHAR(255) NOT NULL,
    content      VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL

);

#2단계
CREATE TABLE comments
(
    comment_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment      VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL,
    username     VARCHAR(255) NOT NULL,
    event_id     BIGINT       NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events (event_id)
);

#5단계.
CREATE TABLE users
(
    user_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    created_date DATETIME     NOT NULL,
    updated_date DATETIME     NOT NULL

);


#5단계
#USER-EVENT 중간 테이블
CREATE TABLE posts
(
    post_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events (event_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

#5단계
ALTER TABLE events DROP username;
ALTER TABLE events ADD user_id VARCHAR(255) NOT NULL ;

# 7단계
ALTER TABLE users ADD password VARCHAR(255) NOT NULL ;

# 9단계
ALTER TABLE users ADD role VARCHAR(255) NOT NULL ;

ALTER TABLE events ADD weather VARCHAR(255);