package com.capgemini.hackyeah.domain.repository;

import com.capgemini.hackyeah.domain.model.Collector;
import com.capgemini.hackyeah.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Integer> {
    List<Collector> getAllByUser(User user);
}
