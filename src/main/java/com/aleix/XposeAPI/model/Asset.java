package com.aleix.XposeAPI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "asset")
@Getter
@Setter
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "asset_authors",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> authors;

    @ManyToMany
    @JoinTable(
            name = "asset_series",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "serie_id")
    )
    private List<Serie> series;
}
