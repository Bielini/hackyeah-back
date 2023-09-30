package com.capgemini.hackyeah.point.service;


import com.capgemini.hackyeah.domain.model.User;
import com.capgemini.hackyeah.domain.model.point.Point;
import com.capgemini.hackyeah.domain.repository.UserRepository;
import com.capgemini.hackyeah.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    private final UserRepository userRepository;

    public void addPoint(Integer points, User user){

        Long userId=user.getId();

        Point point= new Point();
        point.setPoint(points);
        point.setDateEarned(Date.from(Instant.now()));
        point.setUserId(user.getId());
        pointRepository.save(point);

        Long updatePoint = 0L;

        if(nonNull(user.getTotalPoints())){
            updatePoint+=user.getTotalPoints();
        }

        updatePoint+=points;

        user.setTotalPoints(updatePoint);

        userRepository.save(user);


    }

//    get all users with total points
    public List<User>allDistinctUser(){


        return userRepository.findAllByOrderByTotalPointsAsc();
    }
}
