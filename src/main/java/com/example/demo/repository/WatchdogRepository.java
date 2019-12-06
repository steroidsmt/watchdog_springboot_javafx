package com.example.demo.repository;

import com.example.demo.model.WatchDog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchdogRepository extends JpaRepository<WatchDog, Long> {
}
