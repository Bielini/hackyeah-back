package com.capgemini.hackyeah.domain.model.point;


import com.capgemini.hackyeah.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "points")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer point;

    private Date dateEarned;

    @Column(name = "user_id")
    private Long userId;

}
