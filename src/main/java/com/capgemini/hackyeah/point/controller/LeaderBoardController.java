package com.capgemini.hackyeah.point.controller;


import com.capgemini.hackyeah.point.dto.LeaderDto;
import com.capgemini.hackyeah.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leaderboard")
public class LeaderBoardController {

    private final PointService pointService;

    @GetMapping
    public List<LeaderDto> getLeaderBoard(){
        return pointService.getAllLeadersForBoard();
    }
}
