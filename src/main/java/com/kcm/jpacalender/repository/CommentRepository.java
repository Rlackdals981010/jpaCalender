package com.kcm.jpacalender.repository;

import com.kcm.jpacalender.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
