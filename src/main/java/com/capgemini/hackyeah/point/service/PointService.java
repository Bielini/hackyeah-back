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

    private final UserRepository userRepository;

    public void addPoint(Integer points, User user){

        Long userId=user.getId;

        Point point= new Point();
        point.setPoint(points);
        point.setDateEarned(Date.from(Instant.now()));
        point.setUserId(user.getId());
        pointRepository.save(point);

//        get existing point for the user from the database and update it with the new point.

        User user=userRepository.getReferenceById(userId);

        Long updatePoint=user.getTotalPoints()+pointDTO.getPoint();

        user.setTotalPoints(updatePoint);

        userRepository.save(user);


    }
}
