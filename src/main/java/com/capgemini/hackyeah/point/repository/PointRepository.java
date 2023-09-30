package com.capgemini.hackyeah.point.repository;


import com.capgemini.hackyeah.domain.model.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point,Long> {
}
