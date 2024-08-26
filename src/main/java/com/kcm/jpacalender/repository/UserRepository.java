package com.kcm.jpacalender.repository;

import com.kcm.jpacalender.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}