package com.kcm.jpacalender.repository;

import com.kcm.jpacalender.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
