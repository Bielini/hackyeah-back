package com.capgemini.hackyeah.point.controller;


import com.capgemini.hackyeah.domain.model.User;
import com.capgemini.hackyeah.point.dto.LeaderDto;
import com.capgemini.hackyeah.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leader")
public class LeaderBoardController {

    private final PointService pointService;

    @GetMapping(value = "/alltime")
    public List<User>allTimeLeaders(){
        return pointService.allDistinctUser();
    }
}
