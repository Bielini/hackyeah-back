package com.capgemini.hackyeah.point.service;


import com.capgemini.hackyeah.domain.model.User;
import com.capgemini.hackyeah.domain.model.point.Point;
import com.capgemini.hackyeah.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public void addPoint(Integer points, User user){

        Point point= new Point();
        point.setPoint(points);
        point.setDateEarned(Date.from(Instant.now()));
        point.setUserId(user.getId());

        pointRepository.save(point);

    }
}
