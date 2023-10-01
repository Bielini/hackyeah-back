package com.capgemini.hackyeah.point.service;


import com.capgemini.hackyeah.domain.model.Role;
import com.capgemini.hackyeah.domain.model.User;
import com.capgemini.hackyeah.domain.model.point.Point;
import com.capgemini.hackyeah.domain.repository.UserRepository;
import com.capgemini.hackyeah.point.dto.LeaderDto;
import com.capgemini.hackyeah.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    private final UserRepository userRepository;

    public void addPoint(Integer points, User user){
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

    public List<LeaderDto> getAllLeadersForBoard() {
        List<User> userPoints= userRepository.findAllByOrderByTotalPointsDesc().stream().filter(user -> user.getRole().equals(Role.USER)).toList();

        return userPoints.stream().map(user -> LeaderDto.builder().firstname(user.getFirstname()).lastname(user.getLastname()).points(user.getTotalPoints()).build()).toList();
    }
}
