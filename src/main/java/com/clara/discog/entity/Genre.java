package com.clara.discog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "release_id", nullable = false)
    private Release release;

}
