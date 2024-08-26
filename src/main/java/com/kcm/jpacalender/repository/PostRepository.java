package com.kcm.jpacalender.repository;

import com.kcm.jpacalender.entity.Event;
import com.kcm.jpacalender.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
