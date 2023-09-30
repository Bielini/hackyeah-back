package com.capgemini.hackyeah.point.controller;


import com.capgemini.hackyeah.point.dto.PointDTO;
import com.capgemini.hackyeah.point.repository.PointRepository;
import com.capgemini.hackyeah.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    @PostMapping(value = "/add")
    public void addPoint(@RequestBody PointDTO pointDto){

        pointService.addPoint(pointDto);

    }
}
