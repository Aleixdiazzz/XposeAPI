package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface SerieRepository extends JpaRepository<Serie, Long>, JpaSpecificationExecutor<Serie> {

    List<Serie> findByActiveTrue();
}
