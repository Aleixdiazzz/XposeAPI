package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArtistRepository extends JpaRepository<Artist, Long> {

}